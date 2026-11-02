package org.etlforge.integration.server.implementation.webservice.interchange.db;

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

public class WsJdbcDriverInfo implements Serializable {
   private WsJdbcDriverDescriptor[] returnedDrivers;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsJdbcDriverInfo.class, true);

   public WsJdbcDriverInfo() {
   }

   public WsJdbcDriverInfo(WsJdbcDriverDescriptor[] var1) {
      this.returnedDrivers = var1;
   }

   public WsJdbcDriverDescriptor[] getReturnedDrivers() {
      return this.returnedDrivers;
   }

   public void setReturnedDrivers(WsJdbcDriverDescriptor[] var1) {
      this.returnedDrivers = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsJdbcDriverInfo)) {
         return false;
      }

      WsJdbcDriverInfo var2 = (WsJdbcDriverInfo)var1;
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
      boolean var3 = this.returnedDrivers == null && var2.getReturnedDrivers() == null
         || this.returnedDrivers != null && Arrays.equals(this.returnedDrivers, var2.getReturnedDrivers());
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
      if (this.getReturnedDrivers() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getReturnedDrivers()); var2++) {
            Object var3 = Array.get(this.getReturnedDrivers(), var2);
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
      typeDesc.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDriverInfo"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("returnedDrivers");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "returnedDrivers"));
      var0.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDriverDescriptor"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
