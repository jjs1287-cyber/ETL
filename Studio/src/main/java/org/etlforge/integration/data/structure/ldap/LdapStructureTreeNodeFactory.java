package org.etlforge.integration.data.structure.ldap;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;

public final class LdapStructureTreeNodeFactory {
   private final Map<NumericOid, ObjectClassStructure> classStructs = Maps.newHashMap();
   private final Map<LdapStructureEntry, AttributeContributions> attributes;

   public LdapStructureTreeNodeFactory(LdapStructure var1, ObjectClassStructure[] var2) {
      for (ObjectClassStructure var6 : var2) {
         this.classStructs.put(var6.getOid(), var6);
      }

      this.attributes = Maps.newLinkedHashMap();
      this.build(var1);
   }

   public LdapStructureTreeNodeFactory(LdapStructureEntry[] var1, ObjectClassStructure[] var2, ObjectClassStructure[] var3) {
      for (ObjectClassStructure var7 : var2) {
         this.classStructs.put(var7.getOid(), var7);
      }

      if (var3 != null) {
         for (ObjectClassStructure var11 : var3) {
            this.classStructs.put(var11.getOid(), var11);
         }
      }

      this.attributes = Maps.newLinkedHashMap();
      this.build(var1);
   }

   public List<LdapStructureEntryNode> getEntryNodes() {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.attributes.entrySet()) {
         ObjectClassStructure var4 = this.classStructs.get(((LdapStructureEntry)var3.getKey()).getOid());
         AttributeContributions var5 = this.attributes.get(var3.getKey());
         LdapStructureTreeNodeFactory.LdapStructureEntryNodeImpl var6 = new LdapStructureTreeNodeFactory.LdapStructureEntryNodeImpl(
            (LdapStructureEntry)var3.getKey(), var4, var5
         );
         var1.add(var6);
      }

      return var1;
   }

   private void build(LdapStructure var1) {
      for (LdapStructureEntry var5 : var1.getClassEntries()) {
         this.handle(var5);
      }
   }

   private void build(LdapStructureEntry[] var1) {
      for (LdapStructureEntry var5 : var1) {
         this.handle(var5);
      }
   }

   private void handle(LdapStructureEntry var1) {
      AttributeContributions var2 = new AttributeContributions();
      this.addAttributes(var1.getOid(), var2);

      for (NumericOid var6 : var1.getAuxiliaryClasses()) {
         this.addAttributes(var6, var2);
      }

      this.attributes.put(var1, var2);
   }

   private void addAttributes(NumericOid var1, AttributeContributions var2) {
      ObjectClassStructure var3 = this.classStructs.get(var1);
      Preconditions.checkArgument(var3 != null, "Missing class structure for the OID " + var1);
      var2.add(var3);
   }

   private static class LdapStructureEntryNodeImpl implements LdapStructureEntryNode {
      private final LdapStructureEntry entry;
      private final String name;
      private final Collection<LdapStructureAttribute> attributes;

      public LdapStructureEntryNodeImpl(LdapStructureEntry var1, ObjectClassStructure var2, AttributeContributions var3) {
         this.entry = var1;
         this.name = var2.getName();
         this.attributes = var3.getEffectiveAttributes();
         this.attributes.addAll(var1.getExtendedAttributes());
      }

      @Override
      public String getName() {
         return this.name;
      }

      @Override
      public LdapStructureEntry getEntry() {
         return this.entry;
      }

      @Override
      public List<LdapStructureAttribute> getAttributes() {
         return Lists.newArrayList(this.attributes);
      }

      @Override
      public List<LdapStructureAttribute> getSelectedAttributes() {
         String[] var1 = this.getEntry().getAttributeNames();
         if (var1 == null) {
            return Lists.newArrayList(this.attributes);
         }

         if (var1.length == 0) {
            return Lists.newArrayList();
         }

         ArrayList var2 = Lists.newArrayList(this.attributes);
         final HashSet var3 = Sets.newHashSet(var1);
         Iterables.removeIf(var2, new Predicate<LdapStructureAttribute>() {
            public boolean apply(LdapStructureAttribute var1) {
               return !var3.contains(var1.getName());
            }
         });
         return var2;
      }
   }
}
