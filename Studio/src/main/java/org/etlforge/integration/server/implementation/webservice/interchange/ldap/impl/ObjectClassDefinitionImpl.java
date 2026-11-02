package org.etlforge.integration.server.implementation.webservice.interchange.ldap.impl;

import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassType;
import org.etlforge.integration.server.implementation.webservice.interchange.ldap.client.WsObjectClassInfo;

class ObjectClassDefinitionImpl implements ObjectClassDefinition {
   private final WsObjectClassInfo raw;

   ObjectClassDefinitionImpl(WsObjectClassInfo var1) {
      this.raw = var1;
   }

   @Override
   public String getDescription() {
      return this.raw.getDescription();
   }

   @Override
   public String getName() {
      String var1 = this.raw.getName();
      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      return var1;
   }

   @Override
   public NumericOid getOid() {
      return new NumericOid(this.raw.getOid());
   }

   @Override
   public ObjectClassType getType() {
      try {
         return ObjectClassType.valueOf(this.raw.getType());
      } catch (RuntimeException var2) {
         return ObjectClassType.STRUCTURAL;
      }
   }

   @Override
   public boolean isObsolete() {
      return this.raw.isIsObsolete();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof ObjectClassDefinition ? this.getOid().equals(((ObjectClassDefinition)var1).getOid()) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.getOid().hashCode();
   }
}
