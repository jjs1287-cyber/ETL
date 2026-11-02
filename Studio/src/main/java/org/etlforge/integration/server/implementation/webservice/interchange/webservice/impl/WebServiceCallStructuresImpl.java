package org.etlforge.integration.server.implementation.webservice.interchange.webservice.impl;

import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceJtr;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureFactory;

final class WebServiceCallStructuresImpl implements WebServiceCallStructures {
   private final MappingTreeStructure request;
   private final MappingTreeStructure response;

   public WebServiceCallStructuresImpl(WebServiceJtr var1) {
      this.request = MappingTreeStructureFactory.fromString(var1.getInputJtr());
      this.response = MappingTreeStructureFactory.fromString(var1.getOutputJtr());
   }

   @Override
   public MappingTreeStructure request() {
      return this.request;
   }

   @Override
   public MappingTreeStructure response() {
      return this.response;
   }
}
