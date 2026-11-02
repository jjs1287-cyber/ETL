package org.etlforge.integration.salesforce.wsdl.partner.crom;

import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Xml;
import org.etlforge.integration.structure.crom.CromType;

final class CromHeaderValue {
   private final String name;
   private final String schemaType;
   private final String schemaTypeNamespace;
   private final String etlforgeType;
   private boolean nillable;
   private boolean optional;

   public static CromHeaderValue string(String var0) {
      return new CromHeaderValue(var0, "string", "http://www.w3.org/2001/XMLSchema", "String");
   }

   public static CromHeaderValue bool(String var0) {
      return new CromHeaderValue(var0, "boolean", "http://www.w3.org/2001/XMLSchema", "Boolean");
   }

   public static CromHeaderValue integer(String var0) {
      return new CromHeaderValue(var0, "integer", "http://www.w3.org/2001/XMLSchema", "Integer");
   }

   public static CromHeaderValue salesforceId(String var0) {
      return new CromHeaderValue(var0, "ID", "", "String");
   }

   private CromHeaderValue(String var1, String var2, String var3, String var4) {
      this.name = var1;
      this.schemaType = var2;
      this.schemaTypeNamespace = var3;
      this.etlforgeType = var4;
   }

   public CromHeaderValue nillable() {
      this.nillable = true;
      return this;
   }

   public CromHeaderValue optional() {
      this.optional = true;
      return this;
   }

   public CROM createCrom(CROM var1) {
      CROM var2 = new CROM(CromType.CROM_t_Xml, var1);
      var2.setName(this.name);
      var2.setType(1L);
      CROM_Xml var3 = CromUtils.insertXmlExtensionForHeader(var2);
      var3.setSchemaType(this.schemaType);
      var3.setSchemaTypeNamespace(this.schemaTypeNamespace);
      var2.createValueCrom(this.etlforgeType);
      if (this.optional) {
         var2.addType(32L);
         var3.setMinUse(0);
      }

      if (this.nillable) {
         CromUtils.makeNillable(var2);
      }

      return var2;
   }
}
