package renderer;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Camera class represents a camera in the 3D space
 */
public class Camera implements Cloneable {
    private Point p0;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;

    private double width = 0.0;
    private double height = 0.0;
    private double distance = 0.0;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    public int nX=1;
    public int nY=1;

    private BlackBoard blackBoard;

    //MT
    private boolean multithreading = false;
    private int threadsCount = Runtime.getRuntime().availableProcessors();

    // ASS
    private boolean adaptiveAntiAliasing = false;
    private int maxAdaptiveDepth = 2;
    private double adaptiveThreshold = 10.0;
    private static final double MIN_PIXEL_SIZE = 0.0001;


    /**
     * Casts a single ray through a specific pixel and writes its color to the image.
     *
     * @param j pixel column index
     * @param i pixel row index
     */
    private void castRay(int j, int i) {
        int nX = this.nX;
        int nY = this.nY;
        Color color = Color.BLACK;

        if (adaptiveAntiAliasing) { //MP2
            double Ry = height / nY;
            double Rx = width / nX;
            double Yi = -(i - (nY - 1) / 2d) * Ry;
            double Xj = (j - (nX - 1) / 2d) * Rx;
            color = adaptiveAntiAliasing(nX, nY, j, i,
                    maxAdaptiveDepth, Rx, Ry, Xj, Yi);
        } else if (blackBoard.getIsAntiAliasingEnabled()) { //MP1
            List<Ray> rays = constructRays(nX, nY, j, i);
            for (Ray ray : rays) {
                color = color.add(rayTracer.traceRay(ray));
            }
            color = color.reduce(rays.size());
        } else { //REGULAR
            Ray ray = constructRay(nX, nY, j, i);
            color = rayTracer.traceRay(ray);
        }

        imageWriter.writePixel(j, i, color);
    }


    /**
     * Renders the image by casting rays through all pixels and computing their color.
     *
     * @return this camera after rendering
     */
    public Camera renderImage() {
        if (multithreading) {
            return renderImageMultiThreaded();
        }
        int ny = imageWriter.nY();
        int nx = imageWriter.nX();
        for (int i = 0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                castRay(j, i);
            }
        }
        return this;
    }

    /**
     *  Render image using multithreading
     */
    private Camera renderImageMultiThreaded() {
        final int ny = imageWriter.nY();
        final int nx = imageWriter.nX();

        Thread[] threads = new Thread[threadsCount];
        AtomicInteger nextRow = new AtomicInteger(0); // nextRow index

        for (int t = 0; t < threads.length; t++) {
            threads[t] = new Thread(() -> {
                int i;
                while ((i = nextRow.getAndIncrement()) < ny) {
                    for (int j = 0; j < nx; j++) {
                        castRay(j, i);
                    }
                }
            });
        }

        for (Thread thread : threads) thread.start();
        try {
            for (Thread thread : threads) thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Rendering interrupted", e);
        }

        return this;
    }

    /**
     * Enables or disables multithreading for rendering in the camera.
     *
     * @param enabled true to enable multithreading, false to disable it
     * @return the current Camera instance to allow method chaining
     */
    public Camera setMultithreading(boolean enabled) {
        this.multithreading = enabled;
        return this;
    }


    /**
     * Sets the number of threads to be used for rendering when multithreading is enabled.
     *
     * @param count the number of threads to use (should be a positive integer)
     * @return the current Camera instance to allow method chaining
     */
    public Camera setThreadsCount(int count) {
        this.threadsCount = count;
        return this;
    }

    /**
     * Print a grid on the image
     * @param interval the interval between the lines of the grid
     * @param color the color of the grid
     */
    public Camera printGrid(int interval, Color color) {
        for(int i = 0; i < imageWriter.nY(); i++) {
            for(int j = 0; j < imageWriter.nX(); j++) {
                if(i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Write the image to a file with the given filename (without extension)
     * @param fileName the name of the file to save the image to (without extension)
     * @return the camera object itself
     */
    public Camera writeToImage(String fileName) {
        imageWriter.writeToImage(fileName); // assuming imageWriter has a method that accepts a file name
        return this;
    }

    /**
     * Camera getter
     * @return the location of the camera
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Camera getter
     * @return the up direction of the camera
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * Camera getter
     * @return the direction of the camera
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * Camera getter
     * @return the right direction of the camera
     */
    public Vector getVRight() {
        return vRight;
    }

    /**
     * Camera getter
     * @return the width of the view plane
     */
    public double getWidth() {
        return width;
    }

    /**
     * Camera getter
     *
     * @return the height of the view plane
     */
    public double getHeight() {
        return height;
    }

    /**
     * Camera getter
     *
     * @return the distance between the camera and the view plane
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns the center point of the view plane (pixel grid),
     * located at a fixed distance from the camera position in the direction of view.
     * This point serves as the reference for generating rays toward pixels.
     *
     * @return the center point of the view plane.
     */
    public Point getPcenter() {
        return p0.add(vTo.scale(distance));
    }

    /**
     * Camera builder
     */
    public static class Builder {
        private final Camera camera = new Camera();
        private BlackBoard blackBoard;

        /**
         * Sets the ray tracer engine for the camera.
         *
         * @param scene the scene to trace rays in
         * @param type the type of ray tracer to use
         * @return the builder instance for method chaining
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            switch (type) {
                case SIMPLE:
                    camera.rayTracer = new SimpleRayTracer(scene);
                    break;
                default:
                    camera.rayTracer = null;
                    break;
            }
            return this;
        }

        /**
         * Set the location of the camera
         *
         * @param p0 the location of the camera
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Set the direction of the camera
         *
         * @param vTo the direction of the camera
         *            (the vector from the camera to the "look-at" point)
         * @param vUp the up direction of the camera
         *            (the vector from the camera to the up direction)
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!Util.isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the direction of the camera.
         *
         * @param target  the point the camera is looking at
         * @param approxUp  the approximate up direction
         */
        public Builder setDirection(Point target, Vector approxUp) {
            Vector vTo = target.subtract(camera.p0).normalize(); // Calculate forward direction
            Vector vRight = vTo.crossProduct(approxUp).normalize(); // Calculate right direction
            camera.vUp = vRight.crossProduct(vTo).normalize(); // Calculate the precise upward direction
            camera.vTo = vTo;
            return this;
        }

        /**
         * Sets the direction of the camera using a target point.
         * The upward direction is approximated using the Y-axis.
         *
         * @param target the point the camera is looking at
         * @return the builder instance for method chaining
         */
        public Builder setDirection(Point target) {
            return setDirection(target, new Vector(0, 1, 0)); // Using Y-axis as the approximate upward direction
        }

        /**
         * Set the size of the view plane
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Set the distance between the camera and the view plane
         *
         * @param distance the distance between the camera and the view plane
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("distance from camera to view must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the image.
         *
         * @param nX the number of pixels in the horizontal direction
         * @param nY the number of pixels in the vertical direction
         * @return the builder instance for method chaining
         */
        public Builder setResolution(int nX, int nY) {
            camera.nX = nX;
            camera.nY = nY;
            return this;
        }

        /**
         * Sets the BlackBoard instance used for anti-aliasing configuration.
         * The BlackBoard defines the sampling grid size and whether anti-aliasing is enabled.
         *
         * @param blackBoard the BlackBoard object containing anti-aliasing settings
         * @return this builder for method chaining
         */
        public Builder setBlackBoard(BlackBoard blackBoard) {
            this.blackBoard = blackBoard;
            return this;
        }

        /**
         * Enable Adaptive Super Sampling (ASS) for the camera.
         *
         * @param maxDepth   maximum recursion depth.
         *
         * @param threshold  color difference threshold.
         *
         * @return the Builder instance (for method chaining).
         */
        public Builder enableAdaptiveAntiAliasing(int maxDepth, double threshold) {
            camera.adaptiveAntiAliasing = true;   // activate ASS mode
            camera.maxAdaptiveDepth = maxDepth;   // set maximum recursion depth
            camera.adaptiveThreshold = threshold; // set threshold for color difference
            return this;                          // return Builder for fluent chaining
        }



        /**
         * Build the camera
         *
         * @return the camera
         */
        public Camera build() {
            final String className = "Camera";
            final String description = "values not set: ";

            if(camera.p0 == null)
                throw new MissingResourceException(description, className, "p0");
            if(camera.vUp == null)
                throw new MissingResourceException(description, className, "vUp");
            if(camera.vTo == null)
                throw new MissingResourceException(description, className, "vTo");
            if(camera.width == 0.0)
                throw new MissingResourceException(description, className, "width");
            if(camera.height == 0.0)
                throw new MissingResourceException(description, className, "height");
            if(camera.distance == 0.0)
                throw new MissingResourceException(description, className, "distance");

            if (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("nX and nY must be positive");

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            if (camera.rayTracer == null)
                camera.rayTracer = new SimpleRayTracer(null);

            camera.vRight = camera.vTo.crossProduct(camera.vUp);

            if (!Util.isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !Util.isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !Util.isZero(camera.vRight.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("vTo, vUp and vRight must be orthogonal");

            if (!Util.isZero(camera.vTo.length() - 1) ||
                    !Util.isZero(camera.vUp.length() - 1) ||
                    !Util.isZero(camera.vRight.length() - 1)) {
                throw new IllegalArgumentException("vTo, vUp, and vRight must be normalized.");
            }

            if (camera.width <= 0 || camera.height <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            if (camera.distance <= 0)
                throw new IllegalArgumentException("distance from camera to view must be positive");

            camera.blackBoard = this.blackBoard;

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    /**
     * Camera constructor
     */
    private Camera() {
    }

    /**
     * Builder getter
     *
     * @return the camera builder
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a single ray from the camera origin through the center of a pixel.
     *
     * @param nX number of pixels in X direction
     * @param nY number of pixels in Y direction
     * @param j  pixel column index
     * @param i  pixel row index
     * @return a {@link Ray} from the camera origin through the pixel center
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;
        double yI = (i - (nY - 1) / 2.0) * pixelHeight;

        Point pIJ = p0.add(vTo.scale(distance));
        if (xJ != 0) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(vUp.scale(-yI));
        }

        return new Ray(p0, pIJ.subtract(p0));
    }


//HELP
    /**
     * Constructs a ray through a specific offset inside the pixel (used for ASS).
     *
     * @param xShift horizontal offset inside the pixel
     * @param yShift vertical offset inside the pixel
     * @return ray from the camera through the given offset
     */
    private Ray constructRayThroughPoint(double xShift, double yShift) {
        Point pIJ = p0.add(vTo.scale(distance));
        if (!Util.isZero(xShift)) {
            pIJ = pIJ.add(vRight.scale(xShift));
        }
        if (!Util.isZero(yShift)) {
            pIJ = pIJ.add(vUp.scale(yShift));
        }
        return new Ray(p0, pIJ.subtract(p0).normalize());
    }



    /**
     * Constructs multiple rays through a pixel for anti-aliasing.
     *
     * @param nX number of pixels in X direction
     * @param nY number of pixels in Y direction
     * @param j  pixel column index
     * @param i  pixel row index
     * @return list of {@link Ray} through the pixel with jittered sampling
     */
    public List<Ray> constructRays(int nX, int nY, int j, int i) {
        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;
        double yI = (i - (nY - 1) / 2.0) * pixelHeight;

        return blackBoard.sampleJittered(this, pixelWidth, pixelHeight, xJ, yI);
    }



    //RECORSYA
    /**
     * Adaptive Super Sampling recursive function.
     */
    private Color adaptiveAntiAliasing(int nX, int nY, int j, int i,
                                       int depth, double pixelWidth, double pixelHeight,
                                       double centerX, double centerY) {
        if (depth == 0 || pixelWidth < MIN_PIXEL_SIZE || pixelHeight < MIN_PIXEL_SIZE) {
            Ray ray = constructRayThroughPoint(centerX, centerY);
            return rayTracer.traceRay(ray);
        }

        double halfWidth = pixelWidth / 2;
        double halfHeight = pixelHeight / 2;

        // 4 פינות
        double[][] offsets = {
                {-halfWidth, -halfHeight},
                { halfWidth, -halfHeight},
                {-halfWidth,  halfHeight},
                { halfWidth,  halfHeight}
        };

        Color[] colors = new Color[4];
        for (int k = 0; k < 4; k++) {
            double x = centerX + offsets[k][0];
            double y = centerY + offsets[k][1];
            Ray ray = constructRayThroughPoint(x, y);
            colors[k] = rayTracer.traceRay(ray);
        }

        boolean needSplit = false;
        for (int m = 0; m < 4 && !needSplit; m++) {
            for (int n = m + 1; n < 4; n++) {
                if (colors[m].difference(colors[n]) > adaptiveThreshold) {
                    needSplit = true;
                    break;
                }
            }
        }

        if (!needSplit) {
            return colors[0].add(colors[1]).add(colors[2]).add(colors[3]).scale(0.25);
        } else {
            Color totalColor = Color.BLACK;
            double[][] quarterOffsets = {
                    {-halfWidth / 2, -halfHeight / 2},
                    { halfWidth / 2, -halfHeight / 2},
                    {-halfWidth / 2,  halfHeight / 2},
                    { halfWidth / 2,  halfHeight / 2}
            };
            for (int k = 0; k < 4; k++) {
                double x = centerX + quarterOffsets[k][0];
                double y = centerY + quarterOffsets[k][1];
                totalColor = totalColor.add(
                        adaptiveAntiAliasing(nX, nY, j, i, depth - 1,
                                halfWidth, halfHeight, x, y));
            }
            return totalColor.scale(0.25);
        }
    }

}
