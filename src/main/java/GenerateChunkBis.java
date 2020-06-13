import java.util.List;

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
        AIR(0),
        STONE(1),
        GRASS(2),
        DIRT(3),
        BEDROCK(7),
        MOVING_WATER(9),
        SAND(12),
        GRAVEL(13),
        ICE(79);

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
    private final NoiseGeneratorOctavesBis minLimit;
    private final NoiseGeneratorOctavesBis maxLimit;
    private final NoiseGeneratorOctavesBis mainLimit;
    private final NoiseGeneratorOctavesBis shoresBottomComposition;
    private final NoiseGeneratorOctavesBis surfaceElevation;
    public NoiseGeneratorOctavesBis scale;
    public NoiseGeneratorOctavesBis depth;
    public NoiseGeneratorOctavesBis forest;

    public GenerateChunkBis(long worldSeed) {
        // this.biomeGenerationInstance = new BiomeGeneration(worldSeed);
        worldRandom = new Random(worldSeed);
        minLimit = new NoiseGeneratorOctavesBis(worldRandom, 16);
        maxLimit = new NoiseGeneratorOctavesBis(worldRandom, 16);
        mainLimit = new NoiseGeneratorOctavesBis(worldRandom, 8);
        shoresBottomComposition = new NoiseGeneratorOctavesBis(worldRandom, 4);
        surfaceElevation = new NoiseGeneratorOctavesBis(worldRandom, 4);
        scale = new NoiseGeneratorOctavesBis(worldRandom, 10);
        depth = new NoiseGeneratorOctavesBis(worldRandom, 16);
        forest = new NoiseGeneratorOctavesBis(worldRandom, 8);
    }

    public byte[] provideChunk(int chunkX, int chunkZ, BiomeGeneration biomeGenerationInstance,BiomesBase[] biomesForGeneration) {
        worldRandom.setSeed((long) chunkX * 0x4f9939f508L + (long) chunkZ * 0x1ef1565bd5L);
        byte[] chunkCache = new byte[32768];
        // this.biomesForGeneration=this.biomeGenerationInstance.loadBiomes(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        this.biomeGenerationInstance=biomeGenerationInstance;
        double[] temperatures = this.biomeGenerationInstance.temperature;
        generateTerrain(chunkX, chunkZ, chunkCache, temperatures);
        replaceBlockForBiomes(chunkX, chunkZ, chunkCache, biomesForGeneration);
        return chunkCache;
    }


    private double[] fillNoiseColumn(double[] NoiseColumn, int x, int z) {
        // we only need 315 332 400 417 316 333 401 and 418
        // 5 is the cellsize here and 17 the column size, they are inlined constants
        if (NoiseColumn == null) {
            NoiseColumn = new double[5 * 17 * 5];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        double[] temperature = biomeGenerationInstance.temperature;
        double[] humidity = biomeGenerationInstance.humidity;
        // this is super fast
        surfaceNoise = scale.generateFixedNoise(surfaceNoise, x, z, 5, 5, 1.121D, 1.121D);
        depthNoise = depth.generateFixedNoise(depthNoise, x, z, 5, 5, 200D, 200D);

        // this is slow af
        mainLimitPerlinNoise = mainLimit.generateNoise(mainLimitPerlinNoise, x, 0, z, 5, 17, 5, d / 80D, d1 / 160D, d / 80D);
        minLimitPerlinNoise = minLimit.generateNoise(minLimitPerlinNoise, x, 0, z, 5, 17, 5, d, d1, d);
        maxLimitPerlinNoise = maxLimit.generateNoise(maxLimitPerlinNoise, x, 0, z, 5, 17, 5, d, d1, d);

        // this was optimized
        int columnCounter = 306; // 3*5*17+3*17
        int cellCounter = 18; // 3*5+3
        int[] possibleCellCounter={18,19,23,24};
       
        for (int xCell = 3; xCell < 5; xCell++) { // columnCounter 315 332 400 417 or cellCounter 18 19 23 24 or xCell 3 and 4
            int X = xCell * 3 + 1; // 7 13
            for (int zCell = 3; zCell < 5; zCell++) { //cellCounter 18 19 23 24 or zCell 3 4
                int Z = zCell * 3 + 1; // 7 13
                //17 ,20 ,23 ,26 ,29 ,65 ,68 ,71 ,74 ,77 ,113 ,116 ,119 ,122 ,125 ,161 ,164 ,167 ,170 ,173 ,209 ,212 ,215 ,218 ,221
                double aridityXZ = 1.0D - humidity[X * 16 + Z] * temperature[X * 16 + Z];
                aridityXZ *= aridityXZ;
                aridityXZ *= aridityXZ;
                aridityXZ = 1.0D - aridityXZ;  // 1-(1-X)*(1-X)*(1-X)*(1-X) with X=humidityXZ
                double surface = (this.surfaceNoise[cellCounter] / 512D + 256D / 512D) * aridityXZ;
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
                cellCounter++;
                columnCounter+=9;
                for (int column = 9; column < 11; column++) { // we only care at pos 9 and 10 in the column

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
                   // System.out.println(cellCounter+" "+columnCounter);
                    NoiseColumn[columnCounter] = limit;
                    columnCounter++;
                }
                columnCounter+=6;

            }
            columnCounter+=3*17;
            cellCounter+=3;

        }

        return NoiseColumn;
    }

    public void generateTerrain(int chunkX, int chunkZ, byte[] chunkCache, double[] temperatures) {
        byte amplification = 4;
        byte columnSize = 17;
        int cellsize = 5;
        double interpFirstOctave = 0.125D;
        double interpSecondOctave = 0.25D;
        double interpThirdOctave = 0.25D;
        // we only need 315 332 400 417 316 333 401 and 418
        NoiseColumn = fillNoiseColumn(NoiseColumn, chunkX * amplification, chunkZ * amplification);
        for (int x = 3; x < amplification; x++) {
            for (int z = 3; z < amplification; z++) {
                for (int height = 9; height < 10; height++) {
                    int off_0_0 = 18; // should only take care of 3*5+3=18
                    int off_0_1 = 19; // should only take care of 3*5+3+1=19
                    int off_1_0 = 23; // should only take care of 4*5+3=23
                    int off_1_1 = 24; // should only take care of 4*5+3+1=24
                    double firstNoise_0_0 = NoiseColumn[315]; // should only take care of 18*17+9=315
                    double firstNoise_0_1 = NoiseColumn[332]; // should only take care of 19*17+9=332
                    double firstNoise_1_0 = NoiseColumn[400]; // should only take care of 23*17+9=400
                    double firstNoise_1_1 = NoiseColumn[417]; // should only take care of 24*17+9=417
                    double stepFirstNoise_0_0 = (NoiseColumn[316] - firstNoise_0_0) * interpFirstOctave;
                    double stepFirstNoise_0_1 = (NoiseColumn[333] - firstNoise_0_1) * interpFirstOctave;
                    double stepFirstNoise_1_0 = (NoiseColumn[401] - firstNoise_1_0) * interpFirstOctave;
                    double stepFirstNoise_1_1 = (NoiseColumn[418] - firstNoise_1_1) * interpFirstOctave;
                    for (int heightOffset = 0; heightOffset < 8; heightOffset++) {
                        double secondNoise_0_0 = firstNoise_0_0;
                        double secondNoise_0_1 = firstNoise_0_1;
                        double stepSecondNoise_1_0 = (firstNoise_1_0 - firstNoise_0_0) * interpSecondOctave;
                        double stepSecondNoise_1_1 = (firstNoise_1_1 - firstNoise_0_1) * interpSecondOctave;
                        for (int xOffset = 0; xOffset < 4; xOffset++) {
                            int currentHeight=height * 8 + heightOffset;
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
    }


    public void replaceBlockForBiomes(int i, int j, byte[] chunkCache, BiomesBase[] biomes) {
        byte oceanLevel = 64;
        int MIN=oceanLevel;
        double noiseFactor = 0.03125D;
        sandFields = shoresBottomComposition.generateNoise(sandFields, i * 16, j * 16, 0.0D, 16, 16, 1, noiseFactor, noiseFactor, 1.0D);
        gravelField = shoresBottomComposition.generateNoise(gravelField, j * 16, 109.0134D, i * 16, 16, 1, 16, noiseFactor, 1.0D, noiseFactor);
        heightField = surfaceElevation.generateNoise(heightField, i * 16, j * 16, 0.0D, 16, 16, 1, noiseFactor * 2D, noiseFactor * 2D, noiseFactor * 2D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                BiomesBase biome = biomes[x * 16 + z];
                boolean sandy = sandFields[x + z * 16] + worldRandom.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean gravelly = gravelField[x + z * 16] + worldRandom.nextDouble() * 0.20000000000000001D > 3D;
                int elevation = (int) (heightField[x + z * 16] / 3D + 3D + worldRandom.nextDouble() * 0.25D);
                int state = -1;
                byte aboveOceanAkaLand = biome.grassOrEquivalentSand;
                byte belowOceanAkaEarthCrust = biome.dirtOrEquivalentSand;
                for (int y = 127; y >=MIN; y--) {
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
