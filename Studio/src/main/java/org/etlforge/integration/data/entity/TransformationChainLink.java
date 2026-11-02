package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.IntegrationId;

public final class TransformationChainLink {
   public static final String NEXT_TRANSFORMATION = "nexttransformation_id";

   private TransformationChainLink() {
   }

   public static final String[] getPropertyKeys() {
      return new String[]{"operation_id", "transformation_id", "nexttransformation_id"};
   }

   public static final String[] getPropertyValues(Operation var0, IntegrationId var1, IntegrationId var2) {
      return new String[]{var0 != null ? var0.getID().toString() : null, var1.toString(), var2.toString()};
   }
}
