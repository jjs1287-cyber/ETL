package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;

final class HeaderInputChoice implements HeaderInput {
   private final Receiver<Boolean> changeListener;
   private HeaderInput selectedInput;
   private final Map<KongaRadioButton, HeaderInput> choiceMap;

   public HeaderInputChoice(Receiver<Boolean> var1, HeaderInput... var2) {
      this.changeListener = var1;
      this.choiceMap = Maps.newLinkedHashMap();
      boolean var3 = true;

      for (HeaderInput var7 : var2) {
         KongaRadioButton var8 = new KongaRadioButton("", var3);
         this.choiceMap.put(var8, var7);
         var7.addFocusListener(new HeaderInputChoice.FocusActivator(var8, var7));
         if (var3) {
            this.setSelectedInput(var7);
            var3 = false;
         }
      }

      ButtonUtils.addActionListener(new HeaderInputChoice.Listener(), this.choiceMap.keySet());
      ButtonUtils.makeMutuallyExclusive(this.choiceMap.keySet());
   }

   private void setSelectedInput(HeaderInput var1) {
      this.selectedInput = var1;
      this.changeListener.handle(var1.isValid());
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = new TwoColumnPanel();
      var2.withLeftAnchor(GridBagLayoutPanel.Anchor.NORTHWEST).withRightAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);
      var2.withLeftInsets(0, 0, 10, 5).withLeftFill(GridBagLayoutPanel.Fill.NONE).withLeftWeights(0.0, 0.0);
      var2.withLeftInsets(0, 0, 10, 0).withRightFill(GridBagLayoutPanel.Fill.BOTH).withRightWeights(1.0, 0.0);

      for (Entry var4 : this.choiceMap.entrySet()) {
         var2.addRow(((KongaRadioButton)var4.getKey()).asUiProvider(), (UiProvider)var4.getValue());
      }

      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.selectedInput.requestFocus();
   }

   @Override
   public boolean isValid() {
      return this.selectedInput.isValid();
   }

   @Override
   public List<String> getHeaders() {
      return this.selectedInput.getHeaders();
   }

   public HeaderInput getChoice() {
      return this.selectedInput;
   }

   @Override
   public void addFocusListener(FocusListener var1) {
   }

   private class FocusActivator extends FocusAdapter {
      private final KongaRadioButton button;
      private final HeaderInput input;

      public FocusActivator(KongaRadioButton var2, HeaderInput var3) {
         this.button = var2;
         this.input = var3;
      }

      @Override
      public void focusGained(FocusEvent var1) {
         if (!this.button.isSelected()) {
            this.button.setSelected(true);
            HeaderInputChoice.this.setSelectedInput(this.input);
         }
      }
   }

   private class Listener implements ActionListener {
      private Listener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         HeaderInput var2 = HeaderInputChoice.this.choiceMap.get(var1.getSource());
         assert var2 != null : "Unknown button: " + var1.getSource();
         this.activate(var2);
      }

      private void activate(HeaderInput var1) {
         var1.requestFocus();
         HeaderInputChoice.this.changeListener.handle(var1.isValid());
      }
   }
}
