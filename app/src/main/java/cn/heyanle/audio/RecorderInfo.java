package cn.heyanle.audio;

import android.media.MediaRecorder;
import android.media.MicrophoneDirection;

import java.io.IOException;

/**
 * Created by HeYanLe on 2020/5/11 0011.
 * https://github.com/heyanLE
 */
public class RecorderInfo {

    private MediaRecorder mediaRecorder;

    public void prepare(String path) throws IOException {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //音频源
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB); //输出文件格式
        mediaRecorder.setAudioSamplingRate(16000);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB); //编码格式
        mediaRecorder.setOutputFile(path); // 输出路径
        mediaRecorder.prepare();
    }

    public void start(){
        mediaRecorder.start();
    }

    public void stop(){
        mediaRecorder.stop();
    }

    public void release(){
        mediaRecorder.release();
    }

    public static RecorderInfo of(){
        RecorderInfo manager = new RecorderInfo();
        manager.mediaRecorder = new MediaRecorder();
        return manager;
    }

}
