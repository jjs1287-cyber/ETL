package org.etlforge.integration.server.implementation.webservice.admin.project.impl;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.admin.project.DeleteIntegrationProjectResult;
import com.etlforge.integration.client.admin.project.DeleteProjectsCallback;
import com.etlforge.integration.client.admin.project.EnableOperationSchedulesCallback;
import com.etlforge.integration.client.admin.project.EnableSchedulesCallback;
import com.etlforge.integration.client.admin.project.GetScheduledRunsCallback;
import com.etlforge.integration.client.admin.project.ProjectAdministration;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.client.scheduler.ScheduledRun;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl.DeployInterchangeModelWsImpl;
import org.etlforge.integration.server.implementation.webservice.projectadmin.client.FindScheduledOperationsResult;
import org.etlforge.integration.server.implementation.webservice.projectadmin.client.ProjectAdministrationServiceLocator;
import org.etlforge.integration.server.implementation.webservice.projectadmin.client.ScheduledOperation;
import org.etlforge.util.id.KongaGuid;

public final class ProjectAdministrationWsImpl extends CppWebServiceServerCall implements ProjectAdministration {
   private static final String SERVICE_NAME = "konga_project_administration";
   private final ServerConnectionService serverManager;
   private String userName = "";
   private String password = "";
   private final boolean debug = ApplicationConfiguration.getBoolean("ProjectAdministration.WS.Debug", false);

   public ProjectAdministrationWsImpl(CppWebServiceConfiguration var1, ServerConnectionService var2) {
      super(var1);
      this.serverManager = var2;
   }

   private org.etlforge.integration.server.implementation.webservice.projectadmin.client.ProjectAdministration getProjectAdministrationClient(
      ServerCallback var1
   ) throws IntegrationServerException {
      ProjectAdministrationServiceLocator var2 = new ProjectAdministrationServiceLocator();

      try {
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_project_administration", var3, var1)) {
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

   @Override
   public void deleteProjects(List<IntegrationProjectId> var1, DeleteProjectsCallback var2) {
      DeployInterchangeModelWsImpl var3 = new DeployInterchangeModelWsImpl((CppWebServiceConfiguration)this.getConfiguration(), this.serverManager);
      var3.deleteProjects(var1, wrap(var2));
   }

   @Override
   public void setOperationSchedulesEnabled(Collection<OperationId> var1, boolean var2, EnableOperationSchedulesCallback var3) {
      if (var1.isEmpty()) {
         var3.done(var1, var2);
      }

      try {
         org.etlforge.integration.server.implementation.webservice.projectadmin.client.ProjectAdministration var4 = this.getProjectAdministrationClient(var3);
         if (var4 == null) {
            return;
         }

         String[] var5 = new String[var1.size()];
         int var6 = 0;

         for (OperationId var8 : var1) {
            var5[var6] = var8.toString();
            var6++;
         }

         if (var2) {
            var4.enableScheduleForOperation(this.userName, this.password, var5);
         } else {
            var4.disableScheduleForOperation(this.userName, this.password, var5);
         }

         var3.done(var1, var2);
      } catch (RemoteException var9) {
         var3.caught(var9);
      } catch (IntegrationServerException var10) {
         var3.caught(var10);
      }
   }

   @Override
   public void setSchedulesEnabled(Collection<ScheduleId> var1, boolean var2, EnableSchedulesCallback var3) {
      if (var1.isEmpty()) {
         var3.done(var1, var2);
      }

      try {
         org.etlforge.integration.server.implementation.webservice.projectadmin.client.ProjectAdministration var4 = this.getProjectAdministrationClient(var3);
         if (var4 == null) {
            return;
         }

         String[] var5 = new String[var1.size()];
         int var6 = 0;

         for (ScheduleId var8 : var1) {
            var5[var6] = var8.toString();
            var6++;
         }

         if (var2) {
            var4.enableSchedules(this.userName, this.password, var5);
         } else {
            var4.disableSchedules(this.userName, this.password, var5);
         }

         var3.done(var1, var2);
      } catch (RemoteException var9) {
         var3.caught(var9);
      } catch (IntegrationServerException var10) {
         var3.caught(var10);
      }
   }

   private void debug_getScheduledRuns(Date var1, GetScheduledRunsCallback var2) throws InterruptedException, ParseException {
      Thread.sleep(1000L);
      SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      ArrayList var4 = Lists.newArrayList();
      Calendar var5 = Calendar.getInstance();
      var5.setTime(var1);
      int var6 = var5.get(7);
      if (var6 != 1 && var6 != 7) {
         var4.add(new ScheduledRunImpl(var3.parse("2008-12-10 08:00"), new Operation("Check incoming POs"), new Schedule("Every 2 Hours")));
         var4.add(new ScheduledRunImpl(var3.parse("2008-12-10 10:00"), new Operation("Check incoming POs"), new Schedule("Every 2 Hours")));
         var4.add(new ScheduledRunImpl(var3.parse("2008-12-10 12:00"), new Operation("Check incoming POs"), new Schedule("Every 2 Hours")));
         var4.add(new ScheduledRunImpl(var3.parse("2008-12-10 14:00"), new Operation("Check incoming POs"), new Schedule("Every 2 Hours")));
         var4.add(new ScheduledRunImpl(var3.parse("2008-12-10 16:00"), new Operation("Check incoming POs"), new Schedule("Every 2 Hours")));
         var4.add(new ScheduledRunImpl(var3.parse("2008-12-10 18:00"), new Operation("Check incoming POs"), new Schedule("Every 2 Hours")));
         var4.add(new ScheduledRunImpl(var3.parse("2008-12-10 12:30"), new Operation("Update customer table"), new Schedule("Customer Table Update")));
         var4.add(new ScheduledRunImpl(var3.parse("2008-12-10 20:00"), new Operation("Calculate profit"), new Schedule("End of Day")));
      }

      ServerInfo var7 = new ServerInfo(new ServerGuid(KongaGuid.createGuid().toString()), "Test Server");
      var2.scheduledRunsDownloaded(var7, var1, var4);
   }

   private List<ScheduledRun> createScheduledRuns(ScheduledOperation[] var1) throws ParseException {
      ArrayList var2 = Lists.newArrayList();
      if (var1 != null && var1.length > 0) {
         SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

         for (ScheduledOperation var7 : var1) {
            if (var7 != null) {
               String[] var8 = var7.getISO8601ExecutionTimes();
               if (var8 != null && var8.length > 0) {
                  Schedule var9 = new Schedule(new ScheduleId(var7.getScheduleGuid()), var7.getScheduleName());
                  Operation var10 = new Operation(new OperationId(var7.getOperationGuid()), var7.getOperationName());

                  for (String var14 : var8) {
                     ScheduledRunImpl var15 = new ScheduledRunImpl(var3.parse(var14), var10, var9);
                     var2.add(var15);
                  }
               }
            }
         }
      }

      return var2;
   }

   @Override
   public void getScheduledRuns(Date var1, GetScheduledRunsCallback var2) {
      try {
         if (this.debug) {
            this.debug_getScheduledRuns(var1, var2);
            return;
         }

         org.etlforge.integration.server.implementation.webservice.projectadmin.client.ProjectAdministration var3 = this.getProjectAdministrationClient(var2);
         if (var3 == null) {
            return;
         }

         ServerInfo var4 = this.getConfiguration().getServerInfo();
         SimpleDateFormat var5 = new SimpleDateFormat("yyyy-MM-dd");
         String var6 = var5.format(var1) + " 00:00:00";
         String var7 = var5.format(var1) + " 23:59:59";
         FindScheduledOperationsResult var8 = var3.findScheduledOperations(this.userName, this.password, null, null, var6, var7);
         ScheduledOperation[] var9 = var8.getScheduledOperations();
         var2.scheduledRunsDownloaded(var4, var1, this.createScheduledRuns(var9));
      } catch (RemoteException var10) {
         var2.caught(var10);
      } catch (InterruptedException var11) {
         Thread.interrupted();
         var2.caught(var11);
      } catch (ParseException var12) {
         var2.caught(var12);
      } catch (IntegrationServerException var13) {
         var2.caught(var13);
      }
   }

   private static org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl.DeleteProjectsCallback wrap(
      final DeleteProjectsCallback var0
   ) {
      return new org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl.DeleteProjectsCallback() {
         @Override
         public void projectsDeleted(List<DeleteIntegrationProjectResult> var1, ServerInfo var2) {
            var0.projectsWereDeleted(var1, var2);
         }

         @Override
         public void cancelled() {
            var0.cancelled();
         }

         @Override
         public void caught(Throwable var1) {
            var0.caught(var1);
         }
      };
   }
}
