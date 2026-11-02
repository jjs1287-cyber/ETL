package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorSaver;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;

public final class SaveAllEditorsAction extends ApplicationEditorAction {
   public static final String ID = "action.SaveAll";
   private final EditorSaver saver;

   public SaveAllEditorsAction(ApplicationWindow var1, EditorSaver var2) {
      super(var1, "action.SaveAll", WindowActionDictionary.DICTIONARY);
      this.saver = var2;
      this.setEnabled(false);
   }

   @Override
   public void execute(ActionEvent var1) {
      (new BusyWorker(this.getWaitService()) {
         @Override
         protected void doWork() {
            SaveAllEditorsAction.this.saver.saveAllEditors();
         }
      }).run();
   }

   @Override
   public void editorClosed(EditorServiceEvent var1) {
      this.updateEnabledState();
   }

   private void updateEnabledState() {
      boolean var1 = this.isAtLeastOneEditorDirty();
      this.setEnabled(var1);
   }

   private boolean isAtLeastOneEditorDirty() {
      for (Editor var4 : this.getEditorService().getOpenEditors()) {
         if (var4.isDirty()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void editorOpened(EditorServiceEvent var1) {
      this.updateEnabledState();
   }

   @Override
   public void dirtyStateChanged(EditorServiceEvent var1) {
      this.updateEnabledState();
   }
}
