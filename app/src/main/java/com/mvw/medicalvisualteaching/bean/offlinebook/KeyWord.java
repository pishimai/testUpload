package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "KeyWord",
    createInDb = false
)
public class KeyWord implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    /*
    "id":663,
    "name":"骨学",
    "word":"osteology",
    "readAudio":"imed_27/audio/27092.mp3",
    "type":2,
    "link":null,
    "extend":null

     [id]: integer
        [extend]: varchar
        [link]: varchar
        [name]: varchar
        [picture]: varchar
        [word]: varchar
        [paragraph]: bigint

     */
    @Unique
    private String id;
    @Property(
        nameInDb = "name"
    )
    private String name="";
    @Property(
        nameInDb = "word"
    )
    private String word="";
    @Property(
        nameInDb = "type"
    )
    private int type;
    @Property(
        nameInDb = "link"
    )
    private String link;
    @Property(
        nameInDb = "extend"
    )
    private String extend;
    @Property(
        nameInDb = "paragraph_id"
    )
    private String paragraph_id;

    @Property(
        nameInDb = "readAudio"
    )
    private String readAudio;

    @Generated(hash = 1797683718)
    public KeyWord(String id, String name, String word, int type, String link,
            String extend, String paragraph_id, String readAudio) {
        this.id = id;
        this.name = name;
        this.word = word;
        this.type = type;
        this.link = link;
        this.extend = extend;
        this.paragraph_id = paragraph_id;
        this.readAudio = readAudio;
    }

    @Generated(hash = 617591908)
    public KeyWord() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getExtend() {
        return this.extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getParagraph_id() {
        return this.paragraph_id;
    }

    public void setParagraph_id(String paragraph_id) {
        this.paragraph_id = paragraph_id;
    }

    public String getReadAudio() {
        return this.readAudio;
    }

    public void setReadAudio(String readAudio) {
        this.readAudio = readAudio;
    }


}
