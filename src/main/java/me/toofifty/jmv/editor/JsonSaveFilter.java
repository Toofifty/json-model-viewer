package me.toofifty.jmv.editor;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class JsonSaveFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		
		String s = f.getName();
		return s.toLowerCase().endsWith(".json");
	}

	@Override
	public String getDescription() {
		return "*.json";
	}

}
