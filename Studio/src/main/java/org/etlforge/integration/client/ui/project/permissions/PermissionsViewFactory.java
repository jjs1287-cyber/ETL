package org.etlforge.integration.client.ui.project.permissions;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.client.server.user.UserManager;

public class PermissionsViewFactory implements WindowSectionContentProvider {
   private final UserManager userManager;

   public PermissionsViewFactory(UserManager var1) {
      this.userManager = var1;
   }

   public PermissionsView getContent(WindowSection var1) {
      return new PermissionsViewImpl(var1, this.userManager);
   }

   @Override
   public String getContentId() {
      return "permissions_view";
   }
}
