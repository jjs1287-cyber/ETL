package org.etlforge.integration.mscrm.metadata;

import com.google.common.collect.ImmutableSet;

public enum MSCrmDataType {
   BIGINT("long"),
   BOOLEAN("boolean") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return ImmutableSet.of("name");
      }
   },
   CUSTOMER("string") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return REFERENCE.getQueryResponseAttributes();
      }
   },
   DATETIME("dateTime") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return ImmutableSet.of("date", "time");
      }
   },
   DECIMAL("decimal") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return NUMBER.getQueryResponseAttributes();
      }
   },
   DOUBLE("double"),
   ENTITYNAME("string"),
   INTEGER("integer") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return NUMBER.getQueryResponseAttributes();
      }
   },
   LOOKUP("string") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return REFERENCE.getQueryResponseAttributes();
      }
   },
   MANAGEDPROPERTY("string"),
   MEMO("string"),
   MONEY("decimal") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return NUMBER.getQueryResponseAttributes();
      }
   },
   NUMBER("integer") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return ImmutableSet.of("formattedvalue");
      }
   },
   OTHER("string"),
   OWNER("string") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return REFERENCE.getQueryResponseAttributes();
      }
   },
   PARTYLIST("string"),
   PICKLIST("string") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return ImmutableSet.of("name", "formattedvalue");
      }
   },
   REFERENCE("string") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return ImmutableSet.of("yomi", "dsc", "type", "name");
      }
   },
   STATE("string") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return ImmutableSet.of("name");
      }
   },
   STATUS("string") {
      @Override
      public ImmutableSet<String> getQueryResponseAttributes() {
         return ImmutableSet.of("name");
      }
   },
   STRING("string"),
   UNIQUEIDENTIFIER("string");

   private final String xsdType;

   MSCrmDataType(String var3) {
      this.xsdType = var3;
   }

   public final String getXsdType() {
      return this.xsdType;
   }

   public ImmutableSet<String> getQueryResponseAttributes() {
      return ImmutableSet.of();
   }
}
