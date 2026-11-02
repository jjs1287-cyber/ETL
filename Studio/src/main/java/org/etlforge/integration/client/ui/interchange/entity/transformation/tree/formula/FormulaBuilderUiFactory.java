package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import org.etlforge.integration.client.ui.debug.action.DebugActions;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.SourceTreeDataElementContainer;
import org.etlforge.integration.client.ui.script.builder.DefaultScriptTestUi;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderModel;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSection;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSectionFactory;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.integration.client.ui.script.builder.ScriptModeSelector;
import org.etlforge.integration.client.ui.script.builder.ScriptModeSelectorFactory;
import org.etlforge.integration.client.ui.script.builder.ScriptTestUi;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.TargetNode;

public final class FormulaBuilderUiFactory {
   private final ImmutableList<ScriptBuilderSectionFactory> sectionFactories;
   private final ScriptModeSelectorFactory modeSelectorFactory;
   private final DebugService debugService;
   private final DebugActions debugActions;

   public FormulaBuilderUiFactory(List<ScriptBuilderSectionFactory> var1, ScriptModeSelectorFactory var2, DebugService var3, DebugActions var4) {
      this.sectionFactories = ImmutableList.copyOf(var1);
      this.modeSelectorFactory = var2;
      this.debugService = var3;
      this.debugActions = var4;
   }

   public ScriptBuilderUi createScriptBuilderUi(
      Transformation var1, ScriptBuilderModel var2, SourceTreeLayout var3, SourceTreeDataElementContainer var4, TargetNode var5, String var6
   ) {
      ScriptModeSelector var7 = this.modeSelectorFactory.newSelector();
      ScriptBuilderUi var8 = new ScriptBuilderUi(var2, var7);
      var8.setSourceTree(var3);
      List var9 = this.createSections(var2);
      var8.setSections(var9);
      ScriptTestUi var10 = this.createDebugUi(var1, var4, var5);
      var8.setDebugUi(var10);
      var8.setExpression(checkEmptyExpression(var6));
      var8.setDefaultCaretPosition();
      return var8;
   }

   private List<ScriptBuilderSection> createSections(ScriptBuilderModel var1) {
      ArrayList var2 = Lists.newArrayList();

      for (ScriptBuilderSectionFactory var4 : this.sectionFactories) {
         var2.add(var4.newSection(var1));
      }

      return var2;
   }

   private ScriptTestUi createDebugUi(Transformation var1, SourceTreeDataElementContainer var2, TargetNode var3) {
      DefaultScriptTestUi var4 = new DefaultScriptTestUi(this.debugService, var1, this.getIndividualDebugActions());
      var4.getModel().setTargetNode(var3);
      var4.getModel().setSourceDataElementContainer(var2);
      return var4;
   }

   private Iterable<Action> getIndividualDebugActions() {
      return this.debugActions == null ? Collections.emptyList() : this.debugActions.all();
   }

   private static String checkEmptyExpression(String var0) {
      return var0.isEmpty() ? "<trans>\n\n</trans>" : var0;
   }
}
