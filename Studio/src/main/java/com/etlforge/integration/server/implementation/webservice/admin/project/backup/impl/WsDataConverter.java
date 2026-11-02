package com.etlforge.integration.server.implementation.webservice.admin.project.backup.impl;

import com.google.common.base.Function;
import com.etlforge.integration.server.implementation.webservice.projectadmin.backup.client.WsProjectBackupDescriptor;
import org.etlforge.integration.common.server.ServerConversionUtils;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;

final class WsDataConverter {
   public static final Function<WsProjectBackupDescriptor, ProjectBackupDescriptor> BACKUP_DESCRIPTOR = new Function<WsProjectBackupDescriptor, ProjectBackupDescriptor>() {
      public ProjectBackupDescriptor apply(WsProjectBackupDescriptor var1) {
         ProjectBackupDescriptor.Builder var2 = ProjectBackupDescriptor.builder();
         var2.setBackupId(var1.getId())
            .setProjectId(new IntegrationProjectId(var1.getProjectId()))
            .setBackupDate(ServerConversionUtils.fromServerDateFormat(var1.getDateOfBackup()))
            .setServerId(new ServerGuid(var1.getServerId()))
            .setFilePath(var1.getFilePath())
            .setUser(var1.getUserName())
            .setTag(var1.getTag());
         return var2.build();
      }
   };

   private WsDataConverter() {
   }
}
