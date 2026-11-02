package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class TransformationId extends IntegrationEntityId {
   public static final TransformationId UNDEFINED_ID = new TransformationId("c8d3afbe-0f59-4578-b343-5ef31ff520c6");
   public static final TransformationId PASS_THROUGH_ID = new TransformationId("a0f5b95d-b1ee-446a-a136-6b71dfa78574");
   public static final TransformationId SALESFORCE_QUERY_REQUEST_ID = new TransformationId("e4dce626-e022-4aec-916f-099c7ad537ba");
   public static final TransformationId SALESFORCE_STATUS_RESPONSE_ID = new TransformationId("735d2200-fbcd-4502-8cb6-981ecc50cb0f");

   public static boolean isPassThrough(IntegrationId var0) {
      return PASS_THROUGH_ID.equals(var0);
   }

   public TransformationId() {
   }

   public TransformationId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.Transformation;
   }
}
