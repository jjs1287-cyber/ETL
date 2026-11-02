package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.service.BackwardForwardStack;

public final class MoveToPreviousEditorAction extends ApplicationEditorAction {
   public static final String ID = "action.PreviousEditor";
   private BackwardForwardStack stack;

   public MoveToPreviousEditorAction(ApplicationWindow var1) {
      super(var1, "action.PreviousEditor", WindowActionDictionary.DICTIONARY);
      this.updateStack();
   }

   @Override
   public void execute(ActionEvent var1) {
      this.stack.moveBackward();
   }

   @Override
   public void editorClosed(EditorServiceEvent var1) {
      this.updateStack();
   }

   @Override
   public void editorOpened(EditorServiceEvent var1) {
      this.updateStack();
   }

   @Override
   public void activeEditorChanged(EditorServiceEvent var1) {
      this.updateStack();
   }

   private void updateStack() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            MoveToPreviousEditorAction.this.stack = MoveToPreviousEditorAction.this.getEditorService().getBackwardForwardStack();
            Editor var3 = MoveToPreviousEditorAction.this.stack.getPreviousEditor();
            String var1;
            boolean var2;
            if (var3 == null) {
               var1 = WindowActionDictionary.DICTIONARY.getToolTip(MoveToPreviousEditorAction.this.getID());
               var2 = false;
            } else {
               var1 = "Back to " + var3.getTitle();
               var2 = true;
            }

            MoveToPreviousEditorAction.this.putValue("ShortDescription", var1);
            MoveToPreviousEditorAction.this.setEnabled(var2);
         }
      });
   }
}
