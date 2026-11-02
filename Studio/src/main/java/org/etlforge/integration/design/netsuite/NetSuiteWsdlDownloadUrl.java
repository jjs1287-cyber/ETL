package org.etlforge.integration.design.netsuite;

import com.google.common.base.Strings;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersion;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersionFactory;

public class NetSuiteWsdlDownloadUrl {
   public static final String defaultUrl = "https://webservices.netsuite.com/wsdl/v2013_1_0/netsuite.wsdl";
   private static final NetSuiteWsdlVersion defaultVersion = new NetSuiteWsdlVersion(2013, 1);
   private final StringBuilder errorMessage = new StringBuilder();
   private final String wsdlDownloadUrl;
   private final String wsdlDownloadUrlKey;
   private final NetSuiteWsdlVersion wsdlVersion;

   public NetSuiteWsdlDownloadUrl(String var1) {
      if (Strings.isNullOrEmpty(var1)) {
         this.wsdlDownloadUrl = "https://webservices.netsuite.com/wsdl/v2013_1_0/netsuite.wsdl";
         this.wsdlVersion = defaultVersion;
      } else {
         this.wsdlDownloadUrl = var1.trim();
         this.wsdlVersion = NetSuiteWsdlVersionFactory.fromVersionedName(this.wsdlDownloadUrl);
         if (this.wsdlVersion == null) {
            this.errorMessage.append("Invalid url: " + var1 + " does not contains netsuite wsdl version");
         }
      }

      this.wsdlDownloadUrlKey = this.extractKeyFromWsdlDownloadUrl();
   }

   private String extractKeyFromWsdlDownloadUrl() {
      String var1 = this.getWsdlDownloadUrl();
      if (this.isValid()) {
         var1 = var1.replace("https://webservices.", "").replace("netsuite.wsdl", "").replace("netsuite.com/wsdl", "").replace("/", "");
      }

      return var1;
   }

   public boolean isValid() {
      return this.errorMessage.length() == 0;
   }

   public String getInvalidMessage() {
      return this.errorMessage.toString();
   }

   public NetSuiteWsdlVersion getNetSuiteWsdlVersion() {
      return this.wsdlVersion;
   }

   public String getKey() {
      return this.wsdlDownloadUrlKey;
   }

   public String getWsdlDownloadUrl() {
      return this.wsdlDownloadUrl;
   }

   @Override
   public int hashCode() {
      return this.wsdlDownloadUrl.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof NetSuiteWsdlDownloadUrl) {
         NetSuiteWsdlDownloadUrl var2 = (NetSuiteWsdlDownloadUrl)var1;
         return this.wsdlDownloadUrl.equals(var2.wsdlDownloadUrl);
      } else {
         return false;
      }
   }

   public boolean isSameVersion(String var1) {
      NetSuiteWsdlDownloadUrl var2 = new NetSuiteWsdlDownloadUrl(var1);
      return var2.isValid() ? this.equals(var2) : true;
   }
}
