package org.etlforge.application.ui.browser;

class WindowsWebBrowser extends AbstractWebBrowser {
   @Override
   protected String[] getExecArguments(String var1) {
      return new String[]{"rundll32", "url.dll", "FileProtocolHandler", var1};
   }
}
