package org.etlforge.integration.data.connector;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hasher;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSAXEndpointId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.util.version.Version;

public final class MSAXEndpoint extends ConnectorEndpoint {
   @IdProperty
   public static final String MSAX_ENDPOINT_ID = "msaxendpoint_id";
   @NameProperty
   public static final String MSAX_ENDPOINT_NAME = "msaxendpoint_name";
   public static final String DOMAIN = "domain";
   public static final String WSDL_URLS = "wsdl_urls";
   public static final String SERVICE_URL = "service_url";
   private static final boolean isHostRequired = false;

   public MSAXEndpoint(String var1) {
      this(null, var1);
   }

   public MSAXEndpoint(MSAXEndpointId var1, String var2) {
      super(EntityType.MSAXEndpoint, var1, var2, "MSAX");
   }

   public MSAXEndpointId getID() {
      return (MSAXEndpointId)super.getID();
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{"msaxendpoint_id", "msaxendpoint_name", "service_url", "domain", "wsdl_urls", "user", "password"};
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 1, 0, 3);
   }

   @Override
   public EndpointParameters getConnectionParameters() {
      synchronized (this.getDataLock()) {
         return EndpointParameters.readFromKeyValueStore(false, this.getData(), "service_url", "domain", "wsdl_urls").orNull();
      }
   }

   public MSAXEndpoint clone() {
      return (MSAXEndpoint)super.clone();
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      EndpointParameters var2 = this.getConnectionParameters();
      if (var2 == null) {
         var1.addMessage("No connection parameters have been defined.", ValidationStatus.INVALID);
      } else {
         String var3 = var2.getUserCredentials().getUsername();
         if (var3.isEmpty()) {
            var1.addMessage("No user name.", ValidationStatus.INVALID);
         }
      }
   }

   @Override
   public void contributeToCachingKey(Hasher var1) {
      synchronized (this.getDataLock()) {
         EndpointParameters var3 = this.getConnectionParameters();
         var3.contributeToCachingKey(var1);
      }
   }

   public static ImmutableSet<String> getNamesOfCachedProperties() {
      return ImmutableSet.of("host", "user", "service_url", "domain", "wsdl_urls");
   }
}
