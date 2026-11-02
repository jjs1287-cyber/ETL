package org.etlforge.integration.client.ui.interchange.core.action;

import java.util.concurrent.Executor;
import org.etlforge.application.Application;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorSaver;
import org.etlforge.application.ui.window.editor.EditorSaverResult;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.entity.EntityPageLocator;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class EntityPageSaveGate {
   private final EntityPageLocator pageFinder;
   private final EditorSaver pageSaver;

   public EntityPageSaveGate(EntityPageLocator var1, EditorSaver var2) {
      this.pageFinder = var1;
      this.pageSaver = var2;
   }

   public void runInBackgroundThread(IntegrationEntity var1, Runnable var2) {
      Executor var3 = new Executor() {
         @Override
         public void execute(Runnable var1) {
            ApplicationWorker var2x = Application.getWorker();
            var2x.submitForParallel(ApplicationWorker.GUARD.protect(var1));
         }
      };
      this.run(var1, var2, var3);
   }

   private void run(IntegrationEntity var1, Runnable var2, Executor var3) {
      Editor var4 = this.pageFinder.findOpenPage(var1);
      if (this.isDirty(var4)) {
         this.savePageAndRun(var4, var2);
      } else {
         var3.execute(var2);
      }
   }

   private boolean isDirty(Editor var1) {
      return var1 != null && var1.isDirty();
   }

   private void savePageAndRun(Editor var1, Runnable var2) {
      assert var1.isDirty();
      EditorSaverResult var3 = this.pageSaver.saveEditor(var1);
      if (var3.wasSaved(var1)) {
         this.runJob(var2);
      }
   }

   private void runJob(Runnable var1) {
      ApplicationWorker var2 = Application.getWorker();
      var2.submitForParallel(ApplicationWorker.GUARD.protect(var1));
   }

   public void runInCallingThread(IntegrationEntity var1, Runnable var2) {
      Executor var3 = new Executor() {
         @Override
         public void execute(Runnable var1) {
            var1.run();
         }
      };
      this.run(var1, var2, var3);
   }
}
