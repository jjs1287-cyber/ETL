package org.etlforge.integration.etlforgeconnector;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hasher;
import java.util.Properties;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.Password;
import org.etlforge.util.net.Port;
import org.etlforge.util.properties.PropertiesUtils;

public final class EndpointParameters {
   public static final String HOST = "host";
   public static final String PORT = "port";
   public static final String USERNAME = "user";
   public static final String PASSWORD = "password";
   private final ServerParams server;
   private final UserCredentials userCredentials;
   private final Properties additionalParams;

   public EndpointParameters(ServerParams var1, UserCredentials var2) {
      this(var1, var2, null);
   }

   public EndpointParameters(ServerParams var1, UserCredentials var2, Properties var3) {
      this.server = Preconditions.checkNotNull(var1, "server");
      this.userCredentials = Preconditions.checkNotNull(var2, "userCredentials");
      this.additionalParams = var3 == null ? new Properties() : PropertiesUtils.copy(var3);
   }

   public ServerParams getServer() {
      return this.server;
   }

   public UserCredentials getUserCredentials() {
      return this.userCredentials;
   }

   @Nullable
   public String getAdditionalParam(String var1) {
      return this.additionalParams.getProperty(var1);
   }

   public Properties getAdditionalParams() {
      return PropertiesUtils.copy(this.additionalParams);
   }

   public void writeToKeyValueStore(StringKeyValuePairs var1) {
      var1.putString("host", this.server.getHost());
      var1.putInt("port", this.server.getPort().getPort());
      var1.putString("user", this.userCredentials.getUsername());
      var1.putString("password", this.userCredentials.encryptedPassword());
      var1.putAll(this.additionalParams);
   }

   public static Optional<EndpointParameters> readFromKeyValueStore(StringKeyValuePairs var0, String... var1) {
      return readFromKeyValueStore(true, var0, var1);
   }

   public static Optional<EndpointParameters> readFromKeyValueStore(boolean var0, StringKeyValuePairs var1, String... var2) {
      String var3 = var1.get("host");
      ServerParams var4 = null;
      if (StringUtils.isEmpty(var3)) {
         if (var0) {
            return Optional.absent();
         }

         var4 = ServerParams.dummyServer;
      } else {
         int var5 = var1.getInt("port", Port.DEFAULT.getPort());
         var4 = new ServerParams(var3, var5);
      }

      String var11 = var1.get("user");
      if (var11 == null) {
         return Optional.absent();
      }

      String var6 = var1.get("password");
      Password var7 = var6 == null ? null : Password.decrypt(var6);
      UserCredentials var8 = new UserCredentials(var11, var7);
      Properties var9 = readAdditionalParameters(var1, var2);
      return Optional.of(new EndpointParameters(var4, var8, var9));
   }

   public static Properties readAdditionalParameters(StringKeyValuePairs var0, String... var1) {
      Properties var2 = new Properties();

      for (String var6 : var1) {
         String var7 = var0.get(var6);
         if (var7 != null) {
            var2.setProperty(var6, var7);
         }
      }

      return var2;
   }

   public void contributeToCachingKey(Hasher var1, String... var2) {
      var1.putString(this.server.getHost());
      var1.putString(this.userCredentials.getUsername());

      for (String var6 : var2) {
         String var7 = this.additionalParams.getProperty(var6, "");
         var1.putString(var7);
      }
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.server, this.userCredentials, this.additionalParams);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof EndpointParameters)) {
         return false;
      }

      EndpointParameters var2 = (EndpointParameters)var1;
      return this.server.equals(var2.server) && this.userCredentials.equals(var2.userCredentials) && this.additionalParams.equals(var2.additionalParams);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("server", this.server)
         .add("credentials", this.userCredentials)
         .add("additional-params", this.additionalParams)
         .toString();
   }
}
