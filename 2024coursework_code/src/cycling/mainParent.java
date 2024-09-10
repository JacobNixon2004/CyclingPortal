package cycling;

class mainParent {
    
    public String name;
    public String getName(){
        return name;
    }

    public String description;
    public String getDescription(){
        if(description != null){
            return description;
        }
        else{
            return null;
        }
    }

    public Integer raceID;
    public Integer getRaceID(){
        if(raceID != null){
            return raceID;
        }
        else{
            return null;
        }
    }

    public Integer teamID;
    public Integer getTeamID(){
        if(teamID != null){
            return teamID;
        }
        else{
            return null;
        }
    }

    public Integer riderID;
    public Integer getRiderID(){
        if(riderID != null){
            return riderID;
        }
        else{
            return null;
        }
    }
}
