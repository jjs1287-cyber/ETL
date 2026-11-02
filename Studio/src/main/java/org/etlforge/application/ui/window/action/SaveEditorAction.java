package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorSaverResult;
import org.etlforge.application.ui.window.editor.SingleEditorSaver;

public final class SaveEditorAction extends AbstractAction {
   private final Editor editor;

   public SaveEditorAction(Editor var1) {
      this.editor = var1;
      ActionDecorators.decorateFromDictionary(this, WindowActionDictionary.DICTIONARY, "action.Save");
      var1.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            SaveEditorAction.this.checkEnabledState();
         }
      });
      this.checkEnabledState();
   }

   private void checkEnabledState() {
      boolean var1 = this.editor.isDirty();
      this.setEnabled(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      (new BusyWorker(this.editor.getWindow()) {
         @Override
         protected void doWork() {
            SaveEditorAction.this.doSave();
         }
      }).run();
   }

   private void doSave() {
      EditorSaverResult var1 = new EditorSaverResult();
      SingleEditorSaver var2 = new SingleEditorSaver(this.editor, var1);
      var2.run();
   }
}
