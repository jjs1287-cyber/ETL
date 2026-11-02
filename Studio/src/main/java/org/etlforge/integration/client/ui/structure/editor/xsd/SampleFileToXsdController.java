package org.etlforge.integration.client.ui.structure.editor.xsd;

import java.io.File;
import java.util.UUID;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.structure.editor.xml.xml2xsd.XmlToXsdController;
import org.etlforge.io.Folder;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.system.callback.Receiver;

public abstract class SampleFileToXsdController {
   private final FileType fileType;
   private final SampleFileToXsdUi ui;
   private final String tempFolderName;

   protected SampleFileToXsdController(FileType var1, SampleFileToXsdUi var2, String var3) {
      this.fileType = var1;
      this.ui = var2;
      this.tempFolderName = var3;
   }

   public final void createFromLocalFile(File var1, String var2) {
      LocalSampleFileFactory var3 = new LocalSampleFileFactory(this.fileType, var1, this.createTempFolderForOutput());
      this.startJob(var3, var2);
   }

   private final Folder createTempFolderForOutput() {
      return new Folder(ApplicationTempFolder.newFolderStructure(true, this.tempFolderName, UUID.randomUUID().toString()));
   }

   private void startJob(SampleFileFactory var1, String var2) {
      CreateXsdUiJob var3 = this.createUiJob(var1);
      SampleFileToXsdController.UiCallback var4 = new SampleFileToXsdController.UiCallback();
      var3.setXsdFileName(var2);
      var3.setResultReceiver(var4);
      var3.setErrorHandler(var4);
      var3.setUseProgressDialog(10);
      var3.submit(this.ui.getWaitService(), 0);
   }

   protected abstract CreateXsdUiJob createUiJob(SampleFileFactory var1);

   public final void createFromUrl(String var1, String var2) {
      if (!var1.contains("://")) {
         var1 = "http://" + var1;
      }

      RemoteSampleFileFactory var3 = new RemoteSampleFileFactory(this.fileType, var1, this.createTempFolderForOutput());
      this.startJob(var3, var2);
   }

   private class UiCallback implements Receiver<CreateXsdResult>, ExceptionHandler<Exception> {
      private UiCallback() {
      }

      public void handle(CreateXsdResult var1) {
         SampleFileToXsdController.this.ui.showResultingFiles(var1);
      }

      public void caught(Exception var1) {
         ErrorLog.attention(XmlToXsdController.class, "The XSD file could not be created: " + var1.getMessage(), var1);
      }
   }
}
