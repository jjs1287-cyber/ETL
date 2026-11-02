package org.etlforge.integration.data.structure;

import org.etlforge.integration.data.entity.id.WebServiceCallId;

public final class WebServiceResponseStructure extends WebServiceStructure implements FunctionCallResponseStructure {
   public WebServiceResponseStructure(WebServiceCallId var1) {
      super(var1, DataStructureType.WebServiceResponse);
   }
}
