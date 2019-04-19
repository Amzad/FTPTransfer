import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBox;

public class GUI implements Runnable {
	
	JFrame jFrame = new JFrame("FTPTransfer");
	static JTextArea textArea = new JTextArea();	
	JScrollPane jsPane = new JScrollPane(textArea);
	private static JTextField textFieldServerReceiving;
	private static JTextField textFieldClientReceiving;
	private static JTextField textFieldClientSending;
	private static JTextField textFieldServerIP;
	ButtonGroup bGroup;
	private static JRadioButton rdbtnServer;
	private static JRadioButton rdbtnClient;
	private static JButton btnStop;
	public static JButton btnStart;
	private static JCheckBox chckbxEnableTranscoding;
	final JFileChooser fc = new JFileChooser();


	public GUI() {
		jFrame.setTitle("AnimeAIBO");
		jFrame.setResizable(false);
		jFrame.setSize(396, 650);
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
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startButton();				
			}
		});
		btnStart.setBounds(220, 324, 89, 23);
		jFrame.getContentPane().add(btnStart);
		
		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stopButton();
			}
		});
		btnStop.setEnabled(false);
		btnStop.setBounds(84, 324, 89, 23);
		jFrame.getContentPane().add(btnStop);
		
		rdbtnClient = new JRadioButton("Client");
		rdbtnClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnClient.isSelected()) {
					textFieldServerReceiving.setEnabled(false);
					textFieldClientReceiving.setEnabled(true);
					textFieldClientSending.setEnabled(true);
					textFieldServerIP.setEnabled(true);
				}
			}
		});
		rdbtnClient.setBounds(47, 38, 57, 23);
		jFrame.getContentPane().add(rdbtnClient);
		
		rdbtnServer = new JRadioButton("Server");
		rdbtnServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldServerReceiving.setEnabled(true);
				textFieldClientReceiving.setEnabled(false);
				textFieldClientSending.setEnabled(false);
				textFieldServerIP.setEnabled(false);
				
			}
		});
		rdbtnServer.setSelected(true);
		rdbtnServer.setBounds(47, 16, 57, 23);
		jFrame.getContentPane().add(rdbtnServer);
		
		bGroup = new ButtonGroup();
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
		textFieldServerReceiving.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(textFieldServerReceiving.isEnabled()) {
					chooseFolder(textFieldServerReceiving);
				}
				
			}
		});
		textFieldServerReceiving.setToolTipText("Click to select directory");
		textFieldServerReceiving.setBounds(114, 11, 246, 20);
		panel.add(textFieldServerReceiving);
		textFieldServerReceiving.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new LineBorder(UIManager.getColor("scrollbar"), 1, true));
		panel_1.setBounds(10, 163, 370, 131);
		jFrame.getContentPane().add(panel_1);
		
		JLabel lblReceiveDir = new JLabel("Receiving Directory");
		lblReceiveDir.setBounds(10, 45, 94, 14);
		panel_1.add(lblReceiveDir);
		
		textFieldClientReceiving = new JTextField();
		textFieldClientReceiving.setText("C:\\Users\\Amzad\\Documents\\Move\\");
		textFieldClientReceiving.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(textFieldClientReceiving.isEnabled()) {
					chooseFolder(textFieldClientReceiving);
				}
			}
		});
		textFieldClientReceiving.setEnabled(false);
		textFieldClientReceiving.setColumns(10);
		textFieldClientReceiving.setBounds(114, 42, 246, 20);
		panel_1.add(textFieldClientReceiving);
		
		JLabel lblSendDir = new JLabel("Sending Directory");
		lblSendDir.setBounds(10, 14, 94, 14);
		panel_1.add(lblSendDir);
		
		textFieldClientSending = new JTextField();
		textFieldClientSending.setText("C:\\Users\\Amzad\\Documents\\Watch\\");
		textFieldClientSending.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(textFieldClientSending.isEnabled()) {
					chooseFolder(textFieldClientSending);
				}
				
			}
		});
		textFieldClientSending.setEnabled(false);
		textFieldClientSending.setColumns(10);
		textFieldClientSending.setBounds(114, 11, 246, 20);
		panel_1.add(textFieldClientSending);
		
		JLabel lblServerIp = new JLabel("Server IP");
		lblServerIp.setBounds(10, 76, 46, 14);
		panel_1.add(lblServerIp);
		
		textFieldServerIP = new JTextField();
		textFieldServerIP.setText("74.91.116.201");
		textFieldServerIP.setEnabled(false);
		textFieldServerIP.setBounds(114, 73, 246, 20);
		panel_1.add(textFieldServerIP);
		textFieldServerIP.setColumns(10);
		
		JLabel lblClient = new JLabel("Client");
		lblClient.setBounds(10, 144, 46, 14);
		jFrame.getContentPane().add(lblClient);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
			}
		});
		btnClear.setBounds(10, 324, 58, 23);
		jFrame.getContentPane().add(btnClear);
		
		chckbxEnableTranscoding = new JCheckBox("Enable Transcoding");
		chckbxEnableTranscoding.setBounds(122, 38, 132, 23);
		jFrame.getContentPane().add(chckbxEnableTranscoding);
		
		JMenuBar menuBar = new JMenuBar();
		jFrame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnFtp = new JMenu("Preferences");
		menuBar.add(mnFtp);
		
		JMenuItem mntmDirectories = new JMenuItem("Directories");
		mnFtp.add(mntmDirectories);
		
		JMenuItem mntmFtp = new JMenuItem("FTP");
		mnFtp.add(mntmFtp);
		
		JMenuItem mntmMysql = new JMenuItem("MySQL");
		mnFtp.add(mntmMysql);
		
		jFrame.setVisible(true);
	}
	
	public static void print(String input) {
		textArea.append(input + "\n");
		System.out.println(input);
	}
	
	private void startButton() {
		if (rdbtnServer.isSelected()) {
			disableComponents();
			new Thread(new Server(textFieldServerReceiving.getText())).start(); // start server class
		}
		if (rdbtnClient.isSelected()) {
			if (Client.validateIP(textFieldServerIP.getText())) {
				disableComponents();
				if (chckbxEnableTranscoding.isSelected() == true) {
					new Thread(new Client(textFieldServerIP.getText(),
										textFieldClientSending.getText(), 
										textFieldClientReceiving.getText(), true)).start();
				}
				else {
					new Thread(new Client(textFieldServerIP.getText(),
							textFieldClientSending.getText(), 
							textFieldClientReceiving.getText(), false)).start();
				}
				
				textFieldClientReceiving.setEnabled(false);
				textFieldClientSending.setEnabled(false);
				textFieldServerIP.setEnabled(false);
			} else {
				print("Invalid IP Address");
			}
		}
	}
	
	public static void stopButton() {
		if (rdbtnServer.isSelected()) {
			Server.closeServer();
			//btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			textFieldServerReceiving.setEnabled(true);
			rdbtnServer.setEnabled(true);
			rdbtnClient.setEnabled(true);
		}
		if (rdbtnClient.isSelected()) {
			btnStop.setEnabled(false);
			btnStart.setEnabled(true);
			textFieldClientReceiving.setEnabled(true);
			textFieldClientSending.setEnabled(true);
			textFieldServerIP.setEnabled(true);
			rdbtnServer.setEnabled(true);
			rdbtnClient.setEnabled(true);
			chckbxEnableTranscoding.setEnabled(true);
			FolderWatcher.stop();
		}
	}
	
	private String chooseFolder(JTextField field) {
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fc.showOpenDialog(field) == JFileChooser.APPROVE_OPTION) {
			field.setText(fc.getSelectedFile().toString());
		}
		
		return null;
	}

	private void disableComponents() {
		btnStop.setEnabled(true);
		btnStart.setEnabled(false);
		textFieldServerReceiving.setEnabled(false);
		rdbtnServer.setEnabled(false);
		rdbtnClient.setEnabled(false);
		chckbxEnableTranscoding.setEnabled(false);
		
	}
	
	public static void enableComponentsServer() {
		Server.closeServer();
		//btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		textFieldServerReceiving.setEnabled(true);
		rdbtnServer.setEnabled(true);
		rdbtnClient.setEnabled(true);
		chckbxEnableTranscoding.setEnabled(true);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
