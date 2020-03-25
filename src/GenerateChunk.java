import java.util.Random;

public class GenerateChunk {
    private Random worldRandom;
    private BiomesBase[] biomesForGeneration;
    BiomeGeneration biomeGenerationInstance;
    double[] field_4185_d;
    double[] field_4184_e;
    double[] field_4183_f;
    double[] field_4182_g;
    double[] field_4181_h;

    enum Blocks {
        DEFAULT(0),
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

    private double[] field_905_r;
    private double[] field_904_s;
    private double[] field_903_t;
    private double[] NoiseColumn;
    private NoiseGeneratorOctaves field_912_k;
    private NoiseGeneratorOctaves field_911_l;
    private NoiseGeneratorOctaves field_910_m;
    private NoiseGeneratorOctaves field_909_n;
    private NoiseGeneratorOctaves field_908_o;
    public NoiseGeneratorOctaves field_922_a;
    public NoiseGeneratorOctaves field_921_b;
    public NoiseGeneratorOctaves field_920_c;

    public GenerateChunk(long worldSeed) {
        this.biomeGenerationInstance = new BiomeGeneration(worldSeed);
        worldRandom = new Random(worldSeed);
        field_912_k = new NoiseGeneratorOctaves(worldRandom, 16);
        field_911_l = new NoiseGeneratorOctaves(worldRandom, 16);
        field_910_m = new NoiseGeneratorOctaves(worldRandom, 8);
        field_909_n = new NoiseGeneratorOctaves(worldRandom, 4);
        field_908_o = new NoiseGeneratorOctaves(worldRandom, 4);
        field_922_a = new NoiseGeneratorOctaves(worldRandom, 10);
        field_921_b = new NoiseGeneratorOctaves(worldRandom, 16);
        field_920_c = new NoiseGeneratorOctaves(worldRandom, 8);
    }

    public byte[] provideChunk(int chunkX, int chunkZ) {
        worldRandom.setSeed((long) chunkX * 0x4f9939f508L + (long) chunkZ * 0x1ef1565bd5L);
        byte[] chunkCache = new byte[32768];
        biomesForGeneration = biomeGenerationInstance.loadBiomes(biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        double[] temperatures = biomeGenerationInstance.temperature;
        generateTerrain(chunkX, chunkZ, chunkCache, biomesForGeneration, temperatures);
        replaceBlockForBiomes(chunkX, chunkZ, chunkCache, biomesForGeneration);
        return chunkCache;
    }


    private double[] fillNoiseColumn(double[] NoiseColumn, int x, int z) {
        if (NoiseColumn == null) {
            NoiseColumn = new double[5 * 17 * 5];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        double[] ad1 = biomeGenerationInstance.temperature;
        double[] ad2 = biomeGenerationInstance.humidity;
        field_4182_g = field_922_a.func_4109_a(field_4182_g, x, z, 5, 5, 1.121D, 1.121D, 0.5D);
        field_4181_h = field_921_b.func_4109_a(field_4181_h, x, z, 5, 5, 200D, 200D, 0.5D);
        field_4185_d = field_910_m.func_807_a(field_4185_d, x, 0, z, 5, 17, 5, d / 80D, d1 / 160D, d / 80D);
        field_4184_e = field_912_k.func_807_a(field_4184_e, x, 0, z, 5, 17, 5, d, d1, d);
        field_4183_f = field_911_l.func_807_a(field_4183_f, x, 0, z, 5, 17, 5, d, d1, d);
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
                double d6 = field_4181_h[l1] / 8000D;
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
                    double d10 = field_4184_e[k1] / 512D;
                    double d11 = field_4183_f[k1] / 512D;
                    double d12 = (field_4185_d[k1] / 10D + 1.0D) / 2D;
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

    public void generateTerrain(int chunkX, int chunkZ, byte[] chunkCache, BiomesBase[] biomesForGeneration, double[] temperatures) {
        byte cellSize = 4;
        byte byte1 = 64;
        int k = 5;
        byte byte2 = 17;
        int l = 5;
        NoiseColumn = fillNoiseColumn(NoiseColumn, chunkX * cellSize, chunkZ * cellSize);
        for (int x = 0; x < cellSize; x++) {
            for (int z = 0; z < cellSize; z++) {
                for (int k1 = 0; k1 < 16; k1++) {
                    double d = 0.125D;
                    int off_0_0 = x * l + z;
                    int off_0_1 = x * l + (z + 1);
                    int off_1_0 = (x + 1) * l + z;
                    int off_1_1 = (x + 1) * l + (z + 1);
                    double d1 = NoiseColumn[(off_0_0) * byte2 + (k1)];
                    double d2 = NoiseColumn[(off_0_1) * byte2 + (k1)];
                    double d3 = NoiseColumn[off_1_0 * byte2 + (k1)];
                    double d4 = NoiseColumn[off_1_1 * byte2 + (k1)];
                    double d5 = (NoiseColumn[(off_0_0) * byte2 + (k1 + 1)] - d1) * d;
                    double d6 = (NoiseColumn[(off_0_1) * byte2 + (k1 + 1)] - d2) * d;
                    double d7 = (NoiseColumn[off_1_0 * byte2 + (k1 + 1)] - d3) * d;
                    double d8 = (NoiseColumn[off_1_1 * byte2 + (k1 + 1)] - d4) * d;
                    for (int l1 = 0; l1 < 8; l1++) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for (int i2 = 0; i2 < 4; i2++) {
                            int j2 = i2 + x * 4 << 11 | z * 4 << 7 | k1 * 8 + l1;
                            char c = '\200';
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for (int k2 = 0; k2 < 4; k2++) {
                                double d17 = temperatures[(x * 4 + i2) * 16 + (z * 4 + k2)];
                                Blocks l2 = Blocks.DEFAULT;
                                if (k1 * 8 + l1 < byte1) {
                                    if (d17 < 0.5D && k1 * 8 + l1 >= byte1 - 1) {
                                        l2 = Blocks.ICE;
                                    } else {
                                        l2 = Blocks.MOVING_WATER;
                                    }
                                }
                                if (d15 > 0.0D) {
                                    l2 = Blocks.STONE;
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
    }


    public void replaceBlockForBiomes(int i, int j, byte[] chunkCache, BiomesBase[] biomes) {
        byte byte0 = 64;
        double d = 0.03125D;
        field_905_r = field_909_n.func_807_a(field_905_r, i * 16, j * 16, 0.0D, 16, 16, 1, d, d, 1.0D);
        field_904_s = field_909_n.func_807_a(field_904_s, j * 16, 109.0134D, i * 16, 16, 1, 16, d, 1.0D, d);
        field_903_t = field_908_o.func_807_a(field_903_t, i * 16, j * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for (int k = 0; k < 16; k++) {
            for (int l = 0; l < 16; l++) {
                BiomesBase biome = biomes[k * 16 + l];
                boolean flag = field_905_r[k + l * 16] + worldRandom.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean flag1 = field_904_s[k + l * 16] + worldRandom.nextDouble() * 0.20000000000000001D > 3D;
                int i1 = (int) (field_903_t[k + l * 16] / 3D + 3D + worldRandom.nextDouble() * 0.25D);
                int j1 = -1;
                byte byte1 = biome.field_4242_o;
                byte byte2 = biome.field_4241_p;
                for (int k1 = 127; k1 >= 0; k1--) {
                    int l1 = (k * 16 + l) * 128 + k1;
                    if (k1 <= worldRandom.nextInt(5)) {
                        chunkCache[l1] = (byte) Blocks.BEDROCK.getValue();
                        continue;
                    }
                    byte byte3 = chunkCache[l1];
                    if (byte3 == 0) {
                        j1 = -1;
                        continue;
                    }
                    if (byte3 != (byte) Blocks.STONE.getValue()) {
                        continue;
                    }
                    if (j1 == -1) {
                        if (i1 <= 0) {
                            byte1 = 0;
                            byte2 = (byte) Blocks.STONE.getValue();
                        } else if (k1 >= byte0 - 4 && k1 <= byte0 + 1) {
                            byte1 = biome.field_4242_o;
                            byte2 = biome.field_4241_p;
                            if (flag1) {
                                byte1 = 0;
                            }
                            if (flag1) {
                                byte2 = (byte) Blocks.GRAVEL.getValue();
                            }
                            if (flag) {
                                byte1 = (byte) Blocks.SAND.getValue();
                            }
                            if (flag) {
                                byte2 = (byte) Blocks.SAND.getValue();
                            }
                        }
                        if (k1 < byte0 && byte1 == 0) {
                            byte1 = (byte) Blocks.MOVING_WATER.getValue();
                        }
                        j1 = i1;
                        if (k1 >= byte0 - 1) {
                            chunkCache[l1] = byte1;
                        } else {
                            chunkCache[l1] = byte2;
                        }
                        continue;
                    }
                    if (j1 > 0) {
                        j1--;
                        chunkCache[l1] = byte2;
                    }
                }
            }
        }
    }


}
