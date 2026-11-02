package org.etlforge.integration.data.structure;

import org.etlforge.integration.data.entity.id.WebServiceCallId;

public final class WebServiceRequestStructure extends WebServiceStructure implements FunctionCallRequestStructure {
   public WebServiceRequestStructure(WebServiceCallId var1) {
      super(var1, DataStructureType.WebServiceRequest);
   }
}
