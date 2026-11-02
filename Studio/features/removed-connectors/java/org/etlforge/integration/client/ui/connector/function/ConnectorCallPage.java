package org.etlforge.integration.client.ui.connector.function;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.connector.ConnectorCallPanel;
import org.etlforge.integration.client.ui.connector.ConnectorCallStructuresPanel;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.MappingStructureProvider;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.ui.UiProvider;

@ParametersAreNonnullByDefault
public final class ConnectorCallPage<F extends AbstractConnectorCall<E, ?>, E extends ConnectorEndpoint> extends IntegrationEntityPage {
   private final MappingStructureProvider mappingStructureProvider;
   private final TreeWidget<E> endpointSelector;
   private final ConnectorCallStructuresPanel structureTrees;
   private Optional<UiProvider> controlPanel = Optional.absent();
   private ImmutableList<? extends FunctionProperty> properties = ImmutableList.of();

   public ConnectorCallPage(F var1, TreeWidget<E> var2, ConnectorCallStructuresPanel var3, MappingStructureProvider var4) {
      super(new IntegrationEntityPageModel(var1));
      this.endpointSelector = var2;
      this.structureTrees = var3;
      this.mappingStructureProvider = var4;
   }

   public F getObject() {
      return (F)super.getObject();
   }

   @Override
   protected IntegrationEntityPanel createDataPanel() {
      ConnectorCallPanel var1 = new ConnectorCallPanel<>(this.getWindow(), this.getObject(), this.endpointSelector, this.structureTrees, this.properties);
      if (this.controlPanel.isPresent()) {
         var1.setControlPanel(this.controlPanel.get());
      }

      return var1;
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.mappingStructureProvider.loadStructures(this.getObject(), new ConnectorCallPage.DisplayStructuresCallback());
   }

   @Override
   protected void prepareForRemoval(ApplicationWindow var1) {
      super.prepareForRemoval(var1);
      this.endpointSelector.dispose();
   }

   public void refreshMappingStructures() {
      this.mappingStructureProvider.refreshStructures(this.getObject(), new ConnectorCallPage.DisplayStructuresCallback());
   }

   public void installControlPanel(UiProvider var1) {
      this.controlPanel = Optional.of(var1);
   }

   public void setFunctionProperties(List<? extends FunctionProperty> var1) {
      for (FunctionProperty var3 : var1) {
         var3.associateWithPage(this);
      }

      this.properties = ImmutableList.copyOf(var1);
   }

   public Supplier<E> getEndpointSupplier() {
      return new Supplier<E>() {
         public E get() {
            return ConnectorCallPage.this.endpointSelector.getSelectedEntity();
         }
      };
   }

   private final class DisplayStructuresCallback implements MappingStructureProvider.Callback {
      private DisplayStructuresCallback() {
      }

      @Override
      public void gotStructures(@Nullable final MappingTreeStructure var1, final MappingTreeStructure var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ConnectorCallPage.this.structureTrees.displayStructures(var1, var2);
            }
         });
      }
   }
}
