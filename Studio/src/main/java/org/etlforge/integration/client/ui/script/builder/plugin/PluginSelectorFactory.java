package org.etlforge.integration.client.ui.script.builder.plugin;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderModel;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSection;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSectionFactory;

public final class PluginSelectorFactory implements ScriptBuilderSectionFactory {
   private final ScriptPluginProvider pluginProvider;

   public PluginSelectorFactory(ScriptPluginProvider var1) {
      this.pluginProvider = Preconditions.checkNotNull(var1);
   }

   @Override
   public ScriptBuilderSection newSection(ScriptBuilderModel var1) {
      ScriptPluginTree var2 = new ScriptPluginTree(this.pluginProvider);
      return new ScriptPluginTreePanel(var2);
   }
}
