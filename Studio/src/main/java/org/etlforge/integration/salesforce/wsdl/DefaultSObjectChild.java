package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Objects;
import org.etlforge.system.ArgCheck;

public final class DefaultSObjectChild implements SObjectSchema.Child {
   private static final long serialVersionUID = -6444024143630243621L;
   private final String name;
   private final String objectType;

   public DefaultSObjectChild(String var1, String var2) {
      this.name = ArgCheck.checkNotEmpty(var1);
      this.objectType = ArgCheck.checkNotEmpty(var2);
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getObjectType() {
      return this.objectType;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SObjectSchema.Child)) {
         return false;
      }

      SObjectSchema.Child var2 = (SObjectSchema.Child)var1;
      return this.getName().equals(var2.getName()) && this.getObjectType().equals(var2.getObjectType());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.name, this.objectType);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("name", this.name).add("type", this.objectType).toString();
   }
}
