package org.etlforge.integration.client.ui.interchange.entity.webservice;

import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.WebServiceCall;

final class WebServiceCallSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public WebServiceCallSearchAndReplaceProperties() {
      super(
         create("URL", "webservice_url"),
         new WebServiceCallSearchAndReplaceProperties.WsdlProperty(),
         create("SOAP Action", "soapaction"),
         create("Service", "service"),
         create("Port", "port"),
         create("Binding", "binding"),
         create("Operation", "operation")
      );
   }

   private static SearchableProperty<String> create(String var0, String var1) {
      return new ReplaceableKeyValueStringProperty(var0, var1);
   }

   private static final class WsdlProperty implements ReplaceableProperty<String> {
      private WsdlProperty() {
      }

      public void setValue(IntegrationEntity var1, String var2) {
         WebServiceCall var3 = (WebServiceCall)var1;
         synchronized (var3.getDataLock()) {
            var3.getWsdlFile().setLocator(var2);
         }
      }

      @Override
      public String getName() {
         return "WSDL";
      }

      public String getValue(IntegrationEntity var1) {
         WebServiceCall var2 = (WebServiceCall)var1;
         synchronized (var2.getDataLock()) {
            return var2.getWsdlFile().getLocator();
         }
      }

      @Override
      public Class<String> getValueType() {
         return String.class;
      }
   }
}
