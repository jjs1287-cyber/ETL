package com.etlforge.integration.client.ui.sfdc.bulkload;

import com.google.common.collect.Maps;
import com.etlforge.integration.client.ui.sfdc.bulkload.wizard.BulkLoadWizard;
import java.util.EnumMap;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.Orientation;
import org.etlforge.ui.util.option.ExclusiveOptionsPanel;
import org.etlforge.ui.util.option.Option;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.ui.util.option.SelectedOptionListener;
import org.etlforge.util.preference.KongaPreferences;

public final class ActionSelector implements UiProvider {
   private static final String DEFAULT_OPTION = "DefaultOperation";
   private final KongaPreferences prefs = KongaPreferences.getPreferences(BulkLoadWizard.class);
   private final ExclusiveOptionsPanel<ActionSelector.ActionOption> panel;
   private final Receiver<BulkAction> listener;

   public ActionSelector() {
      this(NoReceiver.create());
   }

   public ActionSelector(Receiver<BulkAction> var1) {
      this.listener = var1;
      this.panel = this.createPanel();
      this.listenToSelections();
   }

   private ExclusiveOptionsPanel<ActionSelector.ActionOption> createPanel() {
      EnumMap var1 = Maps.newEnumMap(BulkAction.class);

      for (BulkAction var5 : BulkAction.values()) {
         var1.put(var5, new ActionSelector.ActionOption(var5));
      }

      BulkAction var6 = this.prefs.getEnum("DefaultOperation", BulkAction.class, BulkAction.DEFAULT);
      ActionSelector.ActionOption var7 = (ActionSelector.ActionOption)var1.get(var6);
      this.listener.handle(var6);
      ActionSelector.ActionOption[] var8 = (ActionSelector.ActionOption[]) var1.values().toArray(new ActionSelector.ActionOption[var1.size()]);
      return new ExclusiveOptionsPanel<>(var8, var7, Orientation.HORIZONTAL);
   }

   private void listenToSelections() {
      this.panel.addSelectionListener(new SelectedOptionListener() {
         @Override
         public void selectionChanged(Option[] var1) {
            if (var1.length > 0) {
               BulkAction var2 = ((ActionSelector.ActionOption)var1[0]).action;
               ActionSelector.this.listener.handle(var2);
               ActionSelector.this.prefs.putEnum("DefaultOperation", var2);
            }
         }
      });
   }

   public void setAction(BulkAction var1) {
      for (ActionSelector.ActionOption var5 : this.panel.getAllOptions()) {
         if (var5.action == var1) {
            this.panel.setSelectedOption(var5);
            break;
         }
      }
   }

   public BulkAction getAction() {
      return this.panel.getSelectedOption().action;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.panel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.panel.requestFocus();
   }

   private static class ActionOption implements Option {
      public final BulkAction action;

      public ActionOption(BulkAction var1) {
         this.action = var1;
      }

      @Override
      public void decorate(OptionSelector var1) {
         switch (this.action) {
            case UPSERT:
               var1.setText("Upsert");
               break;
            case UPDATE:
               var1.setText("Update");
               break;
            case CREATE:
               var1.setText("Create");
               break;
            default:
               throw new RuntimeException("Unexpected operation: " + this.action);
         }
      }
   }
}
