package org.etlforge.integration.client.ui.script.text;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.entity.selector.EntityTreeSelector;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityCollectionFilter;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.widget.tree.LeafDisplayNameFilterFunction;
import org.etlforge.util.name.Nameables;

public final class ProjectItemArgumentSuggestion implements FunctionArgumentSuggestion {
   private final FunctionProjectItems projectItems;

   public ProjectItemArgumentSuggestion(FunctionProjectItems var1) {
      this.projectItems = var1;
   }

   @Override
   public void insertArgument(Function var1, JTextComponent var2) {
      List var3 = this.getApplicableItems(var1);
      if (!var3.isEmpty()) {
         ProjectItemArgumentSuggestion.Dialog var4 = new ProjectItemArgumentSuggestion.Dialog(var3, "Select Argument for " + var1.getName(), var2);
         IntegrationEntity var5 = var4.select();
         if (var5 != null) {
            this.insertItemTag(var2, var5);
         }
      }
   }

   private List<IntegrationEntity> getApplicableItems(Function var1) {
      List var2 = this.projectItems.getApplicableProjectItemArguments(var1);
      if (!var2.isEmpty()) {
         Collections.sort(var2, new EntityTypeAndNameSorter());
      }

      return var2;
   }

   private void insertItemTag(final JTextComponent var1, IntegrationEntity var2) {
      final String var3 = ScriptConstants.getProjectItemTagString(var2);
      var1.requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.replaceSelection(var3);
         }
      });
   }

   @Override
   public String[] getFunctionNames() {
      FunctionRegistry var1 = FunctionRegistry.getRegistry();
      ArrayList var2 = Lists.newArrayList(Nameables.toNames(var1.getProjectItemFunctions()));
      return Iterables.toArray(var2, String.class);
   }

   @Override
   public CompletionToken[] getCompletionTokens(Function var1, int var2) {
      ArrayList var3 = Lists.newArrayList();

      for (IntegrationEntity var5 : this.projectItems.getApplicableProjectItemArguments(var1)) {
         var3.add(new ProjectItemCompletionToken(var5, var2));
      }

      return (CompletionToken[]) var3.toArray(new CompletionToken[var3.size()]);
   }

   private static final class Dialog {
      private final EntityTreeSelector<IntegrationEntity> tree;
      private final ArgumentSelectionDialog impl;

      public Dialog(List<IntegrationEntity> var1, String var2, Component var3) {
         this.tree = this.createTree(var1);
         this.impl = new ArgumentSelectionDialog(this.tree, this.tree.getSelectionSource(), var2, var3);
         this.impl.setPreferredWidth(500);
      }

      private EntityTreeSelector<IntegrationEntity> createTree(List<IntegrationEntity> var1) {
         EntityTreeSelector var2 = EntityTreeSelector.newTree(var1, IntegrationEntity.class);
         var2.setFilter(new EntityCollectionFilter(var1));
         var2.setProjectVisible(false);
         var2.setTreeFilterFunction(new LeafDisplayNameFilterFunction(StringMatchers.CONTAINS_IGNORE_CASE));
         return var2;
      }

      public IntegrationEntity select() {
         this.impl.show();
         return this.impl.wasOkPressed() ? this.tree.getSelectedEntity() : null;
      }
   }
}
