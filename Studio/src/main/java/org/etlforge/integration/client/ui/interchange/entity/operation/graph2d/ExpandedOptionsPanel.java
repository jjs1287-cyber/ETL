package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.base.Optional;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.event.UndoableEditListener;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.selector.combo.IntegrationEntityComboBox;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.SchedulePanel;
import org.etlforge.integration.client.ui.interchange.util.InterchangeViewUtils;
import org.etlforge.integration.client.ui.operation.options.OperationOptionsPanel;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.NextOperationFilter;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.DropDownPanel;

public final class ExpandedOptionsPanel implements UiProvider, Disposable {
   private final OperationGraphController controller;
   private final OperationWrapperNode opNode;
   private final InputPanel panel;
   private final TextInputField2 nameField;
   private final Optional<OperationTypeComboBox> typeSelector;
   private final OperationOptionsPanel optionsPanel;
   private final ExpandedOptionsPanel.PostOperationPanel onSuccessPanel;
   private final ExpandedOptionsPanel.PostOperationPanel onFailurePanel;
   private final SchedulePanel schedulePanel;

   public ExpandedOptionsPanel(OperationGraphController var1, OperationWrapperNode var2) {
      this.controller = var1;
      this.opNode = var2;
      this.panel = new InputPanel();
      OperationWrapper var3 = var2.getDataObject();
      this.nameField = this.createNameField(var3);
      this.typeSelector = this.createTypeSelector(var3);
      this.optionsPanel = this.createOptionsPanel(var3);
      this.onSuccessPanel = this.createSuccessPanel(var1.getExplorerSupport(), var3);
      this.onFailurePanel = this.createFailurePanel(var1.getExplorerSupport(), var3);
      this.schedulePanel = this.createSchedulePanel(var1.getExplorerSupport(), var3);
      this.applyOperationTypeSpecificSettings(var3.getPipeline().getType());
      this.doLayout();
      this.panel.setSendInputChangeEvents(true);
      this.panel.setSendUndoEvents(true);
   }

   @Override
   public void dispose() {
      this.panel.dispose();
      this.schedulePanel.dispose();
      this.onSuccessPanel.dispose();
      this.onFailurePanel.dispose();
   }

   public void apply() {
      this.controller.setOperationName(this.opNode, this.nameField.getValue().trim());
      this.applyNextActivities();
      this.controller.setSchedulePolicy(this.opNode, this.schedulePanel.getSelectedPolicy());
      this.controller.setSchedule(this.opNode, this.schedulePanel.getSelectedSchedule());
      if (this.typeSelector.isPresent()) {
         this.controller.setOperationType(this.opNode, this.typeSelector.get().getSelectedOperationType());
      }

      this.controller.setOperationOptions(this.opNode, this.optionsPanel.getDefinedOptions());
   }

   private void applyNextActivities() {
      this.controller.setNextOperation(this.opNode, this.onSuccessPanel.getSelectedOperation(), RouteType.SUCCESS);
      this.controller.setNextEmail(this.opNode, this.onSuccessPanel.getSelectedMessage(), RouteType.SUCCESS);
      this.controller.setNextOperation(this.opNode, this.onFailurePanel.getSelectedOperation(), RouteType.FAILURE);
      this.controller.setNextEmail(this.opNode, this.onFailurePanel.getSelectedMessage(), RouteType.FAILURE);
   }

   public boolean validate() {
      try {
         this.nameField.validateField();
         return true;
      } catch (InputFieldException var2) {
         Alert.error(var2.getMessage(), GraphResources.getString("InvalidValue"));
         return false;
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel.getUi());
   }

   @Override
   public void requestFocus() {
      this.nameField.requestFocus();
   }

   public void addInputChangeListener(InputChangeListener var1) {
      this.panel.addInputChangeListener(var1);
   }

   public void addUndoableEditListener(UndoableEditListener var1) {
      this.panel.addUndoableEditListener(var1);
   }

   InputPanel getInputPanel() {
      return this.panel;
   }

   private void applyOperationTypeSpecificSettings(OperationType var1) {
      boolean var2 = var1.isEmailNotificationsSupported();
      this.onSuccessPanel.setEmailSelectionEnabled(var2);
      this.onFailurePanel.setEmailSelectionEnabled(var2);
   }

   private OperationOptionsPanel createOptionsPanel(OperationWrapper var1) {
      OperationOptionsPanel var2 = new OperationOptionsPanel(var1, this.controller.getExplorerSupport());
      this.panel.addInputPanel(var2);
      return var2;
   }

   private SchedulePanel createSchedulePanel(EntityExplorerSupport var1, OperationWrapper var2) {
      SchedulePanel var3 = new SchedulePanel(var2, var1.getProject());
      this.panel.addInputPanel(var3);
      return var3;
   }

   private ExpandedOptionsPanel.PostOperationPanel createSuccessPanel(EntityExplorerSupport var1, OperationWrapper var2) {
      ExpandedOptionsPanel.PostOperationPanel var3 = new ExpandedOptionsPanel.PostOperationPanel(var1, var2.getSuccessOperationId(), var2.getSuccessEmailId());
      this.panel.addInputPanel(var3);
      return var3;
   }

   private ExpandedOptionsPanel.PostOperationPanel createFailurePanel(EntityExplorerSupport var1, OperationWrapper var2) {
      ExpandedOptionsPanel.PostOperationPanel var3 = new ExpandedOptionsPanel.PostOperationPanel(var1, var2.getFailureOperationId(), var2.getFailureEmailId());
      this.panel.addInputPanel(var3);
      return var3;
   }

   private void doLayout() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 0, 20, 0);
      this.layoutTopPart(var1);
      DropDownPanel var2 = this.createDropDownPanel(this.onSuccessPanel, Strings.get("ExpandedOptions.OnSuccess"), GraphResources.SUCCESS_24, true);
      var1.add(var2.getDisplayer());
      DropDownPanel var3 = this.createDropDownPanel(this.onFailurePanel, Strings.get("ExpandedOptions.OnFailure"), GraphResources.FAILURE_24, true);
      var1.add(var3.getDisplayer());
      DropDownPanel var4 = this.createDropDownPanel(
         this.schedulePanel, Strings.get("ExpandedOptions.Schedule"), EntityIcons.forType(EntityType.Schedule).getDefaultIcon(EntityIcons.Size.MEDIUM), false
      );
      this.checkSchedulable(var4);
      var1.add(var4.getDisplayer());
      DropDownPanel var5 = this.createDropDownPanel(this.optionsPanel, Strings.get("ExpandedOptions.Options"), ClientIcons.getIcon("Options.24"), false);
      var1.add(var5.getDisplayer());
      this.panel.wrap(var1);
      this.panel.setBorder(new BorderBuilder().etched().empty(10));
   }

   private void layoutTopPart(OneColumnPanel var1) {
      TwoColumnPanel var2 = new TwoColumnPanel();
      var2.withLeftInsets(0, 0, 10, 10);
      var2.withRightInsets(0, 0, 10, 0);
      var2.withRightFill(TwoColumnPanel.HORIZONTAL);
      var2.withRightWeights(1.0, 0.0);
      var2.addRow(this.nameField.getLabel(), this.nameField.getInputComponent());
      if (this.typeSelector.isPresent()) {
         var2.addRow(UiProviderAdapter.label(Strings.get("ExpandedOptions.Activity")), this.typeSelector.get());
      }

      var1.add(var2);
   }

   private void checkSchedulable(DropDownPanel var1) {
      boolean var2 = this.opNode.getDataObject().isSchedulable();
      var1.setEnabled(var2);
      if (!var2) {
         var1.setTitleToolTip(Strings.get("ExpandedOptions.Schedule.Disabled.ToolTip"));
      }
   }

   private DropDownPanel createDropDownPanel(InputPanel var1, String var2, Icon var3, boolean var4) {
      var1.getUi().setBorder(Empty.border(0, 25, 0, 0));
      DropDownPanel var5 = new DropDownPanel(var1.getUi(), var2, var4);
      var5.setTitleIcon(var3);
      var5.setOpaque(false);
      return var5;
   }

   private TextInputField2 createNameField(OperationWrapper var1) {
      Operation var2 = var1.getActivity();
      String var3 = GraphResources.getString("Name");
      JLabel var4 = new JLabel(var3);
      TextInputField2 var5 = InterchangeViewUtils.getIntegrationEntityNameInputField(var2, var3, var4, 30);
      var5.setValue(var1.getName());
      var5.setSelectAllWhenFocused(true);
      this.panel.addInputField(var5, var5, var5);
      return var5;
   }

   private Optional<OperationTypeComboBox> createTypeSelector(OperationWrapper var1) {
      if (!var1.isOperationTypeChangeable()) {
         return Optional.absent();
      }

      final OperationTypeComboBox var2 = new OperationTypeComboBox();
      var2.setSelectedOperationType(var1.getPipeline().getType());
      this.panel.addInputField(var2.asInputField(Strings.get("ExpandedOptions.Activity")));
      var2.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            OperationType var2x = var2.getSelectedOperationType();
            ExpandedOptionsPanel.this.applyOperationTypeSpecificSettings(var2x);
         }
      });
      return Optional.of(var2);
   }

   private static class PostOperationPanel extends InputPanel implements Disposable {
      private final IntegrationEntityComboBox messageSelector;
      private final IntegrationEntityComboBox operationSelector;

      public PostOperationPanel(EntityExplorerSupport var1, OperationId var2, EmailMessageId var3) {
         this.operationSelector = new IntegrationEntityComboBox(var1.getProject(), EntityType.Operation, new NextOperationFilter(), true);
         this.operationSelector.setSelectedEntity(var2);
         this.messageSelector = new IntegrationEntityComboBox(var1.getProject(), EntityType.EmailMessage, null, true);
         this.messageSelector.setSelectedEntity(var3);
         this.addInputFields();
         this.layoutComponents();
      }

      public Operation getSelectedOperation() {
         return (Operation)this.operationSelector.getSelectedEntity();
      }

      public EmailMessage getSelectedMessage() {
         return (EmailMessage)this.messageSelector.getSelectedEntity();
      }

      public void setEmailSelectionEnabled(boolean var1) {
         this.messageSelector.setEnabled(var1);
      }

      @Override
      public void dispose() {
         this.operationSelector.dispose();
         this.messageSelector.dispose();
         super.dispose();
      }

      private void layoutComponents() {
         TwoColumnPanel var1 = new TwoColumnPanel();
         var1.withInsets(0, 0, 10, 10);
         var1.withRightFill(TwoColumnPanel.HORIZONTAL);
         var1.withRightWeights(1.0, 0.0);
         this.operationSelector.layoutIn(Strings.get("ExpandedOptions.SelectOperation"), var1);
         this.messageSelector.layoutIn(Strings.get("ExpandedOptions.SelectEmail"), var1);
         this.wrap(var1);
      }

      private void addInputFields() {
         this.addInputField(this.operationSelector.asInputField(null, null));
         this.addInputField(this.messageSelector.asInputField(null, null));
      }
   }
}
