package org.etlforge.integration.client.ui.deploy.history;

import javax.swing.JTextArea;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.deploy.DeployedEntityDescriptor;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.DeployResultVisitor;
import org.etlforge.integration.client.ui.deploy.DeployWarningsPresenter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.ui.widget.tree.KongaTree;

public abstract class DeployResultDetailsViewer implements UiProvider {
   protected final DeployResultBasicDetailsDisplayer basicDetails;
   protected final DeployedEntityDescriptorTree dataTree;

   protected DeployResultDetailsViewer(DeployResult var1) {
      this.basicDetails = new DeployResultBasicDetailsDisplayer(var1);
      this.dataTree = new DeployedEntityDescriptorTree(var1.getDeployedDataRoot(), "Deployed items:");
      this.dataTree.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      this.dataTree.expandAll();
      this.dataTree.setVisibleRowCount(16);
   }

   public static DeployResultDetailsViewer create(DeployResult var0, IntegrationProject var1) {
      DeployResultDetailsViewer.Factory var2 = new DeployResultDetailsViewer.Factory(var1);
      return var2.visit(var0);
   }

   private static class Factory extends DeployResultVisitor<DeployResultDetailsViewer> {
      private final IntegrationProject project;

      public Factory(IntegrationProject var1) {
         this.project = var1;
      }

      protected DeployResultDetailsViewer onFailure(DeployResult var1) {
         return new DeployResultDetailsViewer.FailureViewer(var1);
      }

      protected DeployResultDetailsViewer onWarnings(DeployResult var1) {
         return new DeployResultDetailsViewer.WarningsViewer(var1, this.project);
      }

      protected DeployResultDetailsViewer onSuccess(DeployResult var1) {
         return new DeployResultDetailsViewer.SuccessViewer(var1);
      }
   }

   private static final class FailureViewer extends DeployResultDetailsViewer {
      private final JTextArea messageArea;

      public FailureViewer(DeployResult var1) {
         super(var1);
         this.messageArea = this.createMessageArea(var1);
      }

      private JTextArea createMessageArea(DeployResult var1) {
         KongaTextArea var2 = KongaTextArea.viewer(var1.getCause().getMessage(), Size.rows(10), Size.columns(50));
         TextStyles.DefaultErrorText.makeOver(var2);
         return var2;
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
         var2.north(this.basicDetails).center(this.dataTree).south(this.layoutMessageArea());
         var2.decorate(var1);
      }

      private UiProvider layoutMessageArea() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 3);
         return var1.north("Reported error:").center(this.messageArea);
      }

      @Override
      public void requestFocus() {
         this.dataTree.requestFocus();
      }
   }

   private static final class SuccessViewer extends DeployResultDetailsViewer {
      public SuccessViewer(DeployResult var1) {
         super(var1);
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
         var2.north(this.basicDetails).center(this.dataTree);
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.dataTree.requestFocus();
      }
   }

   private static final class WarningsViewer extends DeployResultDetailsViewer implements SelectionListener {
      private final DeployWarningsPresenter warningsUi;

      public WarningsViewer(DeployResult var1, IntegrationProject var2) {
         super(var1);
         this.warningsUi = new DeployWarningsPresenter(var1, var2.getItemLookup());
         this.dataTree.addSelectionListener(this);
      }

      @Override
      public void decorate(UiCanvas var1) {
         TwoColumnPanel var2 = new TwoColumnPanel()
            .withLeftFill(GridBagLayoutPanel.Fill.BOTH)
            .withRightFill(GridBagLayoutPanel.Fill.BOTH)
            .withLeftWeights(0.6, 1.0)
            .withRightWeights(0.4, 1.0)
            .withLeftInsets(0, 0, 0, 10)
            .addRow(this.warningsUi, this.dataTree);
         new BorderLayoutBuilder(0, 10).north(this.basicDetails).center(var2).decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.warningsUi.requestFocus();
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         OrderedSelection var2 = (OrderedSelection)var1.getSelection();
         if (!var2.isEmpty()) {
            DeployedEntityDescriptor var3 = (DeployedEntityDescriptor)var2.head();
            this.warningsUi.setSelectedEntity(var3 != null ? var3.getId() : null);
         }
      }
   }
}
