package me.toofifty.jmv.editor;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import me.toofifty.jmv.JSONModelViewer;
import me.toofifty.jmv.model.Model;

/**
 * Handles creating, editing and removing
 * objects from the GUI
 * 
 * @author Toofifty
 *
 */
public class EditorAction {
	
	/* Helper methods */
	
	/**
	 * Quickly check if a model is present or not.
	 * 
	 * @return boolean
	 */
	public boolean isEditingModel() {
		return JSONModelViewer.instance.model != null;
	}
	
	/**
	 * Quickly get current model
	 * 
	 * @return model
	 */
	public Model getWorkingModel() {
		return JSONModelViewer.instance.model;
	}
	
	/**
	 * Get the main JFrame object.
	 * 
	 * Heh. Main Frame.
	 * 
	 * @return JFrame
	 */
	public JFrame getMainWindow() {
		return JSONModelViewer.instance.getFrame();
	}

	/**
	 * Get the contents of the clipboard as string.
	 * 
	 * @return Clipboard as string or ""
	 */
	public String getClipboardString() {
		try {
			return (String) Toolkit.getDefaultToolkit().getSystemClipboard()
						.getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/* Editor methods */
	
	public void newModel() {
		if (isEditingModel()) {
			int dialogButton = JOptionPane.YES_NO_CANCEL_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(getMainWindow(), "Save changes to the current model?", "Confirm action", dialogButton);
			// NO_OPTION simply continues
			if (dialogResult == JOptionPane.YES_OPTION) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new JsonSaveFilter());
				fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[1]);
				if (fileChooser.showSaveDialog(getMainWindow()) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					System.out.println("Saving model to " + file.getAbsolutePath());
				} else { // Cancel
					return;
				}
			} else if (dialogResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		System.out.println("Creating new model...");
	}
	
	public void loadModel() {
		if (isEditingModel()) {
			int dialogButton = JOptionPane.YES_NO_CANCEL_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(getMainWindow(), "Save changes to the current model?", "Confirm action", dialogButton);
			// NO_OPTION simply continues
			if (dialogResult == JOptionPane.YES_OPTION) {

			} else if (dialogResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		System.out.println("Loading model...");
	}

}
