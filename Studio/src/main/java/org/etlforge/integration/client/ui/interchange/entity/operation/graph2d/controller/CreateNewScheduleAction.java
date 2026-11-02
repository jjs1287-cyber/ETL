package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Schedule;

class CreateNewScheduleAction extends AbstractAction {
   private final OperationGraphController controller;
   private final OperationWrapperNode node;

   public CreateNewScheduleAction(OperationGraphController var1, OperationWrapperNode var2) {
      super(Strings.get("Create.Schedule.Name"));
      this.node = var2;
      this.controller = var1;
      this.putValue("ShortDescription", Strings.get("Create.Schedule.ToolTip"));
      this.setEnabled(var2.getDataObject().isSchedulable());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.controller.isChangeAllowed(this.node)) {
         this.createSchedule();
      }
   }

   private void createSchedule() {
      this.controller.getEntityFactory().createEntity(EntityType.Schedule, new UiEntityFactoryCallback.Adapter() {
         @Override
         public void postProjectInsertion(IntegrationEntity var1) {
            CreateNewScheduleAction.this.node.getDataObject().setScheduleId(((Schedule)var1).getID());
         }
      });
   }
}
