package midas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
/**
 * 편집창의 탭을 커스텀하기위한 Panel
 * @author dnjsd
 *
 */
public class CustomTab extends JPanel {
	private TappedPane tabbedPane;
	public CustomTab(TappedPane tappedPane) {
		this.tabbedPane = tappedPane;
		setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		setOpaque(false);
		addLabel();
        add(new CustomButton("x"));
	}
	
	private void addLabel() {
		JLabel label = new JLabel() {
			public String getText() {
				int index = tabbedPane.indexOfTabComponent(CustomTab.this);
				if(index != -1) {
					return tabbedPane.getTitleAt(index);
				}
				return null;
			}
		};
		label.setBorder(new EmptyBorder(0, 0, 0, 10));
		add(label);
	}
	
	class CustomButton extends JButton implements MouseListener {
        public CustomButton(String text) {
            int size = 15;
            setText(text);
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close the Tab");
            setContentAreaFilled(false);
            setBorder(new EtchedBorder());
            setBorderPainted(false);
            setFocusable(false);
            addMouseListener(this);
 
        }
         @Override
        public void mouseClicked(MouseEvent e) {
            int index = tabbedPane.indexOfTabComponent(CustomTab.this);
            if (index != -1) {
            	tabbedPane.removeTabAt(index);
            }
        }
 
        @Override
        public void mousePressed(MouseEvent e) {
        }
 
        @Override
        public void mouseReleased(MouseEvent e) {
        }
 
        @Override
        public void mouseEntered(MouseEvent e) {
            setBorderPainted(true);
            setForeground(Color.RED);
        }
 
        @Override
        public void mouseExited(MouseEvent e) {
            setBorderPainted(false);
            setForeground(Color.BLACK);
        }
    }
	
}
