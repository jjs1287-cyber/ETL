package org.etlforge.integration.client.connector.serverapi;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public final class SapInfoProviderUtils {
   public static List<ConnectorObject> toPath(SapFunction.FunctionType var0, @Nullable ConnectorObject var1, @Nullable ConnectorObject var2) {
      ArrayList var3 = Lists.newArrayList();
      var3.add(var0);
      if (var0 == SapFunction.FunctionType.IDOC) {
         if (var1 == null) {
            throw new IllegalArgumentException("Must provide a category when asking for IDOC functions.");
         }

         var3.add(var1);
      }

      if (var2 != null) {
         var3.add(var2);
      }

      return var3;
   }

   private SapInfoProviderUtils() {
   }
}
