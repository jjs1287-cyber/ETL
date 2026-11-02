package org.etlforge.integration.client.ui.de.global;

import com.google.common.base.Joiner;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.EnumSet;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.script.de.DataElementReference;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityTableCellRenderer;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaRowTableModel;
import org.etlforge.ui.widget.table.KongaTable;

public final class DataElementReferenceTable implements Disposable, UiProvider {
   private final KongaRowTable table = this.createTable();
   private ExpressionSourceDisplayer sourceDisplayer;
   private String caption;

   public DataElementReferenceTable() {
      this.table.setDefaultAction(new DataElementReferenceTable.DefaultAction());
   }

   @Override
   public void dispose() {
      ((DataElementReferenceTableModel)this.table.getModel()).dispose();
   }

   public DataElementReferenceTable setReferences(Collection<DataElementReference> var1) {
      DataElementReferenceTableModel var2 = new DataElementReferenceTableModel();
      var2.addRows(var1);
      this.setNewModel(var2);
      return this;
   }

   public DataElementReferenceTable clear() {
      DataElementReferenceTableModel var1 = new DataElementReferenceTableModel();
      this.setNewModel(var1);
      return this;
   }

   public DataElementReferenceTable setExpressionSourceDisplayer(ExpressionSourceDisplayer var1) {
      this.sourceDisplayer = var1;
      return this;
   }

   public DataElementReferenceTable setCaption(String var1) {
      this.caption = var1;
      return this;
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.caption != null) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3);
         var2.north(this.caption).center(this.table).deeplyNonOpaque();
         var2.decorate(var1);
      } else {
         this.table.decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private void setNewModel(final KongaRowTableModel var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DataElementReferenceTable.this.table.setModel(var1);
            if (DataElementReferenceTable.this.table.getAdaptee().getRowSorter().getSortKeys().isEmpty()) {
               DataElementReferenceTable.this.table.getAdaptee().setSortingStatus(DataElementReferenceTableModel.SOURCE, SortOrder.ASCENDING);
            }
         }
      });
   }

   private KongaRowTable createTable() {
      DataElementReferenceTableModel var1 = new DataElementReferenceTableModel();
      KongaTable var2 = ComponentFactories.tableFactory().newSortedTable(var1);
      this.installSorter(var2, var1);
      this.setRenderers(var2);
      TableStyles.normal().makeOver(var2);
      return new KongaRowTable(var2);
   }

   private void installSorter(KongaTable var1, DataElementReferenceTableModel var2) {
      EntityTypeAndNameSorter var3 = new EntityTypeAndNameSorter();
      var1.setComparator(0, var3);
      var1.setSortsOnUpdates(true);
   }

   private void setRenderers(KongaTable var1) {
      IntegrationEntityTableCellRenderer var2 = new IntegrationEntityTableCellRenderer();
      var2.setValidationDecorating(true);
      var1.setCellRendererFor(DataElementReferenceTableModel.SOURCE, var2);
      var1.setCellRendererFor(DataElementReferenceTableModel.MAPPING, new DefaultTableCellRenderer() {
         @Override
         protected void setValue(Object var1) {
            super.setValue(var1);
            this.setToolTipText(var1 != null ? var1.toString() : null);
         }
      });
      var1.setCellRendererFor(DataElementReferenceTableModel.ACCESS, new DefaultTableCellRenderer() {
         @Override
         protected void setValue(Object var1) {
            EnumSet var2 = (EnumSet)var1;
            String var3 = Joiner.on(", ").join(var2);
            super.setValue(var3);
            this.setToolTipText(var2.isEmpty() ? null : var3);
         }
      });
   }

   private final class DefaultAction extends AbstractSingleObjectSelectionAction<DataElementReference> {
      public DefaultAction() {
         super(DataElementReference.class);
      }

      protected boolean isEnabledFor(DataElementReference var1) {
         return DataElementReferenceTable.this.sourceDisplayer != null;
      }

      protected void run(DataElementReference var1, ActionEvent var2) {
         if (DataElementReferenceTable.this.sourceDisplayer != null) {
            DataElementReferenceTable.this.sourceDisplayer.display(var1.getSource());
         }
      }
   }
}
