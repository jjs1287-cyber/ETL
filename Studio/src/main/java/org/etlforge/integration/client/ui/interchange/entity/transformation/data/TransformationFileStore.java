package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.integration.debug.client.TestOperationResult;
import org.etlforge.integration.debug.client.TestTransformationResult;
import org.etlforge.integration.debug.client.file.DataFile;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;
import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataResult;
import org.etlforge.util.id.KongaGuid;

public interface TransformationFileStore {
   void removeAll();

   void removeTargetFiles();

   DataFilesInfo getCurrentFilesInfo();

   boolean containsFile(DataFileIdentifier var1);

   TransformationDataFile getFile(DataFileIdentifier var1);

   TransformationDataFile addDataFile(KongaGuid var1, DataFileIdentifier var2, DataFile var3);

   DataFilesInfo storeSourceData(LoadSourceDataResult var1);

   SourceDataIdentifier getSourceDataIdentifier();

   DataFilesInfo storeTestTransformationResult(TestTransformationResult var1);

   DataFilesInfo storeTestOperationResult(TestOperationResult var1);
}
