package org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl;

import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.Operation;

public class DownloadOperationHandler implements DownloadEntityHandler {
   public Operation createEntity(StringKeyValuePairs var1) {
      return OperationFactory.getOperation(var1);
   }
}
