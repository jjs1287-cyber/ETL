package org.etlforge.application.filestore;

public interface FileCollisionResolution {
   FileCollisionResolution.Action getAction();

   String getNewName();

   enum Action {
      USE_EXISTING,
      REPLACE_EXISTING,
      RENAME_AND_USE_NEW;
   }
}
