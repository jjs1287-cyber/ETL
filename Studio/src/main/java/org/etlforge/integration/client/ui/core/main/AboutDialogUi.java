package org.etlforge.integration.client.ui.core.main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.UnsupportedPlatformException;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.browser.WebBrowserFactory;
import org.etlforge.application.ui.browser.WebPage;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.core.window.VersionBanner;
import org.etlforge.integration.client.ui.util.ETLForgeWebPageFactory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.event.DoubleClick;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.util.version.Version;

public final class AboutDialogUi implements UiProvider {
   private static final ETLForgeWebPageFactory WEBPAGE_FACTORY = ETLForgeWebPageFactory.newInstance();
   private static final WebPage HOME_PAGE = WEBPAGE_FACTORY.getHomePage();
   private static final WebPage FORUMS_PAGE = WEBPAGE_FACTORY.getForumsPage();
   private static WebBrowserFactory BROWSER_FACTORY;
   private final JComponent ui;
   private final VersionBanner versionBanner = this.createVersionBanner();

   public AboutDialogUi(JComponent var1) {
      this.ui = this.createLayout(var1);
      this.installKeyBindings(this.ui);
   }

   private VersionBanner createVersionBanner() {
      Version var1 = ClientInfo.getCurrentVersion();
      if (var1 == Version.UNKNOWN) {
         return null;
      }

      try {
         String var2 = "org.etlforge.integration.client.ui.core.main";
         String var3 = String.format("%s.Version_%d_%d", var2, var1.getMajorVersion(), var1.getMinorVersion());
         Class var4 = Class.forName(var3);
         if (VersionBanner.class.isAssignableFrom(var4)) {
            Object var5 = var4.newInstance();
            return VersionBanner.class.cast(var5);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return null;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
   }

   private JComponent createLayout(JComponent var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(var1).south(this.layoutInformation());
      return var2.container();
   }

   private OneColumnPanel layoutInformation() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setWeightX(1.0);
      var1.withInsets(10, 5, 5, 0);
      this.addCaption(var1);
      this.addCopyright(var1);
      this.addVersion(var1);
      this.addBuildTime(var1);
      this.addSessionDetailsButton(var1);
      this.layoutLinks(var1);
      return var1;
   }

   private void addCaption(OneColumnPanel var1) {
      KongaLabel var2 = TextStyles.BigAdminHintBold.makeLabel(ApplicationConfiguration.getProductName());
      var1.add(var2);
   }

   private void addCopyright(OneColumnPanel var1) {
      JLabel var2 = new JLabel(ApplicationConfiguration.getString("Product.Copyright", ""));
      var1.add(var2);
   }

   private void addBuildTime(OneColumnPanel var1) {
      DateFormat var2 = DateFormat.getDateTimeInstance(2, 3);
      String var3 = var2.format(ClientInfo.getBuildTime());
      this.addCaptionValuePair(PackageResources.AboutDialog.BUILD_TIMESTAMP, var3, var1);
   }

   private void addSessionDetailsButton(OneColumnPanel var1) {
      KongaButton var2 = new KongaButton(PackageResources.AboutDialog.SESSION_DETAILS);
      var2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            AboutDialogUi.this.showSessionDetails();
         }
      });
      var1.noFill(var2);
   }

   private void showSessionDetails() {
      SessionDetailsUi var1 = new SessionDetailsUi();
      UiProviderDialogDisplayer.display(UiUtils.getActiveFrame(), var1, PackageResources.AboutDialog.SESSION_DETAILS_TITLE);
   }

   private void addVersion(OneColumnPanel var1) {
      Version var2 = ClientInfo.getCurrentVersion();
      JLabel var3 = this.addCaptionValuePair(PackageResources.AboutDialog.VERSION, var2.toString(), var1);
      if (this.versionBanner != null) {
         DoubleClick.on(var3).toRun(new Runnable() {
            @Override
            public void run() {
               AboutDialogUi.this.showVersionBanner();
            }
         });
      }
   }

   private JLabel addCaptionValuePair(String var1, String var2, OneColumnPanel var3) {
      KongaLabel var4 = TextStyles.DefaultBold.makeLabel(var2);
      BoxBuilder var5 = BoxBuilder.horizontal();
      var5.addAllWithSpace(5, var1, var4);
      var3.add(var5);
      return var4;
   }

   private void showVersionBanner() {
      Window var1 = SwingUtilities.getWindowAncestor(this.ui);
      VersionBannerDialog var2 = new VersionBannerDialog(var1, this.versionBanner);
      var2.show();
   }

   private void layoutLinks(OneColumnPanel var1) {
      TitledBorder var2 = this.getBorder();
      TwoColumnPanel var3 = new TwoColumnPanel()
         .withLeftInsets(0, 10, 10, 5)
         .withRightFill(TwoColumnPanel.HORIZONTAL)
         .withRightWeights(1.0, 0.0)
         .withAnchor(TwoColumnPanel.SOUTHWEST)
         .withBorder(new BorderBuilder(var2).empty(5, 0, 0, 10));
      this.addHomePageLink(var3);
      this.addForumLink(var3);
      var1.withInsets(15, 0, 0, 0);
      var1.add(var3);
   }

   private TitledBorder getBorder() {
      TitledBorder var1 = BorderFactory.createTitledBorder(PackageResources.AboutDialog.ON_THE_WEB);
      var1.setTitleColor(TextStyles.AdminHint.getColor());
      return var1;
   }

   private void addHomePageLink(TwoColumnPanel var1) {
      if (HOME_PAGE != null) {
         String var2 = HOME_PAGE.getUrl();
         String var3 = var2;
         String var4 = "http://";
         if (var3.toLowerCase().startsWith(var4)) {
            var3 = StringUtils.substringAfter(var3, var4);
         }

         this.addUrlRow(var1, PackageResources.AboutDialog.HOMEPAGE, var3, var2);
      }
   }

   private void addForumLink(TwoColumnPanel var1) {
      if (FORUMS_PAGE != null) {
         this.addUrlRow(var1, PackageResources.AboutDialog.FORUM, "Visit our forum", FORUMS_PAGE.getUrl());
      }
   }

   private void addUrlRow(TwoColumnPanel var1, String var2, String var3, String var4) {
      JLabel var5 = new JLabel(var2);
      JLabel var6 = this.createLinkLabel(var3, var4);
      var1.addRow(var5, var6);
   }

   private JLabel createLinkLabel(String var1, final String var2) {
      JLabel var3 = new JLabel("<html><u>" + var1 + "</u>");
      var3.setForeground(Color.BLUE);
      if (BROWSER_FACTORY != null) {
         var3.setCursor(Cursor.getPredefinedCursor(12));
         var3.addMouseListener(new MouseActionListener() {
            @Override
            protected void onSingleClick(MouseEvent var1) {
               AboutDialogUi.BROWSER_FACTORY.createBrowser().open(var2);
            }
         });
      }

      return var3;
   }

   private void installKeyBindings(JComponent var1) {
      KeyBindingInstaller var2 = new KeyBindingInstaller(var1, 2);
      var2.addBinding(KeyStroke.getKeyStroke('a'), "author", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            MainAuthorDialog.display();
         }
      });
      if (this.versionBanner != null) {
         var2.addBinding(KeyStroke.getKeyStroke('v'), "version", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               AboutDialogUi.this.showVersionBanner();
            }
         });
      }

      var2.install();
   }

   static {
      try {
         BROWSER_FACTORY = WebBrowserFactory.newInstance();
      } catch (UnsupportedPlatformException var1) {
         var1.printStackTrace();
      }
   }
}
