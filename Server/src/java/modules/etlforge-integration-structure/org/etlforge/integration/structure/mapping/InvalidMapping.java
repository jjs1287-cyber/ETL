package org.etlforge.integration.structure.mapping;

import com.google.common.base.Preconditions;

public final class InvalidMapping implements Mapping {
   private final Mapping mapping;
   private final String error;

   public InvalidMapping(NodePath var1, String var2, String var3) {
      sanityCheck(var3);
      this.mapping = MappingFactory.newMapping(var1, var2);
      this.error = var3;
   }

   public InvalidMapping(Mapping var1, String var2) {
      Preconditions.checkNotNull(var1, "mapping");
      sanityCheck(var2);
      this.mapping = var1;
      this.error = var2;
   }

   @Override
   public String getExpression() {
      return this.mapping.getExpression();
   }

   @Override
   public NodePath getTargetPath() {
      return this.mapping.getTargetPath();
   }

   public String getError() {
      return this.error;
   }

   @Override
   public String toString() {
      return MapStringFormat.format(this);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof InvalidMapping ? this.equals((InvalidMapping)var1) : super.equals(var1);
      }
   }

   private boolean equals(InvalidMapping var1) {
      return this.mapping.equals(var1.mapping) && this.error.equals(var1.error);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.mapping.hashCode();
      return 31 * var1 + this.error.hashCode();
   }

   private static final void sanityCheck(String var0) {
      Preconditions.checkNotNull(var0, "error");
      Preconditions.checkArgument(!var0.isEmpty(), "error cannot be empty");
   }
}
