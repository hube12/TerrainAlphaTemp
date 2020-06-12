



public class BiomeGeneration {

    public double[] temperature;
    public double[] humidity;
    public double[] precipitation;

    private final SimplexNoiseOctaves tempOctaves;
    private final SimplexNoiseOctaves humidityOctaves;
    private final SimplexNoiseOctaves unknowOctaves;

    public BiomeGeneration(long worldSeed) {
        tempOctaves = new SimplexNoiseOctaves(new Random(worldSeed * 9871L), 4);
        humidityOctaves = new SimplexNoiseOctaves(new Random(worldSeed * 39811L), 4);
        unknowOctaves = new SimplexNoiseOctaves(new Random(worldSeed * 0x84a59L), 2);
    }

    public BiomesBase[] loadBiomes(BiomesBase[] biomes, int chunkX, int chunkZ, int sizeX, int sizeZ) {
        if (biomes == null || biomes.length < sizeX * sizeZ) {
            biomes = new BiomesBase[sizeX * sizeZ];
        }
        temperature = tempOctaves.generateNoise(temperature, chunkX, chunkZ, sizeX, sizeX, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        humidity = humidityOctaves.generateNoise(humidity, chunkX, chunkZ, sizeX, sizeX, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        precipitation = unknowOctaves.generateNoise(precipitation, chunkX, chunkZ, sizeX, sizeX, 0.25D, 0.25D, 0.58823529411764708D);
        int index = 0;
        for (int X = 0; X < sizeX; X++) {
            for (int Z = 0; Z < sizeZ; Z++) {
                double precipitation = this.precipitation[index] * 1.1000000000000001D + 0.5D;
                double scaleDown = 0.01D;
                double d2 = 1.0D - scaleDown;
                double d3 = (temperature[index] * 0.14999999999999999D + 0.69999999999999996D) * d2 + precipitation * scaleDown;
                scaleDown = 0.002D;
                d2 = 1.0D - scaleDown;
                double d4 = (humidity[index] * 0.14999999999999999D + 0.5D) * d2 + precipitation * scaleDown;
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
                temperature[index] = d3;
                humidity[index] = d4;
                biomes[index++] = BiomesBase.getBiomesFromLookup(d3, d4);
            }
        }
        return biomes;
    }
}
