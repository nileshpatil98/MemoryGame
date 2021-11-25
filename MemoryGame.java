
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
public class MemoryGame {
    private static final int APPLICATION_SIZE = 400;
    private static final Color BACKGROUND = new JLabel().getBackground();
    private int score = 0;
    public static void main(String[] args) {
        new MemoryGame().runGame();
    }
    private void runGame() {
        JFrame application = new JFrame("color game");
        application.setTitle("Memory Game ");
        JLabel scoreLabel = new JLabel("score: " + score);
        List<Color> colors = Arrays.asList(Color.BLUE, Color.CYAN, Color.RED, Color.YELLOW, Color.GREEN, Color.BLACK);
        List<JButton> fieldCells = initializeGame(colors);
        JPanel gameFiled = initializeView(fieldCells);
        bindViewToModel(colors, fieldCells, scoreLabel);
        JPanel gameControl = setupController(colors, fieldCells, application, scoreLabel);
        application.getContentPane().add(gameFiled);
        application.getContentPane().add(gameControl, BorderLayout.SOUTH);
        application.setSize(APPLICATION_SIZE, 400);
        application.setVisible(true);
    }
    private JPanel setupController(List<Color> colors,
            List<JButton> fieldCells,
            JFrame application,
            JLabel scoreLabel) {
        JPanel gameControl = new JPanel(new GridLayout(1, 0));
        gameControl.add(new JButton(new AbstractAction("RESTART") {
            @Override
            public void actionPerformed(ActionEvent e) {
                bindViewToModel(colors, fieldCells, scoreLabel);
            }
        }));
        gameControl.add(scoreLabel);
        gameControl.add(new JButton(new AbstractAction("QUIT") {
            @Override
            public void actionPerformed(ActionEvent e) {
                application.dispose();
            }
        }));
        return gameControl;
    }
    private void bindViewToModel(List<Color> colors, List<JButton> fieldCells, JLabel scoreLabel) {
        Collection<JComponent> clickedButtons = new HashSet<>(); 
        Collections.shuffle(fieldCells);
        Iterator<JButton> randomCells = fieldCells.iterator();
        for (Color color : colors) {
            AbstractAction buttonAction = createButonAction(clickedButtons, color, scoreLabel);
            bindButton(buttonAction, randomCells.next());
            bindButton(buttonAction, randomCells.next());
        }
        clickedButtons.clear();
        score = 0;
    }
    private void bindButton(AbstractAction buttonAction, JButton jButton) {
        jButton.setAction(buttonAction);
        jButton.setBackground(BACKGROUND);
    }
    private JPanel initializeView(List<JButton> fieldCells) {
        JPanel gameFiled = new JPanel(new GridLayout(4, 0));
        for (JButton fieldCell : fieldCells) {
            fieldCell.setBackground(BACKGROUND);
            fieldCell.setEnabled(true);
            gameFiled.add(fieldCell);
        }
        return gameFiled;
    }
    private List<JButton> initializeGame(Collection<Color> colors) {
        List<JButton> fieldCells = new ArrayList<>();
        for (Color color : colors) {
            fieldCells.add(new JButton()); 
            fieldCells.add(new JButton());
        }
        return fieldCells;
    }
    private AbstractAction createButonAction(Collection<JComponent> clickedButtons, Color color, JLabel scoreLabel) {
        @SuppressWarnings("serial")
		AbstractAction buttonAction = new AbstractAction() { 
            Collection<JComponent> clickedPartners = new HashSet<>(); 
            @Override
            public void actionPerformed(ActionEvent e) {
                JComponent thisButton = (JComponent) e.getSource();
                clickedPartners.add(thisButton);
                clickedButtons.add(thisButton);
                thisButton.setBackground(color);
                thisButton.setEnabled(false);
                if (2 == clickedButtons.size()) { 
                    if (2 == clickedPartners.size()) { 
                        score += 5;
                    } else {
                        JOptionPane.showMessageDialog(thisButton, "NO MATCH");
                        for (JComponent partner : clickedButtons) {
                            partner.setBackground(BACKGROUND);
                            partner.setEnabled(true);
                        }
                        score -=2;
                    }
                    clickedButtons.clear();
                    clickedPartners.clear();
                    scoreLabel.setText("score: "+ score);
                }
            }
        };
        return buttonAction;
    }
}
