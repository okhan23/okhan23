/*tetris.java*/

//
// Assignment: Tetris
// Class: CS 342 Spring 2016 - Troy
// Names: Omaid Khan & Jesus Solorzano
//

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.*;

public class Tetris extends JFrame {

    private static int DELAY = 300;
    private static int ROWS = 20;
    private static int COLS = 10;
    private JLabel lblFinalScore, lblCurScore, lblTetris, lblBy, lblnOmaidKhan, lblTime, lblLevelsCleared, lblLevel;
    private JPanel boardPanel;
    private JPanel nxtPanel;
    private JMenuBar menuBar;
    private JMenuItem mntmRestart, mntmQuit, mntmHelp, mntmNewMenuItem, gravItem;
    private Icon emptyImage = new ImageIcon("whitesquare.jpg", "white");
    private Tetriminoes curPiece;
    private Tetriminoes nextPiece;
    private Icon[][] iconGrid = new Icon[ROWS][COLS];
    private JLabel[][] labelArray = new JLabel[ROWS][COLS];
    private myTimer timer;
    private Timer countTimer;
    private int lvls = 0;
    private int tim = 0;
    private boolean newMove = false;
    private int curScore = 0;
    private boolean Gravity = true;

    public Tetris() {
        createGUI();
        this.addKeyListener(new KeyListener() {
            boolean rest = false;

            @Override
            public void keyTyped(KeyEvent e) {


            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    // DOWN-----------------------------
                    case KeyEvent.VK_DOWN:
                        System.out.println("down");
                        if (!rest) {
                            if (timer.actual_timer.isRunning())
                                timer.actual_timer.stop();
                            if (!curPiece.moveDown()) {
                                timer.actual_timer.restart();
                                rest = true;
                            }
                        }
                        break;
                    // DOWN END---------------------------
                    // RIGHT------------------------------
                    case KeyEvent.VK_RIGHT:
                        System.out.println("right");
                        curPiece.moveRight();
//                        if (timer.actual_timer.isRunning())
//                            timer.actual_timer.stop();
//                        if (!curPiece.moveRight()) {
//                            timer.actual_timer.restart();
//                        }
                        break;
                    // RIGHT END--------------------------
                    // LEFT-------------------------------
                    case KeyEvent.VK_LEFT:
                        System.out.println("left");
                        curPiece.moveLeft();
                        break;
                    // LEFT END---------------------------
                    // UP---------------------------------
                    case KeyEvent.VK_UP:
                        System.out.println("up");
                        if (timer.actual_timer.isRunning())
                            timer.actual_timer.stop();
                        curPiece.rotate();
                        timer.actual_timer.restart();
                        break;
                    // UP END-----------------------------
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    System.out.println("up");
                    timer.actual_timer.restart();
                    rest = false;
                }

            }
        });
    }

    public void createGUI() {
        countTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblTime.setText("Time: " + tim++);
            }
        });
        countTimer.start();
        this.setSize(600, 700);
        getContentPane().setLayout(null);

        lblFinalScore = new JLabel("Final Score: 0000");
        lblFinalScore.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblFinalScore.setBounds(348, 513, 200, 29);
        getContentPane().add(lblFinalScore);

        lblCurScore = new JLabel("Current Score: 0000");
        lblCurScore.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblCurScore.setBounds(348, 462, 200, 29);
        getContentPane().add(lblCurScore);

        boardPanel = new JPanel();
        boardPanel.setBounds(10, 11, 300, 600);
        getContentPane().add(boardPanel);
        boardPanel.setLayout(new GridLayout(ROWS, COLS));

        lblTetris = new JLabel("Tetris!");
        lblTetris.setFont(new Font("Tahoma", Font.BOLD, 35));
        lblTetris.setBounds(374, 57, 122, 43);
        getContentPane().add(lblTetris);

        lblBy = new JLabel("by:");
        lblBy.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblBy.setBounds(420, 102, 34, 22);
        getContentPane().add(lblBy);

        lblnOmaidKhan = new JLabel("Omaid Khan & Jesus Solorzano");
        lblnOmaidKhan.setBounds(363, 135, 200, 14);
        getContentPane().add(lblnOmaidKhan);

        nxtPanel = new JPanel();
        nxtPanel.setBounds(376, 175, 120, 120);
        getContentPane().add(nxtPanel);
        nxtPanel.setLayout(new GridLayout(4, 4, 1, 1));

        lblTime = new JLabel("Time: 0");
        lblTime.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblTime.setBounds(348, 419, 200, 22);
        getContentPane().add(lblTime);

        lblLevelsCleared = new JLabel("Lines Cleared: 0");
        lblLevelsCleared.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblLevelsCleared.setBounds(348, 374, 200, 14);
        getContentPane().add(lblLevelsCleared);

        lblLevel = new JLabel("Level: 1");
        lblLevel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblLevel.setBounds(348, 325, 200, 14);
        getContentPane().add(lblLevel);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mntmRestart = new JMenuItem("Restart");
        mntmRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // start new game somehow?
                emptyIconGrid();
                timer.actual_timer.restart();
                lblTime.setText("Time: 0");
                lblFinalScore.setText("Final Score: 0");
                lblCurScore.setText("Current Score: 0");
                lblLevelsCleared.setText("Lines Cleared: 0");
                tim = 0;
                lvls = 0;
                curScore = 0;
                curPiece = randPiece();
                nextPiece = randPiece();
            }
        });
        menuBar.add(mntmRestart);

        mntmQuit = new JMenuItem("Quit");
        mntmQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(-1);
            }
        });
        menuBar.add(mntmQuit);

        mntmHelp = new JMenuItem("Help");
        mntmHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //timer.actual_timer.stop();
                JOptionPane.showMessageDialog(null, "Up Arrow: rotates piece. \n" +
                        "Down Arrow: Hold to move down. Tap to soft down\n" +
                        "Right Arrow: moves piece right\n" +
                        "Left Arrow: moves piece left\n" +
                        "Good Luck!!!!");
               // timer.actual_timer.restart();
            }

        });
        menuBar.add(mntmHelp);

        mntmNewMenuItem = new JMenuItem("About");
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Authors: Omaid Khan & Jesus Solorzano" +
                        "\nAssignment: Tetris\nCourse: CS 342 Spring 2016 - Troy");
            }
        });
        menuBar.add(mntmNewMenuItem);

        gravItem = new JMenuItem("Switch Gravity");
        gravItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!Gravity) {
                    JOptionPane.showMessageDialog(null, "Now Switching to Naive Gravity");
                    Gravity = true;
                }
                else {
                    JOptionPane.showMessageDialog(null, "Now Switching to Flood Fill Gravity");
                    Gravity = false;
                }
            }
        });
        menuBar.add(gravItem);

        //emptyIconGrid();
        pasteIconGrid();
    }

    public void checkEmptyLines() {
        int lvlsCleared = 0;
        if(timer.actual_timer.isRunning())
            timer.actual_timer.stop();

        for (int i = 0; i < 20; i++) {
            boolean missingPiece = false;
            for (int j = 0; j < 10; j++) {
                if (labelArray[i][j].getIcon().toString() == "white")
                    missingPiece = true;
            }
            if(!missingPiece)
            {
                lvlsCleared++;
                lvls++;
//                for (int j = 0; j < 10; j++) {
//                    labelArray[i][j].setIcon(emptyImage);
//                }
                for(int k = i; k > 0; k--)
                    for(int j = 0; j < 10; j++)
                    {
                        labelArray[k][j].setIcon(labelArray[k-1][j].getIcon());
                    }
            }
        }

        switch (lvlsCleared)
        {
            case 0:
                break;
            case 1:
                curScore = 40 *lvls;
                lblCurScore.setText("Current Score: " + curScore);
                break;
            case 2:
                curScore = 100 *lvls;
                lblCurScore.setText("Current Score: " + curScore);
                break;
            case 3:
                curScore = 300 *lvls;
                lblCurScore.setText("Current Score: " + curScore);
                break;
            case 4:
                curScore = 1200 *lvls;
                lblCurScore.setText("Current Score: " + curScore);
                break;
        }
        lblLevelsCleared.setText("Lines Cleared: " + lvls);
        if(lvls%10 == 0 && lvls != 0) {
            lblLevel.setText("Level:" + (lvls % 10 + 1));
            timer.faster();

        }

        repaint();

        timer.actual_timer.restart();
    }

    public void emptyIconGrid() {
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 10; j++) {
                labelArray[i][j].setIcon(emptyImage);
            }

    }

    public void pasteIconGrid() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
//                if(j == 0)
//                    labelArray[i][j] = new JLabel(iconGrid[i][j]);
//                else if(labelArray[i][j-1].getIcon().toString() == "white")
//                    labelArray[i][j] = new JLabel(new ImageIcon("blacksquare.jpg", "black"));
//                else
                labelArray[i][j] = new JLabel(emptyImage);

                boardPanel.add(labelArray[i][j]);
            }
        }
    }

    public void start() {
        timer = new myTimer(DELAY);
        curPiece = randPiece();
        nextPiece = randPiece();
        showNextPiece();
        timer.start();
    }

    public void showNextPiece() {
        nxtPanel.removeAll();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (nextPiece.pieces[i][j])
                    nxtPanel.add(new JLabel(nextPiece.image));
                else
                    nxtPanel.add(new JLabel(emptyImage));
            }
        revalidate();
        repaint();
    }

    public static void main(String[] args) {

        Tetris game = new Tetris();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
        game.start();
    }

    public Tetriminoes randPiece() {
        // this method generates a new random
        // piece and returns it after assigning it
        // to itself
        switch ((new Random().nextInt(7))) {
            case 1:
                return new iTetriminoe();
            case 2:
                return new tTetriminoe();
            case 3:
                return new oTetriminoe();
            case 4:
                return new lTetriminoe();
            case 5:
                return new jTetriminoe();
            case 6:
                return new sTetriminoe();
            case 7:
            default:
                return new zTetriminoe();
        }
    }

    private class Tetriminoes {

        protected boolean pieces[][];
        protected Icon image;
        public int row = -4;
        public int col = 5;

        public Tetriminoes() {
        }

        public void rotate90() {
            boolean temp[][] = new boolean[4][4];
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    temp[i][j] = pieces[i][j];

            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    pieces[i][j] = temp[j][3 - i];

        }

        public void rotate() {
            removePiece();
            rotate90();
            if (canPlace()) {
                placePiece();
            } else {
                rotate90();
                rotate90();
                rotate90();
            }
        }

        public void removePiece() {
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    if (pieces[i][j] && row + i >= 0)
                        labelArray[row + i][col + j].setIcon(emptyImage);
        }

        public void placePiece() {
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    if (pieces[i][j] && row + i >= 0)
                        labelArray[row + i][col + j].setIcon(this.image);
        }

        public boolean canPlace() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    int to_col = j + col;
                    int to_row = i + row;
                    if (pieces[i][j]) {
                        if (0 > to_col || to_col >= COLS // checking left and right bounds
                                || to_row >= ROWS)      // checking lower bound
                        {
                            return false;
                        }
                        if (to_row >= 0 && labelArray[to_row][to_col].getIcon().toString() != "white")
                            return false;
                    }
                }
            }
            return true;
//            for (int i = 0; i < 4; i++)
//                for (int j = 0; j < 4; j++) {
//                    if (pieces[i][j] && row + i >= 0 )
//                        if (row + i <= 16 && col + j >= 0 && col + j < 10) {
//                            if (labelArray[row + i][col + i].getIcon().toString() != "white") {
//                                return false;
//                            }
//                        } else {
//                            return false;
//                        }
//                }
//            return true;
        }

        public boolean moveDown() {
            newMove = false;
            removePiece();
            row++;  // advance row;
            if (canPlace()) {
                placePiece();
                return true;
            } else {
                row--;
                placePiece();
                return false;
            }
        }

        public boolean moveRight() {
            removePiece();
            col++;  // advance row;
            if (canPlace()) {
                placePiece();
                return true;
            } else {
                col--;
                placePiece();
                return false;
            }
        }

        public boolean moveLeft() {
            removePiece();
            col--;  // advance row;
            if (canPlace()) {
                placePiece();
                return true;
            } else {
                col++;
                placePiece();
                return false;
            }
        }

    }

    private class iTetriminoe extends Tetriminoes {
        public iTetriminoe() {
            super.pieces = new boolean[][]
                    {
                            {false, true, false, false},
                            {false, true, false, false},
                            {false, true, false, false},
                            {false, true, false, false},
                    };
            super.image = new ImageIcon("blacksquare.jpg");
        }

    }

    private class tTetriminoe extends Tetriminoes {
        public tTetriminoe() {
            super.pieces = new boolean[][]
                    {
                            {false, false, false, false},
                            {false, true, false, false},
                            {false, true, true, false},
                            {false, true, false, false},
                    };
            super.image = new ImageIcon("brownsquare.jpg");
        }
    }

    private class oTetriminoe extends Tetriminoes {
        public oTetriminoe() {
            super.pieces = new boolean[][]
                    {
                            {false, false, false, false},
                            {false, false, false, false},
                            {false, true, true, false},
                            {false, true, true, false},
                    };
            super.image = new ImageIcon("greensquare.jpg");
        }
    }

    private class lTetriminoe extends Tetriminoes {
        public lTetriminoe() {
            super.pieces = new boolean[][]
                    {
                            {false, false, false, false},
                            {false, true, false, false},
                            {false, true, false, false},
                            {false, true, true, false},
                    };
            super.image = new ImageIcon("lightbluesquare.jpg");
        }
    }

    private class jTetriminoe extends Tetriminoes {
        public jTetriminoe() {
            super.pieces = new boolean[][]
                    {
                            {false, false, false, false},
                            {false, true, true, false},
                            {false, true, false, false},
                            {false, true, false, false},
                    };
            super.image = new ImageIcon("pinksquare.jpg");
        }
    }

    private class sTetriminoe extends Tetriminoes {
        public sTetriminoe() {
            super.pieces = new boolean[][]
                    {
                            {false, false, false, false},
                            {false, true, false, false},
                            {false, true, true, false},
                            {false, false, true, false},
                    };
            super.image = new ImageIcon("purplesquare.jpg");
        }
    }

    private class zTetriminoe extends Tetriminoes {
        public zTetriminoe() {
            super.pieces = new boolean[][]
                    {
                            {false, false, false, false},
                            {false, false, true, false},
                            {false, true, true, false},
                            {false, true, false, false},
                    };
            super.image = new ImageIcon("redsquare.jpg");
        }
    }

    private void gameOver()
    {
        lblFinalScore.setText("Final Score: " + curScore);
        timer.actual_timer.stop();
        countTimer.stop();
        JOptionPane.showMessageDialog(null, "Game Over");
    }

    // inner class for timer event handling
    private class TimerHandler implements ActionListener {

        // handle button event
        public void actionPerformed(ActionEvent event) {
            if (curPiece.moveDown()) {
//                System.out.println("Success");
                newMove = false;

            } else {
                if(curPiece.row == -4)
                {
                    gameOver();
                    return;
                }
                // change the curPiece to next piece and rand next piece.
                checkEmptyLines();
                curPiece = nextPiece;
                nextPiece = randPiece();
                showNextPiece();
                newMove = true;
            }
        }

    } // end private inner class TimerHandler2

    private class myTimer extends Thread {
        private int delay;
        public Timer actual_timer;
        private boolean m_fast = false;
        private ActionListener m_cb;

        public myTimer(int delay) {
            setDelay(delay);
            actual_timer = new Timer(this.delay, new TimerHandler());
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public void faster() {
            delay = (int) (delay * .9);
            actual_timer = new Timer(this.delay, new TimerHandler());
        }

        public void run() {
            actual_timer.start();
            while (true) {
                try {
                    sleep(delay);
                } catch (Exception e) {
                }
            }
        }
    } // end class Timer
}





