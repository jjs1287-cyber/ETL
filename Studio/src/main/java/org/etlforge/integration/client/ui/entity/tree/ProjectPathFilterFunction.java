package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPathFilter;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter2;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.MemoizingKongaTreeFilter;

public final class ProjectPathFilterFunction implements Function<String, KongaTreeFilter> {
   private final Set<IntegrationProject> projects;

   public ProjectPathFilterFunction(IntegrationProject var1) {
      this.projects = Sets.newHashSet(var1);
   }

   public ProjectPathFilterFunction(Collection<IntegrationProject> var1) {
      this.projects = Sets.newHashSet(var1);
   }

   public KongaTreeFilter apply(String var1) {
      if (var1.trim().isEmpty()) {
         return null;
      }

      ProjectPathFilterFunction.FilterImpl var2 = new ProjectPathFilterFunction.FilterImpl(this.projects, var1);
      return new MemoizingKongaTreeFilter(var2);
   }

   private static class FilterImpl implements KongaTreeFilter2 {
      private final MatchingItemsTreeFilter impl;

      public FilterImpl(Set<IntegrationProject> var1, String var2) {
         Set var3 = this.collectItems(var1, var2);
         this.impl = new MatchingItemsTreeFilter(var3);
      }

      private Set<IntegrationEntity> collectItems(Set<IntegrationProject> var1, String var2) {
         HashSet var3 = null;

         for (IntegrationProject var5 : var1) {
            Set var6 = this.collectItemsFromProject(var5, var2);
            if (var6 != null) {
               if (var3 == null) {
                  var3 = Sets.newHashSet();
               }

               var3.addAll(var6);
            }
         }

         return var3;
      }

      private Set<IntegrationEntity> collectItemsFromProject(IntegrationProject var1, String var2) {
         Set var3 = null;
         if (var2.contains("/")) {
            var3 = this.runProjectPathQuery(var1, var2);
         }

         if (var3 == null || var3.isEmpty()) {
            var3 = this.collectItemsWithMatchingNames(var1, var2);
         }

         return var3;
      }

      private Set<IntegrationEntity> runProjectPathQuery(IntegrationProject var1, String var2) {
         ProjectPathFilter var3 = new ProjectPathFilter(var2);
         return var3.apply(var1);
      }

      private Set<IntegrationEntity> collectItemsWithMatchingNames(IntegrationProject var1, String var2) {
         HashSet var3 = Sets.newHashSet(var1.getAllEntities());
         var3.add(var1);
         StringMatcherPredicate var4 = new StringMatcherPredicate(StringMatchers.CONTAINS_IGNORE_CASE, var2.trim());
         EntityFilter var5 = EntityFilters.nameFilter(var4);
         Iterables2.removeIfNot(var3, var5);
         return var3;
      }

      @Override
      public boolean apply(KongaTreeNode var1) {
         return this.impl.apply(var1);
      }

      @Override
      public void hasBeenApplied(KongaTree var1) {
         this.impl.hasBeenApplied(var1);
      }
   }
}
