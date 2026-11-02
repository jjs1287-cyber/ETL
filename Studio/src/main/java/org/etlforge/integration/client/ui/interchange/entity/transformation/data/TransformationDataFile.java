package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import java.io.File;
import java.io.IOException;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;

public interface TransformationDataFile {
   TransformationDataFile NULL = new TransformationDataFile() {
      @Override
      public DataFileIdentifier getFileId() {
         return null;
      }

      @Override
      public File open() {
         return null;
      }

      @Override
      public void close() {
      }
   };

   DataFileIdentifier getFileId();

   File open() throws IOException;

   void close();
}
