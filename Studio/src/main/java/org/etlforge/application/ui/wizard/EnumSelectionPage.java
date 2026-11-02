package org.etlforge.application.ui.wizard;

import com.google.common.collect.Iterables;
import java.awt.EventQueue;
import java.util.EnumSet;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.Empty;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringCollection;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class EnumSelectionPage<E extends Enum<E>> extends AbstractWizardPage {
   private final RadioButtonGroupPanel<E> ui;

   public static <F extends Enum<F>> EnumSelectionPage<F> allOf(Wizard var0, String var1, Class<F> var2) {
      return new EnumSelectionPage<>(var0, var1, var2);
   }

   public static <F extends Enum<F>> EnumSelectionPage<F> of(Wizard var0, String var1, Iterable<F> var2) {
      return new EnumSelectionPage<>(var0, var1, var2);
   }

   public EnumSelectionPage(Wizard var1, String var2, Class<E> var3) {
      this(var1, var2, EnumSet.allOf(var3));
   }

   public EnumSelectionPage(Wizard var1, String var2, Iterable<E> var3) {
      super(var1, var2);
      this.ui = this.createUi(var3);
      this.setCanContinue(true);
   }

   private RadioButtonGroupPanel<E> createUi(Iterable<E> var1) {
      MnemonicStringCollection var2 = new MnemonicStringCollection();
      RadioButtonGroupPanel var3 = new RadioButtonGroupPanel();

      for (Enum var5 : var1) {
         MnemonicString var6 = var2.autoTranslate(var5.toString());
         var3.addOption(var5, var6.getRaw());
      }

      var3.setSelectedValue(Iterables.get(var1, 0));
      return var3;
   }

   public final E getSelectedValue() {
      return this.ui.getSelection();
   }

   public final void setSelectedValue(E var1) {
      this.ui.setSelectedValue((E)var1);
   }

   public final boolean contains(E var1) {
      return this.ui.contains((E)var1);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle());
      var1.add(this.wrapUi(), true, false);
      return var1.asNorthBorderLayout();
   }

   private JPanel wrapUi() {
      JPanel var1 = InvisiblePanel.of(this.ui.oneRowLayout());
      var1.setBorder(Empty.border(8, 16, 0, 0));
      return var1;
   }

   @Override
   public final void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EnumSelectionPage.this.ui.requestFocus();
         }
      });
   }
}
