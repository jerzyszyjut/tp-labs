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
using client;
using model;

namespace clientgui;

/// <summary>
/// Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    Client client;
    public MainWindow()
    {
        client = new Client();
        InitializeComponent();
    }

    public void ConnectButton_Click(object sender, RoutedEventArgs e)
    {
        if(client.ConnectToServer())
        {
            Status.Text = "Connected";
            SendButton.IsEnabled = true;
            DisconnectButton.IsEnabled = true;
            ConnectButton.IsEnabled = false;
        }
    }

    public void DisconnectButton_Click(object sender, RoutedEventArgs e)
    {
        client.Disconnect();
        Status.Text = "Disonnected";
        SendButton.IsEnabled = false;
        DisconnectButton.IsEnabled = false;
        ConnectButton.IsEnabled = true;
    }

    public void SendButton_Click(Object sender, RoutedEventArgs e)
    {
        Model model = new()
        {
            Numerator = int.Parse(NumeratorInput.Text),
            Denominator = int.Parse(DenominatorInput.Text),
            Name = NameInput.Text,
        };

        Model response = client.SendModelToServer(model);

        NumeratorInput.Text = response.Numerator.ToString();
        DenominatorInput.Text = response.Denominator.ToString();
        NameInput.Text = response.Name.ToString();
        ResultOutput.Text = response.Result.ToString();
    }
}