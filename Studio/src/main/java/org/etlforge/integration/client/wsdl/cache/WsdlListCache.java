package org.etlforge.integration.client.wsdl.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.etlforge.application.appdir.TempCache;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

public final class WsdlListCache {
   private static final TempCache<ServerGuid, WsdlFile[]> impl = new TempCache<ServerGuid, WsdlFile[]>(WsdlFile[].class) {
      protected String getFileName(ServerGuid var1) {
         return "wsdllist_" + var1;
      }
   };

   public static void store(ServerGuid var0, Collection<WsdlFile> var1) {
      impl.store(var0, var1.toArray(new WsdlFile[var1.size()]));
   }

   public static List<WsdlFile> restore(ServerGuid var0) {
      WsdlFile[] var1 = impl.restore(var0);
      return var1 == null ? Lists.newArrayList() : Lists.newArrayList(var1);
   }

   public static void update(ServerGuid var0, WsdlFile... var1) {
      synchronized (impl) {
         WsdlFile[] var3 = impl.restore(var0);
         if (var3 != null) {
            HashMap var4 = Maps.newHashMap();

            for (WsdlFile var8 : var3) {
               var4.put(var8.getLocator(), var8);
            }

            for (WsdlFile var14 : var1) {
               var4.put(var14.getLocator(), var14);
            }

            store(var0, var4.values());
         }
      }
   }

   private WsdlListCache() {
   }
}
