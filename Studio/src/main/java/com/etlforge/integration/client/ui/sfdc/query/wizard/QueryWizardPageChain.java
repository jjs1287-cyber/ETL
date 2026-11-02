package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflow;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionController;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import com.etlforge.integration.client.ui.sfdc.wizard.AbstractSalesforceWizardPageChain;
import com.etlforge.integration.client.ui.sfdc.wizard.AutoMapperConfig;
import com.etlforge.integration.client.ui.sfdc.wizard.ObjectSelectionPage;
import com.etlforge.integration.client.ui.sfdc.wizard.TargetLocationPage;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.QueryBuilderModel;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.QueryBuilderPage;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.relational.RelationalFieldSelector;
import java.util.Map;
import java.util.Set;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.EnumSelectionPage;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.salesforce.wsdl.ObjectMethodFilter;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiThreadSupplier;

final class QueryWizardPageChain extends AbstractSalesforceWizardPageChain<SalesforceQuery> {
   private final QueryTargetConfig targetConfig;
   private ObjectSelectionModel objectSelectionModel;
   private ObjectSelectionPage objectSelectionPage;
   private final QueryBuilderModel queryBuilderModel;
   private final SupportedQueryPolicy supportedQueryPolicy;
   private final QueryTester queryTester;
   private QueryBuilderPage queryBuilderPage;
   private TargetLocationPage targetLocationPage;
   private DataLocationType targetLocationType;
   private EnumSelectionPage<QueryTargetType> structureTypePage;
   private QueryTargetType targetStructureType;
   private WizardPageChain targetChain;
   private final Map<QueryWizardPageChain.ExistingTargetChainsKey, WizardPageChain> targetChains = Maps.newHashMap();
   private QueryWizardPageChain.NextPage nextPage = new QueryWizardPageChain.AfterLogin();
   private SoapWizardWorkflow<SalesforceQuery> objects;

   public QueryWizardPageChain(
      QueryWizard var1, ApplicationWindow var2, QueryBuilderModel var3, SupportedQueryPolicy var4, QueryTester var5, QueryTargetConfig var6, boolean var7
   ) {
      super(var1, var7);
      this.queryBuilderModel = var3;
      this.supportedQueryPolicy = var4;
      this.queryTester = var5;
      this.targetConfig = var6;
      this.setAutoMapperConfig(this.createAutoMapperConfig());
   }

   private AutoMapperConfig createAutoMapperConfig() {
      return new AutoMapperConfig() {
         @Override
         public String getMessage() {
            return "<html>When you finish this wizard you will be asked to map the returned data from Salesforce to your target structure.<br>To save time, ETLForge Connect\u2122 can automatically create mappings between similar field names.<br>You can edit these mappings.</html>";
         }

         @Override
         public boolean getDefaultValue() {
            return QueryWizardPageChain.this.targetLocationType != DataLocationType.Database;
         }
      };
   }

   protected QueryWizard getWizard() {
      return (QueryWizard)super.getWizard();
   }

   private static ObjectSelectionModel createObjectSelectionModel() {
      ObjectSelectionModel var0 = new ObjectSelectionModel();
      var0.setObjectFilter(new ObjectMethodFilter(SfdcMethod.QUERY));
      return var0;
   }

   @Override
   protected boolean isLastRequiredPage(WizardPage var1) {
      return this.targetChain != null && this.targetChain.canFinish(var1);
   }

   public void apply() {
      this.applyAutoMapSetting();
      this.configureQuery();
   }

   private void configureQuery() {
      if (this.objects == null) {
         this.objects = this.createObjectSummary();
      }
   }

   private SoapWizardWorkflow<SalesforceQuery> createObjectSummary() {
      return UiThreadSupplier.call(
         new Supplier<SoapWizardWorkflow<SalesforceQuery>>() {
            public SoapWizardWorkflow<SalesforceQuery> get() {
               QueryTarget var1 = this.getQueryTarget();
               Target var2 = QueryWizardPageChain.this.targetLocationPage.getLocation();
               Options var3 = this.getOptions();
               return QueryWizardPageChain.this.getWizard().buildWorkflow(var1, var2, var3);
            }

            private QueryTarget getQueryTarget() {
               QueryTarget var1 = QueryWizardPageChain.this.targetStructureType
                  .createStructure(QueryWizardPageChain.this.getWizard(), QueryWizardPageChain.this.targetChain);
               return new UiThreadSafeQueryTarget(var1);
            }

            private Options getOptions() {
               Schedule var1 = QueryWizardPageChain.this.getSchedule();
               boolean var2 = QueryWizardPageChain.this.targetLocationPage.isUTF8();
               return new Options(var1, var2);
            }
         }
      );
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      this.setSection(this.nextPage.section);
      WizardPage var2 = this.nextPage.page();
      this.nextPage = this.nextPage.next();
      if (var2 == null && this.nextPage != null) {
         this.setSection(this.nextPage.section);
         var2 = this.nextPage.page();
         this.nextPage = this.nextPage.next();
      }

      return var2;
   }

   private WizardPageChain getTargetChain(QueryTargetType var1, Target var2) {
      QueryWizardPageChain.ExistingTargetChainsKey var3 = new QueryWizardPageChain.ExistingTargetChainsKey(var1, var2);
      WizardPageChain var4 = this.targetChains.get(var3);
      if (var4 == null) {
         var4 = var1.createPages(this.getWizard(), var2);
         this.targetChains.put(var3, var4);
      }

      return var4;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.getLoginPage()) {
         return null;
      }

      if (var1 == this.objectSelectionPage) {
         this.nextPage = new QueryWizardPageChain.AfterLogin();
         return this.moveToLoginPage();
      }

      if (var1 == this.queryBuilderPage) {
         this.nextPage = new QueryWizardPageChain.AfterObjectSelection();
         return this.objectSelectionPage;
      }

      if (var1 == this.targetLocationPage) {
         this.setSection("Query");
         this.nextPage = new QueryWizardPageChain.AfterQueryBuilderPage();
         return this.queryBuilderPage;
      }

      if (var1 == this.structureTypePage) {
         this.nextPage = new QueryWizardPageChain.AfterTargetLocationPage();
         return this.targetLocationPage;
      }

      if (var1 == this.targetChain.getFirstPage()) {
         if (this.structureTypePage == null) {
            this.nextPage = new QueryWizardPageChain.AfterTargetLocationPage();
            return this.targetLocationPage;
         } else {
            this.nextPage = new QueryWizardPageChain.AfterStructureTypePage();
            return this.structureTypePage;
         }
      } else if (var1 == this.getScheduleAndOptionsPage()) {
         this.setSection("Target");
         WizardPage var3 = this.targetChain.getLastPage();
         this.nextPage = new QueryWizardPageChain.InTargetChainPage(var3);
         return var3;
      } else if (var1 == this.getSummaryPage()) {
         this.nextPage = new QueryWizardPageChain.Finish();
         return this.moveToScheduleAndOptionsPage();
      } else {
         WizardPage var2 = this.targetChain.getPreviousPage(var1);
         this.nextPage = new QueryWizardPageChain.InTargetChainPage(var2);
         return var2;
      }
   }

   private class AfterLogin extends QueryWizardPageChain.NextPage {
      public AfterLogin() {
         super("Query");
      }

      @Override
      protected WizardPage page() {
         this.moveToObjectSelectionPage();
         return QueryWizardPageChain.this.objectSelectionPage;
      }

      private WizardPage moveToObjectSelectionPage() {
         if (QueryWizardPageChain.this.objectSelectionPage == null) {
            QueryWizardPageChain.this.objectSelectionModel = QueryWizardPageChain.createObjectSelectionModel();
            ObjectSelectionController var1 = QueryWizardPageChain.this.getWizard().getObjectSelectionController(QueryWizardPageChain.this.objectSelectionModel);
            QueryWizardPageChain.this.objectSelectionPage = new ObjectSelectionPage(
               QueryWizardPageChain.this.getWizard(), QueryWizardPageChain.this.objectSelectionModel, var1
            );
         }

         return QueryWizardPageChain.this.objectSelectionPage;
      }

      @Override
      protected QueryWizardPageChain.NextPage next() {
         return QueryWizardPageChain.this.new AfterObjectSelection();
      }
   }

   private class AfterObjectSelection extends QueryWizardPageChain.NextPage {
      public AfterObjectSelection() {
         super("Query");
      }

      @Override
      protected WizardPage page() {
         SObjectSchema var1 = QueryWizardPageChain.this.objectSelectionModel.getSelectedObject();
         QueryWizardPageChain.this.getWizard().setSalesforceObject(var1);
         QueryWizardPageChain.this.queryBuilderModel.setSObject(var1);
         if (QueryWizardPageChain.this.queryBuilderPage == null) {
            QueryWizardPageChain.this.queryBuilderPage = new QueryBuilderPage(
               QueryWizardPageChain.this.getWizard(), QueryWizardPageChain.this.queryBuilderModel, QueryWizardPageChain.this.supportedQueryPolicy
            );
            this.installRelationalFieldSelector();
            QueryWizardPageChain.this.queryBuilderPage.setQueryTester(QueryWizardPageChain.this.queryTester);
            QueryWizardPageChain.this.queryBuilderPage.setSchemaProviderForQueryCorrection(QueryWizardPageChain.this.getWizard().getSchemaProvider());
         }

         return QueryWizardPageChain.this.queryBuilderPage;
      }

      private void installRelationalFieldSelector() {
         RelationalFieldSelector var1 = QueryWizardPageChain.this.getWizard().createRelationalFieldSelector(QueryWizardPageChain.this.queryBuilderModel);
         QueryWizardPageChain.this.queryBuilderPage.setRelationalFieldSelector(var1);
      }

      @Override
      protected QueryWizardPageChain.NextPage next() {
         return QueryWizardPageChain.this.new AfterQueryBuilderPage();
      }
   }

   private class AfterQueryBuilderPage extends QueryWizardPageChain.NextPage {
      public AfterQueryBuilderPage() {
         super("Target");
      }

      @Override
      protected WizardPage page() {
         ParsedQuery var1 = QueryWizardPageChain.this.queryBuilderPage.getParsedQuery();
         QueryWizardPageChain.this.getWizard().setParsedQuery(var1, QueryWizardPageChain.this.queryBuilderModel.isQueryAll());
         if (QueryWizardPageChain.this.targetLocationPage == null) {
            QueryWizardPageChain.this.targetLocationPage = new TargetLocationPage(
               QueryWizardPageChain.this.getWizard(),
               QueryWizardPageChain.this.targetConfig.getLocationTypes(),
               new EditorPanel(QueryWizardPageChain.this.getWizard().getWindow())
            );
            QueryWizardPageChain.this.targetLocationPage.setFileExtensions(QueryWizardPageChain.this.targetConfig.getFileExtensions());
         }

         return QueryWizardPageChain.this.targetLocationPage;
      }

      @Override
      protected QueryWizardPageChain.NextPage next() {
         return QueryWizardPageChain.this.new AfterTargetLocationPage();
      }
   }

   private class AfterStructureTypePage extends QueryWizardPageChain.NextPage {
      public AfterStructureTypePage() {
         super("Target");
      }

      @Override
      protected WizardPage page() {
         QueryWizardPageChain.this.targetStructureType = QueryWizardPageChain.this.structureTypePage.getSelectedValue();
         QueryWizardPageChain.this.targetChain = QueryWizardPageChain.this.getTargetChain(
            QueryWizardPageChain.this.targetStructureType, QueryWizardPageChain.this.targetLocationPage.getLocation()
         );
         return QueryWizardPageChain.this.targetChain.getFirstPage();
      }

      @Override
      protected QueryWizardPageChain.NextPage next() {
         return QueryWizardPageChain.this.new InTargetChainPage(QueryWizardPageChain.this.targetChain.getFirstPage());
      }
   }

   private class AfterTargetLocationPage extends QueryWizardPageChain.NextPage {
      private QueryWizardPageChain.NextPage next;

      public AfterTargetLocationPage() {
         super("Target");
      }

      @Override
      protected WizardPage page() {
         QueryWizardPageChain.this.targetLocationType = QueryWizardPageChain.this.targetLocationPage.getLocationType();
         Set var1 = this.getPossibleStructureTypes();
         if (var1.size() == 1) {
            QueryWizardPageChain.this.structureTypePage = null;
            QueryWizardPageChain.this.targetStructureType = (QueryTargetType)Iterables.get(var1, 0);
            QueryWizardPageChain.this.targetChain = QueryWizardPageChain.this.getTargetChain(
               QueryWizardPageChain.this.targetStructureType, QueryWizardPageChain.this.targetLocationPage.getLocation()
            );
            WizardPage var2 = QueryWizardPageChain.this.targetChain.getFirstPage();
            this.next = QueryWizardPageChain.this.new InTargetChainPage(var2);
            return var2;
         } else {
            QueryWizardPageChain.this.structureTypePage = EnumSelectionPage.of(QueryWizardPageChain.this.getWizard(), "Select File Type", var1);
            this.next = QueryWizardPageChain.this.new AfterStructureTypePage();
            return QueryWizardPageChain.this.structureTypePage;
         }
      }

      private Set<QueryTargetType> getPossibleStructureTypes() {
         ImmutableSet var1 = QueryWizardPageChain.this.targetConfig.getStructureTypes(QueryWizardPageChain.this.targetLocationType);
         if (var1.size() > 1 && QueryWizardPageChain.this.targetLocationType.isFileBased()) {
            Target var2 = QueryWizardPageChain.this.targetLocationPage.getLocation();
            Optional var3 = FileBasedStructureTypeDeducer.deduceType(var2, QueryWizardPageChain.this.targetConfig);
            if (var3.isPresent()) {
               return ImmutableSet.of((QueryTargetType)var3.get());
            }
         }

         return var1;
      }

      @Override
      protected QueryWizardPageChain.NextPage next() {
         return this.next;
      }
   }

   private static final class ExistingTargetChainsKey {
      private final QueryTargetType type;
      private final Target target;

      public ExistingTargetChainsKey(QueryTargetType var1, Target var2) {
         this.type = var1;
         this.target = var2;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (var1 instanceof QueryWizardPageChain.ExistingTargetChainsKey) {
            QueryWizardPageChain.ExistingTargetChainsKey var2 = (QueryWizardPageChain.ExistingTargetChainsKey)var1;
            if (this.type == var2.type) {
               if (this.type == QueryTargetType.DATABASE) {
                  return this.target == var2.target;
               }

               return true;
            }
         }

         return false;
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 37 * var1 + this.type.hashCode();
         if (this.type == QueryTargetType.DATABASE) {
            var1 = 37 * var1 + this.target.hashCode();
         }

         return var1;
      }
   }

   private class Finish extends QueryWizardPageChain.NextPage {
      public Finish() {
         super("Summary");
      }

      @Override
      protected WizardPage page() {
         QueryWizardPageChain.this.objects = QueryWizardPageChain.this.createObjectSummary();
         return QueryWizardPageChain.this.moveToSummaryPage(QueryWizardPageChain.this.objects);
      }

      @Override
      protected QueryWizardPageChain.NextPage next() {
         return null;
      }
   }

   private class InTargetChainPage extends QueryWizardPageChain.NextPage {
      private final WizardPage currentPage;
      private QueryWizardPageChain.NextPage next;

      public InTargetChainPage(WizardPage var2) {
         super("Target");
         this.currentPage = var2;
      }

      @Override
      protected WizardPage page() {
         WizardPage var1 = QueryWizardPageChain.this.targetChain.getNextPage(this.currentPage);
         if (var1 != null) {
            this.next = QueryWizardPageChain.this.new InTargetChainPage(var1);
            return var1;
         } else {
            this.next = QueryWizardPageChain.this.new ToScheduleAndOptions();
            return null;
         }
      }

      @Override
      protected QueryWizardPageChain.NextPage next() {
         return this.next;
      }
   }

   private abstract static class NextPage {
      public final String section;

      public NextPage(String var1) {
         this.section = var1;
      }

      protected abstract WizardPage page();

      protected abstract QueryWizardPageChain.NextPage next();
   }

   private class ToScheduleAndOptions extends QueryWizardPageChain.NextPage {
      public ToScheduleAndOptions() {
         super("Schedule");
      }

      @Override
      protected WizardPage page() {
         return QueryWizardPageChain.this.asScheduleAndOptionsPageSupplier().get();
      }

      @Override
      protected QueryWizardPageChain.NextPage next() {
         return QueryWizardPageChain.this.hasSummaryPage() ? QueryWizardPageChain.this.new Finish() : null;
      }
   }
}
