package app.Model;

import java.util.Deque;

import app.Data.Circle;

public class Delegates {
   
    @FunctionalInterface
    public interface EyeTrackingDelegate {
        void processEyeTrackingData(String data);
    }

    @FunctionalInterface
    public interface EmotionDataDelegate {
        void processEmotionData(String data);
    }

    @FunctionalInterface
    public interface CircleDisplayDelegate {
        void updateCircleDisplay(Deque<Circle> circles);
    }
}
