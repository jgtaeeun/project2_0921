package edu.pnu.domain;

import java.util.List;

public class ImageDataDTO {
    private List<InputImages> inputImages;
    private List<OutputImages> outputImages;

    // Constructors
    public ImageDataDTO(List<InputImages> inputImages, List<OutputImages> outputImages) {
        this.inputImages = inputImages;
        this.outputImages = outputImages;
    }

    // Getters and Setters
    public List<InputImages> getInputImages() {
        return inputImages;
    }

    public void setInputImages(List<InputImages> inputImages) {
        this.inputImages = inputImages;
    }

    public List<OutputImages> getOutputImages() {
        return outputImages;
    }

    public void setOutputImages(List<OutputImages> outputImages) {
        this.outputImages = outputImages;
    }
}
