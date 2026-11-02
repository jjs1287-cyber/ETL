package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.ApplicationWindow;

public final class WriteActiveObjectsAction extends AbstractApplicationAction {
   public static final String ID = "action.ActiveObjects";
   private final ActiveObjectService activeObjectService;

   public WriteActiveObjectsAction(ApplicationWindow var1) {
      super("action.ActiveObjects", WindowActionDictionary.DICTIONARY);
      this.activeObjectService = var1.getActiveObjectService();
      this.setEnabled(true);
   }

   @Override
   public void execute(ActionEvent var1) {
      System.out.println("@@@ Active Objects:");
      System.out.println(this.activeObjectService.getActiveObjects());
      System.out.println();
   }
}
