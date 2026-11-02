package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import org.etlforge.system.ArgCheck;

final class Relationship {
   private final String relationshipName;
   private final String objectType;

   public Relationship(String var1, String var2) {
      this.relationshipName = ArgCheck.checkNotBlank(var1, "relationshipName");
      this.objectType = ArgCheck.checkNotBlank(var2, "objectType");
   }

   public String getRelationshipName() {
      return this.relationshipName;
   }

   public String getObjectType() {
      return this.objectType;
   }

   @Override
   public String toString() {
      return this.relationshipName;
   }
}
