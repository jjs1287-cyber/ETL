package org.etlforge.integration.client.ui.script.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import org.etlforge.integration.client.ui.debug.action.DebugActions;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.debug.client.DebugService;

public final class ScriptBuilderUiFactory {
   private final ImmutableList<ScriptBuilderSectionFactory> sectionFactories;
   private final ScriptModeSelectorFactory modeSelectorFactory;
   private final DebugService debugService;
   private final DebugActions debugActions;

   public ScriptBuilderUiFactory(List<ScriptBuilderSectionFactory> var1, ScriptModeSelectorFactory var2, DebugService var3, DebugActions var4) {
      this.sectionFactories = ImmutableList.copyOf(var1);
      this.modeSelectorFactory = var2;
      this.debugService = var3;
      this.debugActions = var4;
   }

   public DebugActions getDebugActions() {
      return this.debugActions;
   }

   public ScriptBuilderUi newScriptBuilder(Script var1, IntegrationProject var2) {
      return this.createScriptBuilder(var1, var2, false);
   }

   private ScriptBuilderUi createScriptBuilder(Script var1, IntegrationProject var2, boolean var3) {
      ScriptBuilderModel var4 = this.createScriptBuilderModel(var1, var2);
      ScriptModeSelector var5 = this.modeSelectorFactory.newSelector();
      ScriptBuilderUi var6 = new ScriptBuilderUi(var4, var5);
      if (!var3) {
         List var7 = this.createSections(var4);
         var6.setSections(var7);
         this.installDebugUi(var1, this.debugService, var6);
         this.installScriptScanner(var1, var2, var6);
      }

      var6.setExpression(var1.getScript());
      return var6;
   }

   private ScriptBuilderModel createScriptBuilderModel(Script var1, IntegrationProject var2) {
      ScriptBuilderModel var3 = new ScriptBuilderModel(var2);
      var3.setSource(var1);
      var3.setMustBeSingleValue(false);
      return var3;
   }

   private List<ScriptBuilderSection> createSections(ScriptBuilderModel var1) {
      ArrayList var2 = Lists.newArrayList();

      for (ScriptBuilderSectionFactory var4 : this.sectionFactories) {
         var2.add(var4.newSection(var1));
      }

      return var2;
   }

   private void installDebugUi(Script var1, DebugService var2, ScriptBuilderUi var3) {
      if (var2 != null) {
         var3.setDebugUi(new DefaultScriptTestUi(var2, var1, this.getIndividualDebugActions()));
      }
   }

   private Iterable<Action> getIndividualDebugActions() {
      return this.debugActions == null ? Collections.emptyList() : this.debugActions.all();
   }

   private void installScriptScanner(Script var1, IntegrationProject var2, ScriptBuilderUi var3) {
      ExpressionParser var4 = new ExpressionParser(var1);
      ExpressionScanner var5 = new ExpressionScanner(var1, var2.getGlobalDataElements(), var4);
      var3.installScriptScanner(var5);
   }

   public ScriptBuilderUi newReadOnlyScriptBuilder(Script var1, IntegrationProject var2) {
      return this.createScriptBuilder(var1, var2, true);
   }
}
