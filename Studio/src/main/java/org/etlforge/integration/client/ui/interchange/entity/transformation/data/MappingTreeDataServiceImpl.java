package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.debug.client.TestOperationResult;
import org.etlforge.integration.debug.client.TestTransformationResult;
import org.etlforge.integration.debug.client.file.DataFileType;
import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.integration.debug.serverapi.DataFileProvider;
import org.etlforge.integration.loadsource.client.SourceDataService;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataResult;

final class MappingTreeDataServiceImpl implements MappingTreeDataService {
   private final TransformationFileStore dataFileStore;
   private final ServerManager serverManager;
   private SourceDataService loadSourceService;
   private TestTransformationService testTransformationService;
   private TestOperationService testOperationService;
   private final List<MappingTreeDataServiceListener> listeners = Lists.newCopyOnWriteArrayList();

   public MappingTreeDataServiceImpl(TransformationPage var1, ServerManager var2, TransformationFileStore var3, DataFileProvider var4) {
      this.serverManager = var2;
      this.dataFileStore = var3;
      TransformationPageDataFileManager.install(var1, this, var4);
   }

   void setLoadSourceService(SourceDataService var1) {
      this.loadSourceService = var1;
   }

   void setTestTransformationService(TestTransformationService var1) {
      this.testTransformationService = var1;
   }

   void setTestOperationService(TestOperationService var1) {
      this.testOperationService = var1;
   }

   ServerManager getServerManager() {
      return this.serverManager;
   }

   @Override
   public TransformationFileStore getDataFileStore() {
      return this.dataFileStore;
   }

   @Override
   public SourceDataService getSourceDataService() {
      return this.loadSourceService;
   }

   @Override
   public TestOperationService getTestOperationService() {
      return this.testOperationService;
   }

   @Override
   public TestTransformationService getTestTransformationService() {
      return this.testTransformationService;
   }

   @Override
   public boolean hasSourceData() {
      return this.hasFileOfType(DataFileType.SOURCE);
   }

   private boolean hasFileOfType(DataFileType var1) {
      synchronized (this.dataFileStore) {
         DataFilesInfo var3 = this.dataFileStore.getCurrentFilesInfo();
         return var3 != null && var3.containsFileOfType(var1);
      }
   }

   @Override
   public boolean hasTargetData() {
      return this.hasFileOfType(DataFileType.TARGET);
   }

   @Override
   public void removeAllTestData() {
      this.dataFileStore.removeAll();
      this.fireAllDataRemoved();
   }

   private void fireAllDataRemoved() {
      for (MappingTreeDataServiceListener var2 : this.listeners) {
         var2.allDataRemoved();
      }
   }

   @Override
   public void removeTargetData() {
      this.dataFileStore.removeTargetFiles();
      this.fireTargetDataRemoved();
   }

   private void fireTargetDataRemoved() {
      for (MappingTreeDataServiceListener var2 : this.listeners) {
         var2.targetDataRemoved();
      }
   }

   @Override
   public void addListener(MappingTreeDataServiceListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(MappingTreeDataServiceListener var1) {
      this.listeners.remove(var1);
   }

   public void storeTestTransformationResult(TestTransformationResult var1) {
      DataFilesInfo var2 = this.dataFileStore.storeTestTransformationResult(var1);
      if (var2 != null) {
         this.fireTestTransformationDataAvailable(var2);
      }
   }

   private void fireTestTransformationDataAvailable(DataFilesInfo var1) {
      for (MappingTreeDataServiceListener var3 : this.listeners) {
         var3.testTransformationDataAvailable(var1);
      }
   }

   public void storeTestOperationResult(TestOperationResult var1) {
      DataFilesInfo var2 = this.dataFileStore.storeTestOperationResult(var1);
      if (var2 != null) {
         this.fireTestOperationDataAvailable(var2);
      }
   }

   private void fireTestOperationDataAvailable(DataFilesInfo var1) {
      for (MappingTreeDataServiceListener var3 : this.listeners) {
         var3.testOperationDataAvailable(var1);
      }
   }

   public void storeLoadSourceResult(LoadSourceDataResult var1) {
      DataFilesInfo var2 = this.dataFileStore.storeSourceData(var1);
      if (var2 != null) {
         this.fireLoadSourceDataAvailable(var2);
      }
   }

   private void fireLoadSourceDataAvailable(DataFilesInfo var1) {
      for (MappingTreeDataServiceListener var3 : this.listeners) {
         var3.loadSourceDataAvailable(var1);
      }
   }
}
