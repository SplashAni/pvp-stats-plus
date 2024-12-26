package splash.dev.recording.calculations;

public interface Calculation {
    void onStart();
    default void onTick(){

    }
}
