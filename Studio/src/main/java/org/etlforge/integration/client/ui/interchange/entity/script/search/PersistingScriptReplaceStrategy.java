package org.etlforge.integration.client.ui.interchange.entity.script.search;

import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.integration.data.persist.EntityPersistor;

public final class PersistingScriptReplaceStrategy implements ScriptReplaceStrategy {
   private final EntityPersistor persistor;
   private final ExpressionTranslator translator;

   public PersistingScriptReplaceStrategy(EntityPersistor var1, ExpressionTranslator var2) {
      this.persistor = var1;
      this.translator = var2;
   }

   @Override
   public boolean handleReplace(Script var1, String var2) {
      PersistingScriptReplaceStrategy.Saver var3 = new PersistingScriptReplaceStrategy.Saver(var1, var2);
      var3.execute();
      return true;
   }

   private class Saver extends KongaSwingWorker<Void, Void> {
      private final Script script;
      private final String replacedExpression;

      public Saver(Script var2, String var3) {
         this.script = var2;
         this.replacedExpression = var3;
      }

      protected Void doInBackground() throws Exception {
         String var1 = PersistingScriptReplaceStrategy.this.translator.fromDisplay(this.replacedExpression);
         this.script.setScript(var1);
         PersistingScriptReplaceStrategy.this.persistor.save(this.script);
         return null;
      }
   }
}
