package com.etlforge.integration.salesforce.wsdl;

import org.w3c.dom.Node;

final class WsdlUtils {
   public static final String WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";
   public static final String SOAP_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/soap/";
   public static final String LOCATION_ATTRIBUTE = "location";
   public static final String DEFINITIONS = "definitions";

   public static boolean isDefinitionsNode(Node var0) {
      String var1 = var0.getNamespaceURI();
      String var2 = var0.getLocalName();
      return "http://schemas.xmlsoap.org/wsdl/".equals(var1) && "definitions".equals(var2);
   }

   private WsdlUtils() {
   }
}
