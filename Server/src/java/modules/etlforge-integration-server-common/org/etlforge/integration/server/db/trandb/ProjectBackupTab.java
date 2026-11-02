package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;

public class ProjectBackupTab extends TranDbTable {
   private static final String TABLE_NAME = "projectbackuptab";

   public ProjectBackupTab() {
      this(null);
   }

   public ProjectBackupTab(Connection var1) {
      super("projectbackuptab", var1);
   }

   public List<ProjectBackupTabRow> selectBackupsForProject(final IntegrationProjectId var1) throws SQLException {
      ProjectBackupTab.PreparedStatementExecuterImpl var2 = new ProjectBackupTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setString(1, var1.toString());
         }
      };
      return var2.selectSeveralRows("SELECT * FROM projectbackuptab WHERE InterchangeProject_Guid = ?");
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<ProjectBackupTabRow> {
      public PreparedStatementExecuterImpl() {
         super(ProjectBackupTab.this);
      }

      protected ProjectBackupTabRow createEmptyRow() {
         return new ProjectBackupTabRow();
      }
   }
}
