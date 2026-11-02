package org.etlforge.application.ui.widget;

import java.awt.Component;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.wizard.WizardSectionsUi;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.LayeredKongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;

public class WizardStyleDialog extends LayeredKongaDialog {
   public WizardStyleDialog(Window var1, String var2) {
      this(var1, var2, (JComponent)null);
   }

   public WizardStyleDialog(Window var1, String var2, UiProvider var3) {
      this(var1, var2, var3, true);
   }

   public WizardStyleDialog(Window var1, String var2, JComponent var3) {
      this(var1, var2, var3, OK_CANCEL, true, false);
   }

   public WizardStyleDialog(Window var1, String var2, UiProvider var3, boolean var4) {
      this(var1, var2, var3, OK_CANCEL, var4);
   }

   public WizardStyleDialog(Window var1, String var2, UiProvider var3, KongaDialog.ButtonMode var4, boolean var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   public WizardStyleDialog(Window var1, String var2, Component var3, KongaDialog.ButtonMode var4, boolean var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   private WizardStyleDialog(Window var1, String var2, Object var3, KongaDialog.ButtonMode var4, boolean var5, boolean var6) {
      super(var1, var2, var5 ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
      this.getDefaultLayout().withBackground(Colors.ETLFORGE_BLUE);
      this.setFixedButtonSize(this.useFixedButtonSize());
      this.layoutTitleSection();
      this.layoutCenterContent(var3);
      if (var4 != null) {
         JPanel var7 = this.getButtonPanel(var4, 10);
         var7.setOpaque(false);
         var7.setBorder(Empty.border(10));
         this.getDefaultLayout().south(var7);
      }

      this.pack();
   }

   private void layoutTitleSection() {
      String var1 = this.getCaptionTitle();
      if (var1 == null || var1.isEmpty()) return;
      WizardSectionsUi var2 = new WizardSectionsUi(new String[]{var1});
      var2.setHorizontalAlignment(10);
      var2.setBorder(null);
      var2.setSection(var1);
      this.getDefaultLayout().north(var2);
   }

   private void layoutCenterContent(Object var1) {
      if (var1 != null) {
         JComponent var2;
         if (var1 instanceof JComponent) {
            var2 = (JComponent)var1;
            this.setFocusedComponent(var2);
         } else {
            if (!(var1 instanceof UiProvider)) {
               throw new RuntimeException();
            }

            UiProvider var3 = (UiProvider)var1;
            var2 = wrapUi(var3);
            this.setFocusedComponent(var3);
         }

         this.getDefaultLayout().center(var2);
      }
   }

   public static JComponent wrapUi(UiProvider var0) {
      JPanel var1 = BorderLayoutBuilder.fillWith(var0);
      prepareContent(var1);
      return var1;
   }

   public static KongaScrollPane wrapInScroll(UiProvider var0) {
      JComponent var1 = wrapUi(var0);
      KongaScrollPane var2 = new KongaScrollPane(var1);
      var2.setBorder(Empty.border());
      return var2;
   }

   private static void prepareContent(JComponent var0) {
      var0.setOpaque(true);
      var0.setBorder(Empty.border(10));
      UiUtils.setPanelBackgrounds(var0, Colors.NEUTRAL_BACKGROUND);
   }

   protected boolean useFixedButtonSize() {
      return true;
   }

   protected String getCaptionTitle() {
      return this.getTitle();
   }
}

