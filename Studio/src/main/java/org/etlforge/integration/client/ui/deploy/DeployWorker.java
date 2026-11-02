package org.etlforge.integration.client.ui.deploy;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.project.deploy.DeployLog;
import org.etlforge.integration.client.project.deploy.DeployManager;

public final class DeployWorker {
   private volatile DeployWorker.State state;
   private final DeployManager deployManager;
   private final Object lock;
   private List<DeployCallback> optionalCallbacks;
   private final DeployCallback fixedCallback;

   public DeployWorker(DeployManager var1) {
      this.deployManager = var1;
      this.lock = new Object();
      this.state = DeployWorker.State.IDLE;
      this.fixedCallback = new DeployWorker.FixedCallback();
   }

   public void addCallback(DeployCallback var1) {
      Preconditions.checkNotNull(var1, "callback");
      synchronized (this.lock) {
         if (this.state == DeployWorker.State.DEPLOYING) {
            illegalOperation("addCallback");
         }

         if (this.optionalCallbacks == null) {
            this.optionalCallbacks = Lists.newArrayList();
         }

         this.optionalCallbacks.add(var1);
      }
   }

   public void removeCallback(DeployCallback var1) {
      if (var1 != null) {
         synchronized (this.lock) {
            if (this.state == DeployWorker.State.DEPLOYING) {
               illegalOperation("removeCallback");
            }

            if (this.optionalCallbacks != null) {
               if (this.optionalCallbacks.remove(var1) && this.optionalCallbacks.isEmpty()) {
                  this.optionalCallbacks = null;
               }
            }
         }
      }
   }

   public void removeAllCallbacks() {
      synchronized (this.lock) {
         if (this.state == DeployWorker.State.DEPLOYING) {
            illegalOperation("removeAllCallbacks");
         }

         this.optionalCallbacks = null;
      }
   }

   public void deploy(DeployData var1, DeployHistory var2) {
      Preconditions.checkNotNull(var1, "dataToDeploy");
      this.logDeployRequest(var1);
      synchronized (this.lock) {
         if (this.state == DeployWorker.State.DEPLOYING) {
            illegalOperation("deploy");
         }

         this.addHistoryUpdater(var2);
         this.stateTransition(DeployWorker.State.DEPLOYING);
         this.deployImpl(var1);
      }
   }

   private void logDeployRequest(DeployData var1) {
      DeployLog.LOG.info("Received deploy request. Project: " + var1.getProject().getName() + " - Partial: " + var1.isPartial());
      DeployLog.LOG.info("The following items will be deployed:\n" + var1.getProject().getContentString());
   }

   private void deployImpl(DeployData var1) {
      DeployJob var2 = new DeployJob(this.deployManager, var1, this.fixedCallback);
      var2.setUseProgressDialog(0);
      DeployLog.LOG.info("Submitting DeployJob");
      var2.submit();
   }

   private void addHistoryUpdater(final DeployHistory var1) {
      if (var1 != null) {
         this.addCallback(new DeployCallback.Adapter() {
            @Override
            public void deployComplete(DeployData var1x, DeployResult var2) {
               var1.addResult(var2);
            }
         });
      }
   }

   private static void illegalOperation(String var0) throws IllegalStateException {
      IllegalStateException var1 = new IllegalStateException("This method cannot be called when a deploy is in progress.");
      DeployLog.LOG.throwing(DeployWorker.class.getName(), var0, var1);
      throw var1;
   }

   private void stateTransition(DeployWorker.State var1) {
      DeployLog.LOG.info("New state: " + var1);
      this.state = var1;
   }

   private final class FixedCallback implements DeployCallback {
      private FixedCallback() {
      }

      @Override
      public void deployComplete(DeployData var1, DeployResult var2) {
         try {
            if (DeployWorker.this.optionalCallbacks != null) {
               for (DeployCallback var4 : DeployWorker.this.optionalCallbacks) {
                  var4.deployComplete(var1, var2);
               }
            }
         } finally {
            this.done();
         }
      }

      private void done() {
         DeployWorker.this.optionalCallbacks = null;
         DeployWorker.this.stateTransition(DeployWorker.State.IDLE);
      }

      @Override
      public void caught(Throwable var1) {
         try {
            if (DeployWorker.this.optionalCallbacks != null) {
               for (DeployCallback var3 : DeployWorker.this.optionalCallbacks) {
                  var3.caught(var1);
               }
            }
         } finally {
            this.done();
         }
      }

      @Override
      public void cancelled() {
         try {
            if (DeployWorker.this.optionalCallbacks != null) {
               for (DeployCallback var2 : DeployWorker.this.optionalCallbacks) {
                  var2.cancelled();
               }
            }
         } finally {
            this.done();
         }
      }
   }

   private enum State {
      IDLE,
      DEPLOYING;
   }
}
