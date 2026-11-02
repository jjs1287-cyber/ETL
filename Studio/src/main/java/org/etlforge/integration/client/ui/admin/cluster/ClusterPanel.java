package org.etlforge.integration.client.ui.admin.cluster;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.etlforge.integration.client.ui.admin.audit.AuditLogPanel;

public final class ClusterPanel extends JPanel {
 private final AuditLogPanel.Service service;
 private final ObjectMapper json=new ObjectMapper();
 private final JTextField clusterName=new JTextField(24),nodeName=new JTextField(24),address=new JTextField(32);
 private final JLabel clusterId=new JLabel("Not loaded"),mode=new JLabel("Connect to load multi-server configuration."),status=new JLabel("Not connected");
 private final JSpinner timeout=new JSpinner(new SpinnerNumberModel(60,30,600,5));
 private final JButton refresh=new JButton("Refresh"),save=new JButton("Save Configuration"),saveNode=new JButton("Save Node Address");
 private final DefaultTableModel model=new DefaultTableModel(new String[]{"Node","Hostname","API address","Build","Scheduler","Last heartbeat","Node ID"},0){public boolean isCellEditable(int row,int col){return false;}};
 private final JTable table=new JTable(model);
 private JsonNode nodes;
 private long revision;
 private boolean connected,busy,loaded;
 private int generation;
 public ClusterPanel(AuditLogPanel.Service service){
  super(new BorderLayout(10,10));this.service=service;setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
  JPanel top=new JPanel();top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
  JLabel title=new JLabel("Multi-server Configuration");title.setFont(title.getFont().deriveFont(20f));top.add(title);top.add(mode);
  top.add(new JLabel("Database registration and scheduler heartbeats. A scheduler heartbeat does not prove the worker or API is healthy."));
  JPanel config=new JPanel(new GridBagLayout());row(config,0,"Cluster name",clusterName);row(config,1,"Cluster ID",clusterId);row(config,2,"Scheduler heartbeat timeout (seconds)",timeout);
  JPanel buttons=new JPanel(new FlowLayout(FlowLayout.LEFT));buttons.add(save);buttons.add(refresh);row(config,3,"",buttons);top.add(config);add(top,BorderLayout.NORTH);
  table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);table.setAutoCreateRowSorter(true);table.setFillsViewportHeight(true);add(new JScrollPane(table),BorderLayout.CENTER);
  JPanel lower=new JPanel(new BorderLayout());JPanel editor=new JPanel(new GridBagLayout());row(editor,0,"Selected node name",nodeName);row(editor,1,"Advertised API address",address);row(editor,2,"",saveNode);lower.add(editor,BorderLayout.NORTH);
  JPanel notes=new JPanel();notes.setLayout(new BoxLayout(notes,BoxLayout.Y_AXIS));notes.add(new JLabel("Example: http://192.168.1.20:47100. Saving an address does not change the listener, firewall or Studio connection."));
  notes.add(new JLabel("Each computer needs a unique [Cluster] NodeId in etlforge.conf. Registering a node does not enable active-active execution."));notes.add(status);lower.add(notes,BorderLayout.SOUTH);add(lower,BorderLayout.SOUTH);
  table.getSelectionModel().addListSelectionListener(e->selected());refresh.addActionListener(e->start());save.addActionListener(e->save());saveNode.addActionListener(e->saveNode());controls();
 }
 private static void row(JPanel panel,int index,String text,JComponent component){GridBagConstraints c=new GridBagConstraints();c.gridx=0;c.gridy=index;c.insets=new Insets(4,6,4,6);c.anchor=GridBagConstraints.WEST;panel.add(new JLabel(text),c);c.gridx=1;c.weightx=1;c.fill=GridBagConstraints.HORIZONTAL;panel.add(component,c);}
 private void controls(){refresh.setEnabled(connected&&!busy);save.setEnabled(connected&&!busy&&loaded);clusterName.setEnabled(connected&&!busy&&loaded);timeout.setEnabled(connected&&!busy&&loaded);boolean selected=connected&&!busy&&loaded&&table.getSelectedRow()>=0;saveNode.setEnabled(selected);nodeName.setEnabled(selected);address.setEnabled(selected);}
 public void disconnect(){generation++;connected=false;busy=false;loaded=false;clusterId.setText("Not loaded");mode.setText("Connect to load multi-server configuration.");model.setRowCount(0);nodes=null;nodeName.setText("");address.setText("");status.setText("Connect to an administrator account to manage multi-server settings.");controls();}
 public void start(){connected=true;ObjectNode request=json.createObjectNode();request.put("action","cluster.settings");send(request);}
 private void selected(){int row=table.getSelectedRow();if(row>=0&&nodes!=null){JsonNode node=nodes.get(table.convertRowIndexToModel(row));nodeName.setText(node.path("displayName").asText());address.setText(node.path("apiUrl").asText());}controls();}
 private void save(){ObjectNode request=json.createObjectNode();request.put("action","cluster.save");request.put("revision",revision);request.put("clusterName",clusterName.getText().trim());request.put("heartbeatTimeout",((Number)timeout.getValue()).intValue());send(request);}
 private void saveNode(){int row=table.getSelectedRow();if(row<0)return;ObjectNode request=json.createObjectNode();request.put("action","cluster.node.save");request.put("nodeId",nodes.get(table.convertRowIndexToModel(row)).path("nodeId").asText());request.put("displayName",nodeName.getText().trim());request.put("apiUrl",address.getText().trim());send(request);}
 private void send(ObjectNode request){busy=true;controls();status.setText("Contacting server...");final int ticket=++generation;
  new SwingWorker<JsonNode,Void>(){
   protected JsonNode doInBackground()throws Exception{return service.request(request);}
   protected void done(){if(ticket!=generation)return;busy=false;
    try{JsonNode result=get();loaded=true;revision=result.path("revision").asLong();clusterName.setText(result.path("clusterName").asText());clusterId.setText(result.path("clusterId").asText());timeout.setValue(result.path("heartbeatTimeout").asInt(60));mode.setText(result.path("mode").asText());nodes=result.path("nodes");model.setRowCount(0);
     for(JsonNode n:nodes)model.addRow(new Object[]{n.path("displayName").asText(),n.path("hostname").asText(),n.path("apiUrl").asText(),n.path("build").asText(),n.path("schedulerStatus").asText(),n.path("heartbeat").asText(),n.path("nodeId").asText()});
     status.setText(nodes.size()+" registered node(s). Click Refresh for current scheduler status.");if(model.getRowCount()>0)table.setRowSelectionInterval(0,0);
    }catch(Exception e){Throwable cause=e;while(cause.getCause()!=null)cause=cause.getCause();status.setText("Multi-server: "+cause.getMessage());}controls();}
  }.execute();
 }
}
