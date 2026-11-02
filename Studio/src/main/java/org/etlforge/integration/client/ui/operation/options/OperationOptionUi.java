package org.etlforge.integration.client.ui.operation.options;

import org.etlforge.integration.data.entity.operation.options.OperationOption;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;

public interface OperationOptionUi extends UiProvider {
   OperationOption getDefinedOption();

   void registerAsChangeSource(InputPanel var1);
}
