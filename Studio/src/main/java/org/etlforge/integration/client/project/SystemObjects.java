package org.etlforge.integration.client.project;

import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityMapLookup;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.SystemTarget;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.project.IntegrationProject;

public final class SystemObjects {
   private static final BuiltInIntegrationEntity[] SYSTEM_OBJECTS = new BuiltInIntegrationEntity[]{
      SystemSource.SOURCE_FROM_PREVIOUS_OPERATION,
      SystemSource.FROM_HTTP_ENDPOINT,
      SystemTarget.WEB_SERVICE_CALL,
      SystemTransformation.PASS_THROUGH,
      SystemTransformation.SALESFORCE_QUERY_REQUEST,
      SystemTransformation.SALESFORCE_STATUS_RESPONSE
   };

   public static BuiltInIntegrationEntity[] getAll() {
      return (BuiltInIntegrationEntity[])SYSTEM_OBJECTS.clone();
   }

   public static void populate(IntegrationProject var0) {
      var0.setSystemObjects(SYSTEM_OBJECTS);
   }

   public static void populate(IntegrationEntityMapLookup var0) {
      for (BuiltInIntegrationEntity var4 : SYSTEM_OBJECTS) {
         var0.addItem((IntegrationEntity)var4);
      }
   }

   private SystemObjects() {
   }
}
