package org.etlforge.integration.structure.mapping;

import com.google.common.base.Function;

public final class MappingFunctions {
   public static final Function<Mapping, NodePath> TO_TARGET_PATH = new Function<Mapping, NodePath>() {
      public NodePath apply(Mapping var1) {
         return var1.getTargetPath();
      }
   };

   private MappingFunctions() {
   }
}
