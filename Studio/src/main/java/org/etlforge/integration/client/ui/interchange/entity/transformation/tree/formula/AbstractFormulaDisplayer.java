package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import org.etlforge.application.ui.BusyWorker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperMode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.ui.wait.WaitService;

public abstract class AbstractFormulaDisplayer implements FormulaDisplayer {
   private final FormulaBuilderUiFactory uiFactory;
   private AbstractFormulaBuilder formulaBuilder;

   public AbstractFormulaDisplayer(FormulaBuilderUiFactory var1) {
      this.uiFactory = var1;
   }

   public final FormulaBuilderUiFactory getUiFactory() {
      return this.uiFactory;
   }

   protected final AbstractFormulaBuilder getFormulaBuilder() {
      return this.formulaBuilder;
   }

   @Override
   public final void applyCurrentFormulas() {
      if (this.formulaBuilder != null && this.formulaBuilder.isVisible() && this.formulaBuilder.apply()) {
         this.formulaBuilder.hide();
      }
   }

   @Override
   public final void closeIfBlocking() {
      if (this.formulaBuilder != null && this.formulaBuilder.isVisible() && this.isBlocking()) {
         this.formulaBuilder.applySilently();
         this.formulaBuilder.hide();
      }
   }

   protected abstract boolean isBlocking();

   @Override
   public final void insertExpression(String var1) {
      if (this.formulaBuilder != null) {
         this.formulaBuilder.insertExpression(var1);
      }
   }

   @Override
   public final void refreshSourceTree(MappingTreeStructure var1, String var2) {
      if (this.formulaBuilder != null) {
         this.formulaBuilder.refreshSourceTree(var1, var2);
      }
   }

   @Override
   public final void showMapping(final TreeMapper var1, final String var2, final NodeMapping var3, final String var4, final TestResult var5) {
      (new BusyWorker(var1.getTreeMapperContainer().getWaitService()) {
         @Override
         protected void doWork() {
            AbstractFormulaDisplayer.this.showFormulaImpl(var1, var4, var2, var3, var5);
         }
      }).run();
   }

   protected WaitService getWaitService(TreeMapper var1) {
      return var1.getTreeMapperContainer().getWaitService();
   }

   private void showFormulaImpl(TreeMapper var1, String var2, String var3, NodeMapping var4, TestResult var5) {
      if (this.formulaBuilder == null) {
         this.formulaBuilder = this.createFormulaBuilder(var1, var2, var3, var4);
      } else {
         this.formulaBuilder.setMapping(var3, var4, var2);
      }

      this.formulaBuilder.getSourceTree().setSourceDeAvailable(!var3.startsWith("$"));
      this.formulaBuilder.getSourceTree().setHighlightedNode(null);
      if (var5 == null) {
         this.formulaBuilder.showFormula();
      } else {
         this.showTestResultImpl(var5);
      }
   }

   protected abstract AbstractFormulaBuilder createFormulaBuilder(TreeMapper var1, String var2, String var3, NodeMapping var4);

   private void showTestResultImpl(TestResult var1) {
      this.formulaBuilder.showTestResult(var1);
      this.formulaBuilder.open();
   }

   @Override
   public final void showTestResult(TreeMapper var1, Node var2, TestResult var3) {
      if (this.isShowingFormula(var2)) {
         this.showTestResultImpl(var3);
      } else if (var2 != null && !var2.mapDisabled()) {
         var1.doMap(var2, false, var3);
         var1.m_sourceTreeComponent.setHighlightedNode(null);
      }
   }

   private boolean isShowingFormula(Node var1) {
      return this.formulaBuilder != null && this.formulaBuilder.isShowingFormula(var1);
   }

   @Override
   public final void dispose() {
      if (this.formulaBuilder != null) {
         this.formulaBuilder.dispose();
      }
   }

   @Override
   public final void updateSourceTreeDisplayMode(TreeMapperMode var1) {
      if (this.formulaBuilder != null) {
         this.formulaBuilder.getUi().getSourceTree().m_root.updateHiddenFlags();
      }
   }

   @Override
   public final void setShortValueDisplay(boolean var1) {
      if (this.formulaBuilder != null) {
         this.formulaBuilder.setSimpleXmlElementDisplay(var1);
      }
   }
}
