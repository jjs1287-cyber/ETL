package org.etlforge.integration.common.server.project.backup;

import com.google.common.base.Preconditions;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;

public final class ProjectBackupDescriptor {
   private final ProjectBackupDescriptor.Struct struct;

   public static ProjectBackupDescriptor.Builder builder() {
      return new ProjectBackupDescriptor.Builder();
   }

   private ProjectBackupDescriptor(ProjectBackupDescriptor.Struct var1) {
      this.struct = var1;
   }

   public long getBackupId() {
      return this.struct.id;
   }

   public IntegrationProjectId getProjectId() {
      return this.struct.projectId;
   }

   public ServerGuid getServerId() {
      return this.struct.serverId;
   }

   public String getFilePath() {
      return this.struct.filePath;
   }

   public Date getDateOfBackup() {
      return (Date)this.struct.backupDate.clone();
   }

   public String getCreatedBy() {
      return this.struct.userName;
   }

   public String getTag() {
      return this.struct.tag;
   }

   public static final class Builder {
      private ProjectBackupDescriptor.Struct data = new ProjectBackupDescriptor.Struct();

      public ProjectBackupDescriptor build() {
         this.data.check();
         return new ProjectBackupDescriptor(this.data);
      }

      public ProjectBackupDescriptor.Builder reset() {
         this.data = new ProjectBackupDescriptor.Struct();
         return this;
      }

      public ProjectBackupDescriptor.Builder setBackupId(long var1) {
         this.data.id = var1;
         return this;
      }

      public ProjectBackupDescriptor.Builder setProjectId(IntegrationProjectId var1) {
         this.data.projectId = var1;
         return this;
      }

      public ProjectBackupDescriptor.Builder setServerId(ServerGuid var1) {
         this.data.serverId = var1;
         return this;
      }

      public ProjectBackupDescriptor.Builder setFilePath(String var1) {
         this.data.filePath = var1;
         return this;
      }

      public ProjectBackupDescriptor.Builder setBackupDate(Date var1) {
         this.data.backupDate = (Date)var1.clone();
         return this;
      }

      public ProjectBackupDescriptor.Builder setUser(String var1) {
         this.data.userName = var1;
         return this;
      }

      public ProjectBackupDescriptor.Builder setTag(String var1) {
         this.data.tag = var1;
         return this;
      }
   }

   private static final class Struct {
      public Long id;
      public IntegrationProjectId projectId;
      public ServerGuid serverId;
      public String filePath;
      public Date backupDate;
      public String userName;
      public String tag;

      private Struct() {
      }

      public void check() {
         Preconditions.checkState(this.id != null, "must provide a backup ID");
         Preconditions.checkState(this.projectId != null, "must provide a project ID");
         Preconditions.checkState(this.serverId != null, "must provide a server ID");
         Preconditions.checkState(StringUtils.isNotBlank(this.filePath), "must provide a file path");
         Preconditions.checkState(this.backupDate != null, "must provide a backup date");
      }
   }
}
