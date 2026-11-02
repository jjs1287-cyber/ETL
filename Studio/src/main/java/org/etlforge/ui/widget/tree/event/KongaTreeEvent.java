package org.etlforge.ui.widget.tree.event;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public class KongaTreeEvent extends EventObject {
   private final List<KongaTreeNode> sourceNodes;

   public KongaTreeEvent(KongaTree var1, KongaTreeNode var2) {
      super(var1);
      this.sourceNodes = new ArrayList<>();
      this.sourceNodes.add(var2);
   }

   public KongaTreeEvent(KongaTree var1, List<KongaTreeNode> var2) {
      super(var1);
      this.sourceNodes = var2 != null ? new ArrayList<>(var2) : new ArrayList<>();
   }

   public KongaTreeNode getSourceNode() {
      return !this.sourceNodes.isEmpty() ? this.sourceNodes.get(0) : null;
   }

   public List<KongaTreeNode> getSourceNodes() {
      return new ArrayList<>(this.sourceNodes);
   }

   public int getNumberOfSourceNodes() {
      return this.sourceNodes.size();
   }

   public KongaTree getSource() {
      return (KongaTree)super.getSource();
   }
}
