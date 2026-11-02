package org.etlforge.integration.client.ui.interchange.entity.xslt;

import java.awt.Font;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.XsltTransform;
import org.etlforge.ui.input.TextInputField2;

public final class XsltTransformPage extends IntegrationEntityPage {
    public XsltTransformPage(XsltTransform entity) {
        super(new IntegrationEntityPageModel(entity));
        setUseScroll(false);
    }
    @Override protected IntegrationEntityPanel createDataPanel() {
        final XsltTransform entity = (XsltTransform)getObject();
        return new IntegrationEntityPanel(getWindow(), entity) {
            private final JTextArea stylesheet = new JTextArea(entity.getXslt(), 25, 90);
            {
                stylesheet.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
                stylesheet.setTabSize(2);
                TextInputField2 field = new TextInputField2("XSLT stylesheet", stylesheet) {
                    @Override public boolean isInputValid(String value) { return value != null && !value.trim().isEmpty(); }
                };
                addInputField(field, field, field);
                JButton load = new JButton("Load stylesheet...");
                load.addActionListener(event -> {
                    JFileChooser chooser = new JFileChooser();
                    if (chooser.showOpenDialog(getUi()) == JFileChooser.APPROVE_OPTION) {
                        try { stylesheet.setText(new String(Files.readAllBytes(chooser.getSelectedFile().toPath()), StandardCharsets.UTF_8)); }
                        catch (Exception failure) { JOptionPane.showMessageDialog(getUi(), failure.getMessage(), "Load stylesheet", JOptionPane.ERROR_MESSAGE); }
                    }
                });
                addComponentAfterNameField(load);
                getDefaultLayout().add(new JScrollPane(stylesheet), true);
            }
            @Override protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
                try { XsltTransform.validateStylesheet(stylesheet.getText()); }
                catch (Exception failure) { throw new IntegrationDataPanelException(failure); }
                entity.setXslt(stylesheet.getText());
            }
        };
    }
}
