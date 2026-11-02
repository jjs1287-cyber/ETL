package org.etlforge.integration.client.entity.settings;

import org.etlforge.integration.data.entity.IntegrationEntity;

public interface EntityResources {
   EntitySettings getSettings();

   FileResource getFileResource(IntegrationEntity var1, String var2);

   void export(IntegrationEntity var1, EntityResources var2);

   default void exportAs(IntegrationEntity source, IntegrationEntity destination, EntityResources target) {
      if (!source.getID().equals(destination.getID())) {
         throw new UnsupportedOperationException("This resource store cannot translate object identities.");
      }
      export(source, target);
   }
}
