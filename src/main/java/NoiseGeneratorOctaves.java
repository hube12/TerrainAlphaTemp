

import java.util.Arrays;


public class NoiseGeneratorOctaves extends NoiseGenerator {

    public NoiseGeneratorOctaves(Random random, int nbOctaves) {
        octaves = nbOctaves;
        noiseArray = new NoiseGeneratorPerlin[nbOctaves];
        for (int octave = 0; octave < nbOctaves; octave++) {
            noiseArray[octave] = new NoiseGeneratorPerlin(random);
        }

    }

    public double[] generateNoise(double[] buffer, double x, double y, double z,
                                  int sizeX, int sizeY, int sizeZ, double d3, double d4,
                                  double d5) {
        if (buffer == null) {
            buffer = new double[sizeX * sizeY * sizeZ];
        } else {
            Arrays.fill(buffer, 0.0D);

        }
        double octavesFactor = 1.0D;
        for (int octave = 0; octave < octaves; octave++) {
            noiseArray[octave].generatePermutations(buffer, x, y, z, sizeX, sizeY, sizeZ, d3 * octavesFactor, d4 * octavesFactor, d5 * octavesFactor, octavesFactor);
            octavesFactor /= 2D;
        }

        return buffer;
    }

    public double[] generateFixedNoise(double[] buffer, int x, int z, int sizeX, int sizeZ, double d, double d1) {
        return generateNoise(buffer, x, 10D, z, sizeX, 1, sizeZ, d, 1.0D, d1);
    }

    private final NoiseGeneratorPerlin[] noiseArray;
    private final int octaves;
}
