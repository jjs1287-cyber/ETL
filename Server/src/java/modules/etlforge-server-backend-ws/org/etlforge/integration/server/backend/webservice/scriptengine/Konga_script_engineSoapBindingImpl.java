package org.etlforge.integration.server.backend.webservice.scriptengine;

import java.rmi.RemoteException;
import org.etlforge.integration.server.ws.WsUtils;

public final class Konga_script_engineSoapBindingImpl implements ScriptEngine {
   private final ScriptEngineWorker worker = new ScriptEngineWorker();

   @Override
   public Result runGroovyScript(String var1, String var2, String var3, Binding[] var4) throws RemoteException {
      if ("anonymous".equals(var1)) {
         throw new RemoteException("The anonymous user is not allowed to execute scripts.");
      }

      WsUtils.authenticate(var1, var2);
      return this.evaluateGroovyScript(var3, var4);
   }

   private Result evaluateGroovyScript(String var1, Binding[] var2) throws RemoteException {
      try {
         return this.worker.runScript(var1, var2);
      } catch (Exception var4) {
         throw new RemoteException(var4.getMessage(), var4);
      }
   }
}
