package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorSaver;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;

public final class SaveActiveEditorAction extends ApplicationEditorAction {
   public static final String ID = "action.Save";
   private final EditorSaver saver;

   public SaveActiveEditorAction(ApplicationWindow var1, EditorSaver var2) {
      super(var1, "action.Save", WindowActionDictionary.DICTIONARY);
      this.saver = var2;
      this.setEnabled(false);
   }

   @Override
   public void execute(ActionEvent var1) {
      (new BusyWorker(this.getWaitService()) {
         @Override
         protected void doWork() {
            SaveActiveEditorAction.this.saver.saveActiveEditor();
         }
      }).run();
   }

   @Override
   public void dirtyStateChanged(EditorServiceEvent var1) {
      this.checkEnabledState();
   }

   private void checkEnabledState() {
      Editor var1 = this.getActiveEditor();
      boolean var2 = var1 != null && var1.isDirty();
      this.setEnabled(var2);
   }

   @Override
   public void activeEditorChanged(EditorServiceEvent var1) {
      this.checkEnabledState();
   }

   @Override
   public void editorClosed(EditorServiceEvent var1) {
      this.checkEnabledState();
   }

   @Override
   public void editorOpened(EditorServiceEvent var1) {
      this.checkEnabledState();
   }
}
