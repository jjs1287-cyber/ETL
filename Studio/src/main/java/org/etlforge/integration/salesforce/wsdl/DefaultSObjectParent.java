package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import org.etlforge.system.ArgCheck;

public final class DefaultSObjectParent implements SObjectSchema.Parent {
   private static final long serialVersionUID = -3227987108892977246L;
   private final String name;
   private final ImmutableSet<String> objectTypes;
   private final boolean isNameObject;

   public DefaultSObjectParent(String var1, String[] var2, boolean var3) {
      this.name = ArgCheck.checkNotEmpty(var1);
      this.objectTypes = ImmutableSet.copyOf(ArgCheck.checkNotEmpty(var2));
      this.isNameObject = var3;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public ImmutableSet<String> getObjectTypes() {
      return this.objectTypes;
   }

   @Override
   public String getObjectTypeInQuery() {
      return !this.isNameObject && this.objectTypes.size() <= 1 ? Iterables.get(this.objectTypes, 0) : "Name";
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SObjectSchema.Parent)) {
         return false;
      }

      SObjectSchema.Parent var2 = (SObjectSchema.Parent)var1;
      return this.name.equals(var2.getName())
         && this.getObjectTypeInQuery().equals(var2.getObjectTypeInQuery())
         && this.objectTypes.equals(var2.getObjectTypes());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.name, this.getObjectTypeInQuery(), this.objectTypes);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("name", this.getName()).add("types(s)", Ordering.natural().sortedCopy(this.objectTypes)).toString();
   }
}
