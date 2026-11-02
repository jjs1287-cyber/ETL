package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList.Builder;
import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import org.etlforge.integration.salesforce.wsdl.Fields;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.name.NameSorter;
import org.etlforge.util.name.Nameable;
import org.etlforge.util.name.Nameables;

public abstract class FieldOwner implements Nameable {
   private final String name;
   private final RelationshipPath path;
   private final ImmutableMap<String, SObjectSchema.Field> fields;
   private final ImmutableList<Relationship> parentRelationships;

   protected FieldOwner(String var1, RelationshipPath var2, SObjectSchema var3) {
      this.name = ArgCheck.checkNotBlank(var1);
      this.fields = Nameables.toImmutableMap(var3.getFields());
      this.path = var2.append(this);
      this.parentRelationships = listParents(var3);
   }

   private static ImmutableList<Relationship> listParents(SObjectSchema var0) {
      Builder var1 = ImmutableList.builder();

      for (SObjectSchema.Parent var3 : NameSorter.naturalOrdering().sortedCopy(var0.getParents())) {
         var1.add(new Relationship(var3.getName(), var3.getObjectTypeInQuery()));
      }

      return var1.build();
   }

   @Override
   public final String getName() {
      return this.name;
   }

   public abstract String getDisplayName(NameDisplayMode var1);

   public final RelationshipPath getPath() {
      return this.path;
   }

   public final ImmutableList<SObjectSchema.Field> getFields() {
      return Fields.ORDER.immutableSortedCopy(this.fields.values());
   }

   public Optional<SObjectSchema.Field> getField(String var1) {
      return Optional.fromNullable(this.fields.get(var1));
   }

   public final ImmutableList<Relationship> getParentRelationships() {
      return this.parentRelationships;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("path", this.path).toString();
   }

   @Override
   public int hashCode() {
      return this.path.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else {
         return this.getClass().equals(var1.getClass()) ? this.path.equals(((FieldOwner)var1).path) : false;
      }
   }
}
