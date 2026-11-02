package com.etlforge.integration.client.ui.sfdc;

import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlEditor;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.FieldNameDisplayPreference;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.ObjectNameDisplayPreference;
import com.etlforge.integration.salesforce.ui.NameDisplayModes;

public final class SoqlEditorFactory {
   public static SoqlEditor newEditor(SalesforceFacade var0) {
      SoqlEditor var1 = new SoqlEditor(var0);
      var1.setNameDisplayModes(NameDisplayModes.of(ObjectNameDisplayPreference.INSTANCE.get(), FieldNameDisplayPreference.INSTANCE.get()));
      return var1;
   }

   private SoqlEditorFactory() {
   }
}
