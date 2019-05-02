package ArtificialNeuralNetwork;

public class InputTargetPair {

    // Parameters
    private double[] input, target;

    public InputTargetPair(double[] input, double[] target){
        this.input = input;
        this.target = target;
    }

    // Getters
    public double[] getInput() {
        return input;
    }
    public double[] getTarget() {
        return target;
    }
    public int getInputSize() {
        return input.length;
    }
    public int getTargetSize() {
        return target.length;
    }
}
