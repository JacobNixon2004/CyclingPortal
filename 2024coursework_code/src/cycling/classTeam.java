package cycling;

class Team extends mainParent {

    private int[] riderIDs;
    
    public Team(String myName, String myDescription, Integer myTeamID, int[] myRiderIDs){
        name = myName;
        description = myDescription;
        teamID = myTeamID;
        riderIDs = myRiderIDs; 
    }

    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public Integer getTeamID(){
        return teamID;
    }
    public int[] getRiderIDs(){
        return riderIDs;
    }

}
