package org.etlforge.integration.data.location.file;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.location.FileFilterOptions;
import org.etlforge.integration.data.validation.ValidationIssue;

public final class FileFilter {
   private final String filter;
   private final FileFilterOptions options;

   public FileFilter(String var1) {
      this(var1, new FileFilterOptions());
   }

   public FileFilter(String var1, FileFilterOptions var2) {
      Preconditions.checkNotNull(var1, "filter");
      Preconditions.checkNotNull(var2, "options");
      this.filter = var1;
      this.options = var2;
   }

   public String getFilter() {
      return this.filter;
   }

   public FileFilterOptions getOptions() {
      return this.options;
   }

   public Set<ValidationIssue> validate() {
      LinkedHashSet var1 = Sets.newLinkedHashSet();
      if (StringUtils.isBlank(this.filter)) {
         var1.add(FileLocationError.MISSING_FILTER);
      }

      return var1;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof FileFilter)) {
         return false;
      }

      FileFilter var2 = (FileFilter)var1;
      return this.filter.equals(var2.filter) && this.options.equals(var2.options);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.filter.hashCode();
      return 31 * var1 + this.options.hashCode();
   }

   @Override
   public String toString() {
      return this.filter + this.options.toString();
   }
}
