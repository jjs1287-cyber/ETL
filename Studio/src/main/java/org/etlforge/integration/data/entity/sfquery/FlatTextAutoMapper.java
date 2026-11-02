package org.etlforge.integration.data.entity.sfquery;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.structure.mapping.Mapping;

public final class FlatTextAutoMapper extends TextAutoMapper {
   public FlatTextAutoMapper(ParsedQuery var1) {
      super(var1);
   }

   @Override
   protected List<Mapping> createAutoMappings() {
      ParsedQuery var1 = this.getParsedQuery();
      ArrayList var2 = Lists.newArrayList();
      QueryResponseFieldPath var3 = new QueryResponseFieldPath(var1.getObjectName());
      FlatQueryMappingFactory var4 = new FlatQueryMappingFactory(var3);
      var2.add(var4.getGeneratorMapping());

      for (String var6 : var1.getFieldNames()) {
         Mapping var7 = var4.getMapping(var6);
         var2.add(var7);
      }

      return var2;
   }
}
