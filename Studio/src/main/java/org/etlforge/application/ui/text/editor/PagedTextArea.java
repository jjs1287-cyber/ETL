package org.etlforge.application.ui.text.editor;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Insets;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.wait.SingleLockWaitService;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.text.marker.TextMarker;
import org.etlforge.util.preference.BooleanKeyValue;

public abstract class PagedTextArea implements UiProvider {
   private final KongaTextArea textPane;
   private final KongaCheckBox lineWrapCheckBox;
   private BooleanKeyValue lineWrapPreference;
   private final PageNumberControl navigator;
   private final SingleLockWaitService waitService = new SingleLockWaitService();
   private final TextMarker marker;
   private final List<Action> actions;

   public PagedTextArea() {
      this.navigator = new PageNumberControl(new PagedTextArea.OpenPageHandler());
      this.textPane = this.createTextComponent();
      this.lineWrapCheckBox = this.createLineWrapCheckBox();
      this.actions = Lists.newArrayList();
      this.marker = new TextMarker(this.textPane);
      this.textPane.addToContextMenu(this.marker.getMarkerMenu());
      this.setLineWrap(false);
   }

   private KongaTextArea createTextComponent() {
      KongaTextArea var1 = new KongaTextArea();
      var1.setEditable(false);
      var1.setWrapStyleWord(true);
      var1.setMargin(new Insets(2, 4, 2, 4));
      var1.setBorder(new BorderBuilder().line(new Color(25, 25, 25)).empty(2));
      return var1;
   }

   private KongaCheckBox createLineWrapCheckBox() {
      KongaCheckBox var1 = new KongaCheckBox("Word wrap");
      var1.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            PagedTextArea.this.setLineWrap(var1);
         }
      });
      return var1;
   }

   public void setLineWrap(boolean var1) {
      this.textPane.setLineWrap(var1);
      this.lineWrapCheckBox.setSelected(var1);
      if (this.lineWrapPreference != null) {
         this.lineWrapPreference.set("wordWrap", var1);
      }
   }

   public void setLineWrapPreference(BooleanKeyValue var1) {
      this.lineWrapPreference = var1;
      boolean var2 = var1.get("wordWrap", false);
      this.setLineWrap(var2);
   }

   public final void addAction(Action var1) {
      this.actions.add(var1);
   }

   public final void setEnabled(boolean var1) {
      this.textPane.setEnabled(var1);
      this.navigator.setEnabled(var1);
      this.lineWrapCheckBox.setEnabled(var1);
   }

   public final void setNumberOfPages(int var1) {
      this.navigator.setNumberOfPages(var1);
   }

   public final void setCurrentPage(int var1) {
      this.navigator.setPageNumber(var1);
   }

   public final void reset() {
      this.navigator.reset();
      this.textPane.setText("");
   }

   public final void populateIconPopupMenu(KongaPopupMenu var1) {
      var1.add(new FindTextAction(this.textPane));

      for (Action var3 : this.actions) {
         var1.add(var3);
      }

      var1.add(this.marker.getMarkerMenu());
   }

   public final Action getFindAction() {
      return new FindTextAction(this.textPane);
   }

   public final void decorateText(TextDecorator var1) {
      this.textPane.setFont(var1.getFont());
      this.textPane.setForeground(var1.getTextColor());
      this.textPane.setBackground(javax.swing.UIManager.getColor("Panel.background"));
   }

   public final List<? extends JButton> getNavigationButtons() {
      return this.navigator.asButtons();
   }

   public final void setWaitService(WaitService var1) {
      this.waitService.setWrappee(var1);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      JComponent var3 = this.createSouthToolBar();
      var2.center(this.textPane).south(var3);
      var2.deeplyNonOpaque();
      var2.decorate(var1);
   }

   private JComponent createSouthToolBar() {
      BoxBuilder var1 = BoxBuilder.horizontal().withBorder(Empty.border(4, 0, 0, 4));
      var1.addAll(this.actions);
      var1.glue()
         .add(this.lineWrapCheckBox)
         .rigid(10)
         .addAll(ApplicationResources.getString("ChunkedTextFilePage.Label.Navigate"), this.navigator.getGoToNextAndPreviousPageButtons());
      return var1.container();
   }

   @Override
   public final void requestFocus() {
      this.textPane.requestFocusInWindow();
   }

   public final Action createSearchAction(String var1) {
      SearchableTextComponent var2 = new SearchableTextComponent(this.textPane, var1, true);
      return var2.getFindAction();
   }

   protected abstract void getTextForPage(int var1, Receiver<String> var2);

   private class OpenPageHandler implements Receiver<Integer> {
      private OpenPageHandler() {
      }

      public void handle(Integer var1) {
         this.readPage(var1);
      }

      private void readPage(int var1) {
         PagedTextArea.this.waitService.startWait();
         PagedTextArea.this.navigator.setEnabled(false);
         PagedTextArea.this.getTextForPage(var1, PagedTextArea.this.new PageTextReceiver());
      }
   }

   private class PageTextReceiver implements Receiver<String> {
      private PageTextReceiver() {
      }

      public void handle(final String var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               PagedTextArea.this.waitService.stopWait();
               PagedTextArea.this.textPane.setText(var1);
               PagedTextArea.this.textPane.setCaretPosition(0);
               PagedTextArea.this.setEnabled(true);
            }
         });
      }
   }
}

