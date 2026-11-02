package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperTargetTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.CommonAncestorFinder;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

final class SuggestionsModel extends AbstractPropertyBasedModel {
   public static final String WORKING = "working";
   private final Node selectedNode;
   private final TreeComponent opposingTree;
   private Node opposingRoot;
   private Node commonAncestor;
   private final NodeSuggestionStrategy strategy;
   private final Property<Boolean> working;
   private List<Node> nodesFound;
   private final List<SuggestionsModelListener> listeners;

   public SuggestionsModel(Node var1, TreeComponent var2, NodeSuggestionStrategy var3) {
      this.selectedNode = var1;
      this.opposingTree = var2;
      this.opposingRoot = var2.m_root;
      this.strategy = var3;
      this.listeners = new CopyOnWriteArrayList<>();
      this.working = this.newProperty("working", Boolean.FALSE);
   }

   public Node getSelectedNode() {
      return this.selectedNode;
   }

   public TreeComponent getOpposingTree() {
      return this.opposingTree;
   }

   public Node getOpposingRoot() {
      return this.opposingRoot;
   }

   public void setOpposingRoot(Node var1) {
      this.opposingRoot = var1;
   }

   public boolean isSelectedNodeSource() {
      return this.opposingTree instanceof TreeMapperTargetTree;
   }

   public Node getCommonAncestor() {
      return this.commonAncestor;
   }

   public NodeSuggestionStrategy getStrategy() {
      return this.strategy;
   }

   public int getNumberOfTargetNodes() {
      return this.nodesFound.size();
   }

   public void clear() {
      this.nodesFound = Lists.newArrayList();
      this.commonAncestor = null;

      for (SuggestionsModelListener var2 : this.listeners) {
         var2.cleared();
      }
   }

   public void addTargetNodes(List<Node> var1) {
      this.nodesFound.addAll(var1);
      this.updateNodesFoundRoot(var1);

      for (SuggestionsModelListener var3 : this.listeners) {
         var3.nodesFound(var1);
      }
   }

   private void updateNodesFoundRoot(List<Node> var1) {
      Node var2 = CommonAncestorFinder.findCommonAncestor(var1);
      if (var2 == null) {
         this.commonAncestor = null;
      } else if (this.commonAncestor == null || var2.isAncestorOf(this.commonAncestor)) {
         this.commonAncestor = var2.m_parent;
      }
   }

   public void setWorking(boolean var1) {
      this.working.set(var1);
   }

   public boolean isWorking() {
      return this.working.get();
   }

   public void addNodesFoundListener(SuggestionsModelListener var1) {
      this.listeners.add(var1);
   }

   public void removeNodesFoundListener(SuggestionsModelListener var1) {
      this.listeners.remove(var1);
   }
}
