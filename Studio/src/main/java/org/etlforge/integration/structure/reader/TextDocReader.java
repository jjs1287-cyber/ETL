package org.etlforge.integration.structure.reader;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.crom.LinkIterator;

public class TextDocReader extends DocReader {
   private Map<CROM, JbNode> lastJbNodes = Maps.newHashMap();
   private char delimiter;
   private char qualifier;
   private String delimiter_str;
   private CROM dtdRoot;
   private CROM dtd;
   private JbNode node;
   private final boolean isFix;
   private final boolean isCsv;
   private final boolean isDatabase;
   private final boolean isLdap;
   private int pos = 0;
   private int len = 0;

   public TextDocReader(File var1, boolean var2, CROM var3) {
      super(var1, var2, false, null);
      assert var3 != null;
      this.dtdRoot = var3;
      this.dtd = var3.getFirstChildFolder();
      this.isDatabase = this.dtd.isDatabase();
      this.isLdap = this.dtd.isLdap();
      if (!this.isDatabase && !this.isLdap) {
         this.delimiter_str = this.getDelimiterQualifierChar(this.dtd.getDelimiter());
         this.delimiter = this.delimiter_str != null && !this.delimiter_str.isEmpty() ? this.delimiter_str.charAt(0) : '\u0000';
         String var4 = this.getDelimiterQualifierChar(this.dtd.getStrQual());
         this.qualifier = var4 != null && !var4.isEmpty() ? var4.charAt(0) : '\u0000';
         this.fc.setEncoding(Charsets.ISO_8859_1);
      } else {
         this.delimiter = ',';
         this.delimiter_str = ",";
         this.qualifier = '"';
         this.fc.setEncoding(Charsets.UTF_8);
      }

      this.isCsv = this.dtd.isCsv();
      this.isFix = this.delimiter == 0;
      if (this.isFix) {
         this.pos = var3.getFieldPos();
         this.len = var3.getFieldLength();
         this.parser = new FixFieldParser(this.fc, this.pos - 1, this.len, var2);
      } else if (this.isCsv) {
         this.parser = new CSVParser(this.fc, this.qualifier, this.delimiter, this.delimiter_str, var2);
      } else {
         this.parser = new TextParser(this.fc, this.qualifier, this.delimiter, this.delimiter_str, var2);
      }
   }

   public TextDocReader(File var1, Charset var2) throws IOException {
      this(var1, var2.name());
   }

   public TextDocReader(File var1, String var2) throws IOException {
      super(var1, true, false, var2);
      this.isCsv = true;
      this.isDatabase = false;
      this.isLdap = false;
      this.delimiter = ',';
      this.delimiter_str = ",";
      this.qualifier = '"';
      this.isFix = false;
      this.parser = new CSVParser(this.fc, this.qualifier, this.delimiter, this.delimiter_str, this.isFlat);
   }

   @Override
   public JbNode parse_all(int var1, int var2) {
      short var3 = 239;
      short var4 = 187;
      short var5 = 191;

      try {
         this.root = new JbNode(this, null, "", 0L, 0L);
         this.node = this.root;
         long var6 = 0L;
         if ((this.fc.at(0L) & 255) == var3 && (this.fc.at(1L) & 255) == var4 && (this.fc.at(2L) & 255) == var5) {
            var6 = 3L;
         }

         for (int var8 = 1; (var6 = this.parser.readRecord(var6)) >= 0L; var8++) {
            if (var8 > var1) {
               new JbNode(this, this.node, this.parser.getIdentifier(), this.parser.getBeginRecordPos(), this.parser.getEndRecordPos());
               if (var2 > 0) {
                  if (--var2 == 0) {
                     break;
                  }
               }
            }
         }

         return this.root;
      } catch (IOException var9) {
         throw new RuntimeException("TextDocReader parse_all failed.\n" + var9);
      }
   }

   @Override
   public JbNode assignToStructure(CROM_Tree var1, LinkIterator var2, StringBuilder var3) throws IOException {
      if (this.root == null) {
         return null;
      }

      this.root.setCrom(this.dtdRoot);
      List var4 = this.root.m_children;
      this.root.m_children = Lists.newArrayList();
      boolean var5 = var2.readNextLink();
      if (var5) {
         String var6 = var2.getTargetPath();
         if (var6 != null && !var6.equals(this.dtdRoot.getPath_(this.isFlat))) {
            var2.save();
         }
      }

      this.node = this.root;
      this.dtd = this.dtdRoot;
      this.lastJbNodes = Maps.newHashMap();
      if (this.isFlat) {
         if (var5) {
            CROM var10 = null;
            var2.reset();

            for (Object __etlItem135 : var4) {
               JbNode var8 = (JbNode) __etlItem135;
               var2.readNextLink();
               CROM var9 = var2.getTargetDtd();
               if (var9 != var10) {
                  var10 = var9;
                  this.node = new JbNode(this.root, var9, 0L, 0L);
               }

               var8.convertToInstanceNode(this.node);
            }
         } else {
            this.dtd = this.dtd.getFirstChildFolder();
            this.node = new JbNode(this.root, this.dtd, 0L, 0L);

            for (Object __etlItem149 : var4) {
               JbNode var13 = (JbNode) __etlItem149;
               var13.convertToInstanceNode(this.node);
            }
         }
      } else {
         this.addLastJbNode(this.lastJbNodes, this.dtd, this.node);

         for (Object __etlItem156 : var4) {
            JbNode var14 = (JbNode) __etlItem156;
            var2.readNextLink();
            CROM var15 = null;
            if (var5) {
               var15 = var2.getTargetDtd();
            }

            this.createDataNode(var14, var15);
         }
      }

      this.clearStringMap();
      return this.root;
   }

   private void createDataNode(JbNode var1, CROM var2) throws IOException {
      String var3 = var1.getName();
      if (var2 != null) {
         var1.changeId(this, var2.getSegmentIdentifier());
         this.dtd = var2;
      } else {
         this.dtd = this.dtd.findSegment(var3);
      }

      this.dtd.expandNode(null);
      CROM var4 = this.dtd.getParent();
      if (var4 != null) {
         JbNode var5 = this.lastJbNodes.get(var4);
         if (this.dtd.isLoopNode()) {
            JbNode var6 = var5.getChild(this.dtd);
            if (var6 == null) {
               var6 = new JbNode(var5, this.dtd, 0L, 0L);
            }

            var1.convertToInstanceNode(var6);
         } else {
            var1.convertToDtdNode(var5, this.dtd);
         }

         this.addLastJbNode(this.lastJbNodes, this.dtd, var1);
      }
   }

   private void addLastJbNode(Map<CROM, JbNode> var1, CROM var2, JbNode var3) {
      CROM[] var4 = var1.keySet().toArray(new CROM[var1.size()]);

      for (CROM var8 : var4) {
         if (var2.isAncestorOf(var8)) {
            var1.remove(var8);
         }
      }

      var1.put(var2, var3);
   }

   private String getDelimiterQualifierChar(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         char[] var2 = var1.toCharArray();
         int var3 = var2.length;
         if (var3 == 1) {
            return var1;
         }

         if (var2[0] == '\\') {
            if (var3 == 2) {
               if (var2[1] == 't') {
                  return "\t";
               }

               if (var2[1] == 'n') {
                  return "\n";
               }

               if (var2[1] == 'r') {
                  return "\r";
               }
            }

            if (var3 == 4 && Character.isDigit(var2[1]) && Character.isDigit(var2[2]) && Character.isDigit(var2[3])) {
               int var4 = (var2[1] - '0') * 100 + (var2[2] - '0') * 10 + (var2[3] - '0');
               return new String() + (char)var4;
            }
         }

         return var1;
      } else {
         return "";
      }
   }

   public long readRecord(long var1) throws IOException {
      return this.parser.readRecord(var1);
   }
}
