package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdlContents;

public class NetSuiteWsdlContentsHasher {
   String netsuiteWsdlKey;
   final Map<String, String> md5 = Maps.newTreeMap();

   void put(String var1, String var2) {
      this.putMd5(var1, md5(var2));
   }

   void putMd5(String var1, String var2) {
      this.md5.put(var1, var2);
   }

   boolean isvalid() {
      return !this.md5.isEmpty() && !Strings.isNullOrEmpty(this.netsuiteWsdlKey);
   }

   boolean isvalid(NetSuiteWsdlContents var1) {
      if (!this.isvalid()) {
         return false;
      }

      if (this.md5.size() != var1.size()) {
         return false;
      }

      if (!this.netsuiteWsdlKey.equals(var1.getNetsuiteWsdlKey())) {
         return false;
      }

      for (Entry var3 : this.md5.entrySet()) {
         String var4 = var1.getXsdSchema((String)var3.getKey());
         if (Strings.isNullOrEmpty(var4)) {
            return false;
         }

         String var5 = md5(var4);
         if (!var5.equals(var3.getValue())) {
            return false;
         }
      }

      return true;
   }

   public static String md5(String var0) {
      HashCode var1 = Hashing.md5().hashString(var0, Charsets.UTF_8);
      return var1.toString();
   }
}
