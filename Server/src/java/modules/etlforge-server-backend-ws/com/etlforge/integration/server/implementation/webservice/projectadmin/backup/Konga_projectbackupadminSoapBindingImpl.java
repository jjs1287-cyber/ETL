package com.etlforge.integration.server.implementation.webservice.projectadmin.backup;

import com.etlforge.integration.server.license.db.EnterpriseGate;
import java.rmi.RemoteException;
import java.util.List;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.project.backup.ProjectBackupInfoRetriever;
import org.etlforge.integration.server.ws.WsUtils;

public class Konga_projectbackupadminSoapBindingImpl implements ProjectBackupAdministration {
   @Override
   public WsGetBackupListResult getListOfBackups(String var1, String var2, String var3) throws RemoteException {
      ExternalApiSupport var4 = null;

      try {
         var4 = ExternalApiSupport.forCall(var1, var2);
         if (!var4.isAdmin()) {
            throw new RemoteException("User " + var1 + " is not an administrator.");
         }

         this.checkEnterprise(var4);
         ProjectBackupInfoRetriever var5 = new ProjectBackupInfoRetriever(var4.getTranDbConnection());
         List var6 = var5.listBackups(new IntegrationProjectId(var3));
         return ResponseConverter.BACKUP_LIST.apply(var6);
      } catch (Exception var11) {
         WsUtils.convertError(var11, this.getClass(), "getListOfBackups");
      } finally {
         ExternalApiSupport.safeDispose(var4);
      }

      return null;
   }

   private void checkEnterprise(ExternalApiSupport var1) throws Exception {
      EnterpriseGate var2 = new EnterpriseGate(var1.getTranDbConnection());
      var2.ensureServerIsEnterprise();
   }
}
