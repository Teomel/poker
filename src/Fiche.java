import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Fiche extends JLabel
{
	ImageIcon icon_fiche;
	
	public Fiche()
	{
		icon_fiche = new ImageIcon(FinestraPoker.class.getResource("Images/fiche.png"));
		icon_fiche = new ImageIcon(icon_fiche.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
		setIcon(icon_fiche);
	}
	
}
