package org.etlforge.integration.structure.mapping;

import org.etlforge.util.persist.Persistor;

public final class InvalidMappingPersistor {
   private static final String TARGET_PATH = "target";
   private static final String EXPRESSION = "expr";
   private static final String ERROR = "error";

   public static void save(InvalidMapping var0, Persistor var1) {
      var1.putString("target", var0.getTargetPath().toString());
      var1.putString("expr", var0.getExpression());
      var1.putString("error", var0.getError());
   }

   public static InvalidMapping restoreFrom(Persistor var0) {
      NodePath var1 = new NodePath(var0.getString("target"));
      return new InvalidMapping(var1, var0.getString("expr"), var0.getString("error"));
   }

   private InvalidMappingPersistor() {
   }
}
