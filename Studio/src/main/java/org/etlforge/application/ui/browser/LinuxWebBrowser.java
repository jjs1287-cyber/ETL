package org.etlforge.application.ui.browser;

class LinuxWebBrowser extends AbstractWebBrowser {
   @Override
   protected String[] getExecArguments(String var1) {
      return new String[]{"sh", "-c", getBrowserArgument(var1)};
   }

   private static String getBrowserArgument(String var0) {
      StringBuilder var1 = new StringBuilder("netscape ");
      var1.append(var0);
      var1.append(" || mozilla ").append(var0);
      var1.append(" || opera url");
      return var1.toString();
   }
}
