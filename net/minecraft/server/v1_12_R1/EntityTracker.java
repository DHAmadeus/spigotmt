// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.v1_12_R1;

import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.List;
import org.spigotmc.TrackingRange;
import org.spigotmc.AsyncCatcher;
import java.util.Iterator;
import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public class EntityTracker
{
    private static final Logger a;
    private final WorldServer world;
    private final Set<EntityTrackerEntry> c;
    public final IntHashMap<EntityTrackerEntry> trackedEntities;
    private int e;
    
    public EntityTracker(final WorldServer worldserver) {
        this.c = (Set<EntityTrackerEntry>)Sets.newHashSet();
        this.trackedEntities = (IntHashMap<EntityTrackerEntry>)new IntHashMap();
        this.world = worldserver;
        this.e = PlayerChunkMap.getFurthestViewableBlock(worldserver.spigotConfig.viewDistance);
    }
    
    public static long a(final double d0) {
        return MathHelper.d(d0 * 4096.0);
    }
    
    public void track(final Entity entity) {
        if (entity instanceof EntityPlayer) {
            this.addEntity(entity, 512, 2);
            final EntityPlayer entityplayer = (EntityPlayer)entity;
            for (final EntityTrackerEntry entitytrackerentry : this.c) {
                if (entitytrackerentry.b() != entityplayer) {
                    entitytrackerentry.updatePlayer(entityplayer);
                }
            }
        }
        else if (entity instanceof EntityFishingHook) {
            this.addEntity(entity, 64, 5, true);
        }
        else if (entity instanceof EntityArrow) {
            this.addEntity(entity, 64, 20, false);
        }
        else if (entity instanceof EntitySmallFireball) {
            this.addEntity(entity, 64, 10, false);
        }
        else if (entity instanceof EntityFireball) {
            this.addEntity(entity, 64, 10, true);
        }
        else if (entity instanceof EntitySnowball) {
            this.addEntity(entity, 64, 10, true);
        }
        else if (entity instanceof EntityLlamaSpit) {
            this.addEntity(entity, 64, 10, false);
        }
        else if (entity instanceof EntityEnderPearl) {
            this.addEntity(entity, 64, 10, true);
        }
        else if (entity instanceof EntityEnderSignal) {
            this.addEntity(entity, 64, 4, true);
        }
        else if (entity instanceof EntityEgg) {
            this.addEntity(entity, 64, 10, true);
        }
        else if (entity instanceof EntityPotion) {
            this.addEntity(entity, 64, 10, true);
        }
        else if (entity instanceof EntityThrownExpBottle) {
            this.addEntity(entity, 64, 10, true);
        }
        else if (entity instanceof EntityFireworks) {
            this.addEntity(entity, 64, 10, true);
        }
        else if (entity instanceof EntityItem) {
            this.addEntity(entity, 64, 20, true);
        }
        else if (entity instanceof EntityMinecartAbstract) {
            this.addEntity(entity, 80, 3, true);
        }
        else if (entity instanceof EntityBoat) {
            this.addEntity(entity, 80, 3, true);
        }
        else if (entity instanceof EntitySquid) {
            this.addEntity(entity, 64, 3, true);
        }
        else if (entity instanceof EntityWither) {
            this.addEntity(entity, 80, 3, false);
        }
        else if (entity instanceof EntityShulkerBullet) {
            this.addEntity(entity, 80, 3, true);
        }
        else if (entity instanceof EntityBat) {
            this.addEntity(entity, 80, 3, false);
        }
        else if (entity instanceof EntityEnderDragon) {
            this.addEntity(entity, 160, 3, true);
        }
        else if (entity instanceof IAnimal) {
            this.addEntity(entity, 80, 3, true);
        }
        else if (entity instanceof EntityTNTPrimed) {
            this.addEntity(entity, 160, 10, true);
        }
        else if (entity instanceof EntityFallingBlock) {
            this.addEntity(entity, 160, 20, true);
        }
        else if (entity instanceof EntityHanging) {
            this.addEntity(entity, 160, Integer.MAX_VALUE, false);
        }
        else if (entity instanceof EntityArmorStand) {
            this.addEntity(entity, 160, 3, true);
        }
        else if (entity instanceof EntityExperienceOrb) {
            this.addEntity(entity, 160, 20, true);
        }
        else if (entity instanceof EntityAreaEffectCloud) {
            this.addEntity(entity, 160, 10, true);
        }
        else if (entity instanceof EntityEnderCrystal) {
            this.addEntity(entity, 256, Integer.MAX_VALUE, false);
        }
        else if (entity instanceof EntityEvokerFangs) {
            this.addEntity(entity, 160, 2, false);
        }
    }
    
    public void addEntity(final Entity entity, final int i, final int j) {
        this.addEntity(entity, i, j, false);
    }
    
    public void addEntity(final Entity entity, int i, final int j, final boolean flag) {
        AsyncCatcher.catchOp("entity track");
        i = TrackingRange.getEntityTrackingRange(entity, i);
        try {
            if (this.trackedEntities.b(entity.getId())) {
                throw new IllegalStateException("Entity is already tracked!");
            }
            final EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entity, i, this.e, j, flag);
            this.c.add(entitytrackerentry);
            this.trackedEntities.a(entity.getId(), (Object)entitytrackerentry);
            entitytrackerentry.scanPlayers(this.world.players);
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.a(throwable, "Adding entity to track");
            final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity To Track");
            crashreportsystemdetails.a("Tracking range", (Object)(i + " blocks"));
            final int finalI = i;
            crashreportsystemdetails.a("Update interval", (CrashReportCallable)new CrashReportCallable() {
                public String a() throws Exception {
                    String s = "Once per " + finalI + " ticks";
                    if (finalI == Integer.MAX_VALUE) {
                        s = "Maximum (" + s + ")";
                    }
                    return s;
                }
                
                public Object call() throws Exception {
                    return this.a();
                }
            });
            entity.appendEntityCrashDetails(crashreportsystemdetails);
            ((EntityTrackerEntry)this.trackedEntities.get(entity.getId())).b().appendEntityCrashDetails(crashreport.a("Entity That Is Already Tracked"));
            try {
                throw new ReportedException(crashreport);
            }
            catch (ReportedException reportedexception) {
                EntityTracker.a.error("\"Silently\" catching entity tracking error.", (Throwable)reportedexception);
            }
        }
    }
    
    public void untrackEntity(final Entity entity) {
        AsyncCatcher.catchOp("entity untrack");
        if (entity instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)entity;
            for (final EntityTrackerEntry entitytrackerentry : this.c) {
                entitytrackerentry.a(entityplayer);
            }
        }
        final EntityTrackerEntry entitytrackerentry2 = (EntityTrackerEntry)this.trackedEntities.d(entity.getId());
        if (entitytrackerentry2 != null) {
            this.c.remove(entitytrackerentry2);
            entitytrackerentry2.a();
        }
    }
    
    public void updatePlayers() {
        final ArrayList arraylist = Lists.newArrayList();
        final Iterator iterator = this.c.iterator();
        this.world.timings.tracker1.startTiming();
        while (iterator.hasNext()) {
            final EntityTrackerEntry entitytrackerentry = iterator.next();
            entitytrackerentry.track(this.world.players);
            if (entitytrackerentry.b) {
                final Entity entity = entitytrackerentry.b();
                if (!(entity instanceof EntityPlayer)) {
                    continue;
                }
                arraylist.add(entity);
            }
        }
        this.world.timings.tracker1.stopTiming();
        this.world.timings.tracker2.startTiming();
        for (int i = 0; i < arraylist.size(); ++i) {
            final EntityPlayer entityplayer = arraylist.get(i);
            for (final EntityTrackerEntry entitytrackerentry2 : this.c) {
                if (entitytrackerentry2.b() != entityplayer) {
                    entitytrackerentry2.updatePlayer(entityplayer);
                }
            }
        }
        this.world.timings.tracker2.stopTiming();
    }
    
    public void a(final EntityPlayer entityplayer) {
        for (final EntityTrackerEntry entitytrackerentry : this.c) {
            if (entitytrackerentry.b() == entityplayer) {
                entitytrackerentry.scanPlayers(this.world.players);
            }
            else {
                entitytrackerentry.updatePlayer(entityplayer);
            }
        }
    }
    
    public void a(final Entity entity, final Packet<?> packet) {
        final EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntities.get(entity.getId());
        if (entitytrackerentry != null) {
            entitytrackerentry.broadcast(packet);
        }
    }
    
    public void sendPacketToEntity(final Entity entity, final Packet<?> packet) {
        final EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntities.get(entity.getId());
        if (entitytrackerentry != null) {
            entitytrackerentry.broadcastIncludingSelf(packet);
        }
    }
    
    public void untrackPlayer(final EntityPlayer entityplayer) {
        for (final EntityTrackerEntry entitytrackerentry : this.c) {
            entitytrackerentry.clear(entityplayer);
        }
    }
    
    public void a(final EntityPlayer entityplayer, final Chunk chunk) {
        final ArrayList arraylist = Lists.newArrayList();
        final ArrayList arraylist2 = Lists.newArrayList();
        for (final EntityTrackerEntry entitytrackerentry : this.c) {
            final Entity entity = entitytrackerentry.b();
            if (entity != entityplayer && entity.ab == chunk.locX && entity.ad == chunk.locZ) {
                entitytrackerentry.updatePlayer(entityplayer);
                if (entity instanceof EntityInsentient && ((EntityInsentient)entity).getLeashHolder() != null) {
                    arraylist.add(entity);
                }
                if (entity.bF().isEmpty()) {
                    continue;
                }
                arraylist2.add(entity);
            }
        }
        if (!arraylist.isEmpty()) {
            for (final Entity entity2 : arraylist) {
                entityplayer.playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity(entity2, ((EntityInsentient)entity2).getLeashHolder()));
            }
        }
        if (!arraylist2.isEmpty()) {
            for (final Entity entity2 : arraylist2) {
                entityplayer.playerConnection.sendPacket((Packet)new PacketPlayOutMount(entity2));
            }
        }
    }
    
    public void a(final int i) {
        this.e = (i - 1) * 16;
        for (final EntityTrackerEntry entitytrackerentry : this.c) {
            entitytrackerentry.a(this.e);
        }
    }
    
    static {
        a = LogManager.getLogger();
    }
}
