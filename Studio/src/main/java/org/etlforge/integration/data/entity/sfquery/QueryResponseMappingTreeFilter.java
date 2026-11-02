package org.etlforge.integration.data.entity.sfquery;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.etlforge.integration.client.sfdc.data.SoqlObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.transformation.MappingTreeNodePathFilter;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SoqlParser;
import org.etlforge.integration.salesforce.parser.SoqlParserFactory;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.integration.structure.mapping.PathSeparator;

public final class QueryResponseMappingTreeFilter implements MappingTreeNodePathFilter {
   private final NodePath parentPath;
   private final ParsedQuery queryInfo;
   private final QueryResponseMappingTreeFilter.Tree visiblePaths = new QueryResponseMappingTreeFilter.Tree("");

   public QueryResponseMappingTreeFilter(ParsedQuery var1) {
      Preconditions.checkArgument(var1.isValid(), "Invalid query");
      this.queryInfo = var1;
      this.parentPath = this.createParentPath(var1.getObjectName());
      this.buildTreeOfVisiblePaths(var1);
   }

   public QueryResponseMappingTreeFilter(SoqlObject var1) {
      this.parentPath = this.createParentPath(var1.getObjectName().getLocalName());
      SoqlParser var2 = SoqlParserFactory.newParser();
      this.queryInfo = var2.parse(var1.getQueryString());
      this.buildTreeOfVisiblePaths(this.queryInfo);
   }

   private NodePath createParentPath(String var1) {
      String var2 = "root$transaction.response$body$queryResponse$result$records." + var1;
      return new NodePath(var2);
   }

   private void buildTreeOfVisiblePaths(ParsedQuery var1) {
      if (var1.hasFieldInfo()) {
         addFieldNamesToTree(var1, this.visiblePaths);

         for (ParsedQuery var3 : var1.getSubQueries()) {
            String var4 = StringUtils.removeStart(var3.getObjectName(), var1.getObjectName() + ".");
            QueryResponseMappingTreeFilter.Tree var5 = this.visiblePaths.addChild(var4.toLowerCase());
            addFieldNamesToTree(var3, var5);
         }
      }
   }

   private static void addFieldNamesToTree(ParsedQuery var0, QueryResponseMappingTreeFilter.Tree var1) {
      for (String var3 : var0.getFieldNames()) {
         String var4 = var0.getFieldNameWithoutObjectQualifier(var3).toLowerCase();
         String var5 = ".";
         if (var4.contains(var5)) {
            Iterable var6 = Splitter.on(var5).split(var4);
            QueryResponseMappingTreeFilter.Tree var7 = var1;

            for (Object __etlItem64 : var6) {
               String var9 = (String) __etlItem64;
               var7 = var7.addChild(var9);
            }
         } else {
            var1.addChild(var4);
         }
      }
   }

   @Override
   public boolean isHidden(String var1) {
      if (this.visiblePaths.isEmpty()) {
         return false;
      } else {
         NodePath var2 = new NodePath(var1);
         if (this.parentPath.isAncestorOf(var2)) {
            String var3 = this.pathFromParent(var2);
            ArrayList var4 = Lists.newArrayList(PathSeparator.split(var3));
            boolean var5 = this.visiblePaths.containsPath(var4);
            return !var5;
         } else {
            return false;
         }
      }
   }

   private String pathFromParent(NodePath var1) {
      String var2 = var1.pathWithoutBrackets().substring(this.parentPath.path.length()).toLowerCase();
      return PathSeparator.removeLeading(var2);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.parentPath, this.visiblePaths);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof QueryResponseMappingTreeFilter)) {
         return false;
      }

      QueryResponseMappingTreeFilter var2 = (QueryResponseMappingTreeFilter)var1;
      return this.parentPath.equals(var2.parentPath) && this.visiblePaths.equals(var2.visiblePaths);
   }

   private static class Tree {
      private final String name;
      private final Map<String, QueryResponseMappingTreeFilter.Tree> children = Maps.newHashMap();

      public Tree(String var1) {
         this.name = var1;
      }

      public boolean isEmpty() {
         return this.children.isEmpty();
      }

      public QueryResponseMappingTreeFilter.Tree addChild(String var1) {
         QueryResponseMappingTreeFilter.Tree var2 = this.children.get(var1);
         if (var2 == null) {
            var2 = new QueryResponseMappingTreeFilter.Tree(var1);
            this.children.put(var1, var2);
         }

         return var2;
      }

      public boolean containsPath(List<String> var1) {
         if (var1.isEmpty()) {
            return false;
         } else {
            QueryResponseMappingTreeFilter.Tree var2 = this.children.get(var1.get(0));
            if (var2 == null) {
               return false;
            } else {
               return var1.size() == 1 ? true : var2.containsPath(var1.subList(1, var1.size()));
            }
         }
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(this.name, this.children);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof QueryResponseMappingTreeFilter.Tree)) {
            return false;
         }

         QueryResponseMappingTreeFilter.Tree var2 = (QueryResponseMappingTreeFilter.Tree)var1;
         return this.name.equals(var2.name) && this.children.equals(var2.children);
      }

      @Override
      public String toString() {
         return Objects.toStringHelper(this).add("name", this.name).add("children", this.children).toString();
      }
   }
}
