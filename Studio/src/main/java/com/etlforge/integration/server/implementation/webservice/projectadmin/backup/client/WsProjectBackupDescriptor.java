package com.etlforge.integration.server.implementation.webservice.projectadmin.backup.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsProjectBackupDescriptor implements Serializable {
   private long id;
   private String projectId;
   private String serverId;
   private String dateOfBackup;
   private String filePath;
   private String userName;
   private String userId;
   private String tag;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsProjectBackupDescriptor.class, true);

   public WsProjectBackupDescriptor() {
   }

   public WsProjectBackupDescriptor(long var1, String var3, String var4, String var5, String var6, String var7, String var8, String var9) {
      this.id = var1;
      this.projectId = var3;
      this.serverId = var4;
      this.dateOfBackup = var5;
      this.filePath = var6;
      this.userName = var7;
      this.userId = var8;
      this.tag = var9;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long var1) {
      this.id = var1;
   }

   public String getProjectId() {
      return this.projectId;
   }

   public void setProjectId(String var1) {
      this.projectId = var1;
   }

   public String getServerId() {
      return this.serverId;
   }

   public void setServerId(String var1) {
      this.serverId = var1;
   }

   public String getDateOfBackup() {
      return this.dateOfBackup;
   }

   public void setDateOfBackup(String var1) {
      this.dateOfBackup = var1;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public void setFilePath(String var1) {
      this.filePath = var1;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String var1) {
      this.userName = var1;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String var1) {
      this.userId = var1;
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String var1) {
      this.tag = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsProjectBackupDescriptor)) {
         return false;
      }

      WsProjectBackupDescriptor var2 = (WsProjectBackupDescriptor)var1;
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
      boolean var3 = this.id == var2.getId()
         && (this.projectId == null && var2.getProjectId() == null || this.projectId != null && this.projectId.equals(var2.getProjectId()))
         && (this.serverId == null && var2.getServerId() == null || this.serverId != null && this.serverId.equals(var2.getServerId()))
         && (this.dateOfBackup == null && var2.getDateOfBackup() == null || this.dateOfBackup != null && this.dateOfBackup.equals(var2.getDateOfBackup()))
         && (this.filePath == null && var2.getFilePath() == null || this.filePath != null && this.filePath.equals(var2.getFilePath()))
         && (this.userName == null && var2.getUserName() == null || this.userName != null && this.userName.equals(var2.getUserName()))
         && (this.userId == null && var2.getUserId() == null || this.userId != null && this.userId.equals(var2.getUserId()))
         && (this.tag == null && var2.getTag() == null || this.tag != null && this.tag.equals(var2.getTag()));
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
      var1 += new Long(this.getId()).hashCode();
      if (this.getProjectId() != null) {
         var1 += this.getProjectId().hashCode();
      }

      if (this.getServerId() != null) {
         var1 += this.getServerId().hashCode();
      }

      if (this.getDateOfBackup() != null) {
         var1 += this.getDateOfBackup().hashCode();
      }

      if (this.getFilePath() != null) {
         var1 += this.getFilePath().hashCode();
      }

      if (this.getUserName() != null) {
         var1 += this.getUserName().hashCode();
      }

      if (this.getUserId() != null) {
         var1 += this.getUserId().hashCode();
      }

      if (this.getTag() != null) {
         var1 += this.getTag().hashCode();
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
      typeDesc.setXmlType(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "WsProjectBackupDescriptor"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("id");
      var0.setXmlName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "id"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("projectId");
      var0.setXmlName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "projectId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("serverId");
      var0.setXmlName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "serverId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dateOfBackup");
      var0.setXmlName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "dateOfBackup"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("filePath");
      var0.setXmlName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "filePath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userName");
      var0.setXmlName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "userName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userId");
      var0.setXmlName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "userId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("tag");
      var0.setXmlName(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "tag"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
