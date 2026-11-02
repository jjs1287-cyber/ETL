package org.etlforge.integration.structure.reader;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import org.apache.axis.utils.StringUtils;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.crom.LinkIterator;

public class XmlReader extends DocReader {
   public static final short START_DOCUMENT = 1;
   public static final short XML_HEADER = 2;
   public static final short XML_DOCTYPE = 3;
   public static final short START_ELEMENT = 4;
   public static final short END_ELEMENT = 5;
   public static final short CHARACTERS = 6;
   public static final short IGNORABLE_CHARACTERS = 7;
   public static final short END_DOCUMENT = 8;
   public static final short COMMENTS = 9;
   private JbNode xml_header;
   private JbNode doc_type;
   private final CROM dtdRoot;
   private final Map<CROM, NodePatternMatchMaker> mmm;
   private boolean isJson;

   public XmlReader(File var1, CROM var2) {
      super(var1, false, true, null);
      this.dtdRoot = var2;
      var2.defineCromMatchingScheme();
      this.mmm = Maps.newHashMap();
      this.parser = new XmlParser(this.fc);
   }

   public static String findValueByName(String var0, String var1) {
      int var2 = 0;
      int var3 = var1.length();

      while ((var2 = var0.indexOf(var1, var2)) >= 0) {
         if (var2 == 0 || Character.isWhitespace(var0.charAt(var2 - 1))) {
            var2 += var3;
            if (var0.charAt(var2) == '=') {
               char var4 = var0.charAt(++var2);
               if (var4 == '"' || var4 == '\'') {
                  int var5 = var0.indexOf(var4, var2 + 1);
                  if (var5 > var2 + 1) {
                     return var0.substring(var2 + 1, var5);
                  }
               }
            }
         }
      }

      return null;
   }

   public JbNode getDocType() {
      return this.doc_type;
   }

   public void useForJson() {
      this.isJson = true;
   }

   @Override
   public JbNode parse_all(int var1, int var2) {
      XmlParser var3 = (XmlParser)this.parser;
      var3.init();
      this.root = null;
      JbNode var4 = null;
      JbNode var5 = null;

      while (var3.hasMoreEvent()) {
         switch (var3.getNextEvent(var4)) {
            case 2:
               this.xml_header = new JbNode(this, null, var3.getIdentifier(), var3.getBeginRecordPos(), var3.getEndRecordPos());

               try {
                  String var6 = this.xml_header.getStr(this);
                  String var7 = findValueByName(var6, "encoding");
                  Charset var8 = StringUtils.isEmpty(var7) ? Charsets.UTF_8 : Charset.forName(var7);
                  this.fc.setEncoding(var8);
               } catch (IOException var9) {
                  var9.printStackTrace();
               }
               break;
            case 3:
               this.doc_type = new JbNode(this, null, var3.getIdentifier(), var3.getBeginRecordPos(), var3.getEndRecordPos());
               break;
            case 4:
               var4 = new JbNode(this, var4, var3.getIdentifier(), var3.getBeginRecordPos(), var3.getEndRecordPos());
               if (this.root == null) {
                  this.root = var4;
               }
               break;
            case 5:
               if (var4 == null) {
                  throw new RuntimeException("END_ELEMENT should not occured when node not created.");
               }

               var4 = var4.getParent();
               var5 = null;
               break;
            case 6:
               if (var5 == null) {
                  var5 = new JbNode(this, var4, "", var3.getBeginRecordPos(), var3.getEndRecordPos());
               } else {
                  var5.m_length = (int)(var3.getEndRecordPos() - var5.getPos());
               }
         }
      }

      return this.root;
   }

   @Override
   public JbNode assignToStructure(CROM_Tree var1, LinkIterator var2, StringBuilder var3) throws Exception {
      if (this.root == null) {
         var3.append("The root element of the data tree is null. Perhaps the file is empty or invalid.");
      } else {
         this.root.setCrom(this.dtdRoot);
         this.convertSubElementsToXmlDataNode(this.root, var1, var3);
      }

      return this.root;
   }

   NodePatternMatchMaker getNodePatternMatchMaker(CROM var1, CROM_Tree var2) {
      NodePatternMatchMaker var3 = this.mmm.get(var1);
      if (var3 == null) {
         var3 = new NodePatternMatchMaker(var1, var2);
         if (this.isJson) {
            var3.useForJson();
         }

         this.mmm.put(var1, var3);
      }

      return var3;
   }

                                                   
                                                                                                                                                                                     
   public void convertSubElementsToXmlDataNode(JbNode var1, CROM_Tree var2, StringBuilder var3) {
      NodePatternMatchMaker var4 = null;

      try {
         int var5 = var1.m_children.size();
         CROM var6 = var1.getCrom();
         if (var6 == null) {
            throw new Exception("convert child nodes to xml subelements failed. Cannot find the structure node for the node.");
         }

         if (!var6.isXmlAnyType()) {
            var4 = this.getNodePatternMatchMaker(var6, var2);
            CROM[] var28 = var4.findCroms(var1.m_children, this, var1, var3);
            JbNode[] var29 = var4.getJbChildArray();
            var5 = var29.length;
            var1.m_children.clear();

            for (int var30 = 0; var30 < var5; var30++) {
               JbNode var32 = var29[var30];
               if (var28[var30] != null) {
                  this.moveTo(var32, var28[var30], var30, var2);
               }
            }

            for (int var31 = 0; var31 < var5; var31++) {
               JbNode var33 = var29[var31];
               if (var28[var31] != null) {
                  this.convertSubElementsToXmlDataNode(var33, var2, var3);
               }
            }

            return;
         }

         assert var6.getChildCount() == 1;
         List var7 = var1.getChildren();
         if (var7 != null) {
            CROM var8 = var6.getChild(0);
            var7.clear();
            String var9 = var1.getStr(this);
            assert var9.length() > 2 && var9.charAt(0) == '<';
            {
               int var10 = 1;

               for (int var11 = var9.length(); var10 < var11; var10++) {
                  char var12 = var9.charAt(var10);
                  if (var12 == '/') {
                     return;
                  }

                  if (var12 == '>' || Character.isWhitespace(var12)) {
                     String var13 = "</" + var9.substring(1, var10) + ">";
                     long var14 = var1.getEndPos();
                     long var16 = this.fc.indexOf(var13, var14);
                     var7.add(new JbNode(var1, var8, var14, var16));
                  }
               }

               return;
            }

         }
      } catch (NullPointerException var23) {
         var23.printStackTrace();
         return;
      } catch (ArrayIndexOutOfBoundsException var24) {
         var24.printStackTrace();
         var3.append("ArrayIndexOutOfBoundsException " + var24.getMessage()).append('\n');
         if (var3.length() > 2000) {
            var3.append("...");
         }

         return;
      } catch (Exception var25) {
         if (var3.length() < 2000) {
            var3.append(var25.getMessage()).append('\n');
            if (var3.length() > 2000) {
               var3.append("...");
               return;
            }
         }

         return;
      } finally {
         if (var4 != null) {
            var4.resetData();
         }
      }
   }

   private long findEndOfElementPosition(JbNode var1) {
      XmlParser var2 = (XmlParser)this.parser;
      assert var2 != null;
      var2.init(var1.m_pos);
      JbNode var3 = this.root;
      this.root = null;
      JbNode var4 = null;

      while (var2.hasMoreEvent()) {
         switch (var2.getNextEvent(var4)) {
            case 4:
               var4 = new JbNode(this, var4, var2.getIdentifier(), var2.getBeginRecordPos(), var2.getEndRecordPos());
               if (this.root == null) {
                  this.root = var4;
               }
               break;
            case 5:
               if (var4 == null) {
                  throw new RuntimeException();
               }

               if (var4 == this.root) {
                  this.root = var3;
                  return var2.getTokenEndPosition();
               }

               var4 = var4.getParent();
         }
      }

      throw new RuntimeException("findEndOfElementPosition() failed");
   }

   void moveTo(JbNode var1, CROM var2, int var3, CROM_Tree var4) throws Exception {
      if (var2.isAnyElement()) {
         var1.m_children.clear();
         long var5 = var1.m_pos;
         if (!var1.getName().isEmpty()) {
            long var7 = this.findEndOfElementPosition(var1);
            var1.setLength((int)(var7 - var5));
         }
      }

      if (var2.isDerivedTypeHolder()) {
         String var15 = var1.getXmlAttributeValue(this, "xsi:type");
         if (var15 == null || var15.isEmpty()) {
            var15 = var2.getSchemaType();
         }

         if (!var15.isEmpty()) {
            var2 = this.findDerivedType(var2, var4, var15);
         }
      }

      CROM var16 = var1.m_parent.getCrom();
      if (var16 == null) {
         throw new RuntimeException(
            "failed to move, the parent CROM is not yet defined, the parent is " + var1.m_parent.getName() + ", the target crom is " + var2
         );
      }

      Stack var6 = new Stack();
      assert var16.isAncestorOf(var2);

      for (CROM var17 = var2.getParent(); var17 != null && var17 != var16; var17 = var17.getParent()) {
         var6.push(var17);
      }

      JbNode var8 = var1.m_parent;

      while (var6.size() > 0) {
         CROM var10 = (CROM)var6.pop();
         JbNode var11 = var8.getChild(var10);
         if (var11 == null) {
            var11 = new JbNode(var8, var10, 0L, 0L);
         }

         if (var10.isLoopNode()) {
            int var13 = var11.m_children.size();
            JbNode var12;
            if (var13 != 0 && !var10.isSubstitutionGroupHead() && !var10.isDerivedTypeHolder()) {
               var12 = var11.m_children.get(var13 - 1);
               CROM var14 = var6.size() > 0 ? (CROM)var6.peek() : var2;
               if (var10.isOneOfNode()) {
                  if (var14 != null && var12.getChild(var14) == null) {
                     var12 = new JbNode(var11, 0L, 0L);
                  }
               } else if (var14 != null && outOfOrder(var12, var14)) {
                  var12 = new JbNode(var11, 0L, 0L);
               }
            } else {
               var12 = new JbNode(var11, 0L, 0L);
            }

            var8 = var12;
         } else {
            var8 = var11;
         }
      }

      if (var2.isLoopNode()) {
         JbNode var18 = var8.getChild(var2);
         if (var18 == null) {
            var1.m_parent = new JbNode(var8, var2, 0L, 0L);
         } else {
            var1.m_parent = var18;
         }

         var1.m_id = var1.m_parent.m_children.size();
         var1.m_parent.m_children.add(var1);
      } else {
         if (!this.isAllGroup(var16)) {
            var8 = this.getValidParent(var8, var2);
         }

         var1.m_parent = var8;
         var1.m_id = var2;
         var1.m_parent.m_children.add(var1);
      }
   }

   private boolean isAllGroup(CROM var1) {
      return var1.isAllGroupConverted() || this.representsJsonObject(var1);
   }

   private boolean representsJsonObject(CROM var1) {
      return this.isJson && var1.findAttribute("object") != null;
   }

   private CROM findDerivedType(CROM var1, CROM_Tree var2, String var3) throws RuntimeException {
      int var4 = var3.indexOf(58);
      if (var4 > 0) {
         var3 = var3.substring(var4 + 1);
      }

      CROM var5 = var1.findSubNode(var3);
      if (var5 == null) {
         for (Entry var7 : var2.getElementTypeMap().entrySet()) {
            String var8 = ((CROM)var7.getValue()).getName();
            if (var8.equals(var3)) {
               CROM var9 = (CROM)var7.getValue();
               Stack var10 = new Stack();
               var5 = new CROM(var1, var9, true, var2, var10, "");
               break;
            }
         }
      }

      if (var5 == null) {
         throw new RuntimeException("Could not find type defintion for the type " + var3);
      } else {
         return var5;
      }
   }

   JbNode getValidParent(JbNode var1, CROM var2) {
      if (var2.isValueLeaf()) {
         return var1;
      }

      List var3 = var1.getChildren();
      if (var3 != null && !var3.isEmpty()) {
         CROM var4 = var1.getCrom();
         if (var4.isAllGroupConverted() && var4 == var2.getParent()) {
            return var1;
         }

         JbNode var5 = (JbNode)var3.get(var3.size() - 1);
         CROM var6 = var5.getCrom();
         if (var6 != var2) {
            for (CROM var8 : var2.getParent().getChildren()) {
               if (var8 == var2) {
                  break;
               }

               if (var8 == var6) {
                  return var1;
               }
            }
         }

         Stack var9 = new Stack();
         CROM var11 = var2.getParent();

         JbNode var13;
         for (var13 = var1; !var11.isLoopNode(); var13 = var13.getParent()) {
            var9.push(var11);
            var11 = var11.getParent();
         }

         assert var13 != null;
         if (var13.isInstance()) {
            JbNode var10 = var13.getParent();
            var13 = new JbNode(var10, 0L, 0L);

            while (var9.size() > 0) {
               var11 = (CROM)var9.pop();
               var13 = new JbNode(var13, var11, 0L, 0L);
            }
         } else {
            System.out.println("?");
         }

         System.out.println("\treturn jbParent0=" + var1);
         return var13;
      } else {
         return var1;
      }
   }

   private static boolean outOfOrder(JbNode var0, CROM var1) {
      List var2 = var0.getChildren();
      if (var2 != null && !var2.isEmpty()) {
         JbNode var3 = (JbNode)var2.get(var2.size() - 1);
         assert !var3.isInstance();
         CROM var4 = var3.getCrom();
         if (var4 == var1) {
            return false;
         }

         for (CROM var6 : var1.getParent().getChildren()) {
            if (var6 == var1) {
               return true;
            }

            if (var6 == var4) {
               return false;
            }
         }
      }

      return false;
   }

   String getAttributeString(JbNode var1) throws Exception {
      return this.getStr(var1.m_pos, var1.m_length);
   }

   public static String decode(String var0) {
      if (var0.indexOf(38) < 0) {
         return var0;
      }

      StringBuilder var1 = new StringBuilder();
      int var2 = 0;
      int var3 = var0.length();
      int var9 = 0;

      while (var9 < var3) {
         var9 = var0.indexOf(38, var9);
         if (var9 < 0) {
            break;
         }

         String var5 = var0.substring(var9);
         if (var5.startsWith("&lt;")) {
            if (var9 > var2) {
               var1.append(var0.substring(var2, var9));
            }

            var1.append('<');
            var9 += 4;
            var2 = var9;
         } else if (var5.startsWith("&gt;")) {
            if (var9 > var2) {
               var1.append(var0.substring(var2, var9));
            }

            var1.append('>');
            var9 += 4;
            var2 = var9;
         } else if (var5.startsWith("&amp;")) {
            if (var9 > var2) {
               var1.append(var0.substring(var2, var9));
            }

            var1.append('&');
            var9 += 5;
            var2 = var9;
         } else if (var5.startsWith("&quot;")) {
            if (var9 > var2) {
               var1.append(var0.substring(var2, var9));
            }

            var1.append('"');
            var9 += 6;
            var2 = var9;
         } else if (var5.startsWith("&quot;")) {
            if (var9 > var2) {
               var1.append(var0.substring(var2, var9));
            }

            var1.append('\'');
            var9 += 6;
            var2 = var9;
         } else {
            if (var0.charAt(var9 + 1) == '#' && var9 + 4 < var3) {
               char var6 = var0.charAt(var9 + 2);
               char var7 = var0.charAt(var9 + 3);
               char var8 = var0.charAt(var9 + 4);
               if (Character.isDigit(var6) && Character.isDigit(var7)) {
                  if (var8 == ';') {
                     if (var9 > var2) {
                        var1.append(var0.substring(var2, var9));
                     }

                     var1.append((char)((var6 - '0') * 10 + (var7 - '0')));
                     var9 += 5;
                     var2 = var9;
                     continue;
                  }

                  if (Character.isDigit(var8) && var9 + 5 < var3 && var0.charAt(var9 + 5) == ';') {
                     if (var9 > var2) {
                        var1.append(var0.substring(var2, var9));
                     }

                     var1.append((char)((var6 - '0') * 100 + (var7 - '0') * 10 + (var8 - '0')));
                     var9 += 6;
                     var2 = var9;
                     continue;
                  }
               }
            }

            var9++;
         }
      }

      if (var2 == 0) {
         return var0;
      }

      if (var2 < var3) {
         var1.append(var0.substring(var2));
      }

      return var1.toString();
   }
}

