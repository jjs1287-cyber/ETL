package org.etlforge.integration.client.ui.entity.search;

import java.awt.EventQueue;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.client.ui.entity.EntityPathPublisher;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityTableCellRenderer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.table.AbstractKongaTableModel;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;

final class ResultTable implements ResultDisplayer {
   private final KongaRowTable table = this.createTable();

   public ResultTable(EntitySearchModel var1) {
      ResultTable.ResultBinding var2 = new ResultTable.ResultBinding(var1);
      var2.syncUi();
   }

   private KongaRowTable createTable() {
      TableFactory var1 = ComponentFactories.tableFactory();
      KongaTable var2 = var1.newSortedTable(new ResultTable.Model(), 15);
      var2.setSelectionMode(0);
      var2.setCellRendererFor(ResultTable.Model.NAME, new IntegrationEntityTableCellRenderer());
      ResultTable.DateRenderer var3 = new ResultTable.DateRenderer();
      var2.setCellRendererFor(ResultTable.Model.CREATED, var3);
      var2.setCellRendererFor(ResultTable.Model.MODIFIED, var3);
      KongaRowTable var4 = new KongaRowTable(var2);
      var4.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            IntegrationEntity var2x = ResultTable.this.getSelectedEntity();
            ResultTable.publishPath(ResultTable.this, var2x);
         }
      });
      return var4;
   }

   private static void publishPath(Object var0, IntegrationEntity var1) {
      EntityPathPublisher var2 = new EntityPathPublisher(var0);
      var2.setIncludeProject(true);
      var2.publish(var1);
   }

   @Override
   public JComponent getDisplayer() {
      return BorderLayoutBuilder.fillWith(this.table);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   @Override
   public IntegrationEntity getSelectedEntity() {
      Object var1 = this.table.getSelection().head();
      return var1 instanceof IntegrationEntity ? (IntegrationEntity)var1 : null;
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   private static final class DateRenderer extends DefaultTableCellRenderer {
      private final DateFormat dateFormat = DateFormat.getDateTimeInstance(2, 3);

      private DateRenderer() {
      }

      @Override
      protected void setValue(Object var1) {
         if (var1 instanceof Date) {
            this.setText(this.dateFormat.format((Date)var1));
         } else {
            super.setValue(var1);
         }
      }
   }

   private static final class Model extends AbstractKongaTableModel {
      public static final TableColumnMetaData NAME = new TableColumnMetaData("Name", Object.class, false, 250);
      public static final TableColumnMetaData CREATED = new TableColumnMetaData("Created", Date.class, false, 125);
      public static final TableColumnMetaData MODIFIED = new TableColumnMetaData("Modified", Date.class, false, 125);

      public Model() {
         super(NAME, CREATED, MODIFIED);
      }

      @Override
      protected Object[] getColumnDataFromObject(Object var1) {
         if (var1 instanceof IntegrationEntity) {
            IntegrationEntity var2 = (IntegrationEntity)var1;
            return new Object[]{var2, var2.getCreatedAt(), var2.getModifiedAt()};
         } else if (var1 instanceof String) {
            return new Object[]{(String)var1, null, null};
         } else {
            throw new IllegalArgumentException("o must be either an IntegrationEntity or a String");
         }
      }
   }

   private class ResultBinding extends AbstractBinding<List<IntegrationEntity>> {
      private final EntitySearchModel searchModel;

      public ResultBinding(EntitySearchModel var2) {
         super(var2, "matches");
         this.searchModel = var2;
      }

      protected void updateUiWithValue(List<IntegrationEntity> var1) {
         ResultTable.Model var2 = new ResultTable.Model();
         if (var1.isEmpty() && this.searchModel.hasSearchString()) {
            var2.addRow(EntityResources.getString("Search.NoMatch"));
         } else {
            var2.addRows(var1);
         }

         this.setModel(var2);
      }

      private void setModel(ResultTable.Model var1) {
         assert EventQueue.isDispatchThread() : "setModel must be called from the EDT";
         ResultTable.this.table.setModel(var1);
         ResultTable.this.table.selectFirstRow();
      }
   }
}
