package org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.impl;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.client.ui.operation.jobcontrol.JobControl;
import org.etlforge.integration.client.ui.operation.jobcontrol.RunAllScheduledOperationsCallback;
import org.etlforge.integration.client.ui.operation.jobcontrol.RunOperationCallback;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.JobControlServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.WsRunOperationResult;

public final class JobControlWsImpl implements JobControl {
   private static final String SERVICE_NAME = "konga_jobcontrol";
   private final CppWebServiceConfiguration config;

   public JobControlWsImpl(CppWebServiceConfiguration var1) {
      this.config = var1;
   }

   @Override
   public void runOperation(OperationId var1, RunOperationCallback var2) {
      JobControlWsImpl.Impl var3 = new JobControlWsImpl.Impl(this.config);
      var3.runOperation(var1, var2);
   }

   @Override
   public void runOperationWithSourceData(OperationId var1, String var2, RunOperationCallback var3) {
      JobControlWsImpl.Impl var4 = new JobControlWsImpl.Impl(this.config);
      var4.runOperationWithSourceData(var1, var2, var3);
   }

   @Override
   public void runAllScheduledOperations(RunAllScheduledOperationsCallback var1) {
      JobControlWsImpl.Impl var2 = new JobControlWsImpl.Impl(this.config);
      var2.runAllScheduledOperations(var1);
   }

   private static class Impl extends CppWebServiceServerCall {
      private String userName;
      private String password;

      public Impl(CppWebServiceConfiguration var1) {
         super(var1);
      }

      public void runOperation(OperationId var1, RunOperationCallback var2) {
         try {
            org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.JobControl var3 = this.getJobControl(var2);
            if (var3 == null) {
               return;
            }

            WsRunOperationResult var4 = var3.runOperationInstanceMD5Password(this.userName, this.password, var1.toString());
            var2.done(var4.getOperationInstanceId());
         } catch (RemoteException var5) {
            var2.caught(this.convert(var5));
         } catch (IntegrationServerException var6) {
            var2.caught(new IntegrationServerException("Server exception: " + var6.getMessage(), var6));
         }
      }

      private org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.JobControl getJobControl(ServerCallback var1) throws IntegrationServerException {
         JobControlServiceLocator var2 = new JobControlServiceLocator();

         try {
            StringBuilder var3 = new StringBuilder();
            if (!this.getConfiguration().configureWebservice(var2, "konga_jobcontrol", var3, var1)) {
               return null;
            }

            this.userName = this.getConfiguration().getUserName();
            this.password = this.getConfiguration().getPasswordHash();
            var2.setkonga_jobcontrolEndpointAddress(var3.toString());
            return var2.getkonga_jobcontrol();
         } catch (ServiceException var4) {
            throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
         }
      }

      public void runAllScheduledOperations(RunAllScheduledOperationsCallback var1) {
         try {
            Thread.sleep(2000L);
            var1.done();
         } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
            var1.caught(var3);
         }
      }

      public void runOperationWithSourceData(OperationId var1, String var2, RunOperationCallback var3) {
         try {
            org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.JobControl var4 = this.getJobControl(var3);
            if (var4 == null) {
               return;
            }

            WsRunOperationResult var5 = var4.runOperationWithSourceData(this.userName, this.password, var1.toString(), var2);
            var3.done(var5.getOperationInstanceId());
         } catch (RemoteException var6) {
            var3.caught(this.convert(var6));
         } catch (IntegrationServerException var7) {
            var3.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
         }
      }
   }
}
