package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Comparator;
import org.etlforge.system.ArgCheck;

public final class RelatedObject {
   private final String relationshipName;
   private final String fieldName;
   private final String objectName;
   private final ImmutableList<SObjectSchema.Field> externalIds;

   public static final Comparator<RelatedObject> byRelationshipName() {
      return new RelatedObjectsByRelationshipName();
   }

   public RelatedObject(String var1, String var2, String var3, Iterable<SObjectSchema.Field> var4) {
      this.relationshipName = ArgCheck.checkNotBlank(var1);
      this.fieldName = ArgCheck.checkNotBlank(var2);
      this.objectName = ArgCheck.checkNotBlank(var3);
      this.externalIds = qualifyFieldNamesIfNeeded(var1, var4);
   }

   private static ImmutableList<SObjectSchema.Field> qualifyFieldNamesIfNeeded(String var0, Iterable<SObjectSchema.Field> var1) {
      Builder var2 = ImmutableList.builder();

      for (SObjectSchema.Field var4 : var1) {
         if (RelatedObjectExternalIdFieldName.isQualifiedWith(var0, var4)) {
            var2.add(var4);
         } else {
            String var5 = RelatedObjectExternalIdFieldName.quaulifyName(var0, var4);
            DefaultSObjectField var6 = new DefaultSObjectField(var4, var5);
            var2.add(var6);
         }
      }

      return var2.build();
   }

   public String getRelationshipName() {
      return this.relationshipName;
   }

   public String getFieldName() {
      return this.fieldName;
   }

   public String getObjectName() {
      return this.objectName;
   }

   public ImmutableList<SObjectSchema.Field> getExternalIds() {
      return this.externalIds;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("Relationship", this.relationshipName)
         .add("Field", this.fieldName)
         .add("Object", this.objectName)
         .add("Ext. IDs", this.externalIds)
         .toString();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(Objects.hashCode(this.relationshipName, this.fieldName, this.objectName, this.externalIds));
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof RelatedObject)) {
         return false;
      }

      RelatedObject var2 = (RelatedObject)var1;
      return this.relationshipName.equals(var2.relationshipName)
         && this.fieldName.equals(var2.fieldName)
         && this.objectName.equals(var2.objectName)
         && this.externalIds.equals(var2.externalIds);
   }
}
