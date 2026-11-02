package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.editor.EditorService;

public final class QuickSwitchEditorAction extends AbstractApplicationAction {
   public static final String ID = "action.QuickSwitchEditor";
   private final EditorService editorService;

   public QuickSwitchEditorAction(EditorService var1) {
      super("action.QuickSwitchEditor", WindowActionDictionary.DICTIONARY);
      this.editorService = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            QuickSwitchEditorAction.this.openQuickSelector();
         }
      });
   }

   private void openQuickSelector() {
      if (this.editorService.getNumberOfEditors() >= 2) {
         this.editorService.openQuickSelector();
      }
   }
}
