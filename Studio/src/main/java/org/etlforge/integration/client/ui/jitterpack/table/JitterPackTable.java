package org.etlforge.integration.client.ui.jitterpack.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.ui.jitterpack.JitterPackSelectionListener;
import org.etlforge.integration.client.ui.jitterpack.JitterPackSelectionListeners;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.event.ActionListeners;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DefaultActionHandler;
import org.etlforge.ui.widget.list.NoSelectionModel;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableUtils;
import org.etlforge.ui.widget.table.renderer.YesNoBooleanRenderer;
import org.etlforge.util.version.Version;

public final class JitterPackTable implements UiProvider {
   private final KongaTable table;
   private final JitterPackTableModel tableModel;
   private final ActionListeners actionListeners = new ActionListeners();
   private final JitterPackSelectionListeners selectionListeners = new JitterPackSelectionListeners();

   public JitterPackTable(boolean var1) {
      this(null, var1);
   }

   public JitterPackTable(Collection<JitterPack> var1, boolean var2) {
      this.tableModel = this.createTableModel(var1, var2);
      this.table = this.createTable();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(TableUtils.wrapInScrollPreserveWidth(this.table));
   }

   @Override
   public void requestFocus() {
      this.table.requestFocusInWindow();
   }

   public JitterPack getSelectedJitterPack() {
      int var1 = this.table.getSelectedRow();
      if (var1 >= 0) {
         int var2 = this.table.convertRowIndexToModel(var1);
         return this.tableModel.getRowObjectAt(var2);
      } else {
         return null;
      }
   }

   public boolean setSelectedJitterPack(JitterPack var1) {
      int var2 = 0;

      for (int var3 = this.table.getRowCount(); var2 < var3; var2++) {
         int var4 = this.table.convertRowIndexToModel(var2);
         if (this.tableModel.getRowObjectAt(var4) == var1) {
            final int var5 = var2;
            UiUtils.runOnEventThread(new Runnable() {
               @Override
               public void run() {
                  JitterPackTable.this.table.getSelectionModel().setSelectionInterval(var5, var5);
               }
            });
            return true;
         }
      }

      return false;
   }

   public void disableSelections() {
      this.table.setSelectionModel(new NoSelectionModel());
   }

   public void addJitterPack(JitterPack var1) {
      this.tableModel.addRow(var1);
   }

   public void removeAllRows() {
      this.tableModel.deleteAll();
   }

   public void addActionListener(ActionListener var1) {
      this.actionListeners.add(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.actionListeners.remove(var1);
   }

   public void addSelectionListener(JitterPackSelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   public void removeSelectionListener(JitterPackSelectionListener var1) {
      this.selectionListeners.remove(var1);
   }

   private KongaTable createTable() {
      int var1 = Math.max(8, Math.min(11, this.tableModel.getRowCount()));
      TableFactory var2 = ComponentFactories.tableFactory();
      KongaTable var3 = var2.newSortedTable(this.tableModel, var1);
      var3.setSortingStatus(JitterPackTableModel.LOCATION, SortOrder.DESCENDING);
      var3.setSelectionMode(0);
      var3.getSelectionModel().addListSelectionListener(new JitterPackTable.SelectionNotifier());
      this.setRenderers(var3);
      TableStyles.normal().makeOver(var3);
      this.registerTableController(var3);
      return var3;
   }

   private JitterPackTableModel createTableModel(Collection<JitterPack> var1, boolean var2) {
      JitterPackTableModel var3 = new JitterPackTableModel(var2);
      if (var1 != null) {
         var3.addRows(var1);
      }

      return var3;
   }

   private void setRenderers(KongaTable var1) {
      var1.setDefaultRenderer(JitterPackLocationTableItem.class, TableRenderers.getLocationRenderer());
      var1.setDefaultRenderer(String.class, TableRenderers.getStringRenderer());
      var1.setDefaultRenderer(Date.class, TableRenderers.getDateRenderer());
      var1.setDefaultRenderer(Version.class, TableRenderers.getVersionRenderer());
      var1.setDefaultRenderer(Boolean.class, new YesNoBooleanRenderer());
   }

   private void registerTableController(KongaTable var1) {
      Action var2 = this.getTableAction();
      DefaultActionHandler var3 = new DefaultActionHandler(var1, var2);
      var3.install();
   }

   public void setNumberOfVisibleRows(int var1) {
      this.table.setVisibleRowCount(Math.max(1, var1));
   }

   private Action getTableAction() {
      return new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (JitterPackTable.this.table.getSelectedRow() >= 0) {
               JitterPackTable.this.fireActionEvent();
            }
         }
      };
   }

   private void fireActionEvent() {
      this.actionListeners.call().actionPerformed(new ActionEvent(this, 1001, "table_action"));
   }

   private class SelectionNotifier implements ListSelectionListener {
      private SelectionNotifier() {
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!JitterPackTable.this.selectionListeners.isEmpty()) {
            JitterPack var2 = JitterPackTable.this.getSelectedJitterPack();
            JitterPackTable.this.selectionListeners.call().selectionChanged(var2);
         }
      }
   }
}
