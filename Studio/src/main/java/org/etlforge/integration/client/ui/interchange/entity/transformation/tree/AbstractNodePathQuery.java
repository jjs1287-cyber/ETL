package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.lang.KongaStringUtils;

abstract class AbstractNodePathQuery {
   protected final TreeComponent tree;
   private final AbstractNodePathQuery.Type type;

   public AbstractNodePathQuery(TreeComponent var1) {
      this.tree = var1;
      this.type = getType(var1);
   }

   private static AbstractNodePathQuery.Type getType(TreeComponent var0) {
      DataStructure var1 = getStructure(var0);
      if (var1 instanceof NoStructure) {
         return AbstractNodePathQuery.Type.EMPTY;
      } else {
         return var1.isFlat() ? AbstractNodePathQuery.Type.FLAT : AbstractNodePathQuery.Type.HIERARCHICAL;
      }
   }

   private static DataStructure getStructure(TreeComponent var0) {
      Transformation var1 = var0.getTreeMapper().getTransformation();
      if (var0 instanceof TreeMapperTargetTree) {
         return var1.getOutputStructure();
      } else if (!(var0 instanceof TreeMapperSourceTree) && !(var0 instanceof FormulaBuilderSourceTree)) {
         throw new RuntimeException("Unsupported tree type: " + var0.getClass().getName());
      } else {
         return var1.getInputStructure();
      }
   }

   protected final boolean isFlat() {
      return this.type == AbstractNodePathQuery.Type.FLAT;
   }

   protected final boolean isEmpty() {
      return this.type == AbstractNodePathQuery.Type.EMPTY;
   }

   protected static final String getName(Node var0) {
      String var1 = KongaStringUtils.removeStartAndEndIfBothPresent(var0.m_deName, "[", "]");
      return NodePath.getLeafName(var1);
   }

   private enum Type {
      FLAT,
      HIERARCHICAL,
      EMPTY;
   }
}
