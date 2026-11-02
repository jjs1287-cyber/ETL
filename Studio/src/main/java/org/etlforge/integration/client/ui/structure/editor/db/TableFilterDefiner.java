package org.etlforge.integration.client.ui.structure.editor.db;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import org.etlforge.application.ui.search.string.DefaultStringSearchRule;
import org.etlforge.application.ui.search.string.StringSearchModel;
import org.etlforge.application.ui.search.string.StringSearchModelDefiner;
import org.etlforge.application.ui.search.string.StringSearchModelListener;
import org.etlforge.application.ui.search.string.StringSearchRule;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.DatabaseTable;
import org.etlforge.integration.database.info.DatabaseView;
import org.etlforge.integration.database.info.SystemTable;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.string.StringMatcher;
import org.etlforge.string.WildcardStringMatcher;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class TableFilterDefiner implements UiProvider {
   private final TableSelectionModel model;
   private final StringSearchModelDefiner nameSearchUi;
   private final TableFilterDefiner.TypeSelector tableSelector = new TableFilterDefiner.TypeSelector("Tables", DatabaseTable.class);
   private final TableFilterDefiner.TypeSelector viewSelector = new TableFilterDefiner.TypeSelector("Views", DatabaseView.class);
   private final TableFilterDefiner.TypeSelector systemTableSelector = new TableFilterDefiner.TypeSelector("System Tables", SystemTable.class);
   private final KongaLabel label = KongaLabel.withMnemonic("&Filter search (optional):");
   private ActionListener fieldlistener;
   private Receiver<ListFilter> listFilterReceiver = NoReceiver.create();

   public TableFilterDefiner(TableSelectionModel var1) {
      this.model = var1;
      this.systemTableSelector.setSelected(false);
      this.nameSearchUi = this.createNameSearchUi();
      ButtonUtils.addActionListener(new TableFilterDefiner.CheckHandler(), this.tableSelector, this.viewSelector, this.systemTableSelector);
   }

   private StringSearchModelDefiner createNameSearchUi() {
      DefaultStringSearchRule var1 = new DefaultStringSearchRule(new TableFilterDefiner.ListMatcher(), "");
      StringSearchModelDefiner var2 = new StringSearchModelDefiner(var1);
      var2.setCaption("");
      var2.setTextWhenEmpty("Name filter (% = any string)");
      var2.setExternalLabel(this.label);
      var2.setEmptyStringMatchesEverything(true);
      var2.setToolTip("For example: order% to download only those tables/views that start with \"order\"");
      var2.getSearchModel().addModelListener(new StringSearchModelListener() {
         @Override
         public void searchRuleChanged(StringSearchModel var1, StringSearchRule var2x) {
            TableFilterDefiner.this.applyNewFilter();
         }

         @Override
         public void searchStringChanged(StringSearchModel var1, String var2x) {
            TableFilterDefiner.this.applyNewFilter();
         }
      });
      return var2;
   }

   public void setListFilterReceiver(Receiver<ListFilter> var1) {
      this.listFilterReceiver = var1;
   }

   public void setFieldListener(ActionListener var1) {
      if (this.fieldlistener != null) {
         this.nameSearchUi.removeActionListener(this.fieldlistener);
      }

      this.fieldlistener = var1;
      if (var1 != null) {
         this.nameSearchUi.addActionListener(var1);
      }
   }

   public void setEnabled(boolean var1) {
      this.nameSearchUi.setEnabled(var1);
      this.tableSelector.setEnabled(var1);
      this.viewSelector.setEnabled(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(10, this.label, this.nameSearchUi, this.tableSelector, this.viewSelector, this.systemTableSelector);
      var1.display(InvisiblePanel.of(var2));
   }

   @Override
   public void requestFocus() {
      this.nameSearchUi.requestFocus();
   }

   private void applyNewFilter() {
      this.model.setSearchFilter(this.getDefinedFilter());
      TableFilterDefiner.ListFilterImpl var1 = new TableFilterDefiner.ListFilterImpl();
      this.listFilterReceiver.handle(var1);
   }

   private TableSearchFilter getDefinedFilter() {
      EnumSet var1 = EnumSet.noneOf(DatabaseObjectType.class);
      if (this.tableSelector.isSelected()) {
         var1.add(DatabaseObjectType.TABLE);
      }

      if (this.viewSelector.isSelected()) {
         var1.add(DatabaseObjectType.VIEW);
      }

      if (this.systemTableSelector.isSelected()) {
         var1.add(DatabaseObjectType.SYSTEM_TABLE);
      }

      String var2 = this.getSqlSearchString();
      return new TableSearchFilter(var2, var1);
   }

   private String getSqlSearchString() {
      String var1 = this.nameSearchUi.getSearchModel().getSearchString().trim();
      var1 = var1.replace('*', '%');
      if (!var1.contains("%") && !var1.contains("?")) {
         var1 = var1 + "%";
      }

      return var1;
   }

   private class CheckHandler implements ActionListener {
      private CheckHandler() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TableFilterDefiner.this.applyNewFilter();
      }
   }

   private class ListFilterImpl implements ListFilter {
      private ListFilterImpl() {
      }

      @Override
      public boolean test(Object var1) {
         return var1 instanceof DatabaseObject ? this.test((DatabaseObject)var1) : true;
      }

      private boolean test(DatabaseObject var1) {
         return TableFilterDefiner.this.tableSelector.test(var1)
            && TableFilterDefiner.this.viewSelector.test(var1)
            && TableFilterDefiner.this.systemTableSelector.test(var1)
            && TableFilterDefiner.this.nameSearchUi.getSearchModel().test(var1.getName());
      }
   }

   private static final class ListMatcher implements StringMatcher {
      private final StringMatcher worker = new WildcardStringMatcher(false);

      private ListMatcher() {
      }

      @Override
      public boolean matches(String var1, String var2) {
         String var3 = var1.replace('%', '*');
         return this.worker.matches(var3, var2);
      }
   }

   private static class TypeSelector extends KongaCheckBox {
      private final Class<? extends DatabaseObject> typeClass;

      TypeSelector(String var1, Class<? extends DatabaseObject> var2) {
         super(var1, true);
         this.typeClass = var2;
      }

      public boolean test(DatabaseObject var1) {
         return this.typeClass.isInstance(var1) ? this.isSelected() : true;
      }
   }
}
