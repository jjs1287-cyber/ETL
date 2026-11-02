package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.ApplicationEditorAction;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.project.page.ProjectPage;

public final class EditEntityDescriptionAction extends ApplicationEditorAction {
   public static final String ID = "action.ic.Description";

   public EditEntityDescriptionAction(ApplicationWindow var1) {
      super(var1, "action.ic.Description", InterchangeActionDictionary.DICTIONARY);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EditEntityDescriptionAction.this.openDescriptionEditor();
         }
      });
   }

   private void openDescriptionEditor() {
      IntegrationEntityPage var1 = this.getEntityPage();
      if (var1 != null) {
         var1.showDescriptionEditor();
      }
   }

   @Override
   public void activeEditorChanged(EditorServiceEvent var1) {
      this.setEnabled(this.getEntityPage() != null);
   }

   private IntegrationEntityPage getEntityPage() {
      Editor var1 = this.getActiveEditor();
      if (var1 instanceof IntegrationEntityPage) {
         return var1 instanceof ProjectPage ? null : (IntegrationEntityPage)var1;
      } else {
         return null;
      }
   }
}
