package org.etlforge.integration.data.entity;

import com.google.common.base.Optional;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.PasswordUtils;
import org.etlforge.integration.data.entity.message.MessageValidator;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public abstract class Message extends IntegrationEntity {
   @IdProperty
   public static final String MESSAGE_ID = "message_id";
   @NameProperty
   public static final String MESSAGE_NAME = "message_name";
   public static final String SMTP_SERVER_PROPERTY = "smtp_servers";
   public static final String ACCOUNT_PROPERTY = "account";
   public static final String PASSWORD_PROPERTY = "account_password";
   public static final String SEND_TO_PROPERTY = "send_to";
   public static final String SENT_FROM_PROPERTY = "sent_from";
   public static final String CC_PROPERTY = "cc";
   public static final String BCC_PROPERTY = "bcc";
   public static final String REPLY_TO_PROPERTY = "reply_to";
   public static final String SUBJECT_PROPERTY = "subject";
   public static final String USE_SSL_PROPERTY = "use_ssl";
   public static final String MESSAGE_TEXT_PROPERTY = "message_text";
   private static final String[] PROPERTIES = new String[]{
      "message_id",
      "message_name",
      "smtp_servers",
      "account",
      "account_password",
      "send_to",
      "sent_from",
      "cc",
      "bcc",
      "reply_to",
      "subject",
      "use_ssl",
      "message_text"
   };

   @Override
   public final String[] getPropertyKeys() {
      return (String[])PROPERTIES.clone();
   }

   public Message(EntityType var1, IntegrationEntityId var2, String var3) {
      super(var1, var2, var3);
   }

   public final String getReplyTo() {
      return this.getProperty("reply_to");
   }

   public final void setReplyTo(String var1) {
      this.setStringProperty("reply_to", var1);
   }

   public final String getCC() {
      return this.getProperty("cc");
   }

   public final void setCC(String var1) {
      this.setStringProperty("cc", var1);
   }

   public final String getBCC() {
      return this.getProperty("bcc");
   }

   public final void setBCC(String var1) {
      this.setStringProperty("bcc", var1);
   }

   public final String getSendTo() {
      return this.getProperty("send_to");
   }

   public final void setSendTo(String var1) {
      this.setStringProperty("send_to", var1);
   }

   public final String getSentFrom() {
      return this.getProperty("sent_from");
   }

   public final void setSentFrom(String var1) {
      this.setStringProperty("sent_from", var1);
   }

   public final String getSubject() {
      return this.getProperty("subject");
   }

   public final void setSubject(String var1) {
      this.setStringProperty("subject", var1);
   }

   public final String getSmtpServers() {
      return this.getProperty("smtp_servers");
   }

   public final void setSmtpServers(String var1) {
      this.setStringProperty("smtp_servers", var1);
   }

   public final String getText() {
      return this.getProperty("message_text");
   }

   public final void setText(String var1) {
      this.setStringProperty("message_text", var1);
   }

   public final void setAccountName(String var1) {
      this.setStringProperty("account", var1);
   }

   public final String getAccountName() {
      return this.getProperty("account");
   }

   public final void setAccountPassword(Optional<Password> var1) {
      PasswordUtils.setPassword(this, "account_password", var1);
   }

   public final Optional<Password> getAccountPassword() {
      return PasswordUtils.getPassword(this, "account_password");
   }

   public final void setUseSSL(boolean var1) {
      this.setBooleanProperty("use_ssl", var1, false);
   }

   public final boolean getUseSSL() {
      return this.getBooleanProperty("use_ssl", false);
   }

   @Override
   protected final void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      MessageValidator var2 = new MessageValidator(this, var1);
      var2.run();
   }

   @Override
   public final StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = super.getCopyOfKeyValues();
      var1.put("message_name", this.getName());
      return var1;
   }
}
