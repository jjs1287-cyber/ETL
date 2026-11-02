package org.etlforge.integration.client.ui.deploy.page;

import com.google.common.collect.Lists;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.etlforge.application.Application;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.widget.ApplicationToolBar;
import org.etlforge.application.ui.widget.TextAreaInput;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.DefaultEditorController;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorController;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.deploy.DeployLauncher;
import org.etlforge.integration.client.ui.deploy.option.DeployAllValidItemsOption;
import org.etlforge.integration.client.ui.deploy.option.DeployDirtyItemsOption;
import org.etlforge.integration.client.ui.deploy.option.DeployEverythingOption;
import org.etlforge.integration.client.ui.deploy.option.DeployIndividualItemsOption;
import org.etlforge.integration.client.ui.deploy.option.DeployOption;
import org.etlforge.integration.client.ui.deploy.option.DeployOptionPreference;
import org.etlforge.integration.client.ui.deploy.option.DeployTreePanel;
import org.etlforge.integration.client.ui.deploy.resources.DeployResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.widget.button.KongaButton;

public final class DeployPage extends GenericEditor {
   private final ManagedProject project;
   private final DeployLauncher launcher;
   private final DeployTreePanel deployTree;
   private final JPanel mainPanel;
   private JPanel waitPanel;
   private ReportPanel reportPanel;
   private DeployControlPanel controlPanel;
   private final DeployOption[] options;
   private StatusUpdater validationTimer;
   private final Action refreshAction;
   private final Action deployAction;
   private final Action enterTagAction;
   private Timer publishWaitMessageTimer;
   private String tag;
   private DeployOption preferredOption;

   public DeployPage(String var1, ManagedProject var2, DeployLauncher var3) {
      super(var1, Strings.get("Deploy.Page.Title"), var2.getProject().getName());
      this.project = var2;
      this.launcher = var3;
      this.deployTree = new DeployTreePanel(var2.getProject(), var2.getValidationModel());
      this.options = new DeployOption[]{
         new DeployEverythingOption(var2.getProject(), var3),
         new DeployDirtyItemsOption(var2.getProject(), var3),
         new DeployAllValidItemsOption(var2, var3),
         this.getIndividualItemsOption(var3)
      };
      this.setIcon(ClientIcons.DEPLOY_16);
      this.setCategory(new EditorCategory("Deploy", this.getSmallIcon()));
      this.setMementoPath(new String[]{"Project", "Deploy"});
      this.mainPanel = new JPanel(new CardLayout());
      this.mainPanel.setOpaque(false);
      this.refreshAction = new DeployPage.RefreshAction();
      this.enterTagAction = new DeployPage.EnterTagAction();
      this.deployAction = new DeployPage.DeployAction();
      this.createToolBarButtons();
   }

   @Override
   public Object getObject() {
      return this.launcher;
   }

   public DeployData getDeployData() {
      DeployOption var1 = this.controlPanel.getActiveOption();
      return var1 != null ? var1.getDeployData() : null;
   }

   public void refresh() {
      ActionUtils.run(this.refreshAction, this, "refresh");
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.createComponents(true);
      this.layoutPageAndStartValidation();
      this.setController(this.createController());
   }

   private EditorController createController() {
      DefaultEditorController var1 = new DefaultEditorController();
      var1.registerAction(KeyUtils.ENTER, this.deployAction);
      return var1;
   }

   private void layoutPageAndStartValidation() {
      this.layoutWaitPanel();
      this.setMainPanel(this.mainPanel);
      this.setToolBar(this.createToolBar());
      this.startWaitTimer();
      this.startValidation();
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      boolean var2 = super.removeFromWindow(var1);
      if (var2) {
         this.deployTree.dispose();
      }

      return var2;
   }

   @Override
   public void requestFocus() {
      this.controlPanel.requestFocus();
   }

   private void createComponents(boolean var1) {
      this.createControlPanel();
      if (var1) {
         this.createTitlePanel();
      }

      this.reportPanel = new ReportPanel(this.getOverlayContainer());
   }

   private void createControlPanel() {
      this.controlPanel = new DeployControlPanel(this.options, this.getPreferredOption(this.options));
   }

   private DeployOption getIndividualItemsOption(DeployLauncher var1) {
      return new DeployIndividualItemsOption(this.deployTree, this.project, var1) {
         @Override
         public void setActive(boolean var1) {
            super.setActive(var1);
            JComponent var2 = DeployPage.this.getMainPanel();
            if (var2 != null) {
               var2.revalidate();
               var2.repaint();
            }
         }
      };
   }

   private DeployOption getPreferredOption(DeployOption[] var1) {
      if (this.preferredOption == null) {
         this.preferredOption = this.getOptionPreference().read(var1);
         if (this.preferredOption == null) {
            this.preferredOption = var1[0];
         }
      }

      return this.preferredOption;
   }

   private DeployOptionPreference getOptionPreference() {
      return new DeployOptionPreference(this.project);
   }

   private void createToolBarButtons() {
      ArrayList var1 = Lists.newArrayList(
         newToolBarButton(this.refreshAction), newToolBarButton(this.enterTagAction), null, newToolBarButton(this.deployAction), null
      );
      this.setToolbarButtons(var1);
   }

   private static JButton newToolBarButton(Action var0) {
      KongaButton var1 = new KongaButton(var0);
      ApplicationToolBar.configureTextOnlyButton(var1, true);
      var1.setFont(Fonts.bold(var1));
      return var1;
   }

   private void layoutWaitPanel() {
      this.waitPanel = new JPanel(new FlowLayout(3));
      this.waitPanel.add(this.reportPanel.getDisplayer());
   }

   private void layoutMainPanel(boolean var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.setWeightX(1.0);
      var2.add(this.controlPanel);
      this.layoutButtonPanel(var2);
      this.mainPanel.add(this.createContentBox(var2), "CONTROL");
      ((CardLayout)this.mainPanel.getLayout()).show(this.mainPanel, "CONTROL");
   }

   private void layoutButtonPanel(OneColumnPanel var1) {
      var1.withInsets(15, 0, 0, 0);
      var1.setFillNone();
      var1.setWeightX(0.0);
      var1.setWeightY(1.0);
      Grid var2 = Grid.oneRow(20).nonOpaque();
      var2.addAll(this.enterTagAction, this.deployAction);
      var1.add(var2);
   }

   private JComponent createContentBox(OneColumnPanel var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      var2.setOpaque(false);
      var1.nonOpaque().withBorder(Empty.border(10, 15, 10, 15));
      return var2;
   }

   private void onValidProject() {
      this.reportPanel.onValidProject();
      this.controlPanel.onValidProject();
   }

   private void onInvalidProject() {
      this.reportPanel.onInvalidProject();
      this.controlPanel.onInvalidProject();
      this.deployTree.repaint();
   }

   private void enableActions() {
      this.deployAction.setEnabled(true);
      this.enterTagAction.setEnabled(true);
      this.refreshAction.setEnabled(true);
   }

   private void startValidation() {
      final WaitLock var1 = this.getWindow().startWait();
      Runnable var2 = new Runnable() {
         @Override
         public void run() {
            final boolean var1x = DeployPage.this.validateImpl();
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  DeployPage.this.handleValidationResult(var1x);
                  var1.release();
               }
            });
         }
      };
      Application.getWorker().submitForParallel(var2);
   }

   private boolean validateImpl() {
      this.validationTimer = this.reportPanel.getValidationTimer();
      this.validationTimer.start();
      ValidationStatus var1 = this.project.getValidationModel().validateProject();
      return var1 != ValidationStatus.INVALID;
   }

   private void handleValidationResult(boolean var1) {
      this.validationTimer.stop();
      this.publishWaitMessageTimer.stop();
      if (var1) {
         this.onValidProject();
      } else {
         this.onInvalidProject();
      }

      this.layoutMainPanel(var1);
      this.enableActions();
   }

   private void startWaitTimer() {
      this.publishWaitMessageTimer = new Timer(150, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            DeployPage.this.mainPanel.add(DeployPage.this.reportPanel.getDisplayer(), "REPORT");
         }
      });
      this.publishWaitMessageTimer.setRepeats(false);
      this.publishWaitMessageTimer.start();
   }

   private final class DeployAction extends AbstractAction {
      public DeployAction() {
         super(Strings.get("Deploy.Page.DeployAction.Name"), DeployPage.this.getSmallIcon());
         this.putValue("ShortDescription", Strings.get("Deploy.Page.DeployAction.ToolTip"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DeployOption var1x = DeployPage.this.controlPanel.getActiveOption();
               if (var1x != null) {
                  DeployAction.this.storeSelectedOptionInProjectPreferences(var1x);
                  var1x.execute(DeployPage.this.tag);
               }
            }
         });
      }

      private void storeSelectedOptionInProjectPreferences(DeployOption var1) {
         DeployPage.this.getOptionPreference().write(var1);
      }
   }

   private final class EnterTagAction extends AbstractAction {
      public EnterTagAction() {
         super(Strings.get("Deploy.Page.Tag"), DeployResources.ICONS.getIcon("DeployTag.16"));
         this.putValue("ShortDescription", Strings.get("Deploy.Page.Tag.ToolTip"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TextAreaInput var2 = new TextAreaInput(Strings.get("Deploy.Page.Tag.Title"), new Receiver<String>() {
            public void handle(String var1) {
               if (var1 != null) {
                  DeployPage.this.tag = var1.trim();
               }
            }
         });
         var2.setCaption(Strings.get("Deploy.Page.Tag.Caption"));
         var2.setOverlayContainer(DeployPage.this.getOverlayContainer());
         var2.show(DeployPage.this.tag);
      }
   }

   private final class RefreshAction extends AbstractAction {
      public RefreshAction() {
         ActionDecorators.REFRESH.decorate(this);
         this.putValue("ShortDescription", Strings.get("Deploy.Page.RefreshAction.ToolTip"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.setEnabled(false);
         DeployPage.this.preferredOption = DeployPage.this.controlPanel.getActiveOption();
         this.rebuildPage();
         DeployPage.this.layoutPageAndStartValidation();
      }

      private void rebuildPage() {
         DeployPage.this.mainPanel.removeAll();
         DeployPage.this.deployTree.refreshTree();
         DeployPage.this.createComponents(false);
         DeployPage.this.layoutWaitPanel();
         UiUtils.revalidate(DeployPage.this.mainPanel);
      }
   }
}
