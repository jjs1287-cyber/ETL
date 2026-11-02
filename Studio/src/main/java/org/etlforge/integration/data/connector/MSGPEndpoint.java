package org.etlforge.integration.data.connector;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hasher;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.VersionFactory;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSGPEndpointId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.integration.etlforgeconnector.ServerParams;
import org.etlforge.integration.etlforgeconnector.UserCredentials;
import org.etlforge.util.net.Port;
import org.etlforge.util.version.Version;

public final class MSGPEndpoint extends ConnectorEndpoint {
   @IdProperty
   public static final String MSGP_ENDPOINT_ID = "msgpendpoint_id";
   @NameProperty
   public static final String MSGP_ENDPOINT_NAME = "msgpendpoint_name";
   public static final String DATABASE = "databasename";
   public static final String SERVICE_URL = "service_url";

   public MSGPEndpoint(String var1) {
      this(null, var1);
   }

   public MSGPEndpoint(MSGPEndpointId var1, String var2) {
      super(EntityType.MSGPEndpoint, var1, var2, "MSGP");
   }

   public MSGPEndpointId getID() {
      return (MSGPEndpointId)super.getID();
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{"msgpendpoint_id", "msgpendpoint_name", "databasename", "host", "service_url"};
   }

   @Override
   public Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 0, 0, 500);
   }

   @Override
   public EndpointParameters getConnectionParameters() {
      synchronized (this.getDataLock()) {
         StringKeyValuePairs var2 = this.getData();
         String var3 = var2.get("host");
         if (StringUtils.isEmpty(var3)) {
            return null;
         }

         int var4 = var2.getInt("port", Port.DEFAULT.getPort());
         String var5 = "";
         Object var6 = null;
         UserCredentials var7 = new UserCredentials(var5, (Password)var6);
         ServerParams var8 = new ServerParams(var3, var4);
         Properties var9 = EndpointParameters.readAdditionalParameters(var2, "databasename", "service_url");
         return new EndpointParameters(var8, var7, var9);
      }
   }

   public MSGPEndpoint clone() {
      return (MSGPEndpoint)super.clone();
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      EndpointParameters var2 = this.getConnectionParameters();
      if (var2 == null) {
         var1.addMessage("No connection parameters have been defined.", ValidationStatus.INVALID);
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
      return ImmutableSet.of("host", "databasename", "service_url");
   }
}
