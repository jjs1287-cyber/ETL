package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.application.Application;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.TransformationEditor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.SourceTarget;

final class TransformationEditorLauncher {
   public static void startEditing(TransformationPage var0, SourceTarget var1) {
      TransformationEditor var2 = var0.getStructureEditor();
      runEditor(var2, var1);
   }

   private static void runEditor(final TransformationEditor var0, final SourceTarget var1) {
      ApplicationWorker var2 = Application.getWorker();
      var2.submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            if (var1 == SourceTarget.Source) {
               var0.editInputStructure();
            } else {
               var0.editOutputStructure();
            }
         }
      });
   }

   private TransformationEditorLauncher() {
   }
}
