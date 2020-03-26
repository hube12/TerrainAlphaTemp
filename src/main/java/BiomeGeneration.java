



public class BiomeGeneration {

    public double[] temperature;
    public double[] humidity;
    public double[] unknown;

    private SimplifiedNoiseOctaves tempOctaves;
    private SimplifiedNoiseOctaves humidityOctaves;
    private SimplifiedNoiseOctaves unknowOctaves;

    public BiomeGeneration(long worldSeed) {
        tempOctaves = new SimplifiedNoiseOctaves(new Random(worldSeed * 9871L), 4);
        humidityOctaves = new SimplifiedNoiseOctaves(new Random(worldSeed * 39811L), 4);
        unknowOctaves = new SimplifiedNoiseOctaves(new Random(worldSeed * 0x84a59L), 2);
    }

    public BiomesBase[] loadBiomes(BiomesBase[] biomes, int chunkX, int chunkZ, int sizeX, int sizeZ) {
        if (biomes == null || biomes.length < sizeX * sizeZ) {
            biomes = new BiomesBase[sizeX * sizeZ];
        }
        temperature = tempOctaves.generateNoise(temperature, chunkX, chunkZ, sizeX, sizeX, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        humidity = humidityOctaves.generateNoise(humidity, chunkX, chunkZ, sizeX, sizeX, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        unknown = unknowOctaves.generateNoise(unknown, chunkX, chunkZ, sizeX, sizeX, 0.25D, 0.25D, 0.58823529411764708D);
        int i1 = 0;
        for (int j1 = 0; j1 < sizeX; j1++) {
            for (int k1 = 0; k1 < sizeZ; k1++) {
                double d = unknown[i1] * 1.1000000000000001D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;
                double d3 = (temperature[i1] * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;
                d1 = 0.002D;
                d2 = 1.0D - d1;
                double d4 = (humidity[i1] * 0.14999999999999999D + 0.5D) * d2 + d * d1;
                d3 = 1.0D - (1.0D - d3) * (1.0D - d3);
                if (d3 < 0.0D) {
                    d3 = 0.0D;
                }
                if (d4 < 0.0D) {
                    d4 = 0.0D;
                }
                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }
                if (d4 > 1.0D) {
                    d4 = 1.0D;
                }
                temperature[i1] = d3;
                humidity[i1] = d4;
                biomes[i1++] = BiomesBase.getBiomesFromLookup(d3, d4);
            }
        }
        return biomes;
    }
}
