package org.etlforge.integration.client.ui.interchange.entity.httpendpoint;

import org.etlforge.integration.client.entity.search.AbstractSearchableProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.httpendpoint.ResponseType;

final class HttpEndpointSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public HttpEndpointSearchAndReplaceProperties() {
      super(
         new ReplaceableKeyValueStringProperty("URL", "endpoint_url"),
         new ReplaceableKeyValueStringProperty("Request variable", "request_variable"),
         new HttpEndpointSearchAndReplaceProperties.ResponseTypeProperty(),
         new ReplaceableKeyValueStringProperty("Response variable", "response_variable")
      );
   }

   private static final class ResponseTypeProperty extends AbstractSearchableProperty<ResponseType> {
      public ResponseTypeProperty() {
         super("Response type", ResponseType.class);
      }

      public ResponseType getValue(IntegrationEntity var1) {
         return ((HttpEndpoint)var1).getResponseType();
      }
   }
}
