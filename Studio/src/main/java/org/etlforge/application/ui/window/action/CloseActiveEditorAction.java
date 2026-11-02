package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;

public final class CloseActiveEditorAction extends ApplicationEditorAction {
   public static final String ID = "action.Close";

   public CloseActiveEditorAction(ApplicationWindow var1) {
      super(var1, "action.Close", WindowActionDictionary.DICTIONARY);
      this.update(var1.getEditorService());
   }

   @Override
   public void execute(ActionEvent var1) {
      this.closeActiveEditor();
   }

   private void closeActiveEditor() {
      Editor var1 = this.getEditorService().getActiveEditor();
      if (var1 != null) {
         this.closeEditor(var1);
      }
   }

   private void closeEditor(Editor var1) {
      try {
         var1.close();
      } catch (Exception var3) {
         ErrorLog.log(this.getClass(), "An unexpected error occurred while closing the editor \"" + var1.getTitle() + "\".", var3);
      }
   }

   @Override
   public void editorClosed(EditorServiceEvent var1) {
      this.update(var1.getSource());
   }

   @Override
   public void editorOpened(EditorServiceEvent var1) {
      this.update(var1.getSource());
   }

   @Override
   public void activeEditorChanged(EditorServiceEvent var1) {
      this.update(var1.getSource());
   }

   private void update(EditorService var1) {
      this.setEnabled(var1.getActiveEditor() != null);
   }
}
