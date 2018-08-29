import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JTextArea;

public class GUI {
	
	JFrame jFrame = new JFrame("FTPTransfer");
	JTextArea textArea = new JTextArea();	
	JScrollPane jsPane = new JScrollPane(textArea);
	private JTextField textFieldServerReceiving;
	private JTextField textFieldClientReceiving;
	private JTextField textFieldClientSending;
	private JTextField textFieldServerIP;

	public GUI() {
		jFrame.setSize(406, 650);
		jFrame.getContentPane().setLayout(null);
		jFrame.setLocationRelativeTo(null);
		
		JLabel lblServer = new JLabel("Server");
		lblServer.setBounds(10, 68, 46, 14);
		jFrame.getContentPane().add(lblServer);
		jsPane.setBounds(10, 359, 370, 241);
		jFrame.getContentPane().add(jsPane);
		jsPane.setViewportView(textArea);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		textArea.setEditable(false);
		textArea.setText("");
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(220, 324, 89, 23);
		jFrame.getContentPane().add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.setBounds(84, 324, 89, 23);
		jFrame.getContentPane().add(btnStop);
		
		JRadioButton rdbtnClient = new JRadioButton("Client");
		rdbtnClient.setBounds(47, 38, 57, 23);
		jFrame.getContentPane().add(rdbtnClient);
		
		JRadioButton rdbtnServer = new JRadioButton("Server");
		rdbtnServer.setSelected(true);
		rdbtnServer.setBounds(47, 16, 57, 23);
		jFrame.getContentPane().add(rdbtnServer);
		
		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(rdbtnServer);
		bGroup.add(rdbtnClient);
		
		JLabel lblMode = new JLabel("Mode");
		lblMode.setBounds(10, 20, 46, 14);
		jFrame.getContentPane().add(lblMode);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(UIManager.getColor("scrollbar"), 1, true));
		panel.setBounds(10, 86, 370, 47);
		jFrame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblLocalDirectory = new JLabel("Receiving Directory");
		lblLocalDirectory.setBounds(10, 14, 94, 14);
		panel.add(lblLocalDirectory);
		
		textFieldServerReceiving = new JTextField();
		textFieldServerReceiving.setBounds(114, 11, 246, 20);
		panel.add(textFieldServerReceiving);
		textFieldServerReceiving.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new LineBorder(UIManager.getColor("scrollbar"), 1, true));
		panel_1.setBounds(10, 163, 370, 131);
		jFrame.getContentPane().add(panel_1);
		
		JLabel lblReceiveDir = new JLabel("Receiving Directory");
		lblReceiveDir.setBounds(10, 48, 94, 14);
		panel_1.add(lblReceiveDir);
		
		textFieldClientReceiving = new JTextField();
		textFieldClientReceiving.setEnabled(false);
		textFieldClientReceiving.setColumns(10);
		textFieldClientReceiving.setBounds(114, 45, 246, 20);
		panel_1.add(textFieldClientReceiving);
		
		JLabel lblSendDir = new JLabel("Sending Directory");
		lblSendDir.setBounds(10, 14, 94, 14);
		panel_1.add(lblSendDir);
		
		textFieldClientSending = new JTextField();
		textFieldClientSending.setEnabled(false);
		textFieldClientSending.setColumns(10);
		textFieldClientSending.setBounds(114, 11, 246, 20);
		panel_1.add(textFieldClientSending);
		
		JLabel lblServerIp = new JLabel("Server IP");
		lblServerIp.setBounds(10, 79, 46, 14);
		panel_1.add(lblServerIp);
		
		textFieldServerIP = new JTextField();
		textFieldServerIP.setEditable(false);
		textFieldServerIP.setBounds(114, 76, 246, 20);
		panel_1.add(textFieldServerIP);
		textFieldServerIP.setColumns(10);
		
		JLabel lblClient = new JLabel("Client");
		lblClient.setBounds(10, 144, 46, 14);
		jFrame.getContentPane().add(lblClient);
		//bGroup.setSelected(true);
		
		jFrame.setVisible(true);
	}
	
	public void print(String input) {
		textArea.append(input);
	}
}
