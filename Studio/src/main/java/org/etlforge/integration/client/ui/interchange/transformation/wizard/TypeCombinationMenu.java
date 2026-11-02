package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import com.google.common.collect.ImmutableSet;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.etlforge.collections.Pair;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class TypeCombinationMenu {
   private final ImmutableSet<WizardType> inputTypes;
   private final ImmutableSet<WizardType> outputTypes;
   private Receiver<Pair<WizardType, WizardType>> callback;

   public TypeCombinationMenu(List<WizardType> var1, List<WizardType> var2) {
      this.inputTypes = ImmutableSet.copyOf(var1);
      this.outputTypes = ImmutableSet.copyOf(var2);
      this.callback = NoReceiver.create();
   }

   public void setCallback(Receiver<Pair<WizardType, WizardType>> var1) {
      this.callback = var1;
   }

   public void show(JComponent var1, int var2, int var3) {
      KongaPopupMenu var4 = this.createPopup();
      var4.show(var1, var2, var3);
   }

   private KongaPopupMenu createPopup() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      int var2 = 0;

      for (WizardType var4 : this.inputTypes) {
         if (var4 != WizardTypes.EDI) {
            for (WizardType var6 : this.outputTypes) {
               if (var6 != WizardTypes.EDI) {
                  var1.add(new TypeCombinationMenu.Combination(var2, var4, var6));
                  var2++;
               }
            }
         }
      }

      return var1;
   }

   private static String getMnemonic(int var0) {
      if (var0 < 10) {
         return "&" + var0;
      }

      char var1 = (char)(65 + (var0 - 10));
      return var1 <= 'Z' ? "&" + var1 : "";
   }

   private final class Combination extends AbstractAction {
      private final Pair<WizardType, WizardType> pair;

      public Combination(int var2, WizardType var3, WizardType var4) {
         super(TypeCombinationMenu.getMnemonic(var2) + " " + var3.getName() + " - " + var4.getName());
         this.pair = Pair.of(var3, var4);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TypeCombinationMenu.this.callback.handle(this.pair);
      }
   }
}
