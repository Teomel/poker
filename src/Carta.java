import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class Carta {
	
	public static boolean bloccoSelect = true;
	private int faccia, seme;
	private JLabel label;
	private boolean selected = false;
	
	public Carta(int faccia, int seme)
	{
		this.faccia = faccia;
		this.seme = seme;
		label = new JLabel();
		ImageIcon icon = new ImageIcon(FinestraPoker.class.getResource("Images/"+faccia+seme+".png"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(82, 115, java.awt.Image.SCALE_SMOOTH));
		label.setIcon(icon);
		label.setBorder(new LineBorder(Color.BLACK,1,true));
		label.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!bloccoSelect)
				{
					if (selected) {
						selected = false;
						label.setBorder(new LineBorder(Color.BLACK,1,true));
					}
					else {
						selected = true;
						label.setBorder(new LineBorder(Color.BLUE,2,true));
					}
				}
			}	
		});
	}
	
	public int getFaccia() { return faccia; }
	public int getSeme() { return seme; }
	public JLabel getLabel() { return label; }
	public boolean getSelected() { return selected; }
	public void addMouseListener(MouseListener ml) {
		label.addMouseListener(ml);
	}
	
}
