package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.operation.pipeline.BulkLoadActivity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

public final class BulkLoadNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   public BulkLoadNodeUi2D(Graph2D var1, BulkLoadActivityNode var2) {
      super(var1, var2);
      this.setContentString(this.getContentString());
      this.setToolTipText(this.getContentToolTip());
   }

   public BulkLoadActivity getActivity() {
      return (BulkLoadActivity)this.getNodeObject().getDataObject();
   }

   @Override
   protected Icon getIcon() {
      return EntityIcons.forType(EntityType.SalesforceBulkLoad).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   @Override
   protected String getContentString() {
      SalesforceBulkLoad var1 = this.getBulkLoad();
      return var1 == null ? "Bulk Load" : var1.getName();
   }

   private SalesforceBulkLoad getBulkLoad() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentToolTip() {
      SalesforceBulkLoad var1 = this.getBulkLoad();
      return var1 == null ? "Bulk Load" : ToolTipStringFactory.getToolTip(var1);
   }
}
