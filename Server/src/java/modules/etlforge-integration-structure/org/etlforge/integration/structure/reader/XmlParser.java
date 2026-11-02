package org.etlforge.integration.structure.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.util.csv.FileContents;
import org.etlforge.util.csv.ParseQuoteFlag;

public class XmlParser implements DocParser {
   static final short t_unknown = 11;
   static final short t_xml_header = 12;
   static final short t_doctype = 13;
   static final short t_start_document = 14;
   static final short t_end_document = 15;
   static final short t_start = 16;
   static final short t_start_end = 17;
   static final short t_end = 18;
   static final short t_error = 19;
   static final short t_comments = 20;
   static final short t_characters = 21;
   static final short t_ignore = 22;
   private static char ef = 239;
   private static char bb = 187;
   private static char bf = 191;
   private static String xmlTokens = "<> =\"'";
   private final FileContents fc;
   private long pos1;
   private long pos2;
   private long pos3;
   private long pos4;
   private FileTokenizer tk;
   private String err_msg;
   private short status = 11;
   private final short start = 0;
   private final short start_attribute = 1;
   private final short equal = 2;

   public XmlParser(FileContents var1) {
      this.fc = var1;
   }

   public void init() {
      this.tk = new FileTokenizer(this.fc, xmlTokens);
   }

   public void init(long var1) {
      if (this.tk == null) {
         this.tk = new FileTokenizer(this.fc, xmlTokens);
      }

      this.tk.setPos(var1);
      this.status = 18;
   }

   public long getTokenEndPosition() {
      assert this.tk != null;
      return this.tk.getTokenEndPos();
   }

   boolean hasMoreEvent() {
      return this.status != 15;
   }

   @Override
   public String getValue(String var1, char var2) {
      return var1;
   }

   short getNextEvent(JbNode var1) {
      if (this.status == 17) {
         this.status = 18;
         return 5;
      }

      try {
         if (this.status == 11) {
            this.status = 14;
            if ((this.fc.at(0L) & 255) == ef && (this.fc.at(1L) & 255) == bb && (this.fc.at(2L) & 255) == bf) {
               this.tk.setPos(3L);
            }

            return 1;
         }

         if (this.tk.hasMoreTokens()) {
            String var2 = this.tk.nextToken();
            this.pos1 = this.tk.getTokenBeginPos();
            if (this.fc.getStr(this.pos1, 4).equals("<!--")) {
               while (this.tk.hasMoreTokens()) {
                  if (this.tk.nextToken().charAt(0) == '>') {
                     this.pos4 = this.tk.getTokenEndPos();
                     if (this.fc.getStr(this.pos4 - 3L, 3).equals("-->")) {
                        break;
                     }
                  }
               }

               this.status = 20;
               return 9;
            }

            if (this.fc.getStr(this.pos1, 9).equals("<![CDATA[")) {
               while (this.tk.hasMoreTokens()) {
                  if (this.tk.nextToken().charAt(0) == '>') {
                     this.pos4 = this.tk.getTokenEndPos();
                     if (this.fc.getStr(this.pos4 - 3L, 3).equals("]]>")) {
                        break;
                     }
                  }
               }

               if (var1 != null) {
                  this.status = 21;
                  return 6;
               }

               throw new RuntimeException("Invalid XML: " + this.err_msg);
            }

            char var3 = var2.charAt(0);
            if (var3 == '\'' || var3 == '"') {
               if (this.status == 16) {
                  short var4 = this.consumeCharacters();
                  if (var4 != -1) {
                     return var4;
                  }
               }

               while (this.tk.hasMoreTokens() && this.tk.nextToken().charAt(0) != var3) {
               }
            }

            switch (var3) {
               case '<':
                  this.tk.skip('>', SkipPosition.To, ParseQuoteFlag.TRUE);
                  this.pos4 = this.tk.getTokenEndPos();
                  this.status = this.parseBetween(var1);
                  switch (this.status) {
                     case 12:
                        return 2;
                     case 13:
                        return 3;
                     case 14:
                     case 15:
                     default:
                        throw new RuntimeException("Invalid XML: " + this.err_msg);
                     case 16:
                        return 4;
                     case 17:
                        return 4;
                     case 18:
                        return 5;
                  }
               case '>':
                  this.err_msg = "Unexpected character '>' encountered at " + this.tk.getLocation();
                  throw new RuntimeException(this.err_msg);
               default:
                  if (var1 != null) {
                     short var6 = this.consumeCharacters();
                     if (var6 != -1) {
                        return var6;
                     }
                  }

                  this.status = 22;
                  return 7;
            }
         }
      } catch (IOException var5) {
         throw new RuntimeException("XmlParser getNextEvent() failed.\n" + var5);
      }

      this.status = 15;
      return 8;
   }

   private short consumeCharacters() throws IOException {
      this.tk.skip('<', SkipPosition.Before, ParseQuoteFlag.FALSE);
      this.pos4 = this.tk.getTokenEndPos();

      for (long var1 = this.pos4 - 1L; var1 >= this.pos1; var1--) {
         if (!Character.isWhitespace(this.fc.at(var1))) {
            this.status = 21;
            return 6;
         }
      }

      return -1;
   }

   short parseBetween(JbNode var1) throws IOException {
      this.pos2 = this.pos1 + 1L;
      this.pos3 = this.pos4 - 1L;
      assert this.fc.at(this.pos1) == 60;
      assert this.fc.at(this.pos3) == 62;
      byte var2 = this.fc.at(this.pos2);
      byte var3 = this.fc.at(this.pos3 - 1L);
      if (var2 == 47) {
         this.pos2++;

         for (long var8 = this.pos2; var8 < this.pos3; var8++) {
            if (this.fc.at(var8) == 58) {
               this.pos2 = var8 + 1L;
               break;
            }
         }

         String var9 = this.getStr(this.pos2, (int)(this.pos3 - this.pos2));
         if (var1 != null && !var9.equals(var1.getName())) {
            this.err_msg = "End element name is inconsistent. name=" + var9 + ", node name=" + var1.getName();
            return 19;
         } else {
            return 18;
         }
      } else {
         int var4 = var2 == 63 ? 12 : (var2 == 33 ? 13 : (var3 == 47 ? 17 : 16));
         switch (var4) {
            case 16:
            case 17:
               for (long var5 = this.pos2; var5 < this.pos3; var5++) {
                  byte var7 = this.fc.at(var5);
                  if (var7 == 58) {
                     this.pos2 = var5 + 1L;
                  }

                  if (Character.isWhitespace(var7) || var7 == 47) {
                     this.pos3 = var5;
                     break;
                  }
               }
            case 12:
            case 13:
            case 14:
            case 15:
            default:
               return (short)var4;
         }
      }
   }

   @Override
   public long readValue(long var1) {
      return 0L;
   }

   @Override
   public long getBeginRecordPos() {
      return this.pos1;
   }

   @Override
   public long getEndRecordPos() {
      return this.pos4;
   }

   @Override
   public String getIdentifier() {
      return this.getStr(this.pos2, (int)(this.pos3 - this.pos2));
   }

   @Override
   public long readRecord(long var1) {
      return 0L;
   }

   @Override
   public String getStr(long var1, int var3) {
      return var3 <= 0 ? "" : this.fc.getStr(var1, var3);
   }

   public String getIdString() {
      return null;
   }

   private void loadValueAttributes(JbNode var1, Map<CROM, String> var2) throws IOException {
      CROM var3 = var1.getCrom();
      String var4 = var1.getValueNodeValuet(this);
      if (var4 != null) {
         CROM var5 = var3.findValueNode();
         if (var5 != null) {
            var2.put(var5, var4);
         }
      }

      String var14 = this.getStr(var1.getPos(), var1.getLength());
      String var6 = null;
      Object var7 = null;
      StringTokenizer var8 = new StringTokenizer(var14, "</>=\"' \t\r\n", true);
      byte var9 = 0;

      label68:
      while (var8.hasMoreTokens()) {
         String var10 = var8.nextToken();
         if (!Character.isWhitespace(var10.charAt(0))) {
            char var11 = var10.charAt(0);
            switch (var11) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
               case '<':
                  var9 = 0;
                  break;
               case '"':
               case '\'':
                  if (var6 == null) {
                     throw new RuntimeException("Valid Xml file should have name defined already.");
                  }

                  if (var9 != 2) {
                     throw new RuntimeException("Misplaced '\"' character.\n" + var14);
                  }

                  if (var6.isEmpty()) {
                     throw new RuntimeException("Unexpected '\"' or ''' character.");
                  }

                  StringBuilder var12 = new StringBuilder();

                  while (true) {
                     if (var8.hasMoreTokens()) {
                        var10 = var8.nextToken();
                        if (var10.charAt(0) != var11) {
                           var12.append(var10);
                           continue;
                        }
                     }

                     var7 = var12.toString();
                     if (!var6.equals("xsi:nil")) {
                        int var13 = var6.indexOf(58);
                        if (var13 >= 0 && var13 + 1 < var6.length()) {
                           var6 = var6.substring(var13 + 1);
                        }
                     }

                     CROM var18 = var3.findAttribute(var6);
                     if (var18 != null) {
                        var2.put(var18, (String)var7);
                     }
                     continue label68;
                  }
               case '/':
               case '>':
                  return;
               case '=':
                  var9 = 2;
                  break;
               default:
                  if (var9 == 1) {
                     var6 = var10;
                     var7 = "";
                  }
            }
         } else {
            var9 = 1;
         }
      }
   }

   @Override
   public List<String> splitValues(JbNode var1, boolean var2) throws IOException {
      HashMap var3 = new HashMap();
      ArrayList var4 = new ArrayList();
      if (!var1.isValueNode()) {
         this.loadValueAttributes(var1, var3);
         CROM var5 = var1.getCrom();
         Iterator var6 = var5.getChildren().iterator();

         CROM var7;
         while ((var7 = CROM.getNextValueAttribute(var6)) != null) {
            String var8 = (String)var3.get(var7);
            if (var8 != null && KongaStringUtils.hasXmlEscape(var8)) {
               var8 = StringEscapeUtils.unescapeXml(var8);
            }

            if (var2) {
               var8 = var7 + "=" + var8;
            }

            var4.add(var8);
         }
      }

      return var4;
   }
}
