package org.etlforge.integration.client.ui.core.window.action;

import org.etlforge.application.ui.browser.WebPage;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.util.ETLForgeWebPageFactory;

public final class GotoSupportForumAction extends ETLForgeWebPageLauncherAction {
   public static final String ID = "action.core.Forum";

   public GotoSupportForumAction(ApplicationWindow var1) {
      super(var1, "action.core.Forum");
   }

   @Override
   protected WebPage getWebPage(ETLForgeWebPageFactory var1) {
      return var1.getForumsPage();
   }
}
