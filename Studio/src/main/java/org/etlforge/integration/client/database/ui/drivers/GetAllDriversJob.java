package org.etlforge.integration.client.database.ui.drivers;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.database.serverapi.JdbcDriverListCallback;
import org.etlforge.integration.client.database.serverapi.JdbcInfoProvider;
import org.etlforge.integration.client.database.serverapi.OdbcDriverListCallback;
import org.etlforge.integration.client.database.serverapi.OdbcInfoProvider;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;

final class GetAllDriversJob extends UiJob {
   private final CountDownLatch latch = new CountDownLatch(1);
   private boolean succeeded;
   private ServerInfo serverInfo;
   private final List<DriverDescriptor> drivers = Lists.newArrayList();
   private final EnumSet<DriverType> typesToGet;

   public GetAllDriversJob() {
      this(EnumSet.allOf(DriverType.class));
   }

   public GetAllDriversJob(EnumSet<DriverType> var1) {
      super(DatabaseUiResources.getString("GetDriversJob.Name"));
      Preconditions.checkArgument(!var1.isEmpty(), "Must ask for at least one driver type");
      this.setBlockUntilDone(true);
      this.typesToGet = var1.clone();
   }

   public boolean succeeded() {
      return this.succeeded;
   }

   public void waitUntilDone() throws InterruptedException {
      this.latch.await();
   }

   public ImmutableList<DriverDescriptor> getDownloadedDrivers() {
      return ImmutableList.copyOf(this.drivers);
   }

   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   @Override
   protected void runImpl() throws InterruptedException {
      if (this.isAskingForODBC()) {
         this.getOdbcDrivers();
      }

      if (this.isAskingForJDBC() && !this.isCancelled()) {
         this.getJdbcDrivers();
      }
   }

   private boolean isAskingForODBC() {
      return this.typesToGet.contains(DriverType.ODBC);
   }

   private boolean isAskingForJDBC() {
      return this.typesToGet.contains(DriverType.JDBC);
   }

   @Override
   protected void onJobCancelled() {
      this.latch.countDown();
   }

   private void getOdbcDrivers() throws InterruptedException {
      this.setMessage(DatabaseUiResources.getString("GetDriversJob.Odbc"));
      GetAllDriversJob.OdbcCallback var1 = new GetAllDriversJob.OdbcCallback();
      IntegrationServer var2 = IntegrationServer.getInstance();
      OdbcInfoProvider var3 = var2.getServerCall(OdbcInfoProvider.class);
      var3.getDrivers(var1);
   }

   private void getJdbcDrivers() throws InterruptedException {
      this.setMessage(DatabaseUiResources.getString("GetDriversJob.Jdbc"));
      GetAllDriversJob.JdbcCallback var1 = new GetAllDriversJob.JdbcCallback();
      IntegrationServer var2 = IntegrationServer.getInstance();
      JdbcInfoProvider var3 = var2.getServerCall(JdbcInfoProvider.class);
      var3.getDrivers(var1);
   }

   private abstract class CallbackBase extends AbstractServerCallback {
      public CallbackBase() {
         this.setErrorTitle(DatabaseUiResources.getString("GetDriversJob.ErrorMessage"));
      }

      @Override
      public final void caught(Throwable var1) {
         if (!GetAllDriversJob.this.isCancelled()) {
            GetAllDriversJob.this.setStatus(JobStatus.ERROR);
            GetAllDriversJob.this.setMessage(UiJobMessages.ERROR);
            super.caught(var1);
            GetAllDriversJob.this.latch.countDown();
         }
      }

      @Override
      public final void cancelled() {
         super.cancelled();
         GetAllDriversJob.this.latch.countDown();
      }
   }

   private final class JdbcCallback extends GetAllDriversJob.CallbackBase implements JdbcDriverListCallback {
      private JdbcCallback() {
      }

      @Override
      public void driversRetrieved(JdbcDriverDescriptor[] var1, ServerInfo var2) {
         if (!GetAllDriversJob.this.isCancelled()) {
            GetAllDriversJob.this.serverInfo = var2;
            GetAllDriversJob.this.setCanBeCancelled(false);
            GetAllDriversJob.this.setMayBeInterrupted(false);
            GetAllDriversJob.this.setMessage(DatabaseUiResources.getString("GetDriversJob.UpdateClient"));

            for (JdbcDriverDescriptor var6 : var1) {
               GetAllDriversJob.this.drivers.add(var6);
            }

            GetAllDriversJob.this.succeeded = true;
            GetAllDriversJob.this.latch.countDown();
         }
      }
   }

   private final class OdbcCallback extends GetAllDriversJob.CallbackBase implements OdbcDriverListCallback {
      private OdbcCallback() {
      }

      @Override
      public void driversRetrieved(List<OdbcDriverDescriptor> var1, ServerInfo var2) {
         if (!GetAllDriversJob.this.isCancelled()) {
            GetAllDriversJob.this.serverInfo = var2;
            GetAllDriversJob.this.drivers.addAll(var1);
            if (!GetAllDriversJob.this.isAskingForJDBC()) {
               GetAllDriversJob.this.latch.countDown();
            }
         }
      }
   }
}
