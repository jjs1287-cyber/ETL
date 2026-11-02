package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.integration.client.database.ui.drivers.DriverSanityChecker;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.entity.wizard.NewDatabaseCallback;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.LocatableFilter;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

public final class DatabaseTargetStartPage extends EntitySelectionPage<Target> implements DatabaseStructureWizardPage {
   private Map<DatabaseLocation, DatabaseTableSelectionPage> nextPageCache;
   private boolean singleTableOnly;

   public DatabaseTargetStartPage(ApplicationWindowWizard var1, EntityExplorerSupport var2) {
      super(var1, PackageResources.TargetStartPage.SUB_TITLE, var2, EntityType.Target, Target.class, new LocatableFilter(DataLocationType.Database));
      this.setMainCaption(PackageResources.TargetStartPage.MAIN_CAPTION);
      this.setSubCaption(PackageResources.TargetStartPage.SUB_CAPTION);
   }

   @Override
   protected void setInitialState() {
      this.setSelectorLabel(PackageResources.TargetStartPage.COMBO_LABEL);
      this.addSelectionListener(new DatabaseTargetStartPage.InputValidator());
      this.setNewEntityCallback(new NewDatabaseCallback());
   }

   public DatabaseLocation getSelectedLocation() {
      Target var1 = this.getSelectedEntity();
      if (var1 != null) {
         DataLocation var2 = var1.getLocation();
         if (var2 instanceof DatabaseLocation) {
            return (DatabaseLocation)var2;
         }
      }

      return null;
   }

   public void setSingleTableOnly() {
      this.singleTableOnly = true;
   }

   protected boolean validateSelectedEntity(Target var1) {
      DatabaseLocation var2 = this.getSelectedLocation();
      if (var2 == null) {
         Alert.error(this.getMainPanel(), PackageResources.TargetStartPage.Errors.NO_TARGET, "");
         return false;
      } else {
         return DriverSanityChecker.isDriverValid(var2, SourceTarget.Target);
      }
   }

   public DatabaseTableSelectionPage getNextPage() {
      DatabaseLocation var1 = this.getSelectedLocation();
      if (var1 == null) {
      }

      DatabaseTableSelectionPage var2 = this.getCachedNextPage(var1);
      if (var2 == null) {
         var2 = this.createNewNextPage(var1);
      }

      return var2;
   }

   private DatabaseTableSelectionPage getCachedNextPage(DatabaseLocation var1) {
      if (this.nextPageCache == null) {
         this.nextPageCache = Maps.newHashMap();
         return null;
      } else {
         return this.nextPageCache.get(var1);
      }
   }

   private DatabaseTableSelectionPage createNewNextPage(DatabaseLocation var1) {
      Target var2 = this.getSelectedEntity();
      DatabaseTableSelectionPage var3 = new DatabaseTableSelectionPage(this.getWizard(), var1, null, var2.getID(), SourceTarget.Target, this.singleTableOnly);
      this.nextPageCache.put(var1, var3);
      return var3;
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      Target var2 = this.getSelectedEntity();
      if (var2 != null) {
         DatabaseLocation var3 = (DatabaseLocation)var2.getLocation();
         var1.setDbLocation(var3);
         var1.setTargetId(var2.getID());
      } else {
         throw new RuntimeException("Tried to complete a database structure wizard without a DB Target. This is a bug.");
      }
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      TargetId var2 = var1.getStructure().getTargetId();
      if (var2 == null) {
         var2 = var1.getTransformation().getTargetId();
      }

      this.setSelectedEntityId(var2);
   }

   private final class InputValidator implements SelectionListener {
      private InputValidator() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         DatabaseTargetStartPage.this.setCanContinue(DatabaseTargetStartPage.this.getSelectedLocation() != null);
      }
   }
}
