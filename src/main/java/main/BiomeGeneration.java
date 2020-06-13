package main;

public class BiomeGeneration {

    public double[] temperature;
    public double[] humidity;
    public double[] precipitation;

    private final SimplexNoiseOctaves tempOctaves;
    private final SimplexNoiseOctaves humidityOctaves;
    private final SimplexNoiseOctaves precipitationOctaves;

    public BiomeGeneration(long worldSeed) {
        tempOctaves = new SimplexNoiseOctaves(new Random(worldSeed * 9871L), 4);
        humidityOctaves = new SimplexNoiseOctaves(new Random(worldSeed * 39811L), 4);
        precipitationOctaves = new SimplexNoiseOctaves(new Random(worldSeed * 0x84a59L), 2);
    }

    public BiomesBase[] loadBiomes(BiomesBase[] biomes, int chunkX, int chunkZ, int sizeX, int sizeZ) {
        if (biomes == null || biomes.length < sizeX * sizeZ) {
            biomes = new BiomesBase[sizeX * sizeZ];
        }
        temperature = tempOctaves.generateNoise(temperature, chunkX, chunkZ, sizeX, sizeX, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        humidity = humidityOctaves.generateNoise(humidity, chunkX, chunkZ, sizeX, sizeX, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        precipitation = precipitationOctaves.generateNoise(precipitation, chunkX, chunkZ, sizeX, sizeX, 0.25D, 0.25D, 0.58823529411764708D);
        int index = 0;
        for (int X = 0; X < sizeX; X++) {
            for (int Z = 0; Z < sizeZ; Z++) {
                double precipitation = this.precipitation[index] * 1.1000000000000001D + 0.5D;
                double temp = (temperature[index] * 0.14999999999999999D + 0.69999999999999996D) * (1.0D - 0.01D) + precipitation * 0.01D;
                temp = 1.0D - (1.0D - temp) * (1.0D - temp);
                if (temp < 0.0D) {
                    temp = 0.0D;
                }
                if (temp > 1.0D) {
                    temp = 1.0D;
                }
                double humi = (humidity[index] * 0.14999999999999999D + 0.5D) * (1.0D - 0.002D) + precipitation * 0.002D;
                if (humi < 0.0D) {
                    humi = 0.0D;
                }
                if (humi > 1.0D) {
                    humi = 1.0D;
                }
                temperature[index] = temp;
                humidity[index] = humi;
                biomes[index] = BiomesBase.getBiomesFromLookup(temp, humi);
                index++;
            }
        }
        return biomes;
    }
}
