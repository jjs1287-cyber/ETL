package com.etlforge.integration.client.sfdc.data;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.salesforce.soap.DefaultSoapHeaderLookup;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

final class SoapHeaderSetter {
   private final SoapHeaderTranslator translator;
   private final SoapHeader[] customHeaders;

   public SoapHeaderSetter(SoapHeaderTranslator var1, SoapHeader... var2) {
      this.translator = var1;
      this.customHeaders = var2;
   }

   public void applyHeaders(SalesforceWizardEntity var1) {
      List var2 = DefaultSoapHeaderLookup.getDefaultHeadersWithOverrides(var1.getMethod(), null, this.customHeaders);
      HashSet var3 = Sets.newHashSet();

      for (Object __etlItem23 : var2) {
         SoapHeader var5 = (SoapHeader) __etlItem23;
         var3.add(this.translator.toDataStoreRepresentation(var5));
      }

      var1.setSoapHeaders(var3);
   }
}
