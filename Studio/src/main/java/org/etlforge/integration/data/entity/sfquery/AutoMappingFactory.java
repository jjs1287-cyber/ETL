package org.etlforge.integration.data.entity.sfquery;

import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;

public abstract class AutoMappingFactory {
   public final Mapping getMapping(String var1) {
      String var2 = this.getExpression(var1);
      NodePath var3 = this.getTargetPath(var1);
      return MappingFactory.newMapping(var3, var2);
   }

   public abstract NodePath getTargetPath(String var1);

   protected abstract String getExpression(String var1);

   public abstract Mapping getGeneratorMapping();
}
