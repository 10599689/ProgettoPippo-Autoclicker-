import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MouseJiggler {

    private static Timer timer;
    private static boolean direzione = true;

    public static void main(String[] args) throws Exception {
        final Robot robot = new Robot();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Progetto Pippo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridLayout(4, 1, 6, 6));

            Color lilla = new Color(240, 240, 253);
            frame.getContentPane().setBackground(lilla);

            java.net.URL urlIcona = MouseJiggler.class.getResource("/icon.png");
            if (urlIcona != null) {
                frame.setIconImage(new ImageIcon(urlIcona).getImage());
            }

            JLabel stato = new JLabel("Stato: SPENTO", SwingConstants.CENTER);

            JPanel pannelloIntervallo = new JPanel();
            pannelloIntervallo.setBackground(lilla);
            pannelloIntervallo.add(new JLabel("Ogni (secondi):"));
            JSpinner intervallo = new JSpinner(new SpinnerNumberModel(30, 5, 600, 5));
            pannelloIntervallo.add(intervallo);

            JCheckBox usaTasto = new JCheckBox("Simula anche il tasto F15", true);
            usaTasto.setHorizontalAlignment(SwingConstants.CENTER);
            usaTasto.setBackground(lilla);

            JToggleButton bottone = new JToggleButton("Avvia");

            bottone.addActionListener(_ -> {
                if (bottone.isSelected()) {
                    int secondi = (Integer) intervallo.getValue();
                    timer = new Timer(secondi * 1000, _ -> {
                        Point p = MouseInfo.getPointerInfo().getLocation();
                        int delta = direzione ? 1 : -1;
                        robot.mouseMove(p.x + delta, p.y);
                        direzione = !direzione;
                        if (usaTasto.isSelected()) {
                            robot.keyPress(KeyEvent.VK_F15);
                            robot.keyRelease(KeyEvent.VK_F15);
                        }
                    });
                    timer.setInitialDelay(0);
                    timer.start();
                    bottone.setText("Ferma");
                    intervallo.setEnabled(false);
                    stato.setText("Stato: ATTIVO");
                } else {
                    if (timer != null) {
                        timer.stop();
                    }
                    bottone.setText("Avvia");
                    intervallo.setEnabled(true);
                    stato.setText("Stato: SPENTO");
                }
            });

            frame.add(stato);
            frame.add(pannelloIntervallo);
            frame.add(usaTasto);
            frame.add(bottone);

            frame.setSize(300, 200);
            frame.setMinimumSize(new Dimension(350, 220));
            frame.setAlwaysOnTop(true);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}