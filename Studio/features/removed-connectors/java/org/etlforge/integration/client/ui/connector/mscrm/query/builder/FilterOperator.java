package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

enum FilterOperator {
   AND,
   OR;

   public String getXml() {
      return this.name().toLowerCase();
   }

   public static FilterOperator fromXml(String var0) {
      for (FilterOperator var4 : values()) {
         if (var4.getXml().equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Unknown FilterOperator: " + var0);
   }
}
