package org.etlforge.util.file;

import java.io.File;
import java.util.Date;
import java.util.EnumSet;
import org.etlforge.util.StorageSizeUnit;

public final class FileDescriber {
   private final EnumSet<FileDescriber.Element> elements;

   public FileDescriber(EnumSet<FileDescriber.Element> var1) {
      this.elements = var1.clone();
   }

   public String describe(File var1) {
      StringBuilder var2 = new StringBuilder();

      for (FileDescriber.Element var4 : this.elements) {
         if (var2.length() > 0) {
            var2.append("; ");
         }

         var2.append(var4.describe(var1));
      }

      return var2.toString();
   }

   public enum Element {
      NAME,
      PATH,
      LAST_MODIFIED,
      SIZE;

      public String describe(File var1) {
         switch (this) {
            case NAME:
               return "Name: " + var1.getName();
            case PATH:
               return "Path: " + var1.getAbsolutePath();
            case LAST_MODIFIED:
               return "Last Modified: " + new Date(var1.lastModified());
            case SIZE:
               return "Size: " + StorageSizeUnit.Kilobytes.format(var1.length());
            default:
               return null;
         }
      }
   }
}
