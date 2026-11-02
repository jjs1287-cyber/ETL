package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

public final class UpdateKeySelectionPage extends AbstractWizardPage implements DatabaseStructureWizardPage {
   private final UpdateKeySelectionPageUi ui;
   private final DatabaseObject dbObject;
   private Map<Set<DatabaseColumn>, WizardPage> nextPageCache;
   private final boolean showUpdateSourceDisclaimer;

   public UpdateKeySelectionPage(Wizard var1, DatabaseObject var2, boolean var3) {
      super(var1, PackageResources.PrimaryKeySelectionPage.SUB_TITLE);
      this.dbObject = Preconditions.checkNotNull(var2, "dbObj");
      this.ui = new UpdateKeySelectionPageUi(var2);
      this.showUpdateSourceDisclaimer = var3;
      this.setCanContinue(true);
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      DbFields var2 = new DbFields();
      Set var3 = this.ui.getSelectedColumns();
      var2.addUpdateKeys(var3);
      var1.setDbFields(var2);
      if (var3.isEmpty()) {
         var2.removeEntry(2);
      }
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      DatabaseObject var2 = this.getObjectToEdit(var1);
      if (var2 != null) {
         this.ui.setSelectedColumns(var1.getStructure(), this.dbObject);
      }
   }

   private DatabaseObject getObjectToEdit(StructureToEdit var1) {
      for (DatabaseObject var5 : var1.getObjects()) {
         if (var5.equals(this.dbObject)) {
            return var5;
         }
      }

      return null;
   }

   @Override
   public WizardPage getNextPage() {
      Set var1 = this.ui.getSelectedColumns();
      if (var1.isEmpty()) {
         return null;
      }

      WizardPage var2 = this.getCachedNextPage(var1);
      if (var2 == null) {
         var2 = this.createNewNextPage(var1);
      }

      return var2;
   }

   private WizardPage getCachedNextPage(Set<DatabaseColumn> var1) {
      if (this.nextPageCache == null) {
         this.nextPageCache = Maps.newHashMap();
         return null;
      } else {
         return this.nextPageCache.get(var1);
      }
   }

   private WizardPage createNewNextPage(Set<DatabaseColumn> var1) {
      UpdateSourceDatabasePage var2 = new UpdateSourceDatabasePage(this.getWizard(), this.dbObject, var1, this.showUpdateSourceDisclaimer);
      this.nextPageCache.put(var1, var2);
      return var2;
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         PackageResources.PrimaryKeySelectionPage.MAIN_CAPTION, PackageResources.PrimaryKeySelectionPage.SUB_CAPTION
      );
      var1.add(this.ui, false, true);
      return var1.asWestBorderLayout();
   }
}
