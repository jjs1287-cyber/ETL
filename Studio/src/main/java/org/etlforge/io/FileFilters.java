package org.etlforge.io;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileFilter;
import java.util.Collection;

public final class FileFilters {
   public static final FileFilter FILES_ONLY = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isFile();
      }
   };
   public static final FileFilter DIRECTORIES_ONLY = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isDirectory();
      }
   };

   public static FileFilter or(Collection<? extends FileFilter> var0) {
      return new FileFilters.OrFilter(var0);
   }

   private FileFilters() {
   }

   private static final class OrFilter implements FileFilter {
      private final ImmutableList<FileFilter> parts;

      public OrFilter(Collection<? extends FileFilter> var1) {
         this.parts = ImmutableList.copyOf(var1);
      }

      @Override
      public boolean accept(File var1) {
         for (FileFilter var3 : this.parts) {
            if (var3.accept(var1)) {
               return true;
            }
         }

         return false;
      }

      @Override
      public String toString() {
         return "Any of " + this.parts;
      }
   }
}
