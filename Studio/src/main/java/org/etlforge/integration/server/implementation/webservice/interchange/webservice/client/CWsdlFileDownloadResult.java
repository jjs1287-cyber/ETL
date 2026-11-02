package org.etlforge.integration.server.implementation.webservice.interchange.webservice.client;

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

public class CWsdlFileDownloadResult implements Serializable {
   private CWsdlFile wsdlFile;
   private CWsdlFile[] dependencies;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(CWsdlFileDownloadResult.class, true);

   public CWsdlFileDownloadResult() {
   }

   public CWsdlFileDownloadResult(CWsdlFile var1, CWsdlFile[] var2) {
      this.wsdlFile = var1;
      this.dependencies = var2;
   }

   public CWsdlFile getWsdlFile() {
      return this.wsdlFile;
   }

   public void setWsdlFile(CWsdlFile var1) {
      this.wsdlFile = var1;
   }

   public CWsdlFile[] getDependencies() {
      return this.dependencies;
   }

   public void setDependencies(CWsdlFile[] var1) {
      this.dependencies = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof CWsdlFileDownloadResult)) {
         return false;
      }

      CWsdlFileDownloadResult var2 = (CWsdlFileDownloadResult)var1;
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
      boolean var3 = (this.wsdlFile == null && var2.getWsdlFile() == null || this.wsdlFile != null && this.wsdlFile.equals(var2.getWsdlFile()))
         && (
            this.dependencies == null && var2.getDependencies() == null
               || this.dependencies != null && Arrays.equals(this.dependencies, var2.getDependencies())
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
      if (this.getWsdlFile() != null) {
         var1 += this.getWsdlFile().hashCode();
      }

      if (this.getDependencies() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getDependencies()); var2++) {
            Object var3 = Array.get(this.getDependencies(), var2);
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFileDownloadResult")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("wsdlFile");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WsdlFile"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dependencies");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "Dependencies"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
