package org.etlforge.integration.client.ui.de.global;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashSet;
import java.util.List;
import javax.swing.Action;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.script.de.BuiltInDataElements;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.integration.uidelegate.project.ProjectIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionListeners;
import org.etlforge.ui.selection.SelectionSource;

public final class SplitDataElementsListWidget implements DataElementsWidget {
   private final DataElementsList projectDataElementsList;
   private final DataElementsList builtInDataElementsList;
   private GlobalDataElements deStore;
   private final SplitDataElementsListWidget.SelectionSupport selectionSupport;
   private final JLabel projectHeaderLabel = new JLabel("Project Variables", ProjectIcons.PROJECT_16, 10);

   public SplitDataElementsListWidget() {
      this.projectDataElementsList = new DataElementsList();
      this.builtInDataElementsList = new DataElementsList(BuiltInDataElements.getBuiltInDataElements());
      this.builtInDataElementsList.clearSelection();
      this.selectionSupport = new SplitDataElementsListWidget.SelectionSupport();
      this.configureList(this.projectDataElementsList, this.builtInDataElementsList);
      this.configureList(this.builtInDataElementsList, this.projectDataElementsList);
   }

   private void configureList(DataElementsList var1, DataElementsList var2) {
      var1.addFocusListener(new SplitDataElementsListWidget.SelectionClearer(var2));
      var1.addSelectionListener(this.selectionSupport);
   }

   @Override
   public void setActionProvider(Supplier<List<Action>> var1) {
      this.projectDataElementsList.setActionProvider(var1);
      this.builtInDataElementsList.setActionProvider(var1);
   }

   @Override
   public void setDataElements(GlobalDataElements var1) {
      this.deStore = var1;
      this.update(false);
   }

   private void update(boolean var1) {
      HashSet var2 = Sets.newHashSet();
      if (this.deStore != null) {
         var2.addAll(this.deStore.getAllDataElements());
      }

      this.projectDataElementsList.setDataElements(var2, var1);
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      var1.setSelectionSource(this.selectionSupport);
      this.projectDataElementsList.setDefaultAction(new SplitDataElementsListWidget.DefaultAction(var1));
      this.builtInDataElementsList.setDefaultAction(new SplitDataElementsListWidget.DefaultAction(var1));
   }

   @Override
   public void setIncludeBuiltInDataElements(boolean var1) {
   }

   @Override
   public void update() {
      this.update(true);
   }

   @Override
   public void decorate(UiCanvas var1) {
      UiProvider var2 = this.layoutList(this.projectDataElementsList, this.projectHeaderLabel);
      UiProvider var3 = this.layoutList(this.builtInDataElementsList, new JLabel("ETLForge Variables", ClientIcons.APPLICATION_16, 10));
      Grid var4 = Grid.oneRow(10).addAll(var2, var3);
      var4.deeplyNonOpaque().decorate(var1);
   }

   private UiProvider layoutList(DataElementsList var1, JLabel var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 3);
      TextStyles.DefaultBold.makeOver(var2);
      var3.north(var2).center(var1).withBorder(Empty.border(5));
      return var3;
   }

   @Override
   public void requestFocus() {
      this.projectDataElementsList.requestFocus();
      this.builtInDataElementsList.clearSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionSupport.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionSupport.removeSelectionListener(var1);
   }

   public OrderedSelection getSelection() {
      return this.selectionSupport.getSelection();
   }

   @Override
   public void setFilter(Predicate<String> var1) {
      this.projectDataElementsList.setFilter(var1);
      this.builtInDataElementsList.setFilter(var1);
   }

   @Override
   public void selectFirst() {
      if (this.projectDataElementsList.isEmpty()) {
         this.builtInDataElementsList.selectFirst();
      } else {
         this.projectDataElementsList.selectFirst();
      }
   }

   @Override
   public boolean isEmpty() {
      return this.projectDataElementsList.isEmpty() && this.builtInDataElementsList.isEmpty();
   }

   @Override
   public void setProcess(IntegrationEntity var1) {
      if (var1 == null) {
         this.projectHeaderLabel.setText("Project Variables");
         this.projectHeaderLabel.setIcon(ProjectIcons.PROJECT_16);
      } else {
         RendererUtils.decorateRenderer(this.projectHeaderLabel, var1, EntityIcons.Size.SMALL);
      }
   }

   private static final class DefaultAction extends AbstractSelectionAction {
      private final SelectionAction wrappee;

      public DefaultAction(SelectionAction var1) {
         this.wrappee = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (!this.getSelection().isEmpty()) {
            this.wrappee.actionPerformed(var1);
         }
      }
   }

   private static final class SelectionClearer extends FocusAdapter {
      private final DataElementsList listToClear;

      public SelectionClearer(DataElementsList var1) {
         this.listToClear = var1;
      }

      @Override
      public void focusGained(FocusEvent var1) {
         this.listToClear.clearSelection();
      }
   }

   private final class SelectionSupport implements SelectionListener, SelectionSource {
      private final SelectionListeners listeners = new SelectionListeners();
      private OrderedSelection selection = OrderedSelection.EMPTY;

      private SelectionSupport() {
      }

      @Override
      public void addSelectionListener(SelectionListener var1) {
         this.listeners.add(var1);
      }

      public OrderedSelection getSelection() {
         return this.selection;
      }

      @Override
      public void removeSelectionListener(SelectionListener var1) {
         this.listeners.remove(var1);
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         this.selection = SplitDataElementsListWidget.this.projectDataElementsList.getSelection();
         if (this.selection.isEmpty()) {
            this.selection = SplitDataElementsListWidget.this.builtInDataElementsList.getSelection();
         }

         this.listeners.call().selectionChanged(new SelectionChangedEvent(this, this.selection));
      }
   }
}
