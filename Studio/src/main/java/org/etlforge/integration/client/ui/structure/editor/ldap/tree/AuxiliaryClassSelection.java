package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.data.structure.ldap.AttributeContributions;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;

final class AuxiliaryClassSelection {
   private final Map<NumericOid, AttributeContributions> attributeContributions;
   private final Multimap<NumericOid, NumericOid> assignedAuxClasses;
   private final Map<NumericOid, ObjectClassStructure> auxClassStructures;

   public AuxiliaryClassSelection(Map<NumericOid, AttributeContributions> var1) {
      this.attributeContributions = var1;
      this.assignedAuxClasses = HashMultimap.create();
      this.auxClassStructures = Maps.newHashMap();
   }

   public NumericOid[] getAuxiliaryClassesFor(NumericOid var1) {
      if (this.assignedAuxClasses.containsKey(var1)) {
         Collection var2 = this.assignedAuxClasses.get(var1);
         return (NumericOid[]) var2.toArray(new NumericOid[var2.size()]);
      } else {
         return new NumericOid[0];
      }
   }

   public ObjectClassStructure[] getAuxiliaryClassStructures() {
      return this.auxClassStructures.values().toArray(new ObjectClassStructure[this.auxClassStructures.size()]);
   }

   public void setAuxiliaryClasses(NumericOid var1, ObjectClassStructure[] var2) {
      NumericOid[] var3 = this.getAuxiliaryClassesFor(var1);
      AuxiliaryClassSelection.AuxClassDelta var4 = new AuxiliaryClassSelection.AuxClassDelta(var3, var2);
      this.processRemovedAuxClasses(var1, var4);
      this.assignedAuxClasses.removeAll(var1);

      for (ObjectClassStructure var8 : var2) {
         this.auxClassStructures.put(var8.getOid(), var8);
         this.assignedAuxClasses.put(var1, var8.getOid());
      }

      this.processAddedAuxClasses(var1, var4);
   }

   private void processRemovedAuxClasses(NumericOid var1, AuxiliaryClassSelection.AuxClassDelta var2) {
      AttributeContributions var3 = this.attributeContributions.get(var1);
      if (var3 != null) {
         for (NumericOid var5 : var2.toRemove()) {
            ObjectClassStructure var6 = this.auxClassStructures.get(var5);
            var3.remove(var6);
         }
      }
   }

   private void processAddedAuxClasses(NumericOid var1, AuxiliaryClassSelection.AuxClassDelta var2) {
      AttributeContributions var3 = this.getAttributeContributionsCreateIfNotExist(var1);

      for (NumericOid var5 : var2.toAdd()) {
         ObjectClassStructure var6 = this.auxClassStructures.get(var5);
         var3.add(var6);
      }
   }

   private AttributeContributions getAttributeContributionsCreateIfNotExist(NumericOid var1) {
      AttributeContributions var2 = this.attributeContributions.get(var1);
      if (var2 == null) {
         var2 = new AttributeContributions();
         this.attributeContributions.put(var1, var2);
      }

      return var2;
   }

   private static class AuxClassDelta {
      private final Set<NumericOid> toAdd;
      private final Set<NumericOid> toRemove;

      public AuxClassDelta(NumericOid[] var1, ObjectClassStructure[] var2) {
         Set var3 = this.getNewAuxOids(var2);
         this.toRemove = Sets.newHashSet(var1);
         this.toRemove.removeAll(var3);
         this.toAdd = var3;
         this.toAdd.removeAll(Sets.newHashSet(var1));
      }

      public Set<NumericOid> toRemove() {
         return this.toRemove;
      }

      public Set<NumericOid> toAdd() {
         return this.toAdd;
      }

      private Set<NumericOid> getNewAuxOids(ObjectClassStructure[] var1) {
         HashSet var2 = Sets.newHashSet();

         for (ObjectClassStructure var6 : var1) {
            var2.add(var6.getOid());
         }

         return var2;
      }
   }
}
