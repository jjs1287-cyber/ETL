package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperTargetTree;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.integration.structure.mapping.NodePath;

public final class MappingKits {
   private Node m_node;
   private MappingKits m_RootLoopParent;
   private String m_generator;
   private boolean m_bFixed;
   private String m_sourcePath;

   public MappingKits(Node var1) {
      assert var1.getTreeComponent().m_bTarget : "MappingKits should only be created for target nodes";
      this.m_node = var1;

      for (Node var2 = var1.m_parent; var2 != null; var2 = var2.m_parent) {
         if (var2.isRootLoopNode()) {
            this.m_RootLoopParent = var2.m_mappingKits;
            break;
         }
      }
   }

   void setSourcePaths(Set<String> var1) throws Exception {
      assert !this.m_node.isFolder();
      TreeMapperSourceTree var2 = this.m_node.getTreeMapper().m_sourceTreeComponent;
      String var3 = this.m_node.m_deName;
      boolean var4 = false;

      for (String var7 : this.m_node.getPathsToRemove(var1)) {
         this.m_node.removePath(var7);
         if (this.removePath(var2, var7, var3)) {
            var4 = true;
         }
      }

      for (String var8 : this.m_node.getPathsToAdd(var1)) {
         this.m_node.addPath(var8);
         if (this.addPath(var2, var8, var3)) {
            var4 = true;
         }
      }

      if (var4) {
         this.updateGenerator();
      }
   }

   private boolean removePath(TreeComponent var1, String var2, String var3) throws Exception {
      Node var4 = var1.expandTreeAndLookForNode(var2);
      if (var4 != null) {
         var4.updateUsedFlag();
         return true;
      } else {
         return false;
      }
   }

   private boolean addPath(TreeComponent var1, String var2, String var3) throws Exception {
      Node var4 = var1.expandTreeAndLookForNode(var2);
      if (var4 != null) {
         var4.setSourceUsedFlag(true);
         return true;
      } else {
         return false;
      }
   }

   void removeMapping() throws Exception {
      assert !this.m_node.isFolder();
      TreeMapperSourceTree var1 = this.m_node.getTreeMapper().m_sourceTreeComponent;
      TreeMapperTargetTree var2 = this.m_node.getTreeMapper().m_targetTreeComponent;
      String var3 = this.m_node.m_deName;
      this.m_node.clearMapped();
      boolean var4 = false;

      for (String var6 : this.m_node.getPathList()) {
         if (this.removePath(var1, var6, var3)) {
            var4 = true;
         }
      }

      if (var4) {
         this.updateGenerator();
         var1.refresh();
      }

      var2.setLinkedColumnFlag("MappingKits.removeMapping");
      var2.m_root.updateRequireMappingFlag(new HashSet<>());
      var2.refresh();
   }

   public void fixGeneratorPath(String var1) throws Exception {
      assert this.m_node.isRootLoopNode();
      this.m_generator = Utils.removeBrackets(var1);
      this.m_bFixed = true;
      this.updateGenerator();
   }

   public void resetGenerator() {
      this.m_generator = null;
   }

   public boolean hasFixedGenerator() {
      assert this.m_node.isRootLoopNode();
      return this.m_bFixed;
   }

   void removeFixedGenerator() throws Exception {
      assert this.m_node.isRootLoopNode() || this.m_node.isSfdcNode();
      this.m_bFixed = false;
      this.updateGenerator();
   }

   public String getGenerator() {
      assert this.m_node.isRootLoopNode();
      return this.m_bFixed ? this.m_generator : (this.m_generator != null ? this.m_generator : this.getDefaultGenerator());
   }

   private String getDefaultGenerator() {
      assert this.m_node.isRootLoopNode();
      int var1 = this.m_node.getPathListSize();
      return var1 == 0 ? "" : (var1 == 1 ? this.m_node.getPathList().iterator().next() : null);
   }

   public String getGeneratorString() {
      assert this.m_node.isRootLoopNode();
      String var1 = this.getGenerator();
      return this.m_bFixed ? "Fixed [" + var1 + "]" : "[" + var1 + "]";
   }

   private void dropPath(String var1) {
      for (String var3 : this.m_node.getPathList()) {
         if (var3.startsWith(var1)) {
            return;
         }

         if (var1.startsWith(var3)) {
            this.m_node.removePath(var3);
            break;
         }
      }

      this.m_node.addPath(var1);
   }

   public void updateGenerator() throws Exception {
      TreeMapper var1 = this.m_node.getTreeMapper();
      TreeComponent var2 = this.m_node.getTreeComponent();
      TreeMapperSourceTree var3 = var1.m_sourceTreeComponent;
      if (this.m_RootLoopParent != null) {
         this.m_RootLoopParent.resetLoopBranches(var3);
      }

      Node var4 = var2.m_root;
      Map var5 = var1.m_generatorMap;
      var5.clear();
      Map var6 = var1.m_conflictMap;
      HashSet var7 = Sets.newHashSet();
      Node var8 = var3.getDtdRoot();
      clearTargetLoopList(var8);
      String var9 = Utils.removeBrackets(var8.m_deName);
      var4.m_mappingKits.updateGenerator(var9, var3, var5, var6, var7);
      var1.getMappingManager().setConflictMappings(var7);
   }

   private void resetLoopBranches(TreeComponent var1) throws Exception {
      assert this.m_node.isRootLoopNode();
      HashSet var2 = Sets.newHashSet();
      this.collectDePathList(var2);
      this.m_node.clearPathList();

      for (Object __etlItem182 : var2) {
         String var4 = (String) __etlItem182;
         Node var5 = var1.expandTreeAndLookForNode(var4);
         if (var5 != null) {
            String var6;
            if (var5.getTreeComponent().m_flat) {
               var6 = Utils.removeBrackets(var5.m_parent.m_deName);
            } else {
               var6 = var1.findGenratorPath(var4);
            }

            this.dropPath(var6);
         }
      }
   }

   private void collectDePathList(Set<String> var1) {
      for (Node var3 : this.m_node.getChildren()) {
         if (var3.isFolder()) {
            if (!var3.isLoopNode()) {
               var3.m_mappingKits.collectDePathList(var1);
            }
         } else {
            var3.collectPaths(var1);
         }
      }
   }

   private static void clearTargetLoopList(Node var0) {
      if (var0.isRootLoopNode()) {
         var0.clearPathList();
      }

      for (Node var2 : var0.getChildren()) {
         if (var2.isFolder()) {
            clearTargetLoopList(var2);
         }
      }
   }

   private void updateGenerator(String var1, TreeComponent var2, Map<Node, Node> var3, Map<Node, Set<Node>> var4, Set<InvalidMapping> var5) throws Exception {
      TreeComponent var6 = this.m_node.getTreeComponent();
      if (!this.m_node.isRootLoopNode() && this.m_node == var6.m_root) {
         this.m_node = this.m_node.getFirstChildFolder();
         assert this.m_node.isRoot();
      }

      String var7 = var1;
      if (this.m_node != this.m_node.getTreeComponent().m_root && this.m_node.isRootLoopNode()) {
         String var8 = this.m_bFixed ? this.m_generator : this.getDefaultGenerator();
         if (!this.m_bFixed) {
            if (var8 == null || !var8.startsWith(var1)) {
               this.m_generator = var1;
            } else if (this.m_generator == null || !this.m_generator.equals(var8)) {
               this.m_generator = var8;
            }
         }

         this.updateGeneratorMap(var2, var3, var4, var5);
         var7 = this.m_generator;
      }

      for (Node var9 : this.m_node.getChildren()) {
         if (var9.isFolder()) {
            var9.m_mappingKits.updateGenerator(var7, var2, var3, var4, var5);
         }
      }
   }

   private void updateGeneratorMap(TreeComponent var1, Map<Node, Node> var2, Map<Node, Set<Node>> var3, Set<InvalidMapping> var4) throws TreeMapperException {
      try {
         Node var5 = this.m_generator.isEmpty() ? var1.getDtdRoot() : var1.expandTreeAndLookForNode("[" + this.m_generator + "]");
         if (this.m_bFixed && var5 == var1.m_root && var5.getChildCount() == 1) {
            var5 = var5.getFirstChildFolder();
         }

         if (var5 != null && var5 != var1.m_root) {
            var2.put(this.m_node, var5);
            var5.addPath(this.m_node.m_deName);
         }
      } catch (TreeMapperException var9) {
         var9.printStackTrace();
      }

      Set var10 = (Set)var3.get(this.m_node);
      if (var10 == null) {
         var10 = Sets.newHashSet();
      } else {
         var10.clear();
      }

      for (String var7 : this.m_node.getPathList()) {
         if (!this.m_generator.startsWith(var7)) {
            Node var8 = var1.expandTreeAndLookForNode("[" + var7 + "]");
            if (var8 != null && var8 != var1.m_root) {
               var10.add(var8);
            }
         }
      }

      var3.put(this.m_node, var10);
      if (var10.size() > 0) {
         StringBuilder var11 = new StringBuilder("The mappings of the target loop node:")
            .append(this.m_node)
            .append(" depends on more than one source loop node branches:\n")
            .append(this.m_generator)
            .append("\n");

         for (Object __etlItem289 : var10) {
            Node var13 = (Node) __etlItem289;
            var11.append(var13.m_deName).append("\n");
         }

         var4.add(new InvalidMapping(new NodePath(this.m_node.m_deName), this.m_generator, var11.toString()));
      }
   }

   MappingKits getRootLoopParent() {
      return this.m_RootLoopParent;
   }

   public String getSourcePath() {
      return this.m_sourcePath;
   }

   public void setSourcePath(String var1) {
      this.m_sourcePath = var1;
   }
}
