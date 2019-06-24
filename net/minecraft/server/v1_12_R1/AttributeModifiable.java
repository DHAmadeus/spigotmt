// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.v1_12_R1;

import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Collection;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.UUID;
import java.util.Set;
import java.util.Map;

public class AttributeModifiable implements AttributeInstance
{
    private final AttributeMapBase a;
    private final IAttribute b;
    private final Map<Integer, Set<AttributeModifier>> c;
    private final Map<String, Set<AttributeModifier>> d;
    private final Map<UUID, AttributeModifier> e;
    private double f;
    private boolean g;
    private double h;
    
    public AttributeModifiable(final AttributeMapBase a, final IAttribute b) {
        this.c = (Map<Integer, Set<AttributeModifier>>)Maps.newHashMap();
        this.d = (Map<String, Set<AttributeModifier>>)Maps.newHashMap();
        this.e = (Map<UUID, AttributeModifier>)Maps.newHashMap();
        this.g = true;
        this.a = a;
        this.b = b;
        this.f = b.getDefault();
        for (int i = 0; i < 3; ++i) {
            this.c.put(i, Sets.newHashSet());
        }
    }
    
    public IAttribute getAttribute() {
        return this.b;
    }
    
    public double b() {
        return this.f;
    }
    
    public void setValue(final double f) {
        if (f == this.b()) {
            return;
        }
        this.f = f;
        this.f();
    }
    
    public Collection<AttributeModifier> a(final int n) {
        return this.c.get(n);
    }
    
    public Collection<AttributeModifier> c() {
        final HashSet hashSet = Sets.newHashSet();
        for (int i = 0; i < 3; ++i) {
            hashSet.addAll(this.a(i));
        }
        return (Collection<AttributeModifier>)hashSet;
    }
    
    @Nullable
    public AttributeModifier a(final UUID uuid) {
        return this.e.get(uuid);
    }
    
    public boolean a(final AttributeModifier attributeModifier) {
        return this.e.get(attributeModifier.a()) != null;
    }
    
    public void b(final AttributeModifier attributeModifier) {
        if (this.a(attributeModifier.a()) != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        Set<AttributeModifier> hashSet = this.d.get(attributeModifier.b());
        if (hashSet == null) {
            hashSet = (Set<AttributeModifier>)Sets.newHashSet();
            this.d.put(attributeModifier.b(), hashSet);
        }
        this.c.get(attributeModifier.c()).add(attributeModifier);
        hashSet.add(attributeModifier);
        this.e.put(attributeModifier.a(), attributeModifier);
        this.f();
    }
    
    protected void f() {
        this.g = true;
        this.a.a((AttributeInstance)this);
    }
    
    public void c(final AttributeModifier attributeModifier) {
        for (int i = 0; i < 3; ++i) {
            this.c.get(i).remove(attributeModifier);
        }
        final Set<AttributeModifier> set = this.d.get(attributeModifier.b());
        if (set != null) {
            set.remove(attributeModifier);
            if (set.isEmpty()) {
                this.d.remove(attributeModifier.b());
            }
        }
        this.e.remove(attributeModifier.a());
        this.f();
    }
    
    public void b(final UUID uuid) {
        final AttributeModifier a = this.a(uuid);
        if (a != null) {
            this.c(a);
        }
    }
    
    public double getValue() {
        if (this.g) {
            this.h = this.g();
            this.g = false;
        }
        return this.h;
    }
    
    private double g() {
        double b = this.b();
        final Iterator<AttributeModifier> iterator = this.b(0).iterator();
        while (iterator.hasNext()) {
            b += iterator.next().d();
        }
        double n = b;
        final Iterator<AttributeModifier> iterator2 = this.b(1).iterator();
        while (iterator2.hasNext()) {
            n += b * iterator2.next().d();
        }
        final Iterator<AttributeModifier> iterator3 = this.b(2).iterator();
        while (iterator3.hasNext()) {
            n *= 1.0 + iterator3.next().d();
        }
        return this.b.a(n);
    }
    
    private Collection<AttributeModifier> b(final int n) {
        final HashSet hashSet = Sets.newHashSet((Iterable)this.a(n));
        for (IAttribute attribute = this.b.d(); attribute != null; attribute = attribute.d()) {
            final AttributeInstance a = this.a.a(attribute);
            if (a != null) {
                hashSet.addAll(a.a(n));
            }
        }
        return (Collection<AttributeModifier>)hashSet;
    }
}
