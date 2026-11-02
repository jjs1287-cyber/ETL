package org.etlforge.integration.structure.mapping;

import com.google.common.base.Predicate;
import java.io.Serializable;

public final class MappingPredicates {
   public static Predicate<Mapping> toTargetNode(String var0) {
      return new MappingPredicates.ToTargetNode(var0);
   }

   private MappingPredicates() {
   }

   private static final class ToTargetNode implements Predicate<Mapping>, Serializable {
      private final String targetNode;

      public ToTargetNode(String var1) {
         this.targetNode = var1;
      }

      public boolean apply(Mapping var1) {
         return var1.getTargetPath().toString().equals(this.targetNode);
      }

      @Override
      public String toString() {
         return "Target node = " + this.targetNode;
      }
   }
}
