package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;

public class NodeMappingFactory {
   private final TreeMapper treeMapper;

   public NodeMappingFactory(TreeMapper var1) {
      if (var1 == null) {
         throw new NullPointerException("treeMapper");
      }

      this.treeMapper = var1;
   }

   public NodeMapping newMapping(Mapping var1) {
      String var2 = var1.getTargetPath().toString();
      Node var3 = this.treeMapper.m_targetTreeComponent.getTlnDtdRoot(var2);
      return new NodeMapping(this.treeMapper, var1, var3);
   }

   public NodeMapping newMapping(NodePath var1, String var2) {
      Mapping var3 = MappingFactory.newMapping(var1, var2);
      return this.newMapping(var3);
   }

   public NodeMapping newMapping(String var1, String var2) {
      return this.newMapping(new NodePath(var1), var2);
   }
}
