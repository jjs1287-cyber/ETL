package org.etlforge.integration.client.deploy;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.util.name.Nameable;

public interface DeployedEntityDescriptor extends Nameable {
   IntegrationEntityId getId();

   EntityType getType();

   @Override
   String getName();

   Iterable<DeployedEntityDescriptor> getChildren();

   @Override
   boolean equals(Object var1);

   @Override
   int hashCode();
}
