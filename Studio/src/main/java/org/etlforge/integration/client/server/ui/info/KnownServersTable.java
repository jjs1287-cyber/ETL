package org.etlforge.integration.client.server.ui.info;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.renderer.BoldFontRenderer;
import org.etlforge.util.version.Version;

public final class KnownServersTable implements UiProvider, SelectionSource {
   private final KnownServersViewModel viewModel;
   private final KongaRowTable serverTable;
   private KnownServersTableModel tableModel;

   public KnownServersTable(KnownServersViewModel var1) {
      this.viewModel = Preconditions.checkNotNull(var1);
      this.serverTable = this.createTable();
      this.bindProperties();
   }

   private KongaRowTable createTable() {
      KnownServersTableModel var1 = KnownServersTableModel.minimalModel();
      byte var2 = 6;
      KongaTable var3 = ComponentFactories.tableFactory().newSortedTable(var1, var2);
      var3.setSortingStatus(KnownServersTableModel.LATEST_LOGIN, SortOrder.DESCENDING);
      var3.setVisibleRowCount(4);
      this.setTableRenderers(var3);
      this.setTableStyle(var3);
      return new KongaRowTable(var3);
   }

   private void setTableStyle(KongaTable var1) {
      TableStyle var2 = TableStyles.normal();
      var2.addRendererPreparer(new BoldFontRenderer() {
         @Override
         protected boolean isBold(JTable var1, int var2x, int var3) {
            int var4 = var1.convertRowIndexToModel(var2x);
            KnownServer var5 = KnownServersTable.this.tableModel.getRowObjectAt(var4);
            return KnownServersTable.this.viewModel.isConnectedTo(var5);
         }
      });
      var2.makeOver(var1);
   }

   private void setTableRenderers(KongaTable var1) {
      var1.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
         @Override
         public String getToolTipText() {
            return this.getText();
         }
      });
      var1.setCellRendererFor(KnownServersTableModel.NAME, TableRenderers.getNameRenderer());
      var1.setDefaultRenderer(Version.class, TableRenderers.getVersionRenderer());
      var1.setDefaultRenderer(DateCell.class, TableRenderers.getDateRenderer());
      var1.setCellRendererFor(KnownServersTableModel.LATEST_USER, TableRenderers.getUserRenderer());
   }

   private void bindProperties() {
      Bindings.of(new KnownServersTable.TableDataBinding(this.viewModel), new KnownServersTable.ConnectionStatusBinding(this.viewModel)).syncUi();
   }

   public void setDefaultAction(SelectionAction var1) {
      this.serverTable.setDefaultAction(var1);
   }

   public void setPopupMenuRequestHandler(PopupMenuRequestHandler var1) {
      PopupMenuRequestHandlers.install(this.serverTable.getAdaptee(), var1);
   }

   public void installKeyBinding(Action var1) {
      this.serverTable.installKeyBinding(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.serverTable.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.serverTable.requestFocus();
   }

   public OrderedSelection getSelection() {
      return this.serverTable.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.serverTable.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.serverTable.removeSelectionListener(var1);
   }

   private class ConnectionStatusBinding extends AbstractBinding<ServerInfo> {
      public ConnectionStatusBinding(KnownServersViewModel var2) {
         super(var2, "connected_to");
      }

      protected void updateUiWithValue(ServerInfo var1) {
         KnownServersTable.this.serverTable.getAdaptee().repaint();
      }
   }

   private class TableDataBinding extends AbstractBinding<List<KnownServer>> {
      public TableDataBinding(KnownServersViewModel var2) {
         super(var2, "servers");
      }

      protected void updateUiWithValue(List<KnownServer> var1) {
         this.createTableModel(var1);
         this.publish();
      }

      private void createTableModel(List<KnownServer> var1) {
         KnownServersTable.this.tableModel = KnownServersTableModel.minimalModel();
         Collections.sort(var1, TableComparators.getLatestLoginComparator());
         KnownServersTable.this.tableModel.addRows(var1);
      }

      private void publish() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               KnownServersTable.this.serverTable.setModel(KnownServersTable.this.tableModel);
               KnownServersTable.this.serverTable.getAdaptee().setSortingStatus(KnownServersTableModel.LATEST_LOGIN, SortOrder.DESCENDING);
            }
         });
      }
   }
}
