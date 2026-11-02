package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.etlforge.integration.data.entity.id.TransformationId;

public final class TransformationsTab extends TranDbTable {
   public TransformationsTab() {
      this(null);
   }

   public TransformationsTab(Connection var1) {
      super("transformationstab_new", var1);
   }

   public TransformationsTabRow query(final long var1) throws SQLException {
      TransformationsTab.PreparedStatementExecuterImpl var3 = new TransformationsTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setLong(1, var1);
         }
      };
      return var3.selectSingleRowWithFilter("transformation_id = ?");
   }

   public TransformationsTabRow query(final TransformationId var1) throws SQLException {
      TransformationsTab.PreparedStatementExecuterImpl var2 = new TransformationsTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setString(1, var1.toString());
         }
      };
      return var2.selectSingleRow("SELECT * FROM guidsfortransformationsview WHERE guid = ?");
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<TransformationsTabRow> {
      public PreparedStatementExecuterImpl() {
         super(TransformationsTab.this);
      }

      protected TransformationsTabRow createEmptyRow() {
         return new TransformationsTabRow();
      }
   }
}
