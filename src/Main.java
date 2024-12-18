import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main{

    public enum INT_CONSTANTS{
        WINDOW_WIDTH(1200), WINDOW_HEIGHT(WINDOW_WIDTH.value/2), BOUNDING_POS(15), BOUNDING_SIZE(BOUNDING_POS.value*5), DARK_COLOR(167);
        public final int value;

        INT_CONSTANTS(int type) {this.value = type;}
    }

    static boolean hexState = false;
    static boolean coloredBGs = true;
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

        //INIT UI ELEMENTS ---------------------------------------------------------------------------

        final int windowWidth =INT_CONSTANTS.WINDOW_WIDTH.value;
        final int windowHeight = INT_CONSTANTS.WINDOW_HEIGHT.value;
        final int boundingPos = INT_CONSTANTS.BOUNDING_POS.value;
        final int boundingSize = INT_CONSTANTS.BOUNDING_SIZE.value;

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

        //button to set R in RGB
        JTextField RTextField = new JTextField("R Value...");
        RTextField.setBounds(windowWidth/2, (boundingPos*5)+20,150,50);
        panel.add(RTextField);

        //button to set G in RGB
        JTextField GTextField = new JTextField("G Value...");
        GTextField.setBounds(windowWidth/2, RTextField.getY()+320/6,150,50);
        panel.add(GTextField);

        //button to set B in RGB
        JTextField BTextField = new JTextField("B Value...");
        BTextField.setBounds(windowWidth/2, GTextField.getY()+320/6,150,50);
        panel.add(BTextField);

        //button to set HEx
        JTextField hexField = new JTextField("Hex Value...");
        hexField.setBounds(windowWidth/2, BTextField.getY()+320/6,150,50);
        hexField.setVisible(hexState);
        hexField.setEnabled(hexState);
        panel.add(hexField);

        //button to set rgb color
        JButton setRGBButton = new JButton("Set Colors!");
        setRGBButton.setBounds(windowWidth/2, hexField.getY()+320/6,150,50);
        panel.add(setRGBButton);

        //button to randomize color
        JButton randomButton = new JButton("Randomize");
        randomButton.setBounds(windowWidth/2, setRGBButton.getY()+320/6,150,50);
        panel.add(randomButton);

        //the big square that shows ur color
        JTextField colorPreview = new JTextField();
        colorPreview.setBounds(boundingPos, boundingPos, windowWidth/3, windowHeight-(boundingSize+boundingPos));
        colorPreview.setBackground(new Color(0,0,0));
        colorPreview.setEditable(false);
        panel.add(colorPreview);

        JLabel colorLabel = new JLabel("RGB and Hex data will show up here");
        colorLabel.setBounds((int)(colorPreview.getWidth()/3.5), windowHeight-(boundingSize+boundingPos), 500, 50);
        panel.add(colorLabel);

        JCheckBox hexTargetCheckbox = new JCheckBox("Enable/Disable Hex Input (Overrides RGB Values)");
        hexTargetCheckbox.setBounds((int)(windowWidth*0.75)-boundingPos*4, RTextField.getY()+boundingPos*4, 500, 20);
        panel.add(hexTargetCheckbox);

        JCheckBox colorBGCheckbox = new JCheckBox("Enable/Disable input field colored backgrounds");
        colorBGCheckbox.setBounds(hexTargetCheckbox.getX(), hexTargetCheckbox.getY()+boundingPos*2, 500, 20);
        colorBGCheckbox.setSelected(true);
        panel.add(colorBGCheckbox);

        //ACTION LISTENERS-----------------------------


        //runs when the randomizebutton is clicked
        randomButton.addActionListener(_ -> {
            Random rand = new Random();
            RTextField.setText(String.valueOf(rand.nextInt(256)));
            GTextField.setText(String.valueOf(rand.nextInt(256)));
            BTextField.setText(String.valueOf(rand.nextInt(256)));
            hexField.setText(String.format("#%02x%02x%02x", Integer.parseInt(RTextField.getText()), Integer.parseInt(GTextField.getText()), Integer.parseInt(BTextField.getText())).toUpperCase());

            updateAllFields(colorPreview, RTextField, GTextField, BTextField, hexField, panel, colorLabel);
        });

        //update for rfield
        RTextField.addActionListener(_ -> {updateAllFields(colorPreview, RTextField, GTextField, BTextField, hexField, panel, colorLabel);});

        //update for gfield
        GTextField.addActionListener(_ -> {updateAllFields(colorPreview, RTextField,GTextField,BTextField,hexField,panel,colorLabel);});

        //update for bfield
        BTextField.addActionListener(_ -> {updateAllFields(colorPreview, RTextField,GTextField,BTextField,hexField,panel,colorLabel);});

        //update for hexfield
        hexField.addActionListener(_ -> {
            hexChecksum(hexField);
            updateAllFields(colorPreview, RTextField, GTextField, BTextField, hexField, panel, colorLabel);
        });

        //runs when the RGB button is clicked
        setRGBButton.addActionListener(_ -> {updateAllFields(colorPreview, RTextField, GTextField, BTextField, hexField, panel, colorLabel);});

        //runs when checkbox for color bgs is clicked
        colorBGCheckbox.addActionListener(_ -> {
            coloredBGs = colorBGCheckbox.isSelected();

            if(!coloredBGs){
                oneFieldUpdate(Color.WHITE, 255, RTextField);
                oneFieldUpdate(Color.WHITE, 255, GTextField);
                oneFieldUpdate(Color.WHITE, 255, BTextField);
            }else{updateAllFields(colorPreview,RTextField,GTextField,BTextField,hexField,panel,colorLabel);}
        });

        //runs when checkbox for hex override is clicked
        hexTargetCheckbox.addActionListener(_ -> {
            hexState = hexTargetCheckbox.isSelected();
            hexField.setVisible(hexState);
            hexField.setEnabled(hexState);

            panel.repaint();
            panel.revalidate();
        });

        panel.repaint();
        panel.revalidate();
    }

    private static void hexChecksum(JTextField hexField) {
        String value = hexField.getText();
        Color hexColor;
        boolean valid = true;
        try {hexColor = Color.decode(value);}
        catch (Exception e) {
            hexColor = Color.BLACK; //placeholder idgaf
            value = "#000000";
            valid = false;
        }
        if(valid){
            final boolean darkColor = hexColor.getRed()<INT_CONSTANTS.DARK_COLOR.value&&hexColor.getGreen()<INT_CONSTANTS.DARK_COLOR.value&&hexColor.getBlue()<INT_CONSTANTS.DARK_COLOR.value;
            if(darkColor){hexField.setForeground(Color.WHITE);
            }else{hexField.setForeground(Color.BLACK);}
            hexField.setText(value);
        }
    }

    private static void updateAllFields(JTextField preview, JTextField RField, JTextField GField, JTextField BField, JTextField hexField, JPanel panel, JLabel label){
        int R;
        int G;
        int B;
        hexChecksum(hexField);

        if(!hexState) {
            R = isValidInt(RField.getText());
            G = isValidInt(GField.getText());
            B = isValidInt(BField.getText());
            hexField.setText(String.format("#%02x%02x%02x", R, G, B).toUpperCase());
        }else{
            Color color = Color.decode(hexField.getText());
            R = color.getRed();
            G = color.getGreen();
            B = color.getBlue();
        }

        preview.setBackground(new Color(R,G,B));

        oneFieldUpdate(new Color(R,0,0), R, RField);
        oneFieldUpdate(new Color(0,G,0), G, GField);
        oneFieldUpdate(new Color(0,0,B), B, BField);

        if(coloredBGs){
            BField.setForeground(Color.WHITE); //this must be called afterwards because for some reason all blue colors have terrible contrast lol
            hexField.setBackground(preview.getBackground());
        }

        label.setText("RGB: "+R+", "+G+", "+B+" // HEX: "+hexField.getText());
        panel.repaint();
        panel.revalidate();
    }

    private static void oneFieldUpdate(Color color, int value, JTextField field) {

        if (coloredBGs) {
            field.setBackground(color);
            field.setForeground(isDarkColor(color));
        }
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

        if(out==-1) {System.out.println("BRO U FUCKED UP SOMEWHERE WITH VALIDITY CHECKS");} //err handlr if out somehow never gets changed
        return out;
    }

    private static Color isDarkColor(Color color){
        int R = color.getRed();
        int G = color.getGreen();
        int B = color.getBlue();
        int darkThreshold = INT_CONSTANTS.DARK_COLOR.value;

        if(R<darkThreshold&&G<darkThreshold&&B<darkThreshold){return Color.WHITE;
        }else{return Color.BLACK;}
    }
}
