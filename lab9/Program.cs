using System.Xml.Linq;
using System.Xml.Serialization;
using System.Xml.XPath;

namespace lab9;

internal class Program
{
    static void Main(string[] args)
    {
        List<Car> cars = new List<Car>() {
         new Car("E250", new Engine(1.8, 204, "CGI"), 2009),
         new Car("E350", new Engine(3.5, 292, "CGI"), 2009),
         new Car("A6", new Engine(2.5, 187, "FSI"), 2012),
         new Car("A6", new Engine(2.8, 220, "FSI"), 2012),
         new Car("A6", new Engine(3.0, 295, "TFSI"), 2012),
         new Car("A6", new Engine(2.0, 175, "TDI"), 2011),
         new Car("A6", new Engine(3.0, 309, "TDI"), 2011),
         new Car("S6", new Engine(4.0, 414, "TFSI"), 2012),
         new Car("S8", new Engine(4.0, 513, "TFSI"), 2012)
        };

        // 1 A
        Console.WriteLine("1A");

        var query1a = cars
            .Where(car => car.Model == "A6")
            .Select(car => new
            {
                engineType = car.Motor.Model == "TDI" ? "diesel" : "petrol",
                hppl = (double)car.Motor.Horsepower / car.Motor.Displacement,
            });

        foreach (var car in query1a)
        {
            Console.WriteLine(car);
        }

        // 1 B
        Console.WriteLine("1B");

        var query1b = query1a
            .GroupBy(car => car.engineType)
            .OrderBy(group => group.Key);


        foreach (var group in query1b)
        {
            Console.WriteLine($"{group.Key} - average {group.Average(car => car.hppl)}");
        }

        // 2
        Console.WriteLine("2");

        string filename = "CarsCollection.xml";
        string currentDirectory = Directory.GetCurrentDirectory();
        string path = Path.Combine(currentDirectory, filename);
        StreamWriter writer = new StreamWriter(filename);

        XmlSerializer serializer = new XmlSerializer(typeof(List<Car>), new XmlRootAttribute("cars"));
        serializer.Serialize(writer, cars);
        
        writer.Close();

        StreamReader reader = new StreamReader(filename);
        List<Car>? deserializedCars = serializer.Deserialize(reader) as List<Car>;
        reader.Close();

        foreach (var car in deserializedCars)
        {
            Console.WriteLine(car);
        }

        // 3
        Console.WriteLine("3");

        XElement rootNode = XElement.Load(filename);
        double avgHP = (double)rootNode.XPathEvaluate("sum(//car/engine[@Model!=\"TDI\"]/Horsepower) div count(//car/engine[@Model!=\"TDI\"]/Horsepower)");
        Console.WriteLine($"Average other than TDI: {avgHP}");

        IEnumerable<XElement> models = rootNode.XPathSelectElements("//car/engine[@Model and not(@Model = preceding::car/engine/@Model)]");
        foreach (var model in models)
        {
            Console.WriteLine(model);
        }

        // 4
        Console.WriteLine("4");

        IEnumerable<XElement> nodes = cars
            .Select(
                car =>
                new XElement("car",
                    new XElement("Model", car.Model),
                    new XElement("engine",
                        new XElement("Displacement", car.Motor.Displacement),
                        new XElement("Horsepower", car.Motor.Horsepower),
                        new XAttribute("Model", car.Motor.Model)
                    ),
                    new XElement("Year", car.Year)
            ));
        XElement rootNode2 = new XElement("cars", nodes);
        rootNode2.Save("CarsFromLinq.xml");

        // 5
        Console.WriteLine("5");

        var rows = cars
            .Select(car => new XElement("tr",
                new XElement("td", car.Model),
                new XElement("td", car.Motor.Model),
                new XElement("td", car.Motor.Displacement),
                new XElement("td", car.Motor.Horsepower),
                new XElement("td", car.Year)
                )
            );

        var table = new XElement("table", rows);

        var template = XElement.Load("template.html");
        var body = template.Element("{http://www.w3.org/1999/xhtml}body");
        body?.Add(table);
        template.Save("result.html");

        // 6
        Console.WriteLine("6");

        var doc = XDocument.Load("CarsCollection.xml");

        foreach (var car in doc.Root!.Elements())
        {
            foreach (var field in car.Elements())
            {
                if (field.Name == "engine")
                {
                    foreach (var engineElement in field.Elements())
                    {
                        if (engineElement.Name == "Horsepower")
                        {
                            engineElement.Name = "hp";
                        }
                    }
                }
                else if (field.Name == "Model")
                {
                    var yearField = car.Element("Year");
                    var attribute = new XAttribute("Year", yearField!.Value);
                    field.Add(attribute);
                    yearField.Remove();
                }
            }
        }

        doc.Save("CarsCollectionResult.xml");
    }
}
