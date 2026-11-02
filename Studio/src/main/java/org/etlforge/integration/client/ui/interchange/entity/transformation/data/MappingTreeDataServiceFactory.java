package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import java.io.File;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.CurrentProjectServiceListener;
import org.etlforge.integration.client.project.ProjectFolders;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.TestOperationResult;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.TestTransformationResult;
import org.etlforge.integration.debug.serverapi.DataFileProvider;
import org.etlforge.io.Folder;

public final class MappingTreeDataServiceFactory {
   private final MappingTreeDataServiceFactory.CleaningService cleaningService = new MappingTreeDataServiceFactory.CleaningService();
   private final ServerManager serverManager;
   private final DebugService debugService;

   public MappingTreeDataServiceFactory(CurrentProjectService var1, ServerManager var2, DebugService var3) {
      this.serverManager = var2;
      this.debugService = var3;
      var1.addListener(this.cleaningService);
   }

   public MappingTreeDataService newService(TransformationPage var1, DeployFacade var2, DataFileProvider var3) {
      TransformationFileStore var4 = this.createFileStore(var1);
      final MappingTreeDataServiceImpl var5 = new MappingTreeDataServiceImpl(var1, this.serverManager, var4, var3);
      SourceDataServiceImpl var6 = new SourceDataServiceImpl(var1, var5, var2);
      var5.setLoadSourceService(var6);
      TestTransformationServiceImpl var7 = new TestTransformationServiceImpl(var1, this.debugService, var6, var2, this.getTestProcessCallback(var5));
      var5.setTestTransformationService(var7);
      TestOperationServiceImpl var8 = new TestOperationServiceImpl(var1, this.debugService, new TestProcessCallback() {
         @Override
         public void beforeTestStarts() {
            var5.removeAllTestData();
         }

         @Override
         public void testCompletedSuccessfully(TestResult var1) {
            var5.storeTestOperationResult((TestOperationResult)var1);
         }
      });
      var5.setTestOperationService(var8);
      return var5;
   }

   private TestProcessCallback getTestProcessCallback(final MappingTreeDataServiceImpl var1) {
      return new TestProcessCallback() {
         @Override
         public void beforeTestStarts() {
         }

         @Override
         public void testCompletedSuccessfully(TestResult var1x) {
            var1.storeTestTransformationResult((TestTransformationResult)var1x);
         }
      };
   }

   private TransformationFileStore createFileStore(TransformationPage var1) {
      Transformation var2 = var1.getObject();
      Folder var3 = this.getTestFileRoot(var2);
      return new TransformationFileStoreImpl(var2, var3);
   }

   private Folder getTestFileRoot(Transformation var1) {
      IntegrationProject var2 = ProjectUtils.getProject(var1);
      return getProjectTestFileRoot(var2);
   }

   private static Folder getProjectTestFileRoot(IntegrationProject var0) {
      File var1 = var0.getLocationOnDisk();
      Folder var2 = ProjectFolders.getTestDataFolder(var1);
      return var2.subFolder("tx");
   }

   private static class CleaningService extends CurrentProjectServiceListener.Adapter {
      private Folder folderToClean;

      private CleaningService() {
      }

      @Override
      public synchronized void projectOpened(CurrentProjectEvent var1) {
         IntegrationProject var2 = var1.getProject();
         this.folderToClean = MappingTreeDataServiceFactory.getProjectTestFileRoot(var2);
      }

      @Override
      public synchronized void projectClosed(CurrentProjectEvent var1) {
         if (this.folderToClean != null) {
            for (Folder var5 : this.folderToClean.listSubFolders()) {
               var5.delete();
            }
         }
      }
   }
}
