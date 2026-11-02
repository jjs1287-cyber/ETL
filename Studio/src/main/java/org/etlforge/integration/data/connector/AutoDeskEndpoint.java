package org.etlforge.integration.data.connector;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hasher;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.AutoDeskEndpointId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.util.version.Version;

public final class AutoDeskEndpoint extends ConnectorEndpoint {
   @IdProperty
   public static final String AUTODESK_ENDPOINT_ID = "autodeskendpoint_id";
   @NameProperty
   public static final String AUTODESK_ENDPOINT_NAME = "autodeskendpoint_name";

   public AutoDeskEndpoint(String var1) {
      this(null, var1);
   }

   public AutoDeskEndpoint(AutoDeskEndpointId var1, String var2) {
      super(EntityType.AutoDeskEndpoint, var1, var2, "AUTODESK");
   }

   public AutoDeskEndpointId getID() {
      return (AutoDeskEndpointId)super.getID();
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{"autodeskendpoint_id", "autodeskendpoint_name", "host", "user", "password"};
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 1, 0, 1);
   }

   @Override
   public EndpointParameters getConnectionParameters() {
      synchronized (this.getDataLock()) {
         return EndpointParameters.readFromKeyValueStore(this.getData()).orNull();
      }
   }

   public AutoDeskEndpoint clone() {
      return (AutoDeskEndpoint)super.clone();
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
      return ImmutableSet.of("host", "user");
   }
}
