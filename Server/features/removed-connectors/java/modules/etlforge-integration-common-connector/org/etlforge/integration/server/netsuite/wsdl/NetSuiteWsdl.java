package org.etlforge.integration.server.netsuite.wsdl;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteWsdl {
   private NetSuiteDefinition definitions = new NetSuiteDefinition();
   private Map<String, XsdGlobalElementName> headers = Maps.newHashMap();
   private Map<String, XsdGlobalElementName> messages = Maps.newHashMap();
   private String serviceUrl = null;

   public Map<String, XsdGlobalElementName> getMessages() {
      return this.messages;
   }

   public void setMessages(Map<String, XsdGlobalElementName> var1) {
      this.messages = var1;
   }

   public Map<String, XsdGlobalElementName> getHeaders() {
      return this.headers;
   }

   public void setHeaders(Map<String, XsdGlobalElementName> var1) {
      this.headers = var1;
   }

   public String getServiceUrl() {
      return this.serviceUrl;
   }

   public void setServiceUrl(String var1) {
      this.serviceUrl = var1;
   }

   public NetSuiteDefinition getDefinitions() {
      return this.definitions;
   }

   public void setDefinitions(NetSuiteDefinition var1) {
      this.definitions = var1;
   }
}
