package org.etlforge.ui.widget.list;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.AbstractChangeSource;
import org.etlforge.ui.input.ChangeSource;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.KeyUtils;

public final class MutableKongaList implements KongaList {
   private final KongaList list;
   private final MutableKongaList.ChangeSourceImpl changeSource = new MutableKongaList.ChangeSourceImpl();

   public MutableKongaList(JList var1) {
      this(new DefaultKongaList(Preconditions.checkNotNull(var1, "list")));
   }

   public MutableKongaList(KongaList var1) {
      this.list = Preconditions.checkNotNull(var1, "list");
   }

   public ChangeSource asChangeSource() {
      return this.changeSource;
   }

   public SelectionAction getDeleteAction() {
      MutableKongaList.DeleteValuesAction var1 = new MutableKongaList.DeleteValuesAction();
      var1.setSelectionSource(this.list);
      this.list.addKeyBinding(KeyUtils.DELETE, "delete", var1);
      return var1;
   }

   public void addItem(Object var1) {
      this.addItemsImpl(Collections.singleton(Preconditions.checkNotNull(var1, "item")));
   }

   public void addItems(Collection<?> var1) {
      Preconditions.checkNotNull(var1, "items");
      if (!var1.isEmpty()) {
         this.addItemsImpl(var1);
      }
   }

   public void addItems(Iterable<?> var1) {
      this.addItemsImpl(Preconditions.checkNotNull(var1, "items"));
   }

   public void addItemsImpl(Iterable<?> var1) {
      MutableKongaList.IndexedItems var2 = new MutableKongaList.IndexedItems();
      MutableListModel var3 = this.getMutableModel();
      int var4 = var3.getSize();

      for (Object var6 : var1) {
         var2.add(var4, var6);
         var4++;
      }

      for (Object var8 : var1) {
         var3.addElement(var8);
      }

      this.changeSource.valuesAdded(var2);
   }

   public void deleteItem(Object var1) {
      this.deleteItemsImpl(Collections.singleton(Preconditions.checkNotNull(var1, "item")));
   }

   public void deleteItems(Collection<?> var1) {
      Preconditions.checkNotNull(var1, "items");
      if (!var1.isEmpty()) {
         this.deleteItemsImpl(var1);
      }
   }

   public void deleteItems(Iterable<?> var1) {
      this.deleteItemsImpl(Preconditions.checkNotNull(var1, "items"));
   }

   private void deleteItemsImpl(Iterable<?> var1) {
      MutableListModel var2 = this.getMutableModel();
      MutableKongaList.IndexedItems var3 = this.deleteValues(var2, var1);
      if (!var3.isEmpty()) {
         if (var2.getSize() > 0) {
            this.list.setSelectedIndex(Math.min(var3.smallestIndex(), var2.getSize() - 1));
         }

         this.changeSource.valuesRemoved(var3);
      }
   }

   private MutableKongaList.IndexedItems deleteValues(MutableListModel var1, Iterable<?> var2) {
      MutableKongaList.IndexedItems var3 = new MutableKongaList.IndexedItems();

      for (Object var5 : var2) {
         int var6 = var1.indexOf(var5);
         if (var6 >= 0) {
            var3.add(var6, var5);
         }
      }

      var3.removeFromModel(var1);
      return var3;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.list.addFocusListener(var1);
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.list.addSelectionListener(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.list.removeFocusListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.list.removeSelectionListener(var1);
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
   public void setEnabled(boolean var1) {
      this.list.setEnabled(var1);
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   @Override
   public IndexedSelection getSelection() {
      return this.list.getSelection();
   }

   @Override
   public void setSelection(Selection var1) {
      this.list.setSelection(var1);
   }

   @Override
   public void setSelectedIndex(int var1) {
      this.list.setSelectedIndex(var1);
   }

   @Override
   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   @Override
   public void selectFirst() {
      this.list.selectFirst();
   }

   @Override
   public Disposable installKeyTraversalBindings(JComponent var1) {
      return this.list.installKeyTraversalBindings(var1);
   }

   @Override
   public void setTransient(boolean var1) {
      this.list.setTransient(var1);
   }

   @Override
   public boolean processKeyEvent(KeyEvent var1) {
      return this.list.processKeyEvent(var1);
   }

   @Override
   public void ensureSelectedIndexVisible() {
      this.list.ensureSelectedIndexVisible();
   }

   @Override
   public void repaint() {
      this.list.repaint();
   }

   @Override
   public void addKeyBinding(KeyStroke var1, String var2, Action var3) {
      this.list.addKeyBinding(var1, var2, var3);
   }

   private MutableListModel getMutableModel() {
      ListModel var1 = this.getModel();
      if (var1 instanceof MutableListModel) {
         return (MutableListModel)var1;
      } else if (var1 instanceof DefaultListModel) {
         return new KongaListModel((DefaultListModel)var1);
      } else {
         throw new UnsupportedOperationException(
            "The list model is not a MutableListModel, and not of a type that can be converted to a MutableListModel. The model class is: "
               + var1.getClass().getName()
         );
      }
   }

   private class ChangeSourceImpl extends AbstractChangeSource {
      private ChangeSourceImpl() {
      }

      public void valuesAdded(MutableKongaList.IndexedItems var1) {
         this.fireChangeEvent(new InputChangeEvent(this, "items"));
         this.fireUndoableEditHappened(new UndoableEditEvent(this, new MutableKongaList.ChangeSourceImpl.ValuesAddedEdit(var1)));
      }

      public void valuesRemoved(MutableKongaList.IndexedItems var1) {
         this.fireChangeEvent(new InputChangeEvent(this, "items"));
         this.fireUndoableEditHappened(new UndoableEditEvent(this, new MutableKongaList.ChangeSourceImpl.ValuesRemovedEdit(var1)));
      }

      private class ValuesAddedEdit extends AbstractUndoableEdit {
         private final MutableKongaList.IndexedItems items;

         public ValuesAddedEdit(MutableKongaList.IndexedItems var2) {
            this.items = var2;
         }

         @Override
         public void undo() throws CannotUndoException {
            super.undo();
            this.items.removeFromModel(MutableKongaList.this.getMutableModel());
         }

         @Override
         public void redo() throws CannotRedoException {
            super.redo();
            this.items.addBackToList(MutableKongaList.this.list, MutableKongaList.this.getMutableModel());
         }
      }

      private class ValuesRemovedEdit extends AbstractUndoableEdit {
         private final MutableKongaList.IndexedItems items;

         public ValuesRemovedEdit(MutableKongaList.IndexedItems var2) {
            this.items = var2;
         }

         @Override
         public void undo() throws CannotUndoException {
            super.undo();
            this.items.addBackToList(MutableKongaList.this.list, MutableKongaList.this.getMutableModel());
         }

         @Override
         public void redo() throws CannotRedoException {
            super.redo();
            this.items.removeFromModel(MutableKongaList.this.getMutableModel());
         }
      }
   }

   private class DeleteValuesAction extends AbstractSingleTypeSelectionAction<Object> {
      public DeleteValuesAction() {
         super(Object.class, "Delete");
         this.putValue("ShortDescription", "Delete the selected values from the list");
      }

      @Override
      protected void run(List<Object> var1) {
         MutableKongaList.this.deleteItems(var1);
      }
   }

   private static class IndexedItems implements Serializable {
      private final TreeMap<Integer, Object> values = Maps.newTreeMap();

      private IndexedItems() {
      }

      public void add(int var1, Object var2) {
         this.values.put(var1, var2);
      }

      public boolean isEmpty() {
         return this.values.isEmpty();
      }

      public int smallestIndex() {
         return this.values.firstKey();
      }

      public void removeFromModel(MutableListModel var1) {
         for (Integer var3 : this.values.descendingKeySet()) {
            var1.removeItemAt(var3);
         }
      }

      public void addBackToList(KongaList var1, MutableListModel var2) {
         for (Entry var4 : this.values.entrySet()) {
            var2.addElementAt(var4.getValue(), (Integer)var4.getKey());
         }

         var1.setSelection(SelectionFactory.newOrderedSelection(this.values.values()));
      }
   }
}
