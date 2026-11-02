package org.etlforge.integration.salesforce.wsdl.partner.crom;

import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_ext;

class FlatObjectCromBuilder {
   private final SObjectSchema so;
   private boolean optionalLoop;
   private boolean allGroup;

   public FlatObjectCromBuilder(SObjectSchema var1) {
      this.so = var1;
   }

   public final void setOptionalLoop(boolean var1) {
      this.optionalLoop = var1;
   }

   public void setAllGroup(boolean var1) {
      this.allGroup = var1;
   }

   public final CROM build(CROM var1) {
      CROM var2 = AbstractCallStructureBuilder.createElementInSObjectNamespace(var1, this.so.getName());
      CROM_ext var3 = var2.getExtension();
      var3.setSchemaType(this.so.getName());
      var3.setSchemaTypeNamespace("urn:sobject.partner.soap.sforce.com");
      if (this.optionalLoop) {
         CromUtils.makeOptionalLoop(var2);
      }

      if (this.allGroup) {
         var2.addType(2097152L);
      }

      this.addFields(var2);
      return var2;
   }

   private void addFields(CROM var1) {
      for (SObjectSchema.Field var4 : this.so.getFields()) {
         this.addField(var4, var1);
      }
   }

   private void addField(SObjectSchema.Field var1, CROM var2) {
      CROM var3 = CromUtils.addField(var1, var2);
      this.customizeField(var1, var3);
   }

   protected void customizeField(SObjectSchema.Field var1, CROM var2) {
   }
}
