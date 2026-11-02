package org.etlforge.integration.data.script;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSortedMap.Builder;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.script.ProjectGuidPathCache;
import org.etlforge.integration.data.project.IntegrationProject;

public final class FunctionRegistry {
   private static FunctionRegistry INSTANCE = new FunctionRegistry();
   private final ImmutableList<FunctionCategory> categories;
   private final ImmutableSortedMap<String, Function> nameMap;
   private final ImmutableMap<String, ProjectItemArgDescriptor> projectItemArgDescriptors;
   private final FunctionRegistry.CaseInsentiveNameMap caseInsensitiveNameMap = new FunctionRegistry.CaseInsentiveNameMap();
   private final ImmutableSet<String> functionsWithProjectItems;
   private final FunctionWizards wizards = new FunctionWizards();

   private FunctionRegistry() {
      FunctionLoader var1 = new FunctionLoader();
      this.categories = var1.load();
      this.nameMap = this.buildFunctionMap();
      this.projectItemArgDescriptors = var1.getProjectItemArgDescriptors();
      this.functionsWithProjectItems = this.buildFunctionsWithProjectItems();
   }

   private ImmutableSortedMap<String, Function> buildFunctionMap() {
      Builder var1 = ImmutableSortedMap.orderedBy(FunctionNameComparator.INSTANCE);

      for (FunctionCategory var3 : this.categories) {
         for (Function var5 : var3.getFunctions()) {
            var1.put(var5.getName(), var5);
            this.caseInsensitiveNameMap.add(var5);
         }
      }

      return var1.build();
   }

   private ImmutableSet<String> buildFunctionsWithProjectItems() {
      HashSet var1 = Sets.newHashSet();

      for (Function var3 : this.nameMap.values()) {
         if (var3 instanceof ProjectItemFunction) {
            var1.add(var3.getName().toUpperCase());
         }
      }

      return ImmutableSet.copyOf(var1);
   }

   public static FunctionRegistry getRegistry() {
      return INSTANCE;
   }

   public boolean doesFunctionUseProjectItems(String var1) {
      return this.functionsWithProjectItems.contains(var1.toUpperCase());
   }

   public Iterable<FunctionCategory> getCategories() {
      return this.categories;
   }

   public Function getFunctionByName(String var1) {
      return this.caseInsensitiveNameMap.get(StringUtils.removeEnd(var1, "("));
   }

   public ImmutableList<String> getDisplayNames() {
      return ImmutableList.copyOf(this.nameMap.keySet());
   }

   public Iterable<ProjectItemFunction> getProjectItemFunctions() {
      Predicate var1 = Predicates.in(this.functionsWithProjectItems);
      return Iterables.filter(this.caseInsensitiveNameMap.matching(var1), ProjectItemFunction.class);
   }

   public Optional<String> getPrefixForProjectItemArgumentId(Class<? extends IntegrationEntityId> var1) {
      for (ProjectItemArgDescriptor var3 : this.projectItemArgDescriptors.values()) {
         if (var3.matches(var1)) {
            return Optional.of(var3.getPrefix());
         }
      }

      return Optional.absent();
   }

   public Optional<Class<? extends IntegrationEntityId>> getProjectItemIdClassForPrefix(String var1) {
      ProjectItemArgDescriptor var2 = this.projectItemArgDescriptors.get(var1);
      Class var3 = var2 == null ? null : var2.getIdClass();
      return Optional.fromNullable(var3);
   }

   public Iterable<String> getAllProjectItemArgumentPrefixes() {
      return Iterables.transform(this.projectItemArgDescriptors.values(), ProjectItemArgDescriptor.toPrefix());
   }

   public String getFunctionInfo(String var1) {
      StringBuilder var2 = new StringBuilder("<html>");
      Function var3 = this.caseInsensitiveNameMap.get(var1);
      if (var3 != null) {
         String var4 = StringEscapeUtils.escapeHtml(var3.getDisplayName());
         var2.append("<code><font size=\"4\"><b>").append(var4).append("</b></font></code><br><br><hr>");
         var2.append(var3.getDocumentation());
      }

      var2.append("</html>");
      return var2.toString();
   }

   public ProjectGuidPathCache buildProjectBuidPathCache(IntegrationProject var1) {
      return new ProjectGuidPathCache(var1, this.projectItemArgDescriptors.values());
   }

   public boolean hasWizard(Function var1) {
      return this.wizards.hasFunctionWizard(var1);
   }

   public FunctionInserter getFunctionInserter(Function var1) {
      return this.wizards.getFunctionWizard(var1);
   }

   private static class CaseInsentiveNameMap {
      private final Map<String, Function> functions = Maps.newHashMap();

      private CaseInsentiveNameMap() {
      }

      public void add(Function var1) {
         this.functions.put(var1.getName().toUpperCase(), var1);
      }

      public Function get(String var1) {
         return this.functions.get(var1.toUpperCase());
      }

      public Iterable<Function> matching(Predicate<String> var1) {
         return Maps.filterKeys(this.functions, var1).values();
      }
   }
}
