package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.mapping.PathSeparator;
import org.etlforge.integration.structure.reader.DocReader;
import org.etlforge.integration.structure.reader.JbNode;
import org.etlforge.lang.KongaStringUtils;

public class FillDataElementKit {
   DocReader reader;
   boolean isFlat;
   JbNode jbRoot;
   JbNode jbGenerator;
   CROM generator;
   String generator_path;
   Map<String, Object> vde_map = new HashMap<>();
   Map<JbNode, Map<String, String>> attr_map_map = new HashMap<>();

   public FillDataElementKit(DocReader var1, Node var2) {
      this.reader = var1;
      if (var1 != null) {
         this.jbRoot = var1.root;
         this.isFlat = var1.isFlat;
         this.jbGenerator = this.jbRoot;
         if (var2 != null) {
            this.jbGenerator = var2.m_jbNode;
            this.generator = var2.getCROM();
            this.generator_path = this.generator.getPath_(this.isFlat);
         }
      }
   }

   private FillDataElementKit.Status cmp(String var1) {
      if (this.generator_path != null) {
         if (this.generator_path.equals(var1)) {
            return FillDataElementKit.Status.is_generator;
         }

         if (this.generator_path.startsWith(var1)) {
            return FillDataElementKit.Status.is_parent;
         }

         if (var1.startsWith(this.generator_path)) {
            return FillDataElementKit.Status.is_child;
         }
      }

      return FillDataElementKit.Status.is_diff_branch;
   }

   @VisibleForTesting
   static Integer getInstanceNumber(String var0) {
      if (!Strings.isNullOrEmpty(var0) && !var0.equals("_")) {
         String var1 = var0.charAt(0) == '_' ? "-" + var0.substring(1) : var0;
         return Integer.valueOf(var1);
      } else {
         return null;
      }
   }

   private Object deepClone(Object var1) {
      if (var1 == null) {
         return null;
      }

      if (!(var1 instanceof List)) {
         return var1;
      }

      ArrayList var2 = new ArrayList();

      for (Object var4 : (List)var1) {
         var2.add(this.deepClone(var4));
      }

      return var2;
   }

   private Object replaceWithChildNode(Object var1, String var2) {
      if (var1 == null) {
         return var1;
      }

      if (var1 instanceof List) {
         List var8 = (List)var1;
         if (var8.size() == 0) {
            return new ArrayList();
         }

         int var9 = 0;

         for (int var10 = var8.size(); var9 < var10; var9++) {
            Object var12 = var8.get(var9);
            Object var7 = this.replaceWithChildNode(var12, var2);
            if (var7 != var12) {
               var8.set(var9, var7);
            }
         }

         return var8;
      } else if (var1 instanceof JbNode) {
         JbNode var3 = (JbNode)var1;
         StringTokenizer var4 = PathSeparator.tokenize(var2, true);
         String var5 = null;

         while (var4.hasMoreTokens()) {
            String var6 = var4.nextToken();
            if (PathSeparator.isSeparator(var6.charAt(0))) {
               var3 = var3.getChild(var5);
               var5 = null;
            } else {
               var5 = StringUtils.substringBefore(var6, "#");
            }
         }

         if (var3 != null) {
            CROM var11 = var3.getCrom();
            if (var11.isLoopNode()) {
               return var3.m_children;
            } else {
               throw new RuntimeException("replaceWithChildNode() failed, the final replacement child should be a loop node. subpath:" + var2);
            }
         } else {
            return new ArrayList();
         }
      } else {
         return null;
      }
   }

   private JbNode findParentInstance(String var1) {
      JbNode var2 = this.jbGenerator;
      if (var2 == null) {
         return null;
      }

      for (CROM var3 = var2.getCrom(); !var1.startsWith(var3.getPath_(this.isFlat)); var3 = var2.getCrom()) {
         var2 = var2.getParent();
         if (var2 == null) {
            break;
         }
      }

      return var2;
   }

   private JbNode findCromTypeJbChild(JbNode var1, String var2) {
      String var3 = var2.substring(var1.getCrom().getPath_(this.isFlat).length());

      for (String var5 : PathSeparator.split(var3)) {
         if (var1 == null) {
            break;
         }

         JbNode var6 = var1.getChild(var5);
         var1 = var6;
      }

      return var1;
   }

   private JbNode findJbNodeByPath(String var1, FillDataElementKit.FindType var2) {
      if (this.jbGenerator == null) {
         this.jbGenerator = this.jbRoot;
      }

      JbNode var3 = this.jbGenerator;
      if (!this.isFlat) {
         switch (this.cmp(var1)) {
            case is_generator:
               break;
            case is_parent:
               var3 = this.findParentInstance(var1);
               break;
            case is_child:
               var3 = this.findCromTypeJbChild(this.jbGenerator, var1);
               break;
            default:
               JbNode var4 = this.findParentInstance(var1);
               var3 = this.findCromTypeJbChild(var4, var1);
         }
      }

      if (var3 != null && var2 == FillDataElementKit.FindType.is_instance_holder && var3.isInstance()) {
         var3 = var3.getParent();
      }

      return var3;
   }

   private void addVectorDE(String var1, String var2, String var3) {
      if (var1 == null) {
         JbNode var4 = this.findJbNodeByPath(var2, FillDataElementKit.FindType.is_instance_holder);
         if (var4 != null) {
            assert var4.getCrom().isLoopNode() && !var4.isInstance();
            this.vde_map.put(var3, var4.m_children);
         }
      } else {
         Object var7 = this.vde_map.get(var1);
         Object var5 = this.deepClone(var7);
         if (var5 == null) {
            return;
         }

         assert var3.length() > var1.length();
         String var6 = var3.substring(var1.length());
         this.replaceWithChildNode(var5, var6);
         this.vde_map.put(var3, var5);
      }
   }

   private Map<String, String> getAttrMap(JbNode var1) throws IOException {
      Map var2 = this.attr_map_map.get(var1);
      if (var2 == null) {
         var2 = this.reader.getAttributeMap(var1);
         this.attr_map_map.put(var1, var2);
      }

      return var2;
   }

   private Object createVectorDE(Object var1, List<Integer> var2, int var3) {
      if (var1 == null) {
         return null;
      }

      int var4 = var2.size();

      for (int var5 = var3; var5 < var4; var5++) {
         if (var1 instanceof List) {
            List var6 = (List)var1;
            Integer var7 = (Integer)var2.get(var5);
            if (var7 == null) {
               ArrayList var8 = new ArrayList();

               for (Object var10 : var6) {
                  var8.add(this.createVectorDE(var10, var2, var5 + 1));
               }

               return var8;
            }

            if (var7 < 0) {
               var7 = var7 + var6.size() + 1;
            }

            if (var7 <= 0 || var7 > var6.size()) {
               return null;
            }

            var1 = var6.get(var7 - 1);
         } else {
            System.out.println("cast to List failed");
         }
      }

      return var1;
   }

   private Object replaceWithAttributeValue(Object var1, String var2, String var3) throws IOException {
      if (var1 instanceof List) {
         List var9 = (List)var1;
         int var10 = 0;

         for (int var12 = var9.size(); var10 < var12; var10++) {
            Object var13 = var9.get(var10);
            Object var8 = this.replaceWithAttributeValue(var13, var2, var3);
            if (var8 != var13) {
               var9.set(var10, var8);
            }
         }

         return var9;
      } else {
         JbNode var4 = (JbNode)var1;
         String var5 = "";
         if (!this.isFlat) {
            var5 = var2.substring(var4.getCrom().getPath_(false).length());
         }

         for (String var7 : PathSeparator.split(var5)) {
            if (var4 == null) {
               break;
            }

            var4 = var4.getChild(var7);
         }

         Map var11 = this.getAttrMap(var4);
         return var11.get(var3);
      }
   }

   private void addToStringBuilder_recursive(Object var1, StringBuilder var2) {
      if (var1 instanceof List) {
         var2.append('{');
         boolean var3 = false;

         for (Object var5 : (List)var1) {
            if (var3) {
               var2.append(",\n");
            } else {
               var3 = true;
            }

            this.addToStringBuilder_recursive(var5, var2);
         }

         var2.append('}');
      } else if (var1 == null) {
         var2.append("NULL");
      } else {
         assert var1 instanceof String;
         String var6 = (String)var1;
         if (KongaStringUtils.CSV_RequireQualifier(var6, "{};,\"")) {
            var6 = "\"" + KongaStringUtils.CSV_EscapeDoubleQuote(var6) + "\"";
         }

         var2.append(var6);
      }
   }

   public String getDEValue(String var1) {
      if (this.reader != null && !var1.startsWith("[")) {
         StringTokenizer var2 = PathSeparator.tokenize(var1, true);
         String var3 = "";
         String var4 = "";
         String var5 = null;
         String var6 = null;
         String var7 = null;
         ArrayList var8 = new ArrayList();
         Object var9 = null;
         int var10 = -1;

         while (var2.hasMoreTokens()) {
            String var11 = var2.nextToken();
            switch (var11.charAt(0)) {
               case '$':
                  var3 = var3 + var6 + var11;
                  var4 = var4 + var6 + var11;
                  var6 = null;
                  break;
               case '.':
                  var3 = var3 + var6 + var11;
                  FillDataElementKit.Status var12 = this.cmp(var3);
                  if (var10 <= 0 && var12 != FillDataElementKit.Status.is_child && var12 != FillDataElementKit.Status.is_diff_branch) {
                     var4 = var4 + var6 + var11;
                  } else {
                     var4 = var4 + var6 + "#.";
                     var8.add(getInstanceNumber(var7));
                     this.addVectorDE(var5, var3, var4);
                     var5 = var4;
                  }

                  var6 = null;
                  break;
               default:
                  var6 = StringUtils.substringBefore(var11, "#");
                  var7 = StringUtils.substringAfter(var11, "#");
                  var10 = var11.indexOf(35);
            }
         }

         if (var5 == null) {
            if (var7 != null && !var7.isEmpty()) {
               JbNode var16 = this.findJbNodeByPath(var3, FillDataElementKit.FindType.is_instance_holder);
               int var19 = getInstanceNumber(var7);
               int var13 = var16.getChildren().size();
               if (var19 < 0) {
                  var19 += var13;
               } else {
                  var19--;
               }

               var9 = var19 >= 0 && var19 < var13 ? var16.getChildren().get(var19) : null;
            } else {
               var9 = this.findJbNodeByPath(var3, FillDataElementKit.FindType.is_single_instance);
            }
         } else {
            var9 = this.vde_map.get(var5);
            if (var9 != null) {
               var9 = this.createVectorDE(var9, var8, 0);
            }
         }

         if (var9 != null) {
            String var17 = var6;
            if (var17 == null) {
               var17 = "";
            }

            try {
               var9 = this.replaceWithAttributeValue(var9, var3, var17);
            } catch (IOException var14) {
               throw new RuntimeException(var14);
            }
         }

         if (var9 != null) {
            StringBuilder var18 = new StringBuilder();
            this.addToStringBuilder_recursive(var9, var18);
            return var18.toString();
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   enum FindType {
      is_instance_holder,
      is_single_instance;
   }

   enum Status {
      is_generator,
      is_parent,
      is_child,
      is_diff_branch;
   }
}
