package org.etlforge.application.ui.browser;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.etlforge.application.errorlog.ErrorLog;

public class DesktopWebBrowser implements WebBrowser {
   public static boolean isSupported() {
      return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE);
   }

   @Override
   public void open(WebPage var1) {
      this.open(var1.getUrl());
   }

   @Override
   public void open(String var1) {
      try {
         Desktop.getDesktop().browse(new URI(var1));
      } catch (IOException var3) {
         this.handleException(var1, var3);
      } catch (URISyntaxException var4) {
         this.handleException(var1, var4);
      }
   }

   private void handleException(String var1, Exception var2) {
      ErrorLog.attention(DesktopWebBrowser.class, "Failed to open a web browser pointing to the URL \"" + var1 + "\"", var2);
   }
}
