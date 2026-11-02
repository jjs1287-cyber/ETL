package org.etlforge.integration.data.location;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.ExportableStringProperty;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.location.tempstore.TemporaryStorageLocationValidator;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.lang.Arrays2;

public final class TemporaryStorageLocation extends FileLocation {
   public static final String PATH = "locator";

   public TemporaryStorageLocation() {
      super(DataLocationType.TemporaryStorage);
   }

   public static String[] getPropertyKeys() {
      return Arrays2.append(FileLocation.getPropertyKeys(), new String[]{"locator"});
   }

   public String getPath() {
      return this.getProperty("locator");
   }

   public void setPath(String var1) {
      this.setStringProperty("locator", var1);
   }

   @Override
   public void setLogin(String var1) {
   }

   @Override
   public void setPassword(Optional<Password> var1) {
   }

   @Override
   public void collectValidationMessages(ValidationMessageCollector var1) {
      super.collectValidationMessages(var1);
      TemporaryStorageLocationValidator var2 = new TemporaryStorageLocationValidator(this, var1);
      var2.validate();
   }

   @Override
   public ImmutableList<? extends ExportableProperty> getExportableProperties() {
      return ImmutableList.of(
         new ExportableStringProperty(this, "locator", LocationMessages.getString("FileShareLocation.Property.Path"), false),
         new ExportableStringProperty(this, "file_name", LocationMessages.getString("FileLocation.Property.File"), false)
      );
   }

   @Override
   public ImmutableSet<String> getNamesOfPropertiesToCache() {
      return ImmutableSet.of("locator");
   }
}
