package org.etlforge.ui.text;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter.HighlightPainter;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.util.Interval;

public class BracketMatcher implements Disposable {
   public static final Color DEFAULT_MATCHING_HIGHLIGHT = new Color(187, 255, 187);
   public static final Color DEFAULT_UNBALANCED_HIGHLIGHT = new Color(255, 187, 187);
   private final JTextComponent textComponent;
   private final Document document;
   private final List<BracketMatcher.Finder> finders;
   private final KeyBindingInstaller keyBinding;
   private final DocumentListener docListener;
   private BracketMatcher.AnchorHighlighter anchorHighlighter;

   public BracketMatcher(JTextComponent var1) {
      this(var1, EnumSet.allOf(BracketType.class));
   }

   public BracketMatcher(JTextComponent var1, EnumSet<BracketType> var2) {
      this.document = var1.getDocument();
      this.finders = this.createFinders(var2);
      this.keyBinding = this.installKeyBinding(var1);
      this.textComponent = var1;
      this.docListener = this.createDocumentListener();
      this.document.addDocumentListener(this.docListener);
      this.installHighlighter(DEFAULT_MATCHING_HIGHLIGHT, DEFAULT_UNBALANCED_HIGHLIGHT);
   }

   private void installHighlighter(Color var1, Color var2) {
      if (this.anchorHighlighter != null) {
         this.textComponent.removeCaretListener(this.anchorHighlighter);
      }

      this.anchorHighlighter = new BracketMatcher.AnchorHighlighter(var1, var2);
      this.textComponent.addCaretListener(this.anchorHighlighter);
   }

   private KeyBindingInstaller installKeyBinding(JTextComponent var1) {
      KeyBindingInstaller var2 = new KeyBindingInstaller(var1);
      var2.addBinding(KeyUtils.getCtrlShiftDown(80), "characterMatching", new BracketMatcher.TriggerAction());
      var2.install();
      return var2;
   }

   @Override
   public void dispose() {
      this.keyBinding.remove();
      this.textComponent.removeCaretListener(this.anchorHighlighter);
      this.document.removeDocumentListener(this.docListener);
   }

   public void setHighlightColors(Color var1, Color var2) {
      this.installHighlighter(var1, var2);
   }

   protected boolean isCharacterMatchingAvailable(int var1) {
      return true;
   }

   private List<BracketMatcher.Finder> createFinders(EnumSet<BracketType> var1) {
      ArrayList var2 = new ArrayList();

      for (BracketType var4 : var1) {
         var2.add(new BracketMatcher.Finder(var4.getOpening(), var4.getClosing()));
      }

      return var2;
   }

   private DocumentListener createDocumentListener() {
      return new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            BracketMatcher.this.anchorHighlighter.reset(true);
         }
      };
   }

   private class AnchorHighlighter implements CaretListener {
      private final HighlightPainter matchPainter;
      private final HighlightPainter noMatchPainter;
      private final Set<Object> highlights;

      public AnchorHighlighter(Color var2, Color var3) {
         this.matchPainter = new DefaultHighlightPainter(var2);
         this.noMatchPainter = new DefaultHighlightPainter(var3);
         this.highlights = new HashSet<>();
      }

      @Override
      public void caretUpdate(CaretEvent var1) {
         this.reset(false);
         int var2 = var1.getDot();
         if (BracketMatcher.this.isCharacterMatchingAvailable(var2)) {
            int var3 = BracketMatcher.this.textComponent.getSelectionStart();
            int var4 = BracketMatcher.this.textComponent.getSelectionEnd();
            if (var4 - var3 <= 1) {
               boolean var5;
               if (var4 == var3) {
                  var5 = false;
               } else {
                  var5 = true;
               }

               if (this.isValidPosition(var3, var4) && !this.checkForwardString(var2, var5)) {
                  this.checkBackwardString(var2, var5);
               }
            }
         }
      }

      private boolean isValidPosition(int var1, int var2) {
         return BracketMatcher.this.isCharacterMatchingAvailable(var1) || BracketMatcher.this.isCharacterMatchingAvailable(var2);
      }

      private boolean checkForwardString(int var1, boolean var2) {
         String var3 = this.getForwardString(var1);
         if (this.isOneCharacterString(var3)) {
            char var4 = var3.charAt(0);

            for (BracketMatcher.Finder var6 : BracketMatcher.this.finders) {
               if (var6.isStartAnchor(var4)) {
                  if (this.handleAnchor(var6, var1, var1)) {
                     return true;
                  }
               } else if (var2 && var6.isEndAnchor(var4) && this.handleAnchor(var6, var1, var1)) {
                  return true;
               }
            }
         }

         return false;
      }

      private void checkBackwardString(int var1, boolean var2) {
         String var3 = this.getBackwardString(var1);
         if (this.isOneCharacterString(var3)) {
            char var4 = var3.charAt(0);

            for (BracketMatcher.Finder var6 : BracketMatcher.this.finders) {
               if (var6.isEndAnchor(var4)) {
                  if (this.handleAnchor(var6, var1, var1 - 1)) {
                     return;
                  }
               } else if (var2 && var6.isStartAnchor(var4) && this.handleAnchor(var6, var1 - 1, var1 - 1)) {
                  return;
               }
            }
         }
      }

      private boolean handleAnchor(BracketMatcher.Finder var1, int var2, int var3) {
         boolean var4 = false;
         Interval var5 = var1.findMatch(BracketMatcher.this.document, var2);
         if (var5 != null) {
            this.highlightMatch(var5);
            var4 = true;
         } else {
            this.highlightNoMatch(var3);
         }

         return var4;
      }

      private void highlightMatch(Interval var1) {
         int var2 = var1.startsAt();
         this.highlight(var2, this.matchPainter);
         int var3 = var2 + var1.length();
         this.highlight(var3 - 1, this.matchPainter);
      }

      private void highlightNoMatch(int var1) {
         this.highlight(var1, this.noMatchPainter);
      }

      private void highlight(int var1, HighlightPainter var2) {
         try {
            Highlighter var3 = BracketMatcher.this.textComponent.getHighlighter();
            Object var4 = var3.addHighlight(var1, var1 + 1, var2);
            this.highlights.add(var4);
         } catch (BadLocationException var5) {
         }
      }

      public void reset(boolean var1) {
         Highlighter var2 = BracketMatcher.this.textComponent.getHighlighter();
         if (var1) {
            var2.removeAllHighlights();
         } else {
            for (Object var4 : this.highlights) {
               var2.removeHighlight(var4);
            }

            this.highlights.clear();
         }
      }

      private boolean isOneCharacterString(String var1) {
         return var1 != null && var1.length() == 1;
      }

      private String getForwardString(int var1) {
         try {
            if (var1 < BracketMatcher.this.document.getLength()) {
               return BracketMatcher.this.document.getText(var1, 1);
            }
         } catch (BadLocationException var3) {
         }

         return null;
      }

      private String getBackwardString(int var1) {
         try {
            if (var1 > 0) {
               return BracketMatcher.this.document.getText(var1 - 1, 1);
            }
         } catch (BadLocationException var3) {
         }

         return null;
      }
   }

   private class Finder extends DefaultCharacterPairFinder {
      private final char left;
      private final char right;

      public Finder(char var2, char var3) {
         super(var2, var3);
         this.left = var2;
         this.right = var3;
      }

      @Override
      public boolean isValidOffset(Document var1, int var2) {
         return BracketMatcher.this.isCharacterMatchingAvailable(var2);
      }

      public boolean isStartAnchor(char var1) {
         return var1 == this.left;
      }

      public boolean isEndAnchor(char var1) {
         return var1 == this.right;
      }
   }

   private class TriggerAction extends AbstractAction {
      private TriggerAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int var2 = this.getOffset();
         if (this.isValidOffset(var2)) {
            Interval var3 = this.findMatch(var2);
            if (var3 != null) {
               this.moveCursorToMatch(var3, var2);
            }
         }
      }

      private boolean isValidOffset(int var1) {
         return BracketMatcher.this.isCharacterMatchingAvailable(var1);
      }

      private int getOffset() {
         int var1 = BracketMatcher.this.textComponent.getCaretPosition();
         int var2 = BracketMatcher.this.textComponent.getSelectionStart();
         int var3 = BracketMatcher.this.textComponent.getSelectionEnd();
         if (var3 - var2 > 1) {
            return -1;
         } else {
            return var1 == var2 ? var1 : var2;
         }
      }

      private Interval findMatch(int var1) {
         for (CharacterPairFinder var3 : BracketMatcher.this.finders) {
            Interval var4 = var3.findMatch(BracketMatcher.this.document, var1);
            if (var4 != null) {
               return var4;
            }
         }

         return null;
      }

      private void moveCursorToMatch(Interval var1, int var2) {
         int var3 = var1.startsAt();
         if (var3 == var2) {
            this.moveCursorToEnd(var1, var3);
         } else {
            this.moveCursorToStart(var1);
         }
      }

      private void moveCursorToEnd(Interval var1, int var2) {
         BracketMatcher.this.textComponent.setCaretPosition(var1.length() + var2);
      }

      private void moveCursorToStart(Interval var1) {
         BracketMatcher.this.textComponent.setCaretPosition(var1.startsAt());
      }
   }
}
