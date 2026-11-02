package org.etlforge.integration.server.backend.webservice.json;

import com.google.common.base.Strings;
import java.io.File;
import java.rmi.RemoteException;
import org.etlforge.integration.common.server.ServerErrors;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.conf.ServerConfigListener;
import org.etlforge.integration.server.engine.json.ConversionParams;
import org.etlforge.integration.server.engine.json.JsonEngineConfig;
import org.etlforge.integration.server.engine.json.JsonSession;
import org.etlforge.integration.server.engine.json.JsonSessionCredentials;
import org.etlforge.integration.server.engine.json.JsonSessionId;
import org.etlforge.integration.server.ws.WsUtils;

public final class Konga_json_engineSoapBindingImpl implements JsonEngine {
   private static org.etlforge.integration.server.engine.json.JsonEngine engine;

   private static synchronized org.etlforge.integration.server.engine.json.JsonEngine getEngine() {
      if (engine == null) {
         engine = new org.etlforge.integration.server.engine.json.JsonEngine(JsonEngineConfig.LOG);
         ServerConfig.addConfigurationListener(new ServerConfigListener() {
            @Override
            public void configReloaded() {
               JsonEngineConfig.updateLogLevel();
               Konga_json_engineSoapBindingImpl.engine.configureTimeout();
            }
         });
      }

      return engine;
   }

   @Override
   public WsStartNewJsonSessionResult startNewJsonSession(String var1, String var2) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.json.JsonEngine var3 = getEngine();
         JsonSession var4 = var3.newSession(var1, var2);
         return new WsStartNewJsonSessionResult(var4.getId().toString(), createForSuccess());
      } catch (Throwable var5) {
         return new WsStartNewJsonSessionResult(null, this.createForException(-1, var5, "startNewSession"));
      }
   }

   private static WsJsonError createForSuccess(String var0) {
      return new WsJsonError(0, "", Strings.nullToEmpty(var0));
   }

   private static WsJsonError createForSuccess() {
      return createForSuccess(null);
   }

   private WsJsonError createForException(int var1, Throwable var2, String var3) {
      try {
         var2.printStackTrace();
         StringBuilder var4 = new StringBuilder(WsUtils.escapeExceptionMessageAndGetCause(var2));
         ServerErrors.appendStacktrace(var2, var4);
         JsonEngineConfig.LOG.throwing(this.getClass().getName(), var3, var2);
         return new WsJsonError(var1, var4.toString(), "");
      } catch (Throwable var5) {
         var5.printStackTrace();
         return new WsJsonError(-2, var2.getMessage(), "");
      }
   }

   @Override
   public WsJsonError jsonToXml(WsJsonSessionId var1, WsConversionParams var2) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.json.JsonEngine var3 = getEngine();
         JsonSessionCredentials var4 = createSessionCredentials(var1);
         ConversionParams var5 = createConversionParams(var2);
         var3.jsonToXml(var4, var5);
         return createForSuccess();
      } catch (Throwable var6) {
         return this.createForException(-1, var6, "jsonToXml");
      }
   }

   private static JsonSessionCredentials createSessionCredentials(WsJsonSessionId var0) {
      return new JsonSessionCredentials(new JsonSessionId(var0.getSessionGuid()), var0.getEtlforgeUser(), var0.getMd5Pwd());
   }

   private static ConversionParams createConversionParams(WsConversionParams var0) {
      return new ConversionParams(new File(var0.getJsonFilePath()), new File(var0.getXmlFilePath()), var0.getTransformationId());
   }

   @Override
   public WsJsonError xmlToJson(WsJsonSessionId var1, WsConversionParams var2) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.json.JsonEngine var3 = getEngine();
         JsonSessionCredentials var4 = createSessionCredentials(var1);
         ConversionParams var5 = createConversionParams(var2);
         var3.xmlToJson(var4, var5);
         return createForSuccess();
      } catch (Throwable var6) {
         return this.createForException(-1, var6, "jsonToXml");
      }
   }

   @Override
   public WsJsonError closeJsonSession(WsJsonSessionId var1) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.json.JsonEngine var2 = getEngine();
         JsonSessionCredentials var3 = createSessionCredentials(var1);
         var2.closeSession(var3);
         return createForSuccess();
      } catch (Throwable var4) {
         return this.createForException(-1, var4, "closeSession");
      }
   }
}
