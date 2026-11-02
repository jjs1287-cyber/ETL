package com.etlforge.integration.client.ui.sfdc.soql.v4;

import java.util.List;
import org.etlforge.integration.salesforce.SObject;

public interface SObjectSelectionListener {
   void objectSelected(SObject var1);

   void fieldsSelected(SObject var1, List<SObject.Field> var2);

   class Adapter implements SObjectSelectionListener {
      @Override
      public void objectSelected(SObject var1) {
      }

      @Override
      public void fieldsSelected(SObject var1, List<SObject.Field> var2) {
      }
   }
}
