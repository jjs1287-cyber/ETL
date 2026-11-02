package com.etlforge.integration.server.implementation.webservice.bulkload;

import com.google.common.base.Strings;
import com.etlforge.integration.server.engine.bulkload.BulkLoadEngineLog;
import com.etlforge.integration.server.engine.bulkload.BulkLoadException;
import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import com.etlforge.integration.server.engine.bulkload.BulkLoadResult;
import com.etlforge.integration.server.engine.bulkload.BulkLoadSession;
import com.etlforge.integration.server.engine.bulkload.BulkLoadSessionCredentials;
import com.etlforge.integration.server.engine.bulkload.BulkLoadSessionId;
import com.etlforge.integration.server.engine.bulkload.batch.BatchProcessConfig;
import java.rmi.RemoteException;
import org.etlforge.integration.common.server.ServerErrors;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.conf.ServerConfigListener;
import org.etlforge.integration.server.ws.WsUtils;

public class Konga_bulkload_engineSoapBindingImpl implements BulkLoadEngine {
   private static com.etlforge.integration.server.engine.bulkload.BulkLoadEngine engine;

   private static synchronized com.etlforge.integration.server.engine.bulkload.BulkLoadEngine getEngine() {
      if (engine == null) {
         createEngine();
      }

      return engine;
   }

   private static void createEngine() {
      engine = new com.etlforge.integration.server.engine.bulkload.BulkLoadEngine(BulkLoadEngineLog.LOG);
      ServerConfig.addConfigurationListener(new ServerConfigListener() {
         @Override
         public void configReloaded() {
            BulkLoadEngineLog.updateLogLevel();
            Konga_bulkload_engineSoapBindingImpl.engine.configureTimeout();
         }
      });
   }

   @Override
   public WsStartNewBulkLoadSessionResult startNewBulkLoadSession(String var1, String var2) throws RemoteException {
      try {
         com.etlforge.integration.server.engine.bulkload.BulkLoadEngine var3 = getEngine();
         BulkLoadSession var4 = var3.newSession(var1, var2);
         return new WsStartNewBulkLoadSessionResult(var4.getId().toString(), createForSuccess());
      } catch (Exception var5) {
         return new WsStartNewBulkLoadSessionResult(null, createForException(-1, var5, "startNewSession"));
      }
   }

   @Override
   public WsBulkLoadError closeBulkLoadSession(WsBulkLoadSessionId var1) throws RemoteException {
      try {
         com.etlforge.integration.server.engine.bulkload.BulkLoadEngine var2 = getEngine();
         var2.closeSession(new BulkLoadSessionCredentials(new BulkLoadSessionId(var1.getSessionGuid()), var1.getEtlforgeUser(), var1.getMd5Pwd()));
         return createForSuccess();
      } catch (Exception var3) {
         return createForException(-1, var3, "closeSession");
      }
   }

   @Override
   public WsBulkLoadResult runBulkLoad(WsBulkLoadSessionId var1, WsBulkLoadParams var2, WsSalesforceEndpoint var3, WsKeyValue[] var4) throws RemoteException {
      try {
         BulkLoadInputReader var5 = new BulkLoadInputReader(var1, var2, var3, var4);
         BulkLoadParams var12 = var5.getBulkLoadParams();
         BulkLoadSessionCredentials var7 = var5.getSessionCredentials();
         BatchProcessConfig var8 = var5.getBatchProcessConfig();
         com.etlforge.integration.server.engine.bulkload.BulkLoadEngine var9 = getEngine();
         BulkLoadResult var10 = var9.runBulkLoad(var7, var12, var8);
         return createResult(var10);
      } catch (Exception var11) {
         WsBulkLoadError var6 = createForException(-1, var11, "runBulkLoad");
         return new WsBulkLoadResult(false, var6, null);
      }
   }

   private static WsBulkLoadResult createResult(BulkLoadResult var0) {
      WsBulkLoadResult var1 = new WsBulkLoadResult();
      var1.setAllBatchesWereSent(var0.allBatchesSent);
      var1.setLogFile(var0.logFilePath);
      BulkLoadException var2 = var0.fatalError;
      if (var2 != null) {
         var1.setError(createForException(var2.getErrorCode(), var2, "runBulkLoad"));
      } else {
         var1.setError(createForSuccess());
      }

      return var1;
   }

   private static WsBulkLoadError createForSuccess(String var0) {
      return new WsBulkLoadError(0, "", Strings.nullToEmpty(var0));
   }

   private static WsBulkLoadError createForSuccess() {
      return createForSuccess(null);
   }

   private static WsBulkLoadError createForException(int var0, Exception var1, String var2) {
      try {
         var1.printStackTrace();
         StringBuilder var3 = new StringBuilder(WsUtils.escapeExceptionMessageAndGetCause(var1));
         ServerErrors.appendStacktrace(var1, var3);
         BulkLoadEngineLog.LOG.throwing(Konga_bulkload_engineSoapBindingImpl.class.getName(), var2, var1);
         return 0 == var0 ? new WsBulkLoadError(var0, "", var1.getMessage()) : new WsBulkLoadError(var0, var3.toString(), "");
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         return new WsBulkLoadError(-2, var1.getMessage(), "");
      }
   }
}
