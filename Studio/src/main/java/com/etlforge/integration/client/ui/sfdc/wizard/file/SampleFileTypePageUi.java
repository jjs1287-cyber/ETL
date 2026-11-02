package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.util.focus.Focusable;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class SampleFileTypePageUi implements UiProvider {
   private final LinkedHashMap<KongaRadioButton, SampleFileTypeOption> optionMap = Maps.newLinkedHashMap();
   private final KongaCheckBox reviewStructureChoice = new KongaCheckBox(
      "I'd like to &review the " + EntityType.Document.getDisplayName() + " created by ETLForge"
   );
   private final JComponent layout;

   public SampleFileTypePageUi() {
      this.createButtons();
      this.layout = this.createLayout();
      this.listenToUi();
   }

   private void createButtons() {
      boolean var1 = true;

      for (SampleFileTypeOption var5 : this.getAllOptions()) {
         KongaRadioButton var6 = new KongaRadioButton(var5.getText(), var1);
         WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var6);
         this.optionMap.put(var6, var5);
         if (var5 instanceof Focusable) {
            FocusUtils.bindChoiceComponents(var6, (Focusable)var5);
         }

         var1 = false;
      }

      ButtonUtils.makeMutuallyExclusive(this.optionMap.keySet());
   }

   private SampleFileTypeOption[] getAllOptions() {
      return new SampleFileTypeOption[]{
         new CsvSampleFileTypeOption(true),
         new CsvSampleFileTypeOption(false),
         new DelimitedSampleFileTypeOption(true),
         new DelimitedSampleFileTypeOption(false),
         new OtherSampleFileTypeOption()
      };
   }

   private JComponent createLayout() {
      return new OneColumnPanel().setWeightX(1.0).withInsets(0, 0, 16, 0).of(this.layoutChoices(), this.layoutReviewChoice()).invisible();
   }

   private JComponent layoutChoices() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
      byte var2 = 16;

      for (Entry var4 : this.optionMap.entrySet()) {
         Optional var5 = ((SampleFileTypeOption)var4.getValue()).getUi();
         boolean var6 = var5.isPresent();
         var1.withInsets(0, 0, var6 ? 4 : var2, 0);
         var1.add((Component)var4.getKey());
         if (var6) {
            var1.withInsets(0, 0, var2, 0);
            var1.add((UiProvider)var5.get());
         }
      }

      return InvisiblePanel.of(var1);
   }

   private JComponent layoutReviewChoice() {
      Border var1 = StyleSheet.getDefaultTitleBorder("Review " + EntityType.Document.getDisplayName());
      return new BorderLayoutBuilder().withBorder(var1).north(this.reviewStructureChoice).container();
   }

   private void listenToUi() {
      ActionListener var1 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            SampleFileTypePageUi.this.updateEnabledStateOfReviewStructureChoice();
         }
      };
      ButtonUtils.addActionListener(var1, this.optionMap.keySet());
   }

   private void updateEnabledStateOfReviewStructureChoice() {
      SampleFileTypeOption var1 = this.getSelectedOption();
      boolean var2 = var1.isGeneratedAutomatically();
      this.reviewStructureChoice.setEnabled(var2);
   }

   public SampleFileTypeOption getSelectedOption() {
      for (Entry var2 : this.optionMap.entrySet()) {
         if (((KongaRadioButton)var2.getKey()).isSelected()) {
            return (SampleFileTypeOption)var2.getValue();
         }
      }

      throw new RuntimeException("It should be impossible to unselect all options, but somehow it has happened.");
   }

   public boolean isStructureReviewRequested() {
      return this.reviewStructureChoice.isEnabled() && this.reviewStructureChoice.isSelected();
   }

   public void setBorder(Border var1) {
      this.layout.setBorder(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.layout);
   }

   @Override
   public void requestFocus() {
      KongaRadioButton var1 = this.optionMap.keySet().iterator().next();
      var1.requestFocus();
   }
}
