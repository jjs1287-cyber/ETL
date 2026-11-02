package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.MappableStatus;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.structure.mapping.NodePath;

abstract class NodeSuggestionStrategy {
   private final String selectedNodeName;

   public static NodeSuggestionStrategy forSourceNode(Node var0) {
      return new NodeSuggestionStrategy(var0) {
         @Override
         protected boolean isMappable(Node var1) {
            return !var1.isFolder();
         }
      };
   }

   public static NodeSuggestionStrategy forTargetNode(Node var0) {
      return new NodeSuggestionStrategy(var0) {
         @Override
         protected boolean isMappable(Node var1) {
            MappableStatus var2 = MappableStatus.of(var1);
            return var2.isMappable();
         }
      };
   }

   public NodeSuggestionStrategy(Node var1) {
      this.selectedNodeName = getNodeName(var1);
   }

   private static String getNodeName(Node var0) {
      NodePath var1 = new NodePath(var0.m_deName);
      return var1.getLeafName().toLowerCase().replace("_", "");
   }

   public final boolean isCandidate(Node var1) {
      if (!this.isMappable(var1)) {
         return false;
      }

      String var2 = getNodeName(var1);
      return var2.contains(this.selectedNodeName) || this.selectedNodeName.contains(var2);
   }

   protected abstract boolean isMappable(Node var1);
}
