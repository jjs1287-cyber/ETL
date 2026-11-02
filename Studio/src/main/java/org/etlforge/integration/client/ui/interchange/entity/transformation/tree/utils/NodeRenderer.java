package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.Icon;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.StagingPanel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperIcons;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.lang.KongaStringUtils;

public class NodeRenderer {
   private final TreeComponent m_TreeComponent;
   private CROM sfdcNode;

   public NodeRenderer(TreeComponent var1) {
      this.m_TreeComponent = var1;
   }

   public void paint(Graphics var1, Node var2) {
      boolean var3 = true;
      this.sfdcNode = this.m_TreeComponent.sfdcNode;
      if (this.sfdcNode != null) {
         var3 = this.isLimitedDisplay(var2, this.sfdcNode);
      }

      int var4 = var2.m_p.x;
      if (var3) {
         this.updatePoint(var2, this.sfdcNode);
         if (this.hasOnlyNilAttribute(var2)) {
            var2.setShowMode(0);
         }

         if (var2 == this.m_TreeComponent.m_showNode) {
            this.m_TreeComponent.m_showNodePos = var2.m_p.y;
         }

         Node.NodeLocation var5 = this.paintLineAndTextAndIcon(var1, var2, true);
         this.m_TreeComponent.m_savedPointsNode.add(var5);
         if (var2.isNotExpanded() || !var2.hasChildren()) {
            return;
         }

         var4 = (int)(var2.m_p.x + Node.m_dx + Node.m_dx / 2 + 0.5);
      }

      int var9 = var2.m_p.y + Node.m_textHeight;
      int var6 = (int)((Node.m_textHeight - this.m_TreeComponent.m_signRectSize) / 2 + 0.5);

      for (Node var8 : Lists.newArrayList(var2.getChildren())) {
         if (!var8.isHidden()) {
            if (var8.isFolder()) {
               var9 = this.paintChildFolder(var1, var8, var3, var4, var9, var6);
            } else {
               var9 = this.paintChildLeaf(var1, var2, var8, var3, this.sfdcNode, var4, var9);
            }
         }
      }
   }

   private int paintChildLeaf(Graphics var1, Node var2, Node var3, boolean var4, CROM var5, int var6, int var7) {
      if (LdapUtils.isLdapAttributeCommand(this.m_TreeComponent, var3)) {
         return var7;
      }

      if (!var2.isFullyExpanded()) {
         return var7;
      }

      if (this.m_TreeComponent.m_checkMapMode
         && (!this.m_TreeComponent.m_checkMapMode || !var3.isSourceUsed() && !var3.isTargetMapped() && var3 != this.m_TreeComponent.getHighlightedNode())) {
         return var7;
      }

      if (var4) {
         this.updatePoint(var3, var5);
         if (var3 == this.m_TreeComponent.m_showNode) {
            this.m_TreeComponent.m_showNodePos = var2.m_p.y;
         }

         Node.NodeLocation var8 = this.paintLineAndTextAndIcon(var1, var3, false);
         this.m_TreeComponent.m_savedPointsNode.add(var8);
         int var9 = var7;
         int var10 = var3.m_p.y + Node.m_textHeight / 2;
         if (var9 < this.m_TreeComponent.m_ymin) {
            var9 = this.m_TreeComponent.m_ymin;
         }

         if (var10 > this.m_TreeComponent.m_ymax) {
            var10 = this.m_TreeComponent.m_ymax;
         }

         if (var10 > var9 && var1 != null) {
            var1.drawLine(var6, var9, var6, var10);
         }

         var7 = var10;
      }

      return var7;
   }

   private boolean isLimitedDisplay(Node var1, CROM var2) {
      CROM var3 = var1.getCROM();
      return var3 == var2 || var2.isAncestorOf(var3);
   }

   private void updatePoint(Node var1, CROM var2) {
      if (var1.m_CROM == var2) {
         var1.m_p.x = Node.m_dx;
         this.m_TreeComponent.m_y = this.m_TreeComponent.m_root.m_p.y;
      } else if (var1.m_parent != null) {
         var1.m_p.x = var1.m_parent.m_p.x + Node.m_dx;
         this.m_TreeComponent.m_y = this.m_TreeComponent.m_y + Node.m_dy;
      }

      var1.m_p.y = this.m_TreeComponent.m_y;
   }

   private boolean hasOnlyNilAttribute(Node var1) {
      return !var1.m_CROM.isTypeExpanded() && var1.getChildCount() == 1 && var1.getChildAt(0).getDisplayName().equals("xsi:nil");
   }

   private int paintChildFolder(Graphics var1, Node var2, boolean var3, int var4, int var5, int var6) {
      this.paint(var1, var2);
      if (var3) {
         int var7 = var5 - var6;
         int var8 = var2.m_p.y + var6;
         if (var7 < this.m_TreeComponent.m_ymin) {
            var7 = this.m_TreeComponent.m_ymin;
         }

         if (var8 > this.m_TreeComponent.m_ymax) {
            var8 = this.m_TreeComponent.m_ymax;
         }

         if (var8 > var7 && var1 != null) {
            var1.drawLine(var4, var7, var4, var8);
         }

         var5 = var8 + this.m_TreeComponent.m_signRectSize;
      }

      return var5;
   }

   private void displayMatchingString(Graphics var1, String var2, Node var3, String var4, int var5) {
      int var6 = var2.length();
      int var7 = 0;
      int var8 = var4.length();
      int var10 = var3.m_x7;
      String var12 = var4.toLowerCase();

      int var9;
      while ((var9 = var12.indexOf(var2, var7)) >= 0) {
         if (var9 > var7) {
            var1.setColor(StagingPanel.m_clrDefaultValueText);
            String var11 = var4.substring(var7, var9);
            var1.drawString(var11, var10, var5);
            var10 += var1.getFontMetrics().stringWidth(var11);
         }

         var7 = var9 + var6;
         String var13 = var4.substring(var9, var7);
         var1.setColor(Color.red);
         var1.drawString(var13, var10, var5);
         var10 += var1.getFontMetrics().stringWidth(var13);
      }

      if (var7 < var8) {
         var1.setColor(StagingPanel.m_clrDefaultValueText);
         String var14 = var4.substring(var7, var8);
         var1.drawString(var14, var10, var5);
         var10 += var1.getFontMetrics().stringWidth(var14);
      }

      var3.m_x8 = (short)(var10 + 10);
   }

   private Node.NodeLocation paintLineAndTextAndIcon(Graphics var1, Node var2, boolean var3) {
      if (var1 != null) {
         var1.setColor(this.m_TreeComponent.m_textColor);
      }

      int var4 = (int)(var2.m_p.y + (Node.m_textHeight - this.m_TreeComponent.m_signRectSize) / 2 + 0.5);
      int var5 = (int)(var2.m_p.y + (Node.m_textHeight - 14) / 2 + 0.5);
      int var6 = (int)(var2.m_p.y + Node.m_textHeight / 2 + 0.5);
      int var7 = var2.m_p.y + Node.m_textHeight;
      int var8 = (int)(var7 - Node.m_textHeight / 6 + 0.5);
      var2.m_x0 = (short)(var2.m_p.x + (Node.m_dx - this.m_TreeComponent.m_signRectSize) / 2 + 0.5);
      short var9 = (short)(var2.m_p.x + Node.m_dx / 2 + 0.5);
      var2.m_x2 = (short)(var2.m_p.x + (Node.m_dx + this.m_TreeComponent.m_signRectSize) / 2 + 0.5);
      var2.m_x3 = (short)(var2.m_p.x + Node.m_dx + (Node.m_dx - 14) / 2 + 0.5);
      var2.m_x5 = (short)(var2.m_p.x + Node.m_dx + (Node.m_dx + 14) / 2 + 0.5);
      var2.m_x6 = (short)(var2.m_p.x + 2 * Node.m_dx + 0.5);
      if (var1 != null && var6 > this.m_TreeComponent.m_ymin && var6 < this.m_TreeComponent.m_ymax) {
         if (var3) {
            if (var2.getDisplay().endsWith("No source is specified")) {
               var1.drawLine(var9, var6, var2.m_x3, var6);
               var1.drawLine(var9, var6, var9, var6 - this.m_TreeComponent.m_signRectSize / 2);
               this.drawClosedFolder(var1, var2, var5);
            } else {
               if (var2 != this.m_TreeComponent.m_root) {
                  var1.setColor(this.m_TreeComponent.m_textColor);
                  var1.drawRect(var2.m_x0, var4, this.m_TreeComponent.m_signRectSize, this.m_TreeComponent.m_signRectSize);
                  var1.drawLine(var2.m_x0 + this.m_TreeComponent.m_signOffSet, var6, var2.m_x2 - this.m_TreeComponent.m_signOffSet, var6);
                  if (var2.isNotExpanded()) {
                     var1.drawLine(
                        var9, var4 + this.m_TreeComponent.m_signOffSet, var9, var4 + this.m_TreeComponent.m_signRectSize - this.m_TreeComponent.m_signOffSet
                     );
                  }

                  var1.drawLine(var2.m_x2 + 1, var6, var2.m_x3 - 1, var6);
               }

               if (var2.isExpanded()) {
                  this.drawOpenFolder(var1, var2, var5);
               } else {
                  this.drawClosedFolder(var1, var2, var5);
               }
            }
         } else {
            var1.drawLine(var9, var6, var2.m_x3 - 1, var6);
            Icon var10 = var2.getNodeIcon();
            var10.paintIcon(this.m_TreeComponent, var1, var2.m_x3, var5);
         }
      }

      boolean var19 = var2 == this.m_TreeComponent.getHighlightedNode();
      boolean var11 = var2.m_parent != null && LdapUtils.isLdapAttributeDelete(this.m_TreeComponent, var2.m_parent) && var2.m_CROM.getName().equals("");
      boolean var12 = var2.m_parent != null
         && LdapUtils.isLdapAttributeReplaceAll(this.m_TreeComponent, var2.m_parent)
         && var2.m_CROM.getName().equals("Old?value");
      String var13 = this.getStringToPaint(var2, var11, var12);
      Font var14 = var2.isBold() ? StagingPanel.m_font11Bold : StagingPanel.m_font11Plain;
      FontMetrics var15 = this.m_TreeComponent.getFontMetrics(var14);
      var2.m_x7 = (short)(var2.m_x6 + var15.stringWidth(var13) + 2);
      var2.m_x8 = var2.m_x7;
      if (var1 != null && var6 > this.m_TreeComponent.m_ymin && var6 < this.m_TreeComponent.m_ymax) {
         var1.setFont(var14);
         if (var19) {
            var1.setColor(StagingPanel.m_clrHighLightRect);
            var1.drawRect(var2.m_x6 - 1, var2.m_p.y - 1, var2.m_x7 - var2.m_x6 + 2, Node.m_textHeight + 3);
         }

         if (var2.isSelected()) {
            var1.setColor(this.m_TreeComponent.m_clrYellowUnderLine);
            var1.drawLine(var2.m_x6 - 1, var2.m_p.y + Node.m_textHeight + 4, var2.m_x7 + 1, var2.m_p.y + Node.m_textHeight + 4);
            var1.drawLine(var2.m_x6 - 1, var2.m_p.y + Node.m_textHeight + 3, var2.m_x7 + 1, var2.m_p.y + Node.m_textHeight + 3);
         }

         Color var16 = this.getTextColor(var2, var11, var12);
         var1.setColor(var16);
         var1.drawString(var13, var2.m_x6, var8);
         String var17 = this.getNodeValue(var2);
         if (var2.getCROM() == this.sfdcNode && var17.isEmpty()) {
            var17 = this.getNodeValue(var2.m_parent);
         }

         if (!var17.isEmpty()) {
            Font var18 = var1.getFont();
            var1.setFont(StagingPanel.dataValueFont);
            if (var19 && this.m_TreeComponent.m_hight_light_string != null && !var3) {
               this.displayMatchingString(var1, this.m_TreeComponent.m_hight_light_string, var2, var17, var8);
            } else {
               var1.setColor(StagingPanel.m_clrDefaultValueText);
               var1.drawString(var17, var2.m_x7, var8);
               var2.m_x8 = (short)(var2.m_x7 + var1.getFontMetrics().stringWidth(var17) + 10);
            }

            var1.setFont(var18);
         }

         if (var2.isDeleteAttribute()) {
            var1.drawLine(var2.m_x6, var2.m_p.y + Node.m_textHeight / 2, var2.m_x7, var2.m_p.y + Node.m_textHeight / 2);
         }
      }

      if (var1 != null) {
         int var20 = var2.isFolder() ? -1 : 5;
         if (this.m_TreeComponent.getTreeMapperMigration() != null) {
            var1.setColor(Color.gray);
            int var22 = var2.m_p.y + Node.m_textHeight / 2;
            if (this.m_TreeComponent.draw_leftExtentionLine(var2)) {
               var1.drawLine(0, var22, var2.m_x0 + var20, var22);
            }

            if (this.m_TreeComponent.draw_rightExtentionLine(var2)) {
               var1.drawLine(var2.m_x8 + 2, var22, this.m_TreeComponent.getSize().width, var22);
            }
         } else if (this.m_TreeComponent.getTreeMapper() != null) {
            var1.setColor(Color.lightGray);
            int var21 = var2.m_p.y + Node.m_textHeight / 2;
            if (this.m_TreeComponent == this.m_TreeComponent.m_TreeMapper.m_targetTreeComponent) {
               if (var2.isRootLoopNode() || var2.isTargetMapped()) {
                  var1.drawLine(0, var21, var2.m_x0 + var20, var21);
               }
            } else if (var2.isRootLoopNode() && var2.getPathListSize() > 0 || var2.isSourceUsed()) {
               var1.drawLine(var2.m_x8 + 2, var21, this.m_TreeComponent.getSize().width, var21);
            }
         }

         var1.setColor(this.m_TreeComponent.m_textColor);
      }

      this.m_TreeComponent.m_xmax = this.m_TreeComponent.m_xmax > var2.m_x8 ? this.m_TreeComponent.m_xmax : var2.m_x8;
      return new Node.NodeLocation(var2, new Rectangle(var2.m_p.x, var2.m_p.y, var2.m_x8 - var2.m_p.x, Node.m_textHeight));
   }


   private void drawClosedFolder(Graphics var1, Node var2, int var3) {
      TreeMapperIcons.CLOSED_FOLDER_NODE.paintIcon(this.m_TreeComponent, var1, var2.m_x3, var3);
   }

   private void drawOpenFolder(Graphics var1, Node var2, int var3) {
      TreeMapperIcons.OPEN_FOLDER_NODE.paintIcon(this.m_TreeComponent, var1, var2.m_x3, var3);
   }

   private String getStringToPaint(Node var1, boolean var2, boolean var3) {
      String var4;
      if (this.m_TreeComponent.isSimpleNodeDisplay() && var1.m_parent != null) {
         var4 = this.getSimpleNodeString(var1);
      } else {
         var4 = this.getStringToPaint(var1);
      }

      if (var2) {
         var4 = var4 + " N/A";
      } else if (var3) {
         if (!var1.hasValue()) {
            var4 = var4 + " Replace all";
         }
      } else if (LdapUtils.isLdapClassEntryCommand(this.m_TreeComponent, var1) && StringUtils.isNotEmpty(var1.m_cmd)) {
         var4 = var4 + " cmd=" + var1.m_cmd;
      }

      return var4;
   }

   private String getStringToPaint(Node var1) {
      String var2 = this.m_TreeComponent.actualNodeDisplayNames.get(var1);
      return var2 != null ? var2 : var1.getDisplay();
   }

   private String getSimpleNodeString(Node var1) {
      String var2 = KongaStringUtils.removeStartAndEndIfBothPresent(var1.m_deName, "[", "]");
      String var3 = NodePath.getLeafName(var2);
      if (var3.isEmpty()) {
         var3 = this.getStringToPaint(var1);
      }

      return var3;
   }

   private Color getTextColor(Node var1, boolean var2, boolean var3) {
      Color var4 = this.m_TreeComponent.m_checkMapMode && !var1.isLoopNode() && !var1.isRoot() ? Color.lightGray : this.getDefaultColor(var1);
      if (var1.isPrecondition()) {
         var4 = this.m_TreeComponent.m_clrPreconditionText;
      }

      if (var1.hasMapWarning()) {
         var4 = StagingPanel.m_clrWarningText;
      }

      if (var1.isBatchNode()) {
         var4 = StagingPanel.m_clrBatchNode;
      }

      if (var2) {
         var4 = Color.GRAY;
      } else if (var3 && !var1.hasValue()) {
         var4 = Color.GRAY;
      }

      return var4;
   }

   private Color getDefaultColor(Node var1) {
      if (var1.isPrecondition()) {
         return this.m_TreeComponent.m_clrPreconditionText;
      } else {
         return var1.isFolder() ? this.m_TreeComponent.m_folderTextColor : this.m_TreeComponent.m_textColor;
      }
   }

   private String getNodeValue(Node var1) {
      String var2 = "";
      if (var1.hasValue()) {
         if (var1.isFolder()) {
            var2 = " " + var1.getValue();
         } else if (StringUtils.isNotEmpty(var1.m_cmd)) {
            var2 = ", value = " + var1.getValue();
         } else {
            var2 = " = " + var1.getValue();
         }
      } else if (var1.hasDefaultValue() && !var1.isFolder()) {
         if (var1.isFixedValueAttribute()) {
            var2 = " FIXED = " + var1.getDefaultValue();
         } else {
            var2 = " def = " + var1.getDefaultValue();
         }
      }

      return var2;
   }
}
