package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.SourceTarget;

public final class EditTransformationSourceStructureAction extends AbstractEditTransformationAction {
   public static final String ID = "action.ic.tx.EditSource";

   public EditTransformationSourceStructureAction(TransformationPage var1) {
      super("action.ic.tx.EditSource", var1, SourceTarget.Source);
   }
}
