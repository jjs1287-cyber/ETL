package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.SourceTreeDataElementContainer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.script.Transform;

public final class CheckMapping {
   private final TreeMapper m_TreeMapper;
   private boolean m_noSourceDataUsedInMapping;
   public List<String> sourceDeList = Lists.newArrayList();

   public CheckMapping(TreeMapper var1) {
      this.m_TreeMapper = var1;
   }

   public String checkSource(String var1) {
      String var2 = "";
      this.sourceDeList = Lists.newArrayList();
      this.m_noSourceDataUsedInMapping = true;
      TreeMapperSourceTree var3 = this.m_TreeMapper.m_sourceTreeComponent;
      this.m_TreeMapper.m_transform.setSourceDataElementContainer(new SourceTreeDataElementContainer(var3));
      List var4 = this.m_TreeMapper.m_transform.getRpnTokenVector(var1);
      if (var4 != null) {
         List var5 = Transform.getDEVector(var4);
         this.m_noSourceDataUsedInMapping = var5.isEmpty();
         int var7 = var5.size();

         try {
            this.sourceDeList = var5;
            StringBuilder var8 = new StringBuilder();

            for (int var6 = 0; var6 < var7; var6++) {
               String var9 = (String)var5.get(var6);
               if (this.m_TreeMapper.findSourceNode(var9) == null) {
                  TreeMapperSourceTree var10 = this.m_TreeMapper.m_sourceTreeComponent;
                  Node var11 = var10.expandTreeAndLookForNode(var9);
                  if (var11 == null && var9.endsWith("xsi:nil")) {
                     String var12 = var9.substring(0, var9.length() - 7);
                     Node var13 = var10.findNode(var12);
                     if (var13 == null) {
                        throw new TreeMapperException(
                           "Internal Error: Cannot find the source node "
                              + var9
                              + " in the source tree. \n You may avoid the problem by removing the mapping. "
                        );
                     }

                     var11 = var13.createNilNodeToMap();
                  }

                  var10.setDirty();
                  if (var11 == null) {
                     var8.append("undefined source DE: ").append(var9).append("\n");
                  }
               }
            }

            var2 = var8.toString();
         } catch (TreeMapperException var14) {
            var2 = var14.getMessage();
         }
      } else {
         var2 = this.m_TreeMapper.m_transform.m_err.getErrorMessage();
      }

      return var2;
   }

   public boolean checkLoopSourceToSingleTarget(String var1) {
      List var2 = this.m_TreeMapper.m_transform.getRpnTokenVector(var1);
      if (var2 == null) {
         return false;
      }

      for (Object __etlItem80 : Transform.getDEVector(var2)) {
         String var5 = (String) __etlItem80;
         int var6 = 0;

         int var7;
         while ((var7 = var5.indexOf(".", var6)) >= 0) {
            int var8;
            if ((var8 = var5.indexOf(35, var6)) < 0 || var8 > var7) {
               return true;
            }

            var6 = var7 + 1;
         }
      }

      return false;
   }

   public boolean noSourceDataUsed() {
      return this.m_noSourceDataUsedInMapping;
   }
}
