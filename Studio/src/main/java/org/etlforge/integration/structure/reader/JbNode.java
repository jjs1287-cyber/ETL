package org.etlforge.integration.structure.reader;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.util.csv.FileContents;

public class JbNode {
   Object m_id;
   JbNode m_parent;
   public JbNode m_source;
   long m_pos;
   int m_length;
   public List<JbNode> m_children = Lists.newArrayList();

   public JbNode() {
   }

   public JbNode(DocReader var1, JbNode var2, String var3, long var4, long var6) {
      this.m_id = var1.getString(var3);
      this.m_parent = var2;
      this.m_pos = var4;
      this.m_length = (int)(var6 - var4);
      if (this.m_parent != null) {
         this.m_parent.m_children.add(this);
      }
   }

   public JbNode(JbNode var1, CROM var2, long var3, long var5) {
      this.m_id = var2;
      this.m_parent = var1;
      this.m_pos = var3;
      this.m_length = (int)(var5 - var3);
      if (this.m_parent != null) {
         this.m_parent.m_children.add(this);
      }
   }

   public JbNode(JbNode var1, long var2, long var4) {
      assert var1 != null : "This parent is the instance holder for a loop node, can't be null.";
      this.m_id = var1.m_children.size();
      this.m_parent = var1;
      this.m_pos = var2;
      this.m_length = (int)(var4 - var2);
      this.m_parent.m_children.add(this);
   }

   public void convertToDtdNode(JbNode var1, CROM var2) {
      this.m_id = var2;
      this.m_parent = var1;
      if (this.m_parent != null) {
         this.m_parent.m_children.add(this);
      }
   }

   public void convertToInstanceNode(JbNode var1) {
      this.m_id = var1.m_children.size();
      this.m_parent = var1;
      this.m_parent.m_children.add(this);
   }

   public JbNode getChild(CROM var1) {
      for (JbNode var3 : this.m_children) {
         CROM var4 = var3.getCrom();
         if (var4 == null) {
            return null;
         }

         if (var4 == var1) {
            return var3;
         }
      }

      return null;
   }

   public JbNode getChild(String var1) {
      for (JbNode var3 : this.m_children) {
         CROM var4 = var3.getCrom();
         if (var4 != null && var4.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public List<JbNode> getChildren() {
      return this.m_children;
   }

   public long getPos() {
      return this.m_pos;
   }

   public int getLength() {
      return this.m_length;
   }

   public JbNode getParent() {
      return this.m_parent;
   }

   public boolean isCrom() {
      return this.m_id instanceof CROM;
   }

   public boolean isInstance() {
      return this.m_id instanceof Integer && this.m_parent != null;
   }

   public int getInstanceNumber() {
      return (Integer)this.m_id;
   }

   public CROM getCrom() {
      if (this.m_id instanceof CROM) {
         return (CROM)this.m_id;
      } else {
         return this.m_id instanceof Integer && this.m_parent != null ? (CROM)this.m_parent.m_id : null;
      }
   }

   public String getName() {
      if (this.m_id instanceof String) {
         return (String)this.m_id;
      } else if (this.m_id instanceof CROM) {
         return ((CROM)this.m_id).getDisplayName();
      } else if (this.m_id instanceof Integer) {
         return this.m_parent == null ? null : this.m_parent.getName();
      } else {
         return null;
      }
   }

   public boolean isValueNode() {
      if (this.m_id instanceof CROM) {
         return ((CROM)this.m_id).isValueLeaf();
      } else {
         return this.m_id instanceof String && this.m_parent != null ? ((String)this.m_id).isEmpty() : false;
      }
   }

   public String getText(DocReader var1) throws IOException {
      return null;
   }

   public String getStr(DocReader var1) throws IOException {
      return var1.fc.getStr(this.m_pos, this.m_length);
   }

   public JbNode getValueNode() {
      if (this.m_children != null && this.m_children.size() != 0) {
         for (JbNode var2 : this.m_children) {
            if (var2.isValueNode()) {
               return var2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public String getValueNodeValuet(XmlParser var1) throws IOException {
      if (this.m_children != null && this.m_children.size() != 0) {
         String var2 = null;

         for (JbNode var4 : this.m_children) {
            if (var4.isValueNode()) {
               if (var2 == null) {
                  var2 = var1.getStr(var4.m_pos, var4.m_length);
               } else {
                  var2 = var2 + var1.getStr(var4.m_pos, var4.m_length);
               }
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public void setLength(int var1) {
      this.m_length = var1;
   }

   public long getEndPos() {
      return this.m_pos + this.m_length;
   }

   public String getText(XmlReader var1) throws Exception {
      String var2 = "";
      if (this.isValueNode()) {
         var2 = var1.getStr(this.m_pos, this.m_length);
      } else {
         JbNode var3 = this.getValueNode();
         if (var3 != null) {
            var2 = var1.getStr(var3.m_pos, var3.m_length);
         }
      }

      if (KongaStringUtils.hasXmlEscape(var2)) {
         var2 = StringEscapeUtils.unescapeXml(var2);
      }

      return var2.trim();
   }

   public String getXmlPrefix(DocReader var1) throws IOException {
      FileContents var2 = var1.fc;

      for (int var4 = 0; var4 < this.m_length; var4++) {
         byte var3;
         if ((var3 = var2.at(this.m_pos + 1L + var4)) == 58) {
            return var2.getStr(this.m_pos + 1L, var4);
         }

         if (Character.isWhitespace(var3)) {
            break;
         }
      }

      return "";
   }

   public String getXmlNamespaceURI(DocReader var1) throws Exception {
      String var2 = this.getXmlPrefix(var1);
      return this.getXmlNamespaceFromPrefix(var1, var2);
   }

   private String getXmlNamespaceFromPrefix(DocReader var1, String var2) throws Exception {
      return var2 != null && !var2.isEmpty() ? this.getXmlNS(var1, "xmlns:" + var2) : this.getXmlNS(var1, "xmlns");
   }

   private String getXmlNS(DocReader var1, String var2) throws Exception {
      String var3 = this.getXmlAttributeValue(var1, var2);
      return var3 == null && this.m_parent != null ? this.m_parent.getXmlNS(var1, var2) : var3;
   }

   public String getXmlAttributeValue(DocReader var1, String var2) {
      String var3 = var1.getStr(this.m_pos, this.m_length);
      int var4 = var3.length();
      int var5 = var2.length();
      int var10 = 0;
      int var7 = 0;
      int var8 = 0;

      while ((var10 = var3.indexOf(var2, var10)) >= 0) {
         if (Character.isWhitespace(var3.charAt(var10 - 1))) {
            var10 += var5;
            if (var10 + 2 >= var4) {
               return "";
            }

            if (var3.charAt(var10) == '=') {
               var7 = var3.indexOf(34, var10);
               if (var7 >= 0) {
                  var8 = var3.indexOf(34, var7 + 1);
                  if (var8 > var7) {
                     String var9 = var3.substring(var7 + 1, var8);
                     if (KongaStringUtils.hasXmlEscape(var9)) {
                        var9 = StringEscapeUtils.unescapeXml(var9);
                     }

                     return var9;
                  }
               }
            }
         }

         var10++;
      }

      return null;
   }

   JbNode getParent(CROM var1, CROM var2) {
      CROM var3 = this.getCrom();
      assert var3 != null;
      CROM var4 = var1.getParent();
      Stack var5 = new Stack();

      while (var4 != var3) {
         var5.push(var4);
         var4 = var4.getParent();
      }

      JbNode var6 = this;

      while (var5.size() > 0) {
         CROM var7 = (CROM)var5.pop();
         JbNode var8 = var6.getChild(var7);
         if (var8 == null) {
            var8 = new JbNode(var6, var7, 0L, 0L);
         }

         if (var7.isLoopNode()) {
            int var9 = var8.m_children.size();
            if (var9 == 0 || var7 == var2) {
               var8 = new JbNode(var6, 0L, 0L);
            }
         }

         var6 = var8;
      }

      return var6;
   }

   public void setCrom(CROM var1) {
      this.m_id = var1;
   }

   void addToStringBuilder(DocParser var1, StringBuilder var2, String var3) throws Exception {
      CROM var4 = this.getCrom();
      if (var4 == null || !var4.isValueLeaf()) {
         var2.append(var3).append(this.toString(var1));
         var2.append('\n');

         for (JbNode var6 : this.m_children) {
            var6.addToStringBuilder(var1, var2, var3 + " ");
         }
      }
   }

   public String toString(DocParser var1) throws Exception {
      StringBuilder var2 = new StringBuilder();
      CROM var3 = null;
      boolean var4 = false;
      if (this.m_id instanceof Integer) {
         var3 = (CROM)this.m_parent.m_id;
         if (var3 != null) {
            if (var3.isOneOfNode()) {
               var2.append("OneOf");
            } else {
               var2.append(var3.getName());
            }
         }

         var2.append("[").append(this.m_id).append("] ");
      } else {
         if (!(this.m_id instanceof CROM)) {
            if (this.m_id instanceof String) {
               return var1.getStr(this.m_pos, this.m_length);
            }

            return "[UNRECOGNIZED] " + var1.getStr(this.m_pos, this.m_length);
         }

         var3 = (CROM)this.m_id;
         if (var3.isValueLeaf()) {
            var2.append("Value: ");
         } else {
            var4 = var3.isLoopNode();
            String var5 = var3.isOneOfNode() ? "OneOf" : var3.getName();
            if (var5.isEmpty()) {
               var5 = "_DbRoot";
            }

            String var6 = "";
            if (var3.isAtLeastOneNode()) {
               var6 = "+";
            } else if (var3.isCollectionNode()) {
               var6 = "*";
            } else if (var3.isOptionalNode()) {
               var6 = "?";
            }

            var2.append("[").append(var5).append(var6).append("] ");
         }
      }

      if (!var4) {
         List var9 = var1.splitValues(this, true);
         if (var9 != null && var9.size() > 0) {
            var2.append(var9.toString());
         }
      }

      return var2.toString();
   }

   public void print(PrintStream var1, DocParser var2) throws Exception {
      StringBuilder var3 = new StringBuilder();
      this.addToStringBuilder(var2, var3, "");
      var1.println(var3);
   }

   public JbNode findFirstInstance(CROM var1) {
      for (JbNode var3 : this.m_children) {
         if (var3.getCrom() == var1) {
            if (!var1.isLoopNode()) {
               return var3;
            }

            if (var3.m_children.size() > 0) {
               return var3.m_children.get(0);
            }
         }
      }

      return null;
   }

   public String getJbTreeString(StringBuilder var1, String var2) {
      boolean var3 = var1 == null;
      if (var1 == null) {
         var1 = new StringBuilder();
      }

      if (!var2.isEmpty()) {
         var1.append(var2);
      }

      var1.append(this.toString()).append("\n");
      if (this.m_children != null) {
         for (JbNode var5 : this.m_children) {
            var5.getJbTreeString(var1, var2 + "  ");
         }
      }

      return !var3 ? "" : var1.toString();
   }

   public void resetSource() {
      this.m_source = null;

      for (JbNode var2 : this.m_children) {
         var2.resetSource();
      }
   }

   public void doSearch(List<Object> var1, String var2, DocReader var3) throws IOException {
      if (var3 != null) {
         List var4 = var3.parser.splitValues(this, false);
         CROM var5 = this.getCrom();
         Iterator var6 = var5.getChildren().iterator();
         CROM var7 = CROM.getNextValueAttribute(var6);
         if (var4 != null && !var4.isEmpty()) {
            for (Object __etlItem449 : var4) {
               String var9 = (String) __etlItem449;
               if (var7 == null) {
                  break;
               }

               if (var9 != null && var9.toLowerCase().indexOf(var2) >= 0) {
                  var1.add(new JbNode.SearchDataNodeItem(this, var7));
               }

               var7 = CROM.getNextValueAttribute(var6);
            }
         }

         if (this.m_children != null) {
            for (JbNode var11 : this.m_children) {
               var11.doSearch(var1, var2, var3);
            }
         }
      }
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this.m_id instanceof Integer) {
         var1.append(this.getParent().getCrom()).append("[");
         var1.append("Int ");
         Stack var2 = new Stack();

         for (JbNode var3 = this; var3 != null; var3 = var3.m_parent) {
            if (var3.isInstance()) {
               var2.add(var3.getInstanceNumber());
            }
         }

         while (var2.size() > 0) {
            int var4 = (Integer)var2.pop();
            var1.append(var4).append(';');
         }

         var1.append("]");
      } else if (this.m_id instanceof CROM) {
         CROM var5 = (CROM)this.m_id;
         if (var5.isValueLeaf()) {
            var1.append("Value[").append(var5.getParent().getName()).append("]");
         } else {
            var1.append("DTD[").append(var5.getName()).append("]");
         }
      } else if (this.m_id instanceof String) {
         var1.append("Node[").append((String)this.m_id).append("]");
      } else {
         var1.append("UNRECOGNIZED");
      }

      return var1.toString();
   }

   public void changeId(DocReader var1, String var2) {
      this.m_id = var1.getString(var2);
   }

   public static class SearchDataNodeItem {
      CROM attribute;
      JbNode jbNode;

      public SearchDataNodeItem(JbNode var1, CROM var2) {
         this.jbNode = var1;
         this.attribute = var2;
      }

      public CROM getAttributeNode() {
         return this.attribute;
      }

      public JbNode getJbNode() {
         return this.jbNode;
      }

      @Override
      public String toString() {
         return this.jbNode.toString() + " " + this.attribute.getName();
      }
   }
}
