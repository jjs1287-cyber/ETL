package com.etlforge.integration.client.ui.script.fcn.db;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.script.WizardBasedFunctionInserter;
import com.etlforge.integration.client.ui.script.fcn.Expression;
import java.util.List;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardListener;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.functors.function.KongaFunctions;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.SourceTreeAwareFunctionInserter;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.client.ui.script.builder.TestableFunctionInserter;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.DataElementAwareFunctionInserter;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.ProjectItemFunction;
import org.etlforge.integration.data.script.ProjectItemFunctionInserter;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.system.DisposableCollection;

public abstract class AbstractDbFunctionInserter
   extends WizardBasedFunctionInserter
   implements ProjectItemFunctionInserter,
   DataElementAwareFunctionInserter,
   SourceTreeAwareFunctionInserter,
   TestableFunctionInserter {
   private Supplier<FormulaBuilderSourceTree> sourceTreeFactory;
   protected IntegrationProject project;
   protected GlobalDataElements deStore;
   private final DisposableCollection disposables = new DisposableCollection();
   protected FunctionTester tester;
   protected ExpressionParser parser;

   protected AbstractDbFunctionInserter(Function var1) {
      super(var1);
   }

   @Override
   public final void setDataElements(GlobalDataElements var1) {
      this.deStore = var1;
   }

   @Override
   public final void setProject(IntegrationProject var1) {
      this.project = var1;
   }

   @Override
   public void setSourceTree(Supplier<FormulaBuilderSourceTree> var1) {
      this.sourceTreeFactory = var1;
   }

   @Override
   public void setFunctionTester(FunctionTester var1) {
      this.tester = var1;
   }

   @Override
   public void setParser(ExpressionParser var1) {
      this.parser = var1;
   }

   @Override
   protected final Wizard<Expression> createWizard() {
      List var1 = this.getAvailableDatabases();
      if (var1.isEmpty()) {
         this.showNoDatabasesMessage();
         this.dispose();
         return null;
      }

      FormulaBuilderSourceTree var2 = this.getSourceTree();
      AbstractDbFunctionWizard var3 = this.createWizard(var1, var2);
      if (this.tester != null && this.parser != null) {
         var3.installTestingTools(this.tester, this.parser);
      }

      var3.addWizardListener(new AbstractDbFunctionInserter.CleanupHandler());
      return var3;
   }

   private List<Locatable> getAvailableDatabases() {
      if (this.project != null) {
         List var1 = ((ProjectItemFunction)this.function).getApplicableProjectItems(this.project);
         return translate(var1);
      } else {
         return Lists.newArrayList();
      }
   }

   private static List<Locatable> translate(List<IntegrationEntity> var0) {
      return KongaListUtils.transform(var0, KongaFunctions.cast(Locatable.class));
   }

   private void showNoDatabasesMessage() {
      Alert.info("The project has no database sources or targets that can be queried.", "No Databases Available");
   }

   protected abstract AbstractDbFunctionWizard createWizard(List<Locatable> var1, FormulaBuilderSourceTree var2);

   private void dispose() {
      this.disposables.dispose();
   }

   private FormulaBuilderSourceTree getSourceTree() {
      if (this.sourceTreeFactory != null) {
         FormulaBuilderSourceTree var1 = this.sourceTreeFactory.get();
         this.disposables.add(var1);
         return var1;
      } else {
         return null;
      }
   }

   private class CleanupHandler implements WizardListener<Expression> {
      private CleanupHandler() {
      }

      public void wizardCompleted(Expression var1) {
         AbstractDbFunctionInserter.this.dispose();
      }

      @Override
      public void wizardFailed(Throwable var1) {
         AbstractDbFunctionInserter.this.dispose();
      }

      @Override
      public void wizardCancelled() {
         AbstractDbFunctionInserter.this.dispose();
      }
   }
}
