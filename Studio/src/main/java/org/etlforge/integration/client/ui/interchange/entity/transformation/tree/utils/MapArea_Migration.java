package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.StagingPanel;

public final class MapArea_Migration extends JPanel {
   private static final Color clr_connected = Color.GRAY;
   private static final int t_source_source = 1;
   private static final int t_target_target = 2;
   private static final String[] sLocation = new String[]{"unknown", "Source_to_Source", "Target_to_Target"};
   private TreeScrollPane m_newScrollPane;
   private TreeScrollPane m_oldScrollPane;
   public Map<Node, Node> m_table_old2new = null;
   public Map<Node, Node> m_table_new2old = null;
   private boolean m_bIsForTarget;
   private int sy0;
   private int ty0;
   private int xmax;
   private int ymax;
   private int dy;
   private int sx0;
   private int tx0;

   public MapArea_Migration(TreeScrollPane var1, TreeScrollPane var2, boolean var3) {
      this.setBackground(StagingPanel.m_clrLightBgd);
      this.m_newScrollPane = var1;
      this.m_oldScrollPane = var2;
      this.m_bIsForTarget = var3;
      this.m_table_new2old = Maps.newHashMap();
      if (!var3) {
         this.m_table_old2new = Maps.newHashMap();
         this.m_newScrollPane = var1;
         this.m_oldScrollPane = var2;
      } else {
         this.m_newScrollPane = var2;
         this.m_oldScrollPane = var1;
      }

      this.addMouseWheelListener(new MapArea_Migration.MouseWheelListenerImpl());
   }

   @Override
   public void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      this.dy = Node.m_textHeight / 2 + 1;
      this.sy0 = this.m_oldScrollPane.getVerticalScrollBar().getValue() - this.dy;
      this.ty0 = this.m_newScrollPane.getVerticalScrollBar().getValue() - this.dy;
      if (this.m_bIsForTarget) {
         this.sx0 = 0;
         this.tx0 = this.xmax;
      } else {
         this.sx0 = this.xmax;
         this.tx0 = 0;
      }

      this.xmax = this.getSize().width - 1;
      this.ymax = this.getSize().height - 1;
      Color var2 = var1.getColor();
      var1.setColor(Color.black);

      for (Entry var4 : this.m_table_new2old.entrySet()) {
         Node var5 = (Node)var4.getKey();
         Node var6 = (Node)var4.getValue();
         var1.setColor(clr_connected);
         this.drawMapLine(var1, var6, var5);
      }

      var1.setColor(var2);
   }

   private void drawMapLine(Graphics var1, Node var2, Node var3) {
      if (var2.isShown() && var3.isShown()) {
         int var5 = var2.m_p.y - this.sy0;
         int var7 = var3.m_p.y - this.ty0;
         boolean var8 = var5 >= 0 && var5 <= this.ymax;
         boolean var9 = var7 >= 0 && var7 <= this.ymax;
         if ((var5 >= 0 || var7 >= 0) && (var5 <= this.ymax || var7 <= this.ymax)) {
            if (var8 && var9) {
               var1.drawLine(this.sx0, var5, this.tx0, var7);
            } else {
               float var10 = (float)(this.tx0 - this.sx0) / (var7 - var5);
               int var4 = (int)(this.sx0 - var5 * var10);
               int var6 = (int)(this.sx0 + (this.ymax - var5) * var10);
               if (var8) {
                  if (var7 > this.ymax) {
                     var1.drawLine(this.sx0, var5, var6, this.ymax);
                  } else {
                     var1.drawLine(this.sx0, var5, var4, 0);
                  }
               } else if (var9) {
                  if (var5 < 0) {
                     var1.drawLine(this.tx0, var7, var4, 0);
                  } else {
                     var1.drawLine(this.tx0, var7, var6, this.ymax);
                  }
               } else {
                  var1.drawLine(var4, 0, var6, this.ymax);
               }
            }
         }
      }
   }

   public void put(Node var1, Node var2) {
      if (this.m_table_new2old != null && this.m_table_old2new != null) {
         Object var3 = this.m_table_new2old.get(var2);
         if (var3 != null) {
            this.m_table_old2new.remove(var3);
         }

         Object var4 = this.m_table_old2new.get(var1);
         if (var4 != null) {
            this.m_table_new2old.remove(var4);
         }
      }

      if (this.m_table_new2old != null) {
         this.m_table_new2old.put(var2, var1);
      }

      if (this.m_table_old2new != null) {
         this.m_table_old2new.put(var1, var2);
      }
   }

   public void removeMappings() {
      if (this.m_table_new2old != null) {
         this.m_table_new2old.clear();
      }

      if (this.m_table_old2new != null) {
         this.m_table_old2new.clear();
      }
   }

   public void removeNewNode(Node var1) {
      if (var1 != null && this.m_table_new2old.containsKey(var1)) {
         Node var2 = this.m_table_new2old.get(var1);
         this.m_table_new2old.remove(var1);
         if (this.m_table_old2new != null && var2 != null && this.m_table_old2new.containsKey(var2)) {
            this.m_table_old2new.remove(var2);
         }
      }
   }

   public void removeOldNode(Node var1) {
      Node[] var2 = this.m_table_new2old.keySet().toArray(new Node[this.m_table_new2old.size()]);

      for (Node var6 : var2) {
         if (this.m_table_new2old.get(var6) == var1) {
            this.m_table_new2old.remove(var6);
         }
      }

      if (this.m_table_old2new != null && var1 != null && this.m_table_old2new.containsKey(var1)) {
         this.m_table_old2new.remove(var1);
      }
   }

   @Override
   public String toString() {
      return sLocation[this.m_bIsForTarget ? 2 : 1];
   }

   private final class MouseWheelListenerImpl implements MouseWheelListener {
      private MouseWheelListenerImpl() {
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent var1) {
         int var2 = StagingPanel.m_textSize;
         if (var2 < 10) {
            var2 = 10;
         }

         int var3 = var1.getUnitsToScroll() * var2;
         MapArea_Migration.this.m_newScrollPane
            .getVerticalScrollBar()
            .setValue(MapArea_Migration.this.m_newScrollPane.getVerticalScrollBar().getValue() + var3);
         MapArea_Migration.this.m_oldScrollPane
            .getVerticalScrollBar()
            .setValue(MapArea_Migration.this.m_oldScrollPane.getVerticalScrollBar().getValue() + var3);
      }
   }
}
