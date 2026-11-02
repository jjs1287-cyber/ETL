package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.etlforge.application.Application;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationNodeMenuProvider;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.util.name.NameSorter;

final class OperationNodeMenuProviderImpl implements OperationNodeMenuProvider {
   private final OperationGraphController controller;

   public OperationNodeMenuProviderImpl(OperationGraphController var1) {
      this.controller = var1;
   }

   @Override
   public JPopupMenu getScheduleMenuFor(OperationWrapperNode var1) {
      return new ScheduleMenuBuilder(this.controller).buildPopupMenu(var1);
   }

   public KongaPopupMenu getActionMenuFor(OperationWrapperNode var1) {
      KongaPopupMenu var2 = new KongaPopupMenu();
      this.provideActionItems(var1, var2);
      return var2;
   }

   @Override
   public JPopupMenu getHttpEndpointMenuFor(OperationWrapperNode var1) {
      List var2 = Iterables2.sortedCopy(var1.getDataObject().getHttpEndpointTriggers(), NameSorter.ignoreCase());
      if (var2.isEmpty()) {
         return null;
      }

      JPopupMenu var3 = new JPopupMenu();

      for (Object __etlItem50 : var2) {
         HttpEndpoint var5 = (HttpEndpoint) __etlItem50;
         var3.add(new OperationNodeMenuProviderImpl.OpenEntityAction(var5));
      }

      return var3;
   }

   @Override
   public JPopupMenu getSfdcWizardMenuFor(OperationWrapperNode var1) {
      Operation var2 = var1.getDataObject().getActivity();
      IntegrationEntityId var3 = var2.getSalesforceWizardGuid();
      IntegrationEntity var4 = this.controller.getEntityLookup().getEntity(var3);
      JPopupMenu var5 = new JPopupMenu();
      var5.add(new OperationNodeMenuProviderImpl.OpenEntityAction(var4));
      return var5;
   }

   public void provideActionItems(OperationWrapperNode var1, KongaPopupMenu var2) {
      if (var1 != this.controller.getGraph().getModel().getStartNode()) {
         var2.add(new OperationNodeMenuProviderImpl.OpenInSeparateGraphAction(var1));
      }

      var2.add(this.controller.getDeploySelectedItemsAction());
      var2.add(this.controller.getRunOperationAction());
      var2.add(new RunOperationWithSourceDataAction(this.controller.getRunnerFactory(), var1));
      var2.addSeparator();
      var2.add(new RenameNodeAction(this.controller, var1));
      if (var1.isOperationTypeChangeable()) {
         var2.add(new OperationTypeMenuBuilder(this.controller, var1).buildMenu());
      }

      var2.add(this.controller.getViewDependenciesAction());
      var2.add(new LocateInTreeAction(var1));
      var2.addSeparator();
      this.addSuccessFailureMenus(var1, var2);
      this.addScheduleMenu(var2, var1);
      if (var1.getPipeline().getType() == HostedWebServiceOperationType.TYPE) {
         this.addHostedWebServiceActions(var2, var1);
      }

      var2.addSeparator();
      this.addActivityActions(var2);
      var2.addSeparator();
      var2.add(this.controller.getSetPermissionsAction());
      this.addValidationMessagesAction(var2, var1);
      String var3 = "action.ic.Description";
      if (Application.containsBean(var3)) {
         var2.add(Application.getBean(var3, Action.class));
      }

      var2.add(new ShowOptionsAction(this.controller, var1));
      var2.add(this.buildLayoutMenu(var1));
      var2.addSeparator();
      var2.add(new DeleteNodeAction(this.controller, var1));
   }

   private JMenu buildLayoutMenu(OperationWrapperNode var1) {
      KongaMenu var2 = new KongaMenu("Layout");
      var2.addActions(new ResetOperationNodeLayoutAction(this.controller, var1), new SetNodeCoordinatesAction(this.controller, var1));
      return var2;
   }

   private void addSuccessFailureMenus(OperationWrapperNode var1, KongaPopupMenu var2) {
      OnSuccessMenuBuilder var3 = new OnSuccessMenuBuilder(this.controller, var1);
      var2.add(var3.buildMenu());
      OnFailureMenuBuilder var4 = new OnFailureMenuBuilder(this.controller, var1);
      var2.add(var4.buildMenu());
   }

   private void addScheduleMenu(KongaPopupMenu var1, OperationWrapperNode var2) {
      KongaMenu var3 = new ScheduleMenuBuilder(this.controller).buildMenu(var2);
      var1.add(var3);
   }

   private void addHostedWebServiceActions(KongaPopupMenu var1, OperationWrapperNode var2) {
      var1.addSeparator();
      this.addShowHostedWebServiceUrlAction(var1, var2);
      this.addSiblingsAction(var1, var2);
   }

   private void addShowHostedWebServiceUrlAction(KongaPopupMenu var1, OperationWrapperNode var2) {
      ViewHostedWebServiceUrlAction var3 = new ViewHostedWebServiceUrlAction(var2);
      var1.add(var3);
   }

   private void addSiblingsAction(KongaPopupMenu var1, OperationWrapperNode var2) {
      SetSiblingsAction var3 = new SetSiblingsAction(this.controller, var2);
      var1.add(var3);
   }

   private void addValidationMessagesAction(KongaPopupMenu var1, OperationWrapperNode var2) {
      ViewValidationMessagesAction var3 = new ViewValidationMessagesAction(this.controller.getGraph(), var2);
      var1.add(var3);
   }

   private void addActivityActions(JPopupMenu var1) {
      for (String var5 : new String[]{"Log", "History", "Queue"}) {
         Action var6 = Application.getBean("activity.action." + var5, Action.class);
         var1.add(var6);
      }
   }

   private class OpenEntityAction extends AbstractAction {
      private final IntegrationEntity entity;

      public OpenEntityAction(IntegrationEntity var2) {
         super(var2.getName(), IconStore.defaultIcon(var2));
         this.entity = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         OperationNodeMenuProviderImpl.this.controller.openEditor(this.entity);
      }
   }

   private class OpenInSeparateGraphAction extends AbstractAction {
      private final OperationWrapperNode opNode;

      public OpenInSeparateGraphAction(OperationWrapperNode var2) {
         super("Open In Separate Graph");
         this.opNode = var2;
         this.putValue("ShortDescription", "Open this operation in its own graph");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         OperationNodeMenuProviderImpl.this.controller.openEditor(this.opNode.getDataObject().getActivity());
      }
   }
}
