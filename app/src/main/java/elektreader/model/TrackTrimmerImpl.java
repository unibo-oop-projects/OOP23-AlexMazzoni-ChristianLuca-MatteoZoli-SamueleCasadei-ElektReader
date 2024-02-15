package elektreader.model;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import elektreader.api.TrackTrimmer;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.AudioInfo;

/**
 * Implementation of the logic behind the interface TrackTrimmer.
 */
public class TrackTrimmerImpl implements TrackTrimmer {

    private File track;
    private static final int SECONDS_TO_MINUTES = 60;

    // CHECKSTYLE: DesignForExtension OFF
    @Override
    public void setTrack(final Path path) {
        track = new File(path.toUri());
    }

    @Override
    public String trim(final String start, final String end, final String name) {
        if (track == null) {
            return "must select a track";
        }
        if (start.isBlank() || end.isBlank()) {
            return "must enter both start and end";
        }
        final long startTime = timeConverter(start);
        long endTime = timeConverter(end);
        if (startTime > endTime) {
            return "start has to be grater than end";
        }
        final File target = new File(track.getParent() + FileSystems.getDefault().getSeparator() 
            + (name.isBlank() ? track.getName() + "(1)" : name) 
            + "." + getExtesion(track));
        try {
            final MultimediaObject mObj = new MultimediaObject(track);
            final AudioAttributes audioAttrs = new AudioAttributes();
            final AudioInfo aInfo = mObj.getInfo().getAudio();
            audioAttrs.setCodec("mp3".equals(getExtesion(track)) ? "libmp3lame" : "pcm_s16le");
            audioAttrs.setBitRate(aInfo.getBitRate());
            audioAttrs.setChannels(aInfo.getChannels());
            audioAttrs.setSamplingRate(aInfo.getSamplingRate());

            final EncodingAttributes encodingAttrs = new EncodingAttributes();
            encodingAttrs.setOutputFormat(getExtesion(track));
            encodingAttrs.setAudioAttributes(audioAttrs);

            final long duration = mObj.getInfo().getDuration() / 1000;
            if (endTime > duration) {
                endTime = duration;
            }

            final long cutDuration = endTime - startTime;
            encodingAttrs.setDuration((float) cutDuration);
            encodingAttrs.setOffset((float) startTime);

            final Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(track), target, encodingAttrs);
            return "Operation successfull";
        } catch (EncoderException | IllegalArgumentException e) {
            return "Operation failed";
        }
    }
    // CHECKSTYLE: <DesignForExtension> ON

    private long timeConverter(final String timeInput) {
        long output = 0;
        final String[] inputstStrings = timeInput.split(":");
        int i = 1;
        for (final String str : inputstStrings) {
            output += Long.parseLong(str) * Math.pow(SECONDS_TO_MINUTES, inputstStrings.length - i);
            i++;
        }
        return output;
    }

    private String getExtesion(final File file) {
        final String[] name = file.getName().split("\\.");
        return name[name.length - 1];
    }
}
