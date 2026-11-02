package org.etlforge.integration.client.ui.interchange.entity.transformation.database;

import org.etlforge.integration.client.ui.structure.editor.db.MultipleTableWhereClausePageUi;
import org.etlforge.integration.client.ui.structure.editor.db.SingleTableWhereClausePageUi;
import org.etlforge.integration.client.ui.structure.editor.db.TransformationDatabaseInfo;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.data.structure.database.SqlScript;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiCanvas;

public final class WhereClauseEditorUiFactory {
   public static WhereClauseEditorUi createEditorUi(TransformationDatabaseInfo var0) {
      DatabaseStructure var1 = var0.getStructure();
      DatabaseObject[] var2 = var0.getDatabaseObjects();
      switch (var2.length) {
         case 0:
            return null;
         case 1:
            SqlScript var3 = var1.getSqlScript();
            String var4 = var3 == null ? "" : var3.getWhereClause();
            BeginEndQuote var5 = var1.getBeginEndQuote();
            return new WhereClauseEditorUiFactory.SingleTableEditorUi(var2[0], var4, var5);
         default:
            DbTranFilters var6 = var1.getHierarchicalFilters();
            return new WhereClauseEditorUiFactory.MultipleTableEditorUi(var2, var6);
      }
   }

   private WhereClauseEditorUiFactory() {
   }

   private static final class MultipleTableEditorUi implements WhereClauseEditorUi {
      private final MultipleTableWhereClausePageUi ui;

      public MultipleTableEditorUi(DatabaseObject[] var1, DbTranFilters var2) {
         this.ui = new MultipleTableWhereClausePageUi(var1);
         this.ui.setFilters(var2);
      }

      @Override
      public WhereClauseEditorUi.State getMemento() {
         return new WhereClauseEditorUiFactory.MultipleTableEditorUi.StateImpl(this.ui.getDefinedFilters());
      }

      @Override
      public void setMemento(WhereClauseEditorUi.State var1) {
         if (!(var1 instanceof WhereClauseEditorUiFactory.MultipleTableEditorUi.StateImpl)) {
            throw new IllegalArgumentException("Invalid state: " + (var1 != null ? var1.getClass().getName() : null));
         }

         this.ui.setFilters(((WhereClauseEditorUiFactory.MultipleTableEditorUi.StateImpl)var1).filters);
      }

      @Override
      public void applyTo(Transformation var1) {
         DataStructure var2 = var1.getInputStructure();
         if (var2 instanceof DatabaseStructure) {
            DatabaseStructure var3 = (DatabaseStructure)var2;
            var3.setHierarchicalFilters(this.ui.getDefinedFilters());
         }
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.ui.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.ui.requestFocus();
      }

      @Override
      public void terminateEdit() {
         this.ui.terminateEdit();
      }

      private static final class StateImpl implements WhereClauseEditorUi.State {
         private final DbTranFilters filters;

         public StateImpl(DbTranFilters var1) {
            this.filters = var1;
         }

         @Override
         public boolean equals(Object var1) {
            if (var1 == this) {
               return true;
            } else {
               return var1 instanceof WhereClauseEditorUiFactory.MultipleTableEditorUi.StateImpl
                  ? this.filters.equals(((WhereClauseEditorUiFactory.MultipleTableEditorUi.StateImpl)var1).filters)
                  : false;
            }
         }

         @Override
         public int hashCode() {
            return this.filters.hashCode();
         }
      }
   }

   private static final class SingleTableEditorUi implements WhereClauseEditorUi {
      private final SingleTableWhereClausePageUi ui;

      public SingleTableEditorUi(DatabaseObject var1, String var2, BeginEndQuote var3) {
         this.ui = new SingleTableWhereClausePageUi(var1);
         this.ui.setBeginEndQuotes(var3);
         this.ui.setWhereClause(var2);
      }

      @Override
      public WhereClauseEditorUi.State getMemento() {
         return new WhereClauseEditorUiFactory.SingleTableEditorUi.StateImpl(this.ui.getWhereClause());
      }

      @Override
      public void setMemento(WhereClauseEditorUi.State var1) {
         if (!(var1 instanceof WhereClauseEditorUiFactory.SingleTableEditorUi.StateImpl)) {
            throw new IllegalArgumentException("Invalid state: " + (var1 != null ? var1.getClass().getName() : null));
         }

         this.ui.setWhereClause(((WhereClauseEditorUiFactory.SingleTableEditorUi.StateImpl)var1).whereClause);
      }

      @Override
      public void applyTo(Transformation var1) {
         DataStructure var2 = var1.getInputStructure();
         if (var2 instanceof DatabaseStructure) {
            DatabaseStructure var3 = (DatabaseStructure)var2;
            SqlScript var4 = var3.getSqlScript();
            var4.updateWhereClause(this.ui.getWhereClause());
            var3.setSqlScript(var4);
         }
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.ui.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.ui.requestFocus();
      }

      @Override
      public void terminateEdit() {
      }

      private static final class StateImpl implements WhereClauseEditorUi.State {
         private final String whereClause;

         public StateImpl(String var1) {
            this.whereClause = var1;
         }

         @Override
         public boolean equals(Object var1) {
            if (var1 == this) {
               return true;
            } else {
               return var1 instanceof WhereClauseEditorUiFactory.SingleTableEditorUi.StateImpl
                  ? this.whereClause.equals(((WhereClauseEditorUiFactory.SingleTableEditorUi.StateImpl)var1).whereClause)
                  : false;
            }
         }

         @Override
         public int hashCode() {
            return this.whereClause.hashCode();
         }
      }
   }
}
