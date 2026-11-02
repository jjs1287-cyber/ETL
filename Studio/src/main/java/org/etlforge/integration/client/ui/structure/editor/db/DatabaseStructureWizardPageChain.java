package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.LinkedList;
import java.util.Set;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.system.ThreeValuedBool;

public final class DatabaseStructureWizardPageChain implements WizardPageChain {
   private final SourceTarget sourceTarget;
   private final Set<WizardPage> visitedPages = Sets.newHashSet();
   private final DatabaseStructureWizardPage startPage;
   private final LinkedList<WizardPage> pages;
   private StructureToEdit toEdit;
   private final Locatable database;
   private final DbWizardMode mode;

   public DatabaseStructureWizardPageChain(Wizard var1, Source var2) {
      this(var1, var2, SourceTarget.Source);
   }

   public DatabaseStructureWizardPageChain(ApplicationWindowWizard var1, Target var2) {
      this(var1, var2, SourceTarget.Target);
   }

   public DatabaseStructureWizardPageChain(ApplicationWindowWizard var1, EntityExplorerSupport var2, SourceTarget var3) {
      this.sourceTarget = var3;
      this.pages = new LinkedList<>();
      this.startPage = var3 == SourceTarget.Source ? new DatabaseSourceStartPage(var1, var2) : new DatabaseTargetStartPage(var1, var2);
      this.mode = this.configureWizardMode(true);
      this.database = null;
      this.addStartPage();
   }

   private DatabaseStructureWizardPageChain(Wizard var1, Locatable var2, SourceTarget var3) {
      this.sanityCheckOfProvidedDatabase(var2);
      this.sourceTarget = var3;
      this.pages = new LinkedList<>();
      this.database = var2;
      this.mode = this.configureWizardMode(false);
      if (this.isManualSqlStatementSupported(var2)) {
         ManualSqlStatementChoicePage var4 = new ManualSqlStatementChoicePage(var1, (Source)var2);
         if (this.mode == DbWizardMode.MINIMAL) {
            var4.setSingleTableOnly();
         }

         this.startPage = var4;
      } else {
         this.startPage = this.createObjectSelectionPage(var1, var2, var3);
      }

      this.addStartPage();
   }

   private DbWizardMode configureWizardMode(boolean var1) {
      DbWizardMode var2 = DbWizardMode.readFromAppConfig();
      if (var1 && var2 == DbWizardMode.MINIMAL) {
         this.useMinimalMode();
      }

      return var2;
   }

   public void useMinimalMode() {
      if (this.sourceTarget == SourceTarget.Source) {
         this.setUpdateSourceTableAvailable(ThreeValuedBool.FALSE);
      }

      if (this.startPage instanceof DatabaseSourceStartPage) {
         ((DatabaseSourceStartPage)this.startPage).setSingleTableOnly();
      } else if (this.startPage instanceof DatabaseTargetStartPage) {
         ((DatabaseTargetStartPage)this.startPage).setSingleTableOnly();
      } else {
         if (!(this.startPage instanceof ManualSqlStatementChoicePage)) {
            throw new RuntimeException("Unexpected start page: " + this.startPage.getClass());
         }

         ((ManualSqlStatementChoicePage)this.startPage).setSingleTableOnly();
      }
   }

   private void addStartPage() {
      this.visitedPages.add(this.startPage);
      this.pages.add(this.startPage);
   }

   private DatabaseStructureWizardPage createObjectSelectionPage(Wizard var1, Locatable var2, SourceTarget var3) {
      SourceId var4 = null;
      TargetId var5 = null;
      if (var2 instanceof Source) {
         var4 = (SourceId)var2.getID();
      } else {
         var5 = (TargetId)var2.getID();
      }

      boolean var6 = this.mode == DbWizardMode.MINIMAL;
      DatabaseTableSelectionPage var7 = new DatabaseTableSelectionPage(var1, (DatabaseLocation)var2.getLocation(), var4, var5, var3, var6);
      if (this.mode == DbWizardMode.MINIMAL) {
         var7.setUpdateSourceTableAvailable(ThreeValuedBool.FALSE);
      }

      return var7;
   }

   private boolean isManualSqlStatementSupported(Locatable var1) {
      if (var1 instanceof Source) {
         DatabaseLocation var2 = (DatabaseLocation)var1.getLocation();
         DriverDescriptor var3 = var2.getDriver();
         if (var3 != null) {
            return var3.isManualSqlStatementSupported();
         }
      }

      return false;
   }

   private void sanityCheckOfProvidedDatabase(Locatable var1) {
      Preconditions.checkArgument(var1.getLocation() instanceof DatabaseLocation, "db is not a database");
   }

   public void editStructure(StructureToEdit var1) {
      this.toEdit = Preconditions.checkNotNull(var1);
      this.startPage.setStructureToEdit(var1);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.startPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.pages.getLast();
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      while (this.pages.getLast() != var1) {
         this.pages.removeLast();
      }

      WizardPage var2 = ((DatabaseStructureWizardPage)var1).getNextPage();
      if (var2 != null) {
         this.prepareNextPage(var2);
         this.pages.addLast(var2);
      }

      return var2;
   }

   private void prepareNextPage(WizardPage var1) {
      if (this.toEdit != null) {
         if (!this.visitedPages.contains(var1)) {
            if (var1 instanceof DatabaseTableSelectionPage) {
               this.applyMissingQuoteFixIfNecessary();
            }

            ((DatabaseStructureWizardPage)var1).setStructureToEdit(this.toEdit);
            this.visitedPages.add(var1);
         }
      }
   }

   private void applyMissingQuoteFixIfNecessary() {
      DatabaseLocation var1 = this.toEdit.getStructure().getDbLocation();
      BeginEndQuote var2 = this.deriveBeginEndQuotesForEditedStructure(var1);
      if (this.toEdit.getStructure().getIncludeSchemaName()) {
         MissingQuoteFix var3 = new MissingQuoteFix(this.toEdit, var2);
         if (var3.isApplicable()) {
            this.toEdit = var3.apply();
         }
      }
   }

   private BeginEndQuote deriveBeginEndQuotesForEditedStructure(DatabaseLocation var1) {
      BeginEndQuote var2 = var1.getBeginEndQuote();
      if (BeginEndQuote.BLANK.equals(var2)) {
         var2 = BeginEndQuoteGuesser.guess(this.toEdit.getObjects());
      }

      return var2;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.startPage) {
         return null;
      }

      WizardPage var2 = this.startPage;

      for (WizardPage var4 : this.pages) {
         if (var4 == var1) {
            return var2;
         }

         var2 = var4;
      }

      return null;
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return !this.isLastPage(var1);
   }

   private boolean isLastPage(WizardPage var1) {
      return var1 instanceof UpdateSourceDatabasePage || var1 instanceof MultipleTableWhereClausePage || var1 instanceof TargetInsertUpdateModePage;
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 != this.startPage;
   }

   public DatabaseStructure getDefinedStructure() {
      DatabaseStructure var1 = new DatabaseStructure();

      for (WizardPage var3 : this.pages) {
         ((DatabaseStructureWizardPage)var3).applyTo(var1);
      }

      if (this.database != null) {
         var1.setDbLocation((DatabaseLocation)this.database.getLocation());
         if (this.database instanceof Source) {
            var1.setSourceId((SourceId)this.database.getID());
         } else {
            var1.setTargetId((TargetId)this.database.getID());
         }
      }

      return var1;
   }

   public Locatable getDatabase() {
      if (this.database != null) {
         return this.database;
      }

      IntegrationEntity var1 = ((EntitySelectionPage)this.startPage).getSelectedEntity();
      return (Locatable)var1;
   }

   public void applyToEditedTransformation() throws WizardPageException {
      Preconditions.checkState(this.toEdit != null, "This wizard is not editing an existing transformation");
      Transformation var1 = this.toEdit.getTransformation();
      DatabaseStructure var2 = this.getDefinedStructure();
      if (this.sourceTarget == SourceTarget.Source) {
         var1.setInputStructure(var2);
         var1.setSourceId(var2.getSourceId());
      } else {
         var1.setOutputStructure(var2);
         var1.setTargetId(var2.getTargetId());
         this.applyInsertMode(var1);
      }
   }

   public void applyInsertMode(Transformation var1) {
      for (WizardPage var3 : this.pages) {
         if (var3 instanceof TargetInsertUpdateModePage) {
            var1.setDbInsertMode(((TargetInsertUpdateModePage)var3).getInsertMode());
            break;
         }
      }
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1.canContinue() && this.isLastPage(var1);
   }

   public void setUpdateSourceTableAvailable(ThreeValuedBool var1) {
      Preconditions.checkState(this.sourceTarget == SourceTarget.Source, "This option is only available when configuring asource structure");
      if (this.mode == DbWizardMode.MINIMAL) {
         var1 = ThreeValuedBool.FALSE;
      }

      if (this.startPage instanceof DatabaseSourceStartPage) {
         ((DatabaseSourceStartPage)this.startPage).setUpdateSourceTableAvailable(var1);
      } else if (this.startPage instanceof DatabaseTableSelectionPage) {
         ((DatabaseTableSelectionPage)this.startPage).setUpdateSourceTableAvailable(var1);
      } else {
         if (!(this.startPage instanceof ManualSqlStatementChoicePage)) {
            throw new RuntimeException("Unexpected start page: " + this.startPage.getClass());
         }

         ((ManualSqlStatementChoicePage)this.startPage).setUpdateSourceTableAvailable(var1);
      }
   }
}
