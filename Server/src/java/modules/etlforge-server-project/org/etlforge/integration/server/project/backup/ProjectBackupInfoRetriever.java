package org.etlforge.integration.server.project.backup;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.db.trandb.ProjectBackupTab;
import org.etlforge.integration.server.db.trandb.ProjectBackupTabRow;
import org.etlforge.integration.server.db.trandb.UsersTab;
import org.etlforge.integration.server.db.trandb.UsersTabRow;
import org.etlforge.util.id.KongaGuid;

public class ProjectBackupInfoRetriever {
   private final Connection connection;

   public ProjectBackupInfoRetriever(Connection var1) {
      Preconditions.checkNotNull(var1, "connection");
      this.connection = var1;
   }

   public List<ProjectBackupDescriptor> listBackups(IntegrationProjectId var1) {
      ProjectBackupTab var2 = null;
      ProjectBackupInfoRetriever.Converter var3 = null;

      try {
         var2 = new ProjectBackupTab(this.connection);
         List var4 = var2.selectBackupsForProject(var1);
         if (!var4.isEmpty()) {
            File var5 = new File(ServerConfig.getServerHome());
            var3 = new ProjectBackupInfoRetriever.Converter(this.connection, var5);
            return KongaListUtils.transform(var4, var3);
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         if (var3 != null) {
            var3.close();
         }
      }

      return Lists.newArrayList();
   }

   private static class Converter implements Function<ProjectBackupTabRow, ProjectBackupDescriptor> {
      private final Connection connection;
      private final File kongaHome;
      private UsersTab usersTab;

      public Converter(Connection var1, File var2) {
         this.connection = var1;
         this.kongaHome = var2;
      }

      public ProjectBackupDescriptor apply(ProjectBackupTabRow var1) {
         ProjectBackupDescriptor.Builder var2 = ProjectBackupDescriptor.builder();
         var2.setBackupId(var1.get_projectbackup_id().intValue())
            .setProjectId(new IntegrationProjectId(var1.get_interchangeproject_guid()))
            .setServerId(new ServerGuid(KongaGuid.createGuid().toString()))
            .setFilePath(new File(this.kongaHome, var1.get_uploadedfilename()).getAbsolutePath())
            .setBackupDate(var1.get_uploadtime())
            .setTag(var1.get_tag())
            .setUser(this.getUserName(var1));
         return var2.build();
      }

      public void close() {
         if (this.usersTab != null) {
            this.usersTab.close();
         }
      }

      private String getUserName(ProjectBackupTabRow var1) {
         String var2 = var1.get_uploadedby_user_guid();
         if (StringUtils.isNotEmpty(var2)) {
            if (this.usersTab == null) {
               this.usersTab = new UsersTab(this.connection);
            }

            try {
               UsersTabRow var3 = this.usersTab.query(var2);
               if (var3 != null) {
                  return var3.get_login();
               }
            } catch (SQLException var4) {
               var4.printStackTrace();
            }
         }

         return null;
      }
   }
}
