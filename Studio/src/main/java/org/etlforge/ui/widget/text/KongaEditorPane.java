package org.etlforge.ui.widget.text;

import java.io.IOException;
import java.net.URL;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import org.etlforge.ui.UiProvider;

public class KongaEditorPane extends JEditorPane implements KongaTextComponent {
   private final KongaTextComponentSupport support = new KongaTextComponentSupport(this);

   public KongaEditorPane() {
   }

   public KongaEditorPane(String var1, String var2) {
      super(var1, var2);
   }

   public KongaEditorPane(String var1) throws IOException {
      super(var1);
   }

   public KongaEditorPane(URL var1) throws IOException {
      super(var1);
   }

   @Override
   public void disableContextMenu() {
      this.support.disableContextMenu();
   }

   @Override
   public void addActionToContextMenu(Action var1) {
      this.support.addAdditionalAction(var1);
   }

   @Override
   public void addToContextMenu(JMenuItem var1) {
      this.support.addAdditionalMenuItem(var1);
   }

   @Override
   public void setSelectAllWhenFocused(boolean var1) {
      this.support.setSelectAllWhenFocused(var1);
   }

   @Override
   public boolean isEmpty() {
      return this.getDocument().getLength() == 0;
   }

   public void scrollToRow(int var1) {
      this.support.scrollToRow(var1);
   }

   public void setLabel(String var1) {
      this.support.setLabel(var1);
   }

   public void setLabel(JLabel var1) {
      this.support.setLabel(var1);
   }

   public UiProvider asUiProvider() {
      return this.support.asUiProvider();
   }
}
