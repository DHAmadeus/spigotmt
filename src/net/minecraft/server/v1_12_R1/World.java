// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.v1_12_R1;

import java.util.UUID;
import com.google.common.base.MoreObjects;
import com.google.common.base.Function;
import org.bukkit.event.block.BlockCanBuildEvent;
import com.google.common.base.Predicate;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.IdentityHashMap;
import com.destroystokyo.paper.exception.ServerException;
import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerInternalException;
import co.aikar.timings.TimingHistory;
import org.spigotmc.ActivationRange;
import java.util.Collection;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import org.bukkit.event.Cancellable;
import com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Projectile;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.spigotmc.AsyncCatcher;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlockState;
import javax.annotation.Nullable;
import com.destroystokyo.paper.antixray.ChunkPacketBlockControllerAntiXray;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ConcurrentModificationException;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import java.util.Map;
import org.spigotmc.TickLimiter;
import co.aikar.timings.WorldTimingsHandler;
import com.destroystokyo.paper.antixray.ChunkPacketBlockController;
import com.destroystokyo.paper.PaperWorldConfig;
import org.spigotmc.SpigotWorldConfig;
import org.bukkit.block.BlockState;
import java.util.ArrayList;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;
import java.util.List;

public abstract class World implements IBlockAccess
{
    private int a;
    protected boolean d;
    public static final boolean DEBUG_ENTITIES;
    public final List<Entity> entityList;
    protected final Set<Entity> f;
    public final List<TileEntity> tileEntityListTick;
    private final List<TileEntity> b;
    private final Set<TileEntity> tileEntityListUnload;
    public final List<EntityHuman> players;
    public final List<Entity> j;
    protected final IntHashMap<Entity> entitiesById;
    private final long K = 16777215L;
    private int L;
    protected int l;
    protected final int m = 1013904223;
    protected float n;
    protected float o;
    protected float p;
    protected float q;
    private int M;
    public final Random random;
    public WorldProvider worldProvider;
    protected NavigationListener t;
    protected List<IWorldAccess> u;
    protected IChunkProvider chunkProvider;
    protected final IDataManager dataManager;
    public WorldData worldData;
    protected boolean isLoading;
    public PersistentCollection worldMaps;
    protected PersistentVillage villages;
    protected LootTableRegistry B;
    protected AdvancementDataWorld C;
    protected CustomFunctionData D;
    public final MethodProfiler methodProfiler;
    private final Calendar N;
    public Scoreboard scoreboard;
    public final boolean isClientSide;
    public boolean allowMonsters;
    public boolean allowAnimals;
    private boolean O;
    private final WorldBorder P;
    int[] J;
    private final CraftWorld world;
    public boolean pvpMode;
    public boolean keepSpawnInMemory;
    public ChunkGenerator generator;
    public boolean captureBlockStates;
    public boolean captureTreeGeneration;
    public ArrayList<BlockState> capturedBlockStates;
    public List<EntityItem> captureDrops;
    public long ticksPerAnimalSpawns;
    public long ticksPerMonsterSpawns;
    public boolean populating;
    private int tickPosition;
    public final SpigotWorldConfig spigotConfig;
    public final PaperWorldConfig paperConfig;
    public final ChunkPacketBlockController chunkPacketBlockController;
    public final WorldTimingsHandler timings;
    private boolean guardEntityList;
    public static boolean haveWeSilencedAPhysicsCrash;
    public static String blockLocation;
    private TickLimiter entityLimiter;
    private TickLimiter tileLimiter;
    private int tileTickPosition;
    public final Map<Explosion.CacheKey, Float> explosionDensityCache;
    public Map<BlockPosition, TileEntity> capturedTileEntities;
    
    public Set<Entity> getEntityUnloadQueue() {
        return this.f;
    }
    
    private int getSkylightSubtracted() {
        return this.L;
    }
    
    public CraftWorld getWorld() {
        return this.world;
    }
    
    public CraftServer getServer() {
        return (CraftServer)Bukkit.getServer();
    }
    
    public Chunk getChunkIfLoaded(final BlockPosition blockposition) {
        return ((ChunkProviderServer)this.chunkProvider).getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }
    
    public Chunk getChunkIfLoaded(final int x, final int z) {
        return ((ChunkProviderServer)this.chunkProvider).getChunkIfLoaded(x, z);
    }
    
    protected World(final IDataManager idatamanager, final WorldData worlddata, final WorldProvider worldprovider, final MethodProfiler methodprofiler, final boolean flag, final ChunkGenerator gen, final org.bukkit.World.Environment env) {
        this.a = 63;
        this.entityList = new ArrayList<Entity>() {
            @Override
            public Entity remove(final int index) {
                this.guard();
                return super.remove(index);
            }
            
            @Override
            public boolean remove(final Object o) {
                this.guard();
                return super.remove(o);
            }
            
            private void guard() {
                if (World.this.guardEntityList) {
                    throw new ConcurrentModificationException();
                }
            }
        };
        this.f = (Set<Entity>)Sets.newHashSet();
        this.tileEntityListTick = (List<TileEntity>)Lists.newArrayList();
        this.b = (List<TileEntity>)Lists.newArrayList();
        this.tileEntityListUnload = (Set<TileEntity>)Sets.newHashSet();
        this.players = (List<EntityHuman>)Lists.newArrayList();
        this.j = (List<Entity>)Lists.newArrayList();
        this.entitiesById = (IntHashMap<Entity>)new IntHashMap();
        this.l = new Random().nextInt();
        this.random = new Random();
        this.t = new NavigationListener();
        this.keepSpawnInMemory = true;
        this.captureBlockStates = false;
        this.captureTreeGeneration = false;
        this.capturedBlockStates = new ArrayList<BlockState>() {
            @Override
            public boolean add(final BlockState blockState) {
                for (final BlockState blockState2 : this) {
                    if (blockState2.getLocation().equals((Object)blockState.getLocation())) {
                        return false;
                    }
                }
                return super.add(blockState);
            }
        };
        this.explosionDensityCache = new HashMap<Explosion.CacheKey, Float>();
        this.capturedTileEntities = (Map<BlockPosition, TileEntity>)Maps.newHashMap();
        this.spigotConfig = new SpigotWorldConfig(worlddata.getName());
        this.paperConfig = new PaperWorldConfig(worlddata.getName(), this.spigotConfig);
        this.chunkPacketBlockController = (ChunkPacketBlockController)(this.paperConfig.antiXray ? new ChunkPacketBlockControllerAntiXray(this.paperConfig) : ChunkPacketBlockController.NO_OPERATION_INSTANCE);
        this.generator = gen;
        this.world = new CraftWorld((WorldServer)this, gen, env);
        this.ticksPerAnimalSpawns = this.getServer().getTicksPerAnimalSpawns();
        this.ticksPerMonsterSpawns = this.getServer().getTicksPerMonsterSpawns();
        this.u = (List<IWorldAccess>)Lists.newArrayList((Object[])new IWorldAccess[] { this.t });
        this.N = Calendar.getInstance();
        this.scoreboard = new Scoreboard();
        this.allowMonsters = true;
        this.allowAnimals = true;
        this.J = new int[32768];
        this.dataManager = idatamanager;
        this.methodProfiler = methodprofiler;
        this.worldData = worlddata;
        this.worldProvider = worldprovider;
        this.isClientSide = flag;
        this.P = worldprovider.getWorldBorder();
        this.getWorldBorder().world = (WorldServer)this;
        this.getWorldBorder().a((IWorldBorderListener)new IWorldBorderListener() {
            public void a(final WorldBorder worldborder, final double d0) {
                World.this.getServer().getHandle().sendAll((Packet)new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE), (World)worldborder.world);
            }
            
            public void a(final WorldBorder worldborder, final double d0, final double d1, final long i) {
                World.this.getServer().getHandle().sendAll((Packet)new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.LERP_SIZE), (World)worldborder.world);
            }
            
            public void a(final WorldBorder worldborder, final double d0, final double d1) {
                World.this.getServer().getHandle().sendAll((Packet)new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER), (World)worldborder.world);
            }
            
            public void a(final WorldBorder worldborder, final int i) {
                World.this.getServer().getHandle().sendAll((Packet)new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_TIME), (World)worldborder.world);
            }
            
            public void b(final WorldBorder worldborder, final int i) {
                World.this.getServer().getHandle().sendAll((Packet)new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS), (World)worldborder.world);
            }
            
            public void b(final WorldBorder worldborder, final double d0) {
            }
            
            public void c(final WorldBorder worldborder, final double d0) {
            }
        });
        this.getServer().addWorld((org.bukkit.World)this.world);
        this.timings = new WorldTimingsHandler(this);
        this.keepSpawnInMemory = this.paperConfig.keepSpawnInMemory;
        this.entityLimiter = new TickLimiter(this.spigotConfig.entityMaxTickTime);
        this.tileLimiter = new TickLimiter(this.spigotConfig.tileMaxTickTime);
    }
    
    public World b() {
        return this;
    }
    
    public BiomeBase getBiome(final BlockPosition blockposition) {
        if (this.isLoaded(blockposition)) {
            final Chunk chunk = this.getChunkAtWorldCoords(blockposition);
            try {
                return chunk.getBiome(blockposition, this.worldProvider.k());
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.a(throwable, "Getting biome");
                final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Coordinates of biome request");
                crashreportsystemdetails.a("Location", (CrashReportCallable)new CrashReportCallable() {
                    public String a() throws Exception {
                        return CrashReportSystemDetails.a(blockposition);
                    }
                    
                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        return this.worldProvider.k().getBiome(blockposition, Biomes.c);
    }
    
    public WorldChunkManager getWorldChunkManager() {
        return this.worldProvider.k();
    }
    
    protected abstract IChunkProvider n();
    
    public void a(final WorldSettings worldsettings) {
        this.worldData.d(true);
    }
    
    @Nullable
    public MinecraftServer getMinecraftServer() {
        return null;
    }
    
    public IBlockData c(final BlockPosition blockposition) {
        BlockPosition blockposition2;
        for (blockposition2 = new BlockPosition(blockposition.getX(), this.getSeaLevel(), blockposition.getZ()); !this.isEmpty(blockposition2.up()); blockposition2 = blockposition2.up()) {}
        return this.getType(blockposition2);
    }
    
    private static boolean isValidLocation(final BlockPosition blockposition) {
        return blockposition.isValidLocation();
    }
    
    private static boolean E(final BlockPosition blockposition) {
        return blockposition.isInvalidYLocation();
    }
    
    public boolean isEmpty(final BlockPosition blockposition) {
        return this.getType(blockposition).getMaterial() == Material.AIR;
    }
    
    public boolean isLoaded(final BlockPosition blockposition) {
        return this.getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4) != null;
    }
    
    public boolean a(final BlockPosition blockposition, final boolean flag) {
        return this.isChunkLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4, flag);
    }
    
    public boolean areChunksLoaded(final BlockPosition blockposition, final int i) {
        return this.areChunksLoaded(blockposition, i, true);
    }
    
    public boolean areChunksLoaded(final BlockPosition blockposition, final int i, final boolean flag) {
        return this.isAreaLoaded(blockposition.getX() - i, blockposition.getY() - i, blockposition.getZ() - i, blockposition.getX() + i, blockposition.getY() + i, blockposition.getZ() + i, flag);
    }
    
    public boolean areChunksLoadedBetween(final BlockPosition blockposition, final BlockPosition blockposition1) {
        return this.areChunksLoadedBetween(blockposition, blockposition1, true);
    }
    
    public boolean areChunksLoadedBetween(final BlockPosition blockposition, final BlockPosition blockposition1, final boolean flag) {
        return this.isAreaLoaded(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), flag);
    }
    
    public boolean a(final StructureBoundingBox structureboundingbox) {
        return this.b(structureboundingbox, true);
    }
    
    public boolean b(final StructureBoundingBox structureboundingbox, final boolean flag) {
        return this.isAreaLoaded(structureboundingbox.a, structureboundingbox.b, structureboundingbox.c, structureboundingbox.d, structureboundingbox.e, structureboundingbox.f, flag);
    }
    
    private boolean isAreaLoaded(int i, final int j, int k, int l, final int i1, int j1, final boolean flag) {
        if (i1 >= 0 && j < 256) {
            i >>= 4;
            k >>= 4;
            l >>= 4;
            j1 >>= 4;
            for (int k2 = i; k2 <= l; ++k2) {
                for (int l2 = k; l2 <= j1; ++l2) {
                    if (!this.isChunkLoaded(k2, l2, flag)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    protected abstract boolean isChunkLoaded(final int p0, final int p1, final boolean p2);
    
    public Chunk getChunkAtWorldCoords(final BlockPosition blockposition) {
        return this.getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }
    
    public Chunk getChunkAt(final int i, final int j) {
        return this.chunkProvider.getChunkAt(i, j);
    }
    
    public boolean b(final int i, final int j) {
        return this.isChunkLoaded(i, j, false) || this.chunkProvider.e(i, j);
    }
    
    public boolean setTypeAndData(final BlockPosition blockposition, final IBlockData iblockdata, final int i) {
        if (this.captureTreeGeneration) {
            BlockState blockstate = null;
            final Iterator<BlockState> it = this.capturedBlockStates.iterator();
            while (it.hasNext()) {
                final BlockState previous = it.next();
                if (previous.getX() == blockposition.getX() && previous.getY() == blockposition.getY() && previous.getZ() == blockposition.getZ()) {
                    blockstate = previous;
                    it.remove();
                    break;
                }
            }
            if (blockstate == null) {
                blockstate = (BlockState)CraftBlockState.getBlockState(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), i);
            }
            blockstate.setTypeId(CraftMagicNumbers.getId(iblockdata.getBlock()));
            blockstate.setRawData((byte)iblockdata.getBlock().toLegacyData(iblockdata));
            this.capturedBlockStates.add(blockstate);
            return true;
        }
        if (blockposition.isInvalidYLocation()) {
            return false;
        }
        if (!this.isClientSide && this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        }
        final Chunk chunk = this.getChunkAtWorldCoords(blockposition);
        final Block block = iblockdata.getBlock();
        BlockState blockstate2 = null;
        if (this.captureBlockStates) {
            blockstate2 = this.world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()).getState();
            this.capturedBlockStates.add(blockstate2);
        }
        final IBlockData iblockdata2 = chunk.a(blockposition, iblockdata);
        if (iblockdata2 == null) {
            if (this.captureBlockStates) {
                this.capturedBlockStates.remove(blockstate2);
            }
            return false;
        }
        if (iblockdata.c() != iblockdata2.c() || iblockdata.d() != iblockdata2.d()) {
            this.methodProfiler.a("checkLight");
            chunk.runOrQueueLightUpdate(() -> this.w(blockposition));
            this.methodProfiler.b();
        }
        if (!this.captureBlockStates) {
            this.notifyAndUpdatePhysics(blockposition, chunk, iblockdata2, iblockdata, i);
        }
        return true;
    }
    
    public void notifyAndUpdatePhysics(final BlockPosition blockposition, final Chunk chunk, final IBlockData oldBlock, final IBlockData newBlock, final int i) {
        if ((i & 0x2) != 0x0 && (!this.isClientSide || (i & 0x4) == 0x0) && (chunk == null || chunk.isReady())) {
            this.notify(blockposition, oldBlock, newBlock, i);
        }
        if (!this.isClientSide && (i & 0x1) != 0x0) {
            this.update(blockposition, oldBlock.getBlock(), true);
            if (newBlock.n()) {
                this.updateAdjacentComparators(blockposition, newBlock.getBlock());
            }
        }
        else if (!this.isClientSide && (i & 0x10) == 0x0) {
            this.c(blockposition, newBlock.getBlock());
        }
    }
    
    public boolean setAir(final BlockPosition blockposition) {
        return this.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
    }
    
    public boolean setAir(final BlockPosition blockposition, final boolean flag) {
        final IBlockData iblockdata = this.getType(blockposition);
        final Block block = iblockdata.getBlock();
        if (iblockdata.getMaterial() == Material.AIR) {
            return false;
        }
        this.triggerEffect(2001, blockposition, Block.getCombinedId(iblockdata));
        if (flag) {
            block.b(this, blockposition, iblockdata, 0);
        }
        return this.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
    }
    
    public boolean setTypeUpdate(final BlockPosition blockposition, final IBlockData iblockdata) {
        return this.setTypeAndData(blockposition, iblockdata, 3);
    }
    
    public void notify(final BlockPosition blockposition, final IBlockData iblockdata, final IBlockData iblockdata1, final int i) {
        for (int j = 0; j < this.u.size(); ++j) {
            this.u.get(j).a(this, blockposition, iblockdata, iblockdata1, i);
        }
    }
    
    public void update(final BlockPosition blockposition, final Block block, final boolean flag) {
        if (this.worldData.getType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
            if (this.populating) {
                return;
            }
            this.applyPhysics(blockposition, block, flag);
        }
    }
    
    public void a(final int i, final int j, int k, int l) {
        if (k > l) {
            final int i2 = l;
            l = k;
            k = i2;
        }
        if (this.worldProvider.m()) {
            for (int i2 = k; i2 <= l; ++i2) {
                this.c(EnumSkyBlock.SKY, new BlockPosition(i, i2, j));
            }
        }
        this.b(i, k, j, i, l, j);
    }
    
    public void b(final BlockPosition blockposition, final BlockPosition blockposition1) {
        this.b(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
    }
    
    public void b(final int i, final int j, final int k, final int l, final int i1, final int j1) {
        for (int k2 = 0; k2 < this.u.size(); ++k2) {
            this.u.get(k2).a(i, j, k, l, i1, j1);
        }
    }
    
    public void c(final BlockPosition blockposition, final Block block) {
        this.b(blockposition.west(), block, blockposition);
        this.b(blockposition.east(), block, blockposition);
        this.b(blockposition.down(), block, blockposition);
        this.b(blockposition.up(), block, blockposition);
        this.b(blockposition.north(), block, blockposition);
        this.b(blockposition.south(), block, blockposition);
    }
    
    public void applyPhysics(final BlockPosition blockposition, final Block block, final boolean flag) {
        if (this.captureBlockStates) {
            return;
        }
        this.a(blockposition.west(), block, blockposition);
        this.a(blockposition.east(), block, blockposition);
        this.a(blockposition.down(), block, blockposition);
        this.a(blockposition.up(), block, blockposition);
        this.a(blockposition.north(), block, blockposition);
        this.a(blockposition.south(), block, blockposition);
        if (flag) {
            this.c(blockposition, block);
        }
        this.chunkPacketBlockController.updateNearbyBlocks(this, blockposition);
    }
    
    public void a(final BlockPosition blockposition, final Block block, final EnumDirection enumdirection) {
        if (enumdirection != EnumDirection.WEST) {
            this.a(blockposition.west(), block, blockposition);
        }
        if (enumdirection != EnumDirection.EAST) {
            this.a(blockposition.east(), block, blockposition);
        }
        if (enumdirection != EnumDirection.DOWN) {
            this.a(blockposition.down(), block, blockposition);
        }
        if (enumdirection != EnumDirection.UP) {
            this.a(blockposition.up(), block, blockposition);
        }
        if (enumdirection != EnumDirection.NORTH) {
            this.a(blockposition.north(), block, blockposition);
        }
        if (enumdirection != EnumDirection.SOUTH) {
            this.a(blockposition.south(), block, blockposition);
        }
    }
    
    public void a(final BlockPosition blockposition, final Block block, final BlockPosition blockposition1) {
        if (!this.isClientSide) {
            final IBlockData iblockdata = this.getType(blockposition);
            try {
                final CraftWorld world = ((WorldServer)this).getWorld();
                if (world != null && !((WorldServer)this).stopPhysicsEvent) {
                    final BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftMagicNumbers.getId(block), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
                    this.getServer().getPluginManager().callEvent((Event)event);
                    if (event.isCancelled()) {
                        return;
                    }
                }
                iblockdata.doPhysics(this, blockposition, block, blockposition1);
            }
            catch (StackOverflowError stackoverflowerror) {
                World.haveWeSilencedAPhysicsCrash = true;
                World.blockLocation = blockposition.getX() + ", " + blockposition.getY() + ", " + blockposition.getZ();
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.a(throwable, "Exception while updating neighbours");
                final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being updated");
                crashreportsystemdetails.a("Source block type", (CrashReportCallable)new CrashReportCallable() {
                    public String a() throws Exception {
                        try {
                            return String.format("ID #%d (%s // %s)", Block.getId(block), block.a(), block.getClass().getCanonicalName());
                        }
                        catch (Throwable throwable) {
                            return "ID #" + Block.getId(block);
                        }
                    }
                    
                    public Object call() throws Exception {
                        return this.a();
                    }
                });
                CrashReportSystemDetails.a(crashreportsystemdetails, blockposition, iblockdata);
                throw new ReportedException(crashreport);
            }
        }
    }
    
    public void b(final BlockPosition blockposition, final Block block, final BlockPosition blockposition1) {
        if (!this.isClientSide) {
            final IBlockData iblockdata = this.getType(blockposition);
            if (iblockdata.getBlock() == Blocks.dk) {
                try {
                    ((BlockObserver)iblockdata.getBlock()).b(iblockdata, this, blockposition, block, blockposition1);
                }
                catch (Throwable throwable) {
                    final CrashReport crashreport = CrashReport.a(throwable, "Exception while updating neighbours");
                    final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being updated");
                    crashreportsystemdetails.a("Source block type", (CrashReportCallable)new CrashReportCallable() {
                        public String a() throws Exception {
                            try {
                                return String.format("ID #%d (%s // %s)", Block.getId(block), block.a(), block.getClass().getCanonicalName());
                            }
                            catch (Throwable throwable) {
                                return "ID #" + Block.getId(block);
                            }
                        }
                        
                        public Object call() throws Exception {
                            return this.a();
                        }
                    });
                    CrashReportSystemDetails.a(crashreportsystemdetails, blockposition, iblockdata);
                    throw new ReportedException(crashreport);
                }
            }
        }
    }
    
    public boolean a(final BlockPosition blockposition, final Block block) {
        return false;
    }
    
    public boolean h(final BlockPosition blockposition) {
        return this.getChunkAtWorldCoords(blockposition).c(blockposition);
    }
    
    public boolean i(final BlockPosition blockposition) {
        if (blockposition.getY() >= this.getSeaLevel()) {
            return this.h(blockposition);
        }
        BlockPosition blockposition2 = new BlockPosition(blockposition.getX(), this.getSeaLevel(), blockposition.getZ());
        if (!this.h(blockposition2)) {
            return false;
        }
        for (blockposition2 = blockposition2.down(); blockposition2.getY() > blockposition.getY(); blockposition2 = blockposition2.down()) {
            final IBlockData iblockdata = this.getType(blockposition2);
            if (iblockdata.c() > 0 && !iblockdata.getMaterial().isLiquid()) {
                return false;
            }
        }
        return true;
    }
    
    public int j(BlockPosition blockposition) {
        if (blockposition.getY() < 0) {
            return 0;
        }
        if (blockposition.getY() >= 256) {
            blockposition = new BlockPosition(blockposition.getX(), 255, blockposition.getZ());
        }
        return this.getChunkAtWorldCoords(blockposition).a(blockposition, 0);
    }
    
    public boolean isLightLevel(BlockPosition blockposition, final int level) {
        if (!blockposition.isValidLocation()) {
            return true;
        }
        if (this.getType(blockposition).f()) {
            return this.c(blockposition.up(), false) >= level || this.c(blockposition.east(), false) >= level || this.c(blockposition.west(), false) >= level || this.c(blockposition.south(), false) >= level || this.c(blockposition.north(), false) >= level;
        }
        if (blockposition.getY() >= 256) {
            blockposition = new BlockPosition(blockposition.getX(), 255, blockposition.getZ());
        }
        final Chunk chunk = this.getChunkAtWorldCoords(blockposition);
        return chunk.getLightSubtracted(blockposition, this.getSkylightSubtracted()) >= level;
    }
    
    public int getLightLevel(final BlockPosition blockposition) {
        return this.c(blockposition, true);
    }
    
    public final int getLight(final BlockPosition blockposition, final boolean checkNeighbors) {
        return this.c(blockposition, checkNeighbors);
    }
    
    public int c(BlockPosition blockposition, final boolean flag) {
        if (blockposition.getX() < -30000000 || blockposition.getZ() < -30000000 || blockposition.getX() >= 30000000 || blockposition.getZ() >= 30000000) {
            return 15;
        }
        if (flag && this.getType(blockposition).f()) {
            int i = this.c(blockposition.up(), false);
            final int j = this.c(blockposition.east(), false);
            final int k = this.c(blockposition.west(), false);
            final int l = this.c(blockposition.south(), false);
            final int i2 = this.c(blockposition.north(), false);
            if (j > i) {
                i = j;
            }
            if (k > i) {
                i = k;
            }
            if (l > i) {
                i = l;
            }
            if (i2 > i) {
                i = i2;
            }
            return i;
        }
        if (blockposition.getY() < 0) {
            return 0;
        }
        if (blockposition.getY() >= 256) {
            blockposition = new BlockPosition(blockposition.getX(), 255, blockposition.getZ());
        }
        if (!this.isLoaded(blockposition)) {
            return 0;
        }
        final Chunk chunk = this.getChunkAtWorldCoords(blockposition);
        return chunk.a(blockposition, this.L);
    }
    
    public BlockPosition getHighestBlockYAt(final BlockPosition blockposition) {
        return new BlockPosition(blockposition.getX(), this.c(blockposition.getX(), blockposition.getZ()), blockposition.getZ());
    }
    
    public int c(final int i, final int j) {
        int k;
        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (this.isChunkLoaded(i >> 4, j >> 4, true)) {
                k = this.getChunkAt(i >> 4, j >> 4).b(i & 0xF, j & 0xF);
            }
            else {
                k = 0;
            }
        }
        else {
            k = this.getSeaLevel() + 1;
        }
        return k;
    }
    
    @Deprecated
    public int d(final int i, final int j) {
        if (i < -30000000 || j < -30000000 || i >= 30000000 || j >= 30000000) {
            return this.getSeaLevel() + 1;
        }
        if (!this.isChunkLoaded(i >> 4, j >> 4, true)) {
            return 0;
        }
        final Chunk chunk = this.getChunkAt(i >> 4, j >> 4);
        return chunk.w();
    }
    
    public int getBrightness(final EnumSkyBlock enumskyblock, BlockPosition blockposition) {
        if (blockposition.getY() < 0) {
            blockposition = new BlockPosition(blockposition.getX(), 0, blockposition.getZ());
        }
        if (!blockposition.isValidLocation()) {
            return enumskyblock.c;
        }
        if (!this.isLoaded(blockposition)) {
            return enumskyblock.c;
        }
        final Chunk chunk = this.getChunkAtWorldCoords(blockposition);
        return chunk.getBrightness(enumskyblock, blockposition);
    }
    
    public void a(final EnumSkyBlock enumskyblock, final BlockPosition blockposition, final int i) {
        if (blockposition.isValidLocation() && this.isLoaded(blockposition)) {
            final Chunk chunk = this.getChunkAtWorldCoords(blockposition);
            chunk.a(enumskyblock, blockposition, i);
            this.m(blockposition);
        }
    }
    
    public void m(final BlockPosition blockposition) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).a(blockposition);
        }
    }
    
    public float n(final BlockPosition blockposition) {
        return this.worldProvider.o()[this.getLightLevel(blockposition)];
    }
    
    public IBlockData getTypeIfLoaded(final BlockPosition blockposition) {
        final int x = blockposition.getX();
        final int y = blockposition.getY();
        final int z = blockposition.getZ();
        if (this.captureTreeGeneration) {
            final IBlockData previous = this.getCapturedBlockType(x, y, z);
            if (previous != null) {
                return previous;
            }
        }
        final Chunk chunk = ((ChunkProviderServer)this.chunkProvider).getChunkIfLoaded(x >> 4, z >> 4);
        if (chunk != null) {
            return chunk.getBlockData(x, y, z);
        }
        return null;
    }
    
    public IBlockData getType(final BlockPosition blockposition) {
        final int x = blockposition.getX();
        final int y = blockposition.getY();
        final int z = blockposition.getZ();
        if (this.captureTreeGeneration) {
            final IBlockData previous = this.getCapturedBlockType(x, y, z);
            if (previous != null) {
                return previous;
            }
        }
        return this.chunkProvider.getChunkAt(x >> 4, z >> 4).getBlockData(x, y, z);
    }
    
    private IBlockData getCapturedBlockType(final int x, final int y, final int z) {
        for (final BlockState previous : this.capturedBlockStates) {
            if (previous.getX() == x && previous.getY() == y && previous.getZ() == z) {
                return CraftMagicNumbers.getBlock(previous.getTypeId()).fromLegacyData((int)previous.getRawData());
            }
        }
        return null;
    }
    
    public boolean D() {
        return this.L < 4;
    }
    
    @Nullable
    public MovingObjectPosition rayTrace(final Vec3D vec3d, final Vec3D vec3d1) {
        return this.rayTrace(vec3d, vec3d1, false, false, false);
    }
    
    @Nullable
    public MovingObjectPosition rayTrace(final Vec3D vec3d, final Vec3D vec3d1, final boolean flag) {
        return this.rayTrace(vec3d, vec3d1, flag, false, false);
    }
    
    @Nullable
    public MovingObjectPosition rayTrace(Vec3D vec3d, final Vec3D vec3d1, final boolean flag, final boolean flag1, final boolean flag2) {
        if (Double.isNaN(vec3d.x) || Double.isNaN(vec3d.y) || Double.isNaN(vec3d.z)) {
            return null;
        }
        if (Double.isNaN(vec3d1.x) || Double.isNaN(vec3d1.y) || Double.isNaN(vec3d1.z)) {
            return null;
        }
        final int i = MathHelper.floor(vec3d1.x);
        final int j = MathHelper.floor(vec3d1.y);
        final int k = MathHelper.floor(vec3d1.z);
        int l = MathHelper.floor(vec3d.x);
        int i2 = MathHelper.floor(vec3d.y);
        int j2 = MathHelper.floor(vec3d.z);
        BlockPosition blockposition = new BlockPosition(l, i2, j2);
        final IBlockData iblockdata = this.getTypeIfLoaded(blockposition);
        if (iblockdata == null) {
            return null;
        }
        final Block block = iblockdata.getBlock();
        if ((!flag1 || iblockdata.d((IBlockAccess)this, blockposition) != Block.k) && block.a(iblockdata, flag)) {
            final MovingObjectPosition movingobjectposition = iblockdata.a(this, blockposition, vec3d, vec3d1);
            if (movingobjectposition != null) {
                return movingobjectposition;
            }
        }
        MovingObjectPosition movingobjectposition2 = null;
        int k2 = 200;
        while (k2-- >= 0) {
            if (Double.isNaN(vec3d.x) || Double.isNaN(vec3d.y) || Double.isNaN(vec3d.z)) {
                return null;
            }
            if (l == i && i2 == j && j2 == k) {
                return flag2 ? movingobjectposition2 : null;
            }
            boolean flag3 = true;
            boolean flag4 = true;
            boolean flag5 = true;
            double d0 = 999.0;
            double d2 = 999.0;
            double d3 = 999.0;
            if (i > l) {
                d0 = l + 1.0;
            }
            else if (i < l) {
                d0 = l + 0.0;
            }
            else {
                flag3 = false;
            }
            if (j > i2) {
                d2 = i2 + 1.0;
            }
            else if (j < i2) {
                d2 = i2 + 0.0;
            }
            else {
                flag4 = false;
            }
            if (k > j2) {
                d3 = j2 + 1.0;
            }
            else if (k < j2) {
                d3 = j2 + 0.0;
            }
            else {
                flag5 = false;
            }
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = 999.0;
            final double d7 = vec3d1.x - vec3d.x;
            final double d8 = vec3d1.y - vec3d.y;
            final double d9 = vec3d1.z - vec3d.z;
            if (flag3) {
                d4 = (d0 - vec3d.x) / d7;
            }
            if (flag4) {
                d5 = (d2 - vec3d.y) / d8;
            }
            if (flag5) {
                d6 = (d3 - vec3d.z) / d9;
            }
            if (d4 == -0.0) {
                d4 = -1.0E-4;
            }
            if (d5 == -0.0) {
                d5 = -1.0E-4;
            }
            if (d6 == -0.0) {
                d6 = -1.0E-4;
            }
            EnumDirection enumdirection;
            if (d4 < d5 && d4 < d6) {
                enumdirection = ((i > l) ? EnumDirection.WEST : EnumDirection.EAST);
                vec3d = new Vec3D(d0, vec3d.y + d8 * d4, vec3d.z + d9 * d4);
            }
            else if (d5 < d6) {
                enumdirection = ((j > i2) ? EnumDirection.DOWN : EnumDirection.UP);
                vec3d = new Vec3D(vec3d.x + d7 * d5, d2, vec3d.z + d9 * d5);
            }
            else {
                enumdirection = ((k > j2) ? EnumDirection.NORTH : EnumDirection.SOUTH);
                vec3d = new Vec3D(vec3d.x + d7 * d6, vec3d.y + d8 * d6, d3);
            }
            l = MathHelper.floor(vec3d.x) - ((enumdirection == EnumDirection.EAST) ? 1 : 0);
            i2 = MathHelper.floor(vec3d.y) - ((enumdirection == EnumDirection.UP) ? 1 : 0);
            j2 = MathHelper.floor(vec3d.z) - ((enumdirection == EnumDirection.SOUTH) ? 1 : 0);
            blockposition = new BlockPosition(l, i2, j2);
            final IBlockData iblockdata2 = this.getTypeIfLoaded(blockposition);
            if (iblockdata2 == null) {
                return null;
            }
            final Block block2 = iblockdata2.getBlock();
            if (flag1 && iblockdata2.getMaterial() != Material.PORTAL && iblockdata2.d((IBlockAccess)this, blockposition) == Block.k) {
                continue;
            }
            if (block2.a(iblockdata2, flag)) {
                final MovingObjectPosition movingobjectposition3 = iblockdata2.a(this, blockposition, vec3d, vec3d1);
                if (movingobjectposition3 != null) {
                    return movingobjectposition3;
                }
                continue;
            }
            else {
                movingobjectposition2 = new MovingObjectPosition(MovingObjectPosition.EnumMovingObjectType.MISS, vec3d, enumdirection, blockposition);
            }
        }
        return flag2 ? movingobjectposition2 : null;
    }
    
    public void a(@Nullable final EntityHuman entityhuman, final BlockPosition blockposition, final SoundEffect soundeffect, final SoundCategory soundcategory, final float f, final float f1) {
        this.a(entityhuman, blockposition.getX() + 0.5, blockposition.getY() + 0.5, blockposition.getZ() + 0.5, soundeffect, soundcategory, f, f1);
    }
    
    public final void sendSoundEffect(@Nullable final EntityHuman fromEntity, final double x, final double y, final double z, final SoundEffect soundeffect, final SoundCategory soundcategory, final float volume, final float pitch) {
        this.a(fromEntity, x, y, z, soundeffect, soundcategory, volume, pitch);
    }
    
    public void a(@Nullable final EntityHuman entityhuman, final double d0, final double d1, final double d2, final SoundEffect soundeffect, final SoundCategory soundcategory, final float f, final float f1) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).a(entityhuman, soundeffect, soundcategory, d0, d1, d2, f, f1);
        }
    }
    
    public void a(final double d0, final double d1, final double d2, final SoundEffect soundeffect, final SoundCategory soundcategory, final float f, final float f1, final boolean flag) {
    }
    
    public void a(final BlockPosition blockposition, @Nullable final SoundEffect soundeffect) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).a(soundeffect, blockposition);
        }
    }
    
    public void addParticle(final EnumParticle enumparticle, final double d0, final double d1, final double d2, final double d3, final double d4, final double d5, final int... aint) {
        this.a(enumparticle.c(), enumparticle.e(), d0, d1, d2, d3, d4, d5, aint);
    }
    
    public void a(final int i, final double d0, final double d1, final double d2, final double d3, final double d4, final double d5, final int... aint) {
        for (int j = 0; j < this.u.size(); ++j) {
            this.u.get(j).a(i, false, true, d0, d1, d2, d3, d4, d5, aint);
        }
    }
    
    private void a(final int i, final boolean flag, final double d0, final double d1, final double d2, final double d3, final double d4, final double d5, final int... aint) {
        for (int j = 0; j < this.u.size(); ++j) {
            this.u.get(j).a(i, flag, d0, d1, d2, d3, d4, d5, aint);
        }
    }
    
    public boolean strikeLightning(final Entity entity) {
        this.j.add(entity);
        return true;
    }
    
    public boolean addEntity(final Entity entity) {
        return this.addEntity(entity, CreatureSpawnEvent.SpawnReason.DEFAULT);
    }
    
    public boolean addEntity(final Entity entity, final CreatureSpawnEvent.SpawnReason spawnReason) {
        AsyncCatcher.catchOp("entity add");
        if (entity == null) {
            return false;
        }
        if (entity.valid) {
            MinecraftServer.LOGGER.error("Attempted Double World add on " + entity, new Throwable());
            return true;
        }
        Cancellable event = null;
        if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
            final boolean isAnimal = entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal || entity instanceof EntityGolem;
            final boolean isMonster = entity instanceof EntityMonster || entity instanceof EntityGhast || entity instanceof EntitySlime;
            final boolean isNpc = entity instanceof NPC;
            if (spawnReason != CreatureSpawnEvent.SpawnReason.CUSTOM && ((isAnimal && !this.allowAnimals) || (isMonster && !this.allowMonsters) || (isNpc && !this.getServer().getServer().getSpawnNPCs()))) {
                entity.dead = true;
                return false;
            }
            event = (Cancellable)CraftEventFactory.callCreatureSpawnEvent((EntityLiving)entity, spawnReason);
        }
        else if (entity instanceof EntityItem) {
            event = (Cancellable)CraftEventFactory.callItemSpawnEvent((EntityItem)entity);
        }
        else if (entity.getBukkitEntity() instanceof Projectile) {
            event = (Cancellable)CraftEventFactory.callProjectileLaunchEvent(entity);
        }
        else if (entity.getBukkitEntity() instanceof Vehicle) {
            event = (Cancellable)CraftEventFactory.callVehicleCreateEvent(entity);
        }
        else if (entity instanceof EntityExperienceOrb) {
            final EntityExperienceOrb xp = (EntityExperienceOrb)entity;
            final double radius = this.spigotConfig.expMerge;
            if (radius > 0.0) {
                final int maxValue = this.paperConfig.expMergeMaxValue;
                final boolean mergeUnconditionally = this.paperConfig.expMergeMaxValue <= 0;
                if (mergeUnconditionally || xp.value < maxValue) {
                    final List<Entity> entities = this.getEntities(entity, entity.getBoundingBox().grow(radius, radius, radius));
                    for (final Entity e : entities) {
                        if (e instanceof EntityExperienceOrb) {
                            final EntityExperienceOrb loopItem = (EntityExperienceOrb)e;
                            if (loopItem.dead || (maxValue > 0 && loopItem.value >= maxValue) || !new ExperienceOrbMergeEvent((ExperienceOrb)entity.getBukkitEntity(), (ExperienceOrb)loopItem.getBukkitEntity()).callEvent()) {
                                continue;
                            }
                            final long newTotal = xp.value + loopItem.value;
                            if ((int)newTotal < 0) {
                                continue;
                            }
                            if (maxValue > 0 && newTotal > maxValue) {
                                loopItem.value = (int)(newTotal - maxValue);
                                xp.value = maxValue;
                            }
                            else {
                                final EntityExperienceOrb entityExperienceOrb = xp;
                                entityExperienceOrb.value += loopItem.value;
                                loopItem.die();
                            }
                        }
                    }
                }
            }
        }
        if (event != null && (event.isCancelled() || entity.dead)) {
            entity.dead = true;
            return false;
        }
        final int i = MathHelper.floor(entity.locX / 16.0);
        final int j = MathHelper.floor(entity.locZ / 16.0);
        boolean flag = true;
        if (entity.origin == null) {
            entity.origin = entity.getBukkitEntity().getLocation();
        }
        if (entity instanceof EntityHuman) {
            flag = true;
        }
        if (!flag && !this.isChunkLoaded(i, j, false)) {
            return false;
        }
        if (entity instanceof EntityHuman) {
            final EntityHuman entityhuman = (EntityHuman)entity;
            this.players.add(entityhuman);
            this.everyoneSleeping();
        }
        this.getChunkAt(i, j).a(entity);
        if (entity.dead) {
            return false;
        }
        this.entityList.add(entity);
        this.b(entity);
        return true;
    }
    
    protected void b(final Entity entity) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).a(entity);
        }
        entity.valid = true;
        entity.shouldBeRemoved = false;
        new EntityAddToWorldEvent((org.bukkit.entity.Entity)entity.getBukkitEntity()).callEvent();
    }
    
    protected void c(final Entity entity) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).b(entity);
        }
        new EntityRemoveFromWorldEvent((org.bukkit.entity.Entity)entity.getBukkitEntity()).callEvent();
        entity.valid = false;
    }
    
    public void kill(final Entity entity) {
        AsyncCatcher.catchOp("entity kill");
        if (entity.isVehicle()) {
            entity.ejectPassengers();
        }
        if (entity.isPassenger()) {
            entity.stopRiding();
        }
        entity.die();
        if (entity instanceof EntityHuman) {
            this.players.remove(entity);
            for (final Object o : this.worldMaps.c) {
                if (o instanceof WorldMap) {
                    final WorldMap map = (WorldMap)o;
                    map.k.remove(entity);
                    final Iterator<WorldMap.WorldMapHumanTracker> iter = map.i.iterator();
                    while (iter.hasNext()) {
                        if (iter.next().trackee == entity) {
                            map.decorations.remove(entity.getUniqueID());
                            iter.remove();
                        }
                    }
                }
            }
            this.everyoneSleeping();
            this.c(entity);
        }
    }
    
    public void removeEntity(final Entity entity) {
        AsyncCatcher.catchOp("entity remove");
        entity.b(false);
        entity.die();
        if (entity instanceof EntityHuman) {
            this.players.remove(entity);
            this.everyoneSleeping();
        }
        final int i = entity.ab;
        final int j = entity.ad;
        if (entity.aa && this.isChunkLoaded(i, j, true)) {
            this.getChunkAt(i, j).b(entity);
        }
        entity.shouldBeRemoved = true;
        if (!this.guardEntityList) {
            final int index = this.entityList.indexOf(entity);
            if (index != -1) {
                if (index <= this.tickPosition) {
                    --this.tickPosition;
                }
                this.entityList.remove(index);
            }
        }
        this.c(entity);
    }
    
    public void addIWorldAccess(final IWorldAccess iworldaccess) {
        this.u.add(iworldaccess);
    }
    
    private boolean a(@Nullable final Entity entity, final AxisAlignedBB axisalignedbb, final boolean flag, @Nullable final List<AxisAlignedBB> list) {
        final int i = MathHelper.floor(axisalignedbb.a) - 1;
        final int j = MathHelper.f(axisalignedbb.d) + 1;
        final int k = MathHelper.floor(axisalignedbb.b) - 1;
        final int l = MathHelper.f(axisalignedbb.e) + 1;
        final int i2 = MathHelper.floor(axisalignedbb.c) - 1;
        final int j2 = MathHelper.f(axisalignedbb.f) + 1;
        final WorldBorder worldborder = this.getWorldBorder();
        final boolean flag2 = entity != null && entity.bz();
        final boolean flag3 = entity != null && this.g(entity);
        final IBlockData iblockdata = Blocks.STONE.getBlockData();
        final BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();
        try {
            for (int k2 = i; k2 < j; ++k2) {
                for (int l2 = i2; l2 < j2; ++l2) {
                    final boolean flag4 = k2 == i || k2 == j - 1;
                    final boolean flag5 = l2 == i2 || l2 == j2 - 1;
                    if ((!flag4 || !flag5) && this.isLoaded((BlockPosition)blockposition_pooledblockposition.f(k2, 64, l2))) {
                        for (int i3 = k; i3 < l; ++i3) {
                            if ((!flag4 && !flag5) || i3 != l - 1) {
                                if (flag) {
                                    if (k2 < -30000000 || k2 >= 30000000 || l2 < -30000000 || l2 >= 30000000) {
                                        final boolean flag6 = true;
                                        return flag6;
                                    }
                                }
                                else if (entity != null && flag2 == flag3) {
                                    entity.k(!flag3);
                                }
                                blockposition_pooledblockposition.f(k2, i3, l2);
                                IBlockData iblockdata2;
                                if (!flag && !worldborder.a((BlockPosition)blockposition_pooledblockposition) && flag3) {
                                    iblockdata2 = iblockdata;
                                }
                                else {
                                    iblockdata2 = this.getType((BlockPosition)blockposition_pooledblockposition);
                                }
                                iblockdata2.a(this, (BlockPosition)blockposition_pooledblockposition, axisalignedbb, (List)list, entity, false);
                                if (flag && !list.isEmpty()) {
                                    final boolean flag7 = true;
                                    return flag7;
                                }
                            }
                        }
                    }
                }
            }
            return !list.isEmpty();
        }
        finally {
            blockposition_pooledblockposition.t();
        }
    }
    
    public List<AxisAlignedBB> getCubes(@Nullable final Entity entity, final AxisAlignedBB axisalignedbb) {
        final ArrayList arraylist = Lists.newArrayList();
        this.a(entity, axisalignedbb, false, arraylist);
        if (entity != null) {
            if (entity instanceof EntityArmorStand && !entity.world.paperConfig.armorStandEntityLookups) {
                return (List<AxisAlignedBB>)arraylist;
            }
            final List list = this.getEntities(entity, axisalignedbb.g(0.25));
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                if (!entity.x(entity2)) {
                    AxisAlignedBB axisalignedbb2 = entity2.al();
                    if (axisalignedbb2 != null && axisalignedbb2.c(axisalignedbb)) {
                        arraylist.add(axisalignedbb2);
                    }
                    axisalignedbb2 = entity.j(entity2);
                    if (axisalignedbb2 != null && axisalignedbb2.c(axisalignedbb)) {
                        arraylist.add(axisalignedbb2);
                    }
                }
            }
        }
        return (List<AxisAlignedBB>)arraylist;
    }
    
    public boolean g(final Entity entity) {
        double d0 = this.P.b();
        double d2 = this.P.c();
        double d3 = this.P.d();
        double d4 = this.P.e();
        if (entity.bz()) {
            ++d0;
            ++d2;
            --d3;
            --d4;
        }
        else {
            --d0;
            --d2;
            ++d3;
            ++d4;
        }
        return entity.locX > d0 && entity.locX < d3 && entity.locZ > d2 && entity.locZ < d4;
    }
    
    public boolean a(final AxisAlignedBB axisalignedbb) {
        return this.a(null, axisalignedbb, true, Lists.newArrayList());
    }
    
    public int a(final float f) {
        final float f2 = this.c(f);
        float f3 = 1.0f - (MathHelper.cos(f2 * 6.2831855f) * 2.0f + 0.5f);
        f3 = MathHelper.a(f3, 0.0f, 1.0f);
        f3 = 1.0f - f3;
        f3 *= (float)(1.0 - this.j(f) * 5.0f / 16.0);
        f3 *= (float)(1.0 - this.h(f) * 5.0f / 16.0);
        f3 = 1.0f - f3;
        return (int)(f3 * 11.0f);
    }
    
    public float c(final float f) {
        return this.worldProvider.a(this.worldData.getDayTime(), f);
    }
    
    public float G() {
        return WorldProvider.a[this.worldProvider.a(this.worldData.getDayTime())];
    }
    
    public float d(final float f) {
        final float f2 = this.c(f);
        return f2 * 6.2831855f;
    }
    
    public BlockPosition p(final BlockPosition blockposition) {
        return this.getChunkAtWorldCoords(blockposition).f(blockposition);
    }
    
    public BlockPosition q(final BlockPosition blockposition) {
        final Chunk chunk = this.getChunkAtWorldCoords(blockposition);
        BlockPosition blockposition2;
        BlockPosition blockposition3;
        for (blockposition2 = new BlockPosition(blockposition.getX(), chunk.g() + 16, blockposition.getZ()); blockposition2.getY() >= 0; blockposition2 = blockposition3) {
            blockposition3 = blockposition2.down();
            final Material material = chunk.getBlockData(blockposition3).getMaterial();
            if (material.isSolid() && material != Material.LEAVES) {
                break;
            }
        }
        return blockposition2;
    }
    
    public boolean b(final BlockPosition blockposition, final Block block) {
        return true;
    }
    
    public void a(final BlockPosition blockposition, final Block block, final int i) {
    }
    
    public void a(final BlockPosition blockposition, final Block block, final int i, final int j) {
    }
    
    public void b(final BlockPosition blockposition, final Block block, final int i, final int j) {
    }
    
    public void tickEntities() {
        this.methodProfiler.a("entities");
        this.methodProfiler.a("global");
        for (int i = 0; i < this.j.size(); ++i) {
            final Entity entity = this.j.get(i);
            if (entity != null) {
                try {
                    final Entity entity3 = entity;
                    ++entity3.ticksLived;
                    entity.B_();
                }
                catch (Throwable throwable) {
                    final CrashReport crashreport = CrashReport.a(throwable, "Ticking entity");
                    final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity being ticked");
                    if (entity == null) {
                        crashreportsystemdetails.a("Entity", (Object)"~~NULL~~");
                    }
                    else {
                        entity.appendEntityCrashDetails(crashreportsystemdetails);
                    }
                    throw new ReportedException(crashreport);
                }
                if (entity.dead) {
                    this.j.remove(i--);
                }
            }
        }
        this.methodProfiler.c("remove");
        this.timings.entityRemoval.startTiming();
        this.entityList.removeAll(this.f);
        for (final Entity e : this.f) {
            final Chunk chunk = e.isAddedToChunk() ? e.getCurrentChunk() : null;
            if (chunk != null) {
                chunk.removeEntity(e);
            }
        }
        for (final Entity e : this.f) {
            this.c(e);
        }
        this.f.clear();
        this.l();
        this.timings.entityRemoval.stopTiming();
        this.methodProfiler.c("regular");
        ActivationRange.activateEntities(this);
        this.timings.entityTick.startTiming();
        this.guardEntityList = true;
        TimingHistory.entityTicks += this.entityList.size();
        final int entitiesThisCycle = 0;
        this.tickPosition = 0;
        while (this.tickPosition < this.entityList.size()) {
            this.tickPosition = ((this.tickPosition < this.entityList.size()) ? this.tickPosition : 0);
            final Entity entity = this.entityList.get(this.tickPosition);
            final Entity entity2 = entity.bJ();
            Label_0756: {
                if (entity2 != null) {
                    if (!entity2.dead && entity2.w(entity)) {
                        break Label_0756;
                    }
                    entity.stopRiding();
                }
                this.methodProfiler.a("tick");
                if (!entity.dead && !(entity instanceof EntityPlayer)) {
                    try {
                        entity.tickTimer.startTiming();
                        this.h(entity);
                        entity.tickTimer.stopTiming();
                    }
                    catch (Throwable throwable2) {
                        entity.tickTimer.stopTiming();
                        final String msg = "Entity threw exception at " + entity.world.getWorld().getName() + ":" + entity.locX + "," + entity.locY + "," + entity.locZ;
                        System.err.println(msg);
                        throwable2.printStackTrace();
                        this.getServer().getPluginManager().callEvent((Event)new ServerExceptionEvent((ServerException)new ServerInternalException(msg, throwable2)));
                        entity.dead = true;
                        break Label_0756;
                    }
                }
                this.methodProfiler.b();
                this.methodProfiler.a("remove");
                if (entity.dead) {
                    final Chunk chunk2 = entity.isAddedToChunk() ? entity.getCurrentChunk() : null;
                    if (chunk2 != null) {
                        chunk2.removeEntity(entity);
                    }
                    this.guardEntityList = false;
                    this.entityList.remove(this.tickPosition--);
                    this.guardEntityList = true;
                    this.c(entity);
                }
                this.methodProfiler.b();
            }
            ++this.tickPosition;
        }
        this.guardEntityList = false;
        this.timings.entityTick.stopTiming();
        this.methodProfiler.c("blockEntities");
        this.timings.tileEntityTick.startTiming();
        if (!this.tileEntityListUnload.isEmpty()) {
            final Set<TileEntity> toRemove = Collections.newSetFromMap(new IdentityHashMap<TileEntity, Boolean>());
            toRemove.addAll(this.tileEntityListUnload);
            this.tileEntityListTick.removeAll(toRemove);
            this.tileEntityListUnload.clear();
        }
        this.O = true;
        int tilesThisCycle = 0;
        this.tileTickPosition = 0;
        while (this.tileTickPosition < this.tileEntityListTick.size()) {
            this.tileTickPosition = ((this.tileTickPosition < this.tileEntityListTick.size()) ? this.tileTickPosition : 0);
            final TileEntity tileentity = this.tileEntityListTick.get(this.tileTickPosition);
            Label_1372: {
                if (tileentity == null) {
                    this.getServer().getLogger().severe("Spigot has detected a null entity and has removed it, preventing a crash");
                    --tilesThisCycle;
                    this.tileEntityListTick.remove(this.tileTickPosition--);
                }
                else {
                    if (!tileentity.y() && tileentity.u()) {
                        final BlockPosition blockposition = tileentity.getPosition();
                        final Chunk chunk3 = tileentity.getCurrentChunk();
                        boolean shouldTick = chunk3 != null;
                        if (this.paperConfig.skipEntityTickingInChunksScheduledForUnload) {
                            shouldTick = (shouldTick && !chunk3.isUnloading() && chunk3.scheduledForUnload == null);
                        }
                        if (shouldTick && this.P.a(blockposition)) {
                            try {
                                this.methodProfiler.a(() -> String.valueOf(TileEntity.a((Class)tileentity.getClass())));
                                tileentity.tickTimer.startTiming();
                                ((ITickable)tileentity).e();
                                this.methodProfiler.b();
                            }
                            catch (Throwable throwable3) {
                                final String msg2 = "TileEntity threw exception at " + tileentity.world.getWorld().getName() + ":" + tileentity.position.getX() + "," + tileentity.position.getY() + "," + tileentity.position.getZ();
                                System.err.println(msg2);
                                throwable3.printStackTrace();
                                this.getServer().getPluginManager().callEvent((Event)new ServerExceptionEvent((ServerException)new ServerInternalException(msg2, throwable3)));
                                --tilesThisCycle;
                                this.tileEntityListTick.remove(this.tileTickPosition--);
                                break Label_1372;
                            }
                            finally {
                                tileentity.tickTimer.stopTiming();
                            }
                        }
                    }
                    if (tileentity.y()) {
                        --tilesThisCycle;
                        this.tileEntityListTick.remove(this.tileTickPosition--);
                        final Chunk chunk4 = tileentity.getCurrentChunk();
                        if (chunk4 != null) {
                            chunk4.removeTileEntity(tileentity.getPosition());
                        }
                    }
                }
            }
            ++this.tileTickPosition;
        }
        this.timings.tileEntityTick.stopTiming();
        this.timings.tileEntityPending.startTiming();
        this.O = false;
        this.methodProfiler.c("pendingBlockEntities");
        if (!this.b.isEmpty()) {
            for (int i2 = 0; i2 < this.b.size(); ++i2) {
                final TileEntity tileentity2 = this.b.get(i2);
                if (!tileentity2.y() && this.isLoaded(tileentity2.getPosition())) {
                    final Chunk chunk3 = this.getChunkAtWorldCoords(tileentity2.getPosition());
                    final IBlockData iblockdata = chunk3.getBlockData(tileentity2.getPosition());
                    chunk3.a(tileentity2.getPosition(), tileentity2);
                    this.notify(tileentity2.getPosition(), iblockdata, iblockdata, 3);
                    this.a(tileentity2);
                }
            }
            this.b.clear();
        }
        this.timings.tileEntityPending.stopTiming();
        TimingHistory.tileEntityTicks += this.tileEntityListTick.size();
        this.methodProfiler.b();
        this.methodProfiler.b();
    }
    
    protected void l() {
    }
    
    public boolean a(final TileEntity tileentity) {
        final boolean flag = true;
        if (flag && tileentity instanceof ITickable && !this.tileEntityListTick.contains(tileentity)) {
            this.tileEntityListTick.add(tileentity);
        }
        if (this.isClientSide) {
            final BlockPosition blockposition = tileentity.getPosition();
            final IBlockData iblockdata = this.getType(blockposition);
            this.notify(blockposition, iblockdata, iblockdata, 2);
        }
        return flag;
    }
    
    public void b(final Collection<TileEntity> collection) {
        if (this.O) {
            this.b.addAll(collection);
        }
        else {
            for (final TileEntity tileentity : collection) {
                this.a(tileentity);
            }
        }
    }
    
    public void h(final Entity entity) {
        this.entityJoinedWorld(entity, true);
    }
    
    public void entityJoinedWorld(final Entity entity, final boolean flag) {
        if (flag && !ActivationRange.checkIfActive(entity)) {
            ++entity.ticksLived;
            entity.inactiveTick();
            return;
        }
        entity.M = entity.locX;
        entity.N = entity.locY;
        entity.O = entity.locZ;
        entity.lastYaw = entity.yaw;
        entity.lastPitch = entity.pitch;
        if (flag && entity.aa) {
            ++entity.ticksLived;
            ++TimingHistory.activatedEntityTicks;
            if (entity.isPassenger()) {
                entity.aE();
            }
            else {
                entity.B_();
                entity.postTick();
            }
        }
        this.methodProfiler.a("chunkCheck");
        if (Double.isNaN(entity.locX) || Double.isInfinite(entity.locX)) {
            entity.locX = entity.M;
        }
        if (Double.isNaN(entity.locY) || Double.isInfinite(entity.locY)) {
            entity.locY = entity.N;
        }
        if (Double.isNaN(entity.locZ) || Double.isInfinite(entity.locZ)) {
            entity.locZ = entity.O;
        }
        if (Double.isNaN(entity.pitch) || Double.isInfinite(entity.pitch)) {
            entity.pitch = entity.lastPitch;
        }
        if (Double.isNaN(entity.yaw) || Double.isInfinite(entity.yaw)) {
            entity.yaw = entity.lastYaw;
        }
        final int i = MathHelper.floor(entity.locX / 16.0);
        final int j = Math.min(15, Math.max(0, MathHelper.floor(entity.locY / 16.0)));
        final int k = MathHelper.floor(entity.locZ / 16.0);
        if (!entity.aa || entity.ab != i || entity.ac != j || entity.ad != k) {
            if (entity.aa && this.isChunkLoaded(entity.ab, entity.ad, true)) {
                this.getChunkAt(entity.ab, entity.ad).a(entity, entity.ac);
            }
            if (!entity.valid && !entity.bD() && !this.isChunkLoaded(i, k, true)) {
                entity.aa = false;
            }
            else {
                this.getChunkAt(i, k).a(entity);
            }
        }
        this.methodProfiler.b();
        if (flag && entity.aa) {
            for (final Entity entity2 : entity.bF()) {
                if (!entity2.dead && entity2.bJ() == entity) {
                    this.h(entity2);
                }
                else {
                    entity2.stopRiding();
                }
            }
        }
    }
    
    public boolean b(final AxisAlignedBB axisalignedbb) {
        return this.a(axisalignedbb, (Entity)null);
    }
    
    public boolean checkNoVisiblePlayerCollisions(final AxisAlignedBB axisalignedbb, @Nullable final Entity entity) {
        final List list = this.getEntities(null, axisalignedbb);
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity2 = list.get(i);
            if (!(entity instanceof EntityPlayer) || !(entity2 instanceof EntityPlayer) || ((EntityPlayer)entity).getBukkitEntity().canSee((Player)((EntityPlayer)entity2).getBukkitEntity())) {
                if (!entity2.dead && entity2.blocksEntitySpawning()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean a(final AxisAlignedBB axisalignedbb, @Nullable final Entity entity) {
        final List list = this.getEntities(null, axisalignedbb);
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity2 = list.get(i);
            if (!entity2.dead && entity2.i && entity2 != entity && (entity == null || entity2.x(entity))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean c(final AxisAlignedBB axisalignedbb) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.f(axisalignedbb.d);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.f(axisalignedbb.e);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.f(axisalignedbb.f);
        final BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    final IBlockData iblockdata = this.getType((BlockPosition)blockposition_pooledblockposition.f(k2, l2, i3));
                    if (iblockdata.getMaterial() != Material.AIR) {
                        blockposition_pooledblockposition.t();
                        return true;
                    }
                }
            }
        }
        blockposition_pooledblockposition.t();
        return false;
    }
    
    public boolean containsLiquid(final AxisAlignedBB axisalignedbb) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.f(axisalignedbb.d);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.f(axisalignedbb.e);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.f(axisalignedbb.f);
        final BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    final IBlockData iblockdata = this.getType((BlockPosition)blockposition_pooledblockposition.f(k2, l2, i3));
                    if (iblockdata.getMaterial().isLiquid()) {
                        blockposition_pooledblockposition.t();
                        return true;
                    }
                }
            }
        }
        blockposition_pooledblockposition.t();
        return false;
    }
    
    public boolean e(final AxisAlignedBB axisalignedbb) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.f(axisalignedbb.d);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.f(axisalignedbb.e);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.f(axisalignedbb.f);
        if (this.isAreaLoaded(i, k, i2, j, l, j2, true)) {
            final BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();
            for (int k2 = i; k2 < j; ++k2) {
                for (int l2 = k; l2 < l; ++l2) {
                    for (int i3 = i2; i3 < j2; ++i3) {
                        final Block block = this.getType((BlockPosition)blockposition_pooledblockposition.f(k2, l2, i3)).getBlock();
                        if (block == Blocks.FIRE || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA) {
                            blockposition_pooledblockposition.t();
                            return true;
                        }
                    }
                }
            }
            blockposition_pooledblockposition.t();
        }
        return false;
    }
    
    public boolean a(final AxisAlignedBB axisalignedbb, final Material material, final Entity entity) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.f(axisalignedbb.d);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.f(axisalignedbb.e);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.f(axisalignedbb.f);
        if (!this.isAreaLoaded(i, k, i2, j, l, j2, true)) {
            return false;
        }
        boolean flag = false;
        Vec3D vec3d = Vec3D.a;
        final BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    blockposition_pooledblockposition.f(k2, l2, i3);
                    final IBlockData iblockdata = this.getType((BlockPosition)blockposition_pooledblockposition);
                    final Block block = iblockdata.getBlock();
                    if (iblockdata.getMaterial() == material) {
                        final double d0 = l2 + 1 - BlockFluids.b((int)iblockdata.get((IBlockState)BlockFluids.LEVEL));
                        if (l >= d0) {
                            flag = true;
                            vec3d = block.a(this, (BlockPosition)blockposition_pooledblockposition, entity, vec3d);
                        }
                    }
                }
            }
        }
        blockposition_pooledblockposition.t();
        if (vec3d.b() > 0.0 && entity.bo()) {
            vec3d = vec3d.a();
            final double d2 = 0.014;
            entity.motX += vec3d.x * 0.014;
            entity.motY += vec3d.y * 0.014;
            entity.motZ += vec3d.z * 0.014;
        }
        return flag;
    }
    
    public boolean a(final AxisAlignedBB axisalignedbb, final Material material) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.f(axisalignedbb.d);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.f(axisalignedbb.e);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.f(axisalignedbb.f);
        final BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    if (this.getType((BlockPosition)blockposition_pooledblockposition.f(k2, l2, i3)).getMaterial() == material) {
                        blockposition_pooledblockposition.t();
                        return true;
                    }
                }
            }
        }
        blockposition_pooledblockposition.t();
        return false;
    }
    
    public Explosion explode(@Nullable final Entity entity, final double d0, final double d1, final double d2, final float f, final boolean flag) {
        return this.createExplosion(entity, d0, d1, d2, f, false, flag);
    }
    
    public Explosion createExplosion(@Nullable final Entity entity, final double d0, final double d1, final double d2, final float f, final boolean flag, final boolean flag1) {
        final Explosion explosion = new Explosion(this, entity, d0, d1, d2, f, flag, flag1);
        explosion.a();
        explosion.a(true);
        return explosion;
    }
    
    public float a(final Vec3D vec3d, final AxisAlignedBB axisalignedbb) {
        final double d0 = 1.0 / ((axisalignedbb.d - axisalignedbb.a) * 2.0 + 1.0);
        final double d2 = 1.0 / ((axisalignedbb.e - axisalignedbb.b) * 2.0 + 1.0);
        final double d3 = 1.0 / ((axisalignedbb.f - axisalignedbb.c) * 2.0 + 1.0);
        final double d4 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        final double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
        if (d0 >= 0.0 && d2 >= 0.0 && d3 >= 0.0) {
            int i = 0;
            int j = 0;
            for (float f = 0.0f; f <= 1.0f; f += (float)d0) {
                for (float f2 = 0.0f; f2 <= 1.0f; f2 += (float)d2) {
                    for (float f3 = 0.0f; f3 <= 1.0f; f3 += (float)d3) {
                        final double d6 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * f;
                        final double d7 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * f2;
                        final double d8 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * f3;
                        if (this.rayTrace(new Vec3D(d6 + d4, d7, d8 + d5), vec3d) == null) {
                            ++i;
                        }
                        ++j;
                    }
                }
            }
            return i / j;
        }
        return 0.0f;
    }
    
    public boolean douseFire(@Nullable final EntityHuman entityhuman, BlockPosition blockposition, final EnumDirection enumdirection) {
        blockposition = blockposition.shift(enumdirection);
        if (this.getType(blockposition).getBlock() == Blocks.FIRE) {
            this.a(entityhuman, 1009, blockposition, 0);
            this.setAir(blockposition);
            return true;
        }
        return false;
    }
    
    @Nullable
    public TileEntity getTileEntity(final BlockPosition blockposition) {
        if (blockposition.isInvalidYLocation()) {
            return null;
        }
        if (this.capturedTileEntities.containsKey(blockposition)) {
            return this.capturedTileEntities.get(blockposition);
        }
        TileEntity tileentity = null;
        if (this.O) {
            tileentity = this.F(blockposition);
        }
        if (tileentity == null) {
            tileentity = this.getChunkAtWorldCoords(blockposition).a(blockposition, Chunk.EnumTileEntityState.IMMEDIATE);
        }
        if (tileentity == null) {
            tileentity = this.F(blockposition);
        }
        return tileentity;
    }
    
    @Nullable
    private TileEntity F(final BlockPosition blockposition) {
        for (int i = 0; i < this.b.size(); ++i) {
            final TileEntity tileentity = this.b.get(i);
            if (!tileentity.y() && tileentity.getPosition().equals((Object)blockposition)) {
                return tileentity;
            }
        }
        return null;
    }
    
    public void setTileEntity(final BlockPosition blockposition, @Nullable final TileEntity tileentity) {
        if (!blockposition.isInvalidYLocation() && tileentity != null && !tileentity.y()) {
            if (this.captureBlockStates) {
                tileentity.a(this);
                tileentity.setPosition(blockposition);
                this.capturedTileEntities.put(blockposition, tileentity);
                return;
            }
            if (this.O) {
                tileentity.setPosition(blockposition);
                final Iterator iterator = this.b.iterator();
                while (iterator.hasNext()) {
                    final TileEntity tileentity2 = iterator.next();
                    if (tileentity2.getPosition().equals((Object)blockposition)) {
                        tileentity2.z();
                        iterator.remove();
                    }
                }
                tileentity.a(this);
                this.b.add(tileentity);
            }
            else {
                this.getChunkAtWorldCoords(blockposition).a(blockposition, tileentity);
                this.a(tileentity);
            }
        }
    }
    
    public void s(final BlockPosition blockposition) {
        final TileEntity tileentity = this.getTileEntity(blockposition);
        if (tileentity != null && this.O) {
            tileentity.z();
            this.b.remove(tileentity);
        }
        else {
            if (tileentity != null) {
                this.b.remove(tileentity);
                this.tileEntityListTick.remove(tileentity);
            }
            this.getChunkAtWorldCoords(blockposition).d(blockposition);
        }
    }
    
    public void b(final TileEntity tileentity) {
        this.tileEntityListUnload.add(tileentity);
    }
    
    public boolean t(final BlockPosition blockposition) {
        final AxisAlignedBB axisalignedbb = this.getType(blockposition).d((IBlockAccess)this, blockposition);
        return axisalignedbb != Block.k && axisalignedbb.a() >= 1.0;
    }
    
    public boolean d(final BlockPosition blockposition, final boolean flag) {
        if (blockposition.isInvalidYLocation()) {
            return false;
        }
        final Chunk chunk = this.chunkProvider.getLoadedChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);
        if (chunk != null && !chunk.isEmpty()) {
            final IBlockData iblockdata = this.getType(blockposition);
            return iblockdata.getMaterial().k() && iblockdata.g();
        }
        return flag;
    }
    
    public void J() {
        final int i = this.a(1.0f);
        if (i != this.L) {
            this.L = i;
        }
    }
    
    public void setSpawnFlags(final boolean flag, final boolean flag1) {
        this.allowMonsters = flag;
        this.allowAnimals = flag1;
    }
    
    public void doTick() {
        this.t();
    }
    
    protected void K() {
        if (this.worldData.hasStorm()) {
            this.o = 1.0f;
            if (this.worldData.isThundering()) {
                this.q = 1.0f;
            }
        }
    }
    
    protected void t() {
        if (this.worldProvider.m() && !this.isClientSide) {
            final boolean flag = this.getGameRules().getBoolean("doWeatherCycle");
            if (flag) {
                int i = this.worldData.z();
                if (i > 0) {
                    --i;
                    this.worldData.i(i);
                    this.worldData.setThunderDuration(this.worldData.isThundering() ? 1 : 2);
                    this.worldData.setWeatherDuration(this.worldData.hasStorm() ? 1 : 2);
                }
                int j = this.worldData.getThunderDuration();
                if (j <= 0) {
                    if (this.worldData.isThundering()) {
                        this.worldData.setThunderDuration(this.random.nextInt(12000) + 3600);
                    }
                    else {
                        this.worldData.setThunderDuration(this.random.nextInt(168000) + 12000);
                    }
                }
                else {
                    --j;
                    this.worldData.setThunderDuration(j);
                    if (j <= 0) {
                        this.worldData.setThundering(!this.worldData.isThundering());
                    }
                }
                int k = this.worldData.getWeatherDuration();
                if (k <= 0) {
                    if (this.worldData.hasStorm()) {
                        this.worldData.setWeatherDuration(this.random.nextInt(12000) + 12000);
                    }
                    else {
                        this.worldData.setWeatherDuration(this.random.nextInt(168000) + 12000);
                    }
                }
                else {
                    --k;
                    this.worldData.setWeatherDuration(k);
                    if (k <= 0) {
                        this.worldData.setStorm(!this.worldData.hasStorm());
                    }
                }
            }
            this.p = this.q;
            if (this.worldData.isThundering()) {
                this.q += 0.01;
            }
            else {
                this.q -= 0.01;
            }
            this.q = MathHelper.a(this.q, 0.0f, 1.0f);
            this.n = this.o;
            if (this.worldData.hasStorm()) {
                this.o += 0.01;
            }
            else {
                this.o -= 0.01;
            }
            this.o = MathHelper.a(this.o, 0.0f, 1.0f);
            for (int idx = 0; idx < this.players.size(); ++idx) {
                if (this.players.get(idx).world == this) {
                    this.players.get(idx).tickWeather();
                }
            }
        }
    }
    
    protected void j() {
    }
    
    public void a(final BlockPosition blockposition, final IBlockData iblockdata, final Random random) {
        this.d = true;
        iblockdata.getBlock().b(this, blockposition, iblockdata, random);
        this.d = false;
    }
    
    public boolean u(final BlockPosition blockposition) {
        return this.e(blockposition, false);
    }
    
    public boolean v(final BlockPosition blockposition) {
        return this.e(blockposition, true);
    }
    
    public boolean e(final BlockPosition blockposition, final boolean flag) {
        final BiomeBase biomebase = this.getBiome(blockposition);
        final float f = biomebase.a(blockposition);
        if (f >= 0.15f) {
            return false;
        }
        if (blockposition.getY() >= 0 && blockposition.getY() < 256 && this.getBrightness(EnumSkyBlock.BLOCK, blockposition) < 10) {
            final IBlockData iblockdata = this.getType(blockposition);
            final Block block = iblockdata.getBlock();
            if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && (int)iblockdata.get((IBlockState)BlockFluids.LEVEL) == 0) {
                if (!flag) {
                    return true;
                }
                final boolean flag2 = this.G(blockposition.west()) && this.G(blockposition.east()) && this.G(blockposition.north()) && this.G(blockposition.south());
                if (!flag2) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean G(final BlockPosition blockposition) {
        return this.getType(blockposition).getMaterial() == Material.WATER;
    }
    
    public boolean f(final BlockPosition blockposition, final boolean flag) {
        final BiomeBase biomebase = this.getBiome(blockposition);
        final float f = biomebase.a(blockposition);
        if (f >= 0.15f) {
            return false;
        }
        if (!flag) {
            return true;
        }
        if (blockposition.getY() >= 0 && blockposition.getY() < 256 && this.getBrightness(EnumSkyBlock.BLOCK, blockposition) < 10) {
            final IBlockData iblockdata = this.getType(blockposition);
            if (iblockdata.getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlace(this, blockposition)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean w(final BlockPosition blockposition) {
        boolean flag = false;
        if (this.worldProvider.m()) {
            flag |= this.c(EnumSkyBlock.SKY, blockposition);
        }
        flag |= this.c(EnumSkyBlock.BLOCK, blockposition);
        return flag;
    }
    
    private int a(final BlockPosition blockposition, final EnumSkyBlock enumskyblock) {
        if (enumskyblock == EnumSkyBlock.SKY && this.h(blockposition)) {
            return 15;
        }
        final IBlockData iblockdata = this.getType(blockposition);
        int i = (enumskyblock == EnumSkyBlock.SKY) ? 0 : iblockdata.d();
        int j = iblockdata.c();
        if (j >= 15 && iblockdata.d() > 0) {
            j = 1;
        }
        if (j < 1) {
            j = 1;
        }
        if (j >= 15) {
            return 0;
        }
        if (i >= 14) {
            return i;
        }
        final BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();
        try {
            for (final EnumDirection enumdirection : EnumDirection.values()) {
                blockposition_pooledblockposition.j((BaseBlockPosition)blockposition).d(enumdirection);
                final int i2 = this.getBrightness(enumskyblock, (BlockPosition)blockposition_pooledblockposition) - j;
                if (i2 > i) {
                    i = i2;
                }
                if (i >= 14) {
                    final int j2 = i;
                    return j2;
                }
            }
            return i;
        }
        finally {
            blockposition_pooledblockposition.t();
        }
    }
    
    public boolean c(final EnumSkyBlock enumskyblock, final BlockPosition blockposition) {
        final Chunk chunk = this.getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
        if (chunk == null || !chunk.areNeighborsLoaded(1)) {
            return false;
        }
        int i = 0;
        int j = 0;
        this.methodProfiler.a("getBrightness");
        final int k = this.getBrightness(enumskyblock, blockposition);
        final int l = this.a(blockposition, enumskyblock);
        final int i2 = blockposition.getX();
        final int j2 = blockposition.getY();
        final int k2 = blockposition.getZ();
        if (l > k) {
            this.J[j++] = 133152;
        }
        else if (l < k) {
            this.J[j++] = (0x20820 | k << 18);
            while (i < j) {
                final int l2 = this.J[i++];
                final int i3 = (l2 & 0x3F) - 32 + i2;
                final int j3 = (l2 >> 6 & 0x3F) - 32 + j2;
                final int k3 = (l2 >> 12 & 0x3F) - 32 + k2;
                final int l3 = l2 >> 18 & 0xF;
                final BlockPosition blockposition2 = new BlockPosition(i3, j3, k3);
                int l4 = this.getBrightness(enumskyblock, blockposition2);
                if (l4 == l3) {
                    this.a(enumskyblock, blockposition2, 0);
                    if (l3 <= 0) {
                        continue;
                    }
                    final int i4 = MathHelper.a(i3 - i2);
                    final int j4 = MathHelper.a(j3 - j2);
                    final int k4 = MathHelper.a(k3 - k2);
                    if (i4 + j4 + k4 >= 17) {
                        continue;
                    }
                    final BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();
                    for (final EnumDirection enumdirection : EnumDirection.values()) {
                        final int k5 = i3 + enumdirection.getAdjacentX();
                        final int l5 = j3 + enumdirection.getAdjacentY();
                        final int i6 = k3 + enumdirection.getAdjacentZ();
                        blockposition_pooledblockposition.f(k5, l5, i6);
                        final int j6 = Math.max(1, this.getType((BlockPosition)blockposition_pooledblockposition).c());
                        l4 = this.getBrightness(enumskyblock, (BlockPosition)blockposition_pooledblockposition);
                        if (l4 == l3 - j6 && j < this.J.length) {
                            this.J[j++] = (k5 - i2 + 32 | l5 - j2 + 32 << 6 | i6 - k2 + 32 << 12 | l3 - j6 << 18);
                        }
                    }
                    blockposition_pooledblockposition.t();
                }
            }
            i = 0;
        }
        this.methodProfiler.b();
        this.methodProfiler.a("checkedPosition < toCheckCount");
        while (i < j) {
            final int l2 = this.J[i++];
            final int i3 = (l2 & 0x3F) - 32 + i2;
            final int j3 = (l2 >> 6 & 0x3F) - 32 + j2;
            final int k3 = (l2 >> 12 & 0x3F) - 32 + k2;
            final BlockPosition blockposition3 = new BlockPosition(i3, j3, k3);
            final int k6 = this.getBrightness(enumskyblock, blockposition3);
            final int l4 = this.a(blockposition3, enumskyblock);
            if (l4 != k6) {
                this.a(enumskyblock, blockposition3, l4);
                if (l4 <= k6) {
                    continue;
                }
                final int i4 = Math.abs(i3 - i2);
                final int j4 = Math.abs(j3 - j2);
                final int k4 = Math.abs(k3 - k2);
                final boolean flag = j < this.J.length - 6;
                if (i4 + j4 + k4 >= 17 || !flag) {
                    continue;
                }
                if (this.getBrightness(enumskyblock, blockposition3.west()) < l4) {
                    this.J[j++] = i3 - 1 - i2 + 32 + (j3 - j2 + 32 << 6) + (k3 - k2 + 32 << 12);
                }
                if (this.getBrightness(enumskyblock, blockposition3.east()) < l4) {
                    this.J[j++] = i3 + 1 - i2 + 32 + (j3 - j2 + 32 << 6) + (k3 - k2 + 32 << 12);
                }
                if (this.getBrightness(enumskyblock, blockposition3.down()) < l4) {
                    this.J[j++] = i3 - i2 + 32 + (j3 - 1 - j2 + 32 << 6) + (k3 - k2 + 32 << 12);
                }
                if (this.getBrightness(enumskyblock, blockposition3.up()) < l4) {
                    this.J[j++] = i3 - i2 + 32 + (j3 + 1 - j2 + 32 << 6) + (k3 - k2 + 32 << 12);
                }
                if (this.getBrightness(enumskyblock, blockposition3.north()) < l4) {
                    this.J[j++] = i3 - i2 + 32 + (j3 - j2 + 32 << 6) + (k3 - 1 - k2 + 32 << 12);
                }
                if (this.getBrightness(enumskyblock, blockposition3.south()) >= l4) {
                    continue;
                }
                this.J[j++] = i3 - i2 + 32 + (j3 - j2 + 32 << 6) + (k3 + 1 - k2 + 32 << 12);
            }
        }
        this.methodProfiler.b();
        return true;
    }
    
    public boolean a(final boolean flag) {
        return false;
    }
    
    @Nullable
    public List<NextTickListEntry> a(final Chunk chunk, final boolean flag) {
        return null;
    }
    
    @Nullable
    public List<NextTickListEntry> a(final StructureBoundingBox structureboundingbox, final boolean flag) {
        return null;
    }
    
    public List<Entity> getEntities(@Nullable final Entity entity, final AxisAlignedBB axisalignedbb) {
        return this.getEntities(entity, axisalignedbb, (Predicate<? super Entity>)IEntitySelector.e);
    }
    
    public List<Entity> getEntities(@Nullable final Entity entity, final AxisAlignedBB axisalignedbb, @Nullable final Predicate<? super Entity> predicate) {
        final ArrayList arraylist = Lists.newArrayList();
        final int i = MathHelper.floor((axisalignedbb.a - 2.0) / 16.0);
        final int j = MathHelper.floor((axisalignedbb.d + 2.0) / 16.0);
        final int k = MathHelper.floor((axisalignedbb.c - 2.0) / 16.0);
        final int l = MathHelper.floor((axisalignedbb.f + 2.0) / 16.0);
        for (int i2 = i; i2 <= j; ++i2) {
            for (int j2 = k; j2 <= l; ++j2) {
                if (this.isChunkLoaded(i2, j2, true)) {
                    this.getChunkAt(i2, j2).a(entity, axisalignedbb, (List)arraylist, (Predicate)predicate);
                }
            }
        }
        return (List<Entity>)arraylist;
    }
    
    public <T extends Entity> List<T> a(final Class<? extends T> oclass, final Predicate<? super T> predicate) {
        final ArrayList arraylist = Lists.newArrayList();
        for (final Entity entity : this.entityList) {
            if (entity.shouldBeRemoved) {
                continue;
            }
            if (!oclass.isAssignableFrom(entity.getClass()) || !predicate.apply((Object)entity)) {
                continue;
            }
            arraylist.add(entity);
        }
        return (List<T>)arraylist;
    }
    
    public <T extends Entity> List<T> b(final Class<? extends T> oclass, final Predicate<? super T> predicate) {
        final ArrayList arraylist = Lists.newArrayList();
        for (final Entity entity : this.players) {
            if (oclass.isAssignableFrom(entity.getClass()) && predicate.apply((Object)entity)) {
                arraylist.add(entity);
            }
        }
        return (List<T>)arraylist;
    }
    
    public <T extends Entity> List<T> a(final Class<? extends T> oclass, final AxisAlignedBB axisalignedbb) {
        return this.a(oclass, axisalignedbb, (com.google.common.base.Predicate<? super T>)IEntitySelector.e);
    }
    
    public <T extends Entity> List<T> a(final Class<? extends T> oclass, final AxisAlignedBB axisalignedbb, @Nullable final Predicate<? super T> predicate) {
        final int i = MathHelper.floor((axisalignedbb.a - 2.0) / 16.0);
        final int j = MathHelper.f((axisalignedbb.d + 2.0) / 16.0);
        final int k = MathHelper.floor((axisalignedbb.c - 2.0) / 16.0);
        final int l = MathHelper.f((axisalignedbb.f + 2.0) / 16.0);
        final ArrayList arraylist = Lists.newArrayList();
        for (int i2 = i; i2 < j; ++i2) {
            for (int j2 = k; j2 < l; ++j2) {
                if (this.isChunkLoaded(i2, j2, true)) {
                    this.getChunkAt(i2, j2).a((Class)oclass, axisalignedbb, (List)arraylist, (Predicate)predicate);
                }
            }
        }
        return (List<T>)arraylist;
    }
    
    @Nullable
    public <T extends Entity> T a(final Class<? extends T> oclass, final AxisAlignedBB axisalignedbb, final T t0) {
        final List list = this.a((Class<? extends Entity>)oclass, axisalignedbb);
        Entity entity = null;
        double d0 = Double.MAX_VALUE;
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity2 = list.get(i);
            if (entity2 != t0 && IEntitySelector.e.apply((Object)entity2)) {
                final double d2 = t0.h(entity2);
                if (d2 <= d0) {
                    entity = entity2;
                    d0 = d2;
                }
            }
        }
        return (T)entity;
    }
    
    @Nullable
    public Entity getEntity(final int i) {
        return (Entity)this.entitiesById.get(i);
    }
    
    public void b(final BlockPosition blockposition, final TileEntity tileentity) {
        if (this.isLoaded(blockposition)) {
            this.getChunkAtWorldCoords(blockposition).markDirty();
        }
    }
    
    public int a(final Class<?> oclass) {
        int i = 0;
        for (final Entity entity : this.entityList) {
            if (entity.shouldBeRemoved) {
                continue;
            }
            if (entity instanceof EntityInsentient) {
                final EntityInsentient entityinsentient = (EntityInsentient)entity;
                if (entityinsentient.isTypeNotPersistent() && entityinsentient.isPersistent()) {
                    continue;
                }
            }
            if (!oclass.isAssignableFrom(entity.getClass())) {
                continue;
            }
            ++i;
        }
        return i;
    }
    
    public void addChunkEntities(final Collection<Entity> collection) {
        this.a(collection);
    }
    
    public void a(final Collection<Entity> collection) {
        AsyncCatcher.catchOp("entity world add");
        for (final Entity entity : collection) {
            if (entity != null && !entity.dead) {
                if (entity.valid) {
                    continue;
                }
                this.entityList.add(entity);
                this.b(entity);
            }
        }
    }
    
    public void c(final Collection<Entity> collection) {
        this.f.addAll(collection);
    }
    
    public boolean a(final Block block, final BlockPosition blockposition, final boolean flag, final EnumDirection enumdirection, @Nullable final Entity entity) {
        final IBlockData iblockdata = this.getType(blockposition);
        final AxisAlignedBB axisalignedbb = flag ? null : block.getBlockData().d((IBlockAccess)this, blockposition);
        final boolean defaultReturn = (axisalignedbb == Block.k || this.checkNoVisiblePlayerCollisions(axisalignedbb.a(blockposition), entity)) && ((iblockdata.getMaterial() == Material.ORIENTABLE && block == Blocks.ANVIL) || (iblockdata.getMaterial().isReplaceable() && block.canPlace(this, blockposition, enumdirection)));
        final BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftMagicNumbers.getId(block), defaultReturn);
        this.getServer().getPluginManager().callEvent((Event)event);
        return event.isBuildable();
    }
    
    public int getSeaLevel() {
        return this.a;
    }
    
    public void b(final int i) {
        this.a = i;
    }
    
    public int getBlockPower(final BlockPosition blockposition, final EnumDirection enumdirection) {
        return this.getType(blockposition).b((IBlockAccess)this, blockposition, enumdirection);
    }
    
    public WorldType N() {
        return this.worldData.getType();
    }
    
    public int getBlockPower(final BlockPosition blockposition) {
        final byte b0 = 0;
        int i = Math.max(b0, this.getBlockPower(blockposition.down(), EnumDirection.DOWN));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getBlockPower(blockposition.up(), EnumDirection.UP));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getBlockPower(blockposition.north(), EnumDirection.NORTH));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getBlockPower(blockposition.south(), EnumDirection.SOUTH));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getBlockPower(blockposition.west(), EnumDirection.WEST));
        if (i >= 15) {
            return i;
        }
        i = Math.max(i, this.getBlockPower(blockposition.east(), EnumDirection.EAST));
        return (i >= 15) ? i : i;
    }
    
    public boolean isBlockFacePowered(final BlockPosition blockposition, final EnumDirection enumdirection) {
        return this.getBlockFacePower(blockposition, enumdirection) > 0;
    }
    
    public int getBlockFacePower(final BlockPosition blockposition, final EnumDirection enumdirection) {
        final IBlockData iblockdata = this.getType(blockposition);
        return iblockdata.l() ? this.getBlockPower(blockposition) : iblockdata.a((IBlockAccess)this, blockposition, enumdirection);
    }
    
    public boolean isBlockIndirectlyPowered(final BlockPosition blockposition) {
        return this.getBlockFacePower(blockposition.down(), EnumDirection.DOWN) > 0 || this.getBlockFacePower(blockposition.up(), EnumDirection.UP) > 0 || this.getBlockFacePower(blockposition.north(), EnumDirection.NORTH) > 0 || this.getBlockFacePower(blockposition.south(), EnumDirection.SOUTH) > 0 || this.getBlockFacePower(blockposition.west(), EnumDirection.WEST) > 0 || this.getBlockFacePower(blockposition.east(), EnumDirection.EAST) > 0;
    }
    
    public int z(final BlockPosition blockposition) {
        int i = 0;
        for (final EnumDirection enumdirection : EnumDirection.values()) {
            final int l = this.getBlockFacePower(blockposition.shift(enumdirection), enumdirection);
            if (l >= 15) {
                return 15;
            }
            if (l > i) {
                i = l;
            }
        }
        return i;
    }
    
    @Nullable
    public EntityHuman findNearbyPlayer(final Entity entity, final double d0) {
        return this.a(entity.locX, entity.locY, entity.locZ, d0, false);
    }
    
    @Nullable
    public EntityHuman b(final Entity entity, final double d0) {
        return this.a(entity.locX, entity.locY, entity.locZ, d0, true);
    }
    
    @Nullable
    public EntityHuman a(final double d0, final double d1, final double d2, final double d3, final boolean flag) {
        final Predicate predicate = flag ? IEntitySelector.d : IEntitySelector.e;
        return this.a(d0, d1, d2, d3, (Predicate<Entity>)predicate);
    }
    
    @Nullable
    public EntityHuman a(final double d0, final double d1, final double d2, final double d3, final Predicate<Entity> predicate) {
        double d4 = -1.0;
        EntityHuman entityhuman = null;
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityHuman entityhuman2 = this.players.get(i);
            if (entityhuman2 != null) {
                if (!entityhuman2.dead) {
                    if (predicate.apply((Object)entityhuman2)) {
                        final double d5 = entityhuman2.d(d0, d1, d2);
                        if ((d3 < 0.0 || d5 < d3 * d3) && (d4 == -1.0 || d5 < d4)) {
                            d4 = d5;
                            entityhuman = entityhuman2;
                        }
                    }
                }
            }
        }
        return entityhuman;
    }
    
    public boolean isPlayerNearby(final double d0, final double d1, final double d2, final double d3) {
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityHuman entityhuman = this.players.get(i);
            if (IEntitySelector.e.apply((Object)entityhuman) && entityhuman.affectsSpawning) {
                final double d4 = entityhuman.d(d0, d1, d2);
                if (d3 < 0.0 || d4 < d3 * d3) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Nullable
    public EntityHuman a(final Entity entity, final double d0, final double d1) {
        return this.a(entity.locX, entity.locY, entity.locZ, d0, d1, null, null);
    }
    
    @Nullable
    public EntityHuman a(final BlockPosition blockposition, final double d0, final double d1) {
        return this.a(blockposition.getX() + 0.5f, blockposition.getY() + 0.5f, blockposition.getZ() + 0.5f, d0, d1, null, null);
    }
    
    @Nullable
    public EntityHuman a(final double d0, final double d1, final double d2, final double d3, final double d4, @Nullable final Function<EntityHuman, Double> function, @Nullable final Predicate<EntityHuman> predicate) {
        double d5 = -1.0;
        EntityHuman entityhuman = null;
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityHuman entityhuman2 = this.players.get(i);
            if (!entityhuman2.abilities.isInvulnerable && entityhuman2.isAlive() && !entityhuman2.isSpectator() && (predicate == null || predicate.apply((Object)entityhuman2))) {
                final double d6 = entityhuman2.d(d0, entityhuman2.locY, d2);
                double d7 = d3;
                if (entityhuman2.isSneaking()) {
                    d7 = d3 * 0.800000011920929;
                }
                if (entityhuman2.isInvisible()) {
                    float f = entityhuman2.cW();
                    if (f < 0.1f) {
                        f = 0.1f;
                    }
                    d7 *= 0.7f * f;
                }
                if (function != null) {
                    d7 *= (double)MoreObjects.firstNonNull(function.apply((Object)entityhuman2), (Object)1.0);
                }
                if ((d4 < 0.0 || Math.abs(entityhuman2.locY - d1) < d4 * d4) && (d3 < 0.0 || d6 < d7 * d7) && (d5 == -1.0 || d6 < d5)) {
                    d5 = d6;
                    entityhuman = entityhuman2;
                }
            }
        }
        return entityhuman;
    }
    
    @Nullable
    public EntityHuman a(final String s) {
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityHuman entityhuman = this.players.get(i);
            if (s.equals(entityhuman.getName())) {
                return entityhuman;
            }
        }
        return null;
    }
    
    @Nullable
    public EntityHuman b(final UUID uuid) {
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityHuman entityhuman = this.players.get(i);
            if (uuid.equals(entityhuman.getUniqueID())) {
                return entityhuman;
            }
        }
        return null;
    }
    
    public void checkSession() throws ExceptionWorldConflict {
        this.dataManager.checkSession();
    }
    
    public long getSeed() {
        return this.worldData.getSeed();
    }
    
    public long getTime() {
        return this.worldData.getTime();
    }
    
    public long getDayTime() {
        return this.worldData.getDayTime();
    }
    
    public void setDayTime(final long i) {
        this.worldData.setDayTime(i);
    }
    
    public BlockPosition getSpawn() {
        BlockPosition blockposition = new BlockPosition(this.worldData.b(), this.worldData.c(), this.worldData.d());
        if (!this.getWorldBorder().a(blockposition)) {
            blockposition = this.getHighestBlockYAt(new BlockPosition(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
        }
        return blockposition;
    }
    
    public void A(final BlockPosition blockposition) {
        this.worldData.setSpawn(blockposition);
    }
    
    public boolean a(final EntityHuman entityhuman, final BlockPosition blockposition) {
        return true;
    }
    
    public void broadcastEntityEffect(final Entity entity, final byte b0) {
    }
    
    public IChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public void playBlockAction(final BlockPosition blockposition, final Block block, final int i, final int j) {
        this.getType(blockposition).a(this, blockposition, i, j);
    }
    
    public IDataManager getDataManager() {
        return this.dataManager;
    }
    
    public WorldData getWorldData() {
        return this.worldData;
    }
    
    public GameRules getGameRules() {
        return this.worldData.w();
    }
    
    public void everyoneSleeping() {
    }
    
    public void checkSleepStatus() {
        if (!this.isClientSide) {
            this.everyoneSleeping();
        }
    }
    
    public float h(final float f) {
        return (this.p + (this.q - this.p) * f) * this.j(f);
    }
    
    public float j(final float f) {
        return this.n + (this.o - this.n) * f;
    }
    
    public boolean X() {
        return this.h(1.0f) > 0.9;
    }
    
    public boolean isRaining() {
        return this.j(1.0f) > 0.2;
    }
    
    public boolean isRainingAt(final BlockPosition blockposition) {
        if (!this.isRaining()) {
            return false;
        }
        if (!this.h(blockposition)) {
            return false;
        }
        if (this.p(blockposition).getY() > blockposition.getY()) {
            return false;
        }
        final BiomeBase biomebase = this.getBiome(blockposition);
        return !biomebase.c() && !this.f(blockposition, false) && biomebase.d();
    }
    
    public boolean C(final BlockPosition blockposition) {
        final BiomeBase biomebase = this.getBiome(blockposition);
        return biomebase.e();
    }
    
    @Nullable
    public PersistentCollection Z() {
        return this.worldMaps;
    }
    
    public void a(final String s, final PersistentBase persistentbase) {
        this.worldMaps.a(s, persistentbase);
    }
    
    @Nullable
    public PersistentBase a(final Class<? extends PersistentBase> oclass, final String s) {
        return this.worldMaps.get((Class)oclass, s);
    }
    
    public int b(final String s) {
        return this.worldMaps.a(s);
    }
    
    public void a(final int i, final BlockPosition blockposition, final int j) {
        for (int k = 0; k < this.u.size(); ++k) {
            this.u.get(k).a(i, blockposition, j);
        }
    }
    
    public void triggerEffect(final int i, final BlockPosition blockposition, final int j) {
        this.a(null, i, blockposition, j);
    }
    
    public void a(@Nullable final EntityHuman entityhuman, final int i, final BlockPosition blockposition, final int j) {
        try {
            for (int k = 0; k < this.u.size(); ++k) {
                this.u.get(k).a(entityhuman, i, blockposition, j);
            }
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.a(throwable, "Playing level event");
            final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Level event being played");
            crashreportsystemdetails.a("Block coordinates", (Object)CrashReportSystemDetails.a(blockposition));
            crashreportsystemdetails.a("Event source", (Object)entityhuman);
            crashreportsystemdetails.a("Event type", (Object)i);
            crashreportsystemdetails.a("Event data", (Object)j);
            throw new ReportedException(crashreport);
        }
    }
    
    public int getHeight() {
        return 256;
    }
    
    public int ab() {
        return this.worldProvider.n() ? 128 : 256;
    }
    
    public Random a(final int i, final int j, final int k) {
        final long l = i * 341873128712L + j * 132897987541L + this.getWorldData().getSeed() + k;
        this.random.setSeed(l);
        return this.random;
    }
    
    public CrashReportSystemDetails a(final CrashReport crashreport) {
        final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Affected level", 1);
        crashreportsystemdetails.a("Level name", (Object)((this.worldData == null) ? "????" : this.worldData.getName()));
        crashreportsystemdetails.a("All players", (CrashReportCallable)new CrashReportCallable() {
            public String a() {
                return World.this.players.size() + " total; " + World.this.players;
            }
            
            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.a("Chunk stats", (CrashReportCallable)new CrashReportCallable() {
            public String a() {
                return World.this.chunkProvider.getName();
            }
            
            public Object call() throws Exception {
                return this.a();
            }
        });
        try {
            this.worldData.a(crashreportsystemdetails);
        }
        catch (Throwable throwable) {
            crashreportsystemdetails.a("Level Data Unobtainable", throwable);
        }
        return crashreportsystemdetails;
    }
    
    public void c(final int i, final BlockPosition blockposition, final int j) {
        for (int k = 0; k < this.u.size(); ++k) {
            final IWorldAccess iworldaccess = this.u.get(k);
            iworldaccess.b(i, blockposition, j);
        }
    }
    
    public Calendar ae() {
        if (this.getTime() % 600L == 0L) {
            this.N.setTimeInMillis(MinecraftServer.aw());
        }
        return this.N;
    }
    
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }
    
    public void updateAdjacentComparators(final BlockPosition blockposition, final Block block) {
        for (final EnumDirection enumdirection : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
            BlockPosition blockposition2 = blockposition.shift(enumdirection);
            if (this.isLoaded(blockposition2)) {
                IBlockData iblockdata = this.getType(blockposition2);
                if (Blocks.UNPOWERED_COMPARATOR.D(iblockdata)) {
                    iblockdata.doPhysics(this, blockposition2, block, blockposition);
                }
                else {
                    if (!iblockdata.l()) {
                        continue;
                    }
                    blockposition2 = blockposition2.shift(enumdirection);
                    iblockdata = this.getType(blockposition2);
                    if (!Blocks.UNPOWERED_COMPARATOR.D(iblockdata)) {
                        continue;
                    }
                    iblockdata.doPhysics(this, blockposition2, block, blockposition);
                }
            }
        }
    }
    
    public DifficultyDamageScaler D(final BlockPosition blockposition) {
        long i = 0L;
        float f = 0.0f;
        if (this.isLoaded(blockposition)) {
            f = this.G();
            i = this.getChunkAtWorldCoords(blockposition).x();
        }
        return new DifficultyDamageScaler(this.getDifficulty(), this.getDayTime(), i, f);
    }
    
    public EnumDifficulty getDifficulty() {
        return this.getWorldData().getDifficulty();
    }
    
    public int ah() {
        return this.L;
    }
    
    public void c(final int i) {
        this.L = i;
    }
    
    public void d(final int i) {
        this.M = i;
    }
    
    public PersistentVillage ak() {
        return this.villages;
    }
    
    public WorldBorder getWorldBorder() {
        return this.P;
    }
    
    public boolean shouldStayLoaded(final int i, final int j) {
        return this.e(i, j);
    }
    
    public boolean e(final int i, final int j) {
        final BlockPosition blockposition = this.getSpawn();
        final int k = i * 16 + 8 - blockposition.getX();
        final int l = j * 16 + 8 - blockposition.getZ();
        final boolean flag = true;
        final short keepLoadedRange = this.paperConfig.keepLoadedRange;
        return k >= -keepLoadedRange && k <= keepLoadedRange && l >= -keepLoadedRange && l <= keepLoadedRange && this.keepSpawnInMemory;
    }
    
    public void a(final Packet<?> packet) {
        throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
    }
    
    public LootTableRegistry getLootTableRegistry() {
        return this.B;
    }
    
    @Nullable
    public BlockPosition a(final String s, final BlockPosition blockposition, final boolean flag) {
        return null;
    }
    
    static {
        DEBUG_ENTITIES = Boolean.getBoolean("debug.entities");
    }
}
