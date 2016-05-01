
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LabelTest3 extends JFrame {
    private JLabel labelArray[];
    private int lasize, lacount;
    private JButton timeButton;
    private boolean timeToggle;
    private Timer timeClock;
    private Icon iconArray[];
    private String names[] =
            {  "lightbluesquare.jpg", "blacksquare.jpg", "whitesquare.jpg" };


    public LabelTest3()
    {
        super( "Testing JLabel" );

        Container c = getContentPane();
        c.setLayout( new BorderLayout() );

        JPanel labelPanel = new JPanel ();
        int row = 4;
        int column = 8;
        labelPanel.setLayout (new GridLayout (row, column, 0, 0));
        labelPanel.setSize( row*20, column * 20 );

        c.add (labelPanel, BorderLayout.NORTH );

        // create and add icons
        iconArray = new Icon [ names.length ];

        for ( int count = 0; count < names.length; count++ )
        {
            iconArray[ count ] = new ImageIcon ( names[ count ] );
        }

        lasize = 32;
        lacount = 0;

        labelArray = new JLabel [ lasize ];
        for ( int count = 0; count < lasize; count++ )
        {
            labelArray[ count ] = new JLabel ( iconArray [ count % names.length ] );
            labelPanel.add (labelArray [ count ]);
        }

        int delay = 1000;
        timeClock = new Timer(delay, new TimerHandler () );
        // create button
        timeButton = new JButton( "Start Changing Colors" );
        c.add( timeButton, BorderLayout.CENTER);
        ButtonHandler handler = new ButtonHandler();
        timeButton.addActionListener( handler );
        timeToggle = false;


        //setSize( 275, 170 );
        pack();
        show();
    }

    public static void main( String args[] )
    {
        LabelTest3 app = new LabelTest3();

        app.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing( WindowEvent e )
                    {
                        System.exit( 0 );
                    }
                }
        );
    }

    // inner class for button event handling
    private class ButtonHandler implements ActionListener
    {

        // handle button event
        public void actionPerformed( ActionEvent event )
        {
            if (timeToggle == false)
            {
                timeClock.start();
                timeButton.setText ("Stop Changing Colors");
            }
            else
            {
                timeClock.stop();
                timeButton.setText ("Start Changing Colors");
            }
            timeToggle = !timeToggle;
        }

    } // end private inner class ButtonHandler

    // inner class for timer event handling
    private class TimerHandler implements ActionListener
    {

        // handle button event
        public void actionPerformed( ActionEvent event )
        {
            int rval = (int) Math.floor(Math.random() * names.length);

            labelArray[lacount].setIcon(iconArray[rval]);

            lacount = (lacount+1)%lasize;
        }

    } // end private inner class TimerHandler2
}
