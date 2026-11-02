package org.etlforge.ui.text.contentassist;

import com.google.common.base.Predicate;
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.etlforge.collections.Pair;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.ComponentDropDownWindow;
import org.etlforge.ui.widget.OutsideWindowMouseClickListener;
import org.etlforge.ui.widget.list.DefaultKongaListFactory;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.ListUtils;

final class TokenChooserListUi {
   static final int DEFAULT_MAX_WIDTH = 50;
   private final JTextComponent trigger;
   private final Receiver<CompletionToken> callback;
   private final SelectionAction selectionAction;
   private CompletionContext context;
   private KongaList tokenList;
   private ListFactory listFactory;
   private JWindow listPopup;
   private TokenInfoWindow infoPopup;
   private Disposable outsideMouseListener;
   private boolean trackWidthOfTrigger;
   private KeyEventDispatcher nonFocusKeyHandler;
   private int maxCharsWidth;
   private boolean isTransient;
   private boolean autoSelectFirstToken;

   public TokenChooserListUi(JTextComponent var1, Receiver<CompletionToken> var2) {
      this.trigger = var1;
      this.callback = var2;
      this.selectionAction = new TokenChooserListUi.SelectionActionImpl();
   }

   public void setAutoSelectFirstToken(boolean var1) {
      this.autoSelectFirstToken = var1;
   }

   public void display(CompletionContext var1, CompletionToken[] var2) {
      if (!this.isVisible()) {
         this.context = var1;
         if (this.tokenList == null) {
            this.tokenList = this.createTokenList(var2);
            this.listPopup = this.createListPopup();
         }

         this.nonFocusKeyHandler = new TokenChooserListUi.NonFocusKeyHandler();
         KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.nonFocusKeyHandler);
         this.outsideMouseListener = new TokenChooserListUi.OutsideMouseListener();
         this.listPopup.setVisible(true);
      }
   }

   private boolean isVisible() {
      return this.listPopup != null && this.listPopup.isVisible();
   }

   private KongaList createTokenList(CompletionToken[] var1) {
      DefaultListModel var2 = this.createListModel(var1);
      return this.createListImpl(var2);
   }

   private DefaultListModel createListModel(CompletionToken[] var1) {
      DefaultListModel var2 = new DefaultListModel();

      for (CompletionToken var6 : var1) {
         var2.addElement(new CompletionTokenUi(var6));
      }

      return var2;
   }

   private KongaList createListImpl(ListModel var1) {
      DefaultKongaListFactory var2 = new DefaultKongaListFactory() {
         @Override
         protected JList createEmptyList() {
            JList var1x = TokenChooserListUi.this.listFactory != null ? TokenChooserListUi.this.listFactory.newList() : super.createEmptyList();
            var1x.setFocusable(false);
            var1x.setFocusTraversalKeysEnabled(false);
            return var1x;
         }
      };
      var2.setCellRenderer(new DefaultListDecorRenderer());
      var2.setVisibleRowCount(Math.min(20, var1.getSize() + 2));
      var2.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      this.setPrototypeCellValue(var2, var1);
      KongaList var3 = var2.newList(var1);
      var3.setTransient(this.isTransient);
      var3.setDefaultAction(this.selectionAction);
      var3.addSelectionListener(new TokenChooserListUi.SelectedTokenListener());
      return var3;
   }

   private void setPrototypeCellValue(KongaListFactory var1, ListModel var2) {
      int var3 = this.maxCharsWidth > 0 ? this.maxCharsWidth : 50;
      int var4 = Math.min(var3, this.getModelItemMaxLength(var2) + 4);
      StringBuilder var5 = new StringBuilder();

      for (int var6 = 0; var6 < var4; var6++) {
         var5.append("X");
      }

      var1.setPrototypeValue(var5);
   }

   private int getModelItemMaxLength(ListModel var1) {
      int var2 = -1;
      int var3 = 0;

      for (int var4 = var1.getSize(); var3 < var4; var3++) {
         CompletionTokenUi var5 = (CompletionTokenUi)var1.getElementAt(var3);
         int var6 = var5.getDisplayValue().length();
         if (var6 > var2) {
            var2 = var6;
         }
      }

      return var2;
   }

   private JWindow createListPopup() {
      TokenChooserListUi.ListPopup var1 = new TokenChooserListUi.ListPopup();
      var1.setTrackWidthOfOwner(this.trackWidthOfTrigger);
      return var1.buildWindow();
   }

   public void hide(boolean var1) {
      if (this.isVisible()) {
         if (this.nonFocusKeyHandler != null) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.nonFocusKeyHandler);
            this.nonFocusKeyHandler = null;
         }

         if (this.outsideMouseListener != null) {
            this.outsideMouseListener.dispose();
         }

         this.listPopup.dispose();
         if (this.infoPopup != null) {
            this.infoPopup.dispose();
            this.infoPopup = null;
         }

         if (var1) {
            this.restoreFocus();
         }
      }
   }

   private void restoreFocus() {
      FocusUtils.changeFocusAcrossWindows(this.trigger, 100);
   }

   public void setTokens(CompletionToken[] var1) {
      Runnable var2 = null;
      if (var1.length > 0) {
         final DefaultListModel var3 = this.createListModel(var1);
         var2 = new Runnable() {
            @Override
            public void run() {
               TokenChooserListUi.this.tokenList.setModel(var3);
               if (TokenChooserListUi.this.autoSelectFirstToken) {
                  TokenChooserListUi.this.tokenList.selectFirst();
               }
            }
         };
      } else {
         var2 = new Runnable() {
            @Override
            public void run() {
               TokenChooserListUi.this.hide(true);
            }
         };
      }

      EventQueue.invokeLater(var2);
   }

   public void setListFactory(ListFactory var1) {
      this.listFactory = var1;
   }

   public void setTrackWidthOfTrigger(boolean var1) {
      this.trackWidthOfTrigger = var1;
   }

   public void setMaxWidth(int var1) {
      this.maxCharsWidth = var1;
   }

   public void setTransient(boolean var1) {
      this.isTransient = var1;
   }

   private void deleteSelectedToken() {
      CompletionToken var1 = this.getSelectedToken();
      if (var1 != null) {
         if (this.context.delete(var1)) {
            ListUtils.deleteSelectedIndeces(this.tokenList);
            if (this.tokenList.getModel().getSize() == 0) {
               this.hide(true);
            }
         }
      }
   }

   private CompletionToken getSelectedToken() {
      IndexedSelection var1 = this.tokenList.getSelection();
      return var1.size() == 1 && var1.head() instanceof CompletionToken ? (CompletionToken)var1.head() : null;
   }

   private class ListPopup extends ComponentDropDownWindow {
      public ListPopup() {
         super(TokenChooserListUi.this.trigger, TokenChooserListUi.this.tokenList);
      }

      @Override
      protected void onWindowOpened() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (TokenChooserListUi.this.autoSelectFirstToken) {
                  TokenChooserListUi.this.tokenList.selectFirst();
               }
            }
         });
      }

      @Override
      protected Point getFreeBoundsLocation() {
         try {
            Rectangle var1 = TokenChooserListUi.this.trigger.modelToView(TokenChooserListUi.this.trigger.getCaretPosition());
            return var1.getLocation();
         } catch (BadLocationException var2) {
            return super.getFreeBoundsLocation();
         }
      }
   }

   private final class NonFocusKeyHandler implements KeyEventDispatcher {
      private NonFocusKeyHandler() {
      }

      @Override
      public boolean dispatchKeyEvent(KeyEvent var1) {
         KeyStroke var2 = KeyStroke.getKeyStrokeForEvent(var1);
         boolean var3 = false;
         if (var2.equals(KeyUtils.ENTER) || var2.equals(KeyUtils.TAB)) {
            ActionUtils.run(
               TokenChooserListUi.this.selectionAction,
               TokenChooserListUi.this.tokenList,
               (String)TokenChooserListUi.this.selectionAction.getValue("ActionCommandKey")
            );
            var3 = true;
         } else if (var2.equals(KeyUtils.DELETE)) {
            TokenChooserListUi.this.deleteSelectedToken();
            var3 = true;
         } else if (var2.equals(KeyUtils.ESCAPE)) {
            TokenChooserListUi.this.hide(true);
            var3 = true;
         } else {
            var3 = this.handlePossibleNavigationKey(var1);
         }

         return var3;
      }

      private boolean handlePossibleNavigationKey(KeyEvent var1) {
         if (TokenChooserListUi.this.isTransient && this.isTypedCharacter(var1)) {
            return false;
         }

         boolean var2 = TokenChooserListUi.this.tokenList.processKeyEvent(var1);
         if (var2) {
            TokenChooserListUi.this.tokenList.ensureSelectedIndexVisible();
         }

         return var2;
      }

      private boolean isTypedCharacter(KeyEvent var1) {
         return var1.getID() == 400 && var1.getKeyChar() != 0;
      }
   }

   private final class OutsideMouseListener extends OutsideWindowMouseClickListener {
      public OutsideMouseListener() {
         super(TokenChooserListUi.this.listPopup);
         this.addSafeZone(new Predicate<Pair<Point, Integer>>() {
            public boolean apply(Pair<Point, Integer> var1) {
               Point var2 = (Point)var1.first;
               int var3 = (Integer)var1.second;
               return TokenChooserListUi.this.infoPopup != null && TokenChooserListUi.this.infoPopup.contains(var2, var3);
            }
         });
      }

      @Override
      protected void onOutsideClick() {
         TokenChooserListUi.this.hide(false);
      }
   }

   private final class SelectedTokenListener implements SelectionListener, ActionListener {
      private Timer timer;

      private SelectedTokenListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         if (this.timer != null) {
            this.timer.restart();
         }

         if (this.timer == null) {
            this.timer = new Timer(300, this);
            this.timer.setRepeats(false);
            this.timer.start();
         }
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (TokenChooserListUi.this.listPopup.isVisible()) {
            CompletionToken var2 = TokenChooserListUi.this.getSelectedToken();
            if (var2 != null) {
               this.displayInfo(var2);
            }
         }
      }

      private void displayInfo(CompletionToken var1) {
         String var2 = var1.getInfo();
         if (var2 != null && var2.length() != 0) {
            if (!var2.toLowerCase().startsWith("<html>")) {
               var2 = "<html>" + var2 + "</html>";
            }

            this.displayInfoText(var2);
         }
      }

      private void displayInfoText(String var1) {
         if (TokenChooserListUi.this.infoPopup == null) {
            TokenChooserListUi.this.infoPopup = new TokenInfoWindow(TokenChooserListUi.this.listPopup, var1);
            TokenChooserListUi.this.infoPopup.show();
         } else {
            TokenChooserListUi.this.infoPopup.setNewInfo(var1);
            TokenChooserListUi.this.infoPopup.show();
         }
      }
   }

   private final class SelectionActionImpl extends AbstractSelectionAction {
      private SelectionActionImpl() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         CompletionToken var2 = TokenChooserListUi.this.getSelectedToken();
         if (var2 != null) {
            TokenChooserListUi.this.callback.handle(var2);
         }

         TokenChooserListUi.this.hide(false);
      }
   }
}
