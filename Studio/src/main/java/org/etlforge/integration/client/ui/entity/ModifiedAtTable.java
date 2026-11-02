package org.etlforge.integration.client.ui.entity;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.SortOrder;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityTableCellRenderer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;
import org.etlforge.ui.widget.table.renderer.ListDecorTableRenderer;

final class ModifiedAtTable implements UiProvider {
   private volatile IntegrationProject project;
   private final KongaTable table = this.createTable();

   public ModifiedAtTable() {
   }

   private KongaTable createTable() {
      TableFactory var1 = ComponentFactories.tableFactory();
      ModifiedAtTable.Model var2 = new ModifiedAtTable.Model();
      KongaTable var3 = var1.newSortedTable(var2);
      var3.setCellRendererAt(0, new IntegrationEntityTableCellRenderer());
      var3.setCellRendererAt(1, new ListDecorTableRenderer());
      this.installSorter(var3);
      return var3;
   }

   private void installSorter(KongaTable var1) {
      EntityTypeAndNameSorter var2 = new EntityTypeAndNameSorter();
      var2.setUseItemTypeForFolders(false);
      var1.setComparator(0, var2);
      var1.setSortsOnUpdates(true);
   }

   public void setProject(IntegrationProject var1) {
      this.project = var1;
      this.rebuild();
   }

   public void rebuild() {
      final ModifiedAtTable.Model var1 = this.project != null ? new ModifiedAtTable.Model(this.project) : new ModifiedAtTable.Model();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ModifiedAtTable.this.table.setModel(var1);
            ModifiedAtTable.this.table.setSortingStatus(1, SortOrder.DESCENDING);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocusInWindow();
   }

   public void updateModifiedDate(final IntegrationEntity var1) {
      if (!(var1 instanceof Folder)) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ModifiedAtTable.Model var1x = (ModifiedAtTable.Model)ModifiedAtTable.this.table.getModel();
               var1x.updateModifiedDate(var1);
               EventQueue.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     ModifiedAtTable.this.table.repaint();
                  }
               });
            }
         });
      }
   }

   private static class Model extends TypedAbstractKongaTableModel<IntegrationEntity> {
      private int rowToModify;

      public Model() {
         super(IntegrationEntity.class, createColumnDescriptors());
      }

      private static TableColumnMetaData[] createColumnDescriptors() {
         return new TableColumnMetaData[]{
            new TableColumnMetaData("Item", IntegrationEntity.class, false, 200), new TableColumnMetaData("Last Modified At", DateCell.class, false, 200)
         };
      }

      public Model(IntegrationProject var1) {
         this();
         List var2 = var1.getAllEntities();
         Iterables.removeIf(var2, Predicates.instanceOf(Folder.class));
         this.addRows(var2);
      }

      protected Object[] getColumnDataForTypedObject(IntegrationEntity var1) {
         return new Object[]{var1, new DateCell(var1.getModifiedAt())};
      }

      public void updateModifiedDate(IntegrationEntity var1) {
         for (int var2 = 0; var2 < this.getRowCount(); var2++) {
            if (this.getRowObjectAt(var2) == var1) {
               this.rowToModify = var2;
               this.setValueAt(new DateCell(var1.getModifiedAt()), var2, 1);
               this.rowToModify = -1;
               break;
            }
         }
      }

      @Override
      public boolean isCellEditable(int var1, int var2) {
         return var1 == this.rowToModify && var2 == 1;
      }
   }
}
