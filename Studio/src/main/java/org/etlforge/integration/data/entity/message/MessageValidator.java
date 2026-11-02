package org.etlforge.integration.data.entity.message;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.Message;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class MessageValidator {
   private final Message message;
   private final ValidationMessageCollector collector;

   public MessageValidator(Message var1, ValidationMessageCollector var2) {
      Preconditions.checkNotNull(var1, "msg");
      Preconditions.checkNotNull(var2, "collector");
      this.message = var1;
      this.collector = var2;
   }

   public void run() {
      String var1 = this.message.getSmtpServers();
      if (isEmpty(var1)) {
         this.warning(MessageMessages.getString("MessageValidator.NoServer"));
      }

      String var2 = this.message.getSendTo();
      if (isEmpty(var2)) {
         this.fatal(MessageMessages.getString("MessageValidator.NoRecipient"));
      }

      String var3 = this.message.getSentFrom();
      if (isEmpty(var3)) {
         this.warning(MessageMessages.getString("MessageValidator.NoSender"));
      }

      String var4 = this.message.getSubject();
      if (isEmpty(var4)) {
         this.warning(MessageMessages.getString("MessageValidator.NoSubject"));
      }

      String var5 = this.message.getText();
      if (isEmpty(var5)) {
         this.warning(MessageMessages.getString("MessageValidator.NoText"));
      }
   }

   private void warning(String var1) {
      this.collector.addMessage(var1, ValidationStatus.WARNING);
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }

   private static boolean isEmpty(String var0) {
      return var0 == null || var0.trim().length() == 0;
   }
}
