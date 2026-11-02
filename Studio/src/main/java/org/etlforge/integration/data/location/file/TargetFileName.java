package org.etlforge.integration.data.location.file;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.location.DataLocationError;
import org.etlforge.integration.data.validation.ValidationIssue;

public final class TargetFileName {
   public static final ValidationIssue NAME_CONTAINS_ILLEGAL_CHARS = new DataLocationError("Validation.TargetFileEndsContainsIllegalChars");
   public static final ValidationIssue NAME_ENDS_WITH_SEPARATOR = new DataLocationError("Validation.TargetFileEndsWithSeparator");
   public static final ValidationIssue NAME_ENDS_WITH_SEPARATOR_DOTS = new DataLocationError("Validation.TargetFileEndsWithSeparatorDots");
   public static final ValidationIssue NAME_HAS_ONLY_DOTS = new DataLocationError("Validation.TargetFileHasOnlyDots");
   private static final TargetFileNameValidator validator = createValidator();
   private final String name;

   private static TargetFileNameValidator createValidator() {
      TargetFileNameValidator var0 = new TargetFileNameValidator();
      var0.setAllowFolderSeparator(true);
      return var0;
   }

   public TargetFileName(String var1) {
      this.name = Preconditions.checkNotNull(var1);
   }

   public String getFileName() {
      return this.name;
   }

   public boolean hasXmlExtension() {
      String var1 = ".xml";
      return this.hasExtension(var1);
   }

   private boolean hasExtension(String var1) {
      return this.name.toLowerCase().endsWith(var1);
   }

   public boolean hasTextExtension() {
      return this.hasExtension(".txt") || this.hasExtension(".csv");
   }

   public boolean hasJsonExtension() {
      return this.hasExtension(".json");
   }

   public Set<ValidationIssue> validate() {
      LinkedHashSet var1 = Sets.newLinkedHashSet();
      if (StringUtils.isBlank(this.name)) {
         var1.add(FileLocationError.MISSING_FILTER);
      } else if (validator.hasOnlyDots(this.name)) {
         var1.add(NAME_HAS_ONLY_DOTS);
      } else {
         if (validator.endsWithSeparator(this.name)) {
            var1.add(NAME_ENDS_WITH_SEPARATOR);
         }

         if (validator.containsIllegalCharacter(this.name)) {
            var1.add(NAME_CONTAINS_ILLEGAL_CHARS);
         }

         if (validator.endsWithSeparatorAndDots(this.name)) {
            var1.add(NAME_ENDS_WITH_SEPARATOR_DOTS);
         }
      }

      return var1;
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof TargetFileName && this.name.equals(((TargetFileName)var1).name);
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
