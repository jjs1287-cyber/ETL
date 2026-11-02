package org.etlforge.integration.loadsource.serverapi;

import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.file.DataFile;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;

public interface LoadSourceDataResult {
   SourceDataIdentifier getIdentifier();

   TransformationId getTransformationId();

   DataFile getSourceData();
}
