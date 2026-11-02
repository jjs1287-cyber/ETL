package com.etlforge.integration.client.ui.admin.file;

import com.etlforge.integration.client.admin.server.ServerFileLocator;
import java.awt.EventQueue;
import javax.swing.SortOrder;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.SortStatusMemento;
import org.etlforge.ui.widget.table.renderer.DateRenderer;
import org.etlforge.ui.widget.table.renderer.FileSizeRenderer;
import org.etlforge.util.StorageSizeUnit;

public final class ServerFileLocatorTable implements UiProvider, SelectionSource {
   private final KongaRowTable table = this.createTable();

   public void setFiles(ServerFileLocator[] var1) {
      ServerFileLocatorTableModel var2 = this.createNewModel(var1);
      this.table.setModel(var2);
      this.requestFocus();
   }

   public void setSortStatusMemento(SortStatusMemento var1) {
      this.table.getAdaptee().setSortStatusMemento(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ServerFileLocatorTable.this.table.requestFocus();
         }
      });
   }

   public IndexedSelection getSelection() {
      return this.table.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.table.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.table.removeSelectionListener(var1);
   }

   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   public void selectFirst() {
      this.table.selectFirstRow();
   }

   private KongaRowTable createTable() {
      ServerFileLocatorTableModel var1 = new ServerFileLocatorTableModel();
      byte var2 = 6;
      KongaTable var3 = ComponentFactories.tableFactory().newSortedTable(var1, var2);
      var3.setSortingStatus(ServerFileLocatorTableModel.PATH, SortOrder.ASCENDING);
      this.setRenderers(var3);
      TableStyles.normal().makeOver(var3);
      return new KongaRowTable(var3);
   }

   private void setRenderers(KongaTable var1) {
      var1.setCellRendererFor(ServerFileLocatorTableModel.SIZE, new FileSizeRenderer(StorageSizeUnit.Kilobytes));
      var1.setCellRendererFor(ServerFileLocatorTableModel.MODIFIED, DateRenderer.dateAndTime());
   }

   private ServerFileLocatorTableModel createNewModel(ServerFileLocator[] var1) {
      ServerFileLocatorTableModel var2 = new ServerFileLocatorTableModel();

      for (ServerFileLocator var6 : var1) {
         var2.addRow(var6);
      }

      return var2;
   }
}
