package org.etlforge.integration.client.ui.deploy.validation;

import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.validation.EntityValidationResultDisplayerFactory;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.label.KongaLabel;

final class ResultDisplayer {
   private final EntityCollectionValidationResult resultToDisplay;
   private final String title;
   private final String caption;
   private final String message;

   ResultDisplayer(EntityCollectionValidationResult var1, String var2, String var3, String var4) {
      this.resultToDisplay = var1;
      this.title = var2;
      this.caption = var3;
      this.message = var4;
   }

   void showAsError() {
      Alert.message(null, this.createDisplay(), this.title, Alert.Type.ERROR);
   }

   boolean promptForWarning() {
      Answer var1 = Ask.yesNoOrCancel(null, this.createDisplay(), this.title, Alert.Option.YES, Alert.Type.WARNING);
      return var1.isYes();
   }

   private Object createDisplay() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setFillNone();
      this.createAndAddMessageArea(var1);
      this.createAndAddResultArea(var1);
      return var1;
   }

   private void createAndAddMessageArea(OneColumnPanel var1) {
      var1.withInsets(5, 5, 5, 5);
      var1.add(TextStyles.DefaultBold.makeLabel(this.caption));
      var1.withInsets(5, 5, 0, 5);
      KongaLabel var2 = TextStyles.InfoText.makeLabel(this.message);
      var1.add(var2);
   }

   private void createAndAddResultArea(OneColumnPanel var1) {
      var1.withInsets(3, 5, 5, 5);
      UiProvider var2 = EntityValidationResultDisplayerFactory.newTreeDisplayer(this.resultToDisplay);
      var1.fillOut(var2);
   }
}
