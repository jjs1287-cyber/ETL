package org.etlforge.ui.widget.list;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.lang.ArrayUtils;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.collections.Pair;
import org.etlforge.lang.PrimitiveConversions;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionFactory;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.event.ChangeListeners;
import org.etlforge.ui.event.DoubleClick;
import org.etlforge.ui.graphics.BezierCurve;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionListeners;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;

public final class ConnectedLists implements UiProvider, SelectionSource {
   private final JList leftList;
   private final ConnectedLists.Renderer leftRenderer;
   private final ConnectedLists.Caption leftCaption;
   private Function<String, ListFilter> leftFilterFunction;
   private final JList rightList;
   private final ConnectedLists.Renderer rightRenderer;
   private final ConnectedLists.Caption rightCaption;
   private Function<String, ListFilter> rightFilterFunction;
   private final JScrollPane leftScroll;
   private final JScrollPane rightScroll;
   private final JComponent center;
   private final ConnectedLists.Data leftData;
   private final ConnectedLists.Data rightData;
   private JComponent ui;
   private final ChangeListeners changeListeners;
   private final SelectionListeners selectionListeners;
   private ConnectedLists.Mode mode = ConnectedLists.Mode.MANY_TO_MANY;

   public ConnectedLists(ListModel var1, ListModel var2) {
      this.leftData = new ConnectedLists.Data(var1);
      this.rightData = new ConnectedLists.Data(var2);
      this.leftList = this.createList(this.leftData, new ConnectedLists.LeftTransferHandler());
      this.leftCaption = new ConnectedLists.Caption(this.leftList);
      this.leftRenderer = new ConnectedLists.Renderer(this.leftData);
      this.leftList.setCellRenderer(this.leftRenderer);
      this.leftScroll = new JScrollPane(this.leftList);
      this.rightList = this.createList(this.rightData, new ConnectedLists.RightTransferHandler());
      this.rightCaption = new ConnectedLists.Caption(this.rightList);
      this.rightRenderer = new ConnectedLists.Renderer(this.rightData);
      this.rightList.setCellRenderer(this.rightRenderer);
      this.installListActions();
      this.rightScroll = new JScrollPane(this.rightList);
      this.center = new ConnectedLists.Center();
      this.installCenterRepainter();
      this.changeListeners = new ChangeListeners();
      this.selectionListeners = new SelectionListeners();
      ConnectedLists.InternalSelectionListener var3 = new ConnectedLists.InternalSelectionListener();
      this.leftList.addListSelectionListener(var3);
      this.rightList.addListSelectionListener(var3);
   }

   private JList createList(ConnectedLists.Data var1, TransferHandler var2) {
      JList var3 = new JList(var1.getListModel());
      var3.setSelectionMode(0);
      var3.setDragEnabled(true);
      var3.setDropMode(DropMode.ON);
      var3.setTransferHandler(var2);
      return var3;
   }

   private void installListActions() {
      Action var1 = ActionFactory.forKey(KeyUtils.ENTER, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ConnectedLists.this.connectSelectedRows();
         }
      });
      ActionKeyBinding.install(var1, this.leftList);
      ActionKeyBinding.install(var1, this.rightList);
      ConnectedLists.LeftDeleteAction var2 = new ConnectedLists.LeftDeleteAction(this.leftList);
      ActionKeyBinding.install(var2, this.leftList);
      ConnectedLists.RightDeleteAction var3 = new ConnectedLists.RightDeleteAction(this.rightList);
      ActionKeyBinding.install(var3, this.rightList);
      DoubleClick.on(this.leftList).or(this.rightList).toRun(new Runnable() {
         @Override
         public void run() {
            ConnectedLists.this.connectSelectedRows();
         }
      });
   }

   private void installCenterRepainter() {
      ConnectedLists.CenterRepaintTrigger var1 = new ConnectedLists.CenterRepaintTrigger();
      this.leftList.getSelectionModel().addListSelectionListener(var1);
      this.leftScroll.getVerticalScrollBar().addAdjustmentListener(var1);
      this.rightList.getSelectionModel().addListSelectionListener(var1);
      this.rightScroll.getVerticalScrollBar().addAdjustmentListener(var1);
   }

   private void repaint() {
      if (this.ui != null) {
         this.ui.repaint();
      }
   }

   public void replaceModels(ListModel var1, ListModel var2) {
      this.leftData.setModel(var1);
      this.leftList.setModel(this.leftData.getListModel());
      this.rightData.setModel(var2);
      this.rightList.setModel(this.rightData.getListModel());
      this.fireChange();
      this.repaint();
   }

   public ListModel getLeftModel() {
      return this.leftData.getListModel();
   }

   public ListModel getRightModel() {
      return this.rightData.getListModel();
   }

   public void setSelectionMode(ListSelectionMode var1) {
      var1.applyTo(this.leftList);
      var1.applyTo(this.rightList);
   }

   public void setMode(ConnectedLists.Mode var1) {
      this.mode = var1;
   }

   public List<Pair<Object, Object>> getConnections() {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.leftData.connections()) {
         var1.add(Pair.of(var3));
      }

      return var1;
   }

   public int getConnectionCount() {
      return this.leftData.getConnectionCount();
   }

   public boolean areConnected(Object var1, Object var2) {
      return this.leftData.areConnected(var1, var2);
   }

   public boolean isLeftObjectConnected(Object var1) {
      return this.leftData.isConnected(var1);
   }

   public boolean isRightObjectConnected(Object var1) {
      return this.rightData.isConnected(var1);
   }

   public boolean canConnect(Object var1, Object var2) {
      boolean var3 = this.leftData.contains(var1) && this.rightData.contains(var2);
      if (!var3) {
         return false;
      }

      switch (this.mode) {
         case ONE_TO_ONE:
            return !this.leftData.isConnected(var1) && !this.rightData.isConnected(var2);
         case ONE_TO_MANY:
            return !this.rightData.isConnected(var2);
         case MANY_TO_ONE:
            return !this.leftData.isConnected(var1);
         case MANY_TO_MANY:
            return true;
         default:
            throw new RuntimeException("Unexpected mode: " + this.mode);
      }
   }

   public void addConnection(Object var1, Object var2) {
      this.leftData.addConnection(var1, var2);
      this.rightData.addConnection(var2, var1);
      this.leftList.clearSelection();
      this.rightList.clearSelection();
      this.fireChange();
      this.repaint();
   }

   public void removeConnection(Object var1, Object var2) {
      if (this.leftData.removeConnection(var1, var2)) {
         this.rightData.removeConnection(var2, var1);
         this.leftList.clearSelection();
         this.rightList.clearSelection();
         this.fireChange();
         this.repaint();
      }
   }

   public void removeAllConnections() {
      boolean var1 = this.leftData.removeAllConnections();
      boolean var2 = this.rightData.removeAllConnections();
      if (var1 || var2) {
         this.leftList.clearSelection();
         this.rightList.clearSelection();
         this.fireChange();
         this.repaint();
      }
   }

   private void fireChange() {
      this.changeListeners.call().stateChanged(new ChangeEvent(this));
   }

   public void setLeftFilter(ListFilter var1) {
      this.leftData.setFilter(var1);
      this.repaint();
   }

   public void setRightFilter(ListFilter var1) {
      this.rightData.setFilter(var1);
      this.repaint();
   }

   public void setLeftRenderer(ListCellRenderer var1) {
      this.leftRenderer.delegate = var1;
      this.leftList.repaint();
   }

   public void setRightRenderer(ListCellRenderer var1) {
      this.rightRenderer.delegate = var1;
      this.rightList.repaint();
   }

   public void setLeftLabel(String var1) {
      this.leftCaption.setLabel(var1);
   }

   public void setRightLabel(String var1) {
      this.rightCaption.setLabel(var1);
   }

   public void setLabels(String var1, String var2) {
      this.setLeftLabel(var1);
      this.setRightLabel(var2);
   }

   public void setLabelStyle(TextStyle var1) {
      this.leftCaption.setLabelStyle(var1);
      this.rightCaption.setLabelStyle(var1);
   }

   public void addLeftButtons(JButton... var1) {
      this.leftCaption.addButtons(var1);
   }

   public void addRightButtons(JButton... var1) {
      this.rightCaption.addButtons(var1);
   }

   public void installFilterFields(Function<String, ListFilter> var1, Function<String, ListFilter> var2) {
      this.leftFilterFunction = var1;
      this.rightFilterFunction = var2;
   }

   public void setListModels(ListModel var1, ListModel var2, boolean var3) {
      this.leftData.setModel(var1, var3);
      this.rightData.setModel(var2, var3);
      this.leftList.setModel(this.leftData.getListModel());
      this.rightList.setModel(this.rightData.getListModel());
      if (!var3) {
         this.fireChange();
      }

      this.repaint();
   }

   public void setVisibleRowCount(int var1) {
      this.leftList.setVisibleRowCount(var1);
      this.rightList.setVisibleRowCount(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      ConnectedLists.Layout var2 = new ConnectedLists.Layout();
      this.ui = var2.getUi();
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.leftList.requestFocusInWindow();
   }

   public void addChangeListener(ChangeListener var1) {
      this.changeListeners.add(var1);
   }

   public void removeChangeListener(ChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   public ConnectedListsSelection getSelection() {
      return new ConnectedListsSelection(this, this.leftList.getSelectedValues(), this.rightList.getSelectedValues());
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionListeners.remove(var1);
   }

   private void connectSelectedRows() {
      Object var1 = this.leftData.toObject(this.leftList.getSelectedIndex());
      if (var1 != null) {
         Object var2 = this.rightData.toObject(this.rightList.getSelectedIndex());
         if (var2 != null && this.canConnect(var1, var2)) {
            this.addConnection(var1, var2);
         }
      }
   }

   private static class Caption {
      private final JList list;
      private KongaLabel label;
      private TextStyle style;
      private final List<JButton> buttons = Lists.newArrayList();

      public Caption(JList var1) {
         this.list = var1;
      }

      public void setLabel(String var1) {
         if (this.label == null) {
            this.label = KongaLabel.withMnemonic(var1);
            if (this.style != null) {
               this.style.makeOver(this.label);
            }

            this.label.setLabelForList(this.list);
         } else {
            this.label.setMnemonicText(var1);
         }
      }

      public void setLabelStyle(TextStyle var1) {
         if (this.label != null) {
            var1.makeOver(this.label);
         } else {
            this.style = var1;
         }
      }

      public void addButtons(JButton... var1) {
         KongaListUtils.add(this.buttons, var1);
      }

      public boolean isEmpty() {
         return this.label == null && this.buttons.isEmpty();
      }

      public JComponent layout() {
         BoxBuilder var1 = BoxBuilder.horizontal();
         var1.add(this.label == null ? " " : this.label).glue();
         if (!this.buttons.isEmpty()) {
            var1.rigid(5).addAllWithSpace(5, this.buttons);
         }

         return var1.container();
      }
   }

   private class Center extends JComponent {
      private final boolean STRAIGHT_LINES = false;

      public Center() {
         this.setPreferredSize(new Dimension(64, 32));
         this.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport var1) {
               return var1.isDataFlavorSupported(DataFlavor.stringFlavor);
            }
         });
      }

      @Override
      public void paint(Graphics var1) {
         super.paint(var1);
         Graphics2D var2 = (Graphics2D)var1;
         Color var3 = var2.getColor();
         var2.setColor(Color.BLACK);
         RenderingHints var4 = var2.getRenderingHints();
         UiUtils.makeAntiAliased(var2);
         Stroke var5 = var2.getStroke();

         for (Entry var7 : ConnectedLists.this.leftData.connections()) {
            this.connect(var7, var2);
         }

         var2.setStroke(var5);
         var2.setRenderingHints(var4);
         var2.setColor(var3);
      }

      private void connect(Entry<Object, Object> var1, Graphics2D var2) {
         int var3 = ConnectedLists.this.leftData.toIndex(var1.getKey());
         if (var3 != -1) {
            int var4 = ConnectedLists.this.rightData.toIndex(var1.getValue());
            if (var4 != -1) {
               int var5 = this.getY(ConnectedLists.this.leftList, ConnectedLists.this.leftScroll, var3);
               int var6 = this.getY(ConnectedLists.this.rightList, ConnectedLists.this.rightScroll, var4);
               int var7 = this.getLineWidth(var3, var4);
               BasicStroke var8 = new BasicStroke(var7);
               var2.setStroke(var8);
               this.drawLine(var2, var5, var6);
            }
         }
      }

      private int getLineWidth(int var1, int var2) {
         return 1;
      }

      private int getY(JList var1, JScrollPane var2, int var3) {
         Rectangle var4 = var1.getCellBounds(var3, var3);
         return var4.y + var4.height / 2 - var2.getViewport().getViewPosition().y;
      }

      private void drawLine(Graphics2D var1, int var2, int var3) {
         if (var2 == var3) {
            var1.drawLine(0, var2, this.getWidth(), var3);
         } else {
            BezierCurve var4 = new BezierCurve(new int[]{0, this.getWidth() / 2, this.getWidth() / 2, this.getWidth()}, new int[]{var2, var2, var3, var3});
            var4.paint(var1);
         }
      }
   }

   private class CenterRepaintTrigger implements AdjustmentListener, ListSelectionListener {
      private CenterRepaintTrigger() {
      }

      @Override
      public void adjustmentValueChanged(AdjustmentEvent var1) {
         ConnectedLists.this.center.repaint();
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting()) {
            ConnectedLists.this.center.repaint();
         }
      }
   }

   private static class Data {
      private FilteredListModel filteredListModel;
      private ListFilter filter;
      private Map<Object, Integer> objectToIndex;
      private Multimap<Object, Object> connections;

      public Data(ListModel var1) {
         this.setModel(var1);
      }

      public void setModel(ListModel var1) {
         this.filteredListModel = new FilteredListModel(var1);
         this.connections = HashMultimap.create();
         this.reIndex();
      }

      public ListModel getListModel() {
         return this.filteredListModel;
      }

      public void setModel(ListModel var1, boolean var2) {
         this.filteredListModel = new FilteredListModel(var1);
         if (this.filter != null) {
            this.filteredListModel.addFilter(this.filter);
         }

         this.reIndex();
         if (!var2) {
            this.connections = HashMultimap.create();
         }
      }

      public void setFilter(ListFilter var1) {
         if (this.filter != null) {
            this.filteredListModel.removeFilter(this.filter);
         }

         if (var1 != null) {
            this.filteredListModel.addFilter(var1);
         }

         this.reIndex();
         this.filter = var1;
      }

      private void reIndex() {
         this.objectToIndex = HashBiMap.create(this.filteredListModel.getSize());

         for (int var1 = 0; var1 < this.filteredListModel.getSize(); var1++) {
            Object var2 = this.filteredListModel.getElementAt(var1);
            this.objectToIndex.put(var2, var1);
         }
      }

      public Object toObject(int var1) {
         return this.filteredListModel.getElementAt(var1);
      }

      public int toIndex(Object var1) {
         Integer var2 = this.objectToIndex.get(var1);
         return var2 == null ? -1 : var2;
      }

      public boolean contains(Object var1) {
         return this.objectToIndex.containsKey(var1);
      }

      public boolean areConnected(Object var1, Object var2) {
         Collection var3 = this.connections.get(var1);
         return var3.contains(var2);
      }

      public void addConnection(Object var1, Object var2) {
         Preconditions.checkArgument(this.objectToIndex.containsKey(var1));
         this.connections.put(var1, var2);
      }

      public boolean removeAllConnections() {
         if (this.connections.isEmpty()) {
            return false;
         }

         this.connections.clear();
         return true;
      }

      public boolean removeConnection(Object var1, Object var2) {
         return this.connections.remove(var1, var2);
      }

      public Collection<Object> removeAllConnections(Object var1) {
         return this.connections.removeAll(var1);
      }

      public boolean isConnected(Object var1) {
         return this.connections.containsKey(var1);
      }

      public Iterable<Entry<Object, Object>> connections() {
         return this.connections.entries();
      }

      public Collection<Object> getOppositeObjects(Object var1) {
         return this.connections.get(var1);
      }

      public int getConnectionCount() {
         return this.connections.entries().size();
      }
   }

   private abstract class DeleteAction extends AbstractAction {
      private final JList list;
      private final ConnectedLists.Data from;
      private final ConnectedLists.Data to;

      public DeleteAction(JList var2, ConnectedLists.Data var3, ConnectedLists.Data var4) {
         this.list = var2;
         this.from = var3;
         this.to = var4;
         this.putValue("AcceleratorKey", KeyUtils.DELETE);
      }

      @Override
      public final void actionPerformed(ActionEvent var1) {
         int var2 = this.list.getSelectedIndex();
         if (var2 >= 0) {
            this.handleDelete(var2);
         }
      }

      private void handleDelete(int var1) {
         Object var2 = this.from.toObject(var1);
         if (var2 != null) {
            Collection var3 = this.from.removeAllConnections(var2);
            if (var3 != null) {
               for (Object var5 : var3) {
                  this.to.removeConnection(var5, var2);
               }

               ConnectedLists.this.fireChange();
               ConnectedLists.this.repaint();
            }
         }
      }
   }

   private abstract class DragAndDropBase extends TransferHandler {
      private final int sign;

      public DragAndDropBase(int var2) {
         this.sign = var2;
      }

      @Override
      public int getSourceActions(JComponent var1) {
         return 1073741825;
      }

      @Override
      public final boolean canImport(TransferSupport var1) {
         if (var1.isDataFlavorSupported(DraggedIndex.FLAVOR)) {
            try {
               int var2 = this.getEncodedIndex(var1);
               if (this.isFromOppositeList(var2)) {
                  int var3 = this.getDropIndex(var1);
                  if (var3 >= 0) {
                     Object var4 = this.getDraggedObject(this.decodeDraggedIndex(var2));
                     Object var5 = this.getDropTarget(var3);
                     return this.canImport(var4, var5);
                  }
               }
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }

         return false;
      }

      private boolean isFromOppositeList(int var1) {
         return var1 > 0 ? this.sign < 0 : this.sign > 0;
      }

      protected final int getDropIndex(TransferSupport var1) {
         JList.DropLocation var2 = (JList.DropLocation)var1.getDropLocation();
         return var2.getIndex();
      }

      protected abstract Object getDraggedObject(int var1);

      protected abstract Object getDropTarget(int var1);

      protected abstract boolean canImport(Object var1, Object var2);

      private Integer getEncodedIndex(TransferSupport var1) throws UnsupportedFlavorException, IOException {
         return (Integer)var1.getTransferable().getTransferData(DraggedIndex.FLAVOR);
      }

      @Override
      protected final Transferable createTransferable(JComponent var1) {
         int var2 = ((JList)var1).getSelectedIndex();
         if (var2 >= 0 && this.isDraggable(var2)) {
            int var3 = this.encodeDraggedIndex(var2);
            String var4 = ((JList)var1).getModel().getElementAt(var2).toString();
            return new DraggedIndex(var3, var4);
         } else {
            return null;
         }
      }

      protected abstract boolean isDraggable(int var1);

      private int encodeDraggedIndex(int var1) {
         return this.sign * (var1 + 1);
      }

      @Override
      public final boolean importData(TransferSupport var1) {
         if (var1.isDataFlavorSupported(DraggedIndex.FLAVOR)) {
            try {
               int var2 = this.getDraggedIndex(var1);
               int var3 = this.getDropIndex(var1);
               this.handleDrop(var3, var2);
               return true;
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }

         return false;
      }

      private int getDraggedIndex(TransferSupport var1) throws UnsupportedFlavorException, IOException {
         int var2 = this.getEncodedIndex(var1);
         return this.decodeDraggedIndex(var2);
      }

      private int decodeDraggedIndex(int var1) {
         return Math.abs(var1) - 1;
      }

      protected abstract void handleDrop(int var1, int var2);
   }

   private class InternalSelectionListener implements ListSelectionListener {
      private boolean blocked;

      private InternalSelectionListener() {
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting() && !this.blocked) {
            this.checkOppositeSelection((JList)var1.getSource());
            ConnectedListsSelection var2 = ConnectedLists.this.getSelection();
            ConnectedLists.this.selectionListeners.call().selectionChanged(new SelectionChangedEvent(ConnectedLists.this, var2));
         }
      }

      private void checkOppositeSelection(JList var1) {
         Object[] var2 = var1.getSelectedValues();
         this.blocked = true;
         ArrayList var3 = Lists.newArrayList();

         for (Object var7 : var2) {
            ConnectedLists.Data var8 = var1 == ConnectedLists.this.leftList ? ConnectedLists.this.leftData : ConnectedLists.this.rightData;
            var3.addAll(var8.getOppositeObjects(var7));
         }

         JList var9 = var1 == ConnectedLists.this.leftList ? ConnectedLists.this.rightList : ConnectedLists.this.leftList;
         int[] var10 = this.toIndices(var3, var9.getModel());
         if (var10.length > 0) {
            int[] var11 = var9.getSelectedIndices();
            if (var11.length == 0) {
               var9.setSelectedIndices(var10);
               var9.ensureIndexIsVisible(var10[0]);
            } else {
               int[] var12 = ArrayUtils.addAll(var11, var10);
               Arrays.sort(var12);
               var9.setSelectedIndices(var12);
            }
         }

         this.blocked = false;
      }

      private int[] toIndices(List<Object> var1, ListModel var2) {
         ArrayList var3 = Lists.newArrayList();

         for (Object var5 : var1) {
            int var6 = this.indexOf(var5, var2);
            if (var6 >= 0) {
               var3.add(var6);
            }
         }

         Collections.sort(var3);
         return PrimitiveConversions.toIntArray(var3);
      }

      private int indexOf(Object var1, ListModel var2) {
         for (int var3 = 0; var3 < var2.getSize(); var3++) {
            if (var2.getElementAt(var3) == var1) {
               return var3;
            }
         }

         return -1;
      }
   }

   private class Layout {
      private final NColumnPanel grid = new NColumnPanel(3);

      public Layout() {
         this.grid.setXWeights(0.4, 0.2, 0.4);
         this.layoutCaptions();
         this.layoutLists();
         this.layoutFilterFields();
      }

      private void layoutCaptions() {
         if (!ConnectedLists.this.leftCaption.isEmpty() || !ConnectedLists.this.rightCaption.isEmpty()) {
            JComponent var1 = ConnectedLists.this.leftCaption.layout();
            JComponent var2 = ConnectedLists.this.rightCaption.layout();
            this.makeSameWidth(var1, var2);
            this.grid.setFills(GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL);
            this.grid.setInsets(0, 0, 2, 0);
            this.grid.addRow(var1, null, var2);
         }
      }

      private void makeSameWidth(JComponent var1, JComponent var2) {
         Dimension var3 = var1.getPreferredSize();
         Dimension var4 = var2.getPreferredSize();
         int var5 = Math.max(var3.width, var4.width);
         var3.width = var5;
         var1.setPreferredSize(var3);
         var4.width = var5;
         var2.setPreferredSize(var4);
      }

      private void layoutLists() {
         this.makeSameWidth(ConnectedLists.this.leftScroll, ConnectedLists.this.rightScroll);
         this.grid.setInsets(0, 0, 0, 0);
         this.grid.setYWeights(1.0, 1.0, 1.0);
         this.grid.setFills(GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.BOTH);
         this.grid.addRow(ConnectedLists.this.leftScroll, ConnectedLists.this.center, ConnectedLists.this.rightScroll);
      }

      private void layoutFilterFields() {
         if (ConnectedLists.this.leftFilterFunction != null || ConnectedLists.this.rightFilterFunction != null) {
            this.grid.setYWeights(0.0, 0.0, 0.0);
            this.grid.setFills(GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL);
            this.grid.addRow(this.getLeftFilterField(), null, this.getRightFilterField());
         }
      }

      private UiProvider getLeftFilterField() {
         if (ConnectedLists.this.leftFilterFunction == null) {
            return null;
         }

         ListFilterField var1 = this.createFilterField(ConnectedLists.this.leftFilterFunction);
         var1.setCallback(new Receiver<ListFilter>() {
            public void handle(ListFilter var1) {
               ConnectedLists.this.setLeftFilter(var1);
            }
         });
         return var1;
      }

      private ListFilterField createFilterField(Function<String, ListFilter> var1) {
         ListFilterField var2 = new ListFilterField(var1);
         var2.setTextWhenEmpty("Search");
         return var2;
      }

      private UiProvider getRightFilterField() {
         if (ConnectedLists.this.rightFilterFunction == null) {
            return null;
         }

         ListFilterField var1 = this.createFilterField(ConnectedLists.this.rightFilterFunction);
         var1.setCallback(new Receiver<ListFilter>() {
            public void handle(ListFilter var1) {
               ConnectedLists.this.setRightFilter(var1);
            }
         });
         return var1;
      }

      public JComponent getUi() {
         return this.grid.container();
      }
   }

   private class LeftDeleteAction extends ConnectedLists.DeleteAction {
      public LeftDeleteAction(JList var2) {
         super(var2, ConnectedLists.this.leftData, ConnectedLists.this.rightData);
      }
   }

   private class LeftTransferHandler extends ConnectedLists.DragAndDropBase {
      public LeftTransferHandler() {
         super(1);
      }

      @Override
      protected void handleDrop(int var1, int var2) {
         Object var3 = ConnectedLists.this.leftData.toObject(var1);
         if (var3 != null) {
            Object var4 = ConnectedLists.this.rightData.toObject(var2);
            if (var4 != null) {
               ConnectedLists.this.addConnection(var3, var4);
            }
         }
      }

      @Override
      protected boolean isDraggable(int var1) {
         if (ConnectedLists.this.mode != ConnectedLists.Mode.ONE_TO_MANY && ConnectedLists.this.mode != ConnectedLists.Mode.MANY_TO_MANY) {
            Object var2 = ConnectedLists.this.leftData.toObject(var1);
            return !ConnectedLists.this.leftData.isConnected(var2);
         } else {
            return true;
         }
      }

      @Override
      protected Object getDraggedObject(int var1) {
         return ConnectedLists.this.rightData.toObject(var1);
      }

      @Override
      protected Object getDropTarget(int var1) {
         return ConnectedLists.this.leftData.toObject(var1);
      }

      @Override
      protected boolean canImport(Object var1, Object var2) {
         return ConnectedLists.this.canConnect(var2, var1);
      }
   }

   public enum Mode {
      ONE_TO_ONE,
      ONE_TO_MANY,
      MANY_TO_ONE,
      MANY_TO_MANY;
   }

   private static class Renderer implements ListCellRenderer {
      private final ConnectedLists.Data data;
      public ListCellRenderer delegate;

      public Renderer(ConnectedLists.Data var1) {
         this.data = var1;
         this.delegate = new DefaultListCellRenderer();
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         Component var6 = this.delegate.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var4) {
            var6.setBackground(var1.getSelectionBackground());
         } else if (this.data.isConnected(var2)) {
            var6.setBackground(Color.LIGHT_GRAY);
         }

         return var6;
      }
   }

   private class RightDeleteAction extends ConnectedLists.DeleteAction {
      public RightDeleteAction(JList var2) {
         super(var2, ConnectedLists.this.rightData, ConnectedLists.this.leftData);
      }
   }

   private class RightTransferHandler extends ConnectedLists.DragAndDropBase {
      public RightTransferHandler() {
         super(-1);
      }

      @Override
      protected void handleDrop(int var1, int var2) {
         Object var3 = ConnectedLists.this.leftData.toObject(var2);
         if (var3 != null) {
            Object var4 = ConnectedLists.this.rightData.toObject(var1);
            if (var4 != null) {
               ConnectedLists.this.addConnection(var3, var4);
            }
         }
      }

      @Override
      protected boolean isDraggable(int var1) {
         if (ConnectedLists.this.mode != ConnectedLists.Mode.MANY_TO_ONE && ConnectedLists.this.mode != ConnectedLists.Mode.MANY_TO_MANY) {
            Object var2 = ConnectedLists.this.rightData.toObject(var1);
            return !ConnectedLists.this.rightData.isConnected(var2);
         } else {
            return true;
         }
      }

      @Override
      protected Object getDraggedObject(int var1) {
         return ConnectedLists.this.leftData.toObject(var1);
      }

      @Override
      protected Object getDropTarget(int var1) {
         return ConnectedLists.this.rightData.toObject(var1);
      }

      @Override
      protected boolean canImport(Object var1, Object var2) {
         return ConnectedLists.this.canConnect(var1, var2);
      }
   }
}
