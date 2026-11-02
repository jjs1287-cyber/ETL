package org.etlforge.integration.data.entity.sfquery;

import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.lang.KongaStringUtils;

public final class RelationshipQueryMappingFactories {
   private final String rootObject;
   private final QueryResponseFieldPath sourcePaths;

   public RelationshipQueryMappingFactories(String var1) {
      this.rootObject = var1;
      this.sourcePaths = new QueryResponseFieldPath(var1);
   }

   public AutoMappingFactory forRootObject() {
      return new RelationshipQueryMappingFactories.RootObjectMapper();
   }

   public AutoMappingFactory forChildObject(String var1) {
      return new RelationshipQueryMappingFactories.ChildObjectMapper(var1);
   }

   private class ChildObjectMapper extends AutoMappingFactory {
      private final String relationshipName;

      public ChildObjectMapper(String var2) {
         this.relationshipName = KongaStringUtils.substringAfterLastIfPresent(var2, ".");
      }

      @Override
      protected String getExpression(String var1) {
         String var2 = RelationshipQueryMappingFactories.this.sourcePaths.getPathToFieldInChildObject(this.relationshipName, var1);
         return ScriptConstants.wrapInTags(var2);
      }

      @Override
      public NodePath getTargetPath(String var1) {
         String var2 = this.pathToParentSegment();
         String var3 = QueryResponseTextStructureFactory.getTextFieldName(var1);
         return NodePath.of(var2 + var3);
      }

      private String pathToParentSegment() {
         return "_Root$" + RelationshipQueryMappingFactories.this.rootObject + "." + this.relationshipName + ".";
      }

      @Override
      public Mapping getGeneratorMapping() {
         return MappingFactory.newMapping(
            NodePath.toGeneratorPath(this.pathToParentSegment()),
            NodePath.toGeneratorPath(RelationshipQueryMappingFactories.this.sourcePaths.getPathToChildObject(this.relationshipName))
         );
      }
   }

   private class RootObjectMapper extends AutoMappingFactory {
      private RootObjectMapper() {
      }

      @Override
      protected String getExpression(String var1) {
         String var2 = RelationshipQueryMappingFactories.this.sourcePaths.getFieldPath(var1);
         return ScriptConstants.wrapInTags(var2);
      }

      @Override
      public NodePath getTargetPath(String var1) {
         String var2 = this.pathToParentSegment();
         String var3 = QueryResponseTextStructureFactory.getTextFieldName(var1);
         return NodePath.of(var2 + var3);
      }

      private String pathToParentSegment() {
         return "_Root$" + RelationshipQueryMappingFactories.this.rootObject + ".";
      }

      @Override
      public Mapping getGeneratorMapping() {
         return MappingFactory.newMapping(
            NodePath.toGeneratorPath(this.pathToParentSegment()), RelationshipQueryMappingFactories.this.sourcePaths.getGeneratorPath()
         );
      }
   }
}
