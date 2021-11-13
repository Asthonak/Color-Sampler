/*
 * File: ColorSampler.java
 * Author: Ethan M. Park
 * Original Code 1.00 (5/5/2018) - Ethan M. Park
 * Date: Tuesday, May 8, 2018
 */

//package ColorSampler;

import java.io.*; 
import java.awt.*;
import javax.swing.*; 
import java.awt.event.*;
import javax.swing.event.*;

/*a class that contains the data for a color*/
/*It contains its name and its rgb values*/
/*It also contains a paramaterized constructor and a copy constructor*/
class Colors
{
    String name;
    int red;
    int green;
    int blue;
        
    public Colors(String s, int r, int g, int b)
    {
        name = s;
        red = r;
        green = g;
        blue = b;
    }
    
    public Colors(Colors source)
    {
        name = source.name;
        red = source.red;
        green = source.green;
        blue = source.blue;
    }
}

/*a class that contains the data to display a sample color*/
/*contains its rgb value (set to white by default)*/
/*the rgb values will change the color of the sample when repaint() is called*/
class Sample extends JComponent
{
    int r = 255;
    int g = 255;
    int b = 255;
    public void paint(Graphics gr)
    {
        Dimension d = getSize(); 
        gr.setColor(new Color(r,g,b));
        gr.drawRect(1, 1, d.width-2, d.height-2);
        gr.fillRect(1, 1, d.width-2, d.height-2);
    }
}

/*main class*/
public class ColorSampler extends JFrame
{
    /*declare variables*/
    protected Sample sam;
    protected JTextField redText;
    protected JTextField greenText;
    protected JTextField blueText;
    protected JButton redMinusButton;
    protected JButton redPlusButton;
    protected JButton greenMinusButton;
    protected JButton greenPlusButton;
    protected JButton blueMinusButton;
    protected JButton bluePlusButton;
    protected JButton saveButton;
    protected JButton resetButton;
    protected JList listColors = new JList();;
    
    protected Colors c[] = new Colors[11];
    protected Colors unsaved_c[] = new Colors[11];
    
    protected String colorsArray[] = new String[11]; 
    
    /*main function, calls paramaterized constructor of ColorSampler*/
    public static void main (String argv [])
    {
        new ColorSampler("Color Sampler");
    }
    
    /*paramaterized constructor of ColorSampler that takes in a string*/
    public ColorSampler(String title)
    {
        super(title);   // call constructor of base class
        
        int i = 0;
        
        /*read from input file to build array of Colors*/
        try
        {
            FileInputStream stream = new FileInputStream("input.txt");
            InputStreamReader reader = new InputStreamReader(stream);
            StreamTokenizer tokens = new StreamTokenizer(reader);
            String	s;
            int	n1, n2, n3;
            while (tokens.nextToken() != tokens.TT_EOF)
            {
                s = (String) tokens.sval;
                tokens.nextToken(); 
                n1 = (int) tokens.nval;
                tokens.nextToken(); 
                n2 = (int) tokens.nval;
                tokens.nextToken(); 
                n3 = (int) tokens.nval;
                c[i] = new Colors(s,n1,n2,n3);
                i++;
            }
            stream.close();
        }
        catch(IOException e)
        {
            System.err.println("Error reading from input file.");
            System.exit(0);
        }
        //make a copy of the Colors array that will be our unsaved colors
        i = 0;
        while(i < 11)
        {
            unsaved_c[i] = new Colors(c[i]);
            i++;
        }
        //make an array of color names for the selection list
        i = 0;
        while(i < 11)
        {
            colorsArray[i] = c[i].name;
            i++;
        }
        
        /*initialize variables*/
        sam = new Sample();
        JLabel redLabel = new JLabel("Red: ");
        redText = new JTextField("");
        redMinusButton = new JButton("-");
        redPlusButton = new JButton("+");
        JLabel greenLabel = new JLabel("Green: ");
        greenText = new JTextField("");
        greenMinusButton = new JButton("-");
        greenPlusButton = new JButton("+");
        JLabel blueLabel = new JLabel("Blue: ");
        blueText = new JTextField("");
        blueMinusButton = new JButton("-");
        bluePlusButton = new JButton("+");
        saveButton = new JButton("Save");
        resetButton = new JButton("Reset");
        
        /*add window listener to end program upon exiting app*/
        /*and write new colors to output file*/
        addWindowListener(new WindowDestroyer());
        
        /*add action listener to change app upon user input via buttons*/
        redMinusButton.addActionListener(new ActionHandler());
        redPlusButton.addActionListener(new ActionHandler());  
        greenMinusButton.addActionListener(new ActionHandler());  
        greenPlusButton.addActionListener(new ActionHandler());  
        blueMinusButton.addActionListener(new ActionHandler());  
        bluePlusButton.addActionListener(new ActionHandler());  
        saveButton.addActionListener(new ActionHandler());
        resetButton.addActionListener(new ActionHandler());
        
        /*add list selection listener to change app when the user*/
        /*selects a color from the list*/
        listColors.addListSelectionListener(new ListHandler());
        
        //Make text not editable
        redText.setEditable(false);
        greenText.setEditable(false);
        blueText.setEditable(false);
        
        //adding/formatting
        setBounds(100, 100, 350, 340);
        getContentPane().setLayout(null);

        getContentPane().add(sam);
        sam.setBounds(10, 10, 220, 150);
        
        getContentPane().add(redLabel);
        redLabel.setBounds(10, 170, 50, 20);
        getContentPane().add(redText);
        redText.setBounds(60, 170, 50, 25);
        getContentPane().add(redMinusButton);
        redMinusButton.setBounds(120, 170, 50, 25);
        getContentPane().add(redPlusButton);
        redPlusButton.setBounds(180, 170, 50, 25);
        
        getContentPane().add(greenLabel);
        greenLabel.setBounds(10, 200, 50, 20);
        getContentPane().add(greenText);
        greenText.setBounds(60, 200, 50, 25);
        getContentPane().add(greenMinusButton);
        greenMinusButton.setBounds(120, 200, 50, 25);
        getContentPane().add(greenPlusButton);
        greenPlusButton.setBounds(180, 200, 50, 25);
        
        getContentPane().add(blueLabel);
        blueLabel.setBounds(10, 230, 50, 20);
        getContentPane().add(blueText);
        blueText.setBounds(60, 230, 50, 25);
        getContentPane().add(blueMinusButton);
        blueMinusButton.setBounds(120, 230, 50, 25);
        getContentPane().add(bluePlusButton);
        bluePlusButton.setBounds(180, 230, 50, 25);
        
        getContentPane().add(saveButton);
        saveButton.setBounds(40,270,70,25);
        
        getContentPane().add(resetButton);
        resetButton.setBounds(120,270,70,25);

        JScrollPane colorPanel = new JScrollPane(listColors);
        getContentPane().add(colorPanel);
        listColors.setListData(colorsArray);
        colorPanel.setBounds(240,10,100,290);
                
        setVisible(true);
    }
    // define list selction listener
    private class ListHandler implements ListSelectionListener 
    {
        public void valueChanged(ListSelectionEvent e)
        {
            if ( e.getSource() == listColors)
            {
                if ( !e.getValueIsAdjusting() )
                {
                    int i = listColors.getSelectedIndex();
                    //change unsaved values back to saved
                    //and set title to have no *
                    unsaved_c[i].red = c[i].red;
                    unsaved_c[i].green = c[i].green;
                    unsaved_c[i].blue = c[i].blue;
                    setTitle("Color Sampler");
                    //set rgb values of sample to currently selected color
                    //and repaint it
                    sam.r = unsaved_c[i].red;
                    sam.g = unsaved_c[i].green;
                    sam.b = unsaved_c[i].blue;
                    getContentPane().repaint();
                    //change text to reflect values of currently selected color
                    redText.setText("" + unsaved_c[i].red);
                    greenText.setText("" + unsaved_c[i].green);
                    blueText.setText("" + unsaved_c[i].blue);
                }
            }
        }
    }
    
    // define action listener                                       
    private class ActionHandler implements ActionListener 
    {      
	public void actionPerformed(ActionEvent e) 
	{
            //if color hasn't been selected, buttons do not work
            int i = listColors.getSelectedIndex();
            if(i < 0 || i > 11)
            {
                System.out.println("Must select a color first.");
            }
            //for each +/- if value is too low or high it will not change color
            else if ( e.getSource() == redMinusButton)
            {
                if(unsaved_c[i].red <= 0)
                {
                    System.out.println("Cannot have a negative value.");
                }
                else
                {
                    //decrement red in unsaved Color class
                    //reflect this change in the sample
                    //change title to include a *
                    unsaved_c[i].red -= 5;
                    sam.r = unsaved_c[i].red;
                    getContentPane().repaint();
                    redText.setText("" + unsaved_c[i].red);
                    setTitle("Color Sampler*");
                }
            }
            else if ( e.getSource() == redPlusButton )
            {
                if(unsaved_c[i].red >= 255)
                {
                    System.out.println("RGB value cannot be over 255.");
                }
                else
                {
                    //increment red in unsaved Color class
                    //reflect this change in the sample
                    //change title to include a *
                    unsaved_c[i].red += 5;
                    sam.r = unsaved_c[i].red;
                    redText.setText("" + unsaved_c[i].red);
                    getContentPane().repaint();
                    setTitle("Color Sampler*");
                }
            }
            else if ( e.getSource() == greenMinusButton )
            {
                if(unsaved_c[i].green <= 0)
                {
                    System.out.println("Cannot have a negative value.");
                }
                else
                {
                    //decrement green in unsaved Color class
                    //reflect this change in the sample
                    //change title to include a *
                    unsaved_c[i].green -= 5;
                    sam.g = unsaved_c[i].green;
                    greenText.setText("" + unsaved_c[i].green);
                    getContentPane().repaint();
                    setTitle("Color Sampler*");
                }
            }
            else if ( e.getSource() == greenPlusButton )
            {
                if(unsaved_c[i].green >= 255)
                {
                    System.out.println("RGB value cannot be over 255.");
                }
                else
                {
                    //increment green in unsaved Color class
                    //reflect this change in the sample
                    //change title to include a *
                    unsaved_c[i].green += 5;
                    sam.g = unsaved_c[i].green;
                    greenText.setText("" + unsaved_c[i].green);
                    getContentPane().repaint();
                    setTitle("Color Sampler*");
                }
            }
            else if ( e.getSource() == blueMinusButton )
            {
                if(unsaved_c[i].blue <= 0)
                {
                    System.out.println("Cannot have a negative value.");
                }
                else
                {
                    //decrement blue in unsaved Color class
                    //reflect this change in the sample
                    //change title to include a *
                    unsaved_c[i].blue -= 5;
                    sam.b = unsaved_c[i].blue;
                    blueText.setText("" + unsaved_c[i].blue);
                    getContentPane().repaint();
                    setTitle("Color Sampler*");
                }
            }
            else if ( e.getSource() == bluePlusButton )
            {
                if(unsaved_c[i].blue >= 255)
                {
                    System.out.println("RGB value cannot be over 255.");
                }
                else
                {
                    //increment blue in unsaved Color class
                    //reflect this change in the sample
                    //change title to include a *
                    unsaved_c[i].blue += 5;
                    sam.b = unsaved_c[i].blue;
                    blueText.setText("" + unsaved_c[i].blue);
                    getContentPane().repaint();
                    setTitle("Color Sampler*");
                }
            }
            else if ( e.getSource() == saveButton )
            {
                //set current saved color to current unsaved color
                //change title to remove *
                c[i] = new Colors(unsaved_c[i]);
                setTitle("Color Sampler");
            }
            else if ( e.getSource() == resetButton )
            {
               //set current unsaved color to current saved color
               //reset text to reflect current saved color
               //reflect this change in the sample
               //change title to remove *
               unsaved_c[i] = new Colors(c[i]);
               redText.setText("" + c[i].red);
               greenText.setText("" + c[i].green);
               blueText.setText("" + c[i].blue);
               sam.r = unsaved_c[i].red;
               sam.g = unsaved_c[i].green;
               sam.b = unsaved_c[i].blue;
               getContentPane().repaint();
               setTitle("Color Sampler");
            }
        }
    }
    
    // define window adapter
    private class WindowDestroyer extends WindowAdapter 
    {
        public void windowClosing(WindowEvent e) 
        {
            //write to output file upon closing
            try
            {
                FileOutputStream ostream = new FileOutputStream("input.txt");
                PrintWriter writer = new PrintWriter(ostream);
                int j = 0;
                while(j < 11)
                {
                    writer.println(c[j].name + " " + c[j].red + " " 
                            + c[j].green + " " + c[j].blue);
                    j++;
                }
                writer.flush();
                ostream.close();
                setTitle("Color Sampler");
            }
            catch(IOException err)
            {
                System.err.println("Error writing to output file.");
                System.exit(0);
            }
            //exit program
            System.exit(0);
        }
    }
}