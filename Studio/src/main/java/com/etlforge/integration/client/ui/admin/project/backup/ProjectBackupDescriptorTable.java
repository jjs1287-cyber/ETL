package com.etlforge.integration.client.ui.admin.project.backup;

import javax.swing.Icon;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.renderer.DateRenderer;

public class ProjectBackupDescriptorTable implements UiProvider, SelectionSource {
   private final KongaRowTable table = this.createTable();
   private ComponentPainter emptyTablePainter;

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.table.addSelectionListener(var1);
   }

   public IndexedSelection getSelection() {
      return this.table.getSelection();
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.table.removeSelectionListener(var1);
   }

   public void setEmptyTablePainter(ComponentPainter var1) {
      this.emptyTablePainter = var1;
   }

   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   public void setBackups(ProjectBackupDescriptor... var1) {
      final ProjectBackupDescriptorTableModel var2 = new ProjectBackupDescriptorTableModel(var1);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ProjectBackupDescriptorTable.this.table.setModel(var2);
            ProjectBackupDescriptorTable.this.updateTablePainter();
         }
      });
   }

   private KongaRowTable createTable() {
      KongaTable var1 = ComponentFactories.tableFactory().newSortedTable(new ProjectBackupDescriptorTableModel(), 10);
      var1.setCellRendererFor(ProjectBackupDescriptorTableModel.WHEN, DateRenderer.dateAndTime());
      var1.setCellRendererFor(ProjectBackupDescriptorTableModel.WHO, new ProjectBackupDescriptorTable.UserRenderer());
      TableStyles.normal().makeOver(var1);
      var1.setSortingStatus(ProjectBackupDescriptorTableModel.WHEN, SortOrder.DESCENDING);
      return new KongaRowTable(var1);
   }

   private void updateTablePainter() {
      ComponentPainter var1 = this.table.getModel().getRowCount() == 0 ? this.emptyTablePainter : null;
      this.table.getAdaptee().setPainter(var1);
   }

   private static class UserRenderer extends DefaultTableCellRenderer {
      private UserRenderer() {
      }

      @Override
      protected void setValue(Object var1) {
         super.setValue(var1);
         Icon var2 = StringUtils.isEmpty(this.getText()) ? null : ClientIcons.USER_16;
         this.setIcon(var2);
      }
   }
}
