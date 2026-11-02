package org.etlforge.integration.salesforce.wsdl.partner;

import com.google.common.collect.Sets;
import java.util.Set;

public final class PartnerWsdlInfo {
   public static final String LOCATOR = "etlforge.salesforce.partner.29.wsdl";
   public static final String TARGET_NAMESPACE = "urn:partner.soap.sforce.com";

   public static final Set<String> getOtherNamespaceMappings() {
      return Sets.newHashSet(
         "xmlns=http://schemas.xmlsoap.org/wsdl/",
         "xmlns:fns=urn:fault.partner.soap.sforce.com",
         "xmlns:tns=urn:partner.soap.sforce.com",
         "xmlns:xsd=http://www.w3.org/2001/XMLSchema",
         "xmlns:soap=http://schemas.xmlsoap.org/wsdl/soap/",
         "xmlns:ens=urn:sobject.partner.soap.sforce.com"
      );
   }

   private PartnerWsdlInfo() {
   }
}
