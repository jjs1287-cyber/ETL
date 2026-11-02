package org.etlforge.integration.design.netsuite.definition;

import java.io.Serializable;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteOperationRootElements implements Serializable {
   private final XsdGlobalElementName requestRoot;
   private final XsdGlobalElementName responseRoot;

   public NetSuiteOperationRootElements(XsdGlobalElementName var1, XsdGlobalElementName var2) {
      this.requestRoot = var1;
      this.responseRoot = var2;
   }

   public XsdGlobalElementName getRequestRoot() {
      return this.requestRoot;
   }

   public XsdGlobalElementName getResponseRoot() {
      return this.responseRoot;
   }
}
