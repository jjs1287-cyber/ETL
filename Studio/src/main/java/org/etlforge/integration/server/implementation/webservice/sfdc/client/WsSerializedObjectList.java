package org.etlforge.integration.server.implementation.webservice.sfdc.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsSerializedObjectList implements Serializable {
   private String compressedAndBase64EncodedBytes;
   private WsSalesforceError error;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSerializedObjectList.class, true);

   public WsSerializedObjectList() {
   }

   public WsSerializedObjectList(String var1, WsSalesforceError var2) {
      this.compressedAndBase64EncodedBytes = var1;
      this.error = var2;
   }

   public String getCompressedAndBase64EncodedBytes() {
      return this.compressedAndBase64EncodedBytes;
   }

   public void setCompressedAndBase64EncodedBytes(String var1) {
      this.compressedAndBase64EncodedBytes = var1;
   }

   public WsSalesforceError getError() {
      return this.error;
   }

   public void setError(WsSalesforceError var1) {
      this.error = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSerializedObjectList)) {
         return false;
      }

      WsSerializedObjectList var2 = (WsSerializedObjectList)var1;
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
            this.compressedAndBase64EncodedBytes == null && var2.getCompressedAndBase64EncodedBytes() == null
               || this.compressedAndBase64EncodedBytes != null && this.compressedAndBase64EncodedBytes.equals(var2.getCompressedAndBase64EncodedBytes())
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
      if (this.getCompressedAndBase64EncodedBytes() != null) {
         var1 += this.getCompressedAndBase64EncodedBytes().hashCode();
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSerializedObjectList"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("compressedAndBase64EncodedBytes");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "compressedAndBase64EncodedBytes"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("error");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "error"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSalesforceError"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
