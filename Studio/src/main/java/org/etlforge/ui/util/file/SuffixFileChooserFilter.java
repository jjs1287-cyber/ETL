package org.etlforge.ui.util.file;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.lang.StringUtils;
import org.etlforge.io.FileUtils;
import org.etlforge.lang.Arrays2;
import org.etlforge.util.file.ExtensionBasedFileFilter;

public final class SuffixFileChooserFilter extends FileFilter implements ExtensionBasedFileFilter {
   private final List<String> suffices = Lists.newArrayList();
   private final boolean acceptDirectories;
   private String description;

   public SuffixFileChooserFilter(String var1) {
      this(var1, true);
   }

   public SuffixFileChooserFilter(String var1, boolean var2) {
      this(new String[]{var1}, var2);
   }

   public SuffixFileChooserFilter(String... var1) {
      this(var1, true);
   }

   public SuffixFileChooserFilter(Collection<String> var1) {
      this(var1.toArray(new String[var1.size()]), true);
   }

   public SuffixFileChooserFilter(Collection<String> var1, boolean var2) {
      this(var1.toArray(new String[var1.size()]), var2);
   }

   public SuffixFileChooserFilter(String[] var1, boolean var2) {
      for (String var6 : var1) {
         this.suffices.add(removeDot(var6));
      }

      this.acceptDirectories = var2;
      this.description = Arrays2.join(var1, Functions.toStringFunction(), ",");
   }

   private static String removeDot(String var0) {
      return StringUtils.removeStart(var0, ".");
   }

   @Override
   public boolean accept(File var1) {
      if (var1.isDirectory()) {
         return this.acceptDirectories;
      }

      String var2 = FileUtils.getExtension(var1);

      for (String var4 : this.suffices) {
         if (var4.equalsIgnoreCase(var2)) {
            return true;
         }
      }

      return false;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   @Override
   public Iterator<String> getExtensions() {
      return Collections.unmodifiableList(this.suffices).iterator();
   }

   @Override
   public String getDescription() {
      return this.description;
   }
}
