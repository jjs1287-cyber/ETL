package org.etlforge.integration.client.plugin.ui.pipeline.view;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.application.ui.widget.TableFactory;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.widget.table.KongaTable;

public final class AvailabilityResultTable implements UiProvider {
   private final String caption;
   private final KongaTable table;

   public AvailabilityResultTable(String var1, AvailabilityResultTableModel var2) {
      this.caption = var1;
      TableFactory var3 = ComponentFactories.tableFactory();
      this.table = var3.newSortedTable(var2);
      this.table.setSortingStatus(AvailabilityResultTableModel.PROBLEM, SortOrder.DESCENDING);
      this.table.setCellRendererFor(AvailabilityResultTableModel.NAME, new AvailabilityResultTable.PluginRenderer());
      this.table.setCellRendererFor(AvailabilityResultTableModel.PROBLEM, new AvailabilityResultTable.ProblemRenderer());
      this.table.setVisibleRowCount(5);
      TableStyles.normal().makeOver(this.table);
   }

   @Override
   public void decorate(UiCanvas var1) {
      CaptionLayout.of(this.caption, this.table.asUiProvider()).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocusInWindow();
   }

   private static class PluginRenderer extends DefaultTableCellRenderer {
      private PluginRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         this.setIcon(ClientIcons.PLUGIN_16);
         return this;
      }
   }

   private static class ProblemRenderer extends DefaultTableCellRenderer {
      private ProblemRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         AvailabilityProblem var7 = (AvailabilityProblem)var2;
         this.setText(var7.problem);
         if (var7.fatal) {
            this.setIcon(CommonIcons.ERROR_16);
         } else {
            this.setIcon(CommonIcons.WARNING_16);
         }

         return this;
      }
   }
}
