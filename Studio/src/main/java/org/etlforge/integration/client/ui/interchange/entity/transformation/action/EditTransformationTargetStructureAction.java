package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.SourceTarget;

public final class EditTransformationTargetStructureAction extends AbstractEditTransformationAction {
   public static final String ID = "action.ic.tx.EditTarget";

   public EditTransformationTargetStructureAction(TransformationPage var1) {
      super("action.ic.tx.EditTarget", var1, SourceTarget.Target);
   }
}
