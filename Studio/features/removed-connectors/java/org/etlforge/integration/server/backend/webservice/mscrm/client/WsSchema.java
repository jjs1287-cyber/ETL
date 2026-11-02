package org.etlforge.integration.server.backend.webservice.mscrm.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsSchema implements Serializable {
   private String compressedContents;
   private String rootName;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSchema.class, true);

   public WsSchema() {
   }

   public WsSchema(String var1, String var2) {
      this.compressedContents = var1;
      this.rootName = var2;
   }

   public String getCompressedContents() {
      return this.compressedContents;
   }

   public void setCompressedContents(String var1) {
      this.compressedContents = var1;
   }

   public String getRootName() {
      return this.rootName;
   }

   public void setRootName(String var1) {
      this.rootName = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSchema)) {
         return false;
      }

      WsSchema var2 = (WsSchema)var1;
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
      boolean var3 = (
            this.compressedContents == null && var2.getCompressedContents() == null
               || this.compressedContents != null && this.compressedContents.equals(var2.getCompressedContents())
         )
         && (this.rootName == null && var2.getRootName() == null || this.rootName != null && this.rootName.equals(var2.getRootName()));
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
      if (this.getCompressedContents() != null) {
         var1 += this.getCompressedContents().hashCode();
      }

      if (this.getRootName() != null) {
         var1 += this.getRootName().hashCode();
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
      typeDesc.setXmlType(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsSchema"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("compressedContents");
      var0.setXmlName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "CompressedContents"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("rootName");
      var0.setXmlName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "RootName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
