package com.etlforge.integration.client.ui.sfdc.action;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.AbstractActionBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.AbstractPopupMenuItemFactory;
import org.etlforge.application.ui.action.popup.PopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorApis;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;

public class RunSfdcWizardItemFactory<T extends IntegrationEntity & SalesforceWizardEntity> extends AbstractPopupMenuItemFactory {
   private final String actionId;
   private final String name;
   private final Class<T> wizardClass;
   private final ApplicationWindow appWin;
   private final OperationRunnerFactory runnerFactory;
   private Optional<OperationMonitorApis> monitorApis;
   private Optional<Function<T, String>> nameFunction = Optional.absent();

   public RunSfdcWizardItemFactory(String var1, String var2, ApplicationWindow var3, Class<T> var4, OperationRunnerFactory var5) {
      this.actionId = var1;
      this.name = var2;
      this.appWin = var3;
      this.runnerFactory = var5;
      this.wizardClass = var4;
   }

   public final void setNameFunction(Function<T, String> var1) {
      this.nameFunction = Optional.of(var1);
   }

   public final void setMonitorApis(OperationMonitorApis var1) {
      this.monitorApis = Optional.of(var1);
   }

   @Override
   protected final PopupMenuItem createItem() {
      RunSfdcWizardAction var1 = new RunSfdcWizardAction<>(this.actionId, this.name, this.appWin, this.wizardClass, this.runnerFactory);
      if (this.monitorApis.isPresent()) {
         var1.setMonitorApis(this.monitorApis.get());
      }

      return new RunSfdcWizardItemFactory.Item<>(var1, this.wizardClass, this.nameFunction);
   }

   private static class Item<E extends IntegrationEntity & SalesforceWizardEntity> extends AbstractActionBasedPopupMenuItem {
      private final Class<E> wizardClass;
      private final Optional<Function<E, String>> nameFunction;

      public Item(Action var1, Class<E> var2, Optional<Function<E, String>> var3) {
         super(var1);
         this.wizardClass = var2;
         this.nameFunction = var3;
      }

      @Override
      public void updateState(PopupMenuSite var1, List<?> var2) {
         super.updateState(var1, var2);
         List var3 = KongaListUtils.filter(var2, Predicates.instanceOf(this.wizardClass));
         if (var3.size() == 1) {
            this.setVisible(true);
            this.setEnabled(this.getAction().isEnabled());
            this.updateName(this.wizardClass.cast(var3.get(0)));
         } else {
            this.setVisible(false);
         }
      }

      private void updateName(E var1) {
         if (this.nameFunction.isPresent()) {
            String var2 = this.nameFunction.get().apply((E)var1);
            this.setName(var2);
         }
      }
   }
}
