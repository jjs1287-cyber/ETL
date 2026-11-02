package org.etlforge.application.ui.browser;

import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.UnsupportedPlatformException;

public abstract class WebBrowserFactory {
   public static boolean isPlatformSupported() {
      return DesktopWebBrowser.isSupported() || SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_LINUX;
   }

   public abstract WebBrowser createBrowser();

   public static WebBrowserFactory newInstance() throws UnsupportedPlatformException {
      if (DesktopWebBrowser.isSupported()) {
         return new WebBrowserFactory() {
            @Override
            public WebBrowser createBrowser() {
               return new DesktopWebBrowser();
            }
         };
      } else if (SystemUtils.IS_OS_WINDOWS) {
         return new WebBrowserFactory() {
            @Override
            public WebBrowser createBrowser() {
               return new WindowsWebBrowser();
            }
         };
      } else if (SystemUtils.IS_OS_LINUX) {
         return new WebBrowserFactory() {
            @Override
            public WebBrowser createBrowser() {
               return new LinuxWebBrowser();
            }
         };
      } else {
         throw new UnsupportedPlatformException();
      }
   }
}
