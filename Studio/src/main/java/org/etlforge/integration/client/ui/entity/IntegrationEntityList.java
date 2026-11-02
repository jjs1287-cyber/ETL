package org.etlforge.integration.client.ui.entity;

import java.awt.Insets;
import java.util.Collection;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityListDecorRenderer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

public class IntegrationEntityList implements UiProvider {
   private final KongaList list;

   public IntegrationEntityList(Collection<? extends IntegrationEntity> var1) {
      this(var1, 10);
   }

   public IntegrationEntityList(Collection<? extends IntegrationEntity> var1, int var2) {
      this.list = this.createList(var1, var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   private KongaList createList(Collection<? extends IntegrationEntity> var1, int var2) {
      KongaListFactory var3 = ComponentFactories.newKongaListFactory();
      var3.setVisibleRowCount(var2);
      var3.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      IntegrationEntityListDecorRenderer var4 = new IntegrationEntityListDecorRenderer();
      var4.setInsets(new Insets(2, 0, 2, 0));
      var3.setCellRenderer(var4);
      return var3.newList(var1);
   }
}
