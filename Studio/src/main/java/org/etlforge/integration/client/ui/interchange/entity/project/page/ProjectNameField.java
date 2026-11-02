package org.etlforge.integration.client.ui.interchange.entity.project.page;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.interchange.util.InterchangeViewUtils;
import org.etlforge.integration.client.ui.project.ProjectRenamer;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnLayout;

final class ProjectNameField {
   private final IntegrationProject project;
   private final TextInputField2 nameField;
   private final ProjectNameField.RenameAction renameAction;

   public ProjectNameField(IntegrationProject var1) {
      this.project = var1;
      this.renameAction = new ProjectNameField.RenameAction();
      this.nameField = this.createNameField(var1);
   }

   public void setRenamer(ProjectRenamer var1) {
      this.renameAction.setRenamer(var1);
   }

   public void layoutIn(TwoColumnLayout var1) {
      JLabel var2 = this.nameField.getLabel();
      JPanel var3 = BoxBuilder.horizontal().addAllWithSpace(10, this.nameField.getInputComponent(), this.renameAction).container();
      var1.addRow(var2, var3);
   }

   public void requestFocus() {
      this.nameField.requestFocus();
   }

   public BoundProperty asBoundProperty() {
      return BoundPropertyFactory.nameProperty(this.nameField);
   }

   private TextInputField2 createNameField(IntegrationProject var1) {
      String var2 = Strings.get("ProjectPage.Label.Name");
      JLabel var3 = new JLabel(var2);
      TextInputField2 var4 = InterchangeViewUtils.getIntegrationEntityNameInputField(var1, var2, var3, 40);
      var4.setEditable(false);
      return var4;
   }

   private class RenameAction extends AbstractAction {
      private ProjectRenamer renamer;

      public RenameAction() {
         super("Rename...");
         this.setEnabled(false);
      }

      public void setRenamer(ProjectRenamer var1) {
         this.renamer = var1;
         this.setEnabled(var1 != null && ProjectNameField.this.project.isRenameable());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.renamer.renameProject(ProjectNameField.this.project);
      }
   }
}
