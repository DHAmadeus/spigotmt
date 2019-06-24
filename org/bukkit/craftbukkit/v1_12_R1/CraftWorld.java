// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.v1_12_R1;

import net.minecraft.server.v1_12_R1.WorldProvider;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.bukkit.Particle;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomSoundEffect;
import org.bukkit.SoundCategory;
import org.bukkit.Sound;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.WorldType;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.plugin.Plugin;
import net.minecraft.server.v1_12_R1.WorldNBTStorage;
import java.io.File;
import org.bukkit.ChunkSnapshot;
import net.minecraft.server.v1_12_R1.GroupDataEntity;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EnumDirection;
import net.minecraft.server.v1_12_R1.EntityEvokerFangs;
import org.bukkit.entity.EvokerFangs;
import net.minecraft.server.v1_12_R1.EntityAreaEffectCloud;
import org.bukkit.entity.AreaEffectCloud;
import net.minecraft.server.v1_12_R1.EntityFireworks;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Weather;
import net.minecraft.server.v1_12_R1.EntityExperienceOrb;
import org.bukkit.entity.ExperienceOrb;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityTNTPrimed;
import org.bukkit.entity.TNTPrimed;
import net.minecraft.server.v1_12_R1.EntityItemFrame;
import net.minecraft.server.v1_12_R1.EntityPainting;
import org.bukkit.entity.Painting;
import net.minecraft.server.v1_12_R1.EntityLeash;
import net.minecraft.server.v1_12_R1.EntityHanging;
import net.minecraft.server.v1_12_R1.BlockDiodeAbstract;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.ItemFrame;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import net.minecraft.server.v1_12_R1.EntityVindicator;
import org.bukkit.entity.Vindicator;
import net.minecraft.server.v1_12_R1.EntityIllagerIllusioner;
import org.bukkit.entity.Illusioner;
import net.minecraft.server.v1_12_R1.EntityEvoker;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Illager;
import net.minecraft.server.v1_12_R1.EntityVex;
import org.bukkit.entity.Vex;
import net.minecraft.server.v1_12_R1.EntityPolarBear;
import org.bukkit.entity.PolarBear;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import org.bukkit.entity.ArmorStand;
import net.minecraft.server.v1_12_R1.EntityGuardian;
import net.minecraft.server.v1_12_R1.EntityGuardianElder;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Guardian;
import net.minecraft.server.v1_12_R1.EntityEndermite;
import org.bukkit.entity.Endermite;
import net.minecraft.server.v1_12_R1.EntityRabbit;
import org.bukkit.entity.Rabbit;
import net.minecraft.server.v1_12_R1.EntityBat;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Ambient;
import net.minecraft.server.v1_12_R1.EntityEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.ComplexLivingEntity;
import net.minecraft.server.v1_12_R1.EntityWither;
import org.bukkit.entity.Wither;
import net.minecraft.server.v1_12_R1.EntityWitch;
import org.bukkit.entity.Witch;
import net.minecraft.server.v1_12_R1.EntityVillager;
import org.bukkit.entity.Villager;
import net.minecraft.server.v1_12_R1.EntityBlaze;
import org.bukkit.entity.Blaze;
import net.minecraft.server.v1_12_R1.EntityEnderman;
import org.bukkit.entity.Enderman;
import net.minecraft.server.v1_12_R1.EntitySilverfish;
import org.bukkit.entity.Silverfish;
import net.minecraft.server.v1_12_R1.EntityGiantZombie;
import org.bukkit.entity.Giant;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.EntityZombieVillager;
import org.bukkit.entity.ZombieVillager;
import net.minecraft.server.v1_12_R1.EntityZombieHusk;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Zombie;
import net.minecraft.server.v1_12_R1.EntityPigZombie;
import org.bukkit.entity.PigZombie;
import net.minecraft.server.v1_12_R1.EntityParrot;
import org.bukkit.entity.Parrot;
import net.minecraft.server.v1_12_R1.EntityOcelot;
import org.bukkit.entity.Ocelot;
import net.minecraft.server.v1_12_R1.EntityWolf;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Tameable;
import net.minecraft.server.v1_12_R1.EntitySquid;
import org.bukkit.entity.Squid;
import net.minecraft.server.v1_12_R1.EntitySpider;
import net.minecraft.server.v1_12_R1.EntityCaveSpider;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Spider;
import net.minecraft.server.v1_12_R1.EntitySlime;
import net.minecraft.server.v1_12_R1.EntityMagmaCube;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Slime;
import net.minecraft.server.v1_12_R1.EntitySkeleton;
import net.minecraft.server.v1_12_R1.EntitySkeletonWither;
import org.bukkit.entity.WitherSkeleton;
import net.minecraft.server.v1_12_R1.EntitySkeletonStray;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Skeleton;
import net.minecraft.server.v1_12_R1.EntityHorse;
import net.minecraft.server.v1_12_R1.EntityHorseZombie;
import org.bukkit.entity.ZombieHorse;
import net.minecraft.server.v1_12_R1.EntityHorseSkeleton;
import org.bukkit.entity.SkeletonHorse;
import net.minecraft.server.v1_12_R1.EntityLlama;
import org.bukkit.entity.Llama;
import net.minecraft.server.v1_12_R1.EntityHorseMule;
import org.bukkit.entity.Mule;
import net.minecraft.server.v1_12_R1.EntityHorseDonkey;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.AbstractHorse;
import net.minecraft.server.v1_12_R1.EntitySheep;
import org.bukkit.entity.Sheep;
import net.minecraft.server.v1_12_R1.EntityPig;
import org.bukkit.entity.Pig;
import net.minecraft.server.v1_12_R1.EntityGhast;
import org.bukkit.entity.Ghast;
import net.minecraft.server.v1_12_R1.EntityCreeper;
import org.bukkit.entity.Creeper;
import net.minecraft.server.v1_12_R1.EntityShulker;
import org.bukkit.entity.Shulker;
import net.minecraft.server.v1_12_R1.EntityIronGolem;
import org.bukkit.entity.IronGolem;
import net.minecraft.server.v1_12_R1.EntitySnowman;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Golem;
import net.minecraft.server.v1_12_R1.EntityCow;
import net.minecraft.server.v1_12_R1.EntityMushroomCow;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Cow;
import net.minecraft.server.v1_12_R1.EntityChicken;
import org.bukkit.entity.Chicken;
import net.minecraft.server.v1_12_R1.EntityEnderCrystal;
import org.bukkit.entity.EnderCrystal;
import net.minecraft.server.v1_12_R1.EntityEnderSignal;
import org.bukkit.entity.EnderSignal;
import net.minecraft.server.v1_12_R1.EntityMinecartRideable;
import net.minecraft.server.v1_12_R1.EntityMinecartCommandBlock;
import org.bukkit.entity.minecart.CommandMinecart;
import net.minecraft.server.v1_12_R1.EntityMinecartMobSpawner;
import org.bukkit.entity.minecart.SpawnerMinecart;
import net.minecraft.server.v1_12_R1.EntityMinecartHopper;
import org.bukkit.entity.minecart.HopperMinecart;
import net.minecraft.server.v1_12_R1.EntityMinecartTNT;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import net.minecraft.server.v1_12_R1.EntityMinecartChest;
import org.bukkit.entity.minecart.StorageMinecart;
import net.minecraft.server.v1_12_R1.EntityMinecartFurnace;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.Minecart;
import net.minecraft.server.v1_12_R1.EntityLlamaSpit;
import org.bukkit.entity.LlamaSpit;
import net.minecraft.server.v1_12_R1.EntityShulkerBullet;
import org.bukkit.entity.ShulkerBullet;
import net.minecraft.server.v1_12_R1.EntityFireball;
import net.minecraft.server.v1_12_R1.EntityLargeFireball;
import net.minecraft.server.v1_12_R1.EntityDragonFireball;
import org.bukkit.entity.DragonFireball;
import net.minecraft.server.v1_12_R1.EntityWitherSkull;
import org.bukkit.entity.WitherSkull;
import net.minecraft.server.v1_12_R1.EntitySmallFireball;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Fireball;
import net.minecraft.server.v1_12_R1.EntityPotion;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.ThrownPotion;
import net.minecraft.server.v1_12_R1.EntityEnderPearl;
import org.bukkit.entity.EnderPearl;
import net.minecraft.server.v1_12_R1.EntityThrownExpBottle;
import org.bukkit.entity.ThrownExpBottle;
import net.minecraft.server.v1_12_R1.EntityEgg;
import org.bukkit.entity.Egg;
import net.minecraft.server.v1_12_R1.EntitySnowball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Projectile;
import net.minecraft.server.v1_12_R1.EntityBoat;
import org.bukkit.entity.Boat;
import net.minecraft.server.v1_12_R1.EntityFallingBlock;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Consumer;
import org.bukkit.material.MaterialData;
import net.minecraft.server.v1_12_R1.EnumDifficulty;
import org.bukkit.Difficulty;
import net.minecraft.server.v1_12_R1.ExceptionWorldConflict;
import net.minecraft.server.v1_12_R1.IProgressUpdate;
import org.bukkit.entity.HumanEntity;
import net.minecraft.server.v1_12_R1.EntityHuman;
import com.google.common.base.Predicate;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import java.util.Collections;
import java.util.Collection;
import org.bukkit.entity.LivingEntity;
import net.minecraft.server.v1_12_R1.BiomeBase;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.block.Biome;
import net.minecraft.server.v1_12_R1.WorldProviderTheEnd;
import net.minecraft.server.v1_12_R1.WorldProviderHell;
import net.minecraft.server.v1_12_R1.WorldProviderNormal;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import net.minecraft.server.v1_12_R1.PacketPlayOutUpdateTime;
import java.util.UUID;
import net.minecraft.server.v1_12_R1.TileEntity;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockState;
import org.bukkit.block.BlockState;
import org.bukkit.BlockChangeDelegate;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.WorldGenerator;
import net.minecraft.server.v1_12_R1.BlockChorusFlower;
import net.minecraft.server.v1_12_R1.WorldGenMegaTree;
import net.minecraft.server.v1_12_R1.WorldGenForestTree;
import net.minecraft.server.v1_12_R1.WorldGenAcaciaTree;
import net.minecraft.server.v1_12_R1.WorldGenSwampTree;
import net.minecraft.server.v1_12_R1.WorldGenHugeMushroom;
import net.minecraft.server.v1_12_R1.WorldGenGroundBush;
import net.minecraft.server.v1_12_R1.WorldGenTrees;
import net.minecraft.server.v1_12_R1.WorldGenJungleTree;
import net.minecraft.server.v1_12_R1.BlockLeaves;
import net.minecraft.server.v1_12_R1.BlockLeaves1;
import net.minecraft.server.v1_12_R1.IBlockState;
import net.minecraft.server.v1_12_R1.BlockWood;
import net.minecraft.server.v1_12_R1.BlockLog1;
import net.minecraft.server.v1_12_R1.WorldGenTaiga1;
import net.minecraft.server.v1_12_R1.WorldGenTaiga2;
import net.minecraft.server.v1_12_R1.WorldGenForest;
import net.minecraft.server.v1_12_R1.WorldGenBigTree;
import org.bukkit.TreeType;
import org.bukkit.entity.EntityType;
import net.minecraft.server.v1_12_R1.EntityArrow;
import net.minecraft.server.v1_12_R1.EntitySpectralArrow;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionUtil;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import net.minecraft.server.v1_12_R1.EntityTippedArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.util.Vector;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem;
import org.bukkit.event.entity.CreatureSpawnEvent;
import net.minecraft.server.v1_12_R1.EntityItem;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.PlayerChunk;
import net.minecraft.server.v1_12_R1.IChunkProvider;
import net.minecraft.server.v1_12_R1.ChunkCoordIntPair;
import net.minecraft.server.v1_12_R1.MCUtil;
import org.spigotmc.AsyncCatcher;
import org.bukkit.event.Event;
import org.bukkit.event.world.SpawnChangeEvent;
import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLightningStrike;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityLightning;
import org.bukkit.entity.LightningStrike;
import java.util.Iterator;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Material;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.server.v1_12_R1.Chunk;
import net.minecraft.server.v1_12_R1.ChunkProviderServer;
import java.util.Random;
import org.bukkit.craftbukkit.v1_12_R1.metadata.BlockMetadataStore;
import org.bukkit.generator.BlockPopulator;
import java.util.List;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.WorldBorder;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.World;

public class CraftWorld implements World
{
    public static final int CUSTOM_DIMENSION_OFFSET = 10;
    private final WorldServer world;
    private WorldBorder worldBorder;
    private World.Environment environment;
    private final CraftServer server;
    private final ChunkGenerator generator;
    private final List<BlockPopulator> populators;
    private final BlockMetadataStore blockMetadata;
    private int monsterSpawn;
    private int animalSpawn;
    private int waterAnimalSpawn;
    private int ambientSpawn;
    private int chunkLoadCount;
    private int chunkGCTickCount;
    private static final Random rand;
    private final World.Spigot spigot;
    
    public int getEntityCount() {
        return this.world.entityList.size();
    }
    
    public int getTileEntityCount() {
        int size = 0;
        for (final Chunk chunk : ((ChunkProviderServer)this.world.getChunkProvider()).chunks.values()) {
            size += chunk.tileEntities.size();
        }
        return size;
    }
    
    public int getTickableTileEntityCount() {
        return this.world.tileEntityListTick.size();
    }
    
    public int getChunkCount() {
        return this.world.getChunkProviderServer().chunks.size();
    }
    
    public int getPlayerCount() {
        return this.world.players.size();
    }
    
    public CraftWorld(final WorldServer world, final ChunkGenerator gen, final World.Environment env) {
        this.server = (CraftServer)Bukkit.getServer();
        this.populators = new ArrayList<BlockPopulator>();
        this.blockMetadata = new BlockMetadataStore((World)this);
        this.monsterSpawn = -1;
        this.animalSpawn = -1;
        this.waterAnimalSpawn = -1;
        this.ambientSpawn = -1;
        this.chunkLoadCount = 0;
        this.spigot = new World.Spigot() {
            public void playEffect(final Location location, final Effect effect, final int id, final int data, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int particleCount, int radius) {
                Validate.notNull((Object)location, "Location cannot be null");
                Validate.notNull((Object)effect, "Effect cannot be null");
                Validate.notNull((Object)location.getWorld(), "World cannot be null");
                Packet packet;
                if (effect.getType() != Effect.Type.PARTICLE) {
                    final int packetData = effect.getId();
                    packet = (Packet)new PacketPlayOutWorldEvent(packetData, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), id, false);
                }
                else {
                    EnumParticle particle = null;
                    int[] extra = null;
                    final EnumParticle[] values = EnumParticle.values();
                    final int length = values.length;
                    int i = 0;
                    while (i < length) {
                        final EnumParticle p = values[i];
                        if (effect.getName().startsWith(p.b().replace("_", ""))) {
                            particle = p;
                            if (effect.getData() == null) {
                                break;
                            }
                            if (effect.getData().equals(Material.class)) {
                                extra = new int[] { id };
                                break;
                            }
                            extra = new int[] { data << 12 | (id & 0xFFF) };
                            break;
                        }
                        else {
                            ++i;
                        }
                    }
                    if (extra == null) {
                        extra = new int[0];
                    }
                    packet = (Packet)new PacketPlayOutWorldParticles(particle, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), offsetX, offsetY, offsetZ, speed, particleCount, extra);
                }
                radius *= radius;
                for (final Player player : CraftWorld.this.getPlayers()) {
                    if (((CraftPlayer)player).getHandle().playerConnection == null) {
                        continue;
                    }
                    if (!location.getWorld().equals(player.getWorld())) {
                        continue;
                    }
                    final int distance = (int)player.getLocation().distanceSquared(location);
                    if (distance > radius) {
                        continue;
                    }
                    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
                }
            }
            
            public void playEffect(final Location location, final Effect effect) {
                CraftWorld.this.playEffect(location, effect, 0);
            }
            
            public LightningStrike strikeLightning(final Location loc, final boolean isSilent) {
                final EntityLightning lightning = new EntityLightning((net.minecraft.server.v1_12_R1.World)CraftWorld.this.world, loc.getX(), loc.getY(), loc.getZ(), false, isSilent);
                CraftWorld.this.world.strikeLightning((Entity)lightning);
                return (LightningStrike)new CraftLightningStrike(CraftWorld.this.server, lightning);
            }
            
            public LightningStrike strikeLightningEffect(final Location loc, final boolean isSilent) {
                final EntityLightning lightning = new EntityLightning((net.minecraft.server.v1_12_R1.World)CraftWorld.this.world, loc.getX(), loc.getY(), loc.getZ(), true, isSilent);
                CraftWorld.this.world.strikeLightning((Entity)lightning);
                return (LightningStrike)new CraftLightningStrike(CraftWorld.this.server, lightning);
            }
        };
        this.world = world;
        this.generator = gen;
        this.environment = env;
        if (this.server.chunkGCPeriod > 0) {
            this.chunkGCTickCount = CraftWorld.rand.nextInt(this.server.chunkGCPeriod);
        }
    }
    
    public Block getBlockAt(final int x, final int y, final int z) {
        return this.getChunkAt(x >> 4, z >> 4).getBlock(x & 0xF, y, z & 0xF);
    }
    
    public int getBlockTypeIdAt(final int x, final int y, final int z) {
        return CraftMagicNumbers.getId(this.world.getType(new BlockPosition(x, y, z)).getBlock());
    }
    
    public int getHighestBlockYAt(final int x, final int z) {
        if (!this.isChunkLoaded(x >> 4, z >> 4)) {
            this.loadChunk(x >> 4, z >> 4);
        }
        return this.world.getHighestBlockYAt(new BlockPosition(x, 0, z)).getY();
    }
    
    public Location getSpawnLocation() {
        final BlockPosition spawn = this.world.getSpawn();
        return new Location((World)this, (double)spawn.getX(), (double)spawn.getY(), (double)spawn.getZ());
    }
    
    public boolean setSpawnLocation(final Location location) {
        Preconditions.checkArgument(location != null, (Object)"location");
        return this.equals(location.getWorld()) && this.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    public boolean setSpawnLocation(final int x, final int y, final int z) {
        try {
            final Location previousLocation = this.getSpawnLocation();
            this.world.worldData.setSpawn(new BlockPosition(x, y, z));
            final SpawnChangeEvent event = new SpawnChangeEvent((World)this, previousLocation);
            this.server.getPluginManager().callEvent((Event)event);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public void getChunkAtAsync(final int x, final int z, final World.ChunkLoadCallback callback) {
        final ChunkProviderServer cps = this.world.getChunkProviderServer();
        cps.getChunkAt(x, z, (Runnable)new Runnable() {
            @Override
            public void run() {
                callback.onLoad(cps.getChunkAt(x, z).bukkitChunk);
            }
        });
    }
    
    public void getChunkAtAsync(final Block block, final World.ChunkLoadCallback callback) {
        this.getChunkAtAsync(block.getX() >> 4, block.getZ() >> 4, callback);
    }
    
    public void getChunkAtAsync(final Location location, final World.ChunkLoadCallback callback) {
        this.getChunkAtAsync(location.getBlockX() >> 4, location.getBlockZ() >> 4, callback);
    }
    
    public org.bukkit.Chunk getChunkAt(final int x, final int z) {
        return this.world.getChunkProviderServer().getChunkAt(x, z).bukkitChunk;
    }
    
    public org.bukkit.Chunk getChunkAt(final Block block) {
        return this.getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }
    
    public boolean isChunkLoaded(final int x, final int z) {
        return this.world.getChunkProviderServer().isLoaded(x, z);
    }
    
    public org.bukkit.Chunk[] getLoadedChunks() {
        final Object[] chunks = this.world.getChunkProviderServer().chunks.values().toArray();
        final org.bukkit.Chunk[] craftChunks = (org.bukkit.Chunk[])new CraftChunk[chunks.length];
        for (int i = 0; i < chunks.length; ++i) {
            final Chunk chunk = (Chunk)chunks[i];
            craftChunks[i] = chunk.bukkitChunk;
        }
        return craftChunks;
    }
    
    public void loadChunk(final int x, final int z) {
        this.loadChunk(x, z, true);
    }
    
    public boolean unloadChunk(final org.bukkit.Chunk chunk) {
        return this.unloadChunk(chunk.getX(), chunk.getZ());
    }
    
    public boolean unloadChunk(final int x, final int z) {
        return this.unloadChunk(x, z, true);
    }
    
    public boolean unloadChunk(final int x, final int z, final boolean save) {
        return this.unloadChunk(x, z, save, false);
    }
    
    public boolean unloadChunkRequest(final int x, final int z) {
        return this.unloadChunkRequest(x, z, true);
    }
    
    public boolean unloadChunkRequest(final int x, final int z, final boolean safe) {
        AsyncCatcher.catchOp("chunk unload");
        if (safe && this.isChunkInUse(x, z)) {
            return false;
        }
        final Chunk chunk = this.world.getChunkProviderServer().getLoadedChunkAt(x, z);
        if (chunk != null) {
            this.world.getChunkProviderServer().unload(chunk);
        }
        return true;
    }
    
    public boolean unloadChunk(final int x, final int z, final boolean save, final boolean safe) {
        AsyncCatcher.catchOp("chunk unload");
        return !this.isChunkInUse(x, z) && this.unloadChunk0(x, z, save);
    }
    
    private boolean unloadChunk0(final int x, final int z, final boolean save) {
        final Chunk chunk;
        final Boolean result = (Boolean)MCUtil.ensureMain("Unload Chunk", () -> {
            chunk = this.world.getChunkProviderServer().getChunkIfLoaded(x, z);
            if (chunk == null) {
                return true;
            }
            else {
                return this.world.getChunkProviderServer().unloadChunk(chunk, chunk.mustSave || save);
            }
        });
        return result != null && result;
    }
    
    public boolean regenerateChunk(final int x, final int z) {
        if (!this.unloadChunk0(x, z, false)) {
            return false;
        }
        final long chunkKey = ChunkCoordIntPair.a(x, z);
        this.world.getChunkProviderServer().unloadQueue.remove(chunkKey);
        Chunk chunk = null;
        chunk = this.world.getChunkProviderServer().chunkGenerator.getOrCreateChunk(x, z);
        final PlayerChunk playerChunk = this.world.getPlayerChunkMap().getChunk(x, z);
        if (playerChunk != null) {
            playerChunk.chunk = chunk;
        }
        if (chunk != null) {
            this.world.getChunkProviderServer().chunks.put(chunkKey, (Object)chunk);
            chunk.addEntities();
            chunk.loadNearby((IChunkProvider)this.world.getChunkProviderServer(), this.world.getChunkProviderServer().chunkGenerator, true);
            this.refreshChunk(x, z);
        }
        return chunk != null;
    }
    
    public boolean refreshChunk(final int x, final int z) {
        if (!this.isChunkLoaded(x, z)) {
            return false;
        }
        final int px = x << 4;
        final int pz = z << 4;
        final int height = this.getMaxHeight() / 16;
        for (int idx = 0; idx < 64; ++idx) {
            this.world.notify(new BlockPosition(px + idx / height, idx % height * 16, pz), Blocks.AIR.getBlockData(), Blocks.STONE.getBlockData(), 3);
        }
        this.world.notify(new BlockPosition(px + 15, height * 16 - 1, pz + 15), Blocks.AIR.getBlockData(), Blocks.STONE.getBlockData(), 3);
        return true;
    }
    
    public boolean isChunkInUse(final int x, final int z) {
        return this.world.getPlayerChunkMap().isChunkInUse(x, z);
    }
    
    public boolean loadChunk(final int x, final int z, final boolean generate) {
        AsyncCatcher.catchOp("chunk load");
        ++this.chunkLoadCount;
        if (generate) {
            return this.world.getChunkProviderServer().getChunkAt(x, z) != null;
        }
        return this.world.getChunkProviderServer().getOrLoadChunkAt(x, z) != null;
    }
    
    public boolean isChunkLoaded(final org.bukkit.Chunk chunk) {
        return this.isChunkLoaded(chunk.getX(), chunk.getZ());
    }
    
    public void loadChunk(final org.bukkit.Chunk chunk) {
        this.loadChunk(chunk.getX(), chunk.getZ());
        ((CraftChunk)this.getChunkAt(chunk.getX(), chunk.getZ())).getHandle().bukkitChunk = chunk;
    }
    
    public WorldServer getHandle() {
        return this.world;
    }
    
    public Item dropItem(final Location loc, final ItemStack item) {
        Validate.notNull((Object)item, "Cannot drop a Null item.");
        Validate.isTrue(item.getTypeId() != 0, "Cannot drop AIR.");
        final EntityItem entity = new EntityItem((net.minecraft.server.v1_12_R1.World)this.world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        entity.pickupDelay = 10;
        this.world.addEntity((Entity)entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (Item)new CraftItem(this.world.getServer(), entity);
    }
    
    private static void randomLocationWithinBlock(final Location loc, final double xs, final double ys, final double zs) {
        final double prevX = loc.getX();
        final double prevY = loc.getY();
        final double prevZ = loc.getZ();
        loc.add(xs, ys, zs);
        if (loc.getX() < Math.floor(prevX)) {
            loc.setX(Math.floor(prevX));
        }
        if (loc.getX() >= Math.ceil(prevX)) {
            loc.setX(Math.ceil(prevX - 0.01));
        }
        if (loc.getY() < Math.floor(prevY)) {
            loc.setY(Math.floor(prevY));
        }
        if (loc.getY() >= Math.ceil(prevY)) {
            loc.setY(Math.ceil(prevY - 0.01));
        }
        if (loc.getZ() < Math.floor(prevZ)) {
            loc.setZ(Math.floor(prevZ));
        }
        if (loc.getZ() >= Math.ceil(prevZ)) {
            loc.setZ(Math.ceil(prevZ - 0.01));
        }
    }
    
    public Item dropItemNaturally(Location loc, final ItemStack item) {
        final double xs = this.world.random.nextFloat() * 0.7f - 0.35;
        final double ys = this.world.random.nextFloat() * 0.7f - 0.35;
        final double zs = this.world.random.nextFloat() * 0.7f - 0.35;
        loc = loc.clone();
        randomLocationWithinBlock(loc, xs, ys, zs);
        return this.dropItem(loc, item);
    }
    
    public Arrow spawnArrow(final Location loc, final Vector velocity, final float speed, final float spread) {
        return this.spawnArrow(loc, velocity, speed, spread, Arrow.class);
    }
    
    public <T extends Arrow> T spawnArrow(final Location loc, final Vector velocity, final float speed, final float spread, final Class<T> clazz) {
        Validate.notNull((Object)loc, "Can not spawn arrow with a null location");
        Validate.notNull((Object)velocity, "Can not spawn arrow with a null velocity");
        Validate.notNull((Object)clazz, "Can not spawn an arrow with no class");
        EntityArrow arrow;
        if (TippedArrow.class.isAssignableFrom(clazz)) {
            arrow = (EntityArrow)new EntityTippedArrow((net.minecraft.server.v1_12_R1.World)this.world);
            ((EntityTippedArrow)arrow).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
        }
        else if (SpectralArrow.class.isAssignableFrom(clazz)) {
            arrow = (EntityArrow)new EntitySpectralArrow((net.minecraft.server.v1_12_R1.World)this.world);
        }
        else {
            arrow = (EntityArrow)new EntityTippedArrow((net.minecraft.server.v1_12_R1.World)this.world);
        }
        arrow.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.shoot(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        this.world.addEntity((Entity)arrow);
        return (T)arrow.getBukkitEntity();
    }
    
    public org.bukkit.entity.Entity spawnEntity(final Location loc, final EntityType entityType) {
        return this.spawn(loc, (Class<org.bukkit.entity.Entity>)entityType.getEntityClass());
    }
    
    public LightningStrike strikeLightning(final Location loc) {
        final EntityLightning lightning = new EntityLightning((net.minecraft.server.v1_12_R1.World)this.world, loc.getX(), loc.getY(), loc.getZ(), false);
        this.world.strikeLightning((Entity)lightning);
        return (LightningStrike)new CraftLightningStrike(this.server, lightning);
    }
    
    public LightningStrike strikeLightningEffect(final Location loc) {
        final EntityLightning lightning = new EntityLightning((net.minecraft.server.v1_12_R1.World)this.world, loc.getX(), loc.getY(), loc.getZ(), true);
        this.world.strikeLightning((Entity)lightning);
        return (LightningStrike)new CraftLightningStrike(this.server, lightning);
    }
    
    public boolean generateTree(final Location loc, final TreeType type) {
        final BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        WorldGenerator gen = null;
        switch (type) {
            case BIG_TREE: {
                gen = (WorldGenerator)new WorldGenBigTree(true);
                break;
            }
            case BIRCH: {
                gen = (WorldGenerator)new WorldGenForest(true, false);
                break;
            }
            case REDWOOD: {
                gen = (WorldGenerator)new WorldGenTaiga2(true);
                break;
            }
            case TALL_REDWOOD: {
                gen = (WorldGenerator)new WorldGenTaiga1();
                break;
            }
            case JUNGLE: {
                final IBlockData iblockdata1 = Blocks.LOG.getBlockData().set((IBlockState)BlockLog1.VARIANT, (Comparable)BlockWood.EnumLogVariant.JUNGLE);
                final IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set((IBlockState)BlockLeaves1.VARIANT, (Comparable)BlockWood.EnumLogVariant.JUNGLE).set((IBlockState)BlockLeaves.CHECK_DECAY, (Comparable)false);
                gen = (WorldGenerator)new WorldGenJungleTree(true, 10, 20, iblockdata1, iblockdata2);
                break;
            }
            case SMALL_JUNGLE: {
                final IBlockData iblockdata1 = Blocks.LOG.getBlockData().set((IBlockState)BlockLog1.VARIANT, (Comparable)BlockWood.EnumLogVariant.JUNGLE);
                final IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set((IBlockState)BlockLeaves1.VARIANT, (Comparable)BlockWood.EnumLogVariant.JUNGLE).set((IBlockState)BlockLeaves.CHECK_DECAY, (Comparable)false);
                gen = (WorldGenerator)new WorldGenTrees(true, 4 + CraftWorld.rand.nextInt(7), iblockdata1, iblockdata2, false);
                break;
            }
            case COCOA_TREE: {
                final IBlockData iblockdata1 = Blocks.LOG.getBlockData().set((IBlockState)BlockLog1.VARIANT, (Comparable)BlockWood.EnumLogVariant.JUNGLE);
                final IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set((IBlockState)BlockLeaves1.VARIANT, (Comparable)BlockWood.EnumLogVariant.JUNGLE).set((IBlockState)BlockLeaves.CHECK_DECAY, (Comparable)false);
                gen = (WorldGenerator)new WorldGenTrees(true, 4 + CraftWorld.rand.nextInt(7), iblockdata1, iblockdata2, true);
                break;
            }
            case JUNGLE_BUSH: {
                final IBlockData iblockdata1 = Blocks.LOG.getBlockData().set((IBlockState)BlockLog1.VARIANT, (Comparable)BlockWood.EnumLogVariant.JUNGLE);
                final IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set((IBlockState)BlockLeaves1.VARIANT, (Comparable)BlockWood.EnumLogVariant.OAK).set((IBlockState)BlockLeaves.CHECK_DECAY, (Comparable)false);
                gen = (WorldGenerator)new WorldGenGroundBush(iblockdata1, iblockdata2);
                break;
            }
            case RED_MUSHROOM: {
                gen = (WorldGenerator)new WorldGenHugeMushroom(Blocks.RED_MUSHROOM_BLOCK);
                break;
            }
            case BROWN_MUSHROOM: {
                gen = (WorldGenerator)new WorldGenHugeMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
                break;
            }
            case SWAMP: {
                gen = (WorldGenerator)new WorldGenSwampTree();
                break;
            }
            case ACACIA: {
                gen = (WorldGenerator)new WorldGenAcaciaTree(true);
                break;
            }
            case DARK_OAK: {
                gen = (WorldGenerator)new WorldGenForestTree(true);
                break;
            }
            case MEGA_REDWOOD: {
                gen = (WorldGenerator)new WorldGenMegaTree(false, CraftWorld.rand.nextBoolean());
                break;
            }
            case TALL_BIRCH: {
                gen = (WorldGenerator)new WorldGenForest(true, true);
                break;
            }
            case CHORUS_PLANT: {
                BlockChorusFlower.a((net.minecraft.server.v1_12_R1.World)this.world, pos, CraftWorld.rand, 8);
                return true;
            }
            default: {
                gen = (WorldGenerator)new WorldGenTrees(true);
                break;
            }
        }
        return gen.generate((net.minecraft.server.v1_12_R1.World)this.world, CraftWorld.rand, pos);
    }
    
    public boolean generateTree(final Location loc, final TreeType type, final BlockChangeDelegate delegate) {
        this.world.captureTreeGeneration = true;
        this.world.captureBlockStates = true;
        final boolean grownTree = this.generateTree(loc, type);
        this.world.captureBlockStates = false;
        this.world.captureTreeGeneration = false;
        if (grownTree) {
            for (final BlockState blockstate : this.world.capturedBlockStates) {
                final int x = blockstate.getX();
                final int y = blockstate.getY();
                final int z = blockstate.getZ();
                final BlockPosition position = new BlockPosition(x, y, z);
                final IBlockData oldBlock = this.world.getType(position);
                final int typeId = blockstate.getTypeId();
                final int data = blockstate.getRawData();
                final int flag = ((CraftBlockState)blockstate).getFlag();
                delegate.setTypeIdAndData(x, y, z, typeId, data);
                final IBlockData newBlock = this.world.getType(position);
                this.world.notifyAndUpdatePhysics(position, (Chunk)null, oldBlock, newBlock, flag);
            }
            this.world.capturedBlockStates.clear();
            return true;
        }
        this.world.capturedBlockStates.clear();
        return false;
    }
    
    public TileEntity getTileEntityAt(final int x, final int y, final int z) {
        return this.world.getTileEntity(new BlockPosition(x, y, z));
    }
    
    public String getName() {
        return this.world.worldData.getName();
    }
    
    @Deprecated
    public long getId() {
        return this.world.worldData.getSeed();
    }
    
    public UUID getUID() {
        return this.world.getDataManager().getUUID();
    }
    
    @Override
    public String toString() {
        return "CraftWorld{name=" + this.getName() + '}';
    }
    
    public long getTime() {
        long time = this.getFullTime() % 24000L;
        if (time < 0L) {
            time += 24000L;
        }
        return time;
    }
    
    public void setTime(final long time) {
        long margin = (time - this.getFullTime()) % 24000L;
        if (margin < 0L) {
            margin += 24000L;
        }
        this.setFullTime(this.getFullTime() + margin);
    }
    
    public long getFullTime() {
        return this.world.getDayTime();
    }
    
    public void setFullTime(final long time) {
        this.world.setDayTime(time);
        for (final Player p : this.getPlayers()) {
            final CraftPlayer cp = (CraftPlayer)p;
            if (cp.getHandle().playerConnection == null) {
                continue;
            }
            cp.getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutUpdateTime(cp.getHandle().world.getTime(), cp.getHandle().getPlayerTime(), cp.getHandle().world.getGameRules().getBoolean("doDaylightCycle")));
        }
    }
    
    public boolean createExplosion(final double x, final double y, final double z, final float power) {
        return this.createExplosion(x, y, z, power, false, true);
    }
    
    public boolean createExplosion(final double x, final double y, final double z, final float power, final boolean setFire) {
        return this.createExplosion(x, y, z, power, setFire, true);
    }
    
    public boolean createExplosion(final double x, final double y, final double z, final float power, final boolean setFire, final boolean breakBlocks) {
        return !this.world.createExplosion((Entity)null, x, y, z, power, setFire, breakBlocks).wasCanceled;
    }
    
    public boolean createExplosion(final org.bukkit.entity.Entity source, final Location loc, final float power, final boolean setFire, final boolean breakBlocks) {
        return !this.world.createExplosion((source != null) ? ((CraftEntity)source).getHandle() : null, loc.getX(), loc.getY(), loc.getZ(), power, setFire, breakBlocks).wasCanceled;
    }
    
    public boolean createExplosion(final Location loc, final float power) {
        return this.createExplosion(loc, power, false);
    }
    
    public boolean createExplosion(final Location loc, final float power, final boolean setFire) {
        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
    }
    
    public World.Environment getEnvironment() {
        return this.environment;
    }
    
    public void setEnvironment(final World.Environment env) {
        if (this.environment != env) {
            this.environment = env;
            switch (env) {
                case NORMAL: {
                    this.world.worldProvider = (WorldProvider)new WorldProviderNormal();
                    break;
                }
                case NETHER: {
                    this.world.worldProvider = (WorldProvider)new WorldProviderHell();
                    break;
                }
                case THE_END: {
                    this.world.worldProvider = (WorldProvider)new WorldProviderTheEnd();
                    break;
                }
            }
        }
    }
    
    public Block getBlockAt(final Location location) {
        return this.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    public int getBlockTypeIdAt(final Location location) {
        return this.getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    public int getHighestBlockYAt(final Location location) {
        return this.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }
    
    public org.bukkit.Chunk getChunkAt(final Location location) {
        return this.getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }
    
    public boolean isChunkGenerated(final int x, final int z) {
        return this.getHandle().getChunkProviderServer().isChunkGenerated(x, z);
    }
    
    public ChunkGenerator getGenerator() {
        return this.generator;
    }
    
    public List<BlockPopulator> getPopulators() {
        return this.populators;
    }
    
    public Block getHighestBlockAt(final int x, final int z) {
        return this.getBlockAt(x, this.getHighestBlockYAt(x, z), z);
    }
    
    public Block getHighestBlockAt(final Location location) {
        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }
    
    public Biome getBiome(final int x, final int z) {
        return CraftBlock.biomeBaseToBiome(this.world.getBiome(new BlockPosition(x, 0, z)));
    }
    
    public void setBiome(final int x, final int z, final Biome bio) {
        final BiomeBase bb = CraftBlock.biomeToBiomeBase(bio);
        if (this.world.isLoaded(new BlockPosition(x, 0, z))) {
            final Chunk chunk = this.world.getChunkAtWorldCoords(new BlockPosition(x, 0, z));
            if (chunk != null) {
                final byte[] biomevals = chunk.getBiomeIndex();
                biomevals[(z & 0xF) << 4 | (x & 0xF)] = (byte)BiomeBase.REGISTRY_ID.a((Object)bb);
                chunk.markDirty();
            }
        }
    }
    
    public double getTemperature(final int x, final int z) {
        return this.world.getBiome(new BlockPosition(x, 0, z)).getTemperature();
    }
    
    public double getHumidity(final int x, final int z) {
        return this.world.getBiome(new BlockPosition(x, 0, z)).getHumidity();
    }
    
    public List<org.bukkit.entity.Entity> getEntities() {
        final List<org.bukkit.entity.Entity> list = new ArrayList<org.bukkit.entity.Entity>();
        for (final Object o : this.world.entityList) {
            if (o instanceof Entity) {
                final Entity mcEnt = (Entity)o;
                if (mcEnt.shouldBeRemoved) {
                    continue;
                }
                final org.bukkit.entity.Entity bukkitEntity = (org.bukkit.entity.Entity)mcEnt.getBukkitEntity();
                if (bukkitEntity == null) {
                    continue;
                }
                list.add(bukkitEntity);
            }
        }
        return list;
    }
    
    public List<LivingEntity> getLivingEntities() {
        final List<LivingEntity> list = new ArrayList<LivingEntity>();
        for (final Object o : this.world.entityList) {
            if (o instanceof Entity) {
                final Entity mcEnt = (Entity)o;
                if (mcEnt.shouldBeRemoved) {
                    continue;
                }
                final org.bukkit.entity.Entity bukkitEntity = (org.bukkit.entity.Entity)mcEnt.getBukkitEntity();
                if (bukkitEntity == null || !(bukkitEntity instanceof LivingEntity)) {
                    continue;
                }
                list.add((LivingEntity)bukkitEntity);
            }
        }
        return list;
    }
    
    @Deprecated
    public <T extends org.bukkit.entity.Entity> Collection<T> getEntitiesByClass(final Class<T>... classes) {
        return (Collection<T>)this.getEntitiesByClasses((Class<?>[])classes);
    }
    
    public <T extends org.bukkit.entity.Entity> Collection<T> getEntitiesByClass(final Class<T> clazz) {
        final Collection<T> list = new ArrayList<T>();
        for (final Object entity : this.world.entityList) {
            if (entity instanceof Entity) {
                if (((Entity)entity).shouldBeRemoved) {
                    continue;
                }
                final org.bukkit.entity.Entity bukkitEntity = (org.bukkit.entity.Entity)((Entity)entity).getBukkitEntity();
                if (bukkitEntity == null) {
                    continue;
                }
                final Class<?> bukkitClass = bukkitEntity.getClass();
                if (!clazz.isAssignableFrom(bukkitClass)) {
                    continue;
                }
                list.add((T)bukkitEntity);
            }
        }
        return list;
    }
    
    public Collection<org.bukkit.entity.Entity> getEntitiesByClasses(final Class<?>... classes) {
        final Collection<org.bukkit.entity.Entity> list = new ArrayList<org.bukkit.entity.Entity>();
        for (final Object entity : this.world.entityList) {
            if (entity instanceof Entity) {
                if (((Entity)entity).shouldBeRemoved) {
                    continue;
                }
                final org.bukkit.entity.Entity bukkitEntity = (org.bukkit.entity.Entity)((Entity)entity).getBukkitEntity();
                if (bukkitEntity == null) {
                    continue;
                }
                final Class<?> bukkitClass = bukkitEntity.getClass();
                for (final Class<?> clazz : classes) {
                    if (clazz.isAssignableFrom(bukkitClass)) {
                        list.add(bukkitEntity);
                        break;
                    }
                }
            }
        }
        return list;
    }
    
    public Collection<org.bukkit.entity.Entity> getNearbyEntities(final Location location, final double x, final double y, final double z) {
        if (location == null || !location.getWorld().equals(this)) {
            return (Collection<org.bukkit.entity.Entity>)Collections.emptyList();
        }
        final AxisAlignedBB bb = new AxisAlignedBB(location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z);
        final List<Entity> entityList = (List<Entity>)this.getHandle().getEntities((Entity)null, bb, (Predicate)null);
        final List<org.bukkit.entity.Entity> bukkitEntityList = new ArrayList<org.bukkit.entity.Entity>(entityList.size());
        for (final Object entity : entityList) {
            bukkitEntityList.add((org.bukkit.entity.Entity)((Entity)entity).getBukkitEntity());
        }
        return bukkitEntityList;
    }
    
    public List<Player> getPlayers() {
        final List<Player> list = new ArrayList<Player>(this.world.players.size());
        for (final EntityHuman human : this.world.players) {
            final HumanEntity bukkitEntity = (HumanEntity)human.getBukkitEntity();
            if (bukkitEntity != null && bukkitEntity instanceof Player) {
                list.add((Player)bukkitEntity);
            }
        }
        return list;
    }
    
    public org.bukkit.entity.Entity getEntity(final UUID uuid) {
        Validate.notNull((Object)uuid, "UUID cannot be null");
        final Entity entity = this.world.getEntity(uuid);
        return (org.bukkit.entity.Entity)((entity == null) ? null : entity.getBukkitEntity());
    }
    
    public void save() {
        this.save(true);
    }
    
    public void save(final boolean forceSave) {
        this.server.checkSaveState();
        try {
            final boolean oldSave = this.world.savingDisabled;
            this.world.savingDisabled = false;
            this.world.save(forceSave, (IProgressUpdate)null);
            this.world.savingDisabled = oldSave;
        }
        catch (ExceptionWorldConflict ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isAutoSave() {
        return !this.world.savingDisabled;
    }
    
    public void setAutoSave(final boolean value) {
        this.world.savingDisabled = !value;
    }
    
    public void setDifficulty(final Difficulty difficulty) {
        this.getHandle().worldData.setDifficulty(EnumDifficulty.getById(difficulty.getValue()));
    }
    
    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }
    
    public BlockMetadataStore getBlockMetadata() {
        return this.blockMetadata;
    }
    
    public boolean hasStorm() {
        return this.world.worldData.hasStorm();
    }
    
    public void setStorm(final boolean hasStorm) {
        this.world.worldData.setStorm(hasStorm);
        this.setWeatherDuration(0);
    }
    
    public int getWeatherDuration() {
        return this.world.worldData.getWeatherDuration();
    }
    
    public void setWeatherDuration(final int duration) {
        this.world.worldData.setWeatherDuration(duration);
    }
    
    public boolean isThundering() {
        return this.world.worldData.isThundering();
    }
    
    public void setThundering(final boolean thundering) {
        this.world.worldData.setThundering(thundering);
        this.setThunderDuration(0);
    }
    
    public int getThunderDuration() {
        return this.world.worldData.getThunderDuration();
    }
    
    public void setThunderDuration(final int duration) {
        this.world.worldData.setThunderDuration(duration);
    }
    
    public long getSeed() {
        return this.world.worldData.getSeed();
    }
    
    public boolean getPVP() {
        return this.world.pvpMode;
    }
    
    public void setPVP(final boolean pvp) {
        this.world.pvpMode = pvp;
    }
    
    public void playEffect(final Player player, final Effect effect, final int data) {
        this.playEffect(player.getLocation(), effect, data, 0);
    }
    
    public void playEffect(final Location location, final Effect effect, final int data) {
        this.playEffect(location, effect, data, 64);
    }
    
    public <T> void playEffect(final Location loc, final Effect effect, final T data) {
        this.playEffect(loc, effect, data, 64);
    }
    
    public <T> void playEffect(final Location loc, final Effect effect, final T data, final int radius) {
        if (data != null) {
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass()), "Wrong kind of data for this effect!");
        }
        else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }
        if (data != null && data.getClass().equals(MaterialData.class)) {
            final MaterialData materialData = (MaterialData)data;
            Validate.isTrue(materialData.getItemType().isBlock(), "Material must be block");
            this.spigot().playEffect(loc, effect, materialData.getItemType().getId(), (int)materialData.getData(), 0.0f, 0.0f, 0.0f, 1.0f, 1, radius);
        }
        else {
            final int dataValue = (data == null) ? 0 : CraftEffect.getDataValue(effect, (Object)data);
            this.playEffect(loc, effect, dataValue, radius);
        }
    }
    
    public void playEffect(final Location location, final Effect effect, final int data, final int radius) {
        this.spigot().playEffect(location, effect, data, 0, 0.0f, 0.0f, 0.0f, 1.0f, 1, radius);
    }
    
    public <T extends org.bukkit.entity.Entity> T spawn(final Location location, final Class<T> clazz) throws IllegalArgumentException {
        return this.spawn(location, clazz, null, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
    
    public <T extends org.bukkit.entity.Entity> T spawn(final Location location, final Class<T> clazz, final Consumer<T> function) throws IllegalArgumentException {
        return this.spawn(location, clazz, function, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
    
    public FallingBlock spawnFallingBlock(final Location location, final MaterialData data) throws IllegalArgumentException {
        Validate.notNull((Object)data, "MaterialData cannot be null");
        return this.spawnFallingBlock(location, data.getItemType(), data.getData());
    }
    
    public FallingBlock spawnFallingBlock(final Location location, final Material material, final byte data) throws IllegalArgumentException {
        Validate.notNull((Object)location, "Location cannot be null");
        Validate.notNull((Object)material, "Material cannot be null");
        Validate.isTrue(material.isBlock(), "Material must be a block");
        final EntityFallingBlock entity = new EntityFallingBlock((net.minecraft.server.v1_12_R1.World)this.world, location.getX(), location.getY(), location.getZ(), CraftMagicNumbers.getBlock(material).fromLegacyData((int)data));
        entity.ticksLived = 1;
        this.world.addEntity((Entity)entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (FallingBlock)entity.getBukkitEntity();
    }
    
    public FallingBlock spawnFallingBlock(final Location location, final int blockId, final byte blockData) throws IllegalArgumentException {
        return this.spawnFallingBlock(location, Material.getMaterial(blockId), blockData);
    }
    
    public Entity createEntity(final Location location, final Class<? extends org.bukkit.entity.Entity> clazz) throws IllegalArgumentException {
        if (location == null || clazz == null) {
            throw new IllegalArgumentException("Location or entity class cannot be null");
        }
        Entity entity = null;
        final double x = location.getX();
        final double y = location.getY();
        final double z = location.getZ();
        final float pitch = location.getPitch();
        final float yaw = location.getYaw();
        if (Boat.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityBoat((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            entity.setPositionRotation(x, y, z, yaw, pitch);
        }
        else if (Item.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityItem((net.minecraft.server.v1_12_R1.World)this.world, x, y, z, new net.minecraft.server.v1_12_R1.ItemStack(net.minecraft.server.v1_12_R1.Item.getItemOf(Blocks.DIRT)));
        }
        else if (FallingBlock.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityFallingBlock((net.minecraft.server.v1_12_R1.World)this.world, x, y, z, this.world.getType(new BlockPosition(x, y, z)));
        }
        else if (Projectile.class.isAssignableFrom(clazz)) {
            if (Snowball.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntitySnowball((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            }
            else if (Egg.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityEgg((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            }
            else if (Arrow.class.isAssignableFrom(clazz)) {
                if (TippedArrow.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityTippedArrow((net.minecraft.server.v1_12_R1.World)this.world);
                    ((EntityTippedArrow)entity).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
                }
                else if (SpectralArrow.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntitySpectralArrow((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else {
                    entity = (Entity)new EntityTippedArrow((net.minecraft.server.v1_12_R1.World)this.world);
                }
                entity.setPositionRotation(x, y, z, 0.0f, 0.0f);
            }
            else if (ThrownExpBottle.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityThrownExpBottle((net.minecraft.server.v1_12_R1.World)this.world);
                entity.setPositionRotation(x, y, z, 0.0f, 0.0f);
            }
            else if (EnderPearl.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityEnderPearl((net.minecraft.server.v1_12_R1.World)this.world);
                entity.setPositionRotation(x, y, z, 0.0f, 0.0f);
            }
            else if (ThrownPotion.class.isAssignableFrom(clazz)) {
                if (LingeringPotion.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityPotion((net.minecraft.server.v1_12_R1.World)this.world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(Material.LINGERING_POTION, 1)));
                }
                else {
                    entity = (Entity)new EntityPotion((net.minecraft.server.v1_12_R1.World)this.world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
                }
            }
            else if (Fireball.class.isAssignableFrom(clazz)) {
                if (SmallFireball.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntitySmallFireball((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (WitherSkull.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityWitherSkull((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (DragonFireball.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityDragonFireball((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else {
                    entity = (Entity)new EntityLargeFireball((net.minecraft.server.v1_12_R1.World)this.world);
                }
                entity.setPositionRotation(x, y, z, yaw, pitch);
                final Vector direction = location.getDirection().multiply(10);
                ((EntityFireball)entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
            }
            else if (ShulkerBullet.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityShulkerBullet((net.minecraft.server.v1_12_R1.World)this.world);
                entity.setPositionRotation(x, y, z, yaw, pitch);
            }
            else if (LlamaSpit.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityLlamaSpit((net.minecraft.server.v1_12_R1.World)this.world);
                entity.setPositionRotation(x, y, z, yaw, pitch);
            }
        }
        else if (Minecart.class.isAssignableFrom(clazz)) {
            if (PoweredMinecart.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityMinecartFurnace((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            }
            else if (StorageMinecart.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityMinecartChest((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            }
            else if (ExplosiveMinecart.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityMinecartTNT((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            }
            else if (HopperMinecart.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityMinecartHopper((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            }
            else if (SpawnerMinecart.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityMinecartMobSpawner((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            }
            else if (CommandMinecart.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityMinecartCommandBlock((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            }
            else {
                entity = (Entity)new EntityMinecartRideable((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
            }
        }
        else if (EnderSignal.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityEnderSignal((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
        }
        else if (EnderCrystal.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityEnderCrystal((net.minecraft.server.v1_12_R1.World)this.world);
            entity.setPositionRotation(x, y, z, 0.0f, 0.0f);
        }
        else if (LivingEntity.class.isAssignableFrom(clazz)) {
            if (Chicken.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityChicken((net.minecraft.server.v1_12_R1.World)this.world);
            }
            else if (Cow.class.isAssignableFrom(clazz)) {
                if (MushroomCow.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityMushroomCow((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else {
                    entity = (Entity)new EntityCow((net.minecraft.server.v1_12_R1.World)this.world);
                }
            }
            else if (Golem.class.isAssignableFrom(clazz)) {
                if (Snowman.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntitySnowman((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (IronGolem.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityIronGolem((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Shulker.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityShulker((net.minecraft.server.v1_12_R1.World)this.world);
                }
            }
            else if (Creeper.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityCreeper((net.minecraft.server.v1_12_R1.World)this.world);
            }
            else if (Ghast.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityGhast((net.minecraft.server.v1_12_R1.World)this.world);
            }
            else if (Pig.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityPig((net.minecraft.server.v1_12_R1.World)this.world);
            }
            else if (!Player.class.isAssignableFrom(clazz)) {
                if (Sheep.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntitySheep((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (AbstractHorse.class.isAssignableFrom(clazz)) {
                    if (ChestedHorse.class.isAssignableFrom(clazz)) {
                        if (Donkey.class.isAssignableFrom(clazz)) {
                            entity = (Entity)new EntityHorseDonkey((net.minecraft.server.v1_12_R1.World)this.world);
                        }
                        else if (Mule.class.isAssignableFrom(clazz)) {
                            entity = (Entity)new EntityHorseMule((net.minecraft.server.v1_12_R1.World)this.world);
                        }
                        else if (Llama.class.isAssignableFrom(clazz)) {
                            entity = (Entity)new EntityLlama((net.minecraft.server.v1_12_R1.World)this.world);
                        }
                    }
                    else if (SkeletonHorse.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityHorseSkeleton((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else if (ZombieHorse.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityHorseZombie((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else {
                        entity = (Entity)new EntityHorse((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
                else if (Skeleton.class.isAssignableFrom(clazz)) {
                    if (Stray.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntitySkeletonStray((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else if (WitherSkeleton.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntitySkeletonWither((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else {
                        entity = (Entity)new EntitySkeleton((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
                else if (Slime.class.isAssignableFrom(clazz)) {
                    if (MagmaCube.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityMagmaCube((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else {
                        entity = (Entity)new EntitySlime((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
                else if (Spider.class.isAssignableFrom(clazz)) {
                    if (CaveSpider.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityCaveSpider((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else {
                        entity = (Entity)new EntitySpider((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
                else if (Squid.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntitySquid((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Tameable.class.isAssignableFrom(clazz)) {
                    if (Wolf.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityWolf((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else if (Ocelot.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityOcelot((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else if (Parrot.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityParrot((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
                else if (PigZombie.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityPigZombie((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Zombie.class.isAssignableFrom(clazz)) {
                    if (Husk.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityZombieHusk((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else if (ZombieVillager.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityZombieVillager((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else {
                        entity = (Entity)new EntityZombie((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
                else if (Giant.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityGiantZombie((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Silverfish.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntitySilverfish((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Enderman.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityEnderman((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Blaze.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityBlaze((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Villager.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityVillager((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Witch.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityWitch((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Wither.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityWither((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                    if (EnderDragon.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityEnderDragon((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
                else if (Ambient.class.isAssignableFrom(clazz)) {
                    if (Bat.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityBat((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
                else if (Rabbit.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityRabbit((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Endermite.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityEndermite((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Guardian.class.isAssignableFrom(clazz)) {
                    if (ElderGuardian.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityGuardianElder((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                    else {
                        entity = (Entity)new EntityGuardian((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
                else if (ArmorStand.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityArmorStand((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
                }
                else if (PolarBear.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityPolarBear((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Vex.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityVex((net.minecraft.server.v1_12_R1.World)this.world);
                }
                else if (Illager.class.isAssignableFrom(clazz)) {
                    if (Spellcaster.class.isAssignableFrom(clazz)) {
                        if (Evoker.class.isAssignableFrom(clazz)) {
                            entity = (Entity)new EntityEvoker((net.minecraft.server.v1_12_R1.World)this.world);
                        }
                        else if (Illusioner.class.isAssignableFrom(clazz)) {
                            entity = (Entity)new EntityIllagerIllusioner((net.minecraft.server.v1_12_R1.World)this.world);
                        }
                    }
                    else if (Vindicator.class.isAssignableFrom(clazz)) {
                        entity = (Entity)new EntityVindicator((net.minecraft.server.v1_12_R1.World)this.world);
                    }
                }
            }
            if (entity != null) {
                entity.setLocation(x, y, z, yaw, pitch);
                entity.setHeadRotation(yaw);
            }
        }
        else if (Hanging.class.isAssignableFrom(clazz)) {
            final Block block = this.getBlockAt(location);
            BlockFace face = BlockFace.SELF;
            int width = 16;
            int height = 16;
            if (ItemFrame.class.isAssignableFrom(clazz)) {
                width = 12;
                height = 12;
            }
            else if (LeashHitch.class.isAssignableFrom(clazz)) {
                width = 9;
                height = 9;
            }
            final BlockFace[] faces = { BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH };
            final BlockPosition pos = new BlockPosition((int)x, (int)y, (int)z);
            for (final BlockFace dir : faces) {
                final net.minecraft.server.v1_12_R1.Block nmsBlock = CraftMagicNumbers.getBlock(block.getRelative(dir));
                if (nmsBlock.getBlockData().getMaterial().isBuildable() || BlockDiodeAbstract.isDiode(nmsBlock.getBlockData())) {
                    boolean taken = false;
                    final AxisAlignedBB bb = EntityHanging.calculateBoundingBox((Entity)null, pos, CraftBlock.blockFaceToNotch(dir).opposite(), width, height);
                    final List<Entity> list = (List<Entity>)this.world.getEntities((Entity)null, bb);
                    for (Iterator<Entity> it = list.iterator(); !taken && it.hasNext(); taken = true) {
                        final Entity e = it.next();
                        if (e instanceof EntityHanging) {}
                    }
                    if (!taken) {
                        face = dir;
                        break;
                    }
                }
            }
            if (LeashHitch.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityLeash((net.minecraft.server.v1_12_R1.World)this.world, new BlockPosition((int)x, (int)y, (int)z));
                entity.attachedToPlayer = true;
            }
            else {
                Preconditions.checkArgument(face != BlockFace.SELF, "Cannot spawn hanging entity for %s at %s (no free face)", (Object)clazz.getName(), (Object)location);
                final EnumDirection dir2 = CraftBlock.blockFaceToNotch(face).opposite();
                if (Painting.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityPainting((net.minecraft.server.v1_12_R1.World)this.world, new BlockPosition((int)x, (int)y, (int)z), dir2);
                }
                else if (ItemFrame.class.isAssignableFrom(clazz)) {
                    entity = (Entity)new EntityItemFrame((net.minecraft.server.v1_12_R1.World)this.world, new BlockPosition((int)x, (int)y, (int)z), dir2);
                }
            }
            if (entity != null && !((EntityHanging)entity).survives()) {
                throw new IllegalArgumentException("Cannot spawn hanging entity for " + clazz.getName() + " at " + location);
            }
        }
        else if (TNTPrimed.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityTNTPrimed((net.minecraft.server.v1_12_R1.World)this.world, x, y, z, (EntityLiving)null);
        }
        else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityExperienceOrb((net.minecraft.server.v1_12_R1.World)this.world, x, y, z, 0, ExperienceOrb.SpawnReason.CUSTOM, (Entity)null, (Entity)null);
        }
        else if (Weather.class.isAssignableFrom(clazz)) {
            if (LightningStrike.class.isAssignableFrom(clazz)) {
                entity = (Entity)new EntityLightning((net.minecraft.server.v1_12_R1.World)this.world, x, y, z, false);
            }
        }
        else if (Firework.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityFireworks((net.minecraft.server.v1_12_R1.World)this.world, x, y, z, net.minecraft.server.v1_12_R1.ItemStack.a);
        }
        else if (AreaEffectCloud.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityAreaEffectCloud((net.minecraft.server.v1_12_R1.World)this.world, x, y, z);
        }
        else if (EvokerFangs.class.isAssignableFrom(clazz)) {
            entity = (Entity)new EntityEvokerFangs((net.minecraft.server.v1_12_R1.World)this.world, x, y, z, (float)Math.toRadians(yaw), 0, (EntityLiving)null);
        }
        if (entity != null) {
            if (entity instanceof EntityOcelot) {
                ((EntityOcelot)entity).spawnBonus = false;
            }
            return entity;
        }
        throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
    }
    
    public <T extends org.bukkit.entity.Entity> T addEntity(final Entity entity, final CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        return this.addEntity(entity, reason, (org.bukkit.util.Consumer<T>)null);
    }
    
    public <T extends org.bukkit.entity.Entity> T addEntity(final Entity entity, final CreatureSpawnEvent.SpawnReason reason, final Consumer<T> function) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, (Object)"Cannot spawn null entity");
        if (entity instanceof EntityInsentient) {
            ((EntityInsentient)entity).prepare(this.getHandle().D(new BlockPosition(entity)), (GroupDataEntity)null);
        }
        if (function != null) {
            function.accept((Object)entity.getBukkitEntity());
        }
        this.world.addEntity(entity, reason);
        return (T)entity.getBukkitEntity();
    }
    
    public <T extends org.bukkit.entity.Entity> T spawn(final Location location, final Class<T> clazz, final Consumer<T> function, final CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        final Entity entity = this.createEntity(location, clazz);
        return this.addEntity(entity, reason, function);
    }
    
    public ChunkSnapshot getEmptyChunkSnapshot(final int x, final int z, final boolean includeBiome, final boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
    }
    
    public void setSpawnFlags(final boolean allowMonsters, final boolean allowAnimals) {
        this.world.setSpawnFlags(allowMonsters, allowAnimals);
    }
    
    public boolean getAllowAnimals() {
        return this.world.allowAnimals;
    }
    
    public boolean getAllowMonsters() {
        return this.world.allowMonsters;
    }
    
    public int getMaxHeight() {
        return this.world.getHeight();
    }
    
    public int getSeaLevel() {
        return this.world.getSeaLevel();
    }
    
    public boolean getKeepSpawnInMemory() {
        return this.world.keepSpawnInMemory;
    }
    
    public void setKeepSpawnInMemory(final boolean keepLoaded) {
        this.world.keepSpawnInMemory = keepLoaded;
        final BlockPosition chunkcoordinates = this.world.getSpawn();
        final int chunkCoordX = chunkcoordinates.getX() >> 4;
        final int chunkCoordZ = chunkcoordinates.getZ() >> 4;
        for (int radius = this.world.paperConfig.keepLoadedRange / 16, x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (keepLoaded) {
                    this.loadChunk(chunkCoordX + x, chunkCoordZ + z);
                }
                else if (this.isChunkLoaded(chunkCoordX + x, chunkCoordZ + z)) {
                    this.unloadChunk(chunkCoordX + x, chunkCoordZ + z);
                }
            }
        }
    }
    
    @Override
    public int hashCode() {
        return this.getUID().hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftWorld other = (CraftWorld)obj;
        return this.getUID() == other.getUID();
    }
    
    public File getWorldFolder() {
        return ((WorldNBTStorage)this.world.getDataManager()).getDirectory();
    }
    
    public void sendPluginMessage(final Plugin source, final String channel, final byte[] message) {
        StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
        for (final Player player : this.getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }
    
    public Set<String> getListeningPluginChannels() {
        final Set<String> result = new HashSet<String>();
        for (final Player player : this.getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }
        return result;
    }
    
    public WorldType getWorldType() {
        return WorldType.getByName(this.world.getWorldData().getType().name());
    }
    
    public boolean canGenerateStructures() {
        return this.world.getWorldData().shouldGenerateMapFeatures();
    }
    
    public long getTicksPerAnimalSpawns() {
        return this.world.ticksPerAnimalSpawns;
    }
    
    public void setTicksPerAnimalSpawns(final int ticksPerAnimalSpawns) {
        this.world.ticksPerAnimalSpawns = ticksPerAnimalSpawns;
    }
    
    public long getTicksPerMonsterSpawns() {
        return this.world.ticksPerMonsterSpawns;
    }
    
    public void setTicksPerMonsterSpawns(final int ticksPerMonsterSpawns) {
        this.world.ticksPerMonsterSpawns = ticksPerMonsterSpawns;
    }
    
    public void setMetadata(final String metadataKey, final MetadataValue newMetadataValue) {
        this.server.getWorldMetadata().setMetadata((Object)this, metadataKey, newMetadataValue);
    }
    
    public List<MetadataValue> getMetadata(final String metadataKey) {
        return (List<MetadataValue>)this.server.getWorldMetadata().getMetadata((Object)this, metadataKey);
    }
    
    public boolean hasMetadata(final String metadataKey) {
        return this.server.getWorldMetadata().hasMetadata((Object)this, metadataKey);
    }
    
    public void removeMetadata(final String metadataKey, final Plugin owningPlugin) {
        this.server.getWorldMetadata().removeMetadata((Object)this, metadataKey, owningPlugin);
    }
    
    public int getMonsterSpawnLimit() {
        if (this.monsterSpawn < 0) {
            return this.server.getMonsterSpawnLimit();
        }
        return this.monsterSpawn;
    }
    
    public void setMonsterSpawnLimit(final int limit) {
        this.monsterSpawn = limit;
    }
    
    public int getAnimalSpawnLimit() {
        if (this.animalSpawn < 0) {
            return this.server.getAnimalSpawnLimit();
        }
        return this.animalSpawn;
    }
    
    public void setAnimalSpawnLimit(final int limit) {
        this.animalSpawn = limit;
    }
    
    public int getWaterAnimalSpawnLimit() {
        if (this.waterAnimalSpawn < 0) {
            return this.server.getWaterAnimalSpawnLimit();
        }
        return this.waterAnimalSpawn;
    }
    
    public void setWaterAnimalSpawnLimit(final int limit) {
        this.waterAnimalSpawn = limit;
    }
    
    public int getAmbientSpawnLimit() {
        if (this.ambientSpawn < 0) {
            return this.server.getAmbientSpawnLimit();
        }
        return this.ambientSpawn;
    }
    
    public void setAmbientSpawnLimit(final int limit) {
        this.ambientSpawn = limit;
    }
    
    public void playSound(final Location loc, final Sound sound, final float volume, final float pitch) {
        this.playSound(loc, sound, SoundCategory.MASTER, volume, pitch);
    }
    
    public void playSound(final Location loc, final String sound, final float volume, final float pitch) {
        this.playSound(loc, sound, SoundCategory.MASTER, volume, pitch);
    }
    
    public void playSound(final Location loc, final Sound sound, final SoundCategory category, final float volume, final float pitch) {
        if (loc == null || sound == null || category == null) {
            return;
        }
        final double x = loc.getX();
        final double y = loc.getY();
        final double z = loc.getZ();
        this.getHandle().a((EntityHuman)null, x, y, z, CraftSound.getSoundEffect(CraftSound.getSound(sound)), net.minecraft.server.v1_12_R1.SoundCategory.valueOf(category.name()), volume, pitch);
    }
    
    public void playSound(final Location loc, final String sound, final SoundCategory category, final float volume, final float pitch) {
        if (loc == null || sound == null || category == null) {
            return;
        }
        final double x = loc.getX();
        final double y = loc.getY();
        final double z = loc.getZ();
        final PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect(sound, net.minecraft.server.v1_12_R1.SoundCategory.valueOf(category.name()), x, y, z, volume, pitch);
        this.world.getMinecraftServer().getPlayerList().sendPacketNearby((EntityHuman)null, x, y, z, (volume > 1.0f) ? ((double)(16.0f * volume)) : 16.0, this.world, (Packet)packet);
    }
    
    public String getGameRuleValue(final String rule) {
        return this.getHandle().getGameRules().get(rule);
    }
    
    public boolean setGameRuleValue(final String rule, final String value) {
        if (rule == null || value == null) {
            return false;
        }
        if (!this.isGameRule(rule)) {
            return false;
        }
        this.getHandle().getGameRules().set(rule, value);
        return true;
    }
    
    public String[] getGameRules() {
        return this.getHandle().getGameRules().getGameRules();
    }
    
    public boolean isGameRule(final String rule) {
        return this.getHandle().getGameRules().contains(rule);
    }
    
    public WorldBorder getWorldBorder() {
        if (this.worldBorder == null) {
            this.worldBorder = (WorldBorder)new CraftWorldBorder(this);
        }
        return this.worldBorder;
    }
    
    public void spawnParticle(final Particle particle, final Location location, final int count) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }
    
    public void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count) {
        this.spawnParticle(particle, x, y, z, count, (Object)null);
    }
    
    public <T> void spawnParticle(final Particle particle, final Location location, final int count, final T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }
    
    public <T> void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final T data) {
        this.spawnParticle(particle, x, y, z, count, 0.0, 0.0, 0.0, data);
    }
    
    public void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }
    
    public void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, (Object)null);
    }
    
    public <T> void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ, final T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }
    
    public <T> void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ, final T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1.0, (Object)data);
    }
    
    public void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }
    
    public void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, (Object)null);
    }
    
    public <T> void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra, final T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, (Object)data);
    }
    
    public <T> void spawnParticle(final Particle particle, final List<Player> receivers, final Player sender, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra, final T data, final boolean force) {
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        this.getHandle().sendParticles((List)((receivers == null) ? this.getHandle().players : ((List<? super Object>)receivers.stream().map(player -> player.getHandle()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()))), (sender != null) ? ((CraftPlayer)sender).getHandle() : null, CraftParticle.toNMS(particle), force, x, y, z, count, offsetX, offsetY, offsetZ, extra, CraftParticle.toData(particle, (Object)data));
    }
    
    public void processChunkGC() {
        ++this.chunkGCTickCount;
        if (this.chunkLoadCount >= this.server.chunkGCLoadThresh && this.server.chunkGCLoadThresh > 0) {
            this.chunkLoadCount = 0;
        }
        else {
            if (this.chunkGCTickCount < this.server.chunkGCPeriod || this.server.chunkGCPeriod <= 0) {
                return;
            }
            this.chunkGCTickCount = 0;
        }
        final ChunkProviderServer cps = this.world.getChunkProviderServer();
        for (final Chunk chunk : cps.chunks.values()) {
            if (!this.isChunkInUse(chunk.locX, chunk.locZ)) {
                if (chunk.scheduledForUnload != null) {
                    continue;
                }
                if (cps.unloadQueue.contains(ChunkCoordIntPair.a(chunk.locX, chunk.locZ))) {
                    continue;
                }
                cps.unload(chunk);
            }
        }
    }
    
    public World.Spigot spigot() {
        return this.spigot;
    }
    
    static {
        rand = new Random();
    }
}
