package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import javax.swing.Action;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.LoadStructureInvoker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.LoadStructuresJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.ui.UiThreadSupplier;

final class SourceTreeFactory implements Supplier<FormulaBuilderSourceTree> {
   private final TreeMapper treeMapper;

   public SourceTreeFactory(TreeMapper var1) {
      this.treeMapper = var1;
   }

   public FormulaBuilderSourceTree get() {
      return UiThreadSupplier.call(new Supplier<FormulaBuilderSourceTree>() {
         public FormulaBuilderSourceTree get() {
            return SourceTreeFactory.this.newSourceTree();
         }
      });
   }

   public FormulaBuilderSourceTree newSourceTree() {
      FormulaBuilderSourceTree var1 = new FormulaBuilderSourceTree(this.treeMapper);
      this.installLoadSourceAction(var1);
      this.kickoffLoadInputStructureJob(var1);
      return var1;
   }

   private void installLoadSourceAction(FormulaBuilderSourceTree var1) {
      Action var2 = this.treeMapper.getPageAction("action.ic.tx.LoadSource");
      var1.setLoadSourceDataAction(var2);
   }

   private void kickoffLoadInputStructureJob(final FormulaBuilderSourceTree var1) {
      LoadStructureInvoker var2 = new LoadStructureInvoker() {
         @Override
         public void displayLoadedStructures(MappingTreeStructure var1x, MappingTreeStructure var2x) {
            if (var1x != null) {
               this.configureInputTree(var1x);
            }
         }

         private void configureInputTree(MappingTreeStructure var1x) {
            EventQueue.invokeLater(SourceTreeFactory.this.new TreePreparer(var1, var1x));
         }

         @Override
         public Transformation getTransformation() {
            return SourceTreeFactory.this.treeMapper.m_Transformation;
         }

         @Override
         public boolean isAskingForInputStructure() {
            return true;
         }

         @Override
         public boolean isAskingForOutputStructure() {
            return false;
         }

         @Override
         public void jobHasBeenInitialized() {
         }

         @Override
         public void jobHasCompleted() {
         }

         @Override
         public void cancelled() {
         }
      };
      LoadStructuresJob var3 = new LoadStructuresJob(var2, this.treeMapper.getMappingStructureFactory(), this.treeMapper.getTreeStructureCache(), true);
      var3.submit();
   }

   private class TreePreparer implements Runnable {
      private final FormulaBuilderSourceTree sourceTree;
      private final MappingTreeStructure inputJtr;

      public TreePreparer(FormulaBuilderSourceTree var2, MappingTreeStructure var3) {
         this.sourceTree = var2;
         this.inputJtr = var3;
      }

      @Override
      public void run() {
         this.sourceTree.readMappingStructure(this.inputJtr, "Source");
         this.sourceTree.applyCurrentExpansionMode();
         this.sourceTree.checkSalesforceNode();
         this.sourceTree.refresh();
         SourceTreeFactory.this.treeMapper.getSourceDataTree().addTreeComponent(this.sourceTree);
      }
   }
}
