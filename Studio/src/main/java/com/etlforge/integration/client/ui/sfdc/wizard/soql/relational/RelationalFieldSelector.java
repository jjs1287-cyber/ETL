package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;

public final class RelationalFieldSelector implements UiProvider {
   private final RelationalFieldSelector.PathBreadCrumbPanel pathBreadCrumb;
   private final FieldSelector fieldSelector;
   private final ParentRelationshipList parentSelector;
   private final ChildRelationshipList childSelector;
   private boolean includeChildRelations = true;

   public RelationalFieldSelector(RelationalFieldSelectorModel var1, Controller var2) {
      this.pathBreadCrumb = new RelationalFieldSelector.PathBreadCrumbPanel(var1);
      this.fieldSelector = new FieldSelector(var1);
      this.parentSelector = new ParentRelationshipList(var1, var2);
      this.childSelector = new ChildRelationshipList(var1, var2);
   }

   public void hideChildRelations() {
      this.includeChildRelations = false;
   }

   @Override
   public void decorate(UiCanvas var1) {
      Vertical.borderLayout(5).of(this.pathBreadCrumb, this.fieldSelector, this.layoutRelationsBoxes()).decorate(var1);
   }

   private UiProvider layoutRelationsBoxes() {
      ArrayList var1 = Lists.newArrayList(new JSeparator(), this.parentSelector);
      if (this.includeChildRelations) {
         Collections.addAll(var1, new JSeparator(), this.childSelector);
      }

      return Vertical.box().leftAligned().addAllWithSpace(8, var1);
   }

   @Override
   public void requestFocus() {
      this.pathBreadCrumb.requestFocus();
   }

   private static class PathBreadCrumbPanel extends AbstractBinding<FieldOwner> implements UiProvider {
      private final RelationalFieldSelectorModel model;
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder().withBorder(new EtchedBorder()).withBackground(Color.GRAY);
      private PathBreadCrumb crumbs;

      public PathBreadCrumbPanel(RelationalFieldSelectorModel var1) {
         super(var1, "selectedObject");
         this.model = var1;
         this.syncUi();
      }

      protected void updateUiWithValue(FieldOwner var1) {
         this.layout.removeAll();
         this.crumbs = new PathBreadCrumb(var1.getPath(), new Receiver<FieldOwner>() {
            public void handle(FieldOwner var1) {
               PathBreadCrumbPanel.this.model.setSelectedObject(var1);
            }
         });
         this.layout.north(this.crumbs);
         this.layout.refreshLayout();
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }

      @Override
      public void requestFocus() {
         if (this.crumbs != null) {
            this.crumbs.requestFocus();
         }
      }
   }
}
