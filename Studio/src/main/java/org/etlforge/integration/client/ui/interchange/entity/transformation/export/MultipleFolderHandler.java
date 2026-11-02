package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;

final class MultipleFolderHandler {
   private final String relativePathRoot;
   private final Map<String, String> folders = Maps.newHashMap();
   private String path;
   private String userDefinedName;

   public MultipleFolderHandler(String var1) {
      this.relativePathRoot = var1;
   }

   public Map<String, String> getFolders() {
      return this.folders;
   }

   @RelativePathRoot
   public String getRelativePathRoot() {
      return this.relativePathRoot;
   }

   @StartElementHandler("/")
   public void startFolder() {
      this.path = null;
      this.userDefinedName = null;
   }

   @EndElementHandler("/")
   public void endFolder() {
      this.folders.put(this.path, this.userDefinedName);
   }

   @EndElementHandler
   public void path(String var1) {
      this.path = var1;
   }

   @EndElementHandler
   public void name(String var1) {
      this.userDefinedName = var1;
   }
}
