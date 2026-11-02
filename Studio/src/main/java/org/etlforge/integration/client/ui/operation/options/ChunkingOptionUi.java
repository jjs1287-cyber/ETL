package org.etlforge.integration.client.ui.operation.options;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.options.ChunkingOption;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.StructureNature;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.ToggleButtonActivationConstraint;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.UiUtils;

public final class ChunkingOptionUi implements OperationOptionUi {
   private static final String FLAT = "-flat-";
   private final CheckBoxInputField activator;
   private final ChunkSizeAndThreadCountUi chunkSizeAndThreadCountUi;
   private final TextInputField sourceNodeField;
   private final Action selectSourceNodeAction;
   private final TextInputField targetNodeField;
   private final Action selectTargetNodeAction;
   private JComponent layout;
   private boolean mandatory;

   public ChunkingOptionUi(Operation var1) {
      this(var1.getOperationOptions().getChunkingOption(), var1.getPipeline());
   }

   public ChunkingOptionUi(OperationWrapper var1) {
      this(var1.getOptions().getChunkingOption(), var1.getPipeline());
   }

   public ChunkingOptionUi(ChunkingOption var1, OperationPipeline var2) {
      this.activator = this.createActivator(var1);
      this.chunkSizeAndThreadCountUi = new ChunkSizeAndThreadCountUi(var1);
      Transformation var3 = this.getFirstTransformation(var2);
      this.sourceNodeField = this.createChunkNodeField(var1.getSourceChunkNode(), var3 != null ? var3.getInputStructure() : null, "Source chunk node:");
      this.selectSourceNodeAction = new ChunkingOptionUi.SelectChunkNodeAction(var3, this.sourceNodeField, SourceTarget.Source);
      Transformation var4 = this.getLastTransformation(var2);
      this.targetNodeField = this.createChunkNodeField(var1.getTargetChunkNode(), var4 != null ? var4.getOutputStructure() : null, "Target chunk node:");
      this.selectTargetNodeAction = new ChunkingOptionUi.SelectChunkNodeAction(var4, this.targetNodeField, SourceTarget.Target);
      this.bindEnabling();
      this.setEnabled(var1.isApplicableFor(var2));
   }

   public void setChunkSizeInterval(int var1, int var2) {
      this.chunkSizeAndThreadCountUi.setChunkSizeInterval(var1, var2);
      this.chunkSizeAndThreadCountUi.setTargetChunkSizeInterval(var1, var2);
   }

   public void chunkingIsMandatory() {
      this.mandatory = true;
      this.activator.setSelected(true);
      this.activator.setEnabled(false);
   }

   public ChunkingOption getDefinedOption() {
      int var1 = this.activator.isSelected() ? this.chunkSizeAndThreadCountUi.getChunkSize() : 0;
      int var2 = this.activator.isSelected() ? this.chunkSizeAndThreadCountUi.getTargetChunkSize() : 0;
      return new ChunkingOption(
         var1, var2, this.chunkSizeAndThreadCountUi.getNumberOfThreads(), this.sourceNodeField.getValue(), this.targetNodeField.getValue()
      );
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.activator);
      this.chunkSizeAndThreadCountUi.registerAsChangeSource(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layout = this.doLayout();
      var1.display(this.layout);
   }

   private JComponent doLayout() {
      TwoColumnPanel var1 = new TwoColumnPanel().withBorder(Empty.border(0, 30, 0, 0));
      this.chunkSizeAndThreadCountUi.layoutIn(var1);
      this.layoutChunkNodeControls(var1, this.sourceNodeField, this.selectSourceNodeAction);
      this.layoutChunkNodeControls(var1, this.targetNodeField, this.selectTargetNodeAction);
      return this.mandatory ? var1.invisible() : Vertical.box().leftAligned().of(InputFieldFactory.getCapsule(this.activator), var1).invisible();
   }

   private void layoutChunkNodeControls(TwoColumnPanel var1, InputField var2, Action var3) {
      BoxBuilder var4 = BoxBuilder.horizontal();
      var4.addAllWithSpace(5, var2.getInputComponent(), var3);
      var1.addRow(var2.getLabel(), var4.container());
   }

   @Override
   public void requestFocus() {
      if (this.activator.isSelected()) {
         this.chunkSizeAndThreadCountUi.requestFocus();
      } else {
         this.activator.requestFocus();
      }
   }

   private Transformation getFirstTransformation(OperationPipeline var1) {
      TransformationActivity var2 = var1.getFirstActivityOfType(TransformationActivity.class);
      return var2 != null ? var2.getContent() : null;
   }

   private Transformation getLastTransformation(OperationPipeline var1) {
      TransformationActivity var2 = var1.getLastActivityOfType(TransformationActivity.class);
      return var2 != null ? var2.getContent() : null;
   }

   private CheckBoxInputField createActivator(ChunkingOption var1) {
      String var2 = "Enable &Chunking";
      CheckBoxInputField var3 = InputFieldFactory.getCheckBoxInputField(var2, var2);
      var3.setSelected(var1.isEnabled());
      return var3;
   }

   private TextInputField createChunkNodeField(String var1, DataStructure var2, String var3) {
      TextInputField var4 = InputFieldFactory.getTextInputField(var3, var3, 40, false);
      String var5 = var1;
      if (var2 != null && var2.getStructureNature() == StructureNature.Flat) {
         var5 = "-flat-";
      }

      var4.setValue(var5);
      return var4;
   }

   private void bindEnabling() {
      ToggleButtonActivationConstraint var1 = new ToggleButtonActivationConstraint(this.activator);
      this.chunkSizeAndThreadCountUi.bindEnabling(var1);
      var1.bind(this.sourceNodeField, false);
      this.sourceNodeField.addActivationConstraint(var1);
      var1.bind(this.targetNodeField, false);
      this.targetNodeField.addActivationConstraint(var1);
      var1.bind(this.selectSourceNodeAction);
      var1.bind(this.selectTargetNodeAction);
   }

   private void setEnabled(boolean var1) {
      this.activator.setEnabled(var1);
      this.chunkSizeAndThreadCountUi.setEnabled(var1 && this.activator.isSelected());
      this.setToolTip(this.getToolTip(var1));
   }

   private void setToolTip(String var1) {
      if (this.layout != null) {
         this.layout.setToolTipText(var1);
      }

      this.activator.getInputComponent().setToolTipText(var1);
      this.chunkSizeAndThreadCountUi.setToolTip(var1);
   }

   private String getToolTip(boolean var1) {
      return var1 ? null : "Chunking is not available for the selected transformation(s).";
   }

   private static class SelectChunkNodeAction extends AbstractAction {
      private final Transformation tf;
      private final TextInputField field;
      private final SourceTarget st;

      public SelectChunkNodeAction(Transformation var1, TextInputField var2, SourceTarget var3) {
         super("Select...");
         this.tf = var1;
         this.field = var2;
         this.st = var3;
      }

      @Override
      public void setEnabled(boolean var1) {
         super.setEnabled(var1 && this.tf != null);
      }

      @Override
      public final void actionPerformed(ActionEvent var1) {
         if (this.tf != null) {
            this.select(this.tf);
         } else {
            Alert.error("No Transformation is selected", "Error");
         }
      }

      private void select(final Transformation var1) {
         EventQueue.invokeLater(
            new Runnable() {
               @Override
               public void run() {
                  if (var1 instanceof BuiltInIntegrationEntity) {
                     Alert.info(ApplicationUi.getAppWinFrame(), "Chunking is not available.", "Not Available");
                  } else {
                     CROM var1x = ChunkNodeSelectorDialog.selectNode(
                        UiUtils.getActiveFrame(), SelectChunkNodeAction.this.tf, SelectChunkNodeAction.this.st, SelectChunkNodeAction.this.getCurrentValue()
                     );
                     if (var1x != null) {
                        SelectChunkNodeAction.this.setChunkNode(var1x);
                     }
                  }
               }
            }
         );
      }

      private String getCurrentValue() {
         return this.field.getValue().trim();
      }

      private void setChunkNode(CROM var1) {
         String var2 = this.getPath(var1);
         this.field.setValue(var2);
      }

      private String getPath(CROM var1) {
         String var2 = var1.getPath(false, false);
         var2 = KongaStringUtils.removeStartAndEnd(var2, "[", "]");
         if (var2.isEmpty()) {
            var2 = "-flat-";
         }

         return var2;
      }
   }
}
