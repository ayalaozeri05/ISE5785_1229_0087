package renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import primitives.*;

/**
 * The BlackBoard class is responsible for generating multiple rays per pixel
 * to support anti-aliasing using various beam sampling strategies.
 */
public class BlackBoard {

    /**
     * Grid size used for beam sampling (e.g., 3 means 3x3 = 9 rays per pixel).
     */
    private int gridSize; // Default grid size for GRID and JITTERED sampling

    /**
     * Flag indicating whether anti-aliasing is enabled.
     */
    private Boolean isAntiAliasingEnabled;

    /**
     * Enumeration for the type of beam sampling.
     */
    public enum BeamType {
        GRID,
        RANDOM,
        JITTERED
    }

    /**
     * Constructs a BlackBoard with a given grid size.
     *
     * @param gridSize the number of sub-rays per axis (e.g., 3 for 3x3)
     */
    public BlackBoard(int gridSize) {
        this.gridSize = gridSize;
        this.isAntiAliasingEnabled = false;
    }

    /**
     * Sets the grid size for sampling.
     *
     * @param gridSize number of divisions in the pixel (must be > 0)
     * @return the updated BlackBoard object
     */
    public BlackBoard setGridSize(int gridSize) {
        if (gridSize < 1) {
            throw new IllegalArgumentException("Grid size must be at least 1");
        }
        this.gridSize = gridSize;
        return this;
    }

    /**
     * Gets the current grid size.
     *
     * @return the grid size
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * Enables or disables anti-aliasing.
     *
     * @param isAntiAliasingEnabled true to enable, false to disable
     * @return the updated BlackBoard object
     */
    public BlackBoard setIsAntiAliasingEnabled(Boolean isAntiAliasingEnabled) {
        this.isAntiAliasingEnabled = isAntiAliasingEnabled;
        return this;
    }

    /**
     * Checks if anti-aliasing is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public Boolean getIsAntiAliasingEnabled() {
        return isAntiAliasingEnabled;
    }

    /**
     * Creates rays with jittered grid sampling inside the pixel area.
     * Each sub-pixel is jittered randomly to avoid aliasing artifacts.
     *
     * @param camera       the camera used to create rays
     * @param pixelWidth   width of a pixel in the view plane
     * @param pixelHeight  height of a pixel in the view plane
     * @param xJ           horizontal pixel position (j)
     * @param yI           vertical pixel position (i)
     * @return list of rays to be traced from this pixel
     */
    public List<Ray> sampleJittered(Camera camera, double pixelWidth, double pixelHeight, double xJ, double yI) {
        List<Ray> rays = new ArrayList<>();
        Random rand = new Random();
        double stepX = pixelWidth / gridSize;
        double stepY = pixelHeight / gridSize;

        Point p0 = camera.getP0();
        Point center = camera.getPcenter();
        Vector vRight = camera.getVRight();
        Vector vUp = camera.getVUp();

        for (int subI = 0; subI < gridSize; subI++) {
            for (int subJ = 0; subJ < gridSize; subJ++) {
                double jitterX = rand.nextDouble();
                double jitterY = rand.nextDouble();

                double pY = yI + (subI + jitterY) * stepY;
                double pX = xJ + (subJ + jitterX) * stepX;

                Point pIJ = center;

                if (pX != 0) {
                    pIJ = pIJ.add(vRight.scale(pX));
                }
                if (pY != 0) {
                    pIJ = pIJ.add(vUp.scale(-pY));
                }

                rays.add(new Ray(p0, pIJ.subtract(p0).normalize()));
            }
        }
        return rays;
    }
}
