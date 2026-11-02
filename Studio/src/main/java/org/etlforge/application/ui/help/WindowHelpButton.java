package org.etlforge.application.ui.help;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import org.etlforge.ui.widget.button.ButtonUtils;

public final class WindowHelpButton {
   private final JButton helpButton;

   public WindowHelpButton(Window var1, String var2, boolean var3) {
      this.helpButton = ApplicationUiHelp.getHelp().createAndConfigureHelpButton(var2, var3);
      var1.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosed(WindowEvent var1) {
            ApplicationUiHelp.getHelp().disableHelpOnButton(WindowHelpButton.this.helpButton);
         }
      });
   }

   public JButton getButton() {
      return this.helpButton;
   }

   public JButton getButtonForToolbar() {
      return ButtonUtils.configureButtonForToolBar(this.helpButton);
   }
}
