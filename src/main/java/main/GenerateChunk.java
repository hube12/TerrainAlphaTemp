package main;


public class GenerateChunk {
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
    private final NoiseGeneratorOctaves minLimit;
    private final NoiseGeneratorOctaves maxLimit;
    private final NoiseGeneratorOctaves mainLimit;
    private final NoiseGeneratorOctaves shoresBottomComposition;
    private final NoiseGeneratorOctaves surfaceElevation;
    public NoiseGeneratorOctaves scale;
    public NoiseGeneratorOctaves depth;
    public NoiseGeneratorOctaves forest;

    public GenerateChunk(long worldSeed) {
       // this.biomeGenerationInstance = new main.BiomeGeneration(worldSeed);
        worldRandom = new Random(worldSeed);
        minLimit = new NoiseGeneratorOctaves(worldRandom, 16);
        maxLimit = new NoiseGeneratorOctaves(worldRandom, 16);
        mainLimit = new NoiseGeneratorOctaves(worldRandom, 8);
        shoresBottomComposition = new NoiseGeneratorOctaves(worldRandom, 4);
        surfaceElevation = new NoiseGeneratorOctaves(worldRandom, 4);
        scale = new NoiseGeneratorOctaves(worldRandom, 10);
        depth = new NoiseGeneratorOctaves(worldRandom, 16);
        forest = new NoiseGeneratorOctaves(worldRandom, 8);
    }

    public byte[] provideChunk(int chunkX, int chunkZ, boolean fast, BiomeGeneration biomeGenerationInstance,BiomesBase[] biomesForGeneration) {
        worldRandom.setSeed((long) chunkX * 0x4f9939f508L + (long) chunkZ * 0x1ef1565bd5L);
        byte[] chunkCache = new byte[32768];
       // this.biomesForGeneration=this.biomeGenerationInstance.loadBiomes(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        this.biomeGenerationInstance=biomeGenerationInstance;
        double[] temperatures = this.biomeGenerationInstance.temperature;
        byte[] heights = generateTerrain(chunkX, chunkZ, chunkCache, temperatures);
        replaceBlockForBiomes(chunkX, chunkZ, chunkCache, biomesForGeneration);
        return fast ? heights : chunkCache;
    }


    private double[] fillNoiseColumn(double[] NoiseColumn, int x, int z) {
        // 5 is the cellsize here and 17 the column size, they are inlined constants
        if (NoiseColumn == null) {
            NoiseColumn = new double[5 * 17 * 5];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        double[] temperature = biomeGenerationInstance.temperature;
        double[] humidity = biomeGenerationInstance.humidity;
        surfaceNoise = scale.generateFixedNoise(surfaceNoise, x, z, 5, 5, 1.121D, 1.121D);
        depthNoise = depth.generateFixedNoise(depthNoise, x, z, 5, 5, 200D, 200D);
        mainLimitPerlinNoise = mainLimit.generateNoise(mainLimitPerlinNoise, x, 0, z, 5, 17, 5, d / 80D, d1 / 160D, d / 80D);
        minLimitPerlinNoise = minLimit.generateNoise(minLimitPerlinNoise, x, 0, z, 5, 17, 5, d, d1, d);
        maxLimitPerlinNoise = maxLimit.generateNoise(maxLimitPerlinNoise, x, 0, z, 5, 17, 5, d, d1, d);
        int columnCounter = 0;
        int cellCounter = 0;
        for (int xCell = 0; xCell < 5; xCell++) {
            int X = xCell * 3 + 1; // 1 4 7 10 13
            for (int zCell = 0; zCell < 5; zCell++) {
                int Z = zCell * 3 + 1; // 1 4 7 10 13
                double tempXZ = temperature[X * 16 + Z]; //17 ,20 ,23 ,26 ,29 ,65 ,68 ,71 ,74 ,77 ,113 ,116 ,119 ,122 ,125 ,161 ,164 ,167 ,170 ,173 ,209 ,212 ,215 ,218 ,221
                double humidityXZ = humidity[X * 16 + Z] * tempXZ; //17 ,20 ,23 ,26 ,29 ,65 ,68 ,71 ,74 ,77 ,113 ,116 ,119 ,122 ,125 ,161 ,164 ,167 ,170 ,173 ,209 ,212 ,215 ,218 ,221
                double aridityXZ = 1.0D - humidityXZ;
                aridityXZ *= aridityXZ;
                aridityXZ *= aridityXZ;
                aridityXZ = 1.0D - aridityXZ;  // 1-(1-X)*(1-X)*(1-X)*(1-X) with X=humidityXZ
                double surface = (this.surfaceNoise[cellCounter] + 256D) / 512D;
                surface *= aridityXZ;
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
                for (int column = 0; column < 17; column++) {
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
                    if (column > 13) {
                        double normalizedColumn = (float) (column - 13) / 3F;
                        limit = limit * (1.0D - normalizedColumn) + -10D * normalizedColumn; // reduced limit
                    }
                    //System.out.println(columnCounter+" "+limit+" "+minLimit+" "+mainLimit+" "+maxLimit);
                    NoiseColumn[columnCounter] = limit;
                    columnCounter++;
                }

            }

        }

        return NoiseColumn;
    }

    public byte[] generateTerrain(int chunkX, int chunkZ, byte[] chunkCache, double[] temperatures) {
        byte amplification = 4;
        byte seaLevel = 64;
        byte columnSize = 17;
        int cellsize = 5;
        byte[] heights = new byte[256];
        double interpFirstOctave = 0.125D;
        double interpSecondOctave = 0.25D;
        double interpThirdOctave = 0.25D;
        NoiseColumn = fillNoiseColumn(NoiseColumn, chunkX * amplification, chunkZ * amplification);
        for (int x = 0; x < amplification; x++) {
            for (int z = 0; z < amplification; z++) {
                for (int height = 0; height < 16; height++) {
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
                                double temperature = temperatures[(x * 4 + xOffset) * 16 + (z * 4 + zOffset)];
                                Blocks blockType = Blocks.AIR;
                                if (currentHeight< seaLevel) {
                                    if (temperature < 0.5D && currentHeight >= seaLevel - 1) { // ice on water
                                        blockType = Blocks.ICE;
                                    } else {
                                        blockType = Blocks.MOVING_WATER;
                                    }
                                }
                                if (stoneLimit > 0.0D) { //3d perlin condition
                                    blockType = Blocks.STONE;
                                    heights[index >> 7] = (byte)currentHeight; // set at x and z the height (0 to 127)  height * 8 + heightOffset
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
        return heights;
    }


    public void replaceBlockForBiomes(int i, int j, byte[] chunkCache, BiomesBase[] biomes) {
        byte oceanLevel = 64;
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
                for (int y = 127; y >= 0; y--) {

                    int chunkCachePos = (x * 16 + z) * 128 + y;
                    if (y <= worldRandom.nextInt(5)) {
                        chunkCache[chunkCachePos] = (byte) Blocks.BEDROCK.getValue();
                        continue;
                    }
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
                        } else if (y >= oceanLevel - 4 && y <= oceanLevel + 1) { // if at sea level do the shore and rivers
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
                        if (y < oceanLevel && aboveOceanAkaLand == 0) { // set deep water
                            aboveOceanAkaLand = (byte) Blocks.MOVING_WATER.getValue();
                        }
                        state = elevation;
                        if (y >= oceanLevel - 1) { // above ocean level
                            chunkCache[chunkCachePos] = aboveOceanAkaLand;
                        } else {
                            chunkCache[chunkCachePos] = belowOceanAkaEarthCrust;
                        }
                        continue;
                    }
                    if (state > 0) {
                        state--;
                        chunkCache[chunkCachePos] = belowOceanAkaEarthCrust;

                    }
                }
            }
        }
    }


}
