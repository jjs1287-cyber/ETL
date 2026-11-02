package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.integration.debug.client.file.DataFilesInfo;

public interface MappingTreeDataServiceListener {
   void allDataRemoved();

   void targetDataRemoved();

   void loadSourceDataAvailable(DataFilesInfo var1);

   void testTransformationDataAvailable(DataFilesInfo var1);

   void testOperationDataAvailable(DataFilesInfo var1);

   abstract class Adapter implements MappingTreeDataServiceListener {
      @Override
      public void allDataRemoved() {
      }

      @Override
      public void targetDataRemoved() {
      }

      @Override
      public void loadSourceDataAvailable(DataFilesInfo var1) {
      }

      @Override
      public void testTransformationDataAvailable(DataFilesInfo var1) {
      }

      @Override
      public void testOperationDataAvailable(DataFilesInfo var1) {
      }
   }
}
