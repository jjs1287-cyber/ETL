package org.etlforge.integration.client.ui.entity;

import javax.swing.JLabel;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.base.ListenerReport;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class ListenerReportTable implements UiProvider {
   private final KongaRowTable table;
   private final JLabel statusLabel;

   public ListenerReportTable(IntegrationEntity var1) {
      this(var1.reportListeners());
   }

   public ListenerReportTable(ListenerReport var1) {
      this.table = this.createTable(var1);
      this.statusLabel = this.createStatusLabel();
      this.installStatusLabel();
   }

   @Override
   public void decorate(UiCanvas var1) {
      new BorderLayoutBuilder().center(this.table).south(this.statusLabel).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private KongaRowTable createTable(ListenerReport var1) {
      KongaTable var2 = new KongaTable(new ListenerReportTable.Model(var1));
      var2.setAutoCreateRowSorter(true);
      TableStyles.normal().makeOver(var2);
      return new KongaRowTable(var2);
   }

   private JLabel createStatusLabel() {
      KongaLabel var1 = TextStyles.InfoText.makeLabel(" ");
      var1.setHorizontalAlignment(10);
      return var1;
   }

   private void installStatusLabel() {
      this.table.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            int var2 = ListenerReportTable.this.table.getSelection().size();
            if (var2 == 0) {
               ListenerReportTable.this.statusLabel.setText(" ");
            } else if (var2 == 1) {
               ListenerReportTable.this.statusLabel.setText("1 row selected");
            } else {
               ListenerReportTable.this.statusLabel.setText(var2 + " rows selected");
            }
         }
      });
   }

   private static class Model extends TypedAbstractKongaTableModel<ListenerReport.Item> {
      public static final TableColumnMetaData CATEGORY = new TableColumnMetaData("Category", String.class, false, 150);
      public static final TableColumnMetaData LISTENER_CLASS = new TableColumnMetaData("Class", Class.class, false, 300);
      public static final TableColumnMetaData TO_STRING = new TableColumnMetaData("toString()", String.class, false, 200);

      public Model(ListenerReport var1) {
         super(ListenerReport.Item.class, CATEGORY, LISTENER_CLASS, TO_STRING);

         for (String var3 : var1.getCategoriesAlphabetically()) {
            this.addRows(var1.getItems(var3));
         }
      }

      protected Object[] getColumnDataForTypedObject(ListenerReport.Item var1) {
         return new Object[]{var1.getCategory(), var1.getListener().getClass(), var1.getListener().toString()};
      }
   }
}
