package org.etlforge.integration.client.ui.core.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.UnsupportedPlatformException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.browser.WebBrowser;
import org.etlforge.application.ui.browser.WebBrowserFactory;
import org.etlforge.application.ui.browser.WebPage;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractApplicationWindowAction;

public abstract class AbstractWebPageLauncherAction extends AbstractApplicationWindowAction {
   private WebBrowserFactory browserFactory;

   public AbstractWebPageLauncherAction(ApplicationWindow var1, String var2) {
      super(var1, var2, ClientWindowActionDictionary.DICTIONARY);
      this.setEnabled(true);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AbstractWebPageLauncherAction.this.executeImpl();
         }
      });
   }

   private void executeImpl() {
      WebBrowserFactory var1 = this.getBrowserFactory();
      if (var1 != null) {
         WebPage var2 = this.getWebPage();
         if (var2 != null) {
            WebBrowser var3 = var1.createBrowser();
            var3.open(var2);
         }
      }
   }

   protected abstract WebPage getWebPage();

   private WebBrowserFactory getBrowserFactory() {
      if (this.browserFactory == null && WebBrowserFactory.isPlatformSupported()) {
         try {
            this.browserFactory = WebBrowserFactory.newInstance();
         } catch (UnsupportedPlatformException var2) {
            ErrorLog.attention(AbstractWebPageLauncherAction.class, var2.getMessage(), var2);
         }
      }

      return this.browserFactory;
   }
}
