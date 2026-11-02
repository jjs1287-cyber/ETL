package org.etlforge.integration.client.ui.interchange.entity.script.page;

import java.awt.EventQueue;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.ui.widget.text.undo.UndoableEditTimer;

final class PathChangeReaction implements Runnable {
   private final ScriptPage page;

   public PathChangeReaction(ScriptPage var1) {
      this.page = var1;
   }

   @Override
   public void run() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            PathChangeReaction.this.resetUndoRedo();
            PathChangeReaction.this.autoSave();
            PathChangeReaction.this.showMessage();
         }
      });
   }

   private void resetUndoRedo() {
      UndoableEditTimer.getTimer().sendNow();
      this.page.getUndoService().discardAllEdits();
   }

   private void autoSave() {
      try {
         this.page.save();
      } catch (SaveEditorException var2) {
         ErrorLog.log(ScriptPage.class, null, var2);
      }
   }

   private void showMessage() {
      String var1 = "Automatic Change";
      String var2 = "<html>One or more project items referred to in this script were renamed or moved to a new folder.<br><br>The script has been updated accordingly.</html>";
      this.page.showOverlayMessage(var1, var2, true);
   }
}
