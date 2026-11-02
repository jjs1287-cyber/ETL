package org.etlforge.application.filestore;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.xml.Elements;
import org.etlforge.xml.XmlStringBuilder;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

final class XmlBasedDependencyManager implements DependencyManager {
   private static final String ROOT = "dependencies";
   private static final String FILE = "file";
   private static final String REQUIRED = "required";
   private static final String FILE_PATH = "file_path";
   private final File file;
   private final SetMultimap<String, String> links = HashMultimap.create();

   public XmlBasedDependencyManager(File var1) {
      this.file = var1;
      if (var1.exists()) {
         this.load(Files.newInputStreamSupplier(var1));
      }
   }

   @VisibleForTesting
   XmlBasedDependencyManager(InputSupplier<? extends InputStream> var1) {
      this.file = null;
      this.load(var1);
   }

   @Override
   public void add(String var1, String var2) {
      this.links.put(var1, var2);
      this.store();
   }

   @Override
   public Set<String> getRequiredFiles(String var1) {
      Set var2 = this.links.get(var1);
      return var2 == null ? Sets.newHashSet() : Sets.newHashSet(var2);
   }

   @Override
   public void deleteAll() {
      this.links.clear();
      this.store();
   }

   private void store() {
      if (this.file != null) {
         try {
            KongaIoUtils.createAndWrite(this.file, this.generateXml());
         } catch (IOException var2) {
            ErrorLog.log(this, "Failed to store file store dependencies", var2);
         }
      }
   }

   private String generateXml() {
      XmlStringBuilder var1 = new XmlStringBuilder();
      var1.addElement("dependencies");

      for (String var3 : this.links.keySet()) {
         this.addFileToXml(var3, var1);
      }

      var1.closeElement();
      return var1.getXml();
   }

   private void addFileToXml(String var1, XmlStringBuilder var2) {
      var2.addElement("file");
      var2.addAttribute("file_path", var1);
      this.addRequiredFilesToXml(this.links.get(var1), var2);
      var2.closeElement();
   }

   private void addRequiredFilesToXml(Set<? extends String> var1, XmlStringBuilder var2) {
      if (var1 != null) {
         for (String var4 : var1) {
            var2.addElement("required");
            var2.addAttribute("file_path", var4);
            var2.closeElement();
         }
      }
   }

   private void load(InputSupplier<? extends InputStream> var1) {
      try {
         Document var2 = DomParser.parseInput(var1);

         for (Element var4 : Elements.matching("dependencies/file", var2)) {
            String var5 = getFilePath(var4);

            for (Element var7 : Elements.under(var4)) {
               String var8 = getFilePath(var7);
               this.links.put(var5, var8);
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }
   }

   private static String getFilePath(Element var0) {
      return var0.getAttribute("file_path");
   }

   @Override
   public void copy(Map<String, String> var1) {
      HashMultimap var2 = HashMultimap.create();

      for (Entry var4 : this.links.entries()) {
         String var5 = (String)var4.getKey();
         String var6 = (String)var1.get(var5);
         if (var6 != null) {
            String var7 = (String)var4.getValue();
            String var8 = (String)var1.get(var7);
            if (var8 == null) {
               var8 = var7;
            }

            var2.put(var6, var8);
         }
      }

      if (!var2.isEmpty()) {
         this.links.putAll(var2);
         this.store();
      }
   }
}
