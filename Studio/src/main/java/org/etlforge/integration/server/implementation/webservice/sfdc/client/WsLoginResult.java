package org.etlforge.integration.server.implementation.webservice.sfdc.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsLoginResult implements Serializable {
   private WsSessionData sessionData;
   private WsSalesforceError error;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsLoginResult.class, true);

   public WsLoginResult() {
   }

   public WsLoginResult(WsSessionData var1, WsSalesforceError var2) {
      this.sessionData = var1;
      this.error = var2;
   }

   public WsSessionData getSessionData() {
      return this.sessionData;
   }

   public void setSessionData(WsSessionData var1) {
      this.sessionData = var1;
   }

   public WsSalesforceError getError() {
      return this.error;
   }

   public void setError(WsSalesforceError var1) {
      this.error = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsLoginResult)) {
         return false;
      }

      WsLoginResult var2 = (WsLoginResult)var1;
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
      boolean var3 = (this.sessionData == null && var2.getSessionData() == null || this.sessionData != null && this.sessionData.equals(var2.getSessionData()))
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
      if (this.getSessionData() != null) {
         var1 += this.getSessionData().hashCode();
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
      typeDesc.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsLoginResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("sessionData");
      var0.setXmlName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "sessionData"));
      var0.setXmlType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData"));
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
