package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.panel;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar.CheckingException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingManager;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

public class MappingConflictChecker {
   private static final int RESOLVED = -1;
   private static final int DEFAUlT_PATH = 0;
   private static final int CONFLICT_PATH = 1;
   private TreeMapper treeMapper;
   private String targetDE;
   private Node targetNode;
   private int status;

   public MappingConflictChecker(TreeMapper var1, Node var2) {
      try {
         this.treeMapper = var1;
         this.targetNode = var2;
         this.targetDE = var2.m_deName;
         boolean var3 = this.targetNode.isRoot();
         int var4 = this.targetNode.getPathListSize();
         if (var3) {
            if (var4 > 1) {
               this.setStatusSilently(1);
            } else {
               this.setStatusSilently(-1);
            }
         } else if (var4 > 1) {
            this.setStatusSilently(1);
         } else if (var1.m_sourceTreeComponent.m_flat && var4 == 1) {
            var2.setSourcePath(this.targetNode.m_mappingKits.getGenerator());
            this.setStatusSilently(-1);
         } else if (var4 == 1) {
            var2.setSourcePath(this.targetNode.m_mappingKits.getGenerator());
            Node var5 = var2.getRootLoopNode();
            if (var5 != null) {
               String var6 = var5.getSourcePath();
               if (var6 != null && var6.equals("-blank-")) {
                  var6 = "";
               }

               if (var6 != null && !var2.getSourcePath().startsWith(var6)) {
                  this.setStatusSilently(1);
                  var5.setSourcePath(null);
                  var2.setSourcePath(null);
               } else {
                  this.setStatusSilently(0);
               }
            } else {
               this.setStatusSilently(0);
            }
         } else if (var2.getSourcePath() != null) {
            this.setStatusSilently(-1);
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }

   public void checkNotConflict() throws CheckingException {
      if (this.status == 1) {
         throw new CheckingException("Conflict mapping on the target loop node: " + this.targetDE);
      }
   }

   private void setStatusSilently(int var1) {
      this.setStatusImpl(var1, false);
   }

   public void setStatus(int var1) {
      this.setStatusImpl(var1, true);
   }

   private void setStatusImpl(int var1, boolean var2) {
      boolean var3 = var1 != this.status;
      this.status = var1;
      if (var1 == -1) {
         this.resolveMapping(var2);
         if (var3 && var2) {
            this.treeMapper.setDirty(true);
         }
      }
   }

   private void resolveMapping(boolean var1) {
      MappingManager var2 = this.treeMapper.getMappingManager();
      NodeMapping var3 = var2.findMapping(this.targetNode.m_deName);
      if (this.targetNode.getSourcePath() != null) {
         if (var3 == null) {
            var3 = this.treeMapper.getNodeMappingFactory().newMapping(this.targetNode.m_deName, this.targetNode.getSourcePath());
            var2.addMapping(var3);
         } else if (var1) {
            var2.replaceMapping(this.targetNode.getSourcePath(), this.targetNode.m_deName);
         }
      }
   }
}
