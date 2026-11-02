package org.etlforge.integration.salesforce.wsdl.partner.crom;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Attr;
import org.etlforge.integration.structure.crom.CROM_Xml;
import org.etlforge.integration.structure.crom.CROM_ext;
import org.etlforge.integration.structure.crom.CromType;

final class CromUtils {
   public static final String ETLFORGE_NAMESPACE = "http://www.etlforge.invalid/XsdFromWsdl";

   public static void makeNillable(CROM var0) {
      CROM var1 = new CROM(CromType.CROM_t_Xml, var0);
      var1.setExtension(new CROM_Attr());
      var1.setName("xsi:nil");
      var1.setType(16777252L);
      var1.setValueType("Boolean");
      var1.setDefaultValue("false");
   }

   public static void makeOptional(CROM var0) {
      var0.addType(32L);
      CROM_ext var1 = var0.getExtension();
      if (var1 instanceof CROM_Xml) {
         CROM_Xml var2 = (CROM_Xml)var1;
         var2.setMinUse(0);
      }
   }

   public static void makeOptionalLoop(CROM var0) {
      boolean var1 = true;
      makeLoop(var0, var1);
   }

   public static void makeRequiredLoop(CROM var0) {
      boolean var1 = false;
      makeLoop(var0, var1);
   }

   private static void makeLoop(CROM var0, boolean var1) {
      var0.addType(8L);
      CROM_ext var2 = var0.getExtension();
      if (var2 instanceof CROM_Xml) {
         CROM_Xml var3 = (CROM_Xml)var2;
         int var4 = var1 ? 0 : 1;
         var3.setMinUse(var4);
         var3.setMaxUse(-1);
      }
   }

   public static CROM createSoapHeaderRoot(CROM var0) {
      CROM var1 = new CROM(CromType.CROM_t_Xml, var0);
      var1.setName("header");
      var1.setType(33L);
      CROM_Xml var2 = new CROM_Xml();
      var2.setNamespace("http://www.etlforge.invalid/XsdFromWsdl");
      var2.setNSQualify(true);
      var2.setMinUse(0);
      var1.setExtension(var2);
      return var1;
   }

   public static CROM_Xml insertXmlExtensionForHeader(CROM var0) {
      CROM_Xml var1 = new CROM_Xml();
      var1.setNamespace("urn:partner.soap.sforce.com");
      var1.setNSQualify(true);
      var0.setExtension(var1);
      return var1;
   }

   public static CROM addField(SObjectSchema.Field var0, CROM var1) {
      CROM var2 = new CROM(CromType.CROM_t_Xml, var1);
      var2.setName(var0.getName());
      long var3 = 33L;
      var2.setType(var3);
      CROM_Xml var5 = setXmlExtensionForField(var0, var2);
      var5.setMinUse(0);
      SObjectSchema.Field.SoapType var6 = var0.getSoapType();
      CROM var7 = var2.createValueCrom(var6.toETLForgeTypeName());
      String var8 = var6.toETLForgeTypeFormat();
      if (StringUtils.isNotEmpty(var8)) {
         var7.getExtension().setFormat(var8);
      }

      return var2;
   }

   private static CROM_Xml setXmlExtensionForField(SObjectSchema.Field var0, CROM var1) {
      CROM_Xml var2 = new CROM_Xml();
      var2.setNamespace("urn:sobject.partner.soap.sforce.com");
      var2.setNSQualify(true);
      var2.setSchemaType(var0.getSoapType().toXsdLocalTypeName());
      var2.setSchemaTypeNamespace("http://www.w3.org/2001/XMLSchema");
      var1.setExtension(var2);
      return var2;
   }

   public static CROM createElementInNamespace(CROM var0, String var1, String var2) {
      CROM var3 = new CROM(CromType.CROM_t_Xml, var0);
      var3.setName(var1);
      var3.setType(1L);
      CROM_Xml var4 = new CROM_Xml();
      var4.setNamespace(var2);
      var4.setNSQualify(true);
      var3.setExtension(var4);
      return var3;
   }
}
