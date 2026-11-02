package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class PipelinePositionStruct implements Serializable {
   private int entityType;
   private int executionOrder;
   private String relativePosition;
   private String pluginGuid;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(PipelinePositionStruct.class, true);

   public PipelinePositionStruct() {
   }

   public PipelinePositionStruct(int var1, int var2, String var3, String var4) {
      this.entityType = var1;
      this.executionOrder = var2;
      this.relativePosition = var3;
      this.pluginGuid = var4;
   }

   public int getEntityType() {
      return this.entityType;
   }

   public void setEntityType(int var1) {
      this.entityType = var1;
   }

   public int getExecutionOrder() {
      return this.executionOrder;
   }

   public void setExecutionOrder(int var1) {
      this.executionOrder = var1;
   }

   public String getRelativePosition() {
      return this.relativePosition;
   }

   public void setRelativePosition(String var1) {
      this.relativePosition = var1;
   }

   public String getPluginGuid() {
      return this.pluginGuid;
   }

   public void setPluginGuid(String var1) {
      this.pluginGuid = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof PipelinePositionStruct)) {
         return false;
      }

      PipelinePositionStruct var2 = (PipelinePositionStruct)var1;
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
      boolean var3 = this.entityType == var2.getEntityType()
         && this.executionOrder == var2.getExecutionOrder()
         && (
            this.relativePosition == null && var2.getRelativePosition() == null
               || this.relativePosition != null && this.relativePosition.equals(var2.getRelativePosition())
         )
         && (this.pluginGuid == null && var2.getPluginGuid() == null || this.pluginGuid != null && this.pluginGuid.equals(var2.getPluginGuid()));
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
      var1 += this.getEntityType();
      var1 += this.getExecutionOrder();
      if (this.getRelativePosition() != null) {
         var1 += this.getRelativePosition().hashCode();
      }

      if (this.getPluginGuid() != null) {
         var1 += this.getPluginGuid().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "PipelinePositionStruct")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("entityType");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "EntityType"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("executionOrder");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "ExecutionOrder")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("relativePosition");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "RelativePosition")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("pluginGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "PluginGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
