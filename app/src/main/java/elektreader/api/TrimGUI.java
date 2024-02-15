package elektreader.api;

import elektreader.controller.TrackTrimmerController;

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
     * Displays a message based on the value of flag.
     *
     * @param flag the flag indicating the success status
     */
    void success(final boolean flag);
}
