package org.etlforge.integration.server.engine.connector.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SortedXmlNode {
   final Node root;
   final SortedXmlNode.SortedNodeComparator sortedComparator = new SortedXmlNode.SortedNodeComparator(2);

   public SortedXmlNode(Node var1) {
      this.root = var1;
      this.sort();
   }

   public Node getRoot() {
      return this.root;
   }

   public void sort() {
      this.sortNode(0, this.root);
   }

   private void sortNode(int var1, Node var2) {
      NodeList var3 = var2.getChildNodes();

      for (int var4 = 0; var4 < var3.getLength(); var4++) {
         Node var5 = var3.item(var4);
         this.sortNode(var1 + 1, var5);
      }

      if (var3.getLength() > 1) {
         this.sortNodeImpl(var1, var2);
      }
   }

   private void removeAllChildren(Node var1) {
      while (var1.hasChildNodes()) {
         var1.removeChild(var1.getLastChild());
      }
   }

   private void sortNodeImpl(int var1, Node var2) {
      ArrayList var3 = Lists.newArrayList();
      ArrayList var4 = Lists.newArrayList();
      NodeList var5 = var2.getChildNodes();

      for (int var6 = 0; var6 < var5.getLength(); var6++) {
         Node var7 = var5.item(var6);
         if (var7.getNodeType() != 1) {
            var3.add(new SortedXmlNode.SortedNode(var1, var6, var7));
         } else {
            var4.add(new SortedXmlNode.SortedNode(var1, var6, var7));
         }
      }

      if (var4.size() > 1) {
         Collections.sort(var4, this.sortedComparator);
         int var12 = var5.getLength();
         this.removeAllChildren(var2);
         int var8 = 0;
         int var13 = 0;
         SortedXmlNode.SortedNode var9 = null;
         SortedXmlNode.SortedNode var10 = null;

         for (int var11 = 0; var11 < var12; var11++) {
            if (var9 == null && var13 < var3.size()) {
               var9 = (SortedXmlNode.SortedNode)var3.get(var13);
            }

            if (var10 == null && var8 < var4.size()) {
               var10 = (SortedXmlNode.SortedNode)var4.get(var8);
            }

            if (var9 != null && var9.index == var11) {
               var2.appendChild(var9.node);
               var13++;
               var9 = null;
            } else {
               assert var10 != null;
               var2.appendChild(var10.node);
               var8++;
               var10 = null;
            }
         }
      }
   }

   class SortedNode {
      final int level;
      final int index;
      final Node node;

      SortedNode(int var2, int var3, Node var4) {
         this.level = var2;
         this.index = var3;
         this.node = var4;
      }
   }

   class SortedNodeComparator implements Comparator<SortedXmlNode.SortedNode> {
      final int fixedLevel;

      SortedNodeComparator(int var2) {
         this.fixedLevel = var2;
      }

      public int compare(SortedXmlNode.SortedNode var1, SortedXmlNode.SortedNode var2) {
         if (var1.level > this.fixedLevel && var1.node.getNodeType() == var2.node.getNodeType()) {
            int var3 = this.compare(var1.node, var2.node);
            if (var3 != 0) {
               return var3;
            }
         }

         return var1.index - var2.index;
      }

      private int compare(Node var1, Node var2) {
         if (var1.getNodeType() != 1) {
            return var2.getNodeType() != 1 ? var1.getNodeValue().compareTo(var2.getNodeValue()) : -1;
         }

         if (var2.getNodeType() != 1) {
            return 1;
         }

         String var3 = var1.getNodeName();
         String var4 = var2.getNodeName();
         int var5 = var3.compareTo(var4);
         if (var5 != 0) {
            return var5;
         }

         if (var1.hasAttributes() || var2.hasAttributes()) {
            this.compareAttr(var1.getAttributes(), var2.getAttributes());
         }

         var5 = var1.getChildNodes().getLength() - var2.getChildNodes().getLength();
         if (var5 != 0) {
            return var5;
         }

         for (int var6 = 0; var6 < var1.getChildNodes().getLength(); var6++) {
            var5 = this.compare(var1.getChildNodes().item(var6), var2.getChildNodes().item(var6));
            if (var5 != 0) {
               return var5;
            }
         }

         return 0;
      }

      private int compareAttr(NamedNodeMap var1, NamedNodeMap var2) {
         if (var1 != null && var1.getLength() != 0) {
            if (var2 != null && var2.getLength() != 0) {
               int var3 = var1.getLength() - var2.getLength();
               if (var3 != 0) {
                  return var3;
               }

               TreeSet var4 = Sets.newTreeSet();

               for (int var5 = 0; var5 < var1.getLength(); var5++) {
                  Node var6 = var1.item(var5);
                  var4.add(var6.getNodeName());
               }

               for (int var10 = 0; var10 < var2.getLength(); var10++) {
                  Node var12 = var2.item(var10);
                  var4.add(var12.getNodeName());
               }

               for (Object __etlItem180 : var4) {
                  String var13 = (String) __etlItem180;
                  Node var7 = var1.getNamedItem(var13);
                  Node var8 = var2.getNamedItem(var13);
                  if (var7 == null) {
                     return -1;
                  }

                  if (var8 == null) {
                     return 1;
                  }

                  var3 = var7.getNodeValue().compareTo(var8.getNodeValue());
                  if (var3 != 0) {
                     return var3;
                  }
               }

               return 0;
            } else {
               return 1;
            }
         } else {
            return -1;
         }
      }
   }
}
