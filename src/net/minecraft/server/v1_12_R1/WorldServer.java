// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.v1_12_R1;

import com.google.common.util.concurrent.ListenableFuture;
import org.bukkit.entity.Player;
import org.bukkit.WeatherType;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.entity.LightningStrike;
import org.apache.logging.log4j.LogManager;
import com.destroystokyo.paper.PaperWorldConfig;
import org.bukkit.event.Event;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.Random;
import org.bukkit.craftbukkit.v1_12_R1.generator.InternalChunkGenerator;
import co.aikar.timings.TimedChunkGenerator;
import org.bukkit.craftbukkit.v1_12_R1.generator.NormalChunkGenerator;
import org.bukkit.craftbukkit.v1_12_R1.generator.SkyLandsChunkGenerator;
import org.bukkit.craftbukkit.v1_12_R1.generator.NetherChunkGenerator;
import org.bukkit.craftbukkit.v1_12_R1.generator.CustomChunkGenerator;
import java.util.ArrayList;
import co.aikar.timings.Timing;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.Iterator;
import javax.annotation.Nullable;
import org.bukkit.Material;
import java.util.logging.Level;
import java.util.Collection;
import java.io.File;
import org.bukkit.craftbukkit.v1_12_R1.CraftTravelAgent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.generator.ChunkGenerator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import org.bukkit.craftbukkit.v1_12_R1.util.HashTreeSet;
import org.apache.logging.log4j.Logger;

public class WorldServer extends World implements IAsyncTaskHandler {
	private static final Logger a;
	boolean stopPhysicsEvent;
	private final MinecraftServer server;
	public EntityTracker tracker;
	private final PlayerChunkMap manager;
	private final HashTreeSet<NextTickListEntry> nextTickList;
	public final Map<UUID, Entity> entitiesByUUID;
	public boolean savingDisabled;
	private boolean Q;
	private int emptyTime;
	private final PortalTravelAgent portalTravelAgent;
	private final SpawnerCreature spawnerCreature;
	protected final VillageSiege siegeManager;
	private final BlockActionDataList[] U;
	private int V;
	private final List<NextTickListEntry> W;
	public final int dimension;

	private static Throwable getAddToWorldStackTrace(final Entity entity) {
		return new Throwable(entity + " Added to world at " + new Date());
	}

	public WorldServer(final MinecraftServer minecraftserver, final IDataManager idatamanager,
			final WorldData worlddata, final int i, final MethodProfiler methodprofiler,
			final org.bukkit.World.Environment env, final ChunkGenerator gen) {
		super(idatamanager, worlddata, DimensionManager.a(env.getId()).d(), methodprofiler, false, gen, env);
		this.stopPhysicsEvent = false;
		this.nextTickList = (HashTreeSet<NextTickListEntry>) new HashTreeSet();
		this.entitiesByUUID = Maps.newHashMap();
		this.spawnerCreature = new SpawnerCreature();
		this.siegeManager = new VillageSiege((World) this);
		this.U = new BlockActionDataList[] { new BlockActionDataList((Object) null),
				new BlockActionDataList((Object) null) };
		this.W = Lists.newArrayList();
		this.dimension = i;
		this.pvpMode = minecraftserver.getPVP();
		worlddata.world = this;
		this.server = minecraftserver;
		this.tracker = new EntityTracker(this);
		this.manager = new PlayerChunkMap(this);
		this.worldProvider.a((World) this);
		this.chunkProvider = this.n();
		this.portalTravelAgent = (PortalTravelAgent) new CraftTravelAgent(this);
		this.J();
		this.K();
		this.getWorldBorder().a(minecraftserver.aE());
	}

	public World b() {
		this.worldMaps = new PersistentCollection(this.dataManager);
		final String s = PersistentVillage.a(this.worldProvider);
		final PersistentVillage persistentvillage = (PersistentVillage) this.worldMaps
				.get((Class) PersistentVillage.class, s);
		if (persistentvillage == null) {
			this.villages = new PersistentVillage((World) this);
			this.worldMaps.a(s, (PersistentBase) this.villages);
		} else {
			(this.villages = persistentvillage).a((World) this);
		}
		if (this.getServer().getScoreboardManager() == null) {
			this.scoreboard = (Scoreboard) new ScoreboardServer(this.server);
			PersistentScoreboard persistentscoreboard = (PersistentScoreboard) this.worldMaps
					.get((Class) PersistentScoreboard.class, "scoreboard");
			if (persistentscoreboard == null) {
				persistentscoreboard = new PersistentScoreboard();
				this.worldMaps.a("scoreboard", (PersistentBase) persistentscoreboard);
			}
			persistentscoreboard.a(this.scoreboard);
			((ScoreboardServer) this.scoreboard)
					.a((Runnable) new RunnableSaveScoreboard((PersistentBase) persistentscoreboard));
		} else {
			this.scoreboard = this.getServer().getScoreboardManager().getMainScoreboard().getHandle();
		}
		this.B = new LootTableRegistry(new File(new File(this.dataManager.getDirectory(), "data"), "loot_tables"));
		if (this.dimension != 0) {
			this.C = this.server.getAdvancementData();
		}
		if (this.C == null) {
			this.C = new AdvancementDataWorld(
					new File(new File(this.dataManager.getDirectory(), "data"), "advancements"));
		}
		if (this.D == null) {
			this.D = new CustomFunctionData(new File(new File(this.dataManager.getDirectory(), "data"), "functions"),
					this.server);
		}
		this.getWorldBorder().setCenter(this.worldData.B(), this.worldData.C());
		this.getWorldBorder().setDamageAmount(this.worldData.H());
		this.getWorldBorder().setDamageBuffer(this.worldData.G());
		this.getWorldBorder().setWarningDistance(this.worldData.I());
		this.getWorldBorder().setWarningTime(this.worldData.J());
		if (this.worldData.E() > 0L) {
			this.getWorldBorder().transitionSizeBetween(this.worldData.D(), this.worldData.F(), this.worldData.E());
		} else {
			this.getWorldBorder().setSize(this.worldData.D());
		}
		if (this.generator != null) {
			this.getWorld().getPopulators()
					.addAll(this.generator.getDefaultPopulators((org.bukkit.World) this.getWorld()));
		}
		return this;
	}

	public TileEntity getTileEntity(final BlockPosition pos) {
		TileEntity result = super.getTileEntity(pos);
		final Block type = this.getType(pos).getBlock();
		if (type == Blocks.CHEST || type == Blocks.TRAPPED_CHEST) {
			if (!(result instanceof TileEntityChest)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.FURNACE) {
			if (!(result instanceof TileEntityFurnace)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.DROPPER) {
			if (!(result instanceof TileEntityDropper)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.DISPENSER) {
			if (!(result instanceof TileEntityDispenser)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.JUKEBOX) {
			if (!(result instanceof BlockJukeBox.TileEntityRecordPlayer)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.NOTEBLOCK) {
			if (!(result instanceof TileEntityNote)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.MOB_SPAWNER) {
			if (!(result instanceof TileEntityMobSpawner)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.STANDING_SIGN || type == Blocks.WALL_SIGN) {
			if (!(result instanceof TileEntitySign)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.ENDER_CHEST) {
			if (!(result instanceof TileEntityEnderChest)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.BREWING_STAND) {
			if (!(result instanceof TileEntityBrewingStand)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.BEACON) {
			if (!(result instanceof TileEntityBeacon)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.HOPPER) {
			if (!(result instanceof TileEntityHopper)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.ENCHANTING_TABLE) {
			if (!(result instanceof TileEntityEnchantTable)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.END_PORTAL) {
			if (!(result instanceof TileEntityEnderPortal)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.SKULL) {
			if (!(result instanceof TileEntitySkull)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.DAYLIGHT_DETECTOR || type == Blocks.DAYLIGHT_DETECTOR_INVERTED) {
			if (!(result instanceof TileEntityLightDetector)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.POWERED_COMPARATOR || type == Blocks.UNPOWERED_COMPARATOR) {
			if (!(result instanceof TileEntityComparator)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.FLOWER_POT) {
			if (!(result instanceof TileEntityFlowerPot)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.STANDING_BANNER || type == Blocks.WALL_BANNER) {
			if (!(result instanceof TileEntityBanner)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.STRUCTURE_BLOCK) {
			if (!(result instanceof TileEntityStructure)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.END_GATEWAY) {
			if (!(result instanceof TileEntityEndGateway)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.COMMAND_BLOCK) {
			if (!(result instanceof TileEntityCommand)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.STRUCTURE_BLOCK) {
			if (!(result instanceof TileEntityStructure)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type == Blocks.BED) {
			if (!(result instanceof TileEntityBed)) {
				result = this.fixTileEntity(pos, type, result);
			}
		} else if (type instanceof BlockShulkerBox && !(result instanceof TileEntityShulkerBox)) {
			result = this.fixTileEntity(pos, type, result);
		}
		return result;
	}

	private TileEntity fixTileEntity(final BlockPosition pos, final Block type, final TileEntity found) {
		this.getServer().getLogger().log(Level.SEVERE,
				"Block at {0},{1},{2} is {3} but has {4}. Bukkit will attempt to fix this, but there may be additional damage that we cannot recover.",
				new Object[] { pos.getX(), pos.getY(), pos.getZ(), Material.getMaterial(Block.getId(type)).toString(),
						found });
		if (type instanceof ITileEntity) {
			final TileEntity replacement = ((ITileEntity) type).a((World) this, type.toLegacyData(this.getType(pos)));
			((WorldServer) (replacement.world = this)).setTileEntity(pos, replacement);
			return replacement;
		}
		this.getServer().getLogger().severe("Don't know how to fix for this type... Can't do anything! :(");
		return found;
	}

	private boolean canSpawn(final int x, final int z) {
		if (this.generator != null) {
			return this.generator.canSpawn((org.bukkit.World) this.getWorld(), x, z);
		}
		return this.worldProvider.canSpawn(x, z);
	}

	public void doTick() {
		super.doTick();
		if (this.getWorldData().isHardcore() && this.getDifficulty() != EnumDifficulty.HARD) {
			this.getWorldData().setDifficulty(EnumDifficulty.HARD);
		}
		this.worldProvider.k().b();
		if (this.everyoneDeeplySleeping()) {
			if (this.getGameRules().getBoolean("doDaylightCycle")) {
				final long i = this.worldData.getDayTime() + 24000L;
				this.worldData.setDayTime(i - i % 24000L);
			}
			this.f();
		}
		this.methodProfiler.a("mobSpawner");
		final long time = this.worldData.getTime();
		if (this.getGameRules().getBoolean("doMobSpawning")
				&& this.worldData.getType() != WorldType.DEBUG_ALL_BLOCK_STATES
				&& (this.allowMonsters || this.allowAnimals) && this instanceof WorldServer
				&& this.players.size() > 0) {
			this.timings.mobSpawn.startTiming();
			this.spawnerCreature.a(this,
					this.allowMonsters && this.ticksPerMonsterSpawns != 0L && time % this.ticksPerMonsterSpawns == 0L,
					this.allowAnimals && this.ticksPerAnimalSpawns != 0L && time % this.ticksPerAnimalSpawns == 0L,
					this.worldData.getTime() % 400L == 0L);
			this.timings.mobSpawn.stopTiming();
		}
		this.timings.doChunkUnload.startTiming();
		this.methodProfiler.c("chunkSource");
		this.chunkProvider.unloadChunks();
		final int j = this.a(1.0f);
		if (j != this.ah()) {
			this.c(j);
		}
		this.worldData.setTime(this.worldData.getTime() + 1L);
		if (this.getGameRules().getBoolean("doDaylightCycle")) {
			this.worldData.setDayTime(this.worldData.getDayTime() + 1L);
		}
		this.timings.doChunkUnload.stopTiming();
		this.methodProfiler.c("tickPending");
		this.timings.scheduledBlocks.startTiming();
		this.a(false);
		this.timings.scheduledBlocks.stopTiming();
		this.methodProfiler.c("tickBlocks");
		this.timings.chunkTicks.startTiming();
		this.j();
		this.timings.chunkTicks.stopTiming();
		this.methodProfiler.c("chunkMap");
		this.timings.doChunkMap.startTiming();
		this.manager.flush();
		this.timings.doChunkMap.stopTiming();
		this.methodProfiler.c("village");
		this.timings.doVillages.startTiming();
		this.villages.tick();
		this.siegeManager.a();
		this.timings.doVillages.stopTiming();
		this.methodProfiler.c("portalForcer");
		this.timings.doPortalForcer.startTiming();
		this.portalTravelAgent.a(this.getTime());
		this.timings.doPortalForcer.stopTiming();
		this.methodProfiler.b();
		this.timings.doSounds.startTiming();
		this.aq();
		this.timings.doSounds.stopTiming();
		this.timings.doChunkGC.startTiming();
		this.getWorld().processChunkGC();
		this.timings.doChunkGC.stopTiming();
	}

	@Nullable
	public BiomeBase.BiomeMeta a(final EnumCreatureType enumcreaturetype, final BlockPosition blockposition) {
		final List list = this.getChunkProviderServer().a(enumcreaturetype, blockposition);
		return (list != null && !list.isEmpty()) ? ((BiomeBase.BiomeMeta) WeightedRandom.a(this.random, list)) : null;
	}

	public boolean a(final EnumCreatureType enumcreaturetype, final BiomeBase.BiomeMeta biomebase_biomemeta,
			final BlockPosition blockposition) {
		final List list = this.getChunkProviderServer().a(enumcreaturetype, blockposition);
		return list != null && !list.isEmpty() && list.contains(biomebase_biomemeta);
	}

	public void everyoneSleeping() {
		this.Q = false;
		if (!this.players.isEmpty()) {
			int i = 0;
			int j = 0;
			for (final EntityHuman entityhuman : this.players) {
				if (entityhuman.isSpectator()) {
					++i;
				} else {
					if (!entityhuman.isSleeping() && !entityhuman.fauxSleeping) {
						continue;
					}
					++j;
				}
			}
			this.Q = (j > 0 && j >= this.players.size() - i);
		}
	}

	protected void f() {
		this.Q = false;
		final List<EntityHuman> list = this.players.stream().filter(EntityHuman::isSleeping)
				.collect(Collectors.toList());
		for (final EntityHuman entityhuman : list) {
			entityhuman.a(false, false, true);
		}
		if (this.getGameRules().getBoolean("doWeatherCycle")) {
			this.c();
		}
	}

	private void c() {
		this.worldData.setStorm(false);
		if (!this.worldData.hasStorm()) {
			this.worldData.setWeatherDuration(0);
		}
		this.worldData.setThundering(false);
		if (!this.worldData.isThundering()) {
			this.worldData.setThunderDuration(0);
		}
	}

	public boolean everyoneDeeplySleeping() {
		if (this.Q && !this.isClientSide) {
			final Iterator<EntityHuman> iterator = this.players.iterator();
			boolean foundActualSleepers = false;
			while (iterator.hasNext()) {
				final EntityHuman entityhuman = iterator.next();
				if (entityhuman.isDeeplySleeping()) {
					foundActualSleepers = true;
				}
				if (entityhuman.isSpectator() && !entityhuman.isDeeplySleeping() && !entityhuman.fauxSleeping) {
					return false;
				}
			}
			return foundActualSleepers;
		}
		return false;
	}

	protected boolean isChunkLoaded(final int i, final int j, final boolean flag) {
		return this.getChunkProviderServer().isLoaded(i, j);
	}

	protected void i() {
		this.methodProfiler.a("playerCheckLight");
		if (this.spigotConfig.randomLightUpdates && !this.players.isEmpty()) {
			final int i = this.random.nextInt(this.players.size());
			final EntityHuman entityhuman = this.players.get(i);
			final int j = MathHelper.floor(entityhuman.locX) + this.random.nextInt(11) - 5;
			final int k = MathHelper.floor(entityhuman.locY) + this.random.nextInt(11) - 5;
			final int l = MathHelper.floor(entityhuman.locZ) + this.random.nextInt(11) - 5;
			this.w(new BlockPosition(j, k, l));
		}
		this.methodProfiler.b();
	}

	private Object jLocker = new Object();

	protected void j() {
		new Thread(() -> {
			synchronized (jLocker) {
				jMT();
			}
		}, "chunk ticks").start();
	}

	protected void jMT() {
		this.i();
		if (this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			final Iterator<Chunk> iterator = this.manager.b();
			while (iterator.hasNext()) {
				iterator.next().b(false);
			}
		} else {
			final int i = this.getGameRules().c("randomTickSpeed");
			final boolean flag = this.isRaining();
			final boolean flag2 = this.X();
			this.methodProfiler.a("pollingChunks");
			List<PlayerChunk> yêu_ngân = manager.làm_sao_để_em_hiểu_lòng_anh();
			synchronized (yêu_ngân) {
				final Iterator<Chunk> iterator2 = this.manager.b();
				while (iterator2.hasNext()) {
					this.methodProfiler.a("getChunk");
					final Chunk chunk = iterator2.next();
					final int j = chunk.locX * 16;
					final int k = chunk.locZ * 16;
					this.methodProfiler.c("checkNextLight");
					chunk.n();
					this.methodProfiler.c("tickChunk");
					chunk.b(false);
					if (chunk.areNeighborsLoaded(1)) {
						this.methodProfiler.c("thunder");
						if (!this.paperConfig.disableThunder && flag && flag2 && this.random.nextInt(100000) == 0) {
							this.l = this.l * 3 + 1013904223;
							final int l = this.l >> 2;
							final BlockPosition blockposition = this
									.a(new BlockPosition(j + (l & 0xF), 0, k + (l >> 8 & 0xF)));
							if (this.isRainingAt(blockposition)) {
								final DifficultyDamageScaler difficultydamagescaler = this.D(blockposition);
								if (this.getGameRules().getBoolean("doMobSpawning")
										&& this.random.nextDouble() < difficultydamagescaler.b()
												* this.paperConfig.skeleHorseSpawnChance) {
									final EntityHorseSkeleton entityhorseskeleton = new EntityHorseSkeleton(
											(World) this);
									entityhorseskeleton.p(true);
									entityhorseskeleton.setAgeRaw(0);
									entityhorseskeleton.setPosition((double) blockposition.getX(),
											(double) blockposition.getY(), (double) blockposition.getZ());
									this.addEntity((Entity) entityhorseskeleton,
											CreatureSpawnEvent.SpawnReason.LIGHTNING);
									this.strikeLightning((Entity) new EntityLightning((World) this,
											(double) blockposition.getX(), (double) blockposition.getY(),
											(double) blockposition.getZ(), true));
								} else {
									this.strikeLightning((Entity) new EntityLightning((World) this,
											(double) blockposition.getX(), (double) blockposition.getY(),
											(double) blockposition.getZ(), false));
								}
							}
						}
						this.methodProfiler.c("iceandsnow");
						if (!this.paperConfig.disableIceAndSnow && this.random.nextInt(16) == 0) {
							this.l = this.l * 3 + 1013904223;
							final int l = this.l >> 2;
							final BlockPosition blockposition = this
									.p(new BlockPosition(j + (l & 0xF), 0, k + (l >> 8 & 0xF)));
							final BlockPosition blockposition2 = blockposition.down();
							if (this.v(blockposition2)) {
								CraftEventFactory.handleBlockFormEvent((World) this, blockposition2,
										Blocks.ICE.getBlockData(), (Entity) null);
							}
							if (flag && this.f(blockposition, true)) {
								CraftEventFactory.handleBlockFormEvent((World) this, blockposition,
										Blocks.SNOW_LAYER.getBlockData(), (Entity) null);
							}
							if (flag && this.getBiome(blockposition2).d()) {
								this.getType(blockposition2).getBlock().h((World) this, blockposition2);
							}
						}
//					this.timings.chunkTicksBlocks.startTiming();

						if (i > 0) {
							for (final ChunkSection chunksection : chunk.getSections()) {
								if (chunksection != Chunk.a && chunksection.shouldTick()) {
									for (int k2 = 0; k2 < i; ++k2) {
										this.l = this.l * 3 + 1013904223;
										final int l2 = this.l >> 2;
										final int i3 = l2 & 0xF;
										final int j3 = l2 >> 8 & 0xF;
										final int k3 = l2 >> 16 & 0xF;
										final IBlockData iblockdata = chunksection.getType(i3, k3, j3);
										final Block block = iblockdata.getBlock();
										this.methodProfiler.a("randomTick");
										if (block.isTicking()) {
											block.a((World) this,
													new BlockPosition(i3 + j, k3 + chunksection.getYPosition(), j3 + k),
													iblockdata, this.random);
										}
										this.methodProfiler.b();
									}
								}
							}
						}
//					this.timings.chunkTicks.stopTiming();
					}
					this.methodProfiler.b();
				}
			}
			this.methodProfiler.b();
		}
	}

	protected BlockPosition a(final BlockPosition blockposition) {
		BlockPosition blockposition2 = this.p(blockposition);
		final AxisAlignedBB axisalignedbb = new AxisAlignedBB(blockposition2,
				new BlockPosition(blockposition2.getX(), this.getHeight(), blockposition2.getZ())).g(3.0);
		final List<EntityLiving> list = this.a((Class) EntityLiving.class, axisalignedbb,
				(com.google.common.base.Predicate) new com.google.common.base.Predicate() {
					public boolean a(@Nullable final EntityLiving entityliving) {
						return entityliving != null && entityliving.isAlive()
								&& WorldServer.this.h(entityliving.getChunkCoordinates());
					}

					public boolean apply(@Nullable final Object object) {
						return this.a((EntityLiving) object);
					}
				});
		if (!list.isEmpty()) {
			return list.get(this.random.nextInt(list.size())).getChunkCoordinates();
		}
		if (blockposition2.getY() == -1) {
			blockposition2 = blockposition2.up(2);
		}
		return blockposition2;
	}

	public boolean a(final BlockPosition blockposition, final Block block) {
		final NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
		return this.W.contains(nextticklistentry);
	}

	public boolean b(final BlockPosition blockposition, final Block block) {
		final NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
		return this.nextTickList.contains((Object) nextticklistentry);
	}

	public void a(final BlockPosition blockposition, final Block block, final int i) {
		this.a(blockposition, block, i, 0);
	}

	public void a(final BlockPosition blockposition, final Block block, int i, final int j) {
		final net.minecraft.server.v1_12_R1.Material material = block.getBlockData().getMaterial();
		if (this.d && material != net.minecraft.server.v1_12_R1.Material.AIR) {
			if (block.r()) {
				if (this.areChunksLoadedBetween(blockposition.a(-8, -8, -8), blockposition.a(8, 8, 8))) {
					final IBlockData iblockdata = this.getType(blockposition);
					if (iblockdata.getMaterial() != net.minecraft.server.v1_12_R1.Material.AIR
							&& iblockdata.getBlock() == block) {
						iblockdata.getBlock().b((World) this, blockposition, iblockdata, this.random);
					}
				}
				return;
			}
			i = 1;
		}
		final NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
		if (this.isLoaded(blockposition)) {
			if (material != net.minecraft.server.v1_12_R1.Material.AIR) {
				nextticklistentry.a(i + this.worldData.getTime());
				nextticklistentry.a(j);
			}
			if (!this.nextTickList.contains((Object) nextticklistentry)) {
				this.nextTickList.add(nextticklistentry);
			}
		}
	}

	public void b(final BlockPosition blockposition, final Block block, final int i, final int j) {
		final NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
		nextticklistentry.a(j);
		final net.minecraft.server.v1_12_R1.Material material = block.getBlockData().getMaterial();
		if (material != net.minecraft.server.v1_12_R1.Material.AIR) {
			nextticklistentry.a(i + this.worldData.getTime());
		}
		if (!this.nextTickList.contains((Object) nextticklistentry)) {
			this.nextTickList.add(nextticklistentry);
		}
	}

	public void tickEntities() {
		this.m();
		this.worldProvider.s();
		super.tickEntities();
		this.spigotConfig.currentPrimedTnt = 0;
	}

	protected void l() {
		super.l();
		this.methodProfiler.c("players");
		for (int i = 0; i < this.players.size(); ++i) {
			final Entity entity = this.players.get(i);
			final Entity entity2 = entity.bJ();
			if (entity2 != null) {
				if (!entity2.dead && entity2.w(entity)) {
					continue;
				}
				entity.stopRiding();
			}
			this.methodProfiler.a("tick");
			if (!entity.dead) {
				try {
					this.h(entity);
				} catch (Throwable throwable) {
					final CrashReport crashreport = CrashReport.a(throwable, "Ticking player");
					final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Player being ticked");
					entity.appendEntityCrashDetails(crashreportsystemdetails);
					throw new ReportedException(crashreport);
				}
			}
			this.methodProfiler.b();
			this.methodProfiler.a("remove");
			if (entity.dead) {
				final int j = entity.ab;
				final int k = entity.ad;
				if (entity.aa && this.isChunkLoaded(j, k, true)) {
					this.getChunkAt(j, k).b(entity);
				}
				this.entityList.remove(entity);
				this.c(entity);
			}
			this.methodProfiler.b();
		}
	}

	public void m() {
		this.emptyTime = 0;
	}

	public boolean a(final boolean flag) {
		if (this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			return false;
		}
		int i = this.nextTickList.size();
		if (i > 65536) {
			if (i > 1310720) {
				i /= 20;
			} else {
				i = 65536;
			}
		}
		this.methodProfiler.a("cleaning");
		this.timings.scheduledBlocksCleanup.startTiming();
		for (int j = 0; j < i; ++j) {
			final NextTickListEntry nextticklistentry = (NextTickListEntry) this.nextTickList.first();
			if (!flag && nextticklistentry.b > this.worldData.getTime()) {
				break;
			}
			this.nextTickList.remove((Object) nextticklistentry);
			this.W.add(nextticklistentry);
		}
		this.timings.scheduledBlocksCleanup.stopTiming();
		this.methodProfiler.b();
		this.methodProfiler.a("ticking");
		final Iterator<NextTickListEntry> iterator = this.W.iterator();
		this.timings.scheduledBlocksTicking.startTiming();
		while (iterator.hasNext()) {
			final NextTickListEntry nextticklistentry = iterator.next();
			iterator.remove();
			final boolean flag2 = false;
			if (this.areChunksLoadedBetween(nextticklistentry.a.a(0, 0, 0), nextticklistentry.a.a(0, 0, 0))) {
				final IBlockData iblockdata = this.getType(nextticklistentry.a);
				final Timing timing = iblockdata.getBlock().getTiming();
				timing.startTiming();
				if (iblockdata.getMaterial() != net.minecraft.server.v1_12_R1.Material.AIR
						&& Block.a(iblockdata.getBlock(), nextticklistentry.a())) {
					try {
						this.stopPhysicsEvent = (!this.paperConfig.firePhysicsEventForRedstone
								&& (iblockdata.getBlock() instanceof BlockDiodeAbstract
										|| iblockdata.getBlock() instanceof BlockRedstoneTorch));
						iblockdata.getBlock().b((World) this, nextticklistentry.a, iblockdata, this.random);
					} catch (Throwable throwable) {
						final CrashReport crashreport = CrashReport.a(throwable, "Exception while ticking a block");
						final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being ticked");
						CrashReportSystemDetails.a(crashreportsystemdetails, nextticklistentry.a, iblockdata);
						throw new ReportedException(crashreport);
					} finally {
						this.stopPhysicsEvent = false;
					}
				}
				timing.stopTiming();
			} else {
				this.a(nextticklistentry.a, nextticklistentry.a(), 0);
			}
		}
		this.timings.scheduledBlocksTicking.stopTiming();
		this.methodProfiler.b();
		this.W.clear();
		return !this.nextTickList.isEmpty();
	}

	@Nullable
	public List<NextTickListEntry> a(final Chunk chunk, final boolean flag) {
		final ChunkCoordIntPair chunkcoordintpair = chunk.k();
		final int i = (chunkcoordintpair.x << 4) - 2;
		final int j = i + 16 + 2;
		final int k = (chunkcoordintpair.z << 4) - 2;
		final int l = k + 16 + 2;
		return this.a(new StructureBoundingBox(i, 0, k, j, 256, l), flag);
	}

	@Nullable
	public List<NextTickListEntry> a(final StructureBoundingBox structureboundingbox, final boolean flag) {
		ArrayList<NextTickListEntry> arraylist = null;
		for (int i = 0; i < 2; ++i) {
			Iterator<NextTickListEntry> iterator;
			if (i == 0) {
				iterator = this.nextTickList.iterator();
			} else {
				iterator = this.W.iterator();
			}
			while (iterator.hasNext()) {
				final NextTickListEntry nextticklistentry = iterator.next();
				final BlockPosition blockposition = nextticklistentry.a;
				if (blockposition.getX() >= structureboundingbox.a && blockposition.getX() < structureboundingbox.d
						&& blockposition.getZ() >= structureboundingbox.c
						&& blockposition.getZ() < structureboundingbox.f) {
					if (flag) {
						if (i == 0) {
						}
						iterator.remove();
					}
					if (arraylist == null) {
						arraylist = Lists.newArrayList();
					}
					arraylist.add(nextticklistentry);
				}
			}
		}
		return (List<NextTickListEntry>) arraylist;
	}

	private boolean getSpawnNPCs() {
		return this.server.getSpawnNPCs();
	}

	private boolean getSpawnAnimals() {
		return this.server.getSpawnAnimals();
	}

	protected IChunkProvider n() {
		final IChunkLoader ichunkloader = this.dataManager.createChunkLoader(this.worldProvider);
		InternalChunkGenerator gen;
		if (this.generator != null) {
			gen = (InternalChunkGenerator) new CustomChunkGenerator((World) this, this.getSeed(), this.generator);
		} else if (this.worldProvider instanceof WorldProviderHell) {
			gen = (InternalChunkGenerator) new NetherChunkGenerator((World) this, this.getSeed());
		} else if (this.worldProvider instanceof WorldProviderTheEnd) {
			gen = (InternalChunkGenerator) new SkyLandsChunkGenerator((World) this, this.getSeed());
		} else {
			gen = (InternalChunkGenerator) new NormalChunkGenerator((World) this, this.getSeed());
		}
		return (IChunkProvider) new ChunkProviderServer(this, ichunkloader,
				(net.minecraft.server.v1_12_R1.ChunkGenerator) new TimedChunkGenerator(this, gen));
	}

	public List<TileEntity> getTileEntities(final int i, final int j, final int k, final int l, final int i1,
			final int j1) {
		final ArrayList arraylist = Lists.newArrayList();
		for (int chunkX = i >> 4; chunkX <= l - 1 >> 4; ++chunkX) {
			for (int chunkZ = k >> 4; chunkZ <= j1 - 1 >> 4; ++chunkZ) {
				final Chunk chunk = this.getChunkAt(chunkX, chunkZ);
				if (chunk != null) {
					for (final Object te : chunk.tileEntities.values()) {
						final TileEntity tileentity = (TileEntity) te;
						if (tileentity.position.getX() >= i && tileentity.position.getY() >= j
								&& tileentity.position.getZ() >= k && tileentity.position.getX() < l
								&& tileentity.position.getY() < i1 && tileentity.position.getZ() < j1) {
							arraylist.add(tileentity);
						}
					}
				}
			}
		}
		return (List<TileEntity>) arraylist;
	}

	public boolean a(final EntityHuman entityhuman, final BlockPosition blockposition) {
		return !this.server.a((World) this, blockposition, entityhuman) && this.getWorldBorder().a(blockposition);
	}

	public void a(final WorldSettings worldsettings) {
		if (!this.worldData.v()) {
			try {
				this.b(worldsettings);
				if (this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
					this.ap();
				}
				super.a(worldsettings);
			} catch (Throwable throwable) {
				final CrashReport crashreport = CrashReport.a(throwable, "Exception initializing level");
				try {
					this.a(crashreport);
				} catch (Throwable t) {
				}
				throw new ReportedException(crashreport);
			}
			this.worldData.d(true);
		}
	}

	private void ap() {
		this.worldData.f(false);
		this.worldData.c(true);
		this.worldData.setStorm(false);
		this.worldData.setThundering(false);
		this.worldData.i(1000000000);
		this.worldData.setDayTime(6000L);
		this.worldData.setGameType(EnumGamemode.SPECTATOR);
		this.worldData.g(false);
		this.worldData.setDifficulty(EnumDifficulty.PEACEFUL);
		this.worldData.e(true);
		this.getGameRules().set("doDaylightCycle", "false");
	}

	private void b(final WorldSettings worldsettings) {
		if (!this.worldProvider.e()) {
			this.worldData.setSpawn(BlockPosition.ZERO.up(this.worldProvider.getSeaLevel()));
		} else if (this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			this.worldData.setSpawn(BlockPosition.ZERO.up());
		} else {
			this.isLoading = true;
			final WorldChunkManager worldchunkmanager = this.worldProvider.k();
			final List list = worldchunkmanager.a();
			final Random random = new Random(this.getSeed());
			final BlockPosition blockposition = worldchunkmanager.a(0, 0, 256, list, random);
			int i = 8;
			final int j = this.worldProvider.getSeaLevel();
			int k = 8;
			if (this.generator != null) {
				final Random rand = new Random(this.getSeed());
				final Location spawn = this.generator.getFixedSpawnLocation((org.bukkit.World) this.getWorld(), rand);
				if (spawn != null) {
					if (spawn.getWorld() != this.getWorld()) {
						throw new IllegalStateException("Cannot set spawn point for " + this.worldData.getName()
								+ " to be in another world (" + spawn.getWorld().getName() + ")");
					}
					this.worldData.setSpawn(new BlockPosition(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()));
					this.isLoading = false;
					return;
				}
			}
			if (blockposition != null) {
				i = blockposition.getX();
				k = blockposition.getZ();
			} else {
				WorldServer.a.warn("Unable to find spawn biome");
			}
			int l = 0;
			while (!this.canSpawn(i, k)) {
				i += random.nextInt(64) - random.nextInt(64);
				k += random.nextInt(64) - random.nextInt(64);
				if (++l == 1000) {
					break;
				}
			}
			this.worldData.setSpawn(new BlockPosition(i, j, k));
			this.isLoading = false;
			if (worldsettings.c()) {
				this.o();
			}
		}
	}

	protected void o() {
		final WorldGenBonusChest worldgenbonuschest = new WorldGenBonusChest();
		for (int i = 0; i < 10; ++i) {
			final int j = this.worldData.b() + this.random.nextInt(6) - this.random.nextInt(6);
			final int k = this.worldData.d() + this.random.nextInt(6) - this.random.nextInt(6);
			final BlockPosition blockposition = this.q(new BlockPosition(j, 0, k)).up();
			if (worldgenbonuschest.generate((World) this, this.random, blockposition)) {
				break;
			}
		}
	}

	@Nullable
	public BlockPosition getDimensionSpawn() {
		return this.worldProvider.h();
	}

	public void save(final boolean flag, @Nullable final IProgressUpdate iprogressupdate)
			throws ExceptionWorldConflict {
		final ChunkProviderServer chunkproviderserver = this.getChunkProviderServer();
		if (chunkproviderserver.e()) {
			if (flag) {
				Bukkit.getPluginManager().callEvent((Event) new WorldSaveEvent((org.bukkit.World) this.getWorld()));
			}
			this.timings.worldSave.startTiming();
			if (flag || this.server.serverAutoSave) {
				if (iprogressupdate != null) {
					iprogressupdate.a("Saving level");
				}
				this.a();
				if (iprogressupdate != null) {
					iprogressupdate.c("Saving chunks");
				}
			}
			this.timings.worldSaveChunks.startTiming();
			chunkproviderserver.a(flag);
			this.timings.worldSaveChunks.stopTiming();
			this.timings.worldSave.stopTiming();
		}
	}

	public void flushSave() {
		final ChunkProviderServer chunkproviderserver = this.getChunkProviderServer();
		if (chunkproviderserver.e()) {
			chunkproviderserver.c();
		}
	}

	protected void a() throws ExceptionWorldConflict {
		this.timings.worldSaveLevel.startTiming();
		this.checkSession();
		for (final WorldServer worldserver : this.server.worldServer) {
			if (worldserver instanceof SecondaryWorldServer) {
				((SecondaryWorldServer) worldserver).c();
			}
		}
		if (this instanceof SecondaryWorldServer) {
			((SecondaryWorldServer) this).c();
		}
		this.worldData.a(this.getWorldBorder().getSize());
		this.worldData.d(this.getWorldBorder().getCenterX());
		this.worldData.c(this.getWorldBorder().getCenterZ());
		this.worldData.e(this.getWorldBorder().getDamageBuffer());
		this.worldData.f(this.getWorldBorder().getDamageAmount());
		this.worldData.j(this.getWorldBorder().getWarningDistance());
		this.worldData.k(this.getWorldBorder().getWarningTime());
		this.worldData.b(this.getWorldBorder().j());
		this.worldData.e(this.getWorldBorder().i());
		this.dataManager.saveWorldData(this.worldData, this.server.getPlayerList().t());
		this.worldMaps.a();
		this.timings.worldSaveLevel.stopTiming();
	}

	public boolean addEntity(final Entity entity, final CreatureSpawnEvent.SpawnReason spawnReason) {
		return this.j(entity) && super.addEntity(entity, spawnReason);
	}

	public void a(final Collection<Entity> collection) {
		final ArrayList<Entity> arraylist = Lists.newArrayList((Iterable) collection);
		for (final Entity entity : arraylist) {
			if (this.j(entity)) {
				this.entityList.add(entity);
				this.b(entity);
			}
		}
	}

	private boolean j(final Entity entity) {
		if (entity.dead) {
			if (WorldServer.DEBUG_ENTITIES) {
				WorldServer.a.warn("Tried to add entity {} but it was marked as removed already",
						(Object) EntityTypes.a(entity));
				getAddToWorldStackTrace(entity).printStackTrace();
			}
			return false;
		}
		final UUID uuid = entity.getUniqueID();
		if (this.entitiesByUUID.containsKey(uuid)) {
			final Entity entity2 = this.entitiesByUUID.get(uuid);
			if (this.f.contains(entity2) || entity2.dead) {
				this.f.remove(entity2);
			} else {
				if (!(entity instanceof EntityHuman)) {
					if (entity.world.paperConfig.duplicateUUIDMode != PaperWorldConfig.DuplicateUUIDMode.NOTHING
							&& WorldServer.DEBUG_ENTITIES) {
						WorldServer.a.error("Keeping entity {} that already exists with UUID {}", (Object) entity2,
								(Object) uuid.toString());
						WorldServer.a.error(
								"Duplicate entity {} will not be added to the world. See paper.yml duplicate-uuid-resolver and set this to either regen, delete or nothing to get rid of this message",
								(Object) entity);
						if (entity2.addedToWorldStack != null) {
							entity2.addedToWorldStack.printStackTrace();
						}
						getAddToWorldStackTrace(entity).printStackTrace();
					}
					return false;
				}
				WorldServer.a.warn("Force-added player with duplicate UUID {}", (Object) uuid.toString());
			}
			this.removeEntity(entity2);
		}
		return true;
	}

	protected void b(final Entity entity) {
		super.b(entity);
		this.entitiesById.a(entity.getId(), entity);
		if (WorldServer.DEBUG_ENTITIES) {
			entity.addedToWorldStack = getAddToWorldStackTrace(entity);
		}
		final Entity old = this.entitiesByUUID.put(entity.getUniqueID(), entity);
		if (old != null && old.getId() != entity.getId() && old.valid
				&& entity.world.paperConfig.duplicateUUIDMode != PaperWorldConfig.DuplicateUUIDMode.NOTHING) {
			final Logger logger = LogManager.getLogger();
			if (WorldServer.DEBUG_ENTITIES) {
				logger.error("Overwrote an existing entity " + old + " with " + entity);
				if (old.addedToWorldStack != null) {
					old.addedToWorldStack.printStackTrace();
				} else {
					logger.error("Oddly, the old entity was not added to the world in the normal way. Plugins?");
				}
				entity.addedToWorldStack.printStackTrace();
			}
		}
		final Entity[] aentity = entity.bb();
		if (aentity != null) {
			final Entity[] aentity2 = aentity;
			for (int i = aentity.length, j = 0; j < i; ++j) {
				final Entity entity2 = aentity2[j];
				this.entitiesById.a(entity2.getId(), entity2);
			}
		}
	}

	protected void c(final Entity entity) {
		if (!this.entitiesByUUID.containsKey(entity.getUniqueID()) && !entity.valid) {
			return;
		}
		super.c(entity);
		this.entitiesById.d(entity.getId());
		this.entitiesByUUID.remove(entity.getUniqueID());
		final Entity[] aentity = entity.bb();
		if (aentity != null) {
			final Entity[] aentity2 = aentity;
			for (int i = aentity.length, j = 0; j < i; ++j) {
				final Entity entity2 = aentity2[j];
				this.entitiesById.d(entity2.getId());
			}
		}
	}

	public boolean strikeLightning(final Entity entity) {
		final LightningStrikeEvent lightning = new LightningStrikeEvent((org.bukkit.World) this.getWorld(),
				(LightningStrike) entity.getBukkitEntity());
		this.getServer().getPluginManager().callEvent((Event) lightning);
		if (lightning.isCancelled()) {
			return false;
		}
		if (super.strikeLightning(entity)) {
			this.server.getPlayerList().sendPacketNearby((EntityHuman) null, entity.locX, entity.locY, entity.locZ,
					512.0, this, (Packet) new PacketPlayOutSpawnEntityWeather(entity));
			return true;
		}
		return false;
	}

	public void broadcastEntityEffect(final Entity entity, final byte b0) {
		this.getTracker().sendPacketToEntity(entity, (Packet) new PacketPlayOutEntityStatus(entity, b0));
	}

	public ChunkProviderServer getChunkProviderServer() {
		return (ChunkProviderServer) super.getChunkProvider();
	}

	public Explosion createExplosion(@Nullable final Entity entity, final double d0, final double d1, final double d2,
			final float f, final boolean flag, final boolean flag1) {
		final Explosion explosion = super.createExplosion(entity, d0, d1, d2, f, flag, flag1);
		if (explosion.wasCanceled) {
			return explosion;
		}
		if (!flag1) {
			explosion.clearBlocks();
		}
		for (final EntityHuman entityhuman : this.players) {
			if (entityhuman.d(d0, d1, d2) < 4096.0) {
				((EntityPlayer) entityhuman).playerConnection.sendPacket((Packet) new PacketPlayOutExplosion(d0, d1, d2,
						f, explosion.getBlocks(), (Vec3D) explosion.b().get(entityhuman)));
			}
		}
		return explosion;
	}

	public void playBlockAction(final BlockPosition blockposition, final Block block, final int i, final int j) {
		final BlockActionData blockactiondata = new BlockActionData(blockposition, block, i, j);
		for (final BlockActionData blockactiondata2 : this.U[this.V]) {
			if (blockactiondata2.equals((Object) blockactiondata)) {
				return;
			}
		}
		this.U[this.V].add(blockactiondata);
	}

	private void aq() {
		while (!this.U[this.V].isEmpty()) {
			final int i = this.V;
			this.V ^= 0x1;
			for (final BlockActionData blockactiondata : this.U[i]) {
				if (this.a(blockactiondata)) {
					this.server.getPlayerList().sendPacketNearby((EntityHuman) null,
							(double) blockactiondata.a().getX(), (double) blockactiondata.a().getY(),
							(double) blockactiondata.a().getZ(), 64.0, this,
							(Packet) new PacketPlayOutBlockAction(blockactiondata.a(), blockactiondata.d(),
									blockactiondata.b(), blockactiondata.c()));
				}
			}
			this.U[i].clear();
		}
	}

	private boolean a(final BlockActionData blockactiondata) {
		final IBlockData iblockdata = this.getType(blockactiondata.a());
		return iblockdata.getBlock() == blockactiondata.d()
				&& iblockdata.a((World) this, blockactiondata.a(), blockactiondata.b(), blockactiondata.c());
	}

	public void saveLevel() {
		this.dataManager.a();
	}

	protected void t() {
		final boolean flag = this.isRaining();
		super.t();
		synchronized (players) {
			if (flag != this.isRaining()) {
				for (int i = 0; i < this.players.size(); ++i) {
					if (this.players.get(i).world == this) {
						((EntityPlayer) this.players.get(i))
								.setPlayerWeather(flag ? WeatherType.CLEAR : WeatherType.DOWNFALL, false);
					}
				}
			}
			for (int i = 0; i < this.players.size(); ++i) {
				if (this.players.get(i).world == this) {
					((EntityPlayer) this.players.get(i)).updateWeather(this.n, this.o, this.p, this.q);
				}
			}
		}
	}

	@Nullable
	public MinecraftServer getMinecraftServer() {
		return this.server;
	}

	public EntityTracker getTracker() {
		return this.tracker;
	}

	public PlayerChunkMap getPlayerChunkMap() {
		return this.manager;
	}

	public PortalTravelAgent getTravelAgent() {
		return this.portalTravelAgent;
	}

	public DefinedStructureManager y() {
		return this.dataManager.h();
	}

	public void a(final EnumParticle enumparticle, final double d0, final double d1, final double d2, final int i,
			final double d3, final double d4, final double d5, final double d6, final int... aint) {
		this.a(enumparticle, false, d0, d1, d2, i, d3, d4, d5, d6, aint);
	}

	public void a(final EnumParticle enumparticle, final boolean flag, final double d0, final double d1,
			final double d2, final int i, final double d3, final double d4, final double d5, final double d6,
			final int... aint) {
		this.sendParticles(null, enumparticle, flag, d0, d1, d2, i, d3, d4, d5, d6, aint);
	}

	public void sendParticles(final EntityPlayer sender, final EnumParticle enumparticle, final boolean flag,
			final double d0, final double d1, final double d2, final int i, final double d3, final double d4,
			final double d5, final double d6, final int... aint) {
		this.sendParticles(this.players, sender, enumparticle, flag, d0, d1, d2, i, d3, d4, d5, d6, aint);
	}

	public void sendParticles(final List<? extends EntityHuman> receivers, final EntityPlayer sender,
			final EnumParticle enumparticle, final boolean flag, final double d0, final double d1, final double d2,
			final int i, final double d3, final double d4, final double d5, final double d6, final int... aint) {
		final PacketPlayOutWorldParticles packetplayoutworldparticles = new PacketPlayOutWorldParticles(enumparticle,
				flag, (float) d0, (float) d1, (float) d2, (float) d3, (float) d4, (float) d5, (float) d6, i, aint);
		for (final EntityHuman entityhuman : receivers) {
			final EntityPlayer entityplayer = (EntityPlayer) entityhuman;
			if (sender != null && !entityplayer.getBukkitEntity().canSee((Player) sender.getBukkitEntity())) {
				continue;
			}
			final BlockPosition blockposition = entityplayer.getChunkCoordinates();
			final double d7 = blockposition.distanceSquared(d0, d1, d2);
			this.a(entityplayer, flag, d0, d1, d2, (Packet<?>) packetplayoutworldparticles);
		}
	}

	public void a(final EntityPlayer entityplayer, final EnumParticle enumparticle, final boolean flag, final double d0,
			final double d1, final double d2, final int i, final double d3, final double d4, final double d5,
			final double d6, final int... aint) {
		final PacketPlayOutWorldParticles packetplayoutworldparticles = new PacketPlayOutWorldParticles(enumparticle,
				flag, (float) d0, (float) d1, (float) d2, (float) d3, (float) d4, (float) d5, (float) d6, i, aint);
		this.a(entityplayer, flag, d0, d1, d2, (Packet<?>) packetplayoutworldparticles);
	}

	private void a(final EntityPlayer entityplayer, final boolean flag, final double d0, final double d1,
			final double d2, final Packet<?> packet) {
		final BlockPosition blockposition = entityplayer.getChunkCoordinates();
		final double d3 = blockposition.distanceSquared(d0, d1, d2);
		if (d3 <= 1024.0 || (flag && d3 <= 262144.0)) {
			entityplayer.playerConnection.sendPacket((Packet) packet);
		}
	}

	@Nullable
	public Entity getEntity(final UUID uuid) {
		return this.entitiesByUUID.get(uuid);
	}

	public ListenableFuture<Object> postToMainThread(final Runnable runnable) {
		return (ListenableFuture<Object>) this.server.postToMainThread(runnable);
	}

	public boolean isMainThread() {
		return this.server.isMainThread();
	}

	@Nullable
	public BlockPosition a(final String s, final BlockPosition blockposition, final boolean flag) {
		return this.getChunkProviderServer().a((World) this, s, blockposition, flag);
	}

	public AdvancementDataWorld z() {
		return this.C;
	}

	public CustomFunctionData A() {
		return this.D;
	}

	public IChunkProvider getChunkProvider() {
		return (IChunkProvider) this.getChunkProviderServer();
	}

	static {
		a = LogManager.getLogger();
	}

	static class BlockActionDataList extends ArrayList<BlockActionData> {
		private BlockActionDataList() {
		}

		BlockActionDataList(final Object object) {
			this();
		}
	}
}
