package com.etlforge.integration.client.ui.sfdc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class TimeZoneSelector implements UiProvider {
   private final JLabel caption = new JLabel("Specify the time zone for the dates in the source file:");
   private final KongaCheckBox utc = new KongaCheckBox("&GMT");
   private final KongaCheckBox local = new KongaCheckBox(ClientInfo.isDataLoader() ? "&Local time" : "&Local server time");
   private final KongaCheckBox select = new KongaCheckBox("&Select a time zone:");
   private final TimeZoneSelectionWidget selectionWidget = this.createSelectionWidget();

   public TimeZoneSelector() {
      this.local.setSelected(true);
      ButtonUtils.makeMutuallyExclusive(this.utc, this.local, this.select);
      this.select.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TimeZoneSelector.this.selectionWidget.requestFocus();
         }
      });
   }

   private TimeZoneSelectionWidget createSelectionWidget() {
      TimeZoneListSelector var1 = new TimeZoneListSelector();
      var1.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent var1) {
            if (!TimeZoneSelector.this.select.isSelected()) {
               TimeZoneSelector.this.select.setSelected(true);
            }
         }
      });
      return var1;
   }

   public void setCaption(String var1) {
      this.caption.setText(var1);
   }

   public void setSelectedTimeZoneId(TimeZoneId var1) {
      if (var1 == TimeZoneId.SERVER_LOCAL) {
         this.local.setSelected(true);
      } else if (var1 == TimeZoneId.UTC) {
         this.utc.setSelected(true);
      } else {
         this.select.setSelected(true);
         this.selectionWidget.setSelectedTimeZoneId(var1);
      }
   }

   public TimeZoneId getSelectedTimeZoneId() {
      if (this.local.isSelected()) {
         return TimeZoneId.SERVER_LOCAL;
      } else {
         return this.utc.isSelected() ? TimeZoneId.UTC : this.selectionWidget.getSelectedTimeZoneId();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(0, 0, 10, 0).withBorder(StyleSheet.getDefaultTitleBorder("Time Zone")).setWeightX(1.0);
      var2.addAll(this.caption, this.local, this.utc);
      var2.withInsets(0, 0, 8, 0);
      var2.add(this.select);
      var2.add(BoxBuilder.horizontal().rigid(20).add(this.selectionWidget));
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.local.requestFocusInWindow();
   }

   public void setEnabled(boolean var1) {
      this.caption.setEnabled(var1);
      this.local.setEnabled(var1);
      this.utc.setEnabled(var1);
      this.select.setEnabled(var1);
      this.selectionWidget.setEnabled(var1);
   }

   public void registerWithParent(InputPanel var1) {
      var1.addInputField(new CheckBoxInputField("Time Zone Mode", this.utc));
      var1.addInputField(new CheckBoxInputField("Time Zone Mode", this.local));
      var1.addInputField(new CheckBoxInputField("Time Zone Mode", this.select));
      this.selectionWidget.registerWithParent(var1);
   }
}
