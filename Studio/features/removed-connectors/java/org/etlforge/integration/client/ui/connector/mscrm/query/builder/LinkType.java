package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import org.apache.commons.lang.StringUtils;

enum LinkType {
   OUTER,
   INNER;

   public String getXml() {
      return this.name().toLowerCase();
   }

   @Override
   public String toString() {
      return StringUtils.capitalize(this.name().toLowerCase());
   }

   public static LinkType fromXml(String var0) {
      for (LinkType var4 : values()) {
         if (var4.getXml().equals(var0)) {
            return var4;
         }
      }

      throw new RuntimeException("Unknown XML value: " + var0);
   }
}
