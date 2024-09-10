package cycling;

class Checkpoint {

    private CheckpointType type;
    private Double location;
    private Double averageGradient;
    private Integer checkpointID;
    private Double length;

    public Checkpoint(CheckpointType thisType, Double thisLocation, Double thisAverageGradient, Integer thisCheckpointID, Double thisLength){
        length = thisLength;
        type = thisType;
        location = thisLocation;
        averageGradient = thisAverageGradient;
        checkpointID = thisCheckpointID;
    }
    public Checkpoint(Double thisLocation, Integer thisCheckpointID){
        location = thisLocation;
        checkpointID = thisCheckpointID;
        type = CheckpointType.SPRINT;
    }
    public Double getLocation(){
        return location;
    }
    public CheckpointType getCheckpointType(){
        return type;
    }
}
