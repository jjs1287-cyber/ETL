package org.etlforge.integration.data.entity.script;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.Script;

public final class ScriptReferenceReplacer {
   private final Script script;
   private final ReferenceReplacementService service;
   private final List<ScriptedLink> originalLinks;
   private final ExpressionReferenceReplacer replacer;

   public ScriptReferenceReplacer(Script var1, ReferenceReplacementService var2, List<ScriptedLink> var3) {
      this.script = var1;
      this.service = var2;
      this.originalLinks = var3;
      this.replacer = new ExpressionReferenceReplacer(var1);
   }

   public boolean replaceReferences() {
      String var1 = this.script.getScript();
      String var2 = var1;

      for (ScriptedLink var4 : this.originalLinks) {
         var2 = this.replaceLink(var2, var4);
      }

      if (var1.equals(var2)) {
         return false;
      }

      this.script.setScript(var2);
      return true;
   }

   private String replaceLink(String var1, ScriptedLink var2) {
      IntegrationEntity var3 = var2.getMainObject();
      IntegrationEntity var4 = this.service.getReplacement(var3.getID());
      return var4 != null ? this.replace(var1, var3, var4) : var1;
   }

   private String replace(String var1, IntegrationEntity var2, IntegrationEntity var3) {
      return this.replacer.replace(var1, var2, var3);
   }
}
