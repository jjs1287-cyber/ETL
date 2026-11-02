package org.etlforge.application.ui.help;

import javax.swing.JButton;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.ui.widget.button.ButtonUtils;

public final class HelpButtonResource extends AbstractWindowElementResource {
   private final String helpId;
   private final JButton helpButton;

   public static JButton forToolbar(WindowElement var0, String var1) {
      HelpButtonResource var2 = new HelpButtonResource(var1);
      var0.addResource(var2);
      return var2.getButtonForToolbar();
   }

   public HelpButtonResource(String var1) {
      this.helpId = var1;
      this.helpButton = ApplicationUiHelp.createHelpButton(false);
   }

   public JButton getButton() {
      return this.helpButton;
   }

   public JButton getButtonForToolbar() {
      return ButtonUtils.configureButtonForToolBar(this.helpButton);
   }

   @Override
   public void create(WindowElement var1) {
      ApplicationUiHelp.getHelp().enableHelpOnButton(this.helpButton, this.helpId);
   }

   @Override
   public void dispose(WindowElement var1) {
      ApplicationUiHelp.getHelp().disableHelpOnButton(this.helpButton);
   }
}
