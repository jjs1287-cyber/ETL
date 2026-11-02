package com.etlforge.integration.server.implementation.webservice.admin.project.backup.impl;

import com.etlforge.integration.client.admin.project.DownloadBackupCallback;
import com.etlforge.integration.client.admin.project.GetListOfBackupsCallback;
import com.etlforge.integration.client.admin.project.ProjectBackupAdministration;
import com.etlforge.integration.client.admin.server.DownloadServerFileCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.server.implementation.webservice.projectadmin.backup.client.ProjectBackupAdministrationServiceLocator;
import com.etlforge.integration.server.implementation.webservice.projectadmin.backup.client.WsGetBackupListResult;
import java.io.File;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.lang.Arrays2;

public class ProjectBackupAdministrationWsImpl extends JavaWebServiceServerCall implements ProjectBackupAdministration {
   private static final String SERVICE_NAME = "konga_projectbackupadmin";
   private String user;
   private String password;

   public ProjectBackupAdministrationWsImpl(JavaWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void getListOfBackups(IntegrationProjectId var1, GetListOfBackupsCallback var2) {
      try {
         com.etlforge.integration.server.implementation.webservice.projectadmin.backup.client.ProjectBackupAdministration var3 = this.getProvider(var2);
         if (var3 == null) {
            return;
         }

         if (Thread.currentThread().isInterrupted()) {
            var2.cancelled();
            return;
         }

         WsGetBackupListResult var4 = var3.getListOfBackups(this.user, this.password, var1.toString());
         ProjectBackupDescriptor[] var5 = Arrays2.transform(var4.getBackups(), ProjectBackupDescriptor.class, WsDataConverter.BACKUP_DESCRIPTOR);
         if (Thread.currentThread().isInterrupted()) {
            var2.cancelled();
            return;
         }

         var2.backupDescriptorsDownloaded(var5);
      } catch (RemoteException var6) {
         var2.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var2.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   @Override
   public void downloadBackup(ProjectBackupDescriptor var1, DownloadBackupCallback var2) {
      ServerAdministration var3 = IntegrationServer.getInstance().getServerCall(ServerAdministration.class);
      var3.downloadServerFile(var1.getFilePath(), new ProjectBackupAdministrationWsImpl.DownloadFileCallbackWrapper(var2));
   }

   private com.etlforge.integration.server.implementation.webservice.projectadmin.backup.client.ProjectBackupAdministration getProvider(ServerCallback var1) throws IntegrationServerException {
      if (Thread.currentThread().isInterrupted()) {
         var1.cancelled();
         return null;
      }

      try {
         ProjectBackupAdministrationServiceLocator var2 = new ProjectBackupAdministrationServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_projectbackupadmin", var3, var1)) {
            return null;
         }

         this.user = this.getConfiguration().getUserName();
         this.password = this.getConfiguration().getPasswordHash();
         var2.setkonga_projectbackupadminEndpointAddress(var3.toString());
         return var2.getkonga_projectbackupadmin();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   private static class DownloadFileCallbackWrapper implements DownloadServerFileCallback {
      private final DownloadBackupCallback wrappee;

      public DownloadFileCallbackWrapper(DownloadBackupCallback var1) {
         this.wrappee = var1;
      }

      @Override
      public void fileWasDownloaded(File var1) {
         this.wrappee.backupDownloaded(var1);
      }

      @Override
      public void cancelled() {
         this.wrappee.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         this.wrappee.caught(var1);
      }
   }
}
