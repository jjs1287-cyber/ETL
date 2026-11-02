package org.etlforge.integration.salesforce.bulkload;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.lang.Objects;

public final class HeaderMappings {
   private final TargetFields targetFields;
   private final LinkedHashMap<String, SObjectSchema.Field> mappings;

   @VisibleForTesting
   static HeaderMappings emptyForObject(SObjectSchema var0, List<String> var1) {
      TargetFields var2 = TargetFields.withNoRelatedObjects(var0);
      return new HeaderMappings(Lists.newArrayList(var1), var2);
   }

   public HeaderMappings(List<String> var1, TargetFields var2) {
      this.targetFields = var2;
      this.mappings = Maps.newLinkedHashMap();

      for (String var4 : var1) {
         this.mappings.put(var4, null);
      }
   }

   public HeaderMappings(List<String> var1, TargetFields var2, List<Mapping> var3) {
      this(var1, var2);

      for (Mapping var5 : var3) {
         this.validateMapping(var5);
         this.mappings.put(var5.source, var5.target);
      }
   }

   private void validateMapping(Mapping var1) {
      if (!this.mappings.containsKey(var1.source)) {
         throw new IllegalArgumentException("Unknown source header in mapList: " + var1.source);
      }

      this.validateKnownField(var1.target);
   }

   private void validateKnownField(SObjectSchema.Field var1) {
      if (!this.targetFields.contains(var1)) {
         throw new IllegalArgumentException("Unknown field in mapList: " + var1.getName());
      }
   }

   public HeaderMappings(List<String> var1, TargetFields var2, List<Integer> var3, List<SObjectSchema.Field> var4) {
      this(var1, var2);
      Iterator var5 = var3.iterator();
      Iterator var6 = var4.iterator();

      while (var5.hasNext() && var6.hasNext()) {
         String var7 = (String)var1.get((Integer)var5.next());
         if (!this.mappings.containsKey(var7)) {
            throw new IllegalArgumentException();
         }

         SObjectSchema.Field var8 = (SObjectSchema.Field)var6.next();
         this.validateKnownField(var8);
         this.mappings.put(var7, var8);
      }
   }

   private HeaderMappings(TargetFields var1, LinkedHashMap<String, SObjectSchema.Field> var2) {
      this.targetFields = var1;
      this.mappings = var2;
   }

   public List<String> getSourceHeader() {
      return Lists.newArrayList(this.mappings.keySet());
   }

   public List<Integer> getSourceColumnIndices() {
      ArrayList var1 = Lists.newArrayList();
      int var2 = 0;

      for (Entry var4 : this.mappings.entrySet()) {
         if (var4.getValue() != null) {
            var1.add(var2);
         }

         var2++;
      }

      return var1;
   }

   public TargetFields getTargetFields() {
      return this.targetFields;
   }

   public ImmutableCollection<SObjectSchema.Field> getObjectFields() {
      return this.targetFields.getObjectFields();
   }

   public ImmutableSet<RelatedObject> getRelatedObjects() {
      return this.targetFields.getRelatedObjects();
   }

   public List<SObjectSchema.Field> getMappedTargetFields() {
      ArrayList var1 = Lists.newArrayList();

      for (SObjectSchema.Field var3 : this.mappings.values()) {
         if (var3 != null) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public List<Mapping> getMapList() {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.mappings.entrySet()) {
         if (var3.getValue() != null) {
            var1.add(new Mapping((String)var3.getKey(), (SObjectSchema.Field)var3.getValue()));
         }
      }

      return var1;
   }

   public HeaderMappings updateMappings(List<Mapping> var1) {
      LinkedHashMap var2 = Maps.newLinkedHashMap();

      for (String var4 : this.mappings.keySet()) {
         var2.put(var4, null);
      }

      for (Mapping var6 : var1) {
         this.validateMapping(var6);
         var2.put(var6.source, var6.target);
      }

      return new HeaderMappings(this.targetFields, var2);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.targetFields, this.mappings);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof HeaderMappings)) {
         return false;
      }

      HeaderMappings var2 = (HeaderMappings)var1;
      return Objects.equal(this.targetFields, var2.targetFields) && Objects.equal(this.mappings, var2.mappings);
   }
}
