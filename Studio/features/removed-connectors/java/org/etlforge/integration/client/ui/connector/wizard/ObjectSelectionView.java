package org.etlforge.integration.client.ui.connector.wizard;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.widget.SelectionGrid;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.selection.SelectFirstAction;
import org.etlforge.ui.widget.text.KongaTextField;

public final class ObjectSelectionView implements UiProvider {
   private final ObjectSelectionModel model;
   private final Action refreshListAction;
   private final SelectionGrid<ConnectorObject> table;
   private final ObjectSelectionView.FilterUi filterUi;
   private final ObjectSelectionView.NameOptionSelector nameOptionSelector;
   private final ObjectSelectionView.Renderer renderer;

   public ObjectSelectionView(ObjectSelectionModel var1, Action var2) {
      this(var1, var2, false);
   }

   public ObjectSelectionView(ObjectSelectionModel var1, Action var2, boolean var3) {
      this.model = var1;
      this.refreshListAction = var2;
      this.renderer = new ObjectSelectionView.Renderer(var1);
      this.table = this.createTable();
      this.filterUi = new ObjectSelectionView.FilterUi(var3);
      this.nameOptionSelector = new ObjectSelectionView.NameOptionSelector(var1);
      this.nameOptionSelector.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            this.repaintTableLater();
         }

         private void repaintTableLater() {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  ObjectSelectionView.this.table.repaint();
               }
            });
         }
      });
   }

   private SelectionGrid<ConnectorObject> createTable() {
      SelectionGrid var1 = SelectionGrid.emptyGrid(ConnectorObject.class, 3);
      var1.setRenderer(this.renderer);
      var1.bindWithModel(this.model.getObjectsProperty(), this.model.getSelectedObjectProperty());
      return var1;
   }

   public ObjectSelectionModel getModel() {
      return this.model;
   }

   public void setDefaultAction(SelectionAction var1) {
      this.table.setDefaultAction(var1);
   }

   public void setToolTipProducer(Function<ConnectorObject, String> var1) {
      this.renderer.tooltipProducer = Preconditions.checkNotNull(var1, "p");
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = new NColumnPanel(3)
         .setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE)
         .setXWeights(0.0, 1.0, 0.0)
         .setInsets(2, new Insets(0, 10, 0, 0));
      this.filterUi.layoutInThreeColumns(var2);
      this.nameOptionSelector.layoutInThreeColumns(var2);
      KongaButton var3 = new KongaButton(this.refreshListAction);
      WizardStyleSheet.CAPTION_STYLE.makeOver(var3);
      BorderLayoutBuilder var4 = Horizontal.Align.east(var3);
      JPanel var5 = Vertical.borderLayout(5).of(var2, this.table, var4).container();
      ActionKeyBinding.install(this.refreshListAction, var5, 1);
      var1.display(var5);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private class FilterUi {
      private final KongaTextField filterField = new KongaTextField(20);
      private final ObjectTypeFilterSelector typeSelector = ObjectTypeFilterSelector.create(ObjectSelectionView.this.model);

      public FilterUi(boolean var2) {
         this.filterField.setSelectAllWhenFocused(true);
         this.filterField.setLabel("&Filter:");
         SelectFirstAction.installInField(ObjectSelectionView.this.table, this.filterField);
         TextBinding.bindAndSyncUi(ObjectSelectionView.this.model, "filter", this.filterField);
      }

      public void layoutInThreeColumns(NColumnPanel var1) {
         assert var1.getColumnCount() == 3;
         WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(this.filterField.getLabel());
         var1.addRow(this.filterField.getLabel(), this.filterField, this.typeSelector);
      }
   }

   private static class NameOptionSelector {
      private final Optional<RadioButtonGroupPanel<NameOption>> selector;

      public NameOptionSelector(ObjectSelectionModel var1) {
         ImmutableList var2 = var1.getNameOptions();
         if (var2.size() > 1) {
            RadioButtonGroupPanel var3 = this.createButtons(var1, var2);
            this.selector = Optional.of(var3);
         } else {
            this.selector = Optional.absent();
         }
      }

      private RadioButtonGroupPanel<NameOption> createButtons(ObjectSelectionModel var1, ImmutableList<? extends NameOption> var2) {
         RadioButtonGroupPanel var3 = new RadioButtonGroupPanel();

         for (NameOption var5 : var2) {
            var3.addOption(var5, var5.getLabel());
         }

         Binding var6 = var3.bindWith(var1.getProperty("name_option"));
         var6.syncUi();
         return var3;
      }

      public void layoutInThreeColumns(NColumnPanel var1) {
         assert var1.getColumnCount() == 3;
         if (this.selector.isPresent()) {
            KongaLabel var2 = WizardStyleSheet.SMALL_CAPTION_STYLE.makeLabel("Show:");
            var1.addRow(var2, this.selector.get().oneRowLayout(), null);
         }
      }

      public void addActionListener(ActionListener var1) {
         if (this.selector.isPresent()) {
            this.selector.get().addActionListener(var1);
         }
      }
   }

   private static class Renderer extends DefaultTableCellRenderer {
      private NameOption nameOption;
      public Function<ConnectorObject, String> tooltipProducer = new Function<ConnectorObject, String>() {
         public String apply(ConnectorObject var1) {
            return var1.getDescription();
         }
      };
      private ConnectorObject object;

      public Renderer(ObjectSelectionModel var1) {
         AbstractBinding var2 = new AbstractBinding(var1, "name_option") {
            @Override
            protected void updateUiWithValue(Object var1) {
               Renderer.this.nameOption = (NameOption)var1;
            }
         };
         var2.syncUi();
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         if (var2 instanceof ConnectorObject) {
            this.object = (ConnectorObject)var2;
            String var7 = this.nameOption.apply(this.object);
            this.setText(var7);
         }

         return this;
      }

      @Override
      public String getToolTipText() {
         return this.object == null ? null : this.tooltipProducer.apply(this.object);
      }
   }
}
