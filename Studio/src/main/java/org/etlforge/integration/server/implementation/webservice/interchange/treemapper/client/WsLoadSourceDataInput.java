package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

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

public class WsLoadSourceDataInput implements Serializable {
   private String transformationGuid;
   private String sourceGuid;
   private String compressedBase64EncodedData;
   private PipelinePositionStruct[] pipelinePlugins;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsLoadSourceDataInput.class, true);

   public WsLoadSourceDataInput() {
   }

   public WsLoadSourceDataInput(String var1, String var2, String var3, PipelinePositionStruct[] var4) {
      this.transformationGuid = var1;
      this.sourceGuid = var2;
      this.compressedBase64EncodedData = var3;
      this.pipelinePlugins = var4;
   }

   public String getTransformationGuid() {
      return this.transformationGuid;
   }

   public void setTransformationGuid(String var1) {
      this.transformationGuid = var1;
   }

   public String getSourceGuid() {
      return this.sourceGuid;
   }

   public void setSourceGuid(String var1) {
      this.sourceGuid = var1;
   }

   public String getCompressedBase64EncodedData() {
      return this.compressedBase64EncodedData;
   }

   public void setCompressedBase64EncodedData(String var1) {
      this.compressedBase64EncodedData = var1;
   }

   public PipelinePositionStruct[] getPipelinePlugins() {
      return this.pipelinePlugins;
   }

   public void setPipelinePlugins(PipelinePositionStruct[] var1) {
      this.pipelinePlugins = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsLoadSourceDataInput)) {
         return false;
      }

      WsLoadSourceDataInput var2 = (WsLoadSourceDataInput)var1;
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
            this.transformationGuid == null && var2.getTransformationGuid() == null
               || this.transformationGuid != null && this.transformationGuid.equals(var2.getTransformationGuid())
         )
         && (this.sourceGuid == null && var2.getSourceGuid() == null || this.sourceGuid != null && this.sourceGuid.equals(var2.getSourceGuid()))
         && (
            this.compressedBase64EncodedData == null && var2.getCompressedBase64EncodedData() == null
               || this.compressedBase64EncodedData != null && this.compressedBase64EncodedData.equals(var2.getCompressedBase64EncodedData())
         )
         && (
            this.pipelinePlugins == null && var2.getPipelinePlugins() == null
               || this.pipelinePlugins != null && Arrays.equals(this.pipelinePlugins, var2.getPipelinePlugins())
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
      if (this.getTransformationGuid() != null) {
         var1 += this.getTransformationGuid().hashCode();
      }

      if (this.getSourceGuid() != null) {
         var1 += this.getSourceGuid().hashCode();
      }

      if (this.getCompressedBase64EncodedData() != null) {
         var1 += this.getCompressedBase64EncodedData().hashCode();
      }

      if (this.getPipelinePlugins() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getPipelinePlugins()); var2++) {
            Object var3 = Array.get(this.getPipelinePlugins(), var2);
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
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "WsLoadSourceDataInput")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("transformationGuid");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "TransformationGuid")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sourceGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "SourceGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("compressedBase64EncodedData");
      var0.setXmlName(
         new QName(
            "urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "CompressedBase64EncodedData"
         )
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("pipelinePlugins");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "PipelinePlugins")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "PipelinePositionStruct")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
