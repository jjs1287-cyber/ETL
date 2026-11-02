package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import org.etlforge.integration.data.entity.Source;

public interface OperationSourceSelector {
   Source getSelectedSource();

   void addSourceListener(OperationSourceListener var1);
}
