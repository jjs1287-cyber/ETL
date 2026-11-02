package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

public enum NetSuiteSearchOperator {
   none(0),
   after(1),
   anyOf(3),
   before(1),
   between(2),
   contains(1),
   doesNotContain(1),
   doesNotStartWith(1),
   empty(0),
   equalTo(1),
   greaterThan(1),
   greaterThanOrEqualTo(1),
   hasKeywords(1),
   is(1),
   isNot(1),
   lessThan(1),
   lessThanOrEqualTo(1),
   noneOf(3),
   notAfter(1),
   notBefore(1),
   notBetween(2),
   notEmpty(0),
   notEqualTo(1),
   notGreaterThan(1),
   notGreaterThanOrEqualTo(1),
   notLessThan(1),
   notLessThanOrEqualTo(1),
   notOn(1),
   notOnOrAfter(1),
   notOnOrBefore(1),
   notWithin(2),
   on(1),
   onOrAfter(1),
   onOrBefore(1),
   startsWith(1),
   within(2),
   etlforgedummy(1);

   private final int requiredValueNumber;

   NetSuiteSearchOperator(int var3) {
      this.requiredValueNumber = var3;
   }

   public int getRequiredValueNumber() {
      return this.requiredValueNumber;
   }

   public boolean requreSingleValue() {
      return this.requiredValueNumber == 1;
   }

   public boolean requireTwoValue() {
      return this.requiredValueNumber == 2;
   }

   public boolean requireValueList() {
      return !this.requreSingleValue() && !this.requireTwoValue();
   }
}
