package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;
import org.etlforge.integration.debug.client.file.DataFileType;
import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.integration.debug.serverapi.DataFileProvider;
import org.etlforge.system.callback.Receiver;

final class TransformationPageDataFileManager {
   private static final long LARGE_FILE_SIZE = 1000000L;
   private final TransformationPage page;
   private final MappingTreeDataService dataService;
   private final TransformationFileStore fileStore;
   private final MappingTreeDataServiceListener listener;
   private final DataFileRetriever fileRetriever;
   private TransformationDataFile sourceFile;
   private TransformationDataFile linkFile;
   private TransformationDataFile targetFile;

   public static TransformationPageDataFileManager install(TransformationPage var0, MappingTreeDataService var1, DataFileProvider var2) {
      return new TransformationPageDataFileManager(var0, var1, var2);
   }

   private TransformationPageDataFileManager(TransformationPage var1, MappingTreeDataService var2, DataFileProvider var3) {
      this.page = var1;
      this.dataService = var2;
      this.fileStore = var2.getDataFileStore();
      this.fileRetriever = new DataFileRetriever(this.fileStore, var3);
      this.listener = new MappingTreeDataServiceEDTListener(new TransformationPageDataFileManager.ServiceListener());
      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            TransformationPageDataFileManager.this.activate();
         }
      });
   }

   private void activate() {
      this.listenToFileStore();
      this.registerCleanup();
      this.openExistingDataFiles();
   }

   private void listenToFileStore() {
      this.dataService.addListener(this.listener);
   }

   private void registerCleanup() {
      this.page.addResource(new TransformationPageDataFileManager.Disposer());
   }

   private void openExistingDataFiles() {
      synchronized (this.fileStore) {
         DataFilesInfo var2 = this.fileStore.getCurrentFilesInfo();
         if (var2 != null) {
            boolean var3 = true;
            TransformationPageDataFileManager.TestFileOpener var4 = new TransformationPageDataFileManager.TestFileOpener(var2, var3);
            var4.setConfirmDisplayOfLargeTestFiles(true);
            var4.start();
         }
      }
   }

   private boolean hasPageBeenClosed() {
      return this.page.getWindow() == null;
   }

   private File openDataFile(TransformationDataFile var1) {
      try {
         return var1.open();
      } catch (IOException var3) {
         ErrorLog.attention(this.getClass(), "Could not open the requested Transformation data file.", var3);
         return null;
      }
   }

   private void closeOpenFiles() {
      assert EventQueue.isDispatchThread();
      this.closeSourceFile();
      this.closeTargetAndLinkFiles();
   }

   private void closeSourceFile() {
      this.close(this.sourceFile);
      this.sourceFile = null;
   }

   private void closeTargetAndLinkFiles() {
      assert EventQueue.isDispatchThread();
      this.close(this.linkFile);
      this.linkFile = null;
      this.close(this.targetFile);
      this.targetFile = null;
   }

   private void close(TransformationDataFile var1) {
      if (var1 != null) {
         var1.close();
      }
   }

   private class Disposer extends AbstractWindowElementResource {
      private Disposer() {
      }

      @Override
      public void dispose(WindowElement var1) {
         TransformationPageDataFileManager.this.closeOpenFiles();
         this.removeListener();
         this.deleteTestFilesIfNeeded();
      }

      private void removeListener() {
         TransformationPageDataFileManager.this.dataService.removeListener(TransformationPageDataFileManager.this.listener);
      }

      private void deleteTestFilesIfNeeded() {
         boolean var1 = !RememberTreeMapperTestDataPreference.get();
         if (var1) {
            TransformationPageDataFileManager.this.dataService.removeAllTestData();
         }
      }
   }

   private class ServiceListener implements MappingTreeDataServiceListener {
      private ServiceListener() {
      }

      @Override
      public void allDataRemoved() {
         TransformationPageDataFileManager.this.page.getTreeMapper().removeSourceAndTargetData();
         TransformationPageDataFileManager.this.closeOpenFiles();
      }

      @Override
      public void targetDataRemoved() {
         TransformationPageDataFileManager.this.page.getTreeMapper().removeTargetData();
         TransformationPageDataFileManager.this.closeTargetAndLinkFiles();
      }

      @Override
      public void loadSourceDataAvailable(DataFilesInfo var1) {
         this.openSourceFile();
      }

      private void openSourceFile() {
         assert EventQueue.isDispatchThread();
         if (!TransformationPageDataFileManager.this.hasPageBeenClosed()) {
            DataFileIdentifier var1 = DataFileIdentifier.forLoadSource(TransformationPageDataFileManager.this.page.getObject().getID());
            TransformationPageDataFileManager.this.fileRetriever.retrieveDataFile(var1, new Receiver<DataFileRetriever.Result>() {
               public void handle(DataFileRetriever.Result var1) {
                  TransformationPageDataFileManager.this.sourceFile = var1.dataFile;
                  if (!var1.canceled && !var1.failed) {
                     File var2 = TransformationPageDataFileManager.this.openDataFile(TransformationPageDataFileManager.this.sourceFile);
                     if (var2 != null) {
                        TreeMapper var3 = TransformationPageDataFileManager.this.page.getTreeMapper();
                        var3.openSourceDataFile(var2, 0, -1);
                        TransformationPageDataFileManager.this.page.askForAttention();
                     }
                  }
               }
            });
         }
      }

      @Override
      public void testTransformationDataAvailable(DataFilesInfo var1) {
         TransformationPageDataFileManager.this.closeTargetAndLinkFiles();
         this.openTargetAndLinkFile(var1);
      }

      private void openTargetAndLinkFile(DataFilesInfo var1) {
         assert EventQueue.isDispatchThread();
         if (!TransformationPageDataFileManager.this.hasPageBeenClosed()) {
            boolean var2 = false;
            TransformationPageDataFileManager.TestFileOpener var3 = TransformationPageDataFileManager.this.new TestFileOpener(var1, var2);
            var3.setAskForAttention(true);
            var3.start();
         }
      }

      @Override
      public void testOperationDataAvailable(DataFilesInfo var1) {
         this.openTestOperationFiles(var1);
      }

      private void openTestOperationFiles(DataFilesInfo var1) {
         assert EventQueue.isDispatchThread();
         if (!TransformationPageDataFileManager.this.hasPageBeenClosed()) {
            boolean var2 = true;
            TransformationPageDataFileManager.TestFileOpener var3 = TransformationPageDataFileManager.this.new TestFileOpener(var1, var2);
            var3.setAskForAttention(true);
            var3.start();
         }
      }
   }

   private class SourceAndTargetDataDisplayer implements Receiver<Boolean> {
      private final File sourceFile;
      private final File linkFile;
      private final File targetFile;
      private final boolean askUserToConfirm;
      private final boolean askForAttentionWhenDone;

      public SourceAndTargetDataDisplayer(File var2, File var3, File var4, boolean var5, boolean var6) {
         this.sourceFile = var2;
         this.linkFile = var3;
         this.targetFile = var4;
         if (var5) {
            long var7 = this.calculateSize(var2, var3, var4);
            this.askUserToConfirm = var7 > 1000000L;
         } else {
            this.askUserToConfirm = false;
         }

         this.askForAttentionWhenDone = var6;
      }

      private long calculateSize(File... var1) {
         long var2 = 0L;

         for (File var7 : var1) {
            if (var7 != null) {
               var2 += var7.length();
            }
         }

         return var2;
      }

      public void start() {
         if (this.askUserToConfirm) {
            TransformationPageDataFileManager.this.page.confirmOpeningOfLargeTestFile(this);
         } else {
            this.displayData();
         }
      }

      private void displayData() {
         TreeMapper var1 = TransformationPageDataFileManager.this.page.getTreeMapper();
         var1.startWait();
         boolean var2 = false;

         try {
            int[] var3 = this.preProcessLinkFile(var1);
            if (this.sourceFile != null) {
               var1.openSourceDataFile(this.sourceFile, var3[0], var3[1]);
               var2 = true;
            }

            if (this.linkFile != null && this.targetFile != null) {
               var1.openTargetDataFile(this.targetFile, this.linkFile, var3[2], var3[3], var3[4]);
               var2 = true;
            } else {
               TransformationPageDataFileManager.this.closeTargetAndLinkFiles();
            }

            if (var2 && this.askForAttentionWhenDone) {
               TransformationPageDataFileManager.this.page.askForAttention();
            }
         } finally {
            var1.stopWait();
         }
      }

      private int[] preProcessLinkFile(TreeMapper var1) {
         return this.linkFile != null ? var1.preprocessLinkFile(this.linkFile) : new int[]{0, -1, 0, -1, 0};
      }

      public void handle(Boolean var1) {
         if (var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  SourceAndTargetDataDisplayer.this.displayData();
               }
            });
         } else {
            this.deleteAllFiles();
         }
      }

      private void deleteAllFiles() {
         TransformationPageDataFileManager.this.closeOpenFiles();
         TransformationPageDataFileManager.this.dataService.removeAllTestData();
      }
   }

   private class SourceDataDisplayer implements Receiver<Boolean> {
      private final File sourceFile;
      private final boolean confirmLargeFile;

      public SourceDataDisplayer(File var2, boolean var3) {
         this.sourceFile = var2;
         this.confirmLargeFile = var3;
      }

      public void start() {
         if (this.confirmLargeFile && this.sourceFile.length() > 1000000L) {
            TransformationPageDataFileManager.this.page.confirmOpeningOfLargeTestFile(this);
         } else {
            this.displayData();
         }
      }

      private void displayData() {
         TreeMapper var1 = TransformationPageDataFileManager.this.page.getTreeMapper();
         var1.startWait();

         try {
            var1.openSourceDataFile(this.sourceFile, 0, -1);
         } finally {
            var1.stopWait();
         }
      }

      public void handle(Boolean var1) {
         if (var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  SourceDataDisplayer.this.displayData();
               }
            });
         } else {
            this.deleteAllFiles();
         }
      }

      private void deleteAllFiles() {
         TransformationPageDataFileManager.this.closeSourceFile();
         TransformationPageDataFileManager.this.dataService.removeAllTestData();
      }
   }

   private class TestFileOpener {
      private final DataFileIdentifier sourceFileId;
      private final DataFileIdentifier linkFileId;
      private final DataFileIdentifier targetFileId;
      private final boolean startWithSourceFile;
      private boolean askForAttention;
      private boolean confirmDisplayOfLargeTestFiles;

      public TestFileOpener(DataFilesInfo var2, boolean var3) {
         this.startWithSourceFile = var3;
         OperationId var4 = var2.getOperationId();
         TransformationId var5 = TransformationPageDataFileManager.this.page.getObject().getID();
         this.sourceFileId = new DataFileIdentifier(var4, var5, DataFileType.SOURCE, 1, 1);
         this.linkFileId = new DataFileIdentifier(var4, var5, DataFileType.LINK, 1, 1);
         this.targetFileId = new DataFileIdentifier(var4, var5, DataFileType.TARGET, 1, 1);
      }

      public void setAskForAttention(boolean var1) {
         this.askForAttention = var1;
      }

      public void setConfirmDisplayOfLargeTestFiles(boolean var1) {
         this.confirmDisplayOfLargeTestFiles = var1;
      }

      public void start() {
         if (this.startWithSourceFile) {
            this.retrieveSourceFile();
         } else {
            this.retrieveLinkFile();
         }
      }

      private void retrieveSourceFile() {
         TransformationPageDataFileManager.this.fileRetriever.retrieveDataFile(this.sourceFileId, new Receiver<DataFileRetriever.Result>() {
            public void handle(DataFileRetriever.Result var1) {
               assert EventQueue.isDispatchThread();
               TransformationPageDataFileManager.this.sourceFile = var1.dataFile;
               if (TestFileOpener.this.canContinueAfterSource(var1)) {
                  TestFileOpener.this.retrieveLinkFile();
               }
            }
         });
      }

      private boolean canContinueAfterSource(DataFileRetriever.Result var1) {
         if (this.canContinue(var1)) {
            return true;
         } else if (var1.unknownFile) {
            Transformation var2 = TransformationPageDataFileManager.this.page.getObject();
            DataStructure var3 = var2.getInputStructure();
            return var3 instanceof NoStructure;
         } else {
            return false;
         }
      }

      private boolean canContinue(DataFileRetriever.Result var1) {
         return !var1.canceled && !var1.failed && !TransformationPageDataFileManager.this.hasPageBeenClosed();
      }

      private void retrieveLinkFile() {
         TransformationPageDataFileManager.this.fileRetriever
            .retrieveDataFile(
               this.linkFileId,
               new Receiver<DataFileRetriever.Result>() {
                  public void handle(DataFileRetriever.Result var1) {
                     assert EventQueue.isDispatchThread();
                     TransformationPageDataFileManager.this.linkFile = var1.dataFile;
                     if (TestFileOpener.this.canContinue(var1)) {
                        TestFileOpener.this.retrieveTargetFile();
                     } else {
                        this.handleLinkFileNotPresent();
                     }
                  }

                  private void handleLinkFileNotPresent() {
                     if (TestFileOpener.this.startWithSourceFile && !TransformationPageDataFileManager.this.hasPageBeenClosed()) {
                        File var1 = TestFileOpener.this.openSourceFileIfPresent();
                        if (var1 != null) {
                           TransformationPageDataFileManager.SourceDataDisplayer var2 = TransformationPageDataFileManager.this.new SourceDataDisplayer(
                              var1, TestFileOpener.this.confirmDisplayOfLargeTestFiles
                           );
                           var2.start();
                        }
                     }
                  }
               }
            );
      }

      private void retrieveTargetFile() {
         TransformationPageDataFileManager.this.fileRetriever.retrieveDataFile(this.targetFileId, new Receiver<DataFileRetriever.Result>() {
            public void handle(DataFileRetriever.Result var1) {
               assert EventQueue.isDispatchThread();
               TransformationPageDataFileManager.this.targetFile = var1.dataFile;
               if (!TestFileOpener.this.canContinue(var1) && !var1.unknownFile) {
                  TransformationPageDataFileManager.this.closeOpenFiles();
               } else {
                  TestFileOpener.this.openRetrievedFiles();
               }
            }
         });
      }

      private void openRetrievedFiles() {
         assert EventQueue.isDispatchThread();
         File var1 = this.openSourceFileIfPresent();
         if (var1 != null
            || TransformationPageDataFileManager.this.sourceFile == null
            || TransformationPageDataFileManager.this.sourceFile == TransformationDataFile.NULL) {
            File var2 = TransformationPageDataFileManager.this.openDataFile(TransformationPageDataFileManager.this.linkFile);
            File var3 = var2 != null ? TransformationPageDataFileManager.this.openDataFile(TransformationPageDataFileManager.this.targetFile) : null;
            TransformationPageDataFileManager.SourceAndTargetDataDisplayer var4 = TransformationPageDataFileManager.this.new SourceAndTargetDataDisplayer(
               var1, var2, var3, this.confirmDisplayOfLargeTestFiles, this.askForAttention
            );
            var4.start();
         }
      }

      private File openSourceFileIfPresent() {
         return TransformationPageDataFileManager.this.sourceFile == null
            ? null
            : TransformationPageDataFileManager.this.openDataFile(TransformationPageDataFileManager.this.sourceFile);
      }
   }
}
