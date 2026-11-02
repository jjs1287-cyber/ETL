package org.etlforge.integration.data.script;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.RunOperationFilter;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.functors.FileBasedLocationFilter;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.LocatableFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.util.version.Version;

final class FunctionFactory {
   private final List<FunctionCategory> categories = Lists.newArrayList();
   private FunctionFactory.Category currentCategory;

   public FunctionFactory() {
   }

   public void newCategory(String var1, Version var2) {
      this.currentCategory = new FunctionFactory.Category(var1, var2);
      this.categories.add(this.currentCategory);
   }

   public Function newFunction(
      String var1, Version var2, String var3, String var4, String var5, String var6, String var7, boolean var8, Set<ProjectItemArgDescriptor> var9
   ) {
      if (this.currentCategory == null) {
         throw new IllegalStateException("newFunction called without a category.");
      }

      FunctionFactory.FunctionImpl var10;
      if (var9.isEmpty()) {
         var10 = new FunctionFactory.FunctionImpl(var1, var2, var3, var4, var5, var6, var7, var8);
      } else {
         var10 = new FunctionFactory.ProjectItemFunctionImpl(var1, var2, var3, var4, var5, var6, var7, var8, var9);
      }

      this.currentCategory.addFunction(var10);
      return var10;
   }

   public ImmutableList<FunctionCategory> getCategories() {
      return ImmutableList.copyOf(this.categories);
   }

   private static final class CallStoredProcedureFilter implements EntityFilter {
      private final LocatableFilter wrappee = new LocatableFilter(DataLocationType.Database);

      private CallStoredProcedureFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         if (!this.wrappee.apply(var1)) {
            return false;
         }

         DatabaseLocation var2 = (DatabaseLocation)((Locatable)var1).getLocation();
         DriverDescriptor var3 = var2.getDriver();
         return var3 != null && var3.getType() == DriverType.ODBC;
      }
   }

   private static final class Category implements FunctionCategory {
      private final String name;
      private final Version since;
      private final Set<Function> functions;

      public Category(String var1, Version var2) {
         this.name = var1;
         this.since = var2;
         this.functions = Sets.newTreeSet(new Comparator<Function>() {
            public int compare(Function var1, Function var2x) {
               return FunctionNameComparator.INSTANCE.compare(var1.getName(), var2x.getName());
            }
         });
      }

      @Override
      public Iterable<Function> getFunctions() {
         return this.functions;
      }

      @Override
      public String getName() {
         return this.name;
      }

      @Override
      public Version since() {
         return this.since;
      }

      public void addFunction(Function var1) {
         this.functions.add(var1);
      }
   }

   private static final class CustomItemFilters {
      private final ImmutableMap<String, EntityFilter> filters = this.build();

      private CustomItemFilters() {
      }

      private ImmutableMap<String, EntityFilter> build() {
         HashMap var1 = Maps.newHashMap();
         var1.put("runoperation", new RunOperationFilter());
         addFilters(var1, new FunctionFactory.GetOperationQueueFilter(), "getoperationqueue", "waitforoperation");
         addFilters(var1, new LocatableFilter(DataLocationType.Database), "dblookup", "dbexecute", "cachelookup", "dbcloseconnection", "dbrollbacktransaction");
         var1.put("callstoredprocedure", new FunctionFactory.CallStoredProcedureFilter());
         addFilters(var1, FunctionFactory.FileFilter.source(), "base64encodefile", "filelist", "readfile");
         addFilters(var1, FunctionFactory.FileFilter.target(), "writefile", "flushfile", "flushallfiles");
         var1.put("ldapconnect", new FunctionFactory.LdapConnectFilter());
         return ImmutableMap.copyOf(var1);
      }

      private static void addFilters(Map<String, EntityFilter> var0, EntityFilter var1, String... var2) {
         for (String var6 : var2) {
            var0.put(var6, var1);
         }
      }

      public Optional<EntityFilter> getFilter(FunctionFactory.ProjectItemFunctionImpl var1) {
         String var2 = var1.getName().toLowerCase();
         return Optional.fromNullable(this.filters.get(var2));
      }
   }

   private static final class FileFilter implements EntityFilter {
      private final EntityFilter fileBased = new FileBasedLocationFilter();
      private final EntityType type;

      public static final EntityFilter source() {
         return new FunctionFactory.FileFilter(EntityType.Source);
      }

      public static final EntityFilter target() {
         return new FunctionFactory.FileFilter(EntityType.Target);
      }

      public FileFilter(EntityType var1) {
         this.type = var1;
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1.getEntityType() == this.type && this.fileBased.apply(var1);
      }
   }

   private static class FunctionImpl implements Function {
      private final String name;
      private final Version since;
      private final String displayName;
      private final String documentation;
      private final String format;
      private final FCN grammar;
      private final String returnType;
      private final String insertValue;
      private final boolean isInstanceFunction;
      private final boolean hidden;

      public FunctionImpl(String var1, Version var2, String var3, String var4, String var5, String var6, String var7, boolean var8) {
         this.name = var1;
         this.since = var2;
         this.displayName = var3;
         this.format = var5;
         this.grammar = FunctionGrammarParser.parse(var5);
         this.returnType = var6;
         this.insertValue = var7;
         this.documentation = var4;
         this.hidden = var8;
         this.isInstanceFunction = var5.contains("#");
      }

      @Override
      public String getName() {
         return this.name;
      }

      @Override
      public String getDocumentation() {
         return this.documentation;
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getFormat() {
         return this.format;
      }

      @Override
      public FCN getGrammar() {
         return this.grammar;
      }

      @Override
      public String getInsertValue() {
         return this.insertValue;
      }

      @Override
      public Version since() {
         return this.since;
      }

      @Override
      public boolean isInstanceFunction() {
         return this.isInstanceFunction;
      }

      @Override
      public boolean acceptsProjectItemAsInput(IntegrationEntity var1) {
         return false;
      }

      @Override
      public String toString() {
         return this.displayName;
      }

      @Override
      public boolean isReturnArray() {
         return "array".equals(this.returnType);
      }

      @Override
      public boolean isHidden() {
         return this.hidden;
      }
   }

   private static final class GetOperationQueueFilter implements EntityFilter {
      private GetOperationQueueFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         if (var1 instanceof Operation) {
            Operation var2 = (Operation)var1;
            return !this.isHostedWebService(var2);
         } else {
            return false;
         }
      }

      private boolean isHostedWebService(Operation var1) {
         OperationType var2 = var1.getOperationType();
         return var2 == HostedWebServiceOperationType.TYPE;
      }
   }

   private static final class LdapConnectFilter implements EntityFilter {
      private LdapConnectFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof Target ? ((Target)var1).getLocation() instanceof LdapLocation : false;
      }
   }

   private static class ProjectItemFunctionImpl extends FunctionFactory.FunctionImpl implements ProjectItemFunction {
      private static final FunctionFactory.CustomItemFilters CUSTOM_FILTERS = new FunctionFactory.CustomItemFilters();
      private final ImmutableSet<ProjectItemArgDescriptor> itemArgs;

      public ProjectItemFunctionImpl(
         String var1, Version var2, String var3, String var4, String var5, String var6, String var7, boolean var8, Set<ProjectItemArgDescriptor> var9
      ) {
         super(var1, var2, var3, var4, var5, var6, var7, var8);
         assert !var9.isEmpty();
         this.itemArgs = ImmutableSet.copyOf(var9);
      }

      @Override
      public Optional<? extends IntegrationEntityId> getItemId(String var1) {
         if (!var1.startsWith("\"")) {
            return Optional.absent();
         }

         int var2 = var1.indexOf(46);
         if (var2 == -1) {
            return Optional.absent();
         }

         String var3 = var1.substring(1, var2);

         for (ProjectItemArgDescriptor var5 : this.itemArgs) {
            if (var5.getPrefix().equals(var3)) {
               String var6 = var1.substring(var2 + 1, var1.length() - 1);
               IntegrationEntityId var7 = var5.getIdFromGuidString(var6);
               return Optional.of(var7);
            }
         }

         return Optional.absent();
      }

      @Override
      public boolean acceptsProjectItemAsInput(IntegrationEntity var1) {
         EntityFilter var2 = this.getFilter();
         return var2.apply(var1);
      }

      @Override
      public List<IntegrationEntity> getApplicableProjectItems(IntegrationProject var1) {
         EntityFilter var2 = this.getFilter();
         return KongaListUtils.filter(var1.getAllEntities(), var2);
      }

      private EntityFilter getFilter() {
         return CUSTOM_FILTERS.getFilter(this).or(this.defaultEntityFilter());
      }

      private Supplier<EntityFilter> defaultEntityFilter() {
         return new Supplier<EntityFilter>() {
            public EntityFilter get() {
               return EntityFilters.or(Iterables.transform(ProjectItemFunctionImpl.this.itemArgs, ProjectItemArgDescriptor.toFilter()));
            }
         };
      }
   }
}
