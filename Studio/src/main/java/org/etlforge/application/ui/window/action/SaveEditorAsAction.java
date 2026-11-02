package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.ui.widget.text.undo.UndoableEditTimer;

public final class SaveEditorAsAction extends ApplicationEditorAction {
   public static final String ID = "action.SaveAs";

   public SaveEditorAsAction(ApplicationWindow var1) {
      super(var1, "action.SaveAs", WindowActionDictionary.DICTIONARY);
      this.setEnabled(false);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SaveEditorAsAction.this.saveAs();
         }
      });
   }

   private void saveAs() {
      Editor var1 = this.getEditorService().getActiveEditor();
      if (var1 != null) {
         UndoableEditTimer.getTimer().sendNow();
         this.saveEditor(var1);
      }
   }

   private void saveEditor(Editor var1) {
      try {
         var1.saveAs();
      } catch (SaveEditorException var3) {
         ErrorLog.attention(SaveEditorAsAction.class, var3.getMessage(), var3);
      }
   }

   @Override
   public void activeEditorChanged(EditorServiceEvent var1) {
      Editor var2 = var1.getEditor();
      boolean var3 = var2 != null && var2.isSaveAsAllowed();
      this.setEnabled(var3);
   }
}
