package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.DataTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.system.Disposable;

public final class FormulaBuilderSourceTree extends TreeComponent implements Disposable {
   private JButton sourceTransferButton;
   private Action loadSourceDataAction;

   public FormulaBuilderSourceTree(TreeMapper var1) {
      super(var1, 77, var1.m_sourceTreeComponent.isSimpleNodeDisplay(), var1.m_sourceTreeComponent.isSimpleXmlElementDisplay(), false, null);
      this.setSearchTitle("Search Source Tree");
      this.createScroll();
      this.m_flat = var1.m_sourceTreeComponent.m_flat;
      this.insertOnDoubleClick = true;
   }

   private void createScroll() {
      this.m_scroll = new JScrollPane(this, 20, 30);
      this.m_scroll.setPreferredSize(new Dimension(300, 200));
   }

   public Action getLoadSourceDataAction() {
      return this.loadSourceDataAction;
   }

   public void setLoadSourceDataAction(Action var1) {
      this.loadSourceDataAction = var1;
   }

   @Override
   protected DataStructure getRepresentedStructure() {
      return this.m_TreeMapper.getTransformation().getInputStructure();
   }

   @Override
   protected void handleDoubleClick(Node var1, int var2, int var3) {
      if (this.sourceTransferButton != null && var3 == 3) {
         String var4 = var1.m_deName;
         if (var4 != null) {
            this.sourceTransferButton.doClick();
         }
      }
   }

   public void setSourceTransferButton(JButton var1) {
      this.sourceTransferButton = var1;
   }

   @Override
   protected boolean supportsToolTip() {
      return true;
   }

   public void checkSalesforceNode() {
      this.setSimpleSfdcRepresentationRoot(null);
      Node var1 = this.getTreeMapper().m_sourceTreeComponent.getSalesForceNode();
      if (var1 != null) {
         Node var2 = this.findNode(var1.m_deName);
         if (var2 != null) {
            this.setSimpleSfdcRepresentationRoot(var2);
         }
      }
   }

   @Override
   protected boolean canHaveData() {
      return true;
   }

   @Override
   public void dispose() {
      DataTree var1 = this.getDataTree();
      var1.removeTreeComponent(this);
      this.m_root = null;
      this.m_CROM_Tree = null;
   }

   @Override
   public DataTree getDataTree() {
      return this.m_TreeMapper.getSourceDataTree();
   }

   public Node getGenerator(NodePath var1) {
      TreeMapperSourceTree var2 = this.getTreeMapper().m_sourceTreeComponent;
      return var2.getGenerator(var1);
   }
}
