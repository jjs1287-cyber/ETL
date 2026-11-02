package org.etlforge.integration.client.ui.entity.dependencies.graph;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.graph2d.GraphNodeEditor;
import org.etlforge.ui.graph2d.dnd.DropStatus;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.graph2d.node.NodeSizeListener;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.border.BorderBuilder;

public final class EntityNodeUi implements GraphNodeUi2D {
   private static final Border SELECTED_BORDER = new BorderBuilder().line().empty(4);
   private static final Border UNSELECTED_BORDER = Empty.border(5);
   private final IntegrationEntity entity;
   private final JLabel label;

   public EntityNodeUi(IntegrationEntity var1, boolean var2) {
      this.entity = var1;
      TextStyle var3 = var2 ? TextStyles.DefaultBoldLarger : TextStyles.DefaultText;
      this.label = var3.makeLabel("");
      RendererUtils.decorateRenderer(this.label, var1, false);
      this.setSelected(false);
   }

   public void setIcon(Icon var1) {
      this.label.setIcon(var1);
   }

   @Override
   public Object getNodeObject() {
      return this.entity;
   }

   public void setSelected(boolean var1) {
      this.label.setBorder(var1 ? SELECTED_BORDER : UNSELECTED_BORDER);
   }

   @Override
   public List<Point2D> getConnectionPoints() {
      Dimension var1 = this.getSize();
      int var2 = var1.height / 2;
      Point var3 = new Point(0, var2);
      Point var4 = new Point(var1.width, var2);
      return Arrays.asList(var3, var4);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.label.getMinimumSize();
   }

   @Override
   public Dimension getPreferredSize() {
      return this.label.getPreferredSize();
   }

   @Override
   public JComponent getRenderer(boolean var1) {
      this.setSelected(var1);
      return this.label;
   }

   @Override
   public GraphNodeEditor getEditor() {
      return null;
   }

   @Override
   public Dimension getSize() {
      Dimension var1 = this.label.getSize();
      if (var1.width == 0 || var1.height == 0) {
         var1 = this.getMinimumSize();
      }

      return var1;
   }

   @Override
   public void setSize(Dimension var1) {
      this.label.setSize(var1);
   }

   @Override
   public boolean isResizable() {
      return false;
   }

   @Override
   public boolean containsPoint(Point2D var1) {
      Dimension var2 = this.getSize();
      return new Rectangle(var2.width, var2.height).contains(var1);
   }

   @Override
   public String getToolTipText(MouseEvent var1) {
      return this.label.getToolTipText();
   }

   @Override
   public boolean handleMouseEvent(MouseEvent var1) {
      return false;
   }

   @Override
   public void setDropStatus(DropStatus var1) {
   }

   @Override
   public void setMoving(boolean var1) {
   }

   @Override
   public void dispose() {
   }

   @Override
   public void addSizeListener(NodeSizeListener var1) {
   }

   @Override
   public void removeSizeListener(NodeSizeListener var1) {
   }
}
