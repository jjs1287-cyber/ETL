package org.jitterbit.integration.client.ui.interchange.component.start;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D.Double;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.apache.commons.lang.StringUtils;
import org.jitterbit.application.UnsupportedPlatformException;
import org.jitterbit.application.config.ApplicationConfiguration;
import org.jitterbit.application.errorlog.ErrorLog;
import org.jitterbit.application.prefs.BooleanPreference;
import org.jitterbit.application.ui.browser.WebBrowser;
import org.jitterbit.application.ui.browser.WebBrowserFactory;
import org.jitterbit.application.ui.look.Colors;
import org.jitterbit.application.ui.look.StyleSheet;
import org.jitterbit.application.ui.look.TextStyles;
import org.jitterbit.application.ui.look.WizardStyleSheet;
import org.jitterbit.application.ui.resources.ApplicationResources;
import org.jitterbit.application.ui.widget.HtmlDisplayer;
import org.jitterbit.integration.client.ui.icons.ClientIcons;
import org.jitterbit.integration.uidelegate.entity.EntityIcons;
import org.jitterbit.system.callback.Receiver;
import org.jitterbit.ui.Empty;
import org.jitterbit.ui.UiCanvas;
import org.jitterbit.ui.UiProvider;
import org.jitterbit.ui.event.ApplicationMessageEvent;
import org.jitterbit.ui.layout.BorderLayoutBuilder;
import org.jitterbit.ui.layout.BoxBuilder;
import org.jitterbit.ui.layout.Grid;
import org.jitterbit.ui.layout.Horizontal;
import org.jitterbit.ui.layout.OneColumnPanel;
import org.jitterbit.ui.style.TextStyle;
import org.jitterbit.ui.util.UiUtils;
import org.jitterbit.ui.widget.button.ButtonUtils;
import org.jitterbit.ui.widget.button.KongaButton;
import org.jitterbit.ui.widget.button.KongaCheckBox;
import org.jitterbit.ui.widget.label.KongaLabel;
import org.jitterbit.ui.widget.panel.InvisiblePanel;

public final class GettingStartedUi implements UiProvider {
   private static final String JITTERBIT_BASICS_URL = ApplicationConfiguration.getString("GettingStarted.Url.Basics", null);
   private static final String JITTERBIT_TUTORIAL_URL = ApplicationConfiguration.getString("QuickStart.Url", null);
   private static final String JITTERBIT_SUPPORT_URL = ApplicationConfiguration.getString("GettingStarted.Url.Support", null);
   private final ActionListener newOperationAction;
   private final ActionListener startConnectAction;
   private final BooleanPreference doNotShowAgainPref;
   private WebBrowserFactory browserFactory;
   private JComponent ui;

   public GettingStartedUi(ActionListener var1, ActionListener var2, BooleanPreference var3) {
      this.newOperationAction = var1;
      this.startConnectAction = var2;
      this.doNotShowAgainPref = var3;
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.createButton("Create a New Integration", EntityIcons.loadIcon("Operation.64.png"), this.newOperationAction);
      JComponent var3 = this.createButton("Start the Connect\u2122 Wizard", ClientIcons.getIcon("JitterbitConnect.64"), this.startConnectAction);
      BoxBuilder var4 = BoxBuilder.horizontal();
      var4.addAllWithSpace(40, var2, var3);
      BorderLayoutBuilder var5 = new BorderLayoutBuilder(0, 24);
      var5.withBorder(Empty.border(24, 24, 0, 24));
      var5.north(var4).center(this.createInfoBox());
      UiUtils.setPanelBackgrounds(var5.container(), Colors.NEUTRAL_BACKGROUND);
      this.ui = InvisiblePanel.of(var5);
      var1.display(this.ui);
   }

   private JComponent createButton(String var1, Icon var2, ActionListener var3) {
      GettingStartedUi.BigButton var4 = new GettingStartedUi.BigButton(var1);
      var4.addActionListener(var3);
      return BoxBuilder.horizontal().addAllWithSpace(10, var2, var4).container();
   }

   private JComponent createInfoBox() {
      JComponent var1 = this.createGettingStartedHelpBlurb();
      JComponent var2 = this.createLinkArea();
      JComponent var3 = this.createDontForgetArea();
      OneColumnPanel var4 = new OneColumnPanel();
      var4.setWeightX(1.0);
      var4.addAll(var1, var2);
      BorderLayoutBuilder var5 = new BorderLayoutBuilder();
      var5.north(var4).south(var3);
      KongaCheckBox var6 = this.createDoNotShowAgainCheck();
      BoxBuilder var7 = BoxBuilder.horizontal();
      var7.glue().add(var6);
      JComponent var8 = this.createVideoIcon();
      BorderLayoutBuilder var9 = new BorderLayoutBuilder(0, 16);
      var9.north(var7).center(var8);
      return Horizontal.box().addAllWithSpace(20, var5, var9).withBorder(StyleSheet.getDefaultTitleBorder(null)).container();
   }

   private KongaCheckBox createDoNotShowAgainCheck() {
      boolean var1 = !this.doNotShowAgainPref.get();
      KongaCheckBox var2 = new KongaCheckBox("&Don't show this again.", var1);
      var2.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            GettingStartedUi.this.doNotShowAgainPref.set(!var1);
         }
      });
      return var2;
   }

   private JComponent createGettingStartedHelpBlurb() {
      TextStyle var1 = TextStyle.adjustSize(WizardStyleSheet.SECTION_STYLE, 2);
      KongaLabel var2 = var1.makeLabel("Getting Started Help");
      var2.setAntiAliasingEnabled(true);
      var2.setBorder(Empty.border(0, 0, 10, 0));
      TextStyle var3 = TextStyle.adjustSize(TextStyles.DefaultText, 1);
      KongaLabel var4 = var3.makeLabel(
         "<html>First time using Jitterbit? Need a<br>refresher? Use these tools to get<br>started with your first integration.</html>"
      );
      OneColumnPanel var5 = new OneColumnPanel();
      var5.setWeightX(1.0);
      var5.addAll(var2, var4);
      Icon var6 = ClientIcons.getIcon("Help.64");
      BorderLayoutBuilder var7 = new BorderLayoutBuilder(20, 0);
      var7.center(var5).east(var6).withBorder(Empty.border(0, 0, 20, 0));
      return InvisiblePanel.of(var7);
   }

   private JComponent createLinkArea() {
      return new OneColumnPanel()
         .withInsets(0, 0, 10, 0)
         .setWeightX(1.0)
         .addAll(
            this.createLinkButton("Jitterbit Basics - 5 Minute Video Guide", JITTERBIT_BASICS_URL),
            this.createLinkButton("Hands-on Tutorial", JITTERBIT_TUTORIAL_URL),
            this.createLinkButton("Jitterbit Support && Documentation", JITTERBIT_SUPPORT_URL)
         )
         .container();
   }

   private JComponent createLinkButton(String var1, final String var2) {
      if (StringUtils.isEmpty(var2)) {
         return null;
      }

      KongaButton var3 = new KongaButton();
      ButtonUtils.configureButtonForToolBar(var3);
      var3.setText(var1);
      GettingStartedUi.BigButton.STYLE.makeOver(var3);
      var3.setForeground(Colors.JITTERBIT_ORANGE);
      this.configureUrlDisplayer(var3, var2);
      var3.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            GettingStartedUi.this.openUrl(var2);
         }
      });
      var3.setCursor(Cursor.getPredefinedCursor(12));
      return InvisiblePanel.of(var3);
   }

   private void configureUrlDisplayer(JComponent var1, final String var2) {
      var1.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent var1) {
            ApplicationMessageEvent.postInformationMessage(GettingStartedUi.class, var2);
         }

         @Override
         public void mouseExited(MouseEvent var1) {
            ApplicationMessageEvent.postInformationMessage(GettingStartedUi.class, "", new ApplicationMessageEvent.LifeTime(-1L));
         }
      });
   }

   private void openUrl(String var1) {
      WebBrowserFactory var2 = this.getBrowserFactory();
      if (var2 != null) {
         WebBrowser var3 = var2.createBrowser();
         var3.open(var1);
      }
   }

   private WebBrowserFactory getBrowserFactory() {
      if (this.browserFactory == null) {
         try {
            this.browserFactory = WebBrowserFactory.newInstance();
         } catch (UnsupportedPlatformException var3) {
            String var2 = ApplicationResources.STRINGS.getString("TextComponentFactory.WebBrowser.NotSupported");
            ErrorLog.attention(HtmlDisplayer.class, var2, var3);
         }
      }

      return this.browserFactory;
   }

   private JComponent createDontForgetArea() {
      TextStyle var1 = TextStyle.adjustSize(TextStyle.newColor(TextStyles.DefaultBoldLarger, Colors.NEUTRAL_BACKGROUND), 1);
      KongaLabel var2 = var1.makeLabel("Don't forget! If you're a Jitterbit customer");
      KongaLabel var3 = var1.makeLabel("you can reach us by email or phone per your");
      KongaLabel var4 = var1.makeLabel("support contract.");
      Grid var5 = Grid.oneColumn(5);
      var5.addAll(var2, var3, var4);
      var5.withBackground(Colors.JITTERBIT_BLUE).withBorder(Empty.border(8, 16, 8, 16));
      return InvisiblePanel.of(var5.container());
   }

   private JComponent createVideoIcon() {
      Icon var1 = ClientIcons.getIcon("GettingStarted.Vid");
      KongaButton var2 = new KongaButton(var1);
      var2.setContentAreaFilled(false);
      var2.setBorder(null);
      var2.setCursor(Cursor.getPredefinedCursor(12));
      var2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            GettingStartedUi.this.openUrl(GettingStartedUi.JITTERBIT_BASICS_URL);
         }
      });
      this.configureUrlDisplayer(var2, JITTERBIT_BASICS_URL);
      return var2;
   }

   @Override
   public void requestFocus() {
      if (this.ui != null) {
         this.ui.requestFocusInWindow();
      }
   }

   private static class BigButton extends KongaButton {
      public static final TextStyle STYLE = TextStyle.adjustSize(WizardStyleSheet.SECTION_STYLE, -4);

      public BigButton(String var1) {
         super(var1);
         STYLE.makeOver(this);
         this.setForeground(Color.WHITE);
         this.setContentAreaFilled(false);
         this.setFocusPainted(false);
         this.setBorder(Empty.border(8, 8, 8, 16));
         this.setMargin(new Insets(8, 8, 8, 16));
         this.setCursor(Cursor.getPredefinedCursor(12));
      }

      @Override
      public void paintComponent(Graphics var1) {
         Graphics2D var2 = (Graphics2D)var1;
         Color var3 = var2.getColor();
         var2.setColor(Colors.JITTERBIT_ORANGE);
         RenderingHints var4 = var2.getRenderingHints();
         UiUtils.makeAntiAliased(var2);
         Double var5 = new Double(0.0, 0.0, this.getWidth() - 1, this.getHeight() - 1, 24.0, 24.0);
         var2.fill(var5);
         var2.setColor(var3);
         super.paintComponent(var2);
         if (this.getModel().isRollover() || this.getModel().isPressed()) {
            Stroke var6 = var2.getStroke();
            var2.setStroke(new BasicStroke(2.0F));
            Color var7 = this.getModel().isPressed() ? Color.DARK_GRAY : Color.LIGHT_GRAY;
            var2.setColor(var7);
            UiUtils.makeAntiAliased(var2);
            var2.draw(var5);
            var2.setStroke(new BasicStroke(1.0F));
            var2.setColor(var7.darker());
            var2.draw(var5);
            var2.setStroke(var6);
            var2.setColor(var3);
         }

         var2.setRenderingHints(var4);
      }
   }
}
