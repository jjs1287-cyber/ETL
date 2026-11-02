package org.etlforge.integration.server.backend.webservice.scriptengine;

public enum VariableType {
   STRING("java.lang.String") {
      @Override
      public Object getValue(String var1) {
         return var1;
      }
   },
   INTEGER("java.lang.Integer") {
      @Override
      public Object getValue(String var1) {
         return Integer.valueOf(var1);
      }
   },
   DOUBLE("java.lang.Double") {
      @Override
      public Object getValue(String var1) {
         return Double.valueOf(var1);
      }
   },
   BOOLEAN("java.lang.Boolean") {
      @Override
      public Object getValue(String var1) {
         return Boolean.valueOf(var1);
      }
   };

   private final String javaClassName;

   public abstract Object getValue(String var1);

   VariableType(String var3) {
      this.javaClassName = var3;
   }

   public static VariableType getType(String var0) {
      try {
         String var1 = var0.toUpperCase();
         return valueOf(var1);
      } catch (Exception var2) {
         return STRING;
      }
   }

   public static VariableType fromClassName(String var0) {
      for (VariableType var4 : values()) {
         if (var4.javaClassName.equals(var0)) {
            return var4;
         }
      }

      return STRING;
   }
}
