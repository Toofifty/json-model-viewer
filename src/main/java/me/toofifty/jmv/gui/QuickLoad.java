package me.toofifty.jmv.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.Window.Type;

import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;

import me.toofifty.jmv.FileLoader;
import me.toofifty.jmv.JSONModelViewer;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.io.IOException;

import javax.swing.Box;

public class QuickLoad extends JFrame {
	private JTextArea txtrJSON;

	/**
	 * Create the frame.
	 */
	public QuickLoad() {		
		setBounds(JSONModelViewer.instance.getCentreScreen(300, 200));
		setType(Type.POPUP);
		setTitle("JSON Quick Load");
		setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {21, 37, 0, 63, 0, 33};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblEnterJsonHere = new JLabel(" Enter JSON here:");
		GridBagConstraints gbc_lblEnterJsonHere = new GridBagConstraints();
		gbc_lblEnterJsonHere.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblEnterJsonHere.insets = new Insets(0, 0, 5, 5);
		gbc_lblEnterJsonHere.gridx = 2;
		gbc_lblEnterJsonHere.gridy = 0;
		getContentPane().add(lblEnterJsonHere, gbc_lblEnterJsonHere);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 3;
		gbc_verticalStrut.gridy = 0;
		getContentPane().add(verticalStrut, gbc_verticalStrut);
		
		txtrJSON = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(txtrJSON);
		GridBagConstraints gbc_txtrHekki = new GridBagConstraints();
		gbc_txtrHekki.gridheight = 4;
		gbc_txtrHekki.gridwidth = 3;
		gbc_txtrHekki.insets = new Insets(0, 0, 5, 5);
		gbc_txtrHekki.fill = GridBagConstraints.BOTH;
		gbc_txtrHekki.gridx = 2;
		gbc_txtrHekki.gridy = 1;
		getContentPane().add(scrollPane, gbc_txtrHekki);
		
		JButton btnPasteFromClipboard = new JButton("Paste from clipboard");
		btnPasteFromClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String clipboard;
				try {
					clipboard = (String) Toolkit.getDefaultToolkit()
							.getSystemClipboard().getData(DataFlavor.stringFlavor);
					txtrJSON.setText(clipboard);
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (UnsupportedFlavorException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnPasteFromClipboard = new GridBagConstraints();
		gbc_btnPasteFromClipboard.anchor = GridBagConstraints.WEST;
		gbc_btnPasteFromClipboard.insets = new Insets(0, 0, 0, 5);
		gbc_btnPasteFromClipboard.gridx = 2;
		gbc_btnPasteFromClipboard.gridy = 5;
		getContentPane().add(btnPasteFromClipboard, gbc_btnPasteFromClipboard);
		
		JButton btnLoad = new JButton("Continue");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtrJSON.getText() != "" && FileLoader.isValidJSON(txtrJSON.getText())) {
					JSONModelViewer.instance.scheduleModelUpdate(txtrJSON.getText());
					dispose();
				} else {
					new ErrorMessage("Invalid JSON string!");
				}
			}
		});
		GridBagConstraints gbc_btnLoad = new GridBagConstraints();
		gbc_btnLoad.anchor = GridBagConstraints.EAST;
		gbc_btnLoad.insets = new Insets(0, 0, 0, 5);
		gbc_btnLoad.gridx = 3;
		gbc_btnLoad.gridy = 5;
		getContentPane().add(btnLoad, gbc_btnLoad);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.gridx = 4;
		gbc_btnCancel.gridy = 5;
		getContentPane().add(btnCancel, gbc_btnCancel);
		
		//pack();
		setVisible(true);
	}

}
