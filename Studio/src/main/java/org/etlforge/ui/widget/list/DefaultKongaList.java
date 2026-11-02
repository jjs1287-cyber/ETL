package org.etlforge.ui.widget.list;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSourceWidgetSupport;
import org.etlforge.ui.util.UiUtils;

public class DefaultKongaList implements KongaList {
   private final JList list;
   private JLabel label;
   private final DefaultKongaList.SelectionHandler selectionHandler = new DefaultKongaList.SelectionHandler();
   private final SelectionSourceWidgetSupport support;
   private boolean useScrolling = true;
   private SelectionConverter selectionConverter = SelectionConverter.TRIVIAL;
   private boolean isTransient;
   private MouseMotionListSelector mouseMotionSelector;

   public DefaultKongaList() {
      this(null);
   }

   public DefaultKongaList(JList var1) {
      if (var1 == null) {
         var1 = new JList();
      }

      this.list = var1;
      var1.getModel().addListDataListener(this.selectionHandler);
      this.support = new SelectionSourceWidgetSupport(this, var1);
      var1.getSelectionModel().addListSelectionListener(this.selectionHandler);
      ListUtils.installCyclicKeyNavigation(var1);
      var1.addMouseListener(new DefaultKongaList.ContextMenuItemSelector());
      var1.addPropertyChangeListener("model", new DefaultKongaList.NewModelListener());
   }

   public int getSize() {
      return this.getModel().getSize();
   }

   public void setCellRenderer(ListCellRenderer var1) {
      this.list.setCellRenderer(var1);
   }

   public void setVisibleRowCount(int var1) {
      this.list.setVisibleRowCount(var1);
   }

   public void setPrototypeValue(Object var1) {
      this.list.setPrototypeCellValue(var1);
   }

   @Override
   public ListModel getModel() {
      return this.list.getModel();
   }

   @Override
   public void setModel(ListModel var1) {
      this.list.setModel(var1);
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.support.setDefaultAction(var1);
   }

   @Override
   public void setEnabled(boolean var1) {
      UiUtils.setEnabled(var1, this.list, this.label);
   }

   public void setSelectionConverter(SelectionConverter var1) {
      this.selectionConverter = Preconditions.checkNotNull(var1, "converter");
   }

   @Override
   public IndexedSelection getSelection() {
      return this.selectionHandler.getSelection();
   }

   @Override
   public void setSelectedIndex(int var1) {
      Selection var2 = null;
      if (var1 == -1) {
         var2 = IndexedSelection.EMPTY;
      } else {
         Object var3 = this.list.getModel().getElementAt(var1);
         var2 = SelectionFactory.newIndexedSelection(var3, var1);
      }

      this.setSelection(var2);
      if (this.useScrolling && var1 >= 0) {
         this.list.ensureIndexIsVisible(var1);
      }
   }

   @Override
   public boolean isEmpty() {
      return this.getSize() == 0;
   }

   @Override
   public void selectFirst() {
      if (this.getSize() > 0) {
         this.setSelectedIndex(0);
      }
   }

   @Override
   public void repaint() {
      this.list.repaint();
   }

   @Override
   public void setSelection(Selection var1) {
      if (var1 == null) {
         var1 = Selection.EMPTY;
      }

      this.selectionHandler.setSelection(var1);
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.support.addListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.support.removeListener(var1);
   }

   @Override
   public boolean processKeyEvent(KeyEvent var1) {
      Object var2 = var1.getSource();

      try {
         if (var2 != this.list) {
            var1.setSource(this.list);
         }

         this.notifyKeyListenersOnEvent(var1);
         return var1.isConsumed() ? true : this.processKeyStroke(var1);
      } finally {
         if (var2 != this.list) {
            var1.setSource(var2);
         }
      }
   }

   private void notifyKeyListenersOnEvent(KeyEvent var1) {
      for (KeyListener var5 : this.list.getKeyListeners()) {
         this.notifyListenerOnEvent(var1, var5);
      }

      if (var1.isConsumed() || isTypedChar(var1)) {
         var1.consume();
      }
   }

   private void notifyListenerOnEvent(KeyEvent var1, KeyListener var2) {
      int var3 = var1.getID();
      switch (var3) {
         case 400:
            var2.keyTyped(var1);
            break;
         case 401:
            var2.keyPressed(var1);
            break;
         case 402:
            var2.keyReleased(var1);
      }
   }

   private static boolean isTypedChar(KeyEvent var0) {
      return var0.getID() == 400 && var0.getKeyChar() != 0;
   }

   private boolean processKeyStroke(KeyEvent var1) {
      KeyStroke var2 = KeyStroke.getKeyStrokeForEvent(var1);
      boolean var3 = false;
      Object var4 = this.list.getInputMap().get(var2);
      if (var4 != null) {
         Action var5 = this.list.getActionMap().get(var4);
         if (var5 != null) {
            ActionUtils.run(var5, this.list, (String)var5.getValue("ActionCommandKey"));
            var3 = true;
         }
      }

      return var3;
   }

   @Override
   public void setTransient(boolean var1) {
      if (var1 != this.isTransient) {
         if (var1) {
            this.installMouseMotionSelection();
         } else {
            this.uninstallMouseMotionSelection();
         }

         this.support.setSingleClickSelectionEnabled(var1);
         this.isTransient = var1;
      }
   }

   @Override
   public void ensureSelectedIndexVisible() {
      int var1 = this.list.getLeadSelectionIndex();
      if (var1 >= 0) {
         this.list.ensureIndexIsVisible(var1);
      }
   }

   private void installMouseMotionSelection() {
      if (this.mouseMotionSelector == null) {
         this.mouseMotionSelector = MouseMotionListSelector.install(this.list);
      }
   }

   private void uninstallMouseMotionSelection() {
      if (this.mouseMotionSelector != null) {
         this.mouseMotionSelector.uninstall();
         this.mouseMotionSelector = null;
      }
   }

   public void noScrolling() {
      this.useScrolling = false;
   }

   @Override
   public void decorate(UiCanvas var1) {
      Component var2 = null;
      if (this.useScrolling) {
         var2 = this.installScrolling();
      } else {
         var2 = this.list;
      }

      if (this.label == null) {
         var1.display(var2);
      } else {
         BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 2);
         var3.north(this.label).center(var2);
         var3.decorate(var1);
      }
   }

   public void setLabel(JLabel var1) {
      this.label = Preconditions.checkNotNull(var1, "label");
      new KongaListLabel(this, var1);
   }

   private JComponent installScrolling() {
      assert this.useScrolling;
      JScrollPane var1 = new JScrollPane(this.list);
      var1.getViewport().setBackground(javax.swing.UIManager.getColor("Panel.background"));
      return var1;
   }

   @Override
   public void requestFocus() {
      this.list.requestFocusInWindow();
   }

   @Override
   public Disposable installKeyTraversalBindings(JComponent var1) {
      Preconditions.checkNotNull(var1, "c");
      ListContainerKeyBindingsInstaller var2 = new ListContainerKeyBindingsInstaller(var1);
      var2.install(this.list);
      return var2;
   }

   @Override
   public void addKeyBinding(KeyStroke var1, String var2, Action var3) {
      this.list.getInputMap().put(var1, var2);
      this.list.getActionMap().put(var2, var3);
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.list.addFocusListener(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.list.removeFocusListener(var1);
   }

   private class ContextMenuItemSelector extends MouseActionListener {
      private ContextMenuItemSelector() {
      }

      @Override
      protected void onPopupTrigger(MouseEvent var1) {
         if (!var1.isControlDown() && !var1.isAltDown() && !var1.isShiftDown()) {
            this.selectItemClickedOn(var1);
         }
      }

      private void selectItemClickedOn(MouseEvent var1) {
         int var2 = DefaultKongaList.this.list.locationToIndex(var1.getPoint());
         if (var2 != -1 && !DefaultKongaList.this.list.isSelectedIndex(var2)) {
            DefaultKongaList.this.list.setSelectedIndex(var2);
         }
      }
   }

   private class NewModelListener implements PropertyChangeListener {
      private NewModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         ListModel var2 = (ListModel)var1.getOldValue();
         var2.removeListDataListener(DefaultKongaList.this.selectionHandler);
         ListModel var3 = (ListModel)var1.getNewValue();
         var3.addListDataListener(DefaultKongaList.this.selectionHandler);
      }
   }

   private class SelectionHandler implements ListSelectionListener, ListDataListener {
      private IndexedSelection selection = IndexedSelection.EMPTY;

      public SelectionHandler() {
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting()) {
            this.updateSelection();
         }
      }

      private void updateSelection() {
         ListSelection var1 = new ListSelection(DefaultKongaList.this.list, DefaultKongaList.this.selectionConverter);
         this.onNewSelection(var1);
      }

      private void onNewSelection(IndexedSelection var1) {
         this.selection = var1;
         DefaultKongaList.this.support.fireSelectionChange(var1);
      }

      public IndexedSelection getSelection() {
         return this.selection;
      }

      public void setSelection(Selection var1) {
         assert var1 != null : "A null selection should be converted to an empty selection before this point";
         if (var1.isEmpty()) {
            DefaultKongaList.this.list.clearSelection();
         } else if (var1 instanceof IndexedSelection) {
            this.setSelectionImpl((IndexedSelection)var1);
         } else {
            if (!(var1 instanceof OrderedSelection)) {
               throw new IllegalArgumentException("sel must be null, empty, or an OrderedSelection");
            }

            this.setSelectionImpl((OrderedSelection)var1);
         }
      }

      private void setSelectionImpl(OrderedSelection var1) {
         Object[] var2 = var1.toArray();

         for (int var3 = 0; var3 < var2.length; var3++) {
            var2[var3] = DefaultKongaList.this.selectionConverter.toSelection(var2[var3]);
         }

         ListUtils.selectObjects(DefaultKongaList.this.list, var2);
      }

      private void setSelectionImpl(IndexedSelection var1) {
         if (var1.isEmpty()) {
            DefaultKongaList.this.list.clearSelection();
         } else {
            DefaultKongaList.this.list.setSelectedIndices(var1.indeces());
         }
      }

      @Override
      public void intervalAdded(ListDataEvent var1) {
         this.checkSelectionAfterModelChange();
      }

      private void checkSelectionAfterModelChange() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Object[] var1 = SelectionHandler.this.selection.toArray();
               Object[] var2 = DefaultKongaList.this.list.getSelectedValues();
               if (!Arrays.equals(var1, var2)) {
                  SelectionHandler.this.updateSelection();
               }
            }
         });
      }

      @Override
      public void intervalRemoved(ListDataEvent var1) {
         this.checkSelectionAfterModelChange();
      }

      @Override
      public void contentsChanged(ListDataEvent var1) {
         this.checkSelectionAfterModelChange();
      }
   }
}

