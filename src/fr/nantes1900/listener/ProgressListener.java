package fr.nantes1900.listener;

import java.util.EventListener;

public interface ProgressListener extends EventListener {

    public void updateProgress(double progress);
}
