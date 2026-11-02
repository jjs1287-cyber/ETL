package org.etlforge.integration.design.netsuite.adapter;

import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteObject {
   private final String objectElementName;
   private final XsdGlobalElementName objectType;

   public NetSuiteObject(XsdGlobalElementName var1) {
      this(var1.getLocalName(), var1);
   }

   public NetSuiteObject(String var1, XsdGlobalElementName var2) {
      this.objectElementName = var1;
      this.objectType = var2;
   }

   public String getObjectElementName() {
      return this.objectElementName;
   }

   public String getTypeNamespace() {
      return this.objectType.getNamespace();
   }

   public String getTypeLocalName() {
      return this.objectType.getLocalName();
   }

   public XsdGlobalElementName getTypeXsdGlobalElementName() {
      return this.objectType;
   }

   public boolean isType(XsdGlobalElementName var1) {
      return this.objectType.equals(var1);
   }
}
