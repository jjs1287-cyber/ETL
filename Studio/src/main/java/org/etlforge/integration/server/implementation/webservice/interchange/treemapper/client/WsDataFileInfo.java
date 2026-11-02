package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsDataFileInfo implements Serializable {
   private String operationGuid;
   private String transformationGuid;
   private int type;
   private int numberOfChunks;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsDataFileInfo.class, true);

   public WsDataFileInfo() {
   }

   public WsDataFileInfo(String var1, String var2, int var3, int var4) {
      this.operationGuid = var1;
      this.transformationGuid = var2;
      this.type = var3;
      this.numberOfChunks = var4;
   }

   public String getOperationGuid() {
      return this.operationGuid;
   }

   public void setOperationGuid(String var1) {
      this.operationGuid = var1;
   }

   public String getTransformationGuid() {
      return this.transformationGuid;
   }

   public void setTransformationGuid(String var1) {
      this.transformationGuid = var1;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int var1) {
      this.type = var1;
   }

   public int getNumberOfChunks() {
      return this.numberOfChunks;
   }

   public void setNumberOfChunks(int var1) {
      this.numberOfChunks = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsDataFileInfo)) {
         return false;
      }

      WsDataFileInfo var2 = (WsDataFileInfo)var1;
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
            this.operationGuid == null && var2.getOperationGuid() == null || this.operationGuid != null && this.operationGuid.equals(var2.getOperationGuid())
         )
         && (
            this.transformationGuid == null && var2.getTransformationGuid() == null
               || this.transformationGuid != null && this.transformationGuid.equals(var2.getTransformationGuid())
         )
         && this.type == var2.getType()
         && this.numberOfChunks == var2.getNumberOfChunks();
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
      if (this.getOperationGuid() != null) {
         var1 += this.getOperationGuid().hashCode();
      }

      if (this.getTransformationGuid() != null) {
         var1 += this.getTransformationGuid().hashCode();
      }

      var1 += this.getType();
      var1 += this.getNumberOfChunks();
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
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsDataFileInfo")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("operationGuid");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "OperationGuid")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationGuid");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "TransformationGuid")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("type");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "Type"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("numberOfChunks");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "NumberOfChunks")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
