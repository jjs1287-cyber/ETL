package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import java.util.List;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperation;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;

final class RemoveOperationAction extends AbstractSingleTypeSelectionAction<TriggeredOperation> {
   private final ResponseModel responseModel;

   public RemoveOperationAction(ResponseModel var1) {
      super(TriggeredOperation.class, "Remove Operations");
      this.responseModel = var1;
   }

   @Override
   protected void run(List<TriggeredOperation> var1) {
      this.responseModel.removeOperations(var1);
   }
}
