package org.etlforge.integration.activity.ui.log;

import java.util.Date;
import java.util.List;
import javax.swing.RowSorter.SortKey;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ComponentCustomizer;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.ActivityViewUtils;
import org.etlforge.integration.activity.ui.TransferSize;
import org.etlforge.integration.activity.ui.TransferSizeTableRenderer;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.renderer.DurationTableRenderer;
import org.etlforge.ui.widget.table.renderer.NumberFormatRenderer;
import org.etlforge.util.time.Duration;

public final class OperationLogTable extends KongaTable {
   public OperationLogTable(OperationLogTableModel var1) {
      super(var1);
      this.setAutoCreateRowSorter(true);
      this.setRenderers();
      this.getTableHeader().setReorderingAllowed(false);
      this.setSelectionMode(0);
      this.setStyle();
   }

   private void setStyle() {
      TableStyle var1 = TableStyles.normal();
      var1.setGridMode(TableGridMode.BOTH);
      var1.makeOver(this);
   }

   public OperationLogEntry getSelectedLogEntry() {
      OperationLogEntry var1 = null;
      int var2 = this.getSelectedRow();
      if (var2 >= 0) {
         int var3 = this.convertRowIndexToModel(var2);
         if (var3 >= 0) {
            var1 = ((OperationLogTableModel)this.getModel()).getRowObjectAt(var3);
         }
      }

      return var1;
   }

   private void setRenderers() {
      this.setDefaultRenderer(LogStatus.class, new LogStatusTableRenderer());
      this.setDefaultRenderer(Date.class, ActivityViewUtils.getDateCellRenderer());
      this.setDefaultRenderer(Duration.class, new DurationTableRenderer());
      this.setDefaultRenderer(TransferSize.class, new TransferSizeTableRenderer());
      NumberFormatRenderer var1 = NumberFormatRenderer.forInteger();
      this.setDefaultRenderer(Integer.class, var1);
      this.setDefaultRenderer(Long.class, var1);
      this.setFont(TextStyles.DefaultTableText.getFont());
      ComponentCustomizer.customizeTable(this);
   }

   public SortStatus getSortStatus() {
      List var1 = this.getRowSorter().getSortKeys();
      if (var1.size() == 1) {
         SortKey var2 = (SortKey)var1.get(0);
         OperationLogTableColumn var3 = (OperationLogTableColumn)((OperationLogTableModel)this.getModel()).getColumnDescriptor(var2.getColumn());
         return new SortStatus(var3, var2.getSortOrder());
      } else {
         return null;
      }
   }
}
