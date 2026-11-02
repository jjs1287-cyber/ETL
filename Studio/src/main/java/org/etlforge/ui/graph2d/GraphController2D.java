package org.etlforge.ui.graph2d;

import com.google.common.collect.Lists;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.etlforge.ui.graph.GraphInputListener;
import org.etlforge.ui.graph.layout.GraphLayoutEdit;
import org.etlforge.ui.graph.layout.GraphLayoutMemento;
import org.etlforge.ui.graph2d.layout.GraphLayout2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.graphics.RectangleCorner;
import org.etlforge.ui.graphics.RectangleEdge;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.popup.PopupMenuUtils;
import org.etlforge.ui.widget.scroll.AutoScroller;

public class GraphController2D {
   private static final boolean USE_CORNER_RESIZE = true;
   private final Graph2D graph;
   private boolean isDragSelecting;
   private boolean isMoving;
   private boolean movingNodesSupported = true;
   private final List<GraphControllerListener> listeners = Lists.newCopyOnWriteArrayList();
   private final LayoutEditSender layoutEditSender;

   public GraphController2D(Graph2D var1) {
      this.graph = var1;
      this.layoutEditSender = new LayoutEditSender(var1.getLayout(), 200);
      var1.addGraphInputListener(new GraphController2D.GraphInputListenerImpl());
      var1.enableDragSelector(this);
      this.addKeyBindings(var1);
   }

   public void addControllerListener(GraphControllerListener var1) {
      this.listeners.add(var1);
      this.layoutEditSender.addListener(var1);
   }

   public void removeControllerListener(GraphControllerListener var1) {
      this.listeners.remove(var1);
      this.layoutEditSender.removeListener(var1);
   }

   public void setMovingNodesSupported(boolean var1) {
      this.movingNodesSupported = var1;
   }

   protected void handleNodeDoubleClick(Object var1) {
   }

   protected void handleCanvasDoubleClick() {
   }

   protected void handlePopupMenuRequest(Object var1, Point var2) {
   }

   protected Graph2D getGraph() {
      return this.graph;
   }

   protected SelectionAction createToggleNodeExpansionAction() {
      return new ToggleNodeExpansionAction(this.graph);
   }

   private GraphLayout2D getLayout() {
      return this.graph.getLayout();
   }

   protected final void fireLayoutChanged(GraphLayoutMemento var1) {
      if (var1 != null) {
         GraphLayoutMemento var2 = this.graph.getLayout().getMemento();
         GraphLayoutEdit var3 = new GraphLayoutEdit(var1, var2);

         for (GraphControllerListener var5 : this.listeners) {
            var5.layoutWasChanged(var3);
         }
      }
   }

   protected Collection<?> getNodesInRange(Object var1, Object var2) {
      HashSet var3 = new HashSet();
      var3.add(var1);
      var3.add(var2);
      return var3;
   }

   private void addKeyBindings(Graph2D var1) {
      this.addNodeMovingBindings(var1);
      this.addGraphZoomBindings(var1);
      var1.addKeyBinding("graph.toggle-expansion", this.createToggleNodeExpansionAction());
      this.addNodeNavigationBindings(var1);
   }

   private void addNodeMovingBindings(Graph2D var1) {
      var1.addKeyBinding(KeyUtils.getCtrlDown(37), "graph.ctrl-left-arrow", new GraphController2D.LeftKeyInputAction());
      var1.addKeyBinding(KeyUtils.getCtrlDown(39), "graph.ctrl-right-arrow", new GraphController2D.RightKeyInputAction());
      var1.addKeyBinding(KeyUtils.getCtrlDown(38), "graph.ctrl-up-arrow", new GraphController2D.UpKeyInputAction());
      var1.addKeyBinding(KeyUtils.getCtrlDown(40), "graph.ctrl-down-arrow", new GraphController2D.DownKeyInputAction());
   }

   private void addGraphZoomBindings(Graph2D var1) {
      var1.addKeyBinding("graph.zoom-in", new ZoomInAction(var1));
      var1.addKeyBinding("graph.zoom-out", new ZoomOutAction(var1));
      var1.addKeyBinding("graph.zoom-reset", new ResetZoomAction(var1));
   }

   private void addNodeNavigationBindings(Graph2D var1) {
      Action var2 = NavigationActions.forward(var1);
      Action var3 = NavigationActions.backward(var1);
      var1.addKeyBinding(KeyUtils.RIGHT, "graph.navigate-forward", var2);
      var1.addKeyBinding(KeyUtils.TAB, "graph.navigate-forward", var2);
      var1.addKeyBinding(KeyUtils.LEFT, "graph.navigate-backward", var3);
      var1.addKeyBinding(KeyUtils.getShiftDown(9), "graph.navigate-backward", var3);
   }

   private abstract class ArrowKeyInputAction extends AbstractSelectionAction {
      private Point delta;

      private ArrowKeyInputAction() {
      }

      protected void setDelta(int var1, int var2) {
         this.delta = new Point(var1, var2);
      }

      @Override
      public boolean isEnabled() {
         return true;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (GraphController2D.this.movingNodesSupported) {
            Selection var2 = this.getSelection();
            if (!var2.isEmpty()) {
               for (Object var4 : var2.items()) {
                  GraphController2D.this.getLayout().moveBy(var4, this.delta);
               }

               GraphController2D.this.layoutEditSender.newEdit();
            }
         }
      }
   }

   private final class CornerResizer implements GraphController2D.ResizeStrategy {
      private final Object node;
      private boolean resizing;
      private Point startPoint;
      private Dimension originalSize;

      public CornerResizer(Object var2) {
         this.node = var2;
      }

      @Override
      public Object getNode() {
         return this.node;
      }

      @Override
      public boolean checkTrigger(MouseEvent var1) {
         this.resizing = this.isHotCorner(var1);
         if (this.resizing) {
            this.startPoint = var1.getPoint();
            this.originalSize = GraphController2D.this.getLayout().getBounds(this.node).getSize();
         }

         return this.resizing;
      }

      @Override
      public void done() {
         this.resizing = false;
         this.startPoint = null;
         this.originalSize = null;
      }

      @Override
      public Cursor getCursor(MouseEvent var1) {
         return this.isHotCorner(var1) ? RectangleCorner.LOWER_RIGHT.getResizeCursor() : Cursor.getDefaultCursor();
      }

      @Override
      public boolean isResizing() {
         return this.resizing;
      }

      @Override
      public void update(MouseEvent var1) {
         if (this.resizing) {
            Point var2 = var1.getPoint();
            int var3 = var2.x - this.startPoint.x;
            int var4 = var2.y - this.startPoint.y;
            Dimension var5 = new Dimension(this.originalSize.width + var3, this.originalSize.height + var4);
            GraphController2D.this.getLayout().resize(this.node, var5);
            GraphController2D.this.graph.ensureNodeIsVisible(this.node);
         }
      }

      private boolean isHotCorner(MouseEvent var1) {
         return this.getCornerForEvent(var1) == RectangleCorner.LOWER_RIGHT;
      }

      private RectangleCorner getCornerForEvent(MouseEvent var1) {
         Rectangle var2 = GraphController2D.this.getLayout().getBounds(this.node);
         return var2 == null ? null : RectangleCorner.getCorner(var2, var1.getPoint(), 8);
      }
   }

   private class DownKeyInputAction extends GraphController2D.ArrowKeyInputAction {
      public DownKeyInputAction() {
         this.setDelta(0, 1);
      }
   }

   private final class EdgeResizer implements GraphController2D.ResizeStrategy {
      private final Object node;
      private Point lastResizePoint;
      private RectangleEdge draggedEdge;

      public EdgeResizer(Object var2) {
         this.node = var2;
      }

      @Override
      public Object getNode() {
         return this.node;
      }

      @Override
      public boolean checkTrigger(MouseEvent var1) {
         this.draggedEdge = this.getEdgeForEvent(var1);
         if (this.draggedEdge != null) {
            this.lastResizePoint = var1.getPoint();
         }

         return this.isResizing();
      }

      @Override
      public boolean isResizing() {
         return this.draggedEdge != null;
      }

      @Override
      public void update(MouseEvent var1) {
         if (this.draggedEdge != null) {
            Point var2 = var1.getPoint();
            int var3 = var2.x - this.lastResizePoint.x;
            int var4 = var2.y - this.lastResizePoint.y;
            this.lastResizePoint = var2;
            switch (this.draggedEdge) {
               case TOP:
                  if (var4 == 0) {
                     return;
                  }
                  break;
               case RIGHT:
                  this.handleRightEdgeResize(var3, var4);
                  break;
               case BOTTOM:
                  this.handleBottomEdgeResize(var3, var4);
                  break;
               case LEFT:
                  if (var3 == 0) {
                     return;
                  }
            }

            GraphController2D.this.graph.ensureNodeIsVisible(this.node);
         }
      }

      @Override
      public void done() {
         this.draggedEdge = null;
         this.lastResizePoint = null;
      }

      @Override
      public Cursor getCursor(MouseEvent var1) {
         RectangleEdge var2 = this.getEdgeForEvent(var1);
         return var2 != null ? var2.getResizeCursor() : Cursor.getDefaultCursor();
      }

      private RectangleEdge getEdgeForEvent(MouseEvent var1) {
         Rectangle var2 = GraphController2D.this.getLayout().getBounds(this.node);
         return RectangleEdge.getEdgeForPoint(var2, var1.getPoint(), 3);
      }

      private void handleRightEdgeResize(int var1, int var2) {
         if (var1 != 0) {
            GraphController2D.this.getLayout().resize(this.node, var1, var2);
         }
      }

      private void handleBottomEdgeResize(int var1, int var2) {
         if (var2 != 0) {
            GraphController2D.this.getLayout().resize(this.node, var1, var2);
         }
      }
   }

   private class GraphInputListenerImpl implements GraphInputListener {
      private Point movePoint;
      private Object currentNode;
      private GraphController2D.ResizeStrategy resizer;
      private AutoScroller autoScroller;
      private Point autoScrollAnchor;

      private GraphInputListenerImpl() {
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         if (SwingUtilities.isLeftMouseButton(var1)) {
            this.checkDoubleClick(var1);
         }
      }

      private void checkDoubleClick(MouseEvent var1) {
         if (var1.getClickCount() == 2) {
            Point var2 = var1.getPoint();
            Object var3 = GraphController2D.this.graph.getNodeAt(var2);
            if (var3 != null) {
               GraphController2D.this.handleNodeDoubleClick(var3);
            } else {
               GraphController2D.this.handleCanvasDoubleClick();
            }
         }
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
      }

      @Override
      public void mouseExited(MouseEvent var1) {
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         if (!this.handlePotentialPopupMenuRequest(var1)) {
            if (SwingUtilities.isMiddleMouseButton(var1)) {
               if (this.autoScroller != null) {
                  this.stopAutoScrolling();
               } else {
                  this.autoScrollAnchor = GraphController2D.this.graph.convertToViewPort(var1.getPoint());
                  this.autoScroller = GraphController2D.this.graph.startAutoScrolling(var1.getPoint());
               }
            } else if (!SwingUtilities.isLeftMouseButton(var1)) {
               this.movePoint = null;
               this.currentNode = null;
            } else {
               int var2 = var1.getModifiersEx();
               boolean var3 = (var2 & 128) == 128;
               boolean var4 = (var2 & 64) == 64;
               Point var5 = var1.getPoint();
               this.movePoint = var5;
               Object var6 = GraphController2D.this.graph.getNodeAt(var5);
               if (var6 != null && this.currentNode != var6) {
                  GraphController2D.this.getLayout().moveToTop(var6);
               }

               this.currentNode = var6;
               if (var6 != null && !GraphController2D.this.isMoving && !var3 && !var4 && !GraphController2D.this.graph.getSelectionModel().isSelected(var6)) {
                  ArrayList var7 = new ArrayList();
                  var7.add(var6);
                  GraphController2D.this.graph.getSelectionModel().setSelection(var7);
               }

               if (this.resizer != null) {
                  this.resizer.checkTrigger(var1);
               }
            }
         }
      }

      private void handleShiftClickSelecting(Object var1) {
         Object var2 = GraphController2D.this.graph.getModel().getParentOf(var1);
         Collection var3 = null;
         int var4 = 0;

         for (Object var6 : GraphController2D.this.graph.getSelectionModel().getSelection()) {
            if (var6 != var1 && GraphController2D.this.graph.getModel().getParentOf(var6) == var2) {
               Collection var7 = GraphController2D.this.getNodesInRange(var1, var6);
               if (var7 != null && var7.size() > var4) {
                  var4 = var7.size();
                  var3 = var7;
               }
            }
         }

         if (var3 != null) {
            HashSet var8 = new HashSet(var3);
            GraphController2D.this.graph.getSelectionModel().setSelection(var8);
         }
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         boolean var2 = false;
         if (GraphController2D.this.isDragSelecting) {
            GraphController2D.this.isDragSelecting = false;
         } else if (this.resizer != null && this.resizer.isResizing()) {
            this.finishResizing();
            var2 = true;
         } else {
            this.handlePotentialPopupMenuRequest(var1);
            this.processSelectionMouseEvent(var1);
         }

         if (this.autoScroller != null && (this.autoScroller.getDeltaX() != 0 || this.autoScroller.getDeltaY() != 0)) {
            this.stopAutoScrolling();
         }

         if (GraphController2D.this.isMoving) {
            this.finishNodeDragging(var1);
            var2 = true;
         }

         GraphController2D.this.graph.setCursor(Cursor.getDefaultCursor());
         if (var2) {
            GraphController2D.this.graph.recalculateSize();
         }

         GraphController2D.this.graph.repaint();
      }

      private void stopAutoScrolling() {
         GraphController2D.this.graph.stopAutoScrolling();
         this.autoScroller = null;
      }

      private void processSelectionMouseEvent(MouseEvent var1) {
         Point var2 = var1.getPoint();
         Object var3 = GraphController2D.this.graph.getNodeAt(var2);
         if (var3 != null && !GraphController2D.this.isMoving) {
            int var4 = var1.getModifiersEx();
            boolean var5 = (var4 & 128) != 0;
            boolean var6 = (var4 & 64) != 0;
            ArrayList var7 = new ArrayList();
            var7.add(var3);
            if (var5) {
               GraphController2D.this.graph.getSelectionModel().flipSelection(var7);
            } else if (var6) {
               this.handleShiftClickSelecting(var3);
            } else {
               GraphController2D.this.graph.getSelectionModel().setSelection(var7);
            }
         } else if (var3 == null) {
            GraphController2D.this.graph.getSelectionModel().clearSelection();
         }
      }

      private void finishResizing() {
         this.resizer.done();
         GraphController2D.this.layoutEditSender.newEdit();
      }

      private void finishNodeDragging(MouseEvent var1) {
         GraphController2D.this.isMoving = false;
         Object var2 = GraphController2D.this.graph.getNodeAt(var1.getPoint());
         if (var2 != null) {
            GraphController2D.this.graph.ensureNodeIsVisible(var2);
         } else {
            GraphController2D.this.graph.ensureRectangleIsVisible(new Rectangle(var1.getPoint(), new Dimension(10, 10)));
         }

         this.movePoint = null;

         for (Object var4 : GraphController2D.this.graph.getSelectionModel().getSelection()) {
            GraphNodeUi2D var5 = GraphController2D.this.graph.getNodeUi(var4);
            if (var5 != null) {
               var5.setMoving(false);
            }
         }

         GraphController2D.this.layoutEditSender.newEdit();
      }

      @Override
      public void mouseDragged(MouseEvent var1) {
         if (var1.getX() >= 0 && var1.getY() >= 0) {
            if (this.resizer != null && this.resizer.isResizing()) {
               this.resizer.update(var1);
            } else {
               if (!GraphController2D.this.isMoving) {
                  this.updateAutoScroller(var1);
                  if (this.currentNode == null) {
                     if (!GraphController2D.this.isDragSelecting) {
                        GraphController2D.this.graph.getSelectionModel().clearSelection();
                     }
                  } else if (!GraphController2D.this.getGraph().getSelectionModel().isSelected(this.currentNode)) {
                     List var2 = Collections.singletonList(this.currentNode);
                     GraphController2D.this.graph.getSelectionModel().addToSelection(var2);
                  }
               }

               if (GraphController2D.this.movingNodesSupported
                  && this.movePoint != null
                  && !GraphController2D.this.getGraph().getSelectionModel().getSelection().isEmpty()) {
                  this.moveNodes(var1);
               }
            }
         }
      }

      private void updateAutoScroller(MouseEvent var1) {
         if (this.autoScroller != null) {
            Point var2 = GraphController2D.this.graph.convertToViewPort(var1.getPoint());
            this.autoScroller.setDeltaX(var2.x - this.autoScrollAnchor.x);
            this.autoScroller.setDeltaY(var2.y - this.autoScrollAnchor.y);
         }
      }

      private void moveNodes(MouseEvent var1) {
         if (!GraphController2D.this.isDragSelecting) {
            Point var2 = var1.getPoint();
            if (GraphController2D.this.isMoving || !(var2.distance(this.movePoint) < 5.0)) {
               if (!GraphController2D.this.isDragSelecting && (var1.getModifiersEx() & 64) == 64) {
                  GraphController2D.this.isDragSelecting = true;
               }

               GraphController2D.this.isMoving = true;
               Point var3 = new Point(var2.x - this.movePoint.x, var2.y - this.movePoint.y);
               this.movePoint = var2;

               for (Object var5 : GraphController2D.this.getGraph().getSelectionModel().getSelection()) {
                  this.currentNode = var5;
                  this.moveNodeBy(var5, var3);
               }

               this.currentNode = null;
            }
         }
      }

      private void moveNodeBy(Object var1, Point var2) {
         GraphController2D.this.getLayout().moveBy(var1, var2);
         GraphNodeUi2D var3 = GraphController2D.this.graph.getNodeUi(var1);
         if (var3 != null) {
            var3.setMoving(true);
         }
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
         Object var2 = GraphController2D.this.graph.getNodeAt(var1.getPoint());
         GraphNodeUi2D var3 = var2 != null ? GraphController2D.this.graph.getNodeUi(var2) : null;
         Cursor var4 = Cursor.getDefaultCursor();
         if (var3 != null && var3.isResizable()) {
            if (this.resizer == null || var2 != this.resizer.getNode()) {
               this.resizer = this.createNewResizer(var2);
            }

            if (this.resizer != null) {
               var4 = this.resizer.getCursor(var1);
            }
         }

         GraphController2D.this.graph.setCursor(var4);
         this.updateAutoScroller(var1);
      }

      private boolean handlePotentialPopupMenuRequest(MouseEvent var1) {
         if (this.isPopupTrigger(var1)) {
            Point var2 = var1.getPoint();
            Object var3 = GraphController2D.this.graph.getNodeAt(var2);
            GraphController2D.this.handlePopupMenuRequest(var3, var1.getPoint());
            return true;
         } else {
            return false;
         }
      }

      private boolean isPopupTrigger(MouseEvent var1) {
         return PopupMenuUtils.isPopupTrigger(var1);
      }

      private GraphController2D.ResizeStrategy createNewResizer(Object var1) {
         return GraphController2D.this.new CornerResizer(var1);
      }
   }

   private class LeftKeyInputAction extends GraphController2D.ArrowKeyInputAction {
      public LeftKeyInputAction() {
         this.setDelta(-1, 0);
      }
   }

   private interface ResizeStrategy {
      Object getNode();

      boolean isResizing();

      boolean checkTrigger(MouseEvent var1);

      void update(MouseEvent var1);

      void done();

      Cursor getCursor(MouseEvent var1);
   }

   private class RightKeyInputAction extends GraphController2D.ArrowKeyInputAction {
      public RightKeyInputAction() {
         this.setDelta(1, 0);
      }
   }

   private class UpKeyInputAction extends GraphController2D.ArrowKeyInputAction {
      public UpKeyInputAction() {
         this.setDelta(0, -1);
      }
   }
}
