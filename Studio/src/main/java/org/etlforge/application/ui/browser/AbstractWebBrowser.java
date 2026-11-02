package org.etlforge.application.ui.browser;

import java.io.IOException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.resources.ApplicationResources;

abstract class AbstractWebBrowser implements WebBrowser {
   @Override
   public void open(WebPage var1) {
      this.open(var1.getUrl());
   }

   @Override
   public void open(String var1) {
      String[] var2 = this.getExecArguments(var1);

      try {
         Runtime.getRuntime().exec(var2);
      } catch (IOException var5) {
         String var4 = ApplicationResources.STRINGS.getString("Browser.LaunchError");
         ErrorLog.attention(this.getClass(), var4, var5);
      }
   }

   protected abstract String[] getExecArguments(String var1);
}
