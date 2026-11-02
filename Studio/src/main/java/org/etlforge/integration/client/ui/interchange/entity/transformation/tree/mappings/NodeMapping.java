package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;

public final class NodeMapping implements Mapping {
   private final TreeMapper treeMapper;
   private Mapping mapping;
   private final Node targetLoopNode;
   private int targetOrder;

   NodeMapping(TreeMapper var1, Mapping var2, Node var3) {
      this.treeMapper = var1;
      this.mapping = var2;
      this.targetLoopNode = var3;
      this.targetOrder = this.calculateTargetOrder();
      Node var4 = var1.findTargetNode(var2.getTargetPath().toString());
      if (var4 != null) {
         var4.setTargetMappedFlag(true);
         var1.m_targetTreeComponent.repaint();
      }
   }

   public TreeMapper getTreeMapper() {
      return this.treeMapper;
   }

   public int getTargetOrder() {
      return this.targetOrder;
   }

   public void reOrder() {
      this.targetOrder = this.calculateTargetOrder();
   }

   @Override
   public NodePath getTargetPath() {
      return this.mapping.getTargetPath();
   }

   @Override
   public String getExpression() {
      return this.mapping.getExpression();
   }

   public String getTranslatedExpression(ExpressionTranslator var1) {
      return var1.toDisplay(this.getExpression());
   }

   public void setNewExpression(String var1) {
      NodePath var2 = this.getTargetPath();
      this.mapping = MappingFactory.newMapping(var2, var1);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder(this.mapping.toString());
      var1.append(" - targetLoopNode = ").append(this.targetLoopNode != null ? this.targetLoopNode.getDisplay() : null);
      var1.append(" - Order = ").append(this.targetOrder);
      return var1.toString();
   }

   private int calculateTargetOrder() {
      try {
         String var1 = this.mapping.getTargetPath().toString();
         if (this.targetLoopNode != null) {
            return this.targetLoopNode.m_order * 10000 + this.treeMapper.findTargetNode(var1).m_order;
         }

         Node var2 = this.treeMapper.findTargetNode(var1);
         if (var2 != null) {
            return var2.m_order;
         }
      } catch (Exception var3) {
         ErrorLog.attention(NodeMapping.class, "An unexpected error occurred.", var3);
      }

      return -1;
   }
}
