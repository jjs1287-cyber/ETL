package org.etlforge.application.filestore;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileStore {
   FileStoreFile getRoot();

   FileStoreFile addFile(String var1, String var2, FileCollisionResolver var3) throws FileStoreException;

   FileStoreFile addFile(String var1, byte[] var2, FileCollisionResolver var3) throws FileStoreException;

   FileStoreFile addFile(File var1, String var2, FileCollisionResolver var3) throws FileStoreException;

   FileStoreFile getFile(String var1);

   boolean deleteFile(String var1);

   void deleteAll(boolean var1) throws FileStoreException;

   void addDependency(String var1, String var2);

   List<? extends FileStoreFile> getRequiredFiles(String var1) throws FileStoreException;

   void copyDependencies(Map<String, String> var1) throws IOException;
}
