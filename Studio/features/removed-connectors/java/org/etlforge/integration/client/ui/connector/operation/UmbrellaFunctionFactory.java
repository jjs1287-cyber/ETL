package org.etlforge.integration.client.ui.connector.operation;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.entity.factory.Request;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.RadioButtonGroupPanel;

public final class UmbrellaFunctionFactory implements UiEntityFactory {
   private final EntityType umbrellaType;
   private final UiEntityFactory worker;

   public UmbrellaFunctionFactory(EntityType var1, UiEntityFactory var2) {
      this.umbrellaType = Preconditions.checkNotNull(var1, "umbrellaType");
      Preconditions.checkArgument(var1.isUmbrellaType(), var1.name() + " is not an umbrella type");
      this.worker = Preconditions.checkNotNull(var2, "worker");
   }

   @Override
   public void createEntity(EntityType var1, UiEntityFactoryCallback var2) {
      this.createEntity(Request.defaultRequest(var1), var2);
   }

   @Override
   public void createEntity(Request var1, UiEntityFactoryCallback var2) {
      assert var1.getEntityType() == this.umbrellaType;
      EntityType var3 = this.selectType();
      if (var3 != null) {
         Request var4 = var1.forType(var3);
         this.worker.createEntity(var4, var2);
      }
   }

   private EntityType selectType() {
      return UiThreadSupplier.call(new UmbrellaFunctionFactory.TypeSelector(this.umbrellaType.getEncompassedTypes()));
   }

   private static class TypeSelector implements Supplier<EntityType> {
      private final ImmutableSet<EntityType> types;

      public TypeSelector(ImmutableSet<EntityType> var1) {
         this.types = var1;
      }

      public EntityType get() {
         RadioButtonGroupPanel var1 = new RadioButtonGroupPanel();
         UiProvider var2 = this.createUi(var1);
         WizardStyleDialog var3 = this.createDialog(var2);
         var3.setVisible(true);
         return var3.wasOkPressed() ? (EntityType)var1.getSelection() : null;
      }

      private UiProvider createUi(RadioButtonGroupPanel<EntityType> var1) {
         boolean var2 = true;

         for (EntityType var4 : this.types) {
            var1.addOption(var4, var4.getDisplayName(), var2);
            var2 = false;
         }

         return var1.getUi(null, RadioButtonGroupPanel.Layout.ONE_COLUMN);
      }

      private WizardStyleDialog createDialog(UiProvider var1) {
         JFrame var2 = ApplicationUi.getAppWinFrame();
         JComponent var3 = WizardStyleDialog.wrapUi(var1);
         WizardStyleDialog var4 = new WizardStyleDialog(var2, "Select Function Type", var3, KongaDialog.CONTINUE_CANCEL, true);
         var4.setFocusedComponent(var1);
         var4.setResizable(false);
         var4.manageLocation(this.getClass());
         var4.setContinueButtonAsDefault();
         return var4;
      }
   }
}
