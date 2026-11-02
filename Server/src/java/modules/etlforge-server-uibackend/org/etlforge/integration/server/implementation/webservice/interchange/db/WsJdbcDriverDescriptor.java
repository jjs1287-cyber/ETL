package org.etlforge.integration.server.implementation.webservice.interchange.db;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsJdbcDriverDescriptor implements Serializable {
   private String name;
   private String driverClass;
   private String subprotocol;
   private WsJdbcBeginEndQuote beginEndQuote;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsJdbcDriverDescriptor.class, true);

   public WsJdbcDriverDescriptor() {
   }

   public WsJdbcDriverDescriptor(String var1, String var2, String var3, WsJdbcBeginEndQuote var4) {
      this.name = var1;
      this.driverClass = var2;
      this.subprotocol = var3;
      this.beginEndQuote = var4;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getDriverClass() {
      return this.driverClass;
   }

   public void setDriverClass(String var1) {
      this.driverClass = var1;
   }

   public String getSubprotocol() {
      return this.subprotocol;
   }

   public void setSubprotocol(String var1) {
      this.subprotocol = var1;
   }

   public WsJdbcBeginEndQuote getBeginEndQuote() {
      return this.beginEndQuote;
   }

   public void setBeginEndQuote(WsJdbcBeginEndQuote var1) {
      this.beginEndQuote = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsJdbcDriverDescriptor)) {
         return false;
      }

      WsJdbcDriverDescriptor var2 = (WsJdbcDriverDescriptor)var1;
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
         && (this.driverClass == null && var2.getDriverClass() == null || this.driverClass != null && this.driverClass.equals(var2.getDriverClass()))
         && (this.subprotocol == null && var2.getSubprotocol() == null || this.subprotocol != null && this.subprotocol.equals(var2.getSubprotocol()))
         && (this.beginEndQuote == null && var2.getBeginEndQuote() == null || this.beginEndQuote != null && this.beginEndQuote.equals(var2.getBeginEndQuote()));
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

      if (this.getDriverClass() != null) {
         var1 += this.getDriverClass().hashCode();
      }

      if (this.getSubprotocol() != null) {
         var1 += this.getSubprotocol().hashCode();
      }

      if (this.getBeginEndQuote() != null) {
         var1 += this.getBeginEndQuote().hashCode();
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
      typeDesc.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDriverDescriptor"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("name");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "name"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("driverClass");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "driverClass"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("subprotocol");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "subprotocol"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("beginEndQuote");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "beginEndQuote"));
      var0.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcBeginEndQuote"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
