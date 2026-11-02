package org.etlforge.integration.server.implementation.webservice.impl.common.property_keys;

public enum ServerPropertyKeyType {
   STANDARD(0),
   EXTENDED(1),
   EXTENDED_WILDCARD(2);

   private int type;

   ServerPropertyKeyType(int var3) {
      this.type = var3;
   }

   public int toInt() {
      return this.type;
   }

   static ServerPropertyKeyType fromInt(int var0) {
      switch (var0) {
         case 0:
            return STANDARD;
         case 1:
            return EXTENDED;
         case 2:
            return EXTENDED_WILDCARD;
         default:
            return STANDARD;
      }
   }
}
