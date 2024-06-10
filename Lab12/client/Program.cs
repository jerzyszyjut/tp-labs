using model;

namespace client
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Client client = new();
            client.ConnectToServer();
            Model model = new()
            {
                Numerator = 1,
                Denominator = 2,
                Name = "Test",
                Result = 0,
            };
            client.SendModelToServer(model);
            client.Disconnect();
        }
    }
}
