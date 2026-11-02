package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.application.ui.job.JobMessage;
import org.etlforge.application.ui.job.JobMessageListener;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

public abstract class AbstractWsdlProcessingJob implements WsdlProcessingJob {
   private WsdlProcessingCallback callback;
   private boolean isDone;
   private WsdlFile wsdlFile;
   private WebServiceInfo parsedInfo;
   private final List<JobMessageListener> messageListeners = new CopyOnWriteArrayList<>();

   @Override
   public final void setCallback(WsdlProcessingCallback var1) {
      this.callback = var1;
   }

   @Override
   public final synchronized boolean isDone() {
      return this.isDone;
   }

   protected final synchronized void setDone() {
      this.isDone = true;
   }

   @Override
   public final WsdlFile getWsdlFile() {
      return this.wsdlFile;
   }

   protected final void setWsdlFile(WsdlFile var1) {
      this.wsdlFile = var1;
   }

   @Override
   public final WebServiceInfo getParsedInfo() {
      return this.parsedInfo;
   }

   protected final void setParsedInfo(WebServiceInfo var1) {
      this.parsedInfo = var1;
   }

   protected final void signalJobCancelled() {
      if (!this.isDone()) {
         if (this.callback != null) {
            this.callback.processingCancelled();
         }

         this.setDone();
      }
   }

   protected final void signalJobFailed(Throwable var1) {
      if (!this.isDone()) {
         if (this.callback != null) {
            this.callback.processingFailed(var1);
         }

         this.setDone();
      }
   }

   protected final void signalJobSucceeded() {
      if (this.wsdlFile != null && this.parsedInfo != null) {
         this.parsedInfo.setIdentifier(this.wsdlFile.getLocator());
      }

      if (this.callback != null) {
         this.callback.processCompletedSuccessfully(this.wsdlFile, this.parsedInfo);
      }

      this.setDone();
   }

   @Override
   public void addJobMessageListener(JobMessageListener var1) {
      this.messageListeners.add(var1);
   }

   protected void setNewMessage(String var1) {
      JobMessage var2 = new JobMessage(var1);

      for (JobMessageListener var4 : this.messageListeners) {
         var4.messageChanged(var2);
      }
   }
}
