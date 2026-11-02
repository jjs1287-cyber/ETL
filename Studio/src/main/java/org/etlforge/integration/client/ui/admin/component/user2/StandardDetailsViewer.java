package org.etlforge.integration.client.ui.admin.component.user2;

import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.CaptionTitlePanel;

public class StandardDetailsViewer implements ServerItemDetailsViewer, UiProvider {
   private final UserDetailsViewer userDetailsViewer;
   private final GroupDetailsViewer groupDetailsViewer;

   public StandardDetailsViewer(UserConsoleModel var1) {
      this.userDetailsViewer = new UserDetailsViewer(var1);
      this.groupDetailsViewer = new GroupDetailsViewer(var1);
   }

   @Override
   public void dispose() {
      this.userDetailsViewer.dispose();
      this.groupDetailsViewer.dispose();
   }

   @Override
   public void apply() {
      this.userDetailsViewer.apply();
      this.groupDetailsViewer.apply();
   }

   @Override
   public void clear() {
      this.displayUser(null);
      this.displayGroup(null);
   }

   @Override
   public void displayGroup(Group var1) {
      this.groupDetailsViewer.setGroup(var1);
   }

   @Override
   public void displayUser(User var1) {
      this.userDetailsViewer.setUser(var1);
   }

   @Override
   public Group getDisplayedGroup() {
      return this.groupDetailsViewer.getGroup();
   }

   @Override
   public User getDisplayedUser() {
      return this.userDetailsViewer.getUser();
   }

   @Override
   public void addChangeListener(InputChangeListener var1) {
      this.userDetailsViewer.addChangeListener(var1);
      this.groupDetailsViewer.addChangeListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.setWeightX(1.0);
      var2.withInsets(0, 0, 10, 0);
      KongaLabel var3 = TextStyles.AdminHintBold.makeLabel("User Details");
      var2.add(new CaptionTitlePanel(var3, 25)).add(this.userDetailsViewer);
      var2.add(" ");
      KongaLabel var4 = TextStyles.AdminHintBold.makeLabel("Group Details");
      var2.add(new CaptionTitlePanel(var4, 25)).add(this.groupDetailsViewer);
      new BorderLayoutBuilder().north(var2).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.userDetailsViewer.requestFocus();
   }
}
