package org.etlforge.integration.server.implementation.webservice.projectadmin.client;

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

public class ScheduledOperation implements Serializable {
   private String scheduleGuid;
   private String scheduleName;
   private String operationGuid;
   private String operationName;
   private String[] ISO8601ExecutionTimes;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(ScheduledOperation.class, true);

   public ScheduledOperation() {
   }

   public ScheduledOperation(String var1, String var2, String var3, String var4, String[] var5) {
      this.scheduleGuid = var1;
      this.scheduleName = var2;
      this.operationGuid = var3;
      this.operationName = var4;
      this.ISO8601ExecutionTimes = var5;
   }

   public String getScheduleGuid() {
      return this.scheduleGuid;
   }

   public void setScheduleGuid(String var1) {
      this.scheduleGuid = var1;
   }

   public String getScheduleName() {
      return this.scheduleName;
   }

   public void setScheduleName(String var1) {
      this.scheduleName = var1;
   }

   public String getOperationGuid() {
      return this.operationGuid;
   }

   public void setOperationGuid(String var1) {
      this.operationGuid = var1;
   }

   public String getOperationName() {
      return this.operationName;
   }

   public void setOperationName(String var1) {
      this.operationName = var1;
   }

   public String[] getISO8601ExecutionTimes() {
      return this.ISO8601ExecutionTimes;
   }

   public void setISO8601ExecutionTimes(String[] var1) {
      this.ISO8601ExecutionTimes = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof ScheduledOperation)) {
         return false;
      }

      ScheduledOperation var2 = (ScheduledOperation)var1;
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
            this.scheduleGuid == null && var2.getScheduleGuid() == null || this.scheduleGuid != null && this.scheduleGuid.equals(var2.getScheduleGuid())
         )
         && (this.scheduleName == null && var2.getScheduleName() == null || this.scheduleName != null && this.scheduleName.equals(var2.getScheduleName()))
         && (this.operationGuid == null && var2.getOperationGuid() == null || this.operationGuid != null && this.operationGuid.equals(var2.getOperationGuid()))
         && (this.operationName == null && var2.getOperationName() == null || this.operationName != null && this.operationName.equals(var2.getOperationName()))
         && (
            this.ISO8601ExecutionTimes == null && var2.getISO8601ExecutionTimes() == null
               || this.ISO8601ExecutionTimes != null && Arrays.equals(this.ISO8601ExecutionTimes, var2.getISO8601ExecutionTimes())
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
      if (this.getScheduleGuid() != null) {
         var1 += this.getScheduleGuid().hashCode();
      }

      if (this.getScheduleName() != null) {
         var1 += this.getScheduleName().hashCode();
      }

      if (this.getOperationGuid() != null) {
         var1 += this.getOperationGuid().hashCode();
      }

      if (this.getOperationName() != null) {
         var1 += this.getOperationName().hashCode();
      }

      if (this.getISO8601ExecutionTimes() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getISO8601ExecutionTimes()); var2++) {
            Object var3 = Array.get(this.getISO8601ExecutionTimes(), var2);
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
      typeDesc.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ScheduledOperation"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("scheduleGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ScheduleGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("scheduleName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ScheduleName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationGuid");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "OperationGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationName");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "OperationName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ISO8601ExecutionTimes");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ISO8601ExecutionTimes"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
