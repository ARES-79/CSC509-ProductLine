package app.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.Model.Blackboard;
import app.View.Main;

public class MainController implements ActionListener, PropertyChangeListener {
    private static final Logger controllerLog = LoggerFactory.getLogger(MainController.class.getName());
    private final Main parent;

    public MainController(Main parent) {
        this.parent = parent;

        //establish delegates
        Blackboard.getInstance().setEyeTrackingDelegate(data -> {
            controllerLog.info("Eye Tracking Data Received: " + data);
            //parent.processEyeTrackingData(data);
        });

        Blackboard.getInstance().setEmotionDataDelegate(data -> {
            controllerLog.info("Emotion Data Received: " + data);
            //parent.processEmotionData(data);
        });

        Blackboard.getInstance().setCircleDisplayDelegate(circles -> {
            controllerLog.info("Circles Updated");
            //parent.updateCircleDisplay(circles);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Start" -> {
                controllerLog.info(String.format("Connection attempted with:\n%s",
                    Blackboard.getInstance().getFormattedConnectionSettings()));
                parent.connectClients();
            }
            case "Stop" -> {
                controllerLog.info("Stop Pressed. Disconnecting.");
                parent.cleanUpThreads();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Blackboard.PROPERTY_NAME_EYETHREAD_ERROR -> {
                parent.cleanUpThreads();
                parent.createConnectionErrorPopUp("Unable to connect to Eye Tracking server. \n" +
                    "Please check that the server is running and the IP address is correct.", (String) evt.getNewValue());
            }
            case Blackboard.PROPERTY_NAME_EMOTIONTHREAD_ERROR -> {
                parent.createConnectionErrorPopUp("Unable to connect to Emotion server. \n" +
                    "Application will run without emotion data.", (String) evt.getNewValue());
            }
        }
    }
}
