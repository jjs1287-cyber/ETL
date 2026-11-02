package org.etlforge.ui.graph2d;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import org.etlforge.application.ui.widget.BasicTwoColumnLabelDisplayer;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.system.Disposable;
import org.etlforge.ui.DimensionUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.graph.DefaultGraphSelectionModel;
import org.etlforge.ui.graph.DisabledNavigationOrder;
import org.etlforge.ui.graph.Graph;
import org.etlforge.ui.graph.GraphInputListener;
import org.etlforge.ui.graph.GraphSelectionEvent;
import org.etlforge.ui.graph.GraphSelectionListener;
import org.etlforge.ui.graph.GraphSelectionModel;
import org.etlforge.ui.graph.GraphUiElement;
import org.etlforge.ui.graph.NavigationOrder;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.layout.GraphLayoutEvent;
import org.etlforge.ui.graph.layout.GraphLayoutListener;
import org.etlforge.ui.graph.model.GraphModel;
import org.etlforge.ui.graph.model.GraphModelEvent;
import org.etlforge.ui.graph.model.GraphModelListener;
import org.etlforge.ui.graph.model.NodeChangedEvent;
import org.etlforge.ui.graph.model.NodesConnectedEvent;
import org.etlforge.ui.graph.model.NodesDisconnectedEvent;
import org.etlforge.ui.graph.model.NodesInsertedEvent;
import org.etlforge.ui.graph.model.NodesRemovedEvent;
import org.etlforge.ui.graph2d.dnd.DropStatus;
import org.etlforge.ui.graph2d.dnd.GraphDropHandler;
import org.etlforge.ui.graph2d.dnd.NoDropHandler;
import org.etlforge.ui.graph2d.edge.EdgeContextMenuProvider;
import org.etlforge.ui.graph2d.edge.GraphEdgeUi2D;
import org.etlforge.ui.graph2d.layout.DefaultGraphLayout2D;
import org.etlforge.ui.graph2d.layout.GraphLayout2D;
import org.etlforge.ui.graph2d.layout.NodeCollapsedEvent;
import org.etlforge.ui.graph2d.layout.NodeExpandedEvent;
import org.etlforge.ui.graph2d.layout.NodeExpansionListener;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.graph2d.node.NodeSizeListener;
import org.etlforge.ui.print.HasPrintable;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.util.ImageLoader;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.overlay.OverlayAbsolutePositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.scroll.AutoScroller;
import org.etlforge.ui.widget.scroll.ScrollPosition;

public class Graph2D implements Graph, Disposable, HasPrintable {
   public static final String ZOOM_LEVEL = "zoomLevel";
   public static final String MODEL_PROPERTY = "model";
   public static final String LAYOUT_MODEL_PROPERTY = "layoutModel";
   private static final Icon AUTOSCROLL_ANCHOR = ImageLoader.getImageIcon("org.etlforge.ui.graph2d.icons", "autoscrollanchor.gif");
   private final Graph2D.Canvas canvas;
   private GraphModel dataModel;
   private GraphLayout2D layout;
   private final Graph2D.ModelListener modelListener;
   private final GraphSelectionModel selectionModel;
   private final Map<Object, GraphNodeUi2D> nodeUis;
   private final Map<GraphEdge, GraphEdgeUi2D> edgeUis;
   private final KongaLayeredPane ui;
   private final JScrollPane scroll;
   private AutoScroller autoScroller;
   private JComponent autoScrollAnchor;
   private final NodeUiFactory2D nodeUiFactory;
   private EdgeUiFactory2D edgeUiFactory;
   private GraphDropHandler dropHandler;
   private double zoom;
   private final List<GraphInputListener> inputListeners;
   private final PropertyChangeSupport changeSupport;
   private final Graph2D.MouseEventDispatcher mouseEventDispatcher;
   private EdgeContextMenuProvider edgeContextMenuProvider;
   private boolean editable;
   private Graph2D.EditNodeAction editNodeAction;
   private GraphNodeEditor nodeEditor;
   private NavigationOrder navigationOrder;
   private final Map<String, SelectionAction> selectionActions;

   public Graph2D(GraphModel var1) {
      this(var1, new DefaultNodeUiFactory2D(), new DefaultEdgeUiFactory2D());
   }

   public Graph2D(GraphModel var1, NodeUiFactory2D var2, EdgeUiFactory2D var3) {
      Preconditions.checkNotNull(var1, "data");
      this.nodeUiFactory = Preconditions.checkNotNull(var2, "nodeUiFactory");
      this.edgeUiFactory = Preconditions.checkNotNull(var3, "edgeUiFactory");
      this.zoom = 1.0;
      this.nodeUis = Maps.newHashMap();
      this.edgeUis = Maps.newHashMap();
      this.changeSupport = new PropertyChangeSupport(this);
      this.canvas = new Graph2D.Canvas();
      this.modelListener = new Graph2D.ModelListener();
      this.selectionModel = new DefaultGraphSelectionModel();
      this.selectionModel.addSelectionListener(this.modelListener);
      this.selectionActions = Maps.newHashMap();
      this.selectionModel.addSelectionListener(new Graph2D.SelectionActionUpdater());
      this.setModel(var1);
      this.scroll = new KongaScrollPane(this.canvas);
      this.scroll.setBackground(javax.swing.UIManager.getColor("Panel.background"));
      this.dropHandler = new NoDropHandler();
      this.inputListeners = new CopyOnWriteArrayList<>();
      this.mouseEventDispatcher = new Graph2D.MouseEventDispatcher();
      this.setNavigationOrder(null);
      this.registerInputHandler();
      this.bindDebugActions();
      this.ui = new KongaLayeredPane(this.scroll);
   }

   protected GraphLayout2D createLayout() {
      return new DefaultGraphLayout2D(this);
   }

   private void registerInputHandler() {
      Graph2D.InternalInputListener var1 = new Graph2D.InternalInputListener();
      this.canvas.addMouseListener(var1);
      this.canvas.addMouseMotionListener(var1);
   }

   private void bindDebugActions() {
      this.addKeyBindingImpl(KeyUtils.getCtrlDown(73), "mousePositionInfo", new Graph2D.MousePositionInfoAction());
      this.addKeyBindingImpl(KeyUtils.getCtrlDown(112), "nodeInfo", new Graph2D.NodeInfoAction());
   }

   public void setModel(GraphModel var1) {
      this.setModel(var1, null);
   }

   public void setModel(GraphModel var1, GraphLayout2D var2) {
      Preconditions.checkNotNull(var1, "data");
      GraphModel var3 = this.dataModel;
      if (var3 != null) {
         this.clearCanvas();
         var3.removeModelListener(this.modelListener);
      }

      GraphLayout2D var4 = this.layout;
      if (var4 != null) {
         var4.removeGraphLayoutListener(this.modelListener);
         var4.removeNodeExpansionListener(this.modelListener);
      }

      this.dataModel = var1;
      this.recreateNodeAndEdgeUis();
      this.layout = var2 != null ? var2 : this.createLayout();
      this.buildCanvas();
      this.dataModel.addModelListener(this.modelListener);
      this.layout.addGraphLayoutListener(this.modelListener);
      this.layout.addNodeExpansionListener(this.modelListener);
      this.selectionModel.clearSelection();
      this.changeSupport.firePropertyChange("model", var3, this.dataModel);
      this.changeSupport.firePropertyChange("layoutModel", var4, this.layout);
   }

   public final void recreateNodeAndEdgeUis() {
      this.nodeUis.clear();
      this.edgeUis.clear();
      this.createNodeUisFromModel();
      this.createEdgeUisFromModel();
   }

   public void setNavigationOrder(NavigationOrder var1) {
      this.navigationOrder = var1 != null ? var1 : new DisabledNavigationOrder();
   }

   public NavigationOrder getNavigationOrder() {
      return this.navigationOrder;
   }

   public void setNodeUiFactory(NodeUiFactory2D var1) {
      throw new NotImplementedYetException();
   }

   public void setEdgeUiFactory(EdgeUiFactory2D var1) {
      this.edgeUiFactory = var1;
      this.recreateEdgeUisWithConnectingPointsIntact();
      this.repaint();
   }

   private void recreateEdgeUisWithConnectingPointsIntact() {
      HashMap var1 = Maps.newHashMap(this.edgeUis);
      this.edgeUis.clear();

      for (GraphEdge var3 : this.dataModel.getEdges()) {
         GraphEdgeUi2D var4 = this.createUiForEdge(var3);
         GraphEdgeUi2D var5 = (GraphEdgeUi2D)var1.get(var3);
         if (var5 != null) {
            var4.setStartPoint(var5.getStartPoint());
            var4.setEndPoint(var5.getEndPoint());
         }
      }
   }

   public void setDropHandler(GraphDropHandler var1) {
      this.dropHandler = var1 != null ? var1 : new NoDropHandler();
   }

   public void recalculateSize() {
      this.canvas.resetMaxSize();
      this.repaint();
   }

   public Point getPointInWindow(Object var1, Point var2) {
      Rectangle var4 = this.layout.getBounds(var1);
      Point var3;
      if (var4 != null) {
         var3 = var4.getLocation();
         var3.translate((int)(var2.x * this.zoom), (int)(var2.y * this.zoom));
         var3.x = (int)(var3.x * this.zoom);
         var3.y = (int)(var3.y * this.zoom);
      } else {
         var3 = new Point();
      }

      return SwingUtilities.convertPoint(this.canvas, var3, this.getWindowOwner());
   }

   public void setEditable(boolean var1) {
      if (var1 != this.editable) {
         this.editable = true;
         if (var1) {
            if (this.editNodeAction == null) {
               this.editNodeAction = new Graph2D.EditNodeAction();
            }

            this.editNodeAction.updateEnabledState();
            this.editNodeAction.installKeyShortCut();
         } else if (this.editNodeAction != null) {
            this.editNodeAction.removeKeyShortCut();
         }
      }
   }

   public void editNode(Object var1) {
      if (this.nodeEditor == null || this.nodeEditor.stopCellEditing()) {
         GraphNodeUi2D var2 = this.getNodeUi(var1);
         if (var2 != null) {
            GraphNodeEditor var3 = var2.getEditor();
            if (var3 != null) {
               this.showNodeEditor(var3);
            }
         }
      }
   }

   public void showNodeEditor(GraphNodeEditor var1) {
      if (this.nodeEditor == null || this.nodeEditor.stopCellEditing()) {
         this.nodeEditor = var1;
         if (var1 != null) {
            final JComponent var2 = var1.getComponent(this);
            Point var3 = this.layout.getBounds(var1.getNode()).getLocation();
            Point var4 = var1.getOffset();
            var3.translate(var4.x, var4.y);
            var2.setLocation(var3);
            Dimension var5 = var2.getSize();
            if (var5.width == 0 || var5.height == 0) {
               var2.setSize(var2.getPreferredSize());
            }

            this.canvas.add(var2);
            var1.addCellEditorListener(new CellEditorListener() {
               @Override
               public void editingCanceled(ChangeEvent var1) {
                  Graph2D.this.removeEditorComponent(var2);
               }

               @Override
               public void editingStopped(ChangeEvent var1) {
                  Graph2D.this.removeEditorComponent(var2);
               }
            });
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  var2.requestFocusInWindow();
               }
            });
         }

         this.repaint();
      }
   }

   private void removeEditorComponent(JComponent var1) {
      this.canvas.remove(var1);
      this.nodeEditor = null;
      this.repaint();
   }

   public void addKeyBinding(String var1, Action var2) {
      this.addKeyBinding((KeyStroke)var2.getValue("AcceleratorKey"), var1, var2);
   }

   public void addKeyBinding(KeyStroke var1, String var2, Action var3) {
      Graph2D.ActionWrapper var4 = new Graph2D.ActionWrapper(var3);
      if (var3 instanceof SelectionAction) {
         this.registerSelectionAction(var1, var2, (SelectionAction)var3);
      }

      this.addKeyBindingImpl(var1, var2, var4);
   }

   private void registerSelectionAction(KeyStroke var1, String var2, SelectionAction var3) {
      this.selectionActions.put(var2, var3);
      var3.setSelection(SelectionFactory.newOrderedSelection(this.selectionModel.getSelection()));
   }

   private void addKeyBindingImpl(KeyStroke var1, String var2, Action var3) {
      InputMap var4 = this.canvas.getInputMap(1);
      var4.put(var1, var2);
      ActionMap var5 = this.canvas.getActionMap();
      var5.put(var2, var3);
   }

   public OverlayContainer getOverlayContainer() {
      return this.ui;
   }

   public void setZoomLevel(double var1) {
      if (var1 <= 0.0) {
         throw new IllegalArgumentException("zoom must be > 0 (was " + var1 + ")");
      }

      double var3 = this.zoom;
      this.zoom = var1;
      this.changeSupport.firePropertyChange("zoomLevel", var3, var1);
      this.repaint();
   }

   public double getZoomLevel() {
      return this.zoom;
   }

   @Override
   public Printable getPrintable() {
      return new Printable() {
         @Override
         public int print(Graphics var1, PageFormat var2, int var3) throws PrinterException {
            if (var3 == 0) {
               Graphics2D var4 = (Graphics2D)var1;
               double var5 = var2.getImageableHeight();
               double var7 = var2.getImageableWidth();
               double var9 = Graph2D.this.canvas.getHeight();
               double var11 = Graph2D.this.canvas.getWidth();
               double var13;
               double var15;
               if (var2.getOrientation() == 1) {
                  var13 = var5 / var11 * 0.75;
                  var15 = var7 / var9 * 0.75;
               } else {
                  var13 = var7 / var11;
                  var15 = var5 / var9;
               }

               double var17 = 1.0;
               if (Math.min(var13, var15) < 1.0) {
                  var17 = Math.min(var13, var15);
               }

               UiUtils.makeAntiAliased(var4);
               var4.translate(var2.getImageableX(), var2.getImageableY());
               UiUtils.disableDoubleBuffering(Graph2D.this.canvas);
               double var19 = Graph2D.this.getZoomLevel();
               Graph2D.this.setZoomLevel(var17);
               Graph2D.this.canvas.paint(var4);
               Graph2D.this.setZoomLevel(var19);
               UiUtils.enableDoubleBuffering(Graph2D.this.canvas);
               return 0;
            } else {
               return 1;
            }
         }
      };
   }

   public void repaint() {
      this.canvas.repaint();
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.addPropertyChangeListener(var1);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.addPropertyChangeListener(var1, var2);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.removePropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.removePropertyChangeListener(var1, var2);
   }

   public Window getWindowOwner() {
      return SwingUtilities.getWindowAncestor(this.canvas);
   }

   public void setMinimumSize(Dimension var1) {
      this.canvas.setMinimumSize(var1);
   }

   public GraphUiElement getTopUiElement(GraphNodeUi2D var1, GraphEdgeUi2D var2) {
      return var1;
   }

   public Object getNodeAt(Point var1) {
      return this.layout.getNodeAt(var1);
   }

   public GraphNodeUi2D getNodeUiAt(Point var1) {
      Object var2 = this.getNodeAt(var1);
      return var2 != null ? this.getNodeUi(var2) : null;
   }

   public void handleEdgeContextMenuRequest(GraphEdgeUi2D var1, int var2, int var3) {
      if (this.edgeContextMenuProvider != null) {
         JPopupMenu var4 = this.edgeContextMenuProvider.getPopupMenu(var1, var2, var3);
         if (var4 != null) {
            this.showContextMenu(var4, new Point(var2, var3));
         }
      }
   }

   private void buildCanvas() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            this.layoutEdges();
            Graph2D.this.canvas.revalidate();
            Graph2D.this.canvas.repaint();
         }

         private void layoutEdges() {
            for (GraphEdge var2 : Graph2D.this.dataModel.getEdges()) {
               Graph2D.this.createUiForEdge(var2);
            }
         }
      });
   }

   private void clearCanvas() {
      this.canvas.removeAll();
      this.removeAndDisposeNodeUis();
      this.edgeUis.clear();
   }

   private void removeAndDisposeNodeUis() {
      for (GraphNodeUi2D var2 : this.nodeUis.values()) {
         var2.dispose();
      }

      this.nodeUis.clear();
   }

   private void createNodeUisFromModel() {
      for (Object var2 : this.dataModel.getNodes()) {
         this.createAndAddNodeUi(var2);
      }
   }

   private void createEdgeUisFromModel() {
      for (GraphEdge var2 : this.dataModel.getEdges()) {
         this.createUiForEdge(var2);
      }
   }

   public GraphEdge getEdgeAt(Point var1) {
      GraphEdgeUi2D var2 = this.getEdgeUiAt(var1);
      return var2 != null ? var2.getEdge() : null;
   }

   public GraphEdgeUi2D getEdgeUiAt(Point var1) {
      for (GraphEdgeUi2D var3 : this.edgeUis.values()) {
         if (var3.containsPoint(var1) && this.layout.shouldBePainted(var3.getEdge())) {
            return var3;
         }
      }

      return null;
   }

   public GraphEdgeUi2D getEdgeUi(GraphEdge var1) {
      return this.edgeUis.get(var1);
   }

   public GraphNodeUi2D getNodeUi(Object var1) {
      return this.nodeUis.get(var1);
   }

   private GraphNodeUi2D createAndAddNodeUi(Object var1) {
      GraphNodeUi2D var2 = this.nodeUiFactory.createNodeUi(this, var1);
      var2.addSizeListener(this.modelListener);
      this.nodeUis.put(var1, var2);
      return var2;
   }

   private GraphEdgeUi2D createUiForEdge(GraphEdge var1) {
      GraphEdgeUi2D var2 = this.edgeUis.get(var1);
      if (var2 == null) {
         var2 = this.edgeUiFactory.createUi(this, var1);
         this.edgeUis.put(var1, var2);
      }

      return var2;
   }

   @Override
   public GraphModel getModel() {
      return this.dataModel;
   }

   public GraphLayout2D getLayout() {
      return this.layout;
   }

   @Override
   public GraphSelectionModel getSelectionModel() {
      return this.selectionModel;
   }

   @Override
   public void addGraphInputListener(GraphInputListener var1) {
      this.inputListeners.add(var1);
   }

   @Override
   public void removeGraphInputListener(GraphInputListener var1) {
      this.inputListeners.remove(var1);
   }

   @Override
   public void dispose() {
      this.mouseEventDispatcher.dispose();
      this.stopAutoScrolling();

      for (GraphNodeUi2D var2 : this.nodeUis.values()) {
         var2.dispose();
      }
   }

   public void enableDragSelector(GraphController2D var1) {
      new GraphMouseDragSelector(this.canvas, var1, this.layout);
   }

   public ScrollPosition getScrollPosition() {
      return new ScrollPosition(this.scroll);
   }

   public void restoreScrollLocation(ScrollPosition var1) {
      var1.restore(this.scroll);
   }

   public void ensureNodeIsVisible(Object var1) {
      Rectangle var2 = this.layout.getBounds(var1);
      if (var2 != null) {
         this.ensureRectangleIsVisible(var2);
      }
   }

   public void ensureRectangleIsVisible(Rectangle var1) {
      Rectangle var2 = this.scroll.getViewport().getViewRect();
      if (!var2.contains(var1)) {
         this.canvas.scrollRectToVisible(var1);
      }
   }

   public AutoScroller startAutoScrolling(Point var1) {
      this.stopAutoScrolling();
      Icon var2 = AUTOSCROLL_ANCHOR;
      Point var3 = SwingUtilities.convertPoint(this.canvas, var1, this.ui);
      int var4 = var2.getIconWidth() + 2;
      int var5 = var2.getIconHeight() + 2;
      int var6 = var3.x - var4 / 2;
      int var7 = var3.y - var5 / 2;
      this.autoScrollAnchor = new JLabel(var2);
      this.autoScrollAnchor.setBorder(Empty.border(2));
      this.ui.addOverlay(this.autoScrollAnchor, new OverlayAbsolutePositioner(new Point(var6, var7)));
      this.autoScroller = new AutoScroller(this.scroll);
      this.autoScroller.start();
      return this.autoScroller;
   }

   public void stopAutoScrolling() {
      if (this.autoScroller != null) {
         this.ui.removeOverlay(this.autoScrollAnchor);
         this.autoScroller.stop();
         this.autoScroller = null;
      }
   }

   public void toggleExpansion(Object var1, boolean var2) {
      if (this.dataModel.contains(var1)) {
         if (this.layout.isExpanded(var1)) {
            this.layout.collapse(var1);
         } else {
            this.layout.expand(var1);
         }

         if (var2) {
            this.selectionModel.setSelection(Collections.singletonList(var1));
         }
      }
   }

   public Point convertToViewPort(Point var1) {
      return SwingUtilities.convertPoint(this.canvas, var1, this.scroll.getViewport());
   }

   public void printToConsole() {
      for (Object var2 : this.dataModel.getNodes()) {
         System.out.println(var2);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.canvas.requestFocusInWindow();
   }

   public void setCursor(Cursor var1) {
      this.canvas.setCursor(var1);
   }

   public void showContextMenu(JPopupMenu var1, Point var2) {
      var1.show(this.canvas, (int)(this.zoom * var2.x), (int)(this.zoom * var2.y));
   }

   public void showContextMenu(JPopupMenu var1, Object var2, Point var3) {
      Rectangle var4 = this.layout.getBounds(var2);
      if (var4 == null) {
         throw new IllegalArgumentException("node is not displayed in this graph");
      }

      var1.show(this.canvas, (int)(this.zoom * (var4.x + var3.x)), (int)(this.zoom * (var4.y + var3.y)));
   }

   private Point adjustPointForZoom(Point var1) {
      int var2 = var1.x;
      int var3 = var1.y;
      int var4 = (int)(var2 / this.zoom);
      int var5 = (int)(var3 / this.zoom);
      return new Point(var4, var5);
   }

   private MouseEvent translateZoom(MouseEvent var1) {
      Point var2 = this.adjustPointForZoom(var1.getPoint());
      return new MouseEvent(
         var1.getComponent(), var1.getID(), var1.getWhen(), var1.getModifiers(), var2.x, var2.y, var1.getClickCount(), var1.isPopupTrigger(), var1.getButton()
      );
   }

   private boolean isNodeAboveEdge(Object var1, GraphEdge var2) {
      Object var3 = var2.from();
      Object var4 = var2.to();
      if (var1 != var3 && var1 != var4) {
         for (Object var6 : this.layout.getZOrder()) {
            if (var6 == var3 || var6 == var4) {
               return false;
            }

            if (var6 == var1) {
               return true;
            }
         }

         throw new RuntimeException("Unexpected runtime exception error");
      } else {
         return true;
      }
   }

   public void setEdgeContextMenuProvider(EdgeContextMenuProvider var1) {
      this.edgeContextMenuProvider = var1;
   }

   @VisibleForTesting
   public final List<Object> listNodes() {
      return Lists.newArrayList(this.nodeUis.keySet());
   }

   private class ActionWrapper extends AbstractAction {
      private final Action action;

      public ActionWrapper(Action var2) {
         this.action = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (Graph2D.this.nodeEditor == null) {
            this.action.actionPerformed(var1);
         }
      }
   }

   private class Canvas extends JPanel {
      private final CellRendererPane rendererPane;
      private Dimension maxPrefSize;

      public Canvas() {
         this.setLayout(null);
         this.rendererPane = new CellRendererPane();
         this.maxPrefSize = new Dimension();
         this.add(this.rendererPane, "Center");
         this.setBackground(javax.swing.UIManager.getColor("Panel.background"));
         this.setOpaque(true);
         this.setFocusTraversalKeysEnabled(false);
         ToolTipManager.sharedInstance().registerComponent(this);
         new DropTarget(this, new Graph2D.Canvas.DropTargetImplementation());
      }

      public void resetMaxSize() {
         this.maxPrefSize = new Dimension();
         this.revalidate();
      }

      @Override
      public void paint(Graphics var1) {
         Graphics2D var2 = (Graphics2D)var1.create();

         try {
            var2.scale(Graph2D.this.zoom, Graph2D.this.zoom);
            super.paint(var2);
         } finally {
            var2.dispose();
         }
      }

      @Override
      protected void paintComponent(Graphics var1) {
         super.paintComponent(var1);
         Graphics2D var2 = (Graphics2D)var1;
         this.paintNodesAndEdges(var2);
      }

      private void paintNodesAndEdges(Graphics2D var1) {
         List var2 = Graph2D.this.layout.getZOrder();

         for (int var3 = var2.size() - 1; var3 >= 0; var3--) {
            Object var4 = var2.get(var3);
            Rectangle var5 = Graph2D.this.layout.getBounds(var4);
            boolean var6 = Graph2D.this.selectionModel.isSelected(var4);
            JComponent var7 = Graph2D.this.nodeUis.get(var4).getRenderer(var6);
            var7.setSize(var5.getSize());
            var7.doLayout();
            this.rendererPane.paintComponent(var1, var7, this, var5);
            this.paintEdgesForNode(var1, var4);
         }
      }

      private void paintEdgesForNode(Graphics2D var1, Object var2) {
         for (GraphEdge var4 : Graph2D.this.dataModel.getOutgoingEdges(var2)) {
            if (Graph2D.this.layout.shouldBePainted(var4)) {
               GraphEdgeUi2D var5 = Graph2D.this.edgeUis.get(var4);
               if (var5 != null) {
                  var5.paint(var1);
               } else {
                  System.err.println("No edge UI for edge: " + var4);
               }
            }
         }
      }

      @Override
      public String getToolTipText(MouseEvent var1) {
         MouseEvent var2 = Graph2D.this.translateZoom(var1);
         Point var3 = var2.getPoint();
         GraphNodeUi2D var4 = Graph2D.this.getNodeUiAt(var3);
         GraphEdgeUi2D var5 = Graph2D.this.getEdgeUiAt(var3);
         if (var4 == null && var5 == null) {
            return null;
         } else if (var4 != null && var5 != null) {
            return Graph2D.this.isNodeAboveEdge(var4.getNodeObject(), var5.getEdge()) ? this.getNodeUiToolTip(var4, var2) : var5.getToolTipText(var1);
         } else if (var4 == null && var5 != null) {
            return var5.getToolTipText(var1);
         } else {
            return var5 == null ? this.getNodeUiToolTip(var4, var2) : null;
         }
      }

      private String getNodeUiToolTip(GraphNodeUi2D var1, MouseEvent var2) {
         Point var3 = Graph2D.this.layout.getBounds(var1.getNodeObject()).getLocation();
         var2.translatePoint(-var3.x, -var3.y);
         return var1.getToolTipText(var2);
      }

      @Override
      public Dimension getPreferredSize() {
         Dimension var1 = this.getLayoutSize();
         this.checkMaxPrefSize(var1);
         this.checkMinimumSize(var1);
         return var1;
      }

      private Dimension getLayoutSize() {
         int var1 = 0;
         int var2 = 0;

         for (Object var4 : Graph2D.this.layout.getZOrder()) {
            Rectangle var5 = Graph2D.this.layout.getBounds(var4);
            int var6 = var5.x + var5.width;
            if (var6 > var1) {
               var1 = var6;
            }

            int var7 = var5.y + var5.height;
            if (var7 > var2) {
               var2 = var7;
            }
         }

         var1 += 5;
         var2 += 5;
         return new Dimension(var1, var2);
      }

      private void checkMaxPrefSize(Dimension var1) {
         if (var1.width > this.maxPrefSize.width) {
            this.maxPrefSize.width = var1.width;
         } else {
            var1.width = this.maxPrefSize.width;
         }

         if (var1.height > this.maxPrefSize.height) {
            this.maxPrefSize.height = var1.height;
         } else {
            var1.height = this.maxPrefSize.height;
         }
      }

      private void checkMinimumSize(Dimension var1) {
         Dimension var2 = this.getMinimumSize();
         if (var2 != null) {
            DimensionUtils.ensureMinimumSize(var1, var2.width, var2.height);
         }
      }

      private class DropTargetImplementation extends DropTargetAdapter {
         private Object currentTargetNode;
         private GraphEdge currentTargetEdge;

         private DropTargetImplementation() {
         }

         @Override
         public void dragEnter(DropTargetDragEvent var1) {
            this.checkCanvasDrop(var1);
         }

         @Override
         public void dragOver(DropTargetDragEvent var1) {
            Point var2 = Graph2D.this.adjustPointForZoom(var1.getLocation());
            Object var3 = Graph2D.this.getNodeAt(var2);
            GraphEdge var4 = Graph2D.this.getEdgeAt(var2);
            if (var3 == null && var4 == null) {
               this.reset();
               this.checkCanvasDrop(var1);
            } else if (var3 != null && var4 != null) {
               if (Graph2D.this.isNodeAboveEdge(var3, var4)) {
                  this.handleDragOverNode(var3, var1);
               } else {
                  this.handleDragOverEdge(var4, var1);
               }
            } else if (var3 != null) {
               this.handleDragOverNode(var3, var1);
            } else if (var4 != null) {
               this.handleDragOverNode(var4, var1);
            }
         }

         private void handleDragOverNode(Object var1, DropTargetDragEvent var2) {
            this.clearCurrentEdgeDropStatus();
            this.currentTargetEdge = null;
            if (var1 != this.currentTargetNode) {
               this.clearCurrentNodeDropStatus();
               this.checkNodeDrop(var1, var2);
               this.currentTargetNode = var1;
            }
         }

         private void handleDragOverEdge(GraphEdge var1, DropTargetDragEvent var2) {
            this.clearCurrentNodeDropStatus();
            this.currentTargetNode = null;
            if (var1 != this.currentTargetEdge) {
               this.clearCurrentEdgeDropStatus();
               this.checkEdgeDrop(var1, var2);
               this.currentTargetEdge = var1;
            }
         }

         private void checkNodeDrop(Object var1, DropTargetDragEvent var2) {
            GraphNodeUi2D var3 = Graph2D.this.getNodeUi(var1);
            if (var3 != null) {
               boolean var4 = Graph2D.this.dropHandler.isNodeDropAllowed(var1, var2);
               if (var4) {
                  var3.setDropStatus(DropStatus.ALLOWED);
                  var2.acceptDrag(1);
               } else {
                  var3.setDropStatus(DropStatus.DISALLOWED);
                  var2.rejectDrag();
               }
            }
         }

         private void checkEdgeDrop(GraphEdge var1, DropTargetDragEvent var2) {
            GraphEdgeUi2D var3 = Graph2D.this.getEdgeUi(var1);
            if (var3 != null) {
               boolean var4 = Graph2D.this.dropHandler.isEdgeDropAllowed(var1, var2);
               if (var4) {
                  var3.setDropStatus(DropStatus.ALLOWED);
                  var2.acceptDrag(1);
               } else {
                  var3.setDropStatus(DropStatus.DISALLOWED);
                  var2.rejectDrag();
               }
            }
         }

         private void checkCanvasDrop(DropTargetDragEvent var1) {
            if (Graph2D.this.dropHandler.isCanvasDropAllowed(var1)) {
               var1.acceptDrag(1);
            } else {
               var1.rejectDrag();
            }
         }

         @Override
         public void dragExit(DropTargetEvent var1) {
            this.reset();
         }

         @Override
         public void drop(DropTargetDropEvent var1) {
            boolean var2;
            if (this.currentTargetNode != null) {
               var2 = Graph2D.this.dropHandler.handleNodeDrop(this.currentTargetNode, var1);
            } else if (this.currentTargetEdge != null) {
               var2 = Graph2D.this.dropHandler.handleEdgeDrop(this.currentTargetEdge, var1);
            } else {
               var2 = Graph2D.this.dropHandler.handleCanvasDrop(var1);
            }

            var1.dropComplete(var2);
            if (var2) {
               Graph2D.this.getSelectionModel().setSelection(Graph2D.this.dropHandler.getNodeSelection());
               Canvas.this.requestFocus();
            }

            this.reset();
         }

         private void reset() {
            this.clearCurrentNodeDropStatus();
            this.clearCurrentEdgeDropStatus();
            this.currentTargetNode = null;
            this.currentTargetEdge = null;
         }

         private void clearCurrentNodeDropStatus() {
            if (this.currentTargetNode != null) {
               GraphNodeUi2D var1 = Graph2D.this.getNodeUi(this.currentTargetNode);
               if (var1 != null) {
                  var1.setDropStatus(DropStatus.NONE);
               }
            }
         }

         private void clearCurrentEdgeDropStatus() {
            if (this.currentTargetEdge != null) {
               GraphEdgeUi2D var1 = Graph2D.this.getEdgeUi(this.currentTargetEdge);
               if (var1 != null) {
                  var1.setDropStatus(DropStatus.NONE);
               }
            }
         }
      }
   }

   private class EditNodeAction extends AbstractAction implements GraphSelectionListener {
      public EditNodeAction() {
         this.updateEnabledState();
      }

      @Override
      public void selectionChanged(GraphSelectionEvent var1) {
         this.updateEnabledState();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         List var2 = Graph2D.this.selectionModel.getSelection();
         if (var2.size() == 1) {
            Object var3 = var2.get(0);
            Graph2D.this.editNode(var3);
         }
      }

      public void updateEnabledState() {
         boolean var1 = Graph2D.this.editable && Graph2D.this.selectionModel.getNumberOfSelectedObjects() == 1;
         this.setEnabled(var1);
      }

      public void installKeyShortCut() {
         Graph2D.this.addKeyBindingImpl(KeyUtils.F2, "edit_selected_node", this);
      }

      public void removeKeyShortCut() {
         Graph2D.this.canvas.getActionMap().remove("edit_selected_node");
      }
   }

   private class InternalInputListener implements GraphInputListener {
      private InternalInputListener() {
      }

      @Override
      public void mouseDragged(MouseEvent var1) {
         var1 = Graph2D.this.translateZoom(var1);

         for (GraphInputListener var3 : Graph2D.this.inputListeners) {
            var3.mouseDragged(var1);
         }
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
         var1 = Graph2D.this.translateZoom(var1);

         for (GraphInputListener var3 : Graph2D.this.inputListeners) {
            var3.mouseMoved(var1);
         }
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         var1 = Graph2D.this.translateZoom(var1);

         for (GraphInputListener var3 : Graph2D.this.inputListeners) {
            var3.mouseClicked(var1);
         }
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
         var1 = Graph2D.this.translateZoom(var1);

         for (GraphInputListener var3 : Graph2D.this.inputListeners) {
            var3.mouseEntered(var1);
         }
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         var1 = Graph2D.this.translateZoom(var1);

         for (GraphInputListener var3 : Graph2D.this.inputListeners) {
            var3.mouseExited(var1);
         }
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         Graph2D.this.canvas.requestFocusInWindow();
         var1 = Graph2D.this.translateZoom(var1);

         for (GraphInputListener var3 : Graph2D.this.inputListeners) {
            var3.mousePressed(var1);
         }
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         var1 = Graph2D.this.translateZoom(var1);

         for (GraphInputListener var3 : Graph2D.this.inputListeners) {
            var3.mouseReleased(var1);
         }
      }
   }

   private class ModelListener implements GraphModelListener, GraphSelectionListener, GraphLayoutListener, NodeSizeListener, NodeExpansionListener {
      private ModelListener() {
      }

      @Override
      public void nodeSizeChanged(GraphNodeUi2D var1, Dimension var2) {
         Graph2D.this.layout.nodeSizeChanged(var1, var2);
      }

      @Override
      public void modelChanged(GraphModelEvent var1) {
         this.synchronizeNodeUisWithModel();
         Graph2D.this.layout.dataModelChanged(var1);
      }

      @Override
      public void nodeCollapsed(NodeCollapsedEvent var1) {
         this.recreateExpandedOrCollapsedNode(var1.getGroupNode());
      }

      @Override
      public void nodeExpanded(final NodeExpandedEvent var1) {
         this.recreateExpandedOrCollapsedNode(var1.getGroupNode());
         List var2 = var1.getMemberNodes();
         if (!var2.isEmpty()) {
            for (Object var4 : var2) {
               if (!Graph2D.this.nodeUis.containsKey(var4)) {
                  Graph2D.this.createAndAddNodeUi(var4);
               }
            }

            Graph2D.this.createEdgeUisFromModel();
         }

         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Graph2D.this.ensureNodeIsVisible(var1.getGroupNode());
            }
         });
      }

      private void recreateExpandedOrCollapsedNode(Object var1) {
         GraphNodeUi2D var2 = Graph2D.this.nodeUis.remove(var1);
         if (var2 != null) {
            var2.dispose();
         }

         Graph2D.this.createAndAddNodeUi(var1);
         Graph2D.this.repaint();
      }

      private void synchronizeNodeUisWithModel() {
         for (GraphNodeUi2D var2 : Graph2D.this.nodeUis.values()) {
            var2.dispose();
         }

         Graph2D.this.nodeUis.clear();
         Graph2D.this.edgeUis.clear();

         for (Object var5 : Graph2D.this.dataModel.getNodes()) {
            Graph2D.this.createAndAddNodeUi(var5);
         }

         for (GraphEdge var6 : Graph2D.this.dataModel.getEdges()) {
            Graph2D.this.createUiForEdge(var6);
         }
      }

      @Override
      public void layoutChanged(GraphLayoutEvent var1) {
         this.updateCanvas();
      }

      @Override
      public void zOrderChanged(GraphLayoutEvent var1) {
         this.updateCanvas();
      }

      @Override
      public void nodesConnected(NodesConnectedEvent var1) {
         GraphEdge var2 = var1.getNewEdge();
         Graph2D.this.createUiForEdge(var2);
         Graph2D.this.layout.nodesConnected(var1);
         this.updateCanvas();
      }

      @Override
      public void nodesDisconnected(NodesDisconnectedEvent var1) {
         GraphEdge var2 = var1.getRemovedEdge();
         Graph2D.this.edgeUis.remove(var2);
         this.updateCanvas();
      }

      @Override
      public void nodesInserted(NodesInsertedEvent var1) {
         List var2 = var1.getInserted();

         for (Object var4 : var2) {
            Graph2D.this.createAndAddNodeUi(var4);
         }

         this.recreateEdgeUis();
         Graph2D.this.layout.nodesInserted(var1);
         Object var5 = var2.get(0);
         if (var5 != null) {
            Graph2D.this.getSelectionModel().setSelection(Collections.singletonList(var5));
         }
      }

      @Override
      public void nodesRemoved(NodesRemovedEvent var1) {
         for (Object var3 : var1.getRemoved()) {
            this.removeNodeUi(var3);
         }

         this.recreateEdgeUis();
         Graph2D.this.layout.nodesRemoved(var1);
      }

      private void removeNodeUi(Object var1) {
         GraphNodeUi2D var2 = Graph2D.this.nodeUis.remove(var1);
         if (var2 != null) {
            var2.dispose();
         } else {
            System.err.println("Already removed node: " + var1);
         }
      }

      @Override
      public void selectionChanged(GraphSelectionEvent var1) {
         this.updateCanvas();
      }

      private void recreateEdgeUis() {
         Graph2D.this.edgeUis.clear();

         for (GraphEdge var2 : Graph2D.this.dataModel.getEdges()) {
            Graph2D.this.createUiForEdge(var2);
         }
      }

      private void updateCanvas() {
         Graph2D.this.canvas.revalidate();
         Graph2D.this.canvas.repaint();
      }

      @Override
      public void nodeChanged(NodeChangedEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Graph2D.this.repaint();
            }
         });
      }
   }

   private class MouseEventDispatcher implements AWTEventListener, Disposable {
      public MouseEventDispatcher() {
         Toolkit.getDefaultToolkit().addAWTEventListener(this, 16L);
      }

      @Override
      public void dispose() {
         Toolkit.getDefaultToolkit().removeAWTEventListener(this);
      }

      @Override
      public void eventDispatched(AWTEvent var1) {
         if (var1.getSource() == Graph2D.this.canvas) {
            this.dispatchImpl((MouseEvent)var1);
         }
      }

      private void dispatchImpl(MouseEvent var1) {
         MouseEvent var2 = Graph2D.this.translateZoom(var1);
         Point var3 = var2.getPoint();
         GraphNodeUi2D var4 = Graph2D.this.getNodeUiAt(var3);
         GraphEdgeUi2D var5 = Graph2D.this.getEdgeUiAt(var3);
         if (var4 != null || var5 != null) {
            if (var4 != null && var5 != null) {
               if (Graph2D.this.isNodeAboveEdge(var4.getNodeObject(), var5.getEdge())) {
                  this.dispatchEventToNode(var4, var2);
               } else {
                  this.dispatchEventToEdge(var5, var2);
               }
            } else if (var4 == null) {
               this.dispatchEventToEdge(var5, var2);
            } else if (var5 == null) {
               this.dispatchEventToNode(var4, var2);
            }

            if (var2.isConsumed()) {
               var1.consume();
            }
         }
      }

      private void dispatchEventToNode(GraphNodeUi2D var1, MouseEvent var2) {
         Point var3 = Graph2D.this.layout.getBounds(var1.getNodeObject()).getLocation();
         var2.translatePoint(-var3.x, -var3.y);
         if (var1.handleMouseEvent(var2)) {
            var2.consume();
         } else {
            var2.translatePoint(var3.x, var3.y);
         }
      }

      private void dispatchEventToEdge(GraphEdgeUi2D var1, MouseEvent var2) {
         if (var1.handleMouseEvent(var2)) {
            var2.consume();
         }
      }
   }

   private abstract class MouseLocationBasedAction extends AbstractAction {
      private MouseLocationBasedAction() {
      }

      protected final Point getCurrentLocation() {
         PointerInfo var1 = MouseInfo.getPointerInfo();
         Point var2 = var1.getLocation();
         SwingUtilities.convertPointFromScreen(var2, Graph2D.this.canvas);
         return var2.x > 0 && var2.y > 0 ? Graph2D.this.adjustPointForZoom(var2) : null;
      }
   }

   private class MousePositionInfoAction extends Graph2D.MouseLocationBasedAction {
      private MousePositionInfoAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Point var2 = this.getCurrentLocation();
         if (var2 != null) {
            UiProvider var3 = this.createInfoUi(var2);
            UiProviderDialogDisplayer.display(Graph2D.this.getWindowOwner(), var3, "Mouse Position Info");
         }
      }

      private UiProvider createInfoUi(Point var1) {
         BasicTwoColumnLabelDisplayer var2 = new BasicTwoColumnLabelDisplayer();
         var2.addRow("Current mouse position: ", new JLabel("[x = " + var1.x + ", y = " + var1.y + "]"));
         var2.addRow("Active graph element: ", this.getActiveGraphElementInfo(var1));
         return var2;
      }

      private JLabel getActiveGraphElementInfo(Point var1) {
         GraphUiElement var2 = this.getActiveElement(var1);
         return var2 != null ? new JLabel(var2.toString()) : new JLabel("[none]");
      }

      private GraphUiElement getActiveElement(Point var1) {
         GraphNodeUi2D var2 = Graph2D.this.getNodeUiAt(var1);
         GraphEdgeUi2D var3 = Graph2D.this.getEdgeUiAt(var1);
         if (var2 == null && var3 == null) {
            return null;
         } else if (var2 != null && var3 != null) {
            return Graph2D.this.isNodeAboveEdge(var2.getNodeObject(), var3.getEdge()) ? var2 : var3;
         } else if (var2 == null && var3 != null) {
            return var3;
         } else {
            return var3 == null ? var2 : null;
         }
      }
   }

   private class NodeInfoAction extends Graph2D.MouseLocationBasedAction {
      private NodeInfoAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Point var2 = this.getCurrentLocation();
         if (var2 != null) {
            this.processPoint(var2);
         }
      }

      private void processPoint(Point var1) {
         GraphNodeUi2D var2 = Graph2D.this.getNodeUiAt(var1);
         if (var2 != null) {
            this.showNodeInfo(var2);
         }
      }

      private void showNodeInfo(GraphNodeUi2D var1) {
         BasicTwoColumnLabelDisplayer var2 = new BasicTwoColumnLabelDisplayer();
         Rectangle var3 = Graph2D.this.layout.getBounds(var1.getNodeObject());
         Object var4 = var1.getNodeObject();
         var2.addRow("Node: ", var4 != null ? var4.toString() : "[null]");
         var2.addRow("Bounds: ", String.valueOf(var3));
         var2.addRow("Minimum size: ", String.valueOf(var1.getMinimumSize()));
         var2.addRow("Preferred size: ", String.valueOf(var1.getPreferredSize()));

         for (GraphEdge var6 : Graph2D.this.getModel().getIncomingEdges(var4)) {
            var2.addRow("Incoming route: ", var6.toString());
         }

         for (GraphEdge var8 : Graph2D.this.getModel().getOutgoingEdges(var4)) {
            var2.addRow("Outgoing route: ", var8.toString());
         }

         UiProviderDialogDisplayer.display(Graph2D.this.getWindowOwner(), var2, "Node Info");
      }
   }

   private class SelectionActionUpdater implements GraphSelectionListener {
      private SelectionActionUpdater() {
      }

      @Override
      public void selectionChanged(GraphSelectionEvent var1) {
         OrderedSelection var2 = SelectionFactory.newOrderedSelection(Graph2D.this.selectionModel.getSelection());

         for (SelectionAction var4 : Graph2D.this.selectionActions.values()) {
            var4.setSelection(var2);
         }
      }
   }
}

