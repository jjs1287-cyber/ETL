package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import org.etlforge.application.ui.Alert;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeFinder;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.structure.mapping.NodePath;

public final class DeleteMappingExecutor implements CommandExecutor {
   private final TransformationPage page;

   public DeleteMappingExecutor(TransformationPage var1) {
      this.page = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      Node var2 = this.findNode(var1);
      if (var2 != null) {
         this.removeMapping(var2);
      } else {
         showNoSuchNodeMessage(var1[0]);
      }
   }

   private Node findNode(String[] var1) {
      String var2 = var1[0];
      NodeFinder var3 = new NodeFinder(this.page.getTreeMapper().m_targetTreeComponent);
      return var3.findNode(var2);
   }

   private void removeMapping(Node var1) {
      if (var1.isFolder()) {
         this.page.getTreeMapper().deleteAllMappings(var1);
      } else {
         this.page.getTreeMapper().deleteMapping(new NodePath(var1.m_deName));
      }
   }

   private static void showNoSuchNodeMessage(String var0) {
      Alert.error("Could not find a target node matching \"" + var0 + "\".", "No such target node");
   }
}
