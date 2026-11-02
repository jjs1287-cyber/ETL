package org.etlforge.integration.client.ui.connector.autodesk.function.txwiz;

import org.etlforge.application.ui.wizard.EnumSelectionPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.data.connector.AutoDeskFunction;

public final class FunctionTypeSelectionPage extends EnumSelectionPage<AutoDeskFunction.FunctionType> {
   public FunctionTypeSelectionPage(Wizard var1) {
      super(var1, "Select a Function Type", AutoDeskFunction.FunctionType.class);
   }
}
