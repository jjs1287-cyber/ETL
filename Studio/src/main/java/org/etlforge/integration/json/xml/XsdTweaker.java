package org.etlforge.integration.json.xml;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.etlforge.xml.Elements;
import org.etlforge.xml.QualifiedName;
import org.etlforge.xml.SimpleNamespaceContext;
import org.etlforge.xml.XmlFileWriter;
import org.etlforge.xml.XmlNameFactory;
import org.etlforge.xml.dom.DomParser;
import org.etlforge.xml.xsd.XsdUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

final class XsdTweaker {
   private static final String OBJECT_ATTRIBUTE_XPATH = "//xs:attribute[@name='object']";
   private final Map<String, XsdSimpleType> simpleTypeTranslation = Maps.newHashMap();
   private final SimpleNamespaceContext nsCtx = new SimpleNamespaceContext();
   private Document schema;

   public XsdTweaker() {
      this.nsCtx.add("xs", "http://www.w3.org/2001/XMLSchema");
      this.populateSimpleTypeTranslation();
   }

   private void populateSimpleTypeTranslation() {
      this.simpleTypeTranslation.put("boolean", XsdSimpleType.BOOLEAN);
      this.simpleTypeTranslation.put("float", XsdSimpleType.DOUBLE);
      this.simpleTypeTranslation.put("double", XsdSimpleType.DOUBLE);
      this.simpleTypeTranslation.put("decimal", XsdSimpleType.DOUBLE);
      this.simpleTypeTranslation.put("integer", XsdSimpleType.INTEGER);
      this.simpleTypeTranslation.put("nonPositiveInteger", XsdSimpleType.INTEGER);
      this.simpleTypeTranslation.put("nonNegativeInteger", XsdSimpleType.INTEGER);
      this.simpleTypeTranslation.put("negativeInteger", XsdSimpleType.INTEGER);
      this.simpleTypeTranslation.put("positiveInteger", XsdSimpleType.INTEGER);
      this.simpleTypeTranslation.put("short", XsdSimpleType.INTEGER);
      this.simpleTypeTranslation.put("unsignedShort", XsdSimpleType.INTEGER);
      this.simpleTypeTranslation.put("byte", XsdSimpleType.INTEGER);
      this.simpleTypeTranslation.put("unsignedByte", XsdSimpleType.INTEGER);
      this.simpleTypeTranslation.put("int", XsdSimpleType.LONG);
      this.simpleTypeTranslation.put("unsignedInt", XsdSimpleType.LONG);
      this.simpleTypeTranslation.put("long", XsdSimpleType.LONG);
      this.simpleTypeTranslation.put("unsignedLong", XsdSimpleType.LONG);
   }

   public void tweak(File var1, File var2) throws XsdGeneratorException, IOException {
      try {
         this.schema = DomParser.parseFile(var1, true);
         this.fixJsonObjectAttributes();
         this.makeElementsNillableAndOptional();
         this.correctArrays();
         this.correctMixedContentsDeclarations();
         this.reviseSimpleTypes();
         this.writeResult(var2);
      } catch (Exception var4) {
         throw new XsdGeneratorException(var4);
      }
   }

   private void fixJsonObjectAttributes() {
      for (Element var2 : this.findElements("//xs:attribute[@name='object']")) {
         XsdUtils.makeAttributeFixedAndRequired(var2);
      }
   }

   private Elements findElements(String var1) {
      return Elements.matching(var1, this.schema, this.nsCtx);
   }

   private void makeElementsNillableAndOptional() {
      for (Element var2 : this.findElements("/xs:schema/xs:element//xs:element")) {
         var2.setAttribute("nillable", "true");
         var2.setAttribute("minOccurs", "0");
      }
   }

   private void correctArrays() {
      ArrayTweaker var1 = new ArrayTweaker(this.schema, this.nsCtx);
      var1.tweakArrays();
   }

   private void correctMixedContentsDeclarations() {
      for (Element var2 : this.findElements("//xs:complexType[@mixed='true']")) {
         Element var3 = (Element)var2.getParentNode();
         var3.removeAttribute("type");
      }
   }

   private void reviseSimpleTypes() {
      XmlNameFactory var1 = XmlNameFactory.nonConforming();

      for (Element var3 : this.findElements("//xs:element[@type]")) {
         String var4 = var3.getAttribute("type");
         QualifiedName var5 = var1.newQualifiedName(var4);
         if ("http://www.w3.org/2001/XMLSchema".equals(this.nsCtx.getNamespaceURI(var5.getPrefix()))) {
            QualifiedName var6 = this.translateSimpleType(var1, var5);
            var3.setAttribute("type", var6.toString());
         }
      }
   }

   private QualifiedName translateSimpleType(XmlNameFactory var1, QualifiedName var2) {
      XsdSimpleType var3 = this.simpleTypeTranslation.get(var2.getLocalName());
      if (var3 == null) {
         var3 = XsdSimpleType.STRING;
      }

      return var1.newQualifiedName(var2.getPrefix(), var3.getSchemaTypeName());
   }

   private void writeResult(File var1) throws IOException {
      XmlFileWriter var2 = new XmlFileWriter();
      var2.setIndentNumber(2);
      var2.write(this.schema, var1);
   }
}
