package org.etlforge.integration.client.ui.interchange.entity.document.page;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.data.structure.text.SegmentType;
import org.etlforge.integration.data.structure.text.TextStructureType;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class NewDocumentTypeQuickSelector extends AbstractAction {
   private final NewTextDocumentModel model;
   private final JComponent invoker;
   private final Action createManuallyAction;

   public NewDocumentTypeQuickSelector(NewTextDocumentModel var1, JComponent var2, Action var3) {
      this.model = var1;
      this.invoker = var2;
      this.createManuallyAction = var3;
      this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(81, 128));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            NewDocumentTypeQuickSelector.this.showPopupMenu();
         }
      });
   }

   private void showPopupMenu() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      int var2 = 1;

      for (TextStructureType var6 : TextStructureType.values()) {
         for (SegmentType var10 : SegmentType.values()) {
            var1.add(new NewDocumentTypeQuickSelector.Combination(var2, var6, var10));
            var2++;
         }
      }

      var1.show(this.invoker, 50, 50);
   }

   private class Combination extends AbstractAction {
      private final TextStructureType structureType;
      private final SegmentType segmentType;

      public Combination(int var2, TextStructureType var3, SegmentType var4) {
         this.structureType = var3;
         this.segmentType = var4;
         this.putValue("Name", this.createName(var2, var3, var4));
      }

      private String createName(int var1, TextStructureType var2, SegmentType var3) {
         StringBuilder var4 = new StringBuilder();
         var4.append("&").append(var1).append(" ");
         if (var2 == TextStructureType.SIMPLE) {
            var4.append(this.stripMnemonicFromResource("Label.SimpleTextOption"));
         } else {
            var4.append(this.stripMnemonicFromResource("Label.ComplexTextOption"));
         }

         var4.append(" - ");
         if (var3 == SegmentType.DELIMITED) {
            var4.append(this.stripMnemonicFromResource("Label.CharacterOption"));
         } else {
            var4.append(this.stripMnemonicFromResource("Label.FixedWidthOption"));
         }

         return var4.toString();
      }

      private String stripMnemonicFromResource(String var1) {
         ResourceBundle var2 = TextDocumentPage.getResources();
         MnemonicStringFactory var3 = ApplicationResources.MNEMONIC_FACTORY;
         String var4 = var2.getString(var1);
         return var3.asText(var4);
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               NewDocumentTypeQuickSelector.this.model.setTextStructureType(Combination.this.structureType);
               NewDocumentTypeQuickSelector.this.model.setSegmentType(Combination.this.segmentType);
               NewDocumentTypeQuickSelector.this.createManuallyAction.actionPerformed(var1);
            }
         });
      }
   }
}
