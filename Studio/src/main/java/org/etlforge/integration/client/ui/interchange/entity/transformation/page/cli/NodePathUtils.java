package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodePathCompleter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;

final class NodePathUtils {
   public static NodePathCompleter.Completion completePath(TreeComponent var0, String var1, String var2) {
      String var3 = var1.substring(var2.length());
      if (var3.isEmpty()) {
         return null;
      }

      NodePathCompleter var4 = new NodePathCompleter(var0);
      NodePathCompleter.Completion var5 = var4.complete(var3);
      return var5 == null ? null : var5.substring(var3.length());
   }

   private NodePathUtils() {
   }
}
