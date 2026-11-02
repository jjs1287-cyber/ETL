package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JComponent;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringCollection;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.button.ToggleButtonKeySelector;

public final class LocationTypeSelectorUi implements UiProvider {
   private final ImmutableList<DataLocationType> types;
   private final ImmutableList<KongaRadioButton> choices;
   private Receiver<DataLocationType> selectionCallback = NoReceiver.create();
   private Optional<DataLocationType> selectedType = Optional.absent();

   public LocationTypeSelectorUi(Collection<DataLocationType> var1) {
      Preconditions.checkArgument(var1.size() > 1, "At least two types must be provided, otherwise there is nothing to select!");
      this.types = getDisplayOrder(var1);
      this.choices = this.createChoices();
   }

   private static ImmutableList<DataLocationType> getDisplayOrder(Collection<DataLocationType> var0) {
      LocationTypeSelectorUi.Order var1 = new LocationTypeSelectorUi.Order();
      return var1.immutableSortedCopy(var0);
   }

   private ImmutableList<KongaRadioButton> createChoices() {
      ArrayList var1 = Lists.newArrayList();
      MnemonicStringCollection var2 = new MnemonicStringCollection();

      for (DataLocationType var4 : this.types) {
         MnemonicString var5 = var2.autoTranslate(var4.toString());
         KongaRadioButton var6 = new KongaRadioButton(var5);
         var6.putClientProperty("konga.type", var4);
         var1.add(var6);
      }

      ButtonUtils.makeMutuallyExclusive(var1);
      ButtonUtils.addItemListener(new LocationTypeSelectorUi.SelectionListener(), var1);
      return ImmutableList.copyOf(var1);
   }

   public void setSelectionCallback(Receiver<DataLocationType> var1) {
      this.selectionCallback = Preconditions.checkNotNull(var1);
      var1.handle(this.selectedType.isPresent() ? this.selectedType.get() : null);
   }

   public Optional<DataLocationType> getSelectedType() {
      return this.selectedType;
   }

   @Override
   public void decorate(UiCanvas var1) {
      LocationTypeSelectorUi.Layout var2 = new LocationTypeSelectorUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.choices.get(0).requestFocusInWindow();
   }

   public void selectFirst() {
      this.choices.get(0).setSelected(true);
   }

   public void installKeyNavigation(JComponent var1) {
      ToggleButtonKeySelector.install(this.choices, var1);
   }

   private class Layout {
      private final UiProvider ui;

      public Layout() {
         this.ui = Vertical.grid(5).of(LocationTypeSelectorUi.this.choices.toArray());
      }

      public void decorate(UiCanvas var1) {
         this.ui.decorate(var1);
      }
   }

   private static class Order extends Ordering<DataLocationType> {
      private Order() {
      }

      public int compare(DataLocationType first, DataLocationType second) {
         return first.toString().compareToIgnoreCase(second.toString());
      }
   }

   private class SelectionListener implements ItemListener {
      private SelectionListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         if (var1.getStateChange() == 1) {
            KongaRadioButton var2 = (KongaRadioButton)var1.getSource();
            DataLocationType var3 = (DataLocationType)var2.getClientProperty("konga.type");
            LocationTypeSelectorUi.this.selectedType = Optional.of(var3);
            LocationTypeSelectorUi.this.selectionCallback.handle(var3);
         }
      }
   }
}
