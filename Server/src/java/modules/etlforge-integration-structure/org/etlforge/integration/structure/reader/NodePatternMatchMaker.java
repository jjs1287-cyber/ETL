package org.etlforge.integration.structure.reader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;

public class NodePatternMatchMaker {
   public static final int MAX_MESSAGE_LENGTH = 2000;
   CROM parent0;
   boolean parentIsAllGroup = false;
   Map<String, List<CROM>> nameToCromList_map;
   Map<CROM, Integer> cromToIndex_map;
   CROM[] cromChildSequence;
   JbNode jbParent;
   DocReader reader;
   JbNode[] jbChildArray;
   String[] nameArray;
   CROM[] cromArray;
   int nSubElements;
   int nChildren;
   CROM last;
   int indexOfChild;
   public static final boolean bdebug = false;
   private CROM cromXsiAny;
   Map<String, CROM> nameToSubstitutionGroupHead_map = null;
   CROM_Tree crom_tree;
   private boolean hasOnlyValueNode;
   private boolean isArray;
   private boolean isJson;

   public NodePatternMatchMaker(CROM var1, CROM_Tree var2) {
      if (!var1.isLeaf()) {
         this.crom_tree = var2;
         this.parentIsAllGroup = var1.isAllGroup();
         this.parent0 = var1;
         if (!var1.hasChildren() || var1.getChildCount() == 1 && var1.getChild(0).getName().equals("xsi:nil")) {
            var1.expandNode(this.crom_tree);
         }

         this.isArray = var1.isArray();
         this.cromXsiAny = this.parent0.getChildAnyCrom();
         this.nameToCromList_map = Maps.newHashMap();
         this.cromToIndex_map = Maps.newHashMap();
         ArrayList var3 = Lists.newArrayList();
         this.indexOfChild = 0;
         this.initMap(var1, var3);
         this.nChildren = var3.size();
         this.hasOnlyValueNode = this.nChildren == 1 && ((CROM)var3.get(0)).isValueLeaf();
         this.cromChildSequence = (CROM[]) var3.toArray(new CROM[this.nChildren]);

         for (CROM var5 : var1.getChildren()) {
            if (var5.isSubstitutionGroupHead()) {
               for (String var8 : var5.getSubstitutionGroup()) {
                  var8 = CROM.getLocalName(var8);
                  this.addToSubstitutionMap(var8, var5);
               }
            }
         }

         for (CROM var10 : var1.getChildren()) {
            if (var10.isSubstitutionGroupHead() && var10.hasChildren()) {
               CROM var11 = var10.getChild(0);
               assert var11.isSubstitutionGroup();
               this.addToSubstitutionMap(var11.getName(), var10);
            }
         }
      }
   }

   private void addToSubstitutionMap(String var1, CROM var2) {
      if (this.nameToSubstitutionGroupHead_map == null) {
         this.nameToSubstitutionGroupHead_map = new HashMap<>();
      }

      this.nameToSubstitutionGroupHead_map.put(var1, var2);
   }

   private void initMap(CROM var1, List<CROM> var2) {
      if (!var1.hasChildren()) {
         var1.expandNode(this.crom_tree);
      }

      for (CROM var4 : var1.getChildren()) {
         if (var4.isNumericName()) {
            this.parentIsAllGroup = var4.isAllGroup();
            this.initMap(var4, var2);
         } else if (var4.isElementNode()) {
            String var5 = var4.getDisplayName();
            String var6 = var4.getName();
            if (this.parentIsAllGroup) {
               List var7 = this.nameToCromList_map.get(var5);
               if (var7 == null) {
                  ArrayList var8 = Lists.newArrayList(var4);
                  this.nameToCromList_map.put(var5, var8);
               }
            } else if (var6.indexOf(35) < 0) {
               this.addToMap(var5, var4, var2);
            }
         } else if (var4.isValueLeaf()) {
            this.addToMap("", var4, var2);
         }
      }
   }

   private void addToMap(String var1, CROM var2, List<CROM> var3) {
      var3.add(var2);
      this.cromToIndex_map.put(var2, this.indexOfChild);
      this.indexOfChild++;
      List var4 = this.nameToCromList_map.get(var1);
      if (var4 == null) {
         var4 = Lists.newArrayList();
         this.nameToCromList_map.put(var1, var4);
      }

      var4.add(var2);
   }

   private CROM getSubstitutionGroupHead(String var1) {
      return this.nameToSubstitutionGroupHead_map == null ? null : this.nameToSubstitutionGroupHead_map.get(var1);
   }

   public void useForJson() {
      this.isJson = true;
   }

   public CROM[] findCroms(List<JbNode> var1, DocReader var2, JbNode var3, StringBuilder var4) throws Exception {
      this.jbParent = var3;
      this.reader = var2;
      if (var1.size() == 0 && this.hasOnlyValueNode) {
         new JbNode(var2, var3, "", var3.getEndPos(), var3.getEndPos());
      }

      if (!this.isArray) {
         ArrayList var18 = new ArrayList();
         ArrayList var20 = new ArrayList();
         boolean var22 = false;

         for (JbNode var10 : var1) {
            String var11 = var10.getName();
            List var12 = this.nameToCromList_map.get(var11);
            int var13 = var12 != null ? var12.size() : 0;
            if (var13 > 0) {
               var18.add(var11);
               var20.add(var10);
            } else if (this.cromXsiAny != null) {
               var18.add(var11);
               var20.add(var10);
            } else {
               CROM var23;
               if ((var23 = this.getSubstitutionGroupHead(var11)) != null) {
                  var11 = var23.getName();
                  int var14 = var11.indexOf(35);
                  if (var14 >= 0) {
                     var11 = var11.substring(0, var14);
                  }

                  var18.add(var11);
                  var20.add(var10);
               } else if (var4.length() < 2000) {
                  if (!var22) {
                     var22 = true;
                     var4.append("at " + var2.getLocation(var3).toString())
                        .append("\nThe node \"")
                        .append(this.parent0.getXmlPath())
                        .append("\" has undefined value or subelement(s):");
                  } else {
                     var4.append(",");
                  }

                  if (var11.isEmpty()) {
                     String var27 = var10.getStr(var2).trim();
                     var4.append(" (value=\"").append(var27).append("\" file_pos=").append(var10.m_pos).append(")");
                  } else {
                     var4.append(" " + var11);
                  }

                  if (var4.length() > 2000) {
                     var4.append("...");
                  }
               }
            }
         }

         if (var22 && var4.length() < 2000) {
            var4.append('\n');
         }

         this.nSubElements = var18.size();
         this.nameArray = (String[]) var18.toArray(new String[this.nSubElements]);
         this.jbChildArray = (JbNode[]) var20.toArray(new JbNode[this.nSubElements]);
         this.cromArray = new CROM[this.nSubElements];

         try {
            this.findCroms();
            return this.cromArray;
         } catch (ArrayIndexOutOfBoundsException var15) {
            var15.printStackTrace();
            throw new Exception(var15.getMessage());
         } catch (NullPointerException var16) {
            var16.printStackTrace();
            String var25 = var2.getLocation(var3);
            throw new Exception("at " + var25 + "\n" + var16.getMessage());
         } catch (Exception var17) {
            String var24 = var2.getLocation(var3);
            throw new Exception("at " + var24 + "\n" + var17.getMessage());
         }
      } else {
         this.cromArray = new CROM[var1.size()];
         this.jbChildArray = new JbNode[var1.size()];
         CROM var5 = null;

         for (CROM var7 : this.parent0.getChildren()) {
            if (!var7.isLeaf()) {
               var5 = var7;
               break;
            }
         }

         int var19 = 0;

         for (JbNode var8 : var1) {
            this.jbChildArray[var19] = var8;
            this.cromArray[var19++] = var5;
         }

         return this.cromArray;
      }
   }

   public JbNode[] getJbChildArray() {
      return this.jbChildArray;
   }

   private void findCroms() throws Exception {
      this.nSubElements = this.nameArray.length;
      if (this.parentIsAllGroup) {
         for (int var14 = 0; var14 < this.nSubElements; var14++) {
            String var15 = this.nameArray[var14];
            List var19 = this.nameToCromList_map.get(var15);
            if (this.parentIsAllGroup && var19 != null && var19.size() == 1) {
               this.cromArray[var14] = (CROM)var19.get(0);
            }
         }
      } else {
         ArrayList var1 = Lists.newArrayList();
         ArrayList var2 = Lists.newArrayList();

         for (int var3 = 0; var3 < this.nSubElements; var3++) {
            String var4 = this.nameArray[var3];
            List var5 = this.nameToCromList_map.get(var4);
            if (var5 == null || var5.size() == 0) {
               var5 = this.nameToCromList_map.get("Any");
            }

            assert var5 != null && var5.size() > 0;
            this.cromArray[var3] = (CROM)var5.get(0);
            if (var5.size() > 1) {
               var1.add(var3);
               var2.add(var5.toArray(new CROM[var5.size()]));
            }
         }

         if (var1.size() == 0) {
            int var16 = this.validateSequence(-1, this.nSubElements);
            if (var16 >= 0) {
               if (this.nameArray.length != 0) {
                  if (var16 == 0 && this.cromArray.length > 0 && !this.validateStarting(this.cromArray[0])) {
                     String var23 = this.nameArray[0];
                     throw new Exception("Under node \"" + this.parent0.getXmlPath() + "\" the subelement \"" + var23 + "\" can't be a staring element.");
                  }

                  if (var16 == this.nSubElements && this.nSubElements > 0) {
                     String var22 = this.nameArray[this.nSubElements - 1];
                     throw new Exception("Under node \"" + this.parent0.getXmlPath() + "\" the subelement \"" + var22 + "\" can't be at the end.");
                  }

                  StringBuilder var21 = new StringBuilder();
                  var21.append('[');
                  boolean var28 = false;

                  for (String var9 : this.nameArray) {
                     if (var28) {
                        var21.append(',');
                     } else {
                        var28 = true;
                     }

                     if (var9.isEmpty()) {
                        var21.append("(value)");
                     } else {
                        var21.append(var9);
                     }
                  }

                  var21.append(']');
                  throw new Exception(
                     "Under node \""
                        + this.parent0.getXmlPath()
                        + "\" subelements sequence is invalid between position "
                        + (var16 + 1)
                        + " and "
                        + (var16 + 2)
                        + ": "
                        + var21.toString()
                  );
               }

               String var20 = this.jbParent.getXmlAttributeValue(this.reader, "xsi:nil");
               boolean var27 = !var20.equals("1") && !var20.equalsIgnoreCase("true");
               if (var27) {
                  throw new Exception("The node \"" + this.parent0.getXmlPath() + "\" is missing mandatory value or subelements.");
               }
            }
         } else {
            int var17 = var1.size();
            int[] var24 = new int[var17];
            int[] var29 = new int[var17];
            int[] var31 = new int[this.nSubElements + 1];
            int var33 = 0;
            int var37 = 0;

            while (var33 <= var17) {
               int var40 = var33 < var17 ? (Integer)var1.get(var33) : this.nSubElements + 1;

               for (int var10 = var37; var10 < var40; var10++) {
                  var31[var10] = var33 - 1;
               }

               var33++;
               var37 = var40;
            }

            for (int var34 = 0; var34 < var17; var34++) {
               var24[var34] = 0;
               var29[var34] = ((CROM[])var2.get(var34)).length;
            }

            var33 = -1;
            boolean var38 = true;

            for (int var41 = 0; var41 <= this.nSubElements; var41++) {
               if (this.validateSequence(var33, var41) < 0) {
                  var33 = var41;
               } else {
                  var38 = false;
                  int var42 = var31[var41];

                  for (int var11 = var42; var11 >= 0; var11--) {
                     var33 = (Integer)var1.get(var11) - 1;
                     var24[var11]++;

                     while (var24[var11] < var29[var11]) {
                        int var12 = (Integer)var1.get(var11);
                        this.cromArray[var12] = ((CROM[])var2.get(var11))[var24[var11]];
                        int var13 = this.validateSequence(var33, var41);
                        if (var13 < 0) {
                           var13 = var41 + 1;
                        }

                        if (var13 > var33) {
                           var41 = var13 - 1;
                           var33 = var41;
                           var38 = true;
                           break;
                        }

                        var24[var11]++;
                     }

                     if (var38) {
                        break;
                     }

                     for (int var43 = var11; var43 <= var42; var43++) {
                        var24[var43] = 0;
                        int var44 = (Integer)var1.get(var43);
                        this.cromArray[var44] = ((CROM[])var2.get(var43))[0];
                     }

                     if (var11 <= var42) {
                     }
                  }

                  if (!var38) {
                     throw new Exception("pattern not found");
                  }
               }
            }

            if (!var38) {
               throw new Exception(
                  "Cannot find a valid subelement sequence for the parent \""
                     + this.parent0.getDisplayName()
                     + "\", the subelement names are "
                     + Arrays.toString(this.nameArray)
               );
            }
         }

         for (int var18 = 0; var18 < this.nSubElements; var18++) {
            CROM var25 = this.cromArray[var18];
            if (var25.isSubstitutionGroupHead()) {
               String var30 = this.jbChildArray[var18].getName();
               var25 = this.getSubstitutionGroupHead(var30);
               CROM var32 = var25.findSubNode(var30);
               if (var32 == null) {
                  CROM var36 = CROM.getSubstitutionGroupType(this.crom_tree, var30);
                  Stack var39 = new Stack();
                  var32 = new CROM(var25, var36, false, this.crom_tree, var39, "");
               }

               this.cromArray[var18] = var32;
            }
         }
      }
   }


   private int validateSequence(int var1, int var2) throws Exception {
      if (this.nSubElements == 0) {
         return !this.validateStarting(null) ? 0 : -1;
      }

      if (var1 == -1 && !this.validateStarting(this.cromArray[0])) {
         return 0;
      }

      int var3 = var1 > 0 ? var1 : 0;

      for (int var4 = var2 < this.nSubElements - 1 ? var2 : this.nSubElements - 1; var3 < var4; var3++) {
         if (!this.validate(this.cromArray[var3], this.cromArray[var3 + 1], var3)) {
            return var3;
         }
      }

      if (var2 == this.nSubElements && !this.validateEnding(this.cromArray[this.nSubElements - 1])) {
         return this.nSubElements;
      }

      if (var2 < this.nSubElements) {
      }

      return -1;
   }

   private int skipToLastSiblingPosition(CROM var1, CROM var2) {
      int var3 = this.cromToIndex_map.get(var2);
      if (var1.isOneOfNode()) {
         for (CROM var5 : var1.getChildren()) {
            if (var5 == var2) {
               return var3;
            }

            if (var5.isAncestorOf(var2)) {
               for (int var6 = var3 + 1; var6 < this.nChildren; var3 = var6++) {
                  CROM var7 = this.cromChildSequence[var6];
                  if (!var5.isAncestorOf(var7)) {
                     break;
                  }
               }

               return var3;
            }
         }
      }

      for (int var8 = var3 + 1; var8 < this.nChildren; var3 = var8++) {
         CROM var9 = this.cromChildSequence[var8];
         if (!var1.isAncestorOf(var9)) {
            break;
         }
      }

      return var3;
   }

   private CROM getOptionalParent(CROM var1, CROM var2, CROM var3) {
      CROM var4 = var2;

      for (CROM var5 = var4.getParent(); var5 == this.parent0 || var5.getDisplayName().isEmpty(); var5 = var4.getParent()) {
         if (var1 != null && var5.isAncestorOf(var1)) {
            if (var5.isOneOfNode()) {
               return var5;
            }

            return null;
         }

         if (var3 != null && var5.isAncestorOf(var3)) {
            if (var5.isOneOfNode()) {
               return var5;
            }

            return null;
         }

         if (var5.isOneOfNode()) {
            return var4;
         }

         if (var5.isOptionalNode() || var5.isCollectionNode()) {
            return var5;
         }

         var4 = var5;
      }

      return null;
   }

   private boolean validateStarting(CROM var1) throws Exception {
      if (var1 == null && this.nChildren == 0) {
         return true;
      }

      if (!this.isAllGroup(this.parent0) && !this.parent0.isSubstitutionGroupHead()) {
         for (int var2 = 0; var2 < this.nChildren; var2++) {
            CROM var3 = this.cromChildSequence[var2];
            if (var3 == var1) {
               return true;
            }

            if (!var3.isOptionalNode() && !var3.isCollectionNode()) {
               CROM var4 = this.getOptionalParent(null, var3, var1);
               if (var4 == null) {
                  return false;
               }

               var2 = this.skipToLastSiblingPosition(var4, var3);
            }
         }

         return var1 == null;
      } else {
         return true;
      }
   }

   private boolean validateEnding(CROM var1) throws Exception {
      if (!this.isAllGroup(this.parent0) && !this.parent0.isSubstitutionGroupHead()) {
         for (int var2 = this.cromToIndex_map.get(var1) + 1; var2 < this.nChildren; var2++) {
            CROM var3 = this.cromChildSequence[var2];
            if (!var3.isOptionalNode() && !var3.isOptionalValueElement() && !var3.isMultipleMapping() && !var3.isCollectionNode()) {
               if (this.nSubElements == 1 && var3.getName().equals("request")) {
                  return true;
               }

               CROM var4 = this.getOptionalParent(var1, var3, null);
               if (var4 == null) {
                  return false;
               }

               var2 = this.skipToLastSiblingPosition(var4, var3);
            }
         }

         return true;
      } else {
         return true;
      }
   }

   private boolean canStartsWith(CROM var1, CROM var2) {
      if (!var1.isLoopNode()) {
         return false;
      }

      boolean var3 = false;

      for (int var4 = 0; var4 < this.nChildren; var4++) {
         CROM var5 = this.cromChildSequence[var4];
         if (var1.isAncestorOf(var5)) {
            var3 = true;
            if (var5 == var2) {
               return true;
            }

            if (!var5.isOptionalNode() && !var5.isCollectionNode()) {
               CROM var6 = this.getOptionalParent(null, var5, var2);
               if (var6 == null) {
                  return false;
               }

               var4 = this.skipToLastSiblingPosition(var6, var5);
            }
         } else if (var3) {
            return false;
         }
      }

      return false;
   }

   private boolean canEndsWith(CROM var1, CROM var2) {
      if (!var1.isLoopNode()) {
         return false;
      }

      if (var1.isOneOfNode()) {
         for (CROM var9 : var1.getChildren()) {
            if (var9 == var2) {
               return true;
            }

            if (var9.isAncestorOf(var2)) {
               for (int var10 = this.cromToIndex_map.get(var2) + 1; var10 < this.nChildren; var10++) {
                  CROM var6 = this.cromChildSequence[var10];
                  if (var9.isAncestorOf(var6)) {
                     if (!var6.isOptionalNode() && !var6.isCollectionNode()) {
                        CROM var7 = this.getOptionalParent(var2, var6, null);
                        if (var7 == null || var7 == var1 || var7.isAncestorOf(var1)) {
                           return false;
                        }

                        var10 = this.skipToLastSiblingPosition(var7, var6);
                     }
                     continue;
                  }
                  break;
               }

               return true;
            }
         }

         return true;
      } else {
         for (int var3 = this.cromToIndex_map.get(var2) + 1; var3 < this.nChildren; var3++) {
            CROM var4 = this.cromChildSequence[var3];
            if (var1.isAncestorOf(var4)) {
               if (!var4.isOptionalNode() && !var4.isCollectionNode()) {
                  CROM var5 = this.getOptionalParent(var2, var4, null);
                  if (var5 == null || var5 == var1 || var5.isAncestorOf(var1)) {
                     return false;
                  }

                  var3 = this.skipToLastSiblingPosition(var5, var4);
               }
               continue;
            }
            break;
         }

         return true;
      }
   }

   private boolean validate(CROM var1, CROM var2, int var3) throws Exception {
      if (!this.isAllGroup(this.parent0) && !this.parent0.isSubstitutionGroupHead()) {
         CROM var4 = var1.getParent();
         CROM var5 = var2.getParent();
         if (var4 != null && var4 == var5 && var4.isAllGroupConverted()) {
            return true;
         }

         if (var2 == var1 && var1.isLoopNode()) {
            return true;
         }

         CROM var6 = var1.getParent();
         if (var6.isOneOfNode() && var6.isLoopNode() && var2.getParent() == var6) {
            return true;
         }

         int var7 = this.cromToIndex_map.get(var1);
         int var8 = this.cromToIndex_map.get(var2);
         boolean var9 = var7 < var8;
         if (var9) {
            for (int var13 = var7 + 1; var13 < var8; var13++) {
               CROM var11 = this.cromChildSequence[var13];
               if (!var11.isOptionalNode() && !var11.isOptionalValueElement() && !var11.isCollectionNode()) {
                  CROM var12 = this.getOptionalParent(var1, var11, var2);
                  if (var12 == null) {
                     return false;
                  }

                  var13 = this.skipToLastSiblingPosition(var12, var11);
               }
            }

            return true;
         } else {
            for (CROM var10 = var1.getParent(); var10.getDisplayName().isEmpty(); var10 = var10.getParent()) {
               if (this.canEndsWith(var10, var1) && this.canStartsWith(var10, var2)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return true;
      }
   }

   private boolean isAllGroup(CROM var1) {
      return var1.isAllGroupConverted() || this.representsJsonObject(var1);
   }

   private boolean representsJsonObject(CROM var1) {
      return this.isJson && var1.findAttribute("object") != null;
   }

   public void resetData() {
      this.jbChildArray = null;
      this.cromArray = null;
   }

   public String getNameToCromListString() {
      StringBuilder var1 = new StringBuilder();

      for (String var3 : this.nameToCromList_map.keySet()) {
         var1.append(var3).append("\t");
         List var4 = this.nameToCromList_map.get(var3);
         int var5 = 0;

         for (Object __etlItem730 : var4) {
            CROM var7 = (CROM) __etlItem730;
            var1.append(" ").append(var7.toString()).append(var5++);
         }

         var1.append('\n');
      }

      return var1.toString();
   }

   public String getArgumentList() {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;
      int var3 = 0;

      for (String var7 : this.nameArray) {
         var1.append(var2 + " <").append(var7).append('>').append("\t-");
         List var8 = this.nameToCromList_map.get(var7);
         int var9 = 0;

         for (Object __etlItem750 : var8) {
            CROM var11 = (CROM) __etlItem750;
            var1.append("\t").append(var2 + var11.toString()).append(var9++);
         }

         if (var8.size() > 1) {
            var1.append("\tj=" + var3++);
         }

         var1.append('\n');
         var2++;
      }

      return var1.toString();
   }
}
