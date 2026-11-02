package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.search.string.StringSearchModelDefiner;
import org.etlforge.application.ui.search.string.StringSearchRule;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.integration.client.ui.entity.typeselector.EntityTypeSelectionEvent;
import org.etlforge.integration.client.ui.entity.typeselector.EntityTypeSelectionListener;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionFactory;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.label.KongaLabel;

public final class EntitySearchUi implements UiProvider, Disposable {
   private final StringSearchModelDefiner stringSearchDefiner;
   private final EntitySearchService service;
   private final SearchRules searchRules;
   private final EntitySearchModel model;
   private final JComponent displayer;
   private final EntityTypePanel entityTypePanel;
   private final DropDownPanel entityTypeDropDown;
   private final ResultDisplayer resultList;
   private final EntitySearchUi.OpenSingleMatchAction openSingleMatchAction;

   public EntitySearchUi(EntitySearchModel var1) {
      this.model = var1;
      this.searchRules = new SearchRules();
      this.stringSearchDefiner = this.createSearchStringUi();
      this.entityTypePanel = this.createTypeSelector();
      this.entityTypeDropDown = this.createTypeSelectorDropdown();
      this.resultList = this.createResultList();
      this.displayer = new EntitySearchUi.Layout().create();
      this.service = new EntitySearchService(var1);
      this.openSingleMatchAction = new EntitySearchUi.OpenSingleMatchAction();
      this.stringSearchDefiner.installKeyBinding(ActionFactory.forKey(KeyUtils.DOWN, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            EntitySearchUi.this.resultList.requestFocus();
         }
      }));
      this.stringSearchDefiner.addActionListener(this.openSingleMatchAction);
      this.prepareModel();
   }

   private StringSearchModelDefiner createSearchStringUi() {
      StringSearchModelDefiner var1 = new StringSearchModelDefiner(this.searchRules.getRules());
      var1.setFieldWidth(Size.columns(30));
      var1.setSelectedRule(this.searchRules.getDefaultRule());
      var1.setCaption(EntityResources.getString("Search.Field.Label"));
      return var1;
   }

   private EntityTypePanel createTypeSelector() {
      EntityTypePanel var1 = new EntityTypePanel(this.model);
      EntitySearchUi.EntityTypePanelListener var2 = new EntitySearchUi.EntityTypePanelListener();
      var1.addSelectionListener(var2);
      var1.addActionListener(var2);
      return var1;
   }

   private DropDownPanel createTypeSelectorDropdown() {
      DropDownPanel var1 = new DropDownPanel(this.entityTypePanel.getDisplayer(), EntityResources.getString("Search.TypeDropDown.Title"), false);
      var1.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent var1) {
            Window var2 = SwingUtilities.getWindowAncestor(EntitySearchUi.this.displayer);
            if (var2 instanceof JDialog) {
               var2.pack();
            }
         }
      });
      var1.setTitleFont(TextStyles.AdminHint.getFont());
      return var1;
   }

   private ResultDisplayer createResultList() {
      ResultTable var1 = new ResultTable(this.model);
      var1.setDefaultAction(new AbstractSelectionAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            EntitySearchUi.this.highlightSearchResult();
         }
      });
      return var1;
   }

   private void prepareModel() {
      this.model.setStringSearchModel(this.stringSearchDefiner.getSearchModel());
      this.model.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            if ("search_type".equals(var1.getPropertyName())) {
               EntitySearchUi.this.stringSearchDefiner.requestFocus();
               SearchType var2 = EntitySearchUi.this.model.getSearchType();
               boolean var3 = !var2.isEntityTypeSpecific();
               EntitySearchUi.this.entityTypeDropDown.setEnabled(var3);
            }
         }
      });
   }

   public EntitySearchModel getModel() {
      return this.model;
   }

   @Override
   public void dispose() {
      this.service.dispose();
      this.searchRules.rememberSelectedRule(this.stringSearchDefiner.getSearchModel().getSearchRule());
   }

   public void highlightSearchResult() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EntitySearchUi.this.highlightSearchResultImpl();
         }
      });
   }

   private void highlightSearchResultImpl() {
      IntegrationEntity var1 = this.resultList.getSelectedEntity();
      if (var1 != null) {
         UiCompanion.of(var1).openPage();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      this.stringSearchDefiner.requestFocus();
   }

   public Object getMemento() {
      return new EntitySearchUi.Memento(
         this.model.getSearchType().getName(),
         this.model.getSearchString(),
         this.stringSearchDefiner.getSearchModel().getSearchRule(),
         this.entityTypePanel.getSelection()
      );
   }

   public void setMemento(Object var1) {
      Preconditions.checkArgument(var1 instanceof EntitySearchUi.Memento, "Invalid memento");
      EntitySearchUi.Memento var2 = (EntitySearchUi.Memento)var1;

      for (SearchType var6 : this.model.getSearchTypes()) {
         if (var6.getName().equals(var2.searchTypeName)) {
            this.model.setSearchType(var6);
            break;
         }
      }

      this.model.setSelectedEntityTypes(var2.itemTypes);
      this.stringSearchDefiner.setSearchString(var2.searchString);
      this.stringSearchDefiner.setSelectedRule(var2.searchRule);
      this.entityTypePanel.syncSelectionFromModel();
      if (!this.entityTypePanel.allSelected()) {
         this.entityTypeDropDown.expand();
      }
   }

   private final class EntityTypePanelListener implements EntityTypeSelectionListener, ActionListener {
      private EntityTypePanelListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.updateModel();
      }

      private void updateModel() {
         EntitySearchUi.this.model.setSelectedEntityTypes(EntitySearchUi.this.entityTypePanel.getSelection());
      }

      @Override
      public void selectionChanged(EntityTypeSelectionEvent var1) {
         this.updateModel();
      }
   }

   private final class Layout {
      private final TwoColumnPanel grid = new TwoColumnPanel()
         .withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL)
         .withLeftWeights(0.4, 1.0)
         .withLeftInsets(0, 0, 0, 10)
         .withRightFill(GridBagLayoutPanel.Fill.BOTH)
         .withRightWeights(0.6, 1.0)
         .withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);

      public Layout() {
         UiProvider var2 = this.layoutControls();
         UiProvider var3 = this.createResultPanel();
         this.grid.addRow(var2, var3);
      }

      private UiProvider layoutControls() {
         OneColumnPanel var1 = new OneColumnPanel().withInsets(0, 0, 10, 0).withBorder(Empty.border(4, 4, 0, 0)).stretchHorizontally();
         SearchTypeSelector var2 = new SearchTypeSelector(EntitySearchUi.this.model);
         var1.addAll(var2, EntitySearchUi.this.stringSearchDefiner);
         return Vertical.Anchor.north(var1);
      }

      private UiProvider createResultPanel() {
         JLabel var1 = this.createResultCaption();
         JComponent var2 = EntitySearchUi.this.resultList.getDisplayer();
         var1.setLabelFor(var2);
         var2.setAlignmentX(0.0F);
         return new OneColumnPanel().nonOpaque().add(var1).fillOut(var2);
      }

      private JLabel createResultCaption() {
         KongaLabel var1 = TextStyles.DefaultListText.makeLabel(EntityResources.getMnemonicString("Search.Results"));
         var1.setAlignmentX(0.0F);
         return var1;
      }

      public JComponent create() {
         return BorderLayoutBuilder.fillWith(this.grid);
      }
   }

   private static final class Memento {
      public final String searchTypeName;
      public final String searchString;
      public final StringSearchRule searchRule;
      public final ImmutableSet<EntityType> itemTypes;

      public Memento(String var1, String var2, StringSearchRule var3, ImmutableSet<EntityType> var4) {
         this.searchTypeName = var1;
         this.searchString = var2;
         this.searchRule = var3;
         this.itemTypes = var4;
      }
   }

   private class OpenSingleMatchAction implements ActionListener {
      private OpenSingleMatchAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         IntegrationEntity var2 = EntitySearchUi.this.model.getSingleMatch();
         if (var2 != null) {
            UiCompanion.of(var2).openPage();
         }
      }
   }
}
