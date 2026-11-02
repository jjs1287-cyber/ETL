package org.etlforge.integration.client.ui.admin.audit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class AuditLogPanel extends JPanel {
    public interface Service {
        JsonNode request(ObjectNode request) throws Exception;
    }
    private static final ObjectMapper JSON = new ObjectMapper();
    private final Service service;
    private final JSpinner detailDays = new JSpinner(new SpinnerNumberModel(30, 1, 36500, 1));
    private final JSpinner headerDays = new JSpinner(new SpinnerNumberModel(365, 1, 36500, 1));
    private final JCheckBox retention = new JCheckBox("Enable automatic cleanup");
    private final JButton save = new JButton("Save Settings");
    private final JButton refresh = new JButton("Refresh");
    private final JButton previous = new JButton("Previous");
    private final JButton next = new JButton("Next");
    private final JComboBox<String> project = new JComboBox<>();
    private final JComboBox<String> type = new JComboBox<>(new String[]{"All", "Source", "Target", "Transformation", "Operation", "Script", "XSLT", "Project Variable", "File Format", "Schedule", "Web Service", "Hosted HTTP Endpoint"});
    private final int[] typeIds = {0,2,3,4,6,23,34,35,17,5,10,24};
    private final JComboBox<String> dates = new JComboBox<>(new String[]{"Last 7 days", "Last 30 days", "Last 90 days", "Last 365 days", "All retained history"});
    private final int[] dayValues = {7,30,90,365,36500};
    private final JTextField search = new JTextField(18);
    private final JLabel status = new JLabel("Only changed deployments are recorded. Unchanged saves and redeployments are ignored.");
    private final JLabel pageLabel = new JLabel("Page 1");
    private final JLabel captureStatus = new JLabel("Connecting to the audit service...");
    private final JLabel selected = new JLabel("Select an audit entry to see its previous and current values.");
    private final DefaultTableModel events = model("Object Name", "Object Type", "Action", "Changed By", "Deployed At");
    private final JTable eventTable = new JTable(events);
    private final DefaultTableModel oldValues = model("Field", "Previous Value");
    private final DefaultTableModel newValues = model("Field", "Current Value");
    private final JTable oldTable = new JTable(oldValues), newTable = new JTable(newValues);
    private final List<JsonNode> entries = new ArrayList<>();
    private final List<String> projectGuids = new ArrayList<>();
    private final List<Boolean> changedRows = new ArrayList<>();
    private int page, requestGeneration, detailGeneration;
    private boolean loading, settingsEditable, captureEnabled=true;

    public AuditLogPanel(Service service) {
        super(new BorderLayout(8,8));
        this.service = service;
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        JPanel top = new JPanel();
        top.setLayout(new javax.swing.BoxLayout(top,javax.swing.BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Audit Log");
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD,20f));
        top.add(title);
        top.add(captureStatus);
        search.setBackground(new Color(30,30,30));search.setForeground(Color.WHITE);search.setDisabledTextColor(new Color(220,220,220));
        for(JComboBox<String> combo:java.util.Arrays.asList(project,type,dates)) {
            combo.setRenderer(new javax.swing.DefaultListCellRenderer(){
                @Override public Component getListCellRendererComponent(javax.swing.JList<?> list,Object value,int index,boolean selected,boolean focus){
                    Component label=super.getListCellRendererComponent(list,value,index,selected,focus);
                    label.setForeground(Color.WHITE);if(!selected)label.setBackground(new Color(30,30,30));return label;
                }
            });
        }
        JPanel settings = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
        settings.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(128,128,128)), "Retention settings"));
        settings.add(new JLabel("Keep detail (days):")); settings.add(detailDays);
        settings.add(new JLabel("Keep headers (days):")); settings.add(headerDays);
        settings.add(retention); settings.add(save);
        top.add(settings);
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        project.addItem("All permitted projects"); projectGuids.add("");
        dates.setSelectedIndex(1);
        filters.add(new JLabel("Project:")); filters.add(project);
        filters.add(new JLabel("Object type:")); filters.add(type);
        filters.add(new JLabel("Date:")); filters.add(dates);
        top.add(filters);
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        searchBar.add(new JLabel("Object name:")); searchBar.add(search); searchBar.add(refresh);
        top.add(searchBar);
        add(top,BorderLayout.NORTH);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventTable.setAutoCreateRowSorter(false);
        eventTable.setFillsViewportHeight(true);
        eventTable.setRowHeight(24);
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(240);
        eventTable.getColumnModel().getColumn(4).setPreferredWidth(190);
        eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for(int i=1;i<4;i++)eventTable.getColumnModel().getColumn(i).setPreferredWidth(140);
        JPanel history = new JPanel(new BorderLayout());
        history.add(scroll(eventTable),BorderLayout.CENTER);
        JPanel paging = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        paging.add(pageLabel); paging.add(previous); paging.add(next); history.add(paging,BorderLayout.SOUTH);
        JPanel details = new JPanel(new BorderLayout(6,6));
        details.add(selected,BorderLayout.NORTH);
        oldTable.setFillsViewportHeight(true); newTable.setFillsViewportHeight(true);
        oldTable.setRowHeight(24); newTable.setRowHeight(24);
        installValueViewer(oldTable,"Previous Value"); installValueViewer(newTable,"Current Value");
        oldTable.setDefaultRenderer(Object.class,new DifferenceRenderer(false));
        newTable.setDefaultRenderer(Object.class,new DifferenceRenderer(true));
        JSplitPane values = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll(oldTable),scroll(newTable));
        values.setBorder(BorderFactory.createLineBorder(new Color(128,128,128)));
        values.setResizeWeight(0.5); details.add(values,BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,history,details);
        split.setBorder(BorderFactory.createLineBorder(new Color(128,128,128)));
        split.setResizeWeight(0.5); split.setDividerLocation(270); add(split,BorderLayout.CENTER);
        for(JSplitPane pane:new JSplitPane[]{values,split}) {
            pane.setContinuousLayout(true);pane.setDividerSize(7);
            if(pane.getUI() instanceof javax.swing.plaf.basic.BasicSplitPaneUI){
                javax.swing.plaf.basic.BasicSplitPaneDivider divider=((javax.swing.plaf.basic.BasicSplitPaneUI)pane.getUI()).getDivider();
                divider.setBackground(new Color(100,100,100));
                divider.setBorder(BorderFactory.createLineBorder(new Color(128,128,128)));
            }
        }
        add(status,BorderLayout.SOUTH);
        previous.setEnabled(false); next.setEnabled(false); setSettingsEditable(false);
        refresh.addActionListener(e -> load(0)); search.addActionListener(e -> load(0));
        previous.addActionListener(e -> load(Math.max(0,page-1))); next.addActionListener(e -> load(page+1));
        save.addActionListener(e -> saveSettings());
        project.addActionListener(e -> filterChanged());
        type.addActionListener(e -> filterChanged()); dates.addActionListener(e -> filterChanged());
        detailDays.setToolTipText("Days to retain previous and current object values.");
        headerDays.setToolTipText("Days to retain who changed each object and when it was deployed.");
        retention.setToolTipText("Cleanup removes expired audit history; object comparison baselines are preserved.");
        eventTable.getSelectionModel().addListSelectionListener(e -> {if(!e.getValueIsAdjusting())loadDetail();});
    }
    private boolean connected, populating;
    private void filterChanged(){if(connected && !populating && !loading)load(0);}
    private static void installValueViewer(JTable table,String title) {
        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(420);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setToolTipText("Double-click a field to view and copy its complete value.");
        table.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseClicked(java.awt.event.MouseEvent event){
                int row=table.rowAtPoint(event.getPoint());
                if(event.getClickCount()!=2 || row<0)return;
                javax.swing.JTextArea text=new javax.swing.JTextArea(String.valueOf(table.getValueAt(row,1)),18,70);
                text.setEditable(false);text.setLineWrap(true);text.setWrapStyleWord(true);text.setCaretPosition(0);
                javax.swing.JOptionPane.showMessageDialog(table,new JScrollPane(text),title+" — "+table.getValueAt(row,0),javax.swing.JOptionPane.PLAIN_MESSAGE);
            }
        });
    }
    private static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns,0) { @Override public boolean isCellEditable(int row,int col){return false;} };
    }
    private static JScrollPane scroll(JTable table) {
        JScrollPane scroll=new JScrollPane(table);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable owner,Object value,boolean selected,boolean focused,int row,int column){
                super.getTableCellRendererComponent(owner,value,false,false,row,column);
                setBackground(new Color(45,45,45));setForeground(Color.WHITE);
                setBorder(BorderFactory.createMatteBorder(0,0,1,1,new Color(128,128,128)));
                return this;
            }
        });
        scroll.setColumnHeaderView(table.getTableHeader());
        scroll.setBorder(BorderFactory.createLineBorder(new Color(128,128,128)));
        return scroll;
    }
    private ObjectNode request(String action) {return JSON.createObjectNode().put("action",action);}
    public void start() {
        if(service==null){unavailable("Audit capture is not enabled on this server.");return;}
        connected=true;loading=false;setFiltersEnabled(false);
        status.setText("Loading audit settings...");
        final int generation=++requestGeneration;
        background(() -> service.request(request("settings")), settings -> {
            if(generation!=requestGeneration)return;
            detailDays.setValue(settings.path("detailDays").asInt(30));
            headerDays.setValue(settings.path("headerDays").asInt(365));
            retention.setSelected(settings.path("retentionEnabled").asBoolean());
            setSettingsEditable(settings.path("canManageSettings").asBoolean());
            boolean capture=settings.path("captureEnabled").asBoolean(true);
            captureEnabled=capture;
            captureStatus.setText(capture?"Capture enabled for: "+settings.path("captureScope").asText("supported objects")+". Other object types are not yet captured.":"Connected. Deployment capture is not installed yet; new changes are not being recorded.");
            if(!capture){retention.setEnabled(false);retention.setToolTipText("Automatic cleanup is unavailable until the server cleanup integration is installed.");}
            populating=true;project.removeAllItems(); projectGuids.clear();
            project.addItem("All permitted projects"); projectGuids.add("");
            for(JsonNode item: settings.path("projects")) {project.addItem(item.path("name").asText());projectGuids.add(item.path("guid").asText());}
            populating=false;setFiltersEnabled(true);
            loading=false;
            load(0);
        },generation,false);
    }
    public void disconnect() {
        connected=false;populating=true;
        ++requestGeneration; ++detailGeneration;
        entries.clear(); events.setRowCount(0); clearDetails();
        project.removeAllItems(); project.addItem("All permitted projects"); projectGuids.clear(); projectGuids.add("");
        loading=false; refresh.setEnabled(false); previous.setEnabled(false); next.setEnabled(false); setSettingsEditable(false);
        status.setText("Disconnected. Audit data has been cleared.");
        captureStatus.setText("Disconnected. Connect to a server to load projects and audit settings.");
        populating=false;setFiltersEnabled(false);page=0;pageLabel.setText("No audit history loaded");
    }
    public void unavailable(String message){disconnect();status.setText(message);status.setToolTipText(message);selected.setText("Audit history will appear here after server audit capture is enabled.");}
    private void setFiltersEnabled(boolean enabled){project.setEnabled(enabled);type.setEnabled(enabled);dates.setEnabled(enabled);search.setEnabled(enabled);}
    private void setSettingsEditable(boolean editable) {
        settingsEditable=editable; save.setEnabled(editable); detailDays.setEnabled(editable);headerDays.setEnabled(editable);retention.setEnabled(editable);
        for(JSpinner spinner:new JSpinner[]{detailDays,headerDays}) {
            javax.swing.JFormattedTextField field=((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
            field.setBackground(new Color(30,30,30));field.setForeground(Color.WHITE);
            field.setDisabledTextColor(new Color(220,220,220));
        }
    }
    private void load(int requestedPage) {
        if(loading || !connected)return;
        setFiltersEnabled(false);
        loading=true;refresh.setEnabled(false);previous.setEnabled(false);next.setEnabled(false);
        ++detailGeneration; clearDetails();
        final int generation=++requestGeneration;
        ObjectNode payload=request("list").put("days",dayValues[dates.getSelectedIndex()]).put("objectType",typeIds[type.getSelectedIndex()])
            .put("search",search.getText()).put("offset",requestedPage*50).put("limit",50);
        int projectIndex=project.getSelectedIndex();
        payload.put("projectGuid",projectIndex>=0 ? projectGuids.get(projectIndex) : "");
        status.setText("Loading audit history...");
        background(() -> service.request(payload), response -> {
            if(generation!=requestGeneration)return;
            entries.clear();events.setRowCount(0);
            for(JsonNode entry:response.path("entries")) {
                entries.add(entry);
                events.addRow(new Object[]{entry.path("objectName").asText(),entry.path("objectTypeName").asText(),entry.path("action").asText(),entry.path("userName").asText(),localTime(entry.path("deployedAt").asText())});
            }
            page=requestedPage;pageLabel.setText(entries.size()+" entries — Page "+(page+1));
            previous.setEnabled(page>0);next.setEnabled(response.path("hasMore").asBoolean());
            status.setText(entries.isEmpty()?(captureEnabled?"No changes match these filters.":"No stored audit entries. Deployment capture is not enabled yet."):"Select an entry to view its previous and current values.");
        },generation,false);
    }
    private void saveSettings() {
        if(!settingsEditable)return;
        try {detailDays.commitEdit();headerDays.commitEdit();}catch(java.text.ParseException e){status.setText("Enter whole numbers for retention days.");return;}
        int detail=((Number)detailDays.getValue()).intValue(), header=((Number)headerDays.getValue()).intValue();
        if(header<detail){status.setText("Headers must be kept at least as long as details.");return;}
        save.setEnabled(false);
        final int generation=requestGeneration;
        ObjectNode payload=request("saveSettings").put("detailDays",detail).put("headerDays",header).put("retentionEnabled",retention.isSelected());
        background(() -> service.request(payload), result -> {if(generation==requestGeneration)status.setText("Audit retention settings saved.");},generation,false);
    }
    private void loadDetail() {
        final int generation=++detailGeneration;
        int row=eventTable.getSelectedRow();clearDetails();
        if(row<0 || row>=entries.size())return;
        JsonNode entry=entries.get(row);
        selected.setText(entry.path("objectName").asText()+" — "+entry.path("action").asText()+"   |   "+entry.path("userName").asText()+"   |   "+localTime(entry.path("deployedAt").asText()));
        background(() -> service.request(request("detail").put("eventId",entry.path("id").asLong())), result -> {
            if(generation!=detailGeneration)return;
            if(!result.path("detailAvailable").asBoolean()){status.setText("Details have expired under the retention policy. The audit header is still available.");return;}
            Map<String,String> before=new LinkedHashMap<>(),after=new LinkedHashMap<>();
            flatten(result.get("previous"),"",before); flatten(result.get("current"),"",after);
            List<String> paths=new ArrayList<>(before.keySet());for(String path:after.keySet())if(!before.containsKey(path))paths.add(path);Collections.sort(paths);
            for(String path:paths) {
                boolean changed=false;
                for(JsonNode item:result.path("changedPaths")) {String change=item.asText();if(change.equals("/") || path.equals(change) || path.startsWith(change+"/"))changed=true;}
                changedRows.add(changed);
                oldValues.addRow(new Object[]{path,before.containsKey(path)?before.get(path):"—"});
                newValues.addRow(new Object[]{path,after.containsKey(path)?after.get(path):"—"});
            }
            status.setText("Changed fields are highlighted. Double-click a field to view and copy its complete value. Secrets are redacted.");
        },generation,true);
    }
    private void clearDetails(){oldValues.setRowCount(0);newValues.setRowCount(0);changedRows.clear();selected.setText("Select an audit entry to see its previous and current values.");}
    private static void flatten(JsonNode node,String path,Map<String,String> output) {
        if(node==null || (path.isEmpty() && node.isNull()))return;
        if(node.isObject() && node.size()>0) {
            List<String> names=new ArrayList<>();node.fieldNames().forEachRemaining(names::add);Collections.sort(names);
            for(String name:names)flatten(node.get(name),path+"/"+name.replace("~","~0").replace("/","~1"),output);
        } else if(node.isArray() && node.size()>0) {for(int i=0;i<node.size();++i)flatten(node.get(i),path+"/"+i,output);}
        else output.put(path.isEmpty()?"/":path,node.isTextual()?node.textValue():node.toString());
    }
    private static String localTime(String value) {
        try{return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()).format(Instant.parse(value));}catch(RuntimeException e){return value;}
    }
    private void background(Callable<JsonNode> work,Consumer<JsonNode> success,int generation,boolean detail) {
        new SwingWorker<JsonNode,Void>() {
            @Override protected JsonNode doInBackground() throws Exception {return work.call();}
            @Override protected void done() {
                if(generation!=(detail?detailGeneration:requestGeneration))return;
                try{success.accept(get());}catch(Exception e){status.setText("Audit request failed. Check the connection and application logs.");}
                finally{if(!detail && generation==requestGeneration){loading=false;setFiltersEnabled(connected);refresh.setEnabled(connected);save.setEnabled(settingsEditable && connected);previous.setEnabled(connected && page>0);}}
            }
        }.execute();
    }
    private final class DifferenceRenderer extends DefaultTableCellRenderer {
        private final boolean current;
        DifferenceRenderer(boolean current){this.current=current;}
        @Override public Component getTableCellRendererComponent(JTable table,Object value,boolean selected,boolean focused,int row,int col) {
            Component component=super.getTableCellRendererComponent(table,value,selected,focused,row,col);
            if(!selected) {boolean changed=row<changedRows.size() && changedRows.get(row);component.setBackground(changed?(current?new Color(24,65,35):new Color(70,30,30)):table.getBackground());component.setForeground(table.getForeground());}
            return component;
        }
    }
}
