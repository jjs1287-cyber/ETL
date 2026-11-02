package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class BreakLocation implements Serializable {
   private String operationGuid;
   private String transformationGuid;
   private String scriptGuid;
   private String nodePath;
   private int lineNumber;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(BreakLocation.class, true);

   public BreakLocation() {
   }

   public BreakLocation(String var1, String var2, String var3, String var4, int var5) {
      this.operationGuid = var1;
      this.transformationGuid = var2;
      this.scriptGuid = var3;
      this.nodePath = var4;
      this.lineNumber = var5;
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

   public String getScriptGuid() {
      return this.scriptGuid;
   }

   public void setScriptGuid(String var1) {
      this.scriptGuid = var1;
   }

   public String getNodePath() {
      return this.nodePath;
   }

   public void setNodePath(String var1) {
      this.nodePath = var1;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public void setLineNumber(int var1) {
      this.lineNumber = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof BreakLocation)) {
         return false;
      }

      BreakLocation var2 = (BreakLocation)var1;
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
         && (this.scriptGuid == null && var2.getScriptGuid() == null || this.scriptGuid != null && this.scriptGuid.equals(var2.getScriptGuid()))
         && (this.nodePath == null && var2.getNodePath() == null || this.nodePath != null && this.nodePath.equals(var2.getNodePath()))
         && this.lineNumber == var2.getLineNumber();
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

      if (this.getScriptGuid() != null) {
         var1 += this.getScriptGuid().hashCode();
      }

      if (this.getNodePath() != null) {
         var1 += this.getNodePath().hashCode();
      }

      var1 += this.getLineNumber();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "BreakLocation")
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
      var0.setFieldName("scriptGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "ScriptGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("nodePath");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "NodePath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("lineNumber");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.treemapper.TreeMapperInfoProvider", "LineNumber"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
