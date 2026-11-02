package org.etlforge.integration.design.netsuite.definition;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteDerivedBaseTypeModifier {
   private final NetSuiteDefinition instance;
   private final XsdGlobalElementName baseType;
   private final List<NetSuiteObject> replacedTypes;
   private final String xsdPrefix;
   private final boolean alwaysOneOf;
   StringBuilder sb = null;

   public NetSuiteDerivedBaseTypeModifier(NetSuiteDefinition var1, XsdGlobalElementName var2, String var3, List<NetSuiteObject> var4, boolean var5) {
      this.instance = var1;
      this.baseType = var2;
      this.xsdPrefix = var3;
      this.alwaysOneOf = var5;
      this.replacedTypes = var4;
   }

   public NetSuiteDerivedBaseTypeModifier(NetSuiteDefinition var1, XsdGlobalElementName var2, String var3, NetSuiteObject var4) {
      this.instance = var1;
      this.baseType = var2;
      this.xsdPrefix = var3;
      this.alwaysOneOf = false;
      this.replacedTypes = Lists.newArrayList();
      this.replacedTypes.add(var4);
   }

   private String getSequenceElement() {
      return !this.alwaysOneOf && this.replacedTypes.size() <= 1 ? "sequence" : "choice";
   }

   public String elementXmlString() {
      this.sb = new StringBuilder();
      assert !this.baseType.isElement();
      this.appendOpenTag("complexType");
      this.appendAttribute("name", this.baseType.getLocalName());
      this.sb.append('>');
      this.appendOpenTag(this.getSequenceElement());
      this.sb.append('>');

      for (NetSuiteObject var2 : this.replacedTypes) {
         NetSuiteSchemaXsd var3 = this.instance.getNetSuiteSchemaXsdExists(var2.getTypeNamespace());
         String var4 = getQName(var3.getTargetNamespacePrefix(), var2.getTypeLocalName());
         this.appendOpenTag("element");
         String var5 = "xmlns";
         if (!Strings.isNullOrEmpty(var3.getTargetNamespacePrefix())) {
            var5 = var5 + ':' + var3.getTargetNamespacePrefix();
         }

         this.appendAttribute("name", var2.getObjectElementName());
         this.appendAttribute(var5, var2.getTypeNamespace());
         this.appendAttribute("type", var4);
         this.appendAttribute("minOccurs", "0");
         this.sb.append('>');
         this.appendCloseElementTag("element");
      }

      this.appendCloseElementTag(this.getSequenceElement());
      this.appendCloseElementTag("complexType");
      return this.sb.toString();
   }

   private void appendOpenTag(String var1) {
      String var2 = getQName(this.xsdPrefix, var1);
      this.sb.append('<').append(var2);
   }

   private void appendAttribute(String var1, String var2) {
      if (!Strings.isNullOrEmpty(var2)) {
         this.sb.append(' ');
         this.sb.append(var1);
         this.sb.append("=\"");
         this.sb.append(var2);
         this.sb.append('"');
      }
   }

   private void appendCloseElementTag(String var1) {
      String var2 = getQName(this.xsdPrefix, var1);
      XmlElementUtils.endElementTag(this.sb, var2);
   }

   private static String getQName(String var0, String var1) {
      return Strings.isNullOrEmpty(var0) ? var1 : var0 + ':' + var1;
   }
}
