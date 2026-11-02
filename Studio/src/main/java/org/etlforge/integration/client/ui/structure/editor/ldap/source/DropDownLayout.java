package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.Empty;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.DropDownPanel;

class DropDownLayout implements DefinerListLayout {
   private final JComponent container;
   private final Object constraints;

   public DropDownLayout(JComponent var1, Object var2) {
      this.container = var1;
      this.constraints = var2;
   }

   @Override
   public void layoutParameterDefiners(Collection<SearchParametersDefiner> var1) {
      OneColumnPanel var2 = this.createNewLayout(var1);
      var2.addToParent(this.container, this.constraints);
      this.revalidateParent();
   }

   private OneColumnPanel createNewLayout(Collection<SearchParametersDefiner> var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(0, 0, 20, 0);
      var2.setWeightX(1.0);

      for (SearchParametersDefiner var4 : var1) {
         DropDownPanel var5 = this.createDropDown(var4, var1.size() == 1);
         var2.add(var5.getDisplayer());
      }

      return var2.deeplyNonOpaque().withBorder(Empty.border(10, 0, 0, 0));
   }

   private void revalidateParent() {
      JComponent var1 = (JComponent)this.container.getParent();
      var1.revalidate();
      var1.repaint();
   }

   public boolean requiresRevalidationOfParent() {
      return true;
   }

   private DropDownPanel createDropDown(final SearchParametersDefiner var1, boolean var2) {
      final DropDownPanel var3 = new DropDownPanel(ComponentCanvasFactory.northBorder(var1).getUi(), var1.getClassName(), var2);
      var3.decorateTitle(TextStyles.MediumCaption);
      var3.setTitleToolTip(PackageResources.SearchParamsDefiner.getDropDownToopTip(var1.getClassName()));
      var3.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent var1x) {
            if (var3.isShowing()) {
               var1.requestFocus();
            }
         }
      });
      return var3;
   }
}
