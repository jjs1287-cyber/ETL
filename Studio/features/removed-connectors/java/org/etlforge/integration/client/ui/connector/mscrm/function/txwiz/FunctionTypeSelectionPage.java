package org.etlforge.integration.client.ui.connector.mscrm.function.txwiz;

import java.util.EnumSet;
import org.etlforge.application.ui.wizard.EnumSelectionPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.mscrm.FunctionType;

public final class FunctionTypeSelectionPage extends EnumSelectionPage<FunctionType> {
   public FunctionTypeSelectionPage(Wizard var1, boolean var2) {
      super(var1, "Select a MSCrm Function Type", getValues(var2));
   }

   private static Iterable<FunctionType> getValues(boolean var0) {
      EnumSet var1 = EnumSet.allOf(FunctionType.class);
      if (var0) {
         var1.remove(FunctionType.RETRIEVE);
      }

      return var1;
   }
}
