package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import com.google.common.collect.Lists;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.NodeStyle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class ExpandedOperationNodeUi extends AbstractOperationNodeUi {
   private static final ExpandedOperationNodeUi.BorderParams UNSELECTED_BORDER = new ExpandedOperationNodeUi.BorderParams(Color.GRAY, 1);
   private static final ExpandedOperationNodeUi.BorderParams SELECTED_BORDER = new ExpandedOperationNodeUi.BorderParams(org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND, 2);
   private final JComponent titlePaneWrapper = this.createTitlePaneWrapper();
   private Shape shape;
   private ExpandedOperationNodeUi.BorderParams borderParams = UNSELECTED_BORDER;

   public ExpandedOperationNodeUi(Graph2D var1, OperationWrapperNode var2) {
      super(var1, var2);
      this.setLayout(new BorderLayout());
      this.add(this.titlePaneWrapper, "North");
      this.getTitlePane().setBorder(Empty.border(5, 5, 0, 0));
      this.setBorder(null);
   }

   private JComponent createTitlePaneWrapper() {
      JPanel var1 = new JPanel(false);
      InvisiblePanel.convert(var1);
      var1.setOpaque(false);
      var1.add(this.getTitlePane());
      return var1;
   }

   @Override
   public void doLayout() {
      super.doLayout();
      this.titlePaneWrapper.doLayout();
   }

   @Override
   public Dimension getMinimumSize() {
      Dimension var1 = this.titlePaneWrapper.getPreferredSize();
      return new Dimension(var1.width + 75, Math.max(var1.height, 125));
   }

   @Override
   public boolean containsPoint(Point2D var1) {
      return this.shape != null ? this.shape.contains(var1) : super.containsPoint(var1);
   }

   @Override
   public void setSelected(boolean var1) {
      this.borderParams = var1 ? SELECTED_BORDER : UNSELECTED_BORDER;
   }

   @Override
   public void setNodeStyle(NodeStyle var1) {
   }

   @Override
   protected Icon getToggleButtonIcon() {
      return GraphResources.ICONS.getIcon("Collapse.16");
   }

   @Override
   protected String getToggleButtonToolTip() {
      return "Collapse";
   }

   @Override
   protected void handleToggleButtonClick() {
      OperationNodeController var1 = this.getNodeObject().getNodeController();
      if (var1 != null) {
         var1.collapseNode(this.getNodeObject());
      } else {
         this.getGraph().getLayout().collapse(this.getNodeObject());
      }
   }

   @Override
   public List<Point2D> getConnectionPoints() {
      ArrayList var1 = Lists.newArrayList();
      Dimension var2 = this.getTitlePane().getSize();
      Dimension var3 = this.getSize();
      var1.add(new Point(0, var3.height / 2));
      var1.add(new Point(var2.width / 2, 0));
      var1.add(new Point(var3.width, (var3.height - var2.height) / 2 + var2.height));
      var1.add(new Point(var3.width / 2, var3.height));
      return var1;
   }

   @Override
   protected void paintComponent(Graphics var1) {
      Color var2 = var1.getColor();
      Graphics2D var3 = (Graphics2D)var1;
      this.shape = this.createOutline();
      var1.setColor(org.etlforge.application.ui.look.Colors.PANEL_BACKGROUND);
      var3.fill(this.shape);
      Disposable var4 = this.borderParams.configure(var3);
      var3.draw(this.shape);
      var4.dispose();
      var1.setColor(var2);
      super.paintComponent(var1);
   }

   private Shape createOutline() {
      byte var1 = 8;
      int var2 = this.getTitlePane().getY() + this.getTitlePane().getHeight() - 10;
      GeneralPath var3 = new GeneralPath();
      int var4 = var1;
      int var5 = 0;
      var3.moveTo(var4, var5);
      var4 = this.getTitlePane().getX() + this.getTitlePane().getWidth() - 5;
      var3.lineTo(var4, var5);
      byte var6 = 10;
      int var7 = var4 + var6;
      int var8 = var5;
      var4 += 2 * var6;
      var5 += var2 / 3;
      var3.quadTo(var7, var8, var4, var5);
      var4 += 8;
      var5 += var2 / 3;
      var3.lineTo(var4, var5);
      var7 = var4 + var6;
      var4 += 2 * var6;
      var5 = var2;
      var8 = var5;
      var3.quadTo(var7, var8, var4, var5);
      var4 = this.getWidth() - 1;
      var3.lineTo(var4, var5);
      var5 = this.getHeight() - 1;
      var3.lineTo(var4, var5);
      byte var14 = 0;
      var3.lineTo(var14, var5);
      byte var19 = var1;
      var3.lineTo(var14, var19);
      var3.quadTo(0.0F, 0.0F, 8.0F, 0.0F);
      var3.closePath();
      return var3;
   }

   private static class BorderParams {
      private final Color color;
      private final Stroke stroke;

      public BorderParams(Color var1, int var2) {
         this.color = var1;
         this.stroke = new BasicStroke(var2);
      }

      public Disposable configure(final Graphics2D var1) {
         final RenderingHints var2 = var1.getRenderingHints();
         final Stroke var3 = var1.getStroke();
         var1.setColor(this.color);
         var1.setStroke(this.stroke);
         UiUtils.makeAntiAliased(var1);
         return new Disposable() {
            @Override
            public void dispose() {
               var1.setStroke(var3);
               var1.setRenderingHints(var2);
            }
         };
      }
   }
}

