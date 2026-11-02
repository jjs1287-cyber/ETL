package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.Icon;
import javax.swing.JMenu;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.widget.menu.KongaMenu;

public final class OnFailureMenuBuilder {
   private final OperationGraphController controller;
   private final OperationWrapperNode node;

   public OnFailureMenuBuilder(OperationGraphController var1, OperationWrapperNode var2) {
      this.controller = var1;
      this.node = var2;
   }

   public final JMenu buildMenu() {
      JMenu var1 = new JMenu(Strings.get("OnFailure.Menu.Name"));
      this.addToMenu(var1);
      return var1;
   }

   public final void addToMenu(JMenu var1) {
      var1.setIcon(GraphResources.FAILURE_16);
      this.addOperationMenu(var1);
      var1.addSeparator();
      this.addEmailMenu(var1);
   }

   private void addOperationMenu(JMenu var1) {
      KongaMenu var2 = new KongaMenu("&Operation", getMenuIcon(EntityType.Operation));
      FailureOperationSelector var3 = new FailureOperationSelector(this.controller, this.node);
      var2.add(new OpenSuccessFailureSelectorDialogAction(var3));
      CreateNewOperationAction var4 = new CreateNewOperationAction(this.controller, this.node, RouteType.FAILURE);
      var2.add(var4);
      var2.add(new RemoveFailureOperationAction(this.controller, this.node));
      var1.add(var2);
   }

   private static Icon getMenuIcon(EntityType var0) {
      return EntityIcons.forType(var0).getDefaultIcon(EntityIcons.Size.SMALL);
   }

   private void addEmailMenu(JMenu var1) {
      KongaMenu var2 = new KongaMenu("&Email", getMenuIcon(EntityType.EmailMessage));
      FailureEmailSelector var3 = new FailureEmailSelector(this.controller, this.node);
      var2.add(new OpenSuccessFailureSelectorDialogAction(var3));
      CreateNewEmailAction var4 = new CreateNewEmailAction(this.controller, this.node, RouteType.FAILURE);
      var2.add(var4);
      var2.add(new RemoveFailureEmailAction(this.controller, this.node));
      var1.add(var2);
   }
}
