package org.etlforge.integration.server.implementation.webservice.interchange.db.client;

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

public class WsDatabaseInfo implements Serializable {
   private String quoteBegin;
   private String quoteEnd;
   private WsDatabaseObject[] dbInfo;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsDatabaseInfo.class, true);

   public WsDatabaseInfo() {
   }

   public WsDatabaseInfo(String var1, String var2, WsDatabaseObject[] var3) {
      this.quoteBegin = var1;
      this.quoteEnd = var2;
      this.dbInfo = var3;
   }

   public String getQuoteBegin() {
      return this.quoteBegin;
   }

   public void setQuoteBegin(String var1) {
      this.quoteBegin = var1;
   }

   public String getQuoteEnd() {
      return this.quoteEnd;
   }

   public void setQuoteEnd(String var1) {
      this.quoteEnd = var1;
   }

   public WsDatabaseObject[] getDbInfo() {
      return this.dbInfo;
   }

   public void setDbInfo(WsDatabaseObject[] var1) {
      this.dbInfo = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsDatabaseInfo)) {
         return false;
      }

      WsDatabaseInfo var2 = (WsDatabaseInfo)var1;
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
      boolean var3 = (this.quoteBegin == null && var2.getQuoteBegin() == null || this.quoteBegin != null && this.quoteBegin.equals(var2.getQuoteBegin()))
         && (this.quoteEnd == null && var2.getQuoteEnd() == null || this.quoteEnd != null && this.quoteEnd.equals(var2.getQuoteEnd()))
         && (this.dbInfo == null && var2.getDbInfo() == null || this.dbInfo != null && Arrays.equals(this.dbInfo, var2.getDbInfo()));
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
      if (this.getQuoteBegin() != null) {
         var1 += this.getQuoteBegin().hashCode();
      }

      if (this.getQuoteEnd() != null) {
         var1 += this.getQuoteEnd().hashCode();
      }

      if (this.getDbInfo() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getDbInfo()); var2++) {
            Object var3 = Array.get(this.getDbInfo(), var2);
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseInfo"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("quoteBegin");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "QuoteBegin"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("quoteEnd");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "QuoteEnd"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dbInfo");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "DbInfo"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseObject"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
