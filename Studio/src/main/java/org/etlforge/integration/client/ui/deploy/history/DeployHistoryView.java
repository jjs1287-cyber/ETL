package org.etlforge.integration.client.ui.deploy.history;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.deploy.history.DeployResultEntityFilter;
import org.etlforge.integration.client.deploy.history.DeployResultFilter;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;

public final class DeployHistoryView extends AbstractWindowSectionContent {
   public static final String CONTENT_ID = DeployHistoryView.class.getName();
   private final IntegrationProject project;
   private IntegrationEntity displayedEntity;
   private final DeployHistoryTable table;
   private final ViewDeployResultDetailsAction viewDetailsAction;
   private final DeleteSelectedDeployResultsAction deleteAction;
   private final DeployHistoryView.TitlePanel titlePanel;

   public DeployHistoryView(DeployHistory var1, WindowSection var2) {
      this(var1, null, var2);
   }

   public DeployHistoryView(DeployHistory var1, IntegrationEntity var2, WindowSection var3) {
      super(CONTENT_ID, var3);
      this.project = var1.getProject();
      this.displayedEntity = var2 != null ? var2 : this.project;
      this.viewDetailsAction = new ViewDeployResultDetailsAction(this.project);
      this.deleteAction = new DeleteSelectedDeployResultsAction(var1);
      this.table = this.createTable(var2);
      this.titlePanel = new DeployHistoryView.TitlePanel(this.displayedEntity);
      this.addResource(new DisposableWindowElementResource(this.table));
      this.table.setHistory(var1, false);
   }

   private DeployHistoryTable createTable(IntegrationEntity var1) {
      DeployHistoryTable var2 = new DeployHistoryTable(this.getFilter(var1));
      var2.setDefaultAction(this.viewDetailsAction);
      var2.installSelectionAction(this.deleteAction);
      return var2;
   }

   public void setIntegrationEntity(IntegrationEntity var1) {
      if (var1 != this.displayedEntity) {
         this.displayedEntity = var1;
         this.titlePanel.update(var1);
         this.table.setFilter(this.getFilter(var1));
      }
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder.from(var1).north(this.titlePanel.getUi()).center(this.table);
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
   }

   @Override
   public String getTitle() {
      return Strings.get("Deploy.History.View.Title");
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      var1.add(KongaButton.forToolbar(this.viewDetailsAction));
      super.populateToolBar(var1);
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.DEPLOY_16;
   }

   private DeployResultFilter getFilter(IntegrationEntity var1) {
      return var1 != null ? new DeployResultEntityFilter(var1) : DeployResultFilter.ACCEPTS_ALL;
   }

   public void setHistory(final DeployHistory var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DeployHistoryView.this.displayedEntity = var1.getProject();
            DeployHistoryView.this.titlePanel.update(DeployHistoryView.this.displayedEntity);
            DeployHistoryView.this.viewDetailsAction.setProject(var1.getProject());
            DeployHistoryView.this.deleteAction.setHistory(var1);
            DeployHistoryView.this.table.setHistory(var1, true);
         }
      });
   }

   private class TitlePanel {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder();
      private final JLabel entityLabel = new JLabel("");

      public TitlePanel(IntegrationEntity var2) {
         this.update(var2);
         this.doLayout();
         this.layout.withBorder(DeployHistoryView.getTitlePanelBorder());
         UiUtils.setPanelBackgrounds(this.layout.container(), DeployHistoryView.getTitlePanelColor());
      }

      public void update(IntegrationEntity var1) {
         if (var1 != null) {
            if (var1 instanceof IntegrationProject) {
               RendererUtils.decorateProjectRenderer(this.entityLabel, (IntegrationProject)var1);
            } else {
               RendererUtils.decorateRenderer(this.entityLabel, var1, false);
            }
         } else {
            this.entityLabel.setText("");
            this.entityLabel.setIcon(null);
         }
      }

      public JComponent getUi() {
         return this.layout.container();
      }

      private void doLayout() {
         BoxBuilder var1 = BoxBuilder.horizontal().addAllWithSpace(5, Strings.get("Deploy.History.View.SubTitle"), this.entityLabel);
         JToolBar var2 = DeployHistoryView.this.createToolBar();
         this.layout.west(var1).center(Box.createGlue()).east(var2);
      }
   }
}
