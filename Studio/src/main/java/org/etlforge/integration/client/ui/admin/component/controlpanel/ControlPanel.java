package org.etlforge.integration.client.ui.admin.component.controlpanel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.view.ViewActivationResource;
import org.etlforge.application.ui.view.ViewDestroyingResource;
import org.etlforge.application.ui.window.AbstractWindowElement;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.cli.CommandModule;
import org.etlforge.integration.client.ui.admin.AdministrationView;
import org.etlforge.integration.client.ui.admin.core.console.ConsoleLauncher;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.BigButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class ControlPanel extends AbstractWindowElement implements WindowSectionContent {
   private static final String ID = "administration_control_panel";
   private final AdministrationView view;
   private final ImmutableList<ConsoleLauncher> launchers;
   private final ImmutableList<Action> actions;
   private WindowSection windowSection;

   public ControlPanel(AdministrationView var1, Iterable<ConsoleLauncher> var2, Iterable<Action> var3) {
      super("administration_control_panel");
      this.view = var1;
      this.launchers = ImmutableList.copyOf(var2);
      this.actions = ImmutableList.copyOf(var3);
      this.addResource(new ViewDestroyingResource(var1.getWindow().getViewManager(), var1, WindowElementStateSpan.WHEN_OPEN));
      this.addResource(new ViewActivationResource(var1, WindowElementStateSpan.WHEN_ACTIVE));
      this.addResource(new ControlPanel.ContextMenuShortCutResource(var1.getWindow()));
      CommandModule var4 = ConsoleCommands.build(var2);
      this.addResource(new CommandActivator(var1.getWindow().getCommandConsole(), var4));
   }

   public void setWindowSection(WindowSection var1) {
      this.windowSection = var1;
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      return this.view.allowsRemoval() && super.removeFromWindow(var1);
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder.from(var1).center(this.getTabbedPaneConsole());
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.ADMINISTRATION_24;
   }

   @Override
   public String getTitle() {
      return getString("ControlPanel.Title");
   }

   @Override
   public WindowSection getWindowSection() {
      return this.windowSection;
   }

   @Override
   public boolean isDetachable() {
      return false;
   }

   private JComponent getTabbedPaneConsole() {
      JTabbedPane var1 = createTabbedPane();
      this.addConsolePanelToTabPane(var1);
      this.addActionsPanelToTabPane(var1);
      return var1;
   }

   private void addConsolePanelToTabPane(JTabbedPane var1) {
      JComponent var2 = this.createConsoleLaunchingPanel();
      var1.addTab(getString("ControlPanel.Consoles.Title"), new KongaScrollPane(var2));
   }

   private void addActionsPanelToTabPane(JTabbedPane var1) {
      var1.addTab(getString("ControlPanel.Actions.Title"), this.createActionsPanel());
   }

   private static JTabbedPane createTabbedPane() {
      TextStyle var0 = TextStyles.BigAdminHintBold;
      JTabbedPane var1 = new JTabbedPane();
      var1.setFont(var0.getFont());
      var1.setForeground(var0.getColor());
      var1.setBackground(Colors.LIGHT_BLUE_BACKGROUND);
      var1.setOpaque(true);
      var1.setBorder(Empty.border(5, 0, 0, 0));
      return var1;
   }

   private JComponent createConsoleLaunchingPanel() {
      ArrayList var1 = Lists.newArrayList();

      for (ConsoleLauncher var3 : this.launchers) {
         var1.add(this.getWrapper(var3));
      }

      return createOneColumnLayout(var1);
   }

   private JComponent createActionsPanel() {
      ArrayList var1 = Lists.newArrayList();

      for (Action var3 : this.actions) {
         var1.add(BigButton.fromAction(var3, TextStyles.AdminHintBold));
      }

      return createOneColumnLayout(var1);
   }

   private UiProvider getWrapper(final ConsoleLauncher var1) {
      JPanel var2 = InvisiblePanel.of(var1);
      var2.setBorder(Empty.border(5));
      var2.setBackground(javax.swing.UIManager.getColor("Panel.background"));
      BigButton var3 = new BigButton(var2);
      var3.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.launch();
         }
      });
      return var3;
   }

   private static JComponent createOneColumnLayout(Iterable<UiProvider> var0) {
      Grid var1 = Grid.oneColumn();

      for (UiProvider var3 : var0) {
         var1.add(var3);
      }

      return new BorderLayoutBuilder().north(var1).withBorder(Empty.border(10, 0, 0, 0)).withBackground(javax.swing.UIManager.getColor("Panel.background")).container();
   }

   private static String getString(String var0) {
      return AdminViewResources.STRINGS.getString(var0);
   }

   private class ContextMenuShortCutResource extends AbstractWindowElementResource implements WindowKeyDispatcher {
      private final ApplicationWindow appWin;
      private final KeyStroke keyStroke;

      public ContextMenuShortCutResource(ApplicationWindow var2) {
         this.appWin = var2;
         this.keyStroke = KeyUtils.getCtrlShiftDown(65);
      }

      @Override
      public void create(WindowElement var1) {
         this.appWin.addGlobalKeyDispatcher(this.keyStroke, this);
      }

      @Override
      public void dispose(WindowElement var1) {
         this.appWin.removeGlobalKeyDispatcher(this.keyStroke, this);
      }

      @Override
      public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
         KongaPopupMenu var3 = new KongaPopupMenu();

         for (ConsoleLauncher var5 : ControlPanel.this.launchers) {
            var5.addToContextMenu(var3);
         }

         var3.showCentered(this.appWin.getFrame());
         return true;
      }

      @Override
      public String getId() {
         return "admin.console.contextmenu";
      }
   }
}

