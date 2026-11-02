package org.etlforge.integration.structure.mapping;

import com.google.common.base.Preconditions;

public final class MappingFactory {
   public static Mapping newMapping(String var0, String var1) {
      return newMapping(NodePath.of(var0), var1);
   }

   public static Mapping newMapping(NodePath var0, String var1) {
      return new MappingFactory.MappingImpl(var0, var1);
   }

   private MappingFactory() {
   }

   private static class MappingImpl implements Mapping {
      private final NodePath targetPath;
      private final String expression;

      public MappingImpl(NodePath var1, String var2) {
         Preconditions.checkNotNull(var1, "targetPath");
         Preconditions.checkNotNull(var2, "expression");
         this.targetPath = var1;
         this.expression = var2;
      }

      @Override
      public NodePath getTargetPath() {
         return this.targetPath;
      }

      @Override
      public String getExpression() {
         return this.expression;
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
            return var1 instanceof Mapping ? this.equals((Mapping)var1) : super.equals(var1);
         }
      }

      private boolean equals(Mapping var1) {
         return this.targetPath.equals(var1.getTargetPath()) && this.expression.equals(var1.getExpression());
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + this.targetPath.hashCode();
         return 31 * var1 + this.expression.hashCode();
      }
   }
}
