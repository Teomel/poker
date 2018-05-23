import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Scritta extends JLabel
{
	private Font font = new Font("Tahoma", Font.BOLD, 16);
	
	public Scritta(String text)
	{
		setText(text);
		setForeground(Color.white);
		setFont(font);
	}
	
	public Scritta() { this(""); }
}