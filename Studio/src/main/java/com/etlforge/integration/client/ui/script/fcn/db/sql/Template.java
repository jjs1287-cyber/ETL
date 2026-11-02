package com.etlforge.integration.client.ui.script.fcn.db.sql;

import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.integration.client.database.ui.sqlbuilder.NameGenerator;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

public enum Template {
   SELECT {
      @Override
      InsertionData getTableTemplate(String var1) {
         String var2 = "SELECT *\nFROM " + var1 + "\nWHERE ";
         int var3 = var2.indexOf(42);
         int var4 = var3 + 1;
         return new InsertionData(var2, var3, var4);
      }

      @Override
      InsertionData getColumnTemplate(NameGenerator var1, List<DatabaseColumn> var2) {
         String var3 = Template.getTableNameFromColumns(var1, var2);
         String var4 = Utils.concatenateColumnNames(var1, var2);
         return new InsertionData("SELECT " + var4 + "\nFROM " + var3 + "\nWHERE ");
      }
   },
   INSERT {
      @Override
      InsertionData getTableTemplate(String var1) {
         String var2 = "INSERT INTO " + var1 + "\nVALUES ()";
         int var3 = var2.length() - 1;
         return new InsertionData(var2, var3);
      }

      @Override
      InsertionData getColumnTemplate(NameGenerator var1, List<DatabaseColumn> var2) {
         String var3 = Template.getTableNameFromColumns(var1, var2);
         String var4 = Utils.concatenateColumnNames(var1, var2);
         String var5 = "INSERT INTO " + var3 + " (" + var4 + ")\nVALUES ()";
         int var6 = var5.length() - 1;
         return new InsertionData(var5, var6);
      }
   },
   UPDATE {
      @Override
      InsertionData getTableTemplate(String var1) {
         return new InsertionData("UPDATE " + var1 + "\nSET \nWHERE ");
      }

      @Override
      InsertionData getColumnTemplate(NameGenerator var1, List<DatabaseColumn> var2) {
         String var3 = Template.getTableNameFromColumns(var1, var2);
         StringBuilder var4 = new StringBuilder("UPDATE ");
         var4.append(var3).append("\nSET ");
         boolean var5 = true;
         int var6 = -1;

         for (DatabaseColumn var8 : var2) {
            if (!var5) {
               var4.append(",\n");
            }

            var4.append(var1.getColumnName(var8)).append("=");
            if (var5) {
               var6 = var4.length();
            }

            var5 = false;
         }

         var4.append("\nWHERE ");
         return new InsertionData(var4.toString(), var6);
      }
   },
   DELETE {
      @Override
      InsertionData getTableTemplate(String var1) {
         return new InsertionData("DELETE FROM " + var1 + "\n WHERE ");
      }

      @Override
      InsertionData getColumnTemplate(NameGenerator var1, List<DatabaseColumn> var2) {
         String var3 = Template.getTableNameFromColumns(var1, var2);
         StringBuilder var4 = new StringBuilder("DELETE FROM ");
         var4.append(var3).append("\nWHERE ");
         boolean var5 = true;
         int var6 = -1;

         for (DatabaseColumn var8 : var2) {
            if (!var5) {
               var4.append("\nAND ");
            }

            var4.append(var1.getColumnName(var8)).append("=");
            if (var5) {
               var6 = var4.length();
            }

            var5 = false;
         }

         return new InsertionData(var4.toString(), var6);
      }
   },
   OTHER {
      @Override
      InsertionData getTableTemplate(String var1) {
         return new InsertionData(var1);
      }

      @Override
      InsertionData getColumnTemplate(NameGenerator var1, List<DatabaseColumn> var2) {
         String var3 = Utils.concatenateColumnNames(var1, var2);
         return new InsertionData(var3);
      }
   };

   Template() {
   }

   abstract InsertionData getTableTemplate(String var1);

   abstract InsertionData getColumnTemplate(NameGenerator var1, List<DatabaseColumn> var2);

   private static String getTableNameFromColumns(NameGenerator var0, List<DatabaseColumn> var1) {
      Preconditions.checkArgument(!var1.isEmpty(), "columns cannot be empty");
      DatabaseColumn var2 = (DatabaseColumn)var1.get(0);
      DatabaseObject var3 = var2.getDatabaseObject();
      return var0.getTableName(var3);
   }
}
