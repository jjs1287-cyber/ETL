package org.etlforge.integration.client.ui.deploy.page;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.deploy.option.DeployOption;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.ui.util.option.ToggleButtonOptionSelector;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class DeployControlPanel implements UiProvider {
   private static final TextStyle CAPTION_STYLE = TextStyles.BigAdminHintBold;
   private static final TextStyle ADVANCED_STYLE = TextStyles.AdminHintBold;
   private final List<DeployOption> options;
   private final DeployOption preferredOption;
   private List<OptionSelector<DeployOption>> optionSelectors;
   private final Map<JRadioButton, OptionSelector<DeployOption>> optionMap;
   private final JPanel optionPanel;
   private JComponent defaultFocusOwner;
   private DropDownPanel advancedOptionsDropDown;

   public DeployControlPanel(DeployOption[] var1, DeployOption var2) {
      this.options = Arrays.asList(var1);
      this.preferredOption = var2;
      this.optionMap = Maps.newLinkedHashMap();
      this.optionPanel = this.createOptionPanel();
   }

   public DeployOption getActiveOption() {
      for (OptionSelector var2 : this.optionSelectors) {
         if (var2.isSelected()) {
            return (DeployOption)var2.getOption();
         }
      }

      return null;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.optionPanel);
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (DeployControlPanel.this.defaultFocusOwner != null) {
               DeployControlPanel.this.defaultFocusOwner.requestFocusInWindow();
            }
         }
      });
   }

   public void onValidProject() {
      for (Entry var2 : this.optionMap.entrySet()) {
         JRadioButton var3 = (JRadioButton)var2.getKey();
         var3.setEnabled(true);
         if (((OptionSelector)var2.getValue()).getOption() == this.preferredOption) {
            if (var3.isSelected()) {
               ((DeployOption)((OptionSelector)var2.getValue()).getOption()).setActive(true);
            } else {
               var3.setSelected(true);
            }
         }
      }
   }

   public void onInvalidProject() {
      JRadioButton var1 = null;

      for (Entry var3 : this.optionMap.entrySet()) {
         JRadioButton var4 = (JRadioButton)var3.getKey();
         DeployOption var5 = (DeployOption)((OptionSelector)var3.getValue()).getOption();
         if (var5 == this.preferredOption && !this.preferredOption.requiresValidProject()) {
            var1 = var4;
         }

         if (((DeployOption)((OptionSelector)var3.getValue()).getOption()).requiresValidProject()) {
            var4.setEnabled(false);
         } else {
            var4.setEnabled(true);
            if (var1 == null) {
               var1 = var4;
            }
         }
      }

      if (var1 != null) {
         var1.setSelected(true);
      }
   }

   private JPanel createOptionPanel() {
      this.createRadioButtons();
      KongaLabel var1 = CAPTION_STYLE.makeLabel(Strings.get("Deploy.Page.Labels.SelectType"));
      OneColumnPanel var2 = this.createEmptyColumn();
      var2.withInsets(0, 0, 10, 0);
      var2.add(var1);
      var2.withInsets(5, 0, 0, 0);
      this.layoutNormalOptions(var2);
      this.layoutAdvancedOptions(var2);
      var2.withBorder(Empty.border(5, 5, 0, 5)).deeplyNonOpaque();
      return var2.container();
   }

   private void layoutNormalOptions(OneColumnPanel var1) {
      for (Entry var3 : this.optionMap.entrySet()) {
         DeployOption var4 = (DeployOption)((OptionSelector)var3.getValue()).getOption();
         if (!var4.isAdvanced()) {
            this.layoutOption(var4, (JRadioButton)var3.getKey(), var1);
         }
      }
   }

   private void layoutAdvancedOptions(OneColumnPanel var1) {
      OneColumnPanel var2 = this.createEmptyColumn();
      boolean var3 = false;
      DeployControlPanel.AdvancedOptionsListener var4 = new DeployControlPanel.AdvancedOptionsListener();

      for (Entry var6 : this.optionMap.entrySet()) {
         DeployOption var7 = (DeployOption)((OptionSelector)var6.getValue()).getOption();
         if (var7.isAdvanced()) {
            JRadioButton var8 = (JRadioButton)var6.getKey();
            var8.addItemListener(var4);
            this.layoutOption(var7, var8, var2);
            if (var8.isSelected()) {
               var3 = true;
            }
         }
      }

      this.advancedOptionsDropDown = new DropDownPanel(InvisiblePanel.of(var2), Strings.get("Deploy.Page.Advanced"), var3);
      this.advancedOptionsDropDown.decorateTitle(ADVANCED_STYLE);
      var1.add(this.advancedOptionsDropDown.getDisplayer());
   }

   private OneColumnPanel createEmptyColumn() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setFill(OneColumnPanel.HORIZONTAL);
      var1.setWeightX(1.0);
      var1.withInsets(5, 0, 0, 0);
      return var1;
   }

   private void layoutOption(DeployOption var1, JRadioButton var2, OneColumnPanel var3) {
      var3.add(var2);
      var3.add(this.getAdditionalOptionUi(var1));
      var3.add(Box.createVerticalStrut(5));
   }

   private JComponent getAdditionalOptionUi(DeployOption var1) {
      JPanel var2 = new JPanel(new BorderLayout());
      var1.addAdditionalUi(var2);
      return var2;
   }

   private void createRadioButtons() {
      this.optionSelectors = Lists.newArrayList();

      for (final DeployOption var2 : this.options) {
         KongaRadioButton var3 = this.createRadioButton();
         ToggleButtonOptionSelector var4 = new ToggleButtonOptionSelector<>(var2, var3);
         this.optionSelectors.add(var4);
         if (var2 == this.preferredOption) {
            var3.setSelected(true);
            this.defaultFocusOwner = var3;
            var2.setActive(true);
         } else {
            var2.setActive(false);
         }

         var3.addSelectionChangeListener(new Receiver<Boolean>() {
            public void handle(Boolean var1) {
               var2.setActive(var1);
            }
         });
         this.optionMap.put(var3, var4);
      }

      ButtonUtils.makeMutuallyExclusive(this.optionMap.keySet());
   }

   private KongaRadioButton createRadioButton() {
      KongaRadioButton var1 = new KongaRadioButton();
      var1.setEnabled(false);
      var1.setOpaque(false);
      return var1;
   }

   private final class AdvancedOptionsListener implements ItemListener {
      private AdvancedOptionsListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         DeployOption var2 = DeployControlPanel.this.getActiveOption();
         if (var2 != null && var2.isAdvanced()) {
            DeployControlPanel.this.advancedOptionsDropDown.expand();
         }
      }
   }
}
