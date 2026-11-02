package org.etlforge.integration.data.structure;

import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class XsdBasedOutputStructure extends XsdBasedStructure implements FunctionCallResponseStructure {
   public XsdBasedOutputStructure(IntegrationEntityId var1, CallStructureSchema var2) {
      super(DataStructureType.ConnectorCallResponse, var1, var2);
   }
}
