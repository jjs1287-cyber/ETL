package org.etlforge.integration.client.ui.interchange.entity.project.page;

import com.google.common.collect.Lists;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.project.ProjectRenamer;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.project.DownloadInfo;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.text.KongaTextField;

public final class ProjectPanel extends IntegrationEntityPanel {
   private final ProjectNameField nameField;
   private final TextInputField descriptionField;
   private final TextInputField locationOnDiskField;
   private final TextInputField createdAtField;
   private final TextInputField modifiedAtField;
   private final TextInputField downloadedFromField;
   private final TextInputField downloadedAtField;
   private final TextInputField jitterPackCommentField;
   private Action[] projectActions;

   public ProjectPanel(ApplicationWindow var1, IntegrationProject var2) {
      super(var1, var2);
      this.nameField = new ProjectNameField(var2);
      this.projectActions = new Action[0];
      this.locationOnDiskField = this.createLocationOnDiskField(var2);
      DateFormat var3 = DateFormat.getDateTimeInstance();
      DownloadInfo var4 = var2.getDownloadInfo();
      if (var4 == null) {
         this.createdAtField = this.createCreatedAtField(var2, var3);
         this.downloadedFromField = null;
         this.downloadedAtField = null;
      } else {
         this.createdAtField = null;
         this.downloadedFromField = this.createDownloadedFromField(var4);
         this.downloadedAtField = this.createDownloadedAtField(var4, var3);
      }

      this.modifiedAtField = this.createModifiedAtField(var2, var3);
      this.descriptionField = this.createDescriptionField(var2);
      this.jitterPackCommentField = this.createCommentField(var2);
      this.addInputFields();
      this.setIncludeNameField(false);
   }

   private TextInputField createLocationOnDiskField(final IntegrationProject var1) {
      ProjectPanel.FieldValue var2 = new ProjectPanel.FieldValue() {
         @Override
         public String getValue() {
            File var1x = var1.getLocationOnDisk();
            return var1x == null ? "?" : var1x.getAbsolutePath();
         }
      };
      return getInputField(var2, Strings.get("ProjectPage.Label.LocationOnDisk"), 40, false);
   }

   private static TextInputField getInputField(ProjectPanel.FieldValue var0, String var1, int var2, boolean var3) {
      JLabel var4 = new JLabel(var1);
      String var5 = var0.getValue();
      TextInputField var6 = new TextInputField(var1, new KongaTextField(var5, var2), var4);
      var6.setEditable(var3);
      var6.getInputComponent().setToolTipText(var1 + ": " + var5);
      return var6;
   }

   private TextInputField createCreatedAtField(final IntegrationProject var1, final DateFormat var2) {
      ProjectPanel.FieldValue var3 = new ProjectPanel.FieldValue() {
         @Override
         public String getValue() {
            Date var1x = var1.getCreatedAt();
            return var1x == null ? "?" : var2.format(var1x);
         }
      };
      return getInputField(var3, Strings.get("ProjectPage.Label.Created"), 20, false);
   }

   private TextInputField createDownloadedFromField(final DownloadInfo var1) {
      ProjectPanel.FieldValue var2 = new ProjectPanel.FieldValue() {
         @Override
         public String getValue() {
            return var1.getServerName();
         }
      };
      return getInputField(var2, Strings.get("ProjectPage.Label.DownloadedFrom"), 40, false);
   }

   private TextInputField createDownloadedAtField(final DownloadInfo var1, final DateFormat var2) {
      ProjectPanel.FieldValue var3 = new ProjectPanel.FieldValue() {
         @Override
         public String getValue() {
            return var2.format(var1.getWhen());
         }
      };
      return getInputField(var3, Strings.get("ProjectPage.Label.DownloadedAt"), 20, false);
   }

   private TextInputField createModifiedAtField(final IntegrationProject var1, final DateFormat var2) {
      ProjectPanel.FieldValue var3 = new ProjectPanel.FieldValue() {
         @Override
         public String getValue() {
            Date var1x = var1.getModifiedAt();
            return var1x == null ? "?" : var2.format(var1x);
         }
      };
      return getInputField(var3, Strings.get("ProjectPage.Label.Modified"), 20, false);
   }

   private TextInputField createDescriptionField(IntegrationProject var1) {
      TextInputField var2 = InputFieldFactory.getDescriptionField();
      var2.setValue(var1.getDescription());
      var2.getInputComponent().setCaretPosition(0);
      return var2;
   }

   private TextInputField createCommentField(IntegrationProject var1) {
      String var2 = Strings.format("ProjectPage.Label.JitterPackComment", "Jitterpak");
      TextInputField var3 = InputFieldFactory.getTextInputArea(var2, var2, 8, 40);
      var3.setValue(var1.getJitterPackComment());
      var3.getInputComponent().setCaretPosition(0);
      return var3;
   }

   private void addInputFields() {
      this.addInputField(this.descriptionField);
      this.addInputField(this.jitterPackCommentField);
   }

   @Override
   public void display() {
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
      this.layoutComponents();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
      super.display();
   }

   private void layoutComponents() {
      this.wrap((new ProjectPanel.Layout()).layout);
   }

   public void setRenamer(ProjectRenamer var1) {
      this.nameField.setRenamer(var1);
   }

   public void setProjectActions(Action... var1) {
      this.projectActions = (Action[])var1.clone();
   }

   public IntegrationProject getDisplayedObject() {
      return (IntegrationProject)super.getDisplayedObject();
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      this.getDisplayedObject().setDescription(this.descriptionField.getValue());
      this.getDisplayedObject().setJitterPackComment(this.jitterPackCommentField.getValue());
   }

   @Override
   public void setInitialFocus() {
      this.nameField.requestFocus();
   }

   BoundProperty[] getBoundPropertyControls() {
      return new BoundProperty[]{this.nameField.asBoundProperty(), BoundPropertyFactory.descriptionProperty(this.descriptionField)};
   }

   private interface FieldValue {
      String getValue();
   }

   private class Layout {
      private final TwoColumnPanel layout = new TwoColumnPanel()
         .withEmptyBorder(10)
         .withLeftInsets(0, 0, 10, 10)
         .withRightInsets(0, 0, 10, 0)
         .withRightWeights(1.0, 0.0);

      public Layout() {
         ProjectPanel.this.nameField.layoutIn(this.layout);
         this.layoutReadOnlyLabel();
         ArrayList var2 = Lists.newArrayList();
         var2.add(ProjectPanel.this.locationOnDiskField);
         if (ProjectPanel.this.createdAtField != null) {
            var2.add(ProjectPanel.this.createdAtField);
         } else {
            assert ProjectPanel.this.downloadedFromField != null;
            assert ProjectPanel.this.downloadedAtField != null;
            var2.add(ProjectPanel.this.downloadedFromField);
            var2.add(ProjectPanel.this.downloadedAtField);
         }

         var2.add(ProjectPanel.this.modifiedAtField);

         for (Object __etlItem218 : var2) {
            InputField var4 = (InputField) __etlItem218;
            this.layoutField(var4, this.layout);
         }

         this.layoutActions(this.layout);

         for (InputField var6 : Arrays.asList(ProjectPanel.this.descriptionField, ProjectPanel.this.jitterPackCommentField)) {
            this.layoutField(var6, this.layout);
         }
      }

      private void layoutReadOnlyLabel() {
         if (!ProjectPanel.this.getDisplayedObject().isDeployable()) {
            JLabel var1 = new JLabel("This is a read-only project. It cannot be deployed to a server.");
            var1.setBorder(StyleSheet.getDefaultTitleBorder("Read-Only"));
            this.layout.addRow(new JLabel(" "), var1);
         }
      }

      private void layoutField(InputField var1, TwoColumnPanel var2) {
         boolean var3 = this.isScrollable(var1);
         var2.withLeftAnchor(var3 ? TwoColumnPanel.NORTHWEST : TwoColumnPanel.WEST);
         var2.withRightWeights(1.0, var3 ? 0.5 : 0.0);
         var2.withRightFill(this.getRightFill(var1, var3));
         JLabel var4 = var1.getLabel();
         JComponent var5 = var3 ? new JScrollPane(var1.getInputComponent()) : var1.getInputComponent();
         var2.addRow(var4, var5);
      }

      private void layoutActions(TwoColumnPanel var1) {
         if (ProjectPanel.this.projectActions.length > 0) {
            BoxBuilder var2 = Horizontal.box().withSmallSizedIconsForActions();
            var2.addAllWithSpace(10, ProjectPanel.this.projectActions);
            var1.addRow(null, var2);
         }
      }

      private boolean isScrollable(InputField var1) {
         JComponent var2 = var1.getInputComponent();
         return var2 instanceof Scrollable && !(var2 instanceof JTextField);
      }

      private GridBagLayoutPanel.Fill getRightFill(InputField var1, boolean var2) {
         if (var2) {
            return GridBagLayoutPanel.BOTH;
         } else {
            return var1.getInputComponent() instanceof JComboBox ? GridBagLayoutPanel.NONE : GridBagLayoutPanel.HORIZONTAL;
         }
      }
   }
}
