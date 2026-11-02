package org.etlforge.integration.client.ui.interchange.component.tree;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.data.entity.NameChangeListener;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.KongaButton;

final class ProcessFilterHeader implements UiProvider, Disposable {
   private final ActionListener closeListener;
   private final NameChangeListener nameListener;
   private final JLabel label;

   public ProcessFilterHeader(ProcessFilter var1, ActionListener var2) {
      this.closeListener = var2;
      this.label = new JLabel(var1.getProcess().getName());
      this.nameListener = new NameChangeListener(var1.getProcess()) {
         @Override
         protected void nameChanged(String var1, String var2x) {
            ProcessFilterHeader.this.label.setText(var2x);
         }
      };
   }

   @Override
   public void dispose() {
      this.nameListener.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      makeOver(this.label);
      Horizontal.box()
         .withBackground(Colors.ETLFORGE_ORANGE)
         .withBorder(new BorderBuilder().line().empty(4))
         .add(this.label)
         .glue()
         .add(this.closeButton())
         .decorate(var1);
   }

   private static void makeOver(JLabel var0) {
      WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var0);
      var0.setForeground(Color.WHITE);
   }

   private JButton closeButton() {
      KongaButton var1 = new KongaButton(CommonIcons.CLOSE_16).makeToolbarButton();
      var1.addActionListener(this.closeListener);
      var1.setToolTipText("Close the process view and return to the normal project view");
      return var1;
   }

   @Override
   public void requestFocus() {
   }
}
