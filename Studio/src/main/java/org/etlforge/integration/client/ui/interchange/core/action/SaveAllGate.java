package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorSaver;
import org.etlforge.application.ui.window.editor.EditorSaverResult;
import org.etlforge.integration.client.ui.entity.dialog.DirtyDelegatesDialog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.callback.Receiver;

public final class SaveAllGate {
   private final ApplicationWindow appWin;
   private final EditorSaver editorSaver;

   public SaveAllGate(ApplicationWindow var1, EditorSaver var2) {
      this.appWin = var1;
      this.editorSaver = var2;
   }

   public void saveAndRun(Runnable var1) {
      List var2 = this.getDirtyEntities();
      if (var2.isEmpty()) {
         var1.run();
      } else {
         this.saveAndRunImpl(var2, var1);
      }
   }

   private List<IntegrationEntity> getDirtyEntities() {
      ArrayList var1 = Lists.newArrayList();

      for (Editor var5 : this.appWin.getEditorService().getOpenEditors()) {
         if (var5.isDirty() && var5 instanceof IntegrationEntityPage) {
            var1.add((IntegrationEntity)var5.getObject());
         }
      }

      return var1;
   }

   private void saveAndRunImpl(List<IntegrationEntity> var1, final Runnable var2) {
      Receiver var3 = new Receiver<EditorSaverResult>() {
         public void handle(EditorSaverResult var1) {
            if (!var1.wasNoOp() && var1.nothingFailed()) {
               var2.run();
            }
         }
      };
      if (DirtyDelegatesDialog.showSaveDialog(this.appWin.getFrame(), var1) == 0) {
         EditorSaverResult var4 = this.editorSaver.saveAllEditors();
         var3.handle(var4);
      } else {
         var3.handle(new EditorSaverResult());
      }
   }
}
