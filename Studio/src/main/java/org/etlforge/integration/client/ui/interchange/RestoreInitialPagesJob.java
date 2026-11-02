package org.etlforge.integration.client.ui.interchange;

import java.awt.EventQueue;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.project.ProjectUiState;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.util.UiUtils;

final class RestoreInitialPagesJob extends UiJob {
   private final EntityPageDisplayer pageDisplayer;
   private final EditorService editorService;
   private final ProjectUiState state;


   public RestoreInitialPagesJob(EntityPageDisplayer var1, EditorService var2, ProjectUiState var3) {
      super("Restore open pages");
      this.pageDisplayer = var1;
      this.editorService = var2;
      this.state = var3;

   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setCompletionValue(0);
      List var1 = this.state.getEntitiesToDisplayOnStartUp();
      int var2 = var1.size() + 1;
      int var3 = 0;
      IntegrationEntity var4 = this.state.getActiveEntityOnStartUp();

      for (Object __etlItem41 : var1) {
         IntegrationEntity var6 = (IntegrationEntity) __etlItem41;
         if (this.isCancelled()) {
            return;
         }

         if (var6 != var4) {
            this.setMessage("Now opening the page for \"" + var6.getName() + "\"");
            this.openEditor(var6);
            var3++;
            this.setCompletionValue(100 * var3 / var2);
         }
      }

      if (!this.isCancelled()) {
         if (var4 != null) {
            this.setMessage("Setting the active page");
            this.openEditor(var4);
         }


         this.setCompletionValue(100);
         if (!this.isCancelled()) {
            this.setInitialFocus();
         }
      }
   }

   private void openEditor(final IntegrationEntity var1) {
      Runnable var2 = new Runnable() {
         @Override
         public void run() {
            RestoreInitialPagesJob.this.pageDisplayer.openPage(var1);
         }
      };
      UiUtils.runOnEventThreadAndWait(var2, new LoggingExceptionHandler(RestoreInitialPagesJob.class, "Failed to open the page for " + var1.getName()));
   }

   private void setInitialFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Editor var1 = RestoreInitialPagesJob.this.getInitiallyFocusedPage();
            if (var1 != null) {
               RestoreInitialPagesJob.this.giveFocusToPage(var1);
            }
         }
      });
   }

   private Editor getInitiallyFocusedPage() {
      Editor var1 = this.editorService.getActiveEditor();
      if (var1 != null) {
         return var1;
      } else {
         return this.editorService.getNumberOfEditors() > 0 ? this.editorService.getOpenEditors()[0] : null;
      }
   }

   private void giveFocusToPage(final Editor var1) {
      DelayedJob.createAndSchedule(500, new Runnable() {
         @Override
         public void run() {
            var1.requestFocus();
         }
      });
   }
}
