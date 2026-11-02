package org.etlforge.integration.client.ui.deploy.option;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.deploy.DeployDataImpl;
import org.etlforge.integration.client.ui.deploy.DeployLauncher;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.option.OptionSelector;

public class DeployIndividualItemsOption implements DeployOption {
   private final DeployLauncher deployLauncher;
   private final ManagedProject managedProject;
   private final DeployTreePanel deployTree;
   private final TreePanelHolder deployTreeHolder;
   private volatile DeployDataImpl deployData;

   public DeployIndividualItemsOption(DeployTreePanel var1, ManagedProject var2, DeployLauncher var3) {
      this.managedProject = var2;
      this.deployLauncher = var3;
      this.deployTree = var1;
      this.deployTreeHolder = new TreePanelHolder(new DeployIndividualItemsOption.PreviewAction());
      var1.decorate(this.deployTreeHolder);
   }

   @Override
   public final String getId() {
      return "DeployOption.IndividualItems";
   }

   @Override
   public final DeployData getDeployData() {
      return this.deployData;
   }

   @Override
   public final boolean isAdvanced() {
      return true;
   }

   @Override
   public void setActive(boolean var1) {
      this.deployTreeHolder.setVisible(var1);
      if (var1) {
         this.deployTree.activate();
      }
   }

   @Override
   public final void addAdditionalUi(JComponent var1) {
      BorderLayoutBuilder.from(var1).center(this.deployTreeHolder).withEmptyBorder(0, 25, 0, 0);
   }

   @Override
   public final void decorate(OptionSelector var1) {
      var1.setText(Strings.get("Deploy.Individual.Name"));
   }

   @Override
   public final void execute(String var1) {
      IntegrationProject var2 = this.createPartialProject();
      if (var2 != null) {
         var2.markAsDeployDirty();
         this.deployData = new DeployDataImpl(this.managedProject, var2, true);
         this.deployData.setTag(var1);
         this.deployLauncher.startDeploy(this.deployData);
      }
   }

   private IntegrationProject createPartialProject() {
      try {
         return this.deployTree.getProjectToDeploy();
      } catch (DeployTreeException var2) {
         if (var2.isEmptySelection()) {
            Alert.error(var2.getMessage(), Strings.get("Deploy.Individual.EmptySelection"));
         } else {
            this.showInvalidItems(var2.getInvalidItems());
         }

         return null;
      }
   }

   @Override
   public final boolean requiresValidProject() {
      return false;
   }

   private void showInvalidItems(List<IntegrationEntity> var1) {
      JList var2 = ComponentFactories.listFactory().newList(var1);
      var2.setCellRenderer(RendererUtils.getIntegrationEntityListCellRenderer());
      var2.setVisibleRowCount(10);
      OneColumnPanel var3 = new OneColumnPanel();
      var3.withInsets(2, 5, 3, 5);
      String var4 = Strings.get("Deploy.Individual.InvalidItems.Short");
      var3.add(TextStyles.DefaultBold.makeLabel(var4));
      var3.add(new JLabel(Strings.get("Deploy.Individual.InvalidItems.Long")));
      var3.withInsets(0, 5, 5, 5);
      var3.add(new JScrollPane(var2));
      Alert.message(null, var3, var4, Alert.Type.ERROR);
   }

   private class PreviewAction extends AbstractAction {
      public PreviewAction() {
         super("&Preview...");
         this.putValue("ShortDescription", "View the items that have been selected for deploy");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               PreviewAction.this.openPreview();
            }
         });
      }

      private void openPreview() {
         IntegrationProject var1 = DeployIndividualItemsOption.this.createPartialProject();
         if (var1 != null) {
            DeployDataPreview var2 = new DeployDataPreview(var1);
            var2.show();
         }
      }
   }
}
