package pano;// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode



import java.util.ArrayList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            Block, BlockGrass, SpawnListEntry, EntitySpider,
//            EntityZombie, EntitySkeleton, EntityCreeper, EntitySlime,
//            EntitySheep, EntityPig, EntityChicken, EntityCow,
//            EntitySquid, WorldGenBigTree, WorldGenTrees, EnumCreatureType,
//            BiomeGenRainforest, BiomeGenSwamp, BiomeGenForest, BiomeGenDesert,
//            BiomeGenTaiga, BiomeGenHell, BiomeGenSky, WorldGenerator

public class BiomeGenBase
{


    protected BiomeGenBase()
    {
        topBlock = (byte) GenChunk.Blocks.GRASS.getValue();
        fillerBlock = (byte) GenChunk.Blocks.DIRT.getValue();
        field_6502_q = 0x4ee031;
        spawnableMonsterList = new ArrayList();
        spawnableCreatureList = new ArrayList();
        spawnableWaterCreatureList = new ArrayList();
        enableRain = true;
    }

    private BiomeGenBase setDisableRain()
    {
        enableRain = false;
        return this;
    }

    public static void generateBiomeLookup()
    {
        for(int i = 0; i < 64; i++)
        {
            for(int j = 0; j < 64; j++)
            {
                biomeLookupTable[i + j * 64] = getBiome((float)i / 63F, (float)j / 63F);
            }

        }

        desert.topBlock = desert.fillerBlock = (byte) GenChunk.Blocks.SAND.getValue();
        iceDesert.topBlock = iceDesert.fillerBlock = (byte) GenChunk.Blocks.SAND.getValue();
    }



    protected BiomeGenBase setEnableSnow()
    {
        enableSnow = true;
        return this;
    }

    protected BiomeGenBase setBiomeName(String s)
    {
        biomeName = s;
        return this;
    }

    protected BiomeGenBase func_4124_a(int i)
    {
        field_6502_q = i;
        return this;
    }

    protected BiomeGenBase setColor(int i)
    {
        color = i;
        return this;
    }

    public static BiomeGenBase getBiomeFromLookup(double d, double d1)
    {
        int i = (int)(d * 63D);
        int j = (int)(d1 * 63D);
        return biomeLookupTable[i + j * 64];
    }

    public static BiomeGenBase getBiome(float f, float f1)
    {
        f1 *= f;
        if(f < 0.1F)
        {
            return tundra;
        }
        if(f1 < 0.2F)
        {
            if(f < 0.5F)
            {
                return tundra;
            }
            if(f < 0.95F)
            {
                return savanna;
            } else
            {
                return desert;
            }
        }
        if(f1 > 0.5F && f < 0.7F)
        {
            return swampland;
        }
        if(f < 0.5F)
        {
            return taiga;
        }
        if(f < 0.97F)
        {
            if(f1 < 0.35F)
            {
                return shrubland;
            } else
            {
                return forest;
            }
        }
        if(f1 < 0.45F)
        {
            return plains;
        }
        if(f1 < 0.9F)
        {
            return seasonalForest;
        } else
        {
            return rainforest;
        }
    }

    public int getSkyColorByTemp(float f)
    {
        f /= 3F;
        if(f < -1F)
        {
            f = -1F;
        }
        if(f > 1.0F)
        {
            f = 1.0F;
        }
        return java.awt.Color.getHSBColor(0.6222222F - f * 0.05F, 0.5F + f * 0.1F, 1.0F).getRGB();
    }



    public boolean getEnableSnow()
    {
        return enableSnow;
    }

    public boolean canSpawnLightningBolt()
    {
        if(enableSnow)
        {
            return false;
        } else
        {
            return enableRain;
        }
    }

    public static final BiomeGenBase rainforest = (new BiomeGenBase()).setColor(0x8fa36).setBiomeName("Rainforest").func_4124_a(0x1ff458);
    public static final BiomeGenBase swampland = (new BiomeGenBase()).setColor(0x7f9b2).setBiomeName("Swampland").func_4124_a(0x8baf48);
    public static final BiomeGenBase seasonalForest = (new BiomeGenBase()).setColor(0x9be023).setBiomeName("Seasonal Forest");
    public static final BiomeGenBase forest = (new BiomeGenBase()).setColor(0x56621).setBiomeName("Forest").func_4124_a(0x4eba31);
    public static final BiomeGenBase savanna = (new BiomeGenBase()).setColor(0xd9e023).setBiomeName("Savanna");
    public static final BiomeGenBase shrubland = (new BiomeGenBase()).setColor(0xa1ad20).setBiomeName("Shrubland");
    public static final BiomeGenBase taiga = (new BiomeGenBase()).setColor(0x2eb153).setBiomeName("Taiga").setEnableSnow().func_4124_a(0x7bb731);
    public static final BiomeGenBase desert = (new BiomeGenBase()).setColor(0xfa9418).setBiomeName("Desert").setDisableRain();
    public static final BiomeGenBase plains = (new BiomeGenBase()).setColor(0xffd910).setBiomeName("Plains");
    public static final BiomeGenBase iceDesert = (new BiomeGenBase()).setColor(0xffed93).setBiomeName("Ice Desert").setEnableSnow().setDisableRain().func_4124_a(0xc4d339);
    public static final BiomeGenBase tundra = (new BiomeGenBase()).setColor(0x57ebf9).setBiomeName("Tundra").setEnableSnow().func_4124_a(0xc4d339);
    public static final BiomeGenBase hell = (new BiomeGenBase()).setColor(0xff0000).setBiomeName("Hell").setDisableRain();
    public static final BiomeGenBase sky = (new BiomeGenBase()).setColor(0x8080ff).setBiomeName("Sky").setDisableRain();
    public String biomeName;
    public int color;
    public byte topBlock;
    public byte fillerBlock;
    public int field_6502_q;
    protected List spawnableMonsterList;
    protected List spawnableCreatureList;
    protected List spawnableWaterCreatureList;
    private boolean enableSnow;
    private boolean enableRain;
    private static BiomeGenBase biomeLookupTable[] = new BiomeGenBase[4096];

    static
    {
        generateBiomeLookup();
    }
}
