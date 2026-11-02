package org.etlforge.integration.client.ui.interchange.entity.webservice.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.KeyValueToolTip;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.combobox.KongaComboBox;

public final class WebServiceOperationComboBox implements UiProvider {
   private final KongaComboBox comboBox;
   private final String caption;

   public WebServiceOperationComboBox() {
      this(null, null);
   }

   public WebServiceOperationComboBox(String var1) {
      this(null, var1);
   }

   public WebServiceOperationComboBox(WebServiceInfo var1) {
      this(var1, null);
   }

   public WebServiceOperationComboBox(WebServiceInfo var1, String var2) {
      this.comboBox = this.createCombo(var1);
      this.caption = var2 != null ? var2 : PackageResources.OperationComboBox.CAPTION;
      this.comboBox.getModel().setSelectedItem(var2);
   }

   public void setOperations(WebServiceInfo var1) {
      List var2 = this.createComboItems(var1);
      final DefaultComboBoxModel var3 = new DefaultComboBoxModel<>(var2.toArray());
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            WebServiceOperationComboBox.this.setNewModel(var3);
         }
      });
   }

   private void setNewModel(ComboBoxModel var1) {
      assert EventQueue.isDispatchThread() : "setNewModel() must be called from the EDT";
      this.comboBox.setModel(var1);
      var1.setSelectedItem(this.caption);
      if (var1.getSize() > 0) {
         this.comboBox.setForeground(javax.swing.UIManager.getColor("Label.foreground"));
         this.comboBox.setToolTipText(null);
      } else {
         this.comboBox.setForeground(Colors.ERROR_TEXT_COLOR);
         this.comboBox.setToolTipText(PackageResources.OperationComboBox.EMPTY_TOOLTIP);
      }
   }

   private KongaComboBox createCombo(WebServiceInfo var1) {
      List var2 = this.createComboItems(var1);
      KongaComboBox var3 = new KongaComboBox(var2);
      var3.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var3.setRenderer(new DefaultListDecorRenderer());
      return var3;
   }

   private List<?> createComboItems(WebServiceInfo var1) {
      if (var1 != null && var1.hasOperations(true)) {
         List var2 = var1.getOperations(true);
         Collections.sort(var2, WebServiceOperationInfo.getComparator("Operation", false));
         ArrayList var3 = Lists.newArrayList();

         for (Object __etlItem87 : var2) {
            WebServiceOperationInfo var5 = (WebServiceOperationInfo) __etlItem87;
            var3.add(new WebServiceOperationComboBox.OperationItem(var5));
         }

         return var3;
      } else {
         return Collections.singletonList(new WebServiceOperationComboBox.OperationItem(null));
      }
   }

   public WebServiceOperationInfo getSelectedOperation() {
      Object var1 = this.comboBox.getSelectedItem();
      return var1 instanceof WebServiceOperationComboBox.OperationItem ? ((WebServiceOperationComboBox.OperationItem)var1).opInfo : null;
   }

   public void addActionListener(ActionListener var1) {
      this.comboBox.addActionListener(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.comboBox.removeActionListener(var1);
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.comboBox);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.comboBox);
   }

   @Override
   public void requestFocus() {
      this.comboBox.requestFocusInWindow();
      this.setInitialSelectionState();
   }

   public void selectFirst() {
      this.comboBox.selectFirst();
   }

   private void setInitialSelectionState() {
      if (this.comboBox.getItemCount() == 1) {
         this.comboBox.setSelectedIndex(0);
      }
   }

   private static class OperationItem implements ListDecorator {
      private final WebServiceOperationInfo opInfo;
      private final String text;
      private final String tooltip;

      public OperationItem(WebServiceOperationInfo var1) {
         this.opInfo = var1;
         this.text = var1 != null ? var1.getProperty("Operation") : PackageResources.OperationComboBox.EMPTY;
         this.tooltip = createToolTip(var1);
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.text);
         var1.setToolTip(this.tooltip);
         if (!var2) {
            if (this.opInfo != null) {
               var1.setTextColor(var1.getTextColor());
            } else {
               var1.setTextColor(Colors.ERROR_TEXT_COLOR);
            }
         }
      }

      @Override
      public String toString() {
         return this.text;
      }

      private static String createToolTip(WebServiceOperationInfo var0) {
         if (var0 != null) {
            LinkedHashMap var1 = Maps.newLinkedHashMap();
            var1.put(PackageResources.OperationComboBox.TOOLTIP_NAME, var0.getProperty("Operation"));
            var1.put(PackageResources.OperationComboBox.TOOLTIP_INPUT, var0.getProperty("InputName"));
            var1.put(PackageResources.OperationComboBox.TOOLTIP_OUTPUT, var0.getProperty("OutputName"));
            return KeyValueToolTip.create(var1);
         } else {
            return PackageResources.OperationComboBox.EMPTY_TOOLTIP;
         }
      }
   }
}

