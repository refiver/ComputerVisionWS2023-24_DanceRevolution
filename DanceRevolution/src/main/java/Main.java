import org.opencv.core.Core;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            JFrame frame = new JFrame("Kamera-App mit Personenerkennung");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new CameraApp());
            frame.pack();
            frame.setVisible(true);
        });
    }
}
