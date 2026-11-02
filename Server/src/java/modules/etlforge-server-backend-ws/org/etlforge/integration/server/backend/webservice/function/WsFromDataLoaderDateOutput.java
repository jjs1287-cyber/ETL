package org.etlforge.integration.server.backend.webservice.function;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsFromDataLoaderDateOutput implements Serializable {
   private String salesforceDate;
   private String matchingSourceFormat;
   private WsFunctionError error;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsFromDataLoaderDateOutput.class, true);

   public WsFromDataLoaderDateOutput() {
   }

   public WsFromDataLoaderDateOutput(String var1, String var2, WsFunctionError var3) {
      this.salesforceDate = var1;
      this.matchingSourceFormat = var2;
      this.error = var3;
   }

   public String getSalesforceDate() {
      return this.salesforceDate;
   }

   public void setSalesforceDate(String var1) {
      this.salesforceDate = var1;
   }

   public String getMatchingSourceFormat() {
      return this.matchingSourceFormat;
   }

   public void setMatchingSourceFormat(String var1) {
      this.matchingSourceFormat = var1;
   }

   public WsFunctionError getError() {
      return this.error;
   }

   public void setError(WsFunctionError var1) {
      this.error = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsFromDataLoaderDateOutput)) {
         return false;
      }

      WsFromDataLoaderDateOutput var2 = (WsFromDataLoaderDateOutput)var1;
      if (var1 == null) {
         return false;
      }

      if (this == var1) {
         return true;
      }

      if (this.__equalsCalc != null) {
         return this.__equalsCalc == var1;
      }

      this.__equalsCalc = var1;
      boolean var3 = (
            this.salesforceDate == null && var2.getSalesforceDate() == null
               || this.salesforceDate != null && this.salesforceDate.equals(var2.getSalesforceDate())
         )
         && (
            this.matchingSourceFormat == null && var2.getMatchingSourceFormat() == null
               || this.matchingSourceFormat != null && this.matchingSourceFormat.equals(var2.getMatchingSourceFormat())
         )
         && (this.error == null && var2.getError() == null || this.error != null && this.error.equals(var2.getError()));
      this.__equalsCalc = null;
      return var3;
   }

   @Override
   public synchronized int hashCode() {
      if (this.__hashCodeCalc) {
         return 0;
      }

      this.__hashCodeCalc = true;
      int var1 = 1;
      if (this.getSalesforceDate() != null) {
         var1 += this.getSalesforceDate().hashCode();
      }

      if (this.getMatchingSourceFormat() != null) {
         var1 += this.getMatchingSourceFormat().hashCode();
      }

      if (this.getError() != null) {
         var1 += this.getError().hashCode();
      }

      this.__hashCodeCalc = false;
      return var1;
   }

   public static TypeDesc getTypeDesc() {
      return typeDesc;
   }

   public static Serializer getSerializer(String var0, Class var1, QName var2) {
      return new BeanSerializer(var1, var2, typeDesc);
   }

   public static Deserializer getDeserializer(String var0, Class var1, QName var2) {
      return new BeanDeserializer(var1, var2, typeDesc);
   }

   static {
      typeDesc.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFromDataLoaderDateOutput"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("salesforceDate");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "SalesforceDate"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("matchingSourceFormat");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "MatchingSourceFormat"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("error");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Error"));
      var0.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionError"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
