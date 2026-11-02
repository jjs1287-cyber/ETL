package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.OpenEditorsDialog;

public final class ShowOpenPagesDialogAction extends AbstractApplicationWindowAction {
   public static final String ID = "action.ShowOpenPages";

   public ShowOpenPagesDialogAction(ApplicationWindow var1) {
      super(var1, "action.ShowOpenPages", WindowActionDictionary.DICTIONARY);
   }

   @Override
   public void execute(ActionEvent var1) {
      new BusyWorker(this.getWindow(), new Runnable() {
         @Override
         public void run() {
            ApplicationWindow var1x = ShowOpenPagesDialogAction.this.getWindow();
            OpenEditorsDialog.displayPages(var1x.getFrame(), var1x.getEditorService());
         }
      }).run();
   }
}
