package org.etlforge.integration.structure.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.crom.CromType;
import org.etlforge.integration.structure.mapping.CromToMappingStructureWriter;
import org.etlforge.util.csv.FileContents;

public class DtdParser {
   private final String dtdFileName;
   private final Set<String> systemEntities;
   private final Set<String> publicEntities;
   private final Set<String> parameterEntities;
   private final Map<String, String> entityMap;
   private final Map<String, CROM> objMap;
   private final List<String> rootCandidates;
   private final Map<String, List<CROM>> attlistMap;
   private final Set<String> files;
   int nTag = 0;
   private final boolean main_dtd = true;
   private final boolean included_dtd = false;
   private final boolean entity_def_only = true;
   private final boolean process_all = false;

   public DtdParser(String var1) {
      this.dtdFileName = var1;
      this.systemEntities = new HashSet<>();
      this.publicEntities = new HashSet<>();
      this.parameterEntities = new HashSet<>();
      this.entityMap = new HashMap<>();
      this.objMap = new HashMap<>();
      this.attlistMap = new HashMap<>();
      this.files = new HashSet<>();
      this.rootCandidates = new ArrayList<>();
   }

   public void init() {
      this.readDtdFile(this.dtdFileName, true, true);
      this.files.clear();
      this.readDtdFile(this.dtdFileName, true, false);
      this.files.clear();
   }

   public List<String> getRootCandidates() {
      return this.rootCandidates;
   }

   public CROM getStructureTree(String var1) {
      CROM var2 = this.objMap.get(var1);
      CROM var3 = new CROM(null, var2, null);
      this.nTag = 0;
      this.addChildren(var3, var2);
      return var3;
   }

   public static void writeXml(Writer var0, CROM var1) throws IOException {
      CROM_Tree var2 = new CROM_Tree(var1);
      CromToMappingStructureWriter var3 = new CromToMappingStructureWriter(var1, new CROMHeader(var1.cromType.name));
      BufferedWriter var4 = new BufferedWriter(var0);
      var2.writeXml(var4, var3);
      var4.close();
   }

   private void readDtdFile(String var1, boolean var2, boolean var3) {
      String var4 = null;

      try {
         if (!this.files.contains(var1)) {
            this.files.add(var1);
            FileContents var5 = new FileContents(var1);
            long var6 = 0L;
            long var8 = var5.size();

            while (var6 < var8) {
               long var10 = this.findStmtStartPosition(var6, var5);
               long var12 = this.findStmtEndPostion(var10, var5);
               if (var12 < 0L) {
                  var4 = var5.getStr(var10, 100);
                  throw new Exception("can't find the end position of the statement.");
               }

               if (var12 > var10) {
                  var4 = var5.getStr(var10, (int)(var12 - var10));
                  this.parseStatement(var4, var2, var3);
               }

               var6 = var12;
            }

            var5.close();
         }
      } catch (Exception var14) {
         var14.printStackTrace();
         throw new RuntimeException("DtdParser error, " + var14.getMessage() + " Statement:\"" + var4 + "\"");
      }
   }

   private void parseStatements(String var1, boolean var2) {
      String var3 = null;

      try {
         int var4 = 0;
         int var5 = var1.length();

         while (var4 < var5) {
            int var6 = this.findStmtStartPosition(var4, var1);
            int var7 = this.findStmtEndPostion(var6, var1);
            if (var7 < 0) {
               var3 = var1.substring(var6, 100);
               throw new Exception("can't find the end position of the statement.");
            }

            if (var7 > var6) {
               var3 = var1.substring(var6, var7);
               this.parseStatement(var3, var2);
            }

            var4 = var7;
         }
      } catch (Exception var8) {
         throw new RuntimeException("DtdParser error, " + var8.getMessage() + " Statement:\"" + var3 + "\"");
      }
   }

   private void parseStatement(String var1, boolean var2) throws Exception {
      this.parseStatement(var1, var2, false);
   }

   private static String toXmlString(List<CROM> var0) {
      String var1 = "[";
      boolean var2 = false;

      for (CROM var4 : var0) {
         if (!var2) {
            var2 = true;
         } else {
            var1 = var1 + ",";
         }

         var1 = var1 + var4.toXmlString();
      }

      return var1 + "]";
   }

   private void parseStatement(String var1, boolean var2, boolean var3) throws Exception {
      if (var1.startsWith("<!ENTITY")) {
         this.ProcessEntity(var1);
      } else if (var1.startsWith("%") || var1.startsWith("&")) {
         int var9 = var1.length();
         if (var9 >= 3 && var1.charAt(var9 - 1) != ';') {
         }

         String var11 = var1.substring(1, var9 - 1);
         if (this.systemEntities.contains(var11) || this.publicEntities.contains(var11)) {
            String var12 = this.getEntityValue(var11);
            String var7 = this.getLocalDtdFileName(var12);
            this.readDtdFile(var7, false, var3);
         }
      } else if (var1.startsWith("SYSTEM") || var1.startsWith("PUBLIB")) {
         int var8 = var1.indexOf(34);
         int var10;
         if (var8 >= 0 && (var10 = var1.indexOf(34, var8 + 1)) >= 0 && var10 > var8 + 1) {
            String var6 = this.getLocalDtdFileName(var1.substring(var8 + 1, var10));
            this.readDtdFile(var6, false, var3);
         } else {
            throw new Exception("can't find file name.");
         }
      } else if (!var3) {
         if (!var1.startsWith("<!--")) {
            if (var1.startsWith("<!ELEMENT")) {
               CROM var4 = this.ParseELEMENT(var1);
               var4.addType(8193L);
               String var5 = var4.getName();
               if (var5.isEmpty()) {
                  throw new Exception("parse ELEMENT error, object name is empty.");
               }

               this.objMap.put(var5, var4);
               if (var2) {
                  this.rootCandidates.add(var5);
               }
            } else if (var1.startsWith("<!ATTLIST")) {
               this.ParseATTLIST(var1);
            } else if (var1.startsWith("<![")) {
               this.ProcessCondition(var1, var2);
            } else if (!var1.startsWith("<!NOTATION")
               && !var1.startsWith("<!DOCTYPE")
               && !var1.startsWith("<HTML>")
               && !var1.startsWith("<HEAD>")
               && !var1.startsWith("<META")
               && !var1.startsWith("<BODY>")
               && !var1.startsWith("<XMP>")
               && !var1.startsWith("</HTML>")
               && !var1.startsWith("</HEAD>")
               && !var1.startsWith("<META")
               && !var1.startsWith("</BODY>")
               && !var1.startsWith("</XMP>")
               && !var1.startsWith("<?xml ")) {
               if (var1.length() > 200) {
                  var1 = var1.substring(0, 200) + "...";
               }

               throw new Exception("DTDParser encounter unexpected syntax \"" + var1 + "\"");
            }
         }
      }
   }

   private CROM ParseELEMENT(String var1) throws Exception {
      if (var1.length() >= 10 && Character.isWhitespace(var1.charAt(9))) {
         var1 = this.replaceEntity(var1);
         CROM var2 = null;
         CROM var3 = null;
         CROM var4 = null;
         Stack var5 = new Stack();
         StringTokenizer var6 = new StringTokenizer(var1, " \t\r\n>()*?+%|,#", true);

         while (var6.hasMoreTokens()) {
            String var7 = var6.nextToken();
            char var8;
            switch (var8 = var7.charAt(0)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
               case ',':
                  break;
               case '#':
                  if (!var6.hasMoreTokens()) {
                     throw new Exception("parse ELEMENT failed, misplaced '#'.");
                  }

                  var7 = var6.nextToken();
                  if (var7.equals("PCDATA")) {
                     var3 = new CROM(var2, false, "", -1, CromType.CROM_t_Xml);
                     var3.addType(34L);
                  }
                  break;
               case '%':
                  throw new Exception("parse ELEMENT failed, unexpect '%' character encountered.");
               case '(':
                  var3 = new CROM(var2, true, "", -1, CromType.CROM_t_Xml);
                  var3.setType(0L);
                  var5.push(var2);
                  var2 = var3;
                  var3 = null;
                  break;
               case ')':
                  if (var5.size() == 0) {
                     throw new Exception("parse ELEMENT failed, missing closing parenthesis.");
                  }

                  var3 = var2;
                  var2 = (CROM)var5.pop();
                  break;
               case '*':
               case '+':
               case '?':
                  if (var3 == null) {
                     throw new Exception("parse ELEMENT failed, multiplicity character '" + var8 + "' can't find object.");
                  }

                  long var9 = 64L;
                  switch (var8) {
                     case '*':
                        var9 = 8L;
                        break;
                     case '+':
                        var9 = 64L;
                        break;
                     case '?':
                        var9 = 32L;
                  }

                  var3.addType(var9);
                  break;
               case '>':
                  if (var5.size() == 0) {
                     throw new Exception("parse ELEMENT failed, extra '>' character encountered.");
                  }

                  if (var3 != null && var3.getType() == 0L && var2 != null) {
                     var2.removeExtraFolder();
                  }

                  var5.pop();
                  if (var4 != null) {
                     this.nTag = var4.cleanUpUnnecessaryFolder(this.nTag);
                  }
                  break;
               case '|':
                  if (var2 != null && var3 != null) {
                     var2.addType(16L);
                     break;
                  }

                  throw new Exception("parse ELEMENT failed, misplaced domain separator '|'.");
               default:
                  if (var7.equals("EMPTY")) {
                     var3 = var2;
                     if (var3 == null) {
                        throw new Exception("parse ELEMENT failed, missplaced \"EMPTY\" token " + var1);
                     }
                  } else if (var7.equals("ANY")) {
                     if (var2 == null) {
                        throw new Exception("parse ELEMENT failed, missplaced \"ANY\" token " + var1);
                     }

                     var3 = var2;
                     var3.addType(4194304L);
                     new CROM(var3, false, "", -1, CromType.CROM_t_Xml).addType(2L);
                  } else if (!var7.equals("<!ELEMENT")) {
                     var3 = new CROM(var2, true, var7, -1, CromType.CROM_t_Xml);
                     var3.addType(1L);
                     if (var4 == null) {
                        var4 = var3;
                        var2 = var3;
                        var5.push(var2);
                        var3 = null;
                     }
                  }
            }
         }

         return var4;
      } else {
         throw new Exception("parse ELEMENT failed, invalid statement.");
      }
   }

   private static CROM findExistedAttribute(List<CROM> var0, String var1) {
      for (CROM var3 : var0) {
         if (var1.equals(var3.getName())) {
            return var3;
         }
      }

      return null;
   }

   private void ParseATTLIST(String var1) throws Exception {
      if (var1.length() >= 10 && Character.isWhitespace(var1.charAt(9))) {
         var1 = this.replaceEntity(var1);
         CROM var2 = null;
         List var3 = null;
         StringTokenizer var4 = new StringTokenizer(var1, ">(|),# \t\r\n\"'%;", true);

         label102:
         while (var4.hasMoreTokens()) {
            String var5 = var4.nextToken();
            char var7;
            switch (var7 = var5.charAt(0)) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
               case ',':
                  break;
               case '"':
               case '\'':
                  var5 = "";

                  while (true) {
                     if (var4.hasMoreTokens()) {
                        String var16 = var4.nextToken();
                        if (var16.charAt(0) != var7) {
                           var5 = var5 + var16;
                           continue;
                        }
                     }

                     if (var2 != null) {
                        var2.setDefaultValue(var5);
                     }
                     continue label102;
                  }
               case '#':
                  if (var4.hasMoreTokens()) {
                     var5 = var4.nextToken();
                     if (var5.equals("IMPLIED")) {
                        if (var2 != null) {
                           var2.addType(32L);
                        }
                     } else if (var5.equals("FIXED") && var2 != null) {
                        var2.addType(2147483648L);
                     }
                  }
                  break;
               case '%':
                  throw new Exception("internal error in parsing ATTLIST, unexpect '%' character encountered.");
               case '(':
                  if (var2 == null) {
                     throw new Exception("process ATTLIST failed, domain definitions cannot find attribute.");
                  }

                  String var8 = "|";

                  while (true) {
                     boolean var6;
                     if (var6 = var4.hasMoreTokens()) {
                        var5 = var4.nextToken();
                        if ((var7 = var5.charAt(0)) != ')') {
                           if (var7 != ' ' && var7 != '\t' && var7 != '\r' && var7 != '\n') {
                              var8 = var8 + var5;
                           }
                           continue;
                        }
                     }

                     if (!var6) {
                        throw new Exception("PARSE_ATTLIST_MISSING_RP");
                     }

                     var8 = var8 + '|';
                     var2.setDomain(var8);
                     continue label102;
                  }
               case ')':
                  throw new Exception("process ATTLIST syntax error, unexpected ')' encountered");
               case '>':
                  return;
               case '|':
                  throw new Exception("process ATTLIST syntax error, unexpected '|' encountered");
               default:
                  if (!var5.equals("<!ATTLIST")
                     && !var5.equals("ENTITY")
                     && !var5.equals("ID")
                     && !var5.equals("IDREF")
                     && !var5.equals("IDREFS")
                     && !var5.equals("NMTOKEN")
                     && !var5.equals("NMTOKENS")
                     && !var5.equals("ENTITIES")
                     && !var5.equals("CDATA")
                     && !var5.equals("PCDATA")) {
                     if (var3 == null) {
                        String var9 = var5;
                        var3 = this.find_attlist(var9);
                     } else {
                        var2 = findExistedAttribute(var3, var5);
                        if (var2 == null) {
                           var2 = new CROM(null, false, var5, -1, CromType.CROM_t_Xml);
                           var3.add(var2);
                        }
                     }
                  }
            }
         }
      } else {
         throw new Exception("parse ATTLIST failed, invalid statement.");
      }
   }

   private void ProcessEntity(String var1) throws Exception {
      if (var1.length() >= 9 && Character.isWhitespace(var1.charAt(8))) {
         StringTokenizer var2 = new StringTokenizer(var1, " \t\r\n%\"';>", true);
         String var4 = null;
         String var5 = null;
         DtdParser.EntityType var6 = DtdParser.EntityType.general_entity;
         boolean var7 = false;

         while (var2.hasMoreTokens()) {
            String var3 = var2.nextToken();
            char var8 = var3.charAt(0);
            switch (var8) {
               case '\t':
               case '\n':
               case '\r':
               case ' ':
               case ';':
               case '>':
                  break;
               case '"':
               case '\'':
                  var5 = this.getStringUntilToken(var2, var3);
                  break;
               case '%':
                  if (var4 != null) {
                     throw new Exception("process parameter entity failed, encounter '%' character after entity-name defined.");
                  }

                  if (!var2.hasMoreTokens() || var2.nextToken().charAt(0) != ' ') {
                     throw new Exception("process parameter entity failed, can't find space after '%' character.");
                  }

                  if (!var2.hasMoreTokens()) {
                     throw new Exception("process parameter entity failed, can't find entity-name.");
                  }

                  var7 = true;
                  break;
               default:
                  if (!var3.startsWith("<!ENTITY") && !var3.startsWith("CDATA")) {
                     if (var3.startsWith("SYSTEM")) {
                        var6 = DtdParser.EntityType.system_entity;
                     } else if (var3.startsWith("PUBLIC")) {
                        var6 = DtdParser.EntityType.public_enetity;
                     } else {
                        if (var4 != null) {
                           throw new Exception("process parameter entity failed, try to redefine key \"" + var4 + "\".");
                        }

                        var4 = var3;
                        var5 = "";
                     }
                  }
            }
         }

         if (var4 != null && var5 != null) {
            this.entityMap.put(var4, var5);
            if (var7) {
               this.parameterEntities.add(var4);
            }

            if (var6 == DtdParser.EntityType.system_entity) {
               this.systemEntities.add(var4);
            } else if (var6 == DtdParser.EntityType.public_enetity) {
               this.publicEntities.add(var4);
            }
         } else {
            throw new Exception("process parameter entity failed, incomplete pair: key \"" + var4 + "\" value=\"" + var5 + "\"");
         }
      } else {
         throw new Exception("process ENTITY failede, invalid statement");
      }
   }

   private String getStringUntilToken(StringTokenizer var1, String var2) throws Exception {
      String var3 = "";
      boolean var4 = false;

      while (var1.hasMoreTokens()) {
         String var5 = var1.nextToken();
         if (var2.equals(var5)) {
            var4 = true;
            break;
         }

         var3 = var3 + var5;
      }

      if (!var4) {
         throw new Exception("DtdParser ERROR, missing closing quote.");
      } else {
         return var3;
      }
   }

   private void ProcessCondition(String var1, boolean var2) throws Exception {
      if (var1.startsWith("<![") && var1.endsWith("]]>")) {
         String var3 = var1.substring(3, var1.length() - 2);
         String var4 = null;
         String var5 = null;
         StringTokenizer var6 = new StringTokenizer(var3, "%;'\"[]", true);

         while (var6.hasMoreTokens()) {
            String var7 = var6.nextToken();
            char var8 = var7.charAt(0);
            switch (var8) {
               case '\t':
               case '\r':
               case ' ':
                  break;
               case '"':
               case '\'':
                  var7 = this.getStringUntilToken(var6, var7);
               default:
                  if (var4 != null) {
                     throw new Exception("unexpected syntax encountered, after condition was defined.");
                  }

                  var4 = var7;
                  if (var4 == null) {
                     throw new Exception("token can not be null.");
                  }

                  if (!var4.equalsIgnoreCase("INCLUDE")) {
                     return;
                  }
                  break;
               case '%':
                  if (var4 != null || !var6.hasMoreTokens()) {
                     throw new Exception("process condition emcountered misplaced '%'");
                  }

                  String var9 = var6.nextToken();
                  if (!var6.hasMoreTokens()) {
                     throw new Exception("incomplete statement for condition statement");
                  }

                  if (var6.nextToken().charAt(0) != ';') {
                     throw new Exception("incomplete entity reference. statement");
                  }

                  var4 = this.getEntityValue(var9);
                  if (!var4.equalsIgnoreCase("INCLUDE")) {
                     return;
                  }
                  break;
               case '[':
                  var5 = this.getStringUntilToken(var6, "]");
                  if (var4 == null) {
                     throw new Exception("missing condition statement");
                  }

                  this.parseStatements(var5, var2);
            }
         }
      } else {
         throw new Exception("process condition failed, invalid statement.");
      }
   }

   private List<CROM> find_attlist(String var1) {
      List var2 = this.attlistMap.get(var1);
      if (var2 == null) {
         var2 = new ArrayList();
         this.attlistMap.put(var1, var2);
      }

      return var2;
   }

   private String getLocalDtdFileName(String var1) throws Exception {
      int var2 = this.dtdFileName.lastIndexOf(47);
      int var3 = this.dtdFileName.lastIndexOf(92);
      int var4 = var2 > var3 ? var2 : var3;
      String var5 = var4 < 0 ? "" : this.dtdFileName.substring(0, var4 + 1);
      var2 = var1.lastIndexOf(47);
      var3 = var1.lastIndexOf(92);
      var4 = var2 > var3 ? var2 : var3;
      if (var2 <= 0 && var3 <= 0) {
         return var5 + var1;
      } else {
         String var6 = var4 < 0 ? "" : var1.substring(0, var4 + 1);
         if (!var5.equalsIgnoreCase(var6)) {
            throw new Exception(
               "Currently we only consider dtd files are under the same directory. parent_file:\"" + this.dtdFileName + "\", include_file:\"" + var1 + "\""
            );
         } else {
            return var1;
         }
      }
   }

   private int findStmtStartPosition(int var1, String var2) throws IOException {
      int var3 = var1;

      while (Character.isWhitespace(var2.charAt(var3))) {
         var3++;
      }

      return var3;
   }

   private long findStmtStartPosition(long var1, FileContents var3) throws IOException {
      long var4 = var1;

      while (Character.isWhitespace((char)var3.at(var4))) {
         var4++;
      }

      return var4;
   }

   private int findStmtEndPostion(int var1, String var2) throws IOException {
      if (var1 >= var2.length()) {
         return var2.length();
      }

      char var3 = var2.charAt(var1);
      int var4 = -1;
      switch (var3) {
         case '%':
         case '&':
            var4 = var2.indexOf(59, var1 + 1);
            if (var4 > 0) {
               var4++;
            }
            break;
         case '<':
            if (var2.startsWith("<![")) {
               var4 = var2.indexOf("]]>", var1 + 3);
               if (var4 > 0) {
                  var4 += 3;
               }
            } else if (var2.startsWith("<!--")) {
               var4 = var2.indexOf("-->", var1 + 4);
               if (var4 > 0) {
                  var4 += 3;
               }
            } else {
               var4 = var2.indexOf(62, var1 + 1);
               if (var4 > 0) {
                  var4++;
               }
            }
            break;
         case 'P':
         case 'S':
            var4 = var2.indexOf(34, var1);
            if (var4 >= 0) {
               var4 = var2.indexOf(34, ++var4);
               if (var4 > 0) {
                  var4++;
               }
            }
      }

      return var4;
   }

   private long findStmtEndPostion(long var1, FileContents var3) throws IOException {
      byte var4 = var3.at(var1);
      long var5 = -1L;
      switch (var4) {
         case 0:
            var5 = var3.size();
            break;
         case 37:
         case 38:
            var5 = this.findEndPosition(var1 + 1L, ";", var3);
            break;
         case 60:
            if (var3.at(var1 + 1L) == 33 && var3.at(var1 + 2L) == 91) {
               var5 = this.findEndPosition(var1 + 3L, "]]>", var3);
            } else if (var3.at(var1 + 1L) == 33 && var3.at(var1 + 2L) == 45 && var3.at(var1 + 3L) == 45) {
               var5 = this.findEndPosition(var1 + 4L, "-->", var3);
            } else {
               var5 = this.findEndPosition(var1 + 1L, ">", var3);
            }
            break;
         case 80:
         case 83:
            var1 = this.findEndPosition(var1, "\"", var3);
            var5 = this.findEndPosition(var1, "\"", var3);
      }

      return var5;
   }

   private long findEndPosition(long var1, String var3, FileContents var4) throws IOException {
      int var5 = var3.length();
      if (var5 == 1) {
         for (long var6 = var1; var4.at(var6) != 0; var6++) {
            byte var8 = (byte)var3.charAt(0);
            if (var4.at(var6) == var8) {
               return var6 + 1L;
            }
         }
      } else {
         byte[] var7 = var3.getBytes();

         for (long var11 = var1; var4.at(var11) != 0; var11++) {
            int var10 = 0;

            while (var10 < var5 && var4.at(var11 + var10) == var7[var10]) {
               var10++;
            }

            if (var10 == var5) {
               return var11 + var5;
            }
         }
      }

      return -1L;
   }

   private String replaceEntity(String var1) throws Exception {
      Stack var2 = new Stack();
      return this.replaceEntity(var1, var2);
   }

   private String replaceEntity(String var1, Stack<String> var2) throws Exception {
      int var5 = 0;
      int var6 = var1.length();
      String var7 = "";

      for (int var8 = 0; var8 < var6; var8 = var5 + 1) {
         int var3 = var1.indexOf(37, var8);
         int var4 = var1.indexOf(38, var8);
         if (var3 < 0 && var4 < 0) {
            break;
         }

         if (var3 < 0 || var4 >= 0 && var4 < var3) {
            var3 = var4;
         }

         if (var3 < var6) {
            var5 = var1.indexOf(59, var3);
            if (var5 < 0) {
               throw new Exception("entity replacement failed to find closing character ';' from " + var1);
            }
         }

         var7 = var7 + var1.substring(var8, var3);
         String var9 = var1.substring(var3 + 1, var5);
         String var10 = this.getEntityValue(var9, var2);
         var7 = var7 + var10;
      }

      if (var5 > 0) {
         var7 = var7 + var1.substring(var5 + 1, var6);
      }

      return var5 == 0 ? var1 : var7;
   }

   private String getEntityValue(String var1) throws Exception {
      Stack var2 = new Stack();
      return this.getEntityValue(var1, var2);
   }

   private String getEntityValue(String var1, Stack<String> var2) throws Exception {
      if (var2.contains(var1)) {
         throw new Exception("recursive reference of entity \"" + var1 + "\"");
      }

      var2.add(var1);
      String var3 = this.entityMap.get(var1);
      if (var3 == null) {
         throw new Exception("entity replacement failed, can't find entity_name " + var1);
      }

      var3 = this.replaceEntity(var3, var2);
      var2.pop();
      return var3;
   }

   static boolean isNum(String var0) {
      int var1 = 0;

      for (int var2 = var0.length(); var1 < var2; var1++) {
         if (!Character.isDigit(var0.charAt(var1))) {
            return false;
         }
      }

      return true;
   }

   private void addChildren(CROM var1, CROM var2) {
      CROM var3 = null;
      CROM var4 = null;

      for (CROM var6 : var2.getChildren()) {
         var3 = new CROM(var1, var6, var3);
         if (var6.isValueLeaf()) {
            var4 = var3;
         } else {
            String var7 = var3.getName();
            if (!var3.getName().isEmpty() && !isNum(var7)) {
               CROM var8 = this.objMap.get(var7);
               this.addChildren(var3, var8);
            } else {
               this.addChildren(var3, var6);
            }
         }
      }

      String var9 = var2.getName();
      List var10 = this.attlistMap.get(var9);
      if (var10 != null) {
         for (Object __etlItem881 : var10) {
            CROM var12 = (CROM) __etlItem881;
            var4 = new CROM(var1, var12, var4);
         }
      }
   }

   public static void main(String[] var0) {
      String var1 = "C:/Users/Frank/Downloads/";
      String var2 = "FXRateRequest";
      DtdParser var3 = new DtdParser(var1 + "invoice.dtd");
      var3.init();
      CROM var4 = var3.getStructureTree(var2);

      try {
         FileWriter var5 = new FileWriter(new File(var1 + "debug.jtr"));
         writeXml(var5, var4);
      } catch (IOException var6) {
         var6.printStackTrace();
      }
   }

   enum EntityType {
      general_entity,
      system_entity,
      public_enetity;
   }
}
