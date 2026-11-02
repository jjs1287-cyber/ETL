package org.etlforge.integration.client.wsdl;

enum SoapVersion {
   NONE(0),
   SOAP11(1),
   SOAP12(2);

   private final int intRepresentation;

   SoapVersion(int var3) {
      this.intRepresentation = var3;
   }

   int getVersion() {
      return this.intRepresentation;
   }

   String getVersionAsString() {
      return Integer.toString(this.getVersion());
   }

   static SoapVersion fromUri(String var0) {
      if ("http://schemas.xmlsoap.org/wsdl/soap/".equals(var0)) {
         return SOAP11;
      } else {
         return "http://schemas.xmlsoap.org/wsdl/soap12/".equals(var0) ? SOAP12 : NONE;
      }
   }
}
