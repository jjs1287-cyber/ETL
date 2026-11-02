package org.etlforge.integration.client.ui.script.builder.plugin;

import java.text.BreakIterator;
import javax.swing.Icon;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;
import org.etlforge.util.Interval;

final class PluginCompletionToken implements CompletionToken {
   private final PipelinePluginManifest manifest;
   private final DefaultCompletionToken impl;

   public PluginCompletionToken(PipelinePluginManifest var1, int var2) {
      this.manifest = var1;
      this.impl = new DefaultCompletionToken(ScriptConstants.getPluginTagString(var1), var2, var2);
      this.impl.setIcon(ClientIcons.PLUGIN_16);
   }

   @Override
   public void complete(Document var1) {
      this.impl.complete(var1);
   }

   @Override
   public String getDisplayValue() {
      return this.manifest.getDisplayName();
   }

   @Override
   public Icon getIcon() {
      return this.impl.getIcon();
   }

   @Override
   public String getInfo() {
      String var1 = this.manifest.getDescription();
      if (StringUtils.isBlank(var1)) {
         return null;
      }

      BreakIterator var2 = BreakIterator.getSentenceInstance();
      var2.setText(var1);
      int var3 = var2.next();
      return var3 == -1 ? null : var1.substring(0, var3);
   }

   @Override
   public Interval getInterval(Document var1) {
      return this.impl.getInterval(var1);
   }
}
