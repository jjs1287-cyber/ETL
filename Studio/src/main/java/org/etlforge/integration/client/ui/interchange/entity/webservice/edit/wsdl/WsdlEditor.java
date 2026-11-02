package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import java.awt.EventQueue;
import java.io.File;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.interchange.entity.webservice.page.WebServiceCallPage;
import org.etlforge.integration.client.wsdl.WsdlUtils;
import org.etlforge.integration.client.wsdl.serverapi.WsdlProvider;
import org.etlforge.integration.client.wsdl.upload.WsdlUploader;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.SameWsdlPredicate;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitLockHoldingCallback;
import org.etlforge.ui.wait.WaitService;

public final class WsdlEditor {
   private final WebServiceCall wsCall;
   private final EntityPersistor persistor;
   private final DeployFacade deployFacade;
   private final ManagedProject project;
   private WsdlFile wsdlFile;
   private List<WebServiceCall> affected;
   private WebServiceInfo wsInfo;
   private WaitService waitService = WaitService.NONE;

   public WsdlEditor(WebServiceCall var1, EntityPersistor var2, DeployFacade var3) {
      this.persistor = var2;
      this.deployFacade = var3;
      this.wsCall = var1;
      this.project = ProjectUtils.getManagedProject(var1);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void start() {
      WsdlEditor.Workflow var1 = new WsdlEditor.Workflow();
      Application.getWorker().submitForParallel(var1);
   }

   private void continueAfterFileUpload() {
      this.updateAffectedWebServiceCalls();
      this.deploy();
   }

   private void updateAffectedWebServiceCalls() {
      NewWsdlDataApplier var1 = new NewWsdlDataApplier(this.persistor, this.project.getFileStore(), this.wsCall.getWsdlFile());
      WsdlFile var2 = new WsdlFile();
      if (this.wsdlFile.isRemote()) {
         var2.setLocator(this.wsdlFile.getLocator());
      } else {
         var2.setLocator(new File(this.wsdlFile.getLocator()).getName());
      }

      for (WebServiceCall var4 : this.affected) {
         WebServiceOperationInfo var5 = OpInfoFinder.findMatchingOpInfo(var4, this.wsInfo);
         assert var5 != null : "A conflict remained undetected. There is a bug in the conflict detection code.";
         var5.setProperty("Identifier", var2.getLocator());
         var1.apply(var4, var2, var5);
      }
   }

   private void deploy() {
      DeployJob var1 = new DeployJob(this.deployFacade, this.affected);
      var1.submit(this.waitService, 0);
      boolean var2 = var1.awaitResult();
      if (var2) {
         this.refreshAffectedWebServiceCalls();
         this.showSuccessMessage();
      }
   }

   private void refreshAffectedWebServiceCalls() {
      for (WebServiceCall var2 : this.affected) {
         UiCompanion var3 = UiCompanion.of(var2);
         WebServiceCallPage var4 = (WebServiceCallPage)var3.findOpenPage();
         if (var4 != null) {
            var4.refreshTreeStructures();
         }
      }
   }

   private void showSuccessMessage() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SuccessMessage var1 = new SuccessMessage(WsdlEditor.this.affected);
            var1.show();
         }
      });
   }

   private final class UploadLocalWsdlCallback extends WaitLockHoldingCallback {
      public UploadLocalWsdlCallback(WaitLock var2) {
         super(var2);
         this.setReleaseResourceFirst(true);
      }

      @Override
      protected void onSuccess(Object var1) {
         WsdlEditor.this.continueAfterFileUpload();
      }
   }

   private final class UploadRemoteWsdlCallback implements UploadServerFileCallback {
      private final WaitLock waitLock;

      public UploadRemoteWsdlCallback(WaitLock var2) {
         this.waitLock = var2;
      }

      @Override
      public void fileWasUploaded(ServerFileUploadResult var1) {
         this.waitLock.release();
         WsdlEditor.this.continueAfterFileUpload();
      }

      @Override
      public void cancelled() {
         this.waitLock.release();
      }

      @Override
      public void caught(Throwable var1) {
         this.waitLock.release();
      }
   }

   private final class Workflow implements ApplicationWorker.SafeRunnable {
      private Workflow() {
      }

      @Override
      public void run() {
         if (this.acceptableSideEffects() && this.userSelectedNewFile() && this.newFileParsedOk()) {
            this.processPossibleConflicts();
         }
      }

      private boolean acceptableSideEffects() {
         WsdlEditor.this.affected = this.getAffectedWebServiceCalls();
         return this.areSideEffectsGreenLighted();
      }

      private List<WebServiceCall> getAffectedWebServiceCalls() {
         return KongaListUtils.filter(
            WsdlEditor.this.project.getProject().getAllEntitiesOfClass(WebServiceCall.class), new SameWsdlPredicate(WsdlEditor.this.wsCall)
         );
      }

      private boolean areSideEffectsGreenLighted() {
         return WsdlEditor.this.affected.size() == 1 ? true : SideEffectsMessage.isGreenlighted(WsdlEditor.this.wsCall, WsdlEditor.this.affected);
      }

      private boolean userSelectedNewFile() {
         WsdlEditor.this.wsdlFile = NewWsdlSelector.selectFile(WsdlEditor.this.wsCall, WsdlEditor.this.project.getFileStore());
         return WsdlEditor.this.wsdlFile != null;
      }

      private boolean newFileParsedOk() {
         WsdlEditor.this.wsInfo = this.parseWsdlFile(WsdlEditor.this.wsdlFile);
         return WsdlEditor.this.wsInfo != null;
      }

      private WebServiceInfo parseWsdlFile(WsdlFile var1) {
         WsdlParsingJob var2 = new WsdlParsingJob(var1);
         var2.submit();
         return var2.getParsedInfo();
      }

      private void processPossibleConflicts() {
         List var1 = this.getConflictedWsCalls(WsdlEditor.this.wsInfo, WsdlEditor.this.affected);
         if (var1.isEmpty()) {
            this.uploadWsdl(WsdlEditor.this.wsdlFile);
         } else {
            this.showConflicts(var1, WsdlEditor.this.wsInfo);
         }
      }

      private List<WebServiceCall> getConflictedWsCalls(WebServiceInfo var1, List<WebServiceCall> var2) {
         return KongaListUtils.filter(var2, new ConflictDetector(WsdlEditor.this.wsCall, var1));
      }

      private void showConflicts(List<WebServiceCall> var1, WebServiceInfo var2) {
         ConflictUi.show(var1, var2);
      }

      private void uploadWsdl(WsdlFile var1) {
         WaitLock var2 = WsdlEditor.this.waitService.startWait();
         if (var1.isRemote()) {
            WsdlUtils.uploadRemoteWsdl(var1.getLocator(), WsdlEditor.this.new UploadRemoteWsdlCallback(var2));
         } else {
            IntegrationServer var3 = IntegrationServer.getInstance();
            WsdlProvider var4 = var3.getServerCall(WsdlProvider.class);
            WsdlUploader var5 = new WsdlUploader(var4, WsdlEditor.this.project.getFileStore());
            var5.upload(new File(var1.getLocator()), WsdlEditor.this.new UploadLocalWsdlCallback(var2));
         }
      }
   }
}
