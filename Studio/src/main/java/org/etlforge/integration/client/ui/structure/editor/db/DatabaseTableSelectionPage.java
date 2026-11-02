package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.JPanel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.GoToNextPageAction;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.structure.editor.db.rels.TableRelationshipDefinitionPage;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.ThreeValuedBool;
import org.etlforge.ui.property.AbstractBinding;

public final class DatabaseTableSelectionPage extends AbstractWizardPage implements DatabaseStructureWizardPage {
   private final SourceTarget sourceOrTarget;
   private final TableSelectionModel model;
   private final DatabaseTableSelectionPageUi ui;
   private final DatabaseTableSelectionPage.NextPageFactory nextPageFactory;
   private String mainCaption;
   private String subCaption;
   private ThreeValuedBool updateSourceTableAvailable;
   private volatile StructureToEdit structureToEdit;

   public DatabaseTableSelectionPage(Wizard var1, DatabaseLocation var2, SourceId var3, TargetId var4, SourceTarget var5, boolean var6) {
      super(var1, lookupSubTitle(var5));
      Preconditions.checkNotNull(var2, "db");
      Preconditions.checkNotNull(var5, "st");
      Preconditions.checkArgument(var3 != null || var4 != null, "Both srcId and tgtId cannot be null.");
      this.sourceOrTarget = var5;
      this.model = new TableSelectionModel(var2, var3, var4);
      DownloadAvailableTablesAction var7 = new DownloadAvailableTablesAction(this.model, this.getWaitService());
      this.ui = new DatabaseTableSelectionPageUi(this.model, var7, var6);
      this.ui.setDefaultAction(new GoToNextPageAction(this));
      this.mainCaption = StructureEditorResources.getString("Db.ObjectSelectionPage.MainCaption");
      this.subCaption = StructureEditorResources.getString("Db.ObjectSelectionPage.SubCaption");
      this.nextPageFactory = new DatabaseTableSelectionPage.NextPageFactory();
      this.updateSourceTableAvailable = ThreeValuedBool.MAYBE;
      this.bindProperties();
   }

   private static String lookupSubTitle(SourceTarget var0) {
      Preconditions.checkNotNull(var0, "st");
      return var0 == SourceTarget.Source ? PackageResources.ObjectSelectionPage.SOURCE_SUB_TITLE : PackageResources.ObjectSelectionPage.TARGET_SUB_TITLE;
   }

   private void bindProperties() {
      DatabaseTableSelectionPage.ValidStateBinding var1 = new DatabaseTableSelectionPage.ValidStateBinding();
      var1.syncUi();
   }

   public void setMainCaption(String var1) {
      this.mainCaption = var1;
   }

   public void setSubCaption(String var1) {
      this.subCaption = var1;
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      SelectedTables var2 = this.getSelectedTables();
      var2.applyTo(var1, this.model.getBeginEndQuotes(), this.model.isSchemaNameIncluded());
      var1.setIncludeSchemaName(this.model.isSchemaNameIncluded());
   }

   public SelectedTables getSelectedTables() {
      SelectedTables var1 = this.model.getUsageCount();
      return this.model.isSchemaNameIncluded() ? var1.withSchemaName(this.model.getBeginEndQuotes()) : var1;
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      this.structureToEdit = var1;
      ExistingStructureProcessor var2 = new ExistingStructureProcessor(this.model, var1);
      var2.process();
      this.ui.setRefreshColumnsAction(new RefreshColumnInformationAction(this.model, this.getWaitService()));
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.mainCaption, this.subCaption);
      var1.setInsets(0, 0, 0, 0);
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   public WizardPage getNextPage() {
      return this.nextPageFactory.getNextPage();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      this.updateEditedStructure();
      SelectedTablesPreparer var1 = new SelectedTablesPreparer(this, this.model);
      return var1.prepare();
   }

   private void updateEditedStructure() {
      if (this.structureToEdit != null) {
         boolean var1 = this.model.isSchemaNameIncluded();
         this.structureToEdit.setIncludeSchema(var1);
      }
   }

   @Override
   protected void dispose() {
      UiJob var1 = this.model.getCurrentServerInteraction();
      if (var1 != null) {
         ApplicationUi.getJobManager().cancelJob(var1);
      }
   }

   public void setUpdateSourceTableAvailable(ThreeValuedBool var1) {
      this.updateSourceTableAvailable = var1;
   }

   private class NextPageFactory {
      private final Map<SelectedTables, WizardPage> nextPageCache = Maps.newHashMap();

      private NextPageFactory() {
      }

      public WizardPage getNextPage() {
         SelectedTables var1 = DatabaseTableSelectionPage.this.getSelectedTables();
         WizardPage var2 = this.nextPageCache.get(var1);
         if (var2 == null) {
            var2 = this.createNewNextPage(var1);
            this.nextPageCache.put(var1, var2);
         }

         return var2;
      }

      private WizardPage createNewNextPage(SelectedTables var1) {
         return var1.isSingleTable() ? this.createNextPageForSingleTable(var1) : this.createNextPageForMultipleTables(var1);
      }

      private WizardPage createNextPageForSingleTable(SelectedTables var1) {
         DatabaseObject[] var2 = var1.getIncludedTables();
         if (DatabaseTableSelectionPage.this.sourceOrTarget == SourceTarget.Source) {
            assert var2.length == 1;
            SingleTableWhereClausePage var3 = new SingleTableWhereClausePage(
               DatabaseTableSelectionPage.this.getWizard(), DatabaseTableSelectionPage.this.model.getDbLocation(), var2[0]
            );
            var3.setUpdateSourceTableEnabled(DatabaseTableSelectionPage.this.updateSourceTableAvailable);
            return var3;
         } else {
            return new TargetInsertUpdateModePage(DatabaseTableSelectionPage.this.getWizard(), var2);
         }
      }

      private WizardPage createNextPageForMultipleTables(SelectedTables var1) {
         BeginEndQuote var2 = DatabaseTableSelectionPage.this.model.getBeginEndQuotes();
         DatabaseObject[] var3 = var1.getTablesIncludingAliases(var2);
         return new TableRelationshipDefinitionPage(DatabaseTableSelectionPage.this.getWizard(), var3, DatabaseTableSelectionPage.this.sourceOrTarget);
      }
   }

   private class ValidStateBinding extends AbstractBinding<SelectedTables> {
      public ValidStateBinding() {
         super(DatabaseTableSelectionPage.this.model, "ho ho ho");
      }

      protected void updateUiWithValue(SelectedTables var1) {
         boolean var2 = !var1.isEmpty();
         DatabaseTableSelectionPage.this.setCanContinue(var2);
      }
   }
}
