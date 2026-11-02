package org.etlforge.integration.data.entity.script;

import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.script.de.GlobalDataElements;

public final class ScriptVariables {
   private final GlobalDataElements des;

   public ScriptVariables(GlobalDataElements var1) {
      this.des = var1;
   }

   public void scanScript(Script var1) {
      String var2 = var1.getScript();
      this.des.parseExpression(var1, var2);
   }
}
