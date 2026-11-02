package org.etlforge.integration.client.wsdl;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

public final class WsdlParserFactory {
   public static WsdlParser newParser(WsdlFile var0, FileStore var1) {
      return var0.isRemote() ? new WsdlParserFactory.RemoteWsdlParser() : new WsdlParserFactory.LocalWsdlParser(var0, var1);
   }

   private WsdlParserFactory() {
   }

   private static final class LocalWsdlParser implements WsdlParser {
      private final WsdlFile wsdl;
      private final FileStore fileStore;
      private File tmpDir;

      public LocalWsdlParser(WsdlFile var1, FileStore var2) {
         this.wsdl = var1;
         this.fileStore = var2;
      }

      @Override
      public WebServiceInfo parse(byte[] var1) throws WsdlParserException {
         this.tmpDir = this.createTempDirectory();
         File var2 = this.copyFilesToTmpDir(var1);
         WebServiceInfoBuilder var3 = new WebServiceInfoBuilder();
         return var3.build(var2);
      }

      @Override
      public void dispose() {
         if (this.tmpDir != null) {
            try {
               FileUtils.deleteDirectory(this.tmpDir);
               this.tmpDir = null;
            } catch (IOException var2) {
               ErrorLog.log(WsdlParserFactory.LocalWsdlParser.class, "Failed to remove the temporary folder " + this.tmpDir.getAbsolutePath(), var2);
            }
         }
      }

      private File createTempDirectory() throws WsdlParserException {
         File var1 = ApplicationTempFolder.newFile(UUID.randomUUID().toString());
         if (var1.mkdirs()) {
            return var1;
         } else {
            throw new WsdlParserException("Failed to create temporary directory " + var1.getAbsolutePath() + " for parsing");
         }
      }

      private File copyFilesToTmpDir(byte[] var1) throws WsdlParserException {
         try {
            File var2 = new File(this.tmpDir, this.wsdl.getLocator());
            Files.write(var1, var2);

            for (File var4 : this.getRequiredFiles()) {
               FileUtils.copyFileToDirectory(var4, this.tmpDir);
            }

            return var2;
         } catch (IOException var5) {
            throw new WsdlParserException("Failed to copy the WSDL file to parse to a temporary folder: " + var5.getMessage(), var5);
         } catch (FileStoreException var6) {
            throw new WsdlParserException("Failed to read file dependencies from the project file store: " + var6.getMessage(), var6);
         }
      }

      private Collection<File> getRequiredFiles() throws FileStoreException {
         HashSet var1 = new HashSet();
         FileStoreFile var2 = this.fileStore.getFile(this.wsdl.getLocator());
         if (var2 != null) {
            this.collectDependencies(this.fileStore, var2, var1);
         }

         return var1;
      }

      private void collectDependencies(FileStore var1, FileStoreFile var2, Set<File> var3) throws FileStoreException {
         for (FileStoreFile var5 : var1.getRequiredFiles(var2.getPath())) {
            File var6 = var5.getPhysicalRepresentation();
            if (!var3.contains(var6)) {
               var3.add(var6);
               this.collectDependencies(var1, var5, var3);
            }
         }
      }
   }

   private static final class RemoteWsdlParser implements WsdlParser {
      private RemoteWsdlParser() {
      }

      @Override
      public WebServiceInfo parse(byte[] var1) throws WsdlParserException {
         WebServiceInfoBuilder var2 = new WebServiceInfoBuilder();
         return var2.build(var1);
      }

      @Override
      public void dispose() {
      }
   }
}
