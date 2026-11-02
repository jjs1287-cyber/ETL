package com.etlforge.integration.client.ui.sfdc.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;

final class CloseMappingTransformationAction extends AbstractAction {
   private final TransformationPage txPage;
   private final Editor wizardPage;

   public CloseMappingTransformationAction(TransformationPage var1, Editor var2) {
      super("&Finish", ApplicationResources.ICONS.getIcon("NextEditor.24"));
      this.txPage = var1;
      this.wizardPage = var2;
      this.putValue("ShortDescription", "Close the mapping tree and return to the operation you just created");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CloseMappingTransformationAction.this.finish();
         }
      });
   }

   private void finish() {
      boolean var1 = this.saveAndClose();
      if (var1) {
         this.goBackToWizard();
      }
   }

   private boolean saveAndClose() {
      try {
         this.txPage.save();
         return this.txPage.close();
      } catch (SaveEditorException var2) {
         NonBlockingAlert.error(this.txPage.getMainPanel(), var2.getMessage(), "Error");
         return false;
      }
   }

   private void goBackToWizard() {
      ApplicationWindow var1 = this.wizardPage.getWindow();
      if (var1 != null) {
         EditorService var2 = var1.getEditorService();
         var2.openEditor(this.wizardPage);
      }
   }
}
