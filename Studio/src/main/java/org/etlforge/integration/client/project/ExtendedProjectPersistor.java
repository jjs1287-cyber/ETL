package org.etlforge.integration.client.project;

import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;

public interface ExtendedProjectPersistor extends ProjectPersistor {
   void saveDeletedItemsInformation() throws InterchangeSavingException;

   void saveUiState(ProjectUiState var1) throws InterchangeSavingException;

   void savePluginInfo() throws InterchangeSavingException;
}
