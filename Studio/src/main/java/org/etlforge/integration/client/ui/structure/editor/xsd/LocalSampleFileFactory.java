package org.etlforge.integration.client.ui.structure.editor.xsd;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.etlforge.io.Folder;
import org.etlforge.io.KongaIoUtils;

public final class LocalSampleFileFactory implements SampleFileFactory {
   private final FileType fileType;
   private final File localFile;
   private final Folder outputFolder;

   public LocalSampleFileFactory(FileType var1, File var2, Folder var3) {
      this.fileType = var1;
      this.localFile = var2;
      this.outputFolder = var3;
   }

   @Override
   public SampleFile createSampleFile() throws Exception {
      File var1 = this.copyFileIfNecessary();
      var1 = this.inflateFileIfNeccesary(var1);
      return this.fileType.createSampleFile(var1);
   }

   private File copyFileIfNecessary() throws IOException {
      return this.needsCopy() ? this.outputFolder.copyFileToThisFolder(this.localFile) : this.localFile;
   }

   private boolean needsCopy() {
      return !this.outputFolder.asJavaFile().equals(this.localFile.getParentFile());
   }

   private File inflateFileIfNeccesary(File var1) throws IOException {
      LocalSampleFileFactory.GUnzipper var2 = new LocalSampleFileFactory.GUnzipper();
      File var3 = var2.inflate(var1);
      if (var3 == null) {
         LocalSampleFileFactory.Unzipper var4 = new LocalSampleFileFactory.Unzipper();
         var3 = var4.inflate(var1);
      }

      if (var3 == null) {
         return var1;
      }

      var1.delete();
      return var3;
   }

   private static class GUnzipper extends LocalSampleFileFactory.Inflater {
      private GUnzipper() {
      }

      @Override
      protected InputStream createInflatingStream(InputStream var1) {
         try {
            return new GZIPInputStream(var1);
         } catch (IOException var3) {
            return null;
         }
      }
   }

   private abstract static class Inflater {
      private Inflater() {
      }

      public final File inflate(File var1) throws IOException {
         FileInputStream var2 = new FileInputStream(var1);
         InputStream var3 = this.createInflatingStream(var2);
         if (var3 == null) {
            KongaIoUtils.close(var2);
            return null;
         }

         Object var9 = null;

         try {
            File var4 = this.createOutputFile(var1);
            ByteStreams.copy(var3, Files.newOutputStreamSupplier(var4));
            return var4;
         } finally {
            KongaIoUtils.close(var3);
         }
      }

      protected abstract InputStream createInflatingStream(InputStream var1);

      private File createOutputFile(File var1) {
         String var2 = this.getFileName(var1);
         File var3 = new File(var1.getParent(), var2);
         if (var3.equals(var1)) {
            var3 = new File(var1.getParent(), var3.getName() + ".xml");
         }

         return var3;
      }

      protected String getFileName(File var1) {
         String var2 = FilenameUtils.getBaseName(var1.getName());
         if (!var2.toLowerCase().endsWith(".xml")) {
            var2 = var2 + ".xml";
         }

         return var2;
      }
   }

   private static class Unzipper extends LocalSampleFileFactory.Inflater {
      private String entryName;

      private Unzipper() {
      }

      @Override
      protected InputStream createInflatingStream(InputStream var1) {
         ZipInputStream var2 = new ZipInputStream(var1);
         boolean var3 = false;

         try {
            ZipEntry var4 = var2.getNextEntry();
            if (var4 != null) {
               this.entryName = var4.getName();
               var3 = true;
               return var2;
            }
         } catch (IOException var9) {
            var9.printStackTrace();
         } finally {
            if (!var3) {
               KongaIoUtils.close(var2);
            }
         }

         return null;
      }

      @Override
      protected String getFileName(File var1) {
         return StringUtils.isNotBlank(this.entryName) ? this.entryName : super.getFileName(var1);
      }
   }
}
