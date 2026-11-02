package org.etlforge.integration.client.ui.interchange.entity.document.page;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.JRadioButton;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.text.SegmentType;
import org.etlforge.integration.data.structure.text.TextStructureType;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;

final class TypeAndFormatDefinitionPanel extends TextDocumentDefiner {
   private static final ResourceBundle resources = TextDocumentPage.getResources();
   private final NewTextDocumentModel model;
   private final JRadioButton flatOption;
   private final JRadioButton complexOption;
   private final JRadioButton fixedOption;
   private final JRadioButton delimitedOption;

   public TypeAndFormatDefinitionPanel(NewTextDocumentModel var1) {
      this.model = var1;
      this.flatOption = new KongaRadioButton(resources.getString("Label.SimpleTextOption"), true);
      this.complexOption = new KongaRadioButton(resources.getString("Label.ComplexTextOption"), false);
      ButtonUtils.makeMutuallyExclusive(this.flatOption, this.complexOption);
      this.delimitedOption = new KongaRadioButton(resources.getString("Label.CharacterOption"), true);
      this.fixedOption = new KongaRadioButton(resources.getString("Label.FixedWidthOption"), false);
      ButtonUtils.makeMutuallyExclusive(this.delimitedOption, this.fixedOption);
      this.listenToTypeOptions();
      this.listenToFormatOptions();
      this.layoutComponents();
   }

   private void listenToTypeOptions() {
      ActionListener var1 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TypeAndFormatDefinitionPanel.this.updateModelStructureType();
         }
      };
      ButtonUtils.addActionListener(var1, this.flatOption, this.complexOption);
   }

   private void updateModelStructureType() {
      TextStructureType var1 = this.flatOption.isSelected() ? TextStructureType.SIMPLE : TextStructureType.COMPLEX;
      this.model.setTextStructureType(var1);
   }

   private void listenToFormatOptions() {
      ActionListener var1 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TypeAndFormatDefinitionPanel.this.updateModelSegmentType();
         }
      };
      ButtonUtils.addActionListener(var1, this.delimitedOption, this.fixedOption);
   }

   private void updateModelSegmentType() {
      SegmentType var1 = this.delimitedOption.isSelected() ? SegmentType.DELIMITED : SegmentType.FIXED_FIELD;
      this.model.setSegmentType(var1);
   }

   public boolean isSimpleText() {
      return this.flatOption.isSelected();
   }

   public boolean isFixedWidth() {
      return this.fixedOption.isSelected();
   }

   private void layoutComponents() {
      BorderLayoutBuilder var1 = BorderLayoutBuilder.from(this.getUi());
      if (ClientInfo.isDataLoader()) {
         var1.center(this.layoutFormatPanel());
      } else {
         Grid var2 = Grid.oneRow(10).of(this.layoutDocumentTypePanel(), this.layoutFormatPanel());
         var1.center(var2);
      }
   }

   private Container layoutDocumentTypePanel() {
      Box var1 = Box.createVerticalBox();
      var1.setBorder(StyleSheet.getDefaultTitleBorder(resources.getString("Title.DocumentType")));
      var1.add(this.flatOption);
      var1.add(Box.createVerticalStrut(10));
      var1.add(this.complexOption);
      return var1;
   }

   private Container layoutFormatPanel() {
      Box var1 = Box.createVerticalBox();
      var1.setBorder(StyleSheet.getDefaultTitleBorder(resources.getString("Title.DocumentFormat")));
      var1.add(this.delimitedOption);
      var1.add(Box.createVerticalStrut(10));
      var1.add(this.fixedOption);
      return var1;
   }

   @Override
   public void stopEditing() {
   }

   @Override
   public void applyToTextDocument(TextDocument var1) {
      int var2 = this.getDefinedDocType();
      var1.setDocType(var2);
   }

   private int getDefinedDocType() {
      if (this.isSimpleText()) {
         return this.isFixedWidth() ? 1 : 2;
      } else {
         return this.isFixedWidth() ? 3 : 4;
      }
   }
}
