package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.wsdl.WsdlParserException;
import org.etlforge.integration.client.wsdl.WsdlUtils;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.lang.exception.ExceptionHandler;

public class WsdlParsingJob extends UiJob {
   private final WsdlFile file;
   private final CountDownLatch waitLock;
   private WebServiceInfo parsedInfo;
   private final Object lock;

   public WsdlParsingJob(WsdlFile var1) {
      super(Strings.format("EditWsdl.ParsingJob.Name", var1.getLocator()));
      this.file = var1;
      this.waitLock = new CountDownLatch(1);
      this.lock = new Object();
      this.setUseProgressDialog(200);
      this.setUncaughtExceptionHandler(new ExceptionHandler<Throwable>() {
         @Override
         public void caught(Throwable var1) {
            WsdlParsingJob.this.handleError(ApplicationResources.STRINGS.getString("Error.Unexpected"), var1);
         }
      });
      this.setBlockUntilDone(true);
   }

   public WebServiceInfo getParsedInfo() {
      try {
         this.waitLock.await();
         synchronized (this.lock) {
            return this.parsedInfo;
         }
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         return null;
      }
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.parseFile();
   }

   private void parseFile() {
      try {
         WebServiceInfo var1 = this.file.isRemote()
            ? WsdlUtils.parseRemoteWsdl(this.file.getLocator())
            : WsdlUtils.parseLocalWsdl(new File(this.file.getLocator()));
         this.setParsedInfo(var1);
      } catch (MalformedURLException var2) {
         this.handleError(Strings.get("EditWsdl.ParsingJob.InvalidUrl"), var2);
      } catch (WsdlParserException var3) {
         this.handleError(Strings.get("EditWsdl.ParsingJob.ParseError"), var3);
      }
   }

   private void setParsedInfo(WebServiceInfo var1) {
      synchronized (this.lock) {
         this.parsedInfo = var1;
      }

      this.setStatus(JobStatus.OK);
      this.waitLock.countDown();
   }

   private void handleError(String var1, Throwable var2) {
      this.setStatus(JobStatus.ERROR);
      this.setMessage(UiJobMessages.ERROR);
      ErrorLog.attention(this.getClass(), var1, var2);
      this.waitLock.countDown();
   }
}
