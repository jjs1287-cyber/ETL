package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class LocatePageInTreeAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.LocatePageInTree";
   private final EditorService editorService;

   public LocatePageInTreeAction(EditorService var1) {
      super("action.ic.LocatePageInTree", InterchangeActionDictionary.DICTIONARY);
      this.editorService = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      Editor var2 = this.editorService.getActiveEditor();
      if (var2 != null) {
         Object var3 = var2.getObject();
         if (var3 instanceof IntegrationEntity) {
            UiCompanion.of((IntegrationEntity)var3).displayInTree();
         }
      }
   }
}
