package org.etlforge.application.ui.groovy;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.SwingWorker;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.ui.Enablable;

final class Executor {
   private ScriptEngine scriptEngine;
   private final Object lock = new Object();
   private Enablable callback;

   public void execute(String var1) {
      if (!var1.isEmpty()) {
         this.setCallbackState(false);
         new Executor.Worker(var1).execute();
      }
   }

   public void setCallback(Enablable var1) {
      synchronized (this.lock) {
         this.callback = var1;
      }
   }

   private void setCallbackState(boolean var1) {
      synchronized (this.lock) {
         if (this.callback != null) {
            this.callback.setEnabled(var1);
         }
      }
   }

   private final class Worker extends SwingWorker<Void, Void> {
      private final String script;

      public Worker(String var2) {
         this.script = var2;
      }

      protected Void doInBackground() throws Exception {
         ScriptEngine var1 = null;

         try {
            var1 = this.getScriptEngine();
            if (var1 != null) {
               this.evaluateScript(var1);
            }
         } catch (Exception var3) {
            ErrorLog.attention(GroovyConsole.class, var3.getMessage(), var3);
         }

         return null;
      }

      private ScriptEngine getScriptEngine() throws Exception {
         synchronized (Executor.this.lock) {
            if (Executor.this.scriptEngine == null) {
               ScriptEngineManager var2 = new ScriptEngineManager();
               Executor.this.scriptEngine = var2.getEngineByName("groovy");
               if (Executor.this.scriptEngine == null) {
                  throw new Exception("The application has not been configured to use the groovy console: A groovy script engine is not available.");
               }
            }

            return Executor.this.scriptEngine;
         }
      }

      private void evaluateScript(ScriptEngine var1) {
         try {
            var1.eval(this.script);
         } catch (ScriptException var3) {
            NonBlockingAlert.error(var3.getMessage(), "Script Execution Error");
         }
      }

      @Override
      protected void done() {
         Executor.this.setCallbackState(true);
      }
   }
}
