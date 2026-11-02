package org.etlforge.integration.server.implementation.webservice.interchange.db;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsJdbcBeginEndQuote implements Serializable {
   private String beginQuote;
   private String endQuote;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsJdbcBeginEndQuote.class, true);

   public WsJdbcBeginEndQuote() {
   }

   public WsJdbcBeginEndQuote(String var1, String var2) {
      this.beginQuote = var1;
      this.endQuote = var2;
   }

   public String getBeginQuote() {
      return this.beginQuote;
   }

   public void setBeginQuote(String var1) {
      this.beginQuote = var1;
   }

   public String getEndQuote() {
      return this.endQuote;
   }

   public void setEndQuote(String var1) {
      this.endQuote = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsJdbcBeginEndQuote)) {
         return false;
      }

      WsJdbcBeginEndQuote var2 = (WsJdbcBeginEndQuote)var1;
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
      boolean var3 = (this.beginQuote == null && var2.getBeginQuote() == null || this.beginQuote != null && this.beginQuote.equals(var2.getBeginQuote()))
         && (this.endQuote == null && var2.getEndQuote() == null || this.endQuote != null && this.endQuote.equals(var2.getEndQuote()));
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
      if (this.getBeginQuote() != null) {
         var1 += this.getBeginQuote().hashCode();
      }

      if (this.getEndQuote() != null) {
         var1 += this.getEndQuote().hashCode();
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
      typeDesc.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcBeginEndQuote"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("beginQuote");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "beginQuote"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("endQuote");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "endQuote"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
