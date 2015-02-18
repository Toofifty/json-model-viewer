package me.toofifty.jmv.gui;

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

import me.toofifty.jmv.FileLoader;
import me.toofifty.jmv.JSONModelViewer;

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
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.JSlider;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;

import me.toofifty.jmv.editor.EditorAction;
import me.toofifty.jmv.model.Element;
import me.toofifty.jmv.model.Element.Axis;

import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;

import java.awt.Color;

import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import java.awt.SystemColor;
import java.awt.ScrollPane;
import java.awt.Choice;

import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.JLayeredPane;

import java.awt.event.ComponentAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class ApplicationControl extends JFrame {

	private JPanel contentPane;
	private JTextField tfParent;
	private JTextField tfElementName;
	private JTextField tfTextureName;
	private JTextField tfTextureLocation;
	private JTabbedPane tabbedPane;
	private JScrollPane spTextEditor;
	private Canvas canvas;
	private JTextPane tpTextEditor;
	private JMenuItem mntmSaveAs;
	private JMenuItem mntmSave;

	/**
	 * Create the frame.
	 */
	public ApplicationControl(EditorAction ea) {
		super("JSON Model Editor");
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
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ea.newModel();
			}
		});
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ea.openModel();
			}
		});
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ea.saveModel();
			}
		});
		mntmSave.setEnabled(false);
		mnFile.add(mntmSave);
		
		mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.setEnabled(false);
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ea.saveAsModel();
			}
		});
		mnFile.add(mntmSaveAs);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mntmSettings.setEnabled(false);
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
		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = contentPane.getWidth() - 220;
				//layeredPane.setSize(width, contentPane.getHeight() - 22);
				
				if (spTextEditor.isVisible()) {
					width -= spTextEditor.getWidth();
				}
				
				canvas.setSize(width - 3, contentPane.getHeight() - 25);
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setEnabled(false);
		tabbedPane.setPreferredSize(new Dimension(200, 5));
		
		JPanel panelModel = new JPanel();
		tabbedPane.addTab("Model", null, panelModel, null);
		
		JLabel lblParentModel = new JLabel("Parent model");
		lblParentModel.setEnabled(false);
		
		tfParent = new JTextField();
		tfParent.setEnabled(false);
		tfParent.setFont(new Font("Monospaced", Font.PLAIN, 11));
		tfParent.setColumns(10);
		
		JButton bLoadParent = new JButton("Load parent...");
		bLoadParent.setEnabled(false);
		
		JCheckBox cbAmbientOcclusion = new JCheckBox("Ambient Occlusion");
		cbAmbientOcclusion.setEnabled(false);
		cbAmbientOcclusion.setSelected(true);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setToolTipText("Textures");
		
		JSeparator separator_3 = new JSeparator();
		
		JButton bNewElement = new JButton("New");
		bNewElement.setEnabled(false);
		
		JButton bEditElement = new JButton("Edit");
		bEditElement.setEnabled(false);
		bEditElement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton bDeleteElement = new JButton("Delete");
		bDeleteElement.setEnabled(false);
		
		JScrollPane scrollPane_elements = new JScrollPane();
		scrollPane_elements.setEnabled(false);
		scrollPane_elements.setViewportBorder(new TitledBorder(null, "Elements", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JScrollPane scrollPane_textures = new JScrollPane();
		scrollPane_textures.setEnabled(false);
		scrollPane_textures.setViewportBorder(new TitledBorder(null, "Textures", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton bDeleteTexture = new JButton("Delete");
		bDeleteTexture.setEnabled(false);
		
		tfTextureName = new JTextField();
		tfTextureName.setEnabled(false);
		tfTextureName.setColumns(10);
		
		JLabel lblTextureName = new JLabel("Name");
		lblTextureName.setEnabled(false);
		
		tfTextureLocation = new JTextField();
		tfTextureLocation.setEnabled(false);
		tfTextureLocation.setColumns(10);
		
		JLabel lblTextureLocation = new JLabel("Location");
		lblTextureLocation.setEnabled(false);
		
		JButton bNewTexture = new JButton("New");
		bNewTexture.setEnabled(false);
		bNewTexture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton bEditTexture = new JButton("Edit");
		bEditTexture.setEnabled(false);
		
		JButton btnHighlightElement = new JButton("Highlight element");
		btnHighlightElement.setEnabled(false);
		GroupLayout gl_panelModel = new GroupLayout(panelModel);
		gl_panelModel.setHorizontalGroup(
			gl_panelModel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelModel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelModel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelModel.createSequentialGroup()
							.addComponent(btnHighlightElement, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panelModel.createSequentialGroup()
							.addComponent(tfParent, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panelModel.createSequentialGroup()
							.addComponent(cbAmbientOcclusion)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panelModel.createSequentialGroup()
							.addComponent(separator_3, GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
							.addGap(6))
						.addGroup(Alignment.TRAILING, gl_panelModel.createSequentialGroup()
							.addGroup(gl_panelModel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelModel.createSequentialGroup()
									.addComponent(lblParentModel)
									.addPreferredGap(ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
									.addComponent(bLoadParent))
								.addGroup(gl_panelModel.createSequentialGroup()
									.addComponent(bNewElement)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(bEditElement)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(bDeleteElement))
								.addGroup(gl_panelModel.createSequentialGroup()
									.addComponent(bNewTexture)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(bEditTexture)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(bDeleteTexture))
								.addComponent(scrollPane_elements, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
								.addGroup(gl_panelModel.createSequentialGroup()
									.addComponent(lblTextureLocation)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(tfTextureLocation, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
								.addComponent(scrollPane_textures, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panelModel.createSequentialGroup()
							.addComponent(separator_2, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panelModel.createSequentialGroup()
							.addComponent(lblTextureName)
							.addGap(23)
							.addComponent(tfTextureName, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_panelModel.setVerticalGroup(
			gl_panelModel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelModel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelModel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblParentModel)
						.addComponent(bLoadParent))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfParent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(cbAmbientOcclusion)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_textures, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelModel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfTextureName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTextureName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelModel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfTextureLocation, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTextureLocation))
					.addGap(9)
					.addGroup(gl_panelModel.createParallelGroup(Alignment.BASELINE)
						.addComponent(bDeleteTexture)
						.addComponent(bNewTexture)
						.addComponent(bEditTexture))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_3, GroupLayout.PREFERRED_SIZE, 7, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_elements, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelModel.createParallelGroup(Alignment.BASELINE)
						.addComponent(bEditElement)
						.addComponent(bDeleteElement)
						.addComponent(bNewElement))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnHighlightElement)
					.addContainerGap(361, Short.MAX_VALUE))
		);
		
		JList listElements = new JList();
		listElements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listElements.setFont(new Font("Monospaced", Font.PLAIN, 11));
		listElements.setBackground(SystemColor.menu);
		scrollPane_elements.setViewportView(listElements);
		
		JList listTextures = new JList();
		listTextures.setBackground(SystemColor.menu);
		listTextures.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTextures.setFont(new Font("Monospaced", Font.PLAIN, 11));
		scrollPane_textures.setViewportView(listTextures);
		panelModel.setLayout(gl_panelModel);
		
		JPanel panelElement = new JPanel();
		tabbedPane.addTab("Element", null, panelElement, null);
		
		JLabel lblElementName = new JLabel("Element name");
		
		tfElementName = new JTextField();
		tfElementName.setFont(new Font("Monospaced", Font.PLAIN, 11));
		tfElementName.setColumns(10);
		
		JSeparator separator_4 = new JSeparator();
		
		JLabel lblFrom = new JLabel("from");
		
		JLabel lblTo = new JLabel("to");
		
		JCheckBox cbElementShade = new JCheckBox("Shade");
		cbElementShade.setSelected(true);
		
		JSeparator separator_5 = new JSeparator();
		
		JLabel lblOrigin = new JLabel("Origin");
		
		JLabel lblAxis = new JLabel("Axis");
		
		JComboBox coRotationAxis = new JComboBox();
		coRotationAxis.setModel(new DefaultComboBoxModel(Axis.values()));
		
		JSlider slRotationAngle = new JSlider();
		slRotationAngle.setMinorTickSpacing(22);
		slRotationAngle.setSnapToTicks(true);
		slRotationAngle.setPaintLabels(true);
		slRotationAngle.setPaintTicks(true);
		slRotationAngle.setValue(0);
		slRotationAngle.setMinimum(-45);
		slRotationAngle.setMaximum(45);
		slRotationAngle.setMajorTickSpacing(45);
		
		JSeparator separator_6 = new JSeparator();
		
		JCheckBox cbRescaleElement = new JCheckBox("Rescale");
		
		JButton bNewFace = new JButton("New");
		bNewFace.setEnabled(false);
		
		JButton bEditFace = new JButton("Edit");
		
		JButton bDeleteFace = new JButton("Delete");
		
		JScrollPane scrollPane_faces = new JScrollPane();
		scrollPane_faces.setViewportBorder(new TitledBorder(null, "Faces", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JCheckBox cbRotateElement = new JCheckBox("Rotate");
		
		JSeparator separator_7 = new JSeparator();
		
		JSpinner spFromZ = new JSpinner();
		
		JSpinner spToZ = new JSpinner();
		
		JSpinner spOriginZ = new JSpinner();
		
		JSpinner spToY = new JSpinner();
		
		JSpinner spFromY = new JSpinner();
		
		JSpinner spFromX = new JSpinner();
		
		JSpinner spToX = new JSpinner();
		
		JSpinner spOriginY = new JSpinner();
		
		JSpinner spOriginX = new JSpinner();
		GroupLayout gl_panelElement = new GroupLayout(panelElement);
		gl_panelElement.setHorizontalGroup(
			gl_panelElement.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelElement.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelElement.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelElement.createSequentialGroup()
							.addGroup(gl_panelElement.createParallelGroup(Alignment.TRAILING)
								.addComponent(separator_4, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
								.addComponent(separator_5, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
								.addGroup(gl_panelElement.createSequentialGroup()
									.addComponent(lblElementName)
									.addGap(18)
									.addComponent(tfElementName, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
								.addGroup(gl_panelElement.createSequentialGroup()
									.addComponent(cbRotateElement)
									.addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
									.addComponent(cbRescaleElement))
								.addGroup(gl_panelElement.createSequentialGroup()
									.addComponent(lblOrigin)
									.addGap(18)
									.addComponent(spOriginX, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(spOriginY, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(spOriginZ, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
								.addGroup(Alignment.LEADING, gl_panelElement.createSequentialGroup()
									.addGroup(gl_panelElement.createParallelGroup(Alignment.LEADING)
										.addComponent(coRotationAxis, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblAxis))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(slRotationAngle, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
								.addComponent(separator_6, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
								.addGroup(gl_panelElement.createSequentialGroup()
									.addComponent(scrollPane_faces, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panelElement.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(bDeleteFace, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(bEditFace, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(bNewFace, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
								.addGroup(gl_panelElement.createSequentialGroup()
									.addGroup(gl_panelElement.createParallelGroup(Alignment.LEADING)
										.addComponent(lblFrom)
										.addComponent(lblTo))
									.addGap(10)
									.addGroup(gl_panelElement.createParallelGroup(Alignment.TRAILING)
										.addComponent(spToX, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
										.addComponent(spFromX, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panelElement.createParallelGroup(Alignment.TRAILING)
										.addComponent(spToY, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
										.addComponent(spFromY, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panelElement.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(spToZ)
										.addComponent(spFromZ, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))))
							.addContainerGap())
						.addGroup(gl_panelElement.createSequentialGroup()
							.addComponent(separator_7, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
							.addGap(7))
						.addGroup(gl_panelElement.createSequentialGroup()
							.addComponent(cbElementShade)
							.addContainerGap(137, Short.MAX_VALUE))))
		);
		gl_panelElement.setVerticalGroup(
			gl_panelElement.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelElement.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelElement.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblElementName)
						.addComponent(tfElementName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbElementShade)
					.addGap(7)
					.addComponent(separator_4, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panelElement.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelElement.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panelElement.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblFrom)
								.addComponent(spFromZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(Alignment.TRAILING, gl_panelElement.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
							.addGroup(gl_panelElement.createParallelGroup(Alignment.BASELINE)
								.addComponent(spFromY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(spFromX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_panelElement.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTo)
						.addComponent(spToZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(spToY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(spToX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(13)
					.addComponent(separator_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelElement.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbRescaleElement)
						.addComponent(cbRotateElement))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelElement.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOrigin)
						.addComponent(spOriginZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(spOriginY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(spOriginX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelElement.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panelElement.createSequentialGroup()
							.addComponent(lblAxis)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(coRotationAxis, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(slRotationAngle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelElement.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelElement.createSequentialGroup()
							.addComponent(bNewFace)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(bEditFace)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(bDeleteFace))
						.addComponent(scrollPane_faces, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_7, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addGap(453))
		);
		
		JList listFaces = new JList();
		scrollPane_faces.setViewportView(listFaces);
		listFaces.setFont(new Font("Monospaced", Font.PLAIN, 11));
		listFaces.setModel(new AbstractListModel() {
			String[] values = new String[] {"north", "south", "east", "west", "up", "down"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listFaces.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFaces.setBackground(SystemColor.menu);
		listFaces.setBorder(null);
		panelElement.setLayout(gl_panelElement);
		
		JPanel panelFace = new JPanel();
		tabbedPane.addTab("Face", null, panelFace, null);
		GroupLayout gl_panelFace = new GroupLayout(panelFace);
		gl_panelFace.setHorizontalGroup(
			gl_panelFace.createParallelGroup(Alignment.LEADING)
				.addGap(0, 195, Short.MAX_VALUE)
		);
		gl_panelFace.setVerticalGroup(
			gl_panelFace.createParallelGroup(Alignment.LEADING)
				.addGap(0, 832, Short.MAX_VALUE)
		);
		panelFace.setLayout(gl_panelFace);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
				.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
		);
		
		spTextEditor = new JScrollPane();
		splitPane.setRightComponent(spTextEditor);
		
		tpTextEditor = new JTextPane();
		tpTextEditor.setBackground(Color.DARK_GRAY);
		tpTextEditor.setForeground(SystemColor.text);
		tpTextEditor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (tpTextEditor.getText().contains("\t")) {
					tpTextEditor.setText(tpTextEditor.getText().replace("\t", "    "));
				}
				
				if (e.getKeyCode() == 9) {
					e.consume();
					int loc = tpTextEditor.getCaret().getMark();
					try {
						tpTextEditor.getDocument().insertString(loc, "    ", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
				
				SyntaxHighlighter.highlight(tpTextEditor);				
			}
		});
		tpTextEditor.setFont(new Font("Monospaced", Font.PLAIN, 12));
		spTextEditor.setViewportView(tpTextEditor);
		
		JButton btnRefreshModel = new JButton("Refresh model");
		btnRefreshModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SyntaxHighlighter.highlight(tpTextEditor);	
				String json = tpTextEditor.getText();
				if (FileLoader.isValidJSON(json)) {
					JSONModelViewer.instance.scheduleModelUpdate(json);
				} else {
					JOptionPane.showMessageDialog(ApplicationControl.this, "Invalid JSON!", "JSON Parsing Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		spTextEditor.setColumnHeaderView(btnRefreshModel);

		JMenuItem mntmRefreshModel = new JMenuItem("Refresh model from JSON editor");
		mntmRefreshModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SyntaxHighlighter.highlight(tpTextEditor);	
				String json = tpTextEditor.getText();
				if (FileLoader.isValidJSON(json)) {
					JSONModelViewer.instance.scheduleModelUpdate(json);
				} else {
					JOptionPane.showConfirmDialog(ApplicationControl.this, "Invalid JSON!", "JSON Parsing Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		mnTools.add(mntmRefreshModel);
		
		JPanel panelCanvas = new JPanel();
		panelCanvas.setBackground(SystemColor.menu);
		panelCanvas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				canvas.setBounds(0, 0, panelCanvas.getWidth(), panelCanvas.getHeight());
				JSONModelViewer.instance.scheduleResize();
			}
		});
		panelCanvas.setMinimumSize(new Dimension(800, 600));
		splitPane.setLeftComponent(panelCanvas);
		
		canvas = new Canvas();
		panelCanvas.add(canvas);
		contentPane.setLayout(gl_contentPane);
		
		addComponentListener(new ComponentListener(){
			public void componentResized(ComponentEvent e) {
				//JSONModelViewer.instance.scheduleResize();
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
	public JScrollPane getTextEditorPanel() {
		return spTextEditor;
	}
	public JTextPane getTextEditor() {
		return tpTextEditor;
	}
	public void enableSaves() {
		if (!mntmSave.isEnabled()) {
			mntmSave.setEnabled(true);
			mntmSaveAs.setEnabled(true);
		}
	}
	public void disableSaves() {
		if (mntmSave.isEnabled()) {
			mntmSave.setEnabled(false);
			mntmSaveAs.setEnabled(false);
		}
	}
}
