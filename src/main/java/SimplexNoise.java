


public class SimplexNoise {


    public SimplexNoise(Random random) {
        permutations = new int[512];
        xo = random.nextDouble() * 256D;
        yo = random.nextDouble() * 256D;
        zo = random.nextDouble() * 256D;
        for (int index = 0; index < 256; index++) {
            permutations[index] = index;
        }

        for (int index = 0; index < 256; index++) {
            int randomIndex = random.nextInt(256 - index) + index;
            int swapValue = permutations[index];
            permutations[index] = permutations[randomIndex];
            permutations[randomIndex] = swapValue;
            // copy unecessary ( // To remove the need for index wrapping, double the permutation table length)
            permutations[index + 256] = permutations[index];
        }

    }

    private static int fastFloor(double d) {
        return d <= 0.0D ? (int) d - 1 : (int) d;
    }

    private static double dot(int[] g, double x, double z) {
        return (double) g[0] * x + (double) g[1] * z;
    }

    public void noise(double[] cache, double chunkX, double chunkZ, int x, int z, double offsetX, double offsetZ, double octaveFactor) {
        int k = 0;
        for (int X = 0; X < x; X++) {
            double Xcoords = (chunkX + (double) X) * offsetX + xo;
            for (int Z = 0; Z < z; Z++) {
                double Zcoords = (chunkZ + (double) Z) * offsetZ + yo;
                // Skew the input space to determine which simplex cell we're in
                double hairyFactor = (Xcoords + Zcoords) * F2;
                int xHairy = fastFloor(Xcoords + hairyFactor);
                int zHairy = fastFloor(Zcoords + hairyFactor);
                double d11 = (double) (xHairy + zHairy) * G2;
                double X0 = (double) xHairy - d11; // Unskew the cell origin back to (x,y) space
                double Y0 = (double) zHairy - d11;
                double x0 = Xcoords - X0; // The x,y distances from the cell origin
                double y0 = Zcoords - Y0;
                // For the 2D case, the simplex shape is an equilateral triangle.
                // Determine which simplex we are in.
                int offsetSecondCornerX, offsetSecondCornerZ; // Offsets for second (middle) corner of simplex in (i,j) coords

                if (x0 > y0) {  // lower triangle, XY order: (0,0)->(1,0)->(1,1)
                    offsetSecondCornerX = 1;
                    offsetSecondCornerZ = 0;
                } else { // upper triangle, YX order: (0,0)->(0,1)->(1,1)
                    offsetSecondCornerX = 0;
                    offsetSecondCornerZ = 1;
                }

                double x1 = (x0 - (double) offsetSecondCornerX) + G2; // Offsets for middle corner in (x,y) unskewed coords
                double y1 = (y0 - (double) offsetSecondCornerZ) + G2;
                double x2 = (x0 - 1.0D) + 2D * G2; ; // Offsets for last corner in (x,y) unskewed coords
                double y2 = (y0 - 1.0D) + 2D * G2;

                // Work out the hashed gradient indices of the three simplex corners
                int ii = xHairy & 0xff;
                int jj = zHairy & 0xff;
                int gi0 = permutations[ii + permutations[jj]] % 12;
                int gi1 = permutations[ii + offsetSecondCornerX + permutations[jj + offsetSecondCornerZ]] % 12;
                int gi2 = permutations[ii + 1 + permutations[jj + 1]] % 12;

                // Calculate the contribution from the three corners
                double t0 = 0.5D - x0 * x0 - y0 * y0;
                double n0;
                if (t0 < 0.0D) {
                    n0 = 0.0D;
                } else {
                    t0 *= t0;
                    n0 = t0 * t0 * dot(grad3[gi0], x0, y0);  // (x,y) of grad3 used for 2D gradient
                }
                double t1 = 0.5D - x1 * x1 - y1 * y1;
                double n1;
                if (t1 < 0.0D) {
                    n1 = 0.0D;
                } else {
                    t1 *= t1;
                    n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
                }
                double t2 = 0.5D - x2 * x2 - y2 * y2;
                double n2;
                if (t2 < 0.0D) {
                    n2 = 0.0D;
                } else {
                    t2 *= t2;
                    n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
                }
                // Add contributions from each corner to get the final noise value.
                // The result is scaled to return values in the interval [-1,1].
                cache[k++] += 70D * (n0 + n1 + n2) * octaveFactor;
            }

        }

    }

    private static final int[][] grad3 = {{1, 1, 0
    }, {-1, 1, 0
    }, {1, -1, 0
    }, {-1, -1, 0
    }, {1, 0, 1
    }, {-1, 0, 1
    }, {1, 0, -1
    }, {-1, 0, -1
    }, {0, 1, 1
    }, {0, -1, 1
    }, {0, 1, -1
    }, {0, -1, -1
    }
    };
    private final int[] permutations;
    public double xo;
    public double yo;
    public double zo;
    private static final double F2 = 0.5D * (Math.sqrt(3D) - 1.0D);
    private static final double G2 = (3D - Math.sqrt(3D)) / 6D;

}
