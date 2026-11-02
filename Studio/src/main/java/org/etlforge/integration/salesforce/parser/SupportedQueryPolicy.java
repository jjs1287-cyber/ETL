package org.etlforge.integration.salesforce.parser;

public interface SupportedQueryPolicy {
   SupportedQueryPolicy NO_RESTRICTIONS = new SupportedQueryPolicy() {
      @Override
      public boolean isSupported(ParsedQuery var1) {
         return true;
      }

      @Override
      public String whyNot() {
         throw new UnsupportedOperationException();
      }
   };

   boolean isSupported(ParsedQuery var1);

   String whyNot();
}
