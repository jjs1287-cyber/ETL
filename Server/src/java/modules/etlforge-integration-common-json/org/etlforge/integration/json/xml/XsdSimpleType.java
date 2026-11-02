package org.etlforge.integration.json.xml;

import org.etlforge.json.ScalarValueType;

enum XsdSimpleType {
   STRING(ScalarValueType.STRING, "string"),
   INTEGER(ScalarValueType.NUMBER, "integer"),
   LONG(ScalarValueType.NUMBER, "long"),
   DOUBLE(ScalarValueType.NUMBER, "double"),
   BOOLEAN(ScalarValueType.BOOLEAN, "boolean");

   private final ScalarValueType jsonType;
   private final String schemaType;

   XsdSimpleType(ScalarValueType var3, String var4) {
      this.jsonType = var3;
      this.schemaType = var4;
   }

   public ScalarValueType getJsonType() {
      return this.jsonType;
   }

   public String getSchemaTypeName() {
      return this.schemaType;
   }

   public static XsdSimpleType fromSchemaTypeName(String var0) {
      for (XsdSimpleType var4 : values()) {
         if (var4.schemaType.equals(var0)) {
            return var4;
         }
      }

      return null;
   }
}
