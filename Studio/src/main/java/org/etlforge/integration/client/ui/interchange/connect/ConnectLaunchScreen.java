package org.etlforge.integration.client.ui.interchange.connect;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.ui.interchange.TopLevelEntityCategory;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.icons.ArrowRightIcon;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.util.preference.KongaPreferences;

public final class ConnectLaunchScreen implements UiProvider {
   private final ImmutableList<TopLevelEntityCategory.ButtonGrid> sections;
   private final String title;
   private final Icon icon;

   public ConnectLaunchScreen(final EntityConfigLookup var1, Iterable<TopLevelEntityCategory> var2, String var3, Icon var4) {
      this.sections = FluentIterable.<TopLevelEntityCategory>from(var2).transform(new Function<TopLevelEntityCategory, TopLevelEntityCategory.ButtonGrid>() {
         public TopLevelEntityCategory.ButtonGrid apply(TopLevelEntityCategory var1x) {
            return var1x.toButtonGrid(var1);
         }
      }).toImmutableList();
      this.title = var3;
      this.icon = var4;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.withBackground(Colors.NEUTRAL_BACKGROUND);
      JComponent var3 = this.layoutButtons();
      var2.north(this.getCaption()).center(var3);
      var2.decorate(var1);
   }

   protected JComponent layoutButtons() {
      if (this.sections.size() <= 2) {
         ConnectLaunchScreen.FewCategoriesLayout var2 = new ConnectLaunchScreen.FewCategoriesLayout();
         return var2.get();
      } else {
         ConnectLaunchScreen.ManyCategoriesLayout var1 = new ConnectLaunchScreen.ManyCategoriesLayout();
         return var1.get();
      }
   }

   private JComponent getCaption() {
      KongaLabel var1 = WizardStyleSheet.SECTION_STYLE.makeLabel(this.title);
      var1.setForeground(Color.WHITE);
      var1.setIcon(this.icon);
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.withBackground(Colors.ETLFORGE_ORANGE);
      var2.north(var1);
      var2.withBorder(Empty.border(8));
      return var2.container();
   }

   @Override
   public void requestFocus() {
      this.sections.get(0).requestFocus();
   }

   public void addActionListener(ActionListener var1) {
      for (TopLevelEntityCategory.ButtonGrid var3 : this.sections) {
         var3.addActionListener(var1);
      }
   }

   private class FewCategoriesLayout {
      private FewCategoriesLayout() {
      }

      public JComponent get() {
         Grid var1 = Grid.oneRow(20).withBackground(Colors.NEUTRAL_BACKGROUND).withBorder(Empty.border(4, 10, 8, 10)).addAll(ConnectLaunchScreen.this.sections);
         BoxBuilder var2 = BoxBuilder.horizontal().nonOpaque().glue().add(var1).glue();
         return new BorderLayoutBuilder().north(var2).nonOpaque().container();
      }
   }

   private class ManyCategoriesLayout {
      private final NColumnPanel layout = new NColumnPanel(3)
         .setFills(GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.BOTH)
         .setXWeights(0.5, 0.0, 0.5)
         .setYWeights(1.0, 0.0, 1.0)
         .setAnchors(GridBagLayoutPanel.Anchor.WEST, GridBagLayoutPanel.Anchor.CENTER, GridBagLayoutPanel.Anchor.EAST)
         .setInsets(0, new Insets(0, 0, 0, 32))
         .setInsets(1, new Insets(0, 0, 0, 32))
         .withBackground(Colors.NEUTRAL_BACKGROUND)
         .withBorder(Empty.border(4, 10, 8, 10));
      private final CardPanel cards = new CardPanel();
      private final String lastSelectionKey = "lastSelection";

      public ManyCategoriesLayout() {
         this.createCards();
         JComponent var2 = this.layoutSelectors();
         this.layout.addRow(var2, new JLabel(new ArrowRightIcon(24, 32, Color.DARK_GRAY)), this.cards);
      }

      private void createCards() {
         for (TopLevelEntityCategory.ButtonGrid var2 : ConnectLaunchScreen.this.sections) {
            String var3 = var2.getName();
            OneColumnPanel var4 = new OneColumnPanel().withBackground(Colors.NEUTRAL_BACKGROUND);
            var4.setWeightX(1.0);
            var4.setWeightY(0.5);
            var4.setFill(GridBagLayoutPanel.Fill.BOTH);
            var4.add(" ");
            var4.setWeightY(0.0);
            var4.setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
            var4.add(var2);
            var4.setWeightY(0.5);
            var4.setFill(GridBagLayoutPanel.Fill.BOTH);
            var4.add(" ");
            this.cards.add(var4, var3);
         }
      }

      private JComponent layoutSelectors() {
         String var1 = KongaPreferences.getPreferences(ConnectLaunchScreen.class)
            .getString("lastSelection", ConnectLaunchScreen.this.sections.get(0).getName());
         JToggleButton var2 = null;
         ArrayList var3 = new ArrayList();

         for (TopLevelEntityCategory.ButtonGrid var5 : ConnectLaunchScreen.this.sections) {
            String var6 = var5.getName();
            JToggleButton var7 = new JToggleButton(var6, var5.getIcon());
            var7.setHorizontalAlignment(10);
            WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var7);
            var3.add(var7);
            if (var6.equals(var1)) {
               var2 = var7;
            }
         }

         ButtonUtils.addActionListener(new ConnectLaunchScreen.ManyCategoriesLayout.CardListener(), var3);
         ButtonUtils.makeMutuallyExclusive(var3);
         if (var2 == null) {
            var2 = (JToggleButton)var3.get(0);
         }

         var2.doClick();
         KongaLabel var8 = WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeLabel("Select Connector:");
         Grid var9 = Grid.oneColumn(16).of(var3).withBorder(new TitledBorder(""));
         return CaptionLayout.of(var8, var9).deeplyNonOpaque().container();
      }

      public JComponent get() {
         return this.layout.container();
      }

      private class CardListener implements ActionListener {
         private CardListener() {
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            JToggleButton var2 = (JToggleButton)var1.getSource();
            String var3 = var2.getText();
            ManyCategoriesLayout.this.cards.show(var3);
            KongaPreferences.getPreferences(ConnectLaunchScreen.class).putString("lastSelection", var3);
         }
      }
   }
}
