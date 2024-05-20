using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace lab9;

[XmlType("car")]
public class Car
{
    public string Model {  get; set; }
    [XmlElement(ElementName = "engine")]
    public Engine Motor { get; set; }
    public int Year { get; set; }


    public Car(string model, Engine motor, int year)
    {
        Model = model;
        Motor = motor;
        Year = year;
    }

    public Car() { }

    override public string ToString()
    {
        return $"Car - Model: {Model}, Motor: {Motor}, Year: {Year}";
    }
}
