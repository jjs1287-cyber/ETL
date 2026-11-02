package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public class WebServiceCallId extends IntegrationEntityId {
   public static final WebServiceCallId UNDEFINED_ID = new WebServiceCallId("2e9f6a0b-14cd-4494-b40b-1eca51eca43b");

   public WebServiceCallId() {
   }

   public WebServiceCallId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.WebServiceCall;
   }
}
