package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import org.etlforge.application.ui.Alert;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeFinder;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

final class AddMappingExecutor implements CommandExecutor {
   private final TransformationPage page;

   public AddMappingExecutor(TransformationPage var1) {
      this.page = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      Node var2 = this.findSourceNode(var1);
      if (var2 != null) {
         Node var3 = this.findTargetNode(var1);
         if (var3 != null) {
            if (isLeafToLeaf(var2, var3)) {
               this.addSingleMapping(var2, var3);
               return;
            }

            if (isFolderToFolder(var2, var3)) {
               this.doStraightMapping(var2, var3);
               return;
            }
         }
      }

      Alert.error("Cannot create a mapping between the two nodes.", "Error");
   }

   private Node findSourceNode(String[] var1) {
      NodeFinder var2 = new NodeFinder(this.page.getTreeMapper().m_sourceTreeComponent);
      return var2.findNode(var1[0]);
   }

   private Node findTargetNode(String[] var1) {
      NodeFinder var2 = new NodeFinder(this.page.getTreeMapper().m_targetTreeComponent);
      return var2.findNode(var1[1]);
   }

   private static boolean isFolderToFolder(Node var0, Node var1) {
      return var0.isFolder() && var1.isFolder();
   }

   private static boolean isLeafToLeaf(Node var0, Node var1) {
      return !var0.isFolder() && !var1.isFolder();
   }

   private void addSingleMapping(Node var1, Node var2) {
      TreeMapper var3 = this.page.getTreeMapper();
      var3.addMapping(var1, var2);
   }

   private void doStraightMapping(Node var1, Node var2) {
      TreeMapper var3 = this.page.getTreeMapper();
      var3.doStraightMappings(var1, var2);
   }
}
