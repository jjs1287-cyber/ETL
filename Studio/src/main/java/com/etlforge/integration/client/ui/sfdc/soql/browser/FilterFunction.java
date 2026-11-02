package com.etlforge.integration.client.ui.sfdc.soql.browser;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter2;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.MemoizingKongaTreeFilter;
import org.etlforge.util.ObservableValue;

final class FilterFunction implements Function<String, KongaTreeFilter> {
   private final ObservableValue<SObjectSchema> object;
   private boolean includeChildren = true;
   private boolean includeParents = true;

   public FilterFunction(ObservableValue<SObjectSchema> var1) {
      this.object = var1;
   }

   public void setIncludeParents(boolean var1) {
      this.includeParents = var1;
   }

   public void setIncludeChildren(boolean var1) {
      this.includeChildren = var1;
   }

   public KongaTreeFilter apply(String var1) {
      SObjectSchema var2 = this.object.get();
      if (var2 == null) {
         return null;
      } else {
         KongaTreeFilter var3 = this.buildPathFilter(var1, var2);
         if (var3 == null && this.includeParents && this.includeChildren) {
            return null;
         } else {
            RelationshipFilter var4 = new RelationshipFilter(this.includeParents, this.includeChildren);
            if (var3 == null) {
               return var4;
            } else {
               return var3 == KongaTreeFilter.NONE ? var3 : new MemoizingKongaTreeFilter(new FilterFunction.CombinedFilter(var4, var3));
            }
         }
      }
   }

   private KongaTreeFilter buildPathFilter(String var1, SObjectSchema var2) {
      if (var1.isEmpty()) {
         return null;
      }

      if (var1.trim().isEmpty()) {
         return KongaTreeFilter.NONE;
      }

      char var3 = var1.charAt(0);
      if (Character.isWhitespace(var3)) {
         return KongaTreeFilter.NONE;
      }

      char var4 = var1.charAt(var1.length() - 1);
      boolean var5 = Character.isWhitespace(var4);
      String var6 = var1.trim();
      String var7 = var6.replace(".", " ");
      String var8 = KongaStringUtils.collapseSpaces(var7);
      ArrayList var9 = Lists.newArrayList(Splitter.on(' ').split(var8));
      if (!((String)var9.get(0)).equalsIgnoreCase(var2.getName())) {
         var9.add(0, var2.getName());
      }

      return new PathFilter(var9, var5);
   }

   private static class CombinedFilter implements KongaTreeFilter2 {
      private final RelationshipFilter relation;
      private final KongaTreeFilter path;

      public CombinedFilter(RelationshipFilter var1, KongaTreeFilter var2) {
         this.relation = var1;
         this.path = var2;
      }

      @Override
      public boolean apply(KongaTreeNode var1) {
         return this.relation.apply(var1) && this.path.apply(var1);
      }

      @Override
      public void hasBeenApplied(KongaTree var1) {
         if (this.path instanceof KongaTreeFilter2) {
            ((KongaTreeFilter2)this.path).hasBeenApplied(var1);
         }
      }
   }
}
