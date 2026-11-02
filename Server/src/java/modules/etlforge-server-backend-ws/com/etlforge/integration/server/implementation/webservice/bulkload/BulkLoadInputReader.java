package com.etlforge.integration.server.implementation.webservice.bulkload;

import com.google.common.collect.Maps;
import com.etlforge.integration.server.engine.bulkload.BulkLoadEndpoint;
import com.etlforge.integration.server.engine.bulkload.BulkLoadEngineConfig;
import com.etlforge.integration.server.engine.bulkload.BulkLoadEngineLog;
import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import com.etlforge.integration.server.engine.bulkload.BulkLoadSessionCredentials;
import com.etlforge.integration.server.engine.bulkload.BulkLoadSessionId;
import com.etlforge.integration.server.engine.bulkload.DefaultBulkLoadEndpoint;
import com.etlforge.integration.server.engine.bulkload.batch.BatchProcessConfig;
import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.etlforge.integration.salesforce.SalesforceVersion;

final class BulkLoadInputReader {
   private final WsBulkLoadSessionId wsSessionId;
   private final WsBulkLoadParams wsParams;
   private final WsSalesforceEndpoint wsEndpoint;
   private final Map<String, String> keyValues = Maps.newHashMap();

   public BulkLoadInputReader(WsBulkLoadSessionId var1, WsBulkLoadParams var2, WsSalesforceEndpoint var3, WsKeyValue[] var4) throws Exception {
      validateInput(var2, var3);
      this.wsSessionId = var1;
      this.wsParams = var2;
      this.wsEndpoint = var3;

      for (WsKeyValue var8 : var4) {
         this.keyValues.put(var8.getKey(), var8.getValue());
      }

      this.logInputParams();
   }

   private static void validateInput(WsBulkLoadParams var0, WsSalesforceEndpoint var1) throws Exception {
      validateEndpoint(var1);
      if (StringUtils.isEmpty(var0.getQueryString())) {
         validateInputForLoad(var0);
      } else {
         validateInputForQuery(var0);
      }
   }

   private static void validateEndpoint(WsSalesforceEndpoint var0) throws Exception {
      if (StringUtils.isEmpty(var0.getUrl())) {
         throw new Exception("No salesforce URL given.");
      }

      if (StringUtils.isEmpty(var0.getSessionId())) {
         throw new Exception("No salesforce session ID given.");
      }
   }

   private static void validateInputForLoad(WsBulkLoadParams var0) throws Exception {
      if (StringUtils.isEmpty(var0.getSourceFilePath())) {
         throw new Exception("No source file path given.");
      }

      File var1 = new File(var0.getSourceFilePath());
      if (!var1.canRead()) {
         throw new Exception("The source file cannot be read. Make sure it exists and has the proper permissions. (Path = " + var0.getSourceFilePath() + ")");
      }

      if (var1.length() == 0L) {
         throw new Exception("The source file is empty. (Path = " + var0.getSourceFilePath() + ")");
      }
   }

   private static void validateInputForQuery(WsBulkLoadParams var0) throws Exception {
      if (StringUtils.isEmpty(var0.getTargetFilePath())) {
         throw new Exception("No target file path given.");
      }

      File var1 = new File(var0.getTargetFilePath());
      if (var1.exists()) {
         throw new Exception("The target file already exists. (Path = " + var0.getTargetFilePath() + ")");
      }
   }

   private void logInputParams() {
      Logger var1 = BulkLoadEngineLog.LOG;
      StringBuilder var2 = new StringBuilder();
      var2.append("Session ID = ").append(this.wsSessionId.getSessionGuid()).append(SystemUtils.LINE_SEPARATOR);
      var2.append("ETLForge user = ").append(this.wsSessionId.getEtlforgeUser()).append(SystemUtils.LINE_SEPARATOR);
      var2.append("Salesforce Session ID = " + this.wsEndpoint.getSessionId());
      var2.append("Salesforce Service URL = " + this.wsEndpoint.getUrl());
      var2.append("Salesforce Version = " + this.wsParams.getSalesforceVersion());
      var2.append("Bulk Load ID = ").append(this.wsParams.getBulkLoadId()).append(SystemUtils.LINE_SEPARATOR);
      var2.append("Operation instance ID = ").append(this.wsParams.getOperationInstanceId()).append(SystemUtils.LINE_SEPARATOR);
      var2.append("Last operation instance ID = ").append(this.wsParams.getLastOperationInstanceId()).append(SystemUtils.LINE_SEPARATOR);
      var2.append("Source file = ").append(this.wsParams.getSourceFilePath()).append(SystemUtils.LINE_SEPARATOR);
      var2.append("Target file = ").append(this.wsParams.getTargetFilePath()).append(SystemUtils.LINE_SEPARATOR);
      var2.append("Query = ").append(this.wsParams.getQueryString()).append(SystemUtils.LINE_SEPARATOR);

      for (Entry var4 : this.keyValues.entrySet()) {
         var2.append((String)var4.getKey()).append(" = ").append((String)var4.getValue()).append(SystemUtils.LINE_SEPARATOR);
      }

      var1.fine(var2.toString());
   }

   public BulkLoadSessionCredentials getSessionCredentials() {
      return new BulkLoadSessionCredentials(
         new BulkLoadSessionId(this.wsSessionId.getSessionGuid()), this.wsSessionId.getEtlforgeUser(), this.wsSessionId.getMd5Pwd()
      );
   }

   public BulkLoadParams getBulkLoadParams() {
      BulkLoadParams var1 = new BulkLoadParams();
      var1.setBulkloadId(this.wsParams.getBulkLoadId());
      var1.setOperationInstance(this.wsParams.getOperationInstanceId());
      var1.setLastOperationInstance(this.wsParams.getLastOperationInstanceId());
      String var2 = this.wsParams.getQueryString();
      if (StringUtils.isEmpty(var2)) {
         var1.setSourceFile(new File(this.wsParams.getSourceFilePath()));
      } else {
         var1.setTargetFile(new File(this.wsParams.getTargetFilePath()));
         var1.setQueryString(var2);
      }

      var1.setEndpoint(this.getEndpoint());
      return var1;
   }

   private BulkLoadEndpoint getEndpoint() {
      try {
         BulkLoadEndpoint var1 = BulkLoadEngineConfig.getEndpointOverride();
         if (var1 != null) {
            return var1;
         }
      } catch (Exception var2) {
         BulkLoadEngineLog.LOG.info("A non-fatal problem was encountered in the server config:\n" + ExceptionUtils.getFullStackTrace(var2));
      }

      SalesforceVersion var3 = SalesforceVersion.fromId(this.wsParams.getSalesforceVersion());
      return new DefaultBulkLoadEndpoint(this.wsEndpoint.getUrl(), this.wsEndpoint.getSessionId(), var3);
   }

   public BatchProcessConfig getBatchProcessConfig() {
      return BatchProcessConfig.DEFAULT;
   }
}
