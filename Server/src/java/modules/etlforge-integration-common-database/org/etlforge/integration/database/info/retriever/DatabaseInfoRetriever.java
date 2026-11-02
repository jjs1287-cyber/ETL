package org.etlforge.integration.database.info.retriever;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseInfo;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseTable;
import org.etlforge.integration.database.info.DatabaseView;

public final class DatabaseInfoRetriever extends DatabaseAccessor {
   private TableSearchFilter searchFilter = TableSearchFilter.ALL_OBJECTS;

   public DatabaseInfo getInfo() throws SQLException {
      Connection var1 = this.getConnection();
      if (var1 == null) {
         throw new IllegalStateException("No connection");
      }

      try {
         DatabaseMetaData var2 = var1.getMetaData();
         List var3 = this.collectTables(var2);
         BeginEndQuote var4 = this.getBeginEndQuotes(var2);
         return new DatabaseInfo(var3, var4);
      } finally {
         KongaDbUtils.close(var1);
      }
   }

   private List<DatabaseObject> collectTables(DatabaseMetaData var1) throws SQLException {
      ArrayList var2 = Lists.newArrayList();
      ResultSet var3 = var1.getTables(null, null, this.searchFilter.getSearchString(), this.searchFilter.getObjectTypeNames());

      try {
         while (var3.next()) {
            DatabaseObject var4 = this.createTableObject(var3);
            if (var4 != null) {
               var2.add(var4);
            }
         }

         return var2;
      } finally {
         KongaDbUtils.close(var3);
      }
   }

   private DatabaseObject createTableObject(ResultSet var1) throws SQLException {
      String var2 = var1.getString(2);
      String var3 = var1.getString(3);
      String var4 = var1.getString(4);
      if ("TABLE".equals(var4)) {
         return new DatabaseTable(var3, var2);
      } else {
         return "VIEW".equals(var4) ? new DatabaseView(var3, var2) : null;
      }
   }

   private BeginEndQuote getBeginEndQuotes(DatabaseMetaData var1) throws SQLException {
      String var2 = var1.getIdentifierQuoteString();
      return new BeginEndQuote(var2, var2);
   }

   public void setSearchFilter(TableSearchFilter var1) {
      this.searchFilter = var1 != null ? var1 : TableSearchFilter.ALL_OBJECTS;
   }

   public TableSearchFilter getSearchFilter() {
      return this.searchFilter;
   }
}
