package org.etlforge.integration.salesforce.parser;

public enum SoqlTokenId {
   SELECT {
      @Override
      public SoqlToken createToken() {
         return new OneOfSoqlToken(this, new String[]{"select"});
      }
   },
   FROM {
      @Override
      public SoqlToken createToken() {
         return new OneOfSoqlToken(this, new String[]{"from"});
      }
   },
   OBJECT {
      @Override
      public SoqlToken createToken() {
         return new ObjectToken();
      }
   },
   FIELD {
      @Override
      public SoqlToken createToken() {
         return new FieldToken();
      }
   },
   AGGREGATE_FUNCTION {
      @Override
      public SoqlToken createToken() {
         return new AggregateFunctionToken();
      }
   },
   COMMA {
      @Override
      public SoqlToken createToken() {
         return new OneOfSoqlToken(this, new String[]{","}, false);
      }
   },
   SUB_QUERY {
      @Override
      public SoqlToken createToken() {
         return new SubQueryToken();
      }
   },
   OBJECT_ALIAS {
      @Override
      public SoqlToken createToken() {
         return new ObjectAliasToken();
      }
   },
   EVERYTHING_AFTER_OBJECT {
      @Override
      public SoqlToken createToken() {
         return new EverythingAfterObjectToken();
      }
   };

   SoqlTokenId() {
   }

   public abstract SoqlToken createToken();
}
