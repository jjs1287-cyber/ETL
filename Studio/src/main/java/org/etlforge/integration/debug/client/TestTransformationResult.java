package org.etlforge.integration.debug.client;

import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.file.DataFile;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.util.id.KongaGuid;

public interface TestTransformationResult extends TestResult {
   TransformationId getDebuggeeId();

   SourceDataIdentifier getSourceDataIdentifier();

   KongaGuid getDataFilesGuid();

   DataFile getLinkFile();

   DataFile getTargetFile();

   String getIntermediateResult();
}
