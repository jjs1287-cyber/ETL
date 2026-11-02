package com.etlforge.integration.server.implementation.webservice.projectadmin.backup;

import com.google.common.base.Function;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.common.server.ServerDateFormat;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.util.text.ThreadSafeDateFormat;

final class ResponseConverter {
   private static final ThreadSafeDateFormat dateFormat = new ServerDateFormat().getThreadSafeFormatter();
   public static final Function<ProjectBackupDescriptor, WsProjectBackupDescriptor> BACKUP_DESCRIPTOR = new Function<ProjectBackupDescriptor, WsProjectBackupDescriptor>() {
      public WsProjectBackupDescriptor apply(ProjectBackupDescriptor var1) {
         WsProjectBackupDescriptor var2 = new WsProjectBackupDescriptor();
         var2.setId(var1.getBackupId());
         var2.setProjectId(var1.getProjectId().toString());
         var2.setServerId(var1.getServerId().toString());
         var2.setUserName(var1.getCreatedBy());
         var2.setDateOfBackup(ResponseConverter.dateFormat.format(var1.getDateOfBackup()));
         var2.setFilePath(var1.getFilePath());
         var2.setTag(var1.getTag());
         return var2;
      }
   };
   public static final Function<List<ProjectBackupDescriptor>, WsGetBackupListResult> BACKUP_LIST = new Function<List<ProjectBackupDescriptor>, WsGetBackupListResult>() {
      public WsGetBackupListResult apply(List<ProjectBackupDescriptor> var1) {
         List var2 = KongaListUtils.transform(var1, ResponseConverter.BACKUP_DESCRIPTOR);
         WsGetBackupListResult var3 = new WsGetBackupListResult();
         var3.setBackups((WsProjectBackupDescriptor[]) var2.toArray(new WsProjectBackupDescriptor[var2.size()]));
         return var3;
      }
   };

   private ResponseConverter() {
   }
}
