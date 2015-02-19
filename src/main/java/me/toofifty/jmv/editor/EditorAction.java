package me.toofifty.jmv.editor;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;

import me.toofifty.jmv.FileLoader;
import me.toofifty.jmv.JSONModelViewer;
import me.toofifty.jmv.gui.ApplicationControl;
import me.toofifty.jmv.gui.SyntaxHighlighter;
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
	 * Get the running instance
	 * 
	 * @return
	 */
	public JSONModelViewer getMV() {
		return JSONModelViewer.instance;
	}
	
	/**
	 * Quickly get current model
	 * 
	 * @return model
	 */
	public Model getWorkingModel() {
		return getMV().getModel();
	}
	
	/**
	 * Get the main ApplicationControl object.
	 * 
	 * @return frame
	 */
	public ApplicationControl getMainWindow() {
		return getMV().getFrame();
	}
	
	/**
	 * Get the main text editor
	 * 
	 * @return text editor
	 */
	public JTextPane getTextEditor() {
		return getMainWindow().getTextEditor();
	}
	
	/**
	 * Quickly check if a model is present or not.
	 * 
	 * @return boolean
	 */
	public boolean isEditingModel() {
		return getWorkingModel() != null;
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
	
	/**
	 * Update text editor with current working model
	 */
	public void updateTextEditor() {
		try {
			Model model = getMV().getModel();
			if (model != null) {
				getMV().getFrame().getTextEditor().setText(model.getJSON());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Editor methods */

	public void newModel() {
		if (isEditingModel()) {
			int dialogResult = JOptionPane.showConfirmDialog(getMainWindow(),
					"Save changes to the current model?", "Confirm action",
					JOptionPane.YES_NO_CANCEL_OPTION);
			// NO_OPTION simply continues
			if (dialogResult == JOptionPane.YES_OPTION) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new JsonSaveFilter());
				fileChooser
						.setFileFilter(fileChooser.getChoosableFileFilters()[1]);
				if (fileChooser.showSaveDialog(getMainWindow()) == JFileChooser.APPROVE_OPTION) {
					saveModel();
				} else { // Cancel
					return;
				}
			} else if (dialogResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		System.out.println("Creating new model...");
		
		JFileChooser fc = showSaveFileChooser("Select save location for new model");

		getMV().scheduleNewModel();

		if (fc.showSaveDialog(getMainWindow()) == JFileChooser.APPROVE_OPTION) {
			try {
				
				Model model = getMV().getModel();
				String modelJson = model.getJSON();
				
				File f = fc.getSelectedFile();
				String fileStr = f.getAbsolutePath();
				
				// Append .json if not found.
				if (!fileStr.toLowerCase().endsWith(".json")) {
					fileStr += ".json";
					f = new File(f.getAbsolutePath() + ".json");
				}
				
				// Save
				PrintWriter out;
				out = new PrintWriter(fileStr);
				out.println(modelJson);
				out.close();
				System.out.println("Model saved to " + fileStr);
				getMV().setSaveFile(f);
				
				FileLoader.setAssetsDirFromFile(f);

				getMV().scheduleNewModel();
				updateTextEditor();
				SyntaxHighlighter.highlight(getTextEditor());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}

	/**
	 * Open model with JFileChooser
	 */
	public void openModel() {
		if (isEditingModel()) {
			int dialogResult = JOptionPane.showConfirmDialog(getMainWindow(),
					"Save changes to the current model?", "Confirm action",
					JOptionPane.YES_NO_CANCEL_OPTION);
			// NO_OPTION simply continues
			if (dialogResult == JOptionPane.YES_OPTION) {
				saveModel();
			} else if (dialogResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		System.out.println("Loading model...");
		
		JFileChooser fc = new JFileChooser() {
			@Override
			public void approveSelection() {
				
				File f = getSelectedFile();
				String fileStr = f.getAbsolutePath();
				
				if (!fileStr.toLowerCase().endsWith(".json")) {
					JOptionPane.showMessageDialog(this, "File must be a .json");
					return;
				}
				
				if (!(fileStr.toLowerCase().contains("\\models\\block") || fileStr.toLowerCase().contains("\\models\\item"))) {
					int result = JOptionPane.showConfirmDialog(this, "Warning: This file may not be in the corrent location to load parents and textures. Do you still want to load this file?", "Non-model location", JOptionPane.YES_NO_CANCEL_OPTION);
					switch (result) {
					case JOptionPane.YES_OPTION:
						break;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
					default:
						return;
					}
				}
				
				super.approveSelection();
			}
		};

		// Add *.json to list
		fc.addChoosableFileFilter(new JsonSaveFilter());
		// Set *.json to top of list
		fc.setFileFilter(fc.getChoosableFileFilters()[1]);
		
		if (fc.showOpenDialog(getMainWindow()) == JFileChooser.APPROVE_OPTION) {
			try {
				File f = fc.getSelectedFile();
				String fText = FileLoader.readFileString(f.getAbsolutePath());
				System.out.println(f.getAbsolutePath());
				getTextEditor().setText(fText);
				getMV().setSaveFile(f);
				
				getMV().scheduleModelUpdate(fText);
				SyntaxHighlighter.highlight(getTextEditor());
				
				FileLoader.setAssetsDirFromFile(f);
			} catch (IOException e) {
				System.out.println("Bad file load");
				e.printStackTrace();
			}
		}
	}
	
	public JFileChooser showSaveFileChooser(String title) {
		// FileChooser window
		JFileChooser fc = new JFileChooser() {
			@Override
			public void approveSelection() {
				
				File f = getSelectedFile();
				String fileStr = f.getAbsolutePath();
				
				// Append .json if not found.
				if (!fileStr.toLowerCase().endsWith(".json")) {
					fileStr += ".json";
				}
				
				System.out.println("Trying to save to " + fileStr);
				
				// Overwrite message
				if (f.exists()) {
					int result = JOptionPane.showConfirmDialog(this, "That file already exists, overwrite?", "Confirm overwrite", JOptionPane.YES_NO_CANCEL_OPTION);
					switch (result) {
					case JOptionPane.YES_OPTION:
						super.approveSelection();
					default:
						return;					
					}
					
				// Potentially bad directory message
				} else if (!(fileStr.toLowerCase().endsWith("models/block") || 
						fileStr.toLowerCase().endsWith("models/block"))) {

					int dialogResult = JOptionPane
							.showConfirmDialog(this,
									"Are you sure you want to save outside of a /models/<type>/ folder?",
									"Confirm location",
									JOptionPane.YES_NO_OPTION);
					if (dialogResult == JOptionPane.NO_OPTION || dialogResult == JOptionPane.CANCEL_OPTION) {
						return;
					}
				}
				super.approveSelection();
			}
		};
		
		fc.setDialogTitle(title);
		
		// Add *.json to list
		fc.addChoosableFileFilter(new JsonSaveFilter());
		// Set *.json to top of list
		fc.setFileFilter(fc.getChoosableFileFilters()[1]);
		
		return fc;
	}

	/**
	 * Save the current text editor string through a JFileChooser
	 */
	public void saveAsModel() {
		
		// We don't want to save if there is no model
		if (!isEditingModel()) {
			return;
		}
		System.out.println("Saving model...");
		
		JFileChooser fc = showSaveFileChooser("Save As...");
		
		if (fc.showSaveDialog(getMainWindow()) == JFileChooser.APPROVE_OPTION) {
			try {
				
				File f = fc.getSelectedFile();
				String fileStr = f.getAbsolutePath();
				
				// Append .json if not found.
				if (!fileStr.toLowerCase().endsWith(".json")) {
					fileStr += ".json";
					f = new File(f.getAbsolutePath() + ".json");
				}
				
				// Save
				PrintWriter out;
				out = new PrintWriter(fileStr);
				out.println(getTextEditor().getText());
				out.close();
				System.out.println("Model saved to " + fileStr);
				getMV().setSaveFile(f);
				
				FileLoader.setAssetsDirFromFile(f);
				
				getMV().scheduleModelUpdate(getTextEditor().getText());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Save the current model.
	 * Run SaveAs method if no saveFile found.
	 * Save to saveFile if found.
	 */
	public void saveModel() {
		File saveFile = getMV().getSaveFile();
		
		// Save to saveFile if found
		if (saveFile != null) {
			try {
				
				// Save
				PrintWriter out;
				out = new PrintWriter(saveFile);
				out.println(getTextEditor().getText());
				out.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		// SaveAs if not found
		} else {
			saveAsModel();
		}
	}
}
