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

public class WsJdbcDatabaseInfo implements Serializable {
   private WsJdbcDatabaseObject[] objects;
   private WsJdbcBeginEndQuote quotes;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsJdbcDatabaseInfo.class, true);

   public WsJdbcDatabaseInfo() {
   }

   public WsJdbcDatabaseInfo(WsJdbcDatabaseObject[] var1, WsJdbcBeginEndQuote var2) {
      this.objects = var1;
      this.quotes = var2;
   }

   public WsJdbcDatabaseObject[] getObjects() {
      return this.objects;
   }

   public void setObjects(WsJdbcDatabaseObject[] var1) {
      this.objects = var1;
   }

   public WsJdbcBeginEndQuote getQuotes() {
      return this.quotes;
   }

   public void setQuotes(WsJdbcBeginEndQuote var1) {
      this.quotes = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsJdbcDatabaseInfo)) {
         return false;
      }

      WsJdbcDatabaseInfo var2 = (WsJdbcDatabaseInfo)var1;
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
      boolean var3 = (this.objects == null && var2.getObjects() == null || this.objects != null && Arrays.equals(this.objects, var2.getObjects()))
         && (this.quotes == null && var2.getQuotes() == null || this.quotes != null && this.quotes.equals(var2.getQuotes()));
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
      if (this.getObjects() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getObjects()); var2++) {
            Object var3 = Array.get(this.getObjects(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getQuotes() != null) {
         var1 += this.getQuotes().hashCode();
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
      typeDesc.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseInfo"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("objects");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "objects"));
      var0.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseObject"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("quotes");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "quotes"));
      var0.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcBeginEndQuote"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
