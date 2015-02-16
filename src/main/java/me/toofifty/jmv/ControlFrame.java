package me.toofifty.jmv;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;

/**
 * A small window used to control the JSON Model Viewer
 * 
 * @author Toofifty
 *
 */
public class ControlFrame extends JFrame implements ActionListener {
	
	private JFrame frame;
	private JTextArea jsonModel;
	
	/**
	 * A small window used to control the main OpenGL application.
	 */
	public ControlFrame() {
		super("JSON Model Viewer");
		init();
	}
	
	/**
	 * Create frame elements
	 */
	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel label = new JLabel("Paste your JSON here");
		JButton updateButton = new JButton("Update model");
		updateButton.setActionCommand("update");
		updateButton.addActionListener(this);
		JButton floorButton = new JButton("Toggle floor");
		floorButton.setActionCommand("toggleFloor");
		floorButton.addActionListener(this);
		JButton isoButton = new JButton("Set to iso");
		isoButton.setActionCommand("isometric");
		isoButton.addActionListener(this);
		
		jsonModel = new JTextArea(4, 20);
		jsonModel.setLineWrap(true);
		jsonModel.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(jsonModel);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		
		ParallelGroup hGroup = layout
				.createParallelGroup(GroupLayout.Alignment.LEADING);
		// Create a sequential and a parallel groups
		SequentialGroup h1 = layout.createSequentialGroup();
		ParallelGroup h2 = layout
				.createParallelGroup(GroupLayout.Alignment.TRAILING);
		// Add a scroll panel and a label to the parallel group h2
		h2.addComponent(scrollPane, GroupLayout.Alignment.LEADING,
				GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE);
		h2.addComponent(updateButton, GroupLayout.Alignment.LEADING,
				GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE);
		h2.addComponent(floorButton, GroupLayout.Alignment.LEADING,
				GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE);
		h2.addComponent(isoButton, GroupLayout.Alignment.LEADING,
				GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE);

		// Add a container gap to the sequential group h1
		h1.addContainerGap();
		// Add the group h2 to the group h1
		h1.addGroup(h2);
		h1.addContainerGap();
		// Add the group h1 to hGroup
		hGroup.addGroup(Alignment.TRAILING, h1);
		// Create the horizontal group
		layout.setHorizontalGroup(hGroup);

		// Create a parallel group for the vertical axis
		ParallelGroup vGroup = layout
				.createParallelGroup(GroupLayout.Alignment.LEADING);
		// Create a sequential group
		SequentialGroup v1 = layout.createSequentialGroup();
		// Add a container gap to the sequential group v1
		v1.addContainerGap();
		// Add a label to the sequential group v1
		v1.addComponent(updateButton);
		v1.addComponent(floorButton);
		v1.addComponent(isoButton);
		v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		// Add scroll panel to the sequential group v1
		v1.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 100,
				Short.MAX_VALUE);
		v1.addContainerGap();
		// Add the group v1 to vGroup
		vGroup.addGroup(v1);
		// Create the vertical group
		layout.setVerticalGroup(vGroup);
		
		pack();
		setVisible(true);
	}
	
	/**
	 * Get textfield 'jsonModel' string
	 * 
	 * @return JSON model as string
	 */
	public String getJSONModel() {
		return jsonModel.getText();
	}
	
	/**
	 * Called on button press
	 */
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "update":
			JSONModelViewer.instance.scheduleModelUpdate(getJSONModel());
			break;
		case "toggleFloor":
			JSONModelViewer.instance.toggleFloor();
			break;
		case "isometric":
			JSONModelViewer.instance.setIso();
			break;
		}
	}
}
