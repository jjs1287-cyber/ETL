package org.etlforge.integration.data.entity.webservicecall;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.wsdl.Definition;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

public final class WebServiceInfo {
   public static final String OTHER_NAMESPACES_SEPARATOR = "^";
   private String identifier;
   private String webServiceUrl;
   private final String wsdlFile;
   private String targetNamespace;
   private List<String> otherNamespaces;
   private Set<WebServiceOperationInfo> operations;
   private transient Definition wsdlDefinition;

   public WebServiceInfo(String var1, String var2, String var3) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var3), "wsdlFile cannot be null or empty");
      this.identifier = var1;
      this.webServiceUrl = var2;
      this.wsdlFile = var3;
   }

   public void setIdentifier(String var1) {
      this.identifier = var1;
      if (this.operations != null) {
         for (WebServiceOperationInfo var3 : this.operations) {
            var3.setProperty("Identifier", var1);
         }
      }
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public String getWebServiceURL() {
      return this.webServiceUrl;
   }

   public void setWebServiceURL(String var1) {
      this.webServiceUrl = var1;
   }

   public String getWSDLFile() {
      return this.wsdlFile;
   }

   public List<WebServiceOperationInfo> getOperations(boolean var1) {
      List var2 = Lists.newArrayList(this.operations);
      if (var1) {
         var2 = KongaListUtils.filter(var2, new Predicate<WebServiceOperationInfo>() {
            public boolean apply(WebServiceOperationInfo var1) {
               return WebServiceInfo.isSoapType(var1);
            }
         });
      }

      return var2;
   }

   public boolean hasOperations(boolean var1) {
      for (WebServiceOperationInfo var3 : this.operations) {
         if (!var1 || isSoapType(var3)) {
            return true;
         }
      }

      return false;
   }

   private static boolean isSoapType(WebServiceOperationInfo var0) {
      String var1 = var0.getProperty("Transport");
      return var1 != null && var1.toLowerCase().startsWith("soap");
   }

   public void setOperations(WebServiceOperationInfo[] var1) {
      this.operations = Sets.newHashSet(var1);
   }

   public void setTargetNamespace(String var1) {
      this.targetNamespace = var1;
   }

   public String getTargetNamespace() {
      return this.targetNamespace;
   }

   public void setOtherNamespaces(List<String> var1) {
      this.otherNamespaces = var1;
   }

   public List<String> getOtherNamespaces() {
      return Collections.unmodifiableList(this.otherNamespaces);
   }

   public static String getOtherNamespacesString(Iterable<String> var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      for (String var4 : var0) {
         if (!var4.startsWith("name=")) {
            if (var2 > 0) {
               var1.append("^");
            }

            var1.append(var4);
            var2++;
         }
      }

      return var1.toString();
   }

   public String getOtherNamespacesString() {
      return getOtherNamespacesString(this.otherNamespaces);
   }

   public void setWsdlDefinition(Definition var1) {
      this.wsdlDefinition = var1;
   }

   public Definition getWsdlDefinition() {
      return this.wsdlDefinition;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof WebServiceInfo)) {
         return false;
      }

      WebServiceInfo var2 = (WebServiceInfo)var1;
      return Objects.equal(this.identifier, var2.identifier)
         && Objects.equal(this.webServiceUrl, var2.webServiceUrl)
         && Objects.equal(this.wsdlFile, var2.wsdlFile)
         && this.operations.equals(var2.operations);
   }

   @Override
   public int hashCode() {
      return org.etlforge.lang.Objects.hashCode(this.identifier, this.webServiceUrl, this.wsdlFile, this.operations);
   }
}
