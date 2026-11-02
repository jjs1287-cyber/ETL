package org.etlforge.integration.client.ui.de.global.tree;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.TreeSet;
import org.etlforge.integration.data.script.de.BuiltInDataElements;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class TreeModelBuilder {
   public static KongaTreeModel newModel(GlobalDataElements var0, boolean var1) {
      DefaultKongaTreeNode var2 = createRootNode();
      if (var0 != null) {
         addProjectVariableNodes(var0, var2);
      }

      if (var1) {
         addDataElementNodes(new ETLForgeVariablesNode(var2), BuiltInDataElements.getBuiltInDataElements());
      }

      return new DefaultKongaTreeModel(var2);
   }

   private static DefaultKongaTreeNode createRootNode() {
      return new DefaultKongaTreeNode("root", true);
   }

   private static void addProjectVariableNodes(GlobalDataElements var0, KongaTreeNode var1) {
      ProjectVariablesNode var2 = new ProjectVariablesNode(var1);
      addDataElementNodes(var2, var0.getAllDataElements());
      if (var2.getChildCount() == 0) {
         new NoneNode(var2);
      }
   }

   private static void addDataElementNodes(KongaTreeNode var0, Collection<String> var1) {
      TreeSet var2 = Sets.newTreeSet(String.CASE_INSENSITIVE_ORDER);
      var2.addAll(var1);

      for (String var4 : var1) {
         new DataElementNode(var0, var4);
      }
   }

   private TreeModelBuilder() {
   }
}
