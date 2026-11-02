package org.etlforge.integration.structure.crom;

import com.google.common.collect.Maps;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.structure.reader.JbNode;
import org.etlforge.integration.structure.reader.TextDocReader;

public class LinkIterator implements Closeable {
   private TextDocReader link_reader;
   private List<JbNode> list;
   private String sourcePath;
   private String targetPath;
   private String targetTag;
   private CROM dtdRoot;
   private CROM target;
   private Iterator<JbNode> it;
   private boolean saved;
   private final Map<String, CROM> cromMap = Maps.newHashMap();

   public LinkIterator(File var1, CROM_Tree var2, boolean var3) throws IOException {
      if (var1 != null && var1.exists()) {
         this.dtdRoot = var2.getRoot();
         this.buildCromMap(this.dtdRoot, var3);
         String var4 = "utf8";
         boolean var5 = false;

         try {
            this.link_reader = new TextDocReader(var1, var4);
            JbNode var6 = this.link_reader.parse_all(0, 0);
            this.list = var6.getChildren();
            var5 = true;
         } finally {
            if (!var5) {
               this.link_reader.close();
            }
         }

         if (this.list != null) {
            this.it = this.list.iterator();
         }
      }
   }

   @Override
   public void close() throws IOException {
      if (this.link_reader != null) {
         this.link_reader.close();
      }
   }

   public void reset() {
      this.it = this.list.iterator();
      this.saved = false;
   }

   public boolean readNextLink() throws IOException {
      if (this.it == null) {
         return false;
      }

      if (this.saved) {
         this.saved = false;
         return true;
      }

      while (this.it.hasNext()) {
         JbNode var1 = this.it.next();
         String var2 = var1.getStr(this.link_reader);
         if (!var2.startsWith("$$$ ")) {
            List var3 = this.link_reader.parser.splitValues(var1, true);
            int var4 = var3.size();
            this.sourcePath = (String)var3.get(0);
            if (this.sourcePath == null || this.sourcePath == "NULL") {
               this.sourcePath = "";
            }

            this.targetPath = (String)var3.get(2);
            if (this.targetPath == null || this.targetPath == "NULL") {
               this.targetPath = "";
            }

            this.targetTag = var4 > 3 ? (String)var3.get(3) : "";
            if (!this.targetTag.equals("skip")) {
               this.target = this.cromMap.get("[" + this.targetPath + "]");
               return true;
            }
         }
      }

      this.sourcePath = "";
      this.targetPath = "";
      this.targetTag = "";
      this.target = null;
      return false;
   }

   public void save() {
      this.saved = true;
   }

   public CROM getTargetDtd() {
      return this.target;
   }

   public String getTargetPath() {
      return this.targetPath;
   }

   private void buildCromMap(CROM var1, boolean var2) {
      String var3 = var1.getPath(var2, true);
      this.cromMap.put(var3, var1);

      for (CROM var5 : var1.getChildren()) {
         this.buildCromMap(var5, var2);
      }
   }

   public enum LinkType {
      en_root,
      en_parent,
      en_child,
      en_branch;
   }
}
