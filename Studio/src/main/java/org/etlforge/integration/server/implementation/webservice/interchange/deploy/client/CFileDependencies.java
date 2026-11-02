package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

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

public class CFileDependencies implements Serializable {
   private String[] parentFilePath;
   private String[] childFilePath;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CFileDependencies.class, true);

   public CFileDependencies() {
   }

   public CFileDependencies(String[] var1, String[] var2) {
      this.parentFilePath = var1;
      this.childFilePath = var2;
   }

   public String[] getParentFilePath() {
      return this.parentFilePath;
   }

   public void setParentFilePath(String[] var1) {
      this.parentFilePath = var1;
   }

   public String[] getChildFilePath() {
      return this.childFilePath;
   }

   public void setChildFilePath(String[] var1) {
      this.childFilePath = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CFileDependencies)) {
         return false;
      }

      CFileDependencies var2 = (CFileDependencies)var1;
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
            this.parentFilePath == null && var2.getParentFilePath() == null
               || this.parentFilePath != null && Arrays.equals(this.parentFilePath, var2.getParentFilePath())
         )
         && (
            this.childFilePath == null && var2.getChildFilePath() == null
               || this.childFilePath != null && Arrays.equals(this.childFilePath, var2.getChildFilePath())
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
      if (this.getParentFilePath() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getParentFilePath()); var2++) {
            Object var3 = Array.get(this.getParentFilePath(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getChildFilePath() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getChildFilePath()); var4++) {
            Object var5 = Array.get(this.getChildFilePath(), var4);
            if (var5 != null && !var5.getClass().isArray()) {
               var1 += var5.hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CFileDependencies")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("parentFilePath");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ParentFilePath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("childFilePath");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ChildFilePath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
