package org.etlforge.integration.client.database.ui.sqlbuilder;

import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;

public interface DbObjectsModelListener {
   void modelChanged(DbObjectsModel var1);

   void tableChanged(DbObjectsModel var1, DatabaseObject var2);

   void beginEndQuotesChanged(DbObjectsModel var1, BeginEndQuote var2);

   abstract class Adapter implements DbObjectsModelListener {
      @Override
      public void modelChanged(DbObjectsModel var1) {
      }

      @Override
      public void tableChanged(DbObjectsModel var1, DatabaseObject var2) {
      }

      @Override
      public void beginEndQuotesChanged(DbObjectsModel var1, BeginEndQuote var2) {
      }
   }
}
