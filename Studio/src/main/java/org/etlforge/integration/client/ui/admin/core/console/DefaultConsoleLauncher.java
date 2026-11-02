package org.etlforge.integration.client.ui.admin.core.console;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class DefaultConsoleLauncher implements ConsoleLauncher {
   private final Action action;
   private final JComponent ui;

   public DefaultConsoleLauncher(Action var1) {
      this.action = var1;
      this.ui = this.createLabel();
   }

   @Override
   public String getName() {
      String var1 = (String)this.action.getValue("Name");
      return ApplicationResources.MNEMONIC_FACTORY.asText(var1);
   }

   @Override
   public void launch() {
      ActionUtils.run(this.action, this, "activate");
   }

   @Override
   public void addToContextMenu(KongaPopupMenu var1) {
      var1.add(this.action);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocusInWindow();
   }

   private JLabel createLabel() {
      JLabel var1 = new JLabel(this.getName());
      Icon var2 = (Icon)this.action.getValue("SwingLargeIconKey");
      var1.setIcon(var2);
      var1.setToolTipText((String)this.action.getValue("ShortDescription"));
      TextStyles.AdminHintBold.makeOver(var1);
      return var1;
   }
}
