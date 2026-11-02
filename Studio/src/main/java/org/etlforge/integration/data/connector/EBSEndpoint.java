package org.etlforge.integration.data.connector;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hasher;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.EBSEndpointId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.util.version.Version;

public final class EBSEndpoint extends ConnectorEndpoint {
   @IdProperty
   public static final String EBS_ENDPOINT_ID = "ebsendpoint_id";
   @NameProperty
   public static final String EBS_ENDPOINT_NAME = "ebsendpoint_name";
   public static final String CONNECT_URL = "connecturl";

   public EBSEndpoint(String var1) {
      this(null, var1);
   }

   public EBSEndpoint(EBSEndpointId var1, String var2) {
      super(EntityType.EBSEndpoint, var1, var2, "EBS");
   }

   public EBSEndpointId getID() {
      return (EBSEndpointId)super.getID();
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{"ebsendpoint_id", "ebsendpoint_name", "user", "password", "connecturl"};
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 2, 0, 0);
   }

   @Override
   public EndpointParameters getConnectionParameters() {
      synchronized (this.getDataLock()) {
         return EndpointParameters.readFromKeyValueStore(false, this.getData(), "connecturl").orNull();
      }
   }

   public EBSEndpoint clone() {
      return (EBSEndpoint)super.clone();
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
      return ImmutableSet.of("host", "connecturl");
   }
}
