package org.etlforge.integration.server.backend.webservice.function;

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

public class WsSendEmailInput implements Serializable {
   private String smtpMailServers;
   private int useSSL;
   private String to;
   private String from;
   private String account;
   private String accountPassword;
   private String CC;
   private String BCC;
   private String replyTo;
   private String subject;
   private String messageText;
   private String[] attachmentFilePaths;
   private String[] attachmentNames;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsSendEmailInput.class, true);

   public WsSendEmailInput() {
   }

   public WsSendEmailInput(
      String var1,
      int var2,
      String var3,
      String var4,
      String var5,
      String var6,
      String var7,
      String var8,
      String var9,
      String var10,
      String var11,
      String[] var12,
      String[] var13
   ) {
      this.smtpMailServers = var1;
      this.useSSL = var2;
      this.to = var3;
      this.from = var4;
      this.account = var5;
      this.accountPassword = var6;
      this.CC = var7;
      this.BCC = var8;
      this.replyTo = var9;
      this.subject = var10;
      this.messageText = var11;
      this.attachmentFilePaths = var12;
      this.attachmentNames = var13;
   }

   public String getSmtpMailServers() {
      return this.smtpMailServers;
   }

   public void setSmtpMailServers(String var1) {
      this.smtpMailServers = var1;
   }

   public int getUseSSL() {
      return this.useSSL;
   }

   public void setUseSSL(int var1) {
      this.useSSL = var1;
   }

   public String getTo() {
      return this.to;
   }

   public void setTo(String var1) {
      this.to = var1;
   }

   public String getFrom() {
      return this.from;
   }

   public void setFrom(String var1) {
      this.from = var1;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String var1) {
      this.account = var1;
   }

   public String getAccountPassword() {
      return this.accountPassword;
   }

   public void setAccountPassword(String var1) {
      this.accountPassword = var1;
   }

   public String getCC() {
      return this.CC;
   }

   public void setCC(String var1) {
      this.CC = var1;
   }

   public String getBCC() {
      return this.BCC;
   }

   public void setBCC(String var1) {
      this.BCC = var1;
   }

   public String getReplyTo() {
      return this.replyTo;
   }

   public void setReplyTo(String var1) {
      this.replyTo = var1;
   }

   public String getSubject() {
      return this.subject;
   }

   public void setSubject(String var1) {
      this.subject = var1;
   }

   public String getMessageText() {
      return this.messageText;
   }

   public void setMessageText(String var1) {
      this.messageText = var1;
   }

   public String[] getAttachmentFilePaths() {
      return this.attachmentFilePaths;
   }

   public void setAttachmentFilePaths(String[] var1) {
      this.attachmentFilePaths = var1;
   }

   public String[] getAttachmentNames() {
      return this.attachmentNames;
   }

   public void setAttachmentNames(String[] var1) {
      this.attachmentNames = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsSendEmailInput)) {
         return false;
      }

      WsSendEmailInput var2 = (WsSendEmailInput)var1;
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
            this.smtpMailServers == null && var2.getSmtpMailServers() == null
               || this.smtpMailServers != null && this.smtpMailServers.equals(var2.getSmtpMailServers())
         )
         && this.useSSL == var2.getUseSSL()
         && (this.to == null && var2.getTo() == null || this.to != null && this.to.equals(var2.getTo()))
         && (this.from == null && var2.getFrom() == null || this.from != null && this.from.equals(var2.getFrom()))
         && (this.account == null && var2.getAccount() == null || this.account != null && this.account.equals(var2.getAccount()))
         && (
            this.accountPassword == null && var2.getAccountPassword() == null
               || this.accountPassword != null && this.accountPassword.equals(var2.getAccountPassword())
         )
         && (this.CC == null && var2.getCC() == null || this.CC != null && this.CC.equals(var2.getCC()))
         && (this.BCC == null && var2.getBCC() == null || this.BCC != null && this.BCC.equals(var2.getBCC()))
         && (this.replyTo == null && var2.getReplyTo() == null || this.replyTo != null && this.replyTo.equals(var2.getReplyTo()))
         && (this.subject == null && var2.getSubject() == null || this.subject != null && this.subject.equals(var2.getSubject()))
         && (this.messageText == null && var2.getMessageText() == null || this.messageText != null && this.messageText.equals(var2.getMessageText()))
         && (
            this.attachmentFilePaths == null && var2.getAttachmentFilePaths() == null
               || this.attachmentFilePaths != null && Arrays.equals(this.attachmentFilePaths, var2.getAttachmentFilePaths())
         )
         && (
            this.attachmentNames == null && var2.getAttachmentNames() == null
               || this.attachmentNames != null && Arrays.equals(this.attachmentNames, var2.getAttachmentNames())
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
      if (this.getSmtpMailServers() != null) {
         var1 += this.getSmtpMailServers().hashCode();
      }

      var1 += this.getUseSSL();
      if (this.getTo() != null) {
         var1 += this.getTo().hashCode();
      }

      if (this.getFrom() != null) {
         var1 += this.getFrom().hashCode();
      }

      if (this.getAccount() != null) {
         var1 += this.getAccount().hashCode();
      }

      if (this.getAccountPassword() != null) {
         var1 += this.getAccountPassword().hashCode();
      }

      if (this.getCC() != null) {
         var1 += this.getCC().hashCode();
      }

      if (this.getBCC() != null) {
         var1 += this.getBCC().hashCode();
      }

      if (this.getReplyTo() != null) {
         var1 += this.getReplyTo().hashCode();
      }

      if (this.getSubject() != null) {
         var1 += this.getSubject().hashCode();
      }

      if (this.getMessageText() != null) {
         var1 += this.getMessageText().hashCode();
      }

      if (this.getAttachmentFilePaths() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getAttachmentFilePaths()); var2++) {
            Object var3 = Array.get(this.getAttachmentFilePaths(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getAttachmentNames() != null) {
         for (int var5 = 0; var5 < Array.getLength(this.getAttachmentNames()); var5++) {
            Object var6 = Array.get(this.getAttachmentNames(), var5);
            if (var6 != null && !var6.getClass().isArray()) {
               var1 += var6.hashCode();
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
      typeDesc.setXmlType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsSendEmailInput"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("smtpMailServers");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "SmtpMailServers"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("useSSL");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "UseSSL"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("to");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "To"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("from");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "From"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("account");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Account"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("accountPassword");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "AccountPassword"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("CC");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "CC"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("BCC");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "BCC"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("replyTo");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "ReplyTo"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("subject");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "Subject"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("messageText");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "MessageText"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("attachmentFilePaths");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "AttachmentFilePaths"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("attachmentNames");
      var0.setXmlName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "AttachmentNames"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
