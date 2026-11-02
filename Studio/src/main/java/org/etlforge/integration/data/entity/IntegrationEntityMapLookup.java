package org.etlforge.integration.data.entity;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.data.functors.EntityFilters;

public final class IntegrationEntityMapLookup implements IntegrationEntityLookup {
   private final Map<IntegrationEntityId, IntegrationEntity> objects = Maps.newHashMap();

   public static IntegrationEntityMapLookup build(IntegrationEntity var0) {
      IntegrationEntityMapLookup var1 = new IntegrationEntityMapLookup();
      populate(var1, var0);
      return var1;
   }

   private static void populate(IntegrationEntityMapLookup var0, IntegrationEntity var1) {
      var0.addItem(var1);

      for (IntegrationEntity var3 : var1.getAllChildren()) {
         populate(var0, var3);
      }
   }

   public static IntegrationEntityMapLookup of(IntegrationEntity... var0) {
      IntegrationEntityMapLookup var1 = new IntegrationEntityMapLookup();

      for (IntegrationEntity var5 : var0) {
         var1.addItem(var5);
      }

      return var1;
   }

   public void addItem(IntegrationEntity var1) {
      this.objects.put(var1.getID(), var1);
   }

   public void removeItem(IntegrationEntity var1) {
      this.objects.remove(var1.getID());
   }

   public void clear() {
      this.objects.clear();
   }

   @Override
   public IntegrationEntity getEntity(IntegrationEntityId var1) {
      return var1 == null ? null : this.objects.get(var1);
   }

   @Override
   public <T extends IntegrationEntity> T getEntity(IntegrationEntityId var1, Class<T> var2) {
      IntegrationEntity var3 = this.getEntity(var1);
      return (T)(var2.isInstance(var3) ? var2.cast(var3) : null);
   }

   public Iterable<IntegrationEntity> getAllInstances() {
      return Iterables.filter(ImmutableList.copyOf(this.objects.values()), Predicates.not(EntityFilters.BUILT_IN));
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("Size: ");
      var1.append(this.objects.size());
      var1.append("\n");

      for (IntegrationEntityId var3 : this.objects.keySet()) {
         IntegrationEntity var4 = this.objects.get(var3);
         var1.append(var4.getEntityType());
         var1.append(" ");
         var1.append(var3);
         var1.append(": ");
         var1.append(var4.getName());
         var1.append("\n");
      }

      return var1.toString();
   }
}
