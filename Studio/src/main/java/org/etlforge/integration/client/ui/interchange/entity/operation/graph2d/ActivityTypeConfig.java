package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Action;
import javax.swing.Icon;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.ContentProviderGate;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.CreateNewActivityContentAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.SelectExistingActivityContentAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.MutablePipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;

@ParametersAreNonnullByDefault
public abstract class ActivityTypeConfig {
   private final PipelineActivityType type;

   protected ActivityTypeConfig(PipelineActivityType var1) {
      this.type = Preconditions.checkNotNull(var1, "type");
   }

   public final PipelineActivityType getType() {
      return this.type;
   }

   public abstract PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2);

   public abstract EntityContentProvider<?> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3);

   public final List<? extends Action> getContextMenuActions(PipelineActivityNode var1, OperationGraphController var2, ContentProviderGate var3) {
      ArrayList var4 = Lists.newArrayList();
      EntityContentProvider var5 = this.getContentProvider(var2, var1.getParent().getDataObject(), var1.getDataObject());
      if (var5 != null) {
         List var6 = this.getContentProviderActions((MutablePipelineActivity)var1.getDataObject(), var5, var2, var3);
         var4.addAll(var6);
      }

      this.addToContextMenu(var1, var2, var4);
      return var4;
   }

   private List<Action> getContentProviderActions(
      MutablePipelineActivity var1, EntityContentProvider var2, OperationGraphController var3, ContentProviderGate var4
   ) {
      SelectExistingActivityContentAction var5 = new SelectExistingActivityContentAction(var4, var1, var2);
      CreateNewActivityContentAction var6 = new CreateNewActivityContentAction(var4, var1, var2);
      return Lists.newArrayList(var5, var6);
   }

   protected void addToContextMenu(PipelineActivityNode var1, OperationGraphController var2, List<Action> var3) {
   }

   public abstract Icon getSmallIcon();
}
