package com.etlforge.integration.server.email;

import java.io.IOException;
import java.util.Date;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang.StringUtils;

public class MessageSettings {
   private final String from;
   private final String to;
   private final String ccs;
   private final String bccs;
   private final String replyTo;
   private final String subject;
   private final String text;

   private static final String safe(String var0) {
      return var0 == null ? new String("") : var0;
   }

   public MessageSettings(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      if (StringUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("The From: field can not be empty.");
      }

      if (StringUtils.isEmpty(var2)) {
         throw new IllegalArgumentException("The To: field can not be empty.");
      }

      this.from = var1;
      this.to = var2;
      this.ccs = safe(var5);
      this.bccs = safe(var6);
      this.replyTo = safe(var7);
      this.subject = safe(var3);
      this.text = safe(var4);
   }

   private MimeBodyPart createAttachment(EmailAttachment var1) throws MessagingException, IOException {
      MimeBodyPart var2 = new MimeBodyPart();
      var2.attachFile(var1.getPath());
      String var3 = var1.getName();
      if (!var3.isEmpty()) {
         var2.setFileName(var3);
      }

      return var2;
   }

   Message createMessage(Session var1) throws MessagingException {
      return this.createMessage(var1, true);
   }

   public Message createMessage(Session var1, Iterable<EmailAttachment> var2) throws MessagingException, IOException {
      MimeMultipart var3 = new MimeMultipart();

      for (EmailAttachment var5 : var2) {
         var3.addBodyPart(this.createAttachment(var5));
      }

      MimeBodyPart var6 = this.createTextPart();
      var3.addBodyPart(var6);
      MimeMessage var7 = this.createMessage(var1, false);
      var7.setContent(var3);
      var7.saveChanges();
      return var7;
   }

   private MimeBodyPart createTextPart() throws MessagingException {
      MimeBodyPart var1 = new MimeBodyPart();
      var1.setText(this.text, "UTF-8", this.getMimeSubType());
      return var1;
   }

   MimeMessage createMessage(Session var1, boolean var2) throws MessagingException {
      MimeMessage var3 = new MimeMessage(var1);
      var3.setFrom(new InternetAddress(this.from));
      var3.addRecipients(RecipientType.TO, this.to);
      if (!this.ccs.isEmpty()) {
         var3.addRecipients(RecipientType.CC, this.ccs);
      }

      if (!this.bccs.isEmpty()) {
         var3.addRecipients(RecipientType.BCC, this.bccs);
      }

      if (!this.replyTo.isEmpty()) {
         var3.setReplyTo(new Address[]{new InternetAddress(this.replyTo)});
      }

      var3.setSentDate(new Date());
      var3.setSubject(this.subject);
      if (var2) {
         var3.setText(this.text, "UTF-8", this.getMimeSubType());
      }

      return var3;
   }

   private String getMimeSubType() {
      return this.isHtml() ? "html" : "plain";
   }

   private boolean isHtml() {
      return this.text.toLowerCase().startsWith("<html>");
   }
}
