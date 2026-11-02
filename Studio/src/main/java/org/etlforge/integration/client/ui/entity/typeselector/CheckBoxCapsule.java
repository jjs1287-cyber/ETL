package org.etlforge.integration.client.ui.entity.typeselector;

import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;

final class CheckBoxCapsule extends MouseAdapter implements UiProvider {
   private final JCheckBox checkBox = new KongaCheckBox();
   private final KongaLabel label;

   public CheckBoxCapsule(EntityType var1) {
      EntityIcons var2 = EntityIcons.forType(var1);
      this.label = new KongaLabel(var1.getDisplayName(), var2.getDefaultIcon(EntityIcons.Size.SMALL), 2);
      TextStyles.DefaultCheckBoxText.makeOver(this.label);
      this.label.addMouseListener(this);
   }

   public boolean isSelected() {
      return this.checkBox.isSelected();
   }

   public void setSelected(boolean var1) {
      this.checkBox.setSelected(var1);
   }

   public void setEnabled(boolean var1) {
      this.checkBox.setEnabled(var1);
      this.label.setEnabled(var1);
   }

   public void addItemListener(ItemListener var1) {
      this.checkBox.addItemListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Horizontal.box().of(this.checkBox, this.label).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.checkBox.requestFocusInWindow();
   }

   @Override
   public void mouseClicked(MouseEvent var1) {
      if (this.checkBox.isEnabled()) {
         this.checkBox.setSelected(!this.checkBox.isSelected());
      }
   }

   public void setText(MnemonicString var1) {
      this.label.setText(var1);
      if (var1.getMnemonic() != 0) {
         this.label.setLabelForButton(this.checkBox);
         this.label.removeMouseListener(this);
      }
   }
}
