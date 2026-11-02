package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSourceWidgetSupport;

public final class KongaRowTable implements UiProvider, MutableSelectionSource {
   private final KongaTable adaptee;
   private final KongaRowTable.SelectionHandler selectionHandler;
   private final SelectionSourceWidgetSupport selectionSupport;

   public KongaRowTable(KongaTable var1) {
      Preconditions.checkArgument(var1.getModel() instanceof KongaRowTableModel, "The model must be a KongaRowTableModel");
      this.adaptee = var1;
      this.selectionHandler = new KongaRowTable.SelectionHandler();
      this.adaptee.getSelectionModel().addListSelectionListener(this.selectionHandler);
      this.selectionSupport = new SelectionSourceWidgetSupport(this, this.adaptee);
   }

   public KongaTable getAdaptee() {
      return this.adaptee;
   }

   public void setModel(KongaRowTableModel var1) {
      this.adaptee.setModel(var1);
   }

   public KongaRowTableModel getModel() {
      return (KongaRowTableModel)this.adaptee.getModel();
   }

   public void selectFirstRow() {
      this.adaptee.selectFirstRow();
   }

   @Override
   public void decorate(UiCanvas var1) {
      JScrollPane var2 = TableUtils.wrapInScrollPreserveWidth(this.adaptee);
      JLabel var3 = this.adaptee.getLabel();
      if (var3 == null) {
         var1.display(var2);
      } else {
         new BorderLayoutBuilder(0, 2).north(var3).center(var2).decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      this.adaptee.requestFocusInWindow();
   }

   public IndexedSelection getSelection() {
      return this.selectionHandler.getSelection();
   }

   @Override
   public void setSelection(Selection var1) {
      if (var1 == null) {
         var1 = IndexedSelection.EMPTY;
      }

      Preconditions.checkArgument(var1 instanceof IndexedSelection, "sel must be null or an IndexedSelection");
      this.selectionHandler.setSelection((IndexedSelection)var1);
   }

   public void setDefaultAction(SelectionAction var1) {
      this.selectionSupport.setDefaultAction(var1);
   }

   public SelectionAction getDefaultAction() {
      return this.selectionSupport.getDefaultAction();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionSupport.addListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionSupport.removeListener(var1);
   }

   public void clearSelection() {
      this.adaptee.clearSelection();
   }

   public void installActions(Action... var1) {
      for (Action var5 : var1) {
         this.installAction(var5);
      }
   }

   public void installAction(Action var1) {
      if (var1 instanceof SelectionAction) {
         ((SelectionAction)var1).setSelectionSource(this);
      }

      this.installKeyBinding(var1);
   }

   public void installKeyBinding(Action var1) {
      ActionKeyBinding.install(var1, this.adaptee);
   }

   private class SelectionHandler implements ListSelectionListener {
      private IndexedSelection selection = IndexedSelection.EMPTY;

      public SelectionHandler() {
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting()) {
            int[] var2 = KongaRowTable.this.adaptee.getSelectedRows();
            Object[] var3 = new Object[var2.length];
            this.handleTableSelection(KongaRowTable.this.getModel(), var2, var3);
            this.onNewSelection(SelectionFactory.newIndexedSelection(var3, var2));
         }
      }

      private void handleTableSelection(KongaRowTableModel var1, int[] var2, Object[] var3) {
         int var4 = 0;

         for (int var5 = var2.length; var4 < var5; var4++) {
            int var6 = var2[var4];
            int var7 = KongaRowTable.this.adaptee.convertRowIndexToModel(var6);
            var3[var4] = var1.getRowObjectAt(var7);
         }
      }

      private void onNewSelection(IndexedSelection var1) {
         this.selection = var1;
         KongaRowTable.this.selectionSupport.fireSelectionChange(var1);
      }

      public IndexedSelection getSelection() {
         return this.selection;
      }

      public void setSelection(IndexedSelection var1) {
         KongaRowTable.this.adaptee.clearSelection();
         if (!var1.isEmpty()) {
            this.setTableSelection(var1);
         }
      }

      private void setTableSelection(IndexedSelection var1) {
         for (int var5 : var1.indeces()) {
            int var6 = KongaRowTable.this.adaptee.convertRowIndexToModel(var5);
            KongaRowTable.this.adaptee.setRowSelectionInterval(var6, var6);
         }
      }
   }
}
