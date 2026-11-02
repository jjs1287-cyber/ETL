package org.etlforge.integration.data.structure;

import java.io.Serializable;
import java.util.Comparator;
import org.etlforge.collections.StringKeyValuePairs;

public final class WebServiceOperationInfo implements Cloneable {
   public static final String WSDL_LOCATOR = "Identifier";
   public static final String WEB_SERVICE_URL = "WebService_Url";
   public static final String OPERATION_NAME = "Operation";
   public static final String SERVICE_NAME = "Service";
   public static final String PORT_NAME = "Port";
   public static final String BINDING_NAME = "Binding";
   public static final String TRANSPORT_TYPE = "Transport";
   public static final String SOAP_TRANSPORT = "Soap_Transport";
   public static final String HTTP_VERB = "http_verb";
   public static final String SOAP_VERSION = "SoapVersion";
   public static final String SOAP_ACTION = "SoapAction";
   public static final String SOAP_STYLE = "SoapStyle";
   public static final String SOAP_BODY_USE = "SoapBodyUse";
   public static final String DOCUMENTATION = "Documentation";
   public static final String INPUT_NAME = "InputName";
   public static final String OUTPUT_NAME = "OutputName";
   public static final String[] DATABASE_KEYS = new String[]{
      "Operation", "Service", "Port", "Binding", "SoapVersion", "SoapAction", "SoapStyle", "SoapBodyUse"
   };
   private final StringKeyValuePairs data;
   private static final String[] ALL_KEYS = new String[]{
      "Identifier",
      "WebService_Url",
      "Operation",
      "Service",
      "Port",
      "Binding",
      "Transport",
      "Soap_Transport",
      "http_verb",
      "SoapVersion",
      "SoapAction",
      "SoapStyle",
      "SoapBodyUse",
      "Documentation",
      "InputName",
      "OutputName"
   };

   public WebServiceOperationInfo() {
      this.data = new StringKeyValuePairs();
   }

   public WebServiceOperationInfo(WebServiceOperationInfo var1) {
      this.data = new StringKeyValuePairs(var1.data);
   }

   public WebServiceOperationInfo(StringKeyValuePairs var1) {
      this.data = new StringKeyValuePairs();

      for (String var3 : var1.getKeys()) {
         this.data.putString(var3, var1.get(var3));
      }
   }

   public String getProperty(String var1) {
      return this.data.get(var1);
   }

   public String setProperty(String var1, String var2) {
      this.checkKey(var1);
      String var3 = this.data.get(var1);
      this.data.put(var1, var2);
      return var3;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof WebServiceOperationInfo ? this.data.equals(((WebServiceOperationInfo)var1).data) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.data.hashCode();
   }

   @Override
   public String toString() {
      return this.data.get("Operation");
   }

   private void checkKey(String var1) {
      for (String var5 : ALL_KEYS) {
         if (var1.equals(var5)) {
            return;
         }
      }

      throw new IllegalArgumentException("invalid key: " + var1);
   }

   public static Comparator<WebServiceOperationInfo> getComparator(String var0, boolean var1) {
      return new WebServiceOperationInfo.ComparatorImpl(var0, var1);
   }

   public WebServiceOperationInfo clone() {
      return new WebServiceOperationInfo(this.data);
   }

   public String getOperationName() {
      return this.getProperty("Operation");
   }

   private static class ComparatorImpl implements Comparator<WebServiceOperationInfo>, Serializable {
      private final String property;
      private final boolean caseSensitive;

      public ComparatorImpl(String var1, boolean var2) {
         this.property = var1;
         this.caseSensitive = var2;
      }

      public int compare(WebServiceOperationInfo var1, WebServiceOperationInfo var2) {
         String var3 = var1.getProperty(this.property);
         String var4 = var2.getProperty(this.property);
         if (var3 == null) {
            var3 = "";
         }

         if (var4 == null) {
            var4 = "";
         }

         return this.caseSensitive ? var3.compareTo(var4) : var3.compareToIgnoreCase(var4);
      }
   }
}
