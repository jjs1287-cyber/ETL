package org.etlforge.ui.util.find;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class TextSearchAndReplaceUi implements UiProvider, Disposable {
   private final TextSearchController controller;
   private final TextSearchUi searchUi;
   private final TextSearchUi replaceUi;
   private final JTabbedPane tabs;

   public TextSearchAndReplaceUi(TextSearchModel var1, TextSearchController var2) {
      this.controller = var2;
      this.searchUi = TextSearchUi.searchOnly(var1);
      this.replaceUi = TextSearchUi.searchAndReplace(var1);
      this.tabs = this.createTabs();
   }

   private JTabbedPane createTabs() {
      JTabbedPane var1 = new JTabbedPane();
      JComponent var2 = this.layoutSearchUi();
      var1.addTab("Search", var2);
      var1.addTab("Replace", this.layoutReplaceUi());
      return var1;
   }

   private JComponent layoutSearchUi() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(10, 0);
      var1.center(this.searchUi);
      var1.east(this.controller.getFindAction());
      var1.withBorder(Empty.border(5));
      return var1.container();
   }

   private JComponent layoutReplaceUi() {
      ReplaceUiLayout var1 = new ReplaceUiLayout(this.replaceUi, this.controller);
      return BorderLayoutBuilder.fillWith(var1);
   }

   @Override
   public void dispose() {
      this.searchUi.dispose();
      this.replaceUi.dispose();
      this.controller.dispose();
   }

   public void showSearchUi() {
      this.tabs.setSelectedIndex(0);
   }

   public void showReplaceUi() {
      this.tabs.setSelectedIndex(1);
   }

   public void setDefaultAction(Action var1) {
      this.searchUi.setDefaultAction(var1);
      this.replaceUi.setDefaultAction(var1);
   }

   public void setTarget(SearchAndReplaceTarget var1) {
      this.controller.setTarget(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.tabs);
   }

   @Override
   public void requestFocus() {
      if (this.tabs.getSelectedIndex() == 0) {
         this.searchUi.requestFocus();
      } else {
         this.replaceUi.requestFocus();
      }
   }

   public void addObserver(TextSearchObserver var1) {
      this.controller.addObserver(var1);
   }

   public void removeObserver(TextSearchObserver var1) {
      this.controller.removeObserver(var1);
   }
}
