package org.etlforge.integration.client.ui.util;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.browser.DefaultWebPage;
import org.etlforge.application.ui.browser.WebPage;

public abstract class ETLForgeWebPageFactory {
   private static final String HOMEPAGE_URL = ApplicationConfiguration.getString("Company.HomePage.Url", null);
   private static final String FORUM_URL = ApplicationConfiguration.getString("Forum.Url", null);
   private static final String QUICK_START_URL = ApplicationConfiguration.getString("QuickStart.Url", null);

   public abstract WebPage getHomePage();

   public abstract WebPage getForumsPage();

   public abstract WebPage getTutorialPage();

   public static ETLForgeWebPageFactory newInstance() {
      return new ETLForgeWebPageFactory() {
         @Override
         public WebPage getForumsPage() {
            return ETLForgeWebPageFactory.FORUM_URL == null ? null : new DefaultWebPage(ETLForgeWebPageFactory.FORUM_URL);
         }

         @Override
         public WebPage getHomePage() {
            return ETLForgeWebPageFactory.HOMEPAGE_URL == null ? null : new DefaultWebPage(ETLForgeWebPageFactory.HOMEPAGE_URL);
         }

         @Override
         public WebPage getTutorialPage() {
            return ETLForgeWebPageFactory.QUICK_START_URL == null ? null : new DefaultWebPage(ETLForgeWebPageFactory.QUICK_START_URL);
         }
      };
   }
}
