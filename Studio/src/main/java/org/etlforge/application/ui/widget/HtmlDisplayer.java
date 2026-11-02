package org.etlforge.application.ui.widget;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;
import org.etlforge.application.UnsupportedPlatformException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.browser.WebBrowser;
import org.etlforge.application.ui.browser.WebBrowserFactory;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.widget.text.KongaEditorPane;

public class HtmlDisplayer extends KongaEditorPane implements HyperlinkListener {
   private static WebBrowserFactory browserFactory;
   private WebBrowser browser;

   public HtmlDisplayer() {
      this.setEditable(false);
      this.setContentType("text/html");
      this.addHyperlinkListener(this);
   }

   @Override
   public void hyperlinkUpdate(HyperlinkEvent var1) {
      if (var1.getEventType() == EventType.ACTIVATED) {
         WebBrowser var2 = this.getBrowser();
         if (var2 != null) {
            var2.open(var1.getURL().toString());
         }
      }
   }

   private WebBrowser getBrowser() {
      if (this.browser == null) {
         WebBrowserFactory var1 = this.getBrowserFactory();
         if (var1 != null) {
            this.browser = var1.createBrowser();
         }
      }

      return this.browser;
   }

   private WebBrowserFactory getBrowserFactory() {
      if (browserFactory == null) {
         try {
            browserFactory = WebBrowserFactory.newInstance();
         } catch (UnsupportedPlatformException var3) {
            String var2 = ApplicationResources.STRINGS.getString("TextComponentFactory.WebBrowser.NotSupported");
            ErrorLog.attention(HtmlDisplayer.class, var2, var3);
         }
      }

      return browserFactory;
   }
}
