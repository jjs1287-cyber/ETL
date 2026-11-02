package org.etlforge.integration.client.ui.connector.netsuite.function.txwiz;

import java.util.EnumSet;
import org.etlforge.application.ui.wizard.EnumSelectionPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.data.connector.NetSuiteFunction;

public final class FunctionTypeSelectionPage extends EnumSelectionPage<NetSuiteFunction.FunctionType> {
   public FunctionTypeSelectionPage(Wizard var1, boolean var2) {
      super(var1, "Select a NetSuite Function Type", getValues(var2));
   }

   private static Iterable<NetSuiteFunction.FunctionType> getValues(boolean var0) {
      EnumSet var1 = EnumSet.allOf(NetSuiteFunction.FunctionType.class);
      if (var0) {
         var1.remove(NetSuiteFunction.FunctionType.QUERY);
      }

      return var1;
   }
}
