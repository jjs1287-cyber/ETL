package org.etlforge.integration.filetransfer.client.upload.xsd;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.etlforge.application.appdir.TempCache;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;

public final class XsdCache {
   private static final TempCache<ServerGuid, ServerFileFolder[]> impl = new TempCache<ServerGuid, ServerFileFolder[]>(ServerFileFolder[].class) {
      protected String getFileName(ServerGuid var1) {
         return "xsdcache_" + var1;
      }
   };

   public static void store(ServerGuid var0, Collection<ServerFileFolder> var1) {
      impl.store(var0, var1.toArray(new ServerFileFolder[var1.size()]));
   }

   public static List<ServerFileFolder> restore(ServerGuid var0) {
      ServerFileFolder[] var1 = impl.restore(var0);
      return var1 == null ? Collections.emptyList() : Arrays.asList(var1);
   }

   public static void update(ServerGuid var0, ServerFile[] var1) {
      synchronized (impl) {
         ServerFileFolder[] var3 = impl.restore(var0);
         if (var3 != null && var3.length == 1) {
            ServerFileFolder var4 = var3[0];
            ServerFileFolder var5 = updateFolderWithNewFiles(var4, var1);
            impl.store(var0, new ServerFileFolder[]{var5});
         }
      }
   }

   private static ServerFileFolder updateFolderWithNewFiles(ServerFileFolder var0, ServerFile[] var1) {
      HashMap var2 = Maps.newHashMap();

      for (ServerFile var4 : var0.allFiles()) {
         var2.put(var4.getFilePath(), var4);
      }

      for (ServerFile var6 : var1) {
         var2.put(var6.getFilePath(), var6);
      }

      return new ServerFileFolder(var0.getName(), null, var2.values());
   }
}
