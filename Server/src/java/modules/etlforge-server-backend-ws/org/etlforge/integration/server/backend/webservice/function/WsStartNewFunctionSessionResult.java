package org.etlforge.integration.server.backend.webservice.function;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsStartNewFunctionSessionResult implements Serializable {
   private String sessionGuid;
   private WsFunctionError error;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsStartNewFunctionSessionResult.class, true);

   public WsStartNewFunctionSessionResult() {
   }

   public WsStartNewFunctionSessionResult(String var1, WsFunctionError var2) {
      this.sessionGuid = var1;
      this.error = var2;
   }

   public String getSessionGuid() {
      return this.sessionGuid;
   }

   public void setSessionGuid(String var1) {
      this.sessionGuid = var1;
   }

   public WsFunctionError getError() {
      return this.error;
   }

   public void setError(WsFunctionError var1) {
      this.error = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsStartNewFunctionSessionResult)) {
         return false;
      }

      WsStartNewFunctionSessionResult var2 = (WsStartNewFunctionSessionResult)var1;
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
      boolean var3 = (this.sessionGuid == null && var2.getSessionGuid() == null || this.sessionGuid != null && this.sessionGuid.equals(var2.getSessionGuid()))
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
      if (this.getSessionGuid() != null) {
         var1 += this.getSessionGuid().hashCode();
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
      typeDesc.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsStartNewFunctionSessionResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("sessionGuid");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "SessionGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("error");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Error"));
      var0.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionError"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
