package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringCollection;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.property.ComboBoxBinding;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.Property;

final class LocationTypeSelector implements UiProvider {
   private final LocationPageModel<?> model;
   private final RadioButtonGroupPanel<DataLocationType> radioButtons;
   private final EnumComboBox<DataLocationType> dropdown;
   private final KongaCheckBox utf8;

   public LocationTypeSelector(LocationPageModel<?> var1) {
      this.model = var1;
      ImmutableList var2 = var1.getLocationTypes();
      if (var2.size() <= 3) {
         this.radioButtons = this.createRadioButtons(var2);
         this.dropdown = null;
      } else {
         this.dropdown = this.createDropdowns(var2);
         this.radioButtons = null;
      }

      this.utf8 = this.createUtf8Box();
      this.bindToModel();
   }

   private RadioButtonGroupPanel<DataLocationType> createRadioButtons(Iterable<DataLocationType> var1) {
      MnemonicStringCollection var2 = new MnemonicStringCollection();
      var2.markCharactersAsUsed(this.model.getEntityType().getPlural().charAt(0), 'n', 'x', 'B');
      RadioButtonGroupPanel var3 = new RadioButtonGroupPanel();

      for (DataLocationType var5 : var1) {
         String var6 = this.getDisplayName(var5);
         MnemonicString var7 = var2.autoTranslate(var6);
         var3.addOption(var5, var7.getRaw());
      }

      return var3;
   }

   private String getDisplayName(DataLocationType var1) {
      String var2 = var1.toString();
      if (var1 == DataLocationType.Database) {
         var2 = var2 + " (Oracle, SQL Server, MySQL, etc)";
      }

      return var2;
   }

   private EnumComboBox<DataLocationType> createDropdowns(Iterable<DataLocationType> var1) {
      EnumComboBox var2 = EnumComboBox.of(var1);
      var2.generateDisplayStrings(new Function<DataLocationType, String>() {
         public String apply(DataLocationType var1) {
            return LocationTypeSelector.this.getDisplayName(var1);
         }
      });
      return var2;
   }

   private KongaCheckBox createUtf8Box() {
      String var1 = this.model.getEntityType() == EntityType.Source ? "Read" : "Write";
      return new KongaCheckBox(var1 + " the file as UTF-8");
   }

   private void bindToModel() {
      Property var1 = this.model.getProperty("type");
      Bindings.of(this.bindSelectedType(var1), BooleanBinding.bind(this.model, "utf-8", this.utf8), new LocationTypeSelector.Utf8AvailableBinding(var1))
         .syncUi();
   }

   private Binding bindSelectedType(Property<DataLocationType> var1) {
      return this.radioButtons == null ? new ComboBoxBinding<>(this.dropdown, this.model.getLocationTypes(), var1) : this.radioButtons.bindWith(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.radioButtons == null) {
         BoxBuilder var2 = Horizontal.box().centerAligned().add(this.dropdown).rigid(10).add(this.utf8);
         Vertical.column().withInsets(0, 0, 12, 0).of(var2, new JSeparator()).decorate(var1);
      } else {
         JPanel var4 = InvisiblePanel.of(this.utf8);
         UiProvider var3 = this.radioButtons.oneRowLayout();
         Vertical.column().withInsets(0, 0, 12, 0).of(var3, new JSeparator(), var4, new JSeparator()).decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      if (this.radioButtons == null) {
         this.dropdown.requestFocusInWindow();
      } else {
         this.radioButtons.requestFocus();
      }
   }

   private class Utf8AvailableBinding extends AbstractBinding<DataLocationType> {
      public Utf8AvailableBinding(Property<DataLocationType> var2) {
         super(var2);
      }

      protected void updateUiWithValue(DataLocationType var1) {
         LocationTypeSelector.this.utf8.setEnabled(var1.isFileBased());
      }
   }
}
