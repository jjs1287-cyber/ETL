package org.etlforge.integration.server.implementation.webservice.impl.common.property_keys;

import com.google.common.base.Objects;

public class ServerPropertyKey {
   private final ServerPropertyKeyType type;
   private final String serverKeyName;
   private final String clientKeyName;
   private static final String[] DEPLOY_PROPERTY_KEYS = new String[]{"EntityTypeId", "EntityId", "KeyName", "KeyValue"};

   public static String[] getPropertyKeys() {
      return DEPLOY_PROPERTY_KEYS;
   }

   ServerPropertyKey(ServerPropertyKeyType var1, String var2, String var3) {
      this.type = var1;
      this.clientKeyName = var3;
      if (var1 == ServerPropertyKeyType.EXTENDED_WILDCARD) {
         this.serverKeyName = var3;
      } else {
         this.serverKeyName = var2;
      }
   }

   public ServerPropertyKeyType getType() {
      return this.type;
   }

   public String getServerKeyName() {
      return this.serverKeyName;
   }

   public String getClientKeyName() {
      return this.clientKeyName;
   }

   @Override
   public String toString() {
      String var1;
      if (Objects.equal(this.serverKeyName, this.clientKeyName)) {
         var1 = this.serverKeyName;
      } else {
         var1 = this.serverKeyName + " - " + this.clientKeyName;
      }

      return var1 + " [" + this.type + "]";
   }
}
