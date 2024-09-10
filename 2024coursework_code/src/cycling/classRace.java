package cycling;

class Race extends mainParent {
    
    private Integer numberOfStages;

    public Race(Integer myRaceID, Integer myNumberOfStages, String myName, String myDescription){
        raceID = myRaceID;
        name = myName;
        description = myDescription;
        numberOfStages = myNumberOfStages;
    }

    public Integer getRaceID(){
        return raceID;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public Integer getNumberOfStages(){
        return numberOfStages;
    }
    public void setNumberOfStages(){
        numberOfStages = numberOfStages + 1;
    }
    public String getDetails(){
        String details = String.format("Race ID: ",raceID," Race name: ",name," Description of race: ", description," Number of stages: ",numberOfStages);
        return details;
    }
    
    
}
