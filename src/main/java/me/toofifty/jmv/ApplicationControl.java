package me.toofifty.jmv;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import me.toofifty.jmv.gui.QuickLoad;

import org.lwjgl.opengl.Display;

import java.awt.Canvas;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JSplitPane;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSeparator;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.JSlider;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import me.toofifty.jmv.ModelElement.Axis;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import java.awt.SystemColor;

public class ApplicationControl extends JFrame {

	private JPanel contentPane;
	private Canvas canvas;
	private JTextField txtBlockscubeall;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField txtMainblock;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Create the frame.
	 */
	public ApplicationControl() {
		super("JSON Model Editor");
		setTitle("JSON Model Editor - assets\\minecraft\\models\\block\\birch_log.json");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1250, 929);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New...");
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open...");
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mnFile.add(mntmSaveAs);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnFile.add(mntmSettings);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmQuickLoad = new JMenuItem("Quick load");
		mntmQuickLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame popup = new QuickLoad();
			}
		});
		mnTools.add(mntmQuickLoad);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmToggleFloor = new JMenuItem("Toggle floor");
		mntmToggleFloor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONModelViewer.instance.toggleFloor();
			}
		});
		mnView.add(mntmToggleFloor);
		
		JMenuItem mntmIsometric = new JMenuItem("Isometric");
		mntmIsometric.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONModelViewer.instance.setIso();
			}
		});
		mnView.add(mntmIsometric);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		canvas = new Canvas();
		canvas.setVisible(true);
		contentPane.add(canvas, BorderLayout.CENTER);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(200, 5));
		contentPane.add(tabbedPane, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Model", null, panel, null);
		
		JLabel lblParentModel = new JLabel("Parent model");
		
		txtBlockscubeall = new JTextField();
		txtBlockscubeall.setText("blocks/cube_all");
		txtBlockscubeall.setFont(new Font("Monospaced", Font.PLAIN, 11));
		txtBlockscubeall.setColumns(10);
		
		JButton btnLoadParent = new JButton("Load parent...");
		
		JSeparator separator = new JSeparator();
		
		JCheckBox chckbxAmbientOcclusion = new JCheckBox("Ambient Occlusion");
		chckbxAmbientOcclusion.setSelected(true);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setToolTipText("Textures");
		
		JLabel lblTextures = new JLabel("Textures");
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textField_1.setColumns(10);
		
		JLabel lblParticle = new JLabel("\"particle\"");
		
		JButton btnAddTexture = new JButton("Add texture...");
		
		JButton button = new JButton("...");
		
		JLabel lblmain = new JLabel("\"main\"");
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textField_2.setColumns(10);
		
		JButton button_1 = new JButton("...");
		
		JSeparator separator_3 = new JSeparator();
		
		JButton btnAddElement = new JButton("New");
		
		JList list = new JList();
		list.setBackground(SystemColor.menu);
		list.setBorder(new TitledBorder(null, "Elements", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		list.setFont(new Font("Monospaced", Font.PLAIN, 11));
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"main_block"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnDelete = new JButton("Delete");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(separator, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
						.addComponent(txtBlockscubeall, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(lblParentModel)
							.addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
							.addComponent(btnLoadParent))
						.addComponent(chckbxAmbientOcclusion, Alignment.TRAILING)
						.addComponent(separator_2, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(lblTextures)
							.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
							.addComponent(btnAddTexture))
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblParticle)
								.addComponent(lblmain))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(button, GroupLayout.PREFERRED_SIZE, 33, Short.MAX_VALUE)
								.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 33, Short.MAX_VALUE)))
						.addComponent(separator_3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
						.addComponent(list, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnAddElement)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEdit)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnDelete)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblParentModel)
						.addComponent(btnLoadParent))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtBlockscubeall, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxAmbientOcclusion)
					.addGap(7)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTextures)
						.addComponent(btnAddTexture))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblParticle)
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblmain)
						.addComponent(button_1))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_3, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(list, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAddElement)
						.addComponent(btnEdit)
						.addComponent(btnDelete))
					.addContainerGap(444, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Element", null, panel_1, null);
		
		JLabel lblElementName = new JLabel("Element name");
		
		txtMainblock = new JTextField();
		txtMainblock.setFont(new Font("Monospaced", Font.PLAIN, 11));
		txtMainblock.setText("main_block");
		txtMainblock.setColumns(10);
		
		JSeparator separator_4 = new JSeparator();
		
		JLabel lblFrom = new JLabel("from");
		
		JButton btnSetFrom = new JButton("Set");
		btnSetFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JLabel lblTo = new JLabel("to");
		
		JButton btnSet = new JButton("Set");
		
		JCheckBox chckbxShade = new JCheckBox("Shade");
		chckbxShade.setSelected(true);
		
		JSeparator separator_5 = new JSeparator();
		
		JLabel lblRotation = new JLabel("Rotation");
		
		JLabel lblOrigin = new JLabel("Origin");
		
		JButton btnSet_1 = new JButton("Set");
		
		JLabel lblAxis = new JLabel("Axis");
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(Axis.values()));
		
		JSlider slider = new JSlider();
		slider.setMinorTickSpacing(22);
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setValue(0);
		slider.setMinimum(-45);
		slider.setMaximum(45);
		slider.setMajorTickSpacing(45);
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textField_5.setColumns(10);
		
		textField_6 = new JTextField();
		textField_6.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textField_6.setColumns(10);
		
		JSeparator separator_6 = new JSeparator();
		
		JCheckBox chckbxRescale = new JCheckBox("Rescale");
		
		JList list_1 = new JList();
		list_1.setFont(new Font("Monospaced", Font.PLAIN, 11));
		list_1.setModel(new AbstractListModel() {
			String[] values = new String[] {"north", "south", "east", "west", "up", "down"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_1.setBackground(SystemColor.menu);
		list_1.setBorder(new TitledBorder(null, "Faces", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton btnNew_1 = new JButton("New");
		
		JButton btnEdit_2 = new JButton("Edit");
		
		JButton btnDelete_1 = new JButton("Delete");
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(list_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
						.addComponent(separator_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
						.addComponent(separator_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
						.addComponent(chckbxShade, Alignment.LEADING)
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addComponent(lblElementName)
							.addGap(18)
							.addComponent(txtMainblock, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addComponent(lblRotation)
							.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
							.addComponent(chckbxRescale))
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addComponent(lblOrigin)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_6, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSet_1))
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addComponent(lblAxis)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
							.addComponent(slider, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE))
						.addComponent(separator_6, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(lblFrom)
								.addComponent(lblTo))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(textField_5, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSet))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(textField_4, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSetFrom))))
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addComponent(btnNew_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEdit_2)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDelete_1)))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblElementName)
						.addComponent(txtMainblock, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(7)
					.addComponent(chckbxShade)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_4, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFrom)
						.addComponent(btnSetFrom)
						.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTo)
						.addComponent(btnSet)
						.addComponent(textField_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(13)
					.addComponent(separator_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRotation)
						.addComponent(chckbxRescale))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOrigin)
						.addComponent(btnSet_1)
						.addComponent(textField_6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblAxis)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(list_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnEdit_2)
						.addComponent(btnDelete_1)
						.addComponent(btnNew_1))
					.addContainerGap(404, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Face", null, panel_2, null);
		
		addComponentListener(new ComponentListener(){
			public void componentResized(ComponentEvent e) {
				JSONModelViewer.instance.scheduleResize();
			}

			public void componentHidden(ComponentEvent e) { }
			public void componentMoved(ComponentEvent e) { }
			public void componentShown(ComponentEvent e) { }
		});
		
		//pack();
		//setSize(400, 400);
		setVisible(true);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
}
