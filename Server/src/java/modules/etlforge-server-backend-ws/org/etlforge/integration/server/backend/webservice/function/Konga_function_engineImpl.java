package org.etlforge.integration.server.backend.webservice.function;

import com.google.common.base.Strings;
import com.etlforge.integration.server.sfdc.GetServerTimestampInput;
import java.util.Arrays;
import java.util.List;
import org.etlforge.integration.common.server.ServerErrors;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;
import org.etlforge.integration.server.engine.function.ConvertTimeZoneParams;
import org.etlforge.integration.server.engine.function.FunctionEngineConfig;
import org.etlforge.integration.server.engine.function.FunctionEvaluationException;
import org.etlforge.integration.server.ws.WsUtils;

public final class Konga_function_engineImpl {
   private static org.etlforge.integration.server.engine.function.FunctionEngine engine;

   public WsGetSalesforceServerTimeStampResult getSalesforceServerTimestamp(WsGetSalesforceServerTimeStampInput var1) {
      try {
         org.etlforge.integration.server.engine.function.FunctionEngine var2 = getEngine();
         GetServerTimestampInput var8 = new GetServerTimestampInput(var1.getServiceEndpoint(), var1.getSessionId(), var1.getTimeZone());
         String var4 = var2.getSalesforceServerTimestamp(var8);
         return new WsGetSalesforceServerTimeStampResult(var4, createForSuccess());
      } catch (FunctionEvaluationException var5) {
         WsFunctionError var7 = this.createFunctionEvaluationError(var5, "getSalesforceServerTimestamp");
         return new WsGetSalesforceServerTimeStampResult("", var7);
      } catch (Exception var6) {
         WsFunctionError var3 = this.createForException(-1, var6, "getSalesforceServerTimestamp");
         return new WsGetSalesforceServerTimeStampResult("", var3);
      }
   }

   public WsFromDataLoaderDateOutput fromDataLoaderDate(FromDataLoaderDateInput var1) {
      try {
         org.etlforge.integration.server.engine.function.FunctionEngine var2 = getEngine();
         String var9 = var1.getInputValue();
         DateConversionParams var4 = this.createDataLoaderDateConversionParams(var1);
         String var5 = var2.fromDataLoaderDate(var9, var4);
         return this.createDataLoaderDateConversionResult(var5);
      } catch (FunctionEvaluationException var6) {
         WsFunctionError var8 = this.createFunctionEvaluationError(var6, "fromDataLoaderDate");
         return new WsFromDataLoaderDateOutput("", "", var8);
      } catch (Exception var7) {
         WsFunctionError var3 = this.createForException(-1, var7, "fromDataLoaderDate");
         return new WsFromDataLoaderDateOutput("", "", var3);
      }
   }

   public WsFromDataLoaderDateOutput fromDataLoaderDateTime(FromDataLoaderDateInput var1) {
      try {
         org.etlforge.integration.server.engine.function.FunctionEngine var2 = getEngine();
         String var9 = var1.getInputValue();
         DateConversionParams var4 = this.createDataLoaderDateConversionParams(var1);
         String var5 = var2.fromDataLoaderDateTime(var9, var4);
         return this.createDataLoaderDateConversionResult(var5);
      } catch (FunctionEvaluationException var6) {
         WsFunctionError var8 = this.createFunctionEvaluationError(var6, "fromDataLoaderDate");
         return new WsFromDataLoaderDateOutput("", "", var8);
      } catch (Exception var7) {
         WsFunctionError var3 = this.createForException(-1, var7, "fromDataLoaderDate");
         return new WsFromDataLoaderDateOutput("", "", var3);
      }
   }

   public WsConvertTimeZoneOutput convertTimeZone(WsConvertTimeZoneInput var1) {
      try {
         org.etlforge.integration.server.engine.function.FunctionEngine var2 = getEngine();
         ConvertTimeZoneParams var8 = this.createConvertTimeZoneParams(var1);
         String var4 = var2.convertTimeZone(var8);
         return this.createWsConvertTimeZoneOutput(var4, "");
      } catch (FunctionEvaluationException var5) {
         WsFunctionError var7 = this.createFunctionEvaluationError(var5, "fromDataLoaderDate");
         return new WsConvertTimeZoneOutput("", "", var7);
      } catch (Exception var6) {
         WsFunctionError var3 = this.createForException(-1, var6, "fromDataLoaderDate");
         return new WsConvertTimeZoneOutput("", "", var3);
      }
   }

   public WsSendEmailOutput sendEmail(WsSendEmailInput var1) {
      try {
         org.etlforge.integration.server.engine.function.FunctionEngine var2 = getEngine();
         String var3 = var1.getAccount();
         String var4 = var1.getAccountPassword();
         String var5 = var1.getBCC();
         String var6 = var1.getCC();
         String var7 = var1.getFrom();
         String var8 = var1.getMessageText();
         String var9 = var1.getReplyTo();
         String var10 = var1.getSmtpMailServers();
         String var11 = var1.getSubject();
         String var12 = var1.getTo();
         boolean var13 = var1.getUseSSL() != 0;
         List var14 = null;
         List var15 = null;
         String[] var16 = var1.getAttachmentFilePaths();
         if (var16 != null && var16.length > 0) {
            var14 = Arrays.asList(var16);
            var16 = var1.getAttachmentNames();
            if (var16 != null && var16.length > 0) {
               var15 = Arrays.asList(var16);
            }
         }

         var2.sendEmail(var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15);
         return new WsSendEmailOutput(0, "");
      } catch (FunctionEvaluationException var17) {
         return this.createForException(4, var17);
      } catch (Exception var18) {
         return this.createForException(-1, var18);
      }
   }

   private static synchronized org.etlforge.integration.server.engine.function.FunctionEngine getEngine() {
      if (engine == null) {
         engine = new org.etlforge.integration.server.engine.function.FunctionEngine(FunctionEngineConfig.LOG);
      }

      return engine;
   }

   private static WsFunctionError createForSuccess(String var0) {
      return new WsFunctionError(0, "", Strings.nullToEmpty(var0));
   }

   private static WsFunctionError createForSuccess() {
      return createForSuccess(null);
   }

   private WsFunctionError createForException(int var1, Exception var2, String var3) {
      return createForException(var1, var2, this.getClass().getName(), var3);
   }

   static WsFunctionError createForException(int var0, Exception var1, String var2, String var3) {
      try {
         var1.printStackTrace();
         StringBuilder var4 = new StringBuilder(WsUtils.escapeExceptionMessageAndGetCause(var1));
         ServerErrors.appendStacktrace(var1, var4);
         FunctionEngineConfig.LOG.throwing(var2, var3, var1);
         return new WsFunctionError(var0, var4.toString(), "");
      } catch (RuntimeException var5) {
         var5.printStackTrace();
         return new WsFunctionError(-2, var1.getMessage(), "");
      }
   }

   private WsFunctionError createFunctionEvaluationError(Exception var1, String var2) {
      try {
         var1.printStackTrace();
         StringBuilder var3 = new StringBuilder(WsUtils.escapeExceptionMessageAndGetCause(var1));
         ServerErrors.appendStacktrace(var1, var3);
         FunctionEngineConfig.LOG.throwing(this.getClass().getName(), var2, var1);
         return new WsFunctionError(4, var1.getMessage(), "");
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         return new WsFunctionError(-2, var1.getMessage(), "");
      }
   }

   private DateConversionParams createDataLoaderDateConversionParams(FromDataLoaderDateInput var1) {
      TimeZoneId var2 = TimeZoneId.valueOf(var1.getTimeZoneId());
      boolean var3 = var1.isEuropeanFormat();
      return var3 ? DateConversionParams.europeanFormat(var2) : DateConversionParams.usFormat(var2);
   }

   private WsFromDataLoaderDateOutput createDataLoaderDateConversionResult(String var1) {
      String var2 = "";
      return new WsFromDataLoaderDateOutput(var1, var2, createForSuccess());
   }

   private ConvertTimeZoneParams createConvertTimeZoneParams(WsConvertTimeZoneInput var1) {
      return new ConvertTimeZoneParams(
         var1.getInputValue(),
         var1.getFromTimeZoneId(),
         var1.getToTimeZoneId(),
         var1.isFromIsEuropeanFormat(),
         var1.isToIsEuropeanFormat(),
         var1.isFromIsDst(),
         var1.isToIsDst()
      );
   }

   private WsConvertTimeZoneOutput createWsConvertTimeZoneOutput(String var1, String var2) {
      return new WsConvertTimeZoneOutput(var1, var2, createForSuccess());
   }

   private WsSendEmailOutput createForException(int var1, Exception var2) {
      var2.printStackTrace();
      return new WsSendEmailOutput(var1, var2.getMessage());
   }
}
