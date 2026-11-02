package org.etlforge.application.ui.log;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SortOrder;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.log.LogLevelSelector;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

final class ApplicationLogLevelsTable implements UiProvider {
   private final KongaTable table;

   public ApplicationLogLevelsTable(Collection<Logger> var1) {
      this.table = this.createTable(var1);
   }

   private KongaTable createTable(Collection<Logger> var1) {
      ApplicationLogLevelsTable.Model var2 = new ApplicationLogLevelsTable.Model(var1);
      KongaTable var3 = new KongaTable(var2);
      int var4 = Math.max(var2.getRowCount(), 15);
      var3.setVisibleRowCount(var4);
      var3.setAutoCreateRowSorter(true);
      var3.setSortingStatus(ApplicationLogLevelsTable.Model.NAME, SortOrder.ASCENDING);
      var3.setCellEditorFor(ApplicationLogLevelsTable.Model.LEVEL, new LogLevelSelector().asTableEditor());
      this.installTableStyle(var3);
      return var3;
   }

   private void installTableStyle(KongaTable var1) {
      TableStyle var2 = TableStyles.wide();
      var2.setGridMode(TableGridMode.BOTH);
      var2.makeOver(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocusInWindow();
   }

   public void apply() {
      ((ApplicationLogLevelsTable.Model)this.table.getModel()).apply();
   }

   public void reset() {
      ((ApplicationLogLevelsTable.Model)this.table.getModel()).reset();
   }

   public Logger[] getDisplayedLoggers() {
      ArrayList var1 = Lists.newArrayList();
      ApplicationLogLevelsTable.Model var2 = (ApplicationLogLevelsTable.Model)this.table.getModel();

      for (int var3 = 0; var3 < var2.getRowCount(); var3++) {
         var1.add(var2.getRowObjectAt(var3));
      }

      return (Logger[]) var1.toArray(new Logger[var1.size()]);
   }

   public void setEnabled(boolean var1) {
      this.table.setEnabled(var1);
      this.table.setForeground(var1 ? Color.BLACK : Color.GRAY);
   }

   private static final class Model extends TypedAbstractKongaTableModel<Logger> {
      private static final TableColumnMetaData NAME = new TableColumnMetaData("Logger", String.class, false, 300);
      private static final TableColumnMetaData LEVEL = new TableColumnMetaData("Level", Level.class, true, 75);

      public Model(Collection<Logger> var1) {
         super(Logger.class, new TableColumnMetaData[]{NAME, LEVEL}, var1);
      }

      public void apply() {
         for (int var1 = 0; var1 < this.getRowCount(); var1++) {
            Level var2 = (Level)this.getValueAt(var1, LEVEL);
            this.getRowObjectAt(var1).setLevel(var2);
         }
      }

      public void reset() {
         for (int var1 = 0; var1 < this.getRowCount(); var1++) {
            Level var2 = this.getRowObjectAt(var1).getLevel();
            this.setValueAt(var2, var1, LEVEL);
         }
      }

      protected Object[] getColumnDataForTypedObject(Logger var1) {
         return new Object[]{var1.getName(), var1.getLevel()};
      }
   }
}
