package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;
import org.etlforge.util.csv.CSVParams;

public final class LocationPageModel<T extends IntegrationEntity & Locatable> extends AbstractPropertyBasedModel {
   public static final String LOCATION_TYPE = "type";
   public static final String LOCAL_FILE_MODE = "localFileMode";
   public static final String LOCAL_FILE = "localFile";
   public static final String SELECTED_ITEM = "selectedItem";
   public static final String CAN_CONTINUE = "canContinue";
   public static final String UTF_8 = "utf-8";
   private final EntityType entityType;
   private final Class<T> entityClass;
   private final ImmutableList<DataLocationType> locationTypes;
   private final Property<DataLocationType> dataLocationType = this.newProperty("type");
   private final Property<LocalFileSelectionMode> localFileMode = this.newProperty("localFileMode", LocalFileSelectionMode.BROWSE);
   private final Property<File> localFile = this.newProperty("localFile");
   private final Property<T> selectedItem = this.newProperty("selectedItem");
   private final Property<Boolean> utf8 = this.newProperty("utf-8", Boolean.FALSE);
   private final LocalFileValidator fileValidator;
   private Optional<CSVParams> csvParams = Optional.absent();
   private final Property<Boolean> canContinue = this.newProperty("canContinue");

   public static LocationPageModel<Source> forSources(Iterable<DataLocationType> var0) {
      return new LocationPageModel<>(EntityType.Source, Source.class, var0, new LocalSourceFileValidator());
   }

   public static LocationPageModel<Target> forTargets(Iterable<DataLocationType> var0) {
      return new LocationPageModel<>(EntityType.Target, Target.class, var0, new LocalTargetFileValidator());
   }

   private LocationPageModel(EntityType var1, Class<T> var2, Iterable<DataLocationType> var3, LocalFileValidator var4) {
      this.entityType = var1;
      this.entityClass = var2;
      this.locationTypes = ImmutableList.copyOf(var3);
      this.fileValidator = var4;
      this.dataLocationType.set(this.locationTypes.get(0));
      this.dataLocationType.setAllowsNull(false);
      this.localFileMode.setAllowsNull(false);
      this.localFileMode.addListener(new LocationPageModel.LocalFileModeListener());
      this.dataLocationType.addListener(new LocationPageModel.NoLocationTypeStateChecker());
      this.addPropertyChangeListener(new LocationPageModel.CanContinueUpdater());
      this.updateCanContinue();
   }

   public EntityType getEntityType() {
      return this.entityType;
   }

   public Class<T> getEntityClass() {
      return this.entityClass;
   }

   public ImmutableList<DataLocationType> getLocationTypes() {
      return this.locationTypes;
   }

   public LocalFileValidator getFileValidator() {
      return this.fileValidator;
   }

   public T getSelectedItem() {
      return this.selectedItem.get();
   }

   public DataLocationType getSelectedLocationType() {
      return this.dataLocationType.get();
   }

   private void updateCanContinue() {
      boolean var1;
      if (this.isBrowseForLocalFile()) {
         var1 = !this.localFile.isNull();
      } else if (this.dataLocationType.get() == DataLocationType.None) {
         var1 = true;
      } else {
         var1 = !this.selectedItem.isNull();
      }

      this.canContinue.set(var1);
   }

   private boolean isBrowseForLocalFile() {
      return this.isLocalFileSelected() && this.localFileMode.get() == LocalFileSelectionMode.BROWSE;
   }

   public boolean isLocalFileSelected() {
      return this.dataLocationType.get() == DataLocationType.LocalFile;
   }

   public SampleFile getSampleFile() throws IOException {
      if (this.getSelectedLocationType() == DataLocationType.LocalFile) {
         File var1 = this.getLocalFile();
         if (var1.exists() && var1.canRead()) {
            Charset var2 = this.getFileEncoding();
            return SampleFile.create(var1, var2);
         }
      }

      return null;
   }

   private File getLocalFile() {
      assert this.getSelectedLocationType() == DataLocationType.LocalFile;
      if (this.isBrowseForLocalFile()) {
         return this.localFile.get();
      } else {
         IntegrationEntity var1 = this.selectedItem.get();
         if (var1 != null) {
            LocalFileLocation var2 = (LocalFileLocation)((Locatable)var1).getLocation();
            return new File(var2.getFullPath());
         } else {
            return null;
         }
      }
   }

   private Charset getFileEncoding() {
      return this.isUTF8() ? Charsets.UTF_8 : Charset.defaultCharset();
   }

   public void setCsvParams(CSVParams var1) {
      this.csvParams = Optional.of(var1);
   }

   public boolean hasCsvParams() {
      return this.csvParams.isPresent();
   }

   public CSVParams getCsvParams() {
      return this.csvParams.orNull();
   }

   public boolean isUTF8() {
      return this.utf8.get();
   }

   public void setUTF8(boolean var1) {
      this.utf8.set(var1);
   }

   private class CanContinueUpdater implements PropertyChangeListener {
      private CanContinueUpdater() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (!"canContinue".equals(var1.getPropertyName())) {
            LocationPageModel.this.updateCanContinue();
         }
      }
   }

   private class LocalFileModeListener implements PropertyChangeListener {
      private LocalFileModeListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (LocationPageModel.this.localFileMode.get() == LocalFileSelectionMode.BROWSE) {
            LocationPageModel.this.selectedItem.set(null);
         }
      }
   }

   private class NoLocationTypeStateChecker implements PropertyChangeListener {
      private NoLocationTypeStateChecker() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         DataLocationType var2 = (DataLocationType)var1.getNewValue();
         if (var2 == DataLocationType.None) {
            Preconditions.checkState(
               LocationPageModel.this.entityType == EntityType.Source, "DataLocationType.None should only be available for Sources. This is a bug."
            );
         }
      }
   }
}
