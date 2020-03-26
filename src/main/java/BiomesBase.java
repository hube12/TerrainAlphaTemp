

import java.awt.Color;

public class BiomesBase {

    public BiomesBase() {
        grassOrEquivalentSand = (byte) GenerateChunk.Blocks.GRASS.getValue();
        dirtOrEquivalentSand = (byte) GenerateChunk.Blocks.DIRT.getValue();
        defaultColor = 0x4ee031;
    }

    public static void func_4120_a() {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                biomeLookupTables[i + j * 64] = getBiomeFor((float) i / 63F, (float) j / 63F);
            }

        }

        Desert.grassOrEquivalentSand = Desert.dirtOrEquivalentSand = (byte) GenerateChunk.Blocks.SAND.getValue();
        IceDesert.grassOrEquivalentSand = IceDesert.dirtOrEquivalentSand = (byte)GenerateChunk.Blocks.SAND.getValue();
    }

    protected BiomesBase func_4122_b() {
        return this;
    }

    protected BiomesBase func_4125_a(String s) {
        field_6504_m = s;
        return this;
    }

    protected BiomesBase func_4124_a(int i) {
        defaultColor = i;
        return this;
    }

    protected BiomesBase func_4123_b(int i) {
        field_6503_n = i;
        return this;
    }

    public static BiomesBase getBiomesFromLookup(double d, double d1) {
        int i = (int) (d * 63D);
        int j = (int) (d1 * 63D);
        return biomeLookupTables[i + j * 64];
    }

    public static BiomesBase getBiomeFor(float f, float f1) {
        f1 *= f;
        if (f < 0.1F) {
            return Tundra;
        }
        if (f1 < 0.2F) {
            if (f < 0.5F) {
                return Tundra;
            }
            if (f < 0.95F) {
                return Savanna;
            } else {
                return Desert;
            }
        }
        if (f1 > 0.5F && f < 0.7F) {
            return Swampland;
        }
        if (f < 0.5F) {
            return Taiga;
        }
        if (f < 0.97F) {
            if (f1 < 0.35F) {
                return Shrubland;
            } else {
                return Forest;
            }
        }
        if (f1 < 0.45F) {
            return Plains;
        }
        if (f1 < 0.9F) {
            return Seasonal_forest;
        } else {
            return Rainforest;
        }
    }

    public int func_4126_a(float f) {
        f /= 3F;
        if (f < -1F) {
            f = -1F;
        }
        if (f > 1.0F) {
            f = 1.0F;
        }
        return Color.getHSBColor(0.6222222F - f * 0.05F, 0.5F + f * 0.1F, 1.0F).getRGB();
    }


    public static final BiomesBase Rainforest = (new BiomesBase()).func_4123_b(0x8fa36).func_4125_a("Rainforest").func_4124_a(0x1ff458);
    public static final BiomesBase Swampland = (new BiomesBase()).func_4123_b(0x7f9b2).func_4125_a("Swampland").func_4124_a(0x8baf48);
    public static final BiomesBase Seasonal_forest = (new BiomesBase()).func_4123_b(0x9be023).func_4125_a("Seasonal Forest");
    public static final BiomesBase Forest = (new BiomesBase()).func_4123_b(0x56621).func_4125_a("Forest").func_4124_a(0x4eba31);
    public static final BiomesBase Savanna = (new BiomesBase()).func_4123_b(0xd9e023).func_4125_a("Savanna");
    public static final BiomesBase Shrubland = (new BiomesBase()).func_4123_b(0xa1ad20).func_4125_a("Shrubland");
    public static final BiomesBase Taiga = (new BiomesBase()).func_4123_b(0x2eb153).func_4125_a("Taiga").func_4122_b().func_4124_a(0x7bb731);
    public static final BiomesBase Desert = (new BiomesBase()).func_4123_b(0xfa9418).func_4125_a("Desert");
    public static final BiomesBase Plains = (new BiomesBase()).func_4123_b(0xffd910).func_4125_a("Plains");
    public static final BiomesBase IceDesert = (new BiomesBase()).func_4123_b(0xffed93).func_4125_a("Ice Desert").func_4122_b().func_4124_a(0xc4d339);
    public static final BiomesBase Tundra = (new BiomesBase()).func_4123_b(0x57ebf9).func_4125_a("Tundra").func_4122_b().func_4124_a(0xc4d339);
    public static final BiomesBase Nether = (new BiomesBase()).func_4123_b(0xff0000).func_4125_a("Hell");
    public String field_6504_m;
    public int field_6503_n;
    public byte grassOrEquivalentSand;
    public byte dirtOrEquivalentSand;
    public int defaultColor;
    private static BiomesBase[] biomeLookupTables = new BiomesBase[4096];

    static {
        func_4120_a();
    }
}
