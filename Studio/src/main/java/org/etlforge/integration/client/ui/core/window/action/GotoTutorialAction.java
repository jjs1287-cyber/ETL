package org.etlforge.integration.client.ui.core.window.action;

import org.etlforge.application.ui.browser.WebPage;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.util.ETLForgeWebPageFactory;

public final class GotoTutorialAction extends ETLForgeWebPageLauncherAction {
   public static final String ID = "action.core.Tutorial";

   public GotoTutorialAction(ApplicationWindow var1) {
      super(var1, "action.core.Tutorial");
   }

   @Override
   protected WebPage getWebPage(ETLForgeWebPageFactory var1) {
      return var1.getTutorialPage();
   }
}
