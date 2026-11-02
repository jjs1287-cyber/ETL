package org.etlforge.integration.server.engine.function;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.etlforge.integration.server.email.EmailAttachment;
import com.etlforge.integration.server.email.EmailClient;
import com.etlforge.integration.server.email.EmailClientException;
import com.etlforge.integration.server.email.MailServerSettings;
import com.etlforge.integration.server.email.MessageSettings;
import com.etlforge.integration.server.sfdc.GetServerTimestampInput;
import com.etlforge.integration.server.sfdc.ServerTimestampFetcher;
import com.etlforge.integration.server.sfdc.dataloader.DateTransform;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.server.conf.ServerProxySettings;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.system.ArgCheck;

public final class FunctionEngine {
   private final Logger logger;
   private final Map<DateConversionParams, DateTransform> dataLoaderDateTransforms = Maps.newHashMap();
   private final Object lock = new Object();

   public FunctionEngine(Logger var1) {
      this.logger = var1;
   }

   public String getSalesforceServerTimestamp(GetServerTimestampInput var1) throws FunctionEvaluationException {
      this.logger.fine("getSalesforceServerTimestamp(" + var1 + ")");

      try {
         ServerTimestampFetcher var2 = new ServerTimestampFetcher();
         Supplier var3 = ServerProxySettings.getSupplier();
         var2.setProxySettingsSupplier(var3);
         return var2.getServerTimestamp(var1);
      } catch (Exception var4) {
         throw new FunctionEvaluationException(var4);
      }
   }

   public String fromDataLoaderDate(String var1, DateConversionParams var2) throws FunctionEvaluationException {
      this.logDataLoaderDateConversion("fromDataLoaderDate", var1, var2);
      synchronized (this.lock) {
         String var10000;
         try {
            DateTransform var4 = this.getDataLoaderDateTransform(var2);
            var10000 = var4.convertDate(var1);
         } catch (ParseException var6) {
            throw new FunctionEvaluationException(var6);
         }

         return var10000;
      }
   }

   private void logDataLoaderDateConversion(String var1, String var2, DateConversionParams var3) {
      if (this.logger.isLoggable(Level.FINE)) {
         this.logger.fine(var1 + " called with input: " + var2 + ", params: " + var3);
      }
   }

   private DateTransform getDataLoaderDateTransform(DateConversionParams var1) {
      DateTransform var2 = this.dataLoaderDateTransforms.get(var1);
      if (var2 == null) {
         var2 = new DateTransform(var1);
         this.dataLoaderDateTransforms.put(var1, var2);
      }

      return var2;
   }

   public String fromDataLoaderDateTime(String var1, DateConversionParams var2) throws FunctionEvaluationException {
      this.logDataLoaderDateConversion("fromDataLoaderDateTime", var1, var2);
      synchronized (this.lock) {
         String var10000;
         try {
            DateTransform var4 = this.getDataLoaderDateTransform(var2);
            var10000 = var4.convertDateTime(var1);
         } catch (ParseException var6) {
            throw new FunctionEvaluationException(var6);
         }

         return var10000;
      }
   }

   public String convertTimeZone(ConvertTimeZoneParams var1) throws FunctionEvaluationException {
      synchronized (this.lock) {
         String var10000;
         try {
            ConvertTimeZone var3 = new ConvertTimeZone();
            var10000 = var3.Exec(var1);
         } catch (Exception var5) {
            throw new FunctionEvaluationException(var5);
         }

         return var10000;
      }
   }

   public void sendEmail(
      String var1,
      String var2,
      String var3,
      String var4,
      String var5,
      String var6,
      String var7,
      String var8,
      String var9,
      String var10,
      boolean var11,
      List<String> var12,
      List<String> var13
   ) throws FunctionEvaluationException {
      ArgCheck.checkNotEmpty(var8, "No SMTP servers specified.");
      Preconditions.checkArgument(
         var12 == null || var12.isEmpty() || var13 == null || var13.isEmpty() || var12.size() == var13.size(), "Inconsistent attachment arguments."
      );
      String[] var14 = StringUtils.split(var8.trim(), ",");
      if (var14.length == 0) {
         throw new FunctionEvaluationException("Invalid SMTP server specification: " + var8);
      }

      StringBuilder var15 = new StringBuilder();
      EmailClientException var16 = null;

      for (String var20 : var14) {
         var20 = var20.trim();
         String[] var21 = KongaStringUtils.splitInTwo(var20, ":");
         String var22 = var21[0].trim();
         String var23 = var21[1].trim();
         if (!var23.isEmpty()) {
            var23 = var23.substring(1).trim();
         }

         int var24 = -1;
         if (!var23.isEmpty()) {
            try {
               var24 = Integer.parseInt(var23);
            } catch (NumberFormatException var31) {
               if (var15.length() > 0) {
                  var15.append("\n\n");
               }

               var15.append("Invalid SMTP server specificiation: " + var20 + ". " + var31.getMessage());
               continue;
            }
         }

         ArrayList var25 = null;
         if (var12 != null && !var12.isEmpty()) {
            var25 = Lists.newArrayList();
            int var26 = 0;

            for (String var28 : var12) {
               String var29 = var13 != null && !var13.isEmpty() ? (String)var13.get(var26) : null;
               var25.add(new EmailAttachment(var28, var29));
               var26++;
            }
         }

         MailServerSettings var33 = new MailServerSettings(var22, var1, var2, var24, var11);
         MessageSettings var34 = new MessageSettings(var5, var10, var9, var6, var4, var3, var7);

         try {
            EmailClient var35 = new EmailClient();
            if (var25 != null && !var25.isEmpty()) {
               var35.sendWithAttachments(var33, var34, var25);
            } else {
               var35.send(var33, var34);
            }

            return;
         } catch (EmailClientException var30) {
            if (var15.length() > 0) {
               var15.append("\n\n");
            }

            var15.append(var30.getMessage());
            var16 = var30;
         }
      }

      if (var15.length() == 0) {
         throw new FunctionEvaluationException("Sending the message failed but no error was reported.");
      } else {
         throw new FunctionEvaluationException(var15.toString(), var16);
      }
   }
}
