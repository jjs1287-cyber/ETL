package org.etlforge.integration.server.backend.webservice.salesforce;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsSalesforceEngineLoginResult implements Serializable {
   private WsSalesforceEngineSessionParams session;
   private WsSalesforceEngineError error;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSalesforceEngineLoginResult.class, true);

   public WsSalesforceEngineLoginResult() {
   }

   public WsSalesforceEngineLoginResult(WsSalesforceEngineSessionParams var1, WsSalesforceEngineError var2) {
      this.session = var1;
      this.error = var2;
   }

   public WsSalesforceEngineSessionParams getSession() {
      return this.session;
   }

   public void setSession(WsSalesforceEngineSessionParams var1) {
      this.session = var1;
   }

   public WsSalesforceEngineError getError() {
      return this.error;
   }

   public void setError(WsSalesforceEngineError var1) {
      this.error = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSalesforceEngineLoginResult)) {
         return false;
      }

      WsSalesforceEngineLoginResult var2 = (WsSalesforceEngineLoginResult)var1;
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
      boolean var3 = (this.session == null && var2.getSession() == null || this.session != null && this.session.equals(var2.getSession()))
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
      if (this.getSession() != null) {
         var1 += this.getSession().hashCode();
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
      typeDesc.setXmlType(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "WsSalesforceEngineLoginResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("session");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "Session"));
      var0.setXmlType(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "WsSalesforceEngineSessionParams"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("error");
      var0.setXmlName(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "Error"));
      var0.setXmlType(new QName("http://salesforce.webservice.backend.server.integration.etlforge.invalid", "WsSalesforceEngineError"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
