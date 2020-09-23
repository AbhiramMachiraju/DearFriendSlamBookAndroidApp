package dearfriend.abhirams.example.com.dearfriend.Model;

import java.io.Serializable;

public class SlamBookDO implements Serializable {


    Integer Id;
    private String petName;
    private String color;
    private String videoGame; //addiction
    private String cursh;
    private String flaugh;
    private String fanoys;
    private String zoadic;
    private String movie;
    private String buy;
    private String memory;
    private String sports;
    private String song;
    private UserDO userDO=new UserDO();
    private String hobby;
    private  String crazy;

    public SlamBookDO() {
    }

    public SlamBookDO(Integer id, String petName, String color, String videoGame, String cursh, String flaugh, String fanoys, String zoadic, String movie, String buy, String memory, String sports, String song, UserDO userDO, String hobby, String crazy) {
        Id = id;
        this.petName = petName;
        this.color = color;
        this.videoGame = videoGame;
        this.cursh = cursh;
        this.flaugh = flaugh;
        this.fanoys = fanoys;
        this.zoadic = zoadic;
        this.movie = movie;
        this.buy = buy;
        this.memory = memory;
        this.sports = sports;
        this.song = song;
        this.userDO = userDO;
        this.hobby = hobby;
        this.crazy = crazy;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVideoGame() {
        return videoGame;
    }

    public void setVideoGame(String videoGame) {
        this.videoGame = videoGame;
    }

    public String getCursh() {
        return cursh;
    }

    public void setCursh(String cursh) {
        this.cursh = cursh;
    }

    public String getFlaugh() {
        return flaugh;
    }

    public void setFlaugh(String flaugh) {
        this.flaugh = flaugh;
    }

    public String getFanoys() {
        return fanoys;
    }

    public void setFanoys(String fanoys) {
        this.fanoys = fanoys;
    }

    public String getZoadic() {
        return zoadic;
    }

    public void setZoadic(String zoadic) {
        this.zoadic = zoadic;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getCrazy() {
        return crazy;
    }

    public void setCrazy(String crazy) {
        this.crazy = crazy;
    }
}










