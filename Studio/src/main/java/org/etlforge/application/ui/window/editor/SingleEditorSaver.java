package org.etlforge.application.ui.window.editor;

import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.ui.widget.text.undo.UndoableEditTimer;

public final class SingleEditorSaver implements Runnable {
   private final Editor editorToSave;
   private final EditorSaverResult result;

   public SingleEditorSaver(Editor var1, EditorSaverResult var2) {
      this.editorToSave = var1;
      this.result = var2;
   }

   @Override
   public void run() {
      UndoableEditTimer.getTimer().sendNow();
      this.saveEditor();
   }

   private void saveEditor() {
      try {
         this.editorToSave.save();
         this.result.saved(this.editorToSave);
      } catch (SaveEditorException var2) {
         this.handleError(var2);
      }
   }

   private void handleError(SaveEditorException var1) {
      NonBlockingAlert.error(this.editorToSave.getWindow().getFrame(), var1.getMessage(), "Cannot Save");
      this.result.failed(this.editorToSave, var1);
   }
}
