import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Main{

    public enum INT_CONSTANTS{
        WINDOW_WIDTH(1200), WINDOW_HEIGHT(WINDOW_WIDTH.value/2), BOUNDING_POS(15), BOUNDING_SIZE(BOUNDING_POS.value*5);
        public final int value;

        INT_CONSTANTS(int type) {this.value = type;}
    }

    public static void main(String[] args) {
        //changes l&f to windows classic because im a basic bitch like that
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {System.out.println("error with look and feel!\n------DETAILS------\n"+e.getMessage());}


        //TODO: fix the spacing cause the hex is way too close to the other buttons
        //TODO: add ability to disable field color bgs
        //TODO: add ability to disable hex targeting
        //TODO: make hex button have white bg when all 3 values are dark
        //TODO: make hex's checksum less shit


        //BEGIN TO INIT UI ---------------------------------------------------------------------------

        final int windowWidth =INT_CONSTANTS.WINDOW_WIDTH.value;
        final int windowHeight = INT_CONSTANTS.WINDOW_HEIGHT.value;
        final int boundingPos = INT_CONSTANTS.BOUNDING_POS.value;
        final int boundingSize = INT_CONSTANTS.BOUNDING_SIZE.value;

        //put these back in place later
        JTextField RTextField = new JTextField("R Value...");
        JTextField GTextField = new JTextField("G Value...");
        JTextField BTextField = new JTextField("B Value...");

        //Jframe (the window)
        JFrame frame = new JFrame("Color Viewer [NACREOUS] ");
        frame.setSize(INT_CONSTANTS.WINDOW_WIDTH.value, INT_CONSTANTS.WINDOW_HEIGHT.value);
        frame.setResizable(false);
        frame.setVisible(true);

        //Jpanel (the panel that all elements are appended to)
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        panel.setLayout(null);
        frame.add(panel);

        //button to randomize color
        JButton randomButton = new JButton("Randomize");
        randomButton.setBounds(windowWidth/2, windowHeight-boundingPos*10,150,50);
        panel.add(randomButton);

        //button to randomize color
        JButton setRGBButton = new JButton("Set Colors!");
        setRGBButton.setBounds(windowWidth/2, windowHeight-boundingPos*15,150,50);
        panel.add(setRGBButton);

        //button to set R in RGB
        RTextField.setBounds(windowWidth/2, (boundingPos*5)+20,150,50);
        panel.add(RTextField);

        //button to set G in RGB
        GTextField.setBounds(windowWidth/2, (boundingPos*10)+20,150,50);
        panel.add(GTextField);

        //button to set B in RGB
        BTextField.setBounds(windowWidth/2, (boundingPos*15)+20,150,50);
        panel.add(BTextField);

        //button to set HEx
        JTextField hexField = new JTextField("Hex Value...");
        hexField.setBounds(windowWidth/2, (boundingPos*20)+20,150,50);
        panel.add(hexField);

        //the big square that shows ur color
        JTextField colorPreview = new JTextField();
        colorPreview.setBounds(boundingPos, boundingPos, windowWidth/3, windowHeight-(boundingSize+boundingPos));
        colorPreview.setBackground(new Color(0,0,0));
        colorPreview.setEditable(false);
        panel.add(colorPreview);

        JLabel colorLabel = new JLabel("RGB and Hex data will show up here");
        colorLabel.setBounds((int)(colorPreview.getWidth()/3.5), windowHeight-(boundingSize+boundingPos), 500, 50);
        panel.add(colorLabel);

        JLabel hexWarningLabel = new JLabel("<html>The Hex value has a higher priority than RGB values.<br>It'll replace the RGB values when you update the color.</html>");
        hexWarningLabel.setBounds((int)(windowWidth*0.75)-boundingPos*2, (boundingPos*5)+20, 500, 50);
        panel.add(hexWarningLabel);

        //runs when the randomizebutton is clicked
        randomButton.addActionListener(_ -> {
            colorPreview.setBackground(randomizeColor());
            updateAllFields(colorPreview, RTextField, GTextField, BTextField, hexField, panel, hexWarningLabel);
        });

        RTextField.addActionListener(_ -> {
            int value = isValidInt(RTextField.getText());
            RTextField.setText(Integer.toString(value));
            colorPreview.setBackground(new Color(value, isValidInt(GTextField.getText()), isValidInt(BTextField.getText())));

            updateAllFields(colorPreview, RTextField, GTextField, BTextField, hexField, panel, colorLabel);
        });

        GTextField.addActionListener(_ -> {
            int value = isValidInt(GTextField.getText());
            GTextField.setText(Integer.toString(value));
            colorPreview.setBackground(new Color(isValidInt(RTextField.getText()), value, isValidInt(BTextField.getText())));

            updateAllFields(colorPreview, RTextField,GTextField,BTextField,hexField,panel,colorLabel);
        });

        BTextField.addActionListener(_ -> {
            int value = isValidInt(BTextField.getText());
            BTextField.setText(Integer.toString(value));
            colorPreview.setBackground(new Color(isValidInt(RTextField.getText()), isValidInt(GTextField.getText()), value));

            updateAllFields(colorPreview, RTextField,GTextField,BTextField,hexField,panel,colorLabel);
        });

        hexField.addActionListener(_ -> {
            String value = hexField.getText();
            Color hexColor;
            try{hexColor = Color.decode(value);}
            catch(Exception e){
                hexColor = Color.BLACK; //placeholder idgaf
                value = "000000";
            }
            hexField.setText(value);
            colorPreview.setBackground(hexColor);

            updateAllFields(colorPreview, RTextField,GTextField,BTextField,hexField,panel,colorLabel);
        });

        //runs when the RGB button is clicked
        setRGBButton.addActionListener(_ -> {

            //changes rgb values to hex values if selected
            if(!Objects.equals(hexField.getText(), "0")){} //add things later
            int R = isValidInt(RTextField.getText());
            int G = isValidInt(GTextField.getText());
            int B = isValidInt(BTextField.getText());


            colorPreview.setBackground(new Color(R,G,B));

            updateAllFields(colorPreview, RTextField, GTextField, BTextField, hexField, panel, hexWarningLabel);
        });

        panel.repaint();
        panel.revalidate();
    }

    public static Color randomizeColor(){
        Random rand = new Random();
        int R = rand.nextInt(256);
        int G = rand.nextInt(256);
        int B = rand.nextInt(256);

        return new Color(R,G,B);
    }

    private static void updateAllFields(JTextField preview, JTextField RField, JTextField GField, JTextField BField, JTextField hexField, JPanel panel, JLabel label){
        int R = preview.getBackground().getRed();
        int G = preview.getBackground().getGreen();
        int B = preview.getBackground().getBlue();
        hexField.setText(String.format("#%02x%02x%02x", R, G, B).toUpperCase());


        oneFieldUpdate(new Color(R,0,0), R, RField);
        oneFieldUpdate(new Color(0,G,0), G, GField);
        oneFieldUpdate(new Color(0,0,B), B, BField);
        BField.setForeground(Color.WHITE); //this must be called afterwards because for some reason all blue colors have terrible contrast lol
        hexField.setBackground(preview.getBackground());

        label.setText("RGB: "+R+", "+G+", "+B+" // HEX: "+hexField.getText());
        panel.repaint();
        panel.revalidate();
    }

    private static void oneFieldUpdate(Color color, int value, JTextField field){

        field.setBackground(color);
        Color textColor = Color.BLACK;
        if(value<117){textColor = Color.WHITE;} //this is a function so that way the text turns white on darker backgrounds
        field.setForeground(textColor);
        field.setText(String.valueOf(value));
    }

    //used to make sure the user did not input any words or negatives into the RGB input | returns -1 if invalid
    private static int isValidInt(String input){
        int out = -1;
        boolean valid = true;
        try{Integer.parseInt(input);
        }catch (Exception e){
            out = 0;
            valid = false;
        }

        if(valid){
            out = Integer.parseInt(input);
            if(out>=256){out = 255;}
            else if(out<0){out = 0;}
        }

        if(out == -1) {System.out.println("BRO U FUCKED UP SOMEWHERE WITH VALIDITY CHECKS");} //err handlr if out somehow never gets changed
        return out;
    }
}
