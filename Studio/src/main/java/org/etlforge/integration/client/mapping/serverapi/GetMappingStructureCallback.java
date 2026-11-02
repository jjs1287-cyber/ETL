package org.etlforge.integration.client.mapping.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

public interface GetMappingStructureCallback extends ServerCallback {
   void jtrGenerated(MappingTreeStructure var1);
}
