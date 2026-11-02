package com.etlforge.integration.server.email;

import java.io.IOException;
import java.util.ArrayList;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;

public class EmailClient {
   public void send(MailServerSettings var1, MessageSettings var2) throws EmailClientException {
      this.sendWithAttachments(var1, var2, null);
   }

   public void sendWithAttachments(MailServerSettings var1, MessageSettings var2, Iterable<EmailAttachment> var3) throws EmailClientException {
      Session var4 = var1.createSession();

      Message var5;
      try {
         if (var3 != null) {
            var5 = var2.createMessage(var4, var3);
         } else {
            var5 = var2.createMessage(var4);
         }
      } catch (MessagingException var22) {
         throw new EmailClientException("Failed to create message. " + var22.getMessage(), var22);
      } catch (IOException var23) {
         throw new EmailClientException("Failed to create message with attachment. I/O error when reading attachment. " + var23.getMessage(), var23);
      }

      Transport var6 = null;
      String var7 = "unknown";

      try {
         Address[] var8 = var5.getAllRecipients();
         if (var8 == null || var8.length == 0) {
            throw new EmailClientException("No valid recipients specified.");
         }

         var7 = var1.getProtocol();
         var6 = var4.getTransport(var7);
         var6.connect();
         var6.sendMessage(var5, var8);
      } catch (NoSuchProviderException var24) {
         throw new EmailClientException("Internal error: No provider for protocol " + var7, var24);
      } catch (MessagingException var25) {
         String var9 = var25.getMessage();
         Throwable var10 = var25.getCause();
         if (var10 != null && var9 != null) {
            String var11 = var10.getMessage();
            if (var11 != null) {
               var9 = var9 + " " + var11;
            }
         }

         throw new EmailClientException(var9, var25);
      } finally {
         if (var6 != null) {
            try {
               var6.close();
            } catch (Exception var21) {
               var21.printStackTrace();
            }
         }
      }
   }

   public static void main_test(String[] var0) {
      if (var0 != null && var0.length >= 2) {
         String var1 = var0[0];
         String var2 = var0[1];
         String var3 = null;
         if (var0.length > 2) {
            var3 = var0[2];
         }

         String var4 = null;
         if (var0.length > 3) {
            var4 = var0[3];
         }

         byte var5 = -1;
         boolean var6 = true;
         StringBuilder var7 = new StringBuilder("<html><h2>Header type 3</h2>");
         var7.append("<p>paragraph.</p>\n");
         var7.append("<p>Another paragraph.</br></br>\n");
         var7.append("<code>Some code perhaps?</code>\n");
         var7.append("Then we finish this paragraph.</p>\n");
         String var8 = "emanuel.norrbin@etlforge.invalid";
         String var9 = "emanuel_norrbin@yahoo.se";
         Object var10 = null;
         Object var11 = null;
         Object var12 = null;
         String var13 = "Test EmailClient";
         String var14 = var7.toString();
         MessageSettings var15 = new MessageSettings(var8, var9, var13, var14, (String)var10, (String)var11, (String)var12);
         MailServerSettings var16 = new MailServerSettings("smtp.googlemail.com", var1, var2, var5, var6);
         EmailClient var17 = new EmailClient();

         try {
            if (var3 != null) {
               EmailAttachment var18 = new EmailAttachment(var3, var4);
               ArrayList var19 = new ArrayList();
               var19.add(var18);
               var17.sendWithAttachments(var16, var15, var19);
            } else {
               var17.send(var16, var15);
            }
         } catch (EmailClientException var20) {
            var20.printStackTrace();
            return;
         }

         System.out.println("The message was sent successfully.");
      } else {
         throw new IllegalArgumentException("Please specify user name and password as first and second arguments!");
      }
   }
}
