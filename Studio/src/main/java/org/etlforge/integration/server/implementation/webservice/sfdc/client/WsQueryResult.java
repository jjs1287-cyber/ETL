package org.etlforge.integration.server.implementation.webservice.sfdc.client;

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

public class WsQueryResult implements Serializable {
   private boolean done;
   private WsQueryRecord[] records;
   private WsSalesforceError error;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsQueryResult.class, true);

   public WsQueryResult() {
   }

   public WsQueryResult(boolean var1, WsQueryRecord[] var2, WsSalesforceError var3) {
      this.done = var1;
      this.records = var2;
      this.error = var3;
   }

   public boolean isDone() {
      return this.done;
   }

   public void setDone(boolean var1) {
      this.done = var1;
   }

   public WsQueryRecord[] getRecords() {
      return this.records;
   }

   public void setRecords(WsQueryRecord[] var1) {
      this.records = var1;
   }

   public WsSalesforceError getError() {
      return this.error;
   }

   public void setError(WsSalesforceError var1) {
      this.error = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsQueryResult)) {
         return false;
      }

      WsQueryResult var2 = (WsQueryResult)var1;
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
      boolean var3 = this.done == var2.isDone()
         && (this.records == null && var2.getRecords() == null || this.records != null && Arrays.equals(this.records, var2.getRecords()))
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
      var1 += (this.isDone() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      if (this.getRecords() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getRecords()); var2++) {
            Object var3 = Array.get(this.getRecords(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("done");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "done"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("records");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "records"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryRecord"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("error");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "error"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSalesforceError"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
