package org.etlforge.integration.server.exception;

import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class NotDeployedException extends Exception {
   public NotDeployedException(IntegrationEntityId var1) {
      super("The " + var1.getEntityType() + " with GUID = " + var1.toString() + " has not been deployed to this server.");
   }
}
