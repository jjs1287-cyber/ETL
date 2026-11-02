package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.debug.client.TestOperationResult;
import org.etlforge.integration.debug.client.TestTransformationResult;
import org.etlforge.integration.debug.client.file.DataFile;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;
import org.etlforge.integration.debug.client.file.DataFileType;
import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.integration.debug.client.file.DataFilesInfoBuilder;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataResult;
import org.etlforge.io.Folder;
import org.etlforge.util.KongaSerializationUtils;
import org.etlforge.util.id.KongaGuid;
import org.etlforge.util.zip.ZipUtils;
import org.etlforge.xml.XmlPrettyPrinter;

public final class TransformationFileStoreImpl implements TransformationFileStore {
   private final Transformation tx;
   private final Folder locationOnDisk;
   private final File infoFile;
   private DataFilesInfo info;

   public TransformationFileStoreImpl(Transformation var1, Folder var2) {
      this.tx = var1;
      this.locationOnDisk = var2.subFolder(var1.getID().toString());
      this.locationOnDisk.createOnDisk();
      this.infoFile = this.locationOnDisk.getFile("DataFile.Info");
   }

   @Override
   public boolean containsFile(DataFileIdentifier var1) {
      synchronized (this.tx) {
         DataFilesInfo var3 = this.getCurrentFilesInfo();
         return var3 != null && var3.contains(var1);
      }
   }

   @Override
   public DataFilesInfo getCurrentFilesInfo() {
      synchronized (this) {
         if (this.info == null && this.infoFile.exists()) {
            try {
               this.info = KongaSerializationUtils.deserializeFromFile(this.infoFile, DataFilesInfo.class);
            } catch (Exception var4) {
               ErrorLog.log(this, "Corrupt project data.", var4);
               this.infoFile.delete();
            }
         }
      }

      return this.info;
   }

   @Override
   public TransformationDataFile getFile(DataFileIdentifier var1) {
      synchronized (this) {
         if (this.info != null && this.info.contains(var1)) {
            String var3 = DataFileName.fromIdentifier(var1);
            File var4 = this.locationOnDisk.getFile(var3);
            if (var4.exists()) {
               boolean var5 = this.isXml(var1);
               return new TransformationFileStoreImpl.TransformationDataFileImpl(var1, var4, var5);
            }
         }
      }

      return TransformationDataFile.NULL;
   }

   private boolean isXml(DataFileIdentifier var1) {
      switch (var1.type) {
         case SOURCE:
            return isXml(this.tx.getInputStructure());
         case TARGET:
            return isXml(this.tx.getOutputStructure());
         case LINK:
            return false;
         default:
            throw new RuntimeException("Unexpected file type: " + var1.type);
      }
   }

   private static boolean isXml(DataStructure var0) {
      return var0.getStructureType().isXmlBased();
   }

   @Override
   public TransformationDataFile addDataFile(KongaGuid var1, DataFileIdentifier var2, DataFile var3) {
      synchronized (this) {
         if (this.info == null) {
            throw new IllegalStateException("No file info");
         }

         if (!this.info.getGuid().equals(var1)) {
            return TransformationDataFile.NULL;
         }

         String var5 = DataFileName.fromIdentifier(var2);
         File var6 = this.locationOnDisk.getFile(var5);
         return this.writeDataFileToProjectResource(var2, var3, var6);
      }
   }

   private TransformationDataFile writeDataFileToProjectResource(DataFileIdentifier var1, DataFile var2, File var3) {
      byte[] var4 = Base64.decodeBase64(var2.compressedContents.getBytes());

      try {
         Files.write(var4, var3);
         boolean var5 = this.isXml(var1);
         return new TransformationFileStoreImpl.TransformationDataFileImpl(var1, var3, var5);
      } catch (IOException var6) {
         ErrorLog.log(this, "Failed to store a transformation data file to disk in the project.", var6);
         return TransformationDataFile.NULL;
      }
   }

   @Override
   public DataFilesInfo storeSourceData(LoadSourceDataResult var1) {
      this.checkLoadSourceDataResult(var1);
      KongaGuid var3 = null;
      DataFilesInfo var2;
      if (var1 != null) {
         SourceDataIdentifier var4 = var1.getIdentifier();
         var3 = var4.getGuid();
         DataFilesInfoBuilder var5 = new DataFilesInfoBuilder(var4.getServerInfo());
         var2 = var5.buildForLoadSource(var3, var1.getTransformationId());
      } else {
         var2 = null;
      }

      synchronized (this) {
         this.deleteAllDataFiles();
         this.setFilesInfo(var2);
         if (var1 != null) {
            DataFileIdentifier var8 = DataFileIdentifier.forLoadSource(var1.getTransformationId());
            this.addDataFile(var3, var8, var1.getSourceData());
         }

         return var2;
      }
   }

   private void checkLoadSourceDataResult(LoadSourceDataResult var1) {
      if (var1 != null && !var1.getTransformationId().equals(this.tx.getID())) {
         this.throwWrongTransformationIdException(var1.getTransformationId());
      }
   }

   private void throwWrongTransformationIdException(TransformationId var1) throws IllegalArgumentException {
      throw new IllegalArgumentException(
         "This file store expects files for the transformation with ID = " + this.tx.getID() + ", but got files for the transformation with ID " + var1 + "."
      );
   }

   private void deleteAllDataFiles() {
      this.deleteDataFiles(new TransformationFileStoreImpl.AllDataFiles());
   }

   private void deleteDataFiles(FileFilter var1) {
      this.locationOnDisk.deleteFiles(var1);
   }

   private void setFilesInfo(DataFilesInfo var1) {
      synchronized (this) {
         this.info = var1;

         try {
            if (var1 == null) {
               this.infoFile.delete();
            } else {
               KongaSerializationUtils.serializeToFile(var1, this.infoFile);
            }
         } catch (IOException var5) {
            ErrorLog.log(this, "Failed to store information about returned data files.", var5);
         }
      }
   }

   @Override
   public SourceDataIdentifier getSourceDataIdentifier() {
      synchronized (this) {
         DataFilesInfo var2 = this.getCurrentFilesInfo();
         if (var2 != null) {
            OperationId var3 = var2.getOperationId();
            DataFileIdentifier var4 = new DataFileIdentifier(var3, this.tx.getID(), DataFileType.SOURCE, 1, 1);
            if (var2.contains(var4)) {
               return new SourceDataIdentifier(var2.getServerInfo(), var2.getGuid());
            }
         }

         return null;
      }
   }

   @Override
   public DataFilesInfo storeTestTransformationResult(TestTransformationResult var1) {
      this.checkTestTransformationResult(var1);
      KongaGuid var3 = null;
      DataFilesInfo var2;
      if (var1 != null) {
         var2 = DataFilesInfoBuilder.buildForTestTransformation(var1);
         var3 = var2.getGuid();
      } else {
         var2 = null;
      }

      synchronized (this) {
         this.removeTargetFiles();
         this.setFilesInfo(var2);
         if (var1 != null) {
            DataFileIdentifier var5 = new DataFileIdentifier(null, this.tx.getID(), DataFileType.LINK, 1, 1);
            this.addDataFile(var3, var5, var1.getLinkFile());
            DataFileIdentifier var6 = new DataFileIdentifier(null, this.tx.getID(), DataFileType.TARGET, 1, 1);
            this.addDataFile(var3, var6, var1.getTargetFile());
         }

         return var2;
      }
   }

   private void checkTestTransformationResult(TestTransformationResult var1) {
      if (var1 != null && !var1.getDebuggeeId().equals(this.tx.getID())) {
         this.throwWrongTransformationIdException(var1.getDebuggeeId());
      }
   }

   @Override
   public DataFilesInfo storeTestOperationResult(TestOperationResult var1) {
      Map var2 = var1.getDataFilesInfo();
      DataFilesInfo var3 = (DataFilesInfo)var2.get(this.tx.getID());
      if (var3 != null) {
         synchronized (this) {
            this.deleteAllDataFiles();
            this.setFilesInfo(var3);
         }
      }

      return var3;
   }

   @Override
   public void removeAll() {
      synchronized (this) {
         this.deleteAllDataFiles();
         this.infoFile.delete();
         this.info = null;
      }
   }

   public void deleteFolderFromDisk() {
      synchronized (this) {
         this.locationOnDisk.delete();
      }
   }

   @Override
   public void removeTargetFiles() {
      synchronized (this) {
         TransformationFileStoreImpl.AllFilesOtherThanLoadSourceFile var2 = new TransformationFileStoreImpl.AllFilesOtherThanLoadSourceFile(this.tx.getID());
         this.deleteDataFiles(var2);
      }
   }

   private static class AllDataFiles implements FileFilter {
      private AllDataFiles() {
      }

      @Override
      public boolean accept(File var1) {
         String var2 = var1.getName().toLowerCase();
         return var2.startsWith("datafile") && var2.endsWith(".dat");
      }
   }

   private static class AllFilesOtherThanLoadSourceFile implements FileFilter {
      private final TransformationId txId;
      private final DataFileIdentifier loadSourceId;

      public AllFilesOtherThanLoadSourceFile(TransformationId var1) {
         this.txId = var1;
         this.loadSourceId = DataFileIdentifier.forLoadSource(var1);
      }

      @Override
      public boolean accept(File var1) {
         String var2 = var1.getName();
         DataFileIdentifier var3 = DataFileName.toIdentifier(var2, this.txId);
         return !this.loadSourceId.equals(var3);
      }
   }

   private static class TransformationDataFileImpl implements TransformationDataFile {
      private final DataFileIdentifier fileId;
      private final File compressedFile;
      private final boolean isXml;
      private File inflatedFile;
      private final Object lock;

      public TransformationDataFileImpl(DataFileIdentifier var1, File var2, boolean var3) {
         this.fileId = var1;
         this.compressedFile = var2;
         this.isXml = var3;
         this.lock = new Object();
      }

      @Override
      public DataFileIdentifier getFileId() {
         return this.fileId;
      }

      @Override
      public File open() throws IOException {
         if (this.inflatedFile == null) {
            File var1 = this.unzipFile();
            this.inflatedFile = this.isXml ? this.prettyPrintXml(var1) : var1;
         }

         return this.inflatedFile;
      }

      @Override
      public void close() {
         boolean var1 = this.tryToDelete();
         if (!var1) {
            ErrorLog.internal(this, null, new Exception("Could not delete the internal file " + this.inflatedFile));
         }
      }

      private File unzipFile() throws IOException {
         File var1 = ApplicationTempFolder.newFile(UUID.randomUUID().toString() + ".dat");
         var1.deleteOnExit();
         byte[] var2 = Files.toByteArray(this.compressedFile);
         ZipUtils.inflateToFile(var2, var1);
         return var1;
      }

      private File prettyPrintXml(File var1) {
         try {
            File var2 = new File(var1.getAbsolutePath() + ".xml");
            XmlPrettyPrinter.withIndent(2).prettyPrint(var1, var2);
            var1.delete();
            return var2;
         } catch (Exception var3) {
            ErrorLog.internal(this.getClass(), "Failed to create a prettified version of a data file that is presumably XML.", var3);
            return var1;
         }
      }

      private boolean tryToDelete() {
         boolean var1 = true;
         synchronized (this.lock) {
            if (this.inflatedFile != null) {
               var1 = this.inflatedFile.delete();
               if (var1) {
                  this.inflatedFile = null;
               }
            }

            return var1;
         }
      }
   }
}
