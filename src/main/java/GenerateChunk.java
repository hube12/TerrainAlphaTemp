


public class GenerateChunk {
    private Random worldRandom;
    BiomeGeneration biomeGenerationInstance;
    double[] mainLimitPerlinNoise;
    double[] minLimitPerlinNoise;
    double[] maxLimitPerlinNoise;
    double[] field_4182_g;
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

        private int value;

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
    private NoiseGeneratorOctaves minLimit;
    private NoiseGeneratorOctaves maxLimit;
    private NoiseGeneratorOctaves mainLimit;
    private NoiseGeneratorOctaves shoresBottomComposition;
    private NoiseGeneratorOctaves surfaceElevationNoise;
    public NoiseGeneratorOctaves field_922_a;
    public NoiseGeneratorOctaves depth;
    public NoiseGeneratorOctaves field_920_c;

    public GenerateChunk(long worldSeed) {
        //this.biomeGenerationInstance = new BiomeGeneration(worldSeed);
        worldRandom = new Random(worldSeed);
        minLimit = new NoiseGeneratorOctaves(worldRandom, 16);
        maxLimit = new NoiseGeneratorOctaves(worldRandom, 16);
        mainLimit = new NoiseGeneratorOctaves(worldRandom, 8);
        shoresBottomComposition = new NoiseGeneratorOctaves(worldRandom, 4);
        surfaceElevationNoise = new NoiseGeneratorOctaves(worldRandom, 4);
        field_922_a = new NoiseGeneratorOctaves(worldRandom, 10);
        depth = new NoiseGeneratorOctaves(worldRandom, 16);
        field_920_c = new NoiseGeneratorOctaves(worldRandom, 8);
    }

    public byte[] provideChunk(int chunkX, int chunkZ, Boolean fast, BiomeGeneration biomeGenerationInstance,BiomesBase[] biomesForGeneration) {
        worldRandom.setSeed((long) chunkX * 0x4f9939f508L + (long) chunkZ * 0x1ef1565bd5L);
        byte[] chunkCache = new byte[32768];
        this.biomeGenerationInstance=biomeGenerationInstance;
        double[] temperatures = biomeGenerationInstance.temperature;
        byte[] heights = generateTerrain(chunkX, chunkZ, chunkCache, temperatures);
        replaceBlockForBiomes(chunkX, chunkZ, chunkCache, biomesForGeneration);
        return fast ? heights : chunkCache;
    }


    private double[] fillNoiseColumn(double[] NoiseColumn, int x, int z) {
        if (NoiseColumn == null) {
            NoiseColumn = new double[5 * 17 * 5];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        double[] ad1 = biomeGenerationInstance.temperature;
        double[] ad2 = biomeGenerationInstance.humidity;
        field_4182_g = field_922_a.generateFixedNoise(field_4182_g, x, z, 5, 5, 1.121D, 1.121D, 0.5D);
        depthNoise = depth.generateFixedNoise(depthNoise, x, z, 5, 5, 200D, 200D, 0.5D);
        mainLimitPerlinNoise = mainLimit.generateNoise(mainLimitPerlinNoise, x, 0, z, 5, 17, 5, d / 80D, d1 / 160D, d / 80D);
        minLimitPerlinNoise = minLimit.generateNoise(minLimitPerlinNoise, x, 0, z, 5, 17, 5, d, d1, d);
        maxLimitPerlinNoise = maxLimit.generateNoise(maxLimitPerlinNoise, x, 0, z, 5, 17, 5, d, d1, d);
        int k1 = 0;
        int l1 = 0;
        int i2 = 16 / 5;
        for (int j2 = 0; j2 < 5; j2++) {
            int k2 = j2 * i2 + i2 / 2;
            for (int l2 = 0; l2 < 5; l2++) {
                int i3 = l2 * i2 + i2 / 2;
                double d2 = ad1[k2 * 16 + i3];
                double d3 = ad2[k2 * 16 + i3] * d2;
                double d4 = 1.0D - d3;
                d4 *= d4;
                d4 *= d4;
                d4 = 1.0D - d4;
                double d5 = (field_4182_g[l1] + 256D) / 512D;
                d5 *= d4;
                if (d5 > 1.0D) {
                    d5 = 1.0D;
                }
                double d6 = depthNoise[l1] / 8000D;
                if (d6 < 0.0D) {
                    d6 = -d6 * 0.29999999999999999D;
                }
                d6 = d6 * 3D - 2D;
                if (d6 < 0.0D) {
                    d6 /= 2D;
                    if (d6 < -1D) {
                        d6 = -1D;
                    }
                    d6 /= 1.3999999999999999D;
                    d6 /= 2D;
                    d5 = 0.0D;
                } else {
                    if (d6 > 1.0D) {
                        d6 = 1.0D;
                    }
                    d6 /= 8D;
                }
                if (d5 < 0.0D) {
                    d5 = 0.0D;
                }
                d5 += 0.5D;
                d6 = (d6 * (double) 17) / 16D;
                double d7 = (double) 17 / 2D + d6 * 4D;
                l1++;
                for (int j3 = 0; j3 < 17; j3++) {
                    double d8 = 0.0D;
                    double d9 = (((double) j3 - d7) * 12D) / d5;
                    if (d9 < 0.0D) {
                        d9 *= 4D;
                    }
                    double d10 = minLimitPerlinNoise[k1] / 512D;
                    double d11 = maxLimitPerlinNoise[k1] / 512D;
                    double d12 = (mainLimitPerlinNoise[k1] / 10D + 1.0D) / 2D;
                    if (d12 < 0.0D) {
                        d8 = d10;
                    } else if (d12 > 1.0D) {
                        d8 = d11;
                    } else {
                        d8 = d10 + (d11 - d10) * d12;
                    }
                    d8 -= d9;
                    if (j3 > 17 - 4) {
                        double d13 = (float) (j3 - (17 - 4)) / 3F;
                        d8 = d8 * (1.0D - d13) + -10D * d13;
                    }
                    NoiseColumn[k1] = d8;
                    k1++;
                }

            }

        }

        return NoiseColumn;
    }

    public byte[] generateTerrain(int chunkX, int chunkZ, byte[] chunkCache, double[] temperatures) {
        byte cellSize = 4;
        byte byte1 = 64;
        byte byte2 = 17;
        int l = 5;
        byte[] heights = new byte[256];
        NoiseColumn = fillNoiseColumn(NoiseColumn, chunkX * cellSize, chunkZ * cellSize);
        for (int x = 0; x < cellSize; x++) {
            for (int z = 0; z < cellSize; z++) {
                for (int height = 0; height < 16; height++) {
                    double d = 0.125D;
                    int off_0_0 = x * l + z;
                    int off_0_1 = x * l + (z + 1);
                    int off_1_0 = (x + 1) * l + z;
                    int off_1_1 = (x + 1) * l + (z + 1);
                    double d1 = NoiseColumn[(off_0_0) * byte2 + (height)];
                    double d2 = NoiseColumn[(off_0_1) * byte2 + (height)];
                    double d3 = NoiseColumn[off_1_0 * byte2 + (height)];
                    double d4 = NoiseColumn[off_1_1 * byte2 + (height)];
                    double d5 = (NoiseColumn[(off_0_0) * byte2 + (height + 1)] - d1) * d;
                    double d6 = (NoiseColumn[(off_0_1) * byte2 + (height + 1)] - d2) * d;
                    double d7 = (NoiseColumn[off_1_0 * byte2 + (height + 1)] - d3) * d;
                    double d8 = (NoiseColumn[off_1_1 * byte2 + (height + 1)] - d4) * d;
                    for (int l1 = 0; l1 < 8; l1++) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for (int i2 = 0; i2 < 4; i2++) {
                            int j2 = i2 + x * 4 << 11 | z * 4 << 7 | height * 8 + l1;
                            char c = '\200';
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for (int k2 = 0; k2 < 4; k2++) {
                                double d17 = temperatures[(x * 4 + i2) * 16 + (z * 4 + k2)];
                                Blocks l2 = Blocks.AIR;
                                if (height * 8 + l1 < byte1) {
                                    if (d17 < 0.5D && height * 8 + l1 >= byte1 - 1) {
                                        l2 = Blocks.ICE;
                                    } else {
                                        l2 = Blocks.MOVING_WATER;
                                    }
                                }
                                if (d15 > 0.0D) {
                                    l2 = Blocks.STONE;
                                    heights[j2 >> 7] = (byte) (j2 & 0x7F);
                                }
                                chunkCache[j2] = (byte) l2.getValue();
                                j2 += c;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
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
        heightField = surfaceElevationNoise.generateNoise(heightField, i * 16, j * 16, 0.0D, 16, 16, 1, noiseFactor * 2D, noiseFactor * 2D, noiseFactor * 2D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                BiomesBase biome = biomes[x * 16 + z];
                boolean sandy = sandFields[x + z * 16] + worldRandom.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean gravelly = gravelField[x + z * 16] + worldRandom.nextDouble() * 0.20000000000000001D > 3D;
                int elevation = (int) (heightField[x + z * 16] / 3D + 3D + worldRandom.nextDouble() * 0.25D);
                int j1 = -1;
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
                        j1 = -1;
                        continue;
                    }
                    if (previousBlock != (byte) Blocks.STONE.getValue()) {
                        continue;
                    }
                    if (j1 == -1) {
                        if (elevation <= 0) {
                            aboveOceanAkaLand = 0;
                            belowOceanAkaEarthCrust = (byte) Blocks.STONE.getValue();
                        } else if (y >= oceanLevel - 4 && y <= oceanLevel + 1) {
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
                        if (y < oceanLevel && aboveOceanAkaLand == 0) {
                            aboveOceanAkaLand = (byte) Blocks.MOVING_WATER.getValue();
                        }
                        j1 = elevation;
                        if (y >= oceanLevel - 1) {
                            chunkCache[chunkCachePos] = aboveOceanAkaLand;
                        } else {
                            chunkCache[chunkCachePos] = belowOceanAkaEarthCrust;
                        }
                        continue;
                    }
                    if (j1 > 0) {
                        j1--;
                        chunkCache[chunkCachePos] = belowOceanAkaEarthCrust;

                    }
                }
            }
        }
    }


}
