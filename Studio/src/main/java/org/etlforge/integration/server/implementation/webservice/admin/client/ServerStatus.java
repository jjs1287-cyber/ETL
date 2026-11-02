package org.etlforge.integration.server.implementation.webservice.admin.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class ServerStatus implements Serializable {
   private int statusBits;
   private String description;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(ServerStatus.class, true);

   public ServerStatus() {
   }

   public ServerStatus(int var1, String var2) {
      this.statusBits = var1;
      this.description = var2;
   }

   public int getStatusBits() {
      return this.statusBits;
   }

   public void setStatusBits(int var1) {
      this.statusBits = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof ServerStatus)) {
         return false;
      }

      ServerStatus var2 = (ServerStatus)var1;
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
      boolean var3 = this.statusBits == var2.getStatusBits()
         && (this.description == null && var2.getDescription() == null || this.description != null && this.description.equals(var2.getDescription()));
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
      var1 += this.getStatusBits();
      if (this.getDescription() != null) {
         var1 += this.getDescription().hashCode();
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "ServerStatus"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("statusBits");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "StatusBits"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("description");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "Description"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
