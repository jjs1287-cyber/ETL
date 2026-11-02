package org.etlforge.ui.widget.combobox;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.label.KongaLabel;

public class KongaComboBox extends JComboBox implements KongaWidget, MutableSelectionSource {
   private final ComboBoxSelectionSource selectionSource = new ComboBoxSelectionSource(this);
   private KongaComboBox.SelectedObjectSender selectedObjectSender;
   private DelayedJob delayedSelectionSendingJob;
   private JLabel label;
   private UiProvider asUiProvider;

   public KongaComboBox() {
   }

   public KongaComboBox(ComboBoxModel var1) {
      super(var1);
   }

   public KongaComboBox(Object[] var1) {
      super(var1);
   }

   public KongaComboBox(Iterable<?> var1) {
      this(Lists.newArrayList(var1));
   }

   public KongaComboBox(Vector<?> var1) {
      super(var1);
   }

   public KongaComboBox(Collection<?> var1) {
      super(var1.toArray());
   }

   public void setSelectionConverter(SelectionConverter var1) {
      this.selectionSource.setConverter(var1);
   }

   public IndexedSelection getSelection() {
      return this.selectionSource.getSelection();
   }

   @Override
   public void setSelection(Selection var1) {
      this.selectionSource.setSelection(var1);
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionSource.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionSource.removeSelectionListener(var1);
   }

   public void selectFirst() {
      if (this.getItemCount() > 0) {
         this.setSelectedIndex(0);
      }
   }

   @Override
   public UiProvider asUiProvider() {
      if (this.asUiProvider == null) {
         this.asUiProvider = this.createUiProvider();
      }

      return this.asUiProvider;
   }

   private UiProvider createUiProvider() {
      if (this.label == null) {
         return new UiProviderAdapter(this);
      }

      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 2);
      var1.north(this.label).center(this).nonOpaque();
      return var1;
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      throw new NotImplementedYetException();
   }

   public void setSelectionReceiver(Receiver<Object> var1) {
      this.setSelectionReceiver(0, var1);
   }

   public void setSelectionReceiver(int var1, Receiver<Object> var2) {
      this.cancelPendingSelectionNotification();
      this.removeActionListener(this.selectedObjectSender);
      this.selectedObjectSender = null;
      if (var2 != null) {
         this.selectedObjectSender = new KongaComboBox.SelectedObjectSender(var1, var2);
         this.addActionListener(this.selectedObjectSender);
      }
   }

   public void setLabel(String var1) {
      KongaLabel var2 = var1 == null ? null : KongaLabel.withMnemonic(var1);
      this.setLabel(var2);
   }

   public void setLabel(JLabel var1) {
      this.label = var1;
      if (var1 != null) {
         var1.setLabelFor(this);
      }
   }

   @Override
   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      if (this.label != null) {
         this.label.setEnabled(var1);
      }
   }

   private void cancelPendingSelectionNotification() {
      if (this.delayedSelectionSendingJob != null) {
         this.delayedSelectionSendingJob.cancel();
         this.delayedSelectionSendingJob = null;
      }
   }

   public void forceDelayedSelection() {
      if (this.delayedSelectionSendingJob != null) {
         this.delayedSelectionSendingJob.runNow();
         this.delayedSelectionSendingJob = null;
      }
   }

   private final class SelectedObjectSender implements ActionListener {
      private final int delay;
      private final Receiver<Object> receiver;

      public SelectedObjectSender(int var2, Receiver<Object> var3) {
         this.delay = var2;
         this.receiver = var3;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.delay <= 0) {
            this.sendToReceiver();
         } else {
            KongaComboBox.this.cancelPendingSelectionNotification();
            KongaComboBox.this.delayedSelectionSendingJob = DelayedJob.createAndSchedule(this.delay, new Runnable() {
               @Override
               public void run() {
                  SelectedObjectSender.this.sendToReceiver();
               }
            });
         }
      }

      private void sendToReceiver() {
         this.receiver.handle(KongaComboBox.this.getSelectedItem());
      }
   }
}
