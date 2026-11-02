package org.etlforge.integration.server.backend.webservice.connector.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsConnectorSchemaFile implements Serializable {
   private String schemaFileName;
   private String schemaContent;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsConnectorSchemaFile.class, true);

   public WsConnectorSchemaFile() {
   }

   public WsConnectorSchemaFile(String var1, String var2) {
      this.schemaFileName = var1;
      this.schemaContent = var2;
   }

   public String getSchemaFileName() {
      return this.schemaFileName;
   }

   public void setSchemaFileName(String var1) {
      this.schemaFileName = var1;
   }

   public String getSchemaContent() {
      return this.schemaContent;
   }

   public void setSchemaContent(String var1) {
      this.schemaContent = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsConnectorSchemaFile)) {
         return false;
      }

      WsConnectorSchemaFile var2 = (WsConnectorSchemaFile)var1;
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
            this.schemaFileName == null && var2.getSchemaFileName() == null
               || this.schemaFileName != null && this.schemaFileName.equals(var2.getSchemaFileName())
         )
         && (this.schemaContent == null && var2.getSchemaContent() == null || this.schemaContent != null && this.schemaContent.equals(var2.getSchemaContent()));
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
      if (this.getSchemaFileName() != null) {
         var1 += this.getSchemaFileName().hashCode();
      }

      if (this.getSchemaContent() != null) {
         var1 += this.getSchemaContent().hashCode();
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemaFile"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("schemaFileName");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "SchemaFileName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("schemaContent");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "SchemaContent"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
