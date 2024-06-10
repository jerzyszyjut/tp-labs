namespace server;

internal class Program
{
    static void Main(string[] args)
    {
        Server server = new();
        server.Start();
    }
}
