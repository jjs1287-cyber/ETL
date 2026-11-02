from pathlib import Path
root=Path('C:/ETL'); backup=root/'Archives/project-variable-editor-20261029'; backup.mkdir(parents=True,exist_ok=True)
paths=['Studio/src/main/java/org/etlforge/integration/data/entity/ProjectVariable.java','Studio/src/main/java/org/etlforge/integration/client/ui/interchange/entity/projectvariable/ProjectVariablePage.java','Studio/src/main/java/org/etlforge/integration/server/implementation/webservice/interchange/deploy/impl/DeployInterchangeModelWsImplWrapper.java','Server/src/native/integration/konga/UiBackend/WebServices/DeployInterchangeData/Impl/HandleChangedDataImpl.cpp']
for rel in paths:
 p=root/rel; dest=backup/rel; dest.parent.mkdir(parents=True,exist_ok=True); dest.write_bytes(p.read_bytes())
p=root/paths[0];s=p.read_text();s=s.replace('"default_value"};','"default_value", "is_password", "description"};');s=s.replace('    @Override public boolean isDeepCopyable()', '''    public boolean isPassword() { return "1".equals(getProperty("is_password", "0")); }
    public void setPassword(boolean value) { setStringProperty("is_password", value ? "1" : "0"); }
    public String getDescription() { return getProperty("description", ""); }
    public void setDescription(String value) { value = value == null ? "" : value; validateValue(value); setStringProperty("description", value); }
    @Override public boolean isDeepCopyable()''');p.write_text(s,encoding='utf-8')
p=root/paths[1];p.write_text('''package org.etlforge.integration.client.ui.interchange.entity.projectvariable;
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
                TextInputField2 field = new TextInputField2(name, component) { @Override public boolean isInputValid(String text) { return true; } };
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
''',encoding='utf-8')
p=root/paths[2];s=p.read_text().replace('new String[]{"projectvariable_name", "default_value_utf8"}', 'new String[]{"projectvariable_name", "default_value_utf8", "is_password", "description_utf8"}');s=s.replace(' : variable.getName()}));', ' : "is_password".equals(key) ? (variable.isPassword() ? "1" : "0") : "description_utf8".equals(key) ? java.util.Base64.getEncoder().encodeToString(variable.getDescription().getBytes(java.nio.charset.StandardCharsets.UTF_8)) : variable.getName()}));');p.write_text(s,encoding='utf-8')
p=root/paths[3];s=p.read_text();s=s.replace('variables.m_vColumnNames.push_back("default_value");','variables.m_vColumnNames.push_back("default_value");\n    variables.m_vColumnNames.push_back("is_password");\n    variables.m_vColumnNames.push_back("description");');s=s.replace('        InterchangeDataRowStruct row;\n        row.m_vFields.push_back(ClientField(it->first.c_str()));','''        const std::string descriptionEncoded = it->second->m_properties.GetString("description_utf8", "");
        if (descriptionEncoded.size() > 1398104) throw CrException("Project variable description exceeds 1 MiB.");
        int descriptionLength = 0;
        std::unique_ptr<unsigned char[]> descriptionBytes(konga::DecodeBase64(descriptionEncoded, descriptionLength));
        etlforge::validateProjectVariable(name, std::string(reinterpret_cast<const char*>(descriptionBytes.get()), descriptionLength));
        const std::string password = it->second->m_properties.GetString("is_password", "0");
        if (password != "0" && password != "1") throw CrException("Invalid project variable password flag.");
        InterchangeDataRowStruct row;
        row.m_vFields.push_back(ClientField(it->first.c_str()));''',1);s=s.replace('row.m_vFields.push_back(ClientField(encoded.c_str()));','row.m_vFields.push_back(ClientField(encoded.c_str()));\n        row.m_vFields.push_back(ClientField(password.c_str()));\n        row.m_vFields.push_back(ClientField(descriptionEncoded.c_str()));',1);s=s.replace("SET default_value=convert_from(decode(default_value,'base64'),'UTF8') WHERE", "SET default_value=convert_from(decode(default_value,'base64'),'UTF8'), description=convert_from(decode(description,'base64'),'UTF8') WHERE");p.write_text(s,encoding='utf-8')
(root/'Server/database/migrations/011-project-variable-editor.sql').write_text("BEGIN;\nALTER TABLE projectvariablestab ADD COLUMN IF NOT EXISTS is_password integer NOT NULL DEFAULT 0 CHECK (is_password IN (0,1));\nALTER TABLE projectvariablestab ADD COLUMN IF NOT EXISTS description text NOT NULL DEFAULT '';\nCREATE OR REPLACE VIEW guidsforprojectvariablesview AS SELECT g.guid, x.projectvariable_id, x.projectvariable_name, x.default_value, g.item_id, x.is_password, x.description FROM projectvariablestab x JOIN guidsforprojectvariablestab g ON g.item_id=x.projectvariable_id;\nCOMMIT;\n",encoding='utf-8')
