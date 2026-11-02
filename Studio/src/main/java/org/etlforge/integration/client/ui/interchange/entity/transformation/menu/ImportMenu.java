package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;

public final class ImportMenu extends TransformationPageActionsMenu {
   public ImportMenu(TransformationPage var1) {
      super("menu.ic.tx.Import", var1, "action.ic.tx.ImportMappings", "action.ic.tx.ImportMultipleFolders");
   }
}
