package primitives;

public class Vector extends Point {


    /**
     * Constructor for Point.
     *
     * @param xyz The Double3 object representing the coordinates.
     */
    public Vector(Double3 xyz)
    {
        super(xyz);
        //were checking if it isn't vector 0
        if(xyz.equals(Double3.ZERO)) throw new IllegalArgumentException("The vector can not be zero");
    }
    public Vector(double x,double y,double z)
    {
        super(x,y,z);
        //were checking if it isn't vector 0
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("The vector can not be zero");

    }


    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
    public Vector add (Vector other)
    {
        return new Vector(this.xyz.add(other.xyz));
    }
    public Vector scale (double num)
    {
        return  new Vector(this.xyz.scale(num));
    }
    public double dotProduct(Vector other)
    {
        return (xyz.d1()* other.xyz.d1())+(xyz.d2()* other.xyz.d2())+(xyz.d3()* other.xyz.d3());
    }
    public Vector crossProduct(Vector other)
    {
        return new Vector((xyz.d2()*other.xyz.d3())-(xyz.d3()*other.xyz.d2()),(xyz.d3()*other.xyz.d1())-(xyz.d1()*other.xyz.d3()),
                (xyz.d1()*other.xyz.d2())-(xyz.d2()*other.xyz.d1()));
    }
    public double lengthSquared()
    {
        return dotProduct(this);
    }
    public double length()
    {
        return Math.sqrt(this.lengthSquared());
    }
    public Vector normalize()
    {
        return new Vector(this.xyz.reduce(this.length()));
    }


}
