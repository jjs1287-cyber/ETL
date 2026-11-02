package org.etlforge.xml.xsd;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.etlforge.collections.Collections2;

public final class GeneratedXsds {
   private final File mainXsd;
   private final Set<File> additionalXsds;

   public GeneratedXsds(File var1) {
      this(var1, Collections.emptySet());
   }

   public GeneratedXsds(File var1, Set<File> var2) {
      this.mainXsd = var1;
      this.additionalXsds = var2;
   }

   public File getMainXsd() {
      return this.mainXsd;
   }

   public Set<File> getAdditionalXsds() {
      return Sets.newHashSet(this.additionalXsds);
   }

   public Set<File> getAllFiles() {
      Set var1 = this.getAdditionalXsds();
      var1.add(this.mainXsd);
      return var1;
   }

   boolean isSingleFile() {
      return this.additionalXsds.isEmpty();
   }

   Map<File, File> getRenamedFiles() {
      String var1 = FilenameUtils.getBaseName(this.mainXsd.getName()) + "_";
      HashMap var2 = Maps.newHashMap();

      for (File var4 : this.additionalXsds) {
         var2.put(var4, new File(var4.getParent(), var1 + var4.getName()));
      }

      return var2;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder(this.mainXsd.getName());
      if (!this.additionalXsds.isEmpty()) {
         var1.append(" + [");
         var1.append(Collections2.join(this.additionalXsds, new GeneratedXsds.FileNameFunction(), ", "));
         var1.append("]");
      }

      return var1.toString();
   }

   private static class FileNameFunction implements Function<File, String> {
      private FileNameFunction() {
      }

      public String apply(File var1) {
         return var1.getName();
      }
   }
}
