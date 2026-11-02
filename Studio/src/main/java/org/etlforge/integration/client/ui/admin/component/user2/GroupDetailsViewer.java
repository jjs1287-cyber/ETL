package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.base.Objects;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.widget.TextAreaInput;
import org.etlforge.integration.client.server.ui.user.GroupMemberList;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.input.undo.InputPanelUndoRedoEnabler;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.selection.SelectionSourceConverter;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.overlay.OverlayPositioner;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class GroupDetailsViewer implements Disposable, UiProvider {
   private final UserConsoleModel model;
   private final InputPanel inputPanel;
   private final TextInputField2 nameField;
   private final GroupDetailsViewer.DescriptionAction descriptionAction;
   private final GroupMemberList memberList;
   private final Action addMemberAction;
   private final RemoveGroupMemberAction removeMemberAction;
   private Group group;
   private final UserConsoleModelListener modelListener;
   private InputPanelUndoRedoEnabler undoRedo;

   public GroupDetailsViewer(UserConsoleModel var1) {
      this.model = var1;
      var1.addModelListener(this.modelListener = new GroupDetailsViewer.ModelListener());
      this.inputPanel = new InputPanel();
      this.nameField = new GroupDetailsViewer.NameField();
      this.nameField.setEnabled(false);
      this.nameField.setSelectAllWhenFocused(true);
      this.inputPanel.addInputField(this.nameField, this.nameField, this.nameField);
      this.descriptionAction = new GroupDetailsViewer.DescriptionAction();
      this.memberList = new GroupMemberList();
      this.memberList.setDropEnabled(true);
      this.addMemberAction = new GroupDetailsViewer.AddMemberAction();
      this.removeMemberAction = new RemoveGroupMemberAction(this.getMemberSelectionSource(), Strings.get("User.GroupDetails.Remove"));
      this.inputPanel.setSendInputChangeEvents(true);
      this.inputPanel.setSendUndoEvents(true);
   }

   public void setGroup(Group var1) {
      if (var1 != this.group) {
         if (this.undoRedo != null) {
            this.undoRedo.discardAllEdits();
         }

         this.descriptionAction.closeEditor();
         this.group = var1;
         this.memberList.setGroup(var1);
         if (var1 != null) {
            this.nameField.setEnabled(true);
            this.nameField.setEditable(var1.isRenamable() && !this.model.isReadOnly());
            this.nameField.setValueSilently(var1.getName());
            this.addMemberAction.setEnabled(!this.model.isReadOnly());
            this.descriptionAction.setEnabled(!this.model.isReadOnly());
            this.requestFocus();
         } else {
            this.nameField.setEnabled(false);
            this.nameField.setValueSilently("");
            this.addMemberAction.setEnabled(false);
            this.descriptionAction.setEnabled(false);
         }
      }
   }

   public Group getGroup() {
      return this.group;
   }

   public void apply() {
      if (this.group != null) {
         this.applyName();
      }
   }

   @Override
   public void dispose() {
      this.model.removeModelListener(this.modelListener);
   }

   @Override
   public void requestFocus() {
      if (this.nameField.isEditable()) {
         this.nameField.requestFocus();
      }
   }

   public void addChangeListener(InputChangeListener var1) {
      this.inputPanel.addInputChangeListener(var1);
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
      var1.addRow(var2, null);
   }

   private JComponent layoutFields() {
      TwoColumnPanel var1 = LayoutUtils.getDetailsViewerLayout();
      var1.addRow(this.nameField.getLabel(), this.nameField.getInputComponent());
      KongaButton var2 = new KongaButton(this.descriptionAction);
      this.descriptionAction.setTrigger(var2);
      var1.addRow(null, InvisiblePanel.of(var2));
      var1.withAnchor(GridBagLayoutPanel.Anchor.NORTHWEST).withRightFill(GridBagLayoutPanel.Fill.BOTH).withRightWeights(1.0, 1.0);
      var1.addRow(new JLabel("Members"), BorderLayoutBuilder.fillWith(this.memberList));
      var1.withRightFill(GridBagLayoutPanel.Fill.NONE).withRightWeights(0.0, 0.0);
      BoxBuilder var3 = BoxBuilder.horizontal().withSmallSizedIconsForActions();
      var3.addAllWithSpace(10, this.addMemberAction, this.removeMemberAction);
      var1.addRow(null, var3.invisible());
      return new BorderLayoutBuilder().north(var1).container();
   }

   private void applyName() {
      if (this.group.isRenamable()) {
         this.group.setName(this.nameField.getValue().trim());
      }
   }

   private void setReadOnly(boolean var1) {
      boolean var2 = !var1 && this.group != null;
      this.nameField.setEditable(var2 && this.group.isRenamable());
      this.addMemberAction.setEnabled(false);
      this.removeMemberAction.setReadOnly(var1);
      this.descriptionAction.setEnabled(var2);
   }

   private SelectionSource getMemberSelectionSource() {
      return new SelectionSourceConverter(this.memberList.asSelectionSource(), new SelectionConverter() {
         @Override
         public Object fromSelection(Object var1) {
            return new GroupMember((User)var1, GroupDetailsViewer.this.group);
         }

         @Override
         public Object toSelection(Object var1) {
            return ((GroupMember)var1).getUser();
         }
      });
   }

   private final class AddMemberAction extends AbstractAction {
      public AddMemberAction() {
         super(Strings.get("User.GroupDetails.Add"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (GroupDetailsViewer.this.group != null) {
            JPopupMenu var2 = this.createPopup();
            Object var3 = var1.getSource();
            if (var3 instanceof JButton) {
               var2.show((JButton)var3, 8, 8);
            }
         }
      }

      private JPopupMenu createPopup() {
         JPopupMenu var1 = new JPopupMenu();

         for (User var3 : GroupDetailsViewer.this.model.getUsers()) {
            GroupDetailsViewer.AddMemberAction.UserItem var4 = new GroupDetailsViewer.AddMemberAction.UserItem(var3);
            var4.setEnabled(!GroupDetailsViewer.this.group.isMember(var3));
            var1.add(var4);
         }

         return var1;
      }

      private final class UserItem extends JMenuItem implements ActionListener {
         private final User user;

         private UserItem(User var2) {
            super(var2.getName(), ClientIcons.USER_16);
            this.user = var2;
            this.addActionListener(this);
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            if (GroupDetailsViewer.this.group != null) {
               GroupDetailsViewer.this.group.addMember(this.user);
            }
         }
      }
   }

   private final class DescriptionAction extends AbstractAction {
      private TextAreaInput editor;
      private JComponent trigger;

      public DescriptionAction() {
         super(Strings.get("User.GroupDetails.Description"));
         this.setEnabled(false);
      }

      public void setTrigger(JComponent var1) {
         this.trigger = var1;
      }

      public void closeEditor() {
         if (this.editor != null) {
            this.editor.close();
            this.editor = null;
         }
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.closeEditor();
         this.editor = new TextAreaInput("Description for " + GroupDetailsViewer.this.group.getName(), new Receiver<String>() {
            public void handle(String var1) {
               if (var1 != null) {
                  GroupDetailsViewer.this.group.setDescription(var1);
               }
            }
         });
         this.editor.setSize(4, 30);
         this.editor.setTrigger(this.trigger);
         this.editor.setOverlayPositioner(this.getOverlayPositioner());
         this.editor.show(GroupDetailsViewer.this.group.getDescription());
      }

      private OverlayPositioner getOverlayPositioner() {
         return new OverlayRelativePositioner(this.trigger, new Point(8, 8));
      }
   }

   private final class ModelListener extends UserConsoleModelListener.Adapter {
      private ModelListener() {
      }

      @Override
      public void memberAdded(Group var1, User var2) {
         this.updateMemberList(var1);
      }

      @Override
      public void memberRemoved(Group var1, User var2) {
         this.updateMemberList(var1);
      }

      private void updateMemberList(Group var1) {
         if (var1 == GroupDetailsViewer.this.getGroup()) {
            GroupDetailsViewer.this.memberList.update();
         }
      }

      @Override
      public void groupPropertyChanged(UserConsoleModel var1, PropertyChangeEvent var2) {
         String var3 = var2.getPropertyName();
         if ("name".equals(var3)) {
            String var4 = (String)var2.getNewValue();
            if (!Objects.equal(var4, GroupDetailsViewer.this.nameField.getValue())) {
               GroupDetailsViewer.this.nameField.setValueSilently(var4);
            }
         }
      }

      @Override
      public void userPropertyChanged(UserConsoleModel var1, PropertyChangeEvent var2) {
         if (GroupDetailsViewer.this.group != null) {
            GroupDetailsViewer.this.memberList.repaint();
         }
      }

      @Override
      public void readOnlyStateChanged(UserConsoleModel var1, boolean var2) {
         GroupDetailsViewer.this.setReadOnly(var2);
      }
   }

   private final class NameField extends TextInputField2 implements FocusListener {
      public NameField() {
         super("name", LayoutUtils.getDetailField(), new JLabel(Strings.get("User.GroupDetails.Name")));
         this.getInputComponent().addFocusListener(this);
      }

      @Override
      public boolean isInputValid(String var1) {
         return GroupDetailsViewer.this.group == null
            || !var1.isEmpty() && GroupDetailsViewer.this.model.isGroupNameAvailable(GroupDetailsViewer.this.group, var1);
      }

      @Override
      public String getInvalidMessage(String var1) {
         return var1.isEmpty() ? "The name cannot be empty" : "There already exists another group with this name";
      }

      @Override
      public void focusLost(FocusEvent var1) {
         if (GroupDetailsViewer.this.group != null) {
            GroupDetailsViewer.this.applyName();
         }
      }

      @Override
      public void focusGained(FocusEvent var1) {
      }
   }
}
