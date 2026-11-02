package org.etlforge.integration.server.implementation.webservice.scheduler.impl;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import javax.xml.rpc.ServiceException;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.client.scheduler.server.TestScheduleCallback;
import org.etlforge.integration.client.scheduler.server.TestScheduleProvider;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ClientServerDeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.DeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ServerPropertyKey;
import org.etlforge.integration.server.implementation.webservice.projectadmin.client.FindScheduledOperationsResult;
import org.etlforge.integration.server.implementation.webservice.projectadmin.client.ProjectAdministration;
import org.etlforge.integration.server.implementation.webservice.projectadmin.client.ProjectAdministrationServiceLocator;
import org.etlforge.integration.server.implementation.webservice.projectadmin.client.ScheduledOperation;

public final class TestScheduleProviderWsImpl extends CppWebServiceServerCall implements TestScheduleProvider {
   private static String SERVICE_NAME = "konga_project_administration";
   private String userName;
   private String password;

   public TestScheduleProviderWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void testSchedule(ScheduleId var1, int var2, TestScheduleCallback var3) {
      ProjectAdministration var4;
      try {
         var4 = this.getProjectAdministration(var3);
         if (var4 == null) {
            return;
         }
      } catch (IntegrationServerException var8) {
         var3.caught(var8);
         return;
      }

      try {
         FindScheduledOperationsResult var5 = var4.testScheduleWithGuid(this.userName, this.password, var1.toString(), null, null, var2);
         var3.testCompleted(this.getQueueDates(var5));
      } catch (RemoteException var6) {
         var3.caught(var6);
         return;
      } catch (ParseException var7) {
         var3.caught(var7);
      }
   }

   private ProjectAdministration getProjectAdministration(ServerCallback var1) throws IntegrationServerException {
      ProjectAdministrationServiceLocator var2 = new ProjectAdministrationServiceLocator();

      try {
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, SERVICE_NAME, var3, var1)) {
            return null;
         }

         this.userName = this.getConfiguration().getUserName();
         this.password = this.getConfiguration().getPasswordHash();
         var2.setkonga_project_administrationEndpointAddress(var3.toString());
         return var2.getkonga_project_administration();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   private Date[] getQueueDates(FindScheduledOperationsResult var1) throws ParseException {
      ScheduledOperation[] var2 = var1.getScheduledOperations();
      SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      ArrayList var4 = new ArrayList();

      for (ScheduledOperation var8 : var2) {
         String[] var9 = var8.getISO8601ExecutionTimes();

         for (String var13 : var9) {
            var4.add(var3.parse(var13));
         }
      }

      return (Date[]) var4.toArray(new Date[var4.size()]);
   }

   private WsKeyValuePairs constructWsKeyValuePairs(Schedule var1) {
      DeployKeyMappings var2 = ClientServerDeployKeyMappings.getClientServerKeyMappings(EntityType.Schedule);
      StringKeyValuePairs var3 = var1.getCopyOfKeyValues();
      Set var4 = var3.getKeys();
      ArrayList var5 = new ArrayList();

      for (Object __etlItem103 : var4) {
         String var7 = (String) __etlItem103;
         String var8 = var3.get(var7);
         if (var8 != null) {
            ServerPropertyKey var9 = var2.get(var7);
            if (var9 != null) {
               var5.add(new WsKeyValue(var9.getServerKeyName(), var8));
            }
         }
      }

      WsKeyValuePairs var10 = new WsKeyValuePairs();
      var10.setKeyValues((WsKeyValue[]) var5.toArray(new WsKeyValue[var5.size()]));
      return var10;
   }

   @Override
   public void testSchedule(Schedule var1, int var2, TestScheduleCallback var3) {
      ProjectAdministration var4;
      try {
         var4 = this.getProjectAdministration(var3);
         if (var4 == null) {
            return;
         }
      } catch (IntegrationServerException var9) {
         var3.caught(var9);
         return;
      }

      WsKeyValuePairs var5 = this.constructWsKeyValuePairs(var1);

      try {
         FindScheduledOperationsResult var6 = var4.testScheduleWithKeyValues(this.userName, this.password, var5, null, null, var2);
         var3.testCompleted(this.getQueueDates(var6));
      } catch (RemoteException var7) {
         var3.caught(var7);
      } catch (ParseException var8) {
         var3.caught(var8);
      }
   }
}
