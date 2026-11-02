package org.etlforge.integration.server.implementation.webservice.pluginmanagement.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class FilterAsXml implements Serializable {
   private String xmlRepresentation;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(FilterAsXml.class, true);

   public FilterAsXml() {
   }

   public FilterAsXml(String var1) {
      this.xmlRepresentation = var1;
   }

   public String getXmlRepresentation() {
      return this.xmlRepresentation;
   }

   public void setXmlRepresentation(String var1) {
      this.xmlRepresentation = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof FilterAsXml)) {
         return false;
      }

      FilterAsXml var2 = (FilterAsXml)var1;
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
      boolean var3 = this.xmlRepresentation == null && var2.getXmlRepresentation() == null
         || this.xmlRepresentation != null && this.xmlRepresentation.equals(var2.getXmlRepresentation());
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
      if (this.getXmlRepresentation() != null) {
         var1 += this.getXmlRepresentation().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.pluginmanagement.PluginManager", "FilterAsXml"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("xmlRepresentation");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.pluginmanagement.PluginManager", "xmlRepresentation"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
