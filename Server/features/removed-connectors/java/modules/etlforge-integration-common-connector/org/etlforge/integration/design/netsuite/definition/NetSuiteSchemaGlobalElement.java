package org.etlforge.integration.design.netsuite.definition;

import java.io.Serializable;
import java.util.Set;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteSchemaGlobalElement implements Serializable {
   private final Set<XsdGlobalElementName> dependsGlobalElements;
   private final String xmlString;

   public NetSuiteSchemaGlobalElement(Set<XsdGlobalElementName> var1, String var2) {
      this.dependsGlobalElements = var1;
      this.xmlString = var2;
   }

   public String getXmlString() {
      return this.xmlString;
   }

   public Set<XsdGlobalElementName> getDependsGlobalElements() {
      return this.dependsGlobalElements;
   }

   public void removeBaseTypeDepends(XsdGlobalElementName var1) {
      this.dependsGlobalElements.remove(var1);
   }
}
