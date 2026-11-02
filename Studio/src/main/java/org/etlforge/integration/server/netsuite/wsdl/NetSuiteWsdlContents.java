package org.etlforge.integration.server.netsuite.wsdl;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;

public class NetSuiteWsdlContents {
   private final String netsuiteWsdlKey;
   private final Map<String, String> wsdlConnents;

   public NetSuiteWsdlContents(String var1) {
      this.netsuiteWsdlKey = var1;
      this.wsdlConnents = Maps.newHashMap();
   }

   public String getNetsuiteWsdlKey() {
      return this.netsuiteWsdlKey;
   }

   public String getNetsuiteWsdl() {
      return this.getXsdSchema(this.netsuiteWsdlKey);
   }

   public Set<String> getXsdSchemaKeys() {
      return this.wsdlConnents.keySet();
   }

   public String getXsdSchema(String var1) {
      return this.wsdlConnents.get(var1);
   }

   public void setXsdSchema(String var1, String var2) {
      this.wsdlConnents.put(var1, var2);
   }

   public Map<String, String> getWsdlContents() {
      return this.wsdlConnents;
   }

   public int size() {
      return this.wsdlConnents.size();
   }
}
