package org.etlforge.integration.design.netsuite.definition;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.xml.sax.Attributes;

public class NetSuiteSchemaXsd implements Serializable {
   private final String schemaLocation;
   private String targetNamespace;
   private String targetNamespacePrefix = null;
   private String xsdNamespacePrefix = null;
   private String schemaUri = null;
   private String schemaLocalName = null;
   private String schemaQName = null;
   private Map<String, String> schemaAttributeMap = null;
   private Map<String, String> prefixNamespace = null;

   public NetSuiteSchemaXsd(String var1) {
      this.schemaLocation = "ns." + var1;
   }

   public void setTargetNamespace(String var1) {
      this.targetNamespace = var1;
   }

   public String getSchemaQName() {
      return this.schemaQName;
   }

   public void setSchemaQName(String var1) {
      this.schemaQName = var1;
   }

   public Map<String, String> getSchemaAttributeMap() {
      return this.schemaAttributeMap;
   }

   public String toAttributesString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("\n");

      for (Entry var3 : this.schemaAttributeMap.entrySet()) {
         var1.append(" name_m=").append((String)var3.getKey());
         var1.append(",value_m=").append((String)var3.getValue());
         var1.append(";");
      }

      String var4 = var1.toString();
      System.out.println();
      System.out.println();
      System.out.println(this.schemaQName + "attr=" + var4);
      return var4;
   }

   public void setSchemaAttributes(Attributes var1) {
      this.schemaAttributeMap = Maps.newHashMap();

      for (int var2 = 0; var2 < var1.getLength(); var2++) {
         this.schemaAttributeMap.put(var1.getQName(var2), var1.getValue(var2));
      }
   }

   public Map<String, String> getPrefixNamespace() {
      return this.prefixNamespace;
   }

   public void setPrefixNamespace(Map<String, String> var1) {
      this.prefixNamespace = var1;
      this.updatePrefixes();
   }

   private void updatePrefixes() {
      for (Entry var2 : this.prefixNamespace.entrySet()) {
         String var3 = (String)var2.getKey();
         if (!var3.isEmpty()) {
            String var4 = (String)var2.getValue();
            if (this.xsdNamespacePrefix == null && NetSuiteConstants.instance.isXsdNamespace(var4)) {
               this.xsdNamespacePrefix = var3;
            }

            if (this.targetNamespacePrefix == null && this.targetNamespace.equals(var4)) {
               this.targetNamespacePrefix = var3;
            }
         }
      }
   }

   public String getSchemaLocation() {
      return this.schemaLocation;
   }

   public String getTargetNamespacePrefix() {
      return this.targetNamespacePrefix;
   }

   public void setPreferedPrefix(String var1) {
      if (Strings.isNullOrEmpty(this.targetNamespacePrefix)) {
         this.targetNamespacePrefix = var1;
      }
   }

   public String getSchemaUri() {
      return this.schemaUri;
   }

   public void setSchemaUri(String var1) {
      this.schemaUri = var1;
   }

   public String getSchemaLocalName() {
      return this.schemaLocalName;
   }

   public void setSchemaLocalName(String var1) {
      this.schemaLocalName = var1;
   }

   public String getXsdNamespacePrefix() {
      return this.xsdNamespacePrefix;
   }

   public void compress(Set<String> var1) {
      this.prefixNamespace = this.getPrefixNamespace(true, var1);
   }

   public Map<String, String> getPrefixNamespace(Set<String> var1) {
      return this.getPrefixNamespace(false, var1);
   }

   private Map<String, String> getPrefixNamespace(boolean var1, Set<String> var2) {
      HashMap var3 = Maps.newHashMap();

      for (Entry var5 : this.prefixNamespace.entrySet()) {
         String var6 = (String)var5.getKey();
         String var7 = (String)var5.getValue();
         boolean var8 = var2.contains(var7) || NetSuiteConstants.instance.isXsdNamespace(var7) || this.targetNamespace.equals(var7);
         if (!var8) {
            var8 = var1 && NetSuiteConstants.instance.isStandardNamespace(var7);
         }

         if (var8) {
            var3.put(var6, var7);
         }
      }

      return var3;
   }
}
