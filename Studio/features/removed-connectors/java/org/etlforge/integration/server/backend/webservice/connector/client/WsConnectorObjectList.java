package org.etlforge.integration.server.backend.webservice.connector.client;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsConnectorObjectList implements Serializable {
   private WsStatus status;
   private WsConnectorObject[] objectList;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConnectorObjectList.class, true);

   public WsConnectorObjectList() {
   }

   public WsConnectorObjectList(WsStatus var1, WsConnectorObject[] var2) {
      this.status = var1;
      this.objectList = var2;
   }

   public WsStatus getStatus() {
      return this.status;
   }

   public void setStatus(WsStatus var1) {
      this.status = var1;
   }

   public WsConnectorObject[] getObjectList() {
      return this.objectList;
   }

   public void setObjectList(WsConnectorObject[] var1) {
      this.objectList = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConnectorObjectList)) {
         return false;
      }

      WsConnectorObjectList var2 = (WsConnectorObjectList)var1;
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
      boolean var3 = (this.status == null && var2.getStatus() == null || this.status != null && this.status.equals(var2.getStatus()))
         && (this.objectList == null && var2.getObjectList() == null || this.objectList != null && Arrays.equals(this.objectList, var2.getObjectList()));
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
      if (this.getStatus() != null) {
         var1 += this.getStatus().hashCode();
      }

      if (this.getObjectList() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getObjectList()); var2++) {
            Object var3 = Array.get(this.getObjectList(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorObjectList"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "Status"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("objectList");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ObjectList"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorObject"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
