package cycling;

import java.time.LocalDateTime;

class Stage extends mainParent {

    private Double length;
    private Integer stageID;
    private StageType type;
    private Double location;
    private Integer checkpointID;
    private LocalDateTime startTime;
    private Integer numOfCheckpoints;

    public Stage(String myName, Double myLength, Integer myStageID, String myDescription, StageType myType, Integer myRaceID, Double myLocation, Integer myCheckpointID, LocalDateTime myStartTime, Integer myNumOfCheckpoints){
        name = myName;
        length = myLength;
        stageID = myStageID;
        description = myDescription;
        type = myType;
        raceID = myRaceID;
        location = myLocation;
        checkpointID = myCheckpointID;
        startTime = myStartTime;
        numOfCheckpoints = myNumOfCheckpoints;
    }

    public String getStageName(){
        return name;
    }
    public Double getLength(){
        return length;
    }
    public Integer getStageID(){
        return stageID;
    }
    public String getDescription(){
        return description;
    }
    public StageType getType(){
        return type;
    }
    public Integer getRaceID(){
        return raceID;
    }
    public Double getLocation(){
        return location;
    }
    public Integer getCheckpointID(){
        return checkpointID;
    }
    public LocalDateTime getStartTime(){
        return startTime;
    }
    public Integer getNumOfCheckpoints(){
        return numOfCheckpoints;
    }
    public void setNumOfCheckpoints(){
        numOfCheckpoints = numOfCheckpoints + 1;
    }
}
