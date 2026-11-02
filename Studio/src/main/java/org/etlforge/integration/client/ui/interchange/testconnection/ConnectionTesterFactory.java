package org.etlforge.integration.client.ui.interchange.testconnection;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;

public interface ConnectionTesterFactory {
   EntityFilter getApplicableItemsFilter();

   ConnectionTester getConnectionTester(IntegrationEntity var1);
}
