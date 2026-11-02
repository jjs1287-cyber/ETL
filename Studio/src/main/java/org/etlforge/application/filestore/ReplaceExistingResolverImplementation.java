package org.etlforge.application.filestore;

final class ReplaceExistingResolverImplementation implements FileCollisionResolver, FileCollisionResolution {
   @Override
   public FileCollisionResolution resolve(FileStoreFile var1, String var2) {
      return this;
   }

   @Override
   public FileCollisionResolution resolve(FileStoreFile var1, byte[] var2) {
      return this;
   }

   @Override
   public FileCollisionResolution.Action getAction() {
      return FileCollisionResolution.Action.REPLACE_EXISTING;
   }

   @Override
   public String getNewName() {
      return null;
   }
}
