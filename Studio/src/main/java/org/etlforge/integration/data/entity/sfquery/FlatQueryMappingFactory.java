package org.etlforge.integration.data.entity.sfquery;

import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;

public final class FlatQueryMappingFactory extends AutoMappingFactory {
   private final QueryResponseFieldPath sourcePaths;

   public FlatQueryMappingFactory(String var1) {
      this(new QueryResponseFieldPath(var1));
   }

   public FlatQueryMappingFactory(QueryResponseFieldPath var1) {
      this.sourcePaths = var1;
   }

   @Override
   protected String getExpression(String var1) {
      String var2 = this.sourcePaths.getFieldPath(var1);
      return ScriptConstants.wrapInTags(var2);
   }

   @Override
   public NodePath getTargetPath(String var1) {
      return NodePath.of(QueryResponseTextStructureFactory.getTextFieldName(var1));
   }

   @Override
   public Mapping getGeneratorMapping() {
      return MappingFactory.newMapping(NodePath.toGeneratorPath(""), this.sourcePaths.getGeneratorPath());
   }
}
