import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

class Jumpsound {
    final MediaRecorder myjumpsound = new MediaRecorder();
    public final String path;

    public Jumpsound(String path) {
        this.path = sanitizePath(path);
    }

    private String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (!path.contains(".")) {
            path += ".3gp";
        }

        return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
    }

    public void start() throws IOException {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED))
            throw new IOException("SD Card is not mounted. It is " + state + ".");

        File directory = new File(path).getParentFile();

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }
        myjumpsound.setAudioSource(MediaRecorder.AudioSource.MIC);
        myjumpsound.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myjumpsound.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        myjumpsound.setOutputFile(path);
        myjumpsound.prepare();
        myjumpsound.start();
    }

    public void stop() throws IOException {
        myjumpsound.stop();
        myjumpsound.release();

    }

}
