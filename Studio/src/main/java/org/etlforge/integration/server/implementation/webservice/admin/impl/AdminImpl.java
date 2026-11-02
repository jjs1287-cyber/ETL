package org.etlforge.integration.server.implementation.webservice.admin.impl;

import com.google.common.base.Optional;
import com.etlforge.integration.client.proxy.serverapi.GetProxySettingsCallback;
import com.etlforge.integration.client.proxy.serverapi.ProxySettingsApi;
import com.etlforge.integration.client.proxy.serverapi.SetProxySettingsCallback;
import java.rmi.RemoteException;
import java.util.EnumSet;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.GetStatusCallback;
import org.etlforge.integration.client.admin.IsRunningCallback;
import org.etlforge.integration.client.admin.StartCallback;
import org.etlforge.integration.client.admin.StopCallback;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.server.implementation.webservice.admin.client.AdminServiceLocator;
import org.etlforge.integration.server.implementation.webservice.admin.client.ServerStatus;
import org.etlforge.integration.server.implementation.webservice.admin.client.WsProxySettings;
import org.etlforge.util.net.ProxySettings;

public final class AdminImpl extends CppWebServiceServerCall implements Admin, ProxySettingsApi {
   private static final String SERVICE_NAME = "konga_admin";
   private String user;
   private String password;

   public AdminImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void startProcessEngine(StartCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         var2.startEngine(this.user, this.password);
         var1.startComplete();
      } catch (RemoteException var3) {
         var1.caught(this.convert(var3));
      } catch (IntegrationServerException var4) {
         var1.caught(new IntegrationServerException("Server exception: " + var4.getMessage(), var4));
      }
   }

   private org.etlforge.integration.server.implementation.webservice.admin.client.Admin getAdmin(ServerCallback var1) throws IntegrationServerException {
      AdminServiceLocator var2 = new AdminServiceLocator();

      try {
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_admin", var3, var1)) {
            return null;
         }

         this.user = this.getConfiguration().getUserName();
         this.password = this.getConfiguration().getPasswordHash();
         var2.setkonga_adminEndpointAddress(var3.toString());
         return var2.getkonga_admin();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   @Override
   public void stopProcessEngine(StopCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         var2.stopEngine(this.user, this.password);
         var1.stopComplete();
      } catch (RemoteException var3) {
         var1.caught(this.convert(var3));
      } catch (IntegrationServerException var4) {
         var1.caught(new IntegrationServerException("Server exception: " + var4.getMessage(), var4));
      }
   }

   @Override
   public void isProcessEngineRunning(IsRunningCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         boolean var3 = var2.isEngineRunning(this.user, this.password);
         var1.isRunningComplete(var3);
      } catch (RemoteException var4) {
         var1.caught(this.convert(var4));
      } catch (IntegrationServerException var5) {
         var1.caught(new IntegrationServerException("Server exception: " + var5.getMessage(), var5));
      }
   }

   @Override
   public void getProcessEngineStatus(GetStatusCallback var1, Admin.StatusDetail var2) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var3 = this.getAdmin(var1);
         if (var3 == null) {
            return;
         }

         ServerStatus var4 = var3.getEngineStatus(this.user, this.password, var2.getServerCode());
         EnumSet var5 = this.getServerStatus(var4);
         var1.getStatusComplete(var5, var4.getDescription());
      } catch (RemoteException var6) {
         var1.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var1.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   @Override
   public void getSchedulerStatus(GetStatusCallback var1, Admin.StatusDetail var2) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var3 = this.getAdmin(var1);
         if (var3 == null) {
            return;
         }

         ServerStatus var4 = var3.getSchedulerStatus(this.user, this.password, var2.getServerCode());
         EnumSet var5 = this.getServerStatus(var4);
         var1.getStatusComplete(var5, var4.getDescription());
      } catch (RemoteException var6) {
         var1.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var1.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   @Override
   public void isSchedulerRunning(IsRunningCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         boolean var3 = var2.isSchedulerRunning(this.user, this.password);
         var1.isRunningComplete(var3);
      } catch (RemoteException var4) {
         var1.caught(this.convert(var4));
      } catch (IntegrationServerException var5) {
         var1.caught(new IntegrationServerException("Server exception: " + var5.getMessage(), var5));
      }
   }

   @Override
   public void startScheduler(StartCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         var2.startScheduler(this.user, this.password);
         var1.startComplete();
      } catch (RemoteException var3) {
         var1.caught(this.convert(var3));
      } catch (IntegrationServerException var4) {
         var1.caught(new IntegrationServerException("Server exception: " + var4.getMessage(), var4));
      }
   }

   @Override
   public void stopScheduler(StopCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         var2.stopScheduler(this.user, this.password);
         var1.stopComplete();
      } catch (RemoteException var3) {
         var1.caught(this.convert(var3));
      } catch (IntegrationServerException var4) {
         var1.caught(new IntegrationServerException("Server exception: " + var4.getMessage(), var4));
      }
   }

   @Override
   public void isJmsEngineRunning(IsRunningCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         boolean var3 = var2.isJmsEngineRunning(this.user, this.password);
         var1.isRunningComplete(var3);
      } catch (RemoteException var4) {
         var1.caught(this.convert(var4));
      } catch (IntegrationServerException var5) {
         var1.caught(new IntegrationServerException("Server exception: " + var5.getMessage(), var5));
      }
   }

   @Override
   public void startJmsEngine(StartCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         var2.startJmsEngine(this.user, this.password);
         var1.startComplete();
      } catch (RemoteException var3) {
         var1.caught(this.convert(var3));
      } catch (IntegrationServerException var4) {
         var1.caught(new IntegrationServerException("Server exception: " + var4.getMessage(), var4));
      }
   }

   @Override
   public void stopJmsEngine(StopCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         var2.stopJmsEngine(this.user, this.password);
         var1.stopComplete();
      } catch (RemoteException var3) {
         var1.caught(this.convert(var3));
      } catch (IntegrationServerException var4) {
         var1.caught(new IntegrationServerException("Server exception: " + var4.getMessage(), var4));
      }
   }

   @Override
   public void getJmsEngineStatus(GetStatusCallback var1, Admin.StatusDetail var2) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var3 = this.getAdmin(var1);
         if (var3 == null) {
            return;
         }

         ServerStatus var4 = var3.getJmsEngineStatus(this.user, this.password, var2.getServerCode());
         EnumSet var5 = this.getServerStatus(var4);
         var1.getStatusComplete(var5, var4.getDescription());
      } catch (RemoteException var6) {
         var1.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var1.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   private EnumSet<org.etlforge.integration.client.admin.ServerStatus> getServerStatus(ServerStatus var1) {
      EnumSet var2 = EnumSet.noneOf(org.etlforge.integration.client.admin.ServerStatus.class);
      int var3 = var1.getStatusBits();
      if (var3 == 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.UNDEFINED);
      }

      if ((var3 & 1) > 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.STOPPED);
      }

      if ((var3 & 2) > 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.RUNNING);
      }

      if ((var3 & 4) > 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.STOPPING);
      }

      if ((var3 & 8) > 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.ERROR);
      }

      if ((var3 & 16) > 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.SINGLE_THREAD);
      }

      if ((var3 & 32) > 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.RESOURCE_DISTRIBUTION);
      }

      if ((var3 & 64) > 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.RESOURCE_DISTRIBUTION_ERROR);
      }

      if ((var3 & 128) > 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.DISABLED);
      }

      if ((var3 & 256) > 0) {
         var2.add(org.etlforge.integration.client.admin.ServerStatus.UNREACHABLE);
      }

      return var2;
   }

   @Override
   public void getProxySettings(GetProxySettingsCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var2 = this.getAdmin(var1);
         if (var2 == null) {
            return;
         }

         ServerInfo var3 = this.getConfiguration().getServerInfo();
         WsProxySettings var4 = var2.getProxySettings(this.user, this.password);
         ProxySettings var5 = ProxySettingsFunctions.fromWs().apply(var4);
         var1.gotProxySettings(Optional.of(var3), var5);
      } catch (RemoteException var6) {
         var1.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var1.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   @Override
   public void setProxySettings(ProxySettings var1, SetProxySettingsCallback var2) {
      try {
         org.etlforge.integration.server.implementation.webservice.admin.client.Admin var3 = this.getAdmin(var2);
         if (var3 == null) {
            return;
         }

         WsProxySettings var4 = ProxySettingsFunctions.toWs().apply(var1);
         var3.setProxySettings(this.user, this.password, var4);
         var2.done();
      } catch (RemoteException var5) {
         var2.caught(this.convert(var5));
      } catch (IntegrationServerException var6) {
         var2.caught(new IntegrationServerException("Server exception: " + var6.getMessage(), var6));
      }
   }
}
