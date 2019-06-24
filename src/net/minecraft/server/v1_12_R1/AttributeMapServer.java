// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.v1_12_R1;

import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;

public class AttributeMapServer extends AttributeMapBase
{
    private final Set<AttributeInstance> e;
    protected final Map<String, AttributeInstance> d;
    
    public AttributeMapServer() {
        this.e = (Set<AttributeInstance>)Sets.newHashSet();
        this.d = (Map<String, AttributeInstance>)new InsensitiveStringMap();
    }
    
    public AttributeModifiable e(final IAttribute attribute) {
        return (AttributeModifiable)super.a(attribute);
    }
    
    public AttributeModifiable b(final String s) {
        AttributeInstance a = super.a(s);
        if (a == null) {
            a = this.d.get(s);
        }
        return (AttributeModifiable)a;
    }
    
    public AttributeInstance b(final IAttribute attribute) {
        final AttributeInstance b = super.b(attribute);
        if (attribute instanceof AttributeRanged && ((AttributeRanged)attribute).g() != null) {
            this.d.put(((AttributeRanged)attribute).g(), b);
        }
        return b;
    }
    
    protected AttributeInstance c(final IAttribute attribute) {
        return (AttributeInstance)new AttributeModifiable(this, attribute);
    }
    
    public void a(final AttributeInstance attributeInstance) {
        if (attributeInstance.getAttribute().c()) {
            this.e.add(attributeInstance);
        }
        final Iterator<IAttribute> iterator = this.c.get((Object)attributeInstance.getAttribute()).iterator();
        while (iterator.hasNext()) {
            final AttributeModifiable e = this.e(iterator.next());
            if (e != null) {
                e.f();
            }
        }
    }
    
    public Set<AttributeInstance> getAttributes() {
        return this.e;
    }
    
    public Collection<AttributeInstance> c() {
        final HashSet hashSet = Sets.newHashSet();
        for (final AttributeInstance attributeInstance : this.a()) {
            if (attributeInstance.getAttribute().c()) {
                hashSet.add(attributeInstance);
            }
        }
        return (Collection<AttributeInstance>)hashSet;
    }
}
