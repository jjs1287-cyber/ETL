package com.etlforge.integration.client.deploy.ui;

import com.etlforge.integration.client.deploy.api.LastUpdateInfo;
import java.util.List;
import javax.swing.SortOrder;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityTableCellRenderer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaRowTableModel;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.renderer.DateRenderer;

class LastUpdateInfoTable implements UiProvider {
   private final KongaRowTable table;

   public LastUpdateInfoTable(IntegrationProject var1, List<LastUpdateInfo> var2) {
      LastUpdateInfoTableModel var3 = this.createTableModel(var1, var2);
      this.table = this.createTable(var3);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private LastUpdateInfoTableModel createTableModel(IntegrationProject var1, List<LastUpdateInfo> var2) {
      LastUpdateInfoTableModel var3 = new LastUpdateInfoTableModel(var1.getItemLookup());
      var3.addRows(var2);
      return var3;
   }

   private KongaRowTable createTable(KongaRowTableModel var1) {
      KongaTable var2 = ComponentFactories.tableFactory().newSortedTable(var1, 10);
      var2.setCellRendererFor(LastUpdateInfoTableModel.ITEM, new IntegrationEntityTableCellRenderer());
      var2.setCellRendererFor(LastUpdateInfoTableModel.USER, new UserTableRenderer());
      var2.setCellRendererFor(LastUpdateInfoTableModel.WHEN, DateRenderer.dateAndTime(2, 3));
      int var3 = var1.indexOfColumn(LastUpdateInfoTableModel.ITEM);
      var2.setComparator(var3, new EntityTypeAndNameSorter());
      var2.setSortingStatus(LastUpdateInfoTableModel.ITEM, SortOrder.ASCENDING);
      return new KongaRowTable(var2);
   }
}
