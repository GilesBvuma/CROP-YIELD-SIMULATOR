import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public interface Resource {
    double getResources(Weather w);
}

class Weather {
    private double rainfall; // in mm
    private double temperature; // in degrees Celsius

    // Constructor
    public Weather(double rainfall, double temperature) {
        this.rainfall = rainfall;
        this.temperature = temperature;
    }

    // Getters and setters
    public double getRainfall() {
        return rainfall;
    }

    public void setRainfall(double rainfall) {
        this.rainfall = rainfall;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Weather DATA [Rainfall=" + rainfall + "mm, Temperature=" + temperature + " degrees Celsius]";
    }

    public String displayInfo() {
        return "data";
    }
}

class ResourceManager extends Weather implements Resource {
    private double irrigation;
    private double soilFertility;

    public ResourceManager(double rainfall, double temperature, double irrigation, double soilFertility) {
        super(rainfall, temperature);
        this.irrigation = irrigation;
        this.soilFertility = soilFertility;
    }

    public void setIrrigation(double irrigation) {
        this.irrigation = irrigation;
    }

    public void setSoilFertility(double soilFertility) {
        this.soilFertility = soilFertility;
    }

    public double getIrrigation() {
        return irrigation;
    }

    public double getSoilFertility() {
        return soilFertility;
    }

    @Override
    public double getResources(Weather w) {
        return (w.getRainfall() + irrigation) * (soilFertility / 20);
    }

    @Override
    public String displayInfo() {
        return "Resource Data [Irrigation scale:" + irrigation + ", Soil fertility:" + soilFertility + "]";
    }
}

class Crop extends Weather {
    private String season;

    public Crop(double rainfall, double temperature, String season) {
        super(rainfall, temperature);
        this.season = season;
    }

    public double getAvgYield() {
        // Base yield based on season 
        double baseYield;
        if (season.equalsIgnoreCase("Winter")) {
            baseYield = 2.7;
        } else if (season.equalsIgnoreCase("Summer")) {
            baseYield = 3.0;
        } else if (season.equalsIgnoreCase("Perennial")) {
            baseYield = 15.2;
        } else {
            baseYield = 2.0; // Base yield
        }
        // Adjust yield based on temperature
        return adjustYieldByTemperature(baseYield);
    }

    private double adjustYieldByTemperature(double baseYield) {
        double temperature = getTemperature();
        if (temperature < 10) {
            return baseYield * 0.28; // Reduce yield for very low temperatures
        } else if (temperature > 40) {
            return baseYield * 0.67; // Reduce yield for very high temperatures
        } else {
            return baseYield; // Optimal range 
        }
    }

    public String getSeason() {
        return season;
    }

    @Override
    public String displayInfo() {
        return "Crop DATA [Season=" + season + ", Average Yield=" + getAvgYield() + " tonnes per hectare]";
    }
}

class WrongEntryException extends Exception {
    public WrongEntryException(String msg) {
        super(msg);
    }
}

class YieldSimulator {
    public static void main(String[] args) {
        JFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}

class MyFrame extends JFrame implements ActionListener {
    JButton button_1;
    JComboBox<String> box;
    String[] seasonOpts = { "SUMMER", "WINTER", "PERENNIAL" };
    JTextField t1, t2, t3, t4, t5;

    public MyFrame() {
        // Frame
        this.setSize(700, 700);
        this.setResizable(false);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel
        JPanel panel_1 = new JPanel();
        panel_1.setPreferredSize(new Dimension(550, 600));
        this.add(panel_1);
        panel_1.setLayout(new FlowLayout());

        // Text fields
        t1 = new JTextField();
        t1.setPreferredSize(new Dimension(200, 30));

        t2 = new JTextField();
        t2.setPreferredSize(new Dimension(200, 30));

        t3 = new JTextField();
        t3.setPreferredSize(new Dimension(200, 30));

        t4 = new JTextField();
        t4.setPreferredSize(new Dimension(200, 30));

        t5 = new JTextField();
        t5.setPreferredSize(new Dimension(400, 30));
        t5.setEditable(false);

        // Buttons
        button_1 = new JButton("PREDICT YIELD");
        button_1.setPreferredSize(new Dimension(400, 70));
        button_1.setFocusable(false);
        button_1.addActionListener(this);

        // Labels
        JLabel label_1 = new JLabel("AVG TEMPERATURE (degrees Celsius)");
        label_1.setPreferredSize(new Dimension(200, 20));

        JLabel label_2 = new JLabel("AVG RAINFALL (mm)");
        label_2.setPreferredSize(new Dimension(200, 20));

        JLabel label_3 = new JLabel("AVG IRRIGATION (mm)");
        label_3.setPreferredSize(new Dimension(200, 20));

        JLabel label_4 = new JLabel("AVG SOIL FERTILITY (0-10)");
        label_4.setPreferredSize(new Dimension(200, 20));

        JLabel label_5 = new JLabel("CROP SEASON");
        label_5.setPreferredSize(new Dimension(200, 20));

        JLabel label_6 = new JLabel("CROP YIELD SIMULATOR");
        label_6.setPreferredSize(new Dimension(500, 100));

        // ComboBox
        box = new JComboBox<>(seasonOpts);
        box.setPreferredSize(new Dimension(200, 30));

        // Add to panel
        panel_1.add(label_6);
        panel_1.add(label_1);
        panel_1.add(t1);
        panel_1.add(label_2);
        panel_1.add(t2);
        panel_1.add(label_3);
        panel_1.add(t3);
        panel_1.add(label_4);
        panel_1.add(t4);
        panel_1.add(label_5);
        panel_1.add(box);
        panel_1.add(button_1);
        panel_1.add(t5);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (t1.getText().isEmpty() || t2.getText().isEmpty() || t3.getText().isEmpty() || t4.getText().isEmpty()) {
                throw new WrongEntryException("Missing input");
            } else if (e.getSource() == button_1) {
                // Parse input values
                double temp = Double.parseDouble(t1.getText());
                double rainfall = Double.parseDouble(t2.getText());
                double irrigation = Double.parseDouble(t3.getText());
                double soilFertility = Double.parseDouble(t4.getText());
                String season = (String) box.getSelectedItem();

                // Create instances
                Weather weather = new Weather(rainfall, temp);
                ResourceManager resourceManager = new ResourceManager(rainfall, temp, irrigation, soilFertility);
                Crop crop = new Crop(rainfall, temp, season);

                // Calculate results
                double resources = resourceManager.getResources(weather);
                double estimatedYield = crop.getAvgYield() * resources;

                // Display Results
                t5.setText(String.format("Estimated Yield: %.2f tonnes per hectare", estimatedYield));
            }
        } catch (WrongEntryException ex) {
            t5.setText("There are some missing fields of measure. Set them to 0 if not used.");
        } catch (NumberFormatException ex) {
            t5.setText("Invalid input! Please enter valid numbers.");
        }
    }
}