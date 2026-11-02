package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.groovy.GroovyConsole;
import org.etlforge.application.ui.window.ApplicationWindow;

public final class OpenGroovyConsoleAction extends AbstractApplicationWindowAction {
   public static final String ID = "action.GroovyConsole";
   private static final String CONSOLE_BEAN_ID = "GroovyConsole";

   public OpenGroovyConsoleAction(ApplicationWindow var1) {
      super(var1, "action.GroovyConsole", WindowActionDictionary.DICTIONARY);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OpenGroovyConsoleAction.this.openConsole();
         }
      });
   }

   private void openConsole() {
      GroovyConsole var1 = this.getConsole();
      if (var1 != null) {
         var1.open(this.getWindow());
      }
   }

   private GroovyConsole getConsole() {
      if (Application.containsBean("GroovyConsole")) {
         return Application.getBean("GroovyConsole", GroovyConsole.class);
      }

      ErrorLog.attention(this.getClass(), "The current application configuration does not support the Groovy Console.", new Exception());
      return null;
   }
}
