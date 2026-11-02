package org.etlforge.integration.client.ui.core.window.action;

import org.etlforge.application.ui.browser.WebPage;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.util.ETLForgeWebPageFactory;

public abstract class ETLForgeWebPageLauncherAction extends AbstractWebPageLauncherAction {
   private ETLForgeWebPageFactory webPageFactory;

   public ETLForgeWebPageLauncherAction(ApplicationWindow var1, String var2) {
      super(var1, var2);
   }

   @Override
   protected WebPage getWebPage() {
      ETLForgeWebPageFactory var1 = this.getWebPageFactory();
      return this.getWebPage(var1);
   }

   protected abstract WebPage getWebPage(ETLForgeWebPageFactory var1);

   private ETLForgeWebPageFactory getWebPageFactory() {
      if (this.webPageFactory == null) {
         this.webPageFactory = ETLForgeWebPageFactory.newInstance();
      }

      return this.webPageFactory;
   }
}
