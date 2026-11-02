package org.etlforge.integration.client.ui.interchange.entity.script;

import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.util.string.StringLimiter;

public final class ScriptToolTip extends IntegrationEntityToolTip {
   public ScriptToolTip(Script var1) {
      super(var1);
      this.addEntry("Script:", this.getScriptEntry(var1));
   }

   private String getScriptEntry(Script var1) {
      String var2 = this.getStrippedScript(var1);
      return var2.isEmpty() ? "<i>[empty]</i>" : new StringLimiter(var2).getLimitedString(100);
   }

   private String getStrippedScript(Script var1) {
      String var2 = KongaStringUtils.removeStartAndEnd(var1.getScript().trim(), "<trans>", "</trans>");
      return StringEscapeUtils.escapeHtml(var2.trim());
   }
}
