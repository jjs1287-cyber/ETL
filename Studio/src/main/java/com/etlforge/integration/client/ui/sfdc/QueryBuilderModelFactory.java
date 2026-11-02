package com.etlforge.integration.client.ui.sfdc;

import com.etlforge.integration.client.ui.sfdc.wizard.prefs.FieldNameDisplayPreference;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.ObjectNameDisplayPreference;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.QueryBuilderModel;
import com.etlforge.integration.salesforce.ui.NameDisplayModes;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class QueryBuilderModelFactory {
   public static QueryBuilderModel newModel() {
      QueryBuilderModel var0 = new QueryBuilderModel();
      var0.setNameDisplayModes(NameDisplayModes.of(ObjectNameDisplayPreference.INSTANCE.get(), FieldNameDisplayPreference.INSTANCE.get()));
      return var0;
   }

   public static QueryBuilderModel newModel(SObjectSchema var0) {
      QueryBuilderModel var1 = newModel();
      var1.setSObject(var0);
      return var1;
   }

   private QueryBuilderModelFactory() {
   }
}
