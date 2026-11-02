package org.etlforge.application.filestore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DummyFileStore implements FileStore {
   private final DefaultFile rootFile;

   public DummyFileStore(File var1) {
      this.rootFile = new DefaultFile(null, var1.getAbsolutePath());
   }

   @Override
   public FileStoreFile getRoot() {
      return this.rootFile;
   }

   @Override
   public FileStoreFile addFile(String var1, String var2, FileCollisionResolver var3) {
      return null;
   }

   @Override
   public FileStoreFile addFile(String var1, byte[] var2, FileCollisionResolver var3) throws FileStoreException {
      return null;
   }

   @Override
   public FileStoreFile addFile(File var1, String var2, FileCollisionResolver var3) {
      return null;
   }

   @Override
   public FileStoreFile getFile(String var1) {
      return null;
   }

   @Override
   public void addDependency(String var1, String var2) {
   }

   @Override
   public boolean deleteFile(String var1) {
      return true;
   }

   @Override
   public void deleteAll(boolean var1) {
   }

   @Override
   public List<? extends FileStoreFile> getRequiredFiles(String var1) {
      return new ArrayList<>();
   }

   @Override
   public void copyDependencies(Map<String, String> var1) throws IOException {
   }
}
