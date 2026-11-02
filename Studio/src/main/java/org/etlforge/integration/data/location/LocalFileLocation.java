package org.etlforge.integration.data.location;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.io.File;
import org.apache.axis.utils.StringUtils;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.ExportableStringProperty;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.location.file.FileFilter;
import org.etlforge.integration.data.location.local.LocalFileLocationValidator;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.lang.Arrays2;

public final class LocalFileLocation extends FileLocation {
   public static final String FOLDER = "locator";

   public LocalFileLocation() {
      super(DataLocationType.LocalFile);
   }

   public static String[] getPropertyKeys() {
      return Arrays2.append(FileLocation.getPropertyKeys(), new String[]{"locator"});
   }

   public LocalFileLocation(File var1) {
      super(DataLocationType.LocalFile);
      this.setFolderPath(var1.getParent());
      this.setFileFilter(new FileFilter(var1.getName()));
   }

   public String getFolderPath() {
      synchronized (this.getDataLock()) {
         return this.getProperty("locator");
      }
   }

   public void setFolderPath(String var1) {
      this.setStringProperty("locator", var1);
   }

   @Override
   public void setLogin(String var1) {
   }

   @Override
   public void setPassword(Optional<Password> var1) {
   }

   public String getFullPath() {
      String var1 = this.getFolderPath();
      String var2 = this.getFileFilter().getFilter();
      if (StringUtils.isEmpty(var1)) {
         return var2;
      } else if (var1.endsWith("/") || var1.endsWith("\\")) {
         return var1 + var2;
      } else {
         return var1.contains("\\") ? var1 + "\\" + var2 : var1 + "/" + var2;
      }
   }

   @Override
   public void collectValidationMessages(ValidationMessageCollector var1) {
      super.collectValidationMessages(var1);
      LocalFileLocationValidator var2 = new LocalFileLocationValidator(this, var1);
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
