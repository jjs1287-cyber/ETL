package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import com.google.common.collect.Maps;
import java.awt.Rectangle;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityPosition;
import org.etlforge.util.persist.Persistor;

final class OperationNodeLayoutMemento {
   private static final String OPERATION_ID = "opId";
   private static final String COLLAPSED = "collapsed";
   private static final String OP_NODE_BOUNDS = "OpNodeBounds";
   private static final String ACTIVITY_NODE = "ActivityNode";
   private static final String POSITION = "pos";
   private final OperationId operationId;
   private final Rectangle opNodeBounds;
   private final boolean collapsed;
   private final Map<PipelineActivityPosition, Rectangle> activityBounds;

   public OperationNodeLayoutMemento(OperationNodeLayout var1) {
      this.operationId = var1.getOperationNode().getOperationId();
      this.collapsed = var1.isCollapsed();
      this.opNodeBounds = var1.getOperationNodeBounds();
      this.activityBounds = Maps.newHashMap();

      for (Entry var3 : var1.getActivityBounds()) {
         PipelineActivity var4 = ((PipelineActivityNode)var3.getKey()).getDataObject();
         PipelineActivityPosition var5 = var1.getPipeline().getPosition(var4);
         this.activityBounds.put(var5, new Rectangle((Rectangle)var3.getValue()));
      }
   }

   public OperationNodeLayoutMemento(Persistor var1) {
      this.operationId = new OperationId(var1.getString("opId"));
      this.collapsed = var1.getBoolean("collapsed");
      this.opNodeBounds = PersistUtils.readBounds(var1.getFirstChild("OpNodeBounds"));
      this.activityBounds = Maps.newHashMap();

      for (Persistor var3 : var1.getChildren("ActivityNode")) {
         PipelineActivityPosition var4 = PipelineActivityPosition.fromString(var3.getString("pos"));
         Rectangle var5 = PersistUtils.readBounds(var3);
         this.activityBounds.put(var4, var5);
      }
   }

   public OperationId getOperationId() {
      return this.operationId;
   }

   public boolean isCollapsed() {
      return this.collapsed;
   }

   public Rectangle getOperationNodeBounds() {
      return new Rectangle(this.opNodeBounds);
   }

   public int getNumberOfActivities() {
      return this.activityBounds.size();
   }

   public Rectangle getActivityBounds(PipelineActivityPosition var1) {
      Rectangle var2 = this.activityBounds.get(var1);
      return var2 != null ? new Rectangle(var2) : null;
   }

   public void writeTo(Persistor var1) {
      var1.putString("opId", this.operationId.toString());
      var1.putBoolean("collapsed", this.collapsed);
      PersistUtils.writeBounds(var1.createChild("OpNodeBounds"), this.opNodeBounds);

      for (Entry var3 : this.activityBounds.entrySet()) {
         Persistor var4 = var1.createChild("ActivityNode");
         var4.putString("pos", ((PipelineActivityPosition)var3.getKey()).toString());
         PersistUtils.writeBounds(var4, (Rectangle)var3.getValue());
      }
   }
}
