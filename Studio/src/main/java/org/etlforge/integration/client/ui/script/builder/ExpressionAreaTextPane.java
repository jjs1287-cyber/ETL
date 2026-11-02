package org.etlforge.integration.client.ui.script.builder;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import javax.swing.Action;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import org.etlforge.application.ui.text.editor.SearchableTextComponent;
import org.etlforge.integration.client.ui.script.text.ScriptDocument;
import org.etlforge.ui.UIManager2;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.CustomPaintable;
import org.etlforge.ui.widget.text.KongaTextPane;

public final class ExpressionAreaTextPane extends KongaTextPane implements CustomPaintable {
   private final ExpressionAreaTextPane.CaretImpl caret;
   private final boolean withInstanceFunctions;
   private final Action findAction;
   private ComponentPainter painter;

   public ExpressionAreaTextPane(boolean var1) {
      super(new ScriptDocument());
      this.withInstanceFunctions = var1;
      this.findAction = this.createFindAction();
      this.caret = new ExpressionAreaTextPane.CaretImpl();
      this.setCaret(this.caret);
      this.getDocument().addDocumentListener(new ExpressionAreaTextPane.DocumentListenerHandler());
   }

   private Action createFindAction() {
      SearchableTextComponent var1 = new SearchableTextComponent(this, Strings.get("Search.Title"), true, true);
      var1.setActionDescription(Strings.get("Search.ToolTip"));
      return var1.getFindAction();
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      this.painter = var1;
   }

   public ComponentPainter getPainter() {
      return this.painter;
   }

   public ScriptDocument getFormulaBuilderDocument() {
      Document var1 = super.getDocument();
      return var1 instanceof ScriptDocument ? (ScriptDocument)var1 : null;
   }

   @Override
   public String getText() {
      try {
         return this.getText(0, this.getDocument().getLength());
      } catch (BadLocationException var2) {
         return "";
      }
   }

   public boolean supportsInstanceFunctions() {
      return this.withInstanceFunctions;
   }

   public Action getFindAction() {
      return this.findAction;
   }

   @Override
   public void setCaretPosition(int var1) {
      int var2 = this.getDocument().getLength();
      super.setCaretPosition(Math.min(var1, var2));
   }

   public void setSelectWordAction(Action var1) {
      this.getActionMap().put("select-word", var1);
   }

   @Override
   protected void paintComponent(Graphics var1) {
      if (this.painter == null) {
         super.paintComponent(var1);
      } else {
         this.applyPainter((Graphics2D)var1);
      }
   }

   private void applyPainter(Graphics2D var1) {
      assert this.painter != null;
      if (!this.painter.preRendering(this, var1)) {
         super.paintComponent(var1);
         this.painter.postRendering(this, var1);
      }
   }

   public boolean hasTransTag() {
      return this.getText().indexOf("<trans>") >= 0;
   }

   public void setTabSize(int var1) {
      SimpleAttributeSet var2 = this.createTabAttributes(var1);
      this.applyTabAttributes(var2);
   }

   private SimpleAttributeSet createTabAttributes(int var1) {
      TabSet var2 = this.createTabSet(var1);
      SimpleAttributeSet var3 = new SimpleAttributeSet();
      StyleConstants.setTabSet(var3, var2);
      return var3;
   }

   private TabSet createTabSet(int var1) {
      TabStop[] var2 = this.createTabStops(var1);
      return new TabSet(var2);
   }

   private TabStop[] createTabStops(int var1) {
      FontMetrics var2 = this.getFontMetrics(this.getFont());
      int var3 = var2.charWidth('w');
      int var4 = var3 * var1;
      TabStop[] var5 = new TabStop[10];

      for (int var6 = 0; var6 < var5.length; var6++) {
         int var7 = var6 + 1;
         var5[var6] = new TabStop(var7 * var4);
      }

      return var5;
   }

   private void applyTabAttributes(SimpleAttributeSet var1) {
      int var2 = this.getDocument().getLength();
      this.getStyledDocument().setParagraphAttributes(0, var2, var1, false);
   }

   private static final class CaretImpl extends DefaultCaret {
      private boolean blocked;

      public CaretImpl() {
         this.setBlinkRate(UIManager2.getInt("TextPane.caretBlinkRate", 500));
      }

      @Override
      public void setDot(int var1) {
         if (!this.blocked) {
            super.setDot(var1);
         }
      }

      public void setBlocked(boolean var1) {
         this.blocked = var1;
      }

      @Override
      public void focusLost(FocusEvent var1) {
         this.setVisible(false);
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this;
      }

      @Override
      public int hashCode() {
         return System.identityHashCode(this);
      }
   }

   private final class DocumentListenerHandler implements DocumentListener {
      private final DocumentListener[] otherListeners = ((AbstractDocument)ExpressionAreaTextPane.this.getDocument()).getDocumentListeners();

      public DocumentListenerHandler() {
         for (DocumentListener var5 : this.otherListeners) {
            ((AbstractDocument)ExpressionAreaTextPane.this.getDocument()).removeDocumentListener(var5);
         }
      }

      @Override
      public void changedUpdate(DocumentEvent var1) {
         ExpressionAreaTextPane.this.caret.setBlocked(true);

         try {
            for (DocumentListener var5 : this.otherListeners) {
               var5.changedUpdate(var1);
            }
         } finally {
            ExpressionAreaTextPane.this.caret.setBlocked(false);
         }
      }

      @Override
      public void insertUpdate(DocumentEvent var1) {
         this.textValueChanged();

         for (DocumentListener var5 : this.otherListeners) {
            var5.insertUpdate(var1);
         }
      }

      private void textValueChanged() {
         ExpressionAreaTextPane.this.repaint();
         ExpressionAreaTextPane.this.requestFocus();
      }

      @Override
      public void removeUpdate(DocumentEvent var1) {
         this.textValueChanged();

         for (DocumentListener var5 : this.otherListeners) {
            var5.removeUpdate(var1);
         }
      }
   }
}
