package org.etlforge.ui.graph2d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.ui.graph2d.dnd.DropStatus;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.graph2d.node.NodeSizeListener;

public class DefaultNodeUiFactory2D implements NodeUiFactory2D {
   @Override
   public GraphNodeUi2D createNodeUi(Graph2D var1, Object var2) {
      DefaultNodeUiFactory2D.NodeUi var3 = new DefaultNodeUiFactory2D.NodeUi(var2);
      this.customize(var3, var2);
      return var3;
   }

   protected void customize(DefaultNodeUiFactory2D.NodeUi var1, Object var2) {
   }

   protected static class NodeUi extends JPanel implements GraphNodeUi2D {
      private final Object node;
      private final JLabel label;
      private final List<NodeSizeListener> sizeListeners;

      public NodeUi(Object var1) {
         super(new BorderLayout());
         this.node = var1;
         this.label = new JLabel(this.getDisplayString(var1));
         this.add(this.label, "Center");
         this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
         this.sizeListeners = new CopyOnWriteArrayList<>();
         this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent var1) {
               NodeUi.this.fireSizeChanged();
            }
         });
      }

      @Override
      public Object getNodeObject() {
         return this.node;
      }

      protected String getDisplayString(Object var1) {
         return var1.toString();
      }

      @Override
      public final boolean isShowing() {
         return true;
      }

      @Override
      public final JComponent getRenderer(boolean var1) {
         this.setSelected(var1);
         return this;
      }

      public JLabel getLabel() {
         return this.label;
      }

      @Override
      public Dimension getSize() {
         Dimension var1 = super.getSize();
         if (var1.width == 0 && var1.height == 0) {
            var1 = this.getPreferredSize();
         }

         return var1;
      }

      @Override
      public boolean containsPoint(Point2D var1) {
         Dimension var2 = this.getSize();
         return new Rectangle(var2.width, var2.height).contains(var1);
      }

      @Override
      public boolean isResizable() {
         return true;
      }

      @Override
      public void setMoving(boolean var1) {
      }

      @Override
      public void setDropStatus(DropStatus var1) {
      }

      public void setSelected(boolean var1) {
      }

      @Override
      public void dispose() {
      }

      @Override
      public GraphNodeEditor getEditor() {
         return null;
      }

      @Override
      public boolean handleMouseEvent(MouseEvent var1) {
         return false;
      }

      @Override
      public final void addSizeListener(NodeSizeListener var1) {
         this.sizeListeners.add(var1);
      }

      @Override
      public final void removeSizeListener(NodeSizeListener var1) {
         this.sizeListeners.remove(var1);
      }

      protected final void fireSizeChanged() {
         Dimension var1 = this.getSize();

         for (NodeSizeListener var3 : this.sizeListeners) {
            var3.nodeSizeChanged(this, var1);
         }
      }

      @Override
      public List<Point2D> getConnectionPoints() {
         Dimension var1 = this.getSize();
         ArrayList var2 = new ArrayList();
         var2.add(new Point(0, var1.width / 2));
         var2.add(new Point(var1.height / 2, 0));
         var2.add(new Point(var1.height, var1.width / 2));
         var2.add(new Point(var1.height / 2, var1.width));
         return var2;
      }
   }
}
