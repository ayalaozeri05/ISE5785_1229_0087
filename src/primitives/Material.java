package primitives;

/**
 * Class representing material properties for rendering.
 * Only ambient reflection coefficient (kA) is defined at this stage.
 */
public class Material {

    /**
     * Ambient reflection coefficient
     */
    public Double3 kA = Double3.ONE;

    /**
     * kD is the diffuse factor
     */
    public Double3 kD = Double3.ZERO;
    /**
     * kS is the specular factor
     */
    public Double3 kS = Double3.ZERO;
    /**
     * kT is the transparency factor
     */

    /**
     * The transmission coefficient of the material.
     * It is represented as a Double3 object.
     */
    public Double3 kT = Double3.ZERO;

    /**
     * The reflection coefficient of the material.
     * It is represented as a Double3 object.
     */
    public Double3 kR = Double3.ZERO;

    public int nsh = 0;

    /**
     * Setter for kA using a Double3 value
     * @param kA ambient reflection coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Setter for kA using a single double value
     * @param kA ambient reflection coefficient
     * @return the current Material object (for chaining)
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    /**
     * Constructor for Material
     * @param kD the diffuse factor
     * @return the material
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Material setter
     * @param kD the diffuse factor
     * @return the material
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Material setter
     * @param kS the specular factor
     * @return the material
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Material setter
     * @param kS the specular factor
     * @return the material
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Material setter
     * @param nSH the shininess factor
     * @return the material
     */
    public Material setShininess(int nSH) {
        this.nsh = nSH;
        return this;
    }

    /**
     * Sets the transmission coefficient of the material.
     *
     * @param kT The transmission coefficient.
     * @return The current Material object.
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transmission coefficient of the material.
     *
     * @param kT The transmission coefficient.
     * @return The current Material object.
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Sets the reflection coefficient of the material.
     *
     * @param kR The reflection coefficient.
     * @return The current Material object.
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection coefficient of the material.
     *
     * @param kR The reflection coefficient.
     * @return The current Material object.
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }
}
