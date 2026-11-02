package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.Icon;
import javax.swing.JMenu;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.widget.menu.KongaMenu;

public final class OnSuccessMenuBuilder {
   private final OperationGraphController controller;
   private final OperationWrapperNode node;

   public OnSuccessMenuBuilder(OperationGraphController var1, OperationWrapperNode var2) {
      this.controller = var1;
      this.node = var2;
   }

   public final JMenu buildMenu() {
      JMenu var1 = new JMenu(Strings.get("OnSuccess.Menu.Name"));
      this.addToMenu(var1);
      return var1;
   }

   public final void addToMenu(JMenu var1) {
      var1.setIcon(GraphResources.SUCCESS_16);
      this.addOperationMenu(var1);
      var1.addSeparator();
      this.addEmailMenu(var1);
   }

   private void addOperationMenu(JMenu var1) {
      KongaMenu var2 = new KongaMenu("&Operation", getMenuIcon(EntityType.Operation));
      SuccessOperationSelector var3 = new SuccessOperationSelector(this.controller, this.node);
      var2.add(new OpenSuccessFailureSelectorDialogAction(var3));
      var2.add(new CreateNewOperationAction(this.controller, this.node, RouteType.SUCCESS));
      var2.addSeparator();
      var2.add(new RemoveSuccessOperationAction(this.controller, this.node));
      var1.add(var2);
   }

   private static Icon getMenuIcon(EntityType var0) {
      return EntityIcons.forType(var0).getDefaultIcon(EntityIcons.Size.SMALL);
   }

   private void addEmailMenu(JMenu var1) {
      KongaMenu var2 = new KongaMenu("&Email", getMenuIcon(EntityType.EmailMessage));
      SuccessEmailSelector var3 = new SuccessEmailSelector(this.controller, this.node);
      var2.add(new OpenSuccessFailureSelectorDialogAction(var3));
      var2.add(new CreateNewEmailAction(this.controller, this.node, RouteType.SUCCESS));
      var2.addSeparator();
      var2.add(new RemoveSuccessEmailAction(this.controller, this.node));
      var1.add(var2);
   }
}
