package com.etlforge.integration.client.deploy.ui;

import com.etlforge.integration.client.deploy.api.SharedItemInfo;
import java.util.List;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityTableCellRenderer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaRowTableModel;
import org.etlforge.ui.widget.table.KongaTable;

class SharedItemsInfoTable implements UiProvider {
   private final KongaRowTable table;

   public SharedItemsInfoTable(IntegrationProject var1, List<SharedItemInfo> var2) {
      KongaRowTableModel var3 = this.createTableModel(var1, var2);
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

   private KongaRowTableModel createTableModel(IntegrationProject var1, List<SharedItemInfo> var2) {
      SharedItemsInfoTableModel var3 = new SharedItemsInfoTableModel(var1.getItemLookup());
      var3.addRows(var2);
      return var3;
   }

   private KongaRowTable createTable(KongaRowTableModel var1) {
      KongaTable var2 = ComponentFactories.tableFactory().newSortedTable(var1, 10);
      var2.setCellRendererFor(SharedItemsInfoTableModel.ITEM, new IntegrationEntityTableCellRenderer());
      var2.setCellRendererFor(SharedItemsInfoTableModel.PROJECT, new SharedItemsInfoTable.ProjectRenderer());
      var2.setCellRendererFor(SharedItemsInfoTableModel.OWNER, new UserTableRenderer());
      int var3 = var1.indexOfColumn(SharedItemsInfoTableModel.ITEM);
      var2.setComparator(var3, new EntityTypeAndNameSorter());
      var2.setSortingStatus(SharedItemsInfoTableModel.ITEM, SortOrder.ASCENDING);
      return new KongaRowTable(var2);
   }

   private static class ProjectRenderer extends DefaultTableCellRenderer {
      private ProjectRenderer() {
      }

      @Override
      protected void setValue(Object var1) {
         super.setValue(var1);
         this.setIcon(EntityIcons.forType(EntityType.Project).getDefaultIcon(EntityIcons.Size.SMALL));
      }
   }
}
