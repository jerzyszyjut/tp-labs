using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace lab9;

[XmlRoot(ElementName = "engine")]
public class Engine
{
    public double Displacement { get; set; }
    public double Horsepower { get; set; }
    [XmlAttribute]
    public string Model { get; set; }

    public Engine(double displacement, double horsepower, string model)
    {
        Displacement = displacement;
        Horsepower = horsepower;
        Model = model;
    }

    public Engine() { }

    public override string ToString()
    {
        return $"Engine - Displacement: {Displacement}, Horsepower: {Horsepower}, Model: {Model}";
    }
}
