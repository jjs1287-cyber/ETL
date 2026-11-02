package org.etlforge.integration.client.ui.deploy.option;

import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModelFactory;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.tree.check.CheckBoxTreeEvent;

public final class DeployTreePanel implements UiProvider, Disposable {
   private final IntegrationProject project;
   private final ValidationModel validationModel;
   private final DeployTree tree;
   private final JComponent panel;
   private final DeployTreePanel.ContainerImpl container;
   private final DependencyStore dependencies;

   public DeployTreePanel(IntegrationProject var1, ValidationModel var2) {
      this.project = var1;
      this.validationModel = var2;
      this.dependencies = var1.getCurrentDependencies();
      this.container = new DeployTreePanel.ContainerImpl();
      this.tree = this.createTree();
      this.panel = this.createTreePanel();
   }

   private DeployTree createTree() {
      IntegrationEntityTreeModel var1 = this.createTreeModel();
      return new DeployTree(this.container, this.validationModel, var1);
   }

   private IntegrationEntityTreeModel createTreeModel() {
      IntegrationEntityTreeModelFactory var1 = new IntegrationEntityTreeModelFactory();
      return var1.newModel(this.project);
   }

   private JComponent createTreePanel() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder();
      var1.center(this.tree);
      var1.withBorder(BorderFactory.createEtchedBorder());
      return var1.container();
   }

   @Override
   public void dispose() {
      this.dependencies.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   public void activate() {
      this.tree.activate();
   }

   public void repaint() {
      this.tree.repaint();
   }

   public IntegrationProject getProjectToDeploy() throws DeployTreeException {
      return this.tree.getProjectToDeploy();
   }

   public void refreshTree() {
      IntegrationEntityTreeModel var1 = this.createTreeModel();
      this.tree.setTreeModel(var1);
   }

   private class ContainerImpl implements DeployTreeContainer {
      private DeployTreeCheckHandler checkHandler;

      private ContainerImpl() {
      }

      @Override
      public void setTree(DeployTree var1) {
         this.checkHandler = new DeployTreeCheckHandler(var1, DeployTreePanel.this.project, DeployTreePanel.this.dependencies);
      }

      @Override
      public void nodeToggled(CheckBoxTreeEvent var1) {
         if (this.checkHandler == null) {
            throw new IllegalStateException();
         }

         this.checkHandler.nodeToggled(var1);
      }

      @Override
      public void overallCheckedStateChanged(CheckBoxTreeEvent var1) {
      }

      @Override
      public IntegrationProject createPartialProject(List<IntegrationEntity> var1) {
         return IntegrationProject.createPartialProject(DeployTreePanel.this.project, var1);
      }

      @Override
      public void openPage(IntegrationEntity var1) {
         UiCompanion.of(var1).openPage();
      }
   }
}
