package opti;
import main.NoiseGenerator;
import main.Random;


public class NoiseGeneratorPerlinBisBis extends NoiseGenerator {

    private final int[] permutations;
    public double xCoord_03;
    public double yCoord_03;
    public double zCoord_03;

    public NoiseGeneratorPerlinBisBis(Random random) {
        permutations = new int[512];
        xCoord_03 = random.nextDouble() * 256D;
        yCoord_03 = random.nextDouble() * 256D;
        zCoord_03 = random.nextDouble() * 256D;
        for (int i = 0; i < 256; i++) {
            permutations[i] = i;
        }

        for (int index = 0; index < 256; index++) {
            int randomIndex = random.nextInt(256 - index) + index;
            int swapValue = permutations[index];
            permutations[index] = permutations[randomIndex];
            permutations[randomIndex] = swapValue;
            // copy unnecessary
            permutations[index + 256] = permutations[index];
        }

    }

    public final double lerp(double x, double a, double b) {
        return a + x * (b - a);
    }

    public final double grad(int hash, double x, double y, double z) {
        switch (hash & 0xF) {
            case 0x0:
                return x + y;
            case 0x1:
                return -x + y;
            case 0x2:
                return x - y;
            case 0x3:
                return -x - y;
            case 0x4:
                return x + z;
            case 0x5:
                return -x + z;
            case 0x6:
                return x - z;
            case 0x7:
                return -x - z;
            case 0x8:
                return y + z;
            case 0x9:
            case 0xD:
                return -y + z;
            case 0xA:
                return y - z;
            case 0xB:
            case 0xF:
                return -y - z;
            case 0xC:
                return y + x;
            case 0xE:
                return y - x;
            default:
                return 0; // never happens
        }
    }

    //we care only about 60-61, 77-78, 145-146, 162-163, 230-231, 247-248, 315-316, 332-333, 400-401, 417-418
    public void generatePermutations(double[] buffer, double x, double y, double z, int sizeX, int sizeY, int sizeZ, double noiseFactorX, double noiseFactorY, double noiseFactorZ, double octaveSize) {
        double octaveWidth = 1.0D / octaveSize;
        int i2 = -1;
        double x1 = 0.0D;
        double x2 = 0.0D;
        double xx1 = 0.0D;
        double xx2 = 0.0D;
        double t;
        double w;
        int columnIndex=51; // possibleX[0]*5*17+3*17
        int[] possibleX={0,0,1,1,2,2,3,3,4,4};
        int[] possibleZ={3,4,3,4,3,4,3,4,3,4};
        for (int index = 0; index < possibleX.length; index++) {
            double xCoord = (x + (double) possibleX[index]) * noiseFactorX + xCoord_03;
            int clampedXcoord = (int) xCoord;
            if (xCoord < (double) clampedXcoord) {
                clampedXcoord--;
            }
            int xBottoms = clampedXcoord & 0xff;
            xCoord -= clampedXcoord;
            t = xCoord * 6D - 15D;
            w = (xCoord * t + 10D);
            double fadeX = xCoord * xCoord * xCoord * w;
            double zCoord = (z + (double)  possibleZ[index]) * noiseFactorZ + zCoord_03;
            int clampedZCoord = (int) zCoord;
            if (zCoord < (double) clampedZCoord) {
                clampedZCoord--;
            }
            int zBottoms = clampedZCoord & 0xff;
            zCoord -= clampedZCoord;
            t = zCoord * 6D - 15D;
            w = (zCoord * t + 10D);
            double fadeZ = zCoord * zCoord * zCoord * w;
            for (int Y = 0; Y < 11; Y++) { // we cannot limit on lower bound without some issues later
                // ZCoord
                double yCoords = (y + (double) Y) * noiseFactorY + yCoord_03;
                int clampedYCoords = (int) yCoords;
                if (yCoords < (double) clampedYCoords) {
                    clampedYCoords--;
                }
                int yBottoms = clampedYCoords & 0xff;
                yCoords -= clampedYCoords;
                t = yCoords * 6D - 15D;
                w = yCoords * t + 10D;
                double fadeY = yCoords * yCoords * yCoords * w;
                // ZCoord

                if (Y == 0 || yBottoms != i2) { // this is wrong on so many levels, same ybottoms doesnt mean x and z were the same...
                    i2 = yBottoms;
                    int k2 = permutations[permutations[xBottoms] + yBottoms] + zBottoms;
                    int l2 = permutations[permutations[xBottoms] + yBottoms + 1] + zBottoms;
                    int k3 = permutations[permutations[xBottoms + 1] + yBottoms] + zBottoms;
                    int l3 = permutations[permutations[xBottoms + 1] + yBottoms + 1] + zBottoms;
                    x1 = lerp(fadeX, grad(permutations[k2], xCoord, yCoords, zCoord), grad(permutations[k3], xCoord - 1.0D, yCoords, zCoord));
                    x2 = lerp(fadeX, grad(permutations[l2], xCoord, yCoords - 1.0D, zCoord), grad(permutations[l3], xCoord - 1.0D, yCoords - 1.0D, zCoord));
                    xx1 = lerp(fadeX, grad(permutations[k2 + 1], xCoord, yCoords, zCoord - 1.0D), grad(permutations[k3 + 1], xCoord - 1.0D, yCoords, zCoord - 1.0D));
                    xx2 = lerp(fadeX, grad(permutations[l2 + 1], xCoord, yCoords - 1.0D, zCoord - 1.0D), grad(permutations[l3 + 1], xCoord - 1.0D, yCoords - 1.0D, zCoord - 1.0D));
                }
                double y1 = lerp(fadeY, x1, x2);
                double y2 = lerp(fadeY, xx1, xx2);
                buffer[columnIndex] += lerp(fadeZ, y1, y2) * octaveWidth;
                columnIndex++;
            }
            if (index%2==0){
                columnIndex+=6; // 6 to complete Y
            }else{
                columnIndex+=possibleZ[0]*17+6; // 3*17 on Z +6 complete Y
            }
        }
    }
}
