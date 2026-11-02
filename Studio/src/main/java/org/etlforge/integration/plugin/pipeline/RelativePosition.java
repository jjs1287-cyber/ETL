package org.etlforge.integration.plugin.pipeline;

public enum RelativePosition {
   PRE("Pre"),
   POST("Post"),
   ON_REQUEST("OnRequest"),
   ON_RESPONSE("OnResponse"),
   INSIDE("Inside");

   private final String stringRep;

   RelativePosition(String var3) {
      this.stringRep = var3;
   }

   @Override
   public String toString() {
      return this.stringRep;
   }

   public static RelativePosition fromString(String var0) {
      for (RelativePosition var4 : values()) {
         if (var4.stringRep.equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid RelativePosition: " + var0);
   }
}
