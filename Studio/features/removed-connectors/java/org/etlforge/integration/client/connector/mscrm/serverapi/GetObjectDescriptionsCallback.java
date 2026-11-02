package org.etlforge.integration.client.connector.mscrm.serverapi;

import com.google.common.collect.ImmutableList;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.mscrm.metadata.BasicEntityDetails;

public interface GetObjectDescriptionsCallback extends ServerCallback {
   void gotDescriptions(ImmutableList<BasicEntityDetails> var1);
}
