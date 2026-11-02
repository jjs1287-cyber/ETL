package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import org.etlforge.application.ui.Alert;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeFinder;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.structure.mapping.NodePath;

public final class OpenFormulaBuilderExecutor implements CommandExecutor {
   private final TransformationPage page;

   public OpenFormulaBuilderExecutor(TransformationPage var1) {
      this.page = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      String var2 = var1[0];
      NodeFinder var3 = new NodeFinder(this.page.getTreeMapper().m_targetTreeComponent);
      Node var4 = var3.findNode(var2);
      if (var4 != null) {
         if (var4.isFolder()) {
            Alert.error("You cannot map a target folder node.", "Not Allowed");
         } else {
            this.page.showMapping(new NodePath(var4.m_deName));
         }
      }
   }
}
