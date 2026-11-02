package org.etlforge.ui.widget.table;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.IndexedSelectionBuilder;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.selection.SelectionSourceWidgetSupport;

public abstract class TableRowSelectionSource implements SelectionSource {
   private final JTable table;
   private final TableRowSelectionSource.SelectionHandler selectionHandler;
   private final SelectionSourceWidgetSupport selectionSupport;

   public TableRowSelectionSource(JTable var1) {
      this.table = var1;
      this.selectionHandler = new TableRowSelectionSource.SelectionHandler();
      var1.getSelectionModel().addListSelectionListener(this.selectionHandler);
      this.selectionSupport = new SelectionSourceWidgetSupport(this, var1);
   }

   protected abstract Object getRowObjectAt(int var1);

   public final IndexedSelection getSelection() {
      return this.selectionHandler.getSelection();
   }

   @Override
   public final void addSelectionListener(SelectionListener var1) {
      this.selectionSupport.addListener(var1);
   }

   @Override
   public final void removeSelectionListener(SelectionListener var1) {
      this.selectionSupport.removeListener(var1);
   }

   public final void setDefaultAction(SelectionAction var1) {
      this.selectionSupport.setDefaultAction(var1);
   }

   private class SelectionHandler implements ListSelectionListener {
      private IndexedSelection selection = IndexedSelection.EMPTY;

      public SelectionHandler() {
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting()) {
            IndexedSelection var2 = this.createSelection();
            this.handleNewSelection(var2);
         }
      }

      private IndexedSelection createSelection() {
         IndexedSelectionBuilder var1 = new IndexedSelectionBuilder();

         for (int var5 : TableRowSelectionSource.this.table.getSelectedRows()) {
            int var6 = TableRowSelectionSource.this.table.convertRowIndexToModel(var5);
            Object var7 = TableRowSelectionSource.this.getRowObjectAt(var6);
            if (var7 != null) {
               var1.add(var7, var6);
            }
         }

         return var1.build();
      }

      private void handleNewSelection(IndexedSelection var1) {
         this.selection = var1;
         TableRowSelectionSource.this.selectionSupport.fireSelectionChange(var1);
      }

      public IndexedSelection getSelection() {
         return this.selection;
      }
   }
}
