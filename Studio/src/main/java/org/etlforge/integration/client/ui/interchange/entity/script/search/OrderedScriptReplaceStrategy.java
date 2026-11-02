package org.etlforge.integration.client.ui.interchange.entity.script.search;

import org.etlforge.integration.data.entity.Script;

public class OrderedScriptReplaceStrategy implements ScriptReplaceStrategy {
   private final ScriptReplaceStrategy[] order;

   public OrderedScriptReplaceStrategy(ScriptReplaceStrategy... var1) {
      this.order = (ScriptReplaceStrategy[])var1.clone();
   }

   @Override
   public boolean handleReplace(Script var1, String var2) {
      for (ScriptReplaceStrategy var6 : this.order) {
         if (var6.handleReplace(var1, var2)) {
            return true;
         }
      }

      return false;
   }
}
