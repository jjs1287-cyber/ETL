package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import java.io.File;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.integration.loadsource.client.LoadSourceDataFileInput;
import org.etlforge.integration.loadsource.client.RecentSourceDataFiles;
import org.etlforge.integration.loadsource.client.SourceDataService;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataResult;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.system.callback.CallbackResultWrapper;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.wait.WaitLock;

final class SourceDataServiceImpl implements SourceDataService {
   private final MappingTreeDataServiceImpl dataService;
   private final DeployFacade deployFacade;
   private final TransformationPage page;
   private final RecentFileStore testFileStore;

   public SourceDataServiceImpl(TransformationPage var1, MappingTreeDataServiceImpl var2, DeployFacade var3) {
      this.page = var1;
      this.dataService = var2;
      this.deployFacade = var3;
      this.testFileStore = this.createProjectFileStore();
   }

   private RecentFileStore createProjectFileStore() {
      try {
         return new RecentSourceDataFiles(new LoadSourceFileStoreImpl(this.getTransformation()));
      } catch (FileStoreException var2) {
         var2.printStackTrace();
         return RecentFileStore.EMPTY;
      }
   }

   public ServerManager getServerManager() {
      return this.dataService.getServerManager();
   }

   public TransformationFileStore getDataFileStore() {
      return this.dataService.getDataFileStore();
   }

   public void removeTestData() {
      this.dataService.removeAllTestData();
   }

   @Override
   public void storeSourceData(LoadSourceDataResult var1) {
      this.dataService.storeLoadSourceResult(var1);
   }

   @Override
   public SourceDataIdentifier getSourceDataToken() {
      return this.getDataFileStore().getSourceDataIdentifier();
   }

   @Override
   public void loadSourceData(LoadSourceDataInput var1, CallbackResult var2) {
      SourceDataServiceImpl.LoadSourceDataWorkFlow var3 = new SourceDataServiceImpl.LoadSourceDataWorkFlow();
      var3.setInput(var1);
      var3.start(new CallbackResultWrapper(var2));
   }

   @Override
   public void loadSourceData(CallbackResult var1) {
      SourceDataServiceImpl.LoadSourceDataWorkFlow var2 = new SourceDataServiceImpl.LoadSourceDataWorkFlow();
      var2.start(var1);
   }

   private Transformation getTransformation() {
      return this.page.getObject();
   }

   private final class LoadSourceDataWorkFlow {
      private volatile LoadSourceDataInput input;

      private LoadSourceDataWorkFlow() {
      }

      public void setInput(LoadSourceDataInput var1) {
         WaitLock var2 = SourceDataServiceImpl.this.page.getWaitService().startWait(50);

         try {
            if (var1 instanceof LoadSourceDataFileInput) {
               File var3 = ((LoadSourceDataFileInput)var1).getFile();
               if (var3.isFile()) {
                  SourceDataServiceImpl.this.testFileStore.update(var3);
               }
            }

            this.input = var1;
         } finally {
            var2.release();
         }
      }

      public void start(final CallbackResult var1) {
         Transformation var2 = SourceDataServiceImpl.this.getTransformation();
         DeployGate var3 = SourceDataServiceImpl.this.deployFacade.newDeployGate();
         var3.setItemsToCheck(var2);
         var3.setJob(new Runnable() {
            @Override
            public void run() {
               LoadSourceDataWorkFlow.this.loadSourceDataImpl(var1);
            }
         });
         var3.start();
      }

      private void loadSourceDataImpl(CallbackResult var1) {
         LoadSourceDataRunner var2 = new LoadSourceDataRunner(SourceDataServiceImpl.this, SourceDataServiceImpl.this.page, var1);
         var2.setInput(this.input);
         var2.setProjectFileStore(SourceDataServiceImpl.this.testFileStore);
         UiUtils.runOnEventThread(var2);
      }
   }
}
