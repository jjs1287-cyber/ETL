package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.service.BackwardForwardStack;

public final class MoveToNextEditorAction extends ApplicationEditorAction {
   public static final String ID = "action.NextEditor";
   private BackwardForwardStack stack;

   public MoveToNextEditorAction(ApplicationWindow var1) {
      super(var1, "action.NextEditor", WindowActionDictionary.DICTIONARY);
      this.updateStack();
   }

   @Override
   public void execute(ActionEvent var1) {
      this.stack.moveForward();
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
            MoveToNextEditorAction.this.stack = MoveToNextEditorAction.this.getEditorService().getBackwardForwardStack();
            Editor var3 = MoveToNextEditorAction.this.stack.getNextEditor();
            String var1;
            boolean var2;
            if (var3 == null) {
               var1 = WindowActionDictionary.DICTIONARY.getToolTip(MoveToNextEditorAction.this.getID());
               var2 = false;
            } else {
               var1 = "Forward to " + var3.getTitle();
               var2 = true;
            }

            MoveToNextEditorAction.this.putValue("ShortDescription", var1);
            MoveToNextEditorAction.this.setEnabled(var2);
         }
      });
   }
}
