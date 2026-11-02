package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;

public final class EditTransformationMenu extends TransformationPageActionsMenu {
   public EditTransformationMenu(TransformationPage var1) {
      super("menu.ic.tx.Edit", var1, "action.ic.tx.EditSource", "action.ic.tx.EditTarget", "action.ic.tx.EditWhereClause", "action.ic.tx.EditUpdateKeys");
   }
}
