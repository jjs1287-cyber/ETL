package org.etlforge.integration.client.ui.crom.xml;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Sets;
import java.awt.Window;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.client.ui.crom.tree.CromTreeWidget;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.string.StringMatcher;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.string.WildcardStringMatcher;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.widget.SelectorTable;
import org.etlforge.ui.widget.SelectorTableFilterField;
import org.etlforge.ui.widget.SelectorTableStringFilter;
import org.etlforge.ui.widget.TableSelectorDialog;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.util.name.Nameables;

public abstract class AbstractXmlExtensionSelector {
   private final CromTreeWidget tree;
   private final CROM node;
   private final Set<CROM> allExtensions;
   private final Set<CROM> currentExtensions;

   protected AbstractXmlExtensionSelector(CromTreeWidget var1, CROM var2, Collection<String> var3) {
      this.tree = var1;
      this.node = var2;
      this.allExtensions = this.getAllCromTypes(var3);
      this.currentExtensions = this.getCurrentExtensions();
   }

   private Set<CROM> getAllCromTypes(Collection<String> var1) {
      TreeSet var2 = new TreeSet<>(new AbstractXmlExtensionSelector.CromOrder());

      for (String var4 : var1) {
         CROM var5 = this.lookupCromNode(this.tree.getCromTree(), var4);
         if (var5 != null && !var5.isAbstract()) {
            var2.add(var5);
         }
      }

      return var2;
   }

   protected abstract CROM lookupCromNode(CROM_Tree var1, String var2);

   private Set<CROM> getCurrentExtensions() {
      Set var1 = this.collectCurrentChildNames();
      HashSet var2 = Sets.newHashSet();

      for (CROM var4 : this.allExtensions) {
         String var5 = this.getQualifiedName(var4);
         if (var1.contains(var5)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private Set<String> collectCurrentChildNames() {
      HashSet var1 = Sets.newHashSet();

      for (CROM var3 : this.node.getChildren()) {
         if (var3 != null && (var3.isTypeDefinition() || var3.isElementNode())) {
            String var4 = this.getQualifiedName(var3);
            var1.add(var4);
         }
      }

      return var1;
   }

   public final void open(JComponent var1, String var2, String var3) {
      this.getEditor(var1).show(var2, var3);
   }

   private AbstractXmlExtensionSelector.Editor getEditor(JComponent var1) {
      OverlayContainer var2 = WidgetUtils.getOverlayContainerAncestor(var1);
      return var2 != null ? new AbstractXmlExtensionSelector.OverlayEditor(var1, var2) : new AbstractXmlExtensionSelector.DialogEditor(var1);
   }

   protected abstract String getQualifiedName(CROM var1);

   protected abstract void addExtension(CROM_Tree var1, CROM var2, CROM var3);

   private class CromOrder implements Comparator<CROM>, Serializable {
      private CromOrder() {
      }

      public int compare(CROM var1, CROM var2) {
         int var3 = var1.getName().compareToIgnoreCase(var2.getName());
         if (var3 == 0) {
            String var4 = AbstractXmlExtensionSelector.this.getQualifiedName(var1);
            String var5 = AbstractXmlExtensionSelector.this.getQualifiedName(var2);
            var3 = var4.compareToIgnoreCase(var5);
         }

         return var3;
      }
   }

   private class CromRenderer extends DefaultTableCellRenderer {
      private final ImmutableMultiset<String> localNames = ImmutableMultiset.copyOf(Nameables.toNames(AbstractXmlExtensionSelector.this.allExtensions));

      private CromRenderer() {
      }

      @Override
      protected void setValue(Object var1) {
         if (var1 instanceof CROM) {
            CROM var2 = (CROM)var1;
            String var3 = this.getTextForNode(var2);
            super.setValue(var3);
         } else {
            super.setValue(var1);
         }
      }

      private String getTextForNode(CROM var1) {
         String var2 = var1.getName();
         Integer var3 = this.localNames.count(var2);
         return var3 > 1 ? AbstractXmlExtensionSelector.this.getQualifiedName(var1) : var2;
      }
   }

   private class DialogEditor extends AbstractXmlExtensionSelector.Editor {
      private final JComponent owner;

      public DialogEditor(JComponent var2) {
         this.owner = var2;
      }

      @Override
      public void show(String var1, String var2) {
         TableSelectorDialog var3 = this.createDialog(var1, var2);
         var3.setVisible(true);
         if (var3.wasOkPressed()) {
            HashSet var4 = Sets.newHashSet(var3.getSelectedObjects());
            this.applySelection(var4);
         }

         this.giveFocusToTree();
      }

      private TableSelectorDialog<CROM> createDialog(String var1, String var2) {
         Window var3 = SwingUtilities.getWindowAncestor(this.owner);
         TableSelectorDialog var4 = new TableSelectorDialog<>(AbstractXmlExtensionSelector.this.allExtensions, CROM.class, var3, var1, var2, null, true);
         var4.setSelection(SelectionFactory.newOrderedSelection(AbstractXmlExtensionSelector.this.currentExtensions));
         var4.manageLocation(DerivedTypesSelector.class);
         var4.setObjectCellRenderer(AbstractXmlExtensionSelector.this.new CromRenderer());
         var4.setOkButtonAsDefault();
         return var4;
      }
   }

   private abstract class Editor {
      private Editor() {
      }

      public abstract void show(String var1, String var2);

      protected final void applySelection(Set<CROM> var1) {
         if (!AbstractXmlExtensionSelector.this.currentExtensions.equals(var1)) {
            AbstractXmlExtensionSelector.Editor.Worker var2 = new AbstractXmlExtensionSelector.Editor.Worker(var1);
            var2.execute();
         }
      }

      protected final void giveFocusToTree() {
         DelayedJob.createAndSchedule(100, new Runnable() {
            @Override
            public void run() {
               AbstractXmlExtensionSelector.this.tree.requestFocus();
            }
         });
      }

      private class Worker extends KongaSwingWorker<Void, Void> {
         private final Set<CROM> selected;

         public Worker(Set<CROM> var2) {
            this.selected = var2;
         }

         protected Void doInBackground() throws Exception {
            this.removeUnselectedExtensions(this.selected);
            this.addNewTypes(this.selected);
            return null;
         }

         private void removeUnselectedExtensions(Set<CROM> var1) {
            for (CROM var3 : AbstractXmlExtensionSelector.this.currentExtensions) {
               if (!var1.contains(var3)) {
                  this.removeExtension(var3);
               }
            }
         }

         private void removeExtension(CROM var1) {
            for (CROM var3 : AbstractXmlExtensionSelector.this.node.getChildren()) {
               if (var3.getDisplayName().equals(var1.getName())) {
                  AbstractXmlExtensionSelector.this.node.removeChild(var3);
                  return;
               }
            }
         }

         private void addNewTypes(Set<CROM> var1) {
            for (CROM var3 : var1) {
               if (!AbstractXmlExtensionSelector.this.currentExtensions.contains(var3)) {
                  AbstractXmlExtensionSelector.this.addExtension(
                     AbstractXmlExtensionSelector.this.tree.getCromTree(), AbstractXmlExtensionSelector.this.node, var3
                  );
               }
            }
         }

         protected void onSuccess(Void var1) {
            AbstractXmlExtensionSelector.this.tree.rebuildNode(AbstractXmlExtensionSelector.this.node);
            if (AbstractXmlExtensionSelector.this.node.hasChildren()) {
               CROM var2 = AbstractXmlExtensionSelector.this.node.getChild(0);
               AbstractXmlExtensionSelector.this.tree.expandNode(var2);
            }

            AbstractXmlExtensionSelector.this.tree.scrollNodeToVerticalCenter(AbstractXmlExtensionSelector.this.node);
         }
      }
   }

   private static class FilterFieldFunction implements Function<String, Predicate<CROM>> {
      private FilterFieldFunction() {
      }

      public Predicate<CROM> apply(String var1) {
         return var1.isEmpty() ? null : new AbstractXmlExtensionSelector.FilterFieldFunction.Filter(var1);
      }

      private static class Filter extends SelectorTableStringFilter<CROM> {
         private static final StringMatcher MATCHER = new WildcardStringMatcher(false);

         public Filter(String var1) {
            super(new StringMatcherPredicate(MATCHER, var1));
         }

         protected String getString(CROM var1) {
            return var1.getName();
         }
      }
   }

   private class OverlayEditor extends AbstractXmlExtensionSelector.Editor {
      private final JComponent owner;
      private final OverlayContainer container;
      private final SelectorTable<CROM> table;

      public OverlayEditor(JComponent var2, OverlayContainer var3) {
         this.owner = var2;
         this.container = var3;
         this.table = new SelectorTable<>(AbstractXmlExtensionSelector.this.allExtensions, CROM.class);
         this.table.asSelectionSource().setSelection(SelectionFactory.newOrderedSelection(AbstractXmlExtensionSelector.this.currentExtensions));
         this.table.setObjectCellRenderer(AbstractXmlExtensionSelector.this.new CromRenderer());
         this.table.setIncludeBulkSelectionButtons(true);
         this.table.setVisibleRows(16);
      }

      @Override
      public void show(String var1, String var2) {
         OverlayDecorator var3 = this.createOverlay(var1, var2);
         var3.show(this.container, new OverlayCenterPositioner());
         this.table.requestFocus();
      }

      private OverlayDecorator createOverlay(String var1, String var2) {
         BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 5);
         SelectorTableFilterField var4 = SelectorTableFilterField.forTable(this.table, new AbstractXmlExtensionSelector.FilterFieldFunction())
            .withLabel("&Filter")
            .alwaysAcceptSelectedRows();
         var3.northToSouth(var2, this.table, var4).withBorder(Empty.border(10));
         OverlayDecorator var5 = new OverlayDecorator(var3, var1) {
            @Override
            protected void onOk() {
               HashSet var1x = Sets.newHashSet(OverlayEditor.this.table.getSelectedObjects());
               OverlayEditor.this.applySelection(var1x);
            }

            @Override
            protected void onHidden() {
               OverlayEditor.this.giveFocusToTree();
            }
         };
         var5.setFocusReceiverWhenClosed(this.owner);
         var5.setFadeIn(AnimationPreference.get());
         var5.setAlpha(0.8F);
         var5.useOkAndCancel();
         var5.setBlocking(true);
         return var5;
      }
   }
}
