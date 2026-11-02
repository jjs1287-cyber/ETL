package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Sets;
import java.awt.Window;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.structure.crom.CROM;
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

abstract class AbstractXmlExtensionSelector {
   private final TreeComponent tree;
   private final Node node;
   private final Set<CROM> allExtensions;
   private final Set<CROM> currentExtensions;

   protected AbstractXmlExtensionSelector(TreeComponent var1, Node var2, Collection<String> var3) {
      this.tree = var1;
      this.node = var2;
      this.allExtensions = this.getAllCromTypes(var3);
      this.currentExtensions = this.getCurrentExtensions();
   }

   private Set<CROM> getAllCromTypes(Collection<String> var1) {
      TreeSet var2 = new TreeSet<>(new AbstractXmlExtensionSelector.CromOrder());

      for (String var4 : var1) {
         CROM var5 = this.lookupCromNode(this.tree, var4);
         if (var5 != null && !var5.isAbstract()) {
            var2.add(var5);
         }
      }

      return var2;
   }

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

      for (Node var3 : this.node.getChildren()) {
         CROM var4 = var3.getCROM();
         if (var4 != null && (var4.isTypeDefinition() || var4.isElementNode())) {
            String var5 = this.getQualifiedName(var4);
            var1.add(var5);
         }
      }

      return var1;
   }

   public final void open(String var1, String var2) {
      this.getEditor().show(var1, var2);
   }

   private AbstractXmlExtensionSelector.Editor getEditor() {
      OverlayContainer var1 = WidgetUtils.getOverlayContainerAncestor(this.tree);
      return var1 != null ? new AbstractXmlExtensionSelector.OverlayEditor(var1) : new AbstractXmlExtensionSelector.DialogEditor();
   }

   protected abstract String getQualifiedName(CROM var1);

   protected abstract CROM lookupCromNode(TreeComponent var1, String var2);

   protected abstract void addExtension(Node var1, CROM var2);

   private class CromOrder implements Comparator<CROM>, Serializable {
      private CromOrder() {
      }

      public int compare(CROM var1, CROM var2) {
         return ComparisonChain.start()
            .compare(var1.getName(), var2.getName(), String.CASE_INSENSITIVE_ORDER)
            .compare(
               AbstractXmlExtensionSelector.this.getQualifiedName(var1),
               AbstractXmlExtensionSelector.this.getQualifiedName(var2),
               String.CASE_INSENSITIVE_ORDER
            )
            .result();
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
      private DialogEditor() {
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
         Window var3 = SwingUtilities.getWindowAncestor(AbstractXmlExtensionSelector.this.tree);
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
            this.removeUnselectedExtensions(var1);
            this.addNewTypes(var1);
            this.updateTree();
         }
      }

      private void removeUnselectedExtensions(Set<CROM> var1) {
         for (CROM var3 : AbstractXmlExtensionSelector.this.currentExtensions) {
            if (!var1.contains(var3)) {
               this.removeExtension(var3);
            }
         }
      }

      private void addNewTypes(Set<CROM> var1) {
         for (CROM var3 : var1) {
            if (!AbstractXmlExtensionSelector.this.currentExtensions.contains(var3)) {
               AbstractXmlExtensionSelector.this.addExtension(AbstractXmlExtensionSelector.this.node, var3);
            }
         }
      }

      private void updateTree() {
         AbstractXmlExtensionSelector.this.tree.refresh();
         AbstractXmlExtensionSelector.this.tree.setDirty();
         if (AbstractXmlExtensionSelector.this.tree.getTreeMapper() != null) {
            AbstractXmlExtensionSelector.this.tree.getTreeMapper().setDirty(true);
         }
      }

      private void removeExtension(CROM var1) {
         for (Node var3 : AbstractXmlExtensionSelector.this.node.getChildren()) {
            if (var3.getDisplayName().equals(var1.getName())) {
               var3.removeMappingsUnderFolder();
               AbstractXmlExtensionSelector.this.node.removeChild(var3);
               return;
            }
         }
      }

      protected final void giveFocusToTree() {
         DelayedJob.createAndSchedule(100, new Runnable() {
            @Override
            public void run() {
               AbstractXmlExtensionSelector.this.tree.requestFocusInWindow();
            }
         });
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
      private final OverlayContainer container;
      private final SelectorTable<CROM> table;

      public OverlayEditor(OverlayContainer var2) {
         this.container = var2;
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
         var5.setFocusReceiverWhenClosed(AbstractXmlExtensionSelector.this.node.getTreeMapper());
         var5.setFadeIn(AnimationPreference.get());
         var5.setAlpha(0.8F);
         var5.useOkAndCancel();
         var5.setBlocking(true);
         return var5;
      }
   }
}
