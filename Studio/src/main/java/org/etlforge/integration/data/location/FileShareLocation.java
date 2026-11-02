package org.etlforge.integration.data.location;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.ExportableStringProperty;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.lang.Arrays2;

public final class FileShareLocation extends FileLocation implements LoginProtectedResource {
   public static final String PROPERTY_PATH = "locator";

   public static String[] getPropertyKeys() {
      return Arrays2.append(FileLocation.getPropertyKeys(), new String[]{"locator"});
   }

   public FileShareLocation() {
      super(DataLocationType.FileShare);
   }

   public String getPath() {
      return this.getProperty("locator");
   }

   public void setPath(String var1) {
      this.setStringProperty("locator", var1);
   }

   @Override
   public void collectValidationMessages(ValidationMessageCollector var1) {
      super.collectValidationMessages(var1);
      FileShareLocationValidator var2 = new FileShareLocationValidator(this, var1);
      var2.validate();
   }

   @Override
   public ImmutableList<? extends ExportableProperty> getExportableProperties() {
      return ImmutableList.of(
         new ExportableStringProperty(this, "locator", LocationMessages.getString("FileShareLocation.Property.Path"), false),
         new ExportableStringProperty(this, "file_name", LocationMessages.getString("FileLocation.Property.File"), false),
         new ExportableStringProperty(this, "login", LocationMessages.getString("Property.Login"), true),
         new ExportableStringProperty(this, "password", LocationMessages.getString("Property.Password"), true)
      );
   }

   @Override
   public String getLogin() {
      return this.getProperty("login");
   }

   @Override
   public String getPasswordPropertyName() {
      return "password";
   }

   @Override
   public ImmutableSet<String> getNamesOfPropertiesToCache() {
      return ImmutableSet.of("locator", "login");
   }
}
