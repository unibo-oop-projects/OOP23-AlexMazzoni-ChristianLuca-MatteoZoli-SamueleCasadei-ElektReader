package elektreader.api;

import elektreader.controller.TrackTrimmerController;

/**
 * Inteface to describe the actions on the gui.
 */
public interface TrimGUI {

    /**
     * Sets the controller for the TrackGUI.
     *
     * @param controller the TrackTrimmerController to set
     */
    void setController(TrackTrimmerController controller);

    /**
     * Displays the name of the file previously selected.
     *
     * @param fileName the name of the file to be displayed
     */
    void showFile(String fileName);

    /**
     * Displays a message.
     *
     * @param message the message to display
     */
    void success(String message);
}
