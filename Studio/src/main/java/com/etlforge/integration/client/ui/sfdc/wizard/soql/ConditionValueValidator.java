package com.etlforge.integration.client.ui.sfdc.wizard.soql;

interface ConditionValueValidator {
   boolean isValid(String var1, Operator var2);

   String whyNot();
}
