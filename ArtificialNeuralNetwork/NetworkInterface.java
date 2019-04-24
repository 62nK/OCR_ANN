package ArtificialNeuralNetwork;

public interface NetworkInterface {

    // Types
    enum TYPE {input, hidden0, hidden1, hidden2, output}
    enum SQUASHING_TYPE {step, sign, sigmoid, linear}
    SQUASHING_TYPE SQUASHING_FUNCTION = SQUASHING_TYPE.sigmoid;

    // Methods
    void feedForward(double[] input);
    void resetSynapses();
    String toString();
    String outputToString();

    // Flags
    boolean DEBUG = true;
}
