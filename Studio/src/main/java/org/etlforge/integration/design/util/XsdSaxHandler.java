package org.etlforge.integration.design.util;

import com.google.common.base.Strings;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class XsdSaxHandler extends XmlSaxHandler {
   private String targetNamespace = "";
   protected XmlQName handlerXmlQName = null;

   public String getTargetNamespace() {
      return this.targetNamespace;
   }

   @Override
   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.isRoot()) {
         this.targetNamespace = var4.getValue("targetNamespace");
         if (Strings.isNullOrEmpty(this.targetNamespace)) {
            throw new ConnectorNetSuiteDefinitionError("targetNamespace is undefined");
         }
      }

      super.startElement(var1, var2, var3, var4);
   }

   protected XsdGlobalElementName getNetSuiteSchemaGlobalElementName(String var1) {
      return this.getNetSuiteSchemaGlobalElementName(var1, true);
   }

   protected XsdGlobalElementName getNetSuiteSchemaGlobalTypeName(String var1) {
      return this.getNetSuiteSchemaGlobalElementName(var1, false);
   }

   private XsdGlobalElementName getNetSuiteSchemaGlobalElementName(String var1, boolean var2) {
      this.handlerXmlQName = new XmlQName(var1);
      String var3 = this.prefixNamespace.getNamespaceExists(this.handlerXmlQName.getPrefix());
      return XsdGlobalElementName.newInstance(var3, this.handlerXmlQName.getLocalName(), var2);
   }
}
