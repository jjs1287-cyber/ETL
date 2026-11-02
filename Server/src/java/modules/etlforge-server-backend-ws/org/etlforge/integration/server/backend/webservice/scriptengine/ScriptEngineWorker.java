package org.etlforge.integration.server.backend.webservice.scriptengine;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.rmi.RemoteException;
import javax.script.Bindings;
import javax.script.ScriptEngineManager;
import org.etlforge.io.KongaIoUtils;

public class ScriptEngineWorker {
   private final ScriptEngineManager manager = new ScriptEngineManager();

   public Result runScript(String var1, Binding[] var2) throws Exception {
      Reader var3 = this.openScript(var1);

      try {
         javax.script.ScriptEngine var4 = this.getScriptEngine();
         Bindings var5 = BindingsConverter.convert(var2);
         Object var6 = var4.eval(var3, var5);
         return this.convertToResult(var6);
      } finally {
         KongaIoUtils.close(var3);
      }
   }

   private Reader openScript(String var1) throws Exception {
      InputStream var2 = this.getClass().getClassLoader().getResourceAsStream(var1);
      if (var2 == null) {
         throw new Exception("The script \"" + var1 + "\" was not found.");
      } else {
         return new InputStreamReader(var2, java.nio.charset.StandardCharsets.UTF_8);
      }
   }

   private javax.script.ScriptEngine getScriptEngine() throws Exception {
      try {
         synchronized (this) {
            return this.manager.getEngineByName("groovy");
         }
      } catch (Exception var4) {
         throw new RemoteException("The etlforge server is not configured to run groovy scripts.", var4);
      }
   }

   private Result convertToResult(Object var1) {
      if (var1 == null) {
         return null;
      }

      String var2 = var1.getClass().getName();
      VariableType var3 = VariableType.fromClassName(var2);
      return new Result(var3.name(), var1.toString());
   }
}
