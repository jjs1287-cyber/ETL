package org.etlforge.integration.client.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.icons.ClientLogo;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.version.Version;

public class SplashScreenUi implements UiProvider {
   private static final Color BLUE = new Color(86, 126, 185);
   private static final int ICON_TEXT_RIGHT_EDGE = 458;
   private static final int ICON_TEXT_LOWER_EDGE = 163;
   private boolean displayVersion = true;
   private boolean useBorder = true;
   private Icon icon = ClientLogo.LOGO;
   private final Point magicalPoint = new Point(261, 120);
   private final JComponent ui = InvisiblePanel.newPanel();

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.add(this.createContents());
      var1.display(this.ui);
   }

   public void setUseBorder(boolean var1) {
      this.useBorder = var1;
   }

   public void setDisplayVersion(boolean var1) {
      this.displayVersion = var1;
   }

   public void setUseReflectedLogo(boolean var1) {
      if (var1) {
         this.icon = this.createReflectedLogo();
      } else {
         this.icon = ClientLogo.LOGO;
      }
   }

   @Override
   public void requestFocus() {
   }

   private JComponent createContents() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder().center(this.createCenterDisplay());
      var1.withBackground(javax.swing.UIManager.getColor("Panel.background"));
      if (this.useBorder) {
         var1.withBorder(this.getNoNonsenseBorder());
      }

      return var1.container();
   }

   private Border getNoNonsenseBorder() {
      return new LineBorder(Color.BLACK, 3);
   }

   private JComponent createCenterDisplay() {
      JLayeredPane var1 = new JLayeredPane();
      var1.setLayout(null);
      JLabel var2 = this.createBoundedIconLabel();
      var1.add(var2, JLayeredPane.DEFAULT_LAYER);
      if (this.displayVersion) {
         JLabel var3 = this.createBoundedVersionNumberLabel();
         var1.add(var3, JLayeredPane.PALETTE_LAYER);
      }

      Dimension var4 = var2.getSize();
      var4.width += 20;
      var4.height += 10;
      var1.setPreferredSize(var4);
      return var1;
   }

   public void installMagic() {
      SplashScreenUi.MagicPointHandler var1 = new SplashScreenUi.MagicPointHandler();
      this.ui.addMouseMotionListener(var1);
      this.ui.addMouseListener(var1);
   }

   private JLabel createBoundedIconLabel() {
      JLabel var1 = new JLabel(this.icon);
      var1.setBounds(0, 0, this.icon.getIconWidth(), this.icon.getIconHeight());
      return var1;
   }

   private JLabel createBoundedVersionNumberLabel() {
      JLabel var1 = this.createVersionNumberLabel();
      Dimension var2 = var1.getPreferredSize();
      int var3 = var2.width;
      int var4 = var2.height + 10;
      var1.setBounds(458 - var3, 163, var3, var4);
      return var1;
   }

   private JLabel createVersionNumberLabel() {
      Version var1 = ClientInfo.getCurrentVersion();
      String var2 = var1 != null ? var1.toString() : "[not available]";
      JLabel var3 = new JLabel(var2, 4);
      var3.setOpaque(true);
      var3.setBackground(javax.swing.UIManager.getColor("Panel.background"));
      var3.setForeground(BLUE);
      Font var4 = Fonts.bold(var3);
      var3.setFont(var4);
      return var3;
   }

   private Icon createReflectedLogo() {
      BufferedImage var1 = this.getOriginalLogoImage();
      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      BufferedImage var4 = new BufferedImage(var2, 3 * var3 / 2, 2);
      Graphics2D var5 = var4.createGraphics();
      var5.drawImage(var1, 0, 0, null);
      var5.scale(1.0, -1.0);
      var5.drawImage(var1, 0, -2 * var3, null);
      var5.scale(1.0, -1.0);
      var5.translate(0, var3);
      GradientPaint var6 = new GradientPaint(0.0F, 0.0F, new Color(1.0F, 1.0F, 1.0F, 0.5F), 0.0F, var3 / 2.0F, new Color(1.0F, 1.0F, 1.0F, 0.0F));
      var5.setPaint(var6);
      var5.setComposite(AlphaComposite.DstIn);
      var5.fillRect(0, 0, var2, var3);
      var5.dispose();
      return new ImageIcon(var4, null);
   }

   private BufferedImage getOriginalLogoImage() {
      BufferedImage var1 = new BufferedImage(ClientLogo.LOGO.getIconWidth(), ClientLogo.LOGO.getIconHeight(), 1);
      Graphics2D var2 = var1.createGraphics();
      ClientLogo.LOGO.paintIcon(null, var2, 0, 0);
      var2.dispose();
      return var1;
   }

   private class MagicPointHandler extends MouseAdapter {
      private MagicPointHandler() {
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         if (this.isHotSpot(var1)) {
            this.startMagic();
         }
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
         Cursor var2;
         if (this.isHotSpot(var1)) {
            var2 = Cursor.getPredefinedCursor(1);
         } else {
            var2 = Cursor.getDefaultCursor();
         }

         var1.getComponent().setCursor(var2);
      }

      private boolean isHotSpot(MouseEvent var1) {
         return var1.getPoint().distance(SplashScreenUi.this.magicalPoint) < 5.0;
      }

      private void startMagic() {
         try {
            Window var1 = SwingUtilities.getWindowAncestor(SplashScreenUi.this.ui);
            if (var1 instanceof JDialog) {
               var1.dispose();
            }

            Class var2 = Class.forName("org.etlforge.integration.internal.ui.util.MusicalOrbits");
            Method var3 = var2.getMethod("openInDialog");
            var3.invoke(null);
         } catch (Exception var4) {
         }
      }
   }
}

