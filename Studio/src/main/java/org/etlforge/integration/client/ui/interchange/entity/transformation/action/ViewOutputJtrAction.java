package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.data.entity.Transformation;

public final class ViewOutputJtrAction extends ViewMappingStructureDataAction {
   public static final String ID = "action.ic.tx.OutputJtr";

   public ViewOutputJtrAction(ApplicationWindow var1, Transformation var2) {
      super("action.ic.tx.OutputJtr", var1, var2, ViewMappingStructureDataAction.Mode.OUTPUT);
   }
}
