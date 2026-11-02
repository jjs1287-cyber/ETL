package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import java.util.Collections;
import java.util.List;
import javax.swing.JCheckBox;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.client.ui.interchange.entity.webservice.prefs.WsdlEditSideEffectsWarningPreference;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.util.name.NameSorter;

final class SideEffectsMessage {
   private final IntegrationEntityList list;
   private final JCheckBox doNotShowAgain;

   public static Boolean isGreenlighted(final WebServiceCall var0, final List<WebServiceCall> var1) {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            SideEffectsMessage var1x = new SideEffectsMessage(KongaListUtils.filter(var1, Predicates.not(Predicates.equalTo(var0))));
            return var1x.ask().isContinue();
         }
      });
   }

   private SideEffectsMessage(List<WebServiceCall> var1) {
      Collections.sort(var1, NameSorter.ignoreCase());
      this.list = new IntegrationEntityList(var1);
      this.doNotShowAgain = new KongaCheckBox(ApplicationResources.STRINGS.getString("DoNotShowAgain"));
   }

   public Answer ask() {
      if (this.shouldAsk()) {
         Answer var1 = Ask.continueOrCancel(this.getUi(), Strings.get("EditWsdl.SideEffects.Title"));
         if (var1.isContinue()) {
            this.updatePreference();
         }

         return var1;
      } else {
         return Answer.AFFIRMATIVE;
      }
   }

   private boolean shouldAsk() {
      return WsdlEditSideEffectsWarningPreference.get();
   }

   private void updatePreference() {
      if (this.doNotShowAgain.isSelected()) {
         WsdlEditSideEffectsWarningPreference.INSTANCE.set(false);
      }
   }

   private UiProvider getUi() {
      BorderLayoutBuilder var1 = this.layoutMessage();
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.center(var1).south(this.doNotShowAgain);
      return var2;
   }

   private BorderLayoutBuilder layoutMessage() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 5);
      var1.northToSouth(Strings.get("EditWsdl.SideEffects.Message"), this.list, ApplicationResources.STRINGS.getString("Continue?"));
      return var1;
   }
}
