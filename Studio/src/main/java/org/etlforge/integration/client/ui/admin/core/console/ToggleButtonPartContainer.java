package org.etlforge.integration.client.ui.admin.core.console;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList.Builder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.ButtonUtils;

final class ToggleButtonPartContainer implements AdminConsolePartContainer {
   private final ImmutableList<AdminConsolePart> parts;
   private final ImmutableList<JToggleButton> buttons;
   private final CardPanel contentPane = new CardPanel(new JPanel());

   public ToggleButtonPartContainer(List<? extends AdminConsolePart> var1) {
      Preconditions.checkArgument(var1.size() > 1, "parts must contain at least 2 items");
      this.parts = ImmutableList.copyOf(var1);
      this.buttons = this.createButtons(var1);
      this.buttons.get(0).doClick();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.contentPane.container(), false);
      this.setInitialActiveState();
   }

   private ImmutableList<JToggleButton> createButtons(List<? extends AdminConsolePart> var1) {
      Builder var2 = ImmutableList.builder();

      for (AdminConsolePart var4 : var1) {
         JToggleButton var5 = this.createButton(var4);
         KongaScrollPane var6 = new KongaScrollPane(var4);
         var6.getViewport().setOpaque(false);
         this.contentPane.add(var6, var5.getText());
         var2.add(var5);
      }

      ImmutableList var7 = var2.build();
      ButtonUtils.addActionListener(new ToggleButtonPartContainer.Switcher(), var7);
      ButtonUtils.makeMutuallyExclusive(var7);
      return var7;
   }

   private JToggleButton createButton(AdminConsolePart var1) {
      MnemonicString var2 = ApplicationResources.MNEMONIC_FACTORY.fromString(var1.getTitle());
      JToggleButton var3 = new JToggleButton(var2.getText(), var1.getIcon());
      ButtonUtils.applyMnemonics(var3, var2);
      return var3;
   }

   @Override
   public AdminConsolePart getActivePart() {
      int var1 = Iterables.indexOf(this.buttons, ButtonUtils.TOGGLE_BUTTON_SELECTED);
      return var1 >= 0 ? this.parts.get(var1) : null;
   }

   @Override
   public JComponent getToolBarContribution() {
      return Grid.oneRow(10).of(this.buttons).deeplyNonOpaque().invisible();
   }

   @Override
   public void serverConnectionChanged(ServerConnectionEvent var1) {
      for (AdminConsolePart var3 : this.parts) {
         var3.serverConnectionChanged(var1);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.contentPane.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   private void setInitialActiveState() {
      boolean var1 = true;

      for (AdminConsolePart var3 : this.parts) {
         var3.setActive(var1);
         var1 = false;
      }
   }

   private class Switcher implements ActionListener {
      private Switcher() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ToggleButtonPartContainer.this.contentPane.show(var1.getActionCommand());

         for (int var2 = 0; var2 < ToggleButtonPartContainer.this.parts.size(); var2++) {
            ToggleButtonPartContainer.this.parts.get(var2).setActive(ToggleButtonPartContainer.this.buttons.get(var2).isSelected());
         }
      }
   }
}
