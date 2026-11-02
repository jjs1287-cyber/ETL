package org.etlforge.integration.client.ui.structure.editor.xsd;

import java.io.File;
import java.util.Set;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdUploadManager;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.DelayedJob;

public abstract class CreateXsdFromSampleFilePage extends AbstractWizardPage {
   private final XsdFileStore fileStore;
   private XsdUploadManager uploader;
   private final SampleFileToXsdUi ui;
   private volatile CreateXsdResult result;
   private final Object lock = new Object();
   private volatile Set<ServerFile> existingFiles;

   protected CreateXsdFromSampleFilePage(Wizard var1, FileType var2, String var3, XsdFileStore var4) {
      super(var1, var3);
      this.fileStore = var4;
      this.ui = new SampleFileToXsdUi(var2);
      this.installController(this.ui);
      this.setCanContinue(false);
      this.ui.setReceiver(new Receiver<CreateXsdResult>() {
         public void handle(CreateXsdResult var1) {
            CreateXsdFromSampleFilePage.this.setResult(var1);
         }
      });
   }

   protected abstract void installController(SampleFileToXsdUi var1);

   private void setResult(CreateXsdResult var1) {
      this.result = var1;
      this.setCanContinue(var1 != null);
   }

   protected final CreateXsdResult getResult() {
      return this.result;
   }

   @Override
   public boolean prepareForForwardNavigation() {
      if (this.result == null) {
         return false;
      }

      boolean var1 = this.resolveConflicts();
      return var1 ? this.uploadXsdFiles() : false;
   }

   private boolean resolveConflicts() {
      XsdConflictChecker var1 = new XsdConflictChecker(new XsdConflictFinder(), this.getMainPanel());
      boolean var2 = var1.resolveConflicts(this.result, this.existingFiles);
      if (!var2) {
         this.result = null;
         this.clearResult();
      }

      return var2;
   }

   private void clearResult() {
      DelayedJob.createAndSchedule(200, new Runnable() {
         @Override
         public void run() {
            CreateXsdFromSampleFilePage.this.ui.clearResult();
            CreateXsdFromSampleFilePage.this.ui.highlightFileNameField();
         }
      });
   }

   private boolean uploadXsdFiles() {
      synchronized (this.lock) {
         if (this.uploader != null) {
            this.uploader.abort();
         }

         XmlInfoProvider var2 = IntegrationServer.getInstance().getServerCall(XmlInfoProvider.class);
         this.uploader = new XsdUploadManager(var2, this.fileStore);
      }

      File var5 = this.result.getMainXsd();
      this.uploader.upload(null, var5.getAbsolutePath());
      return this.uploader.isSuccess();
   }

   @Override
   protected final void dispose() {
      synchronized (this.lock) {
         if (this.uploader != null) {
            this.uploader.abort();
            this.uploader = null;
         }
      }
   }

   @Override
   protected final void onFirstDisplay() {
      this.ui.setWaitService(this.getWaitService());
   }

   @Override
   protected final JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("", "");
      var1.add(this.ui, true, false);
      return var1.asNorthBorderLayout();
   }

   public final void setPreselectedFile(File var1) {
      this.ui.setPreselectedFile(var1);
   }

   public final void setExistingFiles(Set<ServerFile> var1) {
      this.existingFiles = var1;
   }
}
