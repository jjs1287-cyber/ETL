package org.etlforge.integration.data.structure;

import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class XsdBasedInputStructure extends XsdBasedStructure implements FunctionCallRequestStructure {
   public XsdBasedInputStructure(IntegrationEntityId var1, CallStructureSchema var2) {
      super(DataStructureType.ConnectorCallRequest, var1, var2);
   }
}
