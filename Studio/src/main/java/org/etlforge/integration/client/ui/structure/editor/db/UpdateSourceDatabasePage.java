package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JLabel;
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

public final class UpdateSourceDatabasePage extends AbstractWizardPage implements DatabaseStructureWizardPage {
   private final UpdateSourceDatabasePageUi ui;
   private final DatabaseObject dbObject;
   private final Set<DatabaseColumn> primaryKeys;

   public UpdateSourceDatabasePage(Wizard var1, DatabaseObject var2, Set<DatabaseColumn> var3, boolean var4) {
      super(var1, PackageResources.UpdateSourcePage.SUB_TITLE);
      this.dbObject = Preconditions.checkNotNull(var2, "dbObject");
      this.primaryKeys = var3;
      this.ui = new UpdateSourceDatabasePageUi(var2, var3);
      this.ui.setShowDisclaimer(var4);
      this.setCanContinue(true);
   }

   @Override
   public WizardPage getNextPage() {
      return null;
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      DbFields var2 = var1.getDbFields();
      this.addFieldInformation(var2);
      if (this.primaryKeys != null) {
         var2.addUpdateKeys(this.primaryKeys);
      } else {
         var2.addUpdateKeys(this.dbObject.getPrimaryKeys());
      }

      var1.setDbFields(var2);
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      DatabaseObject var2 = this.getObjectToEdit(var1);
      if (var2 != null) {
         this.ui.populateUi(var1.getStructure(), var2);
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
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(PackageResources.UpdateSourcePage.MAIN_CAPTION, PackageResources.UpdateSourcePage.SUB_CAPTION);
      var1.setInsets(0, 0, 0, 0);
      var1.add(new JLabel(PackageResources.UpdateSourcePage.DESCRIPTION), false, false);
      var1.add(this.ui, false, true);
      return var1.asWestBorderLayout();
   }

   private void addFieldInformation(DbFields var1) {
      ArrayList var2 = Lists.newArrayList();
      ArrayList var3 = Lists.newArrayList();
      this.ui.populateColumnValues(var2, var3);
      if (var2.size() > 0) {
         DatabaseColumn[] var4 = (DatabaseColumn[]) var2.toArray(new DatabaseColumn[var2.size()]);
         String[] var5 = (String[]) var3.toArray(new String[var3.size()]);
         var1.addSourceUpdateValues(var4, var5);
      } else {
         var1.removeEntry(2);
      }
   }
}
