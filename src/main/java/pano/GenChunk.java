package pano;

import main.Random;

public class GenChunk {
    enum Blocks {
        AIR(0),
        STONE(1),
        GRASS(2),
        DIRT(3),
        BEDROCK(7),
        MOVING_WATER(9),
        SAND(12),
        GRAVEL(13),
        ICE(79),
        SANDSTONE(81);

        private final int value;

        Blocks(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    private java.util.Random rand;
    private NoiseGeneratorOctaves field_912_k;
    private NoiseGeneratorOctaves field_911_l;
    private NoiseGeneratorOctaves field_910_m;
    private NoiseGeneratorOctaves field_909_n;
    private NoiseGeneratorOctaves field_908_o;
    public NoiseGeneratorOctaves field_922_a;
    public NoiseGeneratorOctaves field_921_b;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    private double field_4180_q[];
    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];
    private BiomeGenBase biomesForGeneration[];
    double field_4185_d[];
    double field_4184_e[];
    double field_4183_f[];
    double surfaceNoise[];
    double field_4181_h[];
    int field_914_i[][];
    private double generatedTemperatures[];
    private NoiseGeneratorOctaves2 temperatureOctaves;
    private NoiseGeneratorOctaves2 humiditiyOctavess;
    private NoiseGeneratorOctaves2 precipitationOctaves;
    public double temperature[];
    public double humidity[];
    public double precipitations[];
    public GenChunk(long seed){
        temperatureOctaves = new NoiseGeneratorOctaves2(new java.util.Random(seed * 9871L), 4);
        humiditiyOctavess = new NoiseGeneratorOctaves2(new java.util.Random(seed * 39811L), 4);
        precipitationOctaves = new NoiseGeneratorOctaves2(new java.util.Random(seed * 0x84a59L), 2);
        sandNoise = new double[256];
        gravelNoise = new double[256];
        stoneNoise = new double[256];
        field_914_i = new int[32][32];
        rand = new java.util.Random(seed);
        field_912_k = new NoiseGeneratorOctaves(rand, 16);
        field_911_l = new NoiseGeneratorOctaves(rand, 16);
        field_910_m = new NoiseGeneratorOctaves(rand, 8);
        field_909_n = new NoiseGeneratorOctaves(rand, 4);
        field_908_o = new NoiseGeneratorOctaves(rand, 4);
        field_922_a = new NoiseGeneratorOctaves(rand, 10);
        field_921_b = new NoiseGeneratorOctaves(rand, 16);
        mobSpawnerNoise = new NoiseGeneratorOctaves(rand, 8);
    }
    public byte[] provideChunk(int x, int z)
    {
        Random rand= new Random((long)x * 0x4f9939f508L + (long)z * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        biomesForGeneration = biomeGeneration(biomesForGeneration, x * 16, z * 16, 16, 16);
        double ad[] = temperature;
        generateTerrain(x, z, abyte0, biomesForGeneration, ad);
        replaceBlocksForBiome(x, z, abyte0, biomesForGeneration);
        return abyte0;

    }
    public void generateTerrain(int i, int j, byte abyte0[], BiomeGenBase abiomegenbase[], double ad[])
    {
        byte byte0 = 4;
        byte byte1 = 64;
        int k = byte0 + 1;
        byte byte2 = 17;
        int l = byte0 + 1;
        field_4180_q = fillNoiseColumn(field_4180_q, i * byte0, 0, j * byte0, k, byte2, l);
        for(int i1 = 0; i1 < byte0; i1++)
        {
            for(int j1 = 0; j1 < byte0; j1++)
            {
                for(int k1 = 0; k1 < 16; k1++)
                {
                    double d = 0.125D;
                    double d1 = field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte2 + (k1 + 0)];
                    double d2 = field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte2 + (k1 + 0)];
                    double d3 = field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte2 + (k1 + 0)];
                    double d4 = field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte2 + (k1 + 0)];
                    double d5 = (field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte2 + (k1 + 1)] - d1) * d;
                    double d6 = (field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte2 + (k1 + 1)] - d2) * d;
                    double d7 = (field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte2 + (k1 + 1)] - d3) * d;
                    double d8 = (field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte2 + (k1 + 1)] - d4) * d;
                    for(int l1 = 0; l1 < 8; l1++)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for(int i2 = 0; i2 < 4; i2++)
                        {
                            int j2 = i2 + i1 * 4 << 11 | 0 + j1 * 4 << 7 | k1 * 8 + l1;
                            char c = '\200';
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for(int k2 = 0; k2 < 4; k2++)
                            {
                                double d17 = ad[(i1 * 4 + i2) * 16 + (j1 * 4 + k2)];
                                int l2 = 0;
                                if(k1 * 8 + l1 < byte1)
                                {
                                    if(d17 < 0.5D && k1 * 8 + l1 >= byte1 - 1)
                                    {
                                        l2 = Blocks.ICE.value;
                                    } else
                                    {
                                        l2 = Blocks.MOVING_WATER.value;
                                    }
                                }
                                if(d15 > 0.0D)
                                {
                                    l2 = Blocks.STONE.value;
                                }
                                abyte0[j2] = (byte)l2;
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

    public void replaceBlocksForBiome(int i, int j, byte abyte0[], BiomeGenBase abiomegenbase[])
    {
        byte byte0 = 64;
        double d = 0.03125D;
        sandNoise = field_909_n.generateNoiseOctaves(sandNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d, d, 1.0D);
        gravelNoise = field_909_n.generateNoiseOctaves(gravelNoise, i * 16, 109.0134D, j * 16, 16, 1, 16, d, 1.0D, d);
        stoneNoise = field_908_o.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for(int k = 0; k < 16; k++)
        {
            for(int l = 0; l < 16; l++)
            {
                BiomeGenBase biomegenbase = abiomegenbase[k + l * 16];
                boolean flag = sandNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean flag1 = gravelNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                int i1 = (int)(stoneNoise[k + l * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int j1 = -1;
                byte byte1 = biomegenbase.topBlock;
                byte byte2 = biomegenbase.fillerBlock;
                for(int k1 = 127; k1 >= 0; k1--)
                {
                    int l1 = (l * 16 + k) * 128 + k1;
                    if(k1 <= 0 + rand.nextInt(5))
                    {
                        abyte0[l1] = (byte)Blocks.BEDROCK.value;
                        continue;
                    }
                    byte byte3 = abyte0[l1];
                    if(byte3 == 0)
                    {
                        j1 = -1;
                        continue;
                    }
                    if(byte3 != Blocks.STONE.value)
                    {
                        continue;
                    }
                    if(j1 == -1)
                    {
                        if(i1 <= 0)
                        {
                            byte1 = 0;
                            byte2 = (byte)Blocks.STONE.value;
                        } else
                        if(k1 >= byte0 - 4 && k1 <= byte0 + 1)
                        {
                            byte1 = biomegenbase.topBlock;
                            byte2 = biomegenbase.fillerBlock;
                            if(flag1)
                            {
                                byte1 = 0;
                            }
                            if(flag1)
                            {
                                byte2 = (byte)Blocks.GRAVEL.value;
                            }
                            if(flag)
                            {
                                byte1 = (byte)Blocks.SAND.value;
                            }
                            if(flag)
                            {
                                byte2 = (byte)Blocks.SAND.value;
                            }
                        }
                        if(k1 < byte0 && byte1 == 0)
                        {
                            byte1 = (byte)Blocks.MOVING_WATER.value;
                        }
                        j1 = i1;
                        if(k1 >= byte0 - 1)
                        {
                            abyte0[l1] = byte1;
                        } else
                        {
                            abyte0[l1] = byte2;
                        }
                        continue;
                    }
                    if(j1 <= 0)
                    {
                        continue;
                    }
                    j1--;
                    abyte0[l1] = byte2;
                    if(j1 == 0 && byte2 == Blocks.SAND.value)
                    {
                        j1 = rand.nextInt(4);
                        byte2 = (byte)Blocks.SANDSTONE.value;
                    }
                }

            }

        }

    }


    private double[] fillNoiseColumn(double ad[], int i, int j, int k, int l, int i1, int j1)
    {
        if(ad == null)
        {
            ad = new double[l * i1 * j1];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        surfaceNoise = field_922_a.generateFixedNoise(surfaceNoise, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        field_4181_h = field_921_b.generateFixedNoise(field_4181_h, i, k, l, j1, 200D, 200D, 0.5D);
        field_4185_d = field_910_m.generateNoiseOctaves(field_4185_d, i, j, k, l, i1, j1, d / 80D, d1 / 160D, d / 80D);
        field_4184_e = field_912_k.generateNoiseOctaves(field_4184_e, i, j, k, l, i1, j1, d, d1, d);
        field_4183_f = field_911_l.generateNoiseOctaves(field_4183_f, i, j, k, l, i1, j1, d, d1, d);
        int k1 = 0;
        int l1 = 0;
        int i2 = 16 / l;
        for(int j2 = 0; j2 < l; j2++)
        {
            int k2 = j2 * i2 + i2 / 2;
            for(int l2 = 0; l2 < j1; l2++)
            {
                int i3 = l2 * i2 + i2 / 2;
                double d2 = temperature[k2 * 16 + i3];
                double d3 = humidity[k2 * 16 + i3] * d2;
                double d4 = 1.0D - d3;
                d4 *= d4;
                d4 *= d4;
                d4 = 1.0D - d4;
                double d5 = (surfaceNoise[l1] + 256D) / 512D;
                d5 *= d4;
                if(d5 > 1.0D)
                {
                    d5 = 1.0D;
                }
                double d6 = field_4181_h[l1] / 8000D;
                if(d6 < 0.0D)
                {
                    d6 = -d6 * 0.29999999999999999D;
                }
                d6 = d6 * 3D - 2D;
                if(d6 < 0.0D)
                {
                    d6 /= 2D;
                    if(d6 < -1D)
                    {
                        d6 = -1D;
                    }
                    d6 /= 1.3999999999999999D;
                    d6 /= 2D;
                    d5 = 0.0D;
                } else
                {
                    if(d6 > 1.0D)
                    {
                        d6 = 1.0D;
                    }
                    d6 /= 8D;
                }
                if(d5 < 0.0D)
                {
                    d5 = 0.0D;
                }
                d5 += 0.5D;
                d6 = (d6 * (double)i1) / 16D;
                double d7 = (double)i1 / 2D + d6 * 4D;
                l1++;
                for(int j3 = 0; j3 < i1; j3++)
                {
                    double d8 = 0.0D;
                    double d9 = (((double)j3 - d7) * 12D) / d5;
                    if(d9 < 0.0D)
                    {
                        d9 *= 4D;
                    }
                    double d10 = field_4184_e[k1] / 512D;
                    double d11 = field_4183_f[k1] / 512D;
                    double d12 = (field_4185_d[k1] / 10D + 1.0D) / 2D;
                    if(d12 < 0.0D)
                    {
                        d8 = d10;
                    } else
                    if(d12 > 1.0D)
                    {
                        d8 = d11;
                    } else
                    {
                        d8 = d10 + (d11 - d10) * d12;
                    }
                    d8 -= d9;
                    if(j3 > i1 - 4)
                    {
                        double d13 = (float)(j3 - (i1 - 4)) / 3F;
                        d8 = d8 * (1.0D - d13) + -10D * d13;
                    }
                    ad[k1] = d8;
                    k1++;
                }

            }

        }

        return ad;
    }
    public BiomeGenBase[] biomeGeneration(BiomeGenBase abiomegenbase[], int i, int j, int k, int l)
    {
        if(abiomegenbase == null || abiomegenbase.length < k * l)
        {
            abiomegenbase = new BiomeGenBase[k * l];
        }
        temperature = temperatureOctaves.generateNoise(temperature, i, j, k, k, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        humidity = humiditiyOctavess.generateNoise(humidity, i, j, k, k, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        precipitations = precipitationOctaves.generateNoise(precipitations, i, j, k, k, 0.25D, 0.25D, 0.58823529411764708D);
        int index = 0;
        for(int j1 = 0; j1 < k; j1++)
        {
            for(int k1 = 0; k1 < l; k1++)
            {
                double precipitation = precipitations[index] * 1.1000000000000001D + 0.5D;
                double temp = (temperature[index] * 0.14999999999999999D + 0.69999999999999996D) * (1.0D - 0.01D) + precipitation * 0.01D;
                temp = 1.0D - (1.0D - temp) * (1.0D - temp);
                if(temp < 0.0D)
                {
                    temp = 0.0D;
                }
                if(temp > 1.0D)
                {
                    temp = 1.0D;
                }
                double humidity = (this.humidity[index] * 0.14999999999999999D + 0.5D) * (1.0D -0.002D ) + precipitation * 0.002D;
                if(humidity < 0.0D)
                {
                    humidity = 0.0D;
                }
                if(humidity > 1.0D)
                {
                    humidity = 1.0D;
                }
                temperature[index] = temp;
                this.humidity[index] = humidity;
                abiomegenbase[index++] = BiomeGenBase.getBiomeFromLookup(temp, humidity);
            }

        }

        return abiomegenbase;
    }

}
