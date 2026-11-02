package org.etlforge.integration.ldap.structure;

public enum AttributeDataType {
   STRING("String"),
   INTEGER("Integer"),
   LONG("Long"),
   DATE("Date"),
   BOOLEAN("Boolean"),
   BINARY("Binary"),
   UNKNOWN("Unknown");

   private final String stringRep;

   AttributeDataType(String var3) {
      this.stringRep = var3;
   }

   @Override
   public String toString() {
      return this.stringRep;
   }
}
