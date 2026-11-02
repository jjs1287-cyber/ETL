package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.widget.password.AbstractPasswordAction;
import org.etlforge.integration.client.server.ui.user.GroupListUi;
import org.etlforge.integration.client.server.ui.user.MemberOfGroupsList;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.input.undo.InputPanelUndoRedoEnabler;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.selection.SelectionSourceConverter;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class UserDetailsViewer implements Disposable, UiProvider {
   private User user;
   private final UserConsoleModel model;
   private final InputPanel inputPanel;
   private final LinkedHashMap<String, UserDetailsViewer.Field> fields;
   private final MemberOfGroupsList groupList;
   private final PropertyChangeListener userListener;
   private final UserConsoleModelListener modelListener;
   private final UserDetailsViewer.PasswordAction passwordAction;
   private final Action addToGroupAction;
   private final RemoveGroupMemberAction removeFromGroupAction;
   private InputPanelUndoRedoEnabler undoRedo;

   public UserDetailsViewer(UserConsoleModel var1) {
      this.model = var1;
      this.inputPanel = new InputPanel();
      this.fields = Maps.newLinkedHashMap();
      this.groupList = new MemberOfGroupsList(4);
      this.groupList.setDropEnabled(true);
      this.passwordAction = new UserDetailsViewer.PasswordAction();
      this.addToGroupAction = new UserDetailsViewer.AddToGroupAction();
      this.removeFromGroupAction = new RemoveGroupMemberAction(this.getGroupSelectionSource(), Strings.get("User.UserDetails.Remove"));
      this.userListener = new UserDetailsViewer.UserListener();
      var1.addModelListener(this.modelListener = new UserDetailsViewer.ModelListener());
      this.createFields();
      this.inputPanel.setSendInputChangeEvents(true);
   }

   public void setUser(User var1) {
      if (var1 != this.user) {
         if (this.undoRedo != null) {
            this.undoRedo.discardAllEdits();
         }

         this.passwordAction.closeOverlay();
         if (this.user != null) {
            this.apply();
            this.stopListeningToUser();
         }

         this.user = var1;

         for (UserDetailsViewer.Field var3 : this.fields.values()) {
            var3.setUser(var1);
         }

         this.groupList.setUser(var1);
         this.passwordAction.setEnabled(var1 != null && !this.model.isReadOnly());
         this.addToGroupAction.setEnabled(var1 != null && !this.model.isReadOnly());
         if (var1 != null) {
            this.getNameField().setEditable(!this.model.isReadOnly() && var1.isRenamable());
            var1.addPropertyChangeListener(this.userListener);
            this.requestFocus();
         }
      }
   }

   public User getUser() {
      return this.user;
   }

   @Override
   public void dispose() {
      this.stopListeningToUser();
      this.model.removeModelListener(this.modelListener);
   }

   private void stopListeningToUser() {
      if (this.user != null) {
         this.user.removePropertyChangeListener(this.userListener);
      }
   }

   public void apply() {
      if (this.user != null) {
         for (UserDetailsViewer.Field var2 : this.fields.values()) {
            var2.apply();
         }
      }
   }

   public void addChangeListener(InputChangeListener var1) {
      this.inputPanel.addInputChangeListener(var1);
   }

   @Override
   public void requestFocus() {
      KongaTextField var1 = this.getNameField();
      if (var1.isEditable()) {
         var1.requestFocusInWindow();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      KongaLayeredPane var2 = new KongaLayeredPane(this.layoutFields());
      this.undoRedo = new InputPanelUndoRedoEnabler(this.inputPanel, var2);
      var1.display(var2);
   }

   public void layoutIn(TwoColumnLayout var1) {
      JComponent var2 = this.layoutFields();
      this.undoRedo = new InputPanelUndoRedoEnabler(this.inputPanel, var2);
      var1.addRow(var2, BorderLayoutBuilder.fillWith(this.layoutGroupList()));
   }

   private void createFields() {
      UserDetailsViewer.Field var1 = new UserDetailsViewer.Field(Strings.get("User.UserDetails.Login"), "userName") {
         @Override
         protected boolean isValid(String var1) {
            return UserDetailsViewer.this.user == null
               || !var1.isEmpty() && UserDetailsViewer.this.model.isUserNameAvailable(UserDetailsViewer.this.user, var1);
         }

         @Override
         protected String getErrorMessage(String var1) {
            return var1.isEmpty() ? "The name cannot be empty" : "There already exists another user with this name";
         }
      };
      this.registerField(var1);
      this.registerField(new UserDetailsViewer.Field(Strings.get("User.UserDetails.FirstName"), "firstName"));
      this.registerField(new UserDetailsViewer.Field(Strings.get("User.UserDetails.LastName"), "lastName"));
      this.registerField(new UserDetailsViewer.Field(Strings.get("User.UserDetails.Email"), "email"));
   }

   private UserDetailsViewer.Field registerField(UserDetailsViewer.Field var1) {
      this.fields.put(var1.getProperty(), var1);
      var1.register(this.inputPanel);
      return var1;
   }

   private KongaTextField getNameField() {
      return this.fields.get("userName").getWidget();
   }

   private JComponent layoutFields() {
      TwoColumnPanel var1 = LayoutUtils.getDetailsViewerLayout();

      for (UserDetailsViewer.Field var3 : this.fields.values()) {
         var1.addRow(var3.getLabel(), var3.getWidget());
         if (var3.getProperty().equals("userName")) {
            this.layoutPasswordButton(var1);
         }
      }

      var1.withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);
      var1.withRightWeights(1.0, 1.0).withRightFill(GridBagLayoutPanel.Fill.BOTH);
      var1.addRow(new JLabel("Member of"), BorderLayoutBuilder.fillWith(this.groupList));
      var1.withRightFill(GridBagLayoutPanel.Fill.NONE).withRightWeights(0.0, 0.0);
      BoxBuilder var4 = Horizontal.box().withSmallSizedIconsForActions();
      var4.addAllWithSpace(10, this.addToGroupAction, this.removeFromGroupAction);
      var1.addRow(null, InvisiblePanel.of(var4));
      return new BorderLayoutBuilder().north(var1).container();
   }

   private void layoutPasswordButton(TwoColumnPanel var1) {
      KongaButton var2 = new KongaButton(this.passwordAction);
      this.passwordAction.setTrigger(var2);
      var1.addRow(null, InvisiblePanel.of(var2));
   }

   private UiProvider layoutGroupList() {
      return Vertical.borderLayout().of("Member of:", this.groupList).withGap(3);
   }

   private void disableAll() {
      for (UserDetailsViewer.Field var2 : this.fields.values()) {
         var2.disable();
      }
   }

   private void setReadOnly(boolean var1) {
      boolean var2 = !var1 && this.user != null;
      this.passwordAction.setEnabled(var2);
      this.addToGroupAction.setEnabled(var2);
      this.removeFromGroupAction.setReadOnly(var1);
      KongaTextField var3 = this.getNameField();
      var3.setEditable(var2 && this.user.isRenamable());

      for (UserDetailsViewer.Field var5 : this.fields.values()) {
         if (var5.getWidget() != var3) {
            var5.getWidget().setEditable(var2);
         }
      }
   }

   private SelectionSource getGroupSelectionSource() {
      return new SelectionSourceConverter(this.groupList.asSelectionSource(), new SelectionConverter() {
         @Override
         public Object fromSelection(Object var1) {
            return new GroupMember(UserDetailsViewer.this.user, ((GroupListUi)var1).getGroup());
         }

         @Override
         public Object toSelection(Object var1) {
            return ((GroupMember)var1).getGroup();
         }
      });
   }

   private final class AddToGroupAction extends AbstractAction {
      public AddToGroupAction() {
         super(Strings.get("User.UserDetails.Add"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (UserDetailsViewer.this.user != null) {
            JPopupMenu var2 = this.createPopup();
            Object var3 = var1.getSource();
            if (var3 instanceof JButton) {
               var2.show((JButton)var3, 8, 8);
            }
         }
      }

      private JPopupMenu createPopup() {
         JPopupMenu var1 = new JPopupMenu();

         for (Group var3 : UserDetailsViewer.this.model.getGroups()) {
            UserDetailsViewer.AddToGroupAction.GroupItem var4 = new UserDetailsViewer.AddToGroupAction.GroupItem(var3);
            var4.setEnabled(!var3.isMember(UserDetailsViewer.this.user));
            var1.add(var4);
         }

         return var1;
      }

      private final class GroupItem extends JMenuItem implements ActionListener {
         private final Group group;

         private GroupItem(Group var2) {
            super(var2.getName(), ClientIcons.GROUP_16);
            this.group = var2;
            this.addActionListener(this);
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            if (UserDetailsViewer.this.user != null) {
               this.group.addMember(UserDetailsViewer.this.user);
            }
         }
      }
   }

   private static class Field {
      private final TextInputField2 field;
      private final String property;
      private User user;

      public Field(String var1, String var2) {
         this.field = new TextInputField2(var2, LayoutUtils.getDetailField(), new JLabel(var1)) {
            @Override
            public boolean isInputValid(String var1) {
               return Field.this.isValid(var1);
            }

            @Override
            public String getInvalidMessage(String var1) {
               return Field.this.getErrorMessage(var1);
            }
         };
         this.property = var2;
         this.field.getInputComponent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent var1) {
               Field.this.apply();
            }
         });
         this.field.setEnabled(false);
      }

      protected boolean isValid(String var1) {
         return true;
      }

      protected String getErrorMessage(String var1) {
         return null;
      }

      public String getProperty() {
         return this.property;
      }

      public void register(InputPanel var1) {
         var1.addInputField(this.field, this.field, this.field);
      }

      public JLabel getLabel() {
         return this.field.getLabel();
      }

      public KongaTextField getWidget() {
         return (KongaTextField)this.field.getInputComponent();
      }

      public void disable() {
         this.field.setEnabled(false);
         this.field.setValueSilently("");
      }

      public void setUser(User var1) {
         this.user = var1;
         if (var1 != null) {
            this.field.setEnabled(true);
            this.populate(var1);
         } else {
            this.field.setEnabled(false);
            this.field.setValueSilently("");
         }
      }

      public void apply() {
         try {
            if (this.field.isEnabled() && this.field.isEditable()) {
               PropertyDescriptor var1 = new PropertyDescriptor(this.property, User.class);
               Method var2 = var1.getWriteMethod();
               var2.invoke(this.user, this.field.getValue().trim());
            }
         } catch (Exception var3) {
            ErrorLog.log(UserDetailsViewer.class, "Failed to update the User property \"" + this.property + "\"", var3);
         }
      }

      private void populate(User var1) {
         try {
            PropertyDescriptor var2 = new PropertyDescriptor(this.property, User.class);
            Method var3 = var2.getReadMethod();
            this.field.setValueSilently((String)var3.invoke(var1));
         } catch (Exception var4) {
            ErrorLog.log(UserDetailsViewer.class, "Failed to read the User property \"" + this.property + "\"", var4);
         }
      }
   }

   private class ModelListener extends UserConsoleModelListener.Adapter {
      private ModelListener() {
      }

      @Override
      public void memberAdded(Group var1, User var2) {
         if (var2 == UserDetailsViewer.this.user) {
            UserDetailsViewer.this.groupList.update();
         }
      }

      @Override
      public void memberRemoved(Group var1, User var2) {
         if (var2 == UserDetailsViewer.this.user) {
            UserDetailsViewer.this.groupList.update();
         }
      }

      @Override
      public void userRemoved(UserConsoleModel var1, User var2) {
         if (var2 == UserDetailsViewer.this.user) {
            UserDetailsViewer.this.disableAll();
         }
      }

      @Override
      public void groupPropertyChanged(UserConsoleModel var1, PropertyChangeEvent var2) {
         if (UserDetailsViewer.this.user != null) {
            UserDetailsViewer.this.groupList.repaint();
         }
      }

      @Override
      public void readOnlyStateChanged(UserConsoleModel var1, boolean var2) {
         UserDetailsViewer.this.setReadOnly(var2);
      }
   }

   private final class PasswordAction extends AbstractPasswordAction {
      public PasswordAction() {
         this.setIncludeReTypeField(true);
         this.setEnabled(false);
      }

      @Override
      protected void handlePassword(char[] var1) {
         if (var1 != null) {
            UserDetailsViewer.this.user.setPassword(new String(var1));
         }
      }
   }

   private final class UserListener implements PropertyChangeListener {
      private UserListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         UserDetailsViewer.Field var3 = UserDetailsViewer.this.fields.get(var2);
         if (var3 != null) {
            var3.getWidget().setText((String)var1.getNewValue());
         }
      }
   }
}
