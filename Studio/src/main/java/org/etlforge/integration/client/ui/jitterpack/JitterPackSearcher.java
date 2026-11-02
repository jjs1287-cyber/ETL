package org.etlforge.integration.client.ui.jitterpack;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackLocator;
import org.etlforge.integration.client.project.jitterpack.JitterPackLocatorCallback;
import org.etlforge.integration.client.project.jitterpack.JitterPackLocatorFactory;
import org.etlforge.integration.client.ui.jitterpack.table.JitterPackTable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.Flow;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.progress.ProgressListenerPanel;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.table.FileSizeColumn;
import org.etlforge.util.StorageSizeUnit;
import org.etlforge.util.preference.PreferencesKey;
import org.etlforge.util.progress.ProgressEvent;

final class JitterPackSearcher implements JitterPackLocatorUi {
   private final ApplicationWindow window;
   private final ImportParametersDefiner paramsDefiner;
   private FileSelectorPanel rootSelector;
   private JCheckBox extensionCheck;
   private ProgressListenerPanel progressPanel;
   private JComponent progressPanelCapsule;
   private JitterPackTable table;
   private JitterPackSearcher.StartStopAction startStopAction;
   private JPanel panel;
   private boolean enabled;

   public JitterPackSearcher(ApplicationWindow var1, ImportParametersDefiner var2) {
      this.window = var1;
      this.paramsDefiner = var2;
      this.enabled = true;
      this.createComponents();
      this.updateStartStopActionEnabledState();
   }

   @Override
   public void addJitterPackSelectionListener(JitterPackSelectionListener var1) {
      this.table.addSelectionListener(var1);
   }

   @Override
   public void removeJitterPackSelectionListener(JitterPackSelectionListener var1) {
      this.table.removeSelectionListener(var1);
   }

   public void addActionListener(ActionListener var1) {
      this.table.addActionListener(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.table.removeActionListener(var1);
   }

   private void createComponents() {
      this.createStartStopAction();
      this.createRootSelector();
      this.createExtensionCheck();
      this.createProgressPanel();
      this.createTable();
      this.createUi();
   }

   private void createUi() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setFill(OneColumnPanel.NONE);
      var1.withInsets(5, 5, 5, 5);
      var1.add(this.rootSelector);
      var1.add(this.extensionCheck);
      var1.add(this.createSearchCapsule());
      var1.add(this.createTableCapsule());
      this.panel = InvisiblePanel.newPanel();
      var1.addToParent(this.panel, null);
      UiUtils.setOpaqueRecursivelySkipDefaults(this.panel, false);
   }

   @Override
   public JitterPack getJitterPack() {
      return this.table.getSelectedJitterPack();
   }

   @Override
   public boolean isEnabled() {
      return this.enabled;
   }

   @Override
   public void setEnabled(boolean var1) {
      this.enabled = var1;
      UiUtils.setEnabledRecursively(this.panel, var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private void createExtensionCheck() {
      this.extensionCheck = new KongaCheckBox(getExtensionCheckText(), true);
   }

   private static String getExtensionCheckText() {
      return Strings.format("Searcher.ExtensionCheck", ".jpk");
   }

   private void createProgressPanel() {
      this.progressPanel = new ProgressListenerPanel(false);
      LabelDecorator var1 = TextStyles.DefaultText.asLabelDecorator(null);
      this.progressPanel.decorateLabel(var1);
   }

   private JComponent createSearchCapsule() {
      Flow var1 = Flow.leading(5, 5);
      var1.add(new KongaButton(this.startStopAction), this.createProgressPanelCapsule());
      return var1.container();
   }

   private JComponent createProgressPanelCapsule() {
      this.progressPanelCapsule = InvisiblePanel.of(this.progressPanel);
      this.progressPanelCapsule.setVisible(false);
      return this.progressPanelCapsule;
   }

   private void createRootSelector() {
      FileSelectorPanelFactory var1 = FileSelectorFactory.getFactory();
      this.rootSelector = var1.recentFilesPanel(
         ApplicationFileChooser.getInstance(this.window), FileSelectorPanel.OPEN, Strings.get("Searcher.Folder.Caption"), this.getRecentFileStore()
      );
      this.rootSelector.setSelectionMode(FileChooser.DIRECTORIES_ONLY);
      this.rootSelector.addActionListener(this.startStopAction);
      this.rootSelector.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            JitterPackSearcher.this.updateStartStopActionEnabledState();
         }
      });
      LabelDecorator var2 = TextStyles.DefaultText.asLabelDecorator(CommonIcons.FOLDER_CLOSED_16);
      this.rootSelector.decorateTitle(var2);
   }

   private RecentFileStore getRecentFileStore() {
      PreferencesKey var1 = new PreferencesKey(ImportJitterPackUi.class, "SearchDirectory");
      return new PreferencesFileStore(var1, 10);
   }

   private void createTable() {
      this.table = new JitterPackTable(true);
      this.table.addSelectionListener(new JitterPackSearcher.TableSelectionBroadcaster());
   }

   private OneColumnPanel createTableCapsule() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setFill(OneColumnPanel.NONE);
      var1.setWeightX(1.0);
      var1.setWeightY(1.0);
      var1.withInsets(2, 5, 2, 5);
      var1.add(Strings.getJitterPackInPluralString("Searcher.Table.Caption"));
      var1.add(this.table);
      return var1;
   }

   private void createStartStopAction() {
      this.startStopAction = new JitterPackSearcher.StartStopAction();
   }

   private void updateStartStopActionEnabledState() {
      this.startStopAction.setEnabled(this.rootSelector.isInputValid());
   }

   private void validateLayout() {
      this.panel.validate();
      this.panel.repaint();
   }

   private class LocatorCallbackImpl implements JitterPackLocatorCallback {
      private LocatorCallbackImpl() {
      }

      @Override
      public void locatedJitterPack(final JitterPack var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               JitterPackSearcher.this.table.addJitterPack(var1);
            }
         });
      }

      @Override
      public void progressEnded(final ProgressEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               LocatorCallbackImpl.this.progressEndedImpl(var1);
            }
         });
      }

      private void progressEndedImpl(ProgressEvent var1) {
         JitterPackSearcher.this.progressPanel.progressEnded(var1);
         JitterPackSearcher.this.startStopAction.setSelected(false);
         JitterPackSearcher.this.progressPanelCapsule.setVisible(false);
         JitterPackSearcher.this.validateLayout();
      }

      @Override
      public void progressStarted(final ProgressEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               LocatorCallbackImpl.this.progressStartedImpl(var1);
            }
         });
      }

      private void progressStartedImpl(ProgressEvent var1) {
         JitterPackSearcher.this.progressPanelCapsule.setVisible(true);
         JitterPackSearcher.this.validateLayout();
         JitterPackSearcher.this.progressPanel.progressStarted(var1);
      }

      @Override
      public void progressUpdated(final ProgressEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               LocatorCallbackImpl.this.progressUpdatedImpl(var1);
            }
         });
      }

      private void progressUpdatedImpl(ProgressEvent var1) {
         JitterPackSearcher.this.progressPanel.progressUpdated(var1);
      }
   }

   private class StartStopAction extends AbstractAction implements ToggleAction {
      private boolean selected;
      private JitterPackLocator locator;

      public StartStopAction() {
         super(ApplicationResources.getString("Search"));
         this.putValue("AcceleratorKey", KeyUtils.SPACE);
      }

      @Override
      public boolean isSelected() {
         return this.selected;
      }

      @Override
      public void setSelected(boolean var1) {
         if (var1 != this.selected) {
            this.selected = var1;
            if (var1) {
               this.search();
            } else {
               this.stop();
            }
         }
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.setSelected(!this.isSelected());
      }

      private void search() {
         this.putValue("Name", ApplicationResources.getString("Stop"));
         this.putValue("SmallIcon", ApplicationResources.ICONS.getIcon("Stop.16"));
         JitterPackSearcher.this.table.removeAllRows();
         JitterPackSearcher.this.rootSelector.setEnabled(false);
         this.locator = JitterPackLocatorFactory.newLocator();
         File var1 = JitterPackSearcher.this.rootSelector.getSelectedFile();
         this.locator
            .search(
               var1,
               true,
               JitterPackSearcher.this.paramsDefiner.getParameters(),
               JitterPackSearcher.this.extensionCheck.isSelected(),
               JitterPackSearcher.this.new LocatorCallbackImpl()
            );
      }

      private void stop() {
         this.putValue("Name", ApplicationResources.getString("Search"));
         this.putValue("SmallIcon", null);
         if (this.locator != null) {
            this.locator.stop();
            this.locator = null;
         }

         JitterPackSearcher.this.rootSelector.setEnabled(true);
         JitterPackSearcher.this.progressPanelCapsule.setVisible(false);
         JitterPackSearcher.this.validateLayout();
      }
   }

   private class TableSelectionBroadcaster implements JitterPackSelectionListener {
      private TableSelectionBroadcaster() {
      }

      @Override
      public void selectionChanged(JitterPack var1) {
         JitterPackSearcher.this.window.postApplicationMessageEvent(this.getEventToBroadcast(var1));
      }

      private ApplicationMessageEvent getEventToBroadcast(JitterPack var1) {
         return new ApplicationMessageEvent(this, this.getMessageToBroadcast(var1));
      }

      private String getMessageToBroadcast(JitterPack var1) {
         if (var1 == null) {
            return " ";
         }

         StringBuilder var2 = new StringBuilder("Size: ");
         var2.append(new FileSizeColumn(var1.getFile(), StorageSizeUnit.Kilobytes));
         String var3 = var1.getDescriptor().getComment();
         if (var3 != null && var3.length() > 0) {
            var2.append("; Comment: ").append(var3);
         }

         return var2.toString();
      }
   }
}

