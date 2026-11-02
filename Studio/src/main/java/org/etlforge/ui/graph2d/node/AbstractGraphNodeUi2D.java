package org.etlforge.ui.graph2d.node;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.GraphNodeEditor;
import org.etlforge.ui.graph2d.dnd.DropStatus;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.border.RoundedBorder;

public abstract class AbstractGraphNodeUi2D extends JPanel implements GraphNodeUi2D {
   protected static final Color UNSELECTED_BACKGROUND = new Color(32, 32, 32);
   protected static final Color SELECTED_BACKGROUND = new Color(64, 64, 64);
   private static final Color DROP_ALLOWED = new Color(152, 251, 152);
   private static final Color DROP_DISALLOWED = new Color(255, 155, 155);
   private final Graph2D graph;
   private AbstractGraphNodeUi2D.BackgroundSetter backgroundSetter;
   private Color savedBackground;
   private DropStatus dropStatus;
   private final List<NodeSizeListener> sizeListeners;
   private Dimension currentSize;
   private boolean selected;

   protected AbstractGraphNodeUi2D(Graph2D var1) {
      this.graph = var1;
      this.dropStatus = DropStatus.NONE;
      this.sizeListeners = new CopyOnWriteArrayList<>();
      this.currentSize = new Dimension();
      this.selected = true;
      this.setSelected(false);
      this.savedBackground = this.getBackground();
   }

   @Override
   public void dispose() {
      this.sizeListeners.clear();
   }

   public Graph2D getGraph() {
      return this.graph;
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

   @Override
   public boolean containsPoint(Point2D var1) {
      Dimension var2 = this.getSize();
      return new Rectangle(var2.width, var2.height).contains(var1);
   }

   public void setSelected(boolean var1) {
      if (var1 != this.selected) {
         this.selected = var1;
         if (var1) {
            this.setBorder(this.getSelectedBorder());
            this.animateBackgroundChange(SELECTED_BACKGROUND);
         } else {
            this.setBorder(this.getUnselectedBorder());
            this.setBackground(UNSELECTED_BACKGROUND);
         }
      }
   }

   protected Border getSelectedBorder() {
      return new BorderBuilder(new RoundedBorder(8, 8, 2, Color.BLACK)).empty(3);
   }

   protected Border getUnselectedBorder() {
      return new BorderBuilder(new RoundedBorder(8, 8, 1, Color.GRAY)).empty(4);
   }

   @Override
   public void setMoving(boolean var1) {
   }

   @Override
   public boolean handleMouseEvent(MouseEvent var1) {
      return false;
   }

   @Override
   public GraphNodeEditor getEditor() {
      return null;
   }

   @Override
   public final void addSizeListener(NodeSizeListener var1) {
      this.sizeListeners.add(var1);
   }

   @Override
   public final void removeSizeListener(NodeSizeListener var1) {
      this.sizeListeners.remove(var1);
   }

   @Override
   public Dimension getSize() {
      Dimension var1 = super.getSize();
      return var1.height == 0 && var1.width == 0 ? this.getPreferredSize() : var1;
   }

   @Override
   public void setBounds(int var1, int var2, int var3, int var4) {
      if (var3 == 0) {
         var3 = this.currentSize.width;
      }

      if (var4 == 0) {
         var4 = this.currentSize.height;
      }

      super.setBounds(var1, var2, var3, var4);
      if (var3 != this.currentSize.width || var4 != this.currentSize.height) {
         this.currentSize = new Dimension(var3, var4);
         this.fireSizeChanged(var3, var4);
      }
   }

   private void fireSizeChanged(int var1, int var2) {
      Dimension var3 = new Dimension(var1, var2);

      for (NodeSizeListener var5 : this.sizeListeners) {
         var5.nodeSizeChanged(this, var3);
      }
   }

   @Override
   public List<Point2D> getConnectionPoints() {
      ArrayList var1 = Lists.newArrayList();
      var1.add(new Point(0, this.currentSize.height / 2));
      var1.add(new Point(this.currentSize.width / 2, 0));
      var1.add(new Point(this.currentSize.width, this.currentSize.height / 2));
      var1.add(new Point(this.currentSize.width / 2, this.currentSize.height));
      return var1;
   }

   public abstract void setNodeStyle(NodeStyle var1);

   @Override
   public void setDropStatus(DropStatus var1) {
      if (var1 == null) {
         throw new NullPointerException("status");
      }

      if (var1 != this.dropStatus) {
         this.dropStatus = var1;
         Color var2 = null;
         switch (this.dropStatus) {
            case ALLOWED:
               var2 = DROP_ALLOWED;
               break;
            case DISALLOWED:
               var2 = DROP_DISALLOWED;
               break;
            case NONE:
               var2 = this.savedBackground;
               break;
            default:
               throw new IllegalArgumentException("Invalid DropStatus: " + var1);
         }

         this.animateBackgroundChange(var2);
      }
   }

   protected final void animateBackgroundChange(Color var1) {
      if (this.backgroundSetter == null) {
         this.backgroundSetter = new AbstractGraphNodeUi2D.BackgroundSetter();
      }

      this.backgroundSetter.setBackground(var1);
   }

   private class BackgroundSetter {
      private Animator animator;

      private BackgroundSetter() {
      }

      public void setBackground(Color var1) {
         if (this.animator != null && this.animator.isRunning()) {
            this.animator.cancel();
         }

         this.animator = new Animator(150, new PropertySetter(AbstractGraphNodeUi2D.this, "background", var1));
         this.animator.addTarget(new TimingTargetAdapter() {
            @Override
            public void end() {
               BackgroundSetter.this.animator = null;
               AbstractGraphNodeUi2D.this.graph.repaint();
            }

            @Override
            public void timingEvent(float var1) {
               AbstractGraphNodeUi2D.this.graph.repaint();
            }
         });
         this.animator.setAcceleration(0.3F);
         this.animator.setDeceleration(0.2F);
         this.animator.start();
      }
   }
}

