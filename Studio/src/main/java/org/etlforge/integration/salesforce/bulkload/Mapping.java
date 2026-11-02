package org.etlforge.integration.salesforce.bulkload;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.ArgCheck;

public final class Mapping {
   public final String source;
   public final SObjectSchema.Field target;

   public Mapping(String var1, SObjectSchema.Field var2) {
      this.source = ArgCheck.checkNotEmpty(var1);
      this.target = Preconditions.checkNotNull(var2);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.source, this.target);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Mapping)) {
         return false;
      }

      Mapping var2 = (Mapping)var1;
      return this.source.equals(var2.source) && this.target.equals(var2.target);
   }

   @Override
   public String toString() {
      return this.source + " --> " + this.target.getName();
   }
}
