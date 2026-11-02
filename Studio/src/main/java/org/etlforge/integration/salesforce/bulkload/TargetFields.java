package org.etlforge.integration.salesforce.bulkload;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import org.etlforge.integration.salesforce.wsdl.Fields;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.name.Nameables;

public final class TargetFields {
   private final ImmutableMap<String, SObjectSchema.Field> fields;
   private final ImmutableSet<RelatedObject> relatedObjects;
   private final ImmutableMap<String, SObjectSchema.Field> relatedObjectsExtIds;

   public static TargetFields empty() {
      return new TargetFields(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
   }

   public static TargetFields withNoRelatedObjects(SObjectSchema var0) {
      return withNoRelatedObjects(var0.getFields());
   }

   public static TargetFields forDelete() {
      return withNoRelatedObjects(Collections.singletonList(Fields.ID));
   }

   public static TargetFields withNoRelatedObjects(Collection<SObjectSchema.Field> var0) {
      return new TargetFields(var0, Collections.EMPTY_LIST);
   }

   public TargetFields(Collection<SObjectSchema.Field> var1, Collection<RelatedObject> var2) {
      this.fields = Nameables.toImmutableMap(var1);
      this.relatedObjects = ImmutableSet.copyOf(var2);
      this.relatedObjectsExtIds = this.collectRelatedObjectExternalIds();
   }

   private TargetFields(ImmutableMap<String, SObjectSchema.Field> var1, ImmutableSet<RelatedObject> var2, ImmutableMap<String, SObjectSchema.Field> var3) {
      this.fields = var1;
      this.relatedObjects = var2;
      this.relatedObjectsExtIds = var3;
   }

   private ImmutableMap<String, SObjectSchema.Field> collectRelatedObjectExternalIds() {
      Builder var1 = ImmutableMap.builder();

      for (RelatedObject var3 : this.relatedObjects) {
         var1.putAll(Nameables.toHashMap(var3.getExternalIds()));
      }

      return var1.build();
   }

   public boolean contains(SObjectSchema.Field var1) {
      String var2 = var1.getName();
      return this.fields.containsKey(var2) || this.relatedObjectsExtIds.containsKey(var2);
   }

   public ImmutableCollection<SObjectSchema.Field> getObjectFields() {
      return this.fields.values();
   }

   public ImmutableSet<RelatedObject> getRelatedObjects() {
      return this.relatedObjects;
   }

   public SObjectSchema.Field getField(String var1) {
      SObjectSchema.Field var2 = this.fields.get(var1);
      if (var2 == null) {
         var2 = this.relatedObjectsExtIds.get(var1);
      }

      return var2;
   }

   public TargetFields withoutIdField() {
      if (!this.fields.containsKey("Id")) {
         return this;
      }

      HashMap var1 = Maps.newHashMap(this.fields);
      var1.remove("Id");
      return new TargetFields(ImmutableMap.copyOf(var1), this.relatedObjects, this.relatedObjectsExtIds);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.fields, this.relatedObjects);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof TargetFields)) {
         return false;
      }

      TargetFields var2 = (TargetFields)var1;
      return this.fields.equals(var2.fields) && this.relatedObjects.equals(var2.relatedObjects);
   }
}
