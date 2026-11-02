package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityPosition;
import org.etlforge.ui.DimensionUtils;
import org.etlforge.ui.graph2d.RelativePosition;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.graphics.RectanglePredicates;
import org.etlforge.ui.graphics.RectangleUtils;

class OperationNodeLayout {
   private final int START_X = 40;
   private final int START_Y = 80;
   private final int HORIZONTAL_GAP = 50;
   private final OperationGraphLayout graphLayout;
   private final OperationWrapperNode opNode;
   private final OperationPipeline pipeline;
   private boolean collapsed;
   private Rectangle opNodeBounds;
   private final Map<PipelineActivityNode, Rectangle> activityBounds;

   public OperationNodeLayout(OperationGraphLayout var1, OperationWrapperNode var2) {
      this(var1, var2, null, (OperationNodeLayoutMemento)null);
   }

   public OperationNodeLayout(OperationGraphLayout var1, OperationWrapperNode var2, Object var3, OperationNodeLayoutCache var4) {
      this(var1, var2, var3, var4.get(var2.getOperationId(), var2.getPipeline()));
   }

   public OperationNodeLayout(OperationGraphLayout var1, OperationWrapperNode var2, Object var3, OperationNodeLayoutMemento var4) {
      this.graphLayout = var1;
      this.opNode = var2;
      this.pipeline = var2.getPipeline();
      this.activityBounds = Maps.newIdentityHashMap();
      if (var4 != null) {
         this.restoreFromMemento(var4);
      } else {
         this.doDefaultLayout(var3);
      }

      this.collapsed = var4 != null ? var4.isCollapsed() : this.askGraphIfCollapsed();
   }

   public void reset() {
      Point var1 = this.opNodeBounds.getLocation();
      this.doDefaultLayout(null);
      this.opNodeBounds.setLocation(var1);
   }

   public OperationWrapperNode getOperationNode() {
      return this.opNode;
   }

   public boolean isCollapsed() {
      return this.collapsed;
   }

   public OperationPipeline getPipeline() {
      return this.pipeline;
   }

   public Rectangle getOperationNodeBounds() {
      return new Rectangle(this.opNodeBounds);
   }

   public void setOperationNodeLocation(Point var1) {
      this.opNodeBounds.setLocation(var1);
   }

   public Collection<Entry<PipelineActivityNode, Rectangle>> getActivityBounds() {
      return this.activityBounds.entrySet();
   }

   public void reportTo(StringBuilder var1) {
      var1.append("Operation: ").append(this.opNode);
      var1.append("\nActivitys:\n");

      for (Entry var3 : this.activityBounds.entrySet()) {
         var1.append("  ").append(var3.getKey()).append(": ").append(var3.getValue()).append("\n");
      }

      var1.append("\n");
   }

   public boolean contains(PipelineActivityNode var1) {
      return this.activityBounds.containsKey(var1);
   }

   public void expansionStateChanged() {
      this.collapsed = this.askGraphIfCollapsed();
      this.updateOpNodeSize();
   }

   public Rectangle getBounds(Object var1) {
      if (var1 == this.opNode) {
         return this.opNodeBounds;
      }

      if (var1 instanceof PipelineActivityNode) {
         PipelineActivityNode var2 = (PipelineActivityNode)var1;
         if (var2.getParent() == this.opNode) {
            Rectangle var3 = this.activityBounds.get(var2);
            return RectangleUtils.translate(var3, this.opNodeBounds.x, this.opNodeBounds.y);
         }
      }

      return null;
   }

   public void collect(Point var1, List<Object> var2) {
      if (this.opNodeBounds.contains(var1)) {
         var2.add(this.opNode);
         if (this.graphLayout.isExpanded(this.opNode)) {
            Point var3 = new Point(var1);
            var3.translate(-this.opNodeBounds.x, -this.opNodeBounds.y);
            Map var4 = Maps.filterValues(this.activityBounds, RectanglePredicates.contains(var3));
            var2.addAll(var4.keySet());
         }
      }
   }

   public void collect(Rectangle var1, List<Object> var2) {
      if (this.opNodeBounds.intersects(var1)) {
         var2.add(this.opNode);
         if (this.graphLayout.isExpanded(this.opNode)) {
            Rectangle var3 = RectangleUtils.translate(var1, -this.opNodeBounds.x, -this.opNodeBounds.y);
            Map var4 = Maps.filterValues(this.activityBounds, RectanglePredicates.intersects(var3));
            var2.addAll(var4.keySet());
         }
      }
   }

   public void moveBy(Object var1, Point var2) {
      this.moveByImpl(var1, var2, true);
   }

   private void moveByImpl(Object var1, Point var2, boolean var3) {
      if (var1 == this.opNode) {
         this.moveOperationNodeBy(var2);
      } else if (this.activityBounds.containsKey(var1)) {
         this.moveActivityNodeBy((PipelineActivityNode)var1, var2, var3);
      }
   }

   public void moveTo(Point var1) {
      Point var2 = this.opNodeBounds.getLocation();
      if (!var2.equals(var1)) {
         this.opNodeBounds.setLocation(var1);
         this.graphLayout.fireLayoutChanged(this.opNode);
      }
   }

   public void remove(List<Object> var1) {
      Point var2 = this.getMinNodePos();

      for (Object var4 : var1) {
         this.activityBounds.remove(var4);
      }

      Point var5 = this.getMinNodePos();
      this.updateNodePositions(new Point(var2.x - var5.x, var2.y - var5.y));
      this.updateOpNodeSize();
   }

   public void resize(Object var1, Dimension var2) {
      if (var1 == this.opNode) {
         this.resizeOperationNode(var2);
      } else {
         this.activityNodeSizeChanged(var1, var2);
      }
   }

   public void setNewSize(Object var1, Dimension var2) {
      if (var1 == this.opNode) {
         this.opNodeBounds.setSize(var2);
         this.graphLayout.fireLayoutChanged(this.opNode);
      } else {
         this.activityNodeSizeChanged(var1, var2);
      }
   }

   private void resizeOperationNode(Dimension var1) {
      new OperationNodeResizer(this.graphLayout, this.opNode, this.opNodeBounds, this.activityBounds.values()).resize(var1);
   }

   private void activityNodeSizeChanged(Object var1, Dimension var2) {
      Rectangle var3 = this.activityBounds.get(var1);
      if (var3 != null) {
         var3.setSize(var2);
         this.updateOpNodeSize();
         this.graphLayout.fireLayoutChanged(Arrays.asList(this.opNode, var1));
      }
   }

   public void insertPipelineActivityNodes(Object var1, Object var2, List<Object> var3) {
      assert var3.size() == 1 : "Only one activity node can be inserted at the time";
      OperationNodeLayout.ActivityNodeInserter var4 = new OperationNodeLayout.ActivityNodeInserter(
         (PipelineActivityNode)var1, (PipelineActivityNode)var2, (PipelineActivityNode)var3.get(0)
      );
      var4.insert();
   }

   private boolean askGraphIfCollapsed() {
      return this.graphLayout.isCollapsed(this.opNode);
   }

   private Point getMinNodePos() {
      int var1 = Integer.MAX_VALUE;
      int var2 = Integer.MAX_VALUE;

      for (Rectangle var4 : this.activityBounds.values()) {
         if (var4.x < var1) {
            var1 = var4.x;
         }

         if (var4.y < var2) {
            var2 = var4.y;
         }
      }

      return new Point(var1, var2);
   }

   private void updateNodePositions(Point var1) {
      for (Object var3 : this.activityBounds.keySet()) {
         this.moveByImpl(var3, var1, false);
      }

      this.updateOpNodeSize();
      this.updateOpNodeLocation();
      this.graphLayout.fireLayoutChanged(this.getAllNodes());
   }

   private void updateOpNodeSize() {
      this.opNodeBounds.setSize(this.calculateOpNodeSize());
   }

   private void moveOperationNodeBy(Point var1) {
      Point var2 = new Point(this.opNodeBounds.x + var1.x, this.opNodeBounds.y + var1.y);
      var2.x = Math.max(var2.x, 5);
      var2.y = Math.max(var2.y, 5);
      if (!this.opNodeBounds.getLocation().equals(var2)) {
         this.opNodeBounds.setLocation(var2);
         if (this.graphLayout.isCollapsed(this.opNode)) {
            this.graphLayout.fireLayoutChanged(this.opNode);
         } else {
            this.graphLayout.fireLayoutChanged(this.getAllNodes());
         }
      }
   }

   private void moveActivityNodeBy(PipelineActivityNode var1, Point var2, boolean var3) {
      Rectangle var4 = this.activityBounds.get(var1);
      Point var5 = new Point(var4.x + var2.x, var4.y + var2.y);
      if (var5.x < 0) {
         this.adjustForNegativeX(var1, var5);
      }

      if (var5.y < 0) {
         this.adjustForNegativeY(var1, var5);
      }

      var4.setLocation(var5);
      if (var3) {
         this.updateOpNodeSize();
         this.updateOpNodeLocation();
         this.graphLayout.fireLayoutChanged(this.getAllNodes());
      }
   }

   private void adjustForNegativeX(PipelineActivityNode var1, Point var2) {
      if (this.opNodeBounds.x > 0) {
         int var3 = -var2.x;
         this.opNodeBounds.x = Math.max(this.opNodeBounds.x - var3, 0);

         for (Entry var5 : this.activityBounds.entrySet()) {
            if (var5.getKey() != var1) {
               ((Rectangle)var5.getValue()).x += var3;
            }
         }
      }

      var2.x = 0;
   }

   private void adjustForNegativeY(PipelineActivityNode var1, Point var2) {
      if (this.opNodeBounds.y > 0) {
         int var3 = -var2.y;
         this.opNodeBounds.y = Math.max(this.opNodeBounds.y - var3, 0);

         for (Entry var5 : this.activityBounds.entrySet()) {
            if (var5.getKey() != var1) {
               ((Rectangle)var5.getValue()).y += var3;
            }
         }
      }

      var2.y = 0;
   }

   private void updateOpNodeLocation() {
      int var1 = this.getMinimumActivityX() - 40;
      if (var1 > 0) {
         this.opNodeBounds.x += var1;

         for (Rectangle var3 : this.activityBounds.values()) {
            var3.x -= var1;
         }
      }

      int var5 = this.getMinimumActivityY() - 80;
      if (var5 > 0) {
         this.opNodeBounds.y += var5;

         for (Rectangle var4 : this.activityBounds.values()) {
            var4.y -= var5;
         }
      }
   }

   private int getMinimumActivityX() {
      int var1 = Integer.MAX_VALUE;

      for (Rectangle var3 : this.activityBounds.values()) {
         int var4 = var3.x;
         if (var4 < var1) {
            var1 = var4;
         }
      }

      return var1;
   }

   private int getMinimumActivityY() {
      int var1 = Integer.MAX_VALUE;

      for (Rectangle var3 : this.activityBounds.values()) {
         int var4 = var3.y;
         if (var4 < var1) {
            var1 = var4;
         }
      }

      return var1;
   }

   private List<Object> getAllNodes() {
      ArrayList var1 = new ArrayList<>(this.activityBounds.keySet());
      var1.add(this.opNode);
      return var1;
   }

   private void restoreFromMemento(OperationNodeLayoutMemento var1) {
      if (var1.getNumberOfActivities() == this.opNode.getChildCount() && this.tryToRestoreActivityNodesFromMemento(var1)) {
         this.opNodeBounds = var1.getOperationNodeBounds();
      } else {
         this.doDefaultLayout(null);
         this.opNodeBounds.setLocation(var1.getOperationNodeBounds().getLocation());
      }
   }

   private boolean tryToRestoreActivityNodesFromMemento(OperationNodeLayoutMemento var1) {
      for (PipelineActivity var3 : this.pipeline.getActivities()) {
         PipelineActivityPosition var4 = this.pipeline.getPosition(var3);
         Rectangle var5 = var1.getActivityBounds(var4);
         if (var5 == null) {
            return false;
         }

         PipelineActivityNode var6 = this.opNode.getChildFor(var3);
         assert var6 != null;
         this.activityBounds.put(var6, var5);
      }

      return true;
   }

   private void doDefaultLayout(Object var1) {
      this.layoutActivities();
      Dimension var2 = this.calculateOpNodeSize();
      Point var3 = this.getNewOpNodeLocation(var2, var1);
      this.opNodeBounds = new Rectangle(var3.x, var3.y, var2.width, var2.height);
   }

   private Point getNewOpNodeLocation(Dimension var1, Object var2) {
      if (var2 != null) {
         Rectangle var3 = this.graphLayout.getBounds(var2);
         if (var3 != null) {
            return new Point(var3.x + 40, var3.y + var3.height + 40);
         }
      }

      byte var15 = 10;
      byte var4 = 10;
      double var5 = Math.PI / 4;
      double var7 = 0.1308996938995747;
      byte var9 = 0;

      while (true) {
         for (double var10 = 0.0; var10 <= var5; var10 += var7) {
            int var12 = var15 + (int)(Math.cos(var10) * var9);
            int var13 = var4 + (int)(Math.sin(var10) * var9);
            Rectangle var14 = new Rectangle(var12, var13, var1.width, var1.height);
            if (this.graphLayout.getNodesWithin(var14).isEmpty()) {
               return new Point(var12, var13);
            }
         }

         var9 += 40;
      }
   }

   private void layoutActivities() {
      int var1 = 40;

      for (PipelineActivityNode var3 : this.opNode.getChildren()) {
         Dimension var4 = this.graphLayout.getGraph().getNodeUi(var3).getPreferredSize();
         int var5 = var4.height % 2 == 0 ? var4.height : var4.height + 1;
         int var6 = 80 - var5 / 2;
         Rectangle var7 = new Rectangle(new Point(var1, var6), var4);
         this.activityBounds.put(var3, var7);
         var1 += var7.width + 50;
      }
   }

   private Dimension calculateOpNodeSize() {
      if (!this.graphLayout.isExpanded(this.opNode)) {
         return this.graphLayout.getGraph().getNodeUi(this.opNode).getPreferredSize();
      }

      int var1 = 0;
      int var2 = 0;

      for (Rectangle var4 : this.activityBounds.values()) {
         var1 = Math.max(var1, var4.x + var4.width);
         var2 = Math.max(var2, var4.y + var4.height);
      }

      Rectangle var5 = new Rectangle(var1 + 30, var2);
      this.checkOperationNodeBoundingBoxMinimumSize(var5);
      return var5.getSize();
   }

   private void checkOperationNodeBoundingBoxMinimumSize(Rectangle var1) {
      var1.y -= 30;
      var1.height += 30;
      GraphNodeUi2D var2 = this.graphLayout.getGraph().getNodeUi(this.opNode);
      Dimension var3 = var2.getMinimumSize();
      var1.width = Math.max(var1.width, var3.width);
      var1.height = Math.max(var1.height, var3.height);
   }

   private final class ActivityNodeInserter {
      private final PipelineActivityNode insertedNode;
      private final Rectangle startBounds;
      private final Rectangle endBounds;
      private final OperationWrapperNode opNode;
      private int shift;

      public ActivityNodeInserter(PipelineActivityNode var2, PipelineActivityNode var3, PipelineActivityNode var4) {
         this.insertedNode = var4;
         this.startBounds = OperationNodeLayout.this.activityBounds.get(var2);
         this.endBounds = OperationNodeLayout.this.activityBounds.get(var3);
         this.opNode = var4.getParent();
      }

      public void insert() {
         OperationNodeLayout.this.graphLayout.getZOrderList().moveNodeToTop(this.opNode, this.insertedNode);
         Rectangle var1 = this.applyNewBounds();
         this.shiftNodes(var1);
         OperationNodeLayout.this.updateOpNodeSize();
         OperationNodeLayout.this.graphLayout.fireLayoutChanged();
      }

      private Rectangle applyNewBounds() {
         GraphNodeUi2D var1 = OperationNodeLayout.this.graphLayout.getGraph().getNodeUi(this.insertedNode);
         Dimension var2 = DimensionUtils.max(var1.getSize(), var1.getMinimumSize());
         int var3 = this.getX(this.startBounds);
         int var4 = this.getY(this.endBounds, this.endBounds, var2);
         Rectangle var5 = new Rectangle(var3, var4, var2.width, var2.height);
         this.shift = this.shift + var2.width;
         OperationNodeLayout.this.activityBounds.put(this.insertedNode, var5);
         var1.setSize(var5.getSize());
         return var5;
      }

      private Map<PipelineActivityNode, Rectangle> getNodesToShift(Rectangle var1) {
         IdentityHashMap var2 = Maps.newIdentityHashMap();

         for (PipelineActivityNode var4 : this.opNode.getChildren()) {
            if (var4 != this.insertedNode) {
               Rectangle var5 = OperationNodeLayout.this.activityBounds.get(var4);
               if (this.shouldNodeBeShifted(var1, var5)) {
                  var2.put(var4, new Rectangle(var5));
               }
            }
         }

         return var2;
      }

      private boolean shouldNodeBeShifted(Rectangle var1, Rectangle var2) {
         return this.startBounds == null || RelativePosition.RIGHT.isTrue(var2, this.startBounds) && this.intersectsVertically(var2, var1);
      }

      private boolean intersectsVertically(Rectangle var1, Rectangle var2) {
         if (var1.y < var2.y) {
            return var1.y + var1.height > var2.y;
         } else {
            return var1.y > var2.y ? var2.y + var2.height > var1.y : true;
         }
      }

      private void shiftNodes(Rectangle var1) {
         for (Entry var3 : this.getNodesToShift(var1).entrySet()) {
            Rectangle var10000 = (Rectangle)var3.getValue();
            var10000.x = var10000.x + this.shift;
            OperationNodeLayout.this.activityBounds.put((PipelineActivityNode)var3.getKey(), (Rectangle)var3.getValue());
         }
      }

      private int getX(Rectangle var1) {
         if (var1 != null) {
            byte var2 = 50;
            this.shift += var2;
            return var1.x + var1.width + var2;
         } else {
            this.shift += 5;
            return 5;
         }
      }

      private int getY(Rectangle var1, Rectangle var2, Dimension var3) {
         if (var1 == null && var2 == null) {
            return 5;
         }

         int var4 = 0;
         if (var1 != null && var2 != null) {
            int var5 = this.getMidY(var1);
            int var6 = this.getMidY(var2);
            var4 = var5 + (var6 - var5);
         } else if (var1 != null) {
            var4 = this.getMidY(var1);
         } else if (var2 != null) {
            var4 = this.getMidY(var2);
         }

         return var4 - var3.height / 2;
      }

      private int getMidY(Rectangle var1) {
         return var1.y + var1.height / 2;
      }
   }
}
