package org.etlforge.integration.client.ui.interchange.entity.projectvariable;
import javax.swing.*;
import org.etlforge.integration.client.ui.interchange.component.page.entity.*;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.ProjectVariable;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.input.CheckBoxInputField;
public final class ProjectVariablePage extends IntegrationEntityPage {
    public ProjectVariablePage(ProjectVariable variable) { super(new IntegrationEntityPageModel(variable)); setUseScroll(true); }
    public static final class Fields {
        public final JPasswordField value = new JPasswordField(40);
        public final JCheckBox password = new JCheckBox("Is password");
        public final JTextArea description = new JTextArea(4, 40);
        public Fields(ProjectVariable variable) {
            value.setText(variable.getDefaultValue());
            password.setSelected(variable.isPassword());
            value.setEchoChar(password.isSelected() ? '*' : (char)0);
            description.setText(variable.getDescription());
            description.setLineWrap(true);
            description.setWrapStyleWord(true);
            password.addActionListener(event -> {
                if (!password.isSelected()) value.setText("");
                value.setEchoChar(password.isSelected() ? '*' : (char)0);
            });
        }
        public void apply(ProjectVariable variable) {
            char[] characters = value.getPassword();
            try {
                String text = new String(characters);
                ProjectVariable.validateName(variable.getName());
                ProjectVariable.validateValue(text);
                ProjectVariable.validateValue(description.getText());
                variable.setDefaultValue(text);
                variable.setPassword(password.isSelected());
                variable.setDescription(description.getText());
            } finally { java.util.Arrays.fill(characters, (char)0); }
        }
    }
    @Override protected IntegrationEntityPanel createDataPanel() {
        final ProjectVariable variable = (ProjectVariable)getObject();
        return new IntegrationEntityPanel(getWindow(), variable) {
            private final Fields fields = new Fields(variable);
            {
                TextInputField2 valueField = field("Value", fields.value);
                TextInputField2 descriptionField = field("Description", fields.description);
                CheckBoxInputField passwordField = new CheckBoxInputField("Is password", fields.password);
                addInputField(valueField, valueField, valueField);
                addInputField(descriptionField, descriptionField, descriptionField);
                addInputField(passwordField);
                Layout layout = getDefaultLayout();
                layout.addToTopGrid(valueField);
                layout.addToTopGrid(new JLabel(""), fields.password);
                JScrollPane descriptionScroll = new JScrollPane(fields.description);
                descriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                layout.addToTopGrid(new JLabel("Description"), descriptionScroll);
            }
            private TextInputField2 field(String name, javax.swing.text.JTextComponent component) {
                TextInputField2 field = new TextInputField2(name, component, new JLabel(name)) { @Override public boolean isInputValid(String text) { return true; } };
                field.setRequired(false);
                return field;
            }
            @Override protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
                try { fields.apply(variable); }
                catch (IllegalArgumentException error) { throw new IntegrationDataPanelException(error); }
            }
        };
    }
}
