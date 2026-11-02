package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.ui.filestore.FileCollisionResolverFactory;
import org.etlforge.application.ui.job.JobMessage;
import org.etlforge.application.ui.job.JobMessageListener;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl.WsdlProcessingCallback;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.lang.exception.ExceptionHandler;

final class WsdlProcessor extends UiJob implements WsdlProcessingCallback, ExceptionHandler<Throwable>, JobMessageListener {
   private static FileCollisionResolverFactory fileCollisionResolverFactory;
   private final WebServiceWizard wizard;
   private final WebServiceWizardStartPageUi ui;
   private final CountDownLatch latch;
   private volatile boolean succeeded;
   private WsdlFile wsdlFile;
   private WebServiceInfo webServiceCallInfo;

   WsdlProcessor(WebServiceWizard var1, WebServiceWizardStartPageUi var2) {
      super(Strings.get("Wizard.WsdlProcessor.Name"));
      this.wizard = var1;
      this.ui = var2;
      this.latch = new CountDownLatch(1);
      this.setUncaughtExceptionHandler(this);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(Strings.get("Wizard.WsdlProcessor.Started"));
      this.ui.processSelectedWsdlFile(this, this);
   }

   @Override
   protected void onJobCancelled() {
      this.processingCancelled();
   }

   public void waitUntilDone() throws InterruptedException {
      this.latch.await();
   }

   public boolean succeeded() {
      return this.succeeded;
   }

   public WsdlFile getWsdlFile() {
      return this.wsdlFile;
   }

   public WebServiceInfo getParsedInfo() {
      return this.webServiceCallInfo;
   }

   @Override
   public void processCompletedSuccessfully(WsdlFile var1, WebServiceInfo var2) {
      if (!this.isCancelled()) {
         this.addToFileStore(var1, var2);
         this.wsdlFile = var1;
         this.webServiceCallInfo = var2;
         this.succeeded = true;
         this.latch.countDown();
      }
   }

   private void addToFileStore(WsdlFile var1, WebServiceInfo var2) {
      if (!var1.isRemote()) {
         try {
            WsdlFileStore var3 = this.wizard.getFileStore();
            String var4 = var2.getWSDLFile();
            FileCollisionResolver var5 = getFileCollisionResolver(this.wizard.getWindow());
            var3.add(var1.getLocator(), var4, var5);
         } catch (IOException var6) {
            this.onFileStorageFailed(var6);
         } catch (FileStoreException var7) {
            this.onFileStorageFailed(var7);
         }
      }
   }

   private void onFileStorageFailed(Exception var1) {
      String var2 = "Failed to store the WSDL file on the local disk. Please contact ETLForge support.";
      ErrorLog.attention(this.wizard, var2, var1);
   }

   @Override
   public void processingCancelled() {
      this.latch.countDown();
   }

   @Override
   public void processingFailed(Throwable var1) {
      if (!this.isCancelled()) {
         this.setStatus(JobStatus.ERROR);
         this.setMessage(UiJobMessages.ERROR);
         this.wizard.handlePageException(Strings.get("Wizard.WsdlProcessor.ErrorMessage"), var1);
         this.latch.countDown();
      }
   }

   @Override
   public void caught(Throwable var1) {
      this.processingFailed(var1);
   }

   @Override
   public void messageChanged(JobMessage var1) {
      this.setMessage(var1.getMessage());
   }

   private static synchronized FileCollisionResolver getFileCollisionResolver(ApplicationWindow var0) {
      if (fileCollisionResolverFactory == null) {
         fileCollisionResolverFactory = FileCollisionResolverFactory.newInstance();
      }

      return fileCollisionResolverFactory.createResolver(var0);
   }
}
