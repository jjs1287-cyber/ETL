package org.etlforge.application.ui.prefs;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.ui.DimensionUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.wait.WindowWaitService;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.split.KongaSplitPane;

public final class PreferencesViewer {
   private final ApplicationPreferences prefs;
   private final Map<ApplicationPreferences.Node, PreferencesPage> visitedPages;
   private final PreferencesViewer.Ui ui;
   private String[] initialPath;

   public PreferencesViewer(ApplicationPreferences var1) {
      this.prefs = Preconditions.checkNotNull(var1);
      this.ui = new PreferencesViewer.Ui();
      this.visitedPages = Maps.newHashMap();
   }

   public void open(final JFrame var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            PreferencesViewer.this.restoreAllVisitedPages();
            KongaDialog var1x = this.createDialog(PreferencesViewer.this.ui);
            var1x.setVisible(true);
            if (var1x.wasOkPressed()) {
               PreferencesViewer.this.apply(var1x.getOwner());
            }
         }

         private KongaDialog createDialog(PreferencesViewer.Ui var1x) {
            JPanel var2 = BorderLayoutBuilder.fillWith(var1x);
            var2.setOpaque(true);
            UiUtils.setPanelBackgrounds(var2, Colors.NEUTRAL_BACKGROUND);
            WizardStyleDialog var3 = new WizardStyleDialog(var1, "Preferences", var2);
            var3.setFocusedComponent(var1x);
            var3.manageLocation(PreferencesViewer.class);
            Dimension var4 = var3.getSize();
            var3.setSize(new Dimension(Math.max(800, var4.width), Math.max(600, var4.height)));
            var3.getOKButton().setToolTipText("Save all changes and close this dialog");
            var3.getCancelButton().setToolTipText("Close this dialog without saving any changes");
            var3.setOkButtonAsDefault();
            return var3;
         }
      });
   }

   public void setInitialPath(String[] var1) {
      this.initialPath = (String[])var1.clone();
   }

   private void apply(Window var1) {
      (new BusyWorker(this.getWaitService(var1)) {
         @Override
         protected void doWork() {
            PreferencesViewer.this.applyVisitedPages();
         }
      }).run();
   }

   private void applyVisitedPages() {
      for (PreferencesPage var2 : this.visitedPages.values()) {
         var2.apply();
      }
   }

   private WaitService getWaitService(Window var1) {
      if (var1 instanceof WaitService) {
         return (WaitService)var1;
      } else {
         return var1 instanceof RootPaneContainer ? new WindowWaitService((RootPaneContainer)var1) : WaitService.NONE;
      }
   }

   private void restoreAllVisitedPages() {
      for (PreferencesPage var2 : this.visitedPages.values()) {
         var2.restore();
      }
   }

   private static String getNodePathAsString(ApplicationPreferences.Node var0) {
      StringBuilder var1 = new StringBuilder();

      for (String var5 : var0.getPath()) {
         if (var1.length() > 0) {
            var1.append(" > ");
         }

         var1.append(var5);
      }

      return var1.toString();
   }

   private static class EmptyPage extends AbstractPreferencesPage {
      public EmptyPage(ApplicationPreferences.Node var1) {
         super(var1.getPath());
      }

      @Override
      public void display(Container var1) {
         var1.setLayout(new BorderLayout());
         KongaLabel var2 = TextStyles.InfoText.makeLabel("[There are no preferences registered for this node]");
         var2.setHorizontalAlignment(0);
         var1.add(var2, "Center");
      }
   }

   private static class PageLayout {
      private final PreferencesPage page;
      private final JPanel panel;
      private final JButton applyButton;

      public PageLayout(PreferencesPage var1) {
         this.page = var1;
         this.panel = new JPanel(new BorderLayout(0, 5));
         this.applyButton = this.createApplyButton();
         this.layoutPage();
         this.layoutButtons();
         this.listenForChanges();
         UiUtils.setPanelBackgrounds(this.panel, Colors.NEUTRAL_BACKGROUND);
      }

      public JComponent getUi() {
         return new KongaScrollPane(this.panel);
      }

      private void layoutPage() {
         JPanel var1 = new JPanel();
         this.page.display(var1);
         JPanel var2 = BorderLayoutBuilder.fillWith(var1);
         this.panel.add(var2, "Center");
      }

      private void layoutButtons() {
         BoxBuilder var1 = BoxBuilder.horizontal();
         var1.glue().add(this.createRestoreDefaultsButton()).strut(10).add(this.applyButton);
         var1.withEmptyBorder(0, 0, 10, 10);
         var1.addToParent(this.panel, "South");
      }

      private JButton createApplyButton() {
         KongaButton var1 = new KongaButton("&Apply");
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               PageLayout.this.doApply();
            }
         });
         var1.setToolTipText("Save the settings in the current page");
         var1.setEnabled(false);
         return var1;
      }

      private void doApply() {
         this.page.apply();
         this.applyButton.setEnabled(false);
      }

      private JButton createRestoreDefaultsButton() {
         KongaButton var1 = new KongaButton("Restore Defaults");
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               PageLayout.this.page.restoreDefaults();
            }
         });
         var1.setToolTipText("Restore the default settings in the current page");
         return var1;
      }

      private void listenForChanges() {
         this.page.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent var1) {
               PageLayout.this.applyButton.setEnabled(PageLayout.this.page.isDirty());
            }
         });
      }
   }

   private class Ui implements UiProvider, TreeSelectionListener {
      private final PreferencesTreeModel treeModel = new PreferencesTreeModel(PreferencesViewer.this.prefs.getRoot());
      private final JTree tree = this.createTree();
      private final CardPanel cards;
      private final JLabel titlePane;

      public Ui() {
         this.tree.getSelectionModel().addTreeSelectionListener(this);
         this.cards = new CardPanel();
         this.titlePane = TextStyles.DefaultBoldLarger.makeLabel(" ");
         this.titlePane.setBorder(new BorderBuilder().etched().empty(5, 10, 5, 5));
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder();
         var2.north(this.titlePane).center(this.cards);
         KongaSplitPane var3 = KongaSplitPane.horizontalSplit(new KongaScrollPane(this.tree), var2.container());
         var3.setDividerSize(4);
         var3.setDividerLocation(250);
         var1.display(var3);
      }

      @Override
      public void requestFocus() {
         this.tree.requestFocusInWindow();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Ui.this.setInitialSelection();
            }
         });
      }

      private JTree createTree() {
         JTree var1 = new JTree(this.treeModel) {
            @Override
            public Dimension getPreferredSize() {
               return DimensionUtils.withWidthAtLeast(super.getPreferredSize(), 250);
            }
         };
         var1.setRootVisible(false);
         var1.setShowsRootHandles(true);
         var1.getSelectionModel().setSelectionMode(1);
         var1.setRowHeight(20);
         var1.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
               super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
               this.setIcon(null);
               return this;
            }
         });
         return var1;
      }

      @Override
      public void valueChanged(TreeSelectionEvent var1) {
         TreePath var2 = var1.getPath();
         Object var3 = var2.getLastPathComponent();
         if (var3 instanceof ApplicationPreferences.Node) {
            this.displayPage((ApplicationPreferences.Node)var3);
         }
      }

      private void setInitialSelection() {
         if (this.tree.getSelectionCount() == 0) {
            if (PreferencesViewer.this.initialPath != null) {
               this.selectInitialPath();
            } else {
               this.selectFirstNode();
            }
         }
      }

      private void selectInitialPath() {
         ApplicationPreferences.Node var1 = PreferencesViewer.this.prefs.getNodeFromPath(PreferencesViewer.this.initialPath);
         if (var1 != null) {
            TreePath var2 = this.treeModel.getPathFromRoot(var1);
            if (var2 != null) {
               this.tree.makeVisible(var2);
               this.tree.setSelectionPath(var2);
            }
         }
      }

      private void selectFirstNode() {
         ApplicationPreferences.Node var1 = PreferencesViewer.this.prefs.getRoot();
         ApplicationPreferences.Node[] var2 = var1.getChildren();
         if (var2.length != 0) {
            this.tree.setSelectionRow(0);
         }
      }

      private void displayPage(ApplicationPreferences.Node var1) {
         String var2 = PreferencesViewer.getNodePathAsString(var1);
         if (!PreferencesViewer.this.visitedPages.containsKey(var1)) {
            this.createNewPage(var1, var2);
         }

         this.titlePane.setText(var2);
         this.switchToCard(var2);
      }

      private PreferencesPage createNewPage(ApplicationPreferences.Node var1, String var2) {
         PreferencesPage var3 = this.getPageForNode(var1);
         var3.restore();
         PreferencesViewer.PageLayout var4 = new PreferencesViewer.PageLayout(var3);
         this.cards.add(var4.getUi(), var2);
         PreferencesViewer.this.visitedPages.put(var1, var3);
         return var3;
      }

      private void switchToCard(String var1) {
         this.cards.show(var1);
         JDialog var2 = (JDialog)SwingUtilities.getWindowAncestor(this.cards.container());
         var2.getRootPane().revalidate();
      }

      private PreferencesPage getPageForNode(ApplicationPreferences.Node var1) {
         PreferencesPage var2 = PreferencesViewer.this.prefs.getPage(var1);
         if (var2 == null) {
            var2 = new PreferencesViewer.EmptyPage(var1);
         }

         return var2;
      }
   }
}
