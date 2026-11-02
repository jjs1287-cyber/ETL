package org.etlforge.integration.client.database.serverapi;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.OdbcDriverDescriptorFactory;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.DatabaseSynonym;
import org.etlforge.integration.database.info.DatabaseTable;
import org.etlforge.integration.database.info.DatabaseView;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;

public final class MockOdbcInfoProvider implements OdbcInfoProvider {
   private static final String[] TABLE_NAMES = new String[]{
      "Categories",
      "Customers",
      "Employees",
      "EmployeeTerritories",
      "Order Details",
      "Orders",
      "Products",
      "Region",
      "Shippers",
      "SimpleTab",
      "Suppliers",
      "Territories"
   };
   private static final String[] VIEW_NAMES = new String[]{
      "Current Product List", "Invoices", "Order Details Extended", "Order Subtotals", "Order Qry", "Sales by Category", "Sales Totals by Amount"
   };
   private static final String[] SYNONYM_NAMES = new String[]{"Beautiful_Handsome", "Quiet_Calm", "Fast_Quick"};

   @Override
   public void getTableInfo(ConnectionParams var1, SourceId var2, TargetId var3, DatabaseObject[] var4, GetTableInfoCallback var5) {
      try {
         DatabaseObjectContainer[] var6 = new DatabaseObjectContainer[var4.length];
         int var7 = 0;

         for (DatabaseObject var11 : var4) {
            var6[var7++] = this.createContainer(DatabaseObjectType.TABLE, var11.getName());
         }

         var5.tableInfoRetrieved(var6);
      } catch (IntegrationServerException var12) {
         var5.caught(var12);
      }
   }

   @Override
   public void getAllDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, GetDatabaseObjectCallback var4) {
      this.getDatabaseObjects(var1, var2, var3, TableSearchFilter.ALL_OBJECTS, var4);
   }

   public static List<DatabaseTable> getMockTables() {
      return createTables();
   }

   @Override
   public void getDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, TableSearchFilter var4, GetDatabaseObjectCallback var5) {
      try {
         String var6 = var4.getSearchString().toLowerCase();
         EnumSet var7 = var4.getObjectTypes();
         DatabaseObjectContainer[] var8 = new DatabaseObjectContainer[var7.size()];
         int var9 = 0;

         for (Object __etlItem74 : var7) {
            DatabaseObjectType var11 = (DatabaseObjectType) __etlItem74;
            var8[var9++] = this.createContainer(var11, var6);
         }

         var5.objectsRetrieved(var8, new BeginEndQuote("\"", "\""));
      } catch (IntegrationServerException var12) {
         var5.caught(var12);
      }
   }

   @Override
   public void validateWhereClause(DatabaseObject var1, String var2, WhereClauseValidationCallback var3) {
      var3.whereClauseIsValid();
   }

   @Override
   public void getDrivers(OdbcDriverListCallback var1) {
      ArrayList var2 = new ArrayList();
      var2.add(OdbcDriverDescriptorFactory.createDescriptor("PostgreSQL", "\"", "\""));
      var2.add(OdbcDriverDescriptorFactory.createDescriptor("MySQL", "`", "`"));
      var1.driversRetrieved(var2, null);
   }

   private DatabaseObjectContainer createContainer(DatabaseObjectType var1, String var2) throws IntegrationServerException {
      switch (var1) {
         case TABLE:
            DatabaseObjectContainer var3 = new DatabaseObjectContainer(var1);

            for (DatabaseTable var10 : createTables()) {
               if (match(var10.getName(), var2)) {
                  var3.addObject(var10);
               }
            }

            return var3;
         case VIEW:
            DatabaseObjectContainer var4 = new DatabaseObjectContainer(var1);

            for (DatabaseView var11 : createViews()) {
               if (match(var11.getName(), var2)) {
                  var4.addObject(var11);
               }
            }

            return var4;
         case SYNONYM:
            DatabaseObjectContainer var5 = new DatabaseObjectContainer(var1);

            for (DatabaseSynonym var7 : createSynonyms()) {
               if (match(var7.getName(), var2)) {
                  var5.addObject(var7);
               }
            }

            return var5;
         default:
            throw new IntegrationServerException("Unsupported database object type: " + var1);
      }
   }

   private static boolean match(String var0, String var1) {
      return var1 == null || var1.length() == 0 || var0.toLowerCase().indexOf(var1) >= 0;
   }

   private static List<DatabaseTable> createTables() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < TABLE_NAMES.length; var1++) {
         DatabaseTable var2 = new DatabaseTable(TABLE_NAMES[var1], "");

         for (DatabaseColumn var4 : createColumns(var2, var1 % 2 == 0)) {
            var2.addColumn(var4);
         }

         var0.add(var2);
      }

      return var0;
   }

   private static List<DatabaseView> createViews() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < VIEW_NAMES.length; var1++) {
         DatabaseView var2 = new DatabaseView(VIEW_NAMES[var1], "");

         for (DatabaseColumn var4 : createColumns(var2, var1 % 2 == 0)) {
            var2.addColumn(var4);
         }

         var0.add(var2);
      }

      return var0;
   }

   private static List<DatabaseSynonym> createSynonyms() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < SYNONYM_NAMES.length; var1++) {
         DatabaseSynonym var2 = new DatabaseSynonym(SYNONYM_NAMES[var1], "");

         for (DatabaseColumn var4 : createColumns(var2, var1 % 2 == 0)) {
            var2.addColumn(var4);
         }

         var0.add(var2);
      }

      return var0;
   }

   private static List<DatabaseColumn> createColumns(DatabaseObject var0, boolean var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < 6; var3++) {
         DatabaseColumn var4 = new DatabaseColumn(var0, "Column " + (var3 + 1), null, 0, null, true, var1 && var3 == 0, 0, 0);
         var2.add(var4);
      }

      return var2;
   }
}
