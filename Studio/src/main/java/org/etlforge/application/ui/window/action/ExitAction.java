package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.Application;
import org.etlforge.application.ui.action.AbstractApplicationAction;

public class ExitAction extends AbstractApplicationAction {
   public static final String ID = "action.Exit";

   public ExitAction() {
      super("action.Exit", WindowActionDictionary.DICTIONARY);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Application.exit();
         }
      });
   }
}
