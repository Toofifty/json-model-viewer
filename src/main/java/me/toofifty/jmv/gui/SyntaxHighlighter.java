package me.toofifty.jmv.gui;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class SyntaxHighlighter extends SwingWorker {

	public static void highlight(JTextPane jtp) {
		StyleContext style = StyleContext.getDefaultStyleContext();
		AttributeSet defaultStyle = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, Color.white);
		jtp.getStyledDocument().setCharacterAttributes(0, jtp.getText().length(), defaultStyle, false);
		style = StyleContext.getDefaultStyleContext();
		AttributeSet orangeStyle = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, Color.orange);
		AttributeSet cyanStyle = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, Color.cyan);
		AttributeSet grayStyle = style.addAttribute(style.getEmptySet(), StyleConstants.Foreground, Color.lightGray);
		
		String orangeRegex = "[0-9]";
		String grayRegex = "\"#([a-z]*?)\""; //"\\{|\\}|\\[|\\]|\\,|\\:";
		String cyanRegex = "\"(\\S*?)\"\\s*\\:";
		int length = jtp.getDocument().getLength();
		String input;
		try {
			input = jtp.getDocument().getText(0, length);
			Matcher m = Pattern.compile(orangeRegex).matcher(input);
			while (m.find()) {
				jtp.getStyledDocument().setCharacterAttributes(m.start(), (m.end() - m.start()), orangeStyle, false);
			}
			m = Pattern.compile(cyanRegex).matcher(input);
			while (m.find()) {
				jtp.getStyledDocument().setCharacterAttributes(m.start(), (m.end() - m.start()), cyanStyle, false);
			}
			m = Pattern.compile(grayRegex).matcher(input);
			while (m.find()) {
				jtp.getStyledDocument().setCharacterAttributes(m.start(), (m.end() - m.start()), grayStyle, false);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Object doInBackground() throws Exception {
		return null;
	}

}
