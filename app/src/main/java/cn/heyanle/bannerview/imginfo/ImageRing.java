package cn.heyanle.bannerview.imginfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeYanLe on 2020/5/7 0007.
 * https://github.com/heyanLE
 */
public class ImageRing {

    private List<ImageInfo> images = new ArrayList<>();
    private int curImageIndex = 0;

    public int getImagesCount(){
        return images.size();
    }

    public void addImageInfo(ImageInfo info){
        images.add(info);
    }

    public void scrollNext(){
        if (images.isEmpty()){
            return ;
        }
        if (curImageIndex == images.size()-1){
            curImageIndex = 0;
        }else{
            curImageIndex++;
        }

    }

    public void scrollPre(){
        if (images.isEmpty()){
            return ;
        }
        if (curImageIndex == 0){
            curImageIndex = images.size()-1;
        }else{
            curImageIndex--;
        }

    }

    public ImageInfo getNextImage(){
        if (images.isEmpty()){
            return null;
        }
        if (curImageIndex == images.size()-1){
            return images.get(0);
        }
        return images.get(curImageIndex+1);
    }

    public ImageInfo getPreImage(){
        if (images.isEmpty()){
            return null;
        }
        if (curImageIndex == 0){
            return images.get(images.size()-1);
        }
        return images.get(curImageIndex-1);
    }

    public ImageInfo getCurImage(){
        return images.get(curImageIndex);
    }

    public int getCurImageIndex() {
        return curImageIndex;
    }

    public void setCurImage(int curImage) {
        this.curImageIndex = curImage;
    }

    public void recycler(){
        for(int i = 0 ; i < images.size() ; i ++){
            images.get(i).recycler();
        }
        images.clear();
    }
}
