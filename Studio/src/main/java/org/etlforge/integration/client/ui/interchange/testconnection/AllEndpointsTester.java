package org.etlforge.integration.client.ui.interchange.testconnection;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobProgressMonitorAdapter;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.test.EmptyTestConnectionHistory;
import org.etlforge.integration.data.test.TestConnectionHistory;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.util.string.StringLimiter;

public final class AllEndpointsTester {
   private final ImmutableList<ConnectionTesterFactory> factories;
   private volatile TestConnectionHistory testHistory = new EmptyTestConnectionHistory();
   private volatile EntityFilter filter = EntityFilters.ACCEPTS_ALL;
   private final List<TestAllConnectionsListener> listeners = Lists.newCopyOnWriteArrayList();
   private AllEndpointsTester.State state = AllEndpointsTester.State.STOPPED;

   public AllEndpointsTester(Iterable<ConnectionTesterFactory> var1) {
      this.factories = ImmutableList.copyOf(var1);
   }

   public void setTestHistory(TestConnectionHistory var1) {
      this.testHistory = Preconditions.checkNotNull(var1);
   }

   public void setFilter(EntityFilter var1) {
      this.filter = Preconditions.checkNotNull(var1);
   }

   public void addTestListener(TestAllConnectionsListener var1) {
      this.listeners.add(var1);
   }

   public void removeTestListener(TestAllConnectionsListener var1) {
      this.listeners.remove(var1);
   }

   public void testAllEndpoints(IntegrationProject var1) {
      if (this.getState() != AllEndpointsTester.State.RUNNING) {
         AllEndpointsTester.TestJob var2 = new AllEndpointsTester.TestJob(var1);
         var2.addProgressMonitor(new UiJobProgressMonitorAdapter() {
            @Override
            public void setMessage(String var1) {
               for (TestAllConnectionsListener var3 : AllEndpointsTester.this.listeners) {
                  var3.progressUpdate(var1);
               }
            }
         });
         var2.submit();
      }
   }

   private synchronized AllEndpointsTester.State getState() {
      return this.state;
   }

   private synchronized void setState(AllEndpointsTester.State var1) {
      this.state = var1;
   }

   public void stop() {
      if (this.getState() == AllEndpointsTester.State.RUNNING) {
         this.setState(AllEndpointsTester.State.STOPPED);
         this.fireTestStopped();
      }
   }

   private void fireTestStopped() {
      for (TestAllConnectionsListener var2 : this.listeners) {
         var2.testStopped();
      }
   }

   private enum State {
      STOPPED,
      RUNNING,
      DONE;
   }

   private class TestJob extends UiJob {
      private final IntegrationProject project;

      public TestJob(IntegrationProject var2) {
         super("Testing all connections");
         this.project = var2;
         this.setUncaughtExceptionHandler(new ExceptionHandler<Throwable>() {
            @Override
            public void caught(Throwable var1) {
               ErrorLog.attention(AllEndpointsTester.class, null, var1);
               AllEndpointsTester.this.stop();
            }
         });
      }

      @Override
      protected void runImpl() throws InterruptedException {
         AllEndpointsTester.this.setState(AllEndpointsTester.State.RUNNING);
         this.fireTestStarted();

         for (ConnectionTesterFactory var2 : AllEndpointsTester.this.factories) {
            boolean var3 = this.testEntitiesOfType(var2);
            if (!var3) {
               if (this.isCancelled()) {
                  return;
               }
               break;
            }
         }

         this.onDone();
      }

      private void fireTestStarted() {
         for (TestAllConnectionsListener var2 : AllEndpointsTester.this.listeners) {
            var2.testStarted();
         }
      }

      private boolean testEntitiesOfType(ConnectionTesterFactory var1) {
         for (IntegrationEntity var4 : this.getItemsToTest(var1)) {
            if (this.isCancelled()) {
               return false;
            }

            this.testConnection(var4, var1);
         }

         return true;
      }

      private Iterable<IntegrationEntity> getItemsToTest(ConnectionTesterFactory var1) {
         return Iterables.filter(this.project.getAllEntities(), EntityFilters.and(var1.getApplicableItemsFilter(), AllEndpointsTester.this.filter));
      }

      private void testConnection(IntegrationEntity var1, ConnectionTesterFactory var2) {
         if (this.saveIfDirty(var1)) {
            String var3 = "\"" + StringLimiter.limit(var1.getName(), 50) + "\"";
            this.setMessage("Testing the connection to " + var3);
            ConnectionTester var4 = var2.getConnectionTester(var1);
            TestConnectionResult var5 = var4.testConnection();
            this.processResult(var5);
            this.setMessage("Done testing " + var3);
         } else {
            AllEndpointsTester.this.stop();
         }
      }

      private boolean saveIfDirty(final IntegrationEntity var1) {
         return UiThreadSupplier.call(new Supplier<Boolean>() {
            public Boolean get() {
               UiCompanion var1x = UiCompanion.of(var1);
               return var1x.saveIfDirty();
            }
         });
      }

      private void processResult(TestConnectionResult var1) {
         if (var1 == null) {
            AllEndpointsTester.this.stop();
         } else {
            this.fireConnectionResult(var1);
            AllEndpointsTester.this.testHistory.add(var1);
         }
      }

      private void fireConnectionResult(TestConnectionResult var1) {
         for (TestAllConnectionsListener var3 : AllEndpointsTester.this.listeners) {
            var3.connectionWasTested(var1);
         }
      }

      @Override
      protected void onJobCancelled() {
         AllEndpointsTester.this.stop();
      }

      private void onDone() {
         AllEndpointsTester.this.setState(AllEndpointsTester.State.DONE);
         this.fireTestDone();
      }

      private void fireTestDone() {
         for (TestAllConnectionsListener var2 : AllEndpointsTester.this.listeners) {
            var2.testDone();
         }
      }
   }
}
