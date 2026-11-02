package org.etlforge.integration.server.implementation.webservice.interchange.db.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsOdbcDriverDescriptor implements Serializable {
   private String name;
   private String quoteBegin;
   private String quoteEnd;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsOdbcDriverDescriptor.class, true);

   public WsOdbcDriverDescriptor() {
   }

   public WsOdbcDriverDescriptor(String var1, String var2, String var3) {
      this.name = var1;
      this.quoteBegin = var2;
      this.quoteEnd = var3;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getQuoteBegin() {
      return this.quoteBegin;
   }

   public void setQuoteBegin(String var1) {
      this.quoteBegin = var1;
   }

   public String getQuoteEnd() {
      return this.quoteEnd;
   }

   public void setQuoteEnd(String var1) {
      this.quoteEnd = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsOdbcDriverDescriptor)) {
         return false;
      }

      WsOdbcDriverDescriptor var2 = (WsOdbcDriverDescriptor)var1;
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
      boolean var3 = (this.name == null && var2.getName() == null || this.name != null && this.name.equals(var2.getName()))
         && (this.quoteBegin == null && var2.getQuoteBegin() == null || this.quoteBegin != null && this.quoteBegin.equals(var2.getQuoteBegin()))
         && (this.quoteEnd == null && var2.getQuoteEnd() == null || this.quoteEnd != null && this.quoteEnd.equals(var2.getQuoteEnd()));
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
      if (this.getName() != null) {
         var1 += this.getName().hashCode();
      }

      if (this.getQuoteBegin() != null) {
         var1 += this.getQuoteBegin().hashCode();
      }

      if (this.getQuoteEnd() != null) {
         var1 += this.getQuoteEnd().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsOdbcDriverDescriptor"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("name");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "Name"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("quoteBegin");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "QuoteBegin"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("quoteEnd");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "QuoteEnd"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
