package mediaCtr;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import config.PathConfig;

/**
 * Created by Feng on 2016/5/12.
 */
public class ViewMux {
    public String dataName = null;
   // public String aac = null;

    public ViewMux(String dataName){
        this.dataName =dataName;
    }

    public String mux()throws IOException {
        //获得视频
        Movie countVideo = MovieCreator.build(PathConfig.DATA_PATH+"/"+ dataName + ".mp4");
        //获得混音后的音频
        AACTrackImpl aacTrack = new AACTrackImpl(new FileDataSourceImpl(PathConfig.TEMP_AUDIO_PATH +"/"+ dataName + "_hy.aac"));
        //步骤三：新建Movie容器，并将源图像信息与新音频加入容器，实现合并源图像和新音频
        Movie movie = new Movie();
        movie.addTrack(countVideo.getTracks().get(0));
        movie.addTrack(aacTrack);
        //输出
        Container mp4file = new DefaultMp4Builder().build(movie);
        String outPath = PathConfig.DATA_PATH +"/"+ dataName + "_out.mp4";
        FileChannel fc = new FileOutputStream(new File(outPath)).getChannel();
        mp4file.writeContainer(fc);
        fc.close();
        return outPath;
    }
}
