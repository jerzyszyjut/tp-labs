using System;
using System.ComponentModel;
using System.Diagnostics;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Xml.Linq;

namespace lab10;

public partial class MainWindow : Window
{
    private readonly List<Car> Cars = new()
    {
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
    private SortableBindingList<Car> BindingCarList;

    public MainWindow()
    {
        InitializeComponent();

        // ExerciseOne();
        // ExerciseTwo();

        ComboBox.Items.Add("Model");
        ComboBox.Items.Add("Motor");
        ComboBox.Items.Add("Year");

        BindingCarList = new SortableBindingList<Car>(Cars);
        CarsDataGrid.ItemsSource = BindingCarList;
    }

    public void ExerciseOne()
    {
        var elements = from car in Cars
                   where car.Model == "A6"
                   let engineType = car.Motor.Model.Equals("TDI") ? "diesel" : "petrol"
                   let hppl = car.Motor.Horsepower / car.Motor.Displacement
                   group hppl by engineType
                   into car_groups
                   orderby car_groups.Average()
                   select new
                   {
                       engineType = car_groups.Key,
                       avgHPPL = car_groups.Average()
                   };

        foreach (var e in elements) Debug.WriteLine(e.engineType + ": " + e.avgHPPL);

        var elements2 = Cars
            .Where(car => car.Model.Equals("A6"))
            .Select(car => new
            {
                engineType = car.Motor.Model.Equals("TDI") ? "diesel" : "petrol",
                hppl = car.Motor.Horsepower / car.Motor.Displacement
            })
            .GroupBy(car => car.engineType)
            .Select(group => new
            {
                engineType = group.Key,
                avgHPPL = group.Average(c => c.hppl)
            })
            .OrderBy(group => group.avgHPPL);

        foreach (var e in elements2) Debug.WriteLine(e.engineType + ": " + e.avgHPPL);
    }

    private void ExerciseTwo()
    {
        Func<Car, Car, int> arg1 = new Func<Car, Car, int>((x,y) => x.Motor.CompareTo(y.Motor));
        Predicate <Car> arg2 = new Predicate<Car>(car => car.Motor.Model.Equals("TDI"));
        Action<Car> arg3 = new Action<Car>(car => MessageBox.Show(car.ToString()));
        Cars.Sort(new Comparison<Car>(arg1));
        Cars.FindAll(arg2).ForEach(arg3);
    }

    public void Search_Click(object sender, EventArgs e)
    {
        string searchedText = SearchTextBox.Text;
        if (ComboBox.SelectedItem == null) return;
        string searchedField = ComboBox.SelectedItem.ToString();
        List<Car> foundCars = BindingCarList.FindAll(searchedField, searchedText);
        BindingCarList = new SortableBindingList<Car>(foundCars);
        CarsDataGrid.ItemsSource = BindingCarList;
    }

    public void Reset_Click(object sender, EventArgs e)
    {
        BindingCarList = new SortableBindingList<Car>(Cars);
        CarsDataGrid.ItemsSource = BindingCarList;
    }

    public void Add_Click(object sender, EventArgs e)
    {
        string model = Model.Text;
        string engineModel = MotorModel.Text;
        double horsepower = double.Parse(Horsepower.Text);
        double displacement = double.Parse(Displacement.Text);
        int year = int.Parse(Year.Text);
        BindingCarList.Add(new Car(model, new Engine(displacement, horsepower, engineModel), year));
    }

    public void HandleKeyPress(object sender, KeyEventArgs e)
    {
        if (e.Key != Key.Delete)
        {
            return;
        }

        List<Car> temp = BindingCarList.Where(x => x != (Car)(sender as DataGrid).SelectedItem).ToList();
        BindingCarList = new SortableBindingList<Car>(temp);
    }
}