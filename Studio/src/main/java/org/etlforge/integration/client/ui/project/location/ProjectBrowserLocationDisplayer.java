package org.etlforge.integration.client.ui.project.location;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.project.ProjectNameInputField;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.io.FileUtils;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.event.ActionListeners;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.text.ConditionalTextEnabler;
import org.etlforge.ui.text.NonEmptyTextEnablerCondition;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.FilePathCompletionContext;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

public final class ProjectBrowserLocationDisplayer {
   private final ProjectNameInputField nameField;
   private final ConditionalTextEnabler projectNameEnabler;
   private final KongaTextField locationField;
   private final JButton browseButton;
   private final ActionListeners actionListeners;
   private JComponent displayer;
   private boolean isEnabled = true;

   public ProjectBrowserLocationDisplayer(String var1, File var2) {
      Preconditions.checkNotNull(var2, "location");
      if (var1 == null) {
         var1 = "New Project";
      }

      this.nameField = this.createNameField(var1);
      this.locationField = this.createLocationField(var2);
      this.projectNameEnabler = ConditionalTextEnabler.install(this.nameField.getInputComponent());
      this.browseButton = this.createBrowseButton();
      this.actionListeners = new ActionListeners();
      this.addFieldListener();
      this.layoutComponents();
   }

   public JComponent getComponent() {
      return this.displayer;
   }

   public void setProjectNameEditable(boolean var1) {
      this.nameField.setEditable(var1);
   }

   public File getProjectLocation() {
      return new File(this.locationField.getText().trim());
   }

   public void setProjectLocation(File var1) {
      this.locationField.setText(var1.getAbsolutePath());
   }

   public String getProjectName() {
      return this.nameField.getValue();
   }

   public void setProjectName(String var1) {
      this.nameField.setValue(var1);
   }

   public void highlightProjectName() {
      this.nameField.requestFocus();
   }

   public void addActionListener(ActionListener var1) {
      this.actionListeners.add(var1);
   }

   public void validateProjectName() throws IllegalNameException {
      NameValidator var1 = NameValidatorStore.getInstance().get(IntegrationProject.class);
      var1.validateName(this.nameField.getValue());
   }

   public void addValidInputSensor(Enablable var1) {
      NonEmptyTextEnablerCondition var2 = new NonEmptyTextEnablerCondition(true);
      this.projectNameEnabler.addDependency(var1, var2);
   }

   public boolean isInputValid() {
      return this.getProjectName().trim().length() > 0;
   }

   public void setEnabled(boolean var1) {
      if (var1 != this.isEnabled) {
         this.isEnabled = var1;
         UiUtils.setEnabled(var1, this.nameField.getInputComponent(), this.locationField, this.browseButton);
         if (var1) {
            this.nameField.requestFocus();
         }
      }
   }

   private ProjectNameInputField createNameField(String var1) {
      ProjectNameInputField var2 = new ProjectNameInputField(getString("LocationBrowser.Label.Name"));
      var2.setValue(var1);
      return var2;
   }

   private KongaTextField createLocationField(File var1) {
      KongaTextField var2 = new KongaTextField(var1.getAbsolutePath(), 30);
      this.installLocationAutoCompletion(var2);
      return var2;
   }

   private void installLocationAutoCompletion(KongaTextField var1) {
      if (SystemUtils.IS_OS_WINDOWS) {
         FilePathCompletionContext var2 = new FilePathCompletionContext(FileUtils.DIRECTORIES);
         DropDownAutoCompleter var3 = new DropDownAutoCompleter(var2);
         var3.install(var1);
      }
   }

   private JButton createBrowseButton() {
      KongaButton var1 = new KongaButton(UiResourceBundle.getString("Browse.Button"));
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ProjectBrowserLocationDisplayer.this.doBrowse();
         }
      });
      return var1;
   }

   private void addFieldListener() {
      ActionListener var1 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ProjectBrowserLocationDisplayer.this.processInput(var1);
         }
      };
      this.nameField.addActionListener(var1);
      this.locationField.addActionListener(var1);
   }

   private void layoutComponents() {
      TwoColumnPanel var1 = this.createLayout();
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      this.displayer = var2.north(var1).container();
   }

   private TwoColumnPanel createLayout() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withLeftInsets(0, 0, 10, 0);
      var1.withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL).withLeftWeights(1.0, 0.0);
      var1.addRow(this.nameField.asUiProvider());
      this.locationField.setLabel(getString("LocationBrowser.Label.Path"));
      var1.withRightAnchor(TwoColumnPanel.SOUTHEAST);
      var1.withLeftInsets(0, 0, 0, 10);
      var1.withRightInsets(0, 0, 0, 0);
      var1.addRow(this.locationField.asUiProvider(), UiProviderAdapter.of(this.browseButton));
      return var1;
   }

   private void doBrowse() {
      File var1 = null;
      String var2 = this.locationField.getText().trim();
      if (var2.length() == 0) {
         var1 = ProjectLocationBrowser.browseNewProjectLocation(this.displayer);
      } else {
         var1 = ProjectLocationBrowser.browseNewProjectLocation(new File(var2), this.displayer);
      }

      if (var1 != null) {
         this.locationField.setText(var1.getAbsolutePath());
      }
   }

   private void processInput(ActionEvent var1) {
      if (this.actionListeners != null && this.isEnabled) {
         try {
            this.validateProjectName();
            this.actionListeners.call().actionPerformed(new ActionEvent(this, 1001, var1.getActionCommand()));
         } catch (IllegalNameException var3) {
            Alert.error(var3.getMessage(), "Invalid project name");
         }
      }
   }

   private static String getString(String var0) {
      return ProjectUiResources.getString(var0);
   }
}
