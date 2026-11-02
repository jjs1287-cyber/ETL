package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import java.io.File;
import org.etlforge.integration.design.netsuite.NetSuiteWsdlDownloadUrl;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersion;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdlContents;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteWsdlContentsFactory {
   final NetSuiteWsdlDownloadUrl netsuiteWsdlDownloadUrl;
   final String netsuiteWsdlCacheFolder;
   final String netsuiteWsdlHasherName;

   public NetSuiteWsdlContentsFactory(String var1) {
      this.netsuiteWsdlDownloadUrl = new NetSuiteWsdlDownloadUrl(var1);
      this.netsuiteWsdlCacheFolder = this.getMD5andVersion();
      this.netsuiteWsdlHasherName = this.netsuiteWsdlCacheFolder + "_md5.xml";
   }

   private String getMD5andVersion() {
      String var1 = this.netsuiteWsdlDownloadUrl.getWsdlDownloadUrl();
      String var2 = NetSuiteWsdlContentsHasher.md5(var1);
      NetSuiteWsdlVersion var3 = this.netsuiteWsdlDownloadUrl.getNetSuiteWsdlVersion();
      return var3 == null ? var2 + "_UNKNOWNVERSION" : var2 + "_" + var3.toWsdlVersion();
   }

   private boolean deleteFilesRecursively(File var1) {
      if (!var1.exists()) {
         return false;
      }

      if (var1.isFile()) {
         return var1.delete();
      }

      for (File var5 : var1.listFiles()) {
         this.deleteFilesRecursively(var5);
      }

      return var1.delete();
   }

   NetSuiteWsdlContents create(ProxySettings var1) {
      NetSuiteWsdlContentsHasherReader var2 = new NetSuiteWsdlContentsHasherReader(this.netsuiteWsdlHasherName);
      NetSuiteWsdlContentsHasher var3 = var2.read();
      if (var3.isvalid()) {
         NetSuiteWsdlContents var4 = NetSuiteWsdlContentsReader.fromLocal(var3.netsuiteWsdlKey, this.netsuiteWsdlCacheFolder);
         if (var3.isvalid(var4)) {
            return var4;
         }

         File var5 = new File(NetSuiteWsdlFile.getNetSuiteWsdlCacheFolder(), this.netsuiteWsdlCacheFolder);
         this.deleteFilesRecursively(var5);
         this.deleteFilesRecursively(var2.getHashFile());
      }

      String var8 = this.netsuiteWsdlDownloadUrl.getWsdlDownloadUrl();
      NetSuiteWsdlContents var9 = NetSuiteWsdlContentsReader.fromOnline(var1, var8);
      var3 = NetSuiteWsdlContentsHasherReader.readFromContents(var9);
      NetSuiteWsdlContentsHasherWriter var6 = new NetSuiteWsdlContentsHasherWriter(this.netsuiteWsdlHasherName, var3);
      var6.write();
      NetSuiteWsdlContentsWriter.write(this.netsuiteWsdlCacheFolder, var9);
      return var9;
   }
}
