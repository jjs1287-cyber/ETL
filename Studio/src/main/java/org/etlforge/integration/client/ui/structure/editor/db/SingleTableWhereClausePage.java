package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.SqlScript;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.ThreeValuedBool;

public final class SingleTableWhereClausePage extends AbstractWizardPage implements DatabaseStructureWizardPage {
   private final SingleTableWhereClausePageUi ui;
   private final DatabaseLocation dbLocation;
   private final DatabaseObject incomingObject;
   private Map<DatabaseObject, WizardPage> nextPageCache;
   private ThreeValuedBool updateSourceTableApplicable;

   public SingleTableWhereClausePage(Wizard var1, DatabaseLocation var2, DatabaseObject var3) {
      super(var1, PackageResources.SingleTableWhereClausePage.SUB_TITLE);
      this.dbLocation = Preconditions.checkNotNull(var2, "db");
      this.incomingObject = Preconditions.checkNotNull(var3, "object");
      this.ui = new SingleTableWhereClausePageUi(var3);
      this.ui.setBeginEndQuotes(var2.getBeginEndQuote());
      this.ui.installUndoRedo(this.getUndoRedoInstaller());
      this.updateSourceTableApplicable = ThreeValuedBool.MAYBE;
      this.setCanContinue(true);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         PackageResources.SingleTableWhereClausePage.MAIN_CAPTION, PackageResources.SingleTableWhereClausePage.SUB_CAPTION
      );
      var1.setTransparent(false);
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return true;
   }

   @Override
   public WizardPage getNextPage() {
      WizardPage var1 = this.getCachedNextPage();
      if (var1 == null) {
         var1 = this.createNewNextPage();
      }

      return var1;
   }

   private WizardPage getCachedNextPage() {
      if (this.nextPageCache == null) {
         this.nextPageCache = Maps.newHashMap();
         return null;
      } else {
         return this.nextPageCache.get(this.incomingObject);
      }
   }

   private WizardPage createNewNextPage() {
      if (this.updateSourceTableApplicable.isFalse()) {
         return null;
      }

      WizardPage var1 = null;
      boolean var2 = this.updateSourceTableApplicable.isMaybe();
      if (this.incomingObject.hasPrimaryKeys()) {
         var1 = new UpdateSourceDatabasePage(this.getWizard(), this.incomingObject, null, var2);
      } else {
         var1 = new UpdateKeySelectionPage(this.getWizard(), this.incomingObject, var2);
      }

      this.nextPageCache.put(this.incomingObject, var1);
      return var1;
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      SqlScript var2 = new SqlScript();
      var2.setPrimaryKeysEntry(this.incomingObject);
      var2.setSqlStatement(this.incomingObject, this.dbLocation.getBeginEndQuote(), this.ui.getWhereClause());
      var1.setSqlScript(var2);
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      DatabaseObject var2 = this.getObjectToEdit(var1);
      if (var2 != null) {
         SqlScript var3 = var1.getStructure().getSqlScript();
         this.ui.setWhereClause(var3.getWhereClause());
         this.ui.setBeginEndQuotes(var1.getStructure().getBeginEndQuote());
      }
   }

   private DatabaseObject getObjectToEdit(StructureToEdit var1) {
      for (DatabaseObject var5 : var1.getObjects()) {
         if (var5.equals(this.incomingObject)) {
            return var5;
         }
      }

      return null;
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   public void setUpdateSourceTableEnabled(ThreeValuedBool var1) {
      this.updateSourceTableApplicable = var1;
   }
}
