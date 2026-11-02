package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.integration.loadsource.client.SourceDataService;

public interface MappingTreeDataService {
   TransformationFileStore getDataFileStore();

   SourceDataService getSourceDataService();

   TestTransformationService getTestTransformationService();

   TestOperationService getTestOperationService();

   boolean hasSourceData();

   boolean hasTargetData();

   void removeAllTestData();

   void removeTargetData();

   void addListener(MappingTreeDataServiceListener var1);

   void removeListener(MappingTreeDataServiceListener var1);
}
