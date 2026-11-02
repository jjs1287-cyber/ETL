package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;

public final class CloseAllEditorsAction extends ApplicationEditorAction {
   public static final String ID = "action.CloseAll";

   public CloseAllEditorsAction(ApplicationWindow var1) {
      super(var1, "action.CloseAll", WindowActionDictionary.DICTIONARY);
      this.update();
   }

   @Override
   public void execute(ActionEvent var1) {
      this.getEditorService().closeAllEditors();
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
      this.setEnabled(this.getEditorService().getNumberOfEditors() > 0);
   }
}
