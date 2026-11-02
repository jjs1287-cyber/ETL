package org.etlforge.integration.data.connector;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hasher;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.SapEndpointId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.util.version.Version;

public final class SapEndpoint extends ConnectorEndpoint {
   @IdProperty
   public static final String SAP_ENDPOINT_ID = "sapendpoint_id";
   @NameProperty
   public static final String SAP_ENDPOINT_NAME = "sapendpoint_name";
   public static final String SYSTEM_NUMBER = "systemnumber";
   public static final String CLIENT = "client";
   public static final String LANG = "lang";

   public SapEndpoint(String var1) {
      this(null, var1);
   }

   public SapEndpoint(SapEndpointId var1, String var2) {
      super(EntityType.SapEndpoint, var1, var2, "SAP");
   }

   public SapEndpointId getID() {
      return (SapEndpointId)super.getID();
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{"sapendpoint_id", "sapendpoint_name", "host", "port", "user", "password", "systemnumber", "client", "lang"};
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 0, 0, 300);
   }

   @Override
   public EndpointParameters getConnectionParameters() {
      synchronized (this.getDataLock()) {
         return EndpointParameters.readFromKeyValueStore(this.getData(), "systemnumber", "client", "lang").orNull();
      }
   }

   public SapEndpoint clone() {
      return (SapEndpoint)super.clone();
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
         var3.contributeToCachingKey(var1, "systemnumber", "client");
      }
   }

   public static ImmutableSet<String> getNamesOfCachedProperties() {
      return ImmutableSet.of("host", "user", "systemnumber", "client", "lang");
   }
}
