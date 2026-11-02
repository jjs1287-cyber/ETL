package org.etlforge.application.ui;

import java.awt.Component;
import javax.swing.JCheckBox;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.HtmlLabel;

public class DoNotShowAgainQuestion {
   private final Object message;
   private final String title;
   private final BooleanPreference pref;
   private final JCheckBox doNotShowAgainCheck;
   private final Alert.Type messageType;
   private final Alert.Option options;
   private final Alert.Option initialOption;

   public DoNotShowAgainQuestion(Object var1, String var2, BooleanPreference var3, Alert.Option var4, Alert.Option var5, Alert.Type var6) {
      this.message = var1;
      this.title = var2;
      this.pref = var3;
      this.options = var4;
      this.initialOption = var5;
      this.messageType = var6;
      this.doNotShowAgainCheck = new KongaCheckBox(ApplicationResources.STRINGS.getString("DoNotAskAgain"));
   }

   public boolean ask(Component var1) {
      if (this.pref.get()) {
         Alert.Option var2 = Alert.ask(var1, this.createUi(), this.title, this.options, this.initialOption, this.messageType);
         if (var2.isAffirmative()) {
            if (this.doNotShowAgainCheck.isSelected()) {
               this.pref.set(false);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private UiProvider createUi() {
      return new UiProvider() {
         @Override
         public void decorate(UiCanvas var1) {
            OneColumnPanel var2 = new OneColumnPanel();
            var2.setWeightX(1.0);
            var2.withInsets(0, 0, 20, 0);
            if (DoNotShowAgainQuestion.this.message instanceof UiProvider) {
               var2.add((UiProvider)DoNotShowAgainQuestion.this.message);
            } else if (DoNotShowAgainQuestion.this.message instanceof Component) {
               var2.add((Component)DoNotShowAgainQuestion.this.message);
            } else {
               var2.add(new HtmlLabel(DoNotShowAgainQuestion.this.message.toString(), 400));
            }

            var2.withInsets(0, 0, 0, 0);
            var2.add(DoNotShowAgainQuestion.this.doNotShowAgainCheck);
            var2.decorate(var1);
         }

         @Override
         public void requestFocus() {
            DoNotShowAgainQuestion.this.doNotShowAgainCheck.requestFocus();
         }
      };
   }
}
