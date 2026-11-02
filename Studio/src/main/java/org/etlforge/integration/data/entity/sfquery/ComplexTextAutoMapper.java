package org.etlforge.integration.data.entity.sfquery;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.structure.mapping.Mapping;

public final class ComplexTextAutoMapper extends TextAutoMapper {
   private final RelationshipQueryMappingFactories auto;

   public ComplexTextAutoMapper(ParsedQuery var1) {
      super(var1);
      this.auto = new RelationshipQueryMappingFactories(var1.getObjectName());
   }

   @Override
   protected List<Mapping> createAutoMappings() {
      List var1 = this.getRootObjectMappings();
      var1.addAll(this.getAllChildObjectMappings());
      return var1;
   }

   public List<Mapping> getRootObjectMappings() {
      ParsedQuery var1 = this.getParsedQuery();
      ArrayList var2 = Lists.newArrayList();
      this.addMappings(var1, this.auto.forRootObject(), var2);
      return var2;
   }

   private void addMappings(ParsedQuery var1, AutoMappingFactory var2, List<Mapping> var3) {
      var3.add(var2.getGeneratorMapping());

      for (String var5 : var1.getFieldNames()) {
         Mapping var6 = var2.getMapping(var5);
         var3.add(var6);
      }
   }

   public List<Mapping> getAllChildObjectMappings() {
      ParsedQuery var1 = this.getParsedQuery();
      ArrayList var2 = Lists.newArrayList();

      for (ParsedQuery var4 : var1.getSubQueries()) {
         var2.addAll(this.getChildObjectMappings(var4));
      }

      return var2;
   }

   public List<Mapping> getChildObjectMappings(ParsedQuery var1) {
      ArrayList var2 = Lists.newArrayList();
      AutoMappingFactory var3 = this.auto.forChildObject(var1.getObjectName());
      this.addMappings(var1, var3, var2);
      return var2;
   }
}
