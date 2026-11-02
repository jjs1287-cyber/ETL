package com.etlforge.integration.client.ui.script.fcn.arg;

import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.util.focus.StaticFocusTraversalPolicy;
import org.etlforge.ui.widget.button.KongaButton;

final class ArgumentFieldsPanel implements UiProvider {
   private final InputPanel inputPanel;
   private final List<ArgumentField> fields;
   private final JButton testButton;
   private JTextComponent lastFocusedField;

   public ArgumentFieldsPanel(ArgumentFieldLayout var1, List<ArgumentField> var2, Action var3) {
      this.fields = var2;
      this.inputPanel = new InputPanel(new BorderLayout(0, 0));
      this.testButton = var3 == null ? null : new KongaButton(var3);
      this.listenToFocus();
      this.registerFields();
      this.layout(var1);
      this.installFocusTraversal();
      this.inputPanel.setSendInputChangeEvents(true);
   }

   private void listenToFocus() {
      ArgumentFieldsPanel.LastFocusedFieldListener var1 = new ArgumentFieldsPanel.LastFocusedFieldListener();

      for (ArgumentField var3 : this.fields) {
         var3.addFocusListener(var1);
      }
   }

   private void registerFields() {
      for (ArgumentField var2 : this.fields) {
         var2.addToInputPanel(this.inputPanel);
      }
   }

   private void layout(ArgumentFieldLayout var1) {
      for (ArgumentField var3 : this.fields) {
         var3.layoutIn(var1);
      }

      var1.addButton(this.testButton);
      WizardPageLayout var4 = new WizardPageLayout();
      var4.add(var1.getContainer(), true, true);
      this.inputPanel.getUi().add(var4.asNorthBorderLayout(), "North");
      StyleSheet.GenericEditorPanelClass.makeOver(this.inputPanel.getUi());
   }

   private void installFocusTraversal() {
      List var1 = this.getComponentOrder();
      StaticFocusTraversalPolicy var2 = new StaticFocusTraversalPolicy(var1);
      JPanel var3 = this.inputPanel.getUi();
      var3.setFocusCycleRoot(true);
      var3.setFocusTraversalPolicy(var2);
   }

   private List<Component> getComponentOrder() {
      ArrayList var1 = Lists.newArrayList();

      for (ArgumentField var3 : this.fields) {
         var1.add(var3.getComponentForFocusTraversal());
      }

      if (this.testButton != null) {
         var1.add(this.testButton);
      }

      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.inputPanel.getUi());
   }

   @Override
   public void requestFocus() {
      this.fields.get(0).requestFocus();
   }

   public void addChangeListener(InputChangeListener var1) {
      this.inputPanel.addInputChangeListener(var1);
   }

   public void removeChangeListener(InputChangeListener var1) {
      this.inputPanel.removeInputChangeListener(var1);
   }

   public boolean isInputValid() {
      return this.inputPanel.isInputValid();
   }

   public JTextComponent getLastFocusedField() {
      return this.lastFocusedField;
   }

   private class LastFocusedFieldListener extends FocusAdapter {
      private LastFocusedFieldListener() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         if (var1.getSource() instanceof JTextComponent) {
            ArgumentFieldsPanel.this.lastFocusedField = (JTextComponent)var1.getSource();
         }
      }
   }
}
