import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class CameraApp extends JPanel {
    private BufferedImage currentFrame;

    public CameraApp() {
        CascadeClassifier classifier = new CascadeClassifier("haarcascade_fullbody.xml");
        VideoCapture capture = new VideoCapture(0);

        if (!capture.isOpened()) {
            System.out.println("Kamera konnte nicht geöffnet werden.");
            System.exit(1);
        }

        Mat frame = new Mat();
        capture.read(frame);

        if (!frame.empty()) {
            setSize(frame.width(), frame.height());
        }

        Timer timer = new Timer(50, e -> {
            if (capture.read(frame)) {
                detectAndDrawPersons(frame, classifier);
                currentFrame = matToBufferedImage(frame);
                repaint();
            }
        });

        timer.start();
    }

    private void detectAndDrawPersons(Mat frame, CascadeClassifier classifier) {
        MatOfRect persons = new MatOfRect();
        classifier.detectMultiScale(frame, persons);

        for (Rect rect : persons.toArray()) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 3);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentFrame != null) {
            g.drawImage(currentFrame, 0, 0, null);
        }
    }

    private BufferedImage matToBufferedImage(Mat frame) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (frame.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = frame.channels() * frame.cols() * frame.rows();
        byte[] b = new byte[bufferSize];
        frame.get(0, 0, b);

        BufferedImage image = new BufferedImage(frame.cols(), frame.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);

        return image;
    }
}