package org.etlforge.integration.server.backend.webservice.jdbc;

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

public class WsResultSet implements Serializable {
   private WsResultSetRow[] rows;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsResultSet.class, true);

   public WsResultSet() {
   }

   public WsResultSet(WsResultSetRow[] var1) {
      this.rows = var1;
   }

   public WsResultSetRow[] getRows() {
      return this.rows;
   }

   public void setRows(WsResultSetRow[] var1) {
      this.rows = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsResultSet)) {
         return false;
      }

      WsResultSet var2 = (WsResultSet)var1;
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
      boolean var3 = this.rows == null && var2.getRows() == null || this.rows != null && Arrays.equals(this.rows, var2.getRows());
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
      if (this.getRows() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getRows()); var2++) {
            Object var3 = Array.get(this.getRows(), var2);
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
      typeDesc.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsResultSet"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("rows");
      var0.setXmlName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "rows"));
      var0.setXmlType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsResultSetRow"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
