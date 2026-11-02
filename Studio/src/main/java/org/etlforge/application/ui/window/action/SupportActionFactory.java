package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.UnsupportedPlatformException;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.browser.WebBrowser;
import org.etlforge.application.ui.browser.WebBrowserFactory;
import org.etlforge.application.ui.resources.ApplicationResources;

public final class SupportActionFactory {
   public static boolean isEnabled() {
      if (!WebBrowserFactory.isPlatformSupported()) {
         return false;
      }

      String var0 = ApplicationConfiguration.getSupportUrl();
      return var0 != null && !var0.isEmpty();
   }

   public static Action newAction() {
      return new SupportActionFactory.ActionImpl();
   }

   private SupportActionFactory() {
   }

   private static final class ActionImpl extends AbstractAction {
      public ActionImpl() {
         super(getName());
         this.putValue("ShortDescription", getToolTip());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ActionImpl.this.openBrowser();
            }
         });
      }

      private void openBrowser() {
         try {
            WebBrowser var1 = WebBrowserFactory.newInstance().createBrowser();
            var1.open(ApplicationConfiguration.getSupportUrl());
         } catch (UnsupportedPlatformException var2) {
            var2.printStackTrace();
         }
      }

      private static String getName() {
         String var0 = ApplicationResources.STRINGS.getString("Support.Action.Name");
         return MessageFormat.format(var0, ApplicationConfiguration.getCompanyName());
      }

      private static String getToolTip() {
         String var0 = ApplicationResources.STRINGS.getString("Support.Action.ToolTip");
         return MessageFormat.format(var0, ApplicationConfiguration.getCompanyName());
      }
   }
}
