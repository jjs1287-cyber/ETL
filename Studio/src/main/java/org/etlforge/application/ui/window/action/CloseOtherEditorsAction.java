package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;

public final class CloseOtherEditorsAction extends ApplicationEditorAction {
   public static final String ID = "action.CloseOthers";

   public CloseOtherEditorsAction(ApplicationWindow var1) {
      super(var1, "action.CloseOthers", WindowActionDictionary.DICTIONARY);
      this.update();
   }

   @Override
   public void execute(ActionEvent var1) {
      EditorService var2 = this.getEditorService();
      if (var2.getNumberOfEditors() > 1) {
         Editor var3 = var2.getActiveEditor();
         var2.closeOtherEditors(var3);
      }
   }

   @Override
   public void activeEditorChanged(EditorServiceEvent var1) {
      this.update();
   }

   @Override
   public void editorClosed(EditorServiceEvent var1) {
      this.update();
   }

   @Override
   public void editorOpened(EditorServiceEvent var1) {
      this.update();
   }

   private void update() {
      this.setEnabled(this.getEditorService().getNumberOfEditors() > 1);
   }
}
