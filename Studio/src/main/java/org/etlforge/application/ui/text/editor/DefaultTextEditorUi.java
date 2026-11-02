package org.etlforge.application.ui.text.editor;

import java.awt.Color;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledDocument;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.text.KongaTextPane;
import org.etlforge.ui.widget.text.TextComponentUtils;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.text.marker.TextMarker;
import org.etlforge.ui.widget.text.undo.UndoableTextEditSender;

final class DefaultTextEditorUi {
   private final DefaultTextEditor editor;
   private KongaTextPane textPane;
   private UndoableTextEditSender undoSender;
   private TextMarker textMarker;

   public DefaultTextEditorUi(DefaultTextEditor var1) {
      this.editor = var1;
      this.createComponents();
   }

   public JComponent getDisplayer() {
      return new KongaScrollPane(this.textPane);
   }

   public void setEditable(boolean var1) {
      if (var1 != this.textPane.isEditable()) {
         this.textPane.setEditable(var1);
         if (var1) {
            this.installUndoRedo();
         }
      }
   }

   public void setInitialFocus() {
      this.textPane.requestFocusInWindow();
   }

   public void decorateText(TextDecorator var1) {
      MutableAttributeSet var2 = TextComponentUtils.fromFont(var1.getFont(), var1.getTextColor(), Color.WHITE);
      StyledDocument var3 = this.textPane.getStyledDocument();
      int var4 = var3.getLength();
      if (var4 == 0) {
         this.textPane.setFont(var1.getFont());
         this.textPane.setForeground(var1.getTextColor());
         this.textPane.setBackground(javax.swing.UIManager.getColor("Panel.background"));
      } else {
         var3.setCharacterAttributes(0, var3.getLength(), var2, false);
      }
   }

   public void saveToFile(File var1) throws IOException {
      TextComponentUtils.saveFile(var1, this.textPane);
   }

   public void populateTextArea() {
      Object var1 = this.editor.getObject();
      if (var1 instanceof String) {
         this.textPane.setText((String)var1);
      } else if (var1 instanceof File) {
         this.populateTextAreaFromFile(this.textPane, (File)var1, this.editor.getCharset());
      }

      this.textPane.setCaretPosition(0);
   }

   public void populateIconPopupMenu(KongaPopupMenu var1) {
      var1.addItems(new FindTextAction(this.textPane), this.textMarker.getMarkerMenu());
   }

   private void installUndoRedo() {
      if (this.undoSender != null) {
         this.undoSender.dispose();
      }

      this.undoSender = new UndoableTextEditSender(this.textPane);
      this.undoSender.addUndoableEditListener(this.editor);
   }

   private void createComponents() {
      KongaTextPane var1 = createTextComponent();
      this.textPane = var1;
      this.textMarker = new TextMarker(this.textPane);
   }

   private static KongaTextPane createTextComponent() {
      KongaTextPane var0 = new KongaTextPane();
      var0.setCaret(new ShowSelectionWhenNotFocusedCaret());
      var0.setEditable(false);
      var0.setMargin(new Insets(2, 4, 2, 4));
      var0.setBorder(new BorderBuilder().line(new Color(25, 25, 25)).empty(2));
      return var0;
   }

   private void populateTextAreaFromFile(JTextComponent var1, File var2, Charset var3) {
      LoggingExceptionHandler var4 = new LoggingExceptionHandler(this.editor.getClass(), ErrorLevel.ATTENTION, "Failed to open the file " + var2.getName());
      TextComponentUtils.loadFile(var2, var3, var1, null, var4);
      this.installUndoRedo();
   }
}

