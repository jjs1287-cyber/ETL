package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.Window;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Flow;
import org.etlforge.ui.widget.label.KongaLabel;

public final class RouteTypeSelector {
   private final Window owner;

   public RouteTypeSelector(Window var1) {
      this.owner = var1;
   }

   public RouteType select(Operation var1, IntegrationEntity var2) {
      RouteTypeSelector.Ui var3 = new RouteTypeSelector.Ui(var1, var2);
      KongaDialog var4 = this.createDialog(var3);
      this.listen(var3, var4);
      var4.setVisible(true);
      return var4.wasOkPressed() ? var3.getSelection() : null;
   }

   private KongaDialog createDialog(RouteTypeSelector.Ui var1) {
      WizardStyleDialog var2 = new WizardStyleDialog(this.owner, Strings.get("RouteTypeSelector.Title"), var1);
      var2.manageLocation(RouteTypeSelector.class);
      var2.setResizable(false);
      var2.setOkButtonAsDefault();
      return var2;
   }

   private void listen(final RouteTypeSelector.Ui var1, final KongaDialog var2) {
      var1.addChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1x) {
            RouteTypeSelector.this.updateOkButton(var1, var2);
         }
      });
      this.updateOkButton(var1, var2);
   }

   private void updateOkButton(RouteTypeSelector.Ui var1, KongaDialog var2) {
      var2.getOKButton().setEnabled(var1.getSelection() != null);
   }

   private static class Ui implements UiProvider {
      private final InputPanel panel = new InputPanel();
      private final RadioButtonGroupPanel<RouteType> buttons = this.createButtons();
      private final UiProvider layout;

      public Ui(Operation var1, IntegrationEntity var2) {
         this.layout = this.createLayout(var1, var2);
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }

      @Override
      public void requestFocus() {
      }

      public RouteType getSelection() {
         return this.buttons.getSelection();
      }

      public void addChangeListener(InputChangeListener var1) {
         this.panel.addInputChangeListener(var1);
      }

      private RadioButtonGroupPanel<RouteType> createButtons() {
         RadioButtonGroupPanel var1 = new RadioButtonGroupPanel<RouteType>(this.panel) {
            protected JComponent getButtonLayout(JRadioButton var1, RouteType var2) {
               return Flow.invisible().add(var1, Ui.this.createLabelForButton(var1, var2)).container();
            }
         };
         var1.addOption(RouteType.SUCCESS, "", true);
         var1.addOption(RouteType.FAILURE, "", false);
         return var1;
      }

      private UiProvider createLayout(Operation var1, IntegrationEntity var2) {
         KongaLabel var3 = WizardStyleSheet.SMALL_SECTION_STYLE.makeLabel(Strings.get("RouteTypeSelector.SelectType"));
         var3.setBorder(Empty.border(0, 0, 5, 0));
         UiProvider var4 = this.buttons.getUi(var3, RadioButtonGroupPanel.Layout.TWO_ROWS);
         BoxBuilder var5 = BoxBuilder.vertical().leftAligned();
         var5.addAllWithSpace(32, this.createCaption(var1, var2), var4);
         return var5;
      }

      private UiProvider createCaption(Operation var1, IntegrationEntity var2) {
         BoxBuilder var3 = BoxBuilder.vertical().leftAligned();
         var3.addAllWithSpace(
            10, WizardStyleSheet.SMALL_SECTION_STYLE.makeLabel(Strings.get("RouteTypeSelector.NewRoute")), this.createConnectionLabel(var1, var2)
         );
         return var3;
      }

      private UiProvider createConnectionLabel(Operation var1, IntegrationEntity var2) {
         BoxBuilder var3 = BoxBuilder.horizontal();
         JLabel var4 = new JLabel("");
         RendererUtils.decorateRenderer(var4, var1, EntityIcons.Size.MEDIUM);
         JLabel var5 = new JLabel(ApplicationResources.ICONS.getIcon("ArrowRight.16"));
         JLabel var6 = new JLabel("");
         RendererUtils.decorateRenderer(var6, var2, EntityIcons.Size.MEDIUM);
         setLabelStyle(var4, var6);
         return var3.addAllWithSpace(10, var4, var5, var6);
      }

      private static void setLabelStyle(JLabel... var0) {
         WizardStyleSheet.SMALL_CAPTION_STYLE.makeOverAll(var0);
      }

      private JLabel createLabelForButton(JRadioButton var1, RouteType var2) {
         KongaLabel var3 = new KongaLabel(selectorLabel(var2));
         var3.setIcon(selectorIcon(var2));
         var3.setLabelForButton(var1);
         var3.setBorder(Empty.border(0, 8, 0, 0));
         setLabelStyle(var3);
         return var3;
      }

      private static Icon selectorIcon(RouteType var0) {
         switch (var0) {
            case SUCCESS:
               return GraphResources.SUCCESS_24;
            case FAILURE:
               return GraphResources.FAILURE_24;
            default:
               throw new RuntimeException(var0 + " should not be selectable in a RouteTypeSelector");
         }
      }

      private static MnemonicString selectorLabel(RouteType var0) {
         return ApplicationResources.MNEMONIC_FACTORY.fromString(Strings.get("RouteTypeSelector." + var0.name()));
      }
   }
}
