package com.etlforge.integration.client.ui.script.fcn;

import com.google.common.base.Preconditions;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.SourceTreeDataElementContainer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.SourceTreeCompletionContext;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.DataTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.DataTreeListener;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.client.ui.script.text.VariableFieldFactory;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;

public abstract class AbstractSimpleFunctionWizard implements SimpleFunctionWizard {
   private final Function function;
   private GlobalDataElements globalVariables;
   private FormulaBuilderSourceTree sourceTree;
   private final VariableFieldFactory fieldFactory;
   private SimpleFunctionWizardPage page;
   private FunctionTester tester;
   private TestConsole testConsole;
   private TestController testController;
   private final ScriptDataElementsModel deModel;
   private final ExpressionScanner scanner;
   private ExpressionParser parser;
   private final DisposableCollection disposables;

   public AbstractSimpleFunctionWizard(Function var1) {
      Preconditions.checkNotNull(var1, "function");
      this.function = var1;
      this.fieldFactory = new VariableFieldFactory();
      this.disposables = new DisposableCollection(this.fieldFactory);
      this.deModel = new ScriptDataElementsModel();
      this.scanner = new ExpressionScanner(this, this.deModel);
   }

   public final void setGlobalVariables(GlobalDataElements var1) {
      this.globalVariables = var1;
      this.fieldFactory.setGlobalDataElements(var1);
   }

   public final void setSourceTree(FormulaBuilderSourceTree var1) {
      this.sourceTree = var1;
      this.disposables.add(var1);
      SourceTreeCompletionContext var2 = this.createSourceCompletionContext(var1);
      this.fieldFactory.setSourceDataElements(var2);
      this.scanner.setSourceTree(var1);
      this.hookUpParserWithSourceTree();
      AbstractSimpleFunctionWizard.SourceDataTreeListener var3 = new AbstractSimpleFunctionWizard.SourceDataTreeListener();
      var1.getDataTree().addListener(var3);
      this.disposables.add(var3);
   }

   private SourceTreeCompletionContext createSourceCompletionContext(FormulaBuilderSourceTree var1) {
      SourceTreeCompletionContext var2 = new SourceTreeCompletionContext(var1);
      var2.setRequiresTransTags(false);
      var2.setIncludeTokenInfo(false);
      return var2;
   }

   private void hookUpParserWithSourceTree() {
      if (this.parser != null && this.sourceTree != null) {
         this.parser.setSourceDataElementContainer(new SourceTreeDataElementContainer(this.sourceTree));
      }
   }

   public final void setExpressionParser(ExpressionParser var1) {
      this.parser = var1;
      this.hookUpParserWithSourceTree();
      this.scanner.setParser(var1);
      this.fieldFactory.setParser(var1);
   }

   public final void setFunctionTester(FunctionTester var1) {
      this.tester = var1;
      this.scanner.setTester(var1);
      this.testController = new TestController(var1, this.deModel);
   }

   @Override
   public final Expression run() {
      try {
         this.testConsole = this.buildTestConsole();
         VariableSelector var1 = new VariableSelector(this.globalVariables, this.sourceTree);
         this.page = this.createPage(var1);
         SimpleWizardDialogDisplayer var2 = new SimpleWizardDialogDisplayer(this.function, this.page);
         var2.setAllowFinishOnInvalidInput(true);
         if (this.testController != null) {
            this.configureTestController(var2);
         }

         boolean var3 = var2.show();
         if (var3) {
            this.mergeDataElements();
            return this.buildExpression();
         } else {
            return null;
         }
      } finally {
         this.disposables.dispose();
      }
   }

   private TestConsole buildTestConsole() {
      if (this.tester != null) {
         TestAction var1 = this.createTestAction(false);
         TestConsole var2 = new TestConsole(this.deModel, var1);
         this.disposables.add(var2);
         return var2;
      } else {
         return null;
      }
   }

   protected abstract SimpleFunctionWizardPage createPage(VariableSelector var1);

   protected final TestAction createTestAction(boolean var1) {
      return this.tester == null ? null : new TestAction(this, this.testController, this.scanner, this.parser, var1);
   }

   private void configureTestController(SimpleWizardDialogDisplayer var1) {
      TestWaitIndicator var2 = new TestWaitIndicator();
      var1.setWaitIndicator(var2);
      this.testController.setWaitService(var1.getWaitService());
      this.testController.setTestWaitIndicator(var2);
   }

   private void mergeDataElements() {
      if (this.tester != null) {
         Collection var1 = this.deModel.getDataElements();
         this.tester.getDataElements().addDataElementsScannedFromScript(var1, true);
      }
   }

   @Override
   public final void showTestSection() {
      this.page.showTestSection();
   }

   protected final Function getFunction() {
      return this.function;
   }

   protected final VariableFieldFactory getFieldFactory() {
      return this.fieldFactory;
   }

   protected final TestConsole getTestConsole() {
      return this.testConsole;
   }

   protected final ExpressionScanner getScanner() {
      return this.scanner;
   }

   protected final GlobalDataElements getGlobalVariables() {
      return this.globalVariables;
   }

   protected final FormulaBuilderSourceTree getSourceTree() {
      return this.sourceTree;
   }

   protected final SimpleFunctionWizardPage getPage() {
      return this.page;
   }

   private class SourceDataTreeListener implements DataTreeListener, Disposable {
      private SourceDataTreeListener() {
      }

      @Override
      public void dispose() {
         DataTree var1 = AbstractSimpleFunctionWizard.this.sourceTree.getDataTree();
         var1.removeListener(this);
      }

      @Override
      public void newDataLoaded() {
         this.updateDataElementModel();
      }

      @Override
      public void dataRemoved() {
         this.updateDataElementModel();
      }

      @Override
      public void newDataInstanceSelected(String var1) {
         this.updateDataElementModel();
      }

      private void updateDataElementModel() {
         AbstractSimpleFunctionWizard.this.scanner.run();
      }
   }
}
