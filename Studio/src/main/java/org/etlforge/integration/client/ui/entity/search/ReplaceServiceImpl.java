package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.ui.entity.selector.EntitySelector;
import org.etlforge.integration.client.ui.entity.selector.EntityTableSelector;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.button.KongaButton;

final class ReplaceServiceImpl implements ReplaceService {
   private final AdvancedEntitySearchModel model;
   private final EntityPersistor persistor;
   private final Action searchAction;
   private PropertySearchField searchField;
   private KongaDialog dialog;

   public ReplaceServiceImpl(AdvancedEntitySearchModel var1, EntityPersistor var2, Action var3) {
      this.model = var1;
      this.persistor = var2;
      this.searchAction = var3;
   }

   @Override
   public void startReplace(PropertySearchField var1) {
      checkField(var1);
      this.searchField = var1;
      List var2 = this.model.getMatchingItems();
      if (var2.isEmpty()) {
         Alert.error("No project items match the current search.", "Nothing to replace");
      } else {
         ReplaceServiceImpl.ReplaceUi var3 = new ReplaceServiceImpl.ReplaceUi(var1, var2);
         this.dialog = new WizardStyleDialog(UiUtils.getActiveWindow(), "Replace", var3, KongaDialog.CLOSE_ONLY, true);
         var3.setWaitService(this.dialog);
         this.dialog.setLocationRelativeTo(null);
         this.dialog.setVisible(true);
         this.dialog = null;
      }
   }

   private static void checkField(PropertySearchField var0) {
      Preconditions.checkArgument(var0.getProperty() instanceof ReplaceableProperty, "field does not represent a replaceable property.");
   }

   private static class ConfirmationDialog {
      private final ReplaceMemento memento;
      private KongaDialog dialog;
      private final ReplaceServiceImpl.ReplaceMonitor monitor;

      public ConfirmationDialog(ReplaceMemento var1, ReplaceServiceImpl.ReplaceMonitor var2) {
         this.memento = var1;
         this.monitor = var2;
      }

      public void open() {
         this.dialog = new KongaDialog(UiUtils.getActiveWindow(), "Replace Done");
         BorderLayoutBuilder var1 = this.layout();
         this.dialog.standardLayout(var1, null);
         this.dialog.setResizable(false);
         this.dialog.setLocationRelativeTo(null);
         this.dialog.setVisible(true);
         this.dialog = null;
      }

      private BorderLayoutBuilder layout() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
         var1.north("The values have been replaced.");
         JButton var2 = this.createUndoButton();
         JButton var3 = this.createCloseButton();
         this.dialog.setFocusedComponent(var3);
         BoxBuilder var4 = BoxBuilder.horizontal();
         var4.add(var2).glue().add(var3);
         var1.south(var4);
         var1.withBorder(Empty.border(10));
         return var1;
      }

      private JButton createUndoButton() {
         JButton var1 = new JButton("Undo");
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               ConfirmationDialog.this.startUndo();
               ConfirmationDialog.this.dialog.dispose();
            }
         });
         return var1;
      }

      private JButton createCloseButton() {
         JButton var1 = this.dialog.createCloseButton();
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               ConfirmationDialog.this.monitor.done();
            }
         });
         return var1;
      }

      private void startUndo() {
         WaitLock var1 = this.monitor.startWait();
         ReplaceServiceImpl.UndoWorker var2 = new ReplaceServiceImpl.UndoWorker(this.memento, this.monitor, var1);
         var2.execute();
      }
   }

   private interface ReplaceMonitor {
      WaitLock startWait();

      void replaced(List<IntegrationEntity> var1);

      void allItemsProcessed();

      void done();
   }

   private class ReplaceUi implements UiProvider, ReplaceServiceImpl.ReplaceMonitor {
      private final ReplaceableProperty property;
      private final ReplaceField replaceField;
      private final ReplaceServiceImpl.ReplaceUi.ReplaceAction replaceAction;
      private final EntityTableSelector<IntegrationEntity> itemSelector;
      private final JLabel statusLabel;
      private WaitService waitService = WaitService.NONE;

      public ReplaceUi(PropertySearchField var2, List<IntegrationEntity> var3) {
         this.property = (ReplaceableProperty)var2.getProperty();
         this.replaceField = PropertySearchFieldFactory.createReplaceField(this.property);
         if (var2 instanceof BooleanPropertySearchField) {
            boolean var4 = (Boolean)var2.getValue();
            boolean var5 = !var4;
            this.replaceField.setValue(var5);
         }

         this.replaceAction = new ReplaceServiceImpl.ReplaceUi.ReplaceAction(this);
         this.replaceField.setReplaceAction(this.replaceAction);
         this.itemSelector = new EntityTableSelector<>(var3, IntegrationEntity.class);
         this.itemSelector.setSelectionMode(EntitySelector.SelectionMode.MULTIPLE);
         this.itemSelector.setSelectionState(true);
         this.statusLabel = new JLabel(" ");
      }

      @Override
      public WaitLock startWait() {
         return this.waitService.startWait();
      }

      @Override
      public void replaced(List<IntegrationEntity> var1) {
         int var2 = var1.size();
         if (var2 > 0) {
            IntegrationEntity var3 = (IntegrationEntity)var1.get(var2 - 1);
            String var4 = new EntityPath(var3, false).toString();
            if (var4.length() > 50) {
               int var5 = var4.length() - 50;
               var4 = "..." + var4.substring(var5);
            }

            this.statusLabel.setText("Replaced: " + var4);
         }
      }

      @Override
      public void allItemsProcessed() {
         this.statusLabel.setText(" ");
      }

      @Override
      public void done() {
         ActionUtils.run(ReplaceServiceImpl.this.searchAction, this, "search");
         if (ReplaceServiceImpl.this.dialog != null) {
            ReplaceServiceImpl.this.dialog.dispose();
         }
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
         var2.north(this.layoutFields()).center(this.layoutSelector()).south(this.statusLabel);
         var2.decorate(var1);
      }

      private JComponent layoutFields() {
         TwoColumnPanel var1 = TwoColumnPanel.forFormEntry(10, 5);
         var1.addRow(TextStyles.AdminHintBold.makeLabel(this.replaceField.getProperty().getName()));
         String var2 = this.getOldValueString();
         var1.addRow(new JLabel("Old value:"), new JLabel(var2));
         JLabel var3 = new JLabel("Replace value:");
         if (this.replaceField instanceof TwoRowSearchField) {
            TwoRowSearchField var4 = (TwoRowSearchField)this.replaceField;
            var1.addRow(var3, var4.getTopRow());
            var1.addRow(null, var4.getBottomRow());
         } else {
            var1.addRow(new UiProviderAdapter(var3), this.replaceField);
         }

         var1.addRow(new KongaButton(this.replaceAction), null);
         return var1.container();
      }

      private String getOldValueString() {
         Object var1 = ReplaceServiceImpl.this.searchField.getValue();
         if (var1 instanceof String) {
            SearchOptions var2 = ReplaceServiceImpl.this.model.getOptions();
            return var2.getOldValueRepresentation((String)var1);
         } else if (Boolean.TRUE.equals(var1)) {
            return "Yes";
         } else if (Boolean.FALSE.equals(var1)) {
            return "No";
         } else if (var1 instanceof Password) {
            return "[not shown]";
         } else {
            return var1 == null ? "" : var1.toString();
         }
      }

      private JComponent layoutSelector() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 2);
         var1.north("Items to replace:").center(this.itemSelector);
         return var1.container();
      }

      @Override
      public void requestFocus() {
         this.replaceField.requestFocus();
      }

      public void setWaitService(WaitService var1) {
         this.waitService = var1;
      }

      private class ReplaceAction extends AbstractAction {
         private final ReplaceServiceImpl.ReplaceMonitor monitor;

         public ReplaceAction(ReplaceServiceImpl.ReplaceMonitor var2) {
            super("Replace");
            this.monitor = var2;
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  List var1x = ReplaceUi.this.itemSelector.getSelectedEntities();
                  ReplaceServiceImpl.ReplaceUi.Worker var2 = ReplaceUi.this.new Worker(ReplaceUi.this.property, var1x, ReplaceAction.this.monitor);
                  var2.execute();
               }
            });
         }
      }

      private class Worker extends KongaSwingWorker<ReplaceMemento, IntegrationEntity> {
         private final ReplaceableProperty property;
         private final Collection<IntegrationEntity> items;
         private final ReplaceServiceImpl.ReplaceMonitor monitor;
         private volatile WaitLock waitLock;

         public Worker(ReplaceableProperty var2, Collection<IntegrationEntity> var3, ReplaceServiceImpl.ReplaceMonitor var4) {
            this.property = var2;
            this.items = var3;
            this.monitor = var4;
         }

         protected ReplaceMemento doInBackground() throws Exception {
            this.waitLock = this.monitor.startWait();
            ReplaceMemento var1 = new ReplaceMemento(ReplaceServiceImpl.this.persistor, this.property, this.items);
            Replacer var2 = Replacer.getReplacer(ReplaceServiceImpl.this.searchField, ReplaceServiceImpl.this.model.getOptions());
            Object var3 = Utils.getValue(ReplaceUi.this.replaceField);

            for (IntegrationEntity var5 : this.items) {
               var2.doReplace(var5, var3);

               try {
                  ReplaceServiceImpl.this.persistor.save(var5);
                  this.publish(var5);
               } catch (InterchangeSavingException var7) {
                  ErrorLog.attention(ReplaceServiceImpl.class, null, var7);
               }
            }

            return var1;
         }

         @Override
         protected void process(List<IntegrationEntity> var1) {
            this.monitor.replaced(var1);
         }

         @Override
         protected void finished() {
            this.monitor.allItemsProcessed();
            this.waitLock.release();
         }

         protected void onSuccess(ReplaceMemento var1) {
            ReplaceServiceImpl.ConfirmationDialog var2 = new ReplaceServiceImpl.ConfirmationDialog(var1, this.monitor);
            var2.open();
         }
      }
   }

   private static class UndoWorker extends KongaSwingWorker<Void, Void> {
      private final ReplaceMemento memento;
      private final ReplaceServiceImpl.ReplaceMonitor monitor;
      private final WaitLock waitLock;

      public UndoWorker(ReplaceMemento var1, ReplaceServiceImpl.ReplaceMonitor var2, WaitLock var3) {
         this.memento = var1;
         this.monitor = var2;
         this.waitLock = var3;
      }

      protected Void doInBackground() throws Exception {
         this.memento.restore();
         return null;
      }

      @Override
      protected void finished() {
         this.monitor.done();
         this.waitLock.release();
      }
   }
}
