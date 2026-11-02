package org.etlforge.application.ui.browser;

public class DefaultWebPage implements WebPage {
   private final String url;

   public DefaultWebPage(String var1) {
      if (var1 != null && var1.trim().length() != 0) {
         this.url = var1.trim();
      } else {
         throw new IllegalArgumentException("url cannot be null or empty.");
      }
   }

   @Override
   public String getUrl() {
      return this.url;
   }
}
