package org.etlforge.integration.data.connector;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hasher;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.NetSuiteEndpointId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.design.netsuite.NetSuiteWsdlDownloadUrl;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.util.version.Version;

public final class NetSuiteEndpoint extends ConnectorEndpoint {
   @IdProperty
   public static final String NETSUITE_ENDPOINT_ID = "netsuiteendpoint_id";
   @NameProperty
   public static final String NETSUITE_ENDPOINT_NAME = "netsuiteendpoint_name";
   public static final String EMAIL = "email";
   public static final String SANDBOX = "sandbox";
   public static final String WSDLDOWNLOADURL = "wsdldownloadurl";

   public NetSuiteEndpoint(String var1) {
      this(null, var1);
   }

   public NetSuiteEndpoint(NetSuiteEndpointId var1, String var2) {
      super(EntityType.NetSuiteEndpoint, var1, var2, "NETSUITE");
   }

   public NetSuiteEndpointId getID() {
      return (NetSuiteEndpointId)super.getID();
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{"netsuiteendpoint_id", "netsuiteendpoint_name", "user", "password", "email", "wsdldownloadurl", "sandbox"};
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 3, 0, 1);
   }

   @Override
   public EndpointParameters getConnectionParameters() {
      synchronized (this.getDataLock()) {
         return EndpointParameters.readFromKeyValueStore(false, this.getData(), "email", "wsdldownloadurl", "sandbox").orNull();
      }
   }

   public NetSuiteWsdlDownloadUrl getNetSuiteWsdlDownloadUrl() {
      EndpointParameters var1 = this.getConnectionParameters();
      String var2 = var1 == null ? null : var1.getAdditionalParam("wsdldownloadurl");
      return new NetSuiteWsdlDownloadUrl(var2);
   }

   public String getAccount() {
      return this.getProperty("user");
   }

   public NetSuiteEndpoint clone() {
      return (NetSuiteEndpoint)super.clone();
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
         var3.contributeToCachingKey(var1, "wsdldownloadurl", "sandbox");
      }
   }

   public static ImmutableSet<String> getNamesOfCachedProperties() {
      return ImmutableSet.of("host", "user", "email", "wsdldownloadurl");
   }
}
