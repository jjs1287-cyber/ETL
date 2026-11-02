package org.etlforge.ui.graph2d.node;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Stroke;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.layout.BoxBuilder;

public abstract class AbstractIconLabelNodeUi2D extends AbstractGraphNodeUi2D {
   protected static final String TOOLTIP_WHEN_EMPTY = "Right-click or use drag & drop to populate this place holder";
   protected static final Stroke BORDER_STYLE_WHEN_EMPTY = new BasicStroke(1.0F, 0, 0, 1.0F, new float[]{3.0F, 3.0F}, 0.0F);
   private final Object node;
   private final JLabel iconLabel;
   private final JLabel contentLabel;
   private final AbstractIconLabelNodeUi2D.MoveAppearanceHandler moveAppearanceHandler;

   public AbstractIconLabelNodeUi2D(Graph2D var1, Object var2) {
      super(var1);
      this.node = var2;
      this.iconLabel = this.createIconLabel();
      this.contentLabel = this.createContentLabel();
      this.moveAppearanceHandler = new AbstractIconLabelNodeUi2D.MoveAppearanceHandler();
      BoxBuilder.vertical(this).of(this.iconLabel, this.contentLabel);
      this.setSize(this.getPreferredSize());
      this.setToolTipText("Right-click or use drag & drop to populate this place holder");
   }

   private JLabel createContentLabel() {
      JLabel var1 = new JLabel(this.getContentString());
      var1.setHorizontalAlignment(0);
      var1.setAlignmentX(0.5F);
      return var1;
   }

   private JLabel createIconLabel() {
      JLabel var1 = new JLabel(this.getIcon());
      var1.setVerticalAlignment(0);
      var1.setAlignmentX(0.5F);
      return var1;
   }

   @Override
   public Object getNodeObject() {
      return this.node;
   }

   @Override
   public boolean isResizable() {
      return false;
   }

   public final void setIcon(Icon var1) {
      this.iconLabel.setIcon(var1);
   }

   public final JLabel getContentLabel() {
      return this.contentLabel;
   }

   protected abstract Icon getIcon();

   protected abstract String getContentString();

   protected abstract String getContentToolTip();

   public void setContentString(String var1) {
      this.contentLabel.setText(var1);
   }

   @Override
   public void setNodeStyle(NodeStyle var1) {
   }

   @Override
   public Dimension getPreferredSize() {
      Dimension var1 = super.getPreferredSize();
      Dimension var2 = this.contentLabel.getPreferredSize();
      int var3 = Math.min(var2.width, 125);
      var3 = Math.max(this.getIcon().getIconWidth(), var3);
      var3 = Math.max(var3, 50);
      if (this.getBorder() != null) {
         Insets var4 = this.getBorder().getBorderInsets(this);
         var3 += var4.left + var4.right;
      }

      var1.width = var3;
      return var1;
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }

   @Override
   public void setMoving(boolean var1) {
      this.moveAppearanceHandler.setMoving(var1);
   }

   @Override
   protected void paintComponent(Graphics var1) {
      Graphics2D var2 = (Graphics2D)var1.create();

      try {
         this.moveAppearanceHandler.handleAppearanceChange(var2);
         super.paintComponent(var2);
      } finally {
         var2.dispose();
      }
   }

   public final Point getContentLabelOffset() {
      return SwingUtilities.convertPoint(this.getContentLabel(), new Point(), this);
   }

   private static class MoveAppearanceHandler {
      private boolean isMoving = false;

      private MoveAppearanceHandler() {
      }

      public void setMoving(boolean var1) {
         this.isMoving = var1;
      }

      public void handleAppearanceChange(Graphics2D var1) {
         if (this.isMoving) {
            var1.setComposite(AlphaComposite.getInstance(3, 0.5F));
         }
      }
   }
}
