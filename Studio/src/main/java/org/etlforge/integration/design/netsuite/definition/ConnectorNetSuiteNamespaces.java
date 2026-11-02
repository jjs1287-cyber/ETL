package org.etlforge.integration.design.netsuite.definition;

import java.io.Serializable;

public class ConnectorNetSuiteNamespaces implements Serializable {
   public static final String XsdNamespace = "http://www.w3.org/2001/XMLSchema";
   private final NetSuiteNamespacePreferedPrefix preferredNamespacePrefix = new NetSuiteNamespacePreferedPrefix();

   void putPreferedPrefix(String var1, String var2) {
      this.preferredNamespacePrefix.putPreferedPrefix(var1, var2);
   }

   public NetSuiteNamespacePreferedPrefix getNamespacePreferedPrefix() {
      return this.preferredNamespacePrefix;
   }

   public String getMessageNamespace() {
      return this.preferredNamespacePrefix.getMessageNamespace();
   }

   public String getCoreNamespace() {
      return this.preferredNamespacePrefix.getCoreNamespace();
   }

   public String getBaseRefElementNamespace() {
      return this.getMessageNamespace();
   }

   public String getRecordElementNamespace() {
      return this.getMessageNamespace();
   }

   public String getRecordResponseElementNamespace() {
      return this.getCoreNamespace();
   }

   public String getMessageNamespacePrefix() {
      return this.preferredNamespacePrefix.getMessageNamespacePrefix();
   }

   public String getCoreNamespacePrefix() {
      return this.preferredNamespacePrefix.getCoreNamespacePrefix();
   }

   public String getBaseRefElementNamespacePrefix() {
      return this.getMessageNamespacePrefix();
   }

   public String getRecordElementNamespacePrefix() {
      return this.getMessageNamespacePrefix();
   }
}
