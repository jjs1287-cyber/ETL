package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.concurrent.ConcurrentMap;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdlContents;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteWsdlContentsReader {
   public static NetSuiteWsdlContents fromLocal(String var0, String var1) {
      File var2 = new File(NetSuiteWsdlFile.getNetSuiteWsdlCacheFolder(), var1);
      return fromLocal(var0, var2);
   }

   private static NetSuiteWsdlContents fromLocal(String var0, File var1) {
      NetSuiteWsdlContents var2 = new NetSuiteWsdlContents(var0);
      if (!var1.exists()) {
         return var2;
      }

      for (File var6 : var1.listFiles()) {
         if (var6.isFile()) {
            String var7 = ConnectorExecutionSystem.readFileUTF8(var6);
            var2.setXsdSchema(var6.getName(), var7);
         }
      }

      return var2;
   }

   public static NetSuiteWsdlContents fromOnline(ProxySettings var0, String var1) {
      String var2 = var1;
      NetSuiteWsdlContents var3 = new NetSuiteWsdlContents(var2);
      ConcurrentMap var4 = Maps.newConcurrentMap();
      HashSet var5 = Sets.newHashSet();
      ArrayDeque var6 = Queues.newArrayDeque();
      var6.add(var1);

      while (true) {
         String var7 = (String)var6.poll();
         if (var7 == null) {
            return NetSuiteSchemaRenaming.newName(var4, var3);
         }

         if (!var5.contains(var7)) {
            var5.add(var7);
            NetSuiteDownloadXsdParser var8 = new NetSuiteDownloadXsdParser();
            String var9 = NetsuiteWsdlDownloader.download(var0, var7);
            var3.setXsdSchema(var7, var9);
            var8.parseString(var9);
            var6.addAll(var8.locations.keySet());
            var4.putAll(var8.locations);
         }
      }
   }
}
