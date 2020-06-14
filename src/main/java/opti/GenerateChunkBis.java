package opti;

import main.BiomeGeneration;
import main.BiomesBase;
import main.Random;

public class GenerateChunkBis {
    private final Random worldRandom;
    private BiomesBase[] biomesForGeneration;
    BiomeGeneration biomeGenerationInstance;
    double[] mainLimitPerlinNoise;
    double[] minLimitPerlinNoise;
    double[] maxLimitPerlinNoise;
    double[] surfaceNoise;
    double[] depthNoise;

    enum Blocks {
        AIR(0), STONE(1), GRASS(2), DIRT(3), BEDROCK(7), MOVING_WATER(9), SAND(12), GRAVEL(13), ICE(79);

        private final int value;

        Blocks(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private double[] sandFields;
    private double[] gravelField;
    private double[] heightField;
    private double[] NoiseColumn;
    // this is mega optimized
    private final NoiseGeneratorOctavesBisBis minLimit;
    private final NoiseGeneratorOctavesBisBis maxLimit;
    private final NoiseGeneratorOctavesBisBis mainLimit;

    private final NoiseGeneratorOctavesBis shoresBottomComposition;
    private final NoiseGeneratorOctavesBis surfaceElevation;
    public NoiseGeneratorOctavesBis scale;
    public NoiseGeneratorOctavesBis depth;
    public NoiseGeneratorOctavesBis forest;

    public GenerateChunkBis(long worldSeed) {
        // this.biomeGenerationInstance = new main.BiomeGeneration(worldSeed);
        worldRandom = new Random(worldSeed);
        minLimit = new NoiseGeneratorOctavesBisBis(worldRandom, 16);
        maxLimit = new NoiseGeneratorOctavesBisBis(worldRandom, 16);
        mainLimit = new NoiseGeneratorOctavesBisBis(worldRandom, 8);
        shoresBottomComposition = new NoiseGeneratorOctavesBis(worldRandom, 4);
        surfaceElevation = new NoiseGeneratorOctavesBis(worldRandom, 4);
        scale = new NoiseGeneratorOctavesBis(worldRandom, 10);
        depth = new NoiseGeneratorOctavesBis(worldRandom, 16);
        forest = new NoiseGeneratorOctavesBis(worldRandom, 8);
    }

    public byte[] provideChunk(int chunkX, int chunkZ, BiomeGeneration biomeGenerationInstance, BiomesBase[] biomesForGeneration) {
        worldRandom.setSeed((long) chunkX * 0x4f9939f508L + (long) chunkZ * 0x1ef1565bd5L);
        byte[] chunkCache = new byte[32768];
        // this.biomesForGeneration=this.biomeGenerationInstance.loadBiomes(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        this.biomeGenerationInstance = biomeGenerationInstance;
        double[] temperatures = this.biomeGenerationInstance.temperature;
        generateTerrain(chunkX, chunkZ, chunkCache, temperatures);
        replaceBlockForBiomes(chunkX, chunkZ, chunkCache, biomesForGeneration);
        return chunkCache;
    }


    private double[] fillNoiseColumn(double[] NoiseColumn, int x, int z) {
        // we only need
        // (60, 77, 145, 162, 61, 78, 146, 163)
        // (145, 162, 230, 247, 146, 163, 231, 248)
        // (230, 247, 315, 332, 231, 248, 316, 333)
        // (315, 332, 400, 417, 316, 333, 401, 418)
        // which is only 60-61, 77-78, 145-146, 162-163, 230-231, 247-248, 315-316, 332-333, 400-401, 417-418 // so 20
        // or as cellCounter 3,4,8,9,13,14,18,19,23,24
        // or as x indices 1 2 3 4 5 and fixed z to 3-4
        // 5 is the cellsize here and 17 the column size, they are inlined constants
        if (NoiseColumn == null) {
            NoiseColumn = new double[5 * 17 * 5];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        double[] temperature = biomeGenerationInstance.temperature;
        double[] humidity = biomeGenerationInstance.humidity;
        // this is super fast (but we only care about 3,4,8,9,13,14,18,19,23,24)
        surfaceNoise = scale.generateFixedNoise(surfaceNoise, x, z, 5, 5, 1.121D, 1.121D);
        depthNoise = depth.generateFixedNoise(depthNoise, x, z, 5, 5, 200D, 200D);

        // this is slow af but we care only about 60-61, 77-78, 145-146, 162-163, 230-231, 247-248, 315-316, 332-333, 400-401, 417-418
        mainLimitPerlinNoise = mainLimit.generateNoise(mainLimitPerlinNoise, x, 0, z, 5, 17, 5, d / 80D, d1 / 160D, d / 80D);
        minLimitPerlinNoise = minLimit.generateNoise(minLimitPerlinNoise, x, 0, z, 5, 17, 5, d, d1, d);
        maxLimitPerlinNoise = maxLimit.generateNoise(maxLimitPerlinNoise, x, 0, z, 5, 17, 5, d, d1, d);
        int[] possibleCellCounter = {3, 4, 8, 9, 13, 14, 18, 19, 23, 24};
        for (int cellCounter : possibleCellCounter) {
            int X = (cellCounter / 5) * 3 + 1; // 1 4 7 10 13
            int Z = (cellCounter % 5) * 3 + 1; // 7 13
            double aridityXZ = 1.0D - humidity[X * 16 + Z] * temperature[X * 16 + Z];
            aridityXZ *= aridityXZ;
            aridityXZ *= aridityXZ;
            aridityXZ = 1.0D - aridityXZ;  // 1-(1-X)*(1-X)*(1-X)*(1-X) with X=humidity*Temp
            double surface = (surfaceNoise[cellCounter] / 512D + 256D / 512D) * aridityXZ;
            if (surface > 1.0D) {
                surface = 1.0D; // clamp
            }
            double depth = depthNoise[cellCounter] / 8000D;
            if (depth < 0.0D) {
                depth = -depth * 0.29999999999999999D;
            }
            depth = depth * 3D - 2D;
            if (depth < 0.0D) {
                depth /= 2D;
                if (depth < -1D) {
                    depth = -1D;
                }
                depth /= 1.3999999999999999D;
                depth /= 2D;
                surface = 0.0D;
            } else {
                if (depth > 1.0D) {
                    depth = 1.0D;
                }
                depth /= 8D;
            }
            if (surface < 0.0D) {
                surface = 0.0D;
            }
            surface += 0.5D;
            depth = (depth * (double) 17) / 16D;
            double depthColumn = (double) 17 / 2D + depth * 4D;

            for (int column = 9; column < 11; column++) { // we only care at pos 9 and 10 in the column so 2 times
                int columnCounter = cellCounter * 17 + column;
                double limit;
                double columnPerSurface = (((double) column - depthColumn) * 12D) / surface;
                if (columnPerSurface < 0.0D) {
                    columnPerSurface *= 4D;
                }
                double minLimit = minLimitPerlinNoise[columnCounter] / 512D;
                double maxLimit = maxLimitPerlinNoise[columnCounter] / 512D;
                double mainLimit = (mainLimitPerlinNoise[columnCounter] / 10D + 1.0D) / 2D;
                if (mainLimit < 0.0D) {
                    limit = minLimit;
                } else if (mainLimit > 1.0D) {
                    limit = maxLimit;
                } else {
                    limit = minLimit + (maxLimit - minLimit) * mainLimit; // interpolation
                }
                limit -= columnPerSurface;
               // System.out.println(columnCounter+" "+limit+" "+minLimit+" "+mainLimit+" "+maxLimit);
                NoiseColumn[columnCounter] = limit;
            }

        }
        return NoiseColumn;
    }

    public void generateTerrain(int chunkX, int chunkZ, byte[] chunkCache, double[] temperatures) {
        byte quadrant = 4;
        byte columnSize = 17;
        int cellsize = 5;
        double interpFirstOctave = 0.125D;
        double interpSecondOctave = 0.25D;
        double interpThirdOctave = 0.25D;
        // we only need 315 332 400 417 316 333 401 and 418
        NoiseColumn = fillNoiseColumn(NoiseColumn, chunkX * quadrant, chunkZ * quadrant);
        for (int x = 0; x < quadrant; x++) {
            int z = 3;
            for (int height = 9; height < 10; height++) {
                int off_0_0 = x * cellsize + z;
                int off_0_1 = x * cellsize + (z + 1);
                int off_1_0 = (x + 1) * cellsize + z;
                int off_1_1 = (x + 1) * cellsize + (z + 1);
                double firstNoise_0_0 = NoiseColumn[(off_0_0) * columnSize + (height)];
                double firstNoise_0_1 = NoiseColumn[(off_0_1) * columnSize + (height)];
                double firstNoise_1_0 = NoiseColumn[off_1_0 * columnSize + (height)];
                double firstNoise_1_1 = NoiseColumn[off_1_1 * columnSize + (height)];
                double stepFirstNoise_0_0 = (NoiseColumn[(off_0_0) * columnSize + (height + 1)] - firstNoise_0_0) * interpFirstOctave;
                double stepFirstNoise_0_1 = (NoiseColumn[(off_0_1) * columnSize + (height + 1)] - firstNoise_0_1) * interpFirstOctave;
                double stepFirstNoise_1_0 = (NoiseColumn[off_1_0 * columnSize + (height + 1)] - firstNoise_1_0) * interpFirstOctave;
                double stepFirstNoise_1_1 = (NoiseColumn[off_1_1 * columnSize + (height + 1)] - firstNoise_1_1) * interpFirstOctave;

                //double firstNoise_0_0 = NoiseColumn[(x * cellsize + 3) * 17 + 9]; // should only take care of (x*5+3)*17+9
               //double firstNoise_0_1 = NoiseColumn[(x * cellsize + 4) * 17 + 9]; // should only take care of (x*5+4)*17+9
               //double firstNoise_1_0 = NoiseColumn[((x + 1) * cellsize + 3) * 17 + 9]; // should only take care of ((x+1)*5+3)*17+9
               //double firstNoise_1_1 = NoiseColumn[((x + 1) * cellsize + 4) * 17 + 9]; // should only take care of ((x+1)*5+)*17+9
               //double stepFirstNoise_0_0 = (NoiseColumn[(x * cellsize + 3) * 17 + 10] - firstNoise_0_0) * interpFirstOctave;
               //double stepFirstNoise_0_1 = (NoiseColumn[(x * cellsize + 4) * 17 + 10] - firstNoise_0_1) * interpFirstOctave;
               //double stepFirstNoise_1_0 = (NoiseColumn[((x + 1) * cellsize + 3) * 17 + 10] - firstNoise_1_0) * interpFirstOctave;
               //double stepFirstNoise_1_1 = (NoiseColumn[((x + 1) * cellsize + 4) * 17 + 10] - firstNoise_1_1) * interpFirstOctave;
                for (int heightOffset = 0; heightOffset < 8; heightOffset++) {
                    double secondNoise_0_0 = firstNoise_0_0;
                    double secondNoise_0_1 = firstNoise_0_1;
                    double stepSecondNoise_1_0 = (firstNoise_1_0 - firstNoise_0_0) * interpSecondOctave;
                    double stepSecondNoise_1_1 = (firstNoise_1_1 - firstNoise_0_1) * interpSecondOctave;
                    for (int xOffset = 0; xOffset < 4; xOffset++) {
                        int currentHeight = height * 8 + heightOffset;
                        int index = xOffset + x * 4 << 11 | z * 4 << 7 | currentHeight;
                        double stoneLimit = secondNoise_0_0; // aka thirdNoise
                        double stepThirdNoise_0_1 = (secondNoise_0_1 - secondNoise_0_0) * interpThirdOctave;
                        for (int zOffset = 0; zOffset < 4; zOffset++) {
                            Blocks blockType = Blocks.AIR;
                            if (stoneLimit > 0.0D) { //3d perlin condition
                                blockType = Blocks.STONE;
                            }
                            chunkCache[index] = (byte) blockType.getValue();
                            index += 128;
                            stoneLimit += stepThirdNoise_0_1;
                        }

                        secondNoise_0_0 += stepSecondNoise_1_0;
                        secondNoise_0_1 += stepSecondNoise_1_1;
                    }

                    firstNoise_0_0 += stepFirstNoise_0_0;
                    firstNoise_0_1 += stepFirstNoise_0_1;
                    firstNoise_1_0 += stepFirstNoise_1_0;
                    firstNoise_1_1 += stepFirstNoise_1_1;
                }
            }

        }
    }


    public void replaceBlockForBiomes(int i, int j, byte[] chunkCache, BiomesBase[] biomes) {
        byte oceanLevel = 64;
        int MIN = oceanLevel;
        double noiseFactor = 0.03125D;
        sandFields = shoresBottomComposition.generateNoise(sandFields, i * 16, j * 16, 0.0D, 16, 16, 1, noiseFactor, noiseFactor, 1.0D);
        gravelField = shoresBottomComposition.generateNoise(gravelField, j * 16, 109.0134D, i * 16, 16, 1, 16, noiseFactor, 1.0D, noiseFactor);
        heightField = surfaceElevation.generateNoise(heightField, i * 16, j * 16, 0.0D, 16, 16, 1, noiseFactor * 2D, noiseFactor * 2D, noiseFactor * 2D);

        for (int x = 0; x < 16; x++) {
            for (int k = 0; k < 12; k++) {
                worldRandom.nextDouble();
                worldRandom.nextDouble();
                worldRandom.nextDouble();
                for (int w = 0; w < 128; w++) {
                    worldRandom.nextInt(5);
                }
            }
            for (int z = 12; z < 16; z++) {
                BiomesBase biome = biomes[x * 16 + z];
                boolean sandy = sandFields[x + z * 16] + worldRandom.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean gravelly = gravelField[x + z * 16] + worldRandom.nextDouble() * 0.20000000000000001D > 3D;
                int elevation = (int) (heightField[x + z * 16] / 3D + 3D + worldRandom.nextDouble() * 0.25D);
                int state = -1;
                byte aboveOceanAkaLand = biome.grassOrEquivalentSand;
                byte belowOceanAkaEarthCrust = biome.dirtOrEquivalentSand;
                for (int y = 127; y >= MIN; y--) {
                    int chunkCachePos = (x * 16 + z) * 128 + y;
                    byte previousBlock = chunkCache[chunkCachePos];

                    if (previousBlock == 0) {
                        state = -1;
                        continue;
                    }
                    if (previousBlock != (byte) Blocks.STONE.getValue()) {
                        continue;
                    }
                    if (state == -1) { // AIR
                        if (elevation <= 0) { // if in a deep
                            aboveOceanAkaLand = 0;
                            belowOceanAkaEarthCrust = (byte) Blocks.STONE.getValue();
                        } else if (y <= oceanLevel + 1) { // if at sea level do the shore and rivers
                            aboveOceanAkaLand = biome.grassOrEquivalentSand;
                            belowOceanAkaEarthCrust = biome.dirtOrEquivalentSand;
                            if (gravelly) {
                                aboveOceanAkaLand = 0;
                            }
                            if (gravelly) {
                                belowOceanAkaEarthCrust = (byte) Blocks.GRAVEL.getValue();
                            }
                            if (sandy) {
                                aboveOceanAkaLand = (byte) Blocks.SAND.getValue();
                            }
                            if (sandy) {
                                belowOceanAkaEarthCrust = (byte) Blocks.SAND.getValue();
                            }
                        }
                        state = elevation;
                        // above ocean level
                        chunkCache[chunkCachePos] = aboveOceanAkaLand;
                        continue;
                    }
                    if (state > 0) {
                        state--;
                        chunkCache[chunkCachePos] = belowOceanAkaEarthCrust;

                    }
                }
                for (int k = 0; k < 128; k++) {
                    worldRandom.nextInt(5);
                }

            }
        }
    }


}
