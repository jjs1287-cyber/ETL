package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JComponent;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.widget.DropDownPanel;

public final class MultipleTableWhereClausePageUi implements UiProvider {
   private final List<WhereClauseTable> tables;
   private final OneColumnPanel displayer;

   public MultipleTableWhereClausePageUi(DatabaseObject[] var1) {
      this.tables = this.createTables(var1);
      this.displayer = this.createDisplayer();
   }

   private List<WhereClauseTable> createTables(DatabaseObject[] var1) {
      ArrayList var2 = Lists.newArrayList();

      for (DatabaseObject var6 : var1) {
         var2.add(new WhereClauseTable(var6));
      }

      Collections.sort(var2, new Comparator<WhereClauseTable>() {
         public int compare(WhereClauseTable var1, WhereClauseTable var2x) {
            return var1.getDbObject().getName().compareToIgnoreCase(var2x.getDbObject().getName());
         }
      });
      return var2;
   }

   private OneColumnPanel createDisplayer() {
      OneColumnPanel var1 = new OneColumnPanel().nonOpaque().withInsets(2, 0, 10, 0).withBorder(Empty.border(0, 5, 5, 20)).setFill(OneColumnPanel.BOTH);
      var1.setWeightX(1.0).setWeightY(1.0 / this.tables.size());

      for (WhereClauseTable var3 : this.tables) {
         UiProvider var4 = var3.getUi();
         DropDownPanel var5 = new DropDownPanel(BorderLayoutBuilder.fillWith(var4), var3.getDbObject().getName(), true);
         var5.setOpaque(false);
         var1.add(var5.getDisplayer());
      }

      return var1;
   }

   public void terminateEdit() {
      for (WhereClauseTable var2 : this.tables) {
         var2.terminateEdit();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.displayer.decorate(var1);
   }

   @Override
   public void requestFocus() {
      JComponent var1 = this.getComponentToFocus();
      if (var1 != null) {
         var1.requestFocusInWindow();
      }
   }

   private JComponent getComponentToFocus() {
      return null;
   }

   public DbTranFilters getDefinedFilters() {
      DbTranFilters var1 = new DbTranFilters();

      for (WhereClauseTable var3 : this.tables) {
         var3.provideDefinedFilters(var1);
      }

      return var1;
   }

   public void setFilters(DbTranFilters var1) {
      for (WhereClauseTable var3 : this.tables) {
         var3.clear();
      }

      for (DbTranFilters.Entry var6 : var1.getAllEntries()) {
         WhereClauseTable var4 = this.getTable(var6.getTableName());
         if (var4 != null) {
            var4.addFilter(var6.getColumnName(), var6.getCondition(), var6.getSortOrder());
         }
      }
   }

   private WhereClauseTable getTable(String var1) {
      for (WhereClauseTable var3 : this.tables) {
         if (var3.getDbObject().getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
   }
}
