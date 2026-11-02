package org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl;

import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface DownloadEntityHandler {
   IntegrationEntity createEntity(StringKeyValuePairs var1) throws ItemCheckoutException;
}
