package org.etlforge.integration.structure.reader;

import com.google.common.collect.Maps;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.axis.utils.StringUtils;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.crom.LinkIterator;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureFactory;
import org.etlforge.util.csv.FileContents;

public class DocReader implements Closeable {
   private Map<String, String> m_stringMap = Maps.newHashMap();
   FileContents fc;
   String file_name;
   public JbNode root;
   public DocParser parser;
   public boolean isFlat;
   boolean isXml;

   DocReader(File var1, boolean var2, boolean var3, String var4) {
      this.fc = new FileContents(var1, getEncoding(var4));
      this.isFlat = var2;
      this.isXml = var3;
   }

   private static Charset getEncoding(String var0) {
      return StringUtils.isEmpty(var0) ? Charset.defaultCharset() : Charset.forName(var0);
   }

   public JbNode parse_all(int var1, int var2) {
      return null;
   }

   public JbNode assignToStructure(CROM_Tree var1, LinkIterator var2, StringBuilder var3) throws Exception {
      return null;
   }

   public void convertToXmlDataNode() {
   }

   public String getStr(long var1, int var3) {
      return this.fc.getStr(var1, var3);
   }

   String getString(String var1) {
      String var2 = this.m_stringMap.get(var1);
      if (var2 == null) {
         var2 = var1;
         this.m_stringMap.put(var2, var2);
      }

      return var2;
   }

   public void clearStringMap() {
      this.m_stringMap = Maps.newHashMap();
   }

   @Override
   public void close() throws IOException {
      if (this.fc != null) {
         this.fc.close();
      }

      this.fc = null;
   }

   public Map<String, String> getAttributeMap(JbNode var1) throws IOException {
      HashMap var2 = new HashMap();
      if (var1 == null) {
         return var2;
      }

      CROM var3 = var1.getCrom();
      if (var3 == null) {
         return var2;
      }

      if (var3.isAnyElement()) {
         String var9 = this.parser.getStr(var1.m_pos, var1.m_length);
         var2.put("", var9);
         return var2;
      }

      List var4 = this.parser.splitValues(var1, false);
      Iterator var5 = var3.getChildren().iterator();
      CROM var6 = CROM.getNextValueAttribute(var5);
      if (var4 != null) {
         for (Object __etlItem98 : var4) {
            String var8 = (String) __etlItem98;
            if (var6 == null) {
               break;
            }

            var2.put(var6.getName(), var8);
            var6 = CROM.getNextValueAttribute(var5);
         }
      }

      while (var6 != null) {
         var6.setValue(null);
         var6 = CROM.getNextValueAttribute(var5);
      }

      return var2;
   }

   public void moveChildren(JbNode var1, JbNode var2, int var3) {
      List var4 = var1.getChildren();
      int var5 = var4.size();
      int var6 = var5 - var3;
      if (var5 != var3) {
         assert var6 > 0;
         var1.m_children = var4.subList(0, var3);
         var2.m_children = var4.subList(var3, var5);
         int var7 = 0;

         for (JbNode var9 : var2.m_children) {
            var9.m_parent = var2;
            var9.m_id = var7++;
         }
      }
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();

      try {
         this.root.addToStringBuilder(this.parser, var1, "");
      } catch (Exception var3) {
         var1.append("Encounter Exception\n");
      }

      return var1.toString();
   }

   public String getLocation(JbNode var1) {
      long var2 = var1.m_pos;

      try {
         return this.fc.getLocation(var2).toString();
      } catch (IOException var5) {
         return var1.getName() + " pos=" + var2;
      }
   }

   public static void main(String[] var0) {
      short var1 = 521;
      boolean var2 = true;
      String var3 = "Z:\\tmp\\etlforge\\OpId_284_8d189686-75c8-4103-9659-116eabc06490\\tran521.target.bak";
      DocReader var4 = null;

      try {
         String var5 = "C:\\Program Files\\ETLForge Integration Server\\DataInterchange\\jtr\\";
         String var6 = var5 + "tran" + Integer.toString(var1) + (var2 ? "t" : "s") + ".jtr";
         File var7 = new File(var6);
         MappingTreeStructure var8 = MappingTreeStructureFactory.fromFile(var7);
         CROM_Tree var9 = CROM_Tree.fromMappingStructure(var8);
         CROM var10 = var9.getRoot();
         File var11 = new File(var3);
         if (var10.isXml()) {
            var4 = new XmlReader(var11, var10);
         } else {
            var4 = new TextDocReader(var11, false, var10);
         }

         JbNode var12 = var4.parse_all(0, -1);
         var4.assignToStructure(null, null, null);
         var12.print(System.out, var4.parser);
      } catch (IOException var23) {
         var23.printStackTrace();
      } catch (Exception var24) {
         var24.printStackTrace();
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var22) {
         }
      }
   }

   public String getFileName() {
      return this.fc.getFileName();
   }
}
