package org.etlforge.integration.data.location;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.PasswordUtils;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.file.FileFilter;
import org.etlforge.integration.data.location.file.IgnoredLines;
import org.etlforge.integration.data.location.file.RenamedFileName;
import org.etlforge.integration.data.location.file.TargetFileName;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public abstract class FileLocation extends DataLocation implements LineBasedSourceData {
   public static final String PROPERTY_LOGIN = "login";
   public static final String PROPERTY_PASSWORD = "password";
   public static final String PROPERTY_FILE_FILTER = "file_name";
   public static final String PROPERTY_RENAME_FILE = "renamefileto";
   public static final String PROPERTY_ENCRYPTION = "encrypt_choice";
   public static final String PROPERTY_PASS_PHRASE = "encrypt_text";
   public static final String PROPERTY_SECURED = "secured";
   public static final String IGNORE_CASE = "filterOption.ignoreCase";
   public static final String REGULAR_EXPRESSION_FILTER = "filterOption.regularExpression";
   public static final String FILE_FILTER_OPTIONS = "filterOptions";
   public static final String TARGET_FILE_NAME = "targetFileName";
   public static final String DELETE_SOURCE_MARKER = "/\\delete/\\";
   public static final String LINES_TO_IGNORE = "lines_to_ignore";
   public static final String SKIP_EMPTY_FILES = "skip_empty_files";
   public static final String FILE_SHARE_CREATE_DIRECTORIES = "file_share_create_directories";
   public static final String CHARACTER_ENCODING = "character_encoding";

   public static String[] getPropertyKeys() {
      return new String[]{
         "login",
         "password",
         "file_name",
         "renamefileto",
         "encrypt_choice",
         "encrypt_text",
         "secured",
         "filterOption.ignoreCase",
         "filterOption.regularExpression",
         "ignore_first",
         "ignore_last",
         "skip_empty_files",
         "file_share_create_directories",
         "character_encoding",
         "compression_enabled", "compression_format", "compression_mode", "compression_archive_name", "compression_password"
      };
   }

   public FileLocation(DataLocationType var1) {
      super(var1);
   }

   public final FileFilter getFileFilter() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("file_name", "");
         FileFilterOptions var3 = this.getFilterOptions();
         return new FileFilter(var2, var3);
      }
   }

   private FileFilterOptions getFilterOptions() {
      boolean var1 = this.getBooleanProperty("filterOption.ignoreCase", false);
      boolean var2 = this.getBooleanProperty("filterOption.regularExpression", false);
      if (var1 && var2) {
         ErrorLog.log(this.getClass(), "Invalid FileLocation", new Exception("Both filter options ignoreCase and regularException were set to TRUE"));
         var1 = false;
      }

      return new FileFilterOptions(var1, var2);
   }

   public final void setFileFilter(FileFilter var1) {
      FileFilter var2;
      synchronized (this.getDataLock()) {
         var2 = this.getFileFilter();
         this.setProperty("file_name", var1.getFilter());
         this.getData().putBoolean("filterOption.ignoreCase", var1.getOptions().isIgnoreCase());
         this.getData().putBoolean("filterOption.regularExpression", var1.getOptions().isRegularExpression());
      }

      if (!var2.equals(var1)) {
         this.firePropertyChange("file_name", var2, var1);
      }
   }

   public final TargetFileName getTargetFileName() {
      return new TargetFileName(this.getProperty("file_name", ""));
   }

   public final void setTargetFileName(TargetFileName var1) {
      TargetFileName var2;
      synchronized (this.getDataLock()) {
         var2 = this.getTargetFileName();
         this.setProperty("file_name", var1.getFileName());
      }

      if (!var2.equals(var1)) {
         this.firePropertyChange("targetFileName", var2, var1);
      }
   }

   public final RenamedFileName getRenameFileTo() {
      return new RenamedFileName(this.getProperty("renamefileto"));
   }

   public final void setRenameFileTo(RenamedFileName var1) {
      RenamedFileName var2;
      synchronized (this.getDataLock()) {
         var2 = this.getRenameFileTo();
         this.setProperty("renamefileto", var1);
      }

      if (!var2.equals(var1)) {
         this.firePropertyChange("renamefileto", var2, var1);
      }
   }

   public final void setRenameFileTo(String var1) {
      this.setRenameFileTo(new RenamedFileName(var1));
   }

   public final void setDeleteSourceFile() {
      this.setRenameFileTo("/\\delete/\\");
   }

   public final boolean willBeDeletedOnSuccess() {
      return "/\\delete/\\".equals(this.getRenameFileTo().getFileName());
   }

   @Override
   public final IgnoredLines getLinesToIgnore() {
      synchronized (this.getDataLock()) {
         return new IgnoredLines(this.getIntProperty("ignore_first", 0), this.getIntProperty("ignore_last", 0));
      }
   }

   @Override
   public final void setLinesToIgnore(IgnoredLines var1) {
      IgnoredLines var2;
      synchronized (this.getDataLock()) {
         var2 = this.getLinesToIgnore();
         this.setProperty("ignore_first", String.valueOf(var1.getFirst()));
         this.setProperty("ignore_last", String.valueOf(var1.getLast()));
      }

      if (!var2.equals(var1)) {
         this.firePropertyChange("lines_to_ignore", var2, var1);
      }
   }

   @Override
   public void collectValidationMessages(ValidationMessageCollector var1) {
      super.collectValidationMessages(var1);
      FileLocationValidator var2 = new FileLocationValidator(this, var1);
      var2.validate();
   }

   public void setLogin(String var1) {
      this.setStringProperty("login", var1);
   }

   public void setPassword(Optional<Password> var1) {
      PasswordUtils.setPassword(this, "password", var1);
   }

   public final Optional<Password> getPassword() {
      return PasswordUtils.getPassword(this, "password");
   }

   public final boolean getSkipEmptyFiles() {
      return this.getBooleanProperty("skip_empty_files", false);
   }

   public final void setSkipEmptyFiles(boolean var1) {
      this.setBooleanProperty("skip_empty_files", var1, false);
   }

   public final boolean getFileShareCreateDirectories() {
      return this.getBooleanProperty("file_share_create_directories", true);
   }

   public final void setFileShareCreateDirectories(boolean var1) {
      this.setBooleanProperty("file_share_create_directories", var1, false);
   }

   public final String getCharacterEncoding() {
      return this.getProperty("character_encoding", "UTF-8");
   }

   public final void setCharacterEncoding(String var1) {
      this.setProperty("character_encoding", var1);
   }

   @OverridingMethodsMustInvokeSuper
   public FileLocation getCopyForSourceOrTarget(SourceTarget var1) {
      Preconditions.checkNotNull(var1);
      FileLocation var2 = (FileLocation)this.clone();
      if (var1 == SourceTarget.Source) {
         this.setProperty("skip_empty_files", (String)null);
         this.setProperty("file_share_create_directories", (String)null);
         this.setProperty("character_encoding", (String)null);
      } else {
         var2.setProperty("filterOption.ignoreCase", (String)null);
         var2.setProperty("filterOption.regularExpression", (String)null);
         var2.setProperty("renamefileto", (String)null);
         var2.setProperty("ignore_first", (String)null);
         var2.setProperty("ignore_last", (String)null);
      }

      return var2;
   }
}
