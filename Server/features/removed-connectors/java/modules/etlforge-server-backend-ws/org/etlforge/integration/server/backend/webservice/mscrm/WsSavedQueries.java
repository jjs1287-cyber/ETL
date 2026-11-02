package org.etlforge.integration.server.backend.webservice.mscrm;

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
import org.etlforge.integration.server.backend.webservice.connector.WsStatus;

public class WsSavedQueries implements Serializable {
   private WsStatus status;
   private WsFetchXml[] queries;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSavedQueries.class, true);

   public WsSavedQueries() {
   }

   public WsSavedQueries(WsStatus var1, WsFetchXml[] var2) {
      this.status = var1;
      this.queries = var2;
   }

   public WsStatus getStatus() {
      return this.status;
   }

   public void setStatus(WsStatus var1) {
      this.status = var1;
   }

   public WsFetchXml[] getQueries() {
      return this.queries;
   }

   public void setQueries(WsFetchXml[] var1) {
      this.queries = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSavedQueries)) {
         return false;
      }

      WsSavedQueries var2 = (WsSavedQueries)var1;
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
         && (this.queries == null && var2.getQueries() == null || this.queries != null && Arrays.equals(this.queries, var2.getQueries()));
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

      if (this.getQueries() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getQueries()); var2++) {
            Object var3 = Array.get(this.getQueries(), var2);
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
      typeDesc.setXmlType(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsSavedQueries"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "Status"));
      var0.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("queries");
      var0.setXmlName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "Queries"));
      var0.setXmlType(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsFetchXml"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
