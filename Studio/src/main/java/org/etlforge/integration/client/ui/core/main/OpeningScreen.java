package org.etlforge.integration.client.ui.core.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.text.NumberFormat;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import org.etlforge.application.Application;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.view.ViewLauncher;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.system.callback.Acceptor;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.ComponentTraverser;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.overlay.OverlayPositioner;

public final class OpeningScreen {
   private static final int RIGHT_MARGIN = 40;
   private static final Icon LOGO = ClientIcons.getIcon("ETLForge.StartScreen");
   private RootPaneContainer owner;
   private final ViewLauncher designLauncher;
   private final ViewLauncher adminLauncher;
   private final JComponent center;
   private final KongaLayeredPane layeredPane;
   private boolean showing;
   private final JComponent controlPanel;
   private final JComponent contentPanel;

   public OpeningScreen(ViewLauncher var1, ViewLauncher var2) {
      this.designLauncher = var1;
      this.adminLauncher = var2;
      this.contentPanel = new JPanel(new BorderLayout());
      this.controlPanel = this.createControlPanel();
      this.center = this.layoutMainPanel();
      this.layeredPane = new KongaLayeredPane(this.center);
      this.layoutLogo();
   }

   private JComponent createControlPanel() {
      byte var1 = 32;
      int var2 = LOGO.getIconWidth();
      return new BorderLayoutBuilder()
         .withBackground(Colors.NEUTRAL_BACKGROUND)
         .withBorder(Empty.border(0, 0, 0, var1 + var2 + 40))
         .north(new GettingStartedCaption())
         .container();
   }

   private JComponent layoutMainPanel() {
      return Vertical.borderLayout()
         .of(this.controlPanel, this.contentPanel, this.layoutAdminLauncher())
         .withBackground(Colors.FIELD_BACKGROUND)
         .withBorder(Empty.border(0, 30, 20, 3))
         .container();
   }

   private JComponent layoutAdminLauncher() {
      LaunchAdminButton var1 = new LaunchAdminButton(this.designLauncher, this.adminLauncher);
      return var1.getUi();
   }

   private void layoutLogo() {
      JLabel var1 = new JLabel("<html><b>ETLForge</b><br><span style='font-size:10px'>WINDOWS x64</span></html>");
      var1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 28));
      var1.setForeground(Colors.TEXT_FOREGROUND);
      var1.setPreferredSize(new Dimension(LOGO.getIconWidth(), LOGO.getIconHeight()));
      var1.putClientProperty("konga.overlay.fixedPosition", Boolean.TRUE);
      this.layeredPane.addOverlay(var1, new OverlayPositioner() {
         @Override
         public Point getPosition(JComponent var1, JComponent var2) {
            byte var3 = 16;
            int var4 = OpeningScreen.this.layeredPane.getWidth() - OpeningScreen.LOGO.getIconWidth() - 40;
            return new Point(var4, var3);
         }
      });
   }

   public void publish(RootPaneContainer var1) {
      this.owner = var1;
      Container var2 = var1.getContentPane();
      this.removeOriginalComponentsFromContentPane(var2);
      var2.add(this.layeredPane, "Center");
      nudge(this.layeredPane);
      this.designLauncher.activate(new OpeningScreen.LauncherCanvas());
      var1.getRootPane().validate();
      var1.getRootPane().repaint();
      this.showing = true;
      this.logPublishTime();
   }

   private void removeOriginalComponentsFromContentPane(Container var1) {
      String[] var2 = new String[]{"North", "West", "Center", "East"};
      BorderLayout var3 = (BorderLayout)var1.getLayout();

      for (String var7 : var2) {
         Component var8 = var3.getLayoutComponent(var7);
         if (var8 != null) {
            var1.remove(var8);
         }
      }

      Component var9 = var3.getLayoutComponent("South");
      if (var9 != null) {
         var9.setVisible(false);
      }
   }

   private static void nudge(Component var0) {
      Dimension var1 = var0.getSize();
      var1.width++;
      var0.setSize(var1);
      var1.width--;
      var0.setSize(var1);
   }

   public void remove() {
      this.removeUiComponents();
      this.showing = false;
   }

   private void removeUiComponents() {
      if (this.owner != null) {
         this.contentPanel.removeAll();
         Container var1 = this.owner.getContentPane();
         if (this.layeredPane.getParent() == var1) {
            var1.remove(this.layeredPane);
            this.layeredPane.remove(this.center);
            this.makeNotFocusable(this.center);
         }

         this.showStatusPanelAgain(var1);
         this.owner.getRootPane().validate();
         this.owner.getRootPane().repaint();
      }
   }

   private void showStatusPanelAgain(Container var1) {
      BorderLayout var2 = (BorderLayout)var1.getLayout();
      Component var3 = var2.getLayoutComponent("South");
      if (var3 != null) {
         var3.setVisible(true);
      }
   }

   private void makeNotFocusable(Container var1) {
      ComponentTraverser.traverse(var1, new Acceptor<Component>() {
         public boolean accept(Component var1) {
            var1.setFocusable(false);
            return true;
         }
      });
   }

   public boolean isShowing() {
      return this.showing;
   }

   private void logPublishTime() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            long var1 = System.currentTimeMillis() - Application.getStartupTime().getTime();
            ApplicationLog.getMainLogger().info("Opening screen displayed: " + NumberFormat.getIntegerInstance().format(var1) + " ms after startup.");
         }
      });
   }

   private class LauncherCanvas implements UiCanvas {
      private LauncherCanvas() {
      }

      @Override
      public void display(Component var1) {
         boolean var2 = OpeningScreen.this.contentPanel.getComponentCount() > 0;
         OpeningScreen.this.contentPanel.removeAll();
         OpeningScreen.this.contentPanel.add(var1, "Center");
         OpeningScreen.this.contentPanel.revalidate();
         if (var2) {
            OpeningScreen.this.layoutMainPanel();
            OpeningScreen.this.owner.getRootPane().revalidate();
            OpeningScreen.this.owner.getRootPane().repaint();
         } else {
            OpeningScreen.this.contentPanel.repaint();
         }
      }
   }
}

