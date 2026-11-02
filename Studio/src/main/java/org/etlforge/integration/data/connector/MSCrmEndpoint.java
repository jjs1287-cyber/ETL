package org.etlforge.integration.data.connector;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hasher;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSCrmEndpointId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.util.version.Version;

public final class MSCrmEndpoint extends ConnectorEndpoint {
   @IdProperty
   public static final String MSCRM_ENDPOINT_ID = "mscrmendpoint_id";
   @NameProperty
   public static final String MSCRM_ENDPOINT_NAME = "mscrmendpoint_name";
   public static final String AUTH_TYPE = "authType";
   public static final String ORG_UNIQUE_NAME = "organizationUniqueName";
   public static final String DOMAIN = "domain";

   public MSCrmEndpoint(String var1) {
      this(null, var1);
   }

   public MSCrmEndpoint(MSCrmEndpointId var1, String var2) {
      super(EntityType.MSCrmEndpoint, var1, var2, "MSCRM");
   }

   public MSCrmEndpointId getID() {
      return (MSCrmEndpointId)super.getID();
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{"mscrmendpoint_id", "mscrmendpoint_name", "authType", "organizationUniqueName", "domain", "host", "port", "user", "password"};
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 0, 0, 310);
   }

   @Override
   public EndpointParameters getConnectionParameters() {
      synchronized (this.getDataLock()) {
         return EndpointParameters.readFromKeyValueStore(this.getData(), "authType", "organizationUniqueName", "domain").orNull();
      }
   }

   public MSCrmEndpoint clone() {
      return (MSCrmEndpoint)super.clone();
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

         String var4 = var2.getAdditionalParam("organizationUniqueName");
         if (Strings.isNullOrEmpty(var4)) {
            var1.addMessage("No Organization Unique Name.", ValidationStatus.INVALID);
         }
      }
   }

   public MSCrmEndpoint.ConnectivityType getConnectivityType() {
      String var1;
      synchronized (this.getDataLock()) {
         var1 = this.getProperty("authType");
      }

      return Strings.isNullOrEmpty(var1) ? MSCrmEndpoint.ConnectivityType.defaultType() : MSCrmEndpoint.ConnectivityType.fromApiName(var1);
   }

   public void setConnectivityType(MSCrmEndpoint.ConnectivityType var1) {
      MSCrmEndpoint.ConnectivityType var2;
      synchronized (this.getDataLock()) {
         var2 = this.getConnectivityType();
         this.setProperty("authType", var1.getApiName());
      }

      if (var1 != var2) {
         this.firePropertyChange("authType", var2, var1);
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
      return ImmutableSet.of("host", "user", "organizationUniqueName", "domain");
   }

   public enum ConnectivityType {
      NONE("none", "None"),
      ACTIVE_DIRECTORY("activeDirectory", "Active Directory"),
      FEDERATION("federation", "Federation"),
      LIVE_ID("liveId", "Live ID"),
      ONLINE_FEDERATION("onlineFederation", "Online Federation");

      private final String apiName;
      private final String displayName;

      ConnectivityType(String var3, String var4) {
         this.apiName = var3;
         this.displayName = var4;
      }

      public String getApiName() {
         return this.apiName;
      }

      public static MSCrmEndpoint.ConnectivityType fromApiName(String var0) {
         for (MSCrmEndpoint.ConnectivityType var4 : values()) {
            if (var4.apiName.equals(var0)) {
               return var4;
            }
         }

         throw new IllegalArgumentException("Unknown API name: " + var0);
      }

      @Override
      public String toString() {
         return this.displayName;
      }

      public static MSCrmEndpoint.ConnectivityType defaultType() {
         return ONLINE_FEDERATION;
      }
   }
}
