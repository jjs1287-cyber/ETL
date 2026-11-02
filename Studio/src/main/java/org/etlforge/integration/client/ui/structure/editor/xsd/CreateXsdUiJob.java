package org.etlforge.integration.client.ui.structure.editor.xsd;

import org.etlforge.application.ui.job.ResultProducingUiJob;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.xml.xsd.GeneratedXsds;

public abstract class CreateXsdUiJob extends ResultProducingUiJob<CreateXsdResult> {
   private final SampleFileFactory sampleFileFactory;
   private String xsdFileName;
   private volatile ExceptionHandler<Exception> errorHandler;

   public CreateXsdUiJob(SampleFileFactory var1) {
      super("Generating XSD");
      this.sampleFileFactory = var1;
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(new ExceptionHandler<Throwable>() {
         @Override
         public void caught(Throwable var1) {
            if (CreateXsdUiJob.this.errorHandler != null) {
               if (var1 instanceof Exception) {
                  CreateXsdUiJob.this.errorHandler.caught((Exception)var1);
               } else {
                  CreateXsdUiJob.this.errorHandler.caught(new Exception(var1));
               }
            }
         }
      });
   }

   public final void setXsdFileName(String var1) {
      this.xsdFileName = var1;
   }

   protected final String getXsdFileName() {
      return this.xsdFileName;
   }

   public final void setErrorHandler(ExceptionHandler<Exception> var1) {
      this.errorHandler = var1;
   }

   @Override
   protected final void runImpl() throws InterruptedException {
      try {
         this.setMessage("Reading the XML file");
         SampleFile var1 = this.sampleFileFactory.createSampleFile();
         if (this.isCancelled() || Thread.currentThread().isInterrupted()) {
            return;
         }

         this.setMessage("Generating the XSD");
         GeneratedXsds var2 = this.generateXsds(var1);
         if (this.isCancelled() || Thread.currentThread().isInterrupted()) {
            return;
         }

         this.resultWasComputed(new CreateXsdResult(var1, var2));
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
      } catch (Exception var4) {
         if (this.errorHandler != null) {
            this.errorHandler.caught(var4);
         }
      }
   }

   protected abstract GeneratedXsds generateXsds(SampleFile var1) throws Exception;
}
