package org.etlforge.integration.data.function;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.structure.DataStructure;

public interface Function {
   IntegrationEntityId getFunctionID();

   EntityType getEntityType();

   String getName();

   DataStructure getInputStructure();

   DataStructure getOutputStructure();

   boolean acceptsOutputFrom(Function var1);

   boolean hasRequestTransformation();
}
