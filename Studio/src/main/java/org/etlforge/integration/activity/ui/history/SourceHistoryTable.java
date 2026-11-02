package org.etlforge.integration.activity.ui.history;

import java.util.Date;
import javax.swing.SortOrder;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentCustomizer;
import org.etlforge.integration.activity.ui.ActivityViewUtils;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.renderer.FileSizeRenderer;
import org.etlforge.util.StorageSizeUnit;

final class SourceHistoryTable extends KongaTable {
   public SourceHistoryTable(boolean var1) {
      this(SourceHistoryTableModel.emptyModel(var1));
   }

   public SourceHistoryTable(SourceHistoryTableModel var1) {
      super(var1);
      this.setAutoCreateRowSorter(true);
      this.setInitialSortOrder();
      this.getTableHeader().setReorderingAllowed(false);
      this.setSelectionMode(0);
      this.setRenderers();
      this.setStyle();
   }

   private void setInitialSortOrder() {
      this.setSortingStatus(SourceHistoryTableModel.TIMESTAMP, SortOrder.DESCENDING);
   }

   private void setStyle() {
      TableStyle var1 = TableStyles.normal();
      var1.setGridMode(TableGridMode.BOTH);
      var1.makeOver(this);
   }

   private void setRenderers() {
      ComponentCustomizer.customizeTable(this);
      this.setDefaultRenderer(Date.class, ActivityViewUtils.getDateCellRenderer());
      this.setCellRendererFor(SourceHistoryTableModel.FILE_SIZE, new FileSizeRenderer(StorageSizeUnit.Kilobytes));
   }
}
