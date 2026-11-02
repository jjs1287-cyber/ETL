package org.etlforge.integration.client.ui.script.builder.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.plugin.ui.pipeline.PipelinePluginListDisplayer;
import org.etlforge.integration.client.ui.script.text.ArgumentSelectionDialog;
import org.etlforge.integration.client.ui.script.text.FunctionArgumentSuggestion;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.text.contentassist.CompletionToken;

final class PluginArgumentSuggestion implements FunctionArgumentSuggestion {
   private final ScriptPluginProvider pluginProvider;

   public PluginArgumentSuggestion(ScriptPluginProvider var1) {
      this.pluginProvider = var1;
   }

   @Override
   public String[] getFunctionNames() {
      return new String[]{"RunPlugin"};
   }

   @Override
   public CompletionToken[] getCompletionTokens(Function var1, int var2) {
      ArrayList var3 = Lists.newArrayList();

      for (PipelinePluginManifest var5 : this.pluginProvider.getAvailablePlugins()) {
         var3.add(new PluginCompletionToken(var5, var2));
      }

      return (CompletionToken[]) var3.toArray(new CompletionToken[var3.size()]);
   }

   @Override
   public void insertArgument(Function var1, JTextComponent var2) {
      PipelinePluginManifest var3 = this.selectPlugin(var2);
      if (var3 != null) {
         this.insertPluginTag(var2, var3);
      }
   }

   private PipelinePluginManifest selectPlugin(JTextComponent var1) {
      ImmutableList var2 = this.pluginProvider.getAvailablePlugins();
      return var2.isEmpty() ? null : new PluginArgumentSuggestion.Ui(var1, var2).select();
   }

   private void insertPluginTag(final JTextComponent var1, PipelinePluginManifest var2) {
      final String var3 = ScriptConstants.getPluginTagString(var2);
      var1.requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.replaceSelection(var3);
         }
      });
   }

   private static final class Ui {
      private final PipelinePluginListDisplayer list;
      private final ArgumentSelectionDialog impl;

      public Ui(Component var1, List<PipelinePluginManifest> var2) {
         this.list = this.createList(var2);
         this.impl = new ArgumentSelectionDialog(this.list, this.list.asSelectionSource(), "Select Argument for RunPlugin", var1);
      }

      private PipelinePluginListDisplayer createList(List<PipelinePluginManifest> var1) {
         PipelinePluginListDisplayer var2 = new PipelinePluginListDisplayer();
         var2.display(var1);
         return var2;
      }

      public PipelinePluginManifest select() {
         this.impl.show();
         if (this.impl.wasOkPressed()) {
            PipelinePluginManifest[] var1 = this.list.getSelection();
            if (var1.length == 1) {
               return var1[0];
            }
         }

         return null;
      }
   }
}
