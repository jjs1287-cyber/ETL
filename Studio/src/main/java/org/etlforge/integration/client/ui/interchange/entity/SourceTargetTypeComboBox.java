package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.undo.UndoableEdit;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.metadata.DataLocationMetaData;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.LocationDecoratedIcons;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;

final class SourceTargetTypeComboBox {
   private final KongaComboBox comboBox;

   private SourceTargetTypeComboBox(Locatable var1, SourceTarget var2) {
      ComboBoxModel var3 = this.getModel(var1, var2);
      this.comboBox = new KongaComboBox(var3);
      this.comboBox.setRenderer(new SourceTargetTypeComboBox.Renderer(var2));
   }

   public static SourceTargetTypeComboBox getTypesComboBox(Locatable var0) {
      Preconditions.checkNotNull(var0, "locatable");
      if (var0 instanceof Source) {
         return new SourceTargetTypeComboBox(var0, SourceTarget.Source);
      } else if (var0 instanceof Target) {
         return new SourceTargetTypeComboBox(var0, SourceTarget.Target);
      } else {
         throw new RuntimeException("Unexpected Locatable: " + var0.getClass().getName());
      }
   }

   private ComboBoxModel getModel(Locatable var1, SourceTarget var2) {
      List var3 = this.getAllowedTypes(var1, var2);
      return new DefaultComboBoxModel<>(var3.toArray());
   }

   private List<DataLocationType> getAllowedTypes(Locatable var1, SourceTarget var2) {
      DataLocationMetaData var3 = DataLocationMetaData.getInstance();
      DataLocationType[] var4 = var2 == SourceTarget.Source ? var3.getSupportedSourceTypes() : var3.getSupportedTargetTypes();
      ArrayList var5 = Lists.newArrayList();

      for (DataLocationType var9 : var4) {
         if (var1.isLocationTypeAllowed(var9)) {
            var5.add(var9);
         }
      }

      return var5;
   }

   public void setDefaultSelection() {
      if (this.comboBox.getItemCount() == 1) {
         this.comboBox.setSelectedIndex(0);
      } else {
         this.comboBox.setSelectedItem(null);
      }
   }

   public void setSelectedLocationType(DataLocationType var1) {
      this.comboBox.setSelectedItem(var1);
   }

   public DataLocationType getSelectedLocationType() {
      return (DataLocationType)this.comboBox.getSelectedItem();
   }

   public void setSelectionReceiver(int var1, final Receiver<DataLocationType> var2) {
      this.comboBox.setSelectionReceiver(var1, new Receiver<Object>() {
         @Override
         public void handle(Object var1) {
            var2.handle((DataLocationType)var1);
         }
      });
   }

   public void forceDelayedSelection() {
      this.comboBox.forceDelayedSelection();
   }

   public ComboBoxInputField asInputField(String var1, String var2) {
      KongaLabel var3 = var2 != null && var2.length() > 0 ? KongaLabel.withMnemonic(var2) : null;
      return new ComboBoxInputField(var1, this.comboBox, var3) {
         @Override
         protected UndoableEdit createUndoableEdit(Object var1, Object var2x) {
            return var1 == null ? null : super.createUndoableEdit(var1, var2x);
         }
      };
   }

   private static class Renderer extends DefaultListCellRenderer {
      private final LocationDecoratedIcons icons;
      private final SourceTarget st;

      public Renderer(SourceTarget var1) {
         this.st = var1;
         this.icons = var1 == SourceTarget.Source ? LocationDecoratedIcons.forSource() : LocationDecoratedIcons.forTarget();
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof DataLocationType) {
            this.setIcon(this.icons.getIcon((DataLocationType)var2, EntityIcons.Size.SMALL));
         } else {
            this.setIcon(null);
            this.setText("Select a " + this.st + " type  ");
         }

         return this;
      }
   }
}
