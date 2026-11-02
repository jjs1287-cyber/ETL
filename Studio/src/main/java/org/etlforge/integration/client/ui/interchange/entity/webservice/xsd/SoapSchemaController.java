package org.etlforge.integration.client.ui.interchange.entity.webservice.xsd;

import com.google.common.collect.Sets;
import java.awt.EventQueue;
import java.util.Set;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.mapping.serverapi.webservice.GenerateSoapSchemasCallback;
import org.etlforge.integration.client.mapping.serverapi.webservice.SoapSchemaProvider;
import org.etlforge.integration.client.server.AbstractServerCallback;

public final class SoapSchemaController {
   private final SoapSchemaProvider serverApi;
   private final SoapSchemaModel model;
   private final Set<UiJob> runningJobs = Sets.newHashSet();

   public SoapSchemaController(SoapSchemaProvider var1, SoapSchemaModel var2) {
      this.serverApi = var1;
      this.model = var2;
   }

   public void generateWithCaution() {
      boolean var1 = false;
      this.sendRequest(var1);
   }

   private void sendRequest(boolean var1) {
      this.prepareModelForNewRequest();
      SoapSchemaController.Worker var2 = new SoapSchemaController.Worker(var1);
      synchronized (this.runningJobs) {
         this.runningJobs.add(var2);
      }

      var2.submit();
   }

   private void prepareModelForNewRequest() {
      this.model.setBusy(true);
      this.model.setConflictsDetected(false);
      this.model.setDone(false);
   }

   public void generateWithForce() {
      boolean var1 = true;
      this.sendRequest(var1);
   }

   public void cancel() {
      synchronized (this.runningJobs) {
         for (UiJob var3 : this.runningJobs) {
            var3.cancel();
         }

         this.runningJobs.clear();
      }
   }

   private class Worker extends UiJob {
      private final SoapSchemaController.Worker.Callback callback = new SoapSchemaController.Worker.Callback();
      private final boolean forceOverwrite;

      public Worker(boolean var2) {
         super("Generate XSD files for Web Service Call");
         this.forceOverwrite = var2;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         SoapSchemaController.this.serverApi
            .generateXsds(
               SoapSchemaController.this.model.getOpInfo(),
               SoapSchemaController.this.model.getRequestXsdName(),
               SoapSchemaController.this.model.getResponseXsdName(),
               this.forceOverwrite,
               this.callback
            );
      }

      @Override
      protected void completed() {
         SoapSchemaController.this.model.setBusy(false);
         synchronized (SoapSchemaController.this.runningJobs) {
            SoapSchemaController.this.runningJobs.remove(this);
         }
      }

      @Override
      protected void onJobCancelled() {
         this.callback.cancelled();
      }

      private class Callback extends AbstractServerCallback implements GenerateSoapSchemasCallback {
         private Callback() {
         }

         @Override
         public void filesWereGenerated() {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  SoapSchemaController.this.model.setDone(true);
               }
            });
         }

         @Override
         public void fileConflictsDetected(String[] var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  SoapSchemaController.this.model.setConflictsDetected(true);
               }
            });
         }
      }
   }
}
