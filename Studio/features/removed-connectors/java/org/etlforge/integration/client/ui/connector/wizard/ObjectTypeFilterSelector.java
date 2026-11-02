package org.etlforge.integration.client.ui.connector.wizard;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import java.awt.Component;
import javax.annotation.Nullable;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.SingleSelectionBinding;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.util.Property;

final class ObjectTypeFilterSelector implements UiProvider {
   @Nullable
   private final KongaComboBox dropdown;

   public static ObjectTypeFilterSelector create(ObjectSelectionModel var0) {
      ObjectTypeFilterSelector var1 = new ObjectTypeFilterSelector(var0);
      if (var1.dropdown != null) {
         Property var2 = var0.getProperty("type_filter");
         SingleSelectionBinding.of(var2, var1.dropdown, ObjectTypeFilter.class).syncUi();
      }

      return var1;
   }

   private ObjectTypeFilterSelector(ObjectSelectionModel var1) {
      ImmutableList var2 = var1.getTypeFilters();
      if (var2.isEmpty()) {
         this.dropdown = null;
      } else {
         KongaComboBox var3 = createDropDown(var2);
         this.dropdown = var3;
      }
   }

   private static KongaComboBox createDropDown(ImmutableList<ObjectTypeFilter> var0) {
      assert !var0.isEmpty();
      KongaComboBox var1 = new KongaComboBox(var0);
      var1.setRenderer(new ObjectTypeFilterSelector.Renderer());
      int var2 = 0;

      for (ObjectTypeFilter var4 : var0) {
         int var5 = var4.getName().length();
         var2 = Ints.max(var2, var5);
      }

      final String var6 = Strings.repeat("W", var2);
      var1.setPrototypeDisplayValue(new ObjectTypeFilter() {
         @Override
         public String getName() {
            return var6;
         }

         public boolean apply(ConnectorObject var1) {
            throw new UnsupportedOperationException();
         }
      });
      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.dropdown != null) {
         JLabel var2 = this.createLabel();
         Horizontal.box().of(var2, this.dropdown).decorate(var1);
      }
   }

   private JLabel createLabel() {
      KongaLabel var1 = KongaLabel.withMnemonic("&Objects to show: ");
      WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var1);
      var1.setLabelFor(this.dropdown);
      return var1;
   }

   @Override
   public void requestFocus() {
      if (this.dropdown != null) {
         this.dropdown.requestFocusInWindow();
      }
   }

   private static final class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         String var6 = ((ObjectTypeFilter)var2).getName();
         this.setText(var6);
         return this;
      }
   }
}
