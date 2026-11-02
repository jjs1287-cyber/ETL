package org.etlforge.ui.util.option;

import com.google.common.base.Preconditions;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.focus.KongaFocusManager;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.util.event.ListenerManager;
import org.etlforge.util.event.ListenerManagerFactory;

public class InputOptionPanel<T> implements UiProvider, Disposable {
   private final InputOption<T>[] options;
   private final List<OptionSelector<InputOption<T>>> selectors;
   private final List<JRadioButton> buttons;
   private final ListenerManager<SelectedOptionListener> selectionListeners;
   private final JComponent display;
   private KongaFocusManager focusManager;

   public InputOptionPanel(InputOption<T>... var1) {
      Preconditions.checkArgument(var1.length > 0, "options cannot be empty");
      this.options = (InputOption<T>[])var1.clone();
      this.selectors = new ArrayList<>();
      this.buttons = new ArrayList<>();
      this.selectionListeners = ListenerManagerFactory.create(SelectedOptionListener.class);
      this.focusManager = new KongaFocusManager();
      this.createSelectors();
      this.display = this.createDisplay();
      this.focusManager.register();
   }

   private void createSelectors() {
      ButtonGroup var1 = new ButtonGroup();

      for (InputOption var5 : this.options) {
         this.createSelector(var5, var1);
      }
   }

   private void createSelector(final InputOption<T> var1, ButtonGroup var2) {
      KongaRadioButton var3 = new KongaRadioButton();
      var2.add(var3);
      var3.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1x) {
            var1.setActive(var1x);
            if (var1x) {
               InputOptionPanel.this.fireSelectionChanged(var1);
            }
         }
      });
      ToggleButtonOptionSelector var4 = new ToggleButtonOptionSelector<>(var1, var3);
      this.buttons.add(var3);
      this.selectors.add(var4);
   }

   private void fireSelectionChanged(InputOption<T> var1) {
      if (!this.selectionListeners.isEmpty()) {
         Option[] var2 = new Option[]{var1};
         this.selectionListeners.call().selectionChanged(var2);
      }
   }

   private JComponent createDisplay() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 0, 0, 0);
      var1.setFillOut();
      Insets var2 = new Insets(0, 0, 5, 0);
      Insets var3 = new Insets(0, 30, 10, 0);
      int var4 = 0;

      for (InputOption var8 : this.options) {
         this.addInputUi(var1, var8, var2, var3, var4);
         var4++;
      }

      return BorderLayoutBuilder.fillWith(var1);
   }

   private void addInputUi(OneColumnPanel var1, UiProvider var2, Insets var3, Insets var4, int var5) {
      OneColumnPanel var6 = new OneColumnPanel();
      var6.withInsets(var3);
      var6.setWeightX(1.0);
      JRadioButton var7 = this.buttons.get(var5);
      var6.add(var7);
      JPanel var8 = BorderLayoutBuilder.fillWith(var2);
      var6.withInsets(var4);
      this.focusManager.addFocusRoot(var8, new InputOptionPanel.PanelFocusListener(var7));
      var6.fillOut(var8);
      var1.setWeightY(this.getOptionWeightY(var5));
      var1.add(var6);
   }

   protected double getOptionWeightY(int var1) {
      return 1.0;
   }

   public final void setSelectedOption(InputOption<T> var1) {
      int var2 = 0;

      for (InputOption var6 : this.options) {
         if (var6 == var1) {
            this.buttons.get(var2).setSelected(true);
            break;
         }

         var2++;
      }
   }

   @Override
   public final void decorate(UiCanvas var1) {
      var1.display(this.display);
   }

   public final void setBorder(Border var1) {
      this.display.setBorder(var1);
   }

   @Override
   public final void requestFocus() {
      int var1 = 0;

      for (OptionSelector var3 : this.selectors) {
         if (var3.isSelected()) {
            this.options[var1].requestFocus();
            break;
         }

         var1++;
      }
   }

   public final T getValue() {
      for (OptionSelector var2 : this.selectors) {
         if (var2.isSelected()) {
            InputOption var3 = (InputOption)var2.getOption();
            return (T)var3.getValue();
         }
      }

      return null;
   }

   public final void disable() {
      for (JRadioButton var2 : this.buttons) {
         var2.setSelected(false);
         var2.setEnabled(false);
      }
   }

   @Override
   public final void dispose() {
      if (this.focusManager != null) {
         this.focusManager.unregister();
         this.focusManager = null;
      }
   }

   public final void addSelectionListener(SelectedOptionListener var1) {
      this.selectionListeners.add(var1);
   }

   public final void removeSelectionListener(SelectedOptionListener var1) {
      this.selectionListeners.remove(var1);
   }

   private final class PanelFocusListener extends FocusAdapter {
      private final JRadioButton radioButton;

      public PanelFocusListener(JRadioButton var2) {
         this.radioButton = var2;
      }

      @Override
      public void focusGained(FocusEvent var1) {
         if (this.radioButton.isEnabled()) {
            boolean var2 = this.radioButton.isSelected();
            this.radioButton.setSelected(true);
            if (!var2 && var1.getSource() instanceof JButton) {
               ((JButton)var1.getSource()).doClick();
            }
         }
      }
   }
}
