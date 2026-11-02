package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.awt.Cursor;
import java.awt.EventQueue;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;

public final class StraightMappingJob extends UiJob {
   private final Node source;
   private final Node target;
   private final TreeMapper treeMapper;
   private final double dpercent = 0.02;
   private double percent = 0.02;
   private double nNode = 1.0;
   private double order0 = 0.0;

   public StraightMappingJob(TreeMapper var1, Node var2, Node var3) {
      super(TreeStrings.get("StraightMappingJob.Name"));
      this.treeMapper = var1;
      this.source = this.getNodeToUse(var2, var1.m_sourceTreeComponent);
      this.target = this.getNodeToUse(var3, var1.m_targetTreeComponent);
      this.init();
   }

   private Node getNodeToUse(Node var1, TreeComponent var2) {
      return var1 != null && !var2.isAncestorOfVisibleRoot(var1) ? var1 : var2.getVisibleRoot();
   }

   private void init() {
      if (!this.source.hasChildren()) {
         this.source.expandNode();
      }

      this.nNode = this.source.countNodes();
      if (this.nNode == 0.0) {
         this.nNode = 1.0;
      }

      this.order0 = this.source.m_order;
   }

   @Override
   protected void runImpl() {
      if (this.source != null && this.target != null) {
         MappingTreeAutoMapper var1 = new MappingTreeAutoMapper(this.treeMapper, this.source, this.target);
         var1.run();
         final boolean var2 = var1.wasAtLeastOneMappingAdded();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               StraightMappingJob.this.source.getTreeComponent().setCursor(Cursor.getPredefinedCursor(0));
               StraightMappingJob.this.target.getTreeComponent().setCursor(Cursor.getPredefinedCursor(0));
               if (var2) {
                  StraightMappingJob.this.treeMapper.setDirty(true);
               }
            }
         });
      }
   }

   public void checkStatus(int var1) {
      double var2 = (var1 - this.order0) / this.nNode;
      if (var2 > this.percent) {
         this.setCompletionValue((int)(var2 * 100.0));
         this.percent = var2 + 0.02;
      }
   }
}
