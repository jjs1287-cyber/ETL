package org.etlforge.integration.client.ui.script.builder.dataelement;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.TitlePanelFactory;
import org.etlforge.integration.client.ui.de.global.BrowserFilterField;
import org.etlforge.integration.client.ui.de.global.GlobalDataElementsBrowser;
import org.etlforge.integration.client.ui.script.builder.layout.LayoutType;
import org.etlforge.integration.client.ui.script.prefs.LayoutTypePreference;
import org.etlforge.integration.client.ui.script.prefs.ShowReferencedDataElementsOnlyPreference;
import org.etlforge.integration.client.ui.script.resources.ScriptResources;
import org.etlforge.string.WildcardStringMatcher;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.PopupMenuButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class DataElementSelectorLayout {
   private final GlobalDataElementsBrowser browser;
   private final Action transferAction;
   private boolean allowBuiltInVariablesToBeHidden = true;

   public DataElementSelectorLayout(GlobalDataElementsBrowser var1, Action var2) {
      this.browser = var1;
      this.transferAction = var2;
   }

   public void setAllowBuiltInVariablesToBeHidden(boolean var1) {
      this.allowBuiltInVariablesToBeHidden = var1;
   }

   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.layoutTop()).center(this.browser);
      var2.decorate(var1);
   }

   private UiProvider layoutTop() {
      UiProvider var1 = this.createTitlePanel();
      if (isTabBased()) {
         return var1;
      }

      JComponent var2 = this.createFilterField(Empty.border(0, 2, 0, 2));
      return new OneColumnPanel().setWeightX(1.0).of(var1, var2);
   }

   private UiProvider createTitlePanel() {
      TitlePanelFactory var1 = new TitlePanelFactory() {
         @Override
         protected JButton[] getToolBarButtons() {
            ArrayList var1x = Lists.newArrayList();
            var1x.add(new KongaButton(DataElementSelectorLayout.this.transferAction).makeToolbarButton());
            if (DataElementSelectorLayout.this.allowBuiltInVariablesToBeHidden) {
               IncludeBuiltInDataElementsToggle var2 = new IncludeBuiltInDataElementsToggle(
                  DataElementSelectorLayout.this.browser, ShowReferencedDataElementsOnlyPreference.INSTANCE
               );
               PopupMenuButton var3 = new PopupMenuButton(ApplicationResources.ICONS.getIcon("ArrowDown.16"), new Action[]{var2});
               var1x.add(var3);
            }

            return (JButton[]) var1x.toArray(new JButton[var1x.size()]);
         }

         @Override
         protected JComponent[] getAdditionalToolbarComponents() {
            return DataElementSelectorLayout.isTabBased()
               ? new JComponent[]{InvisiblePanel.of(DataElementSelectorLayout.this.createFilterField(null))}
               : new JComponent[0];
         }
      };
      if (!isTabBased()) {
         var1.setTitle(ScriptResources.getString("DataElementSelector.Title"));
      }

      return var1.createPanel();
   }

   private static boolean isTabBased() {
      return LayoutTypePreference.get() == LayoutType.TABS;
   }

   private JComponent createFilterField(Border var1) {
      WildcardStringMatcher var2 = new WildcardStringMatcher(false);
      String var3 = "* = any string, ? = any character";
      BrowserFilterField var4 = new BrowserFilterField(this.browser, var2, var3);
      JComponent var5 = var4.getUi();
      var5.setBorder(var1);
      return var5;
   }
}
