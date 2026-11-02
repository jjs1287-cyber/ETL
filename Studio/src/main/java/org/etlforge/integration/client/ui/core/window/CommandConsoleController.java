package org.etlforge.integration.client.ui.core.window;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.ui.util.KeyUtils;

final class CommandConsoleController {
   private static final KeyStroke OPEN = KeyUtils.getCtrlDown(92);
   private static final KeyStroke HIDE = KeyUtils.getCtrlShiftDown(92);
   private final ApplicationWindow appWin;
   private final MainWindowFrame frame;

   public CommandConsoleController(ApplicationWindow var1, MainWindowFrame var2) {
      this.appWin = var1;
      this.frame = var2;
   }

   public void install() {
      this.installHide();
      this.installGiveFocusToCommandLine();
   }

   private void installHide() {
      KeyStroke var1 = ApplicationUi.requestGlobalKeyStroke(HIDE, this.getClass());
      if (var1 != null) {
         this.appWin.addGlobalKeyDispatcher(var1, new WindowKeyDispatcher() {
            @Override
            public String getId() {
               return "commandline.hide";
            }

            @Override
            public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
               if (CommandConsoleController.this.frame.isCommandConsoleVisible()) {
                  CommandConsoleController.this.frame.setCommandConsoleVisible(false);
                  return true;
               } else {
                  return false;
               }
            }
         });
      }
   }

   private void installGiveFocusToCommandLine() {
      KeyStroke var1 = ApplicationUi.requestGlobalKeyStroke(OPEN, this.getClass());
      if (var1 != null) {
         this.appWin.addGlobalKeyDispatcher(var1, new WindowKeyDispatcher() {
            @Override
            public String getId() {
               return "commandline.focus";
            }

            @Override
            public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
               if (!CommandConsoleController.this.frame.isCommandConsoleVisible()) {
                  CommandConsoleController.this.frame.setCommandConsoleVisible(true);
               }

               CommandConsoleController.this.appWin.getCommandConsole().requestFocus();
               return true;
            }
         });
      }
   }
}
