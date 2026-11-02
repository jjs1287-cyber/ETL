package org.etlforge.util.net;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang.SystemUtils;

@ParametersAreNonnullByDefault
public final class NetworkUtils {
   public static Optional<String> getComputerName() {
      String var0 = null;

      try {
         var0 = InetAddress.getLocalHost().getHostName();
      } catch (UnknownHostException var2) {
         var2.printStackTrace();
      }

      if (Strings.isNullOrEmpty(var0) && SystemUtils.IS_OS_WINDOWS) {
         var0 = System.getenv("COMPUTERNAME");
      }

      return Optional.fromNullable(Strings.emptyToNull(var0));
   }

   private NetworkUtils() {
   }
}
