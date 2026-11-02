package org.etlforge.application.ui.text.editor;

import com.google.common.collect.Lists;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.widget.TextFieldInput;
import org.etlforge.application.ui.widget.TextFieldInputValidator;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;

public final class PageNumberControl implements UiProvider {
   private final JButton button;
   private final Action goToNextPageAction;
   private final Action goToPreviousPageAction;
   private int currentPage;
   private int numberOfPages;
   private final Receiver<Integer> gotoPageHandler;

   public PageNumberControl(Receiver<Integer> var1) {
      this.gotoPageHandler = var1;
      this.goToNextPageAction = new PageNumberControl.GoToNextPageAction();
      this.goToPreviousPageAction = new PageNumberControl.GoToPreviousPageAction();
      this.button = new JButton();
      this.reset();
      this.button.setToolTipText("Current page number. Click to go to a specific page.");
      this.button.setFocusPainted(false);
      this.button.setBorder(null);
      this.button.setContentAreaFilled(false);
      this.button.setEnabled(false);
      this.button.addActionListener(new PageNumberControl.ButtonListener());
   }

   private void currentPageChanged() {
      this.updateUi();
      this.gotoPageHandler.handle(this.currentPage);
   }

   private void updateUi() {
      this.updateLabel();
      this.updateGoToNextAndPreviousActions();
   }

   public void setPageNumber(int var1) {
      if (var1 != this.currentPage) {
         this.currentPage = var1;
         this.currentPageChanged();
      }
   }

   public int getPageNumber() {
      return this.currentPage;
   }

   public void reset() {
      this.currentPage = 0;
      this.numberOfPages = 0;
      this.updateUi();
   }

   public void setNumberOfPages(int var1) {
      this.numberOfPages = var1;
      this.updateUi();
   }

   private void updateLabel() {
      this.button.setText("[Page " + this.currentPage + " / " + this.numberOfPages + "]");
   }

   public void setEnabled(boolean var1) {
      this.button.setEnabled(var1);
      this.updateGoToNextAndPreviousActions();
   }

   public boolean isEnabled() {
      return this.button.isEnabled();
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(10, this.asButtons());
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.button.requestFocusInWindow();
   }

   public JButton asButton() {
      return this.button;
   }

   public List<? extends JButton> asButtons() {
      return Lists.newArrayList(KongaButton.forToolbar(this.goToPreviousPageAction), this.button, KongaButton.forToolbar(this.goToNextPageAction));
   }

   public List<? extends JButton> getGoToNextAndPreviousPageButtons() {
      return Lists.newArrayList(KongaButton.forToolbar(this.goToPreviousPageAction), KongaButton.forToolbar(this.goToNextPageAction));
   }

   private void updateGoToNextAndPreviousActions() {
      boolean var1 = this.isEnabled();
      this.goToNextPageAction.setEnabled(var1 && this.currentPage < this.numberOfPages);
      this.goToPreviousPageAction.setEnabled(var1 && this.currentPage > 1);
   }

   private class ButtonListener implements ActionListener {
      private ButtonListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (PageNumberControl.this.numberOfPages > 20) {
            Window var2 = UiUtils.getWindowAncestor(var1).orNull();
            this.askForPageNumber(var2);
         } else if (PageNumberControl.this.numberOfPages > 1) {
            this.showPageNumberSelector();
         }
      }

      private void askForPageNumber(@Nullable Window var1) {
         TextFieldInput var2 = new TextFieldInput("Go to page", "Enter page number (1-" + PageNumberControl.this.numberOfPages + ")");
         var2.setValidator(TextFieldInputValidator.integerInRange(1, PageNumberControl.this.numberOfPages));
         var2.setLocationRelativeTo(PageNumberControl.this.button);
         String var3 = var2.showInDialog(var1);
         if (var3 != null) {
            int var4 = Integer.parseInt(var3);
            PageNumberControl.this.setPageNumber(var4);
         }
      }

      private void showPageNumberSelector() {
         JPopupMenu var1 = this.createPopupMenu();
         var1.show(PageNumberControl.this.button, 4, 4);
      }

      private JPopupMenu createPopupMenu() {
         JPopupMenu var1 = new JPopupMenu();
         PageNumberControl.ButtonListener.MenuItemListener var2 = new PageNumberControl.ButtonListener.MenuItemListener();

         for (int var3 = 1; var3 <= PageNumberControl.this.numberOfPages; var3++) {
            String var4 = String.valueOf(var3);
            JMenuItem var5 = new JMenuItem("Goto page " + var4);
            if (var3 == PageNumberControl.this.currentPage) {
               var5.setEnabled(false);
            } else {
               var5.setActionCommand(var4);
               var5.addActionListener(var2);
            }

            var1.add(var5);
         }

         return var1;
      }

      private class MenuItemListener implements ActionListener {
         private MenuItemListener() {
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            String var2 = var1.getActionCommand();
            Integer var3 = Integer.valueOf(var2);
            PageNumberControl.this.setPageNumber(var3);
         }
      }
   }

   private class GoToNextPageAction extends AbstractAction {
      public GoToNextPageAction() {
         super(null, UiResourceBundle.getIcon("NavigateForward.16"));
         this.putValue("ShortDescription", "Go to the next page");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ++PageNumberControl.this.currentPage;
         PageNumberControl.this.currentPageChanged();
      }
   }

   private class GoToPreviousPageAction extends AbstractAction {
      public GoToPreviousPageAction() {
         super(null, UiResourceBundle.getIcon("NavigateBack.16"));
         this.putValue("ShortDescription", "Go to the previous page");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         --PageNumberControl.this.currentPage;
         PageNumberControl.this.currentPageChanged();
      }
   }
}
