package org.etlforge.integration.data.structure.ldap;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.ldap.structure.AttributeComparators;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.util.name.Nameables;

public final class AttributeContributions {
   private final Map<NumericOid, AttributeContributions.Contributors> contributors = Maps.newHashMap();
   private Set<LdapStructureAttribute> normalAttributes;
   private Set<LdapStructureAttribute> extendedAttributes;

   public void add(ObjectClassStructure var1) {
      NumericOid var2 = var1.getOid();

      for (LdapStructureAttribute var6 : var1.getAttributes()) {
         NumericOid var7 = var6.getOid();
         AttributeContributions.Contributors var8 = this.getContributors(var7, true);
         var8.add(new AttributeContributions.Contribution(var2, var6));
      }

      this.updateNormalAttributes();
   }

   private AttributeContributions.Contributors getContributors(NumericOid var1, boolean var2) {
      AttributeContributions.Contributors var3 = this.contributors.get(var1);
      if (var3 == null && var2) {
         var3 = new AttributeContributions.Contributors();
         this.contributors.put(var1, var3);
      }

      return var3;
   }

   public void remove(ObjectClassStructure var1) {
      NumericOid var2 = var1.getOid();

      for (LdapStructureAttribute var6 : var1.getAttributes()) {
         NumericOid var7 = var6.getOid();
         AttributeContributions.Contributors var8 = this.getContributors(var7, false);
         if (var8 != null) {
            var8.remove(new AttributeContributions.Contribution(var2, var6));
            if (var8.isEmpty()) {
               this.contributors.remove(var7);
            }
         }
      }

      this.updateNormalAttributes();
   }

   public void setExtendedAttributes(LdapStructureAttribute[] var1) {
      this.extendedAttributes = var1 != null ? Sets.newHashSet(var1) : null;
   }

   public LdapStructureAttribute[] getExtendedAttributes() {
      return this.extendedAttributes == null
         ? new LdapStructureAttribute[0]
         : this.extendedAttributes.toArray(new LdapStructureAttribute[this.extendedAttributes.size()]);
   }

   public boolean isExtendedAttribute(LdapStructureAttribute var1) {
      return this.extendedAttributes != null && this.extendedAttributes.contains(var1) && !this.normalAttributes.contains(var1);
   }

   private void updateNormalAttributes() {
      this.normalAttributes = Sets.newHashSet();
      ArrayList var1 = Lists.newArrayList(this.contributors.values());
      this.normalAttributes.addAll(this.collectAttributesFromContributors(var1));
   }

   public Collection<LdapStructureAttribute> getEffectiveAttributes() {
      HashMap var1 = Nameables.toHashMap(this.normalAttributes);
      if (this.extendedAttributes != null) {
         Comparator var2 = AttributeComparators.useOrder();

         for (LdapStructureAttribute var4 : this.extendedAttributes) {
            boolean var5 = true;
            if (var1.containsKey(var4.getName())) {
               LdapStructureAttribute var6 = (LdapStructureAttribute)var1.get(var4.getName());
               int var7 = var2.compare(var4, var6);
               var5 = var7 < 0;
            }

            if (var5) {
               var1.put(var4.getName(), var4);
            }
         }
      }

      TreeSet var8 = Sets.newTreeSet(AttributeComparators.nameOrder(true));
      var8.addAll(var1.values());
      return var8;
   }

   private List<LdapStructureAttribute> collectAttributesFromContributors(List<AttributeContributions.Contributors> var1) {
      return KongaListUtils.transform(var1, new Function<AttributeContributions.Contributors, LdapStructureAttribute>() {
         public LdapStructureAttribute apply(AttributeContributions.Contributors var1) {
            List var2 = var1.getContributedAttributes();
            return var2.size() > 0 ? (LdapStructureAttribute)var2.get(0) : null;
         }
      }, Predicates.notNull());
   }

   private static class Contribution {
      public final NumericOid classOid;
      public final LdapStructureAttribute attribute;

      public Contribution(NumericOid var1, LdapStructureAttribute var2) {
         this.classOid = var1;
         this.attribute = var2;
      }
   }

   private static class Contributors {
      private final Map<NumericOid, AttributeContributions.Contribution> map = Maps.newHashMap();

      private Contributors() {
      }

      public void add(AttributeContributions.Contribution var1) {
         this.map.put(var1.classOid, var1);
      }

      public void remove(AttributeContributions.Contribution var1) {
         AttributeContributions.Contribution var2 = this.map.get(var1.classOid);
         if (var2 != null && var2.attribute.getOid().equals(var1.attribute.getOid())) {
            this.map.remove(var1.classOid);
         }
      }

      public boolean isEmpty() {
         return this.map.isEmpty();
      }

      public List<LdapStructureAttribute> getContributedAttributes() {
         ArrayList var1 = Lists.newArrayList();

         for (AttributeContributions.Contribution var3 : this.map.values()) {
            var1.add(var3.attribute);
         }

         Collections.sort(var1, AttributeComparators.useOrder());
         return var1;
      }
   }
}
