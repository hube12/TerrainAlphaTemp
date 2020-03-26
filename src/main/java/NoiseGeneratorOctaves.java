

import java.util.Arrays;


public class NoiseGeneratorOctaves extends NoiseGenerator {

    public NoiseGeneratorOctaves(Random random, int i) {
        octaves = i;
        noiseArray = new NoiseGeneratorPerlin[i];
        for (int j = 0; j < i; j++) {
            noiseArray[j] = new NoiseGeneratorPerlin(random);
        }

    }

    public double[] generateNoise(double[] ad, double d, double d1, double d2,
                                  int i, int j, int k, double d3, double d4,
                                  double d5) {
        if (ad == null) {
            ad = new double[i * j * k];
        } else {
            Arrays.fill(ad, 0.0D);

        }
        double d6 = 1.0D;
        for (int i1 = 0; i1 < octaves; i1++) {
            noiseArray[i1].generatePermutations(ad, d, d1, d2, i, j, k, d3 * d6, d4 * d6, d5 * d6, d6);
            d6 /= 2D;
        }

        return ad;
    }

    public double[] generateFixedNoise(double[] ad, int i, int j, int k, int l, double d,
                                       double d1, double d2) {
        return generateNoise(ad, i, 10D, j, k, 1, l, d, 1.0D, d1);
    }

    private NoiseGeneratorPerlin[] noiseArray;
    private int octaves;
}
