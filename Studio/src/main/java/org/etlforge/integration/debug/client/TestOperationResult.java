package org.etlforge.integration.debug.client;

import java.util.Map;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.file.DataFilesInfo;

public interface TestOperationResult extends TestResult {
   Map<TransformationId, DataFilesInfo> getDataFilesInfo();

   String getIntermediateResult();
}
