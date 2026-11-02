package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.ui.util.find.SearchAndReplaceTarget;
import org.etlforge.ui.util.find.SearchAndReplaceTextTarget;
import org.etlforge.ui.util.find.SearchDirection;
import org.etlforge.ui.util.find.SearchTarget;
import org.etlforge.ui.util.find.TextSearchMatch;
import org.etlforge.ui.util.find.TextSearchParameters;
import org.etlforge.util.Interval;

public final class MappingSearchAndReplaceTarget implements SearchAndReplaceTarget {
   private final TreeMapper treeMapper;
   private List<NodeMapping> mappings;
   private int currentIndex;
   private MappingSearchAndReplaceTarget.MappingTarget mappingTarget;
   private MappingSearchAndReplaceTarget.MappingSearchMatchImpl currentMatch;
   private final ExpressionTranslator expressionTranslator;

   public MappingSearchAndReplaceTarget(TreeMapper var1, ExpressionTranslator var2) {
      this.treeMapper = var1;
      this.currentIndex = -1;
      this.mappings = this.getCurrentMappings();
      this.expressionTranslator = var2;
   }

   public MappingSearchAndReplaceTarget.MappingSearchMatchImpl nextMatch(TextSearchParameters var1) {
      MappingSearchAndReplaceTarget.MappingSearchMatchImpl var2 = this.getNextMatchImpl(var1);
      this.currentMatch = var2;
      return var2;
   }

   @Override
   public void replace(SearchTarget.Match var1, String var2) {
      if (!(var1 instanceof MappingSearchAndReplaceTarget.MappingSearchMatchImpl)) {
         throw new IllegalArgumentException("Invalid match: " + var1);
      }

      ((MappingSearchAndReplaceTarget.MappingSearchMatchImpl)var1).replaceWith(var2);
   }

   @Override
   public List<SearchTarget.Match> replaceAll(TextSearchParameters var1) {
      this.treeMapper.startBulkChange();

      try {
         return this.replaceAllImpl(var1);
      } finally {
         this.treeMapper.stopBulkChange();
      }
   }

   private List<SearchTarget.Match> replaceAllImpl(TextSearchParameters var1) {
      MappingSearchAndReplaceTarget.MappingSearchMatchImpl var2 = this.currentMatch;
      String var3 = var1.getReplaceString();
      ArrayList var4 = Lists.newArrayList();

      for (MappingSearchAndReplaceTarget.MappingSearchMatchImpl var5 = var2 != null ? var2 : this.nextMatch(var1); var5 != null; var5 = this.nextMatch(var1)) {
         var4.add(var5);
         this.replace(var5, var3);
      }

      return var4;
   }

   public void reset() {
      this.currentMatch = null;
      this.currentIndex = -1;
      this.mappingTarget = null;
      this.mappings = this.getCurrentMappings();
   }

   private MappingSearchAndReplaceTarget.MappingSearchMatchImpl getNextMatchImpl(TextSearchParameters var1) {
      if (this.mappingTarget == null) {
         this.mappingTarget = this.getNextMappingTarget(var1);
         if (this.mappingTarget == null) {
            return null;
         }
      }

      int var2 = this.currentIndex >= 0 ? this.currentIndex : this.mappings.size() - 1;

      do {
         TextSearchMatch var3 = this.mappingTarget.text.nextMatch(var1);
         if (var3 != null) {
            return new MappingSearchAndReplaceTarget.MappingSearchMatchImpl(this.mappingTarget, var3);
         }

         this.mappingTarget = this.getNextMappingTarget(var1);
         if (this.mappingTarget == null) {
            this.mappingTarget = this.cycle(var1);
            if (this.mappingTarget == null) {
               return null;
            }
         }
      } while (this.currentIndex != var2);

      return null;
   }

   private MappingSearchAndReplaceTarget.MappingTarget cycle(TextSearchParameters var1) {
      if (var1.getDirection() == SearchDirection.DOWN) {
         this.currentIndex = 0;
      } else {
         this.currentIndex = this.mappings.size() - 1;
      }

      return this.getCurrentMappingTargetFromIndex();
   }

   private MappingSearchAndReplaceTarget.MappingTarget getNextMappingTarget(TextSearchParameters var1) {
      if (var1.getDirection() == SearchDirection.DOWN) {
         if (this.currentIndex < this.mappings.size()) {
            this.currentIndex++;
         } else {
            this.currentIndex = 0;
         }
      } else if (this.currentIndex >= 0) {
         this.currentIndex--;
      } else {
         this.currentIndex = this.mappings.size() - 1;
      }

      return this.getCurrentMappingTargetFromIndex();
   }

   private MappingSearchAndReplaceTarget.MappingTarget getCurrentMappingTargetFromIndex() {
      if (this.currentIndex >= 0 && this.currentIndex < this.mappings.size()) {
         NodeMapping var1 = this.mappings.get(this.currentIndex);
         return new MappingSearchAndReplaceTarget.MappingTarget(var1);
      } else {
         return null;
      }
   }

   private List<NodeMapping> getCurrentMappings() {
      return KongaListUtils.asList(this.treeMapper.getMappingManager().getMappings());
   }

   private static class MappingSearchMatchImpl implements SearchTarget.Match, MappingSearchMatch {
      private final MappingSearchAndReplaceTarget.MappingTarget target;
      private final TextSearchMatch span;

      public MappingSearchMatchImpl(MappingSearchAndReplaceTarget.MappingTarget var1, TextSearchMatch var2) {
         this.target = var1;
         this.span = var2;
      }

      @Override
      public NodeMapping getMapping() {
         return this.target.mapping;
      }

      @Override
      public Interval getExpressionSpan() {
         return this.span;
      }

      public void replaceWith(String var1) {
         this.target.replace(this.span, var1);
      }

      @Override
      public int compareLocation(SearchTarget.Match var1) {
         return this.compareLocation((MappingSearchAndReplaceTarget.MappingSearchMatchImpl)var1);
      }

      private int compareLocation(MappingSearchAndReplaceTarget.MappingSearchMatchImpl var1) {
         int var2 = this.target.mapping.getTargetOrder();
         int var3 = var1.target.mapping.getTargetOrder();
         if (var2 < var3) {
            return -1;
         } else {
            return var2 > var3 ? 1 : this.span.compareLocation(var1.span);
         }
      }

      @Override
      public int hashCode() {
         int var1 = this.target.mapping.getTargetPath().hashCode();
         return 31 * var1 + this.span.hashCode();
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof MappingSearchAndReplaceTarget.MappingSearchMatchImpl)) {
            return false;
         }

         MappingSearchAndReplaceTarget.MappingSearchMatchImpl var2 = (MappingSearchAndReplaceTarget.MappingSearchMatchImpl)var1;
         return this.target.mapping.getTargetPath().equals(var2.target.mapping.getTargetPath()) && this.span.equals(var2.span);
      }
   }

   private final class MappingTarget {
      public final NodeMapping mapping;
      public final SearchAndReplaceTextTarget text;

      public MappingTarget(NodeMapping var2) {
         this.mapping = var2;
         String var3 = var2.getTranslatedExpression(MappingSearchAndReplaceTarget.this.expressionTranslator);
         this.text = new SearchAndReplaceTextTarget(var3);
      }

      public void replace(SearchTarget.Match var1, String var2) {
         this.text.replace(var1, var2);
         String var3 = MappingSearchAndReplaceTarget.this.expressionTranslator.fromDisplay(this.text.getText());
         MappingSearchAndReplaceTarget.this.treeMapper.getMappingManager().replaceMapping(var3, this.mapping.getTargetPath().toString());
      }
   }
}
