package org.etlforge.integration.client.database.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.PopupMenuButton;
import org.etlforge.util.preference.KongaPreferences;

public final class ColumnDisplayOrderSelector implements UiProvider {
   private static final String PREF_KEY = "ColumnOrder";
   private final KongaPreferences prefs = KongaPreferences.getPreferences(this.getClass());
   private final JRadioButtonMenuItem alphabetical = new JRadioButtonMenuItem("Alphabetical");
   private final JRadioButtonMenuItem database = new JRadioButtonMenuItem("Database");
   private Receiver<ColumnDisplayOrder> callback;

   public ColumnDisplayOrderSelector() {
      ButtonUtils.makeMutuallyExclusive(this.alphabetical, this.database);
      ButtonUtils.addActionListener(new ColumnDisplayOrderSelector.Listener(), this.alphabetical, this.database);
      this.callback = NoReceiver.create();
      this.restorePreference();
   }

   private void restorePreference() {
      ColumnDisplayOrder var1 = this.prefs.getEnum("ColumnOrder", ColumnDisplayOrder.class, ColumnDisplayOrder.DATABASE);
      if (var1 == ColumnDisplayOrder.DATABASE) {
         this.database.setSelected(true);
      } else {
         this.alphabetical.setSelected(true);
      }
   }

   public ColumnDisplayOrder getSelectedOrder() {
      return this.alphabetical.isSelected() ? ColumnDisplayOrder.ALPHABETICAL : ColumnDisplayOrder.DATABASE;
   }

   public void setCallback(Receiver<ColumnDisplayOrder> var1) {
      this.callback = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      PopupMenuButton var2 = this.createButton();
      Horizontal.box().of("Column display order: ", var2).decorate(var1);
   }

   private PopupMenuButton createButton() {
      JPopupMenu var1 = new JPopupMenu();
      var1.add(this.database);
      var1.add(this.alphabetical);
      PopupMenuButton var2 = new PopupMenuButton(null, ApplicationResources.ICONS.getIcon("ArrowDown.16"), var1);
      return ButtonUtils.configureButtonForToolBar(var2);
   }

   @Override
   public void requestFocus() {
      this.database.requestFocusInWindow();
   }

   private class Listener implements ActionListener {
      private Listener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ColumnDisplayOrder var2 = var1.getSource() == ColumnDisplayOrderSelector.this.database ? ColumnDisplayOrder.DATABASE : ColumnDisplayOrder.ALPHABETICAL;
         ColumnDisplayOrderSelector.this.prefs.putEnum("ColumnOrder", var2);
         ColumnDisplayOrderSelector.this.callback.handle(var2);
      }
   }
}
