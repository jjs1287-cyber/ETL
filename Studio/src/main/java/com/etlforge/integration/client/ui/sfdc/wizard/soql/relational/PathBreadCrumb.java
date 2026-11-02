package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;

final class PathBreadCrumb implements UiProvider {
   private final ImmutableList<KongaButton> buttons;
   private final Receiver<FieldOwner> selectionHandler;
   private final Icon separator = ApplicationResources.getIcon("ArrowRight.16");

   public PathBreadCrumb(RelationshipPath var1, Receiver<FieldOwner> var2) {
      ArgCheck.checkPositive(var1.size(), "path cannot be empty");
      this.selectionHandler = var2;
      this.buttons = ImmutableList.copyOf(Iterables.transform(var1.toList(), new Function<FieldOwner, KongaButton>() {
         public KongaButton apply(FieldOwner var1) {
            return PathBreadCrumb.this.createButton(var1);
         }
      }));
   }

   private KongaButton createButton(final FieldOwner var1) {
      KongaButton var2 = new KongaButton(new AbstractAction("<html><u><b>" + var1.getName() + "</b></u></html>") {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            PathBreadCrumb.this.selectionHandler.handle(var1);
         }
      });
      return ButtonUtils.configureButtonForToolBar(var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = Horizontal.box();
      boolean var3 = true;

      for (KongaButton var5 : this.buttons) {
         if (!var3) {
            var2.add(this.separator);
         }

         var2.add(var5);
         var3 = false;
      }

      var1.display(var2.nonOpaque().invisible());
   }

   @Override
   public void requestFocus() {
      this.buttons.get(0).requestFocusInWindow();
   }
}
