package org.etlforge.integration.client.ui.admin.component.launcher;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.view.ViewLauncher;
import org.etlforge.application.ui.view.ViewManager;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.Label;

public final class AdminViewLauncher implements ViewLauncher {
   private final ViewManager viewManager;

   public AdminViewLauncher(ViewManager var1) {
      Preconditions.checkNotNull(var1, "mgr");
      this.viewManager = var1;
   }

   @Override
   public void activate(UiCanvas var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AdminViewLauncher.this.showView();
         }
      });
   }

   @Override
   public void decorate(Label var1) {
      var1.setIcon(ClientIcons.ADMINISTRATION_24);
      var1.setText(ApplicationResources.MNEMONIC_FACTORY.fromString(AdminViewResources.getString("Launcher.Name")));
      var1.setToolTip(AdminViewResources.getString("Launcher.Tooltip"));
   }

   private void showView() {
      this.viewManager.showView("administration");
   }

   @Override
   public void dismiss() {
   }
}
