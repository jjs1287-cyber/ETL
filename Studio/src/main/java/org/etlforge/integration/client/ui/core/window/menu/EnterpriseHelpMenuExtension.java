package org.etlforge.integration.client.ui.core.window.menu;

import java.util.List;
import javax.swing.Action;
import javax.swing.JMenu;
import org.etlforge.application.ui.menu.AbstractMenuExtension;
import org.etlforge.application.ui.menu.MenuContribution;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.window.menu.HelpMenuExtension;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.widget.menu.KongaMenu;

public final class EnterpriseHelpMenuExtension extends AbstractMenuExtension {
   private final List<? extends Action> licenseActions;
   private final HelpMenuExtension communityExtension;

   public EnterpriseHelpMenuExtension(List<? extends Action> var1, List<? extends Action> var2) {
      super("menu.Help");
      this.communityExtension = new HelpMenuExtension(var1);
      this.licenseActions = var2;
   }

   @Override
   public MenuContribution createExtension(MenuExtensionPoint var1) {
      return var1 == MenuExtensionPoint.EXTENSION_POINT_3 ? this.createLicenseContribution() : this.communityExtension.createExtension(var1);
   }

   private MenuContribution createLicenseContribution() {
      MenuContribution var1 = null;
      var1 = new MenuContribution("Help.License");
      JMenu var2 = this.createLicenseMenu();
      var1.addItem(var2);
      return var1;
   }

   private JMenu createLicenseMenu() {
      KongaMenu var1 = new KongaMenu("&License", ClientIcons.LICENSE_16);

      for (Action var3 : this.licenseActions) {
         var1.add(var3);
      }

      return var1;
   }
}
