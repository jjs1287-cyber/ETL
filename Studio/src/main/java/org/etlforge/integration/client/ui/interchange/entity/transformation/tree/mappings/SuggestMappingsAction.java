package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.MappableStatus;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperTargetTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.KeyUtils;

public final class SuggestMappingsAction extends AbstractAction {
   private final TreeComponent tree;
   private final TreeComponent opposingTree;
   private final boolean treeIsTarget;

   public SuggestMappingsAction(TreeComponent var1, TreeComponent var2) {
      super("Suggest Mappings...");
      this.tree = var1;
      this.opposingTree = var2;
      this.treeIsTarget = var1 instanceof TreeMapperTargetTree;
      this.putValue("AcceleratorKey", KeyUtils.CONTENT_ASSIST);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SuggestMappingsAction.this.suggestMappings();
         }
      });
   }

   private void suggestMappings() {
      Node var1 = this.tree.getHighlightedNode();
      if (this.isMappable(var1)) {
         SuggestionsModel var2 = this.createModel(var1);
         MappingSuggestionsDialog var3 = this.createDialog(var2);
         var3.open();
         this.restoreFocus(var1);
      }
   }

   private boolean isMappable(Node var1) {
      if (var1 == null || var1.isFolder()) {
         return false;
      } else if (this.treeIsTarget) {
         MappableStatus var2 = MappableStatus.of(var1);
         return var2.isMappable();
      } else {
         return true;
      }
   }

   private SuggestionsModel createModel(Node var1) {
      NodeSuggestionStrategy var2 = this.treeIsTarget ? NodeSuggestionStrategy.forTargetNode(var1) : NodeSuggestionStrategy.forSourceNode(var1);
      return new SuggestionsModel(var1, this.opposingTree, var2);
   }

   private MappingSuggestionsDialog createDialog(SuggestionsModel var1) {
      MappingSuggestionsDialog var2 = new MappingSuggestionsDialog(var1);
      var2.setCallback(new SuggestMappingsAction.Mapper(var1));
      return var2;
   }

   private void restoreFocus(final Node var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SuggestMappingsAction.this.tree.setHighlightedNode(var1);
            SuggestMappingsAction.this.tree.requestFocusInWindow();
         }
      });
   }

   private class Mapper implements Receiver<Node> {
      private final SuggestionsModel model;

      public Mapper(SuggestionsModel var2) {
         this.model = var2;
      }

      public void handle(Node var1) {
         if (SuggestMappingsAction.this.treeIsTarget) {
            this.mapTargetToSource(var1);
         } else {
            this.mapSourceToTarget(var1);
         }
      }

      private void mapTargetToSource(Node var1) {
         SuggestMappingsAction.this.opposingTree.setHighlightedNode(var1);
         SuggestMappingsAction.this.tree.getTreeMapper().doMap(this.model.getSelectedNode(), false);
      }

      private void mapSourceToTarget(Node var1) {
         SuggestMappingsAction.this.tree.getTreeMapper().doMap(var1, false);
      }
   }
}
