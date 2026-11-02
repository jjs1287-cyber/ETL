package org.etlforge.integration.client.ui.connector.msgp.function.txwiz;

import org.etlforge.application.ui.wizard.EnumSelectionPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.data.connector.MSGPFunction;

public final class FunctionTypeSelectionPage extends EnumSelectionPage<MSGPFunction.FunctionType> {
   public FunctionTypeSelectionPage(Wizard var1) {
      super(var1, "Select a Function Type", MSGPFunction.FunctionType.class);
   }
}
