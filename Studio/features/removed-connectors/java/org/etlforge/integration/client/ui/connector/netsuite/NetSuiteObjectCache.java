package org.etlforge.integration.client.ui.connector.netsuite;

import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;

public interface NetSuiteObjectCache {
   void evictOldEntries();

   ConnectorObjectCache getObjectListCache();

   NetSuiteDefinitionDesignFileCache getNetSuiteDefinitionDesignFileCache();

   NetSuiteCustomFieldCache getNetSuiteCustomFieldCache();
}
