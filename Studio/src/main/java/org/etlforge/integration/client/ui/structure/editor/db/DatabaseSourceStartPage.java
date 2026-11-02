package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Maps;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Map;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.database.ui.drivers.DriverSanityChecker;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.client.ui.entity.wizard.NewDatabaseCallback;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.LocatableFilter;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.system.Disposable;
import org.etlforge.system.ThreeValuedBool;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

public final class DatabaseSourceStartPage extends EntitySelectionPage<Source> implements DatabaseStructureWizardPage {
   private final ManualModeSwitch manualModeSwitch = new ManualModeSwitch();
   private Map<DatabaseSourceStartPage.CacheKey, WizardPage> nextPageCache;
   private ManualSqlStatementPage currentManualSqlStatementPage;
   private final DatabaseSourceStartPage.DriverChangeListener driverChangeListener;
   private ThreeValuedBool updateSourceTableAvailable;
   private boolean singleTableOnly;

   public DatabaseSourceStartPage(ApplicationWindowWizard var1, EntityExplorerSupport var2) {
      super(var1, PackageResources.SourceStartPage.SUB_TITLE, var2, EntityType.Source, Source.class, new LocatableFilter(DataLocationType.Database));
      this.manualModeSwitch.installUndoRedo(this.getUndoRedoInstaller());
      this.driverChangeListener = new DatabaseSourceStartPage.DriverChangeListener();
      this.updateSourceTableAvailable = ThreeValuedBool.MAYBE;
      this.setAdditionalComponents(Collections.singletonList(this.manualModeSwitch.getUi()));
      this.setMainCaption(PackageResources.SourceStartPage.MAIN_CAPTION);
      this.setSubCaption(PackageResources.SourceStartPage.SUB_CAPTION);
   }

   @Override
   protected void setInitialState() {
      this.setSelectorLabel(PackageResources.SourceStartPage.COMBO_LABEL);
      this.addSelectionListener(new DatabaseSourceStartPage.InputValidator());
      this.setNewEntityCallback(new NewDatabaseCallback());
      this.manualModeSwitch.setDbLocation(this.getSelectedLocation());
   }

   public DatabaseLocation getSelectedLocation() {
      Source var1 = this.getSelectedEntity();
      if (var1 != null) {
         DataLocation var2 = var1.getLocation();
         if (var2 instanceof DatabaseLocation) {
            return (DatabaseLocation)var2;
         }
      }

      return null;
   }

   protected boolean validateSelectedEntity(Source var1) {
      DatabaseLocation var2 = this.getSelectedLocation();
      if (var2 == null) {
         Alert.error(this.getMainPanel(), PackageResources.SourceStartPage.Errors.NO_SOURCE, "");
         return false;
      } else {
         return DriverSanityChecker.isDriverValid(var2, SourceTarget.Source);
      }
   }

   @Override
   public WizardPage getNextPage() {
      DatabaseLocation var1 = this.getSelectedLocation();
      if (var1 == null) {
      }

      WizardPage var2 = this.getCachedNextPage(var1);
      if (var2 == null) {
         var2 = this.createNewNextPage(var1);
      } else if (var2 instanceof ManualSqlStatementPage) {
         this.transferManualSqlStatementIfNeeded((ManualSqlStatementPage)var2);
      }

      return var2;
   }

   private void transferManualSqlStatementIfNeeded(ManualSqlStatementPage var1) {
      if (this.currentManualSqlStatementPage != null && this.currentManualSqlStatementPage != var1) {
         var1.setSqlStatement(this.currentManualSqlStatementPage.getSqlStatement());
      }

      this.currentManualSqlStatementPage = var1;
   }

   private WizardPage getCachedNextPage(DatabaseLocation var1) {
      if (this.nextPageCache == null) {
         this.nextPageCache = Maps.newHashMap();
         return null;
      } else {
         return this.nextPageCache.get(new DatabaseSourceStartPage.CacheKey(var1, this.manualModeSwitch.isSelected()));
      }
   }

   private WizardPage createNewNextPage(DatabaseLocation var1) {
      Source var2 = this.getSelectedEntity();
      boolean var3 = this.manualModeSwitch.isSelected();
      AbstractWizardPage var4;
      if (var3) {
         var4 = new ManualSqlStatementPage(this.getWizard(), var2, var1);
         this.transferManualSqlStatementIfNeeded((ManualSqlStatementPage)var4);
      } else {
         DatabaseTableSelectionPage var5 = new DatabaseTableSelectionPage(this.getWizard(), var1, var2.getID(), null, SourceTarget.Source, this.singleTableOnly);
         var5.setUpdateSourceTableAvailable(this.updateSourceTableAvailable);
         var4 = var5;
      }

      this.nextPageCache.put(new DatabaseSourceStartPage.CacheKey(var1, var3), var4);
      return var4;
   }

   @Override
   public void applyTo(DatabaseStructure var1) {
      Source var2 = this.getSelectedEntity();
      if (var2 != null) {
         DatabaseLocation var3 = (DatabaseLocation)var2.getLocation();
         var1.setDbLocation(var3);
         var1.setSourceId(var2.getID());
      } else {
         throw new RuntimeException("Tried to complete a database structure wizard without a DB source. This is a bug.");
      }
   }

   @Override
   public void setStructureToEdit(StructureToEdit var1) {
      SourceId var2 = var1.getStructure().getSourceId();
      if (var2 == null) {
         var2 = var1.getTransformation().getSourceId();
      }

      this.setSelectedEntityId(var2);
      this.manualModeSwitch.setSelected(var1.getStructure().hasManualSqlStatement());
   }

   @Override
   protected void disposeAddtionalResources() {
      this.driverChangeListener.dispose();
   }

   public void setUpdateSourceTableAvailable(ThreeValuedBool var1) {
      this.updateSourceTableAvailable = var1;
   }

   public void setSingleTableOnly() {
      this.singleTableOnly = true;
   }

   private static final class CacheKey {
      private final Pair<DatabaseLocation, Boolean> data;

      public CacheKey(DatabaseLocation var1, Boolean var2) {
         this.data = Pair.of(var1, var2);
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof DatabaseSourceStartPage.CacheKey && this.data.equals(((DatabaseSourceStartPage.CacheKey)var1).data);
      }

      @Override
      public int hashCode() {
         return this.data.hashCode();
      }
   }

   private final class DriverChangeListener implements PropertyChangeListener, Disposable {
      private DatabaseLocation currentLocation;

      private DriverChangeListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         DatabaseSourceStartPage.this.manualModeSwitch.setDbLocation(DatabaseSourceStartPage.this.getSelectedLocation());
      }

      public void listenTo(DatabaseLocation var1) {
         if (var1 != this.currentLocation) {
            this.stopListening();
            this.currentLocation = var1;
            if (var1 != null) {
               var1.addPropertyChangeListener("driver", this);
            }
         }
      }

      private void stopListening() {
         if (this.currentLocation != null) {
            this.currentLocation.removePropertyChangeListener("driver", this);
            this.currentLocation = null;
         }
      }

      @Override
      public void dispose() {
         this.stopListening();
      }
   }

   private final class InputValidator implements SelectionListener {
      private InputValidator() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         DatabaseLocation var2 = DatabaseSourceStartPage.this.getSelectedLocation();
         DatabaseSourceStartPage.this.setCanContinue(var2 != null);
         DatabaseSourceStartPage.this.driverChangeListener.listenTo(var2);
         DatabaseSourceStartPage.this.manualModeSwitch.setDbLocation(var2);
      }
   }
}
