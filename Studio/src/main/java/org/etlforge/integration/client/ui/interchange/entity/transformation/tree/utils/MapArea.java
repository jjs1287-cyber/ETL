package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.MapAreaDisplayOption;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.SourceTreeDataElementContainer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.StagingPanel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingManager;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration.Migration;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.ui.util.focus.FocusUtils;

public class MapArea extends JPanel {
   private static final Color clr_attribute_normal = Color.green.darker();
   private static final Color clr_warning = Color.orange;
   private static final Color clr_generator = org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND;
   private static final Color clr_error = Color.red;
   private static final Color clr_untranslated = Color.RED;
   private static final int y_offset = 3;
   private static final int m_dx = 3;
   private static final int m_dy = 3;
   private static final int m_dx2 = 6;
   private static final int m_dy2 = 6;
   private final TreeMapper m_TreeMapper;
   private MappingManager m_mappingManager;
   private final Migration m_TreeMapperMigration;
   private final TipWindow m_tip;
   public Map<Node, Set<Node>> m_table;
   public Map<Node, Set<Node>> m_conflict_map;
   public final Map<Node, Node> m_generator_map;
   private final Map<Node, String> m_sourceExpressionTable;
   public Set<Node> m_translatedSourceSet;
   public Set<Node> m_translatedTargetSet;
   public Set<Node> m_mappedSourceSet;
   public Set<Node> m_mappedTargetSet;
   private int sy0;
   private int ty0;
   private int xmax;
   private int ymax;
   private int dy;
   private int xmax1;
   private Node m_showTipTargetNode;
   private final TreeComponent sourceTC;
   private final TreeComponent targetTC;

   public MapArea(TreeMapper var1) {
      super(null);
      this.setBackground(StagingPanel.m_clrLightBgd);
      this.m_TreeMapperMigration = null;
      this.m_TreeMapper = var1;
      this.sourceTC = this.m_TreeMapper.m_sourceTreeComponent;
      this.targetTC = this.m_TreeMapper.m_targetTreeComponent;
      this.m_sourceExpressionTable = var1.m_sourceExpressionTable;
      this.m_mappedSourceSet = new HashSet<>();
      this.m_mappedTargetSet = new HashSet<>();
      this.m_tip = this.m_TreeMapper.m_tip;
      this.m_table = this.m_TreeMapper.m_mapAreaTable;
      this.m_conflict_map = this.m_TreeMapper.m_conflictMap;
      this.m_generator_map = this.m_TreeMapper.m_generatorMap;
      this.m_mappingManager = this.m_TreeMapper.getMappingManager();
      MapArea.MouseInputHandler var2 = new MapArea.MouseInputHandler();
      this.addMouseMotionListener(var2);
      this.addMouseListener(var2);
      this.addMouseWheelListener(var2);
      this.installDisplaySettingsShortcut();
      FocusUtils.makeComponentMouseFocusable(this);
   }

   public MapArea(Migration var1, Transformation var2) {
      super(null);
      this.setBackground(StagingPanel.m_clrLightBgd);
      this.m_TreeMapperMigration = var1;
      this.m_TreeMapper = null;
      this.sourceTC = this.m_TreeMapperMigration.m_oldSourceTree;
      this.targetTC = this.m_TreeMapperMigration.m_oldTargetTree;
      this.m_sourceExpressionTable = new HashMap<>();
      this.m_mappedSourceSet = new HashSet<>();
      this.m_mappedTargetSet = new HashSet<>();
      this.m_generator_map = null;
      this.m_tip = new TipWindow(this);
      TreeComponent var3 = var1.m_oldSourceTree;
      TreeComponent var4 = var1.m_oldTargetTree;
      this.m_table = Maps.newHashMap();
      List var5 = var2.getMapList();
      Transform var6 = new Transform();
      var6.setSourceDataElementContainer(new SourceTreeDataElementContainer(var3));

      for (Object __etlItem109 : var5) {
         String var8 = (String) __etlItem109;
         Mapping var9 = MapStringFormat.parse(var8);
         String var10 = var9.getTargetPath().toString();
         String var11 = var9.getExpression();
         Node var12 = var4.findNode_expandTreeIfNotExist(var10);
         if (var12 != null) {
            this.m_mappedTargetSet.add(var12);
         }

         Set var13 = Utils.getSourceExpressionList(var3, var11, var6);
         if (var13 == null) {
            var13 = Sets.newHashSet();
         }

         this.m_mappedSourceSet.addAll(var13);
         this.m_table.put(var12, var13);
      }

      MapArea.MouseInputHandler var14 = new MapArea.MouseInputHandler();
      this.addMouseMotionListener(var14);
      this.addMouseListener(var14);
      this.addMouseWheelListener(var14);
      this.installDisplaySettingsShortcut();
      FocusUtils.makeComponentMouseFocusable(this);
   }

   private void getMappedLoopNodes(Set<Node> var1) {
      for (Node var3 : this.m_table.keySet()) {
         for (Node var4 = var3.getImmediateRootLoopNode(); var4 != null; var4 = var4.getImmediateRootLoopNode()) {
            var1.add(var4);
         }
      }
   }

   void getTranslatedSourceSet(Map<Node, Node> var1) {
      if (var1 == null) {
         this.m_translatedSourceSet = new HashSet<>();
      } else {
         this.m_translatedSourceSet = var1.keySet();
      }
   }

   void getTranslatedTargetSet(Map<Node, Node> var1) {
      this.m_translatedTargetSet = new HashSet<>();
      if (var1 != null) {
         this.m_translatedTargetSet.addAll(var1.values());
      }
   }

   @Override
   public void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      this.dy = Node.m_textHeight / 2 + 1;
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      if (this.m_TreeMapperMigration != null) {
         this.sy0 = this.m_TreeMapperMigration.m_oldSourceScrollPane.getVerticalScrollBar().getValue() - this.dy;
         this.ty0 = this.m_TreeMapperMigration.m_oldTargetScrollPane.getVerticalScrollBar().getValue() - this.dy;
         var2 = true;
         this.getTranslatedSourceSet(this.m_TreeMapperMigration.m_sourceMapArea.m_table_old2new);
         this.getTranslatedTargetSet(this.m_TreeMapperMigration.m_targetMapArea.m_table_new2old);
      } else {
         this.sy0 = this.m_TreeMapper.m_sourceScroll.getVerticalScrollBar().getValue() - this.dy;
         this.ty0 = this.m_TreeMapper.m_targetScroll.getVerticalScrollBar().getValue() - this.dy;
         var2 = this.m_TreeMapper.isMapDisplayFlagOn(MapAreaDisplayOption.ATTRIBUTES);
         var3 = this.m_TreeMapper.isMapDisplayFlagOn(MapAreaDisplayOption.GENERATORS);
         var4 = this.m_TreeMapper.isMapDisplayFlagOn(MapAreaDisplayOption.ERRORS);
      }

      this.xmax = this.getSize().width - 1;
      this.xmax1 = this.xmax * 3 / 4;
      this.ymax = this.getSize().height - 1;
      Color var5 = var1.getColor();

      try {
         boolean var6 = this.m_conflict_map != null;
         if (var2) {
            for (Entry var8 : this.m_table.entrySet()) {
               Node var9 = (Node)var8.getKey();
               if (var9 != null) {
                  Set var10 = (Set)var8.getValue();
                  this.drawMappingLineWithWarning(var1, var10, var9, clr_attribute_normal, var6);
               }
            }
         }

         if (this.m_conflict_map != null && var4) {
            for (Entry var16 : this.m_conflict_map.entrySet()) {
               Node var18 = (Node)var16.getKey();
               if (var18 != null) {
                  Set var20 = (Set)var16.getValue();
                  if (var20 != null && var20.size() > 0) {
                     this.drawConflictLines(var1, var20, var18, clr_error);
                  }
               }
            }
         }

         HashSet var15 = Sets.newHashSet();
         this.getMappedLoopNodes(var15);
         if (this.m_generator_map != null && var3) {
            for (Entry var19 : this.m_generator_map.entrySet()) {
               Node var21 = (Node)var19.getKey();
               Node var11 = (Node)var19.getValue();
               if (var21 != null && var15.contains(var21)) {
                  this.drawGeneratorLine(var1, var11, var21, clr_generator);
               }
            }
         }
      } catch (RuntimeException var12) {
      }

      var1.setColor(var5);
   }

   private void drawMappingLineWithWarning(Graphics var1, Set<Node> var2, Node var3, Color var4, boolean var5) {
      int var6 = var2.size();
      if (var6 == 0) {
         Color var7 = var4;
         boolean var8 = this.m_mappingManager != null && this.m_mappingManager.getInvalidMapping(var3) != null;
         if (var8 || this.m_translatedTargetSet != null && !this.m_translatedTargetSet.contains(var3)) {
            var7 = clr_untranslated;
         }

         var1.setColor(var7);
         this.drawMapLine0(var1, var3);
      } else if (var5) {
         boolean var12 = this.m_mappingManager.getInvalidMapping(var3) != null;
         if (!var12 && this.sourceTC != null) {
            String var14 = var3.getRootLoopGenerator();

            for (String var10 : var3.getPathList()) {
               String var11 = this.sourceTC.findGenratorPath(var10);
               if (!var14.startsWith(var11)) {
                  var12 = true;
                  break;
               }
            }
         }

         Color var15 = var4;
         if (var12) {
            var15 = clr_error;
         } else if (this.m_TreeMapper != null && this.m_TreeMapper.isTypeConflict(var3)) {
            var15 = clr_warning;
         }

         var1.setColor(var15);

         for (Node var19 : var2) {
            this.drawMapLine2(var1, var19, var3);
         }
      } else {
         Color var13 = var4;
         if (this.m_TreeMapper != null && this.m_TreeMapper.isTypeConflict(var3)) {
            var13 = Color.orange;
         }

         for (Node var18 : var2) {
            if (this.m_translatedTargetSet != null
               && !this.m_translatedTargetSet.contains(var3)
               && this.m_translatedSourceSet != null
               && !this.m_translatedSourceSet.contains(var18)) {
               var1.setColor(clr_untranslated);
            } else {
               var1.setColor(var13);
            }

            this.drawMapLine2(var1, var18, var3);
         }
      }
   }

   private void drawConflictLines(Graphics var1, Set<Node> var2, Node var3, Color var4) {
      int var5 = var2.size();
      if (var5 == 0) {
         Color var6 = var4;
         if (this.m_translatedTargetSet != null && !this.m_translatedTargetSet.contains(var3)) {
            var6 = clr_untranslated;
         }

         var1.setColor(var6);
         this.drawMapLine0(var1, var3);
      } else {
         for (Node var7 : var2) {
            Color var8 = var4;
            if (this.m_translatedTargetSet != null && this.m_translatedSourceSet != null) {
               var8 = this.m_translatedTargetSet.contains(var3) && this.m_translatedSourceSet.contains(var7) ? var4 : clr_untranslated;
            }

            this.drawGeneratorLine(var1, var7, var3, var8);
         }
      }
   }

   private void drawMapLine2(Graphics var1, Node var2, Node var3) {
      if (var2 != null && var3 != null) {
         Node var4 = findShowNode(this.sourceTC, var2);
         Node var5 = findShowNode(this.targetTC, var3);
         if (var4 != null && var5 != null) {
            int var7 = var4.m_p.y - this.sy0 + 3;
            int var9 = var5.m_p.y - this.ty0 + 3;
            boolean var10 = var7 < 0;
            boolean var11 = var7 > this.ymax;
            boolean var12 = var9 < 0;
            boolean var13 = var9 > this.ymax;
            if ((!var10 || !var12) && (!var11 || !var13)) {
               if ((!var10 || !var13) && (!var11 || !var12)) {
                  if (var10) {
                     int var14 = this.xmax1 * var7 / (var7 - var9);
                     var1.drawLine(var14, 0, this.xmax1, var9);
                  } else if (var11) {
                     int var16 = this.xmax1 * (var7 - this.ymax) / (var7 - var9);
                     var1.drawLine(var16, this.ymax, this.xmax1, var9);
                  } else if (var12) {
                     int var15 = this.xmax1 * var7 / (var7 - var9);
                     var1.drawLine(0, var7, var15, 0);
                  } else if (var13) {
                     int var17 = this.xmax1 * (var7 - this.ymax) / (var7 - var9);
                     var1.drawLine(0, var7, var17, this.ymax);
                  } else {
                     var1.drawLine(0, var7, this.xmax1, var9);
                  }
               } else {
                  int var6 = this.xmax1 * var7 / (var7 - var9);
                  int var8 = this.xmax1 * (var7 - this.ymax) / (var7 - var9);
                  var1.drawLine(var6, 0, var8, this.ymax);
               }

               if (!var12 && !var13) {
                  var1.drawLine(this.xmax1, var9, this.xmax, var9);
                  var1.drawRect(this.xmax1 - 3, var9 - 3, 6, 6);
               }
            }
         }
      }
   }

   private void drawGeneratorLine(Graphics var1, Node var2, Node var3, Color var4) {
      if (var2 != null && var3 != null) {
         if (this.sourceTC != null) {
            var2 = this.sourceTC.convertToSfdcNode(var2);
         }

         if (this.targetTC != null) {
            var3 = this.targetTC.convertToSfdcNode(var3);
         }

         Node var5 = findShowNode(this.sourceTC, var2);
         Node var6 = findShowNode(this.targetTC, var3);
         if (var5 != null && var6 != null) {
            int var8 = var5.m_p.y - this.sy0 + 3;
            int var10 = var6.m_p.y - this.ty0 + 3;
            if (var5.m_p.y == 0) {
               var8 += this.dy;
            }

            if (var6.m_p.y == 0) {
               var10 += this.dy;
            }

            boolean var11 = var8 < 0;
            boolean var12 = var8 > this.ymax;
            boolean var13 = var10 < 0;
            boolean var14 = var10 > this.ymax;
            if ((!var11 || !var13) && (!var12 || !var14)) {
               byte var15 = 1;
               int var16 = Math.abs(var8 - var10);
               var1.setColor(var4);
               if (var16 > this.xmax1) {
                  int var17 = this.xmax1 * 5 / (4 * var16) + 3;
                  int var18 = -var17 / 2;
                  int var19 = var18 + var17;
                  if (var11 && var14 || var12 && var13) {
                     int var22 = this.xmax1 * var8 / (var8 - var10);
                     int var27 = this.xmax1 * (var8 - this.ymax) / (var8 - var10);

                     for (int var39 = var18; var39 < var19; var39++) {
                        var1.drawLine(var22 + var39, 0, var27 + var39, this.ymax);
                     }
                  } else if (var11) {
                     int var7 = this.xmax1 * var8 / (var8 - var10);

                     for (int var20 = var18; var20 < var19; var20++) {
                        var1.drawLine(var7 + var20, 0, this.xmax1 + var20, var10);
                     }
                  } else if (var12) {
                     int var9 = this.xmax1 * (var8 - this.ymax) / (var8 - var10);

                     for (int var35 = var18; var35 < var19; var35++) {
                        var1.drawLine(var9 + var35, this.ymax, this.xmax1 + var35, var10);
                     }
                  } else if (var13) {
                     int var21 = this.xmax1 * var8 / (var8 - var10);

                     for (int var36 = var18; var36 < var19; var36++) {
                        var1.drawLine(var36, var8, var21 + var36, 0);
                     }
                  } else if (var14) {
                     int var26 = this.xmax1 * (var8 - this.ymax) / (var8 - var10);

                     for (int var37 = var18; var37 < var19; var37++) {
                        var1.drawLine(var37, var8, var26 + var37, this.ymax);
                     }
                  } else {
                     for (int var38 = var18; var38 < var19; var38++) {
                        var1.drawLine(var38, var8, this.xmax1 + var38, var10);
                     }
                  }
               } else {
                  int var31 = var16 * 5 / (4 * this.xmax1) + 3;
                  int var33 = -var31 / 2;
                  int var34 = var33 + var31;
                  if (var11 && var14 || var12 && var13) {
                     int var25 = this.xmax1 * var8 / (var8 - var10);
                     int var30 = this.xmax1 * (var8 - this.ymax) / (var8 - var10);

                     for (int var45 = var33; var45 < var34; var45++) {
                        var1.drawLine(var25, var45, var30, this.ymax + var45);
                     }
                  } else if (var11) {
                     int var23 = this.xmax1 * var8 / (var8 - var10);

                     for (int var40 = var33; var40 < var34; var40++) {
                        var1.drawLine(var23, var40, this.xmax1, var10 + var40);
                     }
                  } else if (var12) {
                     int var28 = this.xmax1 * (var8 - this.ymax) / (var8 - var10);

                     for (int var41 = var33; var41 < var34; var41++) {
                        var1.drawLine(var28, this.ymax + var41, this.xmax1, var10 + var41);
                     }
                  } else if (var13) {
                     int var24 = this.xmax1 * var8 / (var8 - var10);

                     for (int var42 = var33; var42 < var34; var42++) {
                        var1.drawLine(0, var8 + var42, var24, var42);
                     }
                  } else if (var14) {
                     int var29 = this.xmax1 * (var8 - this.ymax) / (var8 - var10);

                     for (int var43 = var33; var43 < var34; var43++) {
                        var1.drawLine(0, var8 + var43, var29, this.ymax + var43);
                     }
                  } else {
                     for (int var44 = var33; var44 < var34; var44++) {
                        var1.drawLine(0, var8 + var44, this.xmax1, var10 + var44);
                     }
                  }
               }

               if (!var13 && !var14) {
                  for (int var32 = -var15; var32 <= var15; var32++) {
                     var1.drawLine(this.xmax1, var10 + var32, this.xmax, var10 + var32);
                  }

                  var1.drawRect(this.xmax1 - 3, var10 - 3, 6, 6);
               }
            }
         }
      }
   }

   private void drawMapLine0(Graphics var1, Node var2) {
      if (var2 != null) {
         Node var3 = findShowNode(this.targetTC, var2);
         if (var3 != null) {
            int var4 = var3.m_p.y - this.ty0 + 3;
            if (var4 >= 0 && var4 <= this.ymax) {
               var1.drawLine(this.xmax1, var4, this.xmax, var4);
               var1.drawRect(this.xmax1 - 3, var4 - 3, 6, 6);
            }
         }
      }
   }

   private void installDisplaySettingsShortcut() {
      if (this.m_TreeMapper != null) {
         ;
      }
   }

   private static Node findShowNode(TreeComponent var0, Node var1) {
      if (LdapUtils.isLdapAttributeCommand(var0, var1)) {
         return null;
      }

      CROM var2 = var0.sfdcNode;
      if (var2 != null && var1.m_CROM != var2 && !var2.isAncestorOf(var1.m_CROM)) {
         return null;
      }

      Node var3 = var1;

      for (Node var4 = var1.m_parent; var4 != null; var4 = var4.m_parent) {
         if (var4.isNotExpanded()) {
            var3 = null;
         } else if (var3 == null) {
            var3 = var1;
         }

         var1 = var4;
         if (var4.m_CROM == var2) {
            break;
         }
      }

      return var3;
   }

   private final class MouseInputHandler extends MouseInputAdapter implements MouseWheelListener {
      private MouseInputHandler() {
      }

      @Override
      public void mouseDragged(MouseEvent var1) {
         MapArea.this.getParent().dispatchEvent(var1);
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
         Node var2 = this.findTargetNode(var1.getPoint());
         if (MapArea.this.m_showTipTargetNode != var2) {
            if (var2 != null) {
               int var3 = var1.getX();
               int var4 = var1.getY();
               if (MapArea.this.m_TreeMapper != null && MapArea.this.m_TreeMapperMigration == null) {
                  try {
                     Point var10 = MapArea.this.getLocationOnScreen();
                     Point var11 = MapArea.this.m_TreeMapper.getLocationOnScreen();
                     String var12;
                     if (MapArea.this.m_TreeMapper.isTypeConflict(var2)) {
                        var12 = "potential\ndata type conflict";
                     } else {
                        var12 = MapArea.this.m_sourceExpressionTable.get(var2);
                     }

                     if (var12 == null && var2.isRootLoopNode()) {
                        if (MapArea.this.m_TreeMapper != null && MapArea.this.m_TreeMapper.m_targetTreeComponent.m_flat) {
                           var12 = "The multiplicity of the target depends on\nthe multiplicity of the source loop node "
                              + var2.m_mappingKits.getGeneratorString();
                        } else {
                           var12 = "The multiplicity of the target node "
                              + var2.m_deName
                              + " depends on\nthe multiplicity of the source loop node "
                              + var2.m_mappingKits.getGeneratorString();
                        }
                     }

                     InvalidMapping var8 = MapArea.this.m_mappingManager.getInvalidMapping(var2);
                     if (var8 != null) {
                        var12 = var12 + "\n" + var8.getError();
                     }

                     MapArea.this.m_tip.showItAtXY(var12, var3 + var10.x - var11.x, var4 + var10.y - var11.y + 17, this);
                     MapArea.this.m_showTipTargetNode = var2;
                  } catch (Exception var9) {
                  }
               } else {
                  Point var5 = MapArea.this.getLocationOnScreen();
                  Point var6 = MapArea.this.m_TreeMapperMigration.getContentPane().getLocationOnScreen();
                  String var7 = MapArea.this.m_sourceExpressionTable.get(var2);
                  if (var7 != null) {
                     MapArea.this.m_tip.showItAtXY(var7, var3 + var5.x - var6.x, var4 + var5.y - var6.y + 17, this);
                     MapArea.this.m_showTipTargetNode = var2;
                  }
               }
            } else {
               if (MapArea.this.m_showTipTargetNode != null) {
                  MapArea.this.m_tip.setVisible(false);
                  MapArea.this.m_showTipTargetNode = null;
               }

               MapArea.this.getParent().dispatchEvent(var1);
            }
         }
      }

      private Node findTargetNode(Point var1) {
         int var2 = var1.x;
         int var3 = var1.y;
         if (var2 <= MapArea.this.xmax1 + 3 && var2 >= MapArea.this.xmax1 - 3) {
            for (Entry var5 : MapArea.this.m_table.entrySet()) {
               Node var6 = (Node)var5.getKey();
               if (var6 != null && var6.m_p != null) {
                  int var7 = var6.m_p.y - MapArea.this.ty0 + 3;
                  if (var3 >= var7 - 3 && var3 <= var7 + 3) {
                     return var6;
                  }
               }
            }

            if (MapArea.this.m_generator_map == null) {
               return null;
            }

            for (Node var10 : ImmutableList.copyOf(MapArea.this.m_generator_map.keySet())) {
               int var11 = var10.m_p.y - MapArea.this.ty0;
               if (var3 >= var11 - 3 && var3 <= var11 + 3) {
                  return var10;
               }
            }
         }

         return null;
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         if (MapArea.this.m_TreeMapper != null) {
            Node var2 = this.findTargetNode(var1.getPoint());
            int var3 = var1.getClickCount();
            if (var3 == 1) {
               MapArea.this.m_TreeMapper.m_targetTreeComponent.showNode(var2, true);
               MapArea.this.m_TreeMapper.m_targetTreeComponent.refresh();
               MapArea.this.m_TreeMapper.selectTargetNode(var2, MapArea.this.m_TreeMapper.isMappingMode());
            } else {
               MapArea.this.m_TreeMapper.showFormulaBuilder(var2);
            }
         }

         MapArea.this.getParent().dispatchEvent(var1);
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         MapArea.this.getParent().dispatchEvent(var1);
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         MapArea.this.getParent().dispatchEvent(var1);
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent var1) {
         int var2 = var1.getUnitsToScroll();
         if (MapArea.this.m_TreeMapperMigration != null) {
            MapArea.this.m_TreeMapperMigration.m_oldSourceTree.moveByMouseWheel(var2);
            MapArea.this.m_TreeMapperMigration.m_oldTargetTree.moveByMouseWheel(var2);
         } else if (MapArea.this.m_TreeMapper != null) {
            MapArea.this.m_TreeMapper.m_sourceTreeComponent.moveByMouseWheel(var2);
            MapArea.this.m_TreeMapper.m_targetTreeComponent.moveByMouseWheel(var2);
         }
      }
   }
}

