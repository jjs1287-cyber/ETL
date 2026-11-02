package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.rmi.RemoteException;
import org.etlforge.integration.server.log.DebugLogDirectory;
import org.etlforge.integration.server.log.LogDirectory;
import org.etlforge.integration.server.log.OperationLogDirectory;

public final class Konga_serveradminSoapBindingImpl implements ServerAdministration {
   @Override
   public String downloadServerConfigurationFile(String var1, String var2) throws RemoteException {
      DownloadServerConfigFileTask var3 = new DownloadServerConfigFileTask(var1, var2);
      return var3.execute();
   }

   @Override
   public void uploadServerConfigurationFile(String var1, String var2, String var3) throws RemoteException {
      UploadServerConfigFileTask var4 = new UploadServerConfigFileTask(var1, var2, var3);
      var4.execute();
   }

   @Override
   public String downloadFile(String var1, String var2, String var3) throws RemoteException {
      DownloadFileTask var4 = new DownloadFileTask(var1, var2, var3);
      return var4.execute();
   }

   @Override
   public WsServerFileLocators listServerLogFiles(String var1, String var2) throws RemoteException {
      ListServerLogFilesTask var3 = new ListServerLogFilesTask(var1, var2);
      return var3.execute();
   }

   @Override
   public WsServerFileLocators listPluginLogFiles(String var1, String var2) throws RemoteException {
      ListPluginLogFilesTask var3 = new ListPluginLogFilesTask(var1, var2);
      return var3.execute();
   }

   @Override
   public WsServerFileLocators listOperationLogFiles(String var1, String var2, String var3, String var4) throws RemoteException {
      ListLogFilesTask var5 = new ListLogFilesTask("lostOperationLogFiles", var1, var2, var3, var4) {
         @Override
         protected LogDirectory getLogDirectory() throws Exception {
            return new OperationLogDirectory();
         }
      };
      return var5.execute();
   }

   @Override
   public WsServerFileLocators listDebugLogFiles(String var1, String var2, String var3, String var4) throws RemoteException {
      ListLogFilesTask var5 = new ListLogFilesTask("listDebugLogFiles", var1, var2, var3, var4) {
         @Override
         protected LogDirectory getLogDirectory() throws Exception {
            return new DebugLogDirectory();
         }
      };
      return var5.execute();
   }
}
