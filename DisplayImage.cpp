#include <opencv2/core/utility.hpp>
#include <opencv2/tracking.hpp>
#include <opencv2/videoio.hpp>
#include <opencv2/highgui.hpp>
#include <iostream>
#include <cstring>

using namespace cv;
using namespace std;

int main(int, char**){
    Rect roi;
    Mat frame;
    Ptr<Tracker> tracker = TrackerKCF::create();
    VideoCapture cap;
    int deviceID = 0; // 0 = open default camera
    int apiID = cv::CAP_ANY; // 0 = autodetect default API
    cap.open(deviceID, apiID);
    if (!cap.isOpened()) {
        cerr << "ERROR! Unable to open camera\n";
        return -1;
    }
    cout << "Start grabbing" << endl
         << "Press any key to terminate" << endl;
    cap.read(frame);
    roi = selectROI("tracker",frame);
    tracker->init(frame,roi);
    for (;;)
    {
        // wait for a new frame from camera and store it into 'frame'
        cap.read(frame);
        // check if we succeeded
        if (frame.empty())
        {
            cerr << "ERROR! blank frame grabbed\n";
            break;
        }
        // show live and wait for a key with timeout long enough to show images
        tracker->update(frame, roi);
        rectangle( frame, roi, Scalar( 255, 0, 0 ), 2, 1 );
        imshow("Live", frame);
        if (waitKey(5) == 27)
            break;
    }
    return 0;
}
