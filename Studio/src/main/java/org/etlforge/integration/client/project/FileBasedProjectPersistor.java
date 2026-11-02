package org.etlforge.integration.client.project;

import java.io.File;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;

public interface FileBasedProjectPersistor extends ProjectPersistor {
   File getFile(IntegrationEntity var1);
}
