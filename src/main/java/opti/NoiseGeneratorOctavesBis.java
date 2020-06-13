package opti;

import main.NoiseGenerator;
import main.Random;

import java.util.Arrays;

public class NoiseGeneratorOctavesBis extends NoiseGenerator {
    private final NoiseGeneratorPerlinBis[] noiseArray;
    private final int octaves;

    public NoiseGeneratorOctavesBis(Random random, int nbOctaves) {
        octaves = nbOctaves;
        noiseArray = new NoiseGeneratorPerlinBis[nbOctaves];
        for (int octave = 0; octave < nbOctaves; octave++) {
            noiseArray[octave] = new NoiseGeneratorPerlinBis(random);
        }

    }

    public double[] generateNoise(double[] buffer, double x, double y, double z, int sizeX, int sizeY, int sizeZ, double offsetX, double offsetY, double offsetZ) {
        if (buffer == null) {
            buffer = new double[sizeX * sizeY * sizeZ];
        } else {
            Arrays.fill(buffer, 0.0D);

        }
        double octavesFactor = 1.0D;
        for (int octave = 0; octave < octaves; octave++) {
            noiseArray[octave].generatePermutations(buffer, x, y, z, sizeX, sizeY, sizeZ, offsetX * octavesFactor, offsetY * octavesFactor, offsetZ * octavesFactor, octavesFactor);
            octavesFactor /= 2D;
        }

        return buffer;
    }

    public double[] generateFixedNoise(double[] buffer, int x, int z, int sizeX, int sizeZ, double offsetX, double offsetZ) {
        return generateNoise(buffer, x, 10D, z, sizeX, 1, sizeZ, offsetX, 1.0D, offsetZ);
    }


}
