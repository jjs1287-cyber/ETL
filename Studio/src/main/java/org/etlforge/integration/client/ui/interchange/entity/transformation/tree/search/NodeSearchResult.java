package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.search;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.structure.reader.DocReader;
import org.etlforge.integration.structure.reader.JbNode;
import org.etlforge.ui.util.find.SearchDirection;

public final class NodeSearchResult {
   private final NodeSearchParameters searchParams;
   private final TreeComponent tree;
   private final List<Object> matchingNodes;
   private Node currentNode;
   private JbNode.SearchDataNodeItem currentMatch;
   private int order;
   private final boolean isFolder;
   private final boolean isLeaf;
   private final boolean isMappedLeaf;

   public NodeSearchResult(NodeSearchParameters var1, TreeComponent var2) {
      this.searchParams = var1;
      this.tree = var2;
      NodeSearchType var3 = var1.getNodeType();
      this.isFolder = var3 == NodeSearchType.DATA_ELEMENTS_AND_FOLDERS;
      this.isLeaf = var3 == NodeSearchType.DATA_ELEMENTS || var3 == NodeSearchType.DATA_ELEMENTS_AND_FOLDERS;
      this.isMappedLeaf = var3 == NodeSearchType.MAPPED_DATA_ELEMENTS;
      this.matchingNodes = Lists.newArrayList();
      this.findMatchingNodes(var1, var2);
   }

   public Node nextNode() {
      return this.nextNodeImpl(this.tree.getHighlightedNode());
   }

   private boolean isDown() {
      return this.searchParams.getDirection() == SearchDirection.DOWN;
   }

   private Node nextNodeImpl(Node var1) {
      if (this.searchParams.getNodeType() == NodeSearchType.DATA_NODES) {
         int var3 = this.matchingNodes.size();
         int var2;
         if (this.currentMatch == null) {
            var2 = this.isDown() ? 0 : var3 - 1;
         } else {
            var2 = this.matchingNodes.indexOf(this.currentMatch);
            var2 = this.isDown() ? var2 + 1 : var2 - 1;
         }

         if (var2 >= 0 && var2 < var3) {
            this.currentMatch = (JbNode.SearchDataNodeItem)this.matchingNodes.get(var2);
            this.tree.displayJbNode(this.currentMatch.getJbNode(), this.currentMatch.getAttributeNode(), this.searchParams.getSearchString_lowercase());
         } else {
            this.currentNode = null;
         }
      } else {
         this.currentNode = var1;
         if (var1 == null) {
            this.order = this.isDown() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
         } else {
            this.order = this.currentNode.m_order;
         }

         this.currentNode = null;
         if (this.matchingNodes.size() > 0) {
            if (this.isDown()) {
               for (Object var4 : this.matchingNodes) {
                  var1 = (Node)var4;
                  boolean var8 = var1.isFolder();
                  if (var1.m_order > this.order
                     && (this.isFolder && var8 || this.isLeaf && !var8 || this.isMappedLeaf && (var1.isSourceUsed() || var1.isTargetMapped()))) {
                     this.currentNode = var1;
                     break;
                  }
               }
            } else {
               for (Object var12 : this.matchingNodes) {
                  var1 = (Node)var12;
                  boolean var9 = var1.isFolder();
                  if (var1.m_order >= this.order) {
                     break;
                  }

                  if (this.isFolder && var9 || this.isLeaf && !var9 || this.isMappedLeaf && (var1.isSourceUsed() || var1.isTargetMapped())) {
                     this.currentNode = var1;
                  }
               }
            }
         }
      }

      if (this.currentNode == null) {
      }

      return this.currentNode;
   }

   private void findMatchingNodes(NodeSearchParameters var1, TreeComponent var2) {
      this.findMatchingNodes(var1, var2.m_root);
   }

   private void findMatchingNodes(NodeSearchParameters var1, Node var2) {
      String var3 = var1.getSearchString_lowercase();
      if (var1.getNodeType() == NodeSearchType.DATA_NODES) {
         DocReader var4 = var2.getTreeComponent().m_DocReader;
         if (var4 == null) {
            return;
         }

         try {
            if (var2.m_jbNode != null) {
               JbNode var5 = var2.m_jbNode;
               if (var5.isInstance()) {
                  var5 = var5.getParent();
               }

               var5.doSearch(this.matchingNodes, var3, var4);
            } else {
               for (Node var6 : var2.getChildren()) {
                  this.findMatchingNodes(var1, var6);
               }
            }
         } catch (IOException var7) {
            throw new RuntimeException("findMatchingNodes failed.\n" + var7);
         }
      } else {
         if (var2.getDisplay() != null && var2.getDisplay().toLowerCase().indexOf(var3) >= 0) {
            this.matchingNodes.add(var2);
         }

         for (Node var10 : var2.getChildren()) {
            this.findMatchingNodes(var1, var10);
         }
      }
   }

   @Override
   public String toString() {
      return this.currentNode + ", nodeList=" + this.matchingNodes.toString();
   }
}
