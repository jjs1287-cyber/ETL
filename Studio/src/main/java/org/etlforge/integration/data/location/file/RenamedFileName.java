package org.etlforge.integration.data.location.file;

import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.Set;
import org.etlforge.integration.data.location.DataLocationError;
import org.etlforge.integration.data.validation.ValidationIssue;

public final class RenamedFileName {
   public static final ValidationIssue NAME_CONTAINS_ILLEGAL_CHARS = new DataLocationError("Validation.RenameToEndsContainsIllegalChars");
   public static final ValidationIssue NAME_ENDS_WITH_SEPARATOR = new DataLocationError("Validation.RenameToEndsWithSeparator");
   public static final ValidationIssue NAME_ENDS_WITH_SEPARATOR_DOTS = new DataLocationError("Validation.RenameToEndsWithSeparatorDots");
   public static final ValidationIssue NAME_HAS_ONLY_DOTS = new DataLocationError("Validation.RenameToHasOnlyDots");
   public static final ValidationIssue STARTS_WITH_DOUBLE_BACKSLASH = new DataLocationError("Validation.RenameToStartsWithDoubleBackSlash");
   private static final TargetFileNameValidator validator = createValidator();
   private final String name;

   private static TargetFileNameValidator createValidator() {
      TargetFileNameValidator var0 = new TargetFileNameValidator();
      var0.setAllowFolderSeparator(true);
      return var0;
   }

   public RenamedFileName(String var1) {
      this.name = var1 != null ? var1.trim() : "";
   }

   public String getFileName() {
      return this.name;
   }

   public Set<ValidationIssue> validate() {
      LinkedHashSet var1 = Sets.newLinkedHashSet();
      if (!this.name.isEmpty()) {
         this.validateImpl(var1);
      }

      return var1;
   }

   private void validateImpl(Set<ValidationIssue> var1) {
      if (validator.endsWithSeparator(this.name)) {
         var1.add(NAME_ENDS_WITH_SEPARATOR);
      }

      if (validator.containsIllegalCharacter(this.name)) {
         var1.add(NAME_CONTAINS_ILLEGAL_CHARS);
      }

      if (validator.hasOnlyDots(this.name)) {
         var1.add(NAME_HAS_ONLY_DOTS);
      } else if (validator.endsWithSeparatorAndDots(this.name)) {
         var1.add(NAME_ENDS_WITH_SEPARATOR_DOTS);
      } else if (this.name.startsWith("\\\\")) {
         var1.add(STARTS_WITH_DOUBLE_BACKSLASH);
      }
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof RenamedFileName && this.name.equals(((RenamedFileName)var1).name);
   }

   @Override
   public int hashCode() {
      return this.name.hashCode();
   }

   @Override
   public String toString() {
      return this.name;
   }
}
