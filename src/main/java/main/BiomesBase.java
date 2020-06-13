package main;

public class BiomesBase {
    public String name;
    public int color;
    public byte grassOrEquivalentSand;
    public byte dirtOrEquivalentSand;
    public int grassColor;

    public BiomesBase() {
        grassOrEquivalentSand = (byte) GenerateChunk.Blocks.GRASS.getValue();
        dirtOrEquivalentSand = (byte) GenerateChunk.Blocks.DIRT.getValue();
        grassColor = 0x4ee031;
    }

    protected BiomesBase setName(String s) {
        name = s;
        return this;
    }

    protected BiomesBase setGrassColor(int i) {
        grassColor = i;
        return this;
    }

    protected BiomesBase setColor(int i) {
        color = i;
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


    public static final BiomesBase Rainforest = (new BiomesBase()).setColor(0x8fa36).setName("Rainforest").setGrassColor(0x1ff458);
    public static final BiomesBase Swampland = (new BiomesBase()).setColor(0x7f9b2).setName("Swampland").setGrassColor(0x8baf48);
    public static final BiomesBase Seasonal_forest = (new BiomesBase()).setColor(0x9be023).setName("Seasonal Forest");
    public static final BiomesBase Forest = (new BiomesBase()).setColor(0x56621).setName("Forest").setGrassColor(0x4eba31);
    public static final BiomesBase Savanna = (new BiomesBase()).setColor(0xd9e023).setName("Savanna");
    public static final BiomesBase Shrubland = (new BiomesBase()).setColor(0xa1ad20).setName("Shrubland");
    public static final BiomesBase Taiga = (new BiomesBase()).setColor(0x2eb153).setName("Taiga").setGrassColor(0x7bb731);
    public static final BiomesBase Desert = (new BiomesBase()).setColor(0xfa9418).setName("Desert");
    public static final BiomesBase Plains = (new BiomesBase()).setColor(0xffd910).setName("Plains");
    public static final BiomesBase IceDesert = (new BiomesBase()).setColor(0xffed93).setName("Ice Desert").setGrassColor(0xc4d339);
    public static final BiomesBase Tundra = (new BiomesBase()).setColor(0x57ebf9).setName("Tundra").setGrassColor(0xc4d339);

    private static final BiomesBase[] biomeLookupTables = new BiomesBase[4096];

    static {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                biomeLookupTables[i + j * 64] = getBiomeFor((float) i / 63F, (float) j / 63F);
            }
        }
        Desert.grassOrEquivalentSand = Desert.dirtOrEquivalentSand = (byte) GenerateChunk.Blocks.SAND.getValue();
        IceDesert.grassOrEquivalentSand = IceDesert.dirtOrEquivalentSand = (byte) GenerateChunk.Blocks.SAND.getValue();
    }
}
