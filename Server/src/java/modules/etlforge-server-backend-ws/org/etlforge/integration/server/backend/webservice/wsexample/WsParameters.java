package org.etlforge.integration.server.backend.webservice.wsexample;

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

public class WsParameters implements Serializable {
   private String user;
   private String password;
   private long parameterId;
   private String[] additionalParas;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsParameters.class, true);

   public WsParameters() {
   }

   public WsParameters(String var1, String var2, long var3, String[] var5) {
      this.user = var1;
      this.password = var2;
      this.parameterId = var3;
      this.additionalParas = var5;
   }

   public String getUser() {
      return this.user;
   }

   public void setUser(String var1) {
      this.user = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public long getParameterId() {
      return this.parameterId;
   }

   public void setParameterId(long var1) {
      this.parameterId = var1;
   }

   public String[] getAdditionalParas() {
      return this.additionalParas;
   }

   public void setAdditionalParas(String[] var1) {
      this.additionalParas = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsParameters)) {
         return false;
      }

      WsParameters var2 = (WsParameters)var1;
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
      boolean var3 = (this.user == null && var2.getUser() == null || this.user != null && this.user.equals(var2.getUser()))
         && (this.password == null && var2.getPassword() == null || this.password != null && this.password.equals(var2.getPassword()))
         && this.parameterId == var2.getParameterId()
         && (
            this.additionalParas == null && var2.getAdditionalParas() == null
               || this.additionalParas != null && Arrays.equals(this.additionalParas, var2.getAdditionalParas())
         );
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
      if (this.getUser() != null) {
         var1 += this.getUser().hashCode();
      }

      if (this.getPassword() != null) {
         var1 += this.getPassword().hashCode();
      }

      var1 += new Long(this.getParameterId()).hashCode();
      if (this.getAdditionalParas() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getAdditionalParas()); var2++) {
            Object var3 = Array.get(this.getAdditionalParas(), var2);
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
      typeDesc.setXmlType(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "WsParameters"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("user");
      var0.setXmlName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "User"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("password");
      var0.setXmlName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "Password"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("parameterId");
      var0.setXmlName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "ParameterId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("additionalParas");
      var0.setXmlName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "AdditionalParas"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
