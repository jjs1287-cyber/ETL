package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.etlforge.integration.client.sfdc.data.SoapHeaderMappingsReplacer;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

final class SoapHeaderChange implements RequestMappingsModifier {
   private final ImmutableSet<SoapHeader> soapHeaders;
   private Logger logger = Logger.getAnonymousLogger();

   public SoapHeaderChange(Set<SoapHeader> var1) {
      this.soapHeaders = ImmutableSet.copyOf(var1);
   }

   @Override
   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   @Override
   public void modifyMapList(List<String> var1) {
      SoapHeaderMappingsReplacer var2 = new SoapHeaderMappingsReplacer(this.soapHeaders);
      var2.setLogger(this.logger);
      var2.replaceSoapHeaders(var1);
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName();
   }
}
