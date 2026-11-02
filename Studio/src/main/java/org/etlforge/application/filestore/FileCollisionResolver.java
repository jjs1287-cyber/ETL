package org.etlforge.application.filestore;

public interface FileCollisionResolver {
   FileCollisionResolver USE_EXISTING = new UseExistingResolverImplementation();
   FileCollisionResolver REPLACE_EXISTING = new ReplaceExistingResolverImplementation();

   FileCollisionResolution resolve(FileStoreFile var1, String var2);

   FileCollisionResolution resolve(FileStoreFile var1, byte[] var2);
}
