package org.etlforge.application.ui.window.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D.Double;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.application.ui.window.editor.service.ComponentNotification;
import org.etlforge.ui.Empty;
import org.etlforge.ui.GraphicsState;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.GlassSheetPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.util.string.StringLimiter;

public final class EditorThumbnailFactory {
   private final int imageWidthInPixels;
   private final int maxTitleLength;

   public EditorThumbnailFactory(int var1, int var2) {
      this.imageWidthInPixels = var1;
      this.maxTitleLength = var2;
   }

   public EditorThumbnail newThumbnail(Editor var1) {
      return new EditorThumbnailFactory.ThumbnailImpl(var1, this.imageWidthInPixels, this.maxTitleLength);
   }

   private static class PanelImpl extends GlassSheetPanel.RoundedRect {
      private static final Color ACTIVE_COLOR = getActiveColor();
      private final Color inactiveBackground;
      private final Color inactiveBorderColor;

      private static Color getActiveColor() {
         Color var0 = Colors.ETLFORGE_ORANGE;
         return new Color(var0.getRed(), var0.getGreen(), var0.getBlue(), 150);
      }

      public PanelImpl(Color var1) {
         super(ACTIVE_COLOR, 4, 10);
         this.inactiveBackground = var1;
         this.inactiveBorderColor = new Color(64, 64, 64);
      }

      public void setActive(boolean var1) {
         if (AnimationPreference.get()) {
            this.animateGlasSheetVisible(var1);
         } else {
            this.setGlassSheetVisible(var1);
         }
      }

      @Override
      public Point getToolTipLocation(MouseEvent var1) {
         JToolTip var2 = this.createToolTip();
         var2.setTipText(this.getToolTipText());
         Dimension var3 = var2.getPreferredSize();
         if (var3 != null && var3.width != 0 && var3.height != 0) {
            int var4 = (this.getWidth() - var3.width) / 2;
            int var5 = -var3.height - 2;
            return new Point(var4, var5);
         } else {
            return new Point(1, -24);
         }
      }

      @Override
      protected int getAlphaOfBorder() {
         return Math.min(2 * this.getCurrentAlpha(), 255);
      }

      @Override
      protected void paintComponent(Graphics var1) {
         Graphics2D var2 = (Graphics2D)var1;
         GraphicsState var3 = new GraphicsState(var2, GraphicsState.COLOR, GraphicsState.RENDERING_HINTS);
         var2.setColor(this.inactiveBackground);
         Double var4 = new Double(0.0, 0.0, this.getWidth() - 1, this.getHeight() - 1, 8.0, 8.0);
         var2.fill(var4);
         var2.setColor(this.inactiveBorderColor);
         UiUtils.makeAntiAliased(var2);
         var2.draw(var4);
         var3.restore();
      }
   }

   private static class ThumbnailImpl implements EditorThumbnail {
      private static final Color BACKGROUND = new Color(181, 199, 206);
      private static final TextStyle TITLE_STYLE = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 12, 1, new Color(32, 32, 32));
      private final Editor editor;
      private final EditorThumbnailFactory.PanelImpl ui;
      private final JLabel titleLabel;
      private final CardPanel closeButtonCapsule;
      private ComponentNotification notification;

      public ThumbnailImpl(Editor var1, int var2, int var3) {
         this.editor = var1;
         this.ui = new EditorThumbnailFactory.PanelImpl(BACKGROUND);
         this.titleLabel = this.createTitleLabel(var1, var3);
         this.closeButtonCapsule = this.createCloseButtonCapsule();
         this.layoutComponents(var1, var2);
         if (this.needsToolTip()) {
            this.ui.setToolTipText(var1.getTitle());
         }

         this.setActive(false);
      }

      private JLabel createTitleLabel(Editor var1, int var2) {
         String var3 = getThumbnailTitle(var1, var2);
         JLabel var4 = new JLabel(var3);
         TITLE_STYLE.makeOver(var4);
         var4.setIcon(var1.getSmallIcon());
         return var4;
      }

      private static String getThumbnailTitle(Editor var0, int var1) {
         String var2 = var0.getTitle();
         if (var2 == null) {
            EditorCategory var3 = var0.getCategory();
            if (var3 != null) {
               var2 = var3.name;
            }
         }

         if (var2 == null) {
            var2 = "";
         }

         StringLimiter var4 = new StringLimiter(var2);
         return var4.getLimitedString(var1);
      }

      private CardPanel createCloseButtonCapsule() {
         JButton var1 = this.createCloseButton();
         CardPanel var2 = new CardPanel();
         var2.add(var1, "show");
         Icon var3 = Empty.icon(var1.getPreferredSize());
         JLabel var4 = new JLabel(var3);
         var2.add(var4, "hide");
         return var2;
      }

      private JButton createCloseButton() {
         CloseEditorAction var1 = new CloseEditorAction(this.editor);
         KongaButton var2 = KongaButton.forToolbar(var1);
         var2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent var1) {
               ThumbnailImpl.this.ui.repaint();
            }

            @Override
            public void mouseExited(MouseEvent var1) {
               ThumbnailImpl.this.ui.repaint();
            }
         });
         return var2;
      }

      private void layoutComponents(Editor var1, int var2) {
         BorderLayoutBuilder var3 = BorderLayoutBuilder.from(this.ui, 0, 5);
         var3.withBorder(Empty.border(6));
         JComponent var4 = this.layoutTitlePanel();
         JLabel var5 = this.createImageLabel(var1, var2);
         var3.north(var4).center(var5);
      }

      private JComponent layoutTitlePanel() {
         TwoColumnPanel var1 = new TwoColumnPanel();
         var1.withLeftWeights(1.0, 0.0)
            .withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL)
            .withLeftAnchor(GridBagLayoutPanel.Anchor.WEST)
            .withLeftInsets(0, 0, 0, 10);
         var1.withRightWeights(0.0, 0.0)
            .withRightFill(GridBagLayoutPanel.Fill.NONE)
            .withRightAnchor(GridBagLayoutPanel.Anchor.EAST)
            .withRightInsets(0, 0, 0, 0);
         var1.addRow(this.titleLabel, this.closeButtonCapsule.container());
         UiUtils.setPanelBackgrounds(var1.container(), BACKGROUND);
         return var1.container();
      }

      private JLabel createImageLabel(Editor var1, int var2) {
         Image var3 = var1.render(new Dimension(var2, 0));
         return var3 != null ? new JLabel(new ImageIcon(var3)) : null;
      }

      private boolean needsToolTip() {
         return !this.titleLabel.getText().equals(this.editor.getTitle());
      }

      @Override
      public Editor getEditor() {
         return this.editor;
      }

      @Override
      public JComponent getUi() {
         return this.ui;
      }

      @Override
      public void setActive(boolean var1) {
         this.ui.setActive(var1);
         this.closeButtonCapsule.show(var1 ? "show" : "hide");
         if (var1 && this.notification != null) {
            this.stopNotification();
         }
      }

      private void stopNotification() {
         this.notification.stop();
         this.notification = null;
      }

      @Override
      public void addNotification() {
         if (this.notification == null) {
            this.notification = new ComponentNotification(this.titleLabel);
            this.notification.start();
         }
      }

      @Override
      public void dispose() {
         if (this.notification != null) {
            this.stopNotification();
         }
      }

      @Override
      public String toString() {
         return this.editor.getTitle();
      }
   }
}
