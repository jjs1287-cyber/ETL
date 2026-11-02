package org.etlforge.integration.salesforce.wsdl.partner.crom;

import java.util.Iterator;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectExternalIdFieldName;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Xml;

final class RelatedObjectCromBuilder {
   public static CROM build(RelatedObject var0) {
      String var1 = var0.getRelationshipName();
      CROM var2 = CromUtils.createElementInNamespace(null, var1, "urn:sobject.partner.soap.sforce.com");
      CROM_Xml var3 = (CROM_Xml)var2.getExtension();
      var3.setSchemaType(var0.getObjectName());
      var3.setSchemaTypeNamespace("urn:sobject.partner.soap.sforce.com");
      var2.addType(524288L);
      addExternalIdFields(var0, var2);
      return var2;
   }

   private static void addExternalIdFields(RelatedObject var0, CROM var1) {
      for (SObjectSchema.Field var3 : var0.getExternalIds()) {
         CROM var4 = createNodeForExternalId(var1, var3);
         encodeFieldname(var0, var4);
      }
   }

   private static CROM createNodeForExternalId(CROM var0, SObjectSchema.Field var1) {
      CROM var2 = CromUtils.addField(var1, var0);
      String var3 = RelatedObjectExternalIdFieldName.displayName(var1);
      var2.setName(var3);
      var2.setDisplayName(var3);
      return var2;
   }

   private static void encodeFieldname(RelatedObject var0, CROM var1) {
      Iterator var2 = var1.getChildren().iterator();
      CROM var3 = CROM.getNextValueAttribute(var2);
      var3.getExtension().setLinkToPrimaryKey(var0.getFieldName());
   }

   private RelatedObjectCromBuilder() {
   }
}
