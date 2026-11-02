package org.etlforge.integration.client.ui.interchange.component.launcher;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.project.ProjectDirectory;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.project.IntegrationProjectFactory;
import org.etlforge.integration.client.ui.project.IntegrationProjectFactoryException;
import org.etlforge.integration.client.ui.project.location.ProjectBrowserLocationDisplayer;
import org.etlforge.integration.client.ui.project.location.ProjectLocationBrowser;
import org.etlforge.integration.client.ui.project.location.ProjectLocationPreference;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.project.ProjectIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.Enablabes;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

public final class CreateNewProjectOption extends AbstractLaunchOption {
   private static final Logger logger = ApplicationLog.getMainLogger();
   private final ProjectManager projectManager;
   private final JComponent ui;
   private final ProjectBrowserLocationDisplayer inputPanel;
   private final Action createAction;

   public CreateNewProjectOption(ProjectManager var1) {
      super(Strings.get("Launcher.Option.New.Name"), ProjectIcons.PROJECT_32);
      this.projectManager = var1;
      this.createAction = new CreateNewProjectOption.CreateAction();
      this.inputPanel = this.createLocationDisplayer();
      this.ui = this.layoutComponents();
   }

   private ProjectBrowserLocationDisplayer createLocationDisplayer() {
      ProjectLocation var1 = this.getSuggestedProjectNameAndLocation();
      ProjectBrowserLocationDisplayer var2 = ProjectLocationBrowser.getNewProjectBrowser(var1.getProjectName(), var1.getLocation());
      var2.addActionListener(this.createAction);
      Enablable var3 = Enablabes.fromAction(this.createAction);
      var2.addValidInputSensor(var3);
      return var2;
   }

   private ProjectLocation getSuggestedProjectNameAndLocation() {
      File var1 = ProjectLocationPreference.getNewProjectLocation();
      if (var1 == null) {
         var1 = this.projectManager.getProjectDirectory().getLocation();
      }

      String var2 = this.getSuggestedProjectName(var1);
      return new ProjectLocation(var2, var1);
   }

   private String getSuggestedProjectName(File var1) {
      ProjectDirectory var2 = this.projectManager.getProjectDirectory();
      return var2.getSuggestedNewProjectName(var1);
   }

   private JComponent layoutComponents() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setWeightX(1.0);
      JLabel var2 = this.createCaption();
      var1.add(var2);
      var1.add(this.createTopPanel());
      var1.add(InvisiblePanel.of(this.createButton()));
      BorderLayoutBuilder var3 = new BorderLayoutBuilder();
      var3.north(var1).center(" ");
      return var3.container();
   }

   private JComponent createTopPanel() {
      JComponent var1 = this.inputPanel.getComponent();
      UiUtils.setOpaqueRecursivelySkipDefaults(var1, false);
      JPanel var2 = new JPanel(new FlowLayout(3));
      var2.add(var1);
      var2.setOpaque(false);
      var2.setBorder(Empty.border(0, 0, 10, 0));
      return var2;
   }

   private KongaButton createButton() {
      KongaButton var1 = new KongaButton(this.createAction);
      WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var1);
      return var1;
   }

   @Override
   public IntegrationProject getProject() {
      String var1 = this.inputPanel.getProjectName();
      File var2 = new File(this.getNewProjectLocation(), var1);

      try {
         IntegrationProjectFactory var3 = new IntegrationProjectFactory(this.projectManager);
         return var3.createNewProject(var1, var2);
      } catch (IntegrationProjectFactoryException var4) {
         logger.throwing(this.getClass().getName(), "createNewProject", var4);
         ErrorLog.attention(CreateNewProjectOption.class, Strings.get("Launcher.CreateProjectFailed"), var4);
         return null;
      }
   }

   @Override
   public void wakeUp() {
      ProjectLocation var1 = this.getSuggestedProjectNameAndLocation();
      this.inputPanel.setProjectName(var1.getProjectName());
      this.inputPanel.setProjectLocation(var1.getLocation());
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.inputPanel.highlightProjectName();
   }

   @Override
   public boolean canBeDefaultChoice() {
      return true;
   }

   @Override
   public void activated() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CreateNewProjectOption.this.requestFocus();
         }
      });
   }

   private File getNewProjectLocation() {
      return this.inputPanel.getProjectLocation();
   }

   private class CreateAction extends AbstractAction {
      public CreateAction() {
         super(Strings.get("Launcher.Buttons.Create"), ProjectIcons.PROJECT_24);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         String var2 = CreateNewProjectOption.this.inputPanel.getProjectName().trim();
         if (this.isNameOk(var2) && this.checkNewProjectLocationValid(var2)) {
            CreateNewProjectOption.this.launchProject();
         }
      }

      private boolean isNameOk(String var1) {
         try {
            NameValidator var2 = NameValidatorStore.getInstance().get(IntegrationProject.class);
            var2.validateName(var1);
            return true;
         } catch (IllegalNameException var3) {
            this.handleError(var3);
            return false;
         }
      }

      private void handleError(Exception var1) {
         Alert.error(var1.getMessage(), Strings.get("Launcher.Option.New.IllegalName"));
      }

      private boolean checkNewProjectLocationValid(String var1) {
         boolean var2 = true;
         File var3 = new File(CreateNewProjectOption.this.getNewProjectLocation(), var1);
         if (var3.exists()) {
            var2 = this.promptUserForOverWrite(var3);
         }

         return var2;
      }

      private boolean promptUserForOverWrite(File var1) {
         String var2 = this.getFolderExistsMessage(var1.getAbsolutePath());
         String var3 = Strings.get("Launcher.WarningTitle");
         Answer var4 = Ask.yesNoOrCancel(null, var2, var3, Alert.Option.NO, Alert.Type.WARNING);
         return var4.isYes();
      }

      private String getFolderExistsMessage(String var1) {
         return Strings.format("Project.LocationExists", var1);
      }
   }
}
