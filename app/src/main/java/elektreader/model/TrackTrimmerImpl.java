package elektreader.model;

import java.io.File;
import java.nio.file.FileSystems;

import elektreader.api.TrackTrimmer;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.AudioInfo;

public class TrackTrimmerImpl implements TrackTrimmer{

    private File track;

    @Override
    public void chooseTrack(){
        FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MP3, wav", "*.mp3", "*.wav"));
		track = fileChooser.showOpenDialog(null);
    }

    @Override
    public void trim(final TextField start, final TextField end, final TextField name) {
        MultimediaObject mObj = new MultimediaObject(track);
        long startTime = timeConverter(start);
        long endTime = timeConverter(end);
        try {
            if(track == null){
                throw new Exception("must select a track");
            }
            if(startTime > endTime) {
                throw new Exception("start has to be grater than end");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        File target = new File(track.getParent() + FileSystems.getDefault().getSeparator() 
            + (name.getText().isBlank() ? track.getName() + "(1)" : name.getText()) 
            + "." + getExtesion(track));
        try {
            AudioAttributes audioAttrs = new AudioAttributes();
            AudioInfo aInfo = mObj.getInfo().getAudio();
            audioAttrs.setCodec(getExtesion(track).equals("mp3") ? "libmp3lame" : "pcm_s16le");
            audioAttrs.setBitRate(aInfo.getBitRate());
            audioAttrs.setChannels(aInfo.getChannels());
            audioAttrs.setSamplingRate(aInfo.getSamplingRate());

            EncodingAttributes encodingAttrs = new EncodingAttributes();
            encodingAttrs.setOutputFormat(getExtesion(track));
            encodingAttrs.setAudioAttributes(audioAttrs);

            long duration = mObj.getInfo().getDuration()/1000;
            if(endTime > duration) {
                endTime = duration;
            }

            long cutDuration = endTime - startTime;
            encodingAttrs.setDuration((float) cutDuration);
            encodingAttrs.setOffset((float) startTime);

            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(track), target, encodingAttrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long timeConverter(final TextField timeInput) {
        long output = 0;
        String[] inputstStrings = timeInput.getText().split(":");
        int i = 1;
        for (String str : inputstStrings) {
            output += Long.parseLong(str)*Math.pow(60, inputstStrings.length - i);
            i++;
        }
        return output;
    }

    private String getExtesion(final File file) {
        var name = file.getName().split("\\.");
        return name[name.length-1];
    }

    /*public String getSourceName() {
        return track.getName();
    }*/
}
