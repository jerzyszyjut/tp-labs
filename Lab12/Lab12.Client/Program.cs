using Lab12.Model;

namespace Lab12.Client
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Client client = new();
            client.ConnectToServer();
            Model model = new()
            {
                
            }
        }
    }
}
