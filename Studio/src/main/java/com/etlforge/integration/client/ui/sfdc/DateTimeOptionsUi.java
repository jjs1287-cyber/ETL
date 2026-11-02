package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Optional;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class DateTimeOptionsUi implements UiProvider {
   private final KongaCheckBox noConversion = new KongaCheckBox("The source file already uses the correct format");
   private final TimeZoneSelector tzSelector = new TimeZoneSelector();
   private final KongaCheckBox europeanDateFormat = new KongaCheckBox("The source file uses &European date format (dd/mm/yyyy)");
   private Border border = StyleSheet.getDefaultTitleBorder("Date & Time");

   public DateTimeOptionsUi() {
      this(Optional.absent());
   }

   public DateTimeOptionsUi(Optional<DateConversionParams> var1) {
      this.configureNoConversionChoice();
      this.applyExistingParams(var1);
      this.setConversionEnabled(var1.isPresent());
   }

   private void configureNoConversionChoice() {
      this.noConversion.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            boolean var2 = !var1;
            DateTimeOptionsUi.this.setConversionEnabled(var2);
            if (var2) {
               DateTimeOptionsUi.this.tzSelector.requestFocus();
            }
         }
      });
      this.noConversion
         .setToolTipText(
            "<html>Use this option if dateTime fields in your source data already are in  the format required by Salesforce.<br><br><b>Examples:</b><br>2012-02-28T20:00:00Z<br>2012-02-28T12:00:00-08:00</html>"
         );
   }

   private void setConversionEnabled(boolean var1) {
      this.tzSelector.setEnabled(var1);
      this.europeanDateFormat.setEnabled(var1);
   }

   private void applyExistingParams(Optional<DateConversionParams> var1) {
      if (var1.isPresent()) {
         this.noConversion.setSelected(false);
         this.tzSelector.setSelectedTimeZoneId(((DateConversionParams)var1.get()).timeZoneId);
         this.europeanDateFormat.setSelected(((DateConversionParams)var1.get()).isEuropeanFormat());
      } else {
         this.noConversion.setSelected(true);
         this.europeanDateFormat.setSelected(false);
      }
   }

   public void registerWithParent(InputPanel var1) {
      var1.addInputField(new CheckBoxInputField("No Conversion", this.noConversion));
      var1.addInputField(new CheckBoxInputField("Date Format", this.europeanDateFormat));
      this.tzSelector.registerWithParent(var1);
   }

   public boolean isConversionNeeded() {
      return !this.noConversion.isSelected();
   }

   public Optional<DateConversionParams> getConversionParams() {
      if (this.isConversionNeeded()) {
         TimeZoneId var1 = this.tzSelector.getSelectedTimeZoneId();
         DateConversionParams var2 = this.europeanDateFormat.isSelected() ? DateConversionParams.europeanFormat(var1) : DateConversionParams.usFormat(var1);
         return Optional.of(var2);
      } else {
         return Optional.absent();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(0, 0, 16, 0).withBorder(this.border);
      var2.setWeightX(1.0);
      var2.add(WizardStyleSheet.SMALL_CAPTION_STYLE.makeLabel("Convert date and/or date-time fields to the format expected by SFDC"));
      var2.add(this.noConversion);
      TwoColumnPanel var3 = this.layoutConversionFields();
      var2.add(var3.invisible());
      var1.display(var2.invisible());
   }

   private TwoColumnPanel layoutConversionFields() {
      return TwoColumnPanel.forFormEntry(5, 10).addRow(this.tzSelector).addRow(null, this.europeanDateFormat);
   }

   public DateTimeOptionsUi withoutBorder() {
      this.border = null;
      return this;
   }

   @Override
   public void requestFocus() {
      this.noConversion.requestFocusInWindow();
   }
}
